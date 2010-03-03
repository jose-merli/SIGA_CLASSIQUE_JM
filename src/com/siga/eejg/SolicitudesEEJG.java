package com.siga.eejg;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import com.siga.eejg.ws.ConsultaInfoAAPP.ConsultaInfoAAPP;
import com.siga.eejg.ws.ConsultaInfoAAPP.DatosConsultaInfoAAPP;
import com.siga.eejg.ws.RespuestaInfoConsultaInfoAAPP.RespuestaConsultaInfoAAPP;
import com.siga.eejg.ws.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPP;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.Informacion;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;

import com.siga.eejg.ws.ServiciosJGExpedienteServiceLocator;
import com.siga.eejg.ws.ServiciosJGExpedienteServiceSoapBindingStub;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.beans.eejg.ScsEejgXmlBean;

public class SolicitudesEEJG {
	private String urlWS;	
	private String idSistema;

	public SolicitudesEEJG() {
		super();
		init();
	}

	private void init() {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		urlWS = rp.returnProperty("eejg.urlWS");	
		idSistema = rp.returnProperty("eejg.idSistema");
	}

	private EngineConfiguration createClientConfig() { 
		return new FileProvider(SolicitudesEEJG.class.getResourceAsStream("/siga-eejg.wsdd"));
	} 

	public String solicitudPeticionInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		String idPeticionInfoAAPP = null;
		
		ServiciosJGExpedienteServiceLocator locator = new ServiciosJGExpedienteServiceLocator(createClientConfig());
		URL url = new URL(urlWS);		
		ServiciosJGExpedienteServiceSoapBindingStub stub = new ServiciosJGExpedienteServiceSoapBindingStub(url, locator);
		
		String idSolicitudImportada = null;
		
		if (scsEejgPeticionesBean.getIdInstitucion() == null) {
			throw new IllegalArgumentException("La institucion o zona no puede ser nula");
		}
		String idZona = scsEejgPeticionesBean.getIdInstitucion().toString();
		String dNI_NIE_Tramitador = getDNITramitador(usrBean, scsEejgPeticionesBean);
		
		ScsPersonaJGBean scsPersonaJGBean = getDatosSolicitante(usrBean, scsEejgPeticionesBean);
		String dNI_NIE_Solicitante = scsPersonaJGBean.getNif();
		String nombre = scsPersonaJGBean.getNombre();
		String apellido1 = scsPersonaJGBean.getApellido1();
		String apellido2 = scsPersonaJGBean.getApellido2();
		String idioma = scsEejgPeticionesBean.getIdioma();
		
		DatosPeticionInfoAAPP datosPeticionInfoAAPP = new DatosPeticionInfoAAPP(idSistema, idSolicitudImportada, idZona, dNI_NIE_Tramitador, dNI_NIE_Solicitante, nombre, apellido1, apellido2, idioma);
		Informacion informacion = new Informacion(datosPeticionInfoAAPP);
				
		SolicitudPeticionInfoAAPP solicitudPeticionInfoAAPP = new SolicitudPeticionInfoAAPP(informacion);
				
		RespuestaSolicitudPeticionInfoAAPP respuestaSolicitudPeticionInfoAAPP =  stub.solicitudPeticionInfoAAPP(solicitudPeticionInfoAAPP);
		
		if (respuestaSolicitudPeticionInfoAAPP != null) {
			if (respuestaSolicitudPeticionInfoAAPP.getInformacion() != null){
				if (respuestaSolicitudPeticionInfoAAPP.getInformacion().getRespuestaPeticionInfoAAPP() != null) {
					idPeticionInfoAAPP = respuestaSolicitudPeticionInfoAAPP.getInformacion().getRespuestaPeticionInfoAAPP().getIdPeticionInfoAAPP();					
				}
			}
		}
//		if (idPeticionInfoAAPP == null) {
//			insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean, AxisObjectSerializerDeserializer.serializeAxisObject(respuestaSolicitudPeticionInfoAAPP, false, false), ScsEejgXmlBean.RESPUESTA, ScsEejgPeticionesBean.EEJG_ESTADO_ERROR);
//		} else {
//			insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean, AxisObjectSerializerDeserializer.serializeAxisObject(respuestaSolicitudPeticionInfoAAPP, false, false), ScsEejgXmlBean.RESPUESTA, ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA);
//		}
		
		return idPeticionInfoAAPP;
	}

	private String getDNITramitador(UsrBean usrBean, ScsEejgPeticionesBean scsEejgPeticionesBean) throws ClsExceptions {
		String nif = null;
		AdmUsuariosAdm admUsuariosAdm = new AdmUsuariosAdm(usrBean);
		Hashtable hash = new Hashtable();
		hash.put(AdmUsuariosBean.C_IDINSTITUCION, scsEejgPeticionesBean.getIdInstitucion());
		hash.put(AdmUsuariosBean.C_IDUSUARIO, scsEejgPeticionesBean.getIdUsuarioPeticion());
		Vector vector = admUsuariosAdm.selectByPK(hash);
		if (vector != null && vector.size() > 0) {
			AdmUsuariosBean admUsuariosBean = (AdmUsuariosBean) vector.get(0);
			nif = admUsuariosBean.getNIF();
		}
		return nif;
	}

	private ScsPersonaJGBean getDatosSolicitante(UsrBean usrBean, ScsEejgPeticionesBean scsEejgPeticionesBean) throws ClsExceptions {
		ScsPersonaJGAdm scsPersonaJGAdm = new ScsPersonaJGAdm(usrBean);
		ScsPersonaJGBean scsPersonaJGBean = null;
		Hashtable hash = new Hashtable();
		hash.put(ScsPersonaJGBean.C_IDINSTITUCION, scsEejgPeticionesBean.getIdInstitucion());
		hash.put(ScsPersonaJGBean.C_IDPERSONA, scsEejgPeticionesBean.getIdPersona());
		Vector vector = scsPersonaJGAdm.selectByPK(hash);
		if (vector != null && vector.size() > 0) {
			scsPersonaJGBean = (ScsPersonaJGBean) vector.get(0);
		}
		return scsPersonaJGBean;
	}

	public int consultaInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		int idXML = -1;
		ServiciosJGExpedienteServiceLocator locator = new ServiciosJGExpedienteServiceLocator(createClientConfig());
		URL url = new URL(urlWS);		
		ServiciosJGExpedienteServiceSoapBindingStub stub = new ServiciosJGExpedienteServiceSoapBindingStub(url, locator);
				
		String idPeticionInfoAAPP = scsEejgPeticionesBean.getIdSolicitud();
		String idioma = scsEejgPeticionesBean.getIdioma();
		
		DatosConsultaInfoAAPP datosConsultaInfoAAPP = new DatosConsultaInfoAAPP(idSistema, idPeticionInfoAAPP, idioma);
		com.siga.eejg.ws.ConsultaInfoAAPP.Informacion informacion = new com.siga.eejg.ws.ConsultaInfoAAPP.Informacion(datosConsultaInfoAAPP);
		ConsultaInfoAAPP consultaInfoAAPP = new ConsultaInfoAAPP(informacion);
				
		RespuestaConsultaInfoAAPP respuestaConsultaInfoAAPP = stub.consultaInfoAAPP(consultaInfoAAPP);		
		
		if (respuestaConsultaInfoAAPP != null) {			
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
			ScsEejgXmlAdm scsEejgXmlAdm = new ScsEejgXmlAdm(usrBean);			
			idXML = insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean, 
					AxisObjectSerializerDeserializer.serializeAxisObject(respuestaConsultaInfoAAPP, false, false), 
					ScsEejgXmlBean.RESPUESTA, ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO);			
		}
				
		return idXML;
	}

	private int insertaLogBDD(ScsEejgXmlAdm scsEejgXmlAdm,	ScsEejgPeticionesBean scsEejgPeticionesBean, String xml, String envioRespuesta, int estado) {		
		int idXml = -1;

		try {
			ScsEejgXmlBean scsEejgXmlBean = new ScsEejgXmlBean();
			idXml = scsEejgXmlAdm.getNuevoIdXml();
			scsEejgXmlBean.setIdXml(idXml);
			scsEejgXmlBean.setIdPeticion(scsEejgPeticionesBean.getIdPeticion());
			scsEejgXmlBean.setEstado(estado);
			scsEejgXmlBean.setEnvioRespuesta(envioRespuesta);
			scsEejgXmlBean.setXml(xml);

			scsEejgXmlAdm.insert(scsEejgXmlBean);
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al insertar log en " + ScsEejgXmlBean.T_NOMBRETABLA, e, 3);
		}
		return idXml;
	}
}
