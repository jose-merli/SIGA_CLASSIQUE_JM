package com.siga.gratuita.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesBean;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessService;

public interface EejgService extends BusinessService{
	
	public Object executeService();
	public Map<Integer, Map<String, String>> getDatosInformeEejg(ScsUnidadFamiliarEJGBean unidadFamiliar,UsrBean usr);
	public File getInformeEejg(Map<Integer, Map<String, String>> mapInformeEejg,UsrBean usr);
	public Map<Integer, Map<String, String>> getDatosInformeEejgMasivo(List<ScsUnidadFamiliarEJGBean> lUnidadFamiliar,UsrBean usr);
	public Map<Integer, Map<String, String>> getDatosInformeEejgMultiplesEjg(String datosMultiplesEjg,String longitudNumEjg, UsrBean usr) throws BusinessException;
	public void insertarPeticionEejg(ScsEejgPeticionesBean peticionEejg,UsrBean usrBean) throws ClsExceptions;
	public List<ScsEejgPeticionesBean> getPeticionesEejg(ScsEJGBean eejgBean,UsrBean usrBean)throws ClsExceptions;
	public File descargarCertificadoNotificacionEMensaje(String idInstitucion, String idEnvio, UsrBean usrBean) throws BusinessException;
}
