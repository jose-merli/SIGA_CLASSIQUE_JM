package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.DocumentacionAsistenciaService;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionAsistenciaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDocumentacionAsistenciaForm;
import com.siga.gratuita.form.service.DocumentacionAsistenciaVoService;

import es.satec.businessManager.BusinessManager;
/**
 * @author Carlos Ruano Martínez 
 * @date 25/11/2013
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class DefinirDocumentacionAsistenciaAction extends MasterAction {

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
					if(accion!=null && accion.equalsIgnoreCase("downloadFichero")){
						mapDestino = downloadFichero(mapping, miForm, request, response);
					}else if(accion!=null && accion.equalsIgnoreCase("borrarfichero")){ 
						mapDestino = borrarFichero(mapping, miForm, request, response);
					}else{
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

	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			DefinirDocumentacionAsistenciaForm miForm = (DefinirDocumentacionAsistenciaForm) formulario;
			String idDocumentacion = (String)ocultos.get(0);
			String idTipoDocumento = (String)ocultos.get(1);
			
			miForm.setIdDocumentacion(idDocumentacion);
			miForm.setIdTipoDocumento(idTipoDocumento);
			miForm.setIdInstitucion(usr.getLocation());
			
			BusinessManager bm = getBusinessManager();
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo asistenciaVo = documentacionService.get(voService.getForm2Vo(miForm));
			miForm = voService.getVo2Form(asistenciaVo);			
			
			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(idTipoDocumento);
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected);
			
			List<String> idActuacionSelected = new ArrayList<String>();
			idActuacionSelected.add(miForm.getIdActuacion());
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("numero", miForm.getNumero());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));
			request.setAttribute("idActuacionSelected",idActuacionSelected);				
			
			miForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionAsistenciaForm",miForm);
			request.setAttribute("accionModo", "editar");

			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_DocumentacionAsistencia.do",null,request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}finally{
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			}
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";

	}
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			DefinirDocumentacionAsistenciaForm miForm = (DefinirDocumentacionAsistenciaForm) formulario;
			String idDocumentacion = (String)ocultos.get(0);
			String idTipoDocumento = (String)ocultos.get(1);
			
			miForm.setIdDocumentacion(idDocumentacion);
			miForm.setIdTipoDocumento(idTipoDocumento);
			miForm.setIdInstitucion(usr.getLocation());
			
			BusinessManager bm = getBusinessManager();
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo asistenciaVo = documentacionService.get(voService.getForm2Vo(miForm));
			miForm = voService.getVo2Form(asistenciaVo);				

			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(idTipoDocumento);
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected);
			
			List<String> idActuacionSelected = new ArrayList<String>();
			idActuacionSelected.add(miForm.getIdActuacion());
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("numero", miForm.getNumero());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));
			request.setAttribute("idActuacionSelected",idActuacionSelected);			

			miForm.setModo("abrir");
			request.setAttribute("DefinirDocumentacionAsistenciaForm",miForm);

			request.setAttribute("accionModo", "ver");
			
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_DocumentacionAsistencia.do",null,request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}finally{
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			}
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "
	 *  . .Fiesta" para que redirija a la pantalla de inserción.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			DefinirDocumentacionAsistenciaForm miForm = (DefinirDocumentacionAsistenciaForm) formulario;
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("numero", miForm.getNumero());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));


			request.setAttribute("idTipoDocumentoSelected",new ArrayList<String>() );
			request.setAttribute("accionModo", "editar");
			miForm.setModo("insertar");
			
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_DocumentacionAsistencia.do",null,request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			}finally{
				//hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	/**
	 * 
	 * Metodo que inserta el registo en la tabla
	 */
	protected String insertar(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = (DefinirDocumentacionAsistenciaForm) formulario;
			BusinessManager bm = getBusinessManager();
			
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo objectVo = voService.getForm2Vo(definirDocumentacionAsistenciaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));			

			documentacionService.insert(objectVo);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.updated.success", request);
	}
	
	
	/**
	 * Metodo que modifica el registro de la tabla e inserta el fichero, si lo ha selecionado, en el sistema de ficheros	
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = (DefinirDocumentacionAsistenciaForm) formulario;
			BusinessManager bm = getBusinessManager();
			
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo objectVo = voService.getForm2Vo(definirDocumentacionAsistenciaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			
			documentacionService.update(objectVo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.updated.success", request);
	}
	
	
	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = (DefinirDocumentacionAsistenciaForm) formulario;
			definirDocumentacionAsistenciaForm.setIdInstitucion(usr.getLocation());
			definirDocumentacionAsistenciaForm.setIdDocumentacion((String)ocultos.get(0));
			definirDocumentacionAsistenciaForm.setIdTipoDocumento((String)ocultos.get(1));

			BusinessManager bm = getBusinessManager();
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo objectVo = voService.getForm2Vo(definirDocumentacionAsistenciaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			
			documentacionService.delete(objectVo);			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoRefresco("messages.deleted.success", request);
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DefinirDocumentacionAsistenciaForm miForm = (DefinirDocumentacionAsistenciaForm) formulario;
		miForm.setAnio(request.getParameter("anioASI").toString());
		miForm.setNumero(request.getParameter("numeroASI").toString());
		miForm.setIdInstitucion(request.getParameter("idInstitucionASI").toString());
		request.getSession().removeAttribute("DATABACKUP");
		BusinessManager bm = getBusinessManager();
		DocumentacionAsistenciaService documentacioAsistenciaService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idinstitucion", miForm.getIdInstitucion());
		map.put("numero", miForm.getNumero());
		map.put("anio", miForm.getAnio());
		map.put("idLenguaje", usr.getLanguage());
		
		try {

			List<DocumentacionAsistenciaVo> lista = documentacioAsistenciaService.getList(map);
			request.setAttribute("resultado", lista);
			/*String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),"DEJG",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;
				
			}

			request.setAttribute("informeUnico", informeUnico);*/
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";

	}
	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DefinirDocumentacionAsistenciaForm miForm = (DefinirDocumentacionAsistenciaForm) formulario;
		BusinessManager bm = getBusinessManager();
		DocumentacionAsistenciaService documentacioAsistenciaService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);

		Map<String, Object> map = new HashMap<String, Object>();
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		map.put("idinstitucion", miForm.getIdInstitucion());
		map.put("numero", miForm.getNumero());
		map.put("anio", miForm.getAnio());
		map.put("idLenguaje", usr.getLanguage());
		try {

			List<DocumentacionAsistenciaVo> lista = documentacioAsistenciaService.getList(map);
			request.setAttribute("resultado", lista);
			/*String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),"DEJG",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;
				
			}

			request.setAttribute("informeUnico", informeUnico);*/
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";

	}	
	protected String downloadFichero(ActionMapping mapping,	MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		
		try {			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = (DefinirDocumentacionAsistenciaForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			DocumentacionAsistenciaVo objectVo = voService.getForm2Vo(definirDocumentacionAsistenciaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			File file = documentacionService.getFile(objectVo);
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getPath());
			request.setAttribute("accion", "");
			

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}

		return forward;
		
		
	}
	protected String borrarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = (DefinirDocumentacionAsistenciaForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionAsistenciaService documentacionService = (DocumentacionAsistenciaService) bm.getService(DocumentacionAsistenciaService.class);
			VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> voService = new DocumentacionAsistenciaVoService();
			
			DocumentacionAsistenciaVo objectVo = voService.getForm2Vo(definirDocumentacionAsistenciaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			documentacionService.borrarFichero(objectVo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.deleted.success", request);
	}


	
	
	
}