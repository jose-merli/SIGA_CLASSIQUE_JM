/*
 * Created on 19-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

import java.io.InputStream;
import java.util.Properties;

import org.redabogacia.sigaservices.app.util.SIGAReferences;



/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SIGAExcConstants {
	protected static Properties sqlstates=null;
	static {
		sqlstates=new Properties();
		try {
//			FileInputStream in=new FileInputStream(ClsConstants.RESOURCES_DIR+"/"+"except.properties");
//			sqlstates.load(in);
			InputStream is=SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.EXCEPT);
			sqlstates.load(is);
		} catch (Exception e) {e.printStackTrace();}
	}
		
	protected static String getErrorDescription(String sqlstate) {
		if (sqlstate==null || sqlstate.length()==0)
			return null;
		String ret=null;
		ret=sqlstates.getProperty(sqlstate);
		if (ret==null) {
			ret=sqlstates.getProperty(sqlstate.substring(0,2));
		}
		return ret;
	}
	
	protected static String getClsExceptionErrorDescription(String cod) {
		if (cod==null || cod.length()==0)
			return null;
		String ret=null;
		String clscod="CE"+cod;
		ret=sqlstates.getProperty(clscod);
		if (ret==null) {
			ret=sqlstates.getProperty(clscod.substring(0,4));
		}
		return ret;
	}
}
