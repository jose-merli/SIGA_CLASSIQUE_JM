package com.siga.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;



public class ServletPruebaACA extends HttpServlet
{
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
    	doWork(req, res);
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
    	doWork(req, res);
    }
    
    public void doWork (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//        ReadProperties rp = new ReadProperties("SIGA.properties");
        
        String sRetorno = rp.returnProperty("ACA.TMP_VALOR_RETORNO_SERVLET");
        
        String sXML = "<?xml version='1.0' encoding='iso-8859-1'?><field><serialNumber>\"62 B6 25 6E 06 10 9C 13 41 C2 C6 97 26 2B 2A ED\"</serialNumber><codigo>" + sRetorno + "</codigo><descripcion>Error No Data</descripcion></field>";
        
    	PrintWriter pw = res.getWriter();
    	
    	pw.write(sXML);
    	pw.flush();
    	pw.close();
    }
}