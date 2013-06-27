package com.siga.ws;

import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.log4j.Logger;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;

public class SIGAWSListener implements NotificationListener {
	
	private Integer idNotificacion;
	private static Logger log = Logger.getLogger(SIGAWSListener.class);
	
	/**
	 * Lista para guardar las remesas que se estan ejecutando
	 */
	private static List<String> ejecutandose = new ArrayList<String>();
		
	
	/**
	 * Metodo encargado de ejecutar nuestra clase de negocio
	 */
	public void handleNotification(Notification notification, Object handback) {
		
		String key = null;
		SIGAWSClientAbstract sigaWSClient = null;
		log.debug("Notificación recibida...");
		
		try {
			Object obj = notification.getUserData();
			if (obj == null || !(obj instanceof SIGAWSClientAbstract)) {
				throw new IllegalArgumentException("El objeto UserData es nulo o no es del tipo esperado");
			}			
			
			sigaWSClient = (SIGAWSClientAbstract) obj;
			key = getKey(sigaWSClient.getIdInstitucion(), sigaWSClient.getIdRemesa());
			
			ejecutandose.add(key);			
			sigaWSClient.execute();
			log.debug("Se ha ejecutado correctamente");
			
			// paramos la ejecución
			if (handback != null && handback instanceof Timer) {
				Timer timer = (Timer) handback;
				try {
					timer.removeNotification(idNotificacion);
				} catch (InstanceNotFoundException e) {
					ClsLogging.writeFileLogError("Error en el timer de " + this.getClass(), e, 3);
				}
				if (timer.isActive()) {
					timer.stop();
				}
			}

		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR en el envío WebService", e, 3);			
		} finally {
			if (key != null) {
				ejecutandose.remove(key);
			}
			if (sigaWSClient != null) {
				sigaWSClient.closeLogFile();
			}
		}
	}
	
	/**
	 * Metodo para saber si se esta ejecutando una determinada remesa de una institucion
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	public static boolean isEjecutandose(int idInstitucion, int idRemesa) {
		String keyMap = getKey(idInstitucion, idRemesa);
		return ejecutandose.contains(keyMap);
	}

	/**
	 * Para construir el objeto key que se meterá en la lista de ejecutandose
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	private static String getKey(int idInstitucion, int idRemesa) {
		String key = idInstitucion + "-" + idRemesa; 
		log.debug("La key es " + key);
		return key;
	}

	/**
	 * @return the idNotificacion
	 */
	public Integer getIdNotificacion() {
		return idNotificacion;
	}

	/**
	 * @param idNotificacion the idNotificacion to set
	 */
	public void setIdNotificacion(Integer idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

}
