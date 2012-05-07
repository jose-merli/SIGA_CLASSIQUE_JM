package com.siga.administracion.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.AdmConsultaInformeBean;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.ConConsultaBean;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.FileInforme;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;

public interface InformesService extends BusinessService{
	
	public Object executeService(AdmInformeBean informeVO) throws SIGAException,ClsExceptions;
	public List<InformeForm> getInformes(InformeForm bajaTemporal,String idInstitucion,UsrBean usrBean)throws ClsExceptions;
	public void insertaInforme(InformeForm informeForm,UsrBean usrBean)throws ClsExceptions, SIGAException;
	public void borrarInforme(InformeForm informeForm,UsrBean usrBean)throws ClsExceptions, SIGAException;
	public AdmInformeBean getInforme(InformeForm informeForm,UsrBean usrBean)throws ClsExceptions ;
	public void modificarInforme(InformeForm informeForm,UsrBean usrBean)throws ClsExceptions, SIGAException;
	public List<AdmTipoInformeBean> getTiposInforme(UsrBean usrBean) throws ClsExceptions ;
	public List<CenInstitucionBean> getInstitucionesInformes(Integer idInstitucion,UsrBean usrBean) throws ClsExceptions ;
	public FileInforme getFileDirectorio(InformeForm informeForm,boolean permisoBorrado)	throws ClsExceptions, SIGAException ;
	public List<AdmLenguajesBean> getLenguajes(UsrBean usrBean) throws ClsExceptions;
	public void borrarInformeFile(InformeForm informeForm) throws  SIGAException;
	public void uploadFile(InformeForm informeForm) throws  SIGAException;
	public boolean isNombreFisicoUnico(InformeForm informeForm,boolean isInsertar, UsrBean usrBean) throws SIGAException, ClsExceptions;
	public List<InformeForm> getInformesConsulta(ConConsultaBean 	consulta,InformeForm informeForm,UsrBean usrBean)throws ClsExceptions;
	public void borrarConsultaInforme(AdmConsultaInformeBean 	consulta,InformeForm informeForm,UsrBean usrBean)throws ClsExceptions, SIGAException;
	public List<EnvTipoEnviosBean> getTiposEnvio(List<String> excluidosList, UsrBean usrBean) throws ClsExceptions;
	public List<EnvPlantillasEnviosBean> getPlantillasEnvio(String idTipoEnvio, String idInstitucion, UsrBean usrBean) throws ClsExceptions;
	public List<EnvTipoEnviosBean> getTiposEnvioPermitidos(AdmInformeBean informeBean, UsrBean usrBean) throws ClsExceptions;
	
}
