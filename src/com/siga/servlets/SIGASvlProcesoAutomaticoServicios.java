package com.siga.servlets;

import java.util.*;

import javax.servlet.*;
import com.atos.utils.*;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.*;


import javax.management.*;
import weblogic.management.timer.Timer;

public final class SIGASvlProcesoAutomaticoServicios implements ServletContextListener, NotificationListener
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String sNombreProceso = "ProcesoAutomaticoServicios";

    public void contextInitialized(ServletContextEvent event)
    {

    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//        ReadProperties properties=new ReadProperties("SIGA.properties");
        String sIntervaloAux = properties.returnProperty("pys.servicios.tiempo.ciclo");
        String sIntervalo = sIntervaloAux;

        if (sIntervalo==null || sIntervalo.trim().equals(""))
        {
            sIntervalo="0";
        }

        if (sIntervalo.equals("0"))
        {
	        ClsLogging.writeFileLogWithoutSession("    - Notificaci�n \"" + sNombreProceso + "\" no arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecuci�n: Err�neo (" + sIntervaloAux + ").", 3);
        }

        else
        {
	        lIntervalo = Long.parseLong(sIntervalo)*60*1000;

	        timer = new Timer();
	        timer.addNotificationListener(this, null, sNombreProceso);

	        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
	        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);

	        timer.start();

	        ClsLogging.writeFileLogWithoutSession("    - Notificaci�n \"" + sNombreProceso + "\" arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecuci�n: " + sIntervalo + " minuto(s).", 3);
        }

        ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
    }

    public void contextDestroyed(ServletContextEvent event)
    {
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

        try
		{
            timer.stop();
            timer.removeNotification(idNotificacion);

            ClsLogging.writeFileLogWithoutSession("    - Notificaci�n \"" + sNombreProceso + "\" parada.", 3);
            ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destru�das.", 3);
            ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
            //ClsLogging.writeFileLogWithoutSession("", 3);
            //ClsLogging.writeFileLogWithoutSession("", 3);
		}
		catch (InstanceNotFoundException e)
		{
            e.printStackTrace();
        }
    }

    public void handleNotification(Notification notif, Object handback)
    {
		String sProximaEjecucion = "Pr�xima ejecuci�n dentro de " + (lIntervalo/60/1000) + " minuto(s).";
        ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificaci�n: \"" + sNombreProceso + "\".", 3);


		try
		{
		    UsrBean us = new UsrBean();
		    us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
		    CenInstitucionAdm admInstitucion = new CenInstitucionAdm(us);
			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();
			CenInstitucionBean beanInstitucion = null;
		    if (vInstituciones != null) {
		        for (int i = 0; i < vInstituciones.size(); i++)  {
		        	beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);
		        	Integer idInstitucion = beanInstitucion.getIdInstitucion();
		        	
		        	
		            PysServiciosInstitucionAdm ser = new PysServiciosInstitucionAdm(us);
		            ser.ComprobacionServiciosAutomaticos(idInstitucion);
		        }
		    }
		    ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificaci�n: \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		}
		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificaci�n \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
}