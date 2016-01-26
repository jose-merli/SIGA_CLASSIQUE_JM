package com.siga.general;

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
import com.atos.utils.ClsLogging;
import com.ibm.icu.util.Calendar;
import com.siga.servlets.SIGAContextListenerAdapter;

import es.satec.businessManager.BusinessManager;

public abstract class SIGAListenerPorMinutosAbstract extends SIGAContextListenerAdapter implements NotificationListener
{

	private static final Logger log = Logger.getLogger(SIGAListenerAbstract.class);

	/**
	 * Parámetro de la tabla GEN_PARAMETROS que indica los minutos de frecuencia que debe mantener el listener
	 */
	protected abstract PARAMETRO getMinutosIntervaloParam();
	/**
	 * Parámetro de la tabla GEN_PARAMETROS que indica si el listener está activo para una institución en concreto
	 */
	protected abstract PARAMETRO getActivoParam();
	/**
	 * Módulo al que pertence el parámetro
	 */
	protected abstract MODULO getModulo();
	/**
	 * Método que se llamará cada x tiempo siendo x el valor del intervalo del listener.
	 */
	protected abstract void execute(Short idInstitucion);

	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "SIGAListenerPorMinutosAbstract";
	private static final int PERIODICIDAD = Calendar.MINUTE;
	private static final int valorMinutosPorDefecto = 60;

	/**
	 * Llamado en el inicio de la aplicacion
	 */
	public void contextInitialized(ServletContextEvent event)
	{
		super.contextInitialized(event);
		iniciaTimer();
	}

	/**
	 * Crea e inicia un nuevo timer y lo asocia al presente objeto
	 */
	private void iniciaTimer()
	{
		// obteniendo el parametro de tiempo para crear el timer
		GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		GenParametrosExample genParametrosExample = new GenParametrosExample();
		genParametrosExample.createCriteria()
				.andIdinstitucionEqualTo((short) ClsConstants.INSTITUCION_CGAE)
				.andModuloEqualTo(getModulo().name())
				.andParametroEqualTo(getMinutosIntervaloParam().name());
		List<GenParametros> genParametros = parametersService.getList(genParametrosExample);

		// calculando el momento de lanzamiento del timer
		Date date = null;
		Calendar cal = Calendar.getInstance();

		try {
			if (genParametros == null || genParametros.size() != 1) {
				throw new IllegalArgumentException("No se ha encontrado el parámetro");
			}

			cal.add(PERIODICIDAD, Integer.parseInt(genParametros.get(0).getValor()));

		} catch (Exception e) {
			log.error("No se ha podido recuperar correctamente el parámetro " + getMinutosIntervaloParam().name()
					+ ". Se utilizará el valor por defecto = \"" + valorMinutosPorDefecto + "\" . "
					+ "El parámetro ha de ser un entero que indique el número de minutos de frecuencia.");
			cal.add(PERIODICIDAD, valorMinutosPorDefecto);
			
		} finally {
			date = cal.getTime();
		}

		// creando el timer
		try {
			timer = new Timer();
			timer.addNotificationListener(this, null, nombreProceso);
			idNotificacion = timer.addNotification(nombreProceso, nombreProceso, this, date);
			timer.start();
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Se ha producido un error al inicializar el TIMER " + this.getClass(), e, 3);
		}
	}

	/**
	 * Ejecuta el proceso para cada institucion donde este activo el parametro
	 */
	public void handleNotification(Notification notification, Object handback)
	{
		try {
			// obteniendo los parametros de activacion por cada institucion
			GenParametrosService parametersService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			GenParametrosExample genParametrosExample = new GenParametrosExample();
			genParametrosExample.createCriteria()
					.andModuloEqualTo(getModulo().name())
					.andParametroEqualTo(getActivoParam().name());
			List<GenParametros> genParametros = parametersService.getList(genParametrosExample);

			// ejecutando el proceso por cada institucion
			if (genParametros != null) {
				for (GenParametros genParametro : genParametros) {
					if ("1".equals(genParametro.getValor())) {
						ClsLogging.writeFileLog(nombreProceso + "Ejecutando para la institucion " + genParametro.getIdinstitucion(), 3);
						execute(genParametro.getIdinstitucion());
					}
				}
				//TODO Falta comprobar si el parametro generico (0) esta activado: en dicho caso, entonces tendria que ejecutarse para todas las instituciones
				// Es mas, creo que esto esta mal: tendria que comprobarse el parametro como se hace en otros sitios, por institucion
				// Tb lo ideal seria restringir la aparicion de los botones de acciones masivas
			}

		} catch (Exception e) {
			log.error("Error en el listener que maneja el parámetro " + getActivoParam(), e);
			ClsLogging.writeFileLogError("Error en handleNotification", e, 3);
		} finally {
			pararTimer();
			iniciaTimer();
		}
	}

	/**
	 * Para el timer creado anteriormente
	 */
	private void pararTimer()
	{
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
	 * Llamado en el fin de la aplicacion
	 */
	public void contextDestroyed(ServletContextEvent event)
	{
		pararTimer();
	}
}
