
package com.siga.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

import com.atos.utils.*;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.InformesGenericosForm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;

public class MasterReport  {
	
	private UsrBean usuario;
	
	public MasterReport(){
		
	}

	public MasterReport(UsrBean usuario){
		this.usuario = usuario;
	}
	
	
	public Vector obtenerDatosFormulario(InformesGenericosForm form) throws ClsExceptions {
	    Vector salida = new Vector ();
	    String datosInforme = form.getDatosInforme();
	    if(datosInforme.endsWith("%%%")){
	    	int indice = datosInforme.lastIndexOf("%%%");
	    	datosInforme = datosInforme.substring(0,indice);
	    }
	
	
	    
	    try {
	        GstStringTokenizer st1 = new GstStringTokenizer(datosInforme,"%%%");
		    while (st1.hasMoreTokens()) {
		        Hashtable ht = new Hashtable();
		        String registro = st1.nextToken();
		        GstStringTokenizer st = new GstStringTokenizer(registro,"##");
			    while (st.hasMoreTokens()) {
			        String dupla = st.nextToken();
			        String d[]= dupla.split("==");
			        //Cuando no vienen datos completos es porque hay alguna funcionalidad que no requiere estos datos
			        //ejemplo:idPersona del Certificado de IRPF. cuando se saca individulamente
			        //desde la icha colegial si tiene persona. cuando se saca desde la opcion de menu de
			        //informes no porque se supone que es para todos los colegiados que hayan tenido pagos el
			        //anyo del informe
			        if(d.length>1)
			        	ht.put(d[0],d[1]);    
			    }
		        salida.add(ht);
		    }
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al obtener los datos del formulario.");
	    }
	    return salida;
	}
	public Vector obtenerDatosFormulario(DefinirEnviosForm form) throws ClsExceptions {
	    Vector salida = new Vector ();
	    String datosInforme = form.getDatosEnvios();
	    if(datosInforme.endsWith("%%%")){
	    	int indice = datosInforme.lastIndexOf("%%%");
	    	datosInforme = datosInforme.substring(0,indice);
	    }
	    try {
	        GstStringTokenizer st1 = new GstStringTokenizer(datosInforme,"%%%");
		    while (st1.hasMoreTokens()) {
		        Hashtable ht = new Hashtable();
		        String registro = st1.nextToken();
		        GstStringTokenizer st = new GstStringTokenizer(registro,"##");
			    while (st.hasMoreTokens()) {
			        String dupla = st.nextToken();
			        String d[]= dupla.split("==");
			        if(d.length==2)
			        	ht.put(d[0],d[1]);    
			    }
		        salida.add(ht);
		    }
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al obtener los datos del formulario.");
	    }
	    return salida;
	}
	
	/**
	 * Carater de control que delimita las variables en las plantillas FO
	 */
	protected static final String CTR="%%";
	
	//Genera el PDF. Devuelve true si lo ha generado correctamente.
	public boolean generarInforme(HttpServletRequest request, String nombreFicheroFO) throws ClsExceptions {
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		String institucion =usr.getLocation();
		String idioma=request.getParameter("idioma");
		String idiomaExt="";
		if(idioma==null || idioma.length()==0){
			idioma = usr.getLanguage();
		}
		// RGG 26/02/2007 cambio en los codigos de lenguajes
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		idiomaExt = "es";
		try {
			idiomaExt = a.getLenguajeExt(idioma).toUpperCase();
		} catch (Exception e) {
			
		}	
		
		usr.setLanguage(idioma);
		usr.setLanguageExt(idiomaExt);
		
		
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
			Plantilla plantilla = new Plantilla(this.usuario);
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorPlantillas);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
			
			request.setAttribute("rutaFichero", rutaServidorDescargas+nombreFicheroGenerado+".pdf");			
			request.setAttribute("borrarFichero", "true");			
			
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");			
		}
		return true;
	}
	
	
	// OBTENCION DE LA PLANTILLA FO:
	public String obtenerContenidoPlantilla(String rutaServidorPlantillas, String nombrePlantilla)throws Exception{
		ClsLogging.writeFileLog("*************** PLANTILLA : " + rutaServidorPlantillas+ClsConstants.FILE_SEP+nombrePlantilla,10);
		String barraPlantilla="";
		if (rutaServidorPlantillas.indexOf("/") > -1){
			barraPlantilla = "/";
		}
		if (rutaServidorPlantillas.indexOf("\\") > -1){
			barraPlantilla = "\\";
		}
		if(barraPlantilla.equals(""))
			barraPlantilla = ClsConstants.FILE_SEP;
			
			
		
		
		File plantillaFO= new File(rutaServidorPlantillas+barraPlantilla+nombrePlantilla);	 
	    if (!plantillaFO.exists()){
	    	throw new SIGAException("messages.envios.error.noPlantilla");
        }else if (!plantillaFO.canRead()){
//			throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
			throw new ClsExceptions("Error dfe lectura del fichero FOP: "+plantillaFO.getAbsolutePath());
		}
		return UtilidadesString.getFileContent(plantillaFO);
		
		
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
	public File generarInforme(HttpServletRequest request,Hashtable datosBase,
			String rutaServidorTmp, String contenidoPlantilla,
			String rutaServidorDescargas, String nombreFicheroPDF) throws ClsExceptions {
	
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
			String content=reemplazarDatos(request,contenidoPlantilla,datosBase);
			UtilidadesString.setFileContent(ficheroFOP,content);
			//
			// GENERACION DEL FICHERO PDF DEL USUARIO:
			//
			
			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			rutaPDF = new File(rutaServidorDescargas);
			rutaPDF.mkdirs();
			ficheroPDF = new File(rutaServidorDescargas+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");
			
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			Plantilla plantilla = new Plantilla(this.usuario);
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorTmp);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		return ficheroPDF;
	}
	public File generarInforme(UsrBean usr,Hashtable datosBase,
			String rutaServidorTmp, String contenidoPlantilla,
			String rutaServidorDescargas, String nombreFicheroPDF) throws ClsExceptions {
	
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
			String content=reemplazarDatos(usr,contenidoPlantilla,datosBase);
			UtilidadesString.setFileContent(ficheroFOP,content);
			//
			// GENERACION DEL FICHERO PDF DEL USUARIO:
			//
			
			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			rutaPDF = new File(rutaServidorDescargas);
			rutaPDF.mkdirs();
			ficheroPDF = new File(rutaServidorDescargas+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");
			
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			Plantilla plantilla = new Plantilla(this.usuario);
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorTmp);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		return ficheroPDF;
	}
	
	/**
	 * 
	 * @param pathXml Es el path donde se va a crear el fichero que va a ser la fuente de datos 
	 * @param datosFormulario Hash con los valores del formulario necesario para el acceso a BBDD
	 * @return el metodo genera el fichero xml que, aplicandole la un xsl generara un pdf
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected File getXmlFileToReportWithXslFo(String pathXml, Hashtable datosFormulario) throws ClsExceptions, SIGAException{
		 return null;
	 }

	/**
	 * 
	 * @param pathXml Path donde esta la fuente de datos(fichero xml)
	 * @param xslDir Directorio donde se encuentra el fichero xsl
	 * @param xslName Nombre del fichero xsl que se aplicara al xml para transfromarlo a pdf
	 * @param pdfDir Directorio donde se ubicara el fichero pdf generado
	 * @param pdfName Nombre del pdf generado
	 * @param datosFormulario  Hash con los valores del formulario necesario para el acceso a BBDD
	 * @return Devuelve el archivo pdf generado
	 */
	public File generarInformePdfFromXmlToFoXsl(String pathXml,String xslDir,String xslName,
			String pdfDir,String pdfName, Hashtable datosFormulario) throws SIGAException{
		File pdfFile = null;
		File xmlFile = null;
		try {
			
			ClsLogging.writeFileLog("Generando fichero XML para informe XSL-FO...",10);
			xmlFile = getXmlFileToReportWithXslFo(pathXml,datosFormulario);
			ClsLogging.writeFileLog("Fichero XML generado.",10);
			
			
			//Setup directories
			File fileBaseDir = new File(xslDir);
			File filePdfDir = new File(pdfDir);
			File outDir = new File(pdfDir);
			outDir.mkdirs();

			//Setup input and output files            
			File xsltFile = new File(fileBaseDir, xslName);
			pdfFile = new File(filePdfDir, pdfName);

			ClsLogging.writeFileLog("Input xml:"+xmlFile,10);
			ClsLogging.writeFileLog("Stylesheet xsl:"+xsltFile,10);
			ClsLogging.writeFileLog("Output pdf:"+pdfFile,10);
			
			ClsLogging.writeFileLog("Convirtiendo xml a pdf...",10);

			convertXML2PDF(xmlFile, xsltFile, pdfFile);
			ClsLogging.writeFileLog("Pdf creado. ",10);

		} catch (SIGAException e) {
			throw e;	
		} catch (Exception e) {
			System.err.println(ExceptionUtil.printStackTrace(e));
			//System.exit(-1);
		}finally{
			if(xmlFile!=null && xmlFile.exists()){
				ClsLogging.writeFileLog("Eliminando xml generado...",10);
				xmlFile.delete();
				ClsLogging.writeFileLog("Fichero xml eliminado.",10);
			}
			
		}
		return pdfFile;

	}
	/***
	 * 
	 * @param xml Fichero xml a transfomar en pdf
	 * @param xslt Fichero xsl que transforma el xml a pdf
	 * @param pdf Fichero pdf que se convertira en el pdf deseado
	 * @return Fichero pdf generado
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	private File convertXML2PDF(File xml, File xslt, File pdf)
	throws IOException, FOPException, TransformerException {
//		Construct driver
		Driver driver = new Driver();

//		Setup logger
		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
		MessageHandler.setScreenLogger(logger);

//		Setup Renderer (output format)        
		driver.setRenderer(Driver.RENDER_PDF);

//		Setup output
		OutputStream out = new java.io.FileOutputStream(pdf);
		try {
			driver.setOutputStream(out);

//			Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslt));

//			Setup input for XSLT transformation
			Source src = new StreamSource(xml);

//			Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(driver.getContentHandler());

//			Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} finally {
			out.close();
		}
		return pdf;
	}
	
	private File convertXML2PDF(InputStream xml, File xslt, File pdf)
	throws IOException, FOPException, TransformerException {
		return convertXML2PDF(xml, new FileInputStream(xslt),pdf);
		
	}
	
	/***
	 * 
	 * @param xml Fichero xml a transfomar en pdf
	 * @param xslt Fichero xsl que transforma el xml a pdf
	 * @param pdf Fichero pdf que se convertira en el pdf deseado
	 * @return Fichero pdf generado
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	private File convertXML2PDF(InputStream xml, InputStream xslt, File pdf)
	throws IOException, FOPException, TransformerException {
//		Construct driver
		Driver driver = new Driver();

//		Setup logger
		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
		MessageHandler.setScreenLogger(logger);

//		Setup Renderer (output format)        
		driver.setRenderer(Driver.RENDER_PDF);

//		Setup output
		OutputStream out = new java.io.FileOutputStream(pdf);
		try {
			driver.setOutputStream(out);

//			Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslt));

//			Setup input for XSLT transformation
			Source src = new StreamSource(xml);

//			Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(driver.getContentHandler());

//			Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} finally {
			out.close();
		}
		return pdf;
	}
	
	
	/**
	 * Genera el PDF. Devuelve Fichero!=null si lo ha hecho correctamente. Utiliza como temporal la ruta final para despues borrar el temporal.
	 * @param request
	 * @param rutaServidorTmp Ruta del fichero FOP temporal
	 * @param contenidoPlantilla datos a reemplazar
	 * @param contenidoPlantilla String con contenido de plantilla FOP
	 * @param rutaServidorDescargas Ruta del PDF generado
	 * @param nombreFicheroPDF Nombre del PDF generado
	 * @return File Dichero resultante PDF. Distinto de nulo si lo ha generado correctaemnte, nulo en cualquier otro caso.
	 * @throws ClsExceptions
	 * @author RGG 
	 * @since 15/02/2007
	 */
	public File generarInforme(HttpServletRequest request, String rutaServidorTmp, String contenidoPlantilla, String rutaServidorDescargas, String nombreFicheroPDF) throws ClsExceptions 
	{
	
		File ficheroFOP=null;		
		File ficheroPDF=null;
		File rutaTmp=null;
		
		
		try {
			nombreFicheroPDF = UtilidadesString.validarNombreFichero(nombreFicheroPDF);
			
			//Crea la ruta temporal
			rutaTmp = new File(rutaServidorTmp);
			rutaTmp.mkdirs();
			ficheroFOP = new File(rutaTmp+ClsConstants.FILE_SEP+nombreFicheroPDF+System.currentTimeMillis()+".fo");
			//ficheroFOP = new File(rutaTmp.getParent()+ClsConstants.FILE_SEP+nombreFicheroPDF+System.currentTimeMillis()+".fo");
			
			// Generacion del fichero .FOP para este usuario a partir de la plantilla .FO 		
			ClsLogging.writeFileLog("ANTES DE REEMPLAZAR LOS DATOS DE LA PLANTILLA. ",10);
			String content=reemplazarDatos(request,contenidoPlantilla);
			ClsLogging.writeFileLog("DESPUES DE REEMPLAZAR LOS DATOS DE LA PLANTILLA. ",10);
			UtilidadesString.setFileContent(ficheroFOP,content);

			//Crea el fichero. Si no existe el directorio de la institucion para la descarga lo crea.
			ficheroPDF = new File(rutaServidorDescargas+ClsConstants.FILE_SEP+nombreFicheroPDF+".pdf");
			ClsLogging.writeFileLog("RUTA DONDE SE UBICARÁ EL INFORME. "+ficheroPDF,10);
			//Clase para la conversion de FOP a PDF con un directorio base para usar rutas relativas:
			Plantilla plantilla = new Plantilla(this.usuario);
			ClsLogging.writeFileLog("ANTES DE GENERAR EL PDF.",10);
			plantilla.convertFO2PDF(ficheroFOP, ficheroPDF, rutaTmp.getParent());
			ClsLogging.writeFileLog("PDF GENERADO.",10);
			
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe: "+e.getLocalizedMessage());
		} finally {
			if (ficheroFOP!=null && ficheroFOP.exists()) {
				ficheroFOP.delete();
			}
			if (rutaTmp!=null && rutaTmp.exists()) {
				rutaTmp.delete();
			}
			
		}
		return ficheroPDF;
	}
	
	/**
	 * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions{
		 return plantillaFO;
	 }
	protected String reemplazarDatos(UsrBean usr, String plantillaFO) throws ClsExceptions{
		 return plantillaFO;
	 }
	 
	/**
	 * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosBase) throws ClsExceptions, SIGAException{
		 return plantillaFO;
	 }
	protected String reemplazarDatos(UsrBean usr, String plantillaFO, Hashtable datosBase) throws ClsExceptions, SIGAException{
		 return plantillaFO;
	 }
	
	 
	 /**
	  * Obtiene un bloque de plantilla delimitado por las marcas 'INI_delim' y 'FIN_delim'
	  * Crea un bloque en el que reemplaza los valores de cada registro
	  * Inserta todo en la tabla hash de datos con delim como clave
	  * Y sustituye en la plantilla el bloque por un nuevo valor 'delim'
	  * @param plantillaFO Plantilla original antes de la sustitucion
	  * @param registros Valores que llegan de la base de datos para ser sustituidos (Debe cotener objetos Row)
	  * @param htDatos Conjunto de claves que se sutituiran posteriormente en el metodo que eta invocando
	  * @param delim Cadena que delimita
	  * @return Plantilla con el bloque de registro sustituido por el delimitador
	  * y la variable htDatos modificada con el valor que corresponde al delimitador
	  */
	 public String reemplazaRegistros(String plantillaFO, Vector registros, Hashtable htDatos, String delim){
		 String plantilla=plantillaFO;
		 String delimIni=CTR+"INI_"+delim+CTR;
		 String delimFin=CTR+"FIN_"+delim+CTR;
		 String sAux="";
		 
		 // RGG 09/07/2008
		 if (registros==null || registros.isEmpty()) {
		 	ClsLogging.writeFileLog("MASTERREPORT: no hay registros",10);
			 // cuando no existen datos se busca la marca INI_TODO_xxx para sustituirla por NADA.
		     // Estas marcas deben estas fuera de las que pretendemos cambiar con datos.
			 delimIni=CTR+"INI_TODO_"+delim+CTR;
			 delimFin=CTR+"FIN_TODO_"+delim+CTR;
			 sAux="";
		 } else {
		 	
		     String delimTodoIni=CTR+"INI_TODO_"+delim+CTR;
		     String delimTodoFin=CTR+"FIN_TODO_"+delim+CTR;
		    
		     String auxAux=UtilidadesString.encuentraEntreMarcas(plantilla, delimTodoIni, delimTodoFin);
		    
		     plantilla=UtilidadesString.reemplazaEntreMarcasCon(plantilla, delimTodoIni, delimTodoFin,auxAux);
		    
		     if(registros!=null && !registros.isEmpty()){
		     	
				 String plantillaRegistro=UtilidadesString.encuentraEntreMarcas(plantilla, delimIni, delimFin);
				 int size=registros.size();
				 ClsLogging.writeFileLog("MASTERREPORT: TAMAÑO DEL VECTOR PARA REEMPLAZAR DATOS: "+size,10);
				 for(int i=0;i<size;i++){
					 Object rObj=registros.elementAt(i);
					 Hashtable row=null;
					 if(rObj instanceof Row){
						 row=((Row)registros.elementAt(i)).getRow();
					 }else{
						 row=(Hashtable)rObj;
					 }
					 //ClsLogging.writeFileLog("MASTERREPORT: REGISTRO: "+i,10);
			
					 sAux+=UtilidadesString.reemplazaParametros(plantillaRegistro,CTR, row);
			
				 }
			 }
		 }	 
		 
	     plantilla=UtilidadesString.reemplazaEntreMarcasCon(plantilla, delimIni, delimFin,sAux);
	     
		 return plantilla;
	 }
	 public Vector obtenerPlantillasFormulario(InformesGenericosForm form, UsrBean usr) throws ClsExceptions {
		    Vector salida = new Vector ();
		    try {
			    AdmInformeAdm adm = new AdmInformeAdm(usr);
			    StringTokenizer st = new StringTokenizer(form.getIdInforme(),"##");
			    while (st.hasMoreTokens()) {
			        String id = st.nextToken();
			        salida.add(adm.obtenerInforme(form.getIdInstitucion(),id));
			    }
		    } catch (ClsExceptions e) {
		        throw e;
		    } catch (Exception e) {
		        throw new ClsExceptions(e,"Error al obtener los objetos plantillas del formulario.");
		    }
		    return salida;
		}

	 /**
	  * Reemplaza en la plantilla los codigos, delimitados por caracteres de control, por sus respectivos valores 
	  * @param htDatos Conjunto de valores
	  * @param plantillaFO Plantilla que se va a tratar
	  * @return Plantilla con los valores sustituidos
	  */	 
	 public String reemplazaVariables(Hashtable htDatos, String plantillaFO){
		 return UtilidadesString.reemplazaParametros(plantillaFO,CTR, htDatos);
	 }

	public UsrBean getUsuario() {
		return usuario;
	}

	public void setUsuario(UsrBean usuario) {
		this.usuario = usuario;
	}


}
