package com.siga.gratuita.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;
import com.siga.gratuita.service.EejgService;
import com.siga.informes.InformeEejg;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosEejgService extends JtaBusinessServiceTemplate 
	implements EejgService {

	public AtosEejgService(BusinessManager businessManager) {
		super(businessManager);
	}

	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object executeService() {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<Integer, Map<String, String>> getDatosInformeEejg(ScsUnidadFamiliarEJGBean unidadFamiliar,UsrBean usr){
		
		ScsEJGAdm admEJG = new ScsEJGAdm(usr);
		Hashtable<String, Object> miHash = new Hashtable<String, Object>();
		miHash.put("ANIO",unidadFamiliar.getAnio());
		miHash.put("NUMERO",unidadFamiliar.getNumero());
		miHash.put("IDTIPOEJG",unidadFamiliar.getIdTipoEJG());
		miHash.put("IDINSTITUCION",unidadFamiliar.getIdInstitucion());
		Vector<ScsEJGBean> vEjg = null;
		try {
			vEjg = admEJG.selectByPK(miHash);
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		ScsEJGBean ejg = vEjg.get(0);
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Map<String, String> mapParameters = new HashMap<String, String>();
		mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
		mapParameters.put("idPersonaJG", unidadFamiliar.getPersonaJG().getIdPersona().toString());
		mapInformes.put(new Integer(unidadFamiliar.getPeticionEejg().getIdXml()), mapParameters);
		
		return mapInformes;
	}
	public Map<Integer, Map<String, String>> getDatosInformeEejgMasivo(List<ScsUnidadFamiliarEJGBean> lUnidadFamiliar,UsrBean usr){
		ScsEJGAdm admEJG = new ScsEJGAdm(usr);
		Hashtable<String, Object> miHash = null;
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Map<String, String> mapParameters = null;
		ScsEJGBean ejg = null;
		
		Hashtable<StringBuffer, ScsEJGBean> htEjgs = new Hashtable<StringBuffer, ScsEJGBean>();
		for (ScsUnidadFamiliarEJGBean unidadFamiliar:lUnidadFamiliar) {
			StringBuffer keyEjgs = new StringBuffer();
			keyEjgs.append(unidadFamiliar.getAnio());
			keyEjgs.append("||");
			keyEjgs.append(unidadFamiliar.getNumero());
			keyEjgs.append("||");
			keyEjgs.append(unidadFamiliar.getIdTipoEJG());
			keyEjgs.append("||");
			keyEjgs.append(unidadFamiliar.getIdInstitucion());
			keyEjgs.append("||");
			if(htEjgs.containsKey(keyEjgs)){
				ejg = htEjgs.get(keyEjgs);
			
			}else{
				Vector<ScsEJGBean> vEjg = null;
				miHash = new Hashtable<String, Object>();
				miHash.put("ANIO",unidadFamiliar.getAnio());
				miHash.put("NUMERO",unidadFamiliar.getNumero());
				miHash.put("IDTIPOEJG",unidadFamiliar.getIdTipoEJG());
				miHash.put("IDINSTITUCION",unidadFamiliar.getIdInstitucion());
				try {
					vEjg = admEJG.selectByPK(miHash);
				} catch (ClsExceptions e) {
					throw new BusinessException(e.getMessage());
				}
				ejg = (ScsEJGBean)vEjg.get(0);
			}
			htEjgs.put(keyEjgs, ejg);
			mapParameters = new HashMap<String, String>();
			mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
			mapParameters.put("idPersonaJG", unidadFamiliar.getPersonaJG().getIdPersona().toString());
			mapInformes.put(new Integer(unidadFamiliar.getPeticionEejg().getIdXml()), mapParameters);
			
		}
		
		return mapInformes;
	}
	public Map<Integer, Map<String, String>> getDatosInformeEejgMultiplesEjg(String datosMultiplesEjg,UsrBean usr){
		InformeEejg informe = new InformeEejg (usr);
		Vector<Hashtable<String, String>> vDatosMultiplesEjg = null;
		try {
			vDatosMultiplesEjg = informe.obtenerDatosFormulario(datosMultiplesEjg);
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		
		ScsUnidadFamiliarEJGAdm admUnidadFamiliar = new ScsUnidadFamiliarEJGAdm(usr);
		ScsEJGAdm admEJG = new ScsEJGAdm(usr);
		ScsEejgPeticionesBean peticionEejg = null;
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Hashtable<String, Object> miHash = null;
		ScsEJGBean ejg = null;
		ScsUnidadFamiliarEJGBean unidadFamiliar = null;
		Hashtable<StringBuffer, ScsEJGBean> htEjgs = new Hashtable<StringBuffer, ScsEJGBean>();  
		
		for(Hashtable<String, String> datos:vDatosMultiplesEjg){
			String idTipoEJG= (String)datos.get("idtipo");
			String anio= (String)datos.get("anio");
			String numero= (String)datos.get("numero");
			String idInstitucion= (String)datos.get("idinstitucion");
			
			StringBuffer keyEjgs = new StringBuffer();
			keyEjgs.append(anio);
			keyEjgs.append("||");
			keyEjgs.append(numero);
			keyEjgs.append("||");
			keyEjgs.append(idTipoEJG);
			keyEjgs.append("||");
			keyEjgs.append(idInstitucion);
			keyEjgs.append("||");
			if(htEjgs.containsKey(keyEjgs)){
				ejg = htEjgs.get(keyEjgs);
			
			}else{
				Vector<ScsEJGBean> vEjg = null;
				miHash = new Hashtable<String, Object>();
				miHash.put("ANIO",anio);
				miHash.put("NUMERO",numero);
				miHash.put("IDTIPOEJG",idTipoEJG);
				miHash.put("IDINSTITUCION",idInstitucion);
				try {
					vEjg = admEJG.selectByPK(miHash);
				} catch (ClsExceptions e) {
					throw new BusinessException(e.getMessage());
				}
				ejg = (ScsEJGBean)vEjg.get(0);
			}
			htEjgs.put(keyEjgs, ejg);
			
			unidadFamiliar = new ScsUnidadFamiliarEJGBean();
			//miForm.setEjg(ejg);
			unidadFamiliar.setIdInstitucion(new Integer(idInstitucion));
			unidadFamiliar.setIdTipoEJG(new Integer(idTipoEJG));
			unidadFamiliar.setAnio(new Integer(anio));
			unidadFamiliar.setNumero(new Integer(numero));
			unidadFamiliar.setEjg(ejg);
			
			//Sacaremmos solos las peticiones que esten finalizadas 
			peticionEejg = new ScsEejgPeticionesBean();
			peticionEejg.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO);
			unidadFamiliar.setPeticionEejg(peticionEejg);
			List<DefinirUnidadFamiliarEJGForm> alUnidadFamiliar = null;
			try {
				alUnidadFamiliar = admUnidadFamiliar.getUnidadFamiliar(unidadFamiliar.getUnidadFamiliarEjgForm(), usr).getUnidadFamiliar();
			} catch (ClsExceptions e) {
				throw new BusinessException(e.getMessage());
			}
			if(alUnidadFamiliar!=null && alUnidadFamiliar.size()>0){
				for(DefinirUnidadFamiliarEJGForm formUnidadFamiliar:alUnidadFamiliar){
					
					Map<String, String> mapParameters = new HashMap<String, String>();
					mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
					mapParameters.put("idPersonaJG",formUnidadFamiliar.getIdPersona() );
					mapInformes.put(formUnidadFamiliar.getPeticionEejg().getIdXml(), mapParameters);
				}
			}
		}
		
		return mapInformes;
	}
	
	


	public File getInformeEejg(Map<Integer, Map<String, String>> mapInformeEejg,UsrBean usr) {
		InformeEejg informe = new InformeEejg (usr);
		File fichero;
		try {
			fichero = informe.generarInformeEejg(mapInformeEejg);
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		} catch (SIGAException e) {
			throw new BusinessException(e.getLiteral());
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(e.toString());
		}
		return fichero;
	}
	

	
	
	

}
