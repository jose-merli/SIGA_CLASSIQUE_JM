package com.siga.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesString;

public class SIGASvlInfoDirectorioFileManagement extends SIGASvlDownloadFile {
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = 3911680330771584283L;

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
    	}else if(accion.equals("borrar")){
    		deleteFichero(request,response);
    		/* La funcion descargarFichero contiene el borrado */
    	}else{
    		uploadFichero(request,response);
    		/* La funcion descargarFichero contiene el borrado */
    	}
	 }
	 
	 public void uploadFichero(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
		    String newFolder = "";
	        String fileName = "";
	        try{

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
		                ClsLogging.writeFileLog("InfoDirectorio Upload Servlet: fichero: "+newFolder+File.separator+fileName2, req, 8);
		                
				    	File tosave=new File(newFolder+File.separator+fileName2);
				    	if (!tosave.isDirectory()) {
				    	    item.write(tosave);
				    	}
				        
		            }
	            }

	   	    } catch (IOException ioe) {
		       ClsLogging.writeFileLogError("InfoDirectorio Upload Servlet: I/O Error: \n" + ioe.getMessage(),ioe, 3);
		    } catch (Exception e) {
		        ClsLogging.writeFileLogError("InfoDirectorio Upload Servlet: General Error: \n" + e.getMessage(),e, 3);
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
	 
	 public void deleteFichero (HttpServletRequest request, HttpServletResponse res)	throws ServletException, IOException{
		
		
        	
    	String sNombreFichero = (String)request.getAttribute("nombreFichero");
    	String sRutaFichero = (String)request.getAttribute("rutaFichero");

    	// El nombre del fichero y la ruta vienen por parametros
    	if (sNombreFichero == null) {
	    	sNombreFichero = (String)request.getParameter("nombreFichero");
    	}
    	if (sRutaFichero == null) {
    		sRutaFichero = (String)request.getParameter("rutaFichero");
    	}
    	
	    try
        {
	    	// traducri un fichero con espacios.
	    	sRutaFichero = UtilidadesString.replaceAllIgnoreCase(sRutaFichero,"+"," ");
	    	
	        File directorio = null;
	       
	        ClsLogging.writeFileLog("SERVLET DESCARGA > Se va a borrar el fichero de la ruta "+sRutaFichero, request, 10);
	           
	        File f= new File(sRutaFichero);
	        directorio = f.getParentFile();
	        if (f.delete())
	        	ClsLogging.writeFileLog("SERVLET DESCARGA > Fichero borrado por peticion de la ruta "+sRutaFichero, request, 10);
	        else
	        	ClsLogging.writeFileLog("SERVLET DESCARGA > NO se ha borrado el fichero en la ruta "+sRutaFichero, request, 10);
	 
        	if(directorio !=null && directorio.list().length==0){
        	
        		if(directorio.delete())
        			ClsLogging.writeFileLog("SERVLET DESCARGA > Directorio borrado por estar vacio de la ruta "+directorio.getPath(), request, 10);
        		else
		        	ClsLogging.writeFileLog("SERVLET DESCARGA > No se ha borrado el directorio en la ruta "+directorio.getPath(), request, 10);
        	}
	        	
	    } catch (Exception e) {
	        ClsLogging.writeFileLogError("InfoDirectorio Delete Servlet: General Error: \n" + e.getMessage(),e, 3);
	    }
	    finally {
	        /* El siguiente codigo permite retornar a infoDirectorio en el directorio en el que se ha añadido
			   el archivo*/
			request.getSession().setAttribute("mensajeOK", "Fichero borrado");
			request.getSession().setAttribute("path", sRutaFichero);
		 	String app=request.getContextPath();
		 	res.sendRedirect(app+"/html/jsp/general/infoDirectorio.jsp");
	    }
	 }
}
