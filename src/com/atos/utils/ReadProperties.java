/**
 * <p>Title: ReadProperties </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.siga.Utilidades.SIGAReferences;


public class ReadProperties{
	// Read properties file.
	Properties properties = new Properties();
	HttpServletRequest req=null;

	public ReadProperties(SIGAReferences.RESOURCE_FILES resource, HttpServletRequest req){
		this.req=req;
		InputStream is=null;
		
		try {
			is=SIGAReferences.getInputReference(resource);
			properties.load(is);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+resource.getFileName(),req,3);
		} finally {
			try {
				is.close();
			} catch (Exception eee) {}
		}
	}
	
	public ReadProperties(SIGAReferences.RESOURCE_FILES resource){
		InputStream is=null;
		
		try {
			is=SIGAReferences.getInputReference(resource);
			properties.load(is);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+resource.getFileName(),req,3);
		} finally {
			try {
				is.close();
			} catch (Exception eee) {}
		}
	}
	
/*
	public void ReadProperties2(String fileName, HttpServletRequest req){
		this.req=req;
		FileInputStream fis = null;
		String path="";
		
		try {
			path=SIGAReferences.getReference(SIGAReferences.RESOURCE_FILES.WEB_INF.getFileName()+File.separator+fileName);
			fis = new FileInputStream(path);
			//        fis = new FileInputStream(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName);
			properties.load(fis);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+path,req,3);
		} finally {
			try {
				fis.close();
			} catch (Exception eee) {}
		}
	}

	public void ReadProperties2(String fileName){
		FileInputStream fis = null;
		String path="";
		
		try {
			path=SIGAReferences.getReference(SIGAReferences.RESOURCE_FILES.WEB_INF.getFileName()+File.separator+fileName);
			fis = new FileInputStream(path);
			//fis = new FileInputStream(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+fileName);
			properties.load(fis);
		} catch (Exception e) {
			if (req!=null){
				ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+path,req,3);
			}else{
				ClsLogging.writeFileLogWithoutSession("MISSING PROPERTY FILE!!! "+path,3);
			}

		} finally {
			try {
				fis.close();
			} catch (Exception eee) {}
		}
	}

	public void ReadProperties2(String fileName, boolean struts){
		FileInputStream fis = null; 
		String path="";
		
		try {
			path=SIGAReferences.getDirectoryReference(SIGAReferences.RESOURCE_FILES.PROPERTIES)+File.separator+fileName;
			fis = new FileInputStream(path);
			//fis = new FileInputStream(ClsConstants.RESOURCES_DIR_STRUTS+ClsConstants.FILE_SEP+fileName);
			properties.load(fis);
		} catch (Exception e) {
			if (req!=null){
				ClsLogging.writeFileLogError("MISSING PROPERTY FILE!!! "+path,req,3);
			}else{
				ClsLogging.writeFileLogWithoutSession("MISSING PROPERTY FILE!!! "+path,3);
			}

		} finally {
			try {
				fis.close();
			} catch (Exception eee) {}
		}
	}
*/
	
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
	public String returnProperty(String key, boolean enableValorNulo){
		String value = returnProperty(key);
		if ((enableValorNulo) && (value.startsWith("ERROR, NO VALUE IN PROPERTY FILE!")))
			value = null;
		return value;
	}
}
