package com.siga.ws.i2064;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;

public class ResolucionesListener extends SIGAListenerAbstract {

	@Override
	protected void execute(Short idInstitucion) {				
		//INSERTAR EN ECOM_COLA
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion(idInstitucion);
		ecomCola.setIdoperacion(AppConstants.OPERACION.XUNTA_RESOLUCIONES.getId());
		ecomColaService.insertaColaConsultaResoluciones(ecomCola);
	}

	@Override
	protected String getActivoParam() {		
		return AppConstants.PARAMETRO.PCAJG_XUNTA_RESOLUCIONES_ACTIVO.name();
	}

	@Override
	protected String getFechaHoraInicioParam() {		
		return "PCAJG_XUNTA_DIA_HORA_CONSULTA_RESOLUCIONES";
	}

	@Override
	protected String getDiasIntervaloParam() {		
		return "PCAJG_XUNTA_INTERVALO_DIAS_RESOLUCIONES";
	}

}