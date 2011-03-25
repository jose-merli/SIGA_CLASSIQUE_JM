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
public class SolicitudEEJGListener extends SIGAContextListenerAdapter implements NotificationListener {
	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "SolicitudEEJGListener";
	private long intervalo = -1;
	private final static String EEJG_VALOR_ACTIVO = "1";
	private final static String EEJG_TIMER = "EEJG_TIMER";
	private final static String EEJG_ACTIVO = "EEJG_ACTIVO";
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
			intervalo = Long.parseLong(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, MODULO_SCS, EEJG_TIMER, ""));				
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
				ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.contextDestroyed", e, 3);
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
			//miramos si ha cambiado el intervalo en bdd. Asi haremos el cambio en caliente (pedido por LP)
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
			GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
			long nuevoTimer = Long.parseLong(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, MODULO_SCS, EEJG_TIMER, ""));
			if (nuevoTimer != intervalo) {
				intervalo = nuevoTimer;
				pararTimer();
				iniciaTimer();
			} else {
				String isActivo = admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, MODULO_SCS, EEJG_ACTIVO, EEJG_VALOR_ACTIVO); //METER ESTE PARAMETRO
				if (EEJG_VALOR_ACTIVO.equals(isActivo)) {
					InformacionEconomicaEjg gstInformacionEejg = new InformacionEconomicaEjg();
					ClsLogging.writeFileLog("Ejecutando listener de solicitudes iniciadas y pendientes de EEJG", 3);
					gstInformacionEejg.tratarSolicitudesEejg();
				}
			}			
					
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.handleNotification", e, 3);
		}
	}
}
