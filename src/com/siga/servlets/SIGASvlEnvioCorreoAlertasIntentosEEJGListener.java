package com.siga.servlets;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.helper.SigaServiceHelperService;
import org.redabogacia.sigaservices.app.services.scs.ScsEEJGPeticionesService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;
/**
 * @author Carlos Ruano Martínez 
 * @date 18/08/2015
 * 
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class SIGASvlEnvioCorreoAlertasIntentosEEJGListener extends SIGAListenerAbstract {
	
	private static final Logger log = Logger.getLogger(SIGASvlEnvioCorreoAlertasIntentosEEJGListener.class);

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.EEJG_ENVIOMAIL_ALERTAS_DIA_HORA;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.EEJG_ENVIOMAIL_ALERTAS_INTERVALO_DIAS;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.EEJG_ENVIOMAIL_ALERTAS_ACTIVO;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.SCS;
	}

	@Override
	protected void execute(Short idInstitucion) {
		try {
			log.info("Ejecutando el Listener de SIGASvlEnvioCorreoAlertasIntentosEEJGListener");
			ScsEEJGPeticionesService service = (ScsEEJGPeticionesService) BusinessManager.getInstance().getService(ScsEEJGPeticionesService.class);
			
			/** Si hay alguna peticion que lleva ya 3 reintentos de consulta se envia un mail informando sobre ello **/
			log.info("SIGASvlEnvioCorreoAlertasIntentosEEJGListener - Realizando el ENVIO DIARIO DE ALERTAS DE INTENTOS EN EEJG");
			service.enviarEmailDiarioAlertasIntentosEEJG();	
			
			log.info("SIGASvlEnvioCorreoAlertasIntentosEEJGListener - Fin de las operaciones para el envio diario de alertas por numero maximo de intentos");			

		} catch (Exception e) {
			log.error("Error ejecutar el Listener de SIGASvlEnvioCorreoAlertasIntentosEEJGListener", e);
		}
	}
	
}