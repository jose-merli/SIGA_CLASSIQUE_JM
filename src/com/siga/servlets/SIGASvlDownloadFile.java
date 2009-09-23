package com.siga.servlets;

import java.io.*;

import javax.servlet.*;

import com.atos.utils.*;
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
            //System.out.println(isMultipart);
            if (isMultipart) accion="subir";
    	} else {
    		accion=(String)request.getAttribute("accion");
    	}
    	//System.out.println("accion:"+accion);
    	if(accion==null || accion.equals("")){
    		descargarFichero(request,response);
    		
    	}else{
    		uploadFichero2(request,response);
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
	        String pBorrarFichero =(String)request.getParameter("borrarFichero");
	        boolean borrar=false;
	        if(pBorrarFichero==null){
		        String aBorrarFichero =(String)request.getAttribute("borrarFichero");
		        borrar=(aBorrarFichero!=null && aBorrarFichero.equalsIgnoreCase("true"));
	        }else{
	        	borrar=pBorrarFichero.equalsIgnoreCase("true");
	        }
	        		
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
	       

	        //response.flushBuffer();

	        ClsLogging.writeFileLog("SERVLET DESCARGA > OK ", request, 10);
	        
	        if(borrar){
	        	ClsLogging.writeFileLog("SERVLET DESCARGA > Se va a borrar el fichero de la ruta "+sRutaFichero, request, 10);
	        	File f= new File(sRutaFichero);
	        	f.delete();
		        ClsLogging.writeFileLog("SERVLET DESCARGA > Fichero borrado por peticion de la ruta "+sRutaFichero, request, 10);
	        }
	        
	        
        }
        
        catch(Exception e)
        {
        	
        	ClsLogging.writeFileLogError("Error al descargar fichero: " + e.toString() ,e, 1);
        	throw new ServletException("Error al descargar el fichero. " + e);
        }finally{

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


    	
    	
    }
    
//    public void uploadFichero(HttpServletRequest req, HttpServletResponse res)
//    throws ServletException, IOException {
//    	InputStream inStream = null;
//    	ObjectOutputStream outStream = null;
//    	String sOut = "";
//    	File inFile= null;
//    	boolean subido = false;
//    	String sRutaFichero = "";
//    	try {
//    		/* inStream = req.getInputStream(); */
//    		
//    	    
//    	    
//    		String filePath = (String)req.getAttribute("nombreFichero");
//	    	sRutaFichero = (String)req.getAttribute("rutaFichero");
//
//	    	if (filePath == null) {
//		    	filePath = (String)req.getParameter("nombreFichero");
//	    	}
//	    	if (sRutaFichero == null) {
//	    		sRutaFichero = (String)req.getParameter("rutaFichero");
//	    	}
//	    	
//	    	if (!(filePath.equals(""))){
//	    		// Comprueba que se haya introducido un nombre valido antes de intentar subirlo
//		    	String fileName = filePath.substring(filePath.lastIndexOf("\\"));
//		    	ClsLogging.writeFileLogError("Upload Servlet: fichero: "+filePath, req, 1);
//		    	inStream =new FileInputStream(filePath);
//		    	sOut = writeFile(inStream, sRutaFichero, fileName, req);
//		    	subido = true;
//		    	
//	    	}else{
//	    		ClsLogging.writeFileLogError("Upload Servlet: Error: \n" + "No hay archivos para subir", req, 1);
//	    	}
//    	} catch (IOException ioe) {
//    		ClsLogging.writeFileLogError("Upload Servlet: I/O Error: \n" + ioe.getMessage(), req, 1);
//    	} catch (Exception e) {
//    		ClsLogging.writeFileLogError("Upload Servlet: General Error: \n" + e.getMessage(), req, 1);
//    	}
//    	finally {
//    		if (subido) inStream.close();
//    		
//    		/* El siguiente codigo permite retornar a infoDirectorio en el directorio en el que se ha añadido
//    		   el archivo*/
//    		req.getSession().setAttribute("mensajeOK", "Fichero grabado");
//	    	req.getSession().setAttribute("path", sRutaFichero);
//	    	String app=req.getContextPath();
//	    	res.sendRedirect(app+"/html/jsp/general/infoDirectorio.jsp");
//    	}
//    }
    
    private String writeFile(InputStream inStream, String path, String fileName, HttpServletRequest req) {
    	int BUFFER_SIZE = 8 * 1024;
    	String result = "";
        //ReadProperties rp=new ReadProperties("Upload.properties");

        path += ClsConstants.FILE_SEP+fileName;
        File targetFile = new File(path);
        ClsLogging.writeFileLog("Uploading file: "+targetFile.getPath(),req,1);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(targetFile);
            int bytesRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            result = targetFile.getPath();
            outStream.close();
        } catch (IOException ex) {
            try {
                outStream.close();
            } catch (Exception eee) {}
            ClsLogging.writeFileLogError("Exception thrown while uploading files: " + ex.getMessage(),req,1);
        }
        return result;
    }
    
    
    public void uploadFichero2(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
    
	    InputStream inStream = null;
	    ObjectOutputStream outStream = null;
	    String sOut = "";
	    File inFile= null;
	    String newFolder = "";
        String fileName = "";
        try{

            FileUpload fup=new FileUpload();
            boolean isMultipart = FileUpload.isMultipartContent(req);
            //System.out.println(isMultipart);
            DiskFileUpload upload = new DiskFileUpload();

            List items = upload.parseRequest(req);

            Iterator iter = items.iterator();
            while (iter.hasNext()) {

	            FileItem item = (FileItem) iter.next();

	            if (item.isFormField()) {
	                //System.out.println("its a field="+item.getFieldName());
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