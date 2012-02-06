package com.siga.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.facturacion.Facturacion;
import java.util.Hashtable;


/**
 * 
 * @author jorgeta
 *
 */
public class SIGASvlProcesoIndividualConfirmacionFacturacion extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sNombreProceso = "ProcesoIndividualConfirmacionFacturacion";

    
// version de una sola llamada


    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	java.io.PrintWriter out = response.getWriter();
   		try
   		{

   			out.println("INICIO PROCESO AUTOMATICO DE CONFIRMACION INDIVIDUAL DE FACTURACIÓN");
   			FacFacturacionProgramadaBean factBean = new FacFacturacionProgramadaBean();
   			String idInstitucion = request.getParameter("idInstitucion");
   			String idSerieFacturacion = request.getParameter("idSerieFacturacion");
   			String idProgramacion = request.getParameter("idProgramacion");
   			
            factBean.setIdInstitucion(Integer.valueOf(idInstitucion));
            factBean.setIdSerieFacturacion(Long.valueOf(idSerieFacturacion));
            factBean.setIdProgramacion(Long.valueOf(idProgramacion));
            factBean.setRealizarEnvio("0");
            factBean.setGenerarPDF("1");
            factBean.setEnvio("0");
            Facturacion facturacion = new Facturacion(UsrBean.UsrBeanAutomatico(idInstitucion));
            facturacion.confirmarProgramacionFactura(factBean,request,false,null,true,true); 			
   			
   	        response.setContentType("text/html");
   	        out.println("FIN PROCESO AUTOMATICO DE CONFIRMACION INDIVIDUAL DE FACTURACIÓN");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   			
   	        response.setContentType("text/html");
   	        
   	        out.println("ERROR EN PROCESO AUTOMATICO DE CONFIRMACION INDIVIDUAL DE FACTURACIÓN");
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


