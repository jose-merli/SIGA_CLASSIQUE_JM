package com.siga.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;


public class SIGASvlUploadFiles extends SIGAServletAdapter {
	private static final long serialVersionUID = -102663719471049646L;
	private static int BUFFER_SIZE = 8 * 1024;   
    
	public void init() throws ServletException {
		super.init(this.getServletConfig());
	}
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        InputStream inStream = null;
        ObjectOutputStream outStream = null;
        String sOut = "";
//        File inFile= null;
        try {
            inStream = req.getInputStream();
            String fileName = "";
            String newFolder = "";
            if (req.getParameter("folder") != null) {
                newFolder = req.getParameter("folder");
            }
            if (req.getParameter("fileName") != null) {
                fileName = req.getParameter("fileName");
            }

            sOut = writeFile(inStream, newFolder, fileName, req);
            res.setContentType("application/x-java-serialized-object");
            outStream = new ObjectOutputStream(res.getOutputStream());
            outStream.writeObject(sOut);
            outStream.flush();
       } catch (IOException ioe) {
           ClsLogging.writeFileLogError("Upload Servlet: I/O Error: \n" + ioe.getMessage(), req, 1);
        } catch (Exception e) {
            ClsLogging.writeFileLogError("Upload Servlet: General Error: \n" + e.getMessage(), req, 1);
        }
        finally {
	        inStream.close();
	        outStream.close();
        }
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
		//System.out.println("/************ INIT ****************/");
		//System.out.println("/************ REFRESCÓ ****************/");
		
/*		String SEP_PATTERN="//";
		String initPropsFile=System.getProperty("slbutils.initprops");	
		java.util.ResourceBundle initProperties=java.util.ResourceBundle.getBundle(initPropsFile);
		String FILE_SEP = System.getProperty("file.separator");
		String REPLACE_PATTERN=(FILE_SEP.equals("\\"))?FILE_SEP+FILE_SEP:FILE_SEP;
		String USER_DIR = System.getProperty("user.dir");
		String RES_DIR = USER_DIR + FILE_SEP ;

		String APPLICATION_NAME = initProperties.getString("application.name");
		String DEFAULT_LANGUAGE= initProperties.getString("default.lenguage");
		String PATH_DOMAIN = initProperties.getString("application.domain");
		String RES_PROP_DOMAIN=initProperties.getString("path.resprop.domain");

		System.out.println(" * Property File -- " +initPropsFile); 
		System.out.println(" * ClsConstants.USER_DIR -- " + USER_DIR);
		System.out.println(" * ClsConstants.RES_PROP_DOMAIN -- " + RES_PROP_DOMAIN);
		System.out.println(" * OEOEOE ClsConstants.RES_PROP_DOMAIN -- " + RES_PROP_DOMAIN.replaceAll(SEP_PATTERN, REPLACE_PATTERN));
//		System.out.println(" * ClsConstants.RESOURCES_DIR_STRUTS -- " + RESOURCES_DIR_STRUTS);
//		System.out.println(" * ClsConstants.RESOURCES_DIR -- " + RESOURCES_DIR);
*/		

		System.out.println(" * Property File -- " +ClsConstants.initPropsFile); 
		System.out.println(" * ClsConstants.USER_DIR -- " + ClsConstants.USER_DIR);
		System.out.println(" * ClsConstants.RES_PROP_DOMAIN -- " + ClsConstants.RES_PROP_DOMAIN);
//		System.out.println(" * ClsConstants.RESOURCES_DIR_STRUTS -- " + ClsConstants.RESOURCES_DIR_STRUTS);
//		System.out.println(" * ClsConstants.RESOURCES_DIR -- " + ClsConstants.RESOURCES_DIR);
    	

//        doPost(req, res);
    }

    private String writeFile(InputStream inStream, String newFolder, String fileName, HttpServletRequest req) {
        String result = "";
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.UPLOAD);
//        ReadProperties rp=new ReadProperties("Upload.properties");
        String path=rp.returnProperty("UPLOAD.drive")+
                    ClsConstants.FILE_SEP+rp.returnProperty("path.path")+
                    ClsConstants.FILE_SEP+rp.returnProperty("UPLOAD.path")+
                    ClsConstants.FILE_SEP+newFolder;
        File parentFolder = new File(path);
        if (!parentFolder.exists()) {
            ClsLogging.writeFileLog("Creating folder: "+parentFolder.getPath(),req,1);
            parentFolder.mkdir();
        }
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
}