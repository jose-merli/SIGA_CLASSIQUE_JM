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

public class SIGASvlProcesoAutomaticoEnvios extends SIGAServletAdapter implements NotificationListener {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6065534807424027785L;
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

    ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificaci�n: \"" + sNombreProceso + "\".", 3);

	try
	{
	    // invocamos al servlet
		URL url = new URL(urlSiga+ "SIGASvlProcesoEnvios.svrl");
	    Object ret = url.getContent();
	    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificaci�n: \"" + sNombreProceso + "\".", 3);
	    //ClsLogging.writeFileLogWithoutSession(" - EJECUTADO.  >>>  Ejecutando Notificaci�n: \"" + urlSiga+ "SIGASvlProcesoEnvios.svrl" + "\"", 3);
	    
	}
	catch(Exception e)
	{
	    ClsLogging.writeFileLogWithoutSession(" - Notificaci�n \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
	    e.printStackTrace();
	}

 } 

}


