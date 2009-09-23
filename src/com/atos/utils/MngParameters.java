/**
 * <p>Title: MngParameters </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class MngParameters {
/*
    public MngParameters() {

    }
*/
	public String getValue(String sStream, String sParameter) throws ClsExceptions{

		String sValue="";
		Row row = new Row();
		String sSql = "SELECT VALUE FROM _PARAMETERS WHERE STREAM='"+sStream+"' AND PARAMETER='"+sParameter+"'";
				try {
					if (row.find(sSql)){
						sValue=(String)row.getValue("VALUE");
					}
				}
				catch (ClsExceptions ex) {
					throw ex;
				}
		return sValue;
	}

}