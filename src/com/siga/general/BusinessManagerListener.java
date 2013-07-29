package com.siga.general;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.logging.LogFactory;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsLogging;

import es.satec.businessManager.BusinessManager;

public class BusinessManagerListener implements ServletContextListener {
	 
	public void contextInitialized(ServletContextEvent evt) {
		//forzamos a que use log4j mybatis
        LogFactory.useLog4JLogging();
		SIGAReferences.initialize(evt.getServletContext());
//		ClsLogging.writeFileLog("Creando PersistenceBusinessManager", 3);
		try {
			BusinessManager.getInstance(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.ATOS_BUSINESS_CONFIG));
			ClsLogging.writeFileLog("PersistenceBusinessManager creado", 3);
		} catch (Exception e) {
			//Si falla la carga de fichero del BusinessManager luego falla el la clase ClsLogging porque intenta cargar un servicio así que dejamos el e.printStackTrace()			
			e.printStackTrace();//NO QUITAR!! 
			ClsLogging.writeFileLogError("Error al createBusinessManager", e, 3);
		}

	}

	  public void contextDestroyed(ServletContextEvent evt) {
		  // ClsLogging.writeFileLog("Destruyendo PersistenceBusinessManager", 3);
		  // PersistenceBusinessManager.getInstance().closeBusinessManager();
		  // ClsLogging.writeFileLog("PersistenceBusinessManager destruido", 3);
	  }
	}


