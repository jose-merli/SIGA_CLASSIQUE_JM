package com.siga.ws.i2032;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.GEN_RECURSOS;
import org.redabogacia.sigaservices.app.autogen.model.CajgRemesa;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.caj.CajgRemesaService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.siga.Utilidades.UtilidadesString;

import es.satec.businessManager.BusinessManager;

public class PCAJGPaisVascoEnvioEJG extends PCAJGPaisVascoComun {
	
	private static Logger log = Logger.getLogger(PCAJGPaisVascoEnvioEJG.class);
	
	@Override
	public void execute() throws Exception {	
		
		CajgRemesaService cajgRemesaService = (CajgRemesaService) BusinessManager.getInstance().getService(CajgRemesaService.class);
		EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdinstitucion((short)getIdInstitucion());
		if (isSimular()) {
			ecomCola.setIdoperacion(AppConstants.OPERACION.GV_VALIDACION_EXPEDIENTES.getId());
		} else {
			ecomCola.setIdoperacion(AppConstants.OPERACION.GV_ENVIO_EXPEDIENTES.getId());
		}
		log.debug("Insertando un nuevo registro para la validación de datos de una remesa de envío.");
		CajgRemesa cajgRemesaKey = new CajgRemesa();
		cajgRemesaKey.setIdinstitucion((short)getIdInstitucion());
		cajgRemesaKey.setIdremesa((long)getIdRemesa());
		
		cajgRemesaService.deleteCajgRespuestasRemesa(cajgRemesaKey);
		cajgRemesaService.insertaEstadoValidandoRemesa(cajgRemesaKey, UtilidadesString.getMensajeIdioma(getUsrBean(), GEN_RECURSOS.scs_mensaje_validando.getValor()));
		
		ecomColaService.insertaColaRemesa(ecomCola, cajgRemesaKey);
		
	}

}
