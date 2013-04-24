package com.siga.envios.service.ca_sigp;

import org.redabogacia.sigaservices.app.autogen.model.EcomSolimpugresolucionajgWithBLOBs;

import es.satec.businessManager.BusinessException;

public interface SolImpugnacionResolucionAJGService extends IntercambiosInService{
	public void insertaIntercambioTelematico(EcomSolimpugresolucionajgWithBLOBs solimpugresolucionajgWithBLOBs, Integer idUsuario)throws BusinessException;
}
