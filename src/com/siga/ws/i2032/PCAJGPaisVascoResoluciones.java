package com.siga.ws.i2032;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;

public class PCAJGPaisVascoResoluciones extends SIGAListenerAbstract {

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.PCAJG_GV_DIA_HORA_CONSULTA_RESOLUCIONES;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.PCAJG_GV_INTERVALO_DIAS_RESOLUCIONES;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.PCAJG_GV_RESOLUCIONES_ACTIVO;
	}

	@Override
	protected void execute(Short idInstitucion) {
		//INSERTAR EN ECOM_COLA
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion(idInstitucion);
		ecomCola.setIdoperacion(AppConstants.OPERACION.GV_RESOLUCIONES.getId());
		ecomColaService.insertaColaConsultaResoluciones(ecomCola);
		
	}
	
	@Override
	protected MODULO getModulo() {
		return MODULO.SCS;
	}


}
