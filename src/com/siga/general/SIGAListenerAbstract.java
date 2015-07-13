package com.siga.general;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.autogen.model.GenParametrosExample;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.ibm.icu.util.Calendar;
import com.siga.servlets.SIGAContextListenerAdapter;

import es.satec.businessManager.BusinessManager;

public abstract class SIGAListenerAbstract extends SIGAContextListenerAdapter implements NotificationListener {
	
	
	private static final Logger log = Logger.getLogger(SIGAListenerAbstract.class);
	
	/**
	 * Par�metro de la tabla GEN_PARAMETROS que indica la hora de inicio del listener para la instituci�n 2000
	 * @return
	 */
	protected abstract PARAMETRO getFechaHoraInicioParam();
	
	/**
	 * Par�metro de la tabla GEN_PARAMETROS que indica el intervalo en horas que se debe ejecutar el listener
	 * @return
	 */
	protected abstract PARAMETRO getDiasIntervaloParam();
	
	/**
	 * Par�metro de la tabla GEN_PARAMETROS que indica si el listener est� activo para una instituci�n en concreto
	 * @return
	 */
	protected abstract PARAMETRO getActivoParam();
	
	/**
	 * M�dulo al que pertence el par�metro
	 * @return
	 */
	protected abstract MODULO getModulo();
	
	/**
	 * M�todo que se llamar� cada x tiempo siendo x el valor del intervalo del listener.
	 * @return
	 */
	protected abstract void execute(Short idInstitucion);
	
	
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
	
	
	private void iniciaTimer() {
		timer = new Timer();
		
		
		GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		GenParametrosExample genParametrosExample = new GenParametrosExample();
		genParametrosExample.createCriteria().andIdinstitucionEqualTo((short)ClsConstants.INSTITUCION_CGAE).andModuloEqualTo(getModulo().name()).andParametroEqualTo(getFechaHoraInicioParam().name());
		List<GenParametros> genParametros = parametersService.getList(genParametrosExample);
				
			
		Date date = null;
		
		
		try {
			if (genParametros == null || genParametros.size() != 1) {
				String error = "No se ha encontrado  correctamente el par�metro " + getFechaHoraInicioParam().name() + "."; 
				log.error(error);
				throw new IllegalArgumentException(error);
			}
			
			valorFechaHoraInicio = genParametros.get(0).getValor();			
			date = sdf.parse(valorFechaHoraInicio);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			CONFIGURACION_HORAS = cal.get(Calendar.HOUR_OF_DAY);
			CONFIGURACION_MINUTOS = cal.get(Calendar.MINUTE);
			configuracionCorrecta = true;
			
		} catch (Exception e) {
			log.error("No se ha podido recuperar correctamente el par�metro " + getFechaHoraInicioParam().name() + ". Valor = \"" + valorFechaHoraInicio + "\" . " +
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
			
			GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			GenParametrosExample genParametrosExample = new GenParametrosExample();
			genParametrosExample.createCriteria().andIdinstitucionEqualTo((short)ClsConstants.INSTITUCION_CGAE).andModuloEqualTo(getModulo().name()).andParametroEqualTo(getFechaHoraInicioParam().name());
			List<GenParametros> genParametros = parametersService.getList(genParametrosExample);
			
			if (genParametros == null || genParametros.size() != 1) {
				String error = "No se ha encontrado  correctamente el par�metro " + getFechaHoraInicioParam().name() + "."; 
				log.error(error);
				throw new IllegalArgumentException(error);
			}
			
			String valor = genParametros.get(0).getValor();
			
			if (valorFechaHoraInicio != null && valorFechaHoraInicio.trim().equals(valor) && configuracionCorrecta) {	
				genParametrosExample = new GenParametrosExample();
				genParametrosExample.createCriteria().andModuloEqualTo(getModulo().name()).andParametroEqualTo(getActivoParam().name());
				genParametros = parametersService.getList(genParametrosExample);
				
				if (genParametros != null) {
					for (GenParametros genParametro : genParametros) {
						if ("1".equals(genParametro.getValor())) {
							execute(genParametro.getIdinstitucion());
						}
					}
				}
									
				actualizaProximaEjecucion();	
			}		
			
			
					
		} catch (Exception e) {
			log.error("Error en el listener que maneja el par�metro " + getFechaHoraInicioParam(), e);
			ClsLogging.writeFileLogError("Error en handleNotification", e, 3);
		} finally {
			pararTimer();
			iniciaTimer();
		}
	}
	
	private void actualizaProximaEjecucion() throws Exception {
		
		short idInstitucion = (short)ClsConstants.INSTITUCION_CGAE;
		
		GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		GenParametrosExample genParametrosExample = new GenParametrosExample();
		genParametrosExample.createCriteria().andIdinstitucionEqualTo(idInstitucion).andModuloEqualTo(getModulo().name()).andParametroEqualTo(String.valueOf(getDiasIntervaloParam().name()));
		
		List<GenParametros> listaGenParametros = parametersService.getList(genParametrosExample);
		
		//proxima ejecuci�n
		String valorDias = String.valueOf(valorDiasPorDefecto);
		if (listaGenParametros == null || listaGenParametros.size() != 1) {
			String error = "No se ha encontrado  correctamente el par�metro " + String.valueOf(valorDiasPorDefecto) + "."; 
			log.error(error);
		} else {
			valorDias = listaGenParametros.get(0).getValor();
		}
		
		int numeroDias = valorDiasPorDefecto; 				
		try {
			numeroDias = Integer.parseInt(valorDias);
			if (numeroDias < valorDiasPorDefecto) {
				numeroDias = valorDiasPorDefecto;
			}
		} catch (Exception e) {
			// si ocurre un error al transformar a int
			log.error("Se ha producido un error al recuperar el par�metro " + getDiasIntervaloParam().name() + " y tratar de pasarlo a n�mero entero", e);
			numeroDias = valorDiasPorDefecto;				
		}		
		
		GenParametros genParametro = new GenParametros();
		genParametro.setIdinstitucion(idInstitucion);
		genParametro.setModulo(getModulo().name());
		genParametro.setParametro(getFechaHoraInicioParam().name());
		
		genParametro = parametersService.get(genParametro);		
		
		if (genParametro == null) {			
			throw new ClsExceptions("El par�metro " + getFechaHoraInicioParam().name() + " no existe en bdd");			
		}
		
		
		Calendar cal = Calendar.getInstance();
		if (PERIODICIDAD != Calendar.MINUTE) { //para debug
			cal.set(Calendar.HOUR_OF_DAY, CONFIGURACION_HORAS);
			cal.set(Calendar.MINUTE, CONFIGURACION_MINUTOS);
		}
		cal.add(PERIODICIDAD, numeroDias);
		
		String value = sdf.format(cal.getTime());
		genParametro.setValor(value);
		
		if (parametersService.update(genParametro) != 1) {
			throw new ClsExceptions(String.format("No se ha podido actualizar el par�metro %s para el colegio % y el m�dulo %s", getFechaHoraInicioParam().name(), idInstitucion, getModulo().name()));
		}
		
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
