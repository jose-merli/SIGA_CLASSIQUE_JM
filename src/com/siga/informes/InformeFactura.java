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
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacLineaFacturaAdm;
import com.siga.beans.FacPlantillaFacturacionAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;
import com.siga.facturacion.Facturacion;
import com.siga.general.SIGAException;


/**
 * Realiza un informe PDF mediante FOP de la factura según plantilla introducida
 * @author RGG
 * @since 12/02/2007
 */
public class InformeFactura extends MasterReport {
	private UsrBean usrbean=null;
	/*public InformeFactura() {
		super();
	}*/
	
	public InformeFactura(UsrBean usuario) {
		super(usuario);
		this.usrbean = usuario;
	}
/*
	//Genera el PDF. Devuelve true si lo ha generado correctamente.
	public boolean generarInforme(Hashtable htrequest, String nombreFicheroFO) throws ClsExceptions {
		String institucion =(String)htrequest.get("REQ_IDINSTITUCION");
		String idioma =(String)htrequest.get("REQ_IDIOMA");
		String idiomaExt="";

		// RGG 26/02/2007 cambio en los codigos de lenguajes
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		idiomaExt = "es";
		try {
			idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
		} catch (Exception e) {
			
		}	
		
		File plantillaFO=null;
		File ficheroFOP=null;		
		File ficheroPDF=null;
		File rutaTmp=null;
		File rutaPDF=null;
		
		String  rutaServidorPlantillas=null, rutaServidorTmp=null, rutaServidorDescargas=null, rutaFicheroFO=null, rutaFicheroFOP=null;
		String nombreFicheroGenerado=null;		
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);//Solo lectura
		
		try {
			
			//
			// OBTENCION DE LA PLANTILLA FO:
			//
			
			// Ubicacion de la carpeta donde se crean los ficheros FO:
			rutaServidorPlantillas = admParametros.getValor(institucion, "INF", "PATH_INFORMES_PLANTILLA", "");
			rutaServidorPlantillas += ClsConstants.FILE_SEP + institucion;
			
			// Construyo la ruta para la plantilla FO: 	 		
			rutaFicheroFO = rutaServidorPlantillas+ClsConstants.FILE_SEP+nombreFicheroFO+"_"+idiomaExt+".fo";
			ClsLogging.writeFileLog("*********** rutaFicheroFO: "+rutaFicheroFO,7);			
			//Fichero con la plantilla FO:
			plantillaFO = new File(rutaFicheroFO);	 
			
			//
			// NOMBRE DEL FICHERO SIN EXTENSION: nombre del fichero generado a partir del nombre de la plantilla concatenado con la hora en milisegundos:
			//
			nombreFicheroGenerado = nombreFicheroFO+System.currentTimeMillis();
			
			//
			// GENERACION DEL FICHERO FOP DEL USUARIO:
			//	 					
			// Ubicacion de la carpeta donde se crean los ficheros temporales FO:
			rutaServidorTmp = rutaServidorPlantillas+ClsConstants.FILE_SEP+"tmp"+ClsConstants.FILE_SEP;
			
			//Construyo la ruta para la plantilla FO: 	 		
			rutaFicheroFOP = rutaServidorTmp+nombreFicheroGenerado+".fo";
			
			//Crea el fichero. Si no existe el directorio de la institucion para el fichero temporal lo crea.	 		
			rutaTmp = new File(rutaServidorTmp);
			rutaTmp.mkdirs();
			ficheroFOP = new File(rutaFicheroFOP);
			
			// Generacion del fichero .FOP para este usuario a partir de la plantilla .FO correspondiente:						
			//boolean correcto= this.obtencionPagina(ficheroFOP,plantillaFO, hashDatos);
			String sPlantillaFO=UtilidadesString.getFileContent(plantillaFO);
			String content=reemplazarDatos(request,sPlantillaFO);
			UtilidadesString.setFileContent(ficheroFOP,content);
			//
			// GENERACION DEL FICHERO PDF DEL USUARIO:
			//
			// Ubicacion de la carpeta donde se crean los ficheros PDF:
			rutaServidorDescargas = admParametros.getValor(institucion, "INF", "PATH_INFORMES_DESCARGA", "");
			//Antes estaba asi: rutaServidorDescargas += idInstitucion+File.separator;
			rutaServidorDescargas += File.separator+institucion+File.separator;
			
			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			rutaPDF = new File(rutaServidorDescargas);
			rutaPDF.mkdirs();
			ficheroPDF = new File(rutaServidorDescargas+nombreFicheroGenerado+".pdf");
			
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			Plantilla plantilla = new Plantilla();
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorPlantillas);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
			
			//request.setAttribute("rutaFichero", rutaServidorDescargas+nombreFicheroGenerado+".pdf");			
			//request.setAttribute("borrarFichero", "true");			
			
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");			
		}
		return true;
	}
*/
	/**
	 * Invoca varias clases para obtener los datos fijos de la factura 
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param idFactura Codigo factura
	 * @throws ClsExceptions Error interno
	 */	
	protected Hashtable cargarDatosFijos(UsrBean usuario,String institucion, String idFactura) throws ClsExceptions,SIGAException
	{
		Hashtable ht = new Hashtable();
		FacFacturaAdm facAdm = new FacFacturaAdm(usuario);
		ht=facAdm.getDatosImpresionInformeFactura(institucion,idFactura);
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
		Vector vLineasFactura=lineaFacturaAdm.getLineasImpresionInforme(institucion,idFactura);
		plantilla = this.reemplazaRegistros(plantilla, vLineasFactura, null, "LINEAFACTURA");
		
		
		{	// Comprobamos si tenemos que mostar el texto de "exento de iva" (si hay algun registro con iva = 0.0%)
			UtilidadesHash.set(htDatos, "EXENTO_IVA", "");
			if (vLineasFactura != null) {
				for (int i = 0; i < vLineasFactura.size(); i++) {
					Hashtable h = ((Row)vLineasFactura.get(i)).getRow();
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

		String resultado="exito";
		File fPdf = null;
		
		
		
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
		    HttpSession ses = request.getSession();
			
		    // UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		   
			
			GenParametrosAdm admParametros = new GenParametrosAdm(usrbean);
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usrbean);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// necesario para que lo pueda obtener la otra funcion
			request.setAttribute("IDFACTURA_INFORME",idFactura);
			
			//obtener plantilla
			FacFacturaAdm facAdm= new FacFacturaAdm(usrbean);
			Vector v = facAdm.getFactura(institucion,idFactura);
			Hashtable ht =null;
			String modelo ="";
			if (v!=null && v.size()>0) {
				ht = ((Row) v.get(0)).getRow(); 
				FacPlantillaFacturacionAdm plAdm= new FacPlantillaFacturacionAdm(usrbean);
				Vector vv = plAdm.getPlantillaSerieFacturacion(institucion,(String)ht.get(FacFacturaBean.C_IDSERIEFACTURACION));
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
		}catch (SIGAException se) {
			throw se;
		}catch (ClsExceptions ex) {
			throw ex;
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error a l generar el informe: "+e.getLocalizedMessage());
		} finally{
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
		
        return fPdf;
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
	public File generarFacturaRapida (HttpServletRequest request,String institucion, String idFactura) throws ClsExceptions,SIGAException {
		String resultado="exito";
		File rutaZIP = null;
			
		try {
		    HttpSession ses = request.getSession();
			GenParametrosAdm admParametros = new GenParametrosAdm(usrbean);
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    
			// necesario para que lo pueda obtener la otra funcion
			request.setAttribute("IDFACTURA_INFORME",idFactura);
			
			//obtener plantilla
			FacFacturaAdm facAdm= new FacFacturaAdm(usrbean);
			Vector v = facAdm.getFactura(institucion,idFactura);
			Hashtable ht =null;
			if (v!=null && v.size()>0) {
				ht = ((Row) v.get(0)).getRow(); 
				FacPlantillaFacturacionAdm plAdm= new FacPlantillaFacturacionAdm(usrbean);
			}
			
			// obtener ruta almacen
			String idserieidprogramacion = ht.get(FacFacturaBean.C_IDSERIEFACTURACION).toString()+"_" + ht.get(FacFacturaBean.C_IDPROGRAMACION).toString();
			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
    		rutaAlmacen += ClsConstants.FILE_SEP+institucion.toString()+ClsConstants.FILE_SEP+idserieidprogramacion;
    		
    		//Si existe ya el fichero ZIP (porque se ha generado en la confirmacion) se le devuelve, si no se genera y se devuelve
    		String rutaFileZipAux = rutaAlmacen +".zip";
    		rutaZIP =new File(rutaFileZipAux);
    		
    		if(!rutaZIP.exists()){   	
    			//No se generó xq en la serie no estaba configurado que se generara el fichero .zip. AQUI LO CREAMOS    			
				ClsLogging.writeFileLog("ANTES DE GENERAR EL INFORME ZIP. ",10);
				
				//Fichero de log
				String nombreFichero = "LOG_COMPRARAPIDA_FAC_"+ institucion+"_"+idserieidprogramacion+".log.xls"; 
				SIGALogging	log = new SIGALogging(rutaAlmacen+ClsConstants.FILE_SEP+nombreFichero);
				
				Facturacion facturacion = new Facturacion(usrbean);
				facturacion.generaryEnviarProgramacionFactura (request, usrbean, Integer.parseInt(institucion), Long.valueOf(ht.get(FacFacturaBean.C_IDSERIEFACTURACION).toString()),Long.valueOf(ht.get(FacFacturaBean.C_IDPROGRAMACION).toString()), false, log, null);
				ClsLogging.writeFileLog("DESPUES DE GENERAR EL INFORME EN  "+rutaAlmacen,10);			
    		} else {
    			ClsLogging.writeFileLog("SE DEVUELVE EL ZIP PREVIAMENTE GENERADO EN CONFIRMACION",10);
    		}
    		
		}catch (SIGAException se) {
			throw se;
		}catch (ClsExceptions ex) {
			throw ex;
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error a l generar el informe: "+e.getLocalizedMessage());
		} 
		
        return rutaZIP;
	}
	
	
}