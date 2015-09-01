package com.siga.servlets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.GEN_PROPERTIES;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.cen.CenSancionService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.services.helper.SigaServiceHelperService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;
/**
 * @author Carlos Ruano Martínez 
 * @date 25/05/2015
 * 
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener extends SIGAListenerAbstract {
	
	private static final Logger log = Logger.getLogger(SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener.class);

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.ENVIO_CORREO_SANCIONES_CGAE_DIA_HORA;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.ENVIO_CORREO_SANCIONES_CGAE_INTERVALO_DIAS;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.ENVIO_CORREO_SANCIONES_CGAE_ACTIVAR_LISTENER;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.CEN;
	}

	@Override
	protected void execute(Short idInstitucion) {
		try {
			log.info("Ejecutando el Listener de SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener");
			CenSancionService sancionesService = (CenSancionService) BusinessManager.getInstance().getService(CenSancionService.class);	
			
			/** PRIMERO REALIZAMOS EL ENVIO PERIODICO DE LOS CAMBIOS EN SANCIONES **/
			log.info("SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener - Realizando el ENVIO PERIODICO DE LOS CAMBIOS EN SANCIONES");
			sancionesService.enviarEmailPeriodicoSanciones();
			
			/** ARCHIVAMOS LAS SANCIONES REHABILITADAS **/
			log.info("SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener - Realizando el ARCHIVO DE LAS SANCIONES REHABILITADAS");
			sancionesService.archivarSancionesRehabilitadas();
			
			log.info("SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener - Fin de las operaciones para las sanciones de letrados");			

		} catch (Exception e) {
			log.error("Error ejecutar el Listener de SIGASvlEnvioCorreoPeriodicoSancionesCGAEListener", e);
		}
	}
	
}