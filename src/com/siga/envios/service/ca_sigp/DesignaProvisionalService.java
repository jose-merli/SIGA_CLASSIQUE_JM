package com.siga.envios.service.ca_sigp;

import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisionalWithBLOBs;

import com.siga.envios.service.IntercambiosService;

import es.satec.businessManager.BusinessException;


public interface DesignaProvisionalService extends IntercambiosInService{
	public void insertaIntercambioTelematico(EcomDesignaprovisionalWithBLOBs designaprovisionalWithBLOBs, Integer idUsuario)throws BusinessException;
	
	
	
}
