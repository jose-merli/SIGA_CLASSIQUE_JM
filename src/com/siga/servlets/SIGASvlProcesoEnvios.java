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
        //ClsLogging.writeFileLogWithoutSession(" - INVOCANDO... >>>  Ejecutando Notificación \"" + sNombreProceso + "\".", 3);

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
   				ClsLogging.writeFileLog(" Procesado con exito los envios automáticos ", 3);
   			}

   	        
   	        //ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificación \"" + sNombreProceso + "\".", 3);
   	        //ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
   	        //ClsLogging.writeFileLogWithoutSession("", 3);
   	        //ClsLogging.writeFileLogWithoutSession("", 3);  	
   			ClsLogging.writeFileLogWithoutSession(" OK proceso envios automáticos ", 3);
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("OK proceso envios automáticos");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	       
   	        out.println("ERROR en proceso envios automáticos");
   	        out.close();

   			
   			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		}
    }
    
/* RGG version con timer    
  //Global vars
  public void init() throws ServletException {
    ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

    ReadProperties properties=new ReadProperties("SIGA.properties");
    String sIntervaloAux = properties.returnProperty("envios.programacionAutomatica.tiempo.ciclo");
    String sIntervalo = sIntervaloAux;

    if (sIntervalo==null || sIntervalo.trim().equals(""))
    {
        sIntervalo="0";
    }

    if (sIntervalo.equals("0"))
    {
        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervaloAux + ").", 3);
    }

    else
    {
        lIntervalo = Long.parseLong(sIntervalo)*60*1000;

        timer = new Timer();
        timer.addNotificationListener(this, null, sNombreProceso);

        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);

        timer.start();

        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
    }

    ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("", 3);  	
  }
  
 public void destroy() {
    ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

    try
	{
        timer.stop();
        timer.removeNotification(idNotificacion);

        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
        ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
	}

	catch (InstanceNotFoundException e)
	{
        e.printStackTrace();
    }
	// TODO Auto-generated method stub
	super.destroy();

 }

 
 public void handleNotification(Notification notif, Object handback)
 {
		String sProximaEjecucion = "Próxima ejecución dentro de " + (lIntervalo/60/1000) + " minuto(s).";

     ClsLogging.writeFileLogWithoutSession(" ", 3);
     ClsLogging.writeFileLogWithoutSession(" - Ejecutando Notificación \"" + sNombreProceso + "\".", 3);

		try
		{
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor

			Vector vInstituciones = admInstitucion.select();

			if (vInstituciones!=null)
			{
				for (int i=0; i<vInstituciones.size(); i++)
				{
					CenInstitucionBean beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);

					Envio.procesarEnvios(""+beanInstitucion.getIdInstitucion(), ""+AdmUsuariosAdm.USUARIO_SISTEMA);
				}
			}

		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		}

		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
 } 

  RGG fin de parte con timerr 
  
  */


}


/*
package com.siga.servlets;

import java.util.*;
import javax.servlet.*;
import com.atos.utils.*;
import com.siga.beans.*;
import javax.management.*;
import com.siga.envios.*;
import weblogic.management.timer.Timer;

public final class SIGASvlProcesoAutomaticoEnvios implements ServletContextListener, NotificationListener
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String sNombreProceso = "ProcesoAutomaticoEnvios";

    public void contextInitialized(ServletContextEvent event)
    {
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

        ReadProperties properties=new ReadProperties("SIGA.properties");
        String sIntervaloAux = properties.returnProperty("envios.programacionAutomatica.tiempo.ciclo");
        String sIntervalo = sIntervaloAux;

        if (sIntervalo==null || sIntervalo.trim().equals(""))
        {
            sIntervalo="0";
        }

        if (sIntervalo.equals("0"))
        {
	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervaloAux + ").", 3);
        }

        else
        {
	        lIntervalo = Long.parseLong(sIntervalo)*60*1000;

	        timer = new Timer();
	        timer.addNotificationListener(this, null, sNombreProceso);

	        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
	        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);

	        timer.start();

	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
        }

        ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

        try
		{
            timer.stop();
            timer.removeNotification(idNotificacion);

            ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
            ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
            ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
            ClsLogging.writeFileLogWithoutSession("", 3);
            ClsLogging.writeFileLogWithoutSession("", 3);
		}

		catch (InstanceNotFoundException e)
		{
            e.printStackTrace();
        }
    }

    public void handleNotification(Notification notif, Object handback)
    {
		String sProximaEjecucion = "Próxima ejecución dentro de " + (lIntervalo/60/1000) + " minuto(s).";

        ClsLogging.writeFileLogWithoutSession(" ", 3);
        ClsLogging.writeFileLogWithoutSession(" - Ejecutando Notificación \"" + sNombreProceso + "\".", 3);

		try
		{
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor

			Vector vInstituciones = admInstitucion.select();

			if (vInstituciones!=null)
			{
				for (int i=0; i<vInstituciones.size(); i++)
				{
					CenInstitucionBean beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);

					Envio.procesarEnvios(""+beanInstitucion.getIdInstitucion(), ""+AdmUsuariosAdm.USUARIO_SISTEMA);
				}
			}

		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		}

		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
}
*/