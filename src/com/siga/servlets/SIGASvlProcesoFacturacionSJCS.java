package com.siga.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.siga.facturacionSJCS.service.FacturacionSJCSService;

import es.satec.businessManager.BusinessManager;


/**
 * 
 * @author jorgeta 
 * @date   02/07/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class SIGASvlProcesoFacturacionSJCS extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sNombreProceso = "ProcesoFacturacionSJCS";

    
// version de una sola llamada


    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	java.io.PrintWriter out = response.getWriter();
   		try
   		{
   			out.println("INICIO PROCESO DE FACTURACION SJCS EN BACKGROUND");
			
   			BusinessManager businessManager = BusinessManager.getInstance()  ;
   			FacturacionSJCSService facturacionSJCSService = (FacturacionSJCSService)businessManager.getService(FacturacionSJCSService.class);
   			facturacionSJCSService.procesarAutomaticamenteFacturacionSJCS();
   			
   	        response.setContentType("text/html");
   	        out.println("FIN PROCESO  DE FACTURACION SJCS EN BACKGROUND");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   			
   	        response.setContentType("text/html");
   	        
   	        out.println("ERROR EN PROCESO AUTOMATICO DE FACTURACION SJCSEN BACKGROUND");
   			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		}finally{
   			if(out!=null){
   				out.flush();
   				out.close();
   				out = null;
   				
   			}
   			
   			
   		}

    }

}


