package com.siga.servlets;

import javax.servlet.ServletConfig;

import org.apache.struts.action.ActionServlet;

import com.siga.Utilidades.SIGAReferences;

public class SIGAServletAdapter extends ActionServlet {
	private static final long serialVersionUID = -9054121018377626641L;

	public void init(ServletConfig cfg) throws javax.servlet.ServletException {
		super.init();
		SIGAReferences.initialize(cfg.getServletContext());
    }
}
