/**
 * 
 */
package com.siga.eejg;

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;
import com.siga.servlets.SIGAContextListenerAdapter;

/**
 * @author angelcpe
 *
 */
public class SolicitudEEJGListener extends SIGAContextListenerAdapter implements NotificationListener {
	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "SolicitudEEJGListener";
	private long intervalo = -1;
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		intervalo = Long.parseLong(rp.returnProperty("eejg.timerEEJG"));
		
		timer = new Timer();
        timer.addNotificationListener(this, null, nombreProceso);        
        idNotificacion = timer.addNotification(nombreProceso, nombreProceso, this, new Date(), intervalo);
        timer.start();
    }
	
	/**
	 * 
	 */
	public void contextDestroyed(ServletContextEvent event) {

		if (timer != null) {
			if (timer.isActive()) {
				timer.stop();
			}
			try {
				timer.removeNotification(idNotificacion);
			} catch (InstanceNotFoundException e) {
				ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.contextDestroyed", e, 3);
			}
		}
	}

	/**
	 * 
	 */
	public void handleNotification(Notification notification, Object handback) {
				
		try {
			InformacionEconomicaEjg gstInformacionEejg = new InformacionEconomicaEjg();
			gstInformacionEejg.tratarSolicitudesEejg();		
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.handleNotification", e, 3);
		}
	}
}
