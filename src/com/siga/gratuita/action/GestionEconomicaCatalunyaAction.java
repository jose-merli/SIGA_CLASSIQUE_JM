package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.StringHelper;
import org.redabogacia.sigaservices.app.services.scs.GestionEnvioInformacionEconomicaCatalunyaService;
import org.redabogacia.sigaservices.app.services.scs.GestionEnvioInformacionEconomicaCatalunyaService.TIPOINTERCAMBIO;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.scs.GestionEconomicaCatalunyaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.GestionEconomicaCatalunyaForm;

import es.satec.businessManager.BusinessManager;
/***
 * 
 * @author jorgeta 
 * @date   08/03/2018
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class GestionEconomicaCatalunyaAction extends MasterAction {
	private static final Logger log = Logger.getLogger(GestionEconomicaCatalunyaAction.class);
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		GestionEconomicaCatalunyaForm miForm = null;
		try { 
			do {
				miForm = (GestionEconomicaCatalunyaForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					//					System.out.println(" SESSION: " + request.getSession().getAttributeNames()); 
					if(modo!=null)
						accion = modo;

					if (accion == null || accion.equals("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxValidaCertificacion")){
						getAjaxValidaCertificacion (request, response);
						return null;
					}
					
					else if ( accion.equalsIgnoreCase("insertaIntercambios")){
						mapDestino = insertaIntercambios (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("guardar")){
						mapDestino = guardar (mapping, miForm, request, response);

					}
					
					else if ( accion.equalsIgnoreCase("borraIntercambio")){
						mapDestino = borraIntercambio (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("editaIntercambio")){
						mapDestino = editaIntercambio (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("consultaVistaPrevia")){
						mapDestino = consultaVistaPrevia (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("enviaIntercambiosCICAC")){
						mapDestino = enviaIntercambiosCICAC (miForm, request);
						
					}else if ( accion.equalsIgnoreCase("descarga")){
						mapDestino = descarga (mapping, miForm, request, response);
						
					}else if ( accion.equalsIgnoreCase("consulta")){
						mapDestino = consulta(mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("valida")){
						mapDestino = valida (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("adjuntaFicheroError")){
						mapDestino = adjuntaFicheroError (miForm, request);

					}
					else if ( accion.equalsIgnoreCase("descargaErrorValidacion")){
						mapDestino = descargaErrorValidacion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("descargaErrores")){
						mapDestino = descargaErroresJustificacion (mapping, miForm, request, response);

//					}
//					
//					else if ( accion.equalsIgnoreCase("enviarIntercambioGEN")){
//						mapDestino = enviaIntercambioGEN (miForm, request);

					}else if ( accion.equalsIgnoreCase("enviaIntercambiosGEN")){
						mapDestino = enviaIntercambiosGEN (miForm, request);

					}
					else if ( accion.equalsIgnoreCase("finalizaErrores")){
						mapDestino = finalizaErrores (miForm, request);

					}
					else if ( accion.equalsIgnoreCase("insertaErrorGlobal")){
						mapDestino = insertaErrorGlobal  (miForm, request);

					}
					else if ( accion.equalsIgnoreCase("enviaRespuestaCICAC_ICA")){
						mapDestino = enviaRespuestaCICAC_ICA ( miForm, request);
						

					}
					
					
					else if ( accion.equalsIgnoreCase("volver")){
						mapDestino = volver (mapping, miForm, request, response);
					}  else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}

			return mapping.findForward(mapDestino);

		} catch (SIGAException es) {
			throw es;

		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	private String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		GestionEconomicaCatalunyaForm miForm = (GestionEconomicaCatalunyaForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		miForm.setIdEstado("0");
		String propietario = usrBean.getLocation().equals("3001")?"1":"0";
		String paramsEstados = "{\"propietario\":\""+propietario+"\"}";
		request.setAttribute("paramsEstados",paramsEstados);
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().removeAttribute(identificadorFormularioBusqueda);



		request.setAttribute("volverBusqueda","");
		return "inicio";
	}


	private String volver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws  ClsExceptions, SIGAException 
	{
		GestionEconomicaCatalunyaForm miForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		miForm.clear();
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		GestionEconomicaCatalunyaForm gestionEconomicaCatalunyaForm = (GestionEconomicaCatalunyaForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setIdInstitucion(gestionEconomicaCatalunyaForm.getIdInstitucion());
		miForm.setIdColegio(gestionEconomicaCatalunyaForm.getIdColegio());
		miForm.setDescripcion(gestionEconomicaCatalunyaForm.getDescripcion());
		miForm.setAnio(gestionEconomicaCatalunyaForm.getAnio());
		miForm.setIdPeriodo(gestionEconomicaCatalunyaForm.getIdPeriodo());
		miForm.setIdEstado(gestionEconomicaCatalunyaForm.getIdEstado());
		miForm.setFechaDesde(gestionEconomicaCatalunyaForm.getFechaDesde());
		miForm.setFechaHasta(gestionEconomicaCatalunyaForm.getFechaHasta());
		List<String> idPeriodoSelected = new ArrayList<String>();
		idPeriodoSelected.add(miForm.getIdPeriodo());
		request.setAttribute("idPeriodoSelected",idPeriodoSelected);
		List<String> idEstadoSelected = new ArrayList<String>();
		idEstadoSelected.add(miForm.getIdEstado());
		request.setAttribute("idEstadoSelected",idEstadoSelected);
		List<String> idColegioSelected = new ArrayList<String>();
		idColegioSelected.add(miForm.getIdColegio());
		request.setAttribute("idColegioSelected",idColegioSelected);
		
		

		miForm.setIdInstitucion(usrBean.getLocation());
		miForm.setIdEstado("0");
		String propietario = usrBean.getLocation().equals("3001")?"1":"0";
		String paramsEstados = "{\"propietario\":\""+propietario+"\"}";
		//		String[] parametrosComboTipoAsistencia = {this.getUserBean(request).getLocation()};
		request.setAttribute("paramsEstados",paramsEstados);
		request.getSession().removeAttribute(identificadorFormularioBusqueda);


		//        List<String> idTurnoSelected = new ArrayList<String>();
		//        idTurnoSelected.add(miForm.getIdTurno());
		//		request.setAttribute("idTurnoSelected",idTurnoSelected);
		//		




		HashMap<String, String> hashMapPaginador = gestionEconomicaCatalunyaForm.getDatosPaginador();
		Iterator<String> iterador =  hashMapPaginador.keySet().iterator();
		StringBuilder parametros = new StringBuilder();
		while (iterador.hasNext()) {
			String key = (String) iterador.next();
			String elemento =  hashMapPaginador.get(key);
			parametros.append("&");
			parametros.append(key);
			parametros.append("=");
			parametros.append(elemento);

		}


		request.setAttribute("volverBusqueda",parametros.toString());

		return "inicio";

	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException 
	 * @throws JSONException 
	 */
	private void getAjaxValidaCertificacion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = new GestionEconomicaCatalunyaForm();
		gestionEconomicaForm.setUsrBean(this.getUserBean(request));
		
		
		//origen sin viene a nulo es justificacion, si viene 1 es devolucion y si viene 2 es Certificaion
		
		String idPeriodo = request.getParameter("idPeriodo");
		String anyo = request.getParameter("anio");



		gestionEconomicaForm.setAnio(anyo);
		gestionEconomicaForm.setIdPeriodo(idPeriodo);
		
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		
		String listadoColegios = gestionEconomicaCatalunyaService.getListadoColegiosPendientesCertificar(idPeriodo,anyo);
		
		JSONObject json = new JSONObject();
		if(listadoColegios!=null && !listadoColegios.equals("")) {
			
			listadoColegios = StringHelper.getMensajeIdioma(gestionEconomicaForm.getUsrBean().getLanguageInstitucion(),"error.intercambio.cicac")+"\n"+listadoColegios;
		}
		json.put("listadoColegios", listadoColegios);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 	


	}
	private String getAjaxBusqueda (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		gestionEconomicaForm.setUsrBean(this.getUserBean(request));
		
		
		//origen sin viene a nulo es justificacion, si viene 1 es devolucion y si viene 2 es Certificaion
		String idInstitucion = request.getParameter("idInstitucion");
		String descripcion = request.getParameter("descripcion");
		String idEstado = request.getParameter("idEstado");
		String idPeriodo = request.getParameter("idPeriodo");
		String anyo = request.getParameter("anio");
		String fechaDesde = request.getParameter("fechaDesde");
		String fechaHasta = request.getParameter("fechaHasta");
		String pagina = request.getParameter("pagina");
//		String 	idJustificacion = request.getParameter("idJustificacion");

		String 	idColegio = request.getParameter("idColegio");
		
		
//		gestionEconomicaForm.setIdJustificacion(idJustificacion);
		gestionEconomicaForm.setIdInstitucion(idInstitucion);
		gestionEconomicaForm.setDescripcion(descripcion);
		gestionEconomicaForm.setAnio(anyo);
		gestionEconomicaForm.setIdPeriodo(idPeriodo);
		gestionEconomicaForm.setIdEstado(idEstado);
		gestionEconomicaForm.setFechaDesde(fechaDesde);
		gestionEconomicaForm.setFechaHasta(fechaHasta);
		gestionEconomicaForm.setIdColegio(idColegio);



		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		List<GestionEconomicaCatalunyaForm> gestionEconomicaForms = null;
		GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);

		try {
			int rowNumPageSize = 0;
			int page = 1;
			String registrosPorPagina = null;
			Short numRegistrosBusqueda = null;
			GestionEnvioInformacionEconomicaCatalunyaService.TIPOINTERCAMBIO tipo = null;
				numRegistrosBusqueda  = gestionEconomicaCatalunyaService.getNumCabeceraIntercambios(gestionEconomicaCatalunyaVo);
			
			if(pagina ==null || (request.getParameter("totalRegistros")!=null && numRegistrosBusqueda.shortValue()!=Short.valueOf(request.getParameter("totalRegistros")).shortValue())){
				ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				registrosPorPagina = properties.returnProperty("paginador.registrosPorPagina", true);
				rowNumPageSize = Integer.parseInt(registrosPorPagina);

				request.setAttribute("paginaSeleccionada", page);
				request.setAttribute("totalRegistros", numRegistrosBusqueda.toString());
				request.setAttribute("registrosPorPagina", registrosPorPagina);
			}else{
				page = Integer.parseInt(request.getParameter("pagina"));
				request.setAttribute("paginaSeleccionada", page);
				numRegistrosBusqueda = Short.valueOf(request.getParameter("totalRegistros"));
				request.setAttribute("totalRegistros", request.getParameter("totalRegistros"));
				registrosPorPagina = request.getParameter("registrosPorPagina");
				request.setAttribute("registrosPorPagina",registrosPorPagina );
				rowNumPageSize = Integer.parseInt(registrosPorPagina);
			}

			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			HashMap<String, String> paginadorHashMap = new HashMap<String, String>();
			paginadorHashMap.put("pagina", String.valueOf(page));
			paginadorHashMap.put("totalRegistros", numRegistrosBusqueda.toString());
			paginadorHashMap.put("registrosPorPagina", registrosPorPagina);
			gestionEconomicaForm.setDatosPaginador(paginadorHashMap);

			request.getSession().setAttribute(identificadorFormularioBusqueda,gestionEconomicaForm.clone());

			int rowNumStart = ((page - 1) * rowNumPageSize);

			if(numRegistrosBusqueda!=null && numRegistrosBusqueda.shortValue()>0) {
				gestionEconomicaForms =  gestionEconomicaForm.getVo2FormList(gestionEconomicaCatalunyaService.getCabeceraIntercambios(gestionEconomicaCatalunyaVo,rowNumStart,rowNumPageSize));
			
			}
			else
				gestionEconomicaForms = new ArrayList<GestionEconomicaCatalunyaForm>();
			request.setAttribute("mensajeSuccess", "");
			request.setAttribute("listadoRegistros", gestionEconomicaForms);
			return "listadoIntercambios";
		}catch (Exception e){
			gestionEconomicaForms = new ArrayList<GestionEconomicaCatalunyaForm>();
			request.setAttribute("listadoRegistros", gestionEconomicaForms);
			String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
			request.setAttribute("mensajeSuccess", error);
			throw new SIGAException(error,e);

		}


	}
	
	private String guardar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		gestionEconomicaForm.setUsrBean(this.getUserBean(request));
		
		
		


		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		
		GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);

		try {
			gestionEconomicaCatalunyaService.updateIntercambio(gestionEconomicaCatalunyaVo);
		
			return exito("messages.updated.success", request);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
			request.setAttribute("mensajeSuccess", error);
			throw new SIGAException(error,e);

		}


	}
	
	private String insertaIntercambios (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		
		
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
			int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
			if (gestionEconomicaForm.getTheFile() != null && gestionEconomicaForm.getTheFile().getFileSize() > maxSizebytes) {
				StringBuilder error = new StringBuilder();
				error.append(UtilidadesString.getMensajeIdioma(usrBean,"messages.general.file.maxsize"));
				error.replace(error.indexOf("{"), error.indexOf("}")+1, maxsize);
				throw new SIGAException(error.toString());
			}
			
//			log.info("getPathFile"+gestionEconomicaForm.getPathFile());
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			if(usrBean.getLocation().equalsIgnoreCase(""+AppConstants.IDINSTITUCION_CONSEJO_CATALAN)) {
				
				
//				String listadoColegios = gestionEconomicaCatalunyaService.getListadoColegiosPendientesCertificar(gestionEconomicaForm.getIdPeriodo(),gestionEconomicaForm.getAnio());
//				
//				if(listadoColegios!=null && !listadoColegios.equals("")) {
//					
//					listadoColegios = StringHelper.getMensajeIdioma(usrBean.getLanguageInstitucion(),"error.intercambio.cicac")+"\n"+listadoColegios;
//					return errorRefresco(listadoColegios, new ClsExceptions(listadoColegios), request);
//					
//				}
//				
				justificacionVo.setDescripcion(TIPOINTERCAMBIO.CertificacionCICAC.getDescripcio());
				justificacionVo.setIdTipoIntercambio(TIPOINTERCAMBIO.CertificacionCICAC.getId());
//				justificacionVo.setIdTipoCertificacion(GestionEnvioInformacionEconomicaCatalunyaService.ti);
			}
			
//			log.info("getFileErrorData"+justificacionVo.getFileErrorData());
			justificacionVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.insertaIntercambios(justificacionVo);
			
			
		}catch (BusinessException e){
			
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	
	

	private String borraIntercambio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.deleteJustificacion(justificacionVo);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.deleted.success",request);
	}
	
	private String valida (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			
			gestionEconomicaCatalunyaService.valida(justificacionVo);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("facturacion.estadosfac.literal.GenEjecucion",request);
	}
	
	
	
	
	private String descarga (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			justificacionVo.setIdEstado(intercambio.getIdEstado());
			
			File log = gestionEconomicaCatalunyaService.getXmlIntercambio(justificacionVo,false);
			
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
		
		
	}
	private String consulta (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			justificacionVo.setIdEstado((short)30);
			File log = gestionEconomicaCatalunyaService.getFile(justificacionVo,false);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	
	
	
	
	private String descargaErroresJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			justificacionVo.setIdEstado((short)30);
			File log = gestionEconomicaCatalunyaService.getFile(justificacionVo,true);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	private String descargaErrorValidacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			
			File log = gestionEconomicaCatalunyaService.getFileErroresValidacion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	

	private String consultaVistaPrevia(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			justificacionVo.setAnio(intercambio.getAnio());
			justificacionVo.setIdPeriodo(intercambio.getIdPeriodo());
			justificacionVo.setIdInstitucion(intercambio.getIdInstitucion());
			File vistaPreviaJustificacionFile = gestionEconomicaCatalunyaService.getFileVistaPrevia(justificacionVo);
			request.setAttribute("nombreFichero", vistaPreviaJustificacionFile.getName());
			request.setAttribute("rutaFichero", vistaPreviaJustificacionFile.getPath());			
			request.setAttribute("borrarFichero", "false");			

		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	

	private String insertaErrorGlobal (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
//			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			gestionEconomicaCatalunyaService.insertaErrorGlobalCICAC(justificacionVo,justificacionVo.getError());
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	

	
	private String adjuntaFicheroError (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			intercambio.setIdJustificacion(justificacionVo.getIdJustificacion());
			gestionEconomicaCatalunyaService.adjuntaFicheroError(intercambio, justificacionVo.getFileErrorData());
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	
	
	
	
	private String enviaRespuestaCICAC_ICA (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			gestionEconomicaCatalunyaService.enviaRespuestaCICAC_ICA(intercambio);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}

	
	
	private String finalizaErrores (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(justificacionVo.getIdIntercambio());
			gestionEconomicaCatalunyaService.finalizaErrores(intercambio);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}
	private String enviaIntercambiosGEN (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaIntercambiosGEN(gestionEconomicaCatalunyaVo);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}
//	private String enviaIntercambioGEN (MasterForm formulario, HttpServletRequest request) throws SIGAException {
//		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
//		BusinessManager bm = getBusinessManager();
//		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
//		try {
//			GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
//			gestionEconomicaCatalunyaService.enviaIntercambioGEN(gestionEconomicaCatalunyaVo);
//		}catch (BusinessException e){
//			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
//		}
//		catch (Exception e){
//			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
//
//		}
//		return exitoRefresco("messages.envios.procesandoEnvio",request);
//	}
	private String enviaIntercambiosCICAC (MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaIntercambiosCICAC(gestionEconomicaCatalunyaVo);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(), new ClsExceptions(e.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}

	
	
	private String editaIntercambio (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		gestionEconomicaForm.setUsrBean(usrBean);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			
			GestionEconomicaCatalunyaVo gestionEconomicaCatalunyaVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			
			GestionEconomicaCatalunyaVo intercambio = gestionEconomicaCatalunyaService.getCabeceraIntercambio(gestionEconomicaCatalunyaVo.getIdIntercambio());
			gestionEconomicaForm.setIdEstado(intercambio.getIdEstado().toString());
			gestionEconomicaForm.setDescripcionEstado(intercambio.getDescripcionEstado());
			gestionEconomicaForm.setIdInstitucion(usrBean.getLocation());
			List<GestionEconomicaCatalunyaVo> listadoDesgloseIntercambios =   gestionEconomicaCatalunyaService.getIntercambios(intercambio);
			
			request.setAttribute("idEstadoIntercambio",intercambio.getIdEstado().toString());
			
			
			request.setAttribute("listadoDesgloseIntercambios",gestionEconomicaForm.getVo2FormList(listadoDesgloseIntercambios));

			request.setAttribute("intercambio",intercambio);

			gestionEconomicaForm.setAccion("editaIntercambio");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");


		return "editaIntercambio";
	}

	
	



	


}