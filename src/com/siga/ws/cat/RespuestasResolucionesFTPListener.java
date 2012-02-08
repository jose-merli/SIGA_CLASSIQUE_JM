/**
 * 
 */
package com.siga.ws.cat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.JhDate;
import com.atos.utils.UsrBean;
import com.ibm.icu.util.Calendar;
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
	private long intervalo = 3600000;
	private String horaMinuto = null;
	
	private final static String PCAJG_RESOL_FTP_TIMER_HORA = "PCAJG_RESOL_FTP_TIMER_HORA";
	private final static String PCAJG_RESP_RESOL_FTP_TIMER_HORAS = "PCAJG_RESP_RESOL_FTP_TIMER_HORAS";
	private final static String PCAJG_RESP_RESOL_FTP_ACTIVO = "PCAJG_RESP_RESOL_FTP_ACTIVO";
	
	private final static String MODULO_SCS = "SCS";
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		try {			
			iniciaTimer();				
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Se ha producido un error al inicializar el TIMER de FTP Cat", e, 3);
		}
    }
	
	
	private void iniciaTimer() throws Exception {
		timer = new Timer();
		
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
		String[] horaMinutos = null;
		String valor = "00:00";
		try {
			valor = admParametros.getValor(""+ClsConstants.INSTITUCION_POR_DEFECTO, MODULO_SCS, PCAJG_RESOL_FTP_TIMER_HORA, valor);
			
			if (valor != null && valor.length()==5 && !valor.equals("00:00")) {
				JhDate valida = new JhDate();
				horaMinutos = valor.split(":");
				valida.validaHora(Integer.parseInt(horaMinutos[0]), Integer.parseInt(horaMinutos[1]));
			}
			
		} catch (Exception e) {
			ClsLogging.writeFileLogWithoutSession("Parametro "+PCAJG_RESOL_FTP_TIMER_HORA+" mal configurado. Se pone 00:00");
			valor = "00:00";
		}
		this.horaMinuto = valor;
		horaMinutos = valor.split(":");
		
		Calendar horaCal = Calendar.getInstance();
        horaCal.set(Calendar.DAY_OF_YEAR, horaCal.get(Calendar.DAY_OF_YEAR)+1);
        horaCal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(horaMinutos[0]));
        horaCal.set(Calendar.MINUTE,Integer.parseInt(horaMinutos[1]));
        Date timerTriggerAt = horaCal.getTime();
		
        timer.addNotificationListener(this, null, nombreProceso);        
        idNotificacion = timer.addNotification(nombreProceso, nombreProceso, this, timerTriggerAt, intervalo);
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
			String valor = admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, MODULO_SCS, PCAJG_RESP_RESOL_FTP_TIMER_HORAS, "");
			if (valor == null || valor.trim().equals("")) {
				throw new Exception("El parámetro " + PCAJG_RESP_RESOL_FTP_TIMER_HORAS + " no existe o no es un número válido");
			}
			double horas = Double.parseDouble(valor);
			if (!(horas > 0)) {
				throw new Exception("El parámetro " + PCAJG_RESP_RESOL_FTP_TIMER_HORAS + " debe tener un valor mayor que cero.");
			}
			
			long nuevoTimer = (long) (horas * 3600000);			
			String[] horaMinutos = null;
			String horaMinutoActual = "00:00";
			try {
				horaMinutoActual = admParametros.getValor(""+ClsConstants.INSTITUCION_POR_DEFECTO, MODULO_SCS, PCAJG_RESOL_FTP_TIMER_HORA, horaMinutoActual);
				
				if (horaMinutoActual != null && horaMinutoActual.length()==5 && !horaMinutoActual.equals("00:00")) {
					JhDate valida = new JhDate();
					horaMinutos = horaMinutoActual.split(":");
					valida.validaHora(Integer.parseInt(horaMinutos[0]), Integer.parseInt(horaMinutos[1]));
				}
				
			} catch (Exception e) {
				ClsLogging.writeFileLogWithoutSession("Parametro "+PCAJG_RESOL_FTP_TIMER_HORA+" mal configurado. Se pone 00:00");
				valor = "00:00";
			}
			
			if (nuevoTimer != intervalo||!horaMinutoActual.equals(this.horaMinuto)) {
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
