package com.siga.informes;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.facturacion.Facturacion;
import com.siga.general.SIGAException;

/**
 * Realiza un informe PDF mediante FOP de la factura según plantilla introducida
 * @author RGG
 * @since 12/02/2007
 */
public class InformeFactura extends MasterReport {
	private UsrBean usrbean=null;
	
	public InformeFactura(UsrBean usuario) {
		super(usuario);
		this.usrbean = usuario;
	}

	/**
	 * Invoca varias clases para obtener los datos fijos de la factura 
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param idFactura Codigo factura
	 * @throws ClsExceptions Error interno
	 */	
	private Hashtable cargarDatosFijos(UsrBean usuario,String institucion, String idFactura) throws ClsExceptions,SIGAException {
		FacFacturaAdm facAdm = new FacFacturaAdm(usuario);
		Hashtable ht = facAdm.getDatosImpresionInformeFactura(institucion,idFactura);
		return ht;
	}
	


	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, String idFacturaDemonio) throws ClsExceptions,SIGAException{
		Hashtable htDatos= null;
				
		String plantilla=plantillaFO;
		String idFactura = null;
		UsrBean usr = null;
		
		if(request != null){
			HttpSession ses = request.getSession();
			idFactura =  (String)request.getAttribute("IDFACTURA_INFORME");
			usr = (UsrBean)ses.getAttribute("USRBEAN");
		}else{
			idFactura = idFacturaDemonio;
		}
	
		if(usr==null)
			usr = this.usrbean;
		
		String institucion =usr.getLocation();
		
		FacLineaFacturaAdm lineaFacturaAdm = new FacLineaFacturaAdm(usr);
		
		//Cargar datos fijos
		htDatos=cargarDatosFijos(usr, institucion, idFactura);
		
		//Cargar listado de letrados en cola
		Vector<Row> vLineasFactura = lineaFacturaAdm.getLineasImpresionInforme(institucion,idFactura);
		plantilla = this.reemplazaRegistros(plantilla, vLineasFactura, null, "LINEAFACTURA");
		
		
		{	// Comprobamos si tenemos que mostar el texto de "exento de iva" (si hay algun registro con iva = 0.0%)
			UtilidadesHash.set(htDatos, "EXENTO_IVA", "");
			if (vLineasFactura != null) {
				for (int i = 0; i < vLineasFactura.size(); i++) {
					Hashtable<String, Object> h = ((Row)vLineasFactura.get(i)).getRow();
					Float f = UtilidadesHash.getFloat(h, "IVA_LINEA_AUX");
					if (f != null) {
						if (f.doubleValue() == 0.0f) {
							String idioma = usr.getLanguage();
							UtilidadesHash.set(htDatos, "EXENTO_IVA", UtilidadesString.getMensajeIdioma(idioma, "messages.factura.LIVA"));
							break;
						}
					}
				}
			}
		}
		
		plantilla = this.reemplazaVariables(htDatos, plantilla);
		return plantilla;
	}
	
	/**
	 * Metodo que implementa la generacion de la factura en PDF
	 * @param request
	 * @param bFacturaBean
	 * @param bRegenerar
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private File generarPdfFacturaSinFirmar (HttpServletRequest request, FacFacturaBean bFacturaBean, boolean bRegenerar) throws ClsExceptions, SIGAException {
		try {
		    ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    AdmLenguajesAdm admLenguajes = new AdmLenguajesAdm(usrbean);
		    CenClienteAdm admCliente = new CenClienteAdm(usrbean);
		    CenColegiadoAdm admCenColegiado = new CenColegiadoAdm(usrbean);
		    FacPlantillaFacturacionAdm admFacPlantillaFacturacion = new FacPlantillaFacturacionAdm(usrbean);
		    String idFacturaParametro = "-1";
		    //Si se llama a este método desde el demonio de acciones masivas la request viene null
		    if(request != null){
			    // necesario para que lo pueda obtener la otra funcion
			 	request.setAttribute("IDFACTURA_INFORME", bFacturaBean.getIdFactura());
		    }else{
		    	//Al ir la request vacia no se puede almacenar en la request sino que tendremos que pasarla por parámetro
		    	idFacturaParametro = bFacturaBean.getIdFactura();
		    }
			
			// Obtenemos el numero de colegiado
			Hashtable<String,Object> hCenColegiado = admCenColegiado.obtenerDatosColegiado(bFacturaBean.getIdInstitucion().toString(), bFacturaBean.getIdPersona().toString(), usrbean.getLanguage());
			String nColegiado = "";
			if (hCenColegiado!=null && hCenColegiado.size()>0) {
				nColegiado =  UtilidadesHash.getString(hCenColegiado, "NCOLEGIADO_LETRADO");
			}			    
			
			// obtener ruta almacen
			String idSerieIdProgramacion = bFacturaBean.getIdSerieFacturacion().toString() + "_" + bFacturaBean.getIdProgramacion().toString();

			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") + 
								rp.returnProperty("facturacion.directorioFacturaPDFJava") +
								ClsConstants.FILE_SEP + bFacturaBean.getIdInstitucion().toString() + 
								ClsConstants.FILE_SEP + idSerieIdProgramacion;
			File rutaPDF = new File(rutaAlmacen);
			rutaPDF.mkdirs();
			if (!rutaPDF.exists()) {
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} else {
				if (!rutaPDF.canWrite()) {
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			
			//para las facturas que estan generadas pero no confirmadas por lo que no tienen numero se le pondra de nombre del idfactura y se borrara una vez descargada
			String nombrePDF="";
			if (bFacturaBean.getNumeroFactura()==null || bFacturaBean.getNumeroFactura().equals("")) {
				nombrePDF = nColegiado + "-" + bFacturaBean.getIdFactura();
				 //Si se llama a este método desde el demonio de acciones masivas la request viene null
			    if(request != null){
			    	request.setAttribute("borrarFichero", "true"); // Se borra el fichero pdf una vez descargado
			    }
				
			} else { // Contiene numero de factura, por lo tanto, esta confirmada
				nombrePDF = nColegiado + "-" + UtilidadesString.validarNombreFichero(bFacturaBean.getNumeroFactura());
			}				
			
			// Buscamos si el fichero existe
			File fPdf = new File(rutaAlmacen + ClsConstants.FILE_SEP + nombrePDF + ".pdf");
			
			if (bRegenerar || !fPdf.exists()) { // Lo regeneramos en caso de que no exista
				
				Vector<String> vFacPlantillaFacturacion = admFacPlantillaFacturacion.getPlantillaSerieFacturacion(bFacturaBean.getIdInstitucion().toString(), bFacturaBean.getIdSerieFacturacion().toString());
				String modelo="";
				if (vFacPlantillaFacturacion!=null && vFacPlantillaFacturacion.size()>0) {
					modelo = (String) vFacPlantillaFacturacion.get(0);
				}
				
				// Obtenemos el lenguaje del cliente
				String idioma = admCliente.getLenguaje(bFacturaBean.getIdInstitucion().toString(), bFacturaBean.getIdPersona().toString());
				
				// RGG 26/02/2007 cambio en los codigos de lenguajes			
				String idiomaExt = admLenguajes.getLenguajeExt(idioma);
				
				String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava") + 
										rp.returnProperty("facturacion.directorioPlantillaFacturaJava") +
										ClsConstants.FILE_SEP + bFacturaBean.getIdInstitucion().toString() + 
										ClsConstants.FILE_SEP + modelo;
				String nombrePlantilla = "factura_" + idiomaExt + ".fo";
					
				// Utilizamos la ruta de la plantilla para el temporal
				String rutaServidorTmp = rutaPlantilla + ClsConstants.FILE_SEP + "tmp_factura_" + System.currentTimeMillis();
			
				String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla, nombrePlantilla);
				
				ClsLogging.writeFileLog("ANTES DE GENERAR EL INFORME. ",10);
				fPdf = this.generarInforme(request, rutaServidorTmp, contenidoPlantilla, rutaAlmacen, nombrePDF,idFacturaParametro);
				ClsLogging.writeFileLog("DESPUES DE GENERAR EL INFORME EN  "+rutaAlmacen,10);
			}
			
			return fPdf;
			
		} catch (SIGAException se) {
			throw se;
			
		} catch (ClsExceptions ex) {
			throw ex;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al invocar generarPdfFactura: " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * Metodo que implementa la generacion de la factura en PDF firmada
	 * @param request
	 * @param bFacFactura
	 * @param bRegenerar
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public File generarPdfFacturaFirmada (HttpServletRequest request, FacFacturaBean bFacFactura, boolean bRegenerar) throws ClsExceptions, SIGAException {
		try {
			FacFacturaAdm admFacFactura = new FacFacturaAdm(usrbean);
			
			File filePDF = this.generarPdfFacturaSinFirmar(request, bFacFactura, bRegenerar);
			
			if (filePDF==null) {
				throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo");	
				
			} else {
			    ClsLogging.writeFileLog("DESPUES DE LA GENERACION DE LA FACTURA: " + filePDF.getAbsolutePath(), 10);
			    ClsLogging.writeFileLog("Existe el fichero: " + (filePDF.exists() ? "SI" : "NO"), 10);
			}
							
			// Genero una carpeta con las firmas
			String sRutaFirmas = filePDF.getParentFile().getPath() + ClsConstants.FILE_SEP + "firmas";
			File fRutaFirmas = new File(sRutaFirmas);
			fRutaFirmas.mkdirs();
			if (!fRutaFirmas.exists()) {
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} else {
				if (!fRutaFirmas.canWrite()) {
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}					
			
			// Genero una copia del pdf que se va a firmar				
			File fFicheroFirmado = new File(sRutaFirmas + ClsConstants.FILE_SEP + filePDF.getName());
			UtilidadesFicheros.copyFile(filePDF, fFicheroFirmado);		
			
			// Firmo el pdf
			admFacFactura.firmarPDF(fFicheroFirmado, bFacFactura.getIdInstitucion().toString());
							
			// Hay que borrar el pdf sin firma si no tiene numero de factura
			if (bFacFactura.getNumeroFactura()==null || bFacFactura.getNumeroFactura().equals("")) {
				filePDF.delete();
			}
			
	        return fFicheroFirmado;
		
		} catch (SIGAException se) {
			throw se;
			
		} catch (ClsExceptions ex) {
			throw ex;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al invocar generarPdfFacturaFirmada: " + e.getLocalizedMessage());
		}
	}	
	
	/**
	 * Metodo que implementa la generacion de la factura en PDF firmada
	 * @param request
	 * @param hFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public File generarPdfFacturaFirmada(HttpServletRequest request, Hashtable<String, Object> hFactura) throws ClsExceptions, SIGAException {
		try {
			FacFacturaAdm admFacFactura = new FacFacturaAdm(usrbean);
			FacFacturaBean bFacFactura = (FacFacturaBean) admFacFactura.selectByPK(hFactura).firstElement();

			// Generamos el fichero pdf de la factura
			File ficheroPDF = this.generarPdfFacturaFirmada(request, bFacFactura, false);
			
			return ficheroPDF;

		} catch (SIGAException se) {
			throw se;
			
		} catch (ClsExceptions ex) {
			throw ex;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al invocar generarPdfFacturaSinFirma: " + e.getLocalizedMessage());
		}
	}	
	
	/**
	 * Genera un zip con las facturas de los productos o certificados
	 * @param request
	 * @param idInstitucion
	 * @param idPeticion
	 * @param vFacturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private File generarZipFacturacionRapida (HttpServletRequest request, String idInstitucion, String idPeticion, Vector<Hashtable<String,Object>> vFacturas) throws ClsExceptions,SIGAException {
		File ficheroZip = null;
			
		try {
		    ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    
		    // Obtenemos las rutas del zip
		    String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava") + rp.returnProperty("facturacion.directorioFacturaPDFJava") + File.separator + idInstitucion;
		    String rutaFicheroZip = rutaAlmacen + File.separator + idPeticion + ".zip";
		    ficheroZip = new File(rutaFicheroZip);
		
			// Generamos el zip    			
			ClsLogging.writeFileLog("ANTES DE GENERAR EL INFORME ZIP. ",10);
			
			// Comprueba que existe la ruta donde guardar el fichero zip
			File carpetaAlmacen = new File(rutaAlmacen);
			if(!carpetaAlmacen.exists()){
				carpetaAlmacen.mkdirs();
				
				if(!carpetaAlmacen.exists()){
					throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
				} else {
					if(!carpetaAlmacen.canWrite()){
						throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
					}
				}					
			}				
			
			// Se crea un array con los pdf del zip
			ArrayList<File> listaFicherosPDF = new ArrayList<File>();
			ArrayList<Hashtable<String,Object>> arrayDatosFacturas = new ArrayList<Hashtable<String,Object>>();
			
			// Recorre las facturas asociadas a una peticion
			for (int i = 0; i < vFacturas.size(); i++) {
				/** Generamos cada PDF de la facturacion para ir añadiendolo al ZIP **/
				Hashtable<String, Object> hFactura = (Hashtable<String, Object>) vFacturas.get(i);
				File ficheroPdf = this.generarPdfFacturaFirmada(request, hFactura);

				// Comprobamos que exista el fichero pdf de la factura
				if (ficheroPdf == null) {
					throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");
				} else if (!ficheroPdf.exists()) {
					throw new SIGAException("messages.general.error.ficheroNoExisteReintentar");
				}

				// Si llega a este punto, o bien existia el fichero previamente, o bien lo hemos generado => Lo incluimos en el array con los pdf del zip
				listaFicherosPDF.add(ficheroPdf);				

				// Genera un array con los ficheros y ruta de su carpeta
				Hashtable<String, Object> hDatosFactura = new Hashtable<String, Object>();
				hDatosFactura.put("Fichero", ficheroPdf);
				hDatosFactura.put("RutaCarpeta", ficheroPdf.getParentFile().getPath());
				arrayDatosFacturas.add(hDatosFactura);
			}

			// Generamos el fichero zip con todas las facturas asociadas a la solicitud
			Facturacion facturacion = new Facturacion(usrbean);
			//Obtenemos el bean del envio: 
			CenPersonaAdm admPersona = new CenPersonaAdm(usrbean);
			
			facturacion.doZipGeneracionRapida(rutaAlmacen + File.separator, idPeticion, listaFicherosPDF,
					UtilidadesString.eliminarAcentosYCaracteresEspeciales(admPersona.obtenerNombreApellidos((String)vFacturas.get(0).get("IDPERSONA"))));
			
			// Eliminacion de los pdfs firmados y su carpeta
			File directorio = null;
			for (int i=0; i<listaFicherosPDF.size(); i++) {
				File ficheroPdfFirmado = listaFicherosPDF.get(i);
				directorio = ficheroPdfFirmado.getParentFile();
				ficheroPdfFirmado.delete(); // Elimina los pdfs firmados
			}
			if (directorio!=null && directorio.isDirectory() && directorio.list().length==0) {
				directorio.delete(); // borra el directorio de las firmas
			}
    		
		} catch (SIGAException se) {
			throw se;
			
		} catch (ClsExceptions ex) {
			throw ex;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el ZIP de una facturacion rapida: " + e.getLocalizedMessage());
		} 
		
        return ficheroZip;
	}

	/**
	 * @param request
	 * @param idInstitucion
	 * @param idPeticion
	 * @param vFacturas
	 * @return
	 */
	public File generarInformeFacturacionRapida(HttpServletRequest request, String idInstitucion, String idPeticion, Vector<Hashtable<String, Object>> vFacturas) throws ClsExceptions, SIGAException {
		File fichero = null;
		/** Si se trata solo de una factura, se devuelve directamente el fichero PDF **/
		if (vFacturas != null && vFacturas.size() == 1) {
			fichero = this.generarPdfFacturaFirmada(request, vFacturas.get(0));
		} else {
			/** Si generan mas de una factura, se genera un zip con todas las facturas (PDF) **/
			fichero = this.generarZipFacturacionRapida(request, idInstitucion, idPeticion, vFacturas);
		}

		return fichero;
	}		
}