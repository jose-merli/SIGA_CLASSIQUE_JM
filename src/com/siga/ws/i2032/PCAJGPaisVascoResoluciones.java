package com.siga.ws.i2032;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.general.SIGAListenerAbstract;
import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplPortBindingStub;
import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplServiceLocator;
import com.siga.ws.i2032.axis.ejg.ObtResolucionesEnt;

import es.satec.businessManager.BusinessManager;

public class PCAJGPaisVascoResoluciones extends SIGAListenerAbstract {

	@Override
	protected String getFechaHoraInicioParam() {		
		return "PCAJG_GV_DIA_HORA_CONSULTA_RESOLUCIONES";
	}

	@Override
	protected String getDiasIntervaloParam() {
		return "PCAJG_GV_INTERVALO_DIAS_RESOLUCIONES";
	}

	@Override
	protected String getActivoParam() {
		return AppConstants.PARAMETRO.PCAJG_GV_RESOLUCIONES_ACTIVO.name();
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


}
