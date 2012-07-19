package com.siga.ws.i2055;

import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.EcomColaService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

import es.satec.businessManager.BusinessManager;

public class DesignacionProcuradorAsigna {
	
	public void obtenerDesignaciones(UsrBean usrBean, Integer idInstitucion) throws ClsExceptions {
				
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion(Short.valueOf(idInstitucion.toString()));
		ecomCola.setIdoperacion(OPERACION.ASIGNA_OBTENER_PROCURADOR.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
								
		if (ecomColaService.insert(ecomCola) != 1) {			
			throw new ClsExceptions("No se ha podido insertar en la cola de comunicaciones.");
		}
	}
}
