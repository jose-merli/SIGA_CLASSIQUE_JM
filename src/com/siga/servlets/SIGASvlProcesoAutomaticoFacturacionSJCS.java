package com.siga.servlets;

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;
import com.siga.facturacionSJCS.service.FacturacionSJCSService;

import es.satec.businessManager.BusinessManager;

public final class SIGASvlProcesoAutomaticoFacturacionSJCS  extends SIGAContextListenerAdapter implements ServletContextListener, NotificationListener
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    public static String sNombreProceso = "ProcesoAutomaticoFacturacionSJCS";

    public void contextInitialized(ServletContextEvent event)
    {
    	
    	super.contextInitialized(event);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
        String sIntervaloAux = properties.returnProperty("general.gratuita.facturacionsjcs.ciclo");
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
			if (timer!=null) {
				if (timer.isActive())
					timer.stop();
				timer.removeNotification(idNotificacion);
			}

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
		    
			BusinessManager businessManager = BusinessManager.getInstance()  ;
   			FacturacionSJCSService facturacionSJCSService = (FacturacionSJCSService)businessManager.getService(FacturacionSJCSService.class);
   			facturacionSJCSService.procesarAutomaticamenteFacturacionSJCS();
		    ClsLogging.writeFileLogWithoutSession(" - FIN . >>>  Notificaci�n  \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		}
		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificaci�n \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
}