package com.siga.gratuita.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.eejg.SolicitudesEEJG;
import com.siga.general.SIGAException;
import com.siga.gratuita.service.EejgService;
import com.siga.informes.InformeEejg;
import com.sis.firma.core.B64.Base64CODEC;

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
		mapParameters.put("nif", unidadFamiliar.getPeticionEejg().getNif());
		mapParameters.put("idInstitucion", usr.getLocation());
		mapParameters.put("idioma", unidadFamiliar.getPeticionEejg().getIdioma());
		mapParameters.put("csv", unidadFamiliar.getPeticionEejg().getCsv());
		mapInformes.put(new Integer(unidadFamiliar.getPeticionEejg().getIdXml()), mapParameters);
		
		return mapInformes;
	}
	public Map<Integer, Map<String, String>> getDatosInformeEejgMasivo(List<ScsUnidadFamiliarEJGBean> lUnidadFamiliar,UsrBean usr){
		ScsEJGAdm admEJG = new ScsEJGAdm(usr);
		Hashtable<String, Object> miHash = null;
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Map<String, String> mapParameters = null;
		ScsEJGBean ejg = null;
		
		for (ScsUnidadFamiliarEJGBean unidadFamiliar:lUnidadFamiliar) {
		
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
			mapParameters = new HashMap<String, String>();
			mapParameters.put("nif", unidadFamiliar.getPeticionEejg().getNif());
			mapParameters.put("idioma", unidadFamiliar.getPeticionEejg().getIdioma());
			mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
			mapParameters.put("csv", unidadFamiliar.getPeticionEejg().getCsv());
			mapInformes.put(new Integer(unidadFamiliar.getPeticionEejg().getIdXml()), mapParameters);
			
		}
		
		return mapInformes;
	}
	public Map<Integer, Map<String, String>> getDatosInformeEejgMultiplesEjg(String datosMultiplesEjg,String longitudNumEjg,UsrBean usr) throws BusinessException{
		InformeEejg informe = new InformeEejg (usr);
		Vector<Hashtable<String, String>> vDatosMultiplesEjg = null;
		try {
			vDatosMultiplesEjg = informe.obtenerDatosFormulario(datosMultiplesEjg);
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		ScsEejgPeticionesAdm admPeticionEejg = new ScsEejgPeticionesAdm(usr);
		List<ScsEJGBean> scsEJGBeans = new ArrayList<ScsEJGBean>();
		for(Hashtable<String, String> datos:vDatosMultiplesEjg){
			String idTipoEJG= (String)datos.get("idtipo");
			String anio= (String)datos.get("anio");
			String numero= (String)datos.get("numero");
			String idInstitucion= (String)datos.get("idinstitucion");
			ScsEJGBean ejgBean = new ScsEJGBean();
			ejgBean.setIdInstitucion(Integer.parseInt(idInstitucion));
			ejgBean.setAnio(Integer.parseInt(anio));
			ejgBean.setIdTipoEJG(Integer.parseInt(idTipoEJG));
			ejgBean.setNumero(Integer.parseInt(numero));
			scsEJGBeans.add(ejgBean);
		}
		
		List<ScsEejgPeticionesBean> peticionesEejgBeans =  admPeticionEejg.getPeticionesEejg(scsEJGBeans);
		
		ScsEJGAdm admEJG = new ScsEJGAdm(usr);
		Hashtable<String, Object> miHash = null;
		Map<Integer, Map<String, String>> mapInformes = new HashMap<Integer, Map<String,String>>();
		Map<String, String> mapParameters = null;
		ScsEJGBean ejg = null;
		
		for (ScsEejgPeticionesBean peticionesEejgBean:peticionesEejgBeans) {
		
			Vector<ScsEJGBean> vEjg = null;
			miHash = new Hashtable<String, Object>();
			miHash.put("ANIO",peticionesEejgBean.getAnio());
			miHash.put("NUMERO",peticionesEejgBean.getNumero());
			miHash.put("IDTIPOEJG",peticionesEejgBean.getIdTipoEjg());
			miHash.put("IDINSTITUCION",peticionesEejgBean.getIdInstitucion());
			try {
				vEjg = admEJG.selectByPK(miHash);
			} catch (ClsExceptions e) {
				throw new BusinessException(e.getMessage());
			}
			ejg = (ScsEJGBean)vEjg.get(0);
			mapParameters = new HashMap<String, String>();
			mapParameters.put("nif", peticionesEejgBean.getNif());
			mapParameters.put("idioma", peticionesEejgBean.getIdioma());
			mapParameters.put("numEjg", ejg.getAnio()+"-"+ejg.getNumEJG());
			mapParameters.put("csv", peticionesEejgBean.getCsv());
			mapInformes.put(new Integer(peticionesEejgBean.getIdXml()), mapParameters);
			
		}
		
		
		
		
		
		
		return mapInformes;
	}
	
	public File getInformeEejg(Map<Integer, Map<String, String>> mapInformeEejg,UsrBean usr) {
		InformeEejg informe = new InformeEejg (usr);
		File fichero;
		try {
			fichero = informe.generarInformeEejg(mapInformeEejg);
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (SIGAException e) {
			throw new BusinessException(e.getLiteral(), e);
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(e.toString(), e);
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
		String rutaLogoCGAE = plantillaRuta+ClsConstants.FILE_SEP+"recursos"+ClsConstants.FILE_SEP+"cgae.GIF";
		
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
		mapParameters.put("textosFijosFechaDatosPendientesInicio", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.textosFijosFechaDatosPendientesInicio"));
		mapParameters.put("textosFijosFechaDatosPendientesFin", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.textosFijosFechaDatosPendientesFin"));
		
		
		mapParameters.put("textosFijosPeticionTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.peticion.titulo"));
		mapParameters.put("textosFijosNombrePdfTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.nombrePdf.titulo"));
		mapParameters.put("textosFijosFicheroPdfTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.ficheroPdf.titulo"));
		mapParameters.put("textosFijosConveniosTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.convenios.titulo"));
		mapParameters.put("textosFijosAvisoLegalTitulo", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.avisoLegal.titulo"));

		mapParameters.put("textosFijosDeclaranteEs", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.declaranteEs"));
		
		mapParameters.put("textosFijosFechaDatos", UtilidadesString.getMensajeIdioma(usr, "eejg.textosFijos.textosFijosFechaDatos"));
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",new Locale("es_ES"));
		mapParameters.put("fechaDescargaInforme",format.format(new Date()));	
	
		
		
		return mapParameters;
		
	}

	public void insertarPeticionEejg(ScsEejgPeticionesBean peticionEejg,UsrBean usrBean) throws ClsExceptions
			{
		ScsEejgPeticionesAdm admPeticionEejg = new ScsEejgPeticionesAdm(usrBean);
		ScsPersonaJGAdm personaJGAdm = new ScsPersonaJGAdm(usrBean);
		ScsPersonaJGBean personaJGBean = personaJGAdm.getPersonaJGSByPK(peticionEejg.getIdPersona(),peticionEejg.getIdInstitucion());
		peticionEejg.setNif(personaJGBean.getNif());
		peticionEejg.setNombre(personaJGBean.getNombre());
		peticionEejg.setApellido1(personaJGBean.getApellido1());
		peticionEejg.setApellido2(personaJGBean.getApellido2());
		admPeticionEejg.insertarPeticionEejg(peticionEejg);	
		
	}

	public List<ScsEejgPeticionesBean> getPeticionesEejg(ScsEJGBean eejgBean,
			UsrBean usrBean) throws ClsExceptions {
		ScsEejgPeticionesAdm admPeticionEejg = new ScsEejgPeticionesAdm(usrBean);
		return admPeticionEejg.getPeticionesEejg(eejgBean);
	}

	public File descargarCertificadoNotificacionEMensaje(String idInstitucion, String idEnvio, UsrBean usrBean) throws BusinessException {
		File fileFirmado = null;		
		
		try {			
			//Obtenemos el CSV a traves de una consulta a envios
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(usrBean);
			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
			htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
			Vector envios = enviosAdm.selectByPK(htPk);
			if(envios!= null && envios.size()>0){
				EnvEnviosBean bean = (EnvEnviosBean) envios.get(0);
				
				String contenidoPDF = null;
				//LLamamos al servico de EEJG para obtener el PDF a traves de la PFD
			
				SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();
				contenidoPDF = solicitudesEEJG.getDocumentoTO(bean.getCSV());


				//Construimos nuestro fichero
				String pathFile = "CertificadoRecepcionBuroSMS_" + idInstitucion + "_" + idEnvio+ ".pdf";
				fileFirmado = new File(pathFile);

				// Realizamos la decodificacion para su correcta visualización
				Base64CODEC.decodeToFile(contenidoPDF, pathFile);
			}
			
		} catch (ClsExceptions e) {
			throw new BusinessException(e.getMessage());
		}
		
		return fileFirmado;
	}
}
