package com.siga.servlets;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Clase Servlet de pruebas
 * @author RGG
 *
 */
public class SIGATest extends HttpServlet {

	 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	 	throws ServletException {
	 	
	 	
	 	try {
	 		
	 		PrintWriter out = response.getWriter();
	 		
	 		
	 		
	 		// HEADERS REQUEST
	 		out.println("<h1>DATOS DE LA LLAMADA</h3>");
	 		out.println("<hr>");
	 		out.println("<h2>CABECERAS HTTP</h2>");
	 		Enumeration headers = request.getHeaderNames();
	 		while (headers.hasMoreElements()) {
	 			String header = (String) headers.nextElement();
	 			out.println("<b>"+header + ":</b>" + request.getHeader(header));
		 		out.println("<br>");
	 		}
	 		// ATTRIBUTE REQUEST
	 		out.println("<hr>");
	 		out.println("<h2>ATRIBUTOS REQUEST</h2>");
	 		Enumeration headers2 = request.getAttributeNames();
	 		while (headers2.hasMoreElements()) {
	 			String header2 = (String) headers2.nextElement();
	 			out.println("<b>"+header2 + ":</b>" + request.getAttribute(header2));
		 		out.println("<br>");
	 		}
	 		
	 		// ATTRIBUTE SESSION
	 		out.println("<hr>");
	 		out.println("<h2>ATRIBUTOS SESSION</h2>");
	 		headers2 = request.getSession().getAttributeNames();
	 		while (headers2.hasMoreElements()) {
	 			String header2 = (String) headers2.nextElement();
	 			out.println("<b>"+header2 + ":</b>" + request.getSession().getAttribute(header2));
		 		out.println("<br>");
	 		}
	 		
	 		// ATTRIBUTE CONTEXT
	 		out.println("<hr>");
	 		out.println("<h2>ATRIBUTOS CONTEXTO</h2>");
	 		headers2 = this.getServletContext().getAttributeNames();
	 		while (headers2.hasMoreElements()) {
	 			String header2 = (String) headers2.nextElement();
	 			out.println("<b>"+header2 + ":</b>" + this.getServletContext().getAttribute(header2));
		 		out.println("<br>");
	 		}
	 		
	 		// DATOS CGI
	 		out.println("<hr>");
	 		out.println("<h2>DATOS CGI</h2>");
	 		out.println("<b>DOCUMENT_ROOT:</b>" + this.getServletContext().getRealPath("/"));
	 		out.println("<br>");
	 		out.println("<b>AUTH_TYPE:</b>" + request.getAuthType());
	 		out.println("<br>");
	 		out.println("<b>REMOTE_HOST :</b>" + request.getRemoteHost() );
	 		out.println("<br>");
	 		out.println("<b>REMOTE_USER :</b>" + request.getRemoteUser());
	 		out.println("<br>");
	 		out.println("<b>SERVER_NAME :</b>" + request.getServerName() );
	 		out.println("<br>");
	 		out.println("<b>SERVER_PORT :</b>" + String.valueOf(request.getServerPort()));
	 		out.println("<br>");
	 		out.println("<b>SERVER_PROTOCOL :</b>" + request.getProtocol() );
	 		out.println("<br>");
	 		out.println("<b>SERVER_SOFTWARE :</b>" + this.getServletContext().getServerInfo() );
	 		out.println("<br>");
	 		out.println("<b>URI:</b>" + request.getRequestURI());
	 		out.println("<br>");
	 		out.println("<b>URL:</b>" + request.getRequestURL());
	 		out.println("<br>");
	 		out.println("<b>SERVLET INFO:</b>" + this.getServletInfo());
	 		out.println("<br>");
	 		out.println("<br>");
	 		out.println("<br>");
	 		out.println("<b>REMOTE_ADDR:</b>" + request.getRemoteAddr());
	 		out.println("<br>");
	 		out.println("<b>HTTP_X_FORWARDED_FOR:</b>" + request.getHeader("HTTP_X_FORWARDED_FOR"));
	 		out.println("<br>");
	 		

	 		out.close();
			
	 	} catch (Exception e){
	 	}
	 	
	 	
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
