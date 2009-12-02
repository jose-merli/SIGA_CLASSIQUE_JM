package com.siga.gratuita.service;

import com.atos.utils.ClsExceptions;
import com.siga.general.SIGAException;
import com.siga.gratuita.vos.VolantesExpressVo;

import es.satec.businessManager.BusinessService;

public interface VolantesExpressService extends BusinessService{
	
	public Object executeService(VolantesExpressVo volantesExpressVo  ) throws SIGAException,ClsExceptions;

	
}
