package com.siga.eejg;

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.v2.pjgpra.wspjgpra.ConsultaInformacionAAPP.ConsultaInformacionAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.ConsultaInformacionAAPP.DatosConsultaInformacionAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.ConsultaInformacionAAPP.DatosConsultaInformacionAAPPIdioma;
import org.redabogacia.v2.pjgpra.wspjgpra.ConsultaInformacionAAPP.InformacionInf;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaConsultaInformacionCompletaAAPP.AdministracionCompleta;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaConsultaInformacionCompletaAAPP.DatosInformacionCompletaAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaConsultaInformacionCompletaAAPP.InformacionCompletaAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaConsultaInformacionCompletaAAPP.RespuestaConsultaInformacionCompletaAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.Respuesta;
import org.redabogacia.v2.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosAmpliadosPeticionInfoAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosContacto;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosDireccionSolicitante;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosPeticionInfoAAPPIdioma;


import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosProcedimiento;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.DatosProcedimientoTipoSolicitante;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.Informacion;
import org.redabogacia.v2.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;
import org.redabogacia.v2.pjgpra.wspjgpra.service.ServiciosJGExpedienteServiceLocator;
import org.redabogacia.v2.pjgpra.wspjgpra.service.ServiciosJGExpedienteServiceSoapBindingStub;


import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.beans.eejg.ScsEejgXmlBean;
import com.siga.pfd.ws.DocumentoTO;
import com.siga.pfd.ws.ResultSolicitudDocumentoTO;
import com.siga.pfd.ws.ServiciosPFDServiceServiceSoapBindingStub;
import com.siga.pfd.ws.ServiciosPFDService_ServiceLocator;
import com.siga.pfd.ws.SolicitudDocumentoTO;
/**
 * 
 * La imaginación es más importante que el conocimiento.
 * @author jorgeta
 * 
 */
public class SolicitudesEEJGInformacionCompleta {
	private String urlWS;
	private String urlWSPFD;
	private String idSistema;
	private static final Logger logger = Logger.getLogger(SolicitudesEEJGInformacionCompleta.class);
	public SolicitudesEEJGInformacionCompleta() throws ClsExceptions {
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
		return new FileProvider(SolicitudesEEJGInformacionCompleta.class.getResourceAsStream("/siga-eejg.wsdd"));		
	} 
	
	private DatosAmpliadosPeticionInfoAAPP getDatosAmpliadosPeticionInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean,UsrBean usrBean)throws Exception{
		DatosAmpliadosPeticionInfoAAPP datosAmpliadosPeticionInfoAAPP = new DatosAmpliadosPeticionInfoAAPP();
		//Actualizamos los datos de la direccion y del contacto del solicitante
		actualizaDatosSolicitante(datosAmpliadosPeticionInfoAAPP,scsEejgPeticionesBean,usrBean);
		
		DatosProcedimiento datosProcedimiento =  getDatosProcedimiento(scsEejgPeticionesBean,usrBean);
		datosAmpliadosPeticionInfoAAPP.setDatosProcedimiento(datosProcedimiento);
		return datosAmpliadosPeticionInfoAAPP;
		
	}

	private DatosAmpliadosPeticionInfoAAPP actualizaDatosSolicitante(DatosAmpliadosPeticionInfoAAPP datosAmpliadosPeticionInfoAAPP,ScsEejgPeticionesBean scsEejgPeticionesBean,UsrBean usrBean )throws Exception{
		DatosDireccionSolicitante datosDireccionSolicitante = new DatosDireccionSolicitante();
		datosAmpliadosPeticionInfoAAPP.setDatosDireccionSolicitante(datosDireccionSolicitante);
		DatosContacto datosContacto = new DatosContacto();
		datosAmpliadosPeticionInfoAAPP.setDatosContacto(datosContacto);
		
		
		ScsPersonaJGAdm personaJGAdm = new ScsPersonaJGAdm(usrBean);
		Hashtable personaJGHashtable = personaJGAdm.getDatosPersonaJG(scsEejgPeticionesBean.getIdPersona().toString(), scsEejgPeticionesBean.getIdInstitucion().toString());
		//Rellenamos los datos de direcciones
		if(personaJGHashtable.get("DOMI_VIA_PJG")!=null)
			datosDireccionSolicitante.setTipoVia((String)personaJGHashtable.get("DOMI_IDVIA_PJG"));
		
		if(personaJGHashtable.get("DOMI_DIRECCION_PJG")!=null)
			datosDireccionSolicitante.setNombreVia((String)personaJGHashtable.get("DOMI_DIRECCION_PJG"));
		if(personaJGHashtable.get("DOMI_NUMERO_PJG")!=null)
			datosDireccionSolicitante.setNumero((String)personaJGHashtable.get("DOMI_NUMERO_PJG"));
		if(personaJGHashtable.get("DOMI_ESCALERA_PJG")!=null)
			datosDireccionSolicitante.setEscalera((String)personaJGHashtable.get("DOMI_ESCALERA_PJG"));
		if(personaJGHashtable.get("DOMI_PISO_PJG")!=null)
			datosDireccionSolicitante.setPiso((String)personaJGHashtable.get("DOMI_PISO_PJG"));
		if(personaJGHashtable.get("DOMI_PUERTA_PJG")!=null)
			datosDireccionSolicitante.setPuerta((String)personaJGHashtable.get("DOMI_PUERTA_PJG"));
		if(personaJGHashtable.get("CP_PJG")!=null)
			datosDireccionSolicitante.setCodigoPostal((String)personaJGHashtable.get("CP_PJG"));
		if(personaJGHashtable.get("IDMUNICIPIO_PJG")!=null){
			//en el test de compatibilidad le han quitado los ceros de la derecha
			String idMunicipioEJIS = (String)personaJGHashtable.get("IDMUNICIPIO_PJG");
			if(idMunicipioEJIS.length()>5)
				idMunicipioEJIS = idMunicipioEJIS.substring(0,5);
			datosDireccionSolicitante.setMunicipio(idMunicipioEJIS);
		}
		if(personaJGHashtable.get("IDPROVINCIA_PJG")!=null)
			datosDireccionSolicitante.setProvincia((String)personaJGHashtable.get("IDPROVINCIA_PJG"));
		//Rellenamos los datos de contacto
		if(personaJGHashtable.get("TELEFONO1_PJG")!=null){
			String telefono =  (String)personaJGHashtable.get("TELEFONO1_PJG");
			if(telefono.startsWith("9")||telefono.startsWith("8"))
				datosContacto.setTelefonoFijo(telefono);
			else if(telefono.startsWith("6")||telefono.startsWith("7"))
				datosContacto.setTelefonoMovil(telefono);
		}
		if(personaJGHashtable.get("MOVIL_PJG")!=null)
			datosContacto.setTelefonoMovil((String)personaJGHashtable.get("MOVIL_PJG"));
		if(personaJGHashtable.get("CORREOELECTRONICO_PJG")!=null)
			datosContacto.setEmail((String)personaJGHashtable.get("CORREOELECTRONICO_PJG"));
		
		return datosAmpliadosPeticionInfoAAPP;
		
	}
	private DatosProcedimiento getDatosProcedimiento(ScsEejgPeticionesBean scsEejgPeticionesBean,UsrBean usrBean)throws Exception{
		
		DatosProcedimiento datosProcedimiento = new DatosProcedimiento();
		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usrBean);
		Hashtable defensaJuridicaHashtable = scsEJGAdm.getDatosDefensaJuridica(scsEejgPeticionesBean.getIdInstitucion().shortValue(), scsEejgPeticionesBean.getIdTipoEjg().shortValue(),scsEejgPeticionesBean.getAnio().shortValue(),scsEejgPeticionesBean.getNumero());
		if(defensaJuridicaHashtable.get("DECLARANTE")!=null){
			DatosProcedimientoTipoSolicitante procedimientoTipoSolicitante = DatosProcedimientoTipoSolicitante.fromValue((String)defensaJuridicaHashtable.get("DECLARANTE"));
			datosProcedimiento.setTipoSolicitante(procedimientoTipoSolicitante);
		}
		if(defensaJuridicaHashtable.get("JURISDICCION")!=null)
			datosProcedimiento.setJurisdiccion((String)defensaJuridicaHashtable.get("JURISDICCION"));
		if(defensaJuridicaHashtable.get("NUMPROCEDIMINETO")!=null){
			String numProc = (String)defensaJuridicaHashtable.get("NUMPROCEDIMINETO");
			datosProcedimiento.setNumeroProcedimiento(UtilidadesString.replaceAllIgnoreCase(numProc, "/", ""));
		}
		if(defensaJuridicaHashtable.get("PRETENSION")!=null)
			datosProcedimiento.setObjetoPretension((String)defensaJuridicaHashtable.get("PRETENSION"));
		if(defensaJuridicaHashtable.get("JUZGADO")!=null)
			datosProcedimiento.setOrganoJudicial((String)defensaJuridicaHashtable.get("JUZGADO"));
		if(defensaJuridicaHashtable.get("SITUACION")!=null)
			datosProcedimiento.setSituacionProcedimiento((String)defensaJuridicaHashtable.get("SITUACION"));
		return datosProcedimiento;
		
	}
	/**
	 * Este memtodo todavia no esta probado...
	 * @param scsEejgPeticionesBean
	 * @return
	 * @throws Exception
	 */
	public String solicitudPeticionInfoAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		//Metemos el idioma para los recursos
		usrBean.setLanguage(ClsConstants.ES);
		
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
//		DatosPeticionInfoAAPP datosPeticionInfoAAPP = new DatosPeticionInfoAAPP(idSistema, idSolicitudImportada, idZona, dNI_NIE_Tramitador, scsEejgPeticionesBean.getNif(), scsEejgPeticionesBean.getNombre(), scsEejgPeticionesBean.getApellido1(), scsEejgPeticionesBean.getApellido2(),scsEejgPeticionesBean.getIdioma());
		DatosPeticionInfoAAPPIdioma datosConsultaInformacionAAPPIdioma = DatosPeticionInfoAAPPIdioma.fromValue(scsEejgPeticionesBean.getIdioma());
		DatosPeticionInfoAAPP datosPeticionInfoAAPP = new DatosPeticionInfoAAPP(idSistema, idSolicitudImportada, idZona, dNI_NIE_Tramitador, scsEejgPeticionesBean.getNif(), scsEejgPeticionesBean.getNombre(), scsEejgPeticionesBean.getApellido1(), scsEejgPeticionesBean.getApellido2(),datosConsultaInformacionAAPPIdioma);
		DatosAmpliadosPeticionInfoAAPP datosAmpliadosPeticionInfoAAPP = getDatosAmpliadosPeticionInfoAAPP(scsEejgPeticionesBean,usrBean);
		validaDatosAmpliados(datosAmpliadosPeticionInfoAAPP);
		Informacion informacion = new Informacion(datosPeticionInfoAAPP,datosAmpliadosPeticionInfoAAPP);
				
		SolicitudPeticionInfoAAPP solicitudPeticionInfoAAPP = new SolicitudPeticionInfoAAPP(informacion);
		
		ClsLogging.writeFileLog("Llamada al servicio web solicitudPeticionInfoAAPP", 10);
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
	/**
	 * Validando datos enviado. Esto deberia validarolo el esquema. Hasta entonces hacemos las mismas validacinoes que ha hecho Panel.
	 */
	private void validaDatosAmpliados(DatosAmpliadosPeticionInfoAAPP datosAmpliadosPeticionInfoAAPP) throws BusinessException {
		String email = "";
		DatosContacto datosContactoTo = datosAmpliadosPeticionInfoAAPP.getDatosContacto();
		StringBuilder descError = new StringBuilder("");
		logger.info("RESULTADO ZONA COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		email = datosContactoTo.getEmail().toUpperCase();

		
		
		// Validar que el EMAIL tenga formato válido.
		if (email != null && !email.trim().equals("")) {
			byte[] s = email.getBytes();
			int length = s.length;
			byte b = 0;
			boolean resultadoOp = true;
			for (int i = 0; i < length; i++) {
				b = s[i];
				
				if ((b >= 'a') && (b <= 'z')) {
					resultadoOp = true;
				}
				else if ((b >= 'A') && (b <= 'Z')) {
					resultadoOp = true;
				}
				else if ((b >= '0') && (b <= '9')) {
					resultadoOp = true;
				}
				else if (((b == '_') || (b == '-') || (b == '.') || (b == '@'))
						&& (i != 0) 
						&& ((i + 1) != length)) {
					resultadoOp = true;
				}
				else {
					logger.error("EL EMAIL TIENE UN FORMATO NO VÁLIDO. ");
					descError.append("EL EMAIL no tiene un formato válido.\n");
					resultadoOp = false;
					break;
				}
				
			}

			// So the email contains valid characters, but does it contain the
			// below ones.
			if (resultadoOp && email.indexOf("@") < 0) {
				logger.error("EL EMAIL TIENE UN FORMATO NO VÁLIDO( No contiene @) ");
				descError.append("EL EMAIL no tiene un formato válido.\n");
			}

			if (resultadoOp && email.indexOf(".") < 0) {
				logger.error("EL EMAIL TIENE UN FORMATO NO VÁLIDO( No contiene . ). ");
				descError.append("EL EMAIL no tiene un formato válido.\n");
			}

		}

		logger.info("RESULTADO EMAIL COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		String tlfnoFijo = datosContactoTo.getTelefonoFijo();
		int tlfnoFijoAsInteger = 0;

		// Validar que el TELÉFONO FIJO tenga formato válido.
		if (tlfnoFijo!=null && (!tlfnoFijo.equals("")) && (tlfnoFijo.length() > 0)) {
			if (tlfnoFijo.length() == 9) {
				logger.info("EL TELÉFONO FIJO TIENE UNA LONGITUD DE FORMATO VÁLIDA. ");
				if ((tlfnoFijo.startsWith("8")) || (tlfnoFijo.startsWith("9"))) {
					logger.info("EL TELÉFONO FIJO TIENE UN FORMATO VÁLIDO ");

					try {
						tlfnoFijoAsInteger = new Integer(tlfnoFijo).intValue();

						if ((tlfnoFijoAsInteger < 0)) {
							logger.error("EL TELÉFONO FIJO TIENE UN FORMATO NO VÁLIDO.(Debe ser numérico) ");
							descError.append("EL TELÉFONO FIJO no tiene un formato válido(Debe ser numérico).\n");
						}

					} catch (NumberFormatException e) {
						logger.error("EL TELÉFONO FIJO TIENE UN FORMATO NO VÁLIDO(Debe ser numérico). ");
						descError.append("EL TELÉFONO FIJO no tiene un formato válido(Debe ser numérico).\n");
					}
				} else {
					logger.error("EL TELÉFONO FIJO TIENE UN FORMATO NO VÁLIDO(Debe empezar por 8 ó 9). ");
					descError.append("EL TELÉFONO FIJO no tiene un formato válido(Debe empezar por 8 ó 9).\n");
				}
			} else {
				logger.error("EL TELÉFONO FIJO TIENE UN FORMATO NO VÁLIDO(El tamaño debe ser 9). ");
				descError.append("EL TELÉFONO FIJO no tiene un formato válido(El tamaño debe ser 9).\n");
			}
		} 
		String tlfnoMovil = "";

		logger.info("RESULTADO TELÉFONO FIJO COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		tlfnoMovil = datosContactoTo.getTelefonoMovil().toUpperCase();
		int tlfnoMovilAsInteger = 0;

		// Validar que el TELÉFONO MÓVIL tenga formato válido.
		if ((!tlfnoMovil.equals("")) && (tlfnoMovil.length() > 0)) {
			if (tlfnoMovil.length() == 9) {
				logger.info("EL TELÉFONO MÓVIL TIENE UNA LONGITUD DE FORMATO VÁLIDA. ");
				if ((tlfnoMovil.startsWith("6")) || (tlfnoMovil.startsWith("7"))) {
					logger.info("EL TELÉFONO MÓVIL TIENE UN FORMATO VÁLIDO ");

					try {
						tlfnoMovilAsInteger = new Integer(tlfnoMovil).intValue();

						if ((tlfnoMovilAsInteger < 0)) {
							logger.error("EL TELÉFONO MÓVIL TIENE UN FORMATO NO VÁLIDO(Debe ser numérico). ");
							descError.append("EL TELÉFONO MÓVIL no tiene un formato válido(Debe ser numérico).\n");
						}

					} catch (NumberFormatException e) {
						logger.error("EL TELÉFONO MÓVIL TIENE UN FORMATO NO VÁLIDO(Debe ser numérico). ");
						descError.append("EL TELÉFONO MÓVIL no tiene un formato válido(Debe ser numérico).\n");
					}
				} else {
					logger.error("EL TELÉFONO MÓVIL TIENE UN FORMATO NO VÁLIDO(Debe empezar por 6 ó 7). ");
					descError.append("EL TELÉFONO MÓVIL no tiene un formato válido(Debe empezar por 6 ó 7).\n");
				}
			} else {
				logger.error("EL TELÉFONO MÓVIL TIENE UN FORMATO NO VÁLIDO(El tamaño debe ser 9). ");
				descError.append("EL TELÉFONO MÓVIL no tiene un formato válido(El tamaño debe ser 9).\n");
			}
		} 

		DatosDireccionSolicitante datosDireccionSolicitanteTo = datosAmpliadosPeticionInfoAAPP.getDatosDireccionSolicitante();
		DatosProcedimiento datosProcedimientoTo = datosAmpliadosPeticionInfoAAPP.getDatosProcedimiento();
		String cp = "";

		logger.info("RESULTADO JURISDICCION COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		cp = datosDireccionSolicitanteTo.getCodigoPostal();

		// Validar que el CÓDIGO POSTAL tenga formato válido.
		if ((!cp.equals("")) && (cp.length() > 0)) {
			if (cp.length() == 5) {
				logger.info("EL CÓDIGO POSTAL TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL CÓDIGO POSTAL TIENE UN FORMATO NO VÁLIDO(El tamaño debe ser 5). ");
				descError.append("EL CÓDIGO POSTAL no tiene un formato válido(El tamaño debe ser 5).\n");
			}
		}

		String nombreVia = "";

		logger.info("RESULTADO CÓDIGO POSTAL COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		nombreVia = datosDireccionSolicitanteTo.getNombreVia();

		// Validar que el NOMBRE DE LA VIA tenga formato válido.
		if ((!nombreVia.equals("")) && (nombreVia.length() > 0)) {
			if (nombreVia.length() <= 100) {
				logger.info("EL NOMBRE DE LA VIA TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL NOMBRE DE LA VIA TIENE UN FORMATO NO VÁLIDO(El tamaño debe ser < 100). ");
				descError.append("EL NOMBRE DE LA VIA no tiene un formato válido(El tamaño debe ser < 100).\n");
			}
		}

		String numero = "";

		logger.info("RESULTADO NOMBRE DE LA VÍA COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		numero = datosDireccionSolicitanteTo.getNumero();

		// Validar que el NÚMERO DE LA VÍA tenga formato válido.
		if ((!numero.equals("")) && (numero.length() > 0)) {
			if (numero.length() <= 10) {
				logger.info("EL NÚMERO DE LA VÍA TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL NÚMERO DE LA VÍA TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("EL NÚMERO DE LA VÍA no tiene un formato válido.\n");
			}
		} 

		String escalera = "";

		logger.info("RESULTADO NÚMERO DE LA VÍA COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		escalera = datosDireccionSolicitanteTo.getEscalera();

		// Validar que la ESCALERA tenga formato válido.
		if ((!escalera.equals("")) && (escalera.length() > 0)) {
			if (escalera.length() <= 5) {
				logger.info("LA ESCALERA TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("LA ESCALERA TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("LA ESCALERA no tiene un formato válido.\n");
			}
		}

		String piso = "";

		logger.info("RESULTADO ESCALERA COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		piso = datosDireccionSolicitanteTo.getPiso();

		// Validar que el PISO tenga formato válido.
		if ((!piso.equals("")) && (piso.length() > 0)) {
			if (piso.length() <= 5) {
				logger.info("EL PISO TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL PISO TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("EL PISO no tiene un formato válido.\n");
			}
		}

		String puerta = "";

		logger.info("RESULTADO PISO COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		puerta = datosDireccionSolicitanteTo.getPiso();

		// Validar que la PUERTA tenga formato válido.
		if ((!puerta.equals("")) && (puerta.length() > 0)) {
			if (puerta.length() <= 5) {
				logger.info("LA PUERTA TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("LA PUERTA TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("LA PUERTA no tiene un formato válido.\n");
			}
		} 

		String refCatastral = "";

		logger.info("RESULTADO PUERTA COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		refCatastral = datosDireccionSolicitanteTo.getReferenciaCatastral();

		// Validar que la REF. CATASTRAL tenga formato válido.
		if (refCatastral!=null && (!refCatastral.equals("")) && (refCatastral.length() > 0)) {
			if (refCatastral.length() <= 100) {
				logger.info("LA REF. CATASTRAL TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("LA REF. CATASTRAL TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("LA REF. CATASTRAL no tiene un formato válido.\n");
			}
		} 

		String objeto = "";

		logger.info("RESULTADO REF. CATASTRAL COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		objeto = datosProcedimientoTo.getObjetoPretension();

		// Validar que el OBJETO Y PRETENSIÓN tenga formato válido.
		if ((!objeto.equals("")) && (objeto.length() > 0)) {
			if (objeto.length() <= 100) {
				logger.info("EL OBJETO Y PRETENSIÓN TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL OBJETO Y PRETENSIÓN TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("EL OBJETO Y PRETENSIÓN no tiene un formato válido.\n");
			}
		} 

		String organoJudicial = "";

		logger.info("RESULTADO OBJETO Y PRETENSIÓN COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		organoJudicial = datosProcedimientoTo.getOrganoJudicial();

		// Validar que el ÓRGANO JUDICIAL tenga formato válido.
		if ((!organoJudicial.equals("")) && (organoJudicial.length() > 0)) {
			if (organoJudicial.length() <= 100) {
				logger.info("EL ÓRGANO JUDICIAL TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL ÓRGANO JUDICIAL TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("EL ÓRGANO JUDICIAL no tiene un formato válido.\n");
			}
		}

		String numProcedimiento = "";

		logger.info("RESULTADO ÓRGANO JUDICIAL COMUNICACION OK. SEGUIMOS VALIDANDO... ");
		numProcedimiento = datosProcedimientoTo.getNumeroProcedimiento();

		// Validar que el NÚMERO DE PROCEDIMIENTO tenga formato válido.
		if ((!numProcedimiento.equals("")) && (numProcedimiento.length() > 0)) {
			if (numProcedimiento.length() <= 100) {
				logger.info("EL NÚMERO DE PROCEDIMIENTO TIENE UN FORMATO VÁLIDO. ");
			} else {
				logger.error("EL NÚMERO DE PROCEDIMIENTO TIENE UN FORMATO NO VÁLIDO. ");
				descError.append("EL NÚMERO DE PROCEDIMIENTO no tiene un formato válido.\n");
			}
		}
		if (!descError.toString().equals(""))
			throw new BusinessException(descError.toString());

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
	public int consultaInformacionCompletaAAPP(ScsEejgPeticionesBean scsEejgPeticionesBean) throws Exception {
		int idXML = -1;
		ServiciosJGExpedienteServiceLocator locator = new ServiciosJGExpedienteServiceLocator(createClientConfig());
		URL url = new URL(urlWS);		
		ServiciosJGExpedienteServiceSoapBindingStub stub = new ServiciosJGExpedienteServiceSoapBindingStub(url, locator);
				
		String idSolicitud = scsEejgPeticionesBean.getIdSolicitud();
		String idioma = scsEejgPeticionesBean.getIdioma();

//		DatosConsultaInformacionAAPP datosConsultaInformacionAAPP = new DatosConsultaInformacionAAPP (idSistema, idPeticionInfoAAPP, idioma);
		DatosConsultaInformacionAAPPIdioma datosConsultaInformacionAAPPIdioma = DatosConsultaInformacionAAPPIdioma.fromValue(idioma);
		DatosConsultaInformacionAAPP datosConsultaInformacionAAPP = new DatosConsultaInformacionAAPP (idSistema, idSolicitud, datosConsultaInformacionAAPPIdioma);
		InformacionInf informacion = new InformacionInf(datosConsultaInformacionAAPP);		
		ConsultaInformacionAAPP consultaInformacionAAPP = new ConsultaInformacionAAPP(informacion);
		
		ClsLogging.writeFileLog("Llamada al servicio web consultaInformacionAAPP", 10);
		RespuestaConsultaInformacionCompletaAAPP respuestaConsultaInformacionCompletaAAPP = stub.consultaInformacionCompletaAAPP(consultaInformacionAAPP);
		String response = stub._getCall().getMessageContext().getResponseMessage().getSOAPPartAsString();
		
		if (respuestaConsultaInformacionCompletaAAPP != null) {
			if (respuestaConsultaInformacionCompletaAAPP.getInformacionCompleta() != null){
				 InformacionCompletaAAPP infoRespuestaConsultaInfo= respuestaConsultaInformacionCompletaAAPP.getInformacionCompleta().getInformacionCompletaAAPP();
				
				if (infoRespuestaConsultaInfo != null) {
					if ((infoRespuestaConsultaInfo.getTipoError() != null && !infoRespuestaConsultaInfo.getTipoError().trim().equals("")) || (infoRespuestaConsultaInfo.getDescripcionError() != null && !infoRespuestaConsultaInfo.getDescripcionError().trim().equals(""))) {
						String error = infoRespuestaConsultaInfo.getTipoError() + ": " + infoRespuestaConsultaInfo.getDescripcionError();
						scsEejgPeticionesBean.setMsgError(error);
						throw new ClsExceptions("IdPetición: " + scsEejgPeticionesBean.getIdPeticion() + ". Se ha obtenido el siguiente mensaje de error como respuesta del webservice para el idSolicitud \"" + idSolicitud + "\": " + error);
					}
					
					UsrBean usrBean = new UsrBean();
					usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
					ScsEejgXmlAdm scsEejgXmlAdm = new ScsEejgXmlAdm(usrBean);			
//					idXML = insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean,AxisObjectSerializerDeserializer.serializeAxisObject(respuestaConsultaInformacionCompletaAAPP, false, false),ScsEejgXmlBean.RESPUESTA, EEJG_ESTADO.FINALIZADO);
					idXML = insertaLogBDD(scsEejgXmlAdm, scsEejgPeticionesBean,response,ScsEejgXmlBean.RESPUESTA, EEJG_ESTADO.FINALIZADO);
					
					if (isPendiente(infoRespuestaConsultaInfo.getDatosInformacionCompletaAAPP())) {
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.PENDIENTE_INFO.getId());
					} else {
						ClsLogging.writeFileLog("Finalizado Expediente Economico", 10);
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.FINALIZADO.getId());
						scsEejgPeticionesBean.setCsv(infoRespuestaConsultaInfo.getDatosInformacionCompletaAAPP().getCSV());
					}
				}
			}				
		}
				
		return idXML;
	}

	/**
	 * 
	 * @param datosInformacionCompletaAAPP
	 * @return
	 */
	private boolean isPendiente(DatosInformacionCompletaAAPP datosInformacionCompletaAAPP) {
		boolean pendiente = false;
		if (datosInformacionCompletaAAPP != null) {
			// Si tenemos CSV comprobamos si tambien estan todas las fechas
			if(datosInformacionCompletaAAPP.getCSV()!=null && !datosInformacionCompletaAAPP.getCSV().equalsIgnoreCase("")){
				AdministracionCompleta[] administracions = datosInformacionCompletaAAPP.getAdministracionCompleta();
				if (administracions != null) {
					for (AdministracionCompleta administracion : administracions) {
						if (administracion.getFecha_Respuesta() == null || administracion.getFecha_Respuesta().trim().equals("")) {
							ClsLogging.writeFileLog("No encontrada Fecha Respuesta en uno de los nodos Administracion.", 10);
							pendiente = true;
							break;
						}
					}
				}
			}else{
				ClsLogging.writeFileLog("CSV no disponible", 10);
				pendiente = true;
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
			//Como hay que enviar el informe economico a la cajg de la comunidad de madrid, vamos a guardarnos los que solicita lacala(2003)
//			if(scsEejgPeticionesBean.getIdInstitucion()!=null && scsEejgPeticionesBean.getIdInstitucion().intValue()==2003)
			scsEejgXmlBean.setXml(xml);

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

			//Llamada al Stub del web service
			ClsLogging.writeFileLog("Llamada al servicio web obtenerDocumento", 10);
			ResultSolicitudDocumentoTO obtDoc = stub.obtenerDocumento(solDocTO);
			
			if(obtDoc!=null){
				if (obtDoc.getResultado()!=null && obtDoc.getResultado().toUpperCase().equals(AppConstants.PFD_SOLICITUD_DOCUMENTO_OK)){
					DocumentoTO documentoTO = obtDoc.getDocumento();
					contenidoPDF = documentoTO.getFirmab64().toString();
				
				} else if(obtDoc.getResultado().toUpperCase().equals(AppConstants.PFD_SOLICITUD_DOCUMENTO_KO)){
					ClsLogging.writeFileLog("El resultado de la obtencion del documento de la PFD es incorrecto", 10);
					throw new ClsExceptions("No se ha podido realizar la petición. Ha fallado la comunicación con la plataforma generadora de PDF");
				}
			}
			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al solicitar documento a la PFD", e, 3);
			throw new ClsExceptions(e, e.getLocalizedMessage());
		}			
		
		return contenidoPDF;
	}
	
}
