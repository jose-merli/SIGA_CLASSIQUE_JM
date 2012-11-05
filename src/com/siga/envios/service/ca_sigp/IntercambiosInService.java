package com.siga.envios.service.ca_sigp;

import com.siga.envios.service.IntercambiosService;

import es.satec.businessManager.BusinessException;

public interface IntercambiosInService extends IntercambiosService{
	public void reprocesarIntercambio(String idEnvio,	String idInstitucion, Integer idUsuario) throws BusinessException;
	

	
}
