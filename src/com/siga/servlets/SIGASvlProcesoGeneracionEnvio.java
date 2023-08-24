package com.siga.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.envios.EnvioInformesGenericos;


/**
 * 
 * @author jorgeta
 *
 */
public class SIGASvlProcesoGeneracionEnvio extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sNombreProceso = "ProcesoGeneracionEnvio";

    
// version de una sola llamada


    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	java.io.PrintWriter out = response.getWriter();
   		try
   		{
   			out.println("INICIO PROCESO AUTOMATICO DE GENERACION DE ENVIOS EN BACKGROUND");
			
   			EnvioInformesGenericos envios = new EnvioInformesGenericos();
   			AdmTipoInformeAdm admTipoInformeAdm = new AdmTipoInformeAdm(new UsrBean());
   			List<AdmTipoInformeBean> tipoInformrProgramados = admTipoInformeAdm.getTiposDeInformeProgramados();
   			
   			envios.procesarAutomaticamenteGeneracionEnvios(tipoInformrProgramados);
   			
   	        response.setContentType("text/html");
   	        out.println("FIN PROCESO AUTOMATICO DE GENERACION ENVIOS EN BACKGROUND");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   			
   	        response.setContentType("text/html");
   	        
   	        out.println("ERROR EN PROCESO AUTOMATICO DE GENERACION DE ENVIOS EN BACKGROUND");
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


