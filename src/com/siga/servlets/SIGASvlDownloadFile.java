package com.siga.servlets;

import java.io.*;

import javax.servlet.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesString;

import javax.servlet.http.*;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
    
    public void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
    	String accion = "";
    	if (request.getAttribute("accion") == null) {
    		FileUpload fup=new FileUpload();
            boolean isMultipart = FileUpload.isMultipartContent(request);
            if (isMultipart) accion="subir";
    	} else {
    		accion=(String)request.getAttribute("accion");
    	}
    	if(accion==null || accion.equals("")){
    		descargarFichero(request,response);
    		
    	}else{
    		uploadFichero(request,response);
    		/* La funcion descargarFichero contiene el borrado */
    	}
    	
    }
    private void descargarFichero(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
	

    
    
    
    public void uploadFichero(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
	    String newFolder = "";
        String fileName = "";
        try{

            FileUpload fup=new FileUpload();
            boolean isMultipart = FileUpload.isMultipartContent(req);
            DiskFileUpload upload = new DiskFileUpload();

            List items = upload.parseRequest(req);

            Iterator iter = items.iterator();
            while (iter.hasNext()) {

	            FileItem item = (FileItem) iter.next();

	            if (item.isFormField()) {
	                if (item.getFieldName().equals("nombreFichero")) {
	                    fileName = item.getString(); 
	                } else
	                if (item.getFieldName().equals("rutaFichero")) {
	                    newFolder = item.getString(); 
	                }
	            } else {

	                File cfile=new File(item.getName());
	                String fileName2 = fileName.substring(fileName.lastIndexOf("\\")+1);
	                ClsLogging.writeFileLog("Upload Servlet: fichero: "+newFolder+File.separator+fileName2, req, 8);
	                
			    	File tosave=new File(newFolder+File.separator+fileName2);
			    	if (!tosave.isDirectory()) {
			    	    item.write(tosave);
			    	}
			        
	            }
            }

   	    } catch (IOException ioe) {
	       ClsLogging.writeFileLogError("Upload Servlet: I/O Error: \n" + ioe.getMessage(),ioe, 3);
	    } catch (Exception e) {
	        ClsLogging.writeFileLogError("Upload Servlet: General Error: \n" + e.getMessage(),e, 3);
	    }
	    finally {
	        /* El siguiente codigo permite retornar a infoDirectorio en el directorio en el que se ha añadido
			   el archivo*/
			req.getSession().setAttribute("mensajeOK", "Fichero grabado");
		 	req.getSession().setAttribute("path", newFolder);
		 	String app=req.getContextPath();
		 	res.sendRedirect(app+"/html/jsp/general/infoDirectorio.jsp");
	    }
    }	    
}