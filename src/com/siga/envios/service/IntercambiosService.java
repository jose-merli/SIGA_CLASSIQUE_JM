package com.siga.envios.service;

import java.io.File;

import com.atos.utils.ClsExceptions;
import com.siga.envios.form.IntercambioTelematicoForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessService;

public interface IntercambiosService extends BusinessService{
	
	public Object executeService() throws SIGAException,ClsExceptions;

	public IntercambioTelematicoForm getIntercambioTelematico(String idEnvio,String idInstitucion, String idioma)throws BusinessException;
	public File getFicheroLog(String idEnvio,String idInstitucion)throws BusinessException;
	public File getPdfIntercambio(String idEnvio,String idInstitucion) throws BusinessException;
	

	
}
