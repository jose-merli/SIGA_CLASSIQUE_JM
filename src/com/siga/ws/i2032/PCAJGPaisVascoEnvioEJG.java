package com.siga.ws.i2032;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplPortBindingStub;
import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplServiceLocator;
import com.siga.ws.i2032.axis.ejg.RemSolicitudesEnt;
import com.siga.ws.i2032.axis.ejg.RemSolicitudesSal;
import com.siga.ws.i2032.axis.ejg.Solicitud;
import com.siga.ws.i2032.axis.ejg.SolicitudRemitida;
import com.siga.ws.i2032.axis.ejg.SolicitudesRemitidas;
import com.siga.ws.i2032.xsd.ejg.DatosEconomicosBienInmueble;
import com.siga.ws.i2032.xsd.ejg.DatosEconomicosBienMueble;
import com.siga.ws.i2032.xsd.ejg.DatosEconomicosCargaEconomica;
import com.siga.ws.i2032.xsd.ejg.DatosEconomicosIRPF20;
import com.siga.ws.i2032.xsd.ejg.DatosEconomicosIngresos;
import com.siga.ws.i2032.xsd.ejg.DatosPareja;
import com.siga.ws.i2032.xsd.ejg.DatosSolicitante;
import com.siga.ws.i2032.xsd.ejg.DatosSolicitud;
import com.siga.ws.i2032.xsd.ejg.HijosFamiliaresACargo;
import com.siga.ws.i2032.xsd.ejg.ParteContraria;
import com.siga.ws.i2032.xsd.ejg.ProfesionalDesignado;
import com.siga.ws.i2032.xsd.ejg.RemitirDatosSolicitudesDocument;
import com.siga.ws.i2032.xsd.ejg.ResolucionSOJ;
import com.siga.ws.i2032.xsd.ejg.Solicitudes;

public class PCAJGPaisVascoEnvioEJG extends PCAJGPaisVascoComun {

	private static String ERROR_GENERAL_CODIGOCORRECTO = "00";	
	private static String SOLICITUD_RECHAZADA = "REC";
	
	
	Map<String, List<Map<String, String>>> htCargaDtUnidadFamiliar = new Hashtable<String, List<Map<String,String>>>();
	Map<String, List<Map<String, String>>> htCargaDtProfesionalesDesignados = new Hashtable<String, List<Map<String,String>>>();
	
	
	@Override
	public void execute() throws Exception {		
		
		UserTransaction tx = getUsrBean().getTransaction();
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		WSPaisVascoAdm wsPaisVascoAdm = new WSPaisVascoAdm();
		
		List<Hashtable<String, String>> listDtExpedientes = wsPaisVascoAdm.getDtExpedientes(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());		
		construyeHTxEJG(wsPaisVascoAdm.getDtUnidadFamiliar(getIdInstitucion(), getIdRemesa()), htCargaDtUnidadFamiliar);
		construyeHTxEJG(wsPaisVascoAdm.getDtProfesionalesDesignados(getIdInstitucion(), getIdRemesa()), htCargaDtProfesionalesDesignados);
		
		Map<String, String> mapExp;
		String anio = null;
		String numejg = null;
		String numero = null;
		String idTipoEJG = null;
		
		try {			
			tx.begin();
			//elimino primero las posibles respuestas que ya tenga por si se ha relanzado
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			cajgRespuestaEJGRemesaAdm.insertaErrorEJGnoEnviados(getIdInstitucion(), getIdRemesa(), getUsrBean(), PCAJGConstantes.V_WS_2032_EJG);
			
			EnvioSolicitudesImplServiceLocator locator;
			EnvioSolicitudesImplPortBindingStub stub;	
			
			boolean enviado = false;
			
			for (int i = 0; i < listDtExpedientes.size(); i++) {
								
				mapExp = listDtExpedientes.get(i);
				anio = mapExp.get(ANIO);
				numejg = mapExp.get(NUMEJG);
				numero = mapExp.get(NUMERO);
				idTipoEJG = mapExp.get(IDTIPOEJG);
				
				String mensajeLog = "Envío y recepción webservice del colegio " + getIdInstitucion() + " de la remesa " + getIdRemesa() + " para el expediente " + anio + "/" + numejg;
				locator = new EnvioSolicitudesImplServiceLocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), mensajeLog));
				stub = new EnvioSolicitudesImplPortBindingStub(new java.net.URL(getUrlWS()), locator);
				
				escribeLogRemesa("Enviando información del expediente " + anio + "/" + numejg);				
				RemitirDatosSolicitudesDocument remitirDatosSolicitudesDocument = getDatosSolicitudes(mapExp);
								
				if(validateXML_EJG(remitirDatosSolicitudesDocument, anio, numejg, numero, idTipoEJG)) {
					
					com.siga.ws.i2032.xsd.ejg.RemSolicitudesEnt remSolicitudesEntXSD = remitirDatosSolicitudesDocument.getRemitirDatosSolicitudes().getArg0();
					RemSolicitudesEnt remSolicitudesEnt = new RemSolicitudesEnt();
					remSolicitudesEnt.setIdioma(remSolicitudesEntXSD.getIdioma());
					Solicitudes solicitudesXSD = remSolicitudesEntXSD.getSolicitudes();
					
					if (solicitudesXSD != null && solicitudesXSD.getSolicitudArray().length > 0) {
						com.siga.ws.i2032.xsd.ejg.Solicitud solicitudXSD = null;
						Solicitud[] solicitudes = new Solicitud[solicitudesXSD.getSolicitudArray().length]; 
						for (int j = 0; j < solicitudesXSD.getSolicitudArray().length; j++) {
							solicitudXSD = solicitudesXSD.getSolicitudArray()[j];
							String xml = solicitudXSD.xmlText();
							solicitudes[j] = (Solicitud) AxisObjectSerializerDeserializer.deserializeAxisObject(xml, Solicitud.class);							
						}
						remSolicitudesEnt.setSolicitudes(solicitudes);
						
						if (!isSimular()) {							
							RemSolicitudesSal remSolicitudesSal = stub.remitirDatosSolicitudes(remSolicitudesEnt);
							enviado = true;
							SolicitudesRemitidas solicitudesRemitidas = remSolicitudesSal.getSolicitudes();
							String errorGeneral = solicitudesRemitidas.getErrorGeneral();
							
							
							if (ERROR_GENERAL_CODIGOCORRECTO.equals(errorGeneral)) {//si es correcto
								SolicitudRemitida solicitudRemitidas[] = solicitudesRemitidas.getSolicitud();
								if (solicitudRemitidas != null) {
									for (SolicitudRemitida solicitudRemitida : solicitudRemitidas) {
										if (SOLICITUD_RECHAZADA.equals(solicitudRemitida.getResultadoEnvio())) {
											String descripcionError = "El expediente ha sido rechazado." +
													" Motivo del rechazo: \"" + (solicitudRemitida.getMotivoRechazo()!=null?solicitudRemitida.getMotivoRechazo():"") + "\"" +
													" Campos: \"" + (solicitudRemitida.getCampos()!=null?solicitudRemitida.getCampos():"") + "\"";
											escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION);
										}
									}
								}
							} else {
								String descripcionError = "Error: \"" + errorGeneral + "\"";						
								escribeErrorExpediente(anio, numejg, numero, idTipoEJG, descripcionError, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION);						
							}
						}
					}
				}
			}
			
			
			if (enviado) { //si al menos hemos enviado un expediente							
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());				
				
				// Marcar como generada
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_GENERADA);
	
				if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_ENVIADA)) {
					//MARCAMOS COMO ENVIADA
					cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(getUsrBean(), String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
				}				
			}
			tx.commit();
			
		}  finally {			
			
		}
	}

	private RemitirDatosSolicitudesDocument getDatosSolicitudes(Map<String, String> mapExp) {
		RemitirDatosSolicitudesDocument remitirDatosSolicitudesDocument = RemitirDatosSolicitudesDocument.Factory.newInstance();		
		com.siga.ws.i2032.xsd.ejg.RemSolicitudesEnt remSolicitudesEnt = remitirDatosSolicitudesDocument.addNewRemitirDatosSolicitudes().addNewArg0();
		remSolicitudesEnt.setIdioma(mapExp.get(IDIOMA));
		
		com.siga.ws.i2032.xsd.ejg.Solicitud solicitud = remSolicitudesEnt.addNewSolicitudes().addNewSolicitud();
		
		rellenaDatosSolicitud(solicitud.addNewDatosSolicitud(), mapExp);
		rellenaDatosProfesionalesDesignados(solicitud, getKey(mapExp));
		rellenaDatosSolicitanteParejaHijos(solicitud, getKey(mapExp));		
		rellenaParteContraria(solicitud.addNewParteContraria(), mapExp);
		rellenaResolucionSOJ(solicitud.addNewResolucionSOJ(), mapExp);
		return remitirDatosSolicitudesDocument;
	}

	
	private void rellenaDatosProfesionalesDesignados(com.siga.ws.i2032.xsd.ejg.Solicitud solicitud, String key) {
		List<Map<String, String>> list = htCargaDtProfesionalesDesignados.get(key);		
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				ProfesionalDesignado profesionalDesignado = solicitud.addNewProfesionalesDesignados();
				profesionalDesignado.setColegio(map.get(PD_COLEGIO));
				profesionalDesignado.setDeOficio(map.get(PD_DEOFICIO));
				profesionalDesignado.setEstadoDesignacion(map.get(PD_ESTADODESIGNACION));
				profesionalDesignado.setFechaDesignacion(map.get(PD_FECHADESIGNACION));
				profesionalDesignado.setFechaEstadoDesignacion(map.get(PD_FECHAESTADODESIGNACION));
				profesionalDesignado.setFechaRenunciaHonorarios(map.get(PD_FECHARENUNCIAHONORARIOS));
				profesionalDesignado.setNumeroColegiado(map.get(PD_NUMEROCOLEGIADO));
				profesionalDesignado.setNumeroIdentificacion(map.get(PD_NUMEROIDENTIFICACION));
				profesionalDesignado.setRenunciaHonorarios(map.get(PD_RENUNCIAHONORARIOS));
				profesionalDesignado.setTipoIdentificador(map.get(PD_TIPOIDENTIFICADOR));
				profesionalDesignado.setTipoProfesional(map.get(PD_TIPOPROFESIONAL));
			}
		}
	}
	
	private void rellenaDatosSolicitanteParejaHijos(com.siga.ws.i2032.xsd.ejg.Solicitud solicitud, String key) {
		List<Map<String, String>> list = htCargaDtUnidadFamiliar.get(key);		
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				
				if ("1".equals(map.get(S_ISSOLICITANTE))) {
					DatosSolicitante datosSolicitante = solicitud.addNewDatosSolicitante();
					datosSolicitante.setApellido1(map.get(S_APELLIDO1));
					datosSolicitante.setApellido1Madre(map.get(S_APELLIDO1MADRE));
					datosSolicitante.setApellido1Padre(map.get(S_APELLIDO1PADRE));
					datosSolicitante.setApellido2(map.get(S_APELLIDO2));
					datosSolicitante.setApellido2Madre(map.get(S_APELLIDO2MADRE));
					datosSolicitante.setApellido2Padre(map.get(S_APELLIDO2PADRE));
					datosSolicitante.setCalle(map.get(S_CALLE));
					datosSolicitante.setCodigoPostal(map.get(S_CODIGOPOSTAL));
					datosSolicitante.setEMail(map.get(S_EMAIL));
					datosSolicitante.setEstadoCivil(map.get(S_ESTADOCIVIL));
					datosSolicitante.setFechaNacimiento(map.get(S_FECHANACIMIENTO));
					datosSolicitante.setMinusvalia(map.get(S_MINUSVALIA));
					datosSolicitante.setMunicipio(map.get(S_MUNICIPIO));
					datosSolicitante.setNacionalidad(map.get(S_NACIONALIDAD));
					datosSolicitante.setNombre(map.get(S_NOMBRE));
					datosSolicitante.setNombreMadre(map.get(S_NOMBREMADRE));
					datosSolicitante.setNombrePadre(map.get(S_NOMBREPADRE));
					datosSolicitante.setNumero(map.get(S_NUMERO));
					datosSolicitante.setNumeroCIF(map.get(S_NUMEROCIF));
					datosSolicitante.setNumeroIdentificacion(map.get(S_NUMEROIDENTIFICACION));
					datosSolicitante.setNumeroIdentificadorMadre(map.get(S_NUMEROIDENTIFICADORMADRE));
					datosSolicitante.setNumeroIdentificadorPadre(map.get(S_NUMEROIDENTIFICADORPADRE));
					datosSolicitante.setObservaciones(map.get(S_OBSERVACIONES));
					datosSolicitante.setObservacionesMinusvalia(map.get(S_OBSERVACIONESMINUSVALIA));
					datosSolicitante.setPiso(map.get(S_PISO));
					datosSolicitante.setProfesion(map.get(S_PROFESION));
					datosSolicitante.setProvincia(map.get(S_PROVINCIA));
					datosSolicitante.setRazonSocial(map.get(S_RAZONSOCIAL));
					datosSolicitante.setSexo(map.get(S_SEXO));
					datosSolicitante.setTelefono(map.get(S_TELEFONO));
					datosSolicitante.setTipoActividad(map.get(S_TIPOACTIVIDAD));
					datosSolicitante.setTipoIdentificador(map.get(S_TIPOIDENTIFICADOR));
					datosSolicitante.setTipoIdentificadorMadre(map.get(S_TIPOIDENTIFICADORMADRE));
					datosSolicitante.setTipoIdentificadorPadre(map.get(S_TIPOIDENTIFICADORPADRE));
					datosSolicitante.setTipoImplicacion(map.get(S_TIPOIMPLICACION));
					datosSolicitante.setTipoIntervencion(map.get(S_TIPOINTERVENCION));
					datosSolicitante.setTipoPersona(map.get(S_TIPOPERSONA));
					datosSolicitante.setTipoVia(map.get(S_TIPOVIA));
					datosSolicitante.setUnidadConvivencial(map.get(S_UNIDADCONVIVENCIAL));
					
					rellenaDatosEconomicosIngresos(solicitud.addNewDatosEconomicosIngresos(), map);
					rellenaDatosEconomicosBienInmueble(solicitud.addNewDatosEconomicosBienInmueble(), map);
					rellenaDatosEconomicosBieneMuebles(solicitud.addNewDatosEconomicosBienMueble(), map);
					rellenaDatosEconomicosCargaEconomica(solicitud.addNewDatosEconomicosCargaEconomica(), map);
					rellenaDatosEconomicosIRPF20(solicitud.addNewDatosEconomicosIRPF20(), map);
				} else if ("1".equals(map.get(S_ISCONYUGE))) {
					DatosPareja datosPareja = solicitud.addNewDatosPareja();
					datosPareja.setApellido1(map.get(S_APELLIDO1));
					datosPareja.setApellido1Madre(map.get(S_APELLIDO1MADRE));
					datosPareja.setApellido1Padre(map.get(S_APELLIDO1PADRE));
					datosPareja.setApellido2(map.get(S_APELLIDO2));
					datosPareja.setApellido2Madre(map.get(S_APELLIDO2MADRE));
					datosPareja.setApellido2Padre(map.get(S_APELLIDO2PADRE));
					datosPareja.setCalle(map.get(S_CALLE));
					datosPareja.setCodigoPostal(map.get(S_CODIGOPOSTAL));
					datosPareja.setEMail(map.get(S_EMAIL));
					datosPareja.setEstadoCivil(map.get(S_ESTADOCIVIL));
					datosPareja.setFechaNacimiento(map.get(S_FECHANACIMIENTO));
					datosPareja.setMinusvalia(map.get(S_MINUSVALIA));
					datosPareja.setMunicipio(map.get(S_MUNICIPIO));
					datosPareja.setNacionalidad(map.get(S_NACIONALIDAD));
					datosPareja.setNombre(map.get(S_NOMBRE));
					datosPareja.setNombreMadre(map.get(S_NOMBREMADRE));
					datosPareja.setNombrePadre(map.get(S_NOMBREPADRE));
					datosPareja.setNumero(map.get(S_NUMERO));					
					datosPareja.setNumeroIdentificacion(map.get(S_NUMEROIDENTIFICACION));
					datosPareja.setNumeroIdentificadorMadre(map.get(S_NUMEROIDENTIFICADORMADRE));
					datosPareja.setNumeroIdentificadorPadre(map.get(S_NUMEROIDENTIFICADORPADRE));
					datosPareja.setObservaciones(map.get(S_OBSERVACIONES));
					datosPareja.setObservacionesMinusvalia(map.get(S_OBSERVACIONESMINUSVALIA));
					datosPareja.setPiso(map.get(S_PISO));					
					datosPareja.setProvincia(map.get(S_PROVINCIA));					
					datosPareja.setTelefono(map.get(S_TELEFONO));					
					datosPareja.setTipoIdentificador(map.get(S_TIPOIDENTIFICADOR));
					datosPareja.setTipoIdentificadorMadre(map.get(S_TIPOIDENTIFICADORMADRE));
					datosPareja.setTipoIdentificadorPadre(map.get(S_TIPOIDENTIFICADORPADRE));
					datosPareja.setTipoImplicacion(map.get(S_TIPOIMPLICACION));					
					datosPareja.setTipoVia(map.get(S_TIPOVIA));
				} else if ("1".equals(map.get(S_ISHIJO))) {
					HijosFamiliaresACargo hijosFamiliaresACargo = solicitud.addNewHijosFamiliaresACargo();
					hijosFamiliaresACargo.setApellido1(map.get(S_APELLIDO1));					
					hijosFamiliaresACargo.setApellido2(map.get(S_APELLIDO2));					
					hijosFamiliaresACargo.setFechaNacimiento(map.get(S_FECHANACIMIENTO));
					hijosFamiliaresACargo.setMinusvalia(map.get(S_MINUSVALIA));					
					hijosFamiliaresACargo.setNacionalidad(map.get(S_NACIONALIDAD));
					hijosFamiliaresACargo.setNombre(map.get(S_NOMBRE));
					hijosFamiliaresACargo.setNumeroIdentificacion(map.get(S_NUMEROIDENTIFICACION));
					hijosFamiliaresACargo.setObservaciones(map.get(S_OBSERVACIONES));
					hijosFamiliaresACargo.setObservacionesMinusvalia(map.get(S_OBSERVACIONESMINUSVALIA));
					hijosFamiliaresACargo.setRelacionSolicitante(map.get(S_RELACIONSOLICITANTE));
					hijosFamiliaresACargo.setTipoIdentificador(map.get(S_TIPOIDENTIFICADOR));
					hijosFamiliaresACargo.setTipoImplicacion(map.get(S_TIPOIMPLICACION));				
					
				}
			}
		}
		
	}

	private void rellenaDatosEconomicosIRPF20(DatosEconomicosIRPF20 datosEconomicosIRPF20, Map<String, String> map) {
		datosEconomicosIRPF20.setDescripcion(map.get(I2_DESCRIPCION));
		datosEconomicosIRPF20.setEjercicio(map.get(I2_EJERCICIO));
		datosEconomicosIRPF20.setImporte(map.get(I2_IMPORTE));
		datosEconomicosIRPF20.setObservaciones(map.get(I2_OBSERVACIONES));
		datosEconomicosIRPF20.setTipoRendimiento(map.get(I2_TIPORENDIMIENTO));		
	}

	private void rellenaDatosEconomicosCargaEconomica(DatosEconomicosCargaEconomica datosEconomicosCargaEconomica, Map<String, String> map) {
		datosEconomicosCargaEconomica.setDescripcion(map.get(CE_DESCRIPCION));
		datosEconomicosCargaEconomica.setImporte(map.get(CE_IMPORTE));
		datosEconomicosCargaEconomica.setObservaciones(map.get(EI_OBSERVACIONES));
		datosEconomicosCargaEconomica.setPeriodicidad(map.get(CE_PERIODICIDAD));
		datosEconomicosCargaEconomica.setTipoCargaEconomica(map.get(CE_TIPOCARGAECONOMICA));		
	}

	private void rellenaDatosEconomicosBieneMuebles(DatosEconomicosBienMueble datosEconomicosBienMueble, Map<String, String> map) {
		datosEconomicosBienMueble.setDescripcion(map.get(BM_DESCRIPCION));
		datosEconomicosBienMueble.setMatricula(map.get(BM_MATRICULA));		
		datosEconomicosBienMueble.setObservaciones(map.get(BM_OBSERVACIONES));
		datosEconomicosBienMueble.setOrigenValoracion(map.get(BM_ORIGENVALORACION));
		datosEconomicosBienMueble.setTipoMueble(map.get(BM_TIPOMUEBLE));
		datosEconomicosBienMueble.setTitular(map.get(BM_TITULAR));
		datosEconomicosBienMueble.setValoracion(map.get(BM_VALORACION));
		
	}

	private void rellenaDatosEconomicosBienInmueble(DatosEconomicosBienInmueble datosEconomicosBienInmueble, Map<String, String> map) {
		datosEconomicosBienInmueble.setObservaciones(map.get(BI_OBSERVACIONES));
		datosEconomicosBienInmueble.setOrigenValoracion(map.get(BI_ORIGENVALORACION));
		datosEconomicosBienInmueble.setPorcentajePropiedad(map.get(BI_PORCENTAJEPROPIEDAD));
		datosEconomicosBienInmueble.setTipoInmueble(map.get(BI_TIPOINMUEBLE));
		datosEconomicosBienInmueble.setTipoVivienda(map.get(BI_TIPOVIVIENDA));
		datosEconomicosBienInmueble.setTitular(map.get(BI_TITULAR));
		datosEconomicosBienInmueble.setValorCatastral(map.get(BI_VALORCATASTRAL));
		datosEconomicosBienInmueble.setValoracion(map.get(BI_VALORACION));
	}

	private void rellenaDatosEconomicosIngresos(DatosEconomicosIngresos datosEconomicosIngresos, Map<String, String> map) {
		datosEconomicosIngresos.setDescripcion(map.get(EI_DESCRIPCION));
		datosEconomicosIngresos.setImporte(map.get(EI_IMPORTE));
		datosEconomicosIngresos.setObservaciones(map.get(EI_OBSERVACIONES));
		datosEconomicosIngresos.setOrigenValoracion(map.get(EI_ORIGENVALORACION));
		datosEconomicosIngresos.setPerceptor(map.get(EI_PERCEPTOR));
		datosEconomicosIngresos.setPeriodicidad(map.get(EI_PERIODICIDAD));
		datosEconomicosIngresos.setTipoIngreso(map.get(EI_TIPOINGRESO));
	}

	private void rellenaResolucionSOJ(ResolucionSOJ resolucionSOJ, Map<String, String> mapExp) {
		resolucionSOJ.setAlegaciones(mapExp.get(RS_ALEGACIONES));
		resolucionSOJ.setDocumentoResolucion(mapExp.get(RS_DOCUMENTO_RESOLUCION));
		resolucionSOJ.setFechaResolucion(mapExp.get(RS_FECHARESOLUCION));
		resolucionSOJ.setObservacionesResolucion(mapExp.get(RS_OBSERVACIONESRESOLUCION));
		resolucionSOJ.setOrigenResolucion(mapExp.get(RS_ORIGENRESOLUCION));
		resolucionSOJ.setResolucionSOJ(mapExp.get(RS_RESOLUCIONSOJ));
		resolucionSOJ.setTipoResolucionEconomica(mapExp.get(RS_TIPORESOLUCIONECONOMICA));
	}

	private void rellenaParteContraria(ParteContraria parteContraria, Map<String, String> mapExp) {
		parteContraria.setApellido1(mapExp.get(PC_APELLIDO1));
		parteContraria.setApellido2(mapExp.get(PC_APELLIDO2));
		parteContraria.setCalle(mapExp.get(PC_CALLE));
		parteContraria.setCodigoPostal(mapExp.get(PC_CODIGOPOSTAL));
		parteContraria.setMunicipio(mapExp.get(PC_MUNICIPIO));
		parteContraria.setNacionalidad(mapExp.get(PC_NACIONALIDAD));
		parteContraria.setNombre(mapExp.get(PC_NOMBRE));
		parteContraria.setNumero(mapExp.get(PC_NUMERO));
		parteContraria.setNumeroCIF(mapExp.get(PC_NUMEROCIF));
		parteContraria.setNumeroIdentificacion(mapExp.get(PC_NUMEROIDENTIFICACION));
		parteContraria.setPiso(mapExp.get(PC_PISO));
		parteContraria.setProvincia(mapExp.get(PC_PROVINCIA));
		parteContraria.setRazonSocial(mapExp.get(PC_RAZONSOCIAL));
		parteContraria.setRelacionSolicitante(mapExp.get(PC_RELACIONSOLICITANTE));
		parteContraria.setTipoActividad(mapExp.get(PC_TIPOACTIVIDAD));
		parteContraria.setTipoIdentificador(mapExp.get(PC_TIPOIDENTIFICADOR));
		parteContraria.setTipoImplicacion(mapExp.get(PC_TIPOIMPLICACION));
		parteContraria.setTipoPersona(mapExp.get(PC_TIPOPERSONA));
		parteContraria.setTipoVia(mapExp.get(PC_TIPOVIA));
	}

	private void rellenaDatosSolicitud(DatosSolicitud datosSolicitud,	Map<String, String> mapExp) {
		datosSolicitud.setDelito(mapExp.get(DS_DELITO));
		datosSolicitud.setEstadoSolicitud(mapExp.get(DS_ESTADO_SOLICITUD));
		datosSolicitud.setFechaSolicitud(mapExp.get(DS_FECHASOLICITUD));
		datosSolicitud.setMateria(mapExp.get(DS_MATERIA));
		datosSolicitud.setNig(mapExp.get(DS_NIG));
		datosSolicitud.setNumeroProcedimientoOJ(mapExp.get(DS_NUMEROPROCEDIMIENTOOJ));
		datosSolicitud.setNumeroSolicitud(mapExp.get(DS_NUMEROSOLICITUD));
		datosSolicitud.setObservaciones(mapExp.get(DS_OBSERVACIONES));
		datosSolicitud.setOrganoJudicial(mapExp.get(DS_ORGANOJUDICIAL));
		datosSolicitud.setPartidoJudicial(mapExp.get(DS_PARTIDOJUDICIAL));
		datosSolicitud.setProvinciaCAJG(mapExp.get(DS_PROVINCIACAJG));
		datosSolicitud.setTipoAsuntoSOJ(mapExp.get(DS_TIPOASUNTOSOJ));
		datosSolicitud.setTipoProcedimientoCAJG(mapExp.get(DS_TIPOPROCEDIMIENTOCAJG));
		datosSolicitud.setTipoProcedimientoOJ(mapExp.get(DS_TIPOPROCEDIMIENTOOJ));
		datosSolicitud.setTipoTurno(mapExp.get(DS_TIPOTURNO));
		datosSolicitud.setUbicacionSolicitud(mapExp.get(DS_UBICACIONSOLICITUD));
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

}
