package com.siga.servlets;

import java.io.File;
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

public class SIGASvlInfoDirectorioFileManagement extends SIGASvlDownloadFile {
	 
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
    	}else{
    		uploadFichero(request,response);
    		/* La funcion descargarFichero contiene el borrado */
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
}
