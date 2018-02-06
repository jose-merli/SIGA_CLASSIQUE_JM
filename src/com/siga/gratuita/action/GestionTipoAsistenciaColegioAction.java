package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.business.services.scs.TipoAsistenciaColegioService;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.vo.scs.TipoAsistenciaColegioVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.TipoAsistenciaColegioForm;
import com.siga.gratuita.form.service.TipoAsistenciaColegioVoService;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta
 *
 */
public class GestionTipoAsistenciaColegioAction extends MasterAction {	
	
protected ActionForward executeInternal (ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response)throws SIGAException {

	String mapDestino = "exception";
	MasterForm miForm = null;
	
	try {
		miForm = (MasterForm) formulario;
		if (miForm == null) {
			return mapping.findForward(mapDestino);
		}
	
		String accion = miForm.getModo();
		
		// Abrir
		if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
			mapDestino = abrir(mapping, miForm, request, response);
		else if(accion.equalsIgnoreCase("volver")){
			miForm.setModo("buscar");
			TipoAsistenciaColegioForm form = (TipoAsistenciaColegioForm) miForm;
			if(form.getTipoGuardia()!=null && !form.getTipoGuardia().equalsIgnoreCase("")){
				List<String> tiposGuardia = new ArrayList<String>();
				tiposGuardia.add(form.getTipoGuardia());
				request.setAttribute("tiposGuardia", tiposGuardia);
			}
			mapDestino = abrir(mapping, miForm, request, response);						
		}else {
			return super.executeInternal(mapping,
				      formulario,
				      request, 
				      response);
		}
		
		// Redireccionamos el flujo a la JSP correspondiente
		if (mapDestino == null) 
		{ 
			//mapDestino = "exception";
			if (miForm.getModal().equalsIgnoreCase("TRUE"))
			{
				request.setAttribute("exceptionTarget", "parent.modal");
			}
			
			//throw new ClsExceptions("El ActionMapping no puede ser nulo");
			throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
		}
	
	}
	catch (SIGAException es) { 
		throw es; 
	} 
	catch (Exception e) { 
		throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
	} 
	return mapping.findForward(mapDestino);
}	

	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm) formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
		TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
		try {
			miForm.setIdInstitucion(usr.getLocation());
			TipoAsistenciaColegioVo tipoAsistenciaColegioVo = voService.getForm2Vo(miForm);
			List<TipoAsistenciaColegioForm> tiposAsistencia = voService.getVo2FormList(service.getList(tipoAsistenciaColegioVo,usr.getLanguageInstitucion()));
			request.setAttribute("listadoTiposAsistencia", tiposAsistencia);
		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		
		return "listado";					
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm)formulario;
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String idInstitucion = miForm.getIdInstitucion();
			String idTipoAsistenciaColegio = miForm.getIdTipoAsistenciaColegio();
					
			if(ocultos!=null && ocultos.get(0)!=null){
				idInstitucion = (String) ocultos.get(0);
				idTipoAsistenciaColegio = (String) ocultos.get(1);
				
			}else{
				HttpSession ses = request.getSession();
				idTipoAsistenciaColegio = String.valueOf((Short)ses.getAttribute("idTipoAsistenciaColegio"));
				ses.removeAttribute("idTipoAsistenciaColegio");
				
			}
			
			
			String idioma=usr.getLanguageInstitucion();			
			TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
			TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
			TipoAsistenciaColegioForm formEdicion = new TipoAsistenciaColegioForm();
			formEdicion.setIdInstitucion(idInstitucion);
			formEdicion.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio);
			formEdicion = voService.getVo2Form(service.get(voService.getForm2Vo(formEdicion),idioma));
			request.setAttribute("formEdicion", formEdicion);
			request.setAttribute("tiposGuardia", formEdicion.getTiposGuardia());
			
			formEdicion.setModo("modificar");

		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}	
		return "edicion";
	
	}

	
	
	
	
	
	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm)formulario;
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String idInstitucion = miForm.getIdInstitucion();
			String idTipoAsistenciaColegio = miForm.getIdTipoAsistenciaColegio();
					
			if(ocultos!=null && ocultos.get(0)!=null){
				idInstitucion = (String) ocultos.get(0);
				idTipoAsistenciaColegio = (String) ocultos.get(1);
				
			}else{
				HttpSession ses = request.getSession();
				idTipoAsistenciaColegio = String.valueOf((Short)ses.getAttribute("idTipoAsistenciaColegio"));
				ses.removeAttribute("idTipoAsistenciaColegio");
				
			}
			
			
			String idioma=usr.getLanguageInstitucion();			
			TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
			TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
			TipoAsistenciaColegioForm formEdicion = new TipoAsistenciaColegioForm();
			formEdicion.setIdInstitucion(idInstitucion);
			formEdicion.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio);
			formEdicion = voService.getVo2Form(service.get(voService.getForm2Vo(formEdicion),idioma));
			request.setAttribute("formEdicion", formEdicion);
			request.setAttribute("tiposGuardia", formEdicion.getTiposGuardia());
			
			formEdicion.setModo("ver");

		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}	
		return "edicion";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		TipoAsistenciaColegioForm formEdicion = new TipoAsistenciaColegioForm();
		formEdicion.setIdInstitucion(usr.getLocation());
		formEdicion.setModo("insertar");
		request.setAttribute("formEdicion", formEdicion);
		
		return "edicion";
	}

	
	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm) formulario;
		TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
		TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
		try {
			miForm.setIdInstitucion(usr.getLocation());
			TipoAsistenciaColegioVo asistenciaColegioVo = voService.getForm2Vo(miForm); 
			
			asistenciaColegioVo.setIdLenguaje(usr.getLanguageInstitucion());
			asistenciaColegioVo.setIdUsuario(Integer.valueOf(usr.getUserName()));
			asistenciaColegioVo = service.insertMultiidioma(asistenciaColegioVo);
			HttpSession ses = request.getSession();
			ses.setAttribute("idTipoAsistenciaColegio",asistenciaColegioVo.getIdTipoAsistenciaColegio());
			
			
			
		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		
		return exitoRefresco("messages.inserted.success", request);
	}

	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm) formulario;
		
		
		TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
		TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
		try {
			miForm.setIdInstitucion(usr.getLocation());
			TipoAsistenciaColegioVo asistenciaColegioVo = voService.getForm2Vo(miForm); 
			
			asistenciaColegioVo.setIdLenguaje(usr.getLanguageInstitucion());
			asistenciaColegioVo.setIdUsuario(Integer.valueOf(usr.getUserName()));
			service.updateMultiidioma(asistenciaColegioVo);
			HttpSession ses = request.getSession();
			ses.setAttribute("idTipoAsistenciaColegio",asistenciaColegioVo.getIdTipoAsistenciaColegio());
			
			
			
		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		
		return exitoRefresco("messages.updated.success",request);
	}
	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			TipoAsistenciaColegioForm miForm = (TipoAsistenciaColegioForm)formulario;
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String idInstitucion = (String) ocultos.get(0);
			String idTipoAsistenciaColegio = (String) ocultos.get(1);

			TipoAsistenciaColegioService service = (TipoAsistenciaColegioService) BusinessManager.getInstance().getService(TipoAsistenciaColegioService.class);
			TipoAsistenciaColegioVoService voService = new  TipoAsistenciaColegioVoService();
			TipoAsistenciaColegioForm formEdicion = new TipoAsistenciaColegioForm();
			formEdicion.setIdInstitucion(idInstitucion);
			formEdicion.setIdTipoAsistenciaColegio(idTipoAsistenciaColegio);
			service.delete(voService.getForm2Vo(formEdicion));
			

		} catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}	
	   
		return exitoRefresco("messages.deleted.success",request);
	}
	
	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "busqueda";
	}

	
}