/**
 * <p>Title: UploadFile </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.struts.upload.FormFile;



/**
 * This class receives a FormFile and the path of this file to be placed in.
 * The argument filePath has got the name of the file too.
  */


public class UploadFile {

  private UploadFile(){
  }

  public static void upload(File uploadableFile, String folder) throws ClsExceptions{

      //    retrieve the file data
      FileInputStream inStream = null;;
      FileOutputStream outStream = null;

    try {
		//retrieve the file data
		inStream = new FileInputStream(uploadableFile);
		
		outStream = new FileOutputStream(folder +
		        ClsConstants.FILE_SEP + uploadableFile.getName());
		
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
		  outStream.write(buffer, 0, bytesRead);
		}
		//close the streams
		outStream.close();
		inStream.close();
    }catch (FileNotFoundException fnfe) {
        try {
            //close the streams
            outStream.close();
            inStream.close();
        } catch (Exception eee) {}
        new ClsExceptions(fnfe.getMessage());
    }catch (IOException ioe) {
        try {
            //close the streams
            outStream.close();
            inStream.close();
        } catch (Exception eee) {}
        new ClsExceptions(ioe.getMessage());
    }
  }

  public static void upload(FormFile uploadableFile, String filePath) throws ClsExceptions{

      InputStream stream = null;;
      OutputStream bos = null;
      
    try {
      //retrieve the file data
      stream = uploadableFile.getInputStream();

      //write the file to the file specified
      bos = new FileOutputStream(filePath);
      int bytesRead = 0;
      byte[] buffer = new byte[8192];
      while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
        bos.write(buffer, 0, bytesRead);
      }
      bos.close();


      //close the stream
      stream.close();
    }catch (FileNotFoundException fnfe) {
      new ClsExceptions(fnfe.getMessage());
    }catch (IOException ioe) {

      new ClsExceptions(ioe.getMessage());
    }finally{
        try {
            //close the streams
            bos.close();
            stream.close();
        } catch (Exception eee) {}
      //destroys the temporary file
      uploadableFile.destroy();
    }
  }
  public static void upload(FormFile uploadableFile, String filePath, boolean destroy) throws ClsExceptions{

      InputStream stream = null;;
      OutputStream bos = null;
    try {
      //retrieve the file data
      stream = uploadableFile.getInputStream();

      //write the file to the file specified
      bos = new FileOutputStream(filePath);
      int bytesRead = 0;
      byte[] buffer = new byte[8192];
      while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
        bos.write(buffer, 0, bytesRead);
      }
      bos.close();


      //close the stream
      stream.close();
    }catch (FileNotFoundException fnfe) {
      new ClsExceptions(fnfe.getMessage());
    }catch (IOException ioe) {
      new ClsExceptions(ioe.getMessage());
    }finally{
        try {
            //close the streams
            bos.close();
            stream.close();
        } catch (Exception eee) {}
      //destroys the temporary file
      if (destroy)  uploadableFile.destroy();
    }
  }
}
