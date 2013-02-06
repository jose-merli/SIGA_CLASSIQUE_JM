package com.siga.servlets;

import java.util.*;
import javax.servlet.*;
import com.atos.utils.*;
import com.siga.beans.*;
import javax.management.*;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.siga.Utilidades.*;

import weblogic.management.timer.Timer;

public final class SIGASvlProcesoAutomaticoCenso  extends SIGAContextListenerAdapter implements ServletContextListener, NotificationListener
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private long lIntervalo24 = 1;
    private String sNombreProceso = "ProcesoAutomaticoCenso";
    private String sNombreProceso24 = "ProcesoAutomaticoCenso24h";

    public void contextInitialized(ServletContextEvent event)
    {
    	//aalg: inc 504. Modificado para incluir una tarea que se ejecute 1 vez al día para modificar el estadoejercicio según el 
    	//   en el que se encuentra a la fecha actual (de entre los almacenados en cen_datoscolegiadosestados
    	super.contextInitialized(event);
    	
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//        ReadProperties properties=new ReadProperties("SIGA.properties");
        String sIntervaloAux = properties.returnProperty("administracion.factualizables.tiempo.ciclo");
        String sIntervalo = sIntervaloAux;

        String sIntervalo24 = properties.returnProperty("censo.programacionAutomatica.tiempo.ciclo2");
        
        if (sIntervalo==null || sIntervalo.trim().equals(""))
            sIntervalo="0";
        
        if (sIntervalo24==null || sIntervalo24.trim().equals(""))
        	sIntervalo24="0";

        if (sIntervalo.equals("0")) {
	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervaloAux + ").", 3);
        }
        else if (sIntervalo24.equals("0")){
        	ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso24 + "\" no arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervalo24 + ").", 3);
        }
        else
        {
	        lIntervalo = Long.parseLong(sIntervalo)*60*1000;
	        
	        lIntervalo24 = Long.parseLong(sIntervalo24)*60*1000;   

	        timer = new Timer();
	        timer.addNotificationListener(this, null, sNombreProceso);
	        

	        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
	        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);
	        //aalg: inc 504. se añade una notificación para que avise cada 24h
	        idNotificacion = timer.addNotification(sNombreProceso24, sNombreProceso24, this, timerTriggerAt, lIntervalo24);
	        
	        timer.start();

	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
	        
	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso24 + "\" arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: 24 horas.", 3);
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
		String sProximaEjecucion24 = "Próxima ejecución dentro de 24h";
        


		try
		{
		    UsrBean us = new UsrBean();
		    us.setUserName(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString());
			CenInstitucionAdm adm2 = new CenInstitucionAdm (us);
			Vector vInstituciones = adm2.getInstitucionesActAutomatica();
			
			if (notif.getMessage().equals(sNombreProceso)){
				ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
			    if (vInstituciones != null) {
			        for (int i = 0; i < vInstituciones.size(); i++)  {
			        	
			        	Integer idInstitucion = UtilidadesHash.getInteger((Hashtable)vInstituciones.get(i), "INSTITUCION");
			        	String  idioma        = UtilidadesHash.getString ((Hashtable)vInstituciones.get(i), "LENGUAJE");
			        	
			            GestorSolicitudes gs = new GestorSolicitudes();
			            gs.GestionAutomaticaSolicitudes(idInstitucion, idioma);
			            
			        }
			    }
			    ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificación: \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
			} else if (notif.getMessage().equals(sNombreProceso24)) {
				//aalg: inc 504. Para actualizar el campo SITUACIONEJERCICIO de cen_colegiado
				ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso24 + "\".", 3);
	            GestorColegiados gc = new GestorColegiados();
	            gc.GestionAutomaticaColegiados();	
	            ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificación: \"" + sNombreProceso24 + "\" ejecutada OK. " + sProximaEjecucion24, 3);
			
			}
		    
		}
		catch(Exception e)
		{
		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
}