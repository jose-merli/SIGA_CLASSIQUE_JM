/*
 * Created on Aug 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.servlets;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;



/**
 * Clase Servlet para tratar la peticion del Applet AppletDireccionIP de almacenar en la variable de sesion "IPCLIENTE" la IP del usuario.
 * @author david.sanchezp
 *
 */
public class SIGADireccionIP extends HttpServlet {

	/*
	public void init(){
		try {
			super.init();
		} catch (Exception e){
			
		}
	}
	*/
	
	 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	 	throws ServletException {
	 	
	 	String direccionIpCLiente = null;
	 	
	 	try {
	 		direccionIpCLiente = request.getParameter("IPCLIENTE");
	 		if (direccionIpCLiente == null)
	 			direccionIpCLiente = "NO ENCONTRADA";
	 		
	 		//Respuesta del Servlet al Applet:
	 		//response.setContentType("text/html");
	 		//PrintWriter out = response.getWriter();
	 		//out.println("$$OK$$");
	 	} catch (Exception e){
	 		direccionIpCLiente = "NO ENCONTRADA";
	 	}
	 	request.getSession().setAttribute("IPCLIENTE",direccionIpCLiente);
	 	ClsLogging.writeFileLog("IP DEL CLIENTE="+direccionIpCLiente,3);
	 	ClsLogging.writeFileLog("SERVLET SIGADireccionIP: inserta en sesion IP IPCLIENTE="+direccionIpCLiente,10);
	 }

	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	 throws ServletException {
	 	processRequest(request, response);
	 }

	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	 throws ServletException {
	 	processRequest(request, response);
	 }	
}
