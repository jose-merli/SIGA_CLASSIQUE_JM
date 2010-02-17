package com.siga.gratuita.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenInstitucionAdm;
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
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String directorioPlantillas = rp.returnProperty("sjcs.directorioFisicoPlantillaInformeEejg");
		String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
		String plantillaRuta   = directorioPlantillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + usr.getLocation();
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr);
		String institucion = "";
		try {
			institucion = admInstitucion.getNombreInstitucion(usr.getLocation());
		} catch (SIGAException e) {
			throw new BusinessException(e.getMessage());
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		String rutaLogoColegio = plantillaRuta+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"Logo.jpg";
		rutaLogoColegio = UtilidadesString.replaceAllIgnoreCase(rutaLogoColegio, "/","\\\\" );
		
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Map<String, String> mapParameters = new HashMap<String, String>();
		mapParameters = actualizaParametrosComunes(mapParameters, usr);
		mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
		mapParameters.put("idPersonaJG", unidadFamiliar.getPersonaJG().getIdPersona().toString());
		mapParameters.put("institucion", institucion);
		mapParameters.put("rutaLogoColegio", rutaLogoColegio);
		mapParameters.put("idioma", unidadFamiliar.getPeticionEejg().getIdioma());
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
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String directorioPlantillas = rp.returnProperty("sjcs.directorioFisicoPlantillaInformeEejg");
		String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
		String plantillaRuta   = directorioPlantillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + usr.getLocation();
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr);
		String institucion = "";
		try {
			institucion = admInstitucion.getNombreInstitucion(usr.getLocation());
		} catch (SIGAException e) {
			throw new BusinessException(e.getMessage());
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		String rutaLogoColegio = plantillaRuta+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"Logo.jpg";
		rutaLogoColegio = UtilidadesString.replaceAllIgnoreCase(rutaLogoColegio, "/","\\\\" );
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
			mapParameters = actualizaParametrosComunes(mapParameters, usr);
			mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
			mapParameters.put("idioma", unidadFamiliar.getPeticionEejg().getIdioma());
			mapParameters.put("idPersonaJG", unidadFamiliar.getPersonaJG().getIdPersona().toString());
			mapParameters.put("institucion", institucion);
			mapParameters.put("rutaLogoColegio", rutaLogoColegio);
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
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String directorioPlantillas = rp.returnProperty("sjcs.directorioFisicoPlantillaInformeEejg");
		String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
		String plantillaRuta   = directorioPlantillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + usr.getLocation();
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr);
		String institucion = "";
		try {
			institucion = admInstitucion.getNombreInstitucion(usr.getLocation());
		} catch (SIGAException e) {
			throw new BusinessException(e.getMessage());
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		String rutaLogoColegio = plantillaRuta+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"Logo.jpg";
		rutaLogoColegio = UtilidadesString.replaceAllIgnoreCase(rutaLogoColegio, "/","\\\\" );
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
					mapParameters = actualizaParametrosComunes(mapParameters, usr);
					mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
					mapParameters.put("idPersonaJG",formUnidadFamiliar.getIdPersona() );
					mapParameters.put("institucion", institucion);
					mapParameters.put("rutaLogoColegio", rutaLogoColegio);
					mapParameters.put("idioma", formUnidadFamiliar.getPeticionEejg().getIdioma());
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
	private Map<String, String> actualizaParametrosComunes(Map<String, String> mapParameters,UsrBean usr){
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String directorioPlantillas = rp.returnProperty("sjcs.directorioFisicoPlantillaInformeEejg");
		String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
		String plantillaRuta   = directorioPlantillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + usr.getLocation();
		String rutaLogoCGAE = plantillaRuta+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"cgae.jpg";
		rutaLogoCGAE = UtilidadesString.replaceAllIgnoreCase(rutaLogoCGAE, "//","\\" );
		rutaLogoCGAE = UtilidadesString.replaceAllIgnoreCase(rutaLogoCGAE, "/","\\" );
		mapParameters.put("rutaLogoCGAE", rutaLogoCGAE);
		mapParameters.put("conveniosInicio", "El Consejo General de la Abogacía Española, de acuerdo con los convenios de colaboración que a continuación se detallan:");
		mapParameters.put("convenio1", "Convenio de colaboración entre la Agencia Estatal de la Administración Tributaria y el Consejo General de la Abogacía Española para la cesión de información de carácter tributario en los procedimientos de Asistencia Jurídica Gratuita firmado con fecha 3 de julio de 2006");
		mapParameters.put("convenio2", "Convenio de colaboración entre la Secretaria de Estado de Hacienda y Presupuestos (Dirección General del Catastro) y el Consejo General de la Abogacía Española, en materia de gestión catastral firmado el 7 de marzo de 2007");
		mapParameters.put("convenio3", "Convenio de colaboración entre la Tesorería General de la Seguridad Social y Consejo General de la Abogacía Española en orden a obtener cierta información por vía telemática en	las solicitudes de justicia gratuita, firmado el 15 de octubre de 2007");
		mapParameters.put("convenio4", "Convenio de colaboración entre el Servicio Público de Empleo Estatal y el Consejo General de la Abogacía Española en materia de transmisión de datos para la sustitución de certificados en papel en los procedimientos de asistencia jurídica gratuita firmado con fecha 20 de junio de 2007");
		mapParameters.put("conveniosFin", "Certifica que el Ilustre Colegio de Consejo General de la Abogacía Española ha accedido de forma telemática a las Administraciones citadas y ha obtenido la siguiente información del solicitante de referencia");
		mapParameters.put("avisoLegal", "La información contenida en el presente documento procede de cada una de las Administraciones Públicas que se indican, habiendo sido obtenida por medios electrónicos seguros y con el consentimiento de la persona solicitante del beneficio a la justicia gratuita recogida al amparo la ley 1/1996, de 10 de enero, de Asistencia jurídica Gratuita. Los presentes datos se imprimen para la utilización exclusiva de la Comisión de Asistencia Jurídica Gratuita a los fines previstos, y a su solicitud, y no podrán utilizarse para ninguna otra finalidad.");
		return mapParameters;
		
	}
	

	
	
	

}
