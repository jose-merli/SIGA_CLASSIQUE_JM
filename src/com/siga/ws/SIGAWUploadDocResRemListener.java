package com.siga.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CajgRemesaResolucionAdm;
import com.siga.gratuita.form.DefinicionRemesaResolucionesCAJGForm;

/**
 * @author mjm
 *
 */
public class SIGAWUploadDocResRemListener implements NotificationListener {
	
	private Integer idNotificacion;
	private static Logger log = Logger.getLogger(SIGAWUploadDocResRemListener.class);
	
	/**
	 * Lista para guardar las remesas que se estan ejecutando
	 */
	private static List<String> ejecutandose = new ArrayList<String>();
		
	
	/**
	 * Metodo encargado de ejecutar nuestra clase de negocio
	 */
	public void handleNotification(Notification notification, Object handback) {
		
		String key = null;
		SigaWSUploadDocResRem sigaWSUploadDocResRem = null; 
		log.debug("Notificación recibida...");
		
		try {
			Object obj = notification.getUserData();
			if (obj == null || !(obj instanceof SigaWSUploadDocResRem)) {
				throw new IllegalArgumentException("El objeto UserData es nulo o no es del tipo esperado");
			}			
			
			sigaWSUploadDocResRem = (SigaWSUploadDocResRem) obj;
			key = getKey(sigaWSUploadDocResRem.getIdInstitucion(), sigaWSUploadDocResRem.getIdTipoRemesa());
			
			ejecutandose.add(key);			
			sigaWSUploadDocResRem.execute();
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
		}
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	protected static String getKeyMap (String idInstitucion, String idTipoRemesa) {
		return idInstitucion + "-" + idTipoRemesa;
	}

	/**
	 * Metodo para saber si se esta ejecutando una determinada remesa de una institucion
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	public static boolean isEjecutandose(int idInstitucion, int idTipoRemesa) {
		String keyMap = getKey(idInstitucion, idTipoRemesa);
		return ejecutandose.contains(keyMap);
	}

	/**
	 * Para construir el objeto key que se meterá en la lista de ejecutandose
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	private static String getKey(int idInstitucion, int idTipoRemesa) {
		String key = idInstitucion + "-" + idTipoRemesa; 
		log.debug("La key es " + key);
		return key;
	}
	
	/**
	 * @return the idTipoRemesa
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
