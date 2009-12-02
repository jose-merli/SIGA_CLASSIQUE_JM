package com.siga.Utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletContext;

public class SIGAReferences {
	public static enum RESOURCE_FILES{
//		ROOT("/"),
//		LIB("/WEB-INF/lib"),
//		CLASSES("/WEB-INF/classes"),
//		JSP("/html/jsp/"),
		WEB_INF_DIR("/WEB-INF/"),
		PROPERTIES_DIR("/WEB-INF/properties"),
		LOG_DIR("/WEB-INF/Log"),
		CDS_DIR("/WEB-INF/properties/cds"),
		FOP_DIR("/WEB-INF/fopFonts"),		
		PKI("/WEB-INF/properties/pki.properties"),
		COMBO("/WEB-INF/properties/Combo.properties"),
		EXCEPT("/WEB-INF/properties/except.properties"),
		FOP("/WEB-INF/properties/fop.xml"),
		INTERFACE("/WEB-INF/properties/interface.properties"),
		JNDI("/WEB-INF/properties/jndi.properties"),
		LDAP("/WEB-INF/properties/ldap.properties"),
		LOG4J("/WEB-INF/properties/log4j.properties"),
		PESTANA("/WEB-INF/properties/Pestana.properties"),
		POOL("/WEB-INF/properties/pool.properties"),
		PRA("/WEB-INF/properties/pra.properties"),
		SIGA("/WEB-INF/properties/SIGA.properties"),
		UPLOAD("/WEB-INF/properties/Upload.properties"),
		CRCONFIG("/WEB-INF/classes/CRConfig.xml"),
		WORDS_INIT("/html/jsp/general/inicializacionWords.doc"),
		WORDS_LICENSE("/WEB-INF/lib/Aspose.Words.lic"),
		WORDS_INIT_RESULT("/html/jsp/general/resultadoInicializacionWords.doc"),
		CRYSTAL_INIT("/html/jsp/general/inicializacionCrystal.rpt"),
		CRYSTAL_INIT_RESULT("/html/jsp/general/l23p57r22a15d31.pdf"),
		ATOS_BUSINESS_CONFIG("/WEB-INF/classes/AtosBussinesConfig.xml");
		
		private String fileName;
		
		private RESOURCE_FILES (String name){
			this.fileName=name;
		}
		
		public String getFileName(){
			return fileName;
		}
	}
	
	private static ServletContext context;
	
	private SIGAReferences(){
	}
	
	public static void initialize(ServletContext context){
		SIGAReferences.context=context;
	}
	
	public static InputStream getInputReference(RESOURCE_FILES resource){
		if (context!=null)
			return context.getResourceAsStream(resource.getFileName());
		else 
			return null;
	}

	public static OutputStream getOutputReference(RESOURCE_FILES resource) throws MalformedURLException, FileNotFoundException, URISyntaxException{
		if (context!=null){
			return new FileOutputStream(new File(context.getResource(resource.getFileName()).toURI()));
		} else 
			return null;
	}

	public static File getFileReference(RESOURCE_FILES resource) throws MalformedURLException, FileNotFoundException, URISyntaxException{
		if (context!=null){
			return new File(context.getResource(resource.getFileName()).toURI());
		} else 
			return null;
	}

	public static String getDirectoryReference(RESOURCE_FILES resource) throws MalformedURLException, FileNotFoundException, URISyntaxException{
		if (context!=null){
			return (new File(context.getResource(resource.getFileName()).toURI())).getAbsolutePath();
		} else 
			return null;
	}
	
	public static String getReference(String path) throws MalformedURLException, FileNotFoundException, URISyntaxException {
		if (context!=null){
			URL url=context.getResource(path);
			if (url!=null){
				File fichero=(new File(url.toURI()));
				if (fichero!=null){
					return fichero.getAbsolutePath();
				}
			}
		}
		
		return null;
	}
}
