package com.siga.expedientes.service;

import com.atos.utils.ClsExceptions;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;

public interface ExpedientesService extends BusinessService{
	
	public Object executeService() throws SIGAException,ClsExceptions;
	
	public void procesarAutomaticamenteComprobacionAlarmas()throws Exception;
	
	
	
	
	

	
}
