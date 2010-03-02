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
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
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
		mapParameters.put("idInstitucion", usr.getLocation());
		if(unidadFamiliar.getParentesco()!=null)
			mapParameters.put("declaranteEs", unidadFamiliar.getParentesco().getDescripcion().toString());
		actualizaParametrosPersona(mapParameters,usr);
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
			mapParameters.put("idInstitucion", usr.getLocation());
			if(unidadFamiliar.getParentesco()!=null)
				mapParameters.put("declaranteEs", unidadFamiliar.getParentesco().getDescripcion().toString());
			
			actualizaParametrosPersona(mapParameters,usr);
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
			
			
			
			unidadFamiliar = new ScsUnidadFamiliarEJGBean();
			//miForm.setEjg(ejg);
			unidadFamiliar.setIdInstitucion(new Integer(idInstitucion));
			unidadFamiliar.setIdTipoEJG(new Integer(idTipoEJG));
			unidadFamiliar.setAnio(new Integer(anio));
			unidadFamiliar.setNumero(new Integer(numero));
			ejg = new ScsEJGBean();
			ejg.setIdInstitucion(new Integer(idInstitucion));
			ejg.setIdTipoEJG(new Integer(idTipoEJG));
			ejg.setAnio(new Integer(anio));
			ejg.setNumero(new Integer(numero));
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
				
				for(DefinirUnidadFamiliarEJGForm formUnidadFamiliar:alUnidadFamiliar){
					
					Map<String, String> mapParameters = new HashMap<String, String>();
					mapParameters = actualizaParametrosComunes(mapParameters, usr);
					mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
					mapParameters.put("idPersonaJG",formUnidadFamiliar.getIdPersona());
					mapParameters.put("institucion", institucion);
					mapParameters.put("idInstitucion", usr.getLocation());
					if(formUnidadFamiliar.getParentesco()!=null)
						mapParameters.put("declaranteEs", formUnidadFamiliar.getParentesco().getDescripcion().toString());
					actualizaParametrosPersona(mapParameters,usr);
					
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
	private Map<String, String> actualizaParametrosPersona(Map<String, String> mapParameters,UsrBean usr){
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String idInstitucion = mapParameters.get("idInstitucion");
		String idPersonaJG = mapParameters.get("idPersonaJG");
		ScsPersonaJGAdm admPersonaJG = new ScsPersonaJGAdm(usr);
		
		try {
			ScsPersonaJGBean personaBean = admPersonaJG.getPersonaJG(new Long(idPersonaJG), new Integer(idInstitucion));
			mapParameters.put("direccion",personaBean.getDireccion());
			mapParameters.put("codigoPostal",personaBean.getCodigoPostal());
			if(personaBean.getProvincia()!=null)
				mapParameters.put("provincia",personaBean.getProvincia().getNombre());
			else
				mapParameters.put("provincia","");
			if(personaBean.getPoblacion()!=null)
				mapParameters.put("poblacion",personaBean.getPoblacion().getNombre());
			else
				mapParameters.put("poblacion","");
			Vector<ScsTelefonosPersonaJGBean> vTelefonos = personaBean.getTelefonos();
			
			if(vTelefonos!=null && vTelefonos.size()>0){
				int i=0;
				for(ScsTelefonosPersonaJGBean telefono:vTelefonos){
					StringBuffer tfno = new StringBuffer();
					tfno.append(UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.telefono0"));
					if(telefono.getNombreTelefono()!=null){
						tfno.append(" ");
						tfno.append(telefono.getNombreTelefono());
					}
					
					mapParameters.put("textosFijosSolicitanteTelefono"+i,tfno.toString() );	
					mapParameters.put("telefono"+i,telefono.getNumeroTelefono().toString());
					i++;
					
				}
				if(i<2){
					mapParameters.put("textosFijosSolicitanteTelefono1","");
					mapParameters.put("telefono1","");
				}
			}else{
				mapParameters.put("textosFijosSolicitanteTelefono0","");
				mapParameters.put("textosFijosSolicitanteTelefono1","");
				mapParameters.put("telefono0","");
				mapParameters.put("telefono1","");
				
			}
			
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
			
		}
		return mapParameters;
		
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
				
		mapParameters.put("conveniosInicio", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.conveniosInicio"));
		mapParameters.put("convenio1", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenio1"));
		mapParameters.put("convenio2", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenio2"));
		mapParameters.put("convenio3",  UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenio3"));
		mapParameters.put("convenio4",  UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenio4"));
		mapParameters.put("conveniosFin",  UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.conveniosFin"));
		mapParameters.put("avisoLegal", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.avisoLegal"));
		
		
		
		mapParameters.put("textosFijosPagina", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.pagina"));
		mapParameters.put("textosFijosDe", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.de"));
	
		
		mapParameters.put("textosFijosExpediente", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.expediente"));
		mapParameters.put("textosFijosSolicitanteTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.titulo"));
		mapParameters.put("textosFijosSolicitanteNombre", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.nombre"));
		mapParameters.put("textosFijosSolicitanteApellido1", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.apellido1"));
		mapParameters.put("textosFijosSolicitanteApellido2", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.apellido2"));
		mapParameters.put("textosFijosSolicitanteNif", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.nif"));
		mapParameters.put("textosFijosSolicitanteDireccion", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.direccion"));
		mapParameters.put("textosFijosSolicitanteCodigoPostal", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.codigoPostal"));
		mapParameters.put("textosFijosSolicitantePoblacion", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.poblacion"));
		mapParameters.put("textosFijosSolicitanteProvincia", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitante.provincia"));
		
		mapParameters.put("textosFijosSolicitudTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.solicitud.titulo"));
		mapParameters.put("textosFijosFechaDatosCorrectos", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.fechaDatosCorrectos"));
		mapParameters.put("textosFijosPeticionTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.peticion.titulo"));
		mapParameters.put("textosFijosNombrePdfTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.nombrePdf.titulo"));
		mapParameters.put("textosFijosFicheroPdfTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.ficheroPdf.titulo"));
		mapParameters.put("textosFijosConveniosTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenios.titulo"));
		mapParameters.put("textosFijosAvisoLegalTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.avisoLegal.titulo"));

		mapParameters.put("textosFijosDeclaranteEs", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.declaranteEs"));
		
		return mapParameters;
		
	}
	

	
	
	

}
