package com.siga.servlets;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//RGG import weblogic.management.timer.Timer;


import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;

import com.siga.envios.Envio;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoEnvios extends HttpServlet {

	
	
  // RGG   private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String sNombreProceso = "ProcesoAutomaticoEnvios";

    
// version de una sola llamada
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    	//ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX (LLAMADA DIRECTA).", 3);


    	//ClsLogging.writeFileLogWithoutSession(" ", 3);
        //ClsLogging.writeFileLogWithoutSession(" - INVOCANDO... >>>  Ejecutando Notificaci�n \"" + sNombreProceso + "\".", 3);

   		try
   		{
   			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor

   			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();

   			if (vInstituciones!=null)
   			{
   				for (int i=0; i<vInstituciones.size(); i++)
   				{
   					CenInstitucionBean beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);
   					ClsLogging.writeFileLog(">> Envio automatico >> Institucion: "+beanInstitucion.getIdInstitucion().toString(), 3);
					Date ini = new Date();
					
   					Envio.procesarEnvios(beanInstitucion.getIdInstitucion().toString(), UsrBean.UsrBeanAutomatico(beanInstitucion.getIdInstitucion().toString()));
					
					Date fin = new Date();
					// Control de transacciones largas
					if ((fin.getTime()-ini.getTime())>3000) {
					    Date dat = Calendar.getInstance().getTime();
					    SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
					    String fecha = sdfLong.format(dat);
					    ClsLogging.writeFileLog(fecha + ",==> SIGA: Control de tiempo de transacciones (>3 seg.),Proceso envios,"+beanInstitucion.getIdInstitucion().toString()+",automatico,"+new Long((fin.getTime()-ini.getTime())).toString(),10);
					}
   				
   				}
   				ClsLogging.writeFileLog(" Procesado con exito los envios autom�ticos ", 3);
   			}

   	        
   	        //ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificaci�n \"" + sNombreProceso + "\".", 3);
   	        //ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
   	        //ClsLogging.writeFileLogWithoutSession("", 3);
   	        //ClsLogging.writeFileLogWithoutSession("", 3);  	
   			ClsLogging.writeFileLogWithoutSession(" OK proceso envios autom�ticos ", 3);
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("OK proceso envios autom�ticos");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	       
   	        out.println("ERROR en proceso envios autom�ticos");
   	        out.close();

   			
   			ClsLogging.writeFileLogWithoutSession(" - Notificaci�n \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		}
    }
    



}


