package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
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
					}else if ( accion.equalsIgnoreCase("insertaJustificacion")){
						mapDestino = insertaJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("deleteJustificacion")){
						mapDestino = deleteJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("insertaDevolucion")){
						mapDestino = insertaDevolucion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("deleteDevolucion")){
						mapDestino = deleteDevolucion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("validaJustificacion")){
						mapDestino = validaJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("validaDevolucion")){
						mapDestino = validaDevolucion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("descargaErrorValidacionJustificacion")){
						mapDestino = descargaErrorValidacionJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("descargaErrorValidacionDevolucion")){
						mapDestino = descargaErrorValidacionDevolucion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("consultaVistaPreviaJustificacion")){
						mapDestino = consultaVistaPreviaJustificacion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("consultaVistaPreviaDevolucion")){
						mapDestino = consultaVistaPreviaDevolucion (mapping, miForm, request, response);

					}
					
					
					else if ( accion.equalsIgnoreCase("enviaJustificacionGEN")){
						mapDestino = enviaIntercambioGEN (TIPOINTERCAMBIO.Justificaciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("enviaDevolucionGEN")){
						mapDestino = enviaIntercambioGEN (TIPOINTERCAMBIO.Devoluciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("enviaJustificacionCICAC")){
						mapDestino = enviaJustificacionCICAC (mapping, miForm, request, response);
						
					}
					else if ( accion.equalsIgnoreCase("enviaDevolucionCICAC")){
						mapDestino = enviaDevolucionCICAC (mapping, miForm, request, response);
						
					}
					else if ( accion.equalsIgnoreCase("consultaJustificacion")){
//						mapDestino = consultaJustificacion (mapping, miForm, request, response);
						mapDestino = descargaJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("consultaDevolucion")){
//						mapDestino = consultaJustificacion (mapping, miForm, request, response);
						mapDestino = descargaDevolucion(mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("editaJustificacion")){
						mapDestino = editaJustificacion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("finalizaErrores")){
						mapDestino = finalizaErrores (TIPOINTERCAMBIO.Justificaciones, miForm, request);

					}
					else if ( accion.equalsIgnoreCase("descargaJustificacion")){
						mapDestino = descargaJustificacion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("descargaErroresJustificacion")){
						mapDestino = descargaErroresJustificacion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("adjuntaFicheroError")){
						mapDestino = adjuntaFicheroError (TIPOINTERCAMBIO.Justificaciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("insertaErrorGlobal")){
						mapDestino = insertaErrorGlobal  (TIPOINTERCAMBIO.Justificaciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("insertaErrorLineaJustificacion")){
						mapDestino = insertaErrorLinea (TIPOINTERCAMBIO.Justificaciones, miForm, request);

					}


					else if ( accion.equalsIgnoreCase("enviaRespuestaCICAC_ICA")){
						mapDestino = enviaRespuestaCICAC_ICA (TIPOINTERCAMBIO.Justificaciones, miForm, request);
						

					}else if ( accion.equalsIgnoreCase("buscaEdicionJustificacion")){
							mapDestino = buscaEdicionJustificacion (mapping, miForm, request, response);
					}
					
					
					
					else if ( accion.equalsIgnoreCase("editaDevolucion")){
						mapDestino = editaDevolucion (mapping, miForm, request, response);

					}else if ( accion.equalsIgnoreCase("finalizaErroresDevolucion")){
						mapDestino = finalizaErrores (TIPOINTERCAMBIO.Devoluciones, miForm, request);

					}
					else if ( accion.equalsIgnoreCase("descargaDevolucion")){
						mapDestino = descargaDevolucion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("descargaErroresDevolucion")){
						mapDestino = descargaErroresDevolucion (mapping, miForm, request, response);

					}
					else if ( accion.equalsIgnoreCase("adjuntaFicheroErrorDevolucion")){
						mapDestino = adjuntaFicheroError (TIPOINTERCAMBIO.Devoluciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("insertaErrorGlobalDevolucion")){
						mapDestino = insertaErrorGlobal  (TIPOINTERCAMBIO.Devoluciones, miForm, request);

					}else if ( accion.equalsIgnoreCase("insertaErrorLineaDevolucion")){
						mapDestino = insertaErrorLinea (TIPOINTERCAMBIO.Devoluciones, miForm, request);

					}


					else if ( accion.equalsIgnoreCase("enviaRespuestaDevolucionCICAC_ICA")){
						mapDestino = enviaRespuestaCICAC_ICA (TIPOINTERCAMBIO.Devoluciones, miForm, request);
						

					}else if ( accion.equalsIgnoreCase("buscaEdicionDevolucion")){
							mapDestino = buscaEdicionDevolucion (mapping, miForm, request, response);
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
		miForm.setDescripcion(gestionEconomicaCatalunyaForm.getDescripcion());
		miForm.setAnio(gestionEconomicaCatalunyaForm.getAnio());
		miForm.setIdPeriodo(gestionEconomicaCatalunyaForm.getIdPeriodo());
		miForm.setIdFacturacion(gestionEconomicaCatalunyaForm.getIdFacturacion());
		miForm.setIdEstado(gestionEconomicaCatalunyaForm.getIdEstado());
		miForm.setFechaDesde(gestionEconomicaCatalunyaForm.getFechaDesde());
		miForm.setFechaHasta(gestionEconomicaCatalunyaForm.getFechaHasta());


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
	private String getAjaxBusqueda (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		gestionEconomicaForm.setUsrBean(this.getUserBean(request));
		
		
		String origen = request.getParameter("origen");
		//origen sin viene a nulo es justificacion, si viene 1 es devolucion y si viene 2 es Certificaion
		String idInstitucion = request.getParameter("idInstitucion");
		String descripcion = request.getParameter("descripcion");
		String idEstado = request.getParameter("idEstado");
		String idPeriodo = request.getParameter("idPeriodo");
		String idFacturacion = request.getParameter("idFacturacion");
		String anyo = request.getParameter("anio");
		String fechaDesde = request.getParameter("fechaDesde");
		String fechaHasta = request.getParameter("fechaHasta");
		String pagina = request.getParameter("pagina");
		String 	idJustificacion = request.getParameter("idJustificacion");

		String 	idColegio = request.getParameter("idColegio");
		
		
		gestionEconomicaForm.setIdJustificacion(idJustificacion);
		gestionEconomicaForm.setIdInstitucion(idInstitucion);
		gestionEconomicaForm.setDescripcion(descripcion);
		gestionEconomicaForm.setAnio(anyo);
		gestionEconomicaForm.setIdPeriodo(idPeriodo);
		gestionEconomicaForm.setIdFacturacion(idFacturacion);
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
			if(origen==null)
				numRegistrosBusqueda  = gestionEconomicaCatalunyaService.getNumJustificaciones(gestionEconomicaCatalunyaVo);
			else if(origen.equals("1"))
				numRegistrosBusqueda  = gestionEconomicaCatalunyaService.getNumDevoluciones(gestionEconomicaCatalunyaVo);
			else if(origen.equals("2"))
				numRegistrosBusqueda  = gestionEconomicaCatalunyaService.getNumCertificaciones(gestionEconomicaCatalunyaVo);
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
				if(origen==null)
					gestionEconomicaForms =  gestionEconomicaForm.getVo2FormList(gestionEconomicaCatalunyaService.getJustificaciones(gestionEconomicaCatalunyaVo,rowNumStart,rowNumPageSize));
				else if(origen.equals("1"))
					gestionEconomicaForms =  gestionEconomicaForm.getVo2FormList(gestionEconomicaCatalunyaService.getDevoluciones(gestionEconomicaCatalunyaVo,rowNumStart,rowNumPageSize));
				else if(origen.equals("2"))
					gestionEconomicaForms =  gestionEconomicaForm.getVo2FormList(gestionEconomicaCatalunyaService.getCertificaciones(gestionEconomicaCatalunyaVo,rowNumStart,rowNumPageSize));
			}
			else
				gestionEconomicaForms = new ArrayList<GestionEconomicaCatalunyaForm>();
			request.setAttribute("mensajeSuccess", "");
			request.setAttribute("listadoRegistros", gestionEconomicaForms);
			return "listado";
		}catch (Exception e){
			gestionEconomicaForms = new ArrayList<GestionEconomicaCatalunyaForm>();
			request.setAttribute("listadoRegistros", gestionEconomicaForms);
			String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
			request.setAttribute("mensajeSuccess", error);
			throw new SIGAException(error,e);

		}


	}
	private String insertaJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.insertJustificacion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	private String insertaDevolucion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.insertDevolucion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	

	private String deleteJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.deleteJustificacion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.deleted.success",request);
	}
	private String deleteDevolucion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacionVo.setUsuModificacion(Integer.parseInt(usrBean.getUserName()));
			gestionEconomicaCatalunyaService.deleteDevolucion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.deleted.success",request);
	}
	private String validaJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.validaJustificacion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.error.censo.mantenimientoDuplicados.espera",request);
	}
	private String validaDevolucion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.validaDevolucion(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.error.censo.mantenimientoDuplicados.espera",request);
	}
	
	
	
	
	private String descargaJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File log = gestionEconomicaCatalunyaService.getFileJustificacion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	private String descargaDevolucion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File log = gestionEconomicaCatalunyaService.getFileDevolucion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	
	
	private String descargaErroresDevolucion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File log = gestionEconomicaCatalunyaService.getFileErroresDevolucion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
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
			File log = gestionEconomicaCatalunyaService.getFileErroresJustificacion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			//			request.setAttribute("borrarFichero", "false");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	private String descargaErrorValidacionJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File log = gestionEconomicaCatalunyaService.getFileErroresValidacionJustificacion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			request.setAttribute("borrarFichero", "true");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	private String descargaErrorValidacionDevolucion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File log = gestionEconomicaCatalunyaService.getFileErroresValidacionDevolucion(justificacionVo);
			request.setAttribute("nombreFichero", log.getName());
			request.setAttribute("rutaFichero", log.getPath());			
			request.setAttribute("borrarFichero", "true");			



		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}

	private String consultaVistaPreviaJustificacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File vistaPreviaJustificacionFile = gestionEconomicaCatalunyaService.getFileVistaPreviaJustificacion(justificacionVo);
			request.setAttribute("nombreFichero", vistaPreviaJustificacionFile.getName());
			request.setAttribute("rutaFichero", vistaPreviaJustificacionFile.getPath());			
			request.setAttribute("borrarFichero", "false");			

		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}
	private String consultaVistaPreviaDevolucion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			File vistaPreviaJustificacionFile = gestionEconomicaCatalunyaService.getFileVistaPreviaDevolucion(justificacionVo);
			request.setAttribute("nombreFichero", vistaPreviaJustificacionFile.getName());
			request.setAttribute("rutaFichero", vistaPreviaJustificacionFile.getPath());			
			request.setAttribute("borrarFichero", "false");			

		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return "descargaFichero";
	}

	private String insertaErrorGlobal (TIPOINTERCAMBIO tipo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.insertaErrorGlobalCICAC(tipo,justificacionVo.getIdDevolucion()!=null?justificacionVo.getIdDevolucion():justificacionVo.getIdJustificacion(),justificacionVo.getIdInstitucion(),justificacionVo.getError());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	private String insertaErrorLinea(TIPOINTERCAMBIO tipo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			
			
			gestionEconomicaCatalunyaService.insertaErroresLineaJustificacion(tipo,justificacionVo.getIdDevolucion()!=null?justificacionVo.getIdDevolucion():justificacionVo.getIdJustificacion(),justificacionVo.getIdInstitucion(),justificacionVo.getIdLineasJustificacion(),justificacionVo.getError());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}


	
	private String adjuntaFicheroError (TIPOINTERCAMBIO tipo,MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.adjuntaFicheroError(tipo,justificacionVo.getIdJustificacion(),justificacionVo.getFileErrorData());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.inserted.success",request);
	}
	
	
	
	
	private String enviaRespuestaCICAC_ICA (TIPOINTERCAMBIO tipo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaRespuestaCICAC_ICA(tipo,justificacionVo.getIdDevolucion()!=null?justificacionVo.getIdDevolucion():justificacionVo.getIdJustificacion(),justificacionVo.getIdInstitucion());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}

	
	
	private String finalizaErrores (TIPOINTERCAMBIO tipo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.finalizaErrores(tipo,justificacionVo.getIdJustificacion(),justificacionVo.getIdInstitucion());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.error.censo.mantenimientoDuplicados.espera",request);
	}

	private String enviaIntercambioGEN (TIPOINTERCAMBIO tipo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaIntercambioGEN(tipo,justificacionVo.getIdDevolucion()!=null?justificacionVo.getIdDevolucion():justificacionVo.getIdJustificacion(),justificacionVo.getIdInstitucion());
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}
	

	private String enviaJustificacionCICAC (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaJustificacionCICAC(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}
	private String enviaDevolucionCICAC (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacionVo = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			gestionEconomicaCatalunyaService.enviaDevolucionCICAC(justificacionVo);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		return exitoRefresco("messages.envios.procesandoEnvio",request);
	}
	private String editaJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacion = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			
			Date fechaDesde = justificacion.getFechaDesde();
//			TODO Meter filtros en la busqueda de datos de justificaion
			
			System.out.println("fechaDesde"+fechaDesde);
			justificacion =   gestionEconomicaCatalunyaService.getGestionEconomicaCatalunya(justificacion);
			
			request.setAttribute("justificacion",gestionEconomicaForm.getVo2Form(justificacion));


			gestionEconomicaForm.setAccion("editaJustificacion");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");


		return "editaJustificacion";
	}
	private String editaDevolucion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacion = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			
			Date fechaDesde = justificacion.getFechaDesde();
//			TODO Meter filtros en la busqueda de datos de justificaion
			
			System.out.println("fechaDesde"+fechaDesde);
			justificacion =   gestionEconomicaCatalunyaService.getGestionEconomicaCatalunya(justificacion);
			
			request.setAttribute("devolucion",gestionEconomicaForm.getVo2Form(justificacion));


			gestionEconomicaForm.setAccion("editaDevolucion");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");


		return "editaJustificacion";
	}
	
	private String buscaEdicionJustificacion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacion = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacion =   gestionEconomicaCatalunyaService.getGestionEconomicaCatalunya(justificacion);
			request.setAttribute("justificacion",gestionEconomicaForm.getVo2Form(justificacion));
			
			gestionEconomicaForm.setModo("editaJustificacion");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");


		return "editaJustificacion";
	}
	private String buscaEdicionDevolucion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		GestionEconomicaCatalunyaForm gestionEconomicaForm = (GestionEconomicaCatalunyaForm) formulario;
		BusinessManager bm = getBusinessManager();
		GestionEnvioInformacionEconomicaCatalunyaService gestionEconomicaCatalunyaService = (GestionEnvioInformacionEconomicaCatalunyaService) bm.getService(GestionEnvioInformacionEconomicaCatalunyaService.class);
		try {
			GestionEconomicaCatalunyaVo justificacion = gestionEconomicaForm.getForm2Vo(gestionEconomicaForm);
			justificacion =   gestionEconomicaCatalunyaService.getGestionEconomicaCatalunya(justificacion);
			request.setAttribute("devolucion",gestionEconomicaForm.getVo2Form(justificacion));
			
			gestionEconomicaForm.setModo("editaDevolucion");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});

		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");


		return "editaDevolucion";
	}
	
	
	



	


}