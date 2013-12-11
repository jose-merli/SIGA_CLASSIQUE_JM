package com.siga.ws.cen;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;

public class CargaCensoWSListener extends SIGAListenerAbstract {
	
	private static final Logger log = Logger.getLogger(CargaCensoWSListener.class);

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.CEN_WS_CARGA_DIA_HORA;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.CEN_WS_CARGA_INTERVALO_DIAS;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.CEN_WS_CARGA_ACTIVO;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.CEN;
	}

	@Override
	protected void execute(Short idInstitucion) {
		try {
			EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
			if (ecomColaService.insertaColaCargaCenso(idInstitucion) != 1) {
				throw new Exception("No se ha podido insertar correctamente en la cola de carga de censo para el colegio " + idInstitucion);
			}
		} catch (Exception e) {
			log.error("Error al insertar en la cola para el colegio " + idInstitucion, e);
		}
	}

}
