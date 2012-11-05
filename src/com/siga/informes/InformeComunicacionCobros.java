package com.siga.informes;

import java.io.File;


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;

import com.siga.Utilidades.UtilidadesString;

import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;

import com.siga.beans.CenPersonaAdm;
import com.siga.beans.EnvPlantillaGeneracionAdm;


import com.siga.certificados.Plantilla;
import com.siga.general.SIGAException;


/**
 * Realiza un informe PDF mediante FOP de las facturas emitidas según plantilla introducida
 */

public class InformeComunicacionCobros extends MasterReport 
{
	public InformeComunicacionCobros(UsrBean usuario) {
		super(usuario);
	}

	private String reemplazarDatos(String plantillaFO, 
			Vector vDatos, String idioma, String idPersona, String idInstitucion) throws ClsExceptions, SIGAException 
	{
		
	

		if (vDatos != null)
			plantillaFO = this.reemplazaRegistros(plantillaFO, vDatos, null, "DESGLOSE");
		
		double importeTotal = 0;
		double deudaTotal = 0;
		for (int i = 0; i < vDatos.size(); i++)	{
			Row fila = (Row) vDatos.get(i);	
			double importe = Double.parseDouble((String)fila.getString("TOTAL"));
			importeTotal += importe; 
			double deuda = Double.parseDouble((String)fila.getString("DEUDA"));
			deudaTotal += deuda;
			
			
			
		}
		
		
		Hashtable hDatosFijos = new Hashtable();
		//Hay que buscar una query donde se saque el no,bre, direccion, codigoPostal y poblacion
		
		CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUsuario());
		String nombre = personaAdm.obtenerNombreApellidos(idPersona);
		hDatosFijos.put("NOMBRE_CLIENTE", nombre);
		
		
		CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.getUsuario());
		Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,idInstitucion,"2");
		String strPireccion="";
		String codPostal ="";
		String poblacion = "";
		if (direccion.get(CenDireccionesBean.C_DOMICILIO)!=null){
			strPireccion = (String)direccion.get(CenDireccionesBean.C_DOMICILIO);
			
			
		}
		if (direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null){
			codPostal = (String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL);
			
			
		}
		if ((direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
			poblacion=(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA");
		}
		hDatosFijos.put("DIRECCION_CLIENTE", strPireccion);
		hDatosFijos.put("CODIGO_POSTAL_CLIENTE", codPostal);
		hDatosFijos.put("POBLACION_PROVINCIA_CLIENTE", poblacion);
		hDatosFijos.put("LUGAR_FECHA", GstDate.getFechaLenguaje(idioma, UtilidadesBDAdm.getFechaBD("")));
		
		
		//hDatosFijos.put("TEXTO_CARTA_DESIGNACION",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.designacion")) ;
		//hDatosFijos.put("TEXTO_CARTA_BLOQUE_1",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque1")) ;
		//hDatosFijos.put("TEXTO_CARTA_BLOQUE_2",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque2")) ;
		//hDatosFijos.put("TEXTO_CARTA_BLOQUE_3",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.bloque3")) ;
		hDatosFijos.put("TEXTO_CARTA_FECHA_FACTURA",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.fecha"));
		hDatosFijos.put("TEXTO_CARTA_NFACTURA",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.nFactura"));
		hDatosFijos.put("TEXTO_CARTA_TOTAL_FACTURA",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.totalFactura"));
		hDatosFijos.put("TEXTO_CARTA_PENDIENTE_PAGO",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.pendiente"));
		hDatosFijos.put("TEXTO_CARTA_TOTAL",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.total"));
		hDatosFijos.put("GLOBAL_FACTURA",String.valueOf(importeTotal));
		hDatosFijos.put("GLOBAL_PENDIENTE",String.valueOf(deudaTotal));
		
//		hDatosFijos.put("TEXTO_CARTA_DESPEDIDA",UtilidadesString.getMensajeIdioma(idioma,"informes.comunicacionMorosos.despedida"));
		
		plantillaFO = this.reemplazaVariables(hDatosFijos, plantillaFO);
		
		return plantillaFO;
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
	public File generarListadoComunicacionCobros (HttpServletRequest request, Vector vDatos,
			String idTipoEnvio ,String idPlantilla,String idPlantillaGeneracion,
			String idPersona, String idInstitucion) throws ClsExceptions,SIGAException 
	{
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");
		// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
	    String rutaTemporal =  rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
		String barraTemporal = "";
		if (rutaTemporal.indexOf("/") > -1){ 
			barraTemporal = "/";
		}
		if (rutaTemporal.indexOf("\\") > -1){ 
			barraTemporal = "\\";
		}    		
		rutaTemporal += barraTemporal+idInstitucion;
		File rutaFOP=new File(rutaTemporal);
		//Comprobamos que exista la ruta y sino la creamos
		if (!rutaFOP.exists()){
			if(!rutaFOP.mkdirs()){
				throw new ClsExceptions("La ruta de acceso a los ficheros temporales no puede ser creada");					
			}
		}
		
		// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
	    String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoComunicacionesPDFJava")+rp.returnProperty("facturacion.directorioComunicacionesPDFJava");
		String barraAlmacen = "";
		if (rutaAlmacen.indexOf("/") > -1){ 
			barraAlmacen = "/";
		}
		if (rutaAlmacen.indexOf("\\") > -1){ 
			barraAlmacen = "\\";
		}    		
		rutaAlmacen += barraAlmacen+idInstitucion;
		File rutaPDF=new File(rutaAlmacen);
		//Comprobamos que exista la ruta y sino la creamos
		if (!rutaPDF.exists()){
			if(!rutaPDF.mkdirs()){
				throw new ClsExceptions("La ruta de acceso a los ficheros PDF no puede ser creada");					
			}
		}
		
		
		EnvPlantillaGeneracionAdm admEnvPlantilla = new EnvPlantillaGeneracionAdm(this.getUsuario());
		String nombrePlantilla = idTipoEnvio + "_" + idPlantilla + "_" + idPlantillaGeneracion;
		String pathPlantillaFromDB = admEnvPlantilla.getPathPlantillasFromDB(); 
		String barraPlantilla="";
		if (pathPlantillaFromDB.indexOf("/") > -1){
			barraPlantilla = "/";
		}
		if (pathPlantillaFromDB.indexOf("\\") > -1){
			barraPlantilla = "\\";
		}
		
   	    String rutaPlantillas = pathPlantillaFromDB + barraPlantilla + idInstitucion;
   	    String rutaPlantilla = rutaPlantillas + barraPlantilla + nombrePlantilla;
   	    
		
		File rutaModelo=new File(rutaPlantilla);
		//Comprobamos que exista la ruta y sino la creamos
		if (!rutaModelo.exists()){
			throw new SIGAException ("messages.facturacion.errorRutaNoExiste");
			// ClsExceptions("La ruta de acceso a la plantilla de la factura no existe");					
		}

		
		String fechaActual=UtilidadesBDAdm.getFechaBD("").replace('/','-')+"_"+UtilidadesBDAdm.getHoraBD().replace(':','-');
		String nombrePDF = idPersona+"_"+fechaActual;
		
		File fPdf = null;
		File rutaTmp=null;
			
		try {
			UsrBean usr = this.getUsuario();
			String idioma = usr.getLanguage();
			String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantillas,nombrePlantilla);
			fPdf = generarInforme(request,idioma, vDatos, rutaTemporal, rutaPlantillas,contenidoPlantilla, rutaAlmacen, nombrePDF,idPersona,idInstitucion);
		}
		catch (SIGAException se) {
			throw se;
		}
		catch (ClsExceptions ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		} 
		finally {
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return fPdf;
	}
	
	
	/**
	 * Genera el PDF. Devuelve true si lo ha generado correctamente.
	 * @param request
	 * @param datosBase
	 * @param rutaServidorTmp Ruta del fichero FOP temporal
	 * @param contenidoPlantilla datos a reemplazar
	 * @param rutaServidorDescargas Ruta del PDF generado
	 * @param nombreFicheroPDF Nombre del PDF generado
	 * @return True si lo ha generado correctaemnte, false en cualquier otro caso.
	 * @throws ClsExceptions
	 */
	public File generarInforme(HttpServletRequest request ,String idioma,Vector vDatos,
			String rutaServidorTmp,String rutaPlantilla, String contenidoPlantilla,
			String rutaServidorDescargas, String nombreFicheroPDF,
			String idPersona, String idInstitucion) throws ClsExceptions {
		
		
		
		File ficheroFOP=null;		
		File ficheroPDF=null;
		File rutaTmp=null;
		File rutaPDF=null;
		
		try {
			
			//
			// GENERACION DEL FICHERO FOP DEL USUARIO:nombre del fichero generado a partir del nombre de la plantilla concatenado con la hora en milisegundos
			//	 					
			//Crea el fichero. Si no existe el directorio temporal lo crea.	 		
			rutaTmp = new File(rutaServidorTmp);
			rutaTmp.mkdirs();
			ficheroFOP = new File(rutaServidorTmp+ClsConstants.FILE_SEP+nombreFicheroPDF+System.currentTimeMillis()+".fo");
			
			// Generacion del fichero .FOP para este usuario a partir de la plantilla .FO 						
			String content=reemplazarDatos(contenidoPlantilla,vDatos,idioma,idPersona, idInstitucion);
			UtilidadesString.setFileContent(ficheroFOP,content);
			//
			// GENERACION DEL FICHERO PDF DEL USUARIO:
			//
			
			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			rutaPDF = new File(rutaServidorDescargas);
			rutaPDF.mkdirs();
			ficheroPDF = new File(rutaServidorDescargas+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");
			
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			MasterReport masterReport = new  MasterReport();
			masterReport.convertFO2PDF(ficheroFOP, ficheroPDF, rutaPlantilla);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		return ficheroPDF;
	}
	
	
}