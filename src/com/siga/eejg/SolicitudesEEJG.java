/**
 * 
 */
package com.siga.eejg;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.handlers.SimpleSessionHandler;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.ConsultaInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.DatosConsultaInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaInfoConsultaInfoAAPP.RespuestaConsultaInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.FirmaInformacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.Informacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;

import service.ServiciosJGExpediente.ServiciosJGExpedienteServiceLocator;
import service.ServiciosJGExpediente.ServiciosJGExpedienteServiceSoapBindingStub;

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

/**
 * @author angelcpe
 *
 */
public class SolicitudesEEJG {
	
	
	private String urlWS;	

	public SolicitudesEEJG() {
		super();
		init();
	}


	private void init() {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		urlWS = rp.returnProperty("eejg.urlWS");		
	}

	private EngineConfiguration createClientConfig() { 
      SimpleProvider clientConfig=new SimpleProvider(); 
      Handler logHandler=(Handler)new LogHandler(); 
      Handler sigHandler=(Handler)new SignerXMLHandler();
      SimpleChain reqHandler=new SimpleChain(); 
      SimpleChain respHandler=new SimpleChain(); 
      reqHandler.addHandler(sigHandler); 
      reqHandler.addHandler(logHandler);
      respHandler.addHandler(logHandler); 
      respHandler.addHandler(sigHandler);
      Handler pivot=(Handler)new HTTPSender(); 
      Handler transport=new SimpleTargetedChain(reqHandler, pivot, respHandler); 
      clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME,transport); 

      return clientConfig;    
    } 

	/**
	 * 
	 * @param scsEejgPeticionesBean
	 * @throws Exception
	 */
	public String solicitudPeticionInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		String idPeticionInfoAAPP = null;
		
		ServiciosJGExpedienteServiceLocator locator = new ServiciosJGExpedienteServiceLocator(createClientConfig());
		URL url = new URL(urlWS);		
		ServiciosJGExpedienteServiceSoapBindingStub stub = new ServiciosJGExpedienteServiceSoapBindingStub(url, locator);
		
		String idSolicitudImportada = null;
		String id = "ID_PETI";
		String idSistema = String.valueOf(scsEejgPeticionesBean.getIdPeticion());
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
		Informacion informacion = new Informacion(datosPeticionInfoAAPP, id);
				
		FirmaInformacion firmaInformacion = new FirmaInformacion();
		SolicitudPeticionInfoAAPP solicitudPeticionInfoAAPP = new SolicitudPeticionInfoAAPP(informacion, firmaInformacion);
				
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


	/**
	 * 
	 * @param usrBean
	 * @param scsEejgPeticionesBean
	 * @return
	 * @throws ClsExceptions
	 */
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


	/**
	 * 
	 * @param scsEejgPeticionesBean
	 * @throws Exception
	 */
	public int consultaInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		
		int idXML = -1;
		ServiciosJGExpedienteServiceLocator locator = new ServiciosJGExpedienteServiceLocator(createClientConfig());
		URL url = new URL(urlWS);		
		ServiciosJGExpedienteServiceSoapBindingStub stub = new ServiciosJGExpedienteServiceSoapBindingStub(url, locator);
		
		String idSistema = String.valueOf(scsEejgPeticionesBean.getIdPeticion());
		String idPeticionInfoAAPP = scsEejgPeticionesBean.getIdSolicitud();
		String idioma = scsEejgPeticionesBean.getIdioma();
		
		DatosConsultaInfoAAPP datosConsultaInfoAAPP = new DatosConsultaInfoAAPP(idSistema, idPeticionInfoAAPP, idioma);
		String id = "ID_INFO"; 
		org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.Informacion informacion = new org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.Informacion(datosConsultaInfoAAPP, id);
		org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.FirmaInformacion firmaInformacion = new org.redabogacia.www.pjgpra.wspjgpra.ConsultaInfoAAPP.FirmaInformacion();
		ConsultaInfoAAPP consultaInfoAAPP = new ConsultaInfoAAPP(informacion, firmaInformacion);
				
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


	/**
	 * 
	 * @param scsEejgXmlAdm
	 * @param scsEejgPeticionesBean
	 * @param xml
	 * @param envioRespuesta
	 */
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
