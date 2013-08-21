package com.siga.envios.service;

import java.io.File;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisional;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosExample;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;

import com.atos.utils.UsrBean;
import com.siga.envios.form.EntradaEnviosForm;

import es.satec.businessManager.BusinessException;

public interface EntradaEnviosService{
	
	public List<EnvEntradaEnvios> getEntradaEnvios(EntradaEnviosForm entradaEnviosForm)throws BusinessException;
	public EnvEntradaEnviosWithBLOBs getEntradaEnviosWithBlobs(EntradaEnviosForm entradaEnviosForm)throws BusinessException;
	public void procesar(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean,boolean nuevo) throws BusinessException;
	public void borrarRelacionEJG(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean) throws BusinessException;
	public void borrarRelacionDesigna(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean) throws BusinessException;
	public File getPdfIntercambio(String idEnvio, String idInstitucion) throws BusinessException;
	public EcomSoldesignaprovisional getDatosSolDesignaProvisional(String idEnvio, String idInstitucion) throws BusinessException;
	public void updateFormularioDatosSolDesignaProvisional(EntradaEnviosForm entradaEnviosForm) throws BusinessException;
	public void updateFormularioDatosRespuestaSuspensionProcedimiento(EntradaEnviosForm entradaEnviosForm) throws BusinessException;
	public void actualizarEstado(Long idEnvio, Short idInstitucion, Short idEstado) throws BusinessException;
	public void relacionarEnvio(Long idEnvio, Short idInstitucion, Long idEnvioRelacionado) throws BusinessException;
	public EnvEntradaEnviosWithBLOBs getEntradaEnvios(EnvEntradaEnviosExample entradaEnviosExample) throws BusinessException;
	public void actualizarEntradaEnvios(EnvEntradaEnviosExample entradaEnviosExample, EnvEntradaEnviosWithBLOBs envEntradaEnvios)throws BusinessException;
	public List<EnvEntradaEnvios> getComunicacionesEJG(Integer idInstitucion,Short anio,Short idTipo, Integer numero,short comisionAJG ) throws BusinessException ;
	public List<EnvEntradaEnvios> getComunicacionesDesigna(Integer idInstitucion,Short anio,Short idTurno, Integer numero,short comisionAJG ) throws BusinessException ;
	
}
