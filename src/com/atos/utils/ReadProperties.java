/**
 * <p>Title: ReadProperties </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.siga.Utilidades.SIGAReferences;


public class ReadProperties{
	// Read properties file.
	Hashtable propertiesHt = new Hashtable();
	Properties properties = new Properties();
	HttpServletRequest req=null;

	
	public ReadProperties(SIGAReferences.RESOURCE_FILES resource){
		properties = PropertyReader.getProperties(resource);
	}
	

	public String returnProperty(String key){
		Object obj =properties.getProperty(key);
		String value ="ERROR, NO VALUE IN PROPERTY FILE!, key: "+key;
		if (obj!=null)
			value = (String)obj;
		//else
			//ClsLogging.writeFileLogWithoutSession(value,3);
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
