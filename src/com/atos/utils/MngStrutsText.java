/**
 * <p>Title: MngStrutsText </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class MngStrutsText {
/*
 * 
 * ESTA CLASE NO EXISTE. NO SIRVE QUE LA VEAS. ES UNA ILUSION
 * 
//  private static MngStrutsText stext=null;
	static Hashtable files=new Hashtable();
	public static String getText(String strutsKey, String lenguage) throws ClsExceptions{
		String text="";
		String file="ApplicationResources_es.properties";
		
		try{
			// RGG 26/02/2007 cambio de codigos de lenguaje
			AdmLenguajesAdm len = new AdmLenguajesAdm(new UsrBean(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString(),lenguage));
			String lenguajeExt="es";
			lenguajeExt=len.getLenguajeExt(lenguage);
				
			if(lenguajeExt!=null) {
				file="ApplicationResources_"+lenguajeExt.toLowerCase()+".properties";
			}
			ReadProperties rp=(ReadProperties)files.get(file);
			if (rp==null) {
				ClsLogging.writeFileLog("Creo el properties",5);
				rp=new ReadProperties(file,true);
				files.put(file,rp);
			}
			//ReadProperties rp=new ReadProperties(file,true);
			text=rp.returnProperty(strutsKey);
		}catch(Exception ex){
			ClsLogging.writeFileLogError("error en MngStrutsText",3);
        	throw new ClsExceptions("Requested Master Table not found.", "","","","");
		}
		return text;
	}
*/	
/*
	public static String getTextExt(String strutsKey, String lenguajeExt) throws ClsExceptions{
		
	    String text="";
		String file=null;
		
		try{
			if (lenguajeExt==null) {
				lenguajeExt="es";
			}
			file="ApplicationResources_"+lenguajeExt.toLowerCase()+".properties";
			ReadProperties rp=(ReadProperties)files.get(file);
			if (rp==null) {
				ClsLogging.writeFileLog("Creo el properties",5);
				rp=new ReadProperties(file,true);
				files.put(file,rp);
			}
			//ReadProperties rp=new ReadProperties(file,true);
			text=rp.returnProperty(strutsKey);
		}catch(Exception ex){
			ClsLogging.writeFileLogError("error en MngStrutsText",3);
        	throw new ClsExceptions("Requested Master Table not found.", "","","","");
		}
		return text;
	}
	public static String getText(String strutsKey, HttpServletRequest req) throws ClsExceptions{

		String lenguage=(String)((UsrBean)req.getSession().getAttribute("USRBEAN")).getLanguage();
		String text=getText(strutsKey, lenguage);
		return text;

	}

	*/	

}