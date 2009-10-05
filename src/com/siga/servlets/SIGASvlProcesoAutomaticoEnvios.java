package com.siga.servlets;


import java.net.URL;
import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoAutomaticoEnvios extends SIGAServletAdapter implements NotificationListener {

	
	
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String sNombreProceso = "ProcesoAutomaticoEnvios";
    private String urlSiga = "";
    
  //Global vars
  public void init() throws ServletException {
	  super.init();
    //ClsLogging.writeFileLogWithoutSession("", 3);
  	//ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

    
    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//    ReadProperties properties=new ReadProperties("SIGA.properties");
    String sIntervaloAux = properties.returnProperty("envios.programacionAutomatica.tiempo.ciclo");
    String sIntervalo = sIntervaloAux;

    urlSiga = properties.returnProperty("general.urlSIGA");
    
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
    //ClsLogging.writeFileLogWithoutSession("", 3);
    //ClsLogging.writeFileLogWithoutSession("", 3);  	
  }
  
  /* (non-Javadoc)
 * @see javax.servlet.GenericServlet#destroy()
 */
 public void destroy() {
 	//ClsLogging.writeFileLogWithoutSession("", 3);
 	//ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

    try
	{
        timer.stop();
        timer.removeNotification(idNotificacion);

        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
        ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
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

    ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

	try
	{
	    // invocamos al servlet
		URL url = new URL(urlSiga+ "SIGASvlProcesoEnvios.svrl");
	    Object ret = url.getContent();
	    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
	    //ClsLogging.writeFileLogWithoutSession(" - EJECUTADO.  >>>  Ejecutando Notificación: \"" + urlSiga+ "SIGASvlProcesoEnvios.svrl" + "\"", 3);
	    
	}
	catch(Exception e)
	{
	    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
	    e.printStackTrace();
	}
    

    

/* 	
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
*/
 } 

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