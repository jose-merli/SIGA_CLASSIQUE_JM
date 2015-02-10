package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.ScsSolicitudesAcpetadasService;
import org.redabogacia.sigaservices.app.vo.scs.SolicitudAceptadaCentralitaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.SolicitudAceptadaCentralitaForm;
import com.siga.gratuita.form.service.SolicitudAceptadaCentralitaVoService;

import es.satec.businessManager.BusinessManager;
/***
 * 
 * @author jorgeta 
 * @date   28/11/2014
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class GestionSolicitudesAceptadasCentralitaAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		SolicitudAceptadaCentralitaForm miForm = null;
		try { 
			do {
				miForm = (SolicitudAceptadaCentralitaForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
//					System.out.println(" SESSION: " + request.getSession().getAttributeNames()); 
					if(modo!=null)
						accion = modo;
					
					if (accion == null || accion.equals("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getColegiadoAjax")){					
						getColegiadoAjax(mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editarSolicitudAceptada")||accion.equalsIgnoreCase("validarSolicitudAceptada")){
						mapDestino = editarSolicitudAceptada (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("guardarSolicitudAceptada")){
						mapDestino = guardarSolicitudAceptada (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("denegarSolicitudAceptada")||accion.equalsIgnoreCase("denegarSolicitudAceptadaVolver")){
						mapDestino = denegarSolicitudAceptada (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("activarSolicitudAceptadaDenegada")|| accion.equalsIgnoreCase("activarSolicitudAceptadaDenegadaVolver")){
						mapDestino = activarSolicitudAceptadaDenegada (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("guardarSolicitudesAceptadas")){
						mapDestino = guardarSolicitudesAceptadas (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("denegarSolicitudesAceptadas")){
						mapDestino = denegarSolicitudesAceptadas (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("activarSolicitudesAceptadasDenegadas")){
						mapDestino = activarSolicitudesAceptadasDenegadas (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("volver")){
						mapDestino = volver (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultarSolicitudAceptada")){
						mapDestino = consultarSolicitudAceptada (mapping, miForm, request, response);
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
		SolicitudAceptadaCentralitaForm miForm = (SolicitudAceptadaCentralitaForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		miForm.setIdEstado("0");
		String[] parametrosComboTipoAsistencia = {this.getUserBean(request).getLocation()};
		request.setAttribute("parametrosComboTipoAsistencia",parametrosComboTipoAsistencia);
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().removeAttribute(identificadorFormularioBusqueda);
		
		return "inicio";
	}
	private String volver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		SolicitudAceptadaCentralitaForm miForm = (SolicitudAceptadaCentralitaForm) formulario;
		miForm.clear();
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setIdInstitucion(solicitudAceptadaCentralitaForm.getIdInstitucion());
        miForm.setIdLlamada(solicitudAceptadaCentralitaForm.getIdLlamada());
        miForm.setIdEstado(solicitudAceptadaCentralitaForm.getIdEstado());
        miForm.setIdTurno(solicitudAceptadaCentralitaForm.getIdTurno());
        miForm.setIdGuardia(solicitudAceptadaCentralitaForm.getIdGuardia());
        miForm.setFechaDesde(solicitudAceptadaCentralitaForm.getFechaDesde());
        miForm.setFechaHasta(solicitudAceptadaCentralitaForm.getFechaHasta());
        miForm.setIdComisaria(solicitudAceptadaCentralitaForm.getIdComisaria());
        miForm.setIdJuzgado(solicitudAceptadaCentralitaForm.getIdJuzgado());
        miForm.setIdPersona(solicitudAceptadaCentralitaForm.getIdPersona());
        miForm.setColegiadoNumero(solicitudAceptadaCentralitaForm.getColegiadoNumero());
		
        List<String> idTurnoSelected = new ArrayList<String>();
        idTurnoSelected.add(miForm.getIdTurno());
		request.setAttribute("idTurnoSelected",idTurnoSelected);
		List<String> idGuardiaSelected = new ArrayList<String>();
		idGuardiaSelected.add(miForm.getIdGuardia());
		request.setAttribute("idGuardiaSelected",idGuardiaSelected);
		request.setAttribute("paramsGuardiasDeTurno",miForm.getIdTurno());
		
		List<String> idComisariaSelected = new ArrayList<String>();
		idComisariaSelected.add(miForm.getIdComisaria());
		request.setAttribute("idComisariaSelected",idComisariaSelected);
		
		List<String> idJuzgadoSelected = new ArrayList<String>();
		idJuzgadoSelected.add(miForm.getIdJuzgado());
		request.setAttribute("idJuzgadoSelected",idJuzgadoSelected);
        
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
	 */
	private String getAjaxBusqueda (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
			String idInstitucion = request.getParameter("idInstitucion");
			String idLlamada = request.getParameter("idLlamada");
			String idEstado = request.getParameter("idEstado");
			String idTurno = request.getParameter("idTurno");
	        String idGuardia = request.getParameter("idGuardia");
	        String fechaDesde = request.getParameter("fechaDesde");
	        String fechaHasta = request.getParameter("fechaHasta");
	        String idComisaria = request.getParameter("idComisaria");
	        String idJuzgado = request.getParameter("idJuzgado");
	        String idPersona = request.getParameter("idPersona");
	        String colegiadoNumero = request.getParameter("colegiadoNumero");
	        
	        solicitudAceptadaCentralitaForm.setIdInstitucion(idInstitucion);
	        solicitudAceptadaCentralitaForm.setIdLlamada(idLlamada);
	        solicitudAceptadaCentralitaForm.setIdEstado(idEstado);
	        solicitudAceptadaCentralitaForm.setIdTurno(idTurno);
	        solicitudAceptadaCentralitaForm.setIdGuardia(idGuardia);
	        solicitudAceptadaCentralitaForm.setFechaDesde(fechaDesde);
	        solicitudAceptadaCentralitaForm.setFechaHasta(fechaHasta);
	        solicitudAceptadaCentralitaForm.setIdComisaria(idComisaria);
	        solicitudAceptadaCentralitaForm.setIdJuzgado(idJuzgado);
	        solicitudAceptadaCentralitaForm.setIdPersona(idPersona);
	        solicitudAceptadaCentralitaForm.setColegiadoNumero(colegiadoNumero);
	        
	        
	        String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			request.getSession().setAttribute(identificadorFormularioBusqueda,solicitudAceptadaCentralitaForm.clone());
	        
	        
	        BusinessManager bm = getBusinessManager();
			ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
			VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
			List<SolicitudAceptadaCentralitaForm> solicitudesAceptadasForms = null;
			try {
				solicitudesAceptadasForms =  voService.getVo2FormList(scsSolicitudesAcpetadasService.getList(voService.getForm2Vo(solicitudAceptadaCentralitaForm)));
				request.setAttribute("solicitudesAceptadasCentralita", solicitudesAceptadasForms);
				return "listado";
			}catch (Exception e){
				solicitudesAceptadasForms = new ArrayList<SolicitudAceptadaCentralitaForm>();
				request.setAttribute("solicitudesAceptadasCentralita", solicitudesAceptadasForms);
				String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
				throw new SIGAException(error,e);
				
			}
							

		}
		
	  
	private String editarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVo =   scsSolicitudesAcpetadasService.getSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm));
			solicitudAceptadaCentralitaForm.clear();
			solicitudAceptadaCentralitaForm  = voService.getVo2Form(solicitudAceptadaCentralitaVo,solicitudAceptadaCentralitaForm);
			
			String idTurnoLlamada = "-1";
	        if(solicitudAceptadaCentralitaForm.getIdTurno()!=null && !solicitudAceptadaCentralitaForm.getIdTurno().equals("")){
	        	idTurnoLlamada = solicitudAceptadaCentralitaForm.getIdTurno();
			}
	        Map<String, String> fechaGuardiaJsonMap = new HashMap<String, String>();
	        fechaGuardiaJsonMap.put("fechaGuardia", solicitudAceptadaCentralitaForm.getFechaGuardia());
	        request.setAttribute("paramsTurnos",UtilidadesString.createJsonString(fechaGuardiaJsonMap));
	        
	        List<String> idTurnoSelectedList = new ArrayList<String>();
	        Map<String, String> turnoJsonMap = new HashMap<String, String>();
	        turnoJsonMap.put("idinstitucion", solicitudAceptadaCentralitaForm.getIdInstitucion());
	        turnoJsonMap.put("idturno", idTurnoLlamada);
			String idTurnoJsonSelected = UtilidadesString.createJsonString(turnoJsonMap);
			idTurnoSelectedList.add(idTurnoJsonSelected);
			request.setAttribute("idTurnoSelected",idTurnoSelectedList);
			turnoJsonMap.put("fechaGuardia", solicitudAceptadaCentralitaForm.getFechaGuardia());
			request.setAttribute("paramsGuardiasDeTurno",UtilidadesString.createJsonString(turnoJsonMap));
			
			List<String> idGuardiaSelected = new ArrayList<String>();
			idGuardiaSelected.add(solicitudAceptadaCentralitaForm.getIdGuardia());
			request.setAttribute("idGuardiaSelected",idGuardiaSelected);
			
			
			
			Map<String, String> juzgadoJsonMap = new HashMap<String, String>();
			if(solicitudAceptadaCentralitaForm.getIdJuzgado()!=null){
				juzgadoJsonMap.put("idjuzgado", solicitudAceptadaCentralitaForm.getIdJuzgado().split(",")[0]);
				juzgadoJsonMap.put("idinstitucion", solicitudAceptadaCentralitaForm.getIdInstitucion());
			}else{
				juzgadoJsonMap.put("idjuzgado", "-1");
				juzgadoJsonMap.put("idinstitucion", solicitudAceptadaCentralitaForm.getIdInstitucion());
				
			}
			String paramJuzgadosTurnos = UtilidadesString.createJsonString(juzgadoJsonMap);
	        request.setAttribute("paramJuzgadosTurnos",paramJuzgadosTurnos);
			
			
			
			List<String> idComisariaSelected = new ArrayList<String>();
			idComisariaSelected.add(solicitudAceptadaCentralitaForm.getIdComisaria());
			request.setAttribute("idComisariaSelected",idComisariaSelected);
			
			List<String> idJuzgadoSelected = new ArrayList<String>();
			idJuzgadoSelected.add(paramJuzgadosTurnos);
			request.setAttribute("idJuzgadoSelected",idJuzgadoSelected);
			
			
			
			List<String> idTipoAsitenciaSelected = new ArrayList<String>();
			idTipoAsitenciaSelected.add(scsSolicitudesAcpetadasService.getTipoAsistenciaColegioDefecto(solicitudAceptadaCentralitaVo.getIdTipoGuardia()!=null?Short.parseShort(solicitudAceptadaCentralitaVo.getIdTipoGuardia()):null,solicitudAceptadaCentralitaVo.getIdinstitucion()));
			
			request.setAttribute("idTipoAsitenciaSelected",idTipoAsitenciaSelected);
			
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("idTurno", idTurnoLlamada);
			jsonMap.put("idGuardia", solicitudAceptadaCentralitaForm.getIdGuardia());
			jsonMap.put("fechaGuardia", solicitudAceptadaCentralitaForm.getFechaGuardia());
			
			request.setAttribute("parametrosComboColegiadosGuardia",UtilidadesString.createJsonString(jsonMap));
			
			List<String> idColegiadoGuardiaSelected = new ArrayList<String>();
			idColegiadoGuardiaSelected.add(solicitudAceptadaCentralitaForm.getIdPersona());
			request.setAttribute("idColegiadoGuardiaSelected",idColegiadoGuardiaSelected);
			
			solicitudAceptadaCentralitaForm.setModo("editarSolicitudAceptada");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		return "validarSolicitudAceptada";

	}
	private String denegarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			scsSolicitudesAcpetadasService.denegarSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm));
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
			
		}
		if(solicitudAceptadaCentralitaForm.getModo().equals("denegarSolicitudAceptada"))
			return exitoRefresco("messages.updated.success", request);
		else{
			request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
			formulario.setModo("consultarSolicitudAceptada");
			return consultarSolicitudAceptada(mapping, formulario, request, response);
		}
			
						
	}
	private String activarSolicitudAceptadaDenegada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			scsSolicitudesAcpetadasService.activarSolicitudAceptadaDenegada(voService.getForm2Vo(solicitudAceptadaCentralitaForm));
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		if(solicitudAceptadaCentralitaForm.getModo().equals("activarSolicitudAceptadaDenegada"))
			return exitoRefresco("messages.updated.success", request);
		else{
			request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
			formulario.setModo("consultarSolicitudAceptada");
			return consultarSolicitudAceptada(mapping, formulario, request, response);
		}
		
						

	}
	private String guardarSolicitudesAceptadas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		BusinessManager bm = getBusinessManager();
        ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
        VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		String datosMasivos = solicitudAceptadaCentralitaForm.getDatosMasivos();
		String[] registrosSolicitudAceptada = datosMasivos.split(",");
		List<SolicitudAceptadaCentralitaVo> solicitudAceptadaCentralitaList = new ArrayList<SolicitudAceptadaCentralitaVo>();
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm2 = null;
		for (String registroSolicitudAceptada : registrosSolicitudAceptada) {
			String datosSolicitudAceptada[]= registroSolicitudAceptada.split("##");
			String idInstitucion = datosSolicitudAceptada[0];
			String idSolicitudAceptada = datosSolicitudAceptada[1];
			String idLlamada = datosSolicitudAceptada[2];
			String fechaLlamada = datosSolicitudAceptada[3];
			solicitudAceptadaCentralitaForm2= new SolicitudAceptadaCentralitaForm();
			solicitudAceptadaCentralitaForm2.setIdInstitucion(idInstitucion);
			solicitudAceptadaCentralitaForm2.setIdSolicitudAceptada(idSolicitudAceptada);
			solicitudAceptadaCentralitaForm2.setIdLlamada(idLlamada);
			solicitudAceptadaCentralitaForm2.setFechaLlamadaHoras(fechaLlamada);
			solicitudAceptadaCentralitaList.add(voService.getForm2Vo(solicitudAceptadaCentralitaForm2));
			 
		}
		
		try {
			
			scsSolicitudesAcpetadasService.guardarSolicitudesAceptadas(solicitudAceptadaCentralitaList);
		}catch (BusinessException e){
			StringBuffer error = new StringBuffer(e.getMessage());
			error = error.insert(0, UtilidadesString.getMensajeIdioma(this.getUserBean(request), "sjcs.solicitudaceptadacentralita.aviso.datosErroneos"));
					
			return errorRefresco(error.toString(), new ClsExceptions(error.toString()), request);
			
			
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		
		return exitoRefresco("messages.updated.success", request);
						

	}
	
	private String guardarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		BusinessManager bm = getBusinessManager();
        ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
        VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			
			scsSolicitudesAcpetadasService.guardarSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm));
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
		return volver(mapping, formulario, request, response);
						

	}
	
	private String denegarSolicitudesAceptadas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		BusinessManager bm = getBusinessManager();
        ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
        VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		String datosMasivos = solicitudAceptadaCentralitaForm.getDatosMasivos();
		String[] registrosSolicitudAceptada = datosMasivos.split(",");
		List<SolicitudAceptadaCentralitaVo> solicitudAceptadaCentralitaList = new ArrayList<SolicitudAceptadaCentralitaVo>();
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm2 = null;
		for (String registroSolicitudAceptada : registrosSolicitudAceptada) {
			String datosSolicitudAceptada[]= registroSolicitudAceptada.split("##");
			String idInstitucion = datosSolicitudAceptada[0];
			String idSolicitudAceptada = datosSolicitudAceptada[1];
			solicitudAceptadaCentralitaForm2= new SolicitudAceptadaCentralitaForm();
			solicitudAceptadaCentralitaForm2.setIdInstitucion(idInstitucion);
			solicitudAceptadaCentralitaForm2.setIdSolicitudAceptada(idSolicitudAceptada);
			solicitudAceptadaCentralitaList.add(voService.getForm2Vo(solicitudAceptadaCentralitaForm2));
			 
		}
		
		try {
			
			scsSolicitudesAcpetadasService.denegarSolicitudAceptada(solicitudAceptadaCentralitaList);
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		return exitoRefresco("messages.updated.success", request);
						

	}
	private String activarSolicitudesAceptadasDenegadas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		BusinessManager bm = getBusinessManager();
        ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
        VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		String datosMasivos = solicitudAceptadaCentralitaForm.getDatosMasivos();
		String[] registrosSolicitudAceptada = datosMasivos.split(",");
		List<SolicitudAceptadaCentralitaVo> solicitudAceptadaCentralitaList = new ArrayList<SolicitudAceptadaCentralitaVo>();
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm2 = null;
		for (String registroSolicitudAceptada : registrosSolicitudAceptada) {
			String datosSolicitudAceptada[]= registroSolicitudAceptada.split("##");
			String idInstitucion = datosSolicitudAceptada[0];
			String idSolicitudAceptada = datosSolicitudAceptada[1];
			solicitudAceptadaCentralitaForm2= new SolicitudAceptadaCentralitaForm();
			solicitudAceptadaCentralitaForm2.setIdInstitucion(idInstitucion);
			solicitudAceptadaCentralitaForm2.setIdSolicitudAceptada(idSolicitudAceptada);
			solicitudAceptadaCentralitaList.add(voService.getForm2Vo(solicitudAceptadaCentralitaForm2));
			 
		}
		
		try {
			
			scsSolicitudesAcpetadasService.activarSolicitudAceptadaDenegada(solicitudAceptadaCentralitaList);
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		return exitoRefresco("messages.updated.success", request);
						

	}
	private String consultarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVo =   scsSolicitudesAcpetadasService.getSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm));
			solicitudAceptadaCentralitaForm.clear();
			solicitudAceptadaCentralitaForm  = voService.getVo2Form(solicitudAceptadaCentralitaVo,solicitudAceptadaCentralitaForm);
			
			
			
			Map<String, String> clavesSolicitudJsonMap = new HashMap<String, String>();
			clavesSolicitudJsonMap.put("nombreformulario", "SolicitudAceptadaCentralitaForm");
			clavesSolicitudJsonMap.put("idinstitucion", solicitudAceptadaCentralitaForm.getIdInstitucion());
			clavesSolicitudJsonMap.put("idsolicitudaceptada", solicitudAceptadaCentralitaForm.getIdSolicitudAceptada());
			String idSolicitudConsultada = UtilidadesString.createJsonString(clavesSolicitudJsonMap);
			solicitudAceptadaCentralitaForm.setJsonVolver(idSolicitudConsultada);
			
			switch (Integer.parseInt(solicitudAceptadaCentralitaForm.getIdEstado())) {
				case 0:
					
					break;
				case 1:
					List<String> idTipoAsitenciaSelected = new ArrayList<String>();
					idTipoAsitenciaSelected.add(scsSolicitudesAcpetadasService.getTipoAsistenciaColegioDefecto(solicitudAceptadaCentralitaVo.getIdTipoGuardia()!=null?Short.parseShort(solicitudAceptadaCentralitaVo.getIdTipoGuardia()):null,solicitudAceptadaCentralitaVo.getIdinstitucion()));
					request.setAttribute("idTipoAsitenciaSelected",idTipoAsitenciaSelected);
					
					List<SolicitudAceptadaCentralitaForm> solicitudesAceptadasForms = null;

					solicitudesAceptadasForms =  voService.getVo2FormList(scsSolicitudesAcpetadasService.getAsistenciasSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm)));
					request.setAttribute("solicitudesAceptadasCentralita", solicitudesAceptadasForms);
					break;
				case 2:
					
					break;
	

				}
			
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.consulta.titulo");
		return "consultarSolicitudAceptada";
		

	}

	
	
	
}