package com.siga.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.redabogacia.sigaservices.app.util.SIGAReferences;


public abstract class SIGAContextListenerAdapter implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    	SIGAReferences.initialize(event.getServletContext());
    }

	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
