package com.siga.ws.i2032;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.CajgRemesaKey;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import es.satec.businessManager.BusinessManager;

public class PCAJGPaisVascoEnvioEJG extends PCAJGPaisVascoComun {
	
	private static Logger log = Logger.getLogger(PCAJGPaisVascoEnvioEJG.class);
	
	@Override
	public void execute() throws Exception {	
		
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion((short)getIdInstitucion());
		if (isSimular()) {
			ecomCola.setIdoperacion(AppConstants.OPERACION.GV_VALIDACION_EXPEDIENTES.getId());
		} else {
			ecomCola.setIdoperacion(AppConstants.OPERACION.GV_ENVIO_EXPEDIENTES.getId());
		}
		log.debug("Insertando un nuevo registro para la validación de datos de una remesa de envío.");
		CajgRemesaKey cajgRemesaKey = new CajgRemesaKey();
		cajgRemesaKey.setIdinstitucion((short)getIdInstitucion());
		cajgRemesaKey.setIdremesa((long)getIdRemesa());
		ecomColaService.insertaColaRemesa(ecomCola, cajgRemesaKey);
		
	}

}
