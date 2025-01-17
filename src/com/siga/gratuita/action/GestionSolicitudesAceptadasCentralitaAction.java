package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.ScsComisaria;
import org.redabogacia.sigaservices.app.autogen.model.ScsComisariaExample;
import org.redabogacia.sigaservices.app.autogen.model.ScsJuzgado;
import org.redabogacia.sigaservices.app.autogen.model.ScsJuzgadoExample;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.ScsComisariaService;
import org.redabogacia.sigaservices.app.services.scs.ScsJuzgadoService;
import org.redabogacia.sigaservices.app.services.scs.ScsSolicitudesAcpetadasService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
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
 * La imaginaci�n es m�s importante que el conocimiento
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

		actualizarDatosFicha(mapping.getPath(),miForm,  request);
		
		String accessType = testAccess(request.getContextPath() + "/JGR_GestionSolicitudesAceptadasCentralita.do", null, request);
		request.setAttribute("accessType",accessType);		
		request.setAttribute("volverBusqueda","");
		return "inicio";
	}
	private void actualizarDatosFicha(String pathAccion,SolicitudAceptadaCentralitaForm miForm,HttpServletRequest request){
		if(pathAccion.equals("/JGR_PreAsistenciasLetrado")){
			UsrBean usrBean = this.getUserBean(request);
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			miForm.setIdPersona(idPersona);
			StringBuilder descripcioncolegiado = new StringBuilder();
			String nombrePestanha=null, numeroPestanha=null, estadoColegial=null;
			try {
				Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
				nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
				numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
				estadoColegial = (String)datosColegiado.get("ESTADOCOLEGIAL");
				descripcioncolegiado.append(nombrePestanha);
				descripcioncolegiado.append(" ");
				descripcioncolegiado.append(UtilidadesString.getMensajeIdioma(usrBean, "censo.fichaCliente.literal.colegiado"));
				descripcioncolegiado.append(" ");
				descripcioncolegiado.append(numeroPestanha);
				descripcioncolegiado.append(" ");
				descripcioncolegiado.append("(");
				if(estadoColegial!=null && !estadoColegial.equals("")){
					descripcioncolegiado.append(estadoColegial);
				}else{
					
					descripcioncolegiado.append(UtilidadesString.getMensajeIdioma(usrBean, "censo.busquedaClientes.literal.sinEstadoColegial"));
					
				}
				descripcioncolegiado.append(")");
				miForm.setDescripcionColegiado(descripcioncolegiado.toString());
				miForm.setColegiadoNombre(nombrePestanha);
				miForm.setColegiadoNumero(numeroPestanha);
				
			} catch (Exception e){
				nombrePestanha = "";
				numeroPestanha = "";
				miForm.setDescripcionColegiado("");
			}
			
		}
		request.setAttribute("fichaColegial",pathAccion.equals("/JGR_PreAsistenciasLetrado")?AppConstants.DB_TRUE:AppConstants.DB_FALSE);
		
	}
	
	private String volver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws  ClsExceptions, SIGAException 
			{
		SolicitudAceptadaCentralitaForm miForm = (SolicitudAceptadaCentralitaForm) formulario;
		miForm.clear();
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setIdInstitucion(solicitudAceptadaCentralitaForm.getIdInstitucion());
        miForm.setIdLlamada(solicitudAceptadaCentralitaForm.getIdLlamada());
        miForm.setNumAvisoCV(solicitudAceptadaCentralitaForm.getNumAvisoCV());
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
		
		
		actualizarDatosFicha(mapping.getPath(),miForm,  request);
		String accessType = testAccess(request.getContextPath() + "/JGR_GestionSolicitudesAceptadasCentralita.do", null, request);
		request.setAttribute("accessType",accessType);		
		
		HashMap<String, String> hashMapPaginador = solicitudAceptadaCentralitaForm.getDatosPaginador();
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
			SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
			String idInstitucion = request.getParameter("idInstitucion");
			String numAvisoCV = request.getParameter("numAvisoCV");
			String idEstado = request.getParameter("idEstado");
			String idTurno = request.getParameter("idTurno");
	        String idGuardia = request.getParameter("idGuardia");
	        String fechaDesde = request.getParameter("fechaDesde");
	        String fechaHasta = request.getParameter("fechaHasta");
	        String idComisaria = request.getParameter("idComisaria");
	        String idJuzgado = request.getParameter("idJuzgado");
	        String idPersona = request.getParameter("idPersona");
	        String colegiadoNumero = request.getParameter("colegiadoNumero");
	        
	        String pagina = request.getParameter("pagina");
	        
	        solicitudAceptadaCentralitaForm.setIdInstitucion(idInstitucion);
	        solicitudAceptadaCentralitaForm.setNumAvisoCV(numAvisoCV);
	        solicitudAceptadaCentralitaForm.setIdEstado(idEstado);
	        solicitudAceptadaCentralitaForm.setIdTurno(idTurno);
	        solicitudAceptadaCentralitaForm.setIdGuardia(idGuardia);
	        solicitudAceptadaCentralitaForm.setFechaDesde(fechaDesde);
	        solicitudAceptadaCentralitaForm.setFechaHasta(fechaHasta);
	        solicitudAceptadaCentralitaForm.setIdComisaria(idComisaria);
	        solicitudAceptadaCentralitaForm.setIdJuzgado(idJuzgado);
	        solicitudAceptadaCentralitaForm.setIdPersona(idPersona);
	        solicitudAceptadaCentralitaForm.setColegiadoNumero(colegiadoNumero);
	        
	        
	        BusinessManager bm = getBusinessManager();
			ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
			VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
			List<SolicitudAceptadaCentralitaForm> solicitudesAceptadasForms = null;
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVoForm = voService.getForm2Vo(solicitudAceptadaCentralitaForm);
			solicitudAceptadaCentralitaVoForm.setIdioma(this.getUserBean(request).getLanguage());
			
			try {
				int rowNumPageSize = 0;
				int page = 1;
				String registrosPorPagina = null;
				Short numSolicitudAceptadas  = scsSolicitudesAcpetadasService.getNumSolicitudesAceptadas(solicitudAceptadaCentralitaVoForm);
				if(pagina ==null || (request.getParameter("totalRegistros")!=null && numSolicitudAceptadas.shortValue()!=Short.valueOf(request.getParameter("totalRegistros")).shortValue())){
					ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
					registrosPorPagina = properties.returnProperty("paginador.registrosPorPagina", true);
					rowNumPageSize = Integer.parseInt(registrosPorPagina);
					
					request.setAttribute("paginaSeleccionada", page);
					request.setAttribute("totalRegistros", numSolicitudAceptadas.toString());
					request.setAttribute("registrosPorPagina", registrosPorPagina);
				}else{
					page = Integer.parseInt(request.getParameter("pagina"));
					request.setAttribute("paginaSeleccionada", page);
					numSolicitudAceptadas = Short.valueOf(request.getParameter("totalRegistros"));
					request.setAttribute("totalRegistros", request.getParameter("totalRegistros"));
					registrosPorPagina = request.getParameter("registrosPorPagina");
					request.setAttribute("registrosPorPagina",registrosPorPagina );
					rowNumPageSize = Integer.parseInt(registrosPorPagina);
				}
				
				String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
				HashMap<String, String> paginadorHashMap = new HashMap<String, String>();
				paginadorHashMap.put("pagina", String.valueOf(page));
				paginadorHashMap.put("totalRegistros", numSolicitudAceptadas.toString());
				paginadorHashMap.put("registrosPorPagina", registrosPorPagina);
				solicitudAceptadaCentralitaForm.setDatosPaginador(paginadorHashMap);
				
				request.getSession().setAttribute(identificadorFormularioBusqueda,solicitudAceptadaCentralitaForm.clone());
				
				int rowNumStart = ((page - 1) * rowNumPageSize);
				
				
				solicitudesAceptadasForms =  voService.getVo2FormList(scsSolicitudesAcpetadasService.getList(solicitudAceptadaCentralitaVoForm,rowNumStart,rowNumPageSize));	
				request.setAttribute("mensajeSuccess", "");
				request.setAttribute("solicitudesAceptadasCentralita", solicitudesAceptadasForms);
				return "listado";
			}catch (Exception e){
				solicitudesAceptadasForms = new ArrayList<SolicitudAceptadaCentralitaForm>();
				request.setAttribute("solicitudesAceptadasCentralita", solicitudesAceptadasForms);
				String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
				request.setAttribute("mensajeSuccess", error);
				throw new SIGAException(error,e);
				
			}
							

		}
	
	
	
	  
	private String editarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			
			
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVoForm = voService.getForm2Vo(solicitudAceptadaCentralitaForm);
			solicitudAceptadaCentralitaVoForm.setIdioma(this.getUserBean(request).getLanguage());
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVo =   scsSolicitudesAcpetadasService.getSolicitudAceptada(solicitudAceptadaCentralitaVoForm);
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
			
			Long idSolictanteCentralita= null;
			if(solicitudAceptadaCentralitaVo.getIdcentrodetencion()!=null){
				switch (Short.valueOf(solicitudAceptadaCentralitaVo.getIdtipocentrodetencion())) {
					case AppConstants.LUGARACTUACION_JUZGADO:
						ScsJuzgadoService juzgadoService = (ScsJuzgadoService) BusinessManager.getInstance().getService(ScsJuzgadoService.class);
						ScsJuzgadoExample juzgadoExample = new ScsJuzgadoExample();
						juzgadoExample.createCriteria().andIdinstitucionEqualTo(solicitudAceptadaCentralitaVo.getIdinstitucion()).andCodigoextEqualTo(solicitudAceptadaCentralitaVo.getIdcentrodetencion());
						List<ScsJuzgado> list =  juzgadoService.getList(juzgadoExample);
						if(list!=null && list.size()>0){
							idSolictanteCentralita = list.get(0).getIdjuzgado();
							
						}
						if(idSolictanteCentralita!=null)
							solicitudAceptadaCentralitaForm.setIdJuzgado(idSolictanteCentralita+","+solicitudAceptadaCentralitaVo.getIdinstitucion());
						else
							solicitudAceptadaCentralitaForm.setIdJuzgado(null);
					break;
					case AppConstants.LUGARACTUACION_CENTRODETENCION:
						ScsComisariaService comisariaService = (ScsComisariaService) BusinessManager.getInstance().getService(ScsComisariaService.class);
						ScsComisariaExample comisariaExample = new ScsComisariaExample();
						comisariaExample.createCriteria().andIdinstitucionEqualTo(solicitudAceptadaCentralitaVo.getIdinstitucion()).andCodigoextEqualTo(solicitudAceptadaCentralitaVo.getIdcentrodetencion());
						List<ScsComisaria> listComisarias =  comisariaService.getList(comisariaExample);
						if(listComisarias!=null && listComisarias.size()>0)
							idSolictanteCentralita = listComisarias.get(0).getIdcomisaria();
						if(idSolictanteCentralita!=null){
							solicitudAceptadaCentralitaForm.setIdComisaria(idSolictanteCentralita+","+solicitudAceptadaCentralitaVo.getIdinstitucion());
						}else{
							solicitudAceptadaCentralitaForm.setIdComisaria(null);
						}
						break;
				default:
					break;
				}
			}
			
			
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
	        if(solicitudAceptadaCentralitaForm.getIdComisaria()!=null){
				
				idComisariaSelected.add(solicitudAceptadaCentralitaForm.getIdComisaria());
				request.setAttribute("idComisariaSelected",idComisariaSelected);
	        }else{
	        	request.setAttribute("idComisariaSelected",idComisariaSelected);
	        }
			
			List<String> idJuzgadoSelected = new ArrayList<String>();
			idJuzgadoSelected.add(paramJuzgadosTurnos);
			request.setAttribute("idJuzgadoSelected",idJuzgadoSelected);
			
			
			request.setAttribute("idTipoAsitenciaSelected",scsSolicitudesAcpetadasService.getTipoAsistenciaColegioDefecto(solicitudAceptadaCentralitaVo.getIdTipoGuardia()!=null?Short.parseShort(solicitudAceptadaCentralitaVo.getIdTipoGuardia()):null,solicitudAceptadaCentralitaVo.getIdinstitucion()));
			
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("idTurno", idTurnoLlamada);
			jsonMap.put("idGuardia", solicitudAceptadaCentralitaForm.getIdGuardia());
			jsonMap.put("fechaGuardia", solicitudAceptadaCentralitaForm.getFechaGuardia());
			jsonMap.put("idPersona", solicitudAceptadaCentralitaForm.getIdPersona());
			
			request.setAttribute("parametrosComboColegiadosGuardia",UtilidadesString.createJsonString(jsonMap));
			
			List<String> idColegiadoGuardiaSelected = new ArrayList<String>();
			idColegiadoGuardiaSelected.add(solicitudAceptadaCentralitaForm.getIdPersona());
			request.setAttribute("idColegiadoGuardiaSelected",idColegiadoGuardiaSelected);
			request.setAttribute("idColegiadoGuardiaSeleccionado",solicitudAceptadaCentralitaForm.getIdPersona());
			request.setAttribute("nombreColegiadoGuardiaSeleccionado",solicitudAceptadaCentralitaForm.getColegiadoNombre());
			
			
			solicitudAceptadaCentralitaForm.setModo("editarSolicitudAceptada");
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.validacion");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");
		
		actualizarDatosFicha(mapping.getPath(),solicitudAceptadaCentralitaForm,  request);
		
		
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
			String numAviso  = datosSolicitudAceptada[3];
			String fechaLlamada = datosSolicitudAceptada[4];
			solicitudAceptadaCentralitaForm2= new SolicitudAceptadaCentralitaForm();
			solicitudAceptadaCentralitaForm2.setIdInstitucion(idInstitucion);
			solicitudAceptadaCentralitaForm2.setIdSolicitudAceptada(idSolicitudAceptada);
			solicitudAceptadaCentralitaForm2.setIdLlamada(idLlamada);
			solicitudAceptadaCentralitaForm2.setNumAvisoCV(numAviso);
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
	
	private String guardarSolicitudAceptada (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		SolicitudAceptadaCentralitaForm solicitudAceptadaCentralitaForm = (SolicitudAceptadaCentralitaForm) formulario;
		BusinessManager bm = getBusinessManager();
        ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
        VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			
			scsSolicitudesAcpetadasService.guardarSolicitudAceptada(voService.getForm2Vo(solicitudAceptadaCentralitaForm),true);
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
		UsrBean usrBean = this.getUserBean(request);
        BusinessManager bm = getBusinessManager();
		ScsSolicitudesAcpetadasService scsSolicitudesAcpetadasService = (ScsSolicitudesAcpetadasService) bm.getService(ScsSolicitudesAcpetadasService.class);
		VoUiService<SolicitudAceptadaCentralitaForm, SolicitudAceptadaCentralitaVo> voService = new SolicitudAceptadaCentralitaVoService();
		try {
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVoForm = voService.getForm2Vo(solicitudAceptadaCentralitaForm);
			solicitudAceptadaCentralitaVoForm.setIdioma(this.getUserBean(request).getLanguage());
			SolicitudAceptadaCentralitaVo solicitudAceptadaCentralitaVo =   scsSolicitudesAcpetadasService.getSolicitudAceptada(solicitudAceptadaCentralitaVoForm);
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
		request.setAttribute("tituloLocalizacion","sjcs.solicitudaceptadacentralita.consulta");
		request.setAttribute("localizacion","sjcs.solicitudaceptadacentralita.localizacion");
		actualizarDatosFicha(mapping.getPath(),solicitudAceptadaCentralitaForm,  request);
		
		return "consultarSolicitudAceptada";
		

	}

	
	
	
}