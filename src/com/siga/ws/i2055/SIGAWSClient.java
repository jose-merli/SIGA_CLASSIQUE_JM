/**
 * 
 */
package com.siga.ws.i2055;

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
import org.apache.axis.message.MessageElement;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.w3c.dom.Element;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.siga.Utilidades.LogHandler;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument;
import com.siga.ws.i2055.xmlbeans.DtArchivosDocument.DtArchivos;
import com.siga.ws.i2055.xmlbeans.DtDireccionesDocument.DtDirecciones;
import com.siga.ws.i2055.xmlbeans.DtExpedientesDocument.DtExpedientes;
import com.siga.ws.i2055.xmlbeans.DtOtrosIngresosBienesPorPersonaDocument.DtOtrosIngresosBienesPorPersona;
import com.siga.ws.i2055.xmlbeans.DtPersonasDocument.DtPersonas;
import com.siga.ws.i2055.xmlbeans.DtPretensionesDefenderDocument.DtPretensionesDefender;
import com.siga.ws.i2055.xmlbeans.RespuestaDocument.Respuesta;
import com.siga.ws.i2055.xmlbeans.SIGAAsignaDocument.SIGAAsigna;

/**
 * @author angelcpe
 *
 */
public class SIGAWSClient extends SIGAWSClientAbstract implements PCAJGConstantes {

	Map<String, List<Map<String, String>>> htCargaDtPersonas = new Hashtable<String, List<Map<String,String>>>();
	Map<String, List<Map<String, String>>> htCargaDtArchivo = new Hashtable<String, List<Map<String,String>>>();
	
	
	@Override
	public void execute() throws Exception {
		
		WSPamplonaAdm wsPamplonaAdm = new WSPamplonaAdm();
		Map<String, String> mapExp;
		
		Registrar_SolicitudXML_Solicitud registrar_SolicitudXML_Solicitud = new Registrar_SolicitudXML_Solicitud();
					
		WS_SIGA_ASIGNALocator locator = new WS_SIGA_ASIGNALocator(createClientConfig());

		WS_SIGA_ASIGNASoap_BindingStub stub = new WS_SIGA_ASIGNASoap_BindingStub(new java.net.URL(getUrlWS()), locator);
		
		List<Hashtable<String, String>> listDtExpedientes = wsPamplonaAdm.getDtExpedientes(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(wsPamplonaAdm.getDtPersonas(getIdInstitucion(), getIdRemesa()), htCargaDtPersonas);
		construyeHTxEJG(wsPamplonaAdm.getDtArchivo(getIdInstitucion(), getIdRemesa()), htCargaDtArchivo);
		
		String anio = null;
		String numejg = null;
		String numero = null;
		String idTipoEJG = null;
		
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		
		
		int correctos = 0;
		UserTransaction tx = getUsrBean().getTransaction();
		
		
		try {
			
			tx.begin();
			//elimino primero las posibles respuestas que ya tenga por si se ha relanzado
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			cajgRespuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(getIdInstitucion(), getIdRemesa(), getUsrBean());			
			
			for (int i = 0; i < listDtExpedientes.size(); i++) {

				Registrar_SolicitudResponseRegistrar_SolicitudResult registrar_SolicitudResponseRegistrar_SolicitudResult = null;				
				
				try {
					mapExp = listDtExpedientes.get(i);
					anio = mapExp.get(ANIO);
					numejg = mapExp.get(NUMEJG);
					numero = mapExp.get(NUMERO);
					idTipoEJG = mapExp.get(IDTIPOEJG);
					
					SIGAAsignaDocument sigaAsignaDocument = SIGAAsignaDocument.Factory.newInstance();
					SIGAAsigna sigaAsigna = sigaAsignaDocument.addNewSIGAAsigna();
					addDtExpedientes(sigaAsigna, mapExp);
										
					Element element = (Element)sigaAsigna.getDomNode().getOwnerDocument().getFirstChild();					
					MessageElement messageElement = new MessageElement((Element)element);					
					registrar_SolicitudXML_Solicitud.set_any(new MessageElement[]{messageElement});										
					
					
					if (!validateXML_EJG(sigaAsignaDocument, anio, numejg, numero, idTipoEJG)){
						continue;
					}
					
										
					escribeLogRemesa("Enviando información del expediente " + anio + "/" + numejg);
					registrar_SolicitudResponseRegistrar_SolicitudResult = stub.registrar_Solicitud(registrar_SolicitudXML_Solicitud);					
					escribeLogRemesa("El expediente se ha enviado correctamente.");					
					
					MessageElement[] messageElements = registrar_SolicitudResponseRegistrar_SolicitudResult.get_any();
					if (messageElements == null || messageElements.length != 1) {						
						escribeLogRemesa("No se ha obtenido respuesta para el expediente " + anio + "/" + numejg);						
						continue;
					}
					
					messageElement = messageElements[0];

								
					SIGAAsignaDocument sigaAsignaDocumentrespuesta = SIGAAsignaDocument.Factory.parse(messageElement);
					SIGAAsigna sigAsignaRespuesta = sigaAsignaDocumentrespuesta.getSIGAAsigna();
					
					if (sigAsignaRespuesta == null) {
						escribeLogRemesa("No se ha obtenido respuesta para el expediente " + anio + "/" + numejg);						
						continue;
					}
					
					Respuesta respuesta = sigAsignaRespuesta.getRespuesta();
					if (respuesta == null) {
						escribeLogRemesa("No se ha obtenido respuesta para el expediente " + anio + "/" + numejg);						
						continue;
					}
										
					BigInteger idTipoError = respuesta.getIdTipoError();
					if (idTipoError != null && idTipoError.intValue() != 0) {//si tiene un error definido. TODO habrá que enviar un texto !!!
						String descripcionError = idTipoError.toString();
						escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError);
						continue;
					}
					
					if (!sigaAsigna.getDtExpedientes().getIDExpedienteSIGA().equals(respuesta.getIDExpedienteSIGA())) {
						escribeLogRemesa("El identificador SIGA no se corresponde con el enviado para el expediente " + anio + "/" + numejg);						
						continue;
					}
					
					correctos++;
					
				} catch (Exception e) {
					if (e.getCause() instanceof ConnectException) {						
						escribeLogRemesa("Se ha producido un error de conexión con el WebService");					
						ClsLogging.writeFileLogError("Error de conexión al enviar el expediente", e, 3);
						throw e;
					} else {
						escribeLogRemesa("Se ha producido un error al enviar el expediente " + anio + "/" + numejg);					
						ClsLogging.writeFileLogError("Error al enviar el expediente", e, 3);
					}
				}
			}
			
			if (correctos > 0) {				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
				// Marcar como generada
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), Integer.valueOf("1"));				
				//MARCAMOS COMO ENVIADA
				if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), Integer.valueOf("2"))) {
					//cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(getUsrBean(), String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
				}				
				escribeLogRemesa("Los envíos junto con sus respuestas han sido tratatados satisfactoriamente");
			}
		}  finally {
			tx.commit();
		}				
	}
	
	private void escribeErrorExpediente2(CajgEJGRemesaAdm cajgEJGRemesaAdm, CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm, Map<String, String> mapExp, String descripcionError) throws IOException, ClsExceptions {
		
		String anio = mapExp.get(ANIO);
		String numejg = mapExp.get(NUMEJG);
		
		Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
		hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, getIdInstitucion());
		hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, mapExp.get(ANIO));
		hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, mapExp.get(NUMERO));
		hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, mapExp.get(IDTIPOEJG));
		
		hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, getIdInstitucion());
		hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
		
		Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
		if (vectorRemesa.size() == 0) {
			escribeLogRemesa("No se ha encontrado el EJG año/número = " + anio + "/" + numejg + " en la remesa " + getIdRemesa());							
		} else if (vectorRemesa.size() > 1) {
			escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anio + "/" + numejg + " en la remesa " + getIdRemesa());							
		} else {									
		
			CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);
			
			CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();
			cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
			cajgRespuestaEJGRemesaBean.setCodigo("-1");
			cajgRespuestaEJGRemesaBean.setDescripcion(descripcionError);
			cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
						
			cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);			
		}		
	}

	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig() {
		SimpleProvider clientConfig = new SimpleProvider();
		Handler logSIGAasignaHandler = (Handler) new LogHandler();		
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
	private void addDtExpedientes(SIGAAsigna sigaAsigna, Map<String, String> map) throws Exception {
		DtExpedientes dtExpedientes = sigaAsigna.addNewDtExpedientes();
		BigInteger bi = getBigInteger(map.get(IDEXPEDIENTESIGA));
		if (bi != null)	dtExpedientes.setIDExpedienteSIGA(bi);
		String st = map.get(NUMEROEXPEDIENTESIGA);
		if (st != null) dtExpedientes.setNumeroExpedienteSIGA(st);
		st = map.get(ANOEXPEDIENTESIGA);
		if (st != null) dtExpedientes.setAnoExpedienteSIGA(st);
		bi = getBigInteger(map.get(IDORGANISMOCOLEGIOABOGADOS));
		if (bi != null) dtExpedientes.setIDOrganismoColegioAbogados(bi);
		Calendar cal = getDate(map.get(FECHAREGISTRO));
		if (cal != null) dtExpedientes.setFechaRegistro(cal);	
		st = map.get(LUGARPRESENTACION);
		if (st != null) dtExpedientes.setLugarPresentacion(st);
		st = map.get(OTROSDATOSDEINTERES);
		if (st != null) dtExpedientes.setOtrosDatosDeInteres(st);
		cal = getDate(map.get(FECHAPRESENTACION));
		if (cal != null) dtExpedientes.setFechaPresentacion(cal);
		bi = getBigInteger(map.get(IDUSUARIOREGISTRO));
		if (bi != null) dtExpedientes.setIDUsuarioRegistro(bi);
		bi = getBigInteger(map.get(IDORGANISMOREGISTRA));
		if (bi != null) dtExpedientes.setIDOrganismoRegistra(bi);
		addDtPretensionesDefender(dtExpedientes, map);
		addDtPersonas(dtExpedientes, getKey(map));
		addDtArchivos(dtExpedientes, getKey(map));		
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
	 * @param st
	 * @return
	 * @throws Exception
	 */
	private Calendar getDate(String st) throws Exception {		
		
		Calendar cal = null;
		if (st != null && !st.trim().equals("")) {
			cal = Calendar.getInstance();
			cal.setTime(GstDate.convertirFecha(st));			
		}
		return cal;
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
	 * @param st
	 * @return
	 */
	private BigInteger getBigInteger(String st) {
		BigInteger bigInteger = null;
		if (st != null && !st.trim().equals("")) {
			bigInteger = new BigInteger(st);
		}
		return bigInteger;
	}

	
	/**
	 * 
	 * @param dtPersona 
	 * @param map
	 * @return
	 */
	 
	private void addDtDireccion(DtPersonas dtPersona, Map<String, String> map) {		
		DtDirecciones dtDirecciones = dtPersona.addNewDtDirecciones();
		BigInteger bi = getBigInteger(map.get(DIR_IDTIPOVIA));
		if (bi != null) dtDirecciones.setIDTipoVia(bi);
		String st = map.get(DIR_NOMBREVIA);
		if (st != null) dtDirecciones.setNombreVia(st);
		st = map.get(DIR_NUMERO);
		if (st != null) dtDirecciones.setNumero(st);
		st = map.get(DIR_PISO);
		if (st != null) dtDirecciones.setPiso(st);
		bi = getBigInteger(map.get(DIR_IDPOBLACION));
		if (bi != null) dtDirecciones.setIDPoblacion(bi);
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
		bi = getBigInteger(map.get(DIR_IDPAIS));
		if (bi != null) dtDirecciones.setIDPais(bi);			
		
	}

	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 */
	private void addDtArchivos(DtExpedientes dtExpedientes, String str) {
		
		List<Map<String, String>> list = htCargaDtArchivo.get(str);
		
		if (list != null) {		
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (map.get(IDARCHIVO) != null && !map.get(IDARCHIVO).trim().equals("")) {
					DtArchivos dtArchivo = dtExpedientes.addNewDtArchivos();
					BigInteger bi = getBigInteger(map.get(IDARCHIVO));
					if (bi != null) dtArchivo.setIDArchivo(bi);
					String st = map.get(NOMBREARCHIVO);
					if (st != null) dtArchivo.setNombreArchivo(st);
					bi = getBigInteger(map.get(IDTIPOARCHIVO));
					if (bi != null) dtArchivo.setIDTipoArchivo(bi);
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
				
				DtPersonas dtPersona = dtExpedientes.addNewDtPersonas();
				BigInteger bi = getBigInteger(map.get(IDTIPOTERCERO));
				if (bi != null) dtPersona.setIDTipoTercero(bi);
				bi = getBigInteger(map.get(IDTIPOPERSONA));
				if (bi != null) dtPersona.setIDTipoPersona(bi);
				String st = map.get(NOMBRE);
				if (st != null) dtPersona.setNombre(st);
				st = map.get(APELLIDO1);
				if (st != null) dtPersona.setApellido1(st);
				st = map.get(APELLIDO2);
				if (st != null) dtPersona.setApellido2(st);
				bi = getBigInteger(map.get(IDTIPOIDENTIFICACION));
				if (bi != null) dtPersona.setIDTipoIdentificacion(bi);
				st = map.get(NUMEROIDENTIFICACION);
				if (st != null) dtPersona.setNumeroIdentificacion(st);
				addDtDireccion(dtPersona, map);
				bi = getBigInteger(map.get(IDESTADOCIVIL));
				if (bi != null)	dtPersona.setIDEstadoCivil(bi);
				bi = getBigInteger(map.get(RAZONSOCIAL));
				if (bi != null)	dtPersona.setRazonSocial(bi);
				BigDecimal bd = getBigDecimal(map.get(INGRESOSANUALES));
				if (bd != null) dtPersona.setIngresosAnuales(bd);
				Calendar cal = getDate(map.get(FECHANACIMIENTO));
				if (cal != null) dtPersona.setFechaNacimiento(cal);
				st = map.get(OBSERVACIONES);
				if (st != null) dtPersona.setObservaciones(st);
				st = map.get(PROFESION);
				if (st != null) dtPersona.setProfesion(st);
				bi = getBigInteger(map.get(IDREGIMENECONOMICOMATRIMONIAL));
				if (bi != null) dtPersona.setIDRegimenEconomicoMatrimonial(bi);
				bi = getBigInteger(map.get(IDOTRAPRESTACION));
				if (bi != null) dtPersona.setIDOtraPrestacion(bi);
				bd = getBigDecimal(map.get(IMPORTEOTRAPRESTACION));
				if (bd != null) dtPersona.setImporteOtraPrestacion(bd);
				bi = getBigInteger(map.get(IDNACIONALIDAD));
				if (bi != null) dtPersona.setIDNacionalidad(bi);
				addDtOtrosIngresosBienesPorPersona(dtPersona, map);				
			}
		}
		
	}

	/**
	 * 
	 * @param dtPersona 
	 * @param map
	 * @return
	 */
	private void addDtOtrosIngresosBienesPorPersona(DtPersonas dtPersona, Map<String, String> map) {			
		BigInteger idOtroIngresoBien = getBigInteger(map.get(IDOTROINGRESOBIEN));
		if (idOtroIngresoBien != null) {
			DtOtrosIngresosBienesPorPersona dtOtrosIngresosBienesPorPersona = dtPersona.addNewDtOtrosIngresosBienesPorPersona();
			dtOtrosIngresosBienesPorPersona.setIdOtroIngresoBienArray(new BigInteger[]{idOtroIngresoBien});			
		}
	}

	/**
	 * 
	 * @param st
	 * @return
	 */
	private BigDecimal getBigDecimal(String st) {
		BigDecimal bigDecimal = null;
		if (st != null && !st.trim().equals("")) {
			bigDecimal = new BigDecimal(st);
		}
		return bigDecimal;
	}


	/**
	 * 
	 * @param dtExpedientes 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private void addDtPretensionesDefender(DtExpedientes dtExpedientes, Map<String, String> map) throws Exception {
		DtPretensionesDefender dtPretensionesDefender = dtExpedientes.addNewDtPretensionesDefender();
		Boolean b = getBoolean(map.get(PRE_PRECISAABOGADO));
		if (b != null) dtPretensionesDefender.setPrecisaAbogado(b);
		BigInteger bi = getBigInteger(map.get(PRE_IDTIPOPROCEDIMIENTO));
		if (bi != null) dtPretensionesDefender.setIDTipoProcedimiento(bi);
		String st = map.get(PRE_OTROSASPECTOS);
		if (st != null) dtPretensionesDefender.setOtrosAspectos(st);
		bi = getBigInteger(map.get(PRE_IDPARTIDOJUDICIAL));
		if (bi != null) dtPretensionesDefender.setIDPartidoJudicial(bi);
		bi = getBigInteger(map.get(PRE_IDSITUACIONPROCESO));
		if (bi != null) dtPretensionesDefender.setIDSituacionProceso(bi);
		st = map.get(PRE_NUMEROPROCEDIMIENTO);
		if (st != null) dtPretensionesDefender.setNumeroProcedimiento(st);
		st = map.get(PRE_ANOPROCEDIMIENTO);
		if (st != null) dtPretensionesDefender.setAnoProcedimiento(st);
		bi = getBigInteger(map.get(PRE_IDORGANOJUDICIAL));
		if (bi != null) dtPretensionesDefender.setIDOrganoJudicial(bi);
		b = getBoolean(map.get(PRE_PRECISAPROCURADOR));
		if (b != null) dtPretensionesDefender.setPrecisaProcurador(b);
		bi = getBigInteger(map.get(PRE_IDLISTATURNADOABOGADOS));
		if (bi != null) dtPretensionesDefender.setIDListaTurnadoAbogados(bi);
		bi = getBigInteger(map.get(PRE_IDTARIFAABOGADOS));
		if (bi != null) dtPretensionesDefender.setIDTarifaAbogados(bi);
		bi = getBigInteger(map.get(PRE_IDTARIFAPROCURADORES));
		if (bi != null) dtPretensionesDefender.setIDTarifaProcuradores(bi);
		BigDecimal bd = getBigDecimal(map.get(PRE_PORCENTAJETARIFAABOGADO));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaAbogado(bd);
		bd = getBigDecimal(map.get(PRE_PORCENTAJETARIFAPROCURADOR));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaProcurador(bd);
		bi = getBigInteger(map.get(PRE_IDTIPOFACTURACION));
		if (bi != null) dtPretensionesDefender.setIDTipoFacturacion(bi);
		st = map.get(PRE_PRETENSIONDEFENDER);
		if (st != null) dtPretensionesDefender.setPretensionDefender(st);
		b = getBoolean(map.get(PRE_SAM));
		if (b != null) dtPretensionesDefender.setSAM(b);		
	}


}
