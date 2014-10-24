package com.siga.ws.mutualidad;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;
/**
 * @author Carlos Ruano Martínez 
 * @date 22/10/2014
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class MutualidadEnvioCertificadosFnalizadosListener extends SIGAListenerAbstract {
	
	private static final Logger log = Logger.getLogger(MutualidadEnvioCertificadosFnalizadosListener.class);

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.MUTUALIDAD_WS_ENVIO_CERTIFICADOS_DIA_HORA;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.MUTUALIDAD_WS_ENVIO_CERTIFICADOS_INTERVALO_DIAS;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.MUTUALIDAD_WS_ENVIO_CERTIFICADOS_ACTIVAR_LISTENER;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.ECOM;
	}

	@Override
	protected void execute(Short idInstitucion) {
		try {
			log.info("Ejecutando el Listener de Envio Certificados Finalizados a la Mutualidad");
			EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
			if (ecomColaService.insertaColaMutualidadCertificadosFinalizados(idInstitucion) != 1) {
				throw new Exception("No se ha podido insertar correctamente en la cola de envio de certificados finalizados para el colegio " + idInstitucion);
			}
		} catch (Exception e) {
			log.error("Error al insertar en la cola para el colegio " + idInstitucion, e);
		}
	}

}