package com.siga.envios.service.sigp_ca;

import com.siga.envios.service.IntercambiosService;

import es.satec.businessManager.BusinessException;


public interface RespuestaSolSusProcedimientoService extends IntercambiosService{
	public void procesarIntercambio(String idEnvio,	String idInstitucion, Integer idUsuario) throws BusinessException;
//	public void insertaIntercambioTelematico(EcomSolsusprocedimientoWithBLOBs solsusprocedimientoWithBLOBs, Integer idUsuario)throws BusinessException;
	
}
