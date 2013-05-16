package com.siga.gratuita.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ConfConjuntoGuardiasForm;
import com.siga.gratuita.form.ConjuntoGuardiasForm;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.form.HcoConfProgrCalendarioForm;
import com.siga.gratuita.form.ProgrCalendariosForm;
import com.siga.gratuita.service.ProgramacionCalendariosService;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionProgramacionCalendariosAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizarConfiguracion")){
						mapDestino = actualizarConfiguracion (mapping, miForm, request, response);
						
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaConfConjuntoGuardias")){
						request.setAttribute("accion", "actualizacion");
						mapDestino = getAjaxBusquedaConfConjuntoGuardias (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaConfConjuntoGuardiasProgr")){
						request.setAttribute("accion", "consulta");
						mapDestino = getAjaxBusquedaConfConjuntoGuardias (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertarConjuntoGuardias")){
						mapDestino = insertarConjuntoGuardias(mapping, miForm, request, response);
						
						
					}else if ( accion.equalsIgnoreCase("borrarConjuntoGuardias")){
						mapDestino = borrarConjuntoGuardias(mapping, miForm, request, response);
						
						
					}
					else if ( accion.equalsIgnoreCase("insertarConfiguracionConjuntoGuardias")){
						mapDestino = insertarConfiguracionConjuntoGuardias(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaProgrCalendarios")){
						mapDestino = getAjaxBusquedaProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevaProgrCalendarios")){
						mapDestino = nuevaProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertarProgrCalendarios")){
						mapDestino = insertarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultarProgrCalendarios")){
						mapDestino = consultarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editarProgrCalendarios")){
						mapDestino = editarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("modificarProgrCalendarios")){
						mapDestino = modificarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("ejecutarProgramacion")){
						mapDestino = ejecutarProgramacion (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("adelantarProgrCalendarios")){
						mapDestino = adelantarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("reprogramarCalendarios")){
						mapDestino = reprogramarCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("cancelarGeneracionCalendarios")){
						mapDestino = cancelarGeneracionCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrarProgrCalendarios")){
						mapDestino = borrarProgrCalendarios (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusquedaCalendarios")){
						mapDestino = getAjaxBusquedaCalendarios (mapping, miForm, request, response);
					}
					
					else if ( accion.equalsIgnoreCase("getAjaxBusquedaHcoProgramacion")){
						mapDestino = getAjaxBusquedaHcoProgramacion (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrarHcoProgramacion")){
						mapDestino = borrarHcoProgramacion (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxGuardias")){
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
					}
					
										
					
					
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MasterForm miForm = (MasterForm) formulario;
		if(miForm instanceof ConfConjuntoGuardiasForm)
			return inicioConfiguracion(mapping, formulario, request, response);
		else if(miForm instanceof ProgrCalendariosForm)
		return inicioProgramacion(mapping, formulario, request, response);
		else if(miForm instanceof DefinirCalendarioGuardiaForm)
			return inicioCalendarios(mapping, formulario, request, response);
		
		else 
			return "inicio";
		
	}
	protected String actualizarConfiguracion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		ConfConjuntoGuardiasForm confConjuntoGuardiasForm = (ConfConjuntoGuardiasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		//Aqui vuelve detras de la insercion. Buscaremos el ultmo registro insertado
//		if(confConjuntoGuardiasForm.getIdConjuntoGuardia()==null||confConjuntoGuardiasForm.getIdConjuntoGuardia().equals("")){
			ConjuntoGuardiasForm ConjuntoGuardiasForm = programacionCalendariosService.getUltimoConjuntoGuardiaInsertado(usrBean.getLocation(),usrBean);
			confConjuntoGuardiasForm.setIdConjuntoGuardia(ConjuntoGuardiasForm.getIdConjuntoGuardia());
//		}		
		
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		
		
			
		return "inicio";
	}
	protected String inicioConfiguracion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		
			
		return "inicio";
		
	}
	protected String insertarConfiguracionConjuntoGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		ConfConjuntoGuardiasForm confConjuntoGuardiasForm = (ConfConjuntoGuardiasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		confConjuntoGuardiasForm.setIdInstitucion(usrBean.getLocation());
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			programacionCalendariosService.insertarConfiguracionConjuntoGuardias(confConjuntoGuardiasForm,usrBean);
			
			forward = exitoRefresco("messages.updated.success",request);
			
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
		
		
	}
	
	protected String insertarConjuntoGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		ConjuntoGuardiasForm ConjuntoGuardiasForm = (ConjuntoGuardiasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		ConjuntoGuardiasForm.setIdInstitucion(usrBean.getLocation());
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			programacionCalendariosService.insertaConjuntoGuardias(ConjuntoGuardiasForm,usrBean);
			forward = exitoRefresco("messages.updated.success",request);
			
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
		
		
	}
	
	protected String borrarConjuntoGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		ConjuntoGuardiasForm ConjuntoGuardiasForm = (ConjuntoGuardiasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		ConjuntoGuardiasForm.setIdInstitucion(usrBean.getLocation());
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			programacionCalendariosService.borrarConjuntoGuardias(ConjuntoGuardiasForm,usrBean);
			forward = exitoRefresco("messages.deleted.success",request);
			
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
		
		
	}
	protected String getAjaxBusquedaConfConjuntoGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		ConfConjuntoGuardiasForm confConjuntoGuardiasForm = (ConfConjuntoGuardiasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		confConjuntoGuardiasForm.setIdInstitucion(usrBean.getLocation());
		
		String forward = "listadoConfConjuntoGuardias";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			if(confConjuntoGuardiasForm.getIdConjuntoGuardia()!=null && !confConjuntoGuardiasForm.getIdConjuntoGuardia().equals("")){
				boolean isMostrarSoloGuardiasConfiguradas = confConjuntoGuardiasForm.getMostrarSoloGuardiasConfiguradas()!=null && confConjuntoGuardiasForm.getMostrarSoloGuardiasConfiguradas().equalsIgnoreCase("true");
				List<ConfConjuntoGuardiasForm> confConjuntoGuardiasForms = programacionCalendariosService.getConfiguracionConjuntoGuardias(confConjuntoGuardiasForm,isMostrarSoloGuardiasConfiguradas,usrBean);
				request.setAttribute("confConjuntoGuardiasForms", confConjuntoGuardiasForms);
			}else{
				request.setAttribute("confConjuntoGuardiasForms", new ArrayList<ConfConjuntoGuardiasForm>());
			}
			request.setAttribute("error", "");
		} catch (ClsExceptions e) {
			request.setAttribute("confConjuntoGuardiasForms", new ArrayList<ConfConjuntoGuardiasForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
		}catch (Exception e){
			request.setAttribute("confConjuntoGuardiasForms", new ArrayList<ConfConjuntoGuardiasForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
		}
		return forward;
	}
	protected String getAjaxBusquedaHcoProgramacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
		//HcoConfProgrCalendarioForm hcoConfProgrCalendarioForm = (HcoConfProgrCalendarioForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
		
		String forward = "listadoHcoProgrCalendarios";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);

			List<HcoConfProgrCalendarioForm> hcoConfProgrCalendarioForms = programacionCalendariosService.getHcoProgrCalendarios(progrCalendariosForm,usrBean);
				request.setAttribute("hcoConfProgrCalendarioForms", hcoConfProgrCalendarioForms);
				request.setAttribute("error", "");

		} catch (ClsExceptions e) {
			request.setAttribute("hcoConfProgrCalendarioForms", new ArrayList<HcoConfProgrCalendarioForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
		}catch (Exception e){
			request.setAttribute("hcoConfProgrCalendarioForms", new ArrayList<HcoConfProgrCalendarioForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
		}
		return forward;
	}
	
	
	
	
	protected String getAjaxBusquedaCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		DefinirCalendarioGuardiaForm calendarioGuardiaForm = (DefinirCalendarioGuardiaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		calendarioGuardiaForm.setIdInstitucion(usrBean.getLocation());
		
		String forward = "listadoCalendarios";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			List<DefinirCalendarioGuardiaForm> calendarioGuardiaForms = programacionCalendariosService.getCalendarios(calendarioGuardiaForm,usrBean);
			request.setAttribute("calendariosForms", calendarioGuardiaForms);
			request.setAttribute("error", "");
		} catch (ClsExceptions e) {
			request.setAttribute("calendariosForms", new ArrayList<DefinirCalendarioGuardiaForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
	
		}catch (Exception e){
			request.setAttribute("calendariosForms", new ArrayList<DefinirCalendarioGuardiaForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
			
		}
		return forward;
	}
	
	protected String getAjaxBusquedaProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
		
		String forward = "listadoProgrCalendarios";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			List<ProgrCalendariosForm> progrCalendariosForms = programacionCalendariosService.getProgramacionCalendarios(progrCalendariosForm,usrBean);
			request.setAttribute("progrCalendariosForms", progrCalendariosForms);
			request.setAttribute("error", "");
		} catch (ClsExceptions e) {
			request.setAttribute("progrCalendariosForms", new ArrayList<ProgrCalendariosForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
			

	
		}catch (Exception e){
			request.setAttribute("progrCalendariosForms", new ArrayList<ProgrCalendariosForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
			
		}
		return forward;
	}
	
	protected String inicioProgramacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgrCalendariosForm form = (ProgrCalendariosForm)formulario;
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		//form.setIdConjuntoGuardia("");
		form.clear();
			
		return "inicio";
		
	}
	protected String inicioCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		miForm.setIdInstitucion(usrBean.getLocation());
		miForm.setIdTurnoCalendario("");
		miForm.setIdGuardiaCalendario("");
		
		List<ScsTurnoBean> alTurnos =  programacionCalendariosService.getTurnos(usrBean.getLocation(),usrBean);
		if(alTurnos==null)
			alTurnos = new ArrayList<ScsTurnoBean>();
		miForm.setTurnos(alTurnos);
		miForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		request.setAttribute("calendarioForms", new ArrayList<DefinirCalendarioGuardiaForm>());
			
		return "inicio";
		
	}
	private void getAjaxGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		DefinirCalendarioGuardiaForm miForm = (DefinirCalendarioGuardiaForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		//Sacamos las guardias si hay algo selccionado en el turno
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		
		List<ScsGuardiasTurnoBean> alGuardias = null;
		if(miForm.getIdTurnoCalendario()!= null && !miForm.getIdTurnoCalendario().equals("-1")&& !miForm.getIdTurnoCalendario().equals("")){
			alGuardias = programacionCalendariosService.getGuardiasTurnos(new Integer(miForm.getIdTurnoCalendario()),new Integer(usrBean.getLocation()),true,usrBean);
		}
		if(alGuardias==null){
			alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), alGuardias,response);
		
	}
	protected String nuevaProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		ProgrCalendariosForm progrCalendariosFormEdicion = new ProgrCalendariosForm();
		progrCalendariosFormEdicion.setModo("insertarProgrCalendarios");
		
		//Seteamos la fecha sysdate para la hora de programacion (con horas y minuto)
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		progrCalendariosFormEdicion.setFechaProgramacion(sdf.format(date));
		progrCalendariosFormEdicion.setHoraProgramacion(String.valueOf(date.getHours()));
		progrCalendariosFormEdicion.setMinutoProgramacion(String.valueOf(date.getMinutes()));
		
		request.setAttribute("ProgrCalendariosFormEdicion",progrCalendariosFormEdicion);
		return "editarProgrCalendarios";
		
	}
	
	protected String insertarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
			progrCalendariosForm.setEstado("0");
			
			programacionCalendariosService.insertaProgrCalendarios(progrCalendariosForm,usrBean);
			forward = exitoModal("messages.updated.success",request);
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	
	protected String consultarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
		progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
		ScsProgCalendariosBean progCalendariosBean =  programacionCalendariosService.getProgrCalendario(progrCalendariosForm,  usrBean);
		ProgrCalendariosForm progrCalendariosFormEdicion = progCalendariosBean.getProgrCalendariosForm();
		progrCalendariosFormEdicion.setModo("consultarProgrCalendarios");
		request.setAttribute("ProgrCalendariosFormEdicion",progrCalendariosFormEdicion);
			
		return "editarProgrCalendarios";
		
	}
	protected String editarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		UsrBean usrBean = this.getUserBean(request);
		BusinessManager bm = getBusinessManager();
		ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
		List<ConjuntoGuardiasForm> ConjuntoGuardiasForms = programacionCalendariosService.getConjuntosGuardia(usrBean.getLocation(),usrBean);
		request.setAttribute("ConjuntoGuardiasForms", ConjuntoGuardiasForms);
		ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
		progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
		ScsProgCalendariosBean progCalendariosBean =  programacionCalendariosService.getProgrCalendario(progrCalendariosForm,  usrBean);
		ProgrCalendariosForm progrCalendariosFormEdicion = progCalendariosBean.getProgrCalendariosForm();
		progrCalendariosFormEdicion.setModo("modificarProgrCalendarios");
		request.setAttribute("ProgrCalendariosFormEdicion",progrCalendariosFormEdicion);
			
		return "editarProgrCalendarios";
		
	}
	protected String modificarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
			// se supone que solo se modifican los programados
			progrCalendariosForm.setEstado("0");
			
			
			
			programacionCalendariosService.modificaProgrCalendarios(progrCalendariosForm,usrBean);
			forward = exitoModal("messages.updated.success",request);
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	protected String borrarHcoProgramacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			HcoConfProgrCalendarioForm  hcoConfProgrCalendarioForm= (HcoConfProgrCalendarioForm) formulario;
			hcoConfProgrCalendarioForm.setIdInstitucion(usrBean.getLocation());
			
			programacionCalendariosService.borrarHcoProgramacion(hcoConfProgrCalendarioForm,usrBean);
			forward = exitoRefresco("messages.updated.success",request);
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	
	
	protected String cancelarGeneracionCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			
			
			/*ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
			try {
				progrCalendariosAdm.iniciarServicioProgramacion(usrBean);	
			} catch (Exception e) {
				throw new ClsExceptions(e.toString());
			}
			*/
			programacionCalendariosService.cancelarGeneracionCalendarios(progrCalendariosForm,usrBean);
			
		} catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 				
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	protected String borrarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			
			
			/*ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
			try {
				progrCalendariosAdm.iniciarServicioProgramacion(usrBean);	
			} catch (Exception e) {
				throw new ClsExceptions(e.toString());
			}
			*/
			programacionCalendariosService.borrarProgrCalendarios(progrCalendariosForm,usrBean);
			
		} catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 				
		}
		
		return exitoRefresco("messages.deleted.success",request);
	}
	protected String reprogramarCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			
			
			/*ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
			try {
				progrCalendariosAdm.iniciarServicioProgramacion(usrBean);	
			} catch (Exception e) {
				throw new ClsExceptions(e.toString());
			}
			*/
			programacionCalendariosService.reprogramarCalendarios(progrCalendariosForm,usrBean);
			
		} catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 				
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	
	protected String adelantarProgrCalendarios (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			
			
			/*ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
			try {
				progrCalendariosAdm.iniciarServicioProgramacion(usrBean);	
			} catch (Exception e) {
				throw new ClsExceptions(e.toString());
			}
			*/
			programacionCalendariosService.adelantarProgrCalendarios(progrCalendariosForm,usrBean);
			
		} catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 				
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	protected String ejecutarProgramacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			ProgramacionCalendariosService programacionCalendariosService = (ProgramacionCalendariosService)bm.getService(ProgramacionCalendariosService.class);
			ProgrCalendariosForm progrCalendariosForm = (ProgrCalendariosForm) formulario;
			ScsProgCalendariosBean progCalendariosBean =  programacionCalendariosService.getProgrCalendario(progrCalendariosForm,  usrBean);
			progrCalendariosForm.setIdInstitucion(usrBean.getLocation());
			
//			programacionCalendariosService.ejecutaProgrCalendarios(usrBean);
			
		} catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 				
		}
		
		return "exito";
	}
	
	
	protected String refrescar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "exito";
	}
	
	
	
	
	
	
}