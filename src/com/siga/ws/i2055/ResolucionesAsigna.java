package com.siga.ws.i2055;

import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import es.satec.businessManager.BusinessManager;

public class ResolucionesAsigna {
	
	
	public void obtenerResoluciones(String idInstitucion) {	
		EcomCola ecomCola = new EcomCola();
		if (idInstitucion != null) {
			ecomCola.setIdinstitucion(Short.valueOf(idInstitucion));
		}
		ecomCola.setIdoperacion(OPERACION.ASIGNA_RESOLUCIONES.getId());			
		
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		ecomColaService.insertaColaConsultaResoluciones(ecomCola);
	}

}
