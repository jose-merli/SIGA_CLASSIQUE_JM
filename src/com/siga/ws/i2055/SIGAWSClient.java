/**
 * 
 */
package com.siga.ws.i2055;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.axis.types.PositiveInteger;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.siga.Utilidades.LogHandler;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.ws.SIGAWSClientAbstract;

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
		
		
//		Registrar_SolicitudXML_Solicitud registrar_SolicitudXML_Solicitud = new Registrar_SolicitudXML_Solicitud();
					
		ServiceLocator locator = new ServiceLocator(createClientConfig());

		ServiceSoap_BindingStub stub = new ServiceSoap_BindingStub(new java.net.URL(getUrlWS()), locator);
		
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
		
		try {			
			tx.begin();
			//elimino primero las posibles respuestas que ya tenga por si se ha relanzado
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			cajgRespuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(getIdInstitucion(), getIdRemesa(), getUsrBean(), "V_WS_2055_EJG");	
				
			
			for (int i = 0; i < listDtExpedientes.size(); i++) {

				Registrar_SolicitudResult respuesta = null;				
				
				try {
					mapExp = listDtExpedientes.get(i);
					anio = mapExp.get(ANIO);
					numejg = mapExp.get(NUMEJG);
					numero = mapExp.get(NUMERO);
					idTipoEJG = mapExp.get(IDTIPOEJG);								
										
					escribeLogRemesa("Enviando informaci�n del expediente " + anio + "/" + numejg);
					SIGAAsigna sigaAsigna = getDtExpedientes(mapExp);					
					respuesta = stub.registrar_Solicitud(sigaAsigna);					
					escribeLogRemesa("El expediente se ha enviado correctamente.");					
					
					if (respuesta == null) {
						escribeLogRemesa("No se ha obtenido respuesta para el expediente " + anio + "/" + numejg);						
						continue;
					}
										
					BigInteger idTipoError = respuesta.getIdTipoError();
					if (idTipoError != null && idTipoError.intValue() != 0) {//si tiene un error definido. TODO habr� que enviar un texto !!!
						String descripcionError = "Error tras el env�o: [" + idTipoError.toString() + "]";
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
					
					correctos++;
					
				} catch (Exception e) {
					if (e.getCause() instanceof ConnectException) {						
						escribeLogRemesa("Se ha producido un error de conexi�n con el WebService");					
						ClsLogging.writeFileLogError("Error de conexi�n al enviar el expediente", e, 3);
						throw e;
					} else {
						String descripcionError = "Error al enviar el expediente. " + e.getMessage();
						escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
						escribeLogRemesa("Se ha producido un error al enviar el expediente " + anio + "/" + numejg);					
						ClsLogging.writeFileLogError(descripcionError, e, 3);
					}
				}
			}
			
			if (correctos > 0) {				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
				// Marcar como generada
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_GENERADA);				
				//MARCAMOS COMO ENVIADA
				if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_ENVIADA)) {
					//cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(getUsrBean(), String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
				}				
				escribeLogRemesa("Los env�os junto con sus respuestas han sido tratatados satisfactoriamente");
			}
		}  finally {			
			tx.commit();
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
	private SIGAAsigna getDtExpedientes(Map<String, String> map) throws Exception {
		SIGAAsigna sigAsigna = new SIGAAsigna();
		SIGAAsignaDtExpedientes dtExpedientes = new SIGAAsignaDtExpedientes();
		BigInteger bi = getBigInteger(map.get(IDEXPEDIENTESIGA));
		if (bi != null)	dtExpedientes.setIDExpedienteSIGA(bi);
		bi = getBigInteger(map.get(NUMEROEXPEDIENTESIGA));
		if (bi != null) dtExpedientes.setNumeroExpedienteSIGA(bi);
		bi = getBigInteger(map.get(ANOEXPEDIENTESIGA));
		if (bi != null) dtExpedientes.setAnoExpedienteSIGA(bi);
		PositiveInteger in = getInteger(map.get(IDORGANISMOCOLEGIOABOGADOS));
		if (in != null) dtExpedientes.setIDOrganismoColegioAbogados(in);
		Date date = getDate(map.get(FECHAREGISTRO));
		if (date != null) dtExpedientes.setFechaRegistro(date);	
		String st = map.get(LUGARPRESENTACION);
		if (st != null) dtExpedientes.setLugarPresentacion(st);
		st = map.get(OTROSDATOSDEINTERES);
		if (st != null) dtExpedientes.setOtrosDatosDeInteres(st);
		date = getDate(map.get(FECHAPRESENTACION));
		if (date != null) dtExpedientes.setFechaPresentacion(date);
		in = getInteger(map.get(IDUSUARIOREGISTRO));
		if (in != null) dtExpedientes.setIDUsuarioRegistro(in);
		in = getInteger(map.get(IDORGANISMOREGISTRA));
		if (in != null) dtExpedientes.setIDOrganismoRegistra(in);
		dtExpedientes.setDtPretensionesDefender(addDtPretensionesDefender(map));
		
		addDtPersonas(dtExpedientes, getKey(map));
		addDtArchivos(dtExpedientes, getKey(map));		
		sigAsigna.setDtExpedientes(dtExpedientes);
		return sigAsigna;
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
	private Date getDate(String st) throws Exception {		
		
		Date date = null;
		if (st != null && !st.trim().equals("")) {
			date = GstDate.convertirFecha(st);			
		}
		return date;
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
	
	private PositiveInteger getInteger(String st) {		
		PositiveInteger in = null;
		if (st != null && !st.trim().equals("")) {
			in = new PositiveInteger(st);
		}
		return in;
	}
	
	private PositiveInteger getPositiveInteger(String st) {
		PositiveInteger posInt = null;
		if (st != null && !st.trim().equals("")) {
			posInt = new PositiveInteger(st);
		}
		return posInt;
	}

	
	/**
	 * 
	 * @param dtPersona 
	 * @param map
	 * @return
	 */
	 
	private void addDtDireccion(SIGAAsignaDtExpedientesDtPersonas dtPersona, Map<String, String> map) {		
		SIGAAsignaDtExpedientesDtPersonasDtDirecciones dtDirecciones = new SIGAAsignaDtExpedientesDtPersonasDtDirecciones();
		PositiveInteger in = getInteger(map.get(DIR_IDTIPOVIA));
		if (in != null) dtDirecciones.setIDTipoVia(in);
		String st = map.get(DIR_NOMBREVIA);
		if (st != null) dtDirecciones.setNombreVia(st);
		st = map.get(DIR_NUMERO);
		if (st != null) dtDirecciones.setNumero(st);
		st = map.get(DIR_PISO);
		if (st != null) dtDirecciones.setPiso(st);
		in = getInteger(map.get(DIR_IDPOBLACION));
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
		in = getInteger(map.get(DIR_IDPAIS));
		if (in != null) dtDirecciones.setIDPais(in);
		
		if (dtDirecciones.getIDPoblacion() != null) {
			dtPersona.setDtDirecciones(dtDirecciones);
		}
	}

	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 */
	private void addDtArchivos(SIGAAsignaDtExpedientes dtExpedientes, String str) {
		
		List<Map<String, String>> list = htCargaDtArchivo.get(str);
		
		if (list != null && list.size() > 0) {		
			SIGAAsignaDtExpedientesDtArchivos[] dtArchivos = new SIGAAsignaDtExpedientesDtArchivos[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (map.get(IDARCHIVO) != null && !map.get(IDARCHIVO).trim().equals("")) {
					SIGAAsignaDtExpedientesDtArchivos dtArchivo = new SIGAAsignaDtExpedientesDtArchivos();
					PositiveInteger in = getInteger(map.get(IDARCHIVO));
					if (in != null) dtArchivo.setIDArchivo(in);
					String st = map.get(NOMBREARCHIVO);
					if (st != null) dtArchivo.setNombreArchivo(st);
					in = getInteger(map.get(IDTIPOARCHIVO));
					if (in != null) dtArchivo.setIDTipoArchivo(in);
					Boolean b = getBoolean(map.get(PRINCIPAL));
					if (b != null) dtArchivo.setPrincipal(b);
					dtArchivos[i] = dtArchivo;					
				}
			}
			dtExpedientes.setDtArchivos(dtArchivos);
		}
	}
	
	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 * @throws Exception
	 */
	private void addDtPersonas(SIGAAsignaDtExpedientes dtExpedientes, String str) throws Exception {
		
		List<Map<String, String>> list = htCargaDtPersonas.get(str);		
		if (list != null) {			
			SIGAAsignaDtExpedientesDtPersonas dtPersonas[] = new SIGAAsignaDtExpedientesDtPersonas[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				
				SIGAAsignaDtExpedientesDtPersonas dtPersona = new SIGAAsignaDtExpedientesDtPersonas();
				PositiveInteger in = getInteger(map.get(IDTIPOTERCERO));
				if (in != null) dtPersona.setIDTipoTercero(in);
				in = getInteger(map.get(IDTIPOPERSONA));
				if (in != null) dtPersona.setIDTipoPersona(in);
				String st = map.get(NOMBRE);
				if (st != null) dtPersona.setNombre(st);
				st = map.get(APELLIDO1);
				if (st != null) dtPersona.setApellido1(st);
				st = map.get(APELLIDO2);
				if (st != null) dtPersona.setApellido2(st);
				in = getInteger(map.get(IDTIPOIDENTIFICACION));
				if (in != null) dtPersona.setIDTipoIdentificacion(in);
				st = map.get(NUMEROIDENTIFICACION);
				if (st != null) dtPersona.setNumeroIdentificacion(st);
				addDtDireccion(dtPersona, map);
				in = getInteger(map.get(IDESTADOCIVIL));
				if (in != null)	dtPersona.setIDEstadoCivil(in);
				BigInteger bi = getBigInteger(map.get(RAZONSOCIAL));
				if (bi != null)	dtPersona.setRazonSocial(bi);
				BigDecimal bd = getBigDecimal(map.get(INGRESOSANUALES));
				if (bd != null) dtPersona.setIngresosAnuales(bd);
				Date cal = getDate(map.get(FECHANACIMIENTO));
				if (cal != null) dtPersona.setFechaNacimiento(cal);
				st = map.get(OBSERVACIONES);
				if (st != null) dtPersona.setObservaciones(st);
				st = map.get(PROFESION);
				if (st != null) dtPersona.setProfesion(st);
				in = getInteger(map.get(IDREGIMENECONOMICOMATRIMONIAL));
				if (in != null) dtPersona.setIDRegimenEconomicoMatrimonial(in);
				in = getInteger(map.get(IDOTRAPRESTACION));
				if (in != null) dtPersona.setIDOtraPrestacion(in);
				bd = getBigDecimal(map.get(IMPORTEOTRAPRESTACION));
				if (bd != null) dtPersona.setImporteOtraPrestacion(bd);
				in = getInteger(map.get(IDNACIONALIDAD));
				if (in != null) dtPersona.setIDNacionalidad(in);
				addDtOtrosIngresosBienesPorPersona(dtPersona, map);
				dtPersonas[i] = dtPersona;				
			}
			dtExpedientes.setDtPersonas(dtPersonas);
		}
		
	}

	/**
	 * 
	 * @param dtPersona 
	 * @param map
	 * @return
	 */
	private void addDtOtrosIngresosBienesPorPersona(SIGAAsignaDtExpedientesDtPersonas dtPersona, Map<String, String> map) {			
		BigInteger idOtroIngresoBien = getBigInteger(map.get(IDOTROINGRESOBIEN));
		if (idOtroIngresoBien != null) {
			dtPersona.setDtOtrosIngresosBienesPorPersona(new BigInteger[]{idOtroIngresoBien});
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
	private SIGAAsignaDtExpedientesDtPretensionesDefender addDtPretensionesDefender(Map<String, String> map) throws Exception {
		
		SIGAAsignaDtExpedientesDtPretensionesDefender dtPretensionesDefender = new SIGAAsignaDtExpedientesDtPretensionesDefender();
		Boolean b = getBoolean(map.get(PRE_PRECISAABOGADO));
		if (b != null) dtPretensionesDefender.setPrecisaAbogado(b);
		PositiveInteger in = getInteger(map.get(PRE_IDTIPOPROCEDIMIENTO));
		if (in != null) dtPretensionesDefender.setIDTipoProcedimiento(in);
		String st = map.get(PRE_OTROSASPECTOS);
		if (st != null) dtPretensionesDefender.setOtrosAspectos(st);
		in = getInteger(map.get(PRE_IDPARTIDOJUDICIAL));
		if (in != null) dtPretensionesDefender.setIDPartidoJudicial(in);
		in = getInteger(map.get(PRE_IDSITUACIONPROCESO));
		if (in != null) dtPretensionesDefender.setIDSituacionProceso(in);
		
		st = map.get(PRE_NUMEROPROCEDIMIENTO);
		BigInteger bi = getBigInteger(map.get(PRE_ANOPROCEDIMIENTO));
		if ((st != null && !st.trim().equals("")) || bi != null) {
			SIGAAsignaDtExpedientesDtPretensionesDefenderProcedimiento procedimiento = new SIGAAsignaDtExpedientesDtPretensionesDefenderProcedimiento();			
			procedimiento.setNumeroProcedimiento(st);
			procedimiento.setAnoProcedimiento(bi);
			dtPretensionesDefender.setProcedimiento(procedimiento);
		}		
		
		in = getInteger(map.get(PRE_IDORGANOJUDICIAL));
		if (in != null) dtPretensionesDefender.setIDOrganoJudicial(in);
		b = getBoolean(map.get(PRE_PRECISAPROCURADOR));
		if (b != null) dtPretensionesDefender.setPrecisaProcurador(b);
		in = getInteger(map.get(PRE_IDLISTATURNADOABOGADOS));
		if (in != null) dtPretensionesDefender.setIDListaTurnadoAbogados(in);
		PositiveInteger posInt = getPositiveInteger(map.get(PRE_IDTARIFAABOGADOS));
		if (posInt != null) dtPretensionesDefender.setIDTarifaAbogados(posInt);
		posInt = getPositiveInteger(map.get(PRE_IDTARIFAPROCURADORES));
		if (posInt != null) dtPretensionesDefender.setIDTarifaProcuradores(posInt);
		BigDecimal bd = getBigDecimal(map.get(PRE_PORCENTAJETARIFAABOGADO));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaAbogado(bd);
		bd = getBigDecimal(map.get(PRE_PORCENTAJETARIFAPROCURADOR));
		if (bd != null) dtPretensionesDefender.setPorcentajeTarifaProcurador(bd);
		in = getInteger(map.get(PRE_IDTIPOFACTURACION));
		if (in != null) dtPretensionesDefender.setIDTipoFacturacion(in);
		st = map.get(PRE_PRETENSIONDEFENDER);
		if (st != null) dtPretensionesDefender.setPretensionDefender(st);
		b = getBoolean(map.get(PRE_SAM));
		if (b != null) dtPretensionesDefender.setSAM(b);
		return dtPretensionesDefender;
	}


}
