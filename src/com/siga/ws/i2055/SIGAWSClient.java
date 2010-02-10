/**
 * 
 */
package com.siga.ws.i2055;

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

import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SigaWSHelper;
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
					
		WS_SIGA_ASIGNALocator locator = new WS_SIGA_ASIGNALocator();

		WS_SIGA_ASIGNASoap_BindingStub stub = new WS_SIGA_ASIGNASoap_BindingStub(new java.net.URL(getUrlWS()), locator);
		
		List<Hashtable<String, String>> listDtExpedientes = wsPamplonaAdm.getDtExpedientes(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(wsPamplonaAdm.getDtPersonas(getIdInstitucion(), getIdRemesa()), htCargaDtPersonas);
		construyeHTxEJG(wsPamplonaAdm.getDtArchivo(getIdInstitucion(), getIdRemesa()), htCargaDtArchivo);
		
		String anio = null;
		String numejg = null;
		
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		int idRespuesta = cajgRespuestaEJGRemesaAdm.getNextVal();
		
		try {
		
			for (int i = 0; i < listDtExpedientes.size(); i++) {

				Registrar_SolicitudResponseRegistrar_SolicitudResult registrar_SolicitudResponseRegistrar_SolicitudResult = null;				
				
				try {
					mapExp = listDtExpedientes.get(i);
					anio = mapExp.get(ANIO);
					numejg = mapExp.get(NUMEJG);
					
					SIGAAsignaDocument sigaAsignaDocument = SIGAAsignaDocument.Factory.newInstance();
					SIGAAsigna sigaAsigna = sigaAsignaDocument.addNewSIGAAsigna();
					addDtExpedientes(sigaAsigna, mapExp);					
										
					Element element = (Element)sigaAsigna.getDomNode().getOwnerDocument().getFirstChild();
					
					MessageElement messageElement = new MessageElement((Element)element);
					
					registrar_SolicitudXML_Solicitud.set_any(new MessageElement[]{messageElement});										
						
					escribeLogRemesa("Enviando informaci�n del expediente " + anio + "/" + numejg);	
					SigaWSHelper.deleteEmptyNode(messageElement);
										
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
					if (!sigaAsigna.getDtExpedientes().getIDExpedienteSIGA().equals(respuesta.getIDExpedienteSIGA())) {
						escribeLogRemesa("El identificador SIGA no se corresponde con el enviado para el expediente " + anio + "/" + numejg);						
						continue;
					}
					
					BigInteger idTipoError = respuesta.getIdTipoError();
					if (idTipoError != null) {//si tiene un error definido. TODO habr� que enviar un texto !!!
						String descripcion = idTipoError.toString();
						Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
						hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, getIdInstitucion());
						hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, anio);
						hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, mapExp.get(NUMERO));
						hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, mapExp.get(IDTIPOEJG));
						
						hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, getIdInstitucion());
						hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
						
						Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
						if (vectorRemesa.size() == 0) {
							escribeLogRemesa("No se ha encontrado el EJG a�o/n�mero = " + anio + "/" + numejg + " en la remesa " + getIdRemesa());							
						} else if (vectorRemesa.size() > 1) {
							escribeLogRemesa("Se ha encontrado m�s de un EJG a�o/n�mero = " + anio + "/" + numejg + " en la remesa " + getIdRemesa());							
						} else {									
						
							CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);
							
							CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();
							cajgRespuestaEJGRemesaBean.setIdRespuesta(idRespuesta);
							cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
							cajgRespuestaEJGRemesaBean.setCodigo("-1");
							cajgRespuestaEJGRemesaBean.setDescripcion(descripcion);
							cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
							idRespuesta++;
							
							cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
						}
					}
					
				} catch (Exception e) {
					if (e.getCause() instanceof ConnectException) {						
						escribeLogRemesa("Se ha producido un error de conexi�n con el WebService");					
						ClsLogging.writeFileLogError("Error de conexi�n al enviar el expediente", e, 3);
						throw e;
					} else {
						escribeLogRemesa("Se ha producido un error al enviar el expediente " + anio + "/" + numejg);					
						ClsLogging.writeFileLogError("Error al enviar el expediente", e, 3);
					}
				}
			}
			
			UserTransaction tx = getUsrBean().getTransaction();
			CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
						
			tx.begin();
			// Marcar como generada
			cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), Integer.valueOf("1"));
			
			//MARCAMOS COMO ENVIADA
			if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), Integer.valueOf("2"))) {
				//cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(getUsrBean(), String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
			}
			tx.commit();
			escribeLogRemesa("Los env�os junto con sus respuestas han sido tratatados satisfactoriamente");
		}  finally {
			
		}				
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
		dtExpedientes.setIDExpedienteSIGA(getBigInteger(map.get(IDEXPEDIENTESIGA)));		
		dtExpedientes.setNumeroExpedienteSIGA(map.get(NUMEROEXPEDIENTESIGA));
		dtExpedientes.setAnoExpedienteSIGA(map.get(ANOEXPEDIENTESIGA));		
		dtExpedientes.setIDOrganismoColegioAbogados(getBigInteger(map.get(IDORGANISMOCOLEGIOABOGADOS)));		
		dtExpedientes.setFechaRegistro(getDate(map.get(FECHAREGISTRO)));		
		dtExpedientes.setLugarPresentacion(map.get(LUGARPRESENTACION));
		dtExpedientes.setOtrosDatosDeInteres(map.get(OTROSDATOSDEINTERES));		
		dtExpedientes.setFechaPresentacion(getDate(map.get(FECHAPRESENTACION)));
		dtExpedientes.setIDUsuarioRegistro(getBigInteger(map.get(IDUSUARIOREGISTRO)));
		dtExpedientes.setIDOrganismoRegistra(getBigInteger(map.get(IDORGANISMOREGISTRA)));
		addDtPretensionesDefender(dtExpedientes, map);
		addDtPersonas(dtExpedientes, getKey(map));
		addDtArchivos(dtExpedientes, getKey(map));		
	}

	/**
	 * 
	 * @param st
	 * @return
	 */
	private boolean getBoolean(String st) {
		boolean b = false;
		if (st != null && st.trim().equals("1")) {
			b = true;
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
		dtDirecciones.setIDTipoVia(getBigInteger(map.get(DIR_IDTIPOVIA)));
		dtDirecciones.setNombreVia(map.get(DIR_NOMBREVIA));
		dtDirecciones.setNumero(map.get(DIR_NUMERO));
		dtDirecciones.setPiso(map.get(DIR_PISO));
		dtDirecciones.setIDPoblacion(getBigInteger(map.get(DIR_IDPOBLACION)));
		dtDirecciones.setCodigoPostal(map.get(DIR_CODIGOPOSTAL));
		dtDirecciones.setTelefono1(map.get(DIR_TELEFONO1));
		dtDirecciones.setTelefono2(map.get(DIR_TELEFONO2));
		dtDirecciones.setFax(map.get(DIR_FAX));
		dtDirecciones.setEmail(map.get(DIR_EMAIL));
		dtDirecciones.setIDPais(getBigInteger(map.get(DIR_IDPAIS)));			
		
	}

	
	/**
	 * 
	 * @param dtExpedientes 
	 * @param st
	 * @return
	 */
	private void addDtArchivos(DtExpedientes dtExpedientes, String st) {
		
		List<Map<String, String>> list = htCargaDtArchivo.get(st);
		
		if (list != null) {		
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				if (map.get(IDARCHIVO) != null && !map.get(IDARCHIVO).trim().equals("")) {
					DtArchivos dtArchivo = dtExpedientes.addNewDtArchivos();
					dtArchivo.setIDArchivo(getBigInteger(map.get(IDARCHIVO)));
					dtArchivo.setNombreArchivo(map.get(NOMBREARCHIVO));
					dtArchivo.setIDTipoArchivo(getBigInteger(map.get(IDTIPOARCHIVO)));
					dtArchivo.setPrincipal(getBoolean(map.get(PRINCIPAL)));
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
	private void addDtPersonas(DtExpedientes dtExpedientes, String st) throws Exception {
		
		List<Map<String, String>> list = htCargaDtPersonas.get(st);		
		if (list != null) {			
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				
				DtPersonas dtPersona = dtExpedientes.addNewDtPersonas();
				dtPersona.setIDTipoTercero(getBigInteger(map.get(IDTIPOTERCERO)));
				dtPersona.setIDTipoPersona(getBigInteger(map.get(IDTIPOPERSONA)));	
				dtPersona.setNombre(map.get(NOMBRE));
				dtPersona.setApellido1(map.get(APELLIDO1));
				dtPersona.setApellido2(map.get(APELLIDO2));
				dtPersona.setIDTipoIdentificacion(getBigInteger(map.get(IDTIPOIDENTIFICACION)));
				dtPersona.setNumeroIdentificacion(map.get(NUMEROIDENTIFICACION));
				addDtDireccion(dtPersona, map);
				dtPersona.setIDEstadoCivil(getBigInteger(map.get(IDESTADOCIVIL)));
				dtPersona.setRazonSocial(getBigInteger(map.get(RAZONSOCIAL)));
				dtPersona.setIngresosAnuales(getBigDecimal(map.get(INGRESOSANUALES)));
				dtPersona.setFechaNacimiento(getDate(map.get(FECHANACIMIENTO)));
				dtPersona.setObservaciones(map.get(OBSERVACIONES));
				dtPersona.setProfesion(map.get(PROFESION));
				dtPersona.setIDRegimenEconomicoMatrimonial(getBigInteger(map.get(IDREGIMENECONOMICOMATRIMONIAL)));				
				dtPersona.setIDOtraPrestacion(getBigInteger(map.get(IDOTRAPRESTACION)));
				dtPersona.setImporteOtraPrestacion(getBigDecimal(map.get(IMPORTEOTRAPRESTACION)));
				dtPersona.setIDNacionalidad(getBigInteger(map.get(IDNACIONALIDAD)));
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
		dtPretensionesDefender.setPrecisaAbogado(getBoolean(map.get(PRE_PRECISAABOGADO)));
		dtPretensionesDefender.setIDTipoProcedimiento(getBigInteger(map.get(PRE_IDTIPOPROCEDIMIENTO)));		
		dtPretensionesDefender.setOtrosAspectos(map.get(PRE_OTROSASPECTOS));
		dtPretensionesDefender.setIDPartidoJudicial(getBigInteger(map.get(PRE_IDPARTIDOJUDICIAL)));
		dtPretensionesDefender.setIDSituacionProceso(getBigInteger(map.get(PRE_IDSITUACIONPROCESO)));
		dtPretensionesDefender.setNumeroProcedimiento(map.get(PRE_NUMEROPROCEDIMIENTO));
		dtPretensionesDefender.setAnoProcedimiento(map.get(PRE_ANOPROCEDIMIENTO));
		dtPretensionesDefender.setIDOrganoJudicial(getBigInteger(map.get(PRE_IDORGANOJUDICIAL)));
		dtPretensionesDefender.setPrecisaProcurador(getBoolean(map.get(PRE_PRECISAPROCURADOR)));
		dtPretensionesDefender.setIDListaTurnadoAbogados(getBigInteger(map.get(PRE_IDLISTATURNADOABOGADOS)));
		dtPretensionesDefender.setIDTarifaAbogados(getBigInteger(map.get(PRE_IDTARIFAABOGADOS)));
		dtPretensionesDefender.setIDTarifaProcuradores(getBigInteger(map.get(PRE_IDTARIFAPROCURADORES)));
		dtPretensionesDefender.setPorcentajeTarifaAbogado(getBigDecimal(map.get(PRE_PORCENTAJETARIFAABOGADO)));
		dtPretensionesDefender.setPorcentajeTarifaProcurador(getBigDecimal(map.get(PRE_PORCENTAJETARIFAPROCURADOR)));		
		dtPretensionesDefender.setIDTipoFacturacion(getBigInteger(map.get(PRE_IDTIPOFACTURACION)));		
		dtPretensionesDefender.setPretensionDefender(map.get(PRE_PRETENSIONDEFENDER));		
		dtPretensionesDefender.setSAM(getBoolean(map.get(PRE_SAM)));		
	}


}
