/**
 * 
 */
package com.siga.ws.i2055;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.transaction.UserTransaction;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.xmlbeans.XmlOptions;
import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;
import org.redabogacia.sigaservices.app.AppConstants.ESTADOS_EJG;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjgKey;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.caj.AsignaConsultaNumeracionService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.beans.ScsUnidadFamiliarEJGAdm;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.gratuita.service.EejgService;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.i2055.xmlbeans.Direccion;
import com.siga.ws.i2055.xmlbeans.SCalificacion;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.DtArchivos;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.DtIntervinientes;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.DtPretensionesDefender;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.DtPretensionesDefender.Procedimiento;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.DtSolicitante;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna.DtExpedientes.TurnadoAbogado;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;

/**
 * @author angelcpe
 *
 */
public class SIGAWSClient extends SIGAWSClientAbstract implements PCAJGConstantes {
	
	private static int TIPO_TERCERO_SOLICITANTE = 1;

	Map<String, List<Map<String, String>>> htCargaDtPersonas = new Hashtable<String, List<Map<String,String>>>();
	Map<String, List<Map<String, String>>> htCargaDtArchivo = new Hashtable<String, List<Map<String,String>>>();
	
	
	@Override
	public void execute() throws Exception {
		
		WSPamplonaAdm wsPamplonaAdm = new WSPamplonaAdm();
		Map<String, String> mapExp;
		
		
//		Registrar_SolicitudXML_Solicitud registrar_SolicitudXML_Solicitud = new Registrar_SolicitudXML_Solicitud();
					
		ServiceSoap_BindingStub stub = null;
		if (!isSimular()) {
			IntegracionSigaAsignaLocator locator = new IntegracionSigaAsignaLocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), "Envío y recepción webservice del colegio " + getIdInstitucion() + " de la remesa " + getIdRemesa()));
			stub = new ServiceSoap_BindingStub(new java.net.URL(getUrlWS()), locator);
		}
		
		List<Hashtable<String, String>> listDtExpedientes = wsPamplonaAdm.getDtExpedientes(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(wsPamplonaAdm.getDtPersonas(getIdInstitucion(), getIdRemesa()), htCargaDtPersonas);
		construyeHTxEJG(wsPamplonaAdm.getDtArchivo(getIdInstitucion(), getIdRemesa()), htCargaDtArchivo);
		
		String anio = null;
		String numejg = null;
		String numero = null;
		String idTipoEJG = null;
		
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		
		int correctos = 0;
		UserTransaction tx = getUsrBean().getTransaction();
					
		tx.begin();
		//elimino primero las posibles respuestas que ya tenga por si se ha relanzado
		cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
		cajgRespuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(getIdInstitucion(), getIdRemesa(), getUsrBean(), v_ws_2055_ejg);	
		tx.commit();
		
		for (int i = 0; i < listDtExpedientes.size(); i++) {
			
			Registrar_SolicitudResult respuesta = null;
			
			try {
				mapExp = listDtExpedientes.get(i);
				anio = mapExp.get(ANIO);
				numejg = mapExp.get(NUMEJG);
				numero = mapExp.get(NUMERO);
				idTipoEJG = mapExp.get(IDTIPOEJG);								
									
				escribeLogRemesa("Enviando información del expediente " + anio + "/" + numejg);
				SIGAAsignaDocument sigaAsignaDocument = getDtExpedientes(mapExp);
				SIGAServicesHelper.deleteEmptyNode(sigaAsignaDocument.getSIGAAsigna().getDomNode());
				
				//guardamos el xml que vamos a enviar
				saveXML(sigaAsignaDocument);
				
				if(validateXML_EJG(sigaAsignaDocument, anio, numejg, numero, idTipoEJG) && !isSimular() && stub != null){												
					String xml = sigaAsignaDocument.xmlText();
					SIGAAsigna sigaAsigna = (SIGAAsigna) AxisObjectSerializerDeserializer.deserializeAxisObject(xml, SIGAAsigna.class);
				
					respuesta = stub.registrar_Solicitud(sigaAsigna);					
					escribeLogRemesa("El expediente se ha enviado correctamente.");					
					
					if (respuesta == null) {
						escribeLogRemesa("No se ha obtenido respuesta para el expediente " + anio + "/" + numejg);						
						continue;
					}
										
					BigInteger idTipoError = respuesta.getIdTipoError();
					if (idTipoError != null && idTipoError.intValue() != 0) {//si tiene un error definido. TODO habrá que enviar un texto !!!
						String descripcionError = "Error tras el envío: [" + idTipoError.toString() + "]";
						if (respuesta.getDescripcionError() != null) {
							descripcionError += " " + respuesta.getDescripcionError(); 
						}
						escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION);
						continue;
					}
					
					if (!sigaAsigna.getDtExpedientes().getIDExpedienteSIGA().equals(respuesta.getIDExpedienteSIGA())) {
						escribeLogRemesa("El identificador SIGA no se corresponde con el enviado para el expediente " + anio + "/" + numejg);						
						continue;
					}
					//INSERTAR EN LA COLA PARA ENVÍO DE DOCUMENTACIÓN
					enviaDocumentacion(getUsrBean(), getIdInstitucion(), anio, numero, idTipoEJG);
										
					ScsEjgKey scsEjgKey = new ScsEjgKey();
					scsEjgKey.setIdinstitucion((short)getIdInstitucion());
					scsEjgKey.setAnio(Short.valueOf(anio));
					scsEjgKey.setNumero(Long.valueOf(numero));
					scsEjgKey.setIdtipoejg(Short.valueOf(idTipoEJG));
					
					AsignaConsultaNumeracionService asignaConsultaNumeracionService = (AsignaConsultaNumeracionService) BusinessManager.getInstance().getService(AsignaConsultaNumeracionService.class);
					asignaConsultaNumeracionService.insert(scsEjgKey);
					
					correctos++;
				}
			
			} catch (Exception e) {					
				if (e.getCause() instanceof ConnectException) {	
					String descripcionError = "Se ha producido un error de conexión con el WebService";
					escribeLogRemesa(descripcionError);					
					ClsLogging.writeFileLogError("Error de conexión al enviar el expediente", e, 3);
					escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
				} else {
					trataError(anio, numejg, numero, idTipoEJG, e);						
				}
			}
		}
		
		if (!isSimular() && correctos > 0) {				
			CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
			// Marcar como generada
			cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_GENERADA);				
			//MARCAMOS COMO ENVIADA
			if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_ENVIADA)) {
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
				cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(getUsrBean(), String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ESTADOS_EJG.REMITIDO_COMISION);
			}				
			escribeLogRemesa("Los envíos junto con sus respuestas han sido tratatados satisfactoriamente");
		}
				
	}



	private void saveXML(SIGAAsignaDocument sigaAsignaDocument) throws IOException {
		
		String keyPathFicheros = "cajg.directorioFisicoCAJG";		
		String keyPath2 = "cajg.directorioCAJGJava";				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPathFicheros) + p.returnProperty(keyPath2);
		pathFichero += File.separator + getIdInstitucion()  + File.separator + getIdRemesa() + File.separator + "xml";
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();
		
//		Map<String, String> mapa = new HashMap<String, String>();
//		mapa.put(sigaAsignaDocument.getSIGAAsigna().getDomNode().getNamespaceURI(), "");
//		xmlOptions.setSaveSuggestedPrefixes(mapa);
		
		
		File file = new File(pathFichero);
		file.mkdirs();
		file = new File(file, sigaAsignaDocument.getSIGAAsigna().getDtExpedientes().getIDExpedienteSIGA()+".xml");
		
		ClsLogging.writeFileLog("Guardando fichero de envío webservice del colegio " + getIdInstitucion(), 3);
		ClsLogging.writeFileLog("Ruta del fichero: " + file.getAbsolutePath(), 3);
		
		sigaAsignaDocument.save(file, xmlOptions);
		
	}



	private void trataError(String anio, String numejg, String numero, String idTipoEJG, Exception e) throws ClsExceptions, IOException {
		String descripcionError = "Error al enviar el expediente. ";
		if (e.getMessage().indexOf("Non nillable element") > -1) {
			String campo = e.getMessage().substring(e.getMessage().indexOf("'"), e.getMessage().lastIndexOf("'"));
			descripcionError += "El campo '" + campo + "' no puede estar vacío."; 
		} else {
			 descripcionError += e.getMessage();
		}
		
		escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
		escribeLogRemesa("Se ha producido un error al enviar el expediente " + anio + "/" + numejg);					
		ClsLogging.writeFileLogError(descripcionError, e, 3);		
	}



	/**
	 * 
	 * @return
	 */
	public static EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String logDescripcion) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, logDescripcion);		
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();		
		reqHandler.addHandler(logSIGAasignaHandler);
		respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
				
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
				
		return clientConfig;
	}

	/**
	 * 
	 * @param sigaAsigna
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private SIGAAsignaDocument getDtExpedientes(Map<String, String> map) throws Exception {
		SIGAAsignaDocument sigaAsignaDocument = SIGAAsignaDocument.Factory.newInstance();
		DtExpedientes dtExpedientes = sigaAsignaDocument.addNewSIGAAsigna().addNewDtExpedientes();
		
		BigInteger bi = SIGAServicesHelper.getBigInteger("identificador expediente SIGA", map.get(IDEXPEDIENTESIGA));
		if (bi != null)	dtExpedientes.setIDExpedienteSIGA(bi);
		bi = SIGAServicesHelper.getBigInteger("número expediente SIGA", map.get(NUMEROEXPEDIENTESIGA));
		if (bi != null) dtExpedientes.setNumeroExpedienteSIGA(bi);
		bi = SIGAServicesHelper.getBigInteger("año expediente SIGA", map.get(ANOEXPEDIENTESIGA));
		if (bi != null) dtExpedientes.setAnoExpedienteSIGA(bi);
		Integer in = SIGAServicesHelper.getInteger("colegio de abogados", map.get(IDORGANISMOCOLEGIOABOGADOS));
		if (in != null) dtExpedientes.setIDOrganismoColegioAbogados(in);
		Calendar date = SIGAServicesHelper.getCalendar(map.get(FECHAREGISTRO));
		if (date != null) dtExpedientes.setFechaRegistro(date);	
		String st = map.get(LUGARPRESENTACION);
		if (st != null) dtExpedientes.setLugarPresentacion(st);
		st = map.get(OTROSDATOSDEINTERES);
		if (st != null) dtExpedientes.setOtrosDatosDeInteres(st);
		date = SIGAServicesHelper.getCalendar(map.get(FECHAPRESENTACION));
		if (date != null) dtExpedientes.setFechaPresentacion(date);
		in = SIGAServicesHelper.getInteger("código de usuario", map.get(IDUSUARIOREGISTRO));
		if (in != null) dtExpedientes.setIDUsuarioRegistro(in);
		in = SIGAServicesHelper.getInteger("código de colegio", map.get(IDORGANISMOREGISTRA));
		if (in != null) dtExpedientes.setIDOrganismoRegistra(in);
		
		addDtPretensionesDefender(dtExpedientes.addNewDtPretensionesDefender(), map);
		
		addDtPersonas(dtExpedientes, getKey(map));
		addDtArchivos(dtExpedientes, getKey(map));
		addTurnadoAbogado(dtExpedientes.addNewTurnadoAbogado(), map);
		addCalificacionRegistro(dtExpedientes.addNewCalificacionRegistro(), map);
		return sigaAsignaDocument;
		
	}

	private void addCalificacionRegistro(SCalificacion calificacionRegistro, Map<String, String> map) throws Exception {
		String st = map.get(C_CALIFICACION);
		if (st != null && !st.trim().equals("")) {
			try {				
				calificacionRegistro.setCalificacion(com.siga.ws.i2055.xmlbeans.ECalificaciones.Enum.forString(st));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("El código del tipo de dictamen no es correcto.");
			}
		}
		com.siga.ws.i2055.xmlbeans.ArrayOfMotivosRechazo motivoRechazo = calificacionRegistro.addNewMotivoRechazo();
		
		st = map.get(C_MR_CODIGO);
		if (st != null && !st.trim().equals("")) {
			try {				
				motivoRechazo.setCodigo(com.siga.ws.i2055.xmlbeans.EMotivosDeNegacion.Enum.forString(st));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("El código del motivo del dictamen no es correcto.");
			}
		}
		
		motivoRechazo.setDescripcion(map.get(C_MR_DESCRIPCION));
		motivoRechazo.setParam1(map.get(C_MR_PARAM1));
		motivoRechazo.setParam2(map.get(C_MR_PARAM2));
		motivoRechazo.setParam3(map.get(C_MR_PARAM3));
		
		st = map.get(C_OBSERVACIONES);
		if (st != null && !st.trim().equals("")) {
			try {				
				calificacionRegistro.setObservaciones(com.siga.ws.i2055.xmlbeans.EObservaciones.Enum.forString(st));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("El campo observaciones del dictamen no es correcto.");
			}
		}
		calificacionRegistro.setFecha(SIGAServicesHelper.getCalendar(map.get(C_FECHA)));
		
	}



	private void addTurnadoAbogado(TurnadoAbogado turnadoAbogado,	Map<String, String> map) throws Exception {
		turnadoAbogado.setIdentificacion(map.get(TA_IDENTIFICACION));		
		turnadoAbogado.setFechaTurnado(SIGAServicesHelper.getCalendar(map.get(TA_FECHATURNADO)));
	}



	/**
	 * 
	 * @param st
	 * @return
	 */
	private Boolean getBoolean(String st) {
		Boolean b = null;
		if (st != null){
			if (st.trim().equals("1")) {
				b = true;
			} else {
				b = false;
			}			
		}
		return b;
	}
	
	
	/**
	 * 
	 * @param datos
	 * @param htCarga
	 */	
	private void construyeHTxEJG(List<Hashtable<String, String>> datos, Map<String, List<Map<String, String>>> htCarga) {
		if (datos != null) {
			for (int i = 0; i < datos.size(); i++) {
				Hashtable<String, String> ht = datos.get(i);
				String key = getKey(ht);
				
				List<Map<String, String>> list = htCarga.get(key);
				if (list != null) {
					list.add(ht);
				} else {
					list = new ArrayList<Map<String, String>>();
					list.add(ht);
					htCarga.put(key, list);
				}				
			}		
		}
	}
	
	/**
	 * 
	 * @param ht
	 * @return
	 */
	private String getKey(Map<String, String> ht) {
		if (isNull(ht.get(IDINSTITUCION)) || isNull(ht.get(ANIO)) || isNull(ht.get(NUMERO)) || isNull(ht.get(IDTIPOEJG))){
			throw new IllegalArgumentException("Los campos clave del EJG no pueden ser nulos");
		}
		String key = ht.get(IDINSTITUCION) + "##" + ht.get(ANIO) + "##" + ht.get(NUMERO) + "##" + ht.get(IDTIPOEJG);
				
		return key;
	}
	
	/**
	 * 
	 * @param st
	 * @return
	 */
	private boolean isNull(String st) {
		return st == null || st.trim().equals("");
	}

	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 */
	private void addDtArchivos(DtExpedientes dtExpedientes, String str) {
		
		List<Map<String, String>> list = htCargaDtArchivo.get(str);
		
		if (list != null && list.size() > 0) {					
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);				
				if (map.get(IDARCHIVO) != null && !map.get(IDARCHIVO).trim().equals("")) {
					DtArchivos dtArchivo = dtExpedientes.addNewDtArchivos();
					Integer in = SIGAServicesHelper.getInteger("identificador de archivo", map.get(IDARCHIVO));
					if (in != null) dtArchivo.setIDArchivo(in);
					String st = map.get(NOMBREARCHIVO);
					if (st != null) dtArchivo.setNombreArchivo(st);
					in = SIGAServicesHelper.getInteger("tipo de archivo", map.get(IDTIPOARCHIVO));
					if (in != null) dtArchivo.setIDTipoArchivo(in);
					Boolean b = getBoolean(map.get(PRINCIPAL));
					if (b != null) dtArchivo.setPrincipal(b);										
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 * @throws Exception
	 */
	private void addDtPersonas(DtExpedientes dtExpedientes, String str) throws Exception {
		
		List<Map<String, String>> list = htCargaDtPersonas.get(str);		
		if (list != null) {			
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);				
				
				Integer isSolicitante = SIGAServicesHelper.getInteger("es solicitante", map.get(IS_SOLICITANTE));
				
				if (isSolicitante != null && isSolicitante.intValue() == 1) {
					DtSolicitante datosPersona = dtExpedientes.addNewDtSolicitante();					
					datosPersona.setIDTipoTercero(TIPO_TERCERO_SOLICITANTE);
					Integer in = SIGAServicesHelper.getInteger("tipo persona", map.get(IDTIPOPERSONA));
					if (in != null) datosPersona.setIDTipoPersona(in);
					String st = map.get(NOMBRE);
					if (st != null) datosPersona.setNombre(st);
					st = map.get(APELLIDO1);
					if (st != null) datosPersona.setApellido1(st);
					st = map.get(APELLIDO2);
					if (st != null) datosPersona.setApellido2(st);
					in = SIGAServicesHelper.getInteger("tipo identificación", map.get(IDTIPOIDENTIFICACION));
					if (in != null) datosPersona.setIDTipoIdentificacion(in);
					st = map.get(NUMEROIDENTIFICACION);
					if (st != null) datosPersona.setNumeroIdentificacion(st);
					
					//DIRECCION
					Direccion dtDirecciones = datosPersona.addNewDtDirecciones();
					in = SIGAServicesHelper.getInteger("tipo de vía", map.get(DIR_IDTIPOVIA));
					if (in != null) dtDirecciones.setIDTipoVia(in);
					st = map.get(DIR_NOMBREVIA);
					if (st != null) dtDirecciones.setNombreVia(st);
					st = map.get(DIR_NUMERO);
					if (st != null) dtDirecciones.setNumero(st);
					st = map.get(DIR_PISO);
					if (st != null) dtDirecciones.setPiso(st);
					in = SIGAServicesHelper.getInteger("población", map.get(DIR_IDPOBLACION));
					if (in != null) dtDirecciones.setIDPoblacion(in);
					st = map.get(DIR_CODIGOPOSTAL);
					if (st != null) dtDirecciones.setCodigoPostal(st);
					st = map.get(DIR_TELEFONO1);
					if (st != null) dtDirecciones.setTelefono1(st);
					st = map.get(DIR_TELEFONO2);
					if (st != null)	dtDirecciones.setTelefono2(st);
					st = map.get(DIR_FAX);
					if (st != null) dtDirecciones.setFax(st);
					st = map.get(DIR_EMAIL);
					if (st != null) dtDirecciones.setEmail(st);
					in = SIGAServicesHelper.getInteger("país", map.get(DIR_IDPAIS));
					if (in != null) dtDirecciones.setIDPais(in);					
					
					
					in = SIGAServicesHelper.getInteger("código estado civil", map.get(IDESTADOCIVIL));
					if (in != null)	datosPersona.setIDEstadoCivil(in);
					in = SIGAServicesHelper.getInteger("sexo", map.get(SEXO));
					if (in != null) datosPersona.setSexo(in);					
					st = map.get(RAZONSOCIAL);
					if (st != null)	datosPersona.setRazonSocial(st);
					BigDecimal bd = SIGAServicesHelper.getBigDecimal("ingresos anuales", map.get(INGRESOSANUALES));
					if (bd != null) datosPersona.setIngresosAnuales(bd);
					Calendar cal = SIGAServicesHelper.getCalendar(map.get(FECHANACIMIENTO));
					if (cal != null) datosPersona.setFechaNacimiento(cal);
					st = map.get(OBSERVACIONES);
					if (st != null) datosPersona.setObservaciones(st);
					st = map.get(PROFESION);
					if (st != null) datosPersona.setProfesion(st);
					in = SIGAServicesHelper.getInteger("régimen económico matrimonial", map.get(IDREGIMENECONOMICOMATRIMONIAL));
					if (in != null) datosPersona.setIDRegimenEconomicoMatrimonial(in);
					in = SIGAServicesHelper.getInteger("otra prestación", map.get(IDOTRAPRESTACION));
					if (in != null) datosPersona.setIDOtraPrestacion(in);
					bd = SIGAServicesHelper.getBigDecimal("importe otra prestación", map.get(IMPORTEOTRAPRESTACION));
					if (bd != null) datosPersona.setImporteOtraPrestacion(bd);
					in = SIGAServicesHelper.getInteger("nacionalidad", map.get(IDNACIONALIDAD));
					if (in != null) datosPersona.setIDNacionalidad(in);
					addDtOtrosIngresosBienesPorPersona(datosPersona, map);
										
				} else {
					DtIntervinientes datosPersona = dtExpedientes.addNewDtIntervinientes();
					Integer in = SIGAServicesHelper.getInteger("tipo solicitante/contrario", map.get(IDTIPOTERCERO));
					
					if (in != null) datosPersona.setIDTipoTercero(in);
					in = SIGAServicesHelper.getInteger("tipo persona", map.get(IDTIPOPERSONA));
					if (in != null) datosPersona.setIDTipoPersona(in);
					String st = map.get(NOMBRE);
					if (st != null) datosPersona.setNombre(st);
					st = map.get(APELLIDO1);
					if (st != null) datosPersona.setApellido1(st);
					st = map.get(APELLIDO2);
					if (st != null) datosPersona.setApellido2(st);
					in = SIGAServicesHelper.getInteger("tipo identificación", map.get(IDTIPOIDENTIFICACION));
					if (in != null) datosPersona.setIDTipoIdentificacion(in);
					st = map.get(NUMEROIDENTIFICACION);
					if (st != null) datosPersona.setNumeroIdentificacion(st);
					
					//DIRECCION
					Direccion dtDirecciones = datosPersona.addNewDtDirecciones();
					in = SIGAServicesHelper.getInteger("tipo de vía", map.get(DIR_IDTIPOVIA));
					if (in != null) dtDirecciones.setIDTipoVia(in);
					st = map.get(DIR_NOMBREVIA);
					if (st != null) dtDirecciones.setNombreVia(st);
					st = map.get(DIR_NUMERO);
					if (st != null) dtDirecciones.setNumero(st);
					st = map.get(DIR_PISO);
					if (st != null) dtDirecciones.setPiso(st);
					in = SIGAServicesHelper.getInteger("población", map.get(DIR_IDPOBLACION));
					if (in != null) dtDirecciones.setIDPoblacion(in);
					st = map.get(DIR_CODIGOPOSTAL);
					if (st != null) dtDirecciones.setCodigoPostal(st);
					st = map.get(DIR_TELEFONO1);
					if (st != null) dtDirecciones.setTelefono1(st);
					st = map.get(DIR_TELEFONO2);
					if (st != null)	dtDirecciones.setTelefono2(st);
					st = map.get(DIR_FAX);
					if (st != null) dtDirecciones.setFax(st);
					st = map.get(DIR_EMAIL);
					if (st != null) dtDirecciones.setEmail(st);
					in = SIGAServicesHelper.getInteger("país", map.get(DIR_IDPAIS));
					if (in != null) dtDirecciones.setIDPais(in);					
					
					
					in = SIGAServicesHelper.getInteger("estado civil", map.get(IDESTADOCIVIL));
					if (in != null)	datosPersona.setIDEstadoCivil(in);
					in = SIGAServicesHelper.getInteger("sexo", map.get(SEXO));
					if (in != null) datosPersona.setSexo(in);
					st = map.get(RAZONSOCIAL);
					if (st != null)	datosPersona.setRazonSocial(st);
					BigDecimal bd = SIGAServicesHelper.getBigDecimal("ingresos anuales", map.get(INGRESOSANUALES));
					if (bd != null) datosPersona.setIngresosAnuales(bd);
					Calendar cal = SIGAServicesHelper.getCalendar(map.get(FECHANACIMIENTO));
					if (cal != null) datosPersona.setFechaNacimiento(cal);
					st = map.get(OBSERVACIONES);
					if (st != null) datosPersona.setObservaciones(st);
					st = map.get(PROFESION);
					if (st != null) datosPersona.setProfesion(st);
					in = SIGAServicesHelper.getInteger("régimen económico matrimonial", map.get(IDREGIMENECONOMICOMATRIMONIAL));
					if (in != null) datosPersona.setIDRegimenEconomicoMatrimonial(in);
					in = SIGAServicesHelper.getInteger("otra prestación", map.get(IDOTRAPRESTACION));
					if (in != null) datosPersona.setIDOtraPrestacion(in);
					bd = SIGAServicesHelper.getBigDecimal("importe otra prestación", map.get(IMPORTEOTRAPRESTACION));
					if (bd != null) datosPersona.setImporteOtraPrestacion(bd);
					in = SIGAServicesHelper.getInteger("nacionalidad", map.get(IDNACIONALIDAD));
					if (in != null) datosPersona.setIDNacionalidad(in);
					
					BigInteger idOtroIngresoBien = SIGAServicesHelper.getBigInteger("otro ingreso bien", map.get(IDOTROINGRESOBIEN));
					if (idOtroIngresoBien != null) {
						datosPersona.addNewDtOtrosIngresosBienesPorPersona().addIdOtroIngresoBien(idOtroIngresoBien);
					}
					
				}				
								
			}
			
		}
		
	}

	/**
	 * 
	 * @param dtPersona 
	 * @param map
	 * @return
	 */
	private void addDtOtrosIngresosBienesPorPersona(DtSolicitante dtPersona, Map<String, String> map) {			
		BigInteger idOtroIngresoBien = SIGAServicesHelper.getBigInteger("otro ingreso bien", map.get(IDOTROINGRESOBIEN));
		if (idOtroIngresoBien != null) {			
			dtPersona.addNewDtOtrosIngresosBienesPorPersona().setIdOtroIngresoBienArray((new BigInteger[]{idOtroIngresoBien}));
		}
		
	}


	/**
	 * 
	 * @param dtExpedientes 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private void addDtPretensionesDefender(DtPretensionesDefender dtPretensionesDefender, Map<String, String> map) throws Exception {
		
		Boolean b = getBoolean(map.get(PRE_PRECISAABOGADO));
		if (b != null) dtPretensionesDefender.setPrecisaAbogado(b);
		String st = map.get(PRE_CODIGOTIPOPROCEDIMIENTO);
		if (st != null && !st.trim().equals("")) {
			dtPretensionesDefender.setCodigoTipoProcedimiento(st);
		}
		
		st = map.get(PRE_OTROSASPECTOS);
		if (st != null) dtPretensionesDefender.setOtrosAspectos(st);
		Integer in = SIGAServicesHelper.getInteger("partido judicial", map.get(PRE_IDPARTIDOJUDICIAL));
		if (in != null) dtPretensionesDefender.setIDPartidoJudicial(in);
		in = SIGAServicesHelper.getInteger("situación proceso", map.get(PRE_IDSITUACIONPROCESO));
		if (in != null) dtPretensionesDefender.setIDSituacionProceso(in);
		
		st = map.get(PRE_NUMEROPROCEDIMIENTO);
		BigInteger bi = SIGAServicesHelper.getBigInteger("año del procedimiento", map.get(PRE_ANOPROCEDIMIENTO));
		if ((st != null && !st.trim().equals("")) || bi != null) {
			Procedimiento procedimiento =  dtPretensionesDefender.addNewProcedimiento();						
			procedimiento.setNumeroProcedimiento(st);
			procedimiento.setAnoProcedimiento(bi);			
		}		
		
		st = map.get(PRE_CODUNIDADFUNCIONAL);
		if (st != null) dtPretensionesDefender.setCodUnidadFuncional(st);
		in = SIGAServicesHelper.getInteger("orden jurisdiccional", map.get(PRE_IDORDENJURISDICCIONAL));
		if (in != null) dtPretensionesDefender.setIDOrdenJurisdiccional(in);				
		b = getBoolean(map.get(PRE_PRECISAPROCURADOR));
		if (b != null) dtPretensionesDefender.setPrecisaProcurador(b);
		in = SIGAServicesHelper.getInteger("código del turno", map.get(PRE_IDLISTATURNADOABOGADOS));
		if (in != null) dtPretensionesDefender.setIDListaTurnadoAbogados(in);
		BigInteger posInt = SIGAServicesHelper.getBigInteger("código del procedimiento", map.get(PRE_IDTARIFAABOGADOS));
		if (posInt != null) dtPretensionesDefender.setIDTarifaAbogados(posInt);
		posInt = SIGAServicesHelper.getBigInteger("tarifa de procuradores", map.get(PRE_IDTARIFAPROCURADORES));
		if (posInt != null) dtPretensionesDefender.setIDTarifaProcuradores(posInt);
		BigDecimal bd = SIGAServicesHelper.getBigDecimal("porcentaje tarifa abogado", map.get(PRE_PORCENTAJETARIFAABOGADO));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaAbogado(bd);
		bd = SIGAServicesHelper.getBigDecimal("porcentaje tarifa procurador", map.get(PRE_PORCENTAJETARIFAPROCURADOR));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaProcurador(bd);
		in = SIGAServicesHelper.getInteger("tipo de facturación", map.get(PRE_IDTIPOFACTURACION));
		if (in != null) dtPretensionesDefender.setIDTipoFacturacion(in);
		st = map.get(PRE_PRETENSIONDEFENDER);
		if (st != null) dtPretensionesDefender.setPretensionDefender(st);
		b = getBoolean(map.get(PRE_SAM));
		if (b != null) dtPretensionesDefender.setSAM(b);		
	}
	
	private void enviaDocumentacion(UsrBean usrBean, int idInstitucion, String anio,	String numero, String idTipoEJG) throws ClsExceptions {
		ScsEejgPeticionesAdm scsEejgPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put(ScsEejgPeticionesBean.C_IDINSTITUCION, idInstitucion);
		hash.put(ScsEejgPeticionesBean.C_ANIO, anio);
		hash.put(ScsEejgPeticionesBean.C_NUMERO, numero);
		hash.put(ScsEejgPeticionesBean.C_IDTIPOEJG, idTipoEJG);
		
		@SuppressWarnings("rawtypes")
		Vector vector = scsEejgPeticionesAdm.select(hash);
		
		if (vector != null && vector.size() > 0) {
			for (int i = 0; i < vector.size();i++) {
				ScsEejgPeticionesBean scsEejgPeticionesBean = (ScsEejgPeticionesBean)vector.get(i);
				int estado = scsEejgPeticionesBean.getEstado();
				if (estado == EEJG_ESTADO.PENDIENTE_INFO.getId() || estado == EEJG_ESTADO.FINALIZADO.getId()) {
					enviaPDF(scsEejgPeticionesBean, usrBean);
				}
			}
		}
		
	}
	
	private void enviaPDF(ScsEejgPeticionesBean scsEejgPeticionesBean, UsrBean usrBean) {
		try {
			BusinessManager bm = BusinessManager.getInstance();
			EejgService eEjgS = (EejgService)bm.getService(EejgService.class);
			
			ScsUnidadFamiliarEJGAdm scsUnidadFamiliarEJGAdm = new ScsUnidadFamiliarEJGAdm(usrBean);
			ScsUnidadFamiliarEJGBean unidadFamiliarVo = new ScsUnidadFamiliarEJGBean();
			unidadFamiliarVo.setIdInstitucion(scsEejgPeticionesBean.getIdInstitucion());
			unidadFamiliarVo.setAnio(scsEejgPeticionesBean.getAnio());
			unidadFamiliarVo.setIdTipoEJG(scsEejgPeticionesBean.getIdTipoEjg());
			unidadFamiliarVo.setNumero(scsEejgPeticionesBean.getNumero());
			unidadFamiliarVo.setIdPersona(scsEejgPeticionesBean.getIdPersona().intValue());
			
			@SuppressWarnings("unchecked")
			Vector<ScsUnidadFamiliarEJGBean> v = scsUnidadFamiliarEJGAdm.selectByPK(scsUnidadFamiliarEJGAdm.beanToHashTable(unidadFamiliarVo));
			
			if (v == null || v.size() != 1) {
				throw new BusinessException("No se ha encontrado el registro de la unidad familiar.");
			}
			unidadFamiliarVo = v.get(0);
			usrBean.setLocation(String.valueOf(scsEejgPeticionesBean.getIdInstitucion()));
			unidadFamiliarVo.setPeticionEejg(scsEejgPeticionesBean);
			
			//el proceso que genera el fichero recoge el dato de personaJGBean
			ScsPersonaJGBean scsPersonaJGBean = new ScsPersonaJGBean();
			scsPersonaJGBean.setIdPersona(unidadFamiliarVo.getIdPersona());
			unidadFamiliarVo.setPersonaJG(scsPersonaJGBean);
			
			Map<Integer, Map<String, String>> mapInformeEejg = eEjgS.getDatosInformeEejg(unidadFamiliarVo, usrBean);
			File fichero = eEjgS.getInformeEejg(mapInformeEejg, usrBean);

			scsEejgPeticionesBean.setRutaPDF(fichero.getAbsolutePath());
			
			EcomCola ecomCola = new EcomCola();
			ecomCola.setIdoperacion(OPERACION.ASIGNA_ENVIO_DOCUMENTO.getId());
			ecomCola.setIdinstitucion(Short.valueOf(scsEejgPeticionesBean.getIdInstitucion().toString()));
			EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
						
			if (ecomColaService.insert(ecomCola) != 1) {				
				throw new ClsExceptions("No se ha podido insertar en la cola de comunicaciones.");
			}
			scsEejgPeticionesBean.setIdEcomCola(ecomCola.getIdecomcola());
			
			ScsEejgPeticionesAdm scsEejgPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
			if (!scsEejgPeticionesAdm.update(scsEejgPeticionesBean)) {
				throw new ClsExceptions("No se ha podido actualizar scsEejgPeticionesBean.");
			}
			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Se ha producido un error al generar y enviar el pdf a Asigna", e, 3);
		}
	}

}