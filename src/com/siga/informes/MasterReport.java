package com.siga.informes;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Options;
import org.apache.fop.messaging.MessageHandler;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.xml.sax.XMLReader;

import com.atos.utils.*;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.general.SIGAException;
import com.siga.informes.form.InformesGenericosForm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;

public class MasterReport
{
	/**
	 * Carater de control que delimita las variables en las plantillas FO
	 */
	protected static final String CTR="%%";
	
	/**
	 * Usuario del action
	 */
	private UsrBean usuario;
	
	
	// CONSTRUCTORES
	public MasterReport() {
	}
	public MasterReport(UsrBean usuario) {
		this.usuario = usuario;
	}	
	
	
	// METODOS
	public Vector obtenerDatosFormulario(InformesGenericosForm form) throws ClsExceptions {
	    Vector salida = new Vector ();
	    String datosInforme = form.getDatosInforme();
	    if(datosInforme.endsWith("%%%")){
	    	int indice = datosInforme.lastIndexOf("%%%");
	    	datosInforme = datosInforme.substring(0,indice);
	    }
	
	
	    
	    try {
	        String[] st1 = datosInforme.split("%%%");
		    for (String registro: st1) {
		        Hashtable ht = new Hashtable();
		        String[] registro2 = registro.split("##");
			    for (String dupla: registro2) {
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
	public Vector obtenerDatosFormulario(String datosInforme) throws ClsExceptions {
	    Vector salida = new Vector ();
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
	public Vector getDatosInforme(String datosInforme) throws ClsExceptions {
	    Vector salida = new Vector ();
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
	public Vector getDatosInforme(String datosInforme,String idTipoInforme) throws ClsExceptions {
	    Vector salida = new Vector ();


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
			    if(idTipoInforme!=null){
			    	ht.put("idTipoInforme", idTipoInforme);
			    }
		        salida.add(ht);
		    }
	    } catch (Exception e) {
	        throw new ClsExceptions(e,"Error al obtener los datos del formulario.");
	    }
	    return salida;
	}
	
	/**
	 * Genera un informe PDF sin mas configuracion
	 * @return True si lo ha generado correctamente
	 */
	public boolean generarInforme(HttpServletRequest request,
								  String nombreFicheroFO)
			throws ClsExceptions,SIGAException
	{
		// Variables generales
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String institucion = usr.getLocation();
		String idiomaExt;
		
		// Ficheros necesarios: plantilla, intermedio FOP, PDF final
		File plantillaFO = null;
		File ficheroFOP = null;
		File ficheroPDF = null;

		// Rutas
		String rutaServidorPlantillas = null; // ruta base para plantilla e intermedio FOP
		String rutaServidorDescargas = null; // ruta base para PDF final
		String rutaPlantillaFO = null; //ruta plantilla
		String rutaFOP = null; // ruta intermedio FOP (directorio)
		String rutaFicheroFOP = null; // ruta intermedio FOP (fichero)
		String rutaFicheroPDF = null; // ruta PDF final (directorio)
		String nombreFicheroGenerado = null; // ruta PDF final (fichero)

		try {
			// obteniendo idioma
			String idioma = request.getParameter("idioma");
			if (idioma == null || idioma.length() == 0)
				idioma = usr.getLanguage();
			usr.setLanguage(idioma);
			idiomaExt = "es";
			try {
				idiomaExt = new AdmLenguajesAdm(usr).getLenguajeExt(idioma).toUpperCase();
			} catch (Exception e) {
			}
			usr.setLanguageExt(idiomaExt);

			// obteniendo rutas de origen (plantilla), destino (PDF) y nombre de fichero generado unico
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
			rutaServidorPlantillas = admParametros.getValor(institucion, "INF",
					"PATH_INFORMES_PLANTILLA", "")
					+ ClsConstants.FILE_SEP + institucion;
			rutaServidorDescargas = admParametros.getValor(institucion, "INF",
					"PATH_INFORMES_DESCARGA", "") + File.separator + institucion
					+ File.separator;
			nombreFicheroGenerado = nombreFicheroFO + System.currentTimeMillis();

			// 1. obteniendo plantilla FO
			rutaPlantillaFO = rutaServidorPlantillas + ClsConstants.FILE_SEP
					+ nombreFicheroFO + "_" + idiomaExt + ".fo";
			plantillaFO = new File(rutaPlantillaFO);
			ClsLogging.writeFileLog("*********** rutaFicheroFO: "
					+ rutaPlantillaFO, 7);

			// 2. generando intermedio FOP a partir de plantilla y datos
			// 2.1. obteniendo ruta para fichero intermedio FOP
			rutaFOP = rutaServidorPlantillas + ClsConstants.FILE_SEP
					+ "tmp";
			new File(rutaFOP).mkdirs();
			rutaFicheroFOP = rutaFOP + ClsConstants.FILE_SEP
					+ nombreFicheroGenerado + ".fo";
			ficheroFOP = new File(rutaFicheroFOP);

			// 2.2. obteniendo texto de plantilla FO
			String sPlantillaFO = UtilidadesString.getFileContent(plantillaFO);
			
			// 2.3. generando intermedio FOP, reemplazando los datos en la plantilla
			String content = reemplazarDatos(request, sPlantillaFO);
			UtilidadesString.setFileContent(ficheroFOP, content);

			// 3. generando PDF final
			// 3.1. obteniendo ruta para fichero PDF final
			new File(rutaServidorDescargas).mkdirs();
			rutaFicheroPDF = rutaServidorDescargas + nombreFicheroGenerado
					+ ".pdf";
			ficheroPDF = new File(rutaFicheroPDF);
			ClsLogging.writeFileLog("*********** rutaFicheroPDF: "
					+ rutaFicheroPDF, 7);

			// 3.2. convirtiendo FOP a PDF
//			Plantilla plantilla = new Plantilla(this.usuario);
			this.convertFO2PDF(ficheroFOP, ficheroPDF,
					rutaServidorPlantillas);

			// 3.3. borrando fichero intermedio FOP generado
			ficheroFOP.delete();

			// devolviendo el fichero PDF generado
			request.setAttribute("rutaFichero", rutaServidorDescargas
					+ nombreFicheroGenerado + ".pdf");
			request.setAttribute("borrarFichero", "true");

		} catch (SIGAException e){
			throw e;
		}catch (Exception e) {
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		
		return true;
	} // generarInforme()
	
	// OBTENCION DE LA PLANTILLA FO:
	public String obtenerContenidoPlantilla(String rutaServidorPlantillas, String nombrePlantilla) throws SIGAException, ClsExceptions{
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
//			Plantilla plantilla = new Plantilla(this.usuario);
			this.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorTmp);
			
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
//			Plantilla plantilla = new Plantilla(this.usuario);
			this.convertFO2PDF(ficheroFOP, ficheroPDF, rutaServidorTmp);
			
			// Borramos el .FOP que hemos generado para este usuario:
			ficheroFOP.delete();
		} catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe");
		}
		return ficheroPDF;
	}
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
	
	protected File convertXML2PDF(InputStream xml, File xslt, File pdf)
	throws IOException, FOPException, TransformerException {
		return convertXML2PDF(xml, new FileInputStream(xslt),pdf);
		
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
	
	protected File convertXML2PDF(InputStream xml, File xslt, File pdf,Map<String, String> mapParameters)
	throws IOException, FOPException, TransformerException {
		InputStream inputXslt = new FileInputStream(xslt);
		return convertXML2PDF(xml,inputXslt ,pdf,mapParameters);
		
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
	protected File convertXML2PDF(InputStream xml, InputStream xslt, File pdf,Map<String, String> mapParameters)
	throws IOException, FOPException, TransformerException {
//		Construct driver
		Driver driver = new Driver();

//		Setup logger
		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
//		driver.getContentHandler().
		
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
//			String[]pruebaEtiquetas = new String[3];
//			pruebaEtiquetas[0]="Etiqueta 0";
//			pruebaEtiquetas[1]="Etiqueta 1";
//			pruebaEtiquetas[2]="Etiqueta 2";
			for (Map.Entry<String, String> entrada:mapParameters.entrySet()){
				
				transformer.setParameter(entrada.getKey(), entrada.getValue());
			}
//			transformer.setParameter("etiquetas", pruebaEtiquetas);
//			Prueba prueba = new Prueba();
//			prueba.setNombre("Nombre prueba");
//			prueba.setDescripcion("Descripcion prueba");
//			transformer.setParameter("prueba", prueba);
			
//			Setup input for XSLT transformation
			Source src = new StreamSource(xml);
			
//			Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(driver.getContentHandler());
			
//			Start XSLT transformation and FOP processing
			transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-15");
			
			javax.xml.transform.Source xmlSource =
				new javax.xml.transform.stream.StreamSource(new InputStreamReader(xml, "ISO-8859-15"));

			
			
//			transformer.transform(src, res);
			transformer.transform(xmlSource, res);
			
			
			
			
			
			
		} finally {
			out.close();
		}
		return pdf;
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
	public static File convertXML2PDF(InputStream xml, InputStream xslt, File pdf)
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

	
	
	
	/*III. XML a RTF XML to RTF 
xml a la conversión rtf hay algunos pocos problemas, no tenemos un XML a RTF directa de la API, vamos a utilizar la API de JFOR aún no se ha integrado en el FOP xml to rtf conversion there are some little trouble, we do not have a direct XML to RTF from the API, we will use the JFor API has not yet integrated into the FOP 

Ir. Go. JFOR API se puede lograr desde el caso de los documentos a la conversión de archivos RTF, sino que también ofrece una interfaz CONSLE. JFor API can be achieved from the FO documents to RTF file conversion, it also offers consle interface. 

Podemos www.jfor.org obtenidos a partir de la información pertinente JFOR. We can www.jfor.org obtained from jfor relevant information. 

A partir de documentos XML a la conversión de archivos RTF se pueden dividir en dos pasos: From XML documents to the RTF file conversion can be divided into two steps: 

1. 1. Xml con FOP para convertir a Xml using FOP to convert fo 

2. 2. JFOR con RTF se convertirá en la JFor with RTF will be converted into fo 

3.1 con FOP a xml para convertir 3.1 with FOP to xml convert fo 

Este paso se puede utilizar el método descrito anteriormente, el código XML siguiente para la realización de la conversión, todavía puede utilizar el documento XML utilizado anteriormente en This step we can easily use the method described above, the following xml to the realization of fo conversion, they may still use the above xml document used in 

Y el documento XSL-FO. And xsl-fo document. 

OutputStream foOut = new FileOutputStream (fofile); OutputStream foOut = new FileOutputStream (fofile); 
TransformerFactory fábrica = TransformerFactory.newInstance (); TransformerFactory factory = TransformerFactory.newInstance (); 
Transformador = factory.newTransformer (nuevo StreamSource ( Transformer transformer = factory.newTransformer (new StreamSource ( 
xsltfile)); xsltfile)); 
Fuente src = new StreamSource (archivo_xml); Source src = new StreamSource (xmlfile); 
res Resultado = StreamResult nueva (foOut); Result res = new StreamResult (foOut); 
transformer.transform (resolución src); transformer.transform (src, res); 
foOut.close (); foOut.close (); 

3.2 con JFOR convertir RTF a la 3.2 with JFor convert RTF to fo 

Serlvet sólo tiene que conseguir como un ejemplo: Serlvet only needs to achieve as an example: 

InputStream foInput = new FileInputStream (fofile); InputStream foInput = new FileInputStream (fofile); 
InputSource InputSource = new InputSource (foInput); InputSource inputSource = new InputSource (foInput); 

a cabo ByteArrayOutputStream = new ByteArrayOutputStream (); ByteArrayOutputStream out = new ByteArrayOutputStream (); 
Escritor de salida = new OutputStreamWriter (fuera); Writer output = new OutputStreamWriter (out); 

response.setContentType ("application / pdf"); response.setContentType ( "application / msword"); 

nuevo convertidor (InputSource, la producción, Converter.createConverterOption ()); new Converter (inputSource, output, Converter.createConverterOption ()); 
output.flush (); output.flush (); 

bytes de contenido] [= out.toByteArray (); byte [] content = out.toByteArray (); 

System.out.println (out.toString ()); System.out.println (out.toString ()); 

response.setContentLength (content.length); response.setContentLength (content.length); 
response.getOutputStream (). response.getOutputStream (). escribir (contenido); write (content); 
response.getOutputStream (). response.getOutputStream (). flush (); flush (); 

foInput.close (); foInput.close (); 
salida.close (); output.close (); 
out.close (); out.close (); 


Así que hemos logrado convertirse en el documento en formato RTF xml. So that we successfully converted into RTF format xml document. 

*/
	
	
	
	
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
	public File generarInforme(HttpServletRequest request, String rutaServidorTmp, String contenidoPlantilla, String rutaServidorDescargas, String nombreFicheroPDF) throws ClsExceptions , SIGAException
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
//			Plantilla plantilla = new Plantilla(this.usuario);
			ClsLogging.writeFileLog("ANTES DE GENERAR EL PDF.",10);
			this.convertFO2PDF(ficheroFOP, ficheroPDF, rutaTmp.getParent());
			ClsLogging.writeFileLog("PDF GENERADO.",10);
			
		} catch (SIGAException e){
			throw e;
		}catch (Exception e){
			throw new ClsExceptions(e, "Error al generar el informe: "+e.getLocalizedMessage());
		}  finally {
			if (ficheroFOP!=null && ficheroFOP.exists()) {
				ficheroFOP.delete();
			}
			if (rutaTmp!=null && rutaTmp.exists()) {
				rutaTmp.delete();
			}
			
		}
		return ficheroPDF;
	}
	public File getInformeGenericoFo (AdmInformeBean beanInforme,
			Hashtable htDatosInforme, String idiomaExt, String nombreFileOut,
			UsrBean usr) throws SIGAException, ClsExceptions{
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")
			+ rp.returnProperty("informes.directorioPlantillaInformesJava");
		String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
			+ rp.returnProperty("informes.directorioPlantillaInformesJava");

// MODELO DE TIPO FO:
		String carpetaInstitucion = "";
		if(beanInforme.getIdInstitucion()==null || beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0))==0){
			carpetaInstitucion = "2000";
		}else{
			carpetaInstitucion = ""+beanInforme.getIdInstitucion();
		}
		String rutaPlantillaInstitucion = rutaPlantilla + ClsConstants.FILE_SEP
			+ carpetaInstitucion + ClsConstants.FILE_SEP
				+ beanInforme.getDirectorio() + ClsConstants.FILE_SEP;
		String nombrePlantilla = beanInforme.getNombreFisico() + "_"
			+ idiomaExt + ".fo";
		String rutaAlm = rutaAlmacen + ClsConstants.FILE_SEP
			+ carpetaInstitucion + ClsConstants.FILE_SEP
				+ beanInforme.getDirectorio();

		UtilidadesHash.set(htDatosInforme,"RUTA_LOGO",rutaPlantillaInstitucion+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"Logo.jpg");
		String contenidoPlantilla = obtenerContenidoPlantilla(rutaPlantillaInstitucion,nombrePlantilla);
		File fPdf = generarInforme(usr,htDatosInforme,rutaAlm,contenidoPlantilla,rutaAlm,beanInforme.getNombreSalida()+"_"+nombreFileOut);
		return fPdf;

	}
	
	
	/**
	 * Este método se debe sobreescribir para reemplazar los valores en las plantillas FO
	 * @param request Objeto HTTPRequest
	 * @param plantillaFO Plantilla FO con parametros 
	 * @return Plantilla FO en donde se han reemplazado los parámetros
	 * @throws ClsExceptions
	 */
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions,SIGAException{
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
	public static File doZip(List<File> array, String rutaFinal) throws ClsExceptions
	{
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		try {
			if ((array!=null) && (array.size()>0)) {
				
				ficZip = new File(rutaFinal+".zip");
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<array.size(); i++)
				{
					File auxFile = (File)array.get(i);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(auxFile.getName());
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						outTemp.flush();
						fis.close();
						outTemp.closeEntry();
						auxFile.delete();
					}
				}
				
				outTemp.close();
			}
		 
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} finally {
		    try {
		        outTemp.close();
		    } catch (Exception eee) {}
		}
		
		return ficZip;
	}
	 public void convertFO2PDF(File fo, File pdf) 
	    throws IOException, FOPException, ClsExceptions
	    {
	    	convertFO2PDF(fo, pdf, fo.getPath());
	    }
	    
	   public synchronized  void convertFO2PDF(File fo, File pdf, String sBaseDir) 
	    throws IOException, FOPException, ClsExceptions
	    {
	    	OutputStream out = null;
	    	FileOutputStream fileOut = null;
	    	
	        try {
	        	    		
	        	ClsLogging.writeFileLog(">>> baseDir:" + sBaseDir,10);
	        	
	            org.apache.fop.configuration.Configuration.put("baseDir", sBaseDir);
	            org.apache.fop.configuration.Configuration.put("fontBaseDir", SIGAReferences.getReference(SIGAReferences.RESOURCE_FILES.FOP_DIR.getFileName()));
//	            new Options(new File(ClsConstants.FOP_CONFIG_FILE));
	            new Options(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.FOP));

	            ClsLogging.writeFileLog(">>> org.apache.fop.configuration.Configuration",10);
	            // Construct driver and setup output format
	            Driver driver = new Driver();
	            ClsLogging.writeFileLog(">>> Driver driver",10);
	            
	            driver.setRenderer(Driver.RENDER_PDF);
	            ClsLogging.writeFileLog(">>> driver.setRenderer",10);
	            
	    		ClsLogging.writeFileLog("CONVERT: driver creado.",10);
	            
	            // Setup output stream.  Note: Using BufferedOutputStream
	            // for performance reasons (helpful with FileOutputStreams).
	            fileOut = new FileOutputStream(pdf);
	            out = new BufferedOutputStream(fileOut);
	            driver.setOutputStream(out);

	    		ClsLogging.writeFileLog("CONVERT: stream creado.",10);

	            // Setup JAXP using identity transformer
	            TransformerFactory factory = TransformerFactory.newInstance();
	            //System.out.println(factory.getClass().getName());
	            
	            Transformer transformer = factory.newTransformer(); // identity transformer

	    		ClsLogging.writeFileLog("CONVERT: transformer creado.",10);
	            
	            // Setup input stream
	            Source src = new StreamSource(fo);
	            //Source src = new StreamSource(foFormateado);

	    		ClsLogging.writeFileLog("CONVERT: source creado.",10);

	            // Resulting SAX events (the generated FO) must be piped through to FOP
	            Result res = new SAXResult(driver.getContentHandler());

	    		ClsLogging.writeFileLog("CONVERT: result creado.",10);

	            // Start XSLT transformation and FOP processing
	            transformer.transform(src, res);
	            
	    		ClsLogging.writeFileLog("CONVERT: PDF creado.",10);
	            
	            //Borramos la cache de ficheros:
	            org.apache.fop.image.FopImageFactory.resetCache();
	    		ClsLogging.writeFileLog("CONVERT: cache borrada. TODO OK.",10);
	    		ClsLogging.writeFileLog("CONVERT: El fichero se almacenara en "+pdf,10);

	        } 
	        catch (Exception e) {
	        	
	        	    ClsLogging.writeFileLogError("Error transformando PDF desde FOP - Fichero:"+sBaseDir+" - Mensaje:" +e.getLocalizedMessage(),e,3);
	        
	            throw new ClsExceptions (e, "Error transformando PDF desde FOP - Fichero:"+sBaseDir+" - Mensaje:" +e.getLocalizedMessage());
	        } 
	        finally {
	        	try {
		            out.close();
		            fileOut.close();
	        	} catch (Exception e) {}
	        }
	    }
	   
	
	   
	   public static ByteArrayOutputStream convertXML2PDF(InputStream xml, InputStream xslt) throws IOException, FOPException, TransformerException{
		   Driver driver = new Driver();

//			Setup logger
			Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
			driver.setLogger(logger);
			MessageHandler.setScreenLogger(logger);

//			Setup Renderer (output format)        
			driver.setRenderer(Driver.RENDER_PDF);
//			Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			try {
				driver.setOutputStream(outStream);


//				Setup XSLT
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(new StreamSource(xslt));
//				Setup input for XSLT transformation
				Source src = new StreamSource(xml);
				

//				Resulting SAX events (the generated FO) must be piped through to FOP
				Result res = new SAXResult(driver.getContentHandler());

//				Start XSLT transformation and FOP processing
				transformer.transform(src, res);
			} finally {
				outStream.close();
			}

			return outStream;


		}
	   public static InputStream getInputStream(String texto, String encoding) throws IOException{
			return new ByteArrayInputStream(texto.getBytes(encoding));
		}
	   
	   public static String convertXML2HTML(InputStream xmlOrigen, InputStream xslOrigen) throws TransformerException 
	    {
	        Source xmlSource = new StreamSource(xmlOrigen);
	        Source xsltSource = new StreamSource(xslOrigen);
	        StringWriter cadenaSalida = new StringWriter();
	        Result bufferResultado = new StreamResult(cadenaSalida);
	        TransformerFactory factoriaTrans = TransformerFactory.newInstance();
	        Transformer transformador = null;;
			transformador = factoriaTrans.newTransformer(xsltSource);
			transformador.transform(xmlSource, bufferResultado);
			return cadenaSalida.toString();
	    }
	   
	   public static String convertXML2HTML(InputStream xmlOrigen, File xslOrigen) throws TransformerException 
	    {
	        Source xmlSource = new StreamSource(xmlOrigen);
	        Source xsltSource = new StreamSource(xslOrigen);
	        StringWriter cadenaSalida = new StringWriter();
	        Result bufferResultado = new StreamResult(cadenaSalida);
	        TransformerFactory factoriaTrans = TransformerFactory.newInstance();
	        Transformer transformador = null;;
			transformador = factoriaTrans.newTransformer(xsltSource);
			transformador.transform(xmlSource, bufferResultado);
			return cadenaSalida.toString();
	    }
	   public static String convertXML2HTML(File xmlOrigen, File xslOrigen) throws TransformerException 
	    {
	        Source xmlSource = new StreamSource(xmlOrigen);
	        Source xsltSource = new StreamSource(xslOrigen);
	        StringWriter cadenaSalida = new StringWriter();
	        Result bufferResultado = new StreamResult(cadenaSalida);
	        TransformerFactory factoriaTrans = TransformerFactory.newInstance();
	        Transformer transformador = null;;
			transformador = factoriaTrans.newTransformer(xsltSource);
			transformador.transform(xmlSource, bufferResultado);
			return cadenaSalida.toString();
	    }
	   
	/**
	 * 
	 * @param xml
	 * @param xslt
	 * @return
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	/*
	public static ByteArrayOutputStream convertXML2PDF_095(InputStream xml, InputStream xslt) throws IOException, FOPException, TransformerException{


		// the XSL FO file
		//File xsltfile = new File("HelloWorld.xsl");
		// the XML file from which we take the name
		//StreamSource source = new StreamSource(new File("Hello.xml"));
		Source source = new StreamSource(xml);
		// creation of transform source

		StreamSource transformSource = new StreamSource(xslt);
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// to store output
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		Transformer xslfoTransformer;

		TransformerFactory factory = TransformerFactory.newInstance();
		//xslfoTransformer =  getTransformer(transformSource); 
		xslfoTransformer = factory.newTransformer(transformSource);
		// Construct fop with desired output format
		Fop fop;

		fop = fopFactory.newFop	(MimeConstants.MIME_PDF, foUserAgent, outStream);
		// Resulting SAX events (the generated FO) 
		// must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing

		// everything will happen here..
		xslfoTransformer.transform(source, res);
	

		return outStream;


	}
	*/
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
	
	/*protected File convertXML2PDF_095(InputStream xml, InputStream xslt, File pdf,Map<String, String> mapParameters)
	throws IOException, FOPException, TransformerException {

		Source source = new StreamSource(xml);
		// creation of transform source

		StreamSource transformSource = new StreamSource(xslt);
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// to store output
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		Transformer xslfoTransformer;

		TransformerFactory factory = TransformerFactory.newInstance();
		//xslfoTransformer =  getTransformer(transformSource); 
		xslfoTransformer = factory.newTransformer(transformSource);
		
		for (Map.Entry<String, String> entrada:mapParameters.entrySet()){
			
			xslfoTransformer.setParameter(entrada.getKey(), entrada.getValue());
		}
		
		// Construct fop with desired output format
		Fop fop;

		fop = fopFactory.newFop	(MimeConstants.MIME_PDF, foUserAgent, outStream);
		// Resulting SAX events (the generated FO) 
		// must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing

		// everything will happen here..
		xslfoTransformer.transform(source, res);
		// if you want to get the PDF bytes, use the following code
		//return outStream.toByteArray();

		// if you want to save PDF file use the following code

//		OutputStream out = new java.io.FileOutputStream(pdfFile);
//		out = new java.io.BufferedOutputStream(out);
		FileOutputStream str = new FileOutputStream(pdf);
		str.write(outStream.toByteArray());
		str.close();
		outStream.close();

		// to write the content to out put stream
//		byte[] pdfBytes = outStream.toByteArray();
//	                        response.setContentLength(pdfBytes.length);
//	                        response.setContentType("application/pdf");
//	                        response.addHeader("Content-Disposition", 
//						"attachment;filename=pdffile.pdf");
//	                        response.getOutputStream().write(pdfBytes);
//	                        response.getOutputStream().flush();


		return pdf;
	}
	*/
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
	/*private File convertXML2PDF_095(File xml, File xslt, File pdf)
	throws IOException, FOPException, TransformerException {
		Source source = new StreamSource(xml);
		// creation of transform source

		StreamSource transformSource = new StreamSource(xslt);
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// to store output
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		Transformer xslfoTransformer;

		TransformerFactory factory = TransformerFactory.newInstance();
		//xslfoTransformer =  getTransformer(transformSource); 
		xslfoTransformer = factory.newTransformer(transformSource);
		
		
		
		// Construct fop with desired output format
		Fop fop;

		fop = fopFactory.newFop	(MimeConstants.MIME_PDF, foUserAgent, outStream);
		// Resulting SAX events (the generated FO) 
		// must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing

		// everything will happen here..
		xslfoTransformer.transform(source, res);
		// if you want to get the PDF bytes, use the following code
		//return outStream.toByteArray();

		// if you want to save PDF file use the following code

//		OutputStream out = new java.io.FileOutputStream(pdfFile);
//		out = new java.io.BufferedOutputStream(out);
		FileOutputStream str = new FileOutputStream(pdf);
		str.write(outStream.toByteArray());
		str.close();
		outStream.close();

		// to write the content to out put stream
//		byte[] pdfBytes = outStream.toByteArray();
//	                        response.setContentLength(pdfBytes.length);
//	                        response.setContentType("application/pdf");
//	                        response.addHeader("Content-Disposition", 
//						"attachment;filename=pdffile.pdf");
//	                        response.getOutputStream().write(pdfBytes);
//	                        response.getOutputStream().flush();


		return pdf;
	}*/
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
	/*private File convertXML2PDF_095(InputStream xml, InputStream xslt, File pdf)
	throws IOException, FOPException, TransformerException {
		Source source = new StreamSource(xml);
		// creation of transform source

		StreamSource transformSource = new StreamSource(xslt);
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// to store output
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		Transformer xslfoTransformer;

		TransformerFactory factory = TransformerFactory.newInstance();
		//xslfoTransformer =  getTransformer(transformSource); 
		xslfoTransformer = factory.newTransformer(transformSource);
		
		
		
		// Construct fop with desired output format
		Fop fop;

		fop = fopFactory.newFop	(MimeConstants.MIME_PDF, foUserAgent, outStream);
		// Resulting SAX events (the generated FO) 
		// must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing

		// everything will happen here..
		xslfoTransformer.transform(source, res);
		// if you want to get the PDF bytes, use the following code
		//return outStream.toByteArray();

		// if you want to save PDF file use the following code

//		OutputStream out = new java.io.FileOutputStream(pdfFile);
//		out = new java.io.BufferedOutputStream(out);
		FileOutputStream str = new FileOutputStream(pdf);
		str.write(outStream.toByteArray());
		str.close();
		outStream.close();

		// to write the content to out put stream
//		byte[] pdfBytes = outStream.toByteArray();
//	                        response.setContentLength(pdfBytes.length);
//	                        response.setContentType("application/pdf");
//	                        response.addHeader("Content-Disposition", 
//						"attachment;filename=pdffile.pdf");
//	                        response.getOutputStream().write(pdfBytes);
//	                        response.getOutputStream().flush();


		return pdf;
	}*/
	
	/*protected File convertXML2PDF_095(InputStream xml, File xslt, File pdf)
	throws IOException, FOPException, TransformerException {
		return convertXML2PDF_095(xml, new FileInputStream(xslt),pdf);
		
	}*/
	/*protected File convertXML2PDF_095(InputStream xml, File xslt, File pdf,Map<String, String> mapParameters)
	throws IOException, FOPException, TransformerException {
		InputStream inputXslt = new FileInputStream(xslt);
		return convertXML2PDF_095(xml,inputXslt ,pdf,mapParameters);
		
	}*/
	/*public File generarInformePdfFromXmlToFoXsl_095(String pathXml,String xslDir,String xslName,
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

			convertXML2PDF_095(xmlFile, xsltFile, pdfFile);
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

	}*/
	
	/**
     * Converts an FO file to a PDF file using FOP
     * @param fo the FO file
     * @param pdf the target PDF file
     * @throws IOException In case of an I/O problem
     * @throws FOPException In case of a FOP problem
     */
  /* public void convertFO2PDF_095(File fo, File pdf) 
    throws IOException, FOPException, ClsExceptions
    {
    	convertFO2PDF_095(fo, pdf, fo.getPath());
    }*/
    /*
    public synchronized  void convertFO2PDF_095(File fo, File pdf, String sBaseDir) 
    throws IOException, FOPException, ClsExceptions
    {
    	ClsLogging.writeFileLog(">>> StreamSource",10);
    	Source source = new StreamSource(fo);
		// creation of transform source

		
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance();
		// a user agent is needed for transformation
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// to store output
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		Transformer xslfoTransformer = null;

		TransformerFactory factory = TransformerFactory.newInstance();
		//xslfoTransformer =  getTransformer(transformSource); 
		try {
			xslfoTransformer = factory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// Construct fop with desired output format
		Fop fop;

		fop = fopFactory.newFop	(MimeConstants.MIME_PDF, foUserAgent, outStream);
		// Resulting SAX events (the generated FO) 
		// must be piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Start XSLT transformation and FOP processing

		// everything will happen here..
		try {
			xslfoTransformer.transform(source, res);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if you want to get the PDF bytes, use the following code
		//return outStream.toByteArray();

		// if you want to save PDF file use the following code

//		OutputStream out = new java.io.FileOutputStream(pdfFile);
//		out = new java.io.BufferedOutputStream(out);
		FileOutputStream str = new FileOutputStream(pdf);
		str.write(outStream.toByteArray());
		str.close();
		outStream.close();
		ClsLogging.writeFileLog("CONVERT: cache borrada. TODO OK.",10);
		ClsLogging.writeFileLog("CONVERT: El fichero se almacenara en "+pdf,10);
		// to write the content to out put stream
		


    
    }
    */
	
}
