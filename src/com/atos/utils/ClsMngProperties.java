/**
 * <p>Title: ClsMngProperties </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.AdmLenguajesAdm;


public class ClsMngProperties {
	public static String TN_RESOURCES="GEN_RECURSOS";
	public static String FN_ID_RESOURCE="IDRECURSO";
	public static String FN_ID_PROPERTY="IDPROPIEDAD";
	public static String FN_ID_LANGUAGE="IDLENGUAJE";
	public static String FN_DESC_RESOURCE="DESCRIPCION";
	public static String FN_ERROR="ERROR";
	 
	private static Hashtable<String, LinkedHashMap> htLanguages=new Hashtable<String, LinkedHashMap>();
	//private static final String resourcesLocation=ClsConstants.RESOURCES_DIR_STRUTS + ClsConstants.FILE_SEP+"ApplicationResources";
	private static String selectLanguages="select "+ColumnConstants.FN_LANG_ID_LANGUAGE+" from "+TableConstants.TABLE_LANGUAGE;
	private static String selectResources="select "+FN_ID_RESOURCE+", "+FN_DESC_RESOURCE+
										  " from "+TN_RESOURCES+" where "+FN_ID_LANGUAGE+"=";

	/**
	 * Inicializa los recursos de multilenguaje, generando los archivos correspondientes.
	 * Vease documentación de Struts. 
	 * @throws ClsExceptions
	 */
	public static void initProperties() throws ClsExceptions{
		try {
			loadPropertiesFromDatabase();
      		writePropertiesInFiles();
    	} catch (ClsExceptions e) {
      		throw e;
    	}
  	}

	/**
	 * Accede a la tabla de recursos de lenguajes de la base de datos, y carga 
	 * los resultados obtenidos en una structura Hash, ordenando por código de lenguaje. 
	 * @throws ClsExceptions
	 */
	private static void loadPropertiesFromDatabase() throws ClsExceptions{
		RowsContainer rowsLanguages=new RowsContainer();
      	RowsContainer rowsResources=null;
      	LinkedHashMap lhmResources=null;
      	try {
      		if(rowsLanguages.query(selectLanguages)){
				int sizeLanguages=rowsLanguages.size();
				for(int i=0;i<sizeLanguages;i++){
	  				Row rowL=(Row)rowsLanguages.get(i);
	  				String languageCode=(String)rowL.getString(ColumnConstants.FN_LANG_ID_LANGUAGE);
      				rowsResources=new RowsContainer();
	  				lhmResources= new LinkedHashMap();
	  				Hashtable<Integer, String> codigos = new Hashtable<Integer, String>();
	  				codigos.put(new Integer(1),languageCode);
	  				if(rowsResources.queryBind(selectResources+":1 ORDER BY 1",codigos)) {
	    				int sizeResources=rowsResources.size();
	    				for(int j=0;j<sizeResources;j++){
	      					Row rowR=(Row)rowsResources.get(j);
	      					lhmResources.put(rowR.getString(FN_ID_RESOURCE),
											 rowR.getString(FN_DESC_RESOURCE));
	    				}
	  				}
	  				htLanguages.put(languageCode.toLowerCase(),lhmResources);
				}
      		}
  		} catch (ClsExceptions e) {
        	throw e;
      	}
  	}

	/**
	 * Genera los archivos de claves de lenguajes a partir de los datos recuperados de 
	 * la base de datos, y cargados en el Hash <code>htLanguages</code>
	 * @throws ClsExceptions
	 */
	@SuppressWarnings("unchecked")
	private static void writePropertiesInFiles() throws ClsExceptions{
  		FileOutputStream outputStream=null;
  		BufferedWriter bufferedWriter = null;
    	try {
      		String lang=null;
      		LinkedHashMap lhm=null;
      		OutputStreamWriter outputStreamWriter=null;
      		Enumeration<String> enumLanguages=htLanguages.keys();
      		while(enumLanguages.hasMoreElements()) {
        		lang=(String) enumLanguages.nextElement();
        		lhm=(LinkedHashMap) htLanguages.get(lang);
        		
        		// RGG cambio de codigos de lenguaje
        		UsrBean usr = new UsrBean();
				usr.setLanguage("1");
        		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
        		String lenguajeExt=a.getLenguajeExt(lang);
        		outputStream= new FileOutputStream(SIGAReferences.getDirectoryReference(SIGAReferences.RESOURCE_FILES.RECURSOS_DIR)+"/ApplicationResources_"+lenguajeExt.toLowerCase()+".properties");
				outputStreamWriter=new OutputStreamWriter(outputStream);
				bufferedWriter = new BufferedWriter(outputStreamWriter);
				Iterator itResources=lhm.orderedKeys();
				while(itResources.hasNext()) {
	  				String key=(String) itResources.next();
	  				String value=(String)lhm.get(key);
	  				bufferedWriter.write(key+"="+value+"\n");
	  				bufferedWriter.flush();
				}
				outputStream.close();
				bufferedWriter.close();
      		}
    	} catch (IOException e) {
      		throw new ClsExceptions("Error generando fichero, "+ e.toString(), "","","","");
    	} catch (Exception ex) {
      		throw new ClsExceptions("Error generando fichero, "+ ex.toString(), "","","","");
    	} finally {
    	    try {
				bufferedWriter.close();
    	        outputStream.close();
    	    } catch (Exception eee) {}
    	}
  	}

	/**
	 * Lee un archivo cualquiera de Propiedades (extensión properties) y genera el
	 * objeto Java <code>Properties</code> correspondiente. 
	 * @param theFile Nombre completo del archivo (con ruta).
	 * @return Objeto Properties resultado de leer el archivo.
	 * @throws java.io.IOException 
	 */
	public static Properties getPropertiesFromFile (String theFile) throws java.io.IOException {
    	Properties props= new Properties();
    	//java.io.InputStream isProps=ClassLoader.getSystemResourceAsStream(theFile);
    	FileInputStream isProps=new FileInputStream(theFile);
    	props.load(isProps);
    	isProps.close();
    	return props;
  	}

}