package com.siga.envios.service;

import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.log4j.SatecLogger;

import com.siga.envios.service.ca_sigp.DesignaProvisionalService;
import com.siga.envios.service.ca_sigp.SolSusProcedimientoService;
import com.siga.envios.service.sigp_ca.RespuestaSolSusProcedimientoService;
import com.siga.envios.service.sigp_ca.SolicitudDesignaProvisionalService;

import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.BusinessService;

public class IntercambiosServiceDispatcher {

	private static final SatecLogger log = (SatecLogger)SatecLogger.getLogger(IntercambiosServiceDispatcher.class);
	
	public static BusinessService getService(String idTipoIntercambio) {
		BusinessManager bm = BusinessManager.getInstance();
		return getService(bm, idTipoIntercambio);
		
	}
	public static BusinessService getService(BusinessManager bm,String idTipoIntercambio) {
		BusinessService service = null;
		if(idTipoIntercambio.equals(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getCodigo())){
			service = (DesignaProvisionalService)bm.getService(DesignaProvisionalService.class);
		} else if(idTipoIntercambio.equals(TipoIntercambioEnum.ICA_SGP_ENV_SOL_SUSP_PROC.getCodigo())){
			service = (SolSusProcedimientoService)bm.getService(SolSusProcedimientoService.class);
		}else if(idTipoIntercambio.equals(TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
			service = (SolicitudDesignaProvisionalService)bm.getService(SolicitudDesignaProvisionalService.class);
		}else if(idTipoIntercambio.equals(TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
			service = (RespuestaSolSusProcedimientoService)bm.getService(RespuestaSolSusProcedimientoService.class);
		}else {
			log.error("Tipo de Intercambio desconocido " + idTipoIntercambio);
		}
		
		return service;
	}

}



