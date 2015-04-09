package com.siga.censo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.services.cen.SubtiposCVService;
import org.redabogacia.sigaservices.app.services.cen.impl.CargaMasivaDatosCVImpl;
import org.redabogacia.sigaservices.app.vo.cen.CargaMasivaDatosCVVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.censo.form.CargaMasivaCVForm;
import com.siga.censo.form.service.CargaMasivaDatosCVVoService;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;
/**
 * 
 * @author jorgeta 
 * @date   06/04/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaCVAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		CargaMasivaCVForm miForm = null;
		try { 
			do {
				miForm = (CargaMasivaCVForm) formulario;
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
					}else if ( accion.equalsIgnoreCase("procesarFichero")){
						mapDestino = procesarFichero (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("descargaEjemplo")){
						mapDestino = descargaEjemplo (mapping, miForm, request, response);
					}
					
					else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertarSubtiposCV (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizar")){
						mapDestino = actualizarSubtiposCV (mapping, miForm, request, response);
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
		CargaMasivaCVForm miForm = (CargaMasivaCVForm) formulario;
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
		CargaMasivaCVForm miForm = (CargaMasivaCVForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setFechaCarga(cargaMasivaCVForm.getFechaCarga());
		miForm.setIdInstitucion(cargaMasivaCVForm.getIdInstitucion());
		miForm.setModo("vuelta");
        
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
			CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
			String idInstitucion = request.getParameter("idInstitucion");
			String fechaCarga = request.getParameter("fechaCarga");
	        cargaMasivaCVForm.setIdInstitucion(idInstitucion);
	        if(fechaCarga!=null)
	        	cargaMasivaCVForm.setFechaCarga(fechaCarga);
	        	        
	        String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaCVForm.clone());
			List<CargaMasivaCVForm> tiposDatosCurricularesForms = null;
			tiposDatosCurricularesForms = new ArrayList<CargaMasivaCVForm>();
			CargaMasivaCVForm cargaMasivaCVTest = new CargaMasivaCVForm();
			cargaMasivaCVTest.setFechaCarga("25/10/2015");
			cargaMasivaCVTest.setNombreFichero("CArga de Curso de Spring");
			cargaMasivaCVTest.setUsuario("Jorge Torres Acosta");
			cargaMasivaCVTest.setNumRegistros("69");
			tiposDatosCurricularesForms.add(cargaMasivaCVTest);
			
			request.setAttribute("listado", tiposDatosCurricularesForms);
	       /* BusinessManager bm = getBusinessManager();
			SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
			VoUiService<CargaMasivaCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
			
			try {
				SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
				tiposDatosCurricularesVo.setIdioma(this.getUserBean(request).getLanguage());
				tiposDatosCurricularesForms =  voService.getVo2FormList(tiposDatosCurricularesService.getList(tiposDatosCurricularesVo));
				request.setAttribute("tiposDatosCurriculares", tiposDatosCurricularesForms);
				request.setAttribute("maestroTiposCV", tiposDatosCurricularesService.getMaestroTiposCV(this.getUserBean(request).getLanguage()));
				
				return "listado";
			}catch (Exception e){
				tiposDatosCurricularesForms = new ArrayList<CargaMasivaCVForm>();
				request.setAttribute("tiposDatosCurriculares", tiposDatosCurricularesForms);
				String error = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.general.errorExcepcion");
				throw new SIGAException(error,e);
				
			}*/
			return "listado";
							

		}
	
	private String nuevoSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaCVForm tiposDatosCurricularesForm = (CargaMasivaCVForm) formulario;
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
		CargaMasivaCVForm tiposDatosCurricularesForm = (CargaMasivaCVForm) formulario;
		/*BusinessManager bm = getBusinessManager();
        
		SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
        VoUiService<CargaMasivaCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
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
			
		}*/

		return exitoRefresco("messages.inserted.success",request);
						

	}
	private String descargaEjemplo (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
        	        
		CargaMasivaDatosCVImpl cargaMasivaDatosCV = new CargaMasivaDatosCVImpl(); 
//		Vector<Hashtable<String, Object>> datosVector = new Vector<Hashtable<String,Object>>();
//		Hashtable<String, Object> elemento = new Hashtable<String, Object>();
//		List<String> campos =  CargaMasivaDatosCV.CAMPOS;
//		for (int i = 0; i < campos.size(); i++) {
//			elemento.put(campos.get(i),"");
//		} 
//		datosVector.add(elemento);
		File exampleFile =  cargaMasivaDatosCV.createExcelFile(CargaMasivaDatosCVImpl.CAMPOS,null);
		request.setAttribute("nombreFichero", exampleFile.getName());
		request.setAttribute("rutaFichero", exampleFile.getPath());
		request.setAttribute("accion", "");
		return "descargaFichero";
						

	}
	
	private String procesarFichero (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaCVForm tiposDatosCurricularesForm = (CargaMasivaCVForm) formulario;
		CargaMasivaDatosCVImpl cargaMasivaDatosCV = new CargaMasivaDatosCVImpl(); 
		
		try {
			if(tiposDatosCurricularesForm.getTheFile()!=null && tiposDatosCurricularesForm.getTheFile().getFileData()!=null && tiposDatosCurricularesForm.getTheFile().getFileData().length>0){
				List<CargaMasivaDatosCVVo> cargaMasivaDatosCVList = cargaMasivaDatosCV.parseExcelFile(tiposDatosCurricularesForm.getTheFile().getFileData());
				VoUiService<CargaMasivaCVForm, CargaMasivaDatosCVVo> voService = new CargaMasivaDatosCVVoService();
				request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosCVList));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "listadoResumen";
						

	}
	
	private String actualizarSubtiposCV (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaCVForm tiposDatosCurricularesForm = (CargaMasivaCVForm) formulario;
/*		BusinessManager bm = getBusinessManager();
        SubtiposCVService tiposDatosCurricularesService = (SubtiposCVService) bm.getService(SubtiposCVService.class);
        VoUiService<CargaMasivaCVForm, SubtiposCVVo> voService = new SubtiposCVVoService();
		try {
			SubtiposCVVo tiposDatosCurricularesVo = voService.getForm2Vo(tiposDatosCurricularesForm);
			tiposDatosCurricularesVo.setUsumodificacion(Integer.valueOf(this.getUserBean(request).getUserName()));
			tiposDatosCurricularesVo.setIdioma(this.getUserBean(request).getLanguage());
			tiposDatosCurricularesService.actualizarSubtiposCV(tiposDatosCurricularesVo);
		}catch (Exception e){
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.gratuita"});
			
		}
*/
		return exitoRefresco("messages.updated.success",request);
						

	}
	
	
	

	
	
	
}