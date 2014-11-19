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
 * Servlet del proceso automatico de facturacion. Utiliza a su vez al servlet SIGASvlProcesoFacturacion
 */
public class SIGASvlProcesoAutomaticoFacturacion extends SIGAServletAdapter implements NotificationListener
{
	private static final long serialVersionUID = 1L;
	
	private static Timer timer;
	private static Integer idNotificacion;
	private static String sNombreProceso = "ProcesoAutomaticoFacturacion";

	/**
	 * Inicializacion del servidor: se ejecuta cuando se inicia/arranca el servidor
	 */
	public void init() throws ServletException
	{
		super.init();

		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

		iniciarTimer();
		
		ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
	}

	/**
	 * Reinicia el timer para que se vuelva a ejecutar el proceso automatico de facturacion pasado un tiempo.
	 * El tiempo se define por una propiedad interna, que es modificable por BD.
	 * 
	 * Se ejecuta al inicio del servidor y cada vez que termina cada pasada del proceso (SIGASvlProcesoFacturacion)
	 * @throws ServletException
	 */
	public void iniciarTimer() throws ServletException
	{
		ReadProperties properties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sIntervalo = properties.returnProperty("facturacion.programacionAutomatica.tiempo.ciclo");

		if (sIntervalo == null || sIntervalo.trim().equals("") || sIntervalo.trim().equals("0")) {
			ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
			ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervalo + ").", 3);

		} else {
			try {
				if (timer != null) {
					// desactivando notificacion anterior
					if (timer.isActive())
						timer.stop();
					timer.removeNotification(idNotificacion);
				}
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			} finally { // aunque falle la desactivacion de la notificacion anterior, creamos la nueva notificacion
				timer = new Timer();
				timer.addNotificationListener(this, null, sNombreProceso);
	
				long lIntervalo = Long.parseLong(sIntervalo) * 60 * 1000;
				Date timerTriggerAt = new Date();
				timerTriggerAt = new Date(timerTriggerAt.getTime() + lIntervalo);
				idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt);
	
				timer.start();
	
				ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
				ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
			}
		}
	}

	/**
	 * Este metodo se ejecuta cuando el timer termina y lanza este proceso de nuevo.
	 * Solo sirve para dejar registro en LOG.
	 * 
	 * Nota: no se puede utilizar para reiniciar el timer, ya que no sabemos cuando termina el proceso
	 */
	public void handleNotification(Notification notif, Object handback)
	{
		ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + ".", 3);

		try {
			// invocando al servlet
			ReadProperties properties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String urlSiga = properties.returnProperty("general.urlSIGA");
			URL url = new URL(urlSiga + "SIGASvlProcesoFacturacion.svrl");
			url.getContent();
			
			// registrando el exito en la invocacion
			ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

		} catch (Exception e) {
			// registrando el error en la invocacion
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
			e.printStackTrace();
		}
	}

	/**
	 * Finalizacion del servlet: se ejecuta cuando se termina/apaga el servidor
	 */
	public void destroy()
	{
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

		try {
			if (timer != null) {
				// desactivando notificacion anterior
				if (timer.isActive())
					timer.stop();
				timer.removeNotification(idNotificacion);
			}

			ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
			ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
			ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);

		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
	}
}
