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
	protected Hashtable cargarDatosFijos(UsrBean usuario,String institucion, String idFactura) throws ClsExceptions,SIGAException {
		FacFacturaAdm facAdm = new FacFacturaAdm(usuario);
		Hashtable ht = facAdm.getDatosImpresionInformeFactura(institucion,idFactura);
		return ht;
	}
	


	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions,SIGAException{
		Hashtable htDatos= null;
				
		String plantilla=plantillaFO;
		
		HttpSession ses = request.getSession();
		String idFactura =  (String)request.getAttribute("IDFACTURA_INFORME");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
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
	 * Metodo que implementa la generación de la factura en PDF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @param  idFactura - 
	 * @return  File - fichero PDF generado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public File generarFactura (HttpServletRequest request, String idioma, String institucion, String idFactura, String nColegiado) throws ClsExceptions,SIGAException {
		File fPdf = null;
			
		try {
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usrbean);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// necesario para que lo pueda obtener la otra funcion
			request.setAttribute("IDFACTURA_INFORME",idFactura);
			
			//obtener plantilla
			FacFacturaAdm facAdm= new FacFacturaAdm(usrbean);
			Vector<Row> v = facAdm.getFactura(institucion,idFactura);
			Hashtable<String, Object> ht =null;
			String modelo ="";
			if (v!=null && v.size()>0) {
				ht = ((Row) v.get(0)).getRow(); 
				FacPlantillaFacturacionAdm plAdm= new FacPlantillaFacturacionAdm(usrbean);
				Vector<String> vv = plAdm.getPlantillaSerieFacturacion(institucion,(String)ht.get(FacFacturaBean.C_IDSERIEFACTURACION));
				if (vv!=null && vv.size()>0) {
					modelo=(String)vv.get(0);
				}
			}
			String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+rp.returnProperty("facturacion.directorioPlantillaFacturaJava");
			rutaPlantilla += ClsConstants.FILE_SEP+institucion.toString()+ClsConstants.FILE_SEP+modelo;
			String nombrePlantilla="factura"+"_"+idiomaExt+".fo";
			
			// obtener ruta almacen
			String idserieidprogramacion = ht.get(FacFacturaBean.C_IDSERIEFACTURACION).toString()+"_" + ht.get(FacFacturaBean.C_IDPROGRAMACION).toString();

			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
    		rutaAlmacen += ClsConstants.FILE_SEP+institucion.toString()+ClsConstants.FILE_SEP+idserieidprogramacion;
			File rutaPDF=new File(rutaAlmacen);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			rutaAlmacen+=ClsConstants.FILE_SEP;
			String nombrePDF=nColegiado + "-"+(String) ht.get(FacFacturaBean.C_NUMEROFACTURA);
			
			//para las facturas que estan generadas pero no confirmadas por lo que no tienen número
			//se le pondra de nombre del idfactura y se borrará una vez descargada
			if(UtilidadesHash.getString(ht,FacFacturaBean.C_NUMEROFACTURA)==null ||UtilidadesHash.getString(ht,FacFacturaBean.C_NUMEROFACTURA).equals("")){
				nombrePDF = nColegiado + "-"+(String) ht.get(FacFacturaBean.C_IDFACTURA);
				request.setAttribute("borrarFichero", "true");
			}
			
			// utilizamos la ruta de la plantilla para el temporal
			String rutaServidorTmp=rutaPlantilla+ClsConstants.FILE_SEP+"tmp_factura_"+System.currentTimeMillis();
			
			String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			ClsLogging.writeFileLog("ANTES DE GENERAR EL INFORME. ",10);
			fPdf = this.generarInforme(request,rutaServidorTmp,contenidoPlantilla,rutaAlmacen,nombrePDF);
			ClsLogging.writeFileLog("DESPUES DE GENERAR EL INFORME EN  "+rutaAlmacen,10);
			
		} catch (SIGAException se) {
			throw se;
			
		} catch (ClsExceptions ex) {
			throw ex;
			
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error a l generar el informe: "+e.getLocalizedMessage());
		}
		
        return fPdf;
	}
	
	public File generarFacturaFirmada (HttpServletRequest request, String idioma, String institucion, String idFactura, String nColegiado) throws ClsExceptions,SIGAException {
		File filePDF =  generarFactura(request, idioma, institucion, idFactura, nColegiado);
		
		if (filePDF==null) {
			throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");				
		} else {
		    ClsLogging.writeFileLog("DESPUES DE LA GENERACION DE LA FACTURA: "+filePDF.getAbsolutePath(),10);
		    ClsLogging.writeFileLog("Existe el fichero: "+((filePDF.exists())?"SI":"NO"),10);
		}
//
		FacFacturaAdm facfactura=new FacFacturaAdm(usrbean);
		facfactura.firmarPDF(filePDF,institucion);
		
        return filePDF;
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
			fichero = generarPDFFacturacionRapida(request, idInstitucion, (Hashtable<String, Object>) vFacturas.get(0));
		} else {
			/** Si generan mas de una factura, se genera un zip con todas las facturas (PDF) **/
			fichero = generarZipFacturacionRapida(request, idInstitucion, idPeticion, vFacturas);
		}

		return fichero;
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
	public File generarZipFacturacionRapida (HttpServletRequest request, String idInstitucion, String idPeticion, Vector<Hashtable<String,Object>> vFacturas) throws ClsExceptions,SIGAException {
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
				File ficheroPdf = generarPDFFacturacionRapida(request, idInstitucion, hFactura);

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
				String idSerieFacturacion = UtilidadesHash.getString(hFactura, FacFacturaBean.C_IDSERIEFACTURACION);
				String idProgramacion = UtilidadesHash.getString(hFactura, FacFacturaBean.C_IDPROGRAMACION);
				hDatosFactura.put("Fichero", ficheroPdf);
				hDatosFactura.put("RutaCarpeta", rutaAlmacen + File.separator + idSerieFacturacion + "_" + idProgramacion);
				arrayDatosFacturas.add(hDatosFactura);
			}

			// Generamos el fichero zip con todas las facturas asociadas a la solicitud
			Facturacion facturacion = new Facturacion(usrbean);
			//Obtenemos el bean del envio: 
			CenPersonaAdm admPersona = new CenPersonaAdm(usrbean);
			
			facturacion.doZipGeneracionRapida(rutaAlmacen + File.separator, idPeticion, listaFicherosPDF,
					UtilidadesString.eliminarAcentosYCaracteresEspeciales(admPersona.obtenerNombreApellidos((String)vFacturas.get(0).get("IDPERSONA"))));
			
			// Recorre los ficheros generados para eliminarlos 
			for (int i=0; i<arrayDatosFacturas.size(); i++) {
				Hashtable<String,Object> hDatosFactura = (Hashtable<String,Object>) arrayDatosFacturas.get(i);
				File ficheroPdf = (File) hDatosFactura.get("Fichero");
				ficheroPdf.delete();
				String rutaCarpetaFciheroPdf = (String) hDatosFactura.get("RutaCarpeta");
				File carpetaFicheroPdf = new File(rutaCarpetaFciheroPdf);
				carpetaFicheroPdf.delete();
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
	public File generarPDFFacturacionRapida(HttpServletRequest request, String idInstitucion, Hashtable<String, Object> hFactura) throws ClsExceptions, SIGAException {
		File ficheroPDF = null;
		try {
			// Obtiene los datos de la factura
			String idPersona = UtilidadesHash.getString(hFactura, FacFacturaBean.C_IDPERSONA);
			String idFactura = UtilidadesHash.getString(hFactura, FacFacturaBean.C_IDFACTURA);

			// Obtenemos el numero de colegiado
			CenColegiadoAdm admColegiado = new CenColegiadoAdm(usrbean);
			Hashtable<String, Object> hColegiado = admColegiado.obtenerDatosColegiado(usrbean.getLocation(), idPersona, usrbean.getLanguage());
			String nColegiado = "";
			if (hColegiado != null && hColegiado.size() > 0) {
				nColegiado = UtilidadesHash.getString(hColegiado, "NCOLEGIADO_LETRADO");
			}

			// Obtenemos el lenguaje del cliente
			CenClienteAdm admCliente = new CenClienteAdm(usrbean);
			String lenguaje = admCliente.getLenguaje(idInstitucion, idPersona);

			// Generamos el fichero pdf de la factura
			InformeFactura inf = new InformeFactura(usrbean);
			ficheroPDF = inf.generarFactura(request, lenguaje.toUpperCase(), usrbean.getLocation(), idFactura, nColegiado);

		} catch (SIGAException se) {
			throw se;

		} catch (ClsExceptions ex) {
			throw ex;

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el PDF de una facturacion rapida: " + e.getLocalizedMessage());
		}

		return ficheroPDF;
	}
	
}