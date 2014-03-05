package com.siga.eejg;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;
import org.redabogacia.www.pjgpra.wspjgpra.ConsultaInformacionAAPP.ConsultaInformacionAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.ConsultaInformacionAAPP.DatosConsultaInformacionAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.ConsultaInformacionAAPP.InformacionInf;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaConsultaInformacionAAPP.AdministracionInf;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaConsultaInformacionAAPP.DatosInformacionAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaConsultaInformacionAAPP.InformacionAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaConsultaInformacionAAPP.RespuestaConsultaInformacionAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.Respuesta;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPP;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.Informacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;

import service.ServiciosJGExpediente.ServiciosJGExpedienteServiceLocator;
import service.ServiciosJGExpediente.ServiciosJGExpedienteServiceSoapBindingStub;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.beans.eejg.ScsEejgXmlBean;
import com.siga.pfd.ws.DocumentoTO;
import com.siga.pfd.ws.ResultSolicitudDocumentoTO;
import com.siga.pfd.ws.ServiciosPFDServiceServiceSoapBindingStub;
import com.siga.pfd.ws.ServiciosPFDService_ServiceLocator;
import com.siga.pfd.ws.SolicitudDocumentoTO;
import com.sis.firma.core.B64.Base64CODEC;

public class SolicitudesEEJG {
	private String urlWS;
	private String urlWSPFD;
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
		urlWSPFD = admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "PFD_URLWS", "");
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
		
		
		
		if (scsEejgPeticionesBean.getIdInstitucion() == null) {
			throw new IllegalArgumentException("La institucion o zona no puede ser nula");
		}
		
		String idSolicitudImportada = getIdentificadorEjg(usrBean, scsEejgPeticionesBean);	
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
						scsEejgPeticionesBean.setMsgError(error);
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
	private String getIdentificadorEjg(UsrBean usrBean, ScsEejgPeticionesBean scsEejgPeticionesBean) throws ClsExceptions {
		StringBuffer identificadorEjg = null;
		ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
		Hashtable hash = new Hashtable();
		hash.put(ScsEJGBean.C_IDINSTITUCION, scsEejgPeticionesBean.getIdInstitucion());
		hash.put(ScsEJGBean.C_IDTIPOEJG, scsEejgPeticionesBean.getIdTipoEjg());
		hash.put(ScsEJGBean.C_ANIO, scsEejgPeticionesBean.getAnio());
		hash.put(ScsEJGBean.C_NUMERO, scsEejgPeticionesBean.getNumero());
		Vector vector = ejgAdm.selectByPK(hash);
		if (vector != null && vector.size() > 0) {
			ScsEJGBean scsEJGBean = (ScsEJGBean) vector.get(0);
			identificadorEjg = new StringBuffer();
			identificadorEjg.append(scsEJGBean.getAnio());
			identificadorEjg.append("/");
			identificadorEjg.append(scsEJGBean.getNumEJG());
			
		}
		return identificadorEjg!=null?identificadorEjg.toString():null;
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
		
		DatosConsultaInformacionAAPP datosConsultaInformacionAAPP = new DatosConsultaInformacionAAPP (idSistema, idPeticionInfoAAPP, idioma);
		InformacionInf informacion = new InformacionInf(datosConsultaInformacionAAPP);
		ConsultaInformacionAAPP consultaInformacionAAPP = new ConsultaInformacionAAPP(informacion);
				
		RespuestaConsultaInformacionAAPP respuestaConsultaInfoAAPP = stub.consultaInformacionAAPP(consultaInformacionAAPP);		
		
		if (respuestaConsultaInfoAAPP != null) {
			if (respuestaConsultaInfoAAPP.getInformacionInf() != null){
				 InformacionAAPP infoRespuestaConsultaInfo= respuestaConsultaInfoAAPP.getInformacionInf().getInformacionAAPP();
				
				if (infoRespuestaConsultaInfo != null) {
					if ((infoRespuestaConsultaInfo.getTipoError() != null && !infoRespuestaConsultaInfo.getTipoError().trim().equals("")) || (infoRespuestaConsultaInfo.getDescripcionError() != null && !infoRespuestaConsultaInfo.getDescripcionError().trim().equals(""))) {
						String error = infoRespuestaConsultaInfo.getTipoError() + ": " + infoRespuestaConsultaInfo.getDescripcionError();
						scsEejgPeticionesBean.setMsgError(error);
						throw new ClsExceptions("IdPetición: " + scsEejgPeticionesBean.getIdPeticion() + ". Se ha obtenido el siguiente mensaje de error como respuesta del webservice para el idSolicitud \"" + idPeticionInfoAAPP + "\": " + error);
					}
					
					UsrBean usrBean = new UsrBean();
					usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
					ScsEejgXmlAdm scsEejgXmlAdm = new ScsEejgXmlAdm(usrBean);			
					idXML = insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean,null,ScsEejgXmlBean.RESPUESTA, EEJG_ESTADO.FINALIZADO);
					
					if (isPendiente(infoRespuestaConsultaInfo.getDatosInformacionAAPP())) {
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.PENDIENTE_INFO.getId());
					} else {
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.FINALIZADO.getId());
						scsEejgPeticionesBean.setCsv(infoRespuestaConsultaInfo.getDatosInformacionAAPP().getCSV());
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
	private boolean isPendiente(DatosInformacionAAPP datosInfoAAPP) {
		boolean pendiente = false;
		if (datosInfoAAPP != null) {
			AdministracionInf[] administracions = datosInfoAAPP.getAdministracionInf();
			if (administracions != null) {
				for (AdministracionInf administracion : administracions) {
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
	private int insertaLogBDD(ScsEejgXmlAdm scsEejgXmlAdm,	ScsEejgPeticionesBean scsEejgPeticionesBean, String xml, String envioRespuesta, EEJG_ESTADO eejgEstado) {		
		int idXml = -1;

		try {
			ScsEejgXmlBean scsEejgXmlBean = new ScsEejgXmlBean();
			idXml = scsEejgXmlAdm.getNuevoIdXml();
			scsEejgXmlBean.setIdXml(idXml);
			scsEejgXmlBean.setIdPeticion(scsEejgPeticionesBean.getIdPeticion());
			scsEejgXmlBean.setEstado((int)eejgEstado.getId());
			scsEejgXmlBean.setEnvioRespuesta(envioRespuesta);
			//scsEejgXmlBean.setXml(xml);

			scsEejgXmlAdm.insert(scsEejgXmlBean);
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al insertar log en " + ScsEejgXmlBean.T_NOMBRETABLA, e, 3);
		}
		return idXml;
	}
	
	public String getDocumentoTO(String csv) throws ClsExceptions {
		
		//Fichero a recuperar
		String contenidoPDF = "";
		
		try {
			//Configuramos los datos de acceso al WS
			URL url = new URL(urlWSPFD);		
			ServiciosPFDService_ServiceLocator locator = new ServiciosPFDService_ServiceLocator();
			ServiciosPFDServiceServiceSoapBindingStub stub = new ServiciosPFDServiceServiceSoapBindingStub(url, locator);
			SolicitudDocumentoTO solDocTO = new SolicitudDocumentoTO();			
			solDocTO.setIdAppCliente(idSistema);
			solDocTO.setIdValidacion(csv);
			//solDocTO.setIdValidacion("PJGPRA-IQQQ1-R06QK-SSRBI-KCC1Z");

			//Llamada al Stub del web service
			ClsLogging.writeFileLog("Llamada al servicio web obtenerDocumento", 10);
			ResultSolicitudDocumentoTO obtDoc = stub.obtenerDocumento(solDocTO);
			
			if(obtDoc!=null){
				if (obtDoc.getResultado()!=null && obtDoc.getResultado().toUpperCase().equals(AppConstants.PFD_SOLICITUD_DOCUMENTO_OK)){
					DocumentoTO documentoTO = obtDoc.getDocumento();
					contenidoPDF = documentoTO.getFirmab64().toString();
				
				} else if(obtDoc.getResultado().toUpperCase().equals(AppConstants.PFD_SOLICITUD_DOCUMENTO_KO)){
					ClsLogging.writeFileLog("El resultado de la obtencion del documento de la PFD es incorrecto", 10);
				}
			}
			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al solicitar documento a la PFD", e, 3);
		}			
		
		return contenidoPDF;
	}
	
}
