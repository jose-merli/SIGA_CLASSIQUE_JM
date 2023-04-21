package com.siga.envios.service.ca_sigp;

import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimientoWithBLOBs;

import es.satec.businessManager.BusinessException;


public interface SolSusProcedimientoService extends IntercambiosInService{
	
	public void insertaIntercambioTelematico(EcomSolsusprocedimientoWithBLOBs solsusprocedimientoWithBLOBs, Integer idUsuario)throws BusinessException;
	
}
