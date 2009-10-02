package com.siga.servlets;

import java.util.*;

import javax.servlet.*;

import com.atos.utils.*;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;


import javax.management.*;

import weblogic.management.timer.Timer;

public final class SIGASvlProcesoAutomaticoContadores implements ServletContextListener, NotificationListener 
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String lHoraEjecucion = "00:00";
    private String sNombreProceso = "ProcesoAutomaticoContadores";
    private boolean reinicio = true;

    public void contextInitialized(ServletContextEvent event)
    {
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//        ReadProperties properties=new ReadProperties("SIGA.properties");
        String sIntervaloAux = properties.returnProperty("administracion.reconfiguracionContadores.tiempo.ciclo");
        String sIntervalo = sIntervaloAux;
        
        String sHoraEjecucionAux = properties.returnProperty("administracion.reconfiguracionContadores.horaEjecucion");
        String sHoraEjecucion = sHoraEjecucionAux;
        
        
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
	        lHoraEjecucion = sHoraEjecucion;
	        
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
    }

    public void handleNotification(Notification notif, Object handback)
    {
		String sProximaEjecucion = "Próxima ejecución dentro de " + (lIntervalo/60/1000) + " minuto(s).";

        ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
	
		
		try
		{
			java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat("HH:mm");
			Date dateParam= sdfNew.parse(lHoraEjecucion);	
			
			String horaHoy = UtilidadesBDAdm.getHoraBD();
			Date dateNow = sdfNew.parse(horaHoy);
			if (dateNow.before(dateParam) && reinicio == false){
				reinicio = true;
			}
			
			if (dateNow.after(dateParam) && reinicio == true ) {
			    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Es la hora de ejecución: \"" + sNombreProceso + "\" " + sProximaEjecucion, 3);
				Object[] paramIn = new Object[0];
				ClsMngBBDD.callPLProcedure("{call PROC_RECONFIGURACIONCONTADOR (?,?)}", 2, paramIn);
				reinicio = false;
			}
		    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		}

		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
}