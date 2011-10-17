/**
 * 
 */
package com.siga.ws.cat;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.servlets.SIGAContextListenerAdapter;

/**
 * @author angelcpe
 *
 */
public class RespuestasResolucionesFTPListener extends SIGAContextListenerAdapter implements NotificationListener {
	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "RespuestasResolucionesFTPListener";
	private long intervalo = 1;
	
	private final static String PCAJG_RESP_RESOL_FTP_TIMER_HORAS = "PCAJG_RESP_RESOL_FTP_TIMER_HORAS";
	private final static String PCAJG_RESP_RESOL_FTP_ACTIVO = "PCAJG_RESP_RESOL_FTP_ACTIVO";
	
	private final static String MODULO_SCS = "SCS";
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
                
		try {			
			iniciaTimer();				
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Se ha producido un error al inicializar el TIMER de EEJG", e, 3);
		}
    }
	
	
	private void iniciaTimer() {
		timer = new Timer();
        timer.addNotificationListener(this, null, nombreProceso);        
        idNotificacion = timer.addNotification(nombreProceso, nombreProceso, this, new Date(), intervalo);
        timer.start();
	}
	
	
	private void pararTimer(){
		if (timer != null) {
			if (timer.isActive()) {
				timer.stop();
			}
			try {
				timer.removeNotification(idNotificacion);
			} catch (InstanceNotFoundException e) {
				ClsLogging.writeFileLogError("Error en RespuestasResolucionesFTPListener.contextDestroyed", e, 3);
			}
		}
	}
	
	/**
	 * 
	 */
	public void contextDestroyed(ServletContextEvent event) {
		pararTimer();		
	}

	/**
	 * 
	 */
	public void handleNotification(Notification notification, Object handback) {
				
		try {
			ClsLogging.writeFileLog("Ejecutando listener de obtener respuestas y resoluciones de los colegios catalanes por FTP", 3);
			
			//miramos si ha cambiado el intervalo en bdd. Asi haremos el cambio en caliente (pedido por LP)
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
			GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
			double horas = Double.parseDouble(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, MODULO_SCS, PCAJG_RESP_RESOL_FTP_TIMER_HORAS, ""));
			long nuevoTimer = (long) (horas * 3600000);			
			
			if (nuevoTimer != intervalo) {
				intervalo = nuevoTimer;
				pararTimer();
				iniciaTimer();
			} else {
				PCAJGxmlResponse pcajgXmlResponse = new PCAJGxmlResponse();
				
				pcajgXmlResponse.setUsrBean(usrBean);
				pcajgXmlResponse.setIdRemesa(Integer.MAX_VALUE);
				pcajgXmlResponse.setSimular(false);		
				
				Hashtable hashtable = admParametros.getValores(MODULO_SCS, PCAJG_RESP_RESOL_FTP_ACTIVO);
				if (hashtable != null) {
					Enumeration enumeration = hashtable.keys();
					while (enumeration.hasMoreElements()) {
						String idInstitucion = enumeration.nextElement().toString();
						String activo = hashtable.get(idInstitucion).toString();
						if ("1".equals(activo)) {
							pcajgXmlResponse.setIdInstitucion(Integer.parseInt(idInstitucion));
							try {
								pcajgXmlResponse.execute();
							} catch (Exception e) {
								ClsLogging.writeFileLogError("Se ha producido un error al tratar las respuestas o resoluciones de la institución " + idInstitucion, e, 3);
							}								
						}
					}						
				}
			}			
					
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en RespuestasResolucionesFTPListener.handleNotification", e, 3);
		}
	}
}
