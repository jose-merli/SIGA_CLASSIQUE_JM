package com.siga.general;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.atos.utils.ClsLogging;
import com.siga.Utilidades.SIGAReferences;

import es.satec.businessManager.BusinessManager;

public class BusinessManagerListener implements ServletContextListener {
	 
	public void contextInitialized(ServletContextEvent evt) {
		SIGAReferences.initialize(evt.getServletContext());
		ClsLogging.writeFileLog("Creando PersistenceBusinessManager", 3);
		try {
			BusinessManager.getInstance(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.ATOS_BUSINESS_CONFIG));
			ClsLogging.writeFileLog("PersistenceBusinessManager creado", 3);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al createBusinessManager", e, 3);
		}

	}

	  public void contextDestroyed(ServletContextEvent evt) {
		  // ClsLogging.writeFileLog("Destruyendo PersistenceBusinessManager", 3);
		  // PersistenceBusinessManager.getInstance().closeBusinessManager();
		  // ClsLogging.writeFileLog("PersistenceBusinessManager destruido", 3);
	  }
	}


