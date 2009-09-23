/**
 * <p>Title: ReadProperties </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

public class ReadProperties{
    // Read properties file.
    Properties properties = new Properties();
    HttpServletRequest req=null;


  public ReadProperties(String fileName, HttpServletRequest req){
    this.req=req;
    FileInputStream fis = null;
    try {
        fis = new FileInputStream(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName);
        properties.load(fis);

    } catch (IOException e) {
      ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName,req,3);
    } finally {
        try {
            fis.close();
        } catch (Exception eee) {}
    }
  }

  public ReadProperties(String fileName){
      FileInputStream fis = null;
      try {
          fis = new FileInputStream(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName);
          properties.load(fis);

        } catch (IOException e) {
          if (req!=null){
                  ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName,req,3);
          }else{
              ClsLogging.writeFileLogWithoutSession("MISSING PROPERTY FILE!!! "+ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName,3);
          }

        } finally {
            try {
                fis.close();
            } catch (Exception eee) {}
        }
  }

  public ReadProperties(String fileName, boolean struts){
      FileInputStream fis = null; 
      try {
          fis = new FileInputStream(ClsConstants.RESOURCES_DIR_STRUTS+ClsConstants.FILE_SEP+fileName);
          properties.load(fis);
       } catch (IOException e) {
         if (req!=null){
                 ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+ClsConstants.RESOURCES_DIR_STRUTS+ClsConstants.FILE_SEP+fileName,req,3);
         }else{
              ClsLogging.writeFileLogWithoutSession("MISSING PROPERTY FILE!!! "+ClsConstants.RESOURCES_DIR_STRUTS+ClsConstants.FILE_SEP+fileName,3);
         }

       } finally {
           try {
               fis.close();
           } catch (Exception eee) {}
       }
  }

public String returnProperty(String key){

    Object obj =properties.getProperty(key);
    String value ="ERROR, NO VALUE IN PROPERTY FILE!, key: "+key;
    if (obj!=null)
      value = (String)obj;
    else
      ClsLogging.writeFileLogWithoutSession(value,3);
    return value;

    }

	// DCG para que el return sea nulo si no lo encuentra
	public String returnProperty(String key, boolean enableValorNulo)
	{
	    String value = returnProperty(key);
	    if ((enableValorNulo) && (value.startsWith("ERROR, NO VALUE IN PROPERTY FILE!")))
	    	value = null;
	    return value;
	}

}
