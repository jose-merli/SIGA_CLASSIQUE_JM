package com.siga.ws.i2064;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.EcomColaService;

import com.atos.utils.UsrBean;
import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;

public class ResolucionesListener extends SIGAListenerAbstract {

	@Override
	protected void execute(UsrBean usrBean, String idInstitucion) throws Exception {				
		//INSERTAR EN ECOM_COLA
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion(Short.valueOf(idInstitucion));
		ecomCola.setIdoperacion(AppConstants.OPERACION.XUNTA_RESOLUCIONES.getId());
		ecomColaService.insert(ecomCola);
	}

	@Override
	protected String getActivoParam() {		
		return "PCAJG_XUNTA_RESOL_ACTIVO";
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