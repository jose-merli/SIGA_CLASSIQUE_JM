package com.siga.eejg;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.beans.eejg.ScsEejgXmlBean;
import com.siga.eejg.ws.ServiciosJGExpedienteServiceLocator;
import com.siga.eejg.ws.ServiciosJGExpedienteServiceSoapBindingStub;
import com.siga.eejg.ws.ConsultaInfoAAPP.ConsultaInfoAAPP;
import com.siga.eejg.ws.ConsultaInfoAAPP.DatosConsultaInfoAAPP;
import com.siga.eejg.ws.RespuestaInfoConsultaInfoAAPP.Administracion;
import com.siga.eejg.ws.RespuestaInfoConsultaInfoAAPP.DatosInfoAAPP;
import com.siga.eejg.ws.RespuestaInfoConsultaInfoAAPP.RespuestaConsultaInfoAAPP;
import com.siga.eejg.ws.RespuestaSolicitudPeticionInfoAAPP.Respuesta;
import com.siga.eejg.ws.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPP;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.Informacion;
import com.siga.eejg.ws.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;

public class SolicitudesEEJG {
	private String urlWS;	
	private String idSistema;

	public SolicitudesEEJG() throws ClsExceptions {
		super();
		init();
	}

	private void init() throws ClsExceptions {
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
		urlWS = admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_URLWS", "");
		idSistema = admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_IDSISTEMA", "");
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
		
		
		
		DatosPeticionInfoAAPP datosPeticionInfoAAPP = new DatosPeticionInfoAAPP(idSistema, idSolicitudImportada, idZona, dNI_NIE_Tramitador, scsEejgPeticionesBean.getNif(), scsEejgPeticionesBean.getNombre(), scsEejgPeticionesBean.getApellido1(), scsEejgPeticionesBean.getApellido2(), scsEejgPeticionesBean.getIdioma());
		Informacion informacion = new Informacion(datosPeticionInfoAAPP);
				
		SolicitudPeticionInfoAAPP solicitudPeticionInfoAAPP = new SolicitudPeticionInfoAAPP(informacion);
				
		RespuestaSolicitudPeticionInfoAAPP respuestaSolicitudPeticionInfoAAPP =  stub.solicitudPeticionInfoAAPP(solicitudPeticionInfoAAPP);
		
		if (respuestaSolicitudPeticionInfoAAPP != null) {
			if (respuestaSolicitudPeticionInfoAAPP.getInformacion() != null){
				Respuesta respuesta = respuestaSolicitudPeticionInfoAAPP.getInformacion().getRespuestaPeticionInfoAAPP();
				if (respuesta != null) {
					if ((respuesta.getTipoError() != null && !respuesta.getTipoError().trim().equals("")) || (respuesta.getDescripcionError() != null && !respuesta.getDescripcionError().trim().equals(""))) {
						String error = respuesta.getTipoError() + ": " + respuesta.getDescripcionError();
						throw new ClsExceptions("IdPetición: " + scsEejgPeticionesBean.getIdPeticion() + ". Se ha obtenido el siguiente mensaje de error como respuesta del webservice para el colegio " + idZona + " y DNI/NIE solicitado \"" + scsEejgPeticionesBean.getNif() + "\" y DNI/NIE del tramitador \""  + dNI_NIE_Tramitador + "\": " + error);
					} else {
						idPeticionInfoAAPP = respuestaSolicitudPeticionInfoAAPP.getInformacion().getRespuestaPeticionInfoAAPP().getIdPeticionInfoAAPP();
					}
				}
			}
		}
		
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
	 * @param scsEejgPeticionesBean
	 * @return
	 * @throws Exception
	 */
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
			if (respuestaConsultaInfoAAPP.getInformacion() != null){
				com.siga.eejg.ws.RespuestaInfoConsultaInfoAAPP.ConsultaInfoAAPP respuestaConsultaInfo = respuestaConsultaInfoAAPP.getInformacion().getConsultaInfoAAPP();
				if (respuestaConsultaInfo != null) {
					if ((respuestaConsultaInfo.getTipoError() != null && !respuestaConsultaInfo.getTipoError().trim().equals("")) || (respuestaConsultaInfo.getDescripcionError() != null && !respuestaConsultaInfo.getDescripcionError().trim().equals(""))) {
						String error = respuestaConsultaInfo.getTipoError() + ": " + respuestaConsultaInfo.getDescripcionError();
						throw new ClsExceptions("IdPetición: " + scsEejgPeticionesBean.getIdPeticion() + ". Se ha obtenido el siguiente mensaje de error como respuesta del webservice para el idSolicitud \"" + idPeticionInfoAAPP + "\": " + error);
					}
					UsrBean usrBean = new UsrBean();
					usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
					ScsEejgXmlAdm scsEejgXmlAdm = new ScsEejgXmlAdm(usrBean);			
					idXML = insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean, 
							AxisObjectSerializerDeserializer.serializeAxisObject(respuestaConsultaInfoAAPP, false, false), 
							ScsEejgXmlBean.RESPUESTA, ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO);
					
					if (isPendiente(respuestaConsultaInfo.getDatosInfoAAPP())) {
						scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_PENDIENTE_INFO);
					} else {
						scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO);
					}
					
				}
			}				
						
		}
				
		return idXML;
	}

	/**
	 * 
	 * @param datosInfoAAPP
	 * @return
	 */
	private boolean isPendiente(DatosInfoAAPP datosInfoAAPP) {
		boolean pendiente = false;
		if (datosInfoAAPP != null) {
			Administracion[] administracions = datosInfoAAPP.getAdministracion();
			if (administracions != null) {
				for (Administracion administracion : administracions) {
					if (administracion.getFecha_Respuesta() == null || administracion.getFecha_Respuesta().trim().equals("")) {
						pendiente = true;
						break;
					}
				}
			}
		}
		return pendiente;
	}

	/**
	 * 
	 * @param scsEejgXmlAdm
	 * @param scsEejgPeticionesBean
	 * @param xml
	 * @param envioRespuesta
	 * @param estado
	 * @return
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
