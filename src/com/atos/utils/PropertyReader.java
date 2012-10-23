package com.atos.utils;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.SIGAReferences.RESOURCE_FILES;
import com.siga.beans.GenPropertiesAdm;

/**
 * 
 * 
 * @author josebd
 *
 */
public class PropertyReader {
	
	static Hashtable propHT = new Hashtable();

	/**
	 * Metodo que devuelve un Properties(clave, valor) indicado por el parametro resource.
	 * 
	 * @param resource El properties que se quiere recuperar
	 * @return Properties indicado por resource
	 */
	public static Properties getProperties(SIGAReferences.RESOURCE_FILES resource){
		// Recupera de la hashtable el properties indicado
		Properties prop = (Properties)propHT.get(resource);
		// Si es nulo es porque todavia no se ha cargado en memoria
		if(prop==null){
			// Lo recuperamos de fichero o bbdd y lo cargamos en memoria
			prop = returnPropertyFile(resource);
			propHT.put(resource, prop);
		}
		// Finalmente devolvemos el properties pedido
		return prop;
	}
	
	/**
	 * Método que carga en memoria el fichero properties indicado por el parametro resource 
	 * 
	 * @param resource el fichero de propiedades que se quiere cargar
	 * @return
	 */
	private static Properties returnPropertyFile (SIGAReferences.RESOURCE_FILES resource){
		
		Properties prop = new Properties();
		InputStream is=null;
		
		//TODO//jbd// Hay que revisar la captura de excepciones
		
		try {
			//ClsLogging.writeFileLog(" **** Leyendo properties " + resource.name() + " ****",3);
			// Si el fichero properties es SIGA o LOG4J se recoge de base de datos
			if(	resource.compareTo(RESOURCE_FILES.SIGA)==0 || resource.compareTo(RESOURCE_FILES.LOG4J)==0) {
				// Si se van a meter mas properties a bbdd hay que indicarlo aqui
				GenPropertiesAdm propAdm = new GenPropertiesAdm(null);
				prop = propAdm.getProperties(resource);
			}else{
				// Si no está en base de datos se coge de fichero
				is=SIGAReferences.getInputReference(resource);
				prop.load(is);
			}
			
		} catch (Exception e) {
			//ClsLogging.writeFileLog("MISSING PROPERTY FILE!!! "+resource.getFileName(),3);
		} finally {
			try {
				is.close();
			} catch (Exception eee) {}
		}
		
		// Se devuelve el properties ya cargado
		return prop;
	}
	
	public static void cleanProperties(){
		propHT.clear();
	}
	
	
}
