package com.siga.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.struts.action.ActionServlet;

import com.siga.Utilidades.SIGAReferences;

public class SIGAServletAdapter extends HttpServlet {
	private static final long serialVersionUID = -9054121018377626641L;

	public void init(ServletConfig cfg) throws javax.servlet.ServletException {
		SIGAReferences.initialize(cfg.getServletContext());
    }
}
