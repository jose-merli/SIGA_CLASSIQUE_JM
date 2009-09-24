package com.siga.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

/*
 * Created on 11-feb-2008
 *
 */

/**
 * @author danielc
 *
 */

public class ExportarSIGA 
{
	static String pathDestino;
	static String pathOrigen;
	static String entorno;
	
	public static void main(String[] args) 
	{
		if (!inicializacion(args))
			return;
		
		// Comprobamos si existe la carpeta origen
		File forigen = new File(pathOrigen);
		if (!forigen.exists()) {
			traza ("Error: No existe el path origen");
			traza ("Exportacion finalizada");
			traza ("----------------------------------------------");
			return;
		}

		try {
			// Creamos la estructura
			traza ("Creando y limpiando estructura de directorios ...");
			crearEstructura (pathDestino);
			
			// Copiamos los html
			traza ("Exportando HTML ...");
			copy (pathOrigen + File.separator + "WebContent" + File.separator + "html" , 
				  pathDestino + File.separator + "html" );
			
			// Copiamos los src
			traza ("Exportando SRC ...");
			copy (pathOrigen + File.separator + "src" , 
				  pathDestino + File.separator + "src" );
	
			// Copiamos los WEB-INF
			traza ("Exportando WEB-INF ...");
			copy (pathOrigen + File.separator + "WebContent" + File.separator + "WEB-INF" , 
				  pathDestino + File.separator + "WEB-INF" );
			
			// Copiamos las librerias
			traza ("Exportando LIB-WEB ...");
			copy (pathOrigen + File.separator + "WebContent" + File.separator + "WEB-INF" + File.separator + "lib", 
				  pathDestino + File.separator + "lib-web");
	
			// Borramos las clases iniciales
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "lib");
	
			// Borramos las clases compiladas
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "classes");
	
			// Copiamos las plantillas
			traza ("Exportando PLANTILLAS ...");
			copy (pathOrigen + File.separator + "plantillas" , 
				  pathDestino + File.separator + "plantillas");
	
			
			if (entorno.trim().equals("desarrollo")) {
			    
				// Copiamos el built
				traza ("Exportando ANT ...");
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "build.xml", 
					  pathDestino + File.separator + "ant" + File.separator + "build.xml");
		
				// Copiamos los ficheros de configuracion
				traza ("Exportando CONF ...");
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "CRConfig.xml", 
					  pathDestino + File.separator + "conf" + File.separator + "CRConfig.xml");
		
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "log4j.properties", 
						  pathDestino + File.separator + "conf" + File.separator + "log4j.properties");
		
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "pra.properties", 
						  pathDestino + File.separator + "conf" + File.separator + "pra.properties");
		
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "SIGA.properties", 
						  pathDestino + File.separator + "conf" + File.separator + "SIGA.properties");
				
				copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "weblogic.xml", 
						  pathDestino + File.separator + "conf" + File.separator + "weblogic.xml");
		    }

			// Copiamos los ficheros de configuracion
			traza ("Exportando web.xml ...");
			copy (pathOrigen + File.separator + "configuracion" + File.separator + entorno + File.separator + "web.xml", 
				  pathDestino + File.separator + "WEB-INF" + File.separator + "web.xml");
	
			
			// borramos estos ficheros para que al desplegar el ANT no de problemas 
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "properties" + File.separator + "log4j.properties");
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "properties" + File.separator + "pra.properties");
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "properties" + File.separator + "SIGA.properties");
			delete (pathDestino + File.separator + "WEB-INF" + File.separator + "weblogic.xml");
		}
		catch (Exception e) {
			traza ("Error: se ha producido un error en la exportacion");
			traza ("\n\n" + e.getMessage() + "\n\n");
		}

		traza ("\nExportacion finalizada");
		traza ("----------------------------------------------");
	}

	private static void crearEstructura (String path) 
	{
    	File f = new File (path);
		if (!f.exists()) { 
			f.mkdir(); 
		}
		if (entorno.trim().equals("desarrollo")) {
			
			f = new File (path + File.separator + "ant");
			crearDirectorio(f);
			
			f = new File (path + File.separator + "conf");
			crearDirectorio(f);
		}
		
		f = new File (path + File.separator + "html");
		delete(f);

		f = new File (path + File.separator + "src");
		delete(f);

		f = new File (path + File.separator + "plantillas");
		delete(f);

		f = new File (path + File.separator + "lib-web");
		crearDirectorio(f);

		f = new File (path + File.separator + "WEB-INF");
		delete(f);
	}
	
	private static void crearDirectorio (File f) 
	{
		if (f.exists()) {
			delete(f);
		}
		f.mkdir();
	}
	
	private static void copy (String origen, String destino) {
	
		try {
			File forigen = new File(origen);
			File fdestino = new File(destino);
			copy(forigen, fdestino);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copy(File srcDir, File dstDir) throws IOException 
	{ 
		if (!srcDir.getAbsolutePath().toUpperCase().endsWith("CVS")) {
	        if (srcDir.isDirectory()) { 
	            if (!dstDir.exists()) { 
	                dstDir.mkdir(); 
	            } 
	             
	            String[] children = srcDir.list(); 
	            for (int i=0; i<children.length; i++) { 
	                copy(new File(srcDir, children[i]), new File(dstDir, children[i])); 
	            } 
	        } 
	        else { 
	            copyFile(srcDir, dstDir); 
	        }
		}
    } 

	private static void copyFile(File src, File dst) throws IOException 
	{ 
        InputStream in = new FileInputStream(src); 
        OutputStream out = new FileOutputStream(dst); 
         
        byte[] buf = new byte[1024]; 
        int len; 
        while ((len = in.read(buf)) > 0) { 
            out.write(buf, 0, len); 
        } 
        in.close(); 
        out.close(); 
    } 

	public static void delete(String s) 
	{
		File f = new File (s);
		delete (f);
	}

	public static void delete(File dir) 
	{
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				delete(new File(dir, children[i]));
			}
		}
		dir.delete();
	}
	
	private static void traza (String m) {
		System.out.println (m);
	}
	
	private static boolean inicializacion (String[] args) 
	{
     	if (args == null || args[0] == null) {
			traza("Debe establecer el nombre del fichero de configuracion como parametro de entrada");
			return false;
		} 

		Properties properties = new Properties();
	    try {
	        properties.load(new FileInputStream(args[0]));
	    } catch (IOException e) {
	    	System.out.println(e.toString());
	    	return false;
        }
	    
	    boolean error = false;
    	pathOrigen  = (String)properties.getProperty("PATH_ORIGEN");
    	if (pathOrigen == null || pathOrigen.equals("")) {
    		traza ("Falta configurar el path origen");
    		error = true;
    	}
    	
    	pathDestino = (String)properties.getProperty("PATH_DESTINO");
    	if (pathDestino == null || pathDestino.equals("")) {
    		traza ("Falta configurar el path destino");
    		error = true;
    	}

    	entorno     = (String)properties.getProperty("ENTORNO");
    	if (entorno == null || entorno.equals("")) {
    		traza ("Falta configurar el entorno");
    		error = true;
    	}
    	if (error) 
    		return false;
    	
		traza ("----------------------------------------------");
		traza ("Entorno: " + entorno);
		traza ("Origen:  " + pathOrigen);
		traza ("Destino: " + pathDestino);
		traza ("Es correcto la informacion (Se perderan los archivos anteriores)? (s/n)");
		
	    InputStreamReader isr=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(isr);
	    String texto = "n";
		try {
			texto = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (texto.equalsIgnoreCase("s")) {
			traza ("");
        	return true;
        }
		traza ("Exporatacion cancelada");
		traza ("----------------------------------------------");
        return false;
	}
}
 