package com.siga.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;

import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesString;

/**
 * Descarga un fichero deseado.
 */

public class SIGASvlDownloadFile extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doWork(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doWork(request, response);
    }
    
    public void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String accion = "";
    	if (request.getAttribute("accion") == null) {
            boolean isMultipart = FileUpload.isMultipartContent(request);
            if (isMultipart) accion="subir";
    	} else {
    		accion=(String)request.getAttribute("accion");
    	}
    	if(accion==null || accion.equals("")){
    		descargarFichero(request,response);
    	}
    }
    
    protected void descargarFichero(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	OutputStream out = null;
    	InputStream in= null;
        try
        {
        	
	    	String sNombreFichero = (String)request.getAttribute("nombreFichero");
	    	String sRutaFichero = (String)request.getAttribute("rutaFichero");

	    	// El nombre del fichero y la ruta vienen por parametros
	    	if (sNombreFichero == null) {
		    	sNombreFichero = (String)request.getParameter("nombreFichero");
	    	}
	    	if (sRutaFichero == null) {
	    		sRutaFichero = (String)request.getParameter("rutaFichero");
	    	}
	    	
	    	// RGG 30/03/2009 cambio para traducri un fichero con espacios.
	    	sRutaFichero = UtilidadesString.replaceAllIgnoreCase(sRutaFichero,"+"," ");
	    	
	    	// El indicador de borrar fichero puede llegar como parametro o como atributo 
	        boolean borrarFichero = obtenerBooleanAttrParam(request, "borrarFichero");
	        boolean borrarDirectorio = obtenerBooleanAttrParam(request, "borrarDirectorio");
	        		
	        response.setContentType("application/download");	        		
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + sNombreFichero + "\"");
	
	        ClsLogging.writeFileLog("SERVLET DESCARGA > Ruta fichero a descargar: " + sRutaFichero, request, 10);
	
	        byte[] buffer = new byte[1024];
	        int len;
	        out = response.getOutputStream();
	
	        in= new FileInputStream(sRutaFichero);
	
	        while((len = in.read(buffer, 0, 1024)) != -1)
	        {
	        	out.write(buffer, 0, len);
	        }
	       
	        ClsLogging.writeFileLog("SERVLET DESCARGA > OK ", request, 10);
	        File directorio = null;
	        if(borrarFichero){
	        	ClsLogging.writeFileLog("SERVLET DESCARGA > Se va a borrar el fichero de la ruta "+sRutaFichero, request, 10);
	            cerrar(out, in);
	        	File f= new File(sRutaFichero);
	        	directorio = f.getParentFile();
	        	if (f.delete())
	        		ClsLogging.writeFileLog("SERVLET DESCARGA > Fichero borrado por peticion de la ruta "+sRutaFichero, request, 10);
	        	else
	        		ClsLogging.writeFileLog("SERVLET DESCARGA > NO se ha borrado el fichero en la ruta "+sRutaFichero, request, 10);
	        	
	        }
	        if(borrarDirectorio){
	        	ClsLogging.writeFileLog("SERVLET DESCARGA > Se va a borrar el directorio de la ruta "+sRutaFichero, request, 10);
	            cerrar(out, in);
	        	File f= new File(sRutaFichero).getParentFile();
		        if (UtilidadesFicheros.deleteDir(f))
		        	ClsLogging.writeFileLog("SERVLET DESCARGA > Directorio borrado por peticion de la ruta "+f.getPath(), request, 10);
		        else
		        	ClsLogging.writeFileLog("SERVLET DESCARGA > No se ha borrado el directorio en la ruta "+f.getPath(), request, 10);
	        }else{
	        	if(directorio !=null && directorio.list().length==0){
	        		cerrar(out, in);
	        		if(directorio.delete())
	        			ClsLogging.writeFileLog("SERVLET DESCARGA > Directorio borrado por estar vacio de la ruta "+directorio.getPath(), request, 10);
	        		else
			        	ClsLogging.writeFileLog("SERVLET DESCARGA > No se ha borrado el directorio en la ruta "+directorio.getPath(), request, 10);
	        	}
	        	
	        }
	        
	        
        }
        
        catch(Exception e)
        {
        	
        	ClsLogging.writeFileLogError("Error al descargar fichero: " + e.toString() ,e, 1);
        	throw new ServletException("Error al descargar el fichero. " + e);
        }finally{
            cerrar(out, in);
        }


    	
    	
    }

	private void cerrar(OutputStream out, InputStream in) throws IOException {
		if(in !=null){
		        in.close();
		        in = null;
		}

		if(out!=null){
		        out.flush();
		        out.close();
		        out = null;

		}
	}
	
	
	/**
	 * Devuelve <code>true</code> si el attributo o parámetro <code>clave</code> vale true, <code>false</code> en caso contrario.
	 * @param clave Parameter o Attribute a comprobar
	 * @return <code>true</code> si el attributo o parámetro <code>clave</code> vale true, <code>false</code> en caso contrario.
	 */
	private boolean obtenerBooleanAttrParam(HttpServletRequest request, String clave){
		String parameter =(String)request.getParameter(clave);
		if(parameter==null){
			String attribute =(String)request.getAttribute(clave);
			return (attribute!=null && attribute.equalsIgnoreCase("true"));
		}else{
			return parameter.equalsIgnoreCase("true");
		}
	}
	
}