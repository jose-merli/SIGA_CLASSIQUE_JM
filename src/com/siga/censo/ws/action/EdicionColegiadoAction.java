package com.siga.censo.ws.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS;
import org.redabogacia.sigaservices.app.autogen.model.CenPais;
import org.redabogacia.sigaservices.app.autogen.model.CenPoblaciones;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenColegiado;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatos;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosIncidencias;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDireccion;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenMaestroIncidenc;
import org.redabogacia.sigaservices.app.services.cen.CenPaisService;
import org.redabogacia.sigaservices.app.services.cen.CenPoblacionService;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;
import org.redabogacia.sigaservices.app.services.cen.ws.EcomCenColegiadoService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.ws.form.EdicionColegiadoForm;
import com.siga.censo.ws.util.CombosCenWS;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class EdicionColegiadoAction extends MasterAction {
	
	private static final Logger log = Logger.getLogger(EdicionColegiadoAction.class);
	public static final String DATAPAGINADOR = "DATAPAGINADOR_LISTADO_COLEGIADOS_HISTORICO";
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion = miForm.getModo();
				if (accion != null && accion.equals("archivar")) {
					mapDestino = archivar(mapping, miForm, request, response);				
				} else if (accion != null && accion.equals("editarCambio")) {
					mapDestino = editarCambio(mapping, miForm, request, response);	
				} else if (accion != null && accion.equals("verCambio")) {
					mapDestino = verCambio(mapping, miForm, request, response);	
				}else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{		
		return verEditar("ver", mapping, formulario, request, response, null);
	}
	protected String verCambio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{		
		return verEditar("verCambio", mapping, formulario, request, response, null);
	}
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return verEditar("editar", mapping, formulario, request, response, null);
	}
	protected String editarCambio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return verEditar("editarCambio", mapping, formulario, request, response, null);
	}
	
	private String archivar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{
		log.debug("EdicionColegiadoActin.archivar() - INICIO");
		String accion = "ver";		
		Long idcensodatos = null;
		
		try {
			
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;
			
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			//recuperamos el registro de bdd
			EcomCenDatos ecomCenDatos = cenWSService.getEcomCenDatosByPk(edicionColegiadoForm.getIdcensodatos());
						
			//insertamos en el histórico y lanzamos el proceso
			EcomCenColegiadoService ecomCenColegiadoService = (EcomCenColegiadoService) BusinessManager.getInstance().getService(EcomCenColegiadoService.class);
			EcomCenColegiado ecomCenColegiado = ecomCenColegiadoService.getEcomCenColegiado(edicionColegiadoForm.getIdcensodatos());
			EcomCenDireccion ecomCenDireccion = ecomCenColegiadoService.getEcomCenDireccion(ecomCenDatos.getIdcensodireccion());
			//actualizamos el nuevo estado
			ecomCenDatos.setIdestadocolegiado(ECOM_CEN_MAESESTADOCOLEGIAL.ARCHIVADO.getCodigo());
			
			BusinessManager.getInstance().startTransaction();
			ecomCenColegiado = ecomCenColegiadoService.insertHistorico(ecomCenColegiado, ecomCenDatos, ecomCenDireccion, null);			
			
			BusinessManager.getInstance().commitTransaction();
			idcensodatos = ecomCenColegiado.getIdcensodatos();
						
		} catch (Exception e) {
			BusinessManager.getInstance().endTransaction();
			throwExcp("messages.general.error", e, null);
		} finally {
			BusinessManager.getInstance().endTransaction();
		}
		log.debug("EdicionColegiadoActin.archivar() - FIN");
		return verEditar(accion, mapping, formulario, request, response, idcensodatos);
	}
	
	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String modificar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{
		log.debug("EdicionColegiadoActin.modificar() - INICIO");
		String accion = "editar";
		Long idcensodatos = null;
		//CENSO-298@DTT.JAMARTIN@14/06/2022@INICIO
		String numDocumentoOriginal = "";
		//CENSO-298@DTT.JAMARTIN@14/06/2022@FIN 
		
		try {
			
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;
			
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			//recuperamos el registro de bdd pq hay campos que no cambian
			EcomCenDatos ecomCenDatos = cenWSService.getEcomCenDatosByPk(edicionColegiadoForm.getIdcensodatos());
			
			log.debug("Recuperamos ecomCenDatos de BBDD: \n" + 
			" idCensoDatos: "+ (ecomCenDatos.getIdcensodatos() != null ? ecomCenDatos.getIdcensodatos().toString() : "null") + 
			"\n nColegiado: "+ (ecomCenDatos.getNcolegiado() != null ? ecomCenDatos.getNcolegiado().toString() : "null") + 
			"\n NumDocumento: "+ (ecomCenDatos.getNumdocumento() != null ? ecomCenDatos.getNumdocumento().toString() : "null"));
			
			//CENSO-298@DTT.JAMARTIN@14/06/2022@INICIO
			numDocumentoOriginal = ecomCenDatos.getNumdocumento();
			//CENSO-298@DTT.JAMARTIN@14/06/2022@FIN
			ecomCenDatos.setPublicarcolegiado(getCheckShort(edicionColegiadoForm.isPublicarcolegiado()));
			ecomCenDatos.setNumsolicitudcolegiacion(edicionColegiadoForm.getNumsolicitudcolegiacion());
			ecomCenDatos.setNcolegiado(edicionColegiadoForm.getNcolegiado());
			ecomCenDatos.setNombre(edicionColegiadoForm.getNombre());
			ecomCenDatos.setApellido1(edicionColegiadoForm.getApellido1());
			ecomCenDatos.setApellido2(edicionColegiadoForm.getApellido2());
			if (edicionColegiadoForm.getSexo() != null && !edicionColegiadoForm.getSexo().trim().equals("")) {
				ecomCenDatos.setSexo(edicionColegiadoForm.getSexo());
			} else {
				ecomCenDatos.setSexo(null);
			}
			if (edicionColegiadoForm.getFechanacimiento() != null && !edicionColegiadoForm.getFechanacimiento().trim().equals("")) {
				ecomCenDatos.setFechanacimiento(GstDate.convertirFechaDiaMesAnio(edicionColegiadoForm.getFechanacimiento()));
			} else {
				ecomCenDatos.setFechanacimiento(null);
			}
			if (edicionColegiadoForm.getIdcensotipoidentificacion() != null && edicionColegiadoForm.getIdcensotipoidentificacion().shortValue() > 0) {
				ecomCenDatos.setIdcensotipoidentificacion(edicionColegiadoForm.getIdcensotipoidentificacion());
			} else {
				ecomCenDatos.setIdcensotipoidentificacion(null);
			}
			ecomCenDatos.setNumdocumento(edicionColegiadoForm.getNumdocumento());
			if (edicionColegiadoForm.getIdecomcensosituacionejer() != null && edicionColegiadoForm.getIdecomcensosituacionejer().shortValue() > 0) {							
				ecomCenDatos.setIdecomcensosituacionejer(edicionColegiadoForm.getIdecomcensosituacionejer());
			} else {
				ecomCenDatos.setIdecomcensosituacionejer(null);
			}
			
			if (edicionColegiadoForm.getFechasituacion() != null && !edicionColegiadoForm.getFechasituacion().trim().equals("")) {
				ecomCenDatos.setFechasituacion(GstDate.convertirFechaDiaMesAnio(edicionColegiadoForm.getFechasituacion()));
			} else {
				ecomCenDatos.setFechasituacion(null);
			}
			EcomCenDireccion ecomCenDireccion = null;
			if (edicionColegiadoForm.isCambioSituacion()){
				ecomCenDatos.setHaycambiosituacion(new Short("1"));
				ecomCenDatos.setIdmotivosituacion(edicionColegiadoForm.getIdmotivocambio());
				accion = "editarCambio";
			}else{
				ecomCenDatos.setPublicartelefono(getCheckShort(edicionColegiadoForm.isPublicartelefono()));
				ecomCenDatos.setTelefono(edicionColegiadoForm.getTelefono());
				ecomCenDatos.setPublicartelefonomovil(getCheckShort(edicionColegiadoForm.isPublicartelefonomovil()));
				ecomCenDatos.setTelefonomovil(edicionColegiadoForm.getTelefonomovil());
				ecomCenDatos.setPublicarfax(getCheckShort(edicionColegiadoForm.isPublicarfax()));
				ecomCenDatos.setFax(edicionColegiadoForm.getFax());
				ecomCenDatos.setPublicaremail(getCheckShort(edicionColegiadoForm.isPublicaremail()));
				ecomCenDatos.setEmail(edicionColegiadoForm.getEmail());
				ecomCenDatos.setResidente(getCheckShort(edicionColegiadoForm.isResidente()));
				
				ecomCenDireccion = new EcomCenDireccion();
		//		ecomCenDireccion.setIdcensodireccion(edicionColegiadoForm.getIdcensodireccion());
				ecomCenDireccion.setPublicar(getCheckShort(edicionColegiadoForm.isPublicardireccion()));
				ecomCenDireccion.setDesctipovia(edicionColegiadoForm.getDesctipovia());
				ecomCenDireccion.setDomicilio(edicionColegiadoForm.getDomicilio());
				ecomCenDireccion.setCodigopostal(edicionColegiadoForm.getCodigopostal());
				
				String codigoPaisExt = null;
				if (!ClsConstants.ID_PAIS_ESPANA.equals(edicionColegiadoForm.getCodigopaisextranj())) {	
					CenPais cenPais = new CenPais();
					cenPais.setIdpais(edicionColegiadoForm.getCodigopaisextranj());
					CenPaisService cenPaisService = (CenPaisService) getBusinessManager().getService(CenPaisService.class);
					cenPais = cenPaisService.get(cenPais);
					if (cenPais != null) {
						codigoPaisExt = cenPais.getCodigoext();	
					}			
				}
				
				ecomCenDireccion.setCodigopaisextranj(codigoPaisExt);
				
				ecomCenDireccion.setCodigoprovincia(edicionColegiadoForm.getCodigoprovincia());
				
				if (edicionColegiadoForm.getCodigopoblacion() != null && !edicionColegiadoForm.getCodigopoblacion().trim().equals("")) {
					//tenemos el idpoblacion y tenemos que encontrar el codigo externo
					CenPoblacionService cenPoblacionService = (CenPoblacionService) BusinessManager.getInstance().getService(CenPoblacionService.class);
					CenPoblaciones cenPoblaciones = new CenPoblaciones();
					cenPoblaciones.setIdpoblacion(edicionColegiadoForm.getCodigopoblacion());
					cenPoblaciones = cenPoblacionService.get(cenPoblaciones);
					if (cenPoblaciones != null && cenPoblaciones.getCodigoext() != null) {					
						ecomCenDireccion.setCodigopoblacion(cenPoblaciones.getCodigoext());
					}
				}
				
				ecomCenDireccion.setDescripcionpoblacion(edicionColegiadoForm.getDescripcionpoblacion());
			}

			if (edicionColegiadoForm.getMediador() != null && !edicionColegiadoForm.getMediador().trim().equals("")) {
				ecomCenDatos.setMediador(Short.valueOf(edicionColegiadoForm.getMediador()));
			} else {
				ecomCenDatos.setMediador(null);
			}
			if (edicionColegiadoForm.getExentoCuota() != null && !edicionColegiadoForm.getExentoCuota().trim().equals("")) {
				ecomCenDatos.setExentocuotas(Short.valueOf(edicionColegiadoForm.getExentoCuota()));
			} else {
				ecomCenDatos.setExentocuotas(null);
			}
			//eliminamos el hash que tuviera
			ecomCenDatos.setHash(null);

			//insertamos en el histórico y lanzamos el proceso
			EcomCenColegiadoService ecomCenColegiadoService = (EcomCenColegiadoService) BusinessManager.getInstance().getService(EcomCenColegiadoService.class);
			EcomCenColegiado ecomCenColegiado = ecomCenColegiadoService.getEcomCenColegiado(edicionColegiadoForm.getIdcensodatos());
			
			log.debug("El ecomCenColegiado recuperado del formulario es: " + 
					" idCensoColegiado: " + (ecomCenColegiado.getIdcensocolegiado() != null ? ecomCenColegiado.getIdcensocolegiado().toString() : "null") + 
					"\n idCensoWSpagina: " + (ecomCenColegiado.getIdcensowspagina() != null ? ecomCenColegiado.getIdcensowspagina().toString() : "null") + 
					"\n idCensoDatos: " + (ecomCenColegiado.getIdcensodatos() != null ? ecomCenColegiado.getIdcensodatos().toString() : "null") + 
					"\n idPersona: " + (ecomCenColegiado.getIdpersona() != null ? ecomCenColegiado.getIdpersona().toString() : "null"));
			
			BusinessManager.getInstance().startTransaction();
			
			log.debug("El ecomCenDatos tras tratar las modificaciones: \n" + 
					" idCensoDatos: " + (ecomCenDatos.getIdcensodatos() != null ? ecomCenDatos.getIdcensodatos().toString() : "null") + 
					"\n nColegiado: " + (ecomCenDatos.getNcolegiado() != null ? ecomCenDatos.getNcolegiado().toString() : "null") + 
					"\n NumDocumento: " + (ecomCenDatos.getNumdocumento() != null ? ecomCenDatos.getNumdocumento().toString() : "null"));
			
			ecomCenColegiado = ecomCenColegiadoService.insertHistorico(ecomCenColegiado, ecomCenDatos, ecomCenDireccion, null);
			if (edicionColegiadoForm.isIncidenciaNumeroColegiadoDuplicadoRevisada()) {
				ecomCenColegiadoService.addIncidenciasRevisadas(ecomCenColegiado, ECOM_CEN_MAESTRO_INCIDENCIAS.NUMERO_COLEGIADO_DUPLICADO);
			}
			
			if (edicionColegiadoForm.isIncidenciaInscritoRevisada()) {
				ecomCenColegiadoService.addIncidenciasRevisadas(ecomCenColegiado, ECOM_CEN_MAESTRO_INCIDENCIAS.INSCRITO_A_NO_INSCRITO);
			}
			
			short idinstitucion = ecomCenColegiadoService.getIdinstitucion(ecomCenColegiado);
			//CENSO-331@DTT.JAMARTIN@26/01/2023@INICIO
//			//CENSO-298@DTT.JAMARTIN@14/06/2022@INICIO
//			ecomCenColegiadoService.lanzarProcesoAltaModificacionColegiado(idinstitucion, ecomCenColegiado, numDocumentoOriginal);
//			//CENSO-298@DTT.JAMARTIN@14/06/2022@FIN
			
			boolean tieneAdhesiones = ecomCenColegiadoService.tieneAdhesiones(ecomCenColegiado);
			ecomCenColegiadoService.lanzarProcesoAltaModificacionColegiado(idinstitucion, ecomCenColegiado, tieneAdhesiones, numDocumentoOriginal);
			//CENSO-331@DTT.JAMARTIN@26/01/2023@FIN
			
			if (edicionColegiadoForm.isIncidenciaPoblacionNoEncontradaRevisada()) {
				ecomCenColegiadoService.lanzaAltaModificacionPorNuevaPoblacion(idinstitucion, ecomCenColegiado, ecomCenDireccion);
			}
			
			BusinessManager.getInstance().commitTransaction();
			idcensodatos = ecomCenColegiado.getIdcensodatos();
			
						
		} catch (Exception e) {
			BusinessManager.getInstance().endTransaction();
			throwExcp("messages.general.error", e, null);
		} finally {
			BusinessManager.getInstance().endTransaction();
		}
		log.debug("EdicionColegiadoActin.modificar() - FIN");
		return verEditar(accion, mapping, formulario, request, response, idcensodatos);
	}
	
	private Short getCheckShort(boolean value) {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private boolean getCheckShort(Short value) {
		if (value != null && value.shortValue() == 1) {
			return true;
		} else {
			return false;
		}
	}

	protected String verEditar(String accion, ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, Long idcensodatos) throws SIGAException {

		try {	
			
			HttpSession session = request.getSession();
			session.removeAttribute(DATAPAGINADOR);
			
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;
						
			
			if (idcensodatos == null) {
				if (edicionColegiadoForm.getIdcensodatosPadre() != null) {
					idcensodatos = edicionColegiadoForm.getIdcensodatosPadre();				
				} else {
					// Recuperamos los datos del registro que hemos seleccionado
					Vector ocultos = formulario.getDatosTablaOcultos(0);
					if (ocultos != null) {
						if (ocultos.size() > 2) {					
							idcensodatos = Long.valueOf(ocultos.get(2).toString());
						}
					} else {
						throw new IllegalArgumentException("No se ha recibido el identificador para editar el colegiado");
					}
				}
			}
			
			if (edicionColegiadoForm.isHistorico()) {
				edicionColegiadoForm.setIdcensodatosPadre(edicionColegiadoForm.getIdcensodatos());
			} else {
				edicionColegiadoForm.setAccionPadre(accion);
				edicionColegiadoForm.setIdcensodatosPadre(null);
			}
			
					
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			EcomCenDatos ecomCenDatos = cenWSService.getEcomCenDatosByPk(idcensodatos);
						
			edicionColegiadoForm.setIdcensodatos(ecomCenDatos.getIdcensodatos());
			
			edicionColegiadoForm.setPublicarcolegiado(getCheckShort(ecomCenDatos.getPublicarcolegiado()));
			edicionColegiadoForm.setNumsolicitudcolegiacion(getValue(ecomCenDatos.getNumsolicitudcolegiacion()));
			edicionColegiadoForm.setNcolegiado(getValue(ecomCenDatos.getNcolegiado()));
			edicionColegiadoForm.setNombre(getValue(ecomCenDatos.getNombre()));
			edicionColegiadoForm.setApellido1(getValue(ecomCenDatos.getApellido1()));
			edicionColegiadoForm.setApellido2(getValue(ecomCenDatos.getApellido2()));
			edicionColegiadoForm.setSexo(getValue(ecomCenDatos.getSexo()));
			edicionColegiadoForm.setFechanacimiento(getValue(ecomCenDatos.getFechanacimiento()));
			edicionColegiadoForm.setIdcensotipoidentificacion(ecomCenDatos.getIdcensotipoidentificacion());
			edicionColegiadoForm.setNumdocumento(ecomCenDatos.getNumdocumento());
			edicionColegiadoForm.setPublicartelefono(getCheckShort(ecomCenDatos.getPublicartelefono()));
			edicionColegiadoForm.setTelefono(getValue(ecomCenDatos.getTelefono()));
			edicionColegiadoForm.setPublicartelefonomovil(getCheckShort(ecomCenDatos.getPublicartelefonomovil()));
			edicionColegiadoForm.setTelefonomovil(getValue(ecomCenDatos.getTelefonomovil()));
			edicionColegiadoForm.setPublicarfax(getCheckShort(ecomCenDatos.getPublicarfax()));
			edicionColegiadoForm.setFax(getValue(ecomCenDatos.getFax()));
			edicionColegiadoForm.setPublicaremail(getCheckShort(ecomCenDatos.getPublicaremail()));
			edicionColegiadoForm.setEmail(getValue(ecomCenDatos.getEmail()));
			edicionColegiadoForm.setIdecomcensosituacionejer(ecomCenDatos.getIdecomcensosituacionejer());
			edicionColegiadoForm.setFechasituacion(getValue(ecomCenDatos.getFechasituacion()));
			edicionColegiadoForm.setResidente(getCheckShort(ecomCenDatos.getResidente()));
			edicionColegiadoForm.setCambioSituacion(ecomCenDatos.getHaycambiosituacion()!=null && ecomCenDatos.getHaycambiosituacion()==1);
			if (edicionColegiadoForm.isCambioSituacion()) {
				edicionColegiadoForm.setIdmotivocambio(ecomCenDatos.getIdmotivosituacion());
			}
			EcomCenDireccion ecomCenDireccion = cenWSService.getEcomCenDireccionesByPk(ecomCenDatos.getIdcensodireccion());
			if (ecomCenDireccion != null) {
				edicionColegiadoForm.setIdcensodireccion(ecomCenDatos.getIdcensodireccion());
				edicionColegiadoForm.setPublicardireccion(getCheckShort(ecomCenDireccion.getPublicar()));
				edicionColegiadoForm.setDesctipovia(getValue(ecomCenDireccion.getDesctipovia()));
				edicionColegiadoForm.setDomicilio(getValue(ecomCenDireccion.getDomicilio()));
				edicionColegiadoForm.setCodigopostal(getValue(ecomCenDireccion.getCodigopostal()));
				if (ecomCenDireccion.getCodigopaisextranj() != null) {
					edicionColegiadoForm.setCodigopaisextranj(getValue(ecomCenDireccion.getCodigopaisextranj()));
				} else {
					edicionColegiadoForm.setCodigopaisextranj(ClsConstants.ID_PAIS_ESPANA);
				}
									
				edicionColegiadoForm.setCodigoprovincia(getValue(ecomCenDireccion.getCodigoprovincia()));
				edicionColegiadoForm.setCodigopoblacion(getValue(ecomCenDireccion.getCodigopoblacion()));
				edicionColegiadoForm.setDescripcionpoblacion(getValue(ecomCenDireccion.getDescripcionpoblacion()));
			} else {
				edicionColegiadoForm.setIdcensodireccion(null);
				edicionColegiadoForm.setPublicardireccion(false);
				edicionColegiadoForm.setDesctipovia(null);
				edicionColegiadoForm.setDomicilio(null);
				edicionColegiadoForm.setCodigopostal(null);
				edicionColegiadoForm.setCodigopaisextranj(null);
									
				edicionColegiadoForm.setCodigoprovincia(null);
				edicionColegiadoForm.setCodigopoblacion(null);
				edicionColegiadoForm.setDescripcionpoblacion(null);
			}
			
			edicionColegiadoForm.setIdestadocolegiado(ecomCenDatos.getIdestadocolegiado());
			
			List<EcomCenMaestroIncidenc> incidencias = cenWSService.getIncidencias(ecomCenDatos.getIdcensodatos(), true);
			
			edicionColegiadoForm.setIncidenciaNumeroColegiadoDuplicado(false);
			edicionColegiadoForm.setIncidenciaPoblacionNoEncontrada(false);
			edicionColegiadoForm.setIncidenciaInscrito(false);
			
			if (incidencias != null && incidencias.size() > 0) {
				for (EcomCenMaestroIncidenc ecomCenMaestroIncidenc : incidencias) {
					if (AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.NUMERO_COLEGIADO_DUPLICADO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()) {
						edicionColegiadoForm.setIncidenciaNumeroColegiadoDuplicado(true);
					} else if (AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.POBLACION_NO_ENCONTRADA.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()) {
						edicionColegiadoForm.setIncidenciaPoblacionNoEncontrada(true);
					}else if(AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.INSCRITO_A_NO_INSCRITO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()){
						edicionColegiadoForm.setIncidenciaInscrito(true);
					}else if (AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.COLEGIADO_NOENCONTRADO_PARA_CAMBIO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias() 
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.MOTIVO_CAMBIO_SITUACION_NULO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.NCOL_APELL_NUMDOC.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.FECHA_ACUERDO_NULA.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.FECHA_INICIO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.FECHA_INICIO_ANTERIOR.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.TEXTO_SANCION_NULO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.TIPO_SANCION_NULO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.TEXTO_MODIFICACION_NULO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.TIPO_MODIFICACION_NULO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()
							|| AppConstants.ECOM_CEN_MAESTRO_INCIDENCIAS.COLEGIADO_NOENCONTRADO_PARA_CAMBIO.getCodigo() == ecomCenMaestroIncidenc.getIdcensomaestroincidencias()) {
						edicionColegiadoForm.setIncidenciaCambioSituacion(true);
					}
				}
			}
			
			
			
			List<EcomCenDatosIncidencias> incidenciasdatos = cenWSService.getIncidenciasCenDatos(ecomCenDatos.getIdcensodatos());
			
			
			//edicionColegiadoForm.setIncidencias(getDescripcionIncidenciasColegiado(incidencias));
			edicionColegiadoForm.setIncidencias(getDescripcionIncidenciasColegiadoCenDatos(incidenciasdatos,incidencias));
			
			CombosCenWS combosCenWS = new CombosCenWS();
			edicionColegiadoForm.setTiposIdentificacion(combosCenWS.getTiposIdentificacion(getUserBean(request)));
			edicionColegiadoForm.setSituacionesEjerciente(combosCenWS.getSituacionesEjeciente(getUserBean(request)));
			edicionColegiadoForm.setSexos(combosCenWS.getSexos(getUserBean(request)));
			edicionColegiadoForm.setMotivosCambioSituacion(combosCenWS.getMotivosCambioSituacion(getUserBean(request)));	
			if (!edicionColegiadoForm.isColegiadoEditable()) {
				if (edicionColegiadoForm.isCambioSituacion()){
					accion = "verCambio";
				}else{
					accion = "ver";
				}
			}
			
			edicionColegiadoForm.setMediador(null);
			if (ecomCenDatos.getMediador() != null) {
				edicionColegiadoForm.setMediador(String.valueOf(ecomCenDatos.getMediador()));
			}
			edicionColegiadoForm.setExentoCuota(null);
			if (ecomCenDatos.getExentocuotas() != null) {
				edicionColegiadoForm.setExentoCuota(String.valueOf(ecomCenDatos.getExentocuotas()));
			}
			
			edicionColegiadoForm.setAccion(accion);
			
			// Entramos al formulario en modo 'modificación'
			//session.setAttribute("accion", accion);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return accion;
	}

	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm) formulario;			
			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<EdicionColegiadoForm> paginador = (PaginadorVector<EdicionColegiadoForm>) databackup.get("paginador");
				List<EdicionColegiadoForm> datos = new ArrayList<EdicionColegiadoForm>();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
						// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {
				CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
				List<EdicionColegiadoForm> edicionColegiadoForms = new ArrayList<EdicionColegiadoForm>();
				List<EcomCenDatos> ecomCenDatos = getDatos(edicionColegiadoForm);	
				if (ecomCenDatos != null) {
					for (EcomCenDatos ecomCenDato : ecomCenDatos) {
						EdicionColegiadoForm ecf = new EdicionColegiadoForm();
						ecf.setIdcensodatos(ecomCenDato.getIdcensodatos());
						ecf.setFechaCambio(GstDate.getFormatedDateLong("ES", ecomCenDato.getFechamodificacion()));						
						ecf.setIncidencias(getDescripcionIncidenciasColegiado(cenWSService.getIncidencias(ecomCenDato.getIdcensodatos(), true)));
						ecf.setIdestadocolegiado(ecomCenDato.getIdestadocolegiado());
						ecf.setCambioSituacion(ecomCenDato.getHaycambiosituacion()!=null && ecomCenDato.getHaycambiosituacion()==1);
						edicionColegiadoForms.add(ecf);
					}
				}
				
				PaginadorVector<EdicionColegiadoForm> paginador = new PaginadorVector(edicionColegiadoForms);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					edicionColegiadoForms = paginador.obtenerPagina(1);
					databackup.put("datos", edicionColegiadoForms);
					request.getSession().setAttribute(DATAPAGINADOR, databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}
			}				
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "resultado";
	}


	private List<String> getDescripcionIncidenciasColegiado(List<EcomCenMaestroIncidenc> incidencias) {
		List<String> descripciones = null;
		if (incidencias != null && incidencias.size() > 0) {
			descripciones = new ArrayList<String>();
			for (EcomCenMaestroIncidenc ecomCenMaestroIncidenc : incidencias) {
				descripciones.add(ecomCenMaestroIncidenc.getDescripcion());
			}
		}
		return descripciones;
	}
	
	private List<String> getDescripcionIncidenciasColegiadoCenDatos(List<EcomCenDatosIncidencias> incidenciasdatos,List<EcomCenMaestroIncidenc> incidencias) {
		List<String> descripciones = new ArrayList<String>();
		
		Map<Short, EcomCenMaestroIncidenc> mapa = new HashMap<Short, EcomCenMaestroIncidenc>();
		
		if (incidencias != null) {
			for (EcomCenMaestroIncidenc ecomCenMaestroIncidenc : incidencias) {
				mapa.put(ecomCenMaestroIncidenc.getIdcensomaestroincidencias(), ecomCenMaestroIncidenc);
			}
		}
		
		if (incidenciasdatos != null && incidenciasdatos.size() > 0) {	
			for (EcomCenDatosIncidencias ecomCenIncidenc : incidenciasdatos) {
				EcomCenMaestroIncidenc ecomCenMaestroIncidenc = mapa.get(ecomCenIncidenc.getIdcensomaestroincidencias());
				if (ecomCenMaestroIncidenc != null) {
					if (ecomCenIncidenc.getDetalleincidencia() != null) {
						descripciones.add(ecomCenIncidenc.getDetalleincidencia());
					} else {
						descripciones.add(ecomCenMaestroIncidenc.getDescripcion());	
					}
				}
			}
		}
		
		return descripciones;
	}
	
	

	private List<EcomCenDatos> getDatos(EdicionColegiadoForm edicionColegiadoForm) {
		CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
		return cenWSService.getHistorico(edicionColegiadoForm.getIdcensodatos());		
	}


	private String getValue(String value) {
		return value!=null?value:"";
	}
		
	
	private String getValue(Date value) throws ClsExceptions {	
		String ret = "";
		if (value != null) {
			ret = GstDate.getFormatedDateShort((Date)value);			
		}		
		return ret;
	}


	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
