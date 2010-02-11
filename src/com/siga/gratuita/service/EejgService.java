package com.siga.gratuita.service;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.UsrBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;

import es.satec.businessManager.BusinessService;

public interface EejgService extends BusinessService{
	
	public Object executeService();
	public Map<Integer, Map<String, String>> getDatosInformeEejg(ScsUnidadFamiliarEJGBean unidadFamiliar,UsrBean usr);
	public File getInformeEejg(Map<Integer, Map<String, String>> mapInformeEejg,UsrBean usr);
	public Map<Integer, Map<String, String>> getDatosInformeEejgMasivo(List<ScsUnidadFamiliarEJGBean> lUnidadFamiliar,UsrBean usr);
	public Map<Integer, Map<String, String>> getDatosInformeEejgMultiplesEjg(String datosMultiplesEjg,UsrBean usr);

	
}
