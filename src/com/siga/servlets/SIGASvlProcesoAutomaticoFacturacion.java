package com.siga.servlets;

import java.net.URL;
import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletException;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */
public class SIGASvlProcesoAutomaticoFacturacion extends SIGAServletAdapter implements NotificationListener
{
	private static final long serialVersionUID = 1L;
    private static long duracionIntervaloEnms = 1;
	private static long duracionMediaFacturacionEnminutos = 5;
	private static String sNombreProceso = "ProcesoAutomaticoFacturacion";
    private static String urlSiga = "";
	
	public static long getDuracionIntervaloEnminutos()	{
		return duracionIntervaloEnms / 60;
	}
	public static long getDuracionMediaFacturacionEnminutos()	{
		return duracionMediaFacturacionEnminutos;
	}

	private Timer timer;
	private Integer idNotificacion;

  public void init() throws ServletException {
	  super.init();

    //ClsLogging.writeFileLogWithoutSession("", 3);
  	//ClsLogging.writeFileLogWithoutSession("", 3);
    ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

    
    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
    String sIntervaloAux = properties.returnProperty("facturacion.programacionAutomatica.tiempo.ciclo");
    String sIntervalo = sIntervaloAux;

    urlSiga = properties.returnProperty("general.urlSIGA");
    
    if (sIntervalo==null || sIntervalo.trim().equals("")) {
        sIntervalo="0";
    }

    if (sIntervalo.equals("0")) {
        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervaloAux + ").", 3);
        
    } else {
        duracionIntervaloEnms = Long.parseLong(sIntervalo)*60*1000;

        timer = new Timer();
        timer.addNotificationListener(this, null, sNombreProceso);

        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, duracionIntervaloEnms);

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

    try {
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
        
	} catch (InstanceNotFoundException e) {
        e.printStackTrace();
    }
 }

 public void handleNotification(Notification notif, Object handback) {
	 ClsLogging.writeFileLogWithoutSession("SIGASvlProcesoAutomaticoFacturacion.handleNotification() - INICIO", 3);
	 ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

	try {
	    // invocamos al servlet
		URL url = new URL(urlSiga+ "SIGASvlProcesoFacturacion.svrl");
	    Object ret = url.getContent();
	    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
	    //ClsLogging.writeFileLogWithoutSession(" - OK. >>>  Ejecutando Notificación: \"" + urlSiga+ "SIGASvlProcesoFacturacion.svrl" + "\"", 3);
	    ClsLogging.writeFileLogWithoutSession("SIGASvlProcesoAutomaticoFacturacion.handleNotification() - FIN", 3);
	    
	} catch(Exception e) {
	    ClsLogging.writeFileLogWithoutSession("ERROR - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
	    ClsLogging.writeFileLogWithoutSession("SIGASvlProcesoAutomaticoFacturacion.handleNotification() - FIN", 3);
	    e.printStackTrace();
	}
 }
	
}
