package com.siga.facturacionSJCS.service;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessService;
/**
 * 
 * @author jorgeta 
 * @date   02/07/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public interface FacturacionSJCSService extends BusinessService{
	public Object executeService(Object... parameters) throws BusinessException;
	public void procesarAutomaticamenteFacturacionSJCS()throws Exception;
	
	
	
	
	
	

	
}
