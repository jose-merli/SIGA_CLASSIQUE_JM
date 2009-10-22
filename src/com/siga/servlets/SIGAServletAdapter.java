package com.siga.servlets;

import javax.servlet.http.HttpServlet;

import com.siga.Utilidades.SIGAReferences;

public class SIGAServletAdapter extends HttpServlet {
	private static final long serialVersionUID = -9054121018377626641L;

	public void init() throws javax.servlet.ServletException {
		SIGAReferences.initialize(getServletContext());
    }
}
