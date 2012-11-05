package com.siga.envios.service.sigp_ca;

import com.siga.envios.service.IntercambiosService;

import es.satec.businessManager.BusinessException;


public interface SolicitudDesignaProvisionalService extends IntercambiosService{
	public void procesarIntercambio(String idEnvio,	String idInstitucion, Integer idUsuario) throws BusinessException;
//	public void insertaIntercambioTelematico(EcomDesignaprovisionalWithBLOBs designaprovisionalWithBLOBs, Integer idUsuario)throws BusinessException;
	
	
	
}
