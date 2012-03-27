package com.siga.general;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.ibm.icu.util.Calendar;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.servlets.SIGAContextListenerAdapter;

public abstract class SIGAListenerAbstract extends SIGAContextListenerAdapter implements NotificationListener {
	
	
	
	/**
	 * Parámetro de la tabla GEN_PARAMETROS que indica la hora de inicio del listener para la institución CGAE (2000) y módulo SCS
	 * @return
	 */
	protected abstract String getFechaHoraInicioParam();
	
	/**
	 * Parámetro de la tabla GEN_PARAMETROS que indica el intervalo en horas que se debe ejecutar el listener para la institución CGAE (2000) y módulo SCS
	 * @return
	 */
	protected abstract String getDiasIntervaloParam();
	
	/**
	 * Parámetro de la tabla GEN_PARAMETROS que indica si el listener está activo para una institución en concreto. Módulo SCS
	 * @return
	 */
	protected abstract String getActivoParam();
	
	/**
	 * Método que se llamará cada x tiempo siendo x el valor del intervalo del listener.
	 * @return
	 */
	protected abstract void execute(UsrBean usrBean, String idInstitucion) throws Exception;
	
	
	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "SIGAListenerAbstract";
	private String valorFechaHoraInicio = null;
	private boolean configuracionCorrecta = false;
	private static final int valorDiasPorDefecto = 1;
	private SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_MEDIUM_SPANISH);//dd/MM/yyyy HH:mm
	private static final int PERIODICIDAD = Calendar.DAY_OF_YEAR;
	
	private int CONFIGURACION_HORAS = 3;
	private int CONFIGURACION_MINUTOS = 0;
	
	
	private final static String MODULO_SCS = "SCS";
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		try {			
			iniciaTimer();				
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Se ha producido un error al inicializar el TIMER " + this.getClass(), e, 3);
		}
    }
	
	
	private void iniciaTimer() throws Exception {
		timer = new Timer();
		
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
			
		Date date = null;
		
		
		try {
			valorFechaHoraInicio = admParametros.getValor(String.valueOf(ClsConstants.INSTITUCION_CGAE), MODULO_SCS, getFechaHoraInicioParam(), "");			
			date = sdf.parse(valorFechaHoraInicio);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			CONFIGURACION_HORAS = cal.get(Calendar.HOUR_OF_DAY);
			CONFIGURACION_MINUTOS = cal.get(Calendar.MINUTE);
			configuracionCorrecta = true;
			
		} catch (Exception e) {
			ClsLogging.writeFileLogWithoutSession("No se ha podido recuperar correctamente el parámetro " + getFechaHoraInicioParam() + ". Valor = \"" + valorFechaHoraInicio + "\" . " +
					"Compruebe que el formato de fecha y hora (" + ClsConstants.DATE_FORMAT_MEDIUM_SPANISH + ") sea correcto.");
			Calendar cal = Calendar.getInstance();
			cal.add(PERIODICIDAD, valorDiasPorDefecto);
			date = cal.getTime();
			valorFechaHoraInicio = null;
			configuracionCorrecta = false;
		}
				
        timer.addNotificationListener(this, null, nombreProceso);        
        idNotificacion = timer.addNotification(nombreProceso, nombreProceso, configuracionCorrecta, date);
        timer.start();
	}
	


	/**
	 * 
	 */
	public void handleNotification(Notification notification, Object handback) {
				
		try {
			
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));		
			GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
			
			String valor = admParametros.getValor(String.valueOf(ClsConstants.INSTITUCION_CGAE), MODULO_SCS, getFechaHoraInicioParam(), "");
			
			if (valorFechaHoraInicio != null && valorFechaHoraInicio.trim().equals(valor) && configuracionCorrecta) {				
				Hashtable hashtable = admParametros.getValores(MODULO_SCS, getActivoParam());
				if (hashtable != null) {
					Enumeration enumeration = hashtable.keys();
					while (enumeration.hasMoreElements()) {
						String idInstitucion = enumeration.nextElement().toString();
						String activo = hashtable.get(idInstitucion).toString();
						if ("1".equals(activo)) {							
							try {
								execute(usrBean, idInstitucion);
							} catch (Exception e) {
								ClsLogging.writeFileLogError("Se ha producido un error al ejecutar el listener para la el colegio " + idInstitucion, e, 3);
							}								
						}
					}						
				}				
				actualizaProximaEjecucion(admParametros);	
			}		
			
			pararTimer();
			iniciaTimer();
					
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en handleNotification", e, 3);
		}
	}
	
	private void actualizaProximaEjecucion(GenParametrosAdm admParametros) throws Exception {
		//proxima ejecución
		String valorDias = admParametros.getValor(String.valueOf(ClsConstants.INSTITUCION_CGAE), MODULO_SCS, getDiasIntervaloParam(), String.valueOf(valorDiasPorDefecto));
		
		int numeroDias = valorDiasPorDefecto; 				
		try {
			numeroDias = Integer.parseInt(valorDias);
			if (numeroDias < valorDiasPorDefecto) {
				numeroDias = valorDiasPorDefecto;
			}
		} catch (Exception e) {
			// si ocurre un error al transformar a int
			ClsLogging.writeFileLogError("Se ha producido un error al recuperar el parámetro " + getDiasIntervaloParam() + " y tratar de pasarlo a número entero", e, 3);
			numeroDias = valorDiasPorDefecto;				
		}
		
		Hashtable hash = new Hashtable();
		hash.put(GenParametrosBean.C_IDINSTITUCION, ClsConstants.INSTITUCION_CGAE);
		hash.put(GenParametrosBean.C_MODULO, MODULO_SCS);
		hash.put(GenParametrosBean.C_PARAMETRO, getFechaHoraInicioParam());
		
		Vector v = admParametros.selectByPK(hash);
		if (v == null || v.size() != 1) {			
			throw new ClsExceptions("El parámetro " + getFechaHoraInicioParam() + " no existe en bdd");
			
		}
		GenParametrosBean genParametrosBean = (GenParametrosBean)v.get(0);
		
		Calendar cal = Calendar.getInstance();
		if (PERIODICIDAD != Calendar.MINUTE) { //para debug
			cal.set(Calendar.HOUR_OF_DAY, CONFIGURACION_HORAS);
			cal.set(Calendar.MINUTE, CONFIGURACION_MINUTOS);
		}
		cal.add(PERIODICIDAD, numeroDias);
		
		String value = sdf.format(cal.getTime());
		genParametrosBean.setValor(value);
		
		admParametros.update(genParametrosBean);
		
	}

	private void pararTimer(){
		if (timer != null) {
			if (timer.isActive()) {
				timer.stop();
			}
			try {
				timer.removeNotification(idNotificacion);
			} catch (InstanceNotFoundException e) {
				ClsLogging.writeFileLogError("Error en contextDestroyed", e, 3);
			}
		}
	}
	
	/**
	 * 
	 */
	public void contextDestroyed(ServletContextEvent event) {
		pararTimer();		
	}
}
