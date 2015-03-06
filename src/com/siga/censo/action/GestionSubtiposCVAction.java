package com.siga.censo.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.cen.SubtiposCVService;
import org.redabogacia.sigaservices.app.vo.cen.SubtiposCVVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.censo.form.SubtiposCVForm;
import com.siga.censo.form.service.SubtiposCVVoService;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;
/***
 * 
 * @author jorgeta 
 * @date   26/01/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
/*.ui-dialog-buttonset .saveButtonClass{
     background:#446699;
     border: 2px;
     border-color: white;
     color: #ddddee;
     font-size: 13px;
     font-weight: normal;
     
 }
    
.ui-dialog .ui-dialog-buttonpane {
	background:#446699;
}
*/
public class GestionSubtiposCVAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		SubtiposCVForm miForm = null;
		try { 
			do {
				miForm = (SubtiposCVForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					
					if (accion == null || accion.equals("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevo")){
						mapDestino = nuevoSubtiposCV (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editar")){
						mapDestino = editarSubtiposCV (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertarSubtiposCV (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizar")){
						mapDestino = actualizarSubtiposCV (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrar")){
						mapDestino = borrarSubtiposCV(mapping, miForm, request, response); 
					}else if ( accion.equalsIgnoreCase("volver")){
						mapDestino = volver (mapping, miForm, request, response);
					}else {
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
		SubtiposCVForm miForm = (SubtiposCVForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().removeAttribute(identificadorFormularioBusqueda);
		BusinessManager bm = getBusinessManager();
		SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
		request.setAttribute("maestroTiposCV", tiposDatosCurricularesService.getMaestroTiposCV(this.getUserBean(request).getLanguage()));
		
		return "inicio";
	}
	private String volver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		SubtiposCVForm miForm = (SubtiposCVForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setTipoDescripcion(tiposDatosCurricularesForm.getTipoDescripcion());
		miForm.setSubTipo1Descripcion(tiposDatosCurricularesForm.getSubTipo1Descripcion());
		miForm.setSubTipo2Descripcion(tiposDatosCurricularesForm.getSubTipo2Descripcion());
		miForm.setIdInstitucion(usrBean.getLocation());
        
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
			SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
			String idInstitucion = request.getParameter("idInstitucion");
			String tipoDescripcion = request.getParameter("tipoDescripcion");
			String subTipo1Descripcion = request.getParameter("subTipo1Descripcion");
			String subTipo2Descripcion = request.getParameter("subTipo2Descripcion");
				        
	        tiposDatosCurricularesForm.setIdInstitucion(idInstitucion);
	        if(tipoDescripcion!=null)
	        	tiposDatosCurricularesForm.setTipoDescripcion(tipoDescripcion.trim());
	        if(subTipo1Descripcion!=null)
	        	tiposDatosCurricularesForm.setSubTipo1Descripcion(subTipo1Descripcion.trim());
	        if(subTipo2Descripcion!=null)
	        	tiposDatosCurricularesForm.setSubTipo2Descripcion(subTipo2Descripcion.trim());
	        	        
	        String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			request.getSession().setAttribute(identificadorFormularioBusqueda,tiposDatosCurricularesForm.clone());

	        BusinessManager bm = getBusinessManager();
			SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
			VoUiService<SubtiposCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
			List<SubtiposCVForm> tiposDatosCurricularesForms = null;
			try {
				SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
				tiposDatosCurricularesVo.setIdioma(this.getUserBean(request).getLanguage());
				tiposDatosCurricularesForms =  voService.getVo2FormList(tiposDatosCurricularesService.getList(tiposDatosCurricularesVo));
				request.setAttribute("tiposDatosCurriculares", tiposDatosCurricularesForms);
				request.setAttribute("maestroTiposCV", tiposDatosCurricularesService.getMaestroTiposCV(this.getUserBean(request).getLanguage()));
				
				return "listado";
			}catch (Exception e){
				tiposDatosCurricularesForms = new ArrayList<SubtiposCVForm>();
				request.setAttribute("tiposDatosCurriculares", tiposDatosCurricularesForms);
				String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
				throw new SIGAException(error,e);
				
			}
							

		}
		
	  
	private String editarSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
        BusinessManager bm = getBusinessManager();
		VoUiService<SubtiposCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
		try {
			SubtiposCVVo tiposDatosCurricularesVo =  null;// tiposDatosCurricularesService.getSolicitudAceptada(voService.getForm2Vo(tiposDatosCurricularesForm));
			tiposDatosCurricularesForm.clear();
			
			tiposDatosCurricularesForm  = voService.getVo2Form(tiposDatosCurricularesVo,tiposDatosCurricularesForm);
			tiposDatosCurricularesForm.setModo("actualizar");
			
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		return "editar";

	}
	private String nuevoSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
		try {
			String idInstitucion = request.getParameter("idInstitucion");
			tiposDatosCurricularesForm.clear();
			tiposDatosCurricularesForm.setIdInstitucion(idInstitucion);
			tiposDatosCurricularesForm.setModo("insertar");
			
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		request.setAttribute("titulo","sjcs.solicitudaceptadacentralita.validacion.titulo");
		return "editar";

	}
	
	
	
	
	private String insertarSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
		BusinessManager bm = getBusinessManager();
        SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
        VoUiService<SubtiposCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
		try {
			SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
			tiposDatosCurricularesVo.setIdioma(this.getUserBean(request).getLanguage());
			tiposDatosCurricularesVo.setUsumodificacion(Integer.valueOf(this.getUserBean(request).getUserName()));
			tiposDatosCurricularesService.insertarSubtiposCV(tiposDatosCurricularesVo);
		}catch (BusinessException e){
			return errorRefresco(e.getMessage(),new ClsExceptions(e.toString()),request);
			
		}
		catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		//request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
		return exitoRefresco("messages.inserted.success",request);
						

	}
	private String actualizarSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
		BusinessManager bm = getBusinessManager();
        SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
        VoUiService<SubtiposCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
		try {
			SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
			tiposDatosCurricularesVo.setUsumodificacion(Integer.valueOf(this.getUserBean(request).getUserName()));
			tiposDatosCurricularesVo.setIdioma(this.getUserBean(request).getLanguage());
			tiposDatosCurricularesService.actualizarSubtiposCV(tiposDatosCurricularesVo);
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
		//request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
		return exitoRefresco("messages.updated.success",request);
						

	}
	private String borrarSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SubtiposCVForm tiposDatosCurricularesForm = (SubtiposCVForm) formulario;
		BusinessManager bm = getBusinessManager();
        SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
        VoUiService<SubtiposCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
		try {
			SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
			tiposDatosCurricularesVo.setUsumodificacion(Integer.valueOf(this.getUserBean(request).getUserName()));
			tiposDatosCurricularesService.borrarSubtiposCV(tiposDatosCurricularesVo);
		}
		catch (Exception e){
			return errorRefresco("messages.elementoenuso.error",new ClsExceptions(e.toString()),request);
			
		}
		//request.setAttribute("mensajeSuccess",UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.updated.success"));
		return exitoRefresco("messages.deleted.success",request);
						

	}
	
	

	
	
	
}