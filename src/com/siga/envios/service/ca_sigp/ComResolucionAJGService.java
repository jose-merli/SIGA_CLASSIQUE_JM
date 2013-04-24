package com.siga.envios.service.ca_sigp;

import org.redabogacia.sigaservices.app.autogen.model.EcomComunicacionresolucionajgWithBLOBs;

import es.satec.businessManager.BusinessException;

public interface ComResolucionAJGService extends IntercambiosInService{
	public void insertaIntercambioTelematico(EcomComunicacionresolucionajgWithBLOBs comunicacionresolucionajgWithBLOBs,  Integer idUsuario)throws BusinessException;
}
