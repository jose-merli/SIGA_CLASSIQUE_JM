package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.censo.service.BajasTemporalesService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionBajasTemporalesAction extends MasterAction {
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
					}else if ( accion.equalsIgnoreCase("getAjaxGuardias")){
						getAjaxGuardias (mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxColegiado")){
					
						getAjaxColegiado(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevaSolicitud")){
						mapDestino = nuevaSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertarNuevaSolicitud")){
						mapDestino = insertarNuevaSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertarNuevaSolicitudDirect")){
						mapDestino = insertarNuevaSolicitudDirect(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("validarSolicitud")){
						mapDestino = validarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("denegarSolicitud")){
						mapDestino = denegarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrarSolicitud")){
						mapDestino = borrarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultarSolicitud")){
						mapDestino = consultarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editarSolicitud")){
						mapDestino = editarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("modificarSolicitud")){
						mapDestino = modificarSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("refrescar")){
						mapDestino = refrescar(mapping, miForm, request, response);
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
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		bajasTemporalesForm.setUsrBean(usrBean);
		String parameter = request.getParameter("fichaColegial");
		if(parameter!=null && Boolean.parseBoolean(parameter)){
			return inicioFichaColegial(mapping, formulario, request, response);
		}else{
			return inicioGestion(mapping, formulario, request, response);
			
			
		}
	}
	protected String refrescar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "exito";
	}
	
	protected String inicioGestion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
			
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		
		bajasTemporalesForm.clear();
		bajasTemporalesForm.setFechaDesde(GstDate.getHoyJsp());
		UsrBean usrBean = this.getUserBean(request);
		bajasTemporalesForm.setIdInstitucion(usrBean.getLocation());
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(usrBean);
		List<ScsTurnoBean> alTurnos = admTurnos.getTurnos(bajasTemporalesForm.getIdInstitucion());
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
		
		}
		bajasTemporalesForm.setTurnos(alTurnos);
		bajasTemporalesForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		return "inicio";
	}
	protected String inicioFichaColegial (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		String idPersona = request.getParameter("idPersonaPestanha");
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		bajasTemporalesForm.clear();
		bajasTemporalesForm.setFichaColegial(true);
		UsrBean usrBean = this.getUserBean(request);
		bajasTemporalesForm.setIdPersona(idPersona);
		bajasTemporalesForm.setIdInstitucion(usrBean.getLocation());
		bajasTemporalesForm.setTurnos(new ArrayList<ScsTurnoBean>());
		bajasTemporalesForm.setGuardias(new ArrayList<ScsGuardiasTurnoBean>());
		String forward = "listadoBajasTemporales";
		

		 
		try {
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			ArrayList<CenBajasTemporalesBean> alBajasTemporales = (ArrayList<CenBajasTemporalesBean>) bts.getBajasTemporales(bajasTemporalesForm,usrBean);
			
			bajasTemporalesForm.setBajasTemporales(alBajasTemporales);
			bts.setColegiado(bajasTemporalesForm, usrBean);
			
			
		} catch (ClsExceptions e) {
			bajasTemporalesForm.setBajasTemporales(new ArrayList<CenBajasTemporalesBean>());
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			bajasTemporalesForm.setMsgError(error);
	
		}catch (Exception e){
			bajasTemporalesForm.setBajasTemporales(new ArrayList<CenBajasTemporalesBean>());
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			bajasTemporalesForm.setMsgError(error);
			
		}
		return forward;
	}
	
	protected String getAjaxBusqueda (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward = "listadoBajasTemporales";
		

		 
		try {
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			ArrayList<CenBajasTemporalesBean> alBajasTemporales = (ArrayList<CenBajasTemporalesBean>) bts.getBajasTemporales(bajasTemporalesForm,usrBean);
			bajasTemporalesForm.setBajasTemporales(alBajasTemporales);
			
			
		} catch (ClsExceptions e) {
			bajasTemporalesForm.setBajasTemporales(new ArrayList<CenBajasTemporalesBean>());
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			bajasTemporalesForm.setMsgError(error);
	
		}catch (Exception e){
			bajasTemporalesForm.setBajasTemporales(new ArrayList<CenBajasTemporalesBean>());
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			bajasTemporalesForm.setMsgError(error);
			
		}
		return forward;
	}
	
	
	protected void getAjaxGuardias (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BajasTemporalesForm miForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsGuardiasTurnoBean> alGuardias = null;
		if(miForm.getIdTurno()!= null && !miForm.getIdTurno().equals("-1")&& !miForm.getIdTurno().equals("")){
			ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usrBean);
			alGuardias = admGuardias.getGuardiasTurnos(new Integer(miForm.getIdTurno()),new Integer(miForm.getIdInstitucion()),true);
		}
		if(alGuardias==null){
			alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsGuardiasTurnoBean>(), alGuardias,response);
		
	}
	protected void getAjaxColegiado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		BajasTemporalesForm miForm = (BajasTemporalesForm) formulario;
		//Sacamos las guardias si hay algo selccionado en el turno
		Hashtable<String, Object> htCliente = null;
		String colegiadoNumero = miForm.getColegiadoNumero();
		UsrBean usrBean = this.getUserBean(request);
				
		
					
		if(colegiadoNumero!= null && !colegiadoNumero.equals("")){
			CenClienteAdm admCli = new CenClienteAdm(usrBean);
			Vector<Hashtable<String, Object>> vClientes = admCli.getClientePorNColegiado(miForm.getIdInstitucion(),miForm.getColegiadoNumero());
			if(vClientes!=null &&vClientes.size()>0)
				htCliente = vClientes.get(0);
		}
		String colegiadoNombre = null;
		String idPersona = null;
		
		if(htCliente!=null){
			colegiadoNombre = (String)htCliente.get("NOMCOLEGIADO");
			idPersona = (String)htCliente.get("IDPERSONA");
			
		}else{
			colegiadoNombre = "";
			idPersona = "";
			colegiadoNumero = "";
			
		}

		List listaParametros = new ArrayList();
		listaParametros.add(idPersona);
		listaParametros.add(colegiadoNumero);
		listaParametros.add(colegiadoNombre);
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
	}
	protected String nuevaSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		bajasTemporalesForm.setFechaDesde(null);
		bajasTemporalesForm.setFechaHasta(null);
		bajasTemporalesForm.setDescripcion(null);
		bajasTemporalesForm.setTipo(null);
		bajasTemporalesForm.setExistenciaSolapados(false);
		bajasTemporalesForm.setModo("insertarNuevaSolicitud");
		return "solicitud";
	}
	protected String editarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			CenBajasTemporalesBean bajaTemporalBean = bts.getBajaTemporal(bajasTemporalesForm, usrBean); 
			bajasTemporalesForm.setFechaDesde(bajaTemporalBean.getBajaTemporalForm().getFechaDesde());
			bajasTemporalesForm.setFechaHasta(bajaTemporalBean.getBajaTemporalForm().getFechaHasta());
			bajasTemporalesForm.setDescripcion(bajaTemporalBean.getBajaTemporalForm().getDescripcion());
			bajasTemporalesForm.setTipo(bajaTemporalBean.getBajaTemporalForm().getTipo());
			bajasTemporalesForm.setModo("modificarSolicitud");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "solicitud";
	}
	protected String consultarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			CenBajasTemporalesBean bajaTemporalBean = bts.getBajaTemporal(bajasTemporalesForm, usrBean); 
			bajasTemporalesForm.setFechaDesde(bajaTemporalBean.getBajaTemporalForm().getFechaDesde());
			bajasTemporalesForm.setFechaHasta(bajaTemporalBean.getBajaTemporalForm().getFechaHasta());
			bajasTemporalesForm.setDescripcion(bajaTemporalBean.getBajaTemporalForm().getDescripcion());
			bajasTemporalesForm.setTipo(bajaTemporalBean.getBajaTemporalForm().getTipo());
			bajasTemporalesForm.setModo("consultarSolicitud");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "solicitud";
	}
	
	
	
	
	
	protected String insertarNuevaSolicitud(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			try {
				bts.insertaBajasTemporales(bajasTemporalesForm,true, usrBean);
				forward = exitoModal("messages.updated.success",request);
			} catch (Exception e) {
				bajasTemporalesForm.setExistenciaSolapados(true);
				forward="solicitud";
				return  forward;
				
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	protected String insertarNuevaSolicitudDirect (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			bts.insertaBajasTemporales(bajasTemporalesForm,false, usrBean);
			bajasTemporalesForm.setExistenciaSolapados(false);
			forward = exitoModal("messages.updated.success",request);
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	
	protected String denegarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			bts.denegarSolicitudesBajaTemporal(bajasTemporalesForm, usrBean);
			request.setAttribute("mensaje","messages.updated.success");
//			String aviso = UtilidadesString.getMensajeIdioma(usrBean,"messages.updated.success");
//			bajasTemporalesForm.setMsgAviso(aviso);
			
		} catch (ClsExceptions e) {
			
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			bajasTemporalesForm.setMsgError(error);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			bajasTemporalesForm.setMsgError(error);			
		}
		
		return "exito";
	}
	protected String validarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			bts.validarSolicitudesBajaTemporal(bajasTemporalesForm, usrBean);
			request.setAttribute("mensaje","messages.updated.success");
//			String aviso = UtilidadesString.getMensajeIdioma(usrBean,"messages.updated.success");
//			bajasTemporalesForm.setMsgAviso(aviso);
			
		} catch (ClsExceptions e) {
			
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			bajasTemporalesForm.setMsgError(error);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			bajasTemporalesForm.setMsgError(error);			
		}
		
		return "exito";
	}
	protected String borrarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			bts.borrarSolicitudBajaTemporal(bajasTemporalesForm, usrBean);
			request.setAttribute("mensaje","messages.deleted.success");
//			String aviso = UtilidadesString.getMensajeIdioma(usrBean,"messages.updated.success");
//			bajasTemporalesForm.setMsgAviso(aviso);
			
		} catch (ClsExceptions e) {
			
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			bajasTemporalesForm.setMsgError(error);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			bajasTemporalesForm.setMsgError(error);			
		}
		
		return "exito";
	}
	protected String modificarSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		BajasTemporalesForm bajasTemporalesForm = (BajasTemporalesForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			BajasTemporalesService bts = (BajasTemporalesService)bm.getService(BajasTemporalesService.class);
			bts.modificarSolicitudBajaTemporal(bajasTemporalesForm, usrBean);
			bajasTemporalesForm.setIdPersona("");
			
			forward = exitoModal("messages.updated.success",request);
			
		} catch (Exception e) {
			 
			throwExcp("messages.general.errorExcepcion", e, null); 

			
		}
	
		
		return forward;
	}
	
	
	
	
	
}