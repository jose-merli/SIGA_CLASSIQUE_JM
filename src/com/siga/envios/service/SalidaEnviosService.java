package com.siga.envios.service;

import java.io.File;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;

import es.satec.businessManager.BusinessException;

public interface SalidaEnviosService{
	
//	public List<EnvEnvios> getSalidaEnvios(SalidaEnviosForm salidaEnviosForm)throws BusinessException;
	public List<EnvEnvios> getComunicacionesEJG(Integer idInstitucion,Short anio,Short idTipo, Integer numero, short comisionAJG ) throws BusinessException ;
	public List<EnvEnvios> getComunicacionesDesigna(Integer idInstitucion,Short anio,Short idTurno, Integer numero, short comisionAJG ) throws BusinessException;
	public EnvEnvios getEnvio(Long idEnvio,Short idInstitucion) throws BusinessException;
	public void borrarEnvio(EnvEnvios envio) throws BusinessException;
	public void darBajaLogicaEnvio(EnvEnvios envio) throws BusinessException;
	public File getLogComunicacion(Long idEnvio,Short idInstitucion)throws BusinessException;
}
