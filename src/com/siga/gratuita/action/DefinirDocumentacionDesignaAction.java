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
import org.redabogacia.sigaservices.app.services.scs.DocumentacionDesignaService;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionDesignaVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDocumentacionDesignaForm;
import com.siga.gratuita.form.service.DocumentacionDesignaVoService;

import es.satec.businessManager.BusinessManager;
/**
 * 
 * @author jorgeta 
 * @date   26/05/2014
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class DefinirDocumentacionDesignaAction extends MasterAction {

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
					}else if(accion!=null && accion.equalsIgnoreCase("downloadFicheros")){ 
						mapDestino = downloadFicheros(mapping, miForm, request, response);
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
			
			DefinirDocumentacionDesignaForm miForm = (DefinirDocumentacionDesignaForm) formulario;
			if(ocultos!=null){
				String idDocumentacion = (String)ocultos.get(0);
				String idTipoDocumento = (String)ocultos.get(1);
				miForm.setIdDocumentacion(idDocumentacion);
				miForm.setIdTipoDocumento(idTipoDocumento);
			
			}	
			miForm.setIdInstitucion(usr.getLocation());
			
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo designaVo = documentacionService.get(voService.getForm2Vo(miForm));
			miForm = voService.getVo2Form(designaVo);			
			
			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(miForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected);
			
			List<String> idActuacionSelected = new ArrayList<String>();
			if(miForm.getIdActuacion()!=null && !miForm.getIdActuacion().equals("")){
				idActuacionSelected.add(miForm.getIdActuacion());
				
				
			}
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("idturno", miForm.getIdTurno());
			jsonMap.put("numero", miForm.getNumero());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));
			request.setAttribute("idActuacionSelected",idActuacionSelected);				
			
			miForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionDesignaForm",miForm);
			request.setAttribute("accionModo", "editar");
			//pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", true);
			try {
				String permisoFicheros = testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
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
			
			DefinirDocumentacionDesignaForm miForm = (DefinirDocumentacionDesignaForm) formulario;
			
			if(ocultos!=null){
				String idDocumentacion = (String)ocultos.get(0);
				String idTipoDocumento = (String)ocultos.get(1);
				miForm.setIdDocumentacion(idDocumentacion);
				miForm.setIdTipoDocumento(idTipoDocumento);
			
			}		
			
			miForm.setIdInstitucion(usr.getLocation());
			
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo designaVo = documentacionService.get(voService.getForm2Vo(miForm));
			miForm = voService.getVo2Form(designaVo);				

			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(miForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected);
			
			List<String> idActuacionSelected = new ArrayList<String>();
			if(miForm.getIdActuacion()!=null && !miForm.getIdActuacion().equals("")){
				idActuacionSelected.add(miForm.getIdActuacion());
				
				
			}
			request.setAttribute("idActuacionSelected",idActuacionSelected);
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("numero", miForm.getNumero());
			jsonMap.put("idturno", miForm.getIdTurno());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));
						

			miForm.setModo("abrir");
			request.setAttribute("DefinirDocumentacionDesignaForm",miForm);

			request.setAttribute("accionModo", "ver");
			//pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", true);
			try {
				String permisoFicheros = testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
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
			DefinirDocumentacionDesignaForm miForm = (DefinirDocumentacionDesignaForm) formulario;
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Map jsonMap = new HashMap<String, String>();
			jsonMap.put("anio", miForm.getAnio());
			jsonMap.put("numero", miForm.getNumero());
			jsonMap.put("idturno", miForm.getIdTurno());
			request.setAttribute("paramActuacionesJson", UtilidadesString.createJsonString(jsonMap));

			List<String> idTipoDocSelected = new ArrayList<String>();
			List<String> idActuacionSelected = new ArrayList<String>();
			if(miForm.getIdActuacion()!=null && !miForm.getIdActuacion().equals("")){
				idActuacionSelected.add(miForm.getIdActuacion());
				//Si vienee de informe de justificacion ponemos por defeecto el docuemnto de justificacion
				idTipoDocSelected.add("1");
			
				
			}
			request.setAttribute("idActuacionSelected",idActuacionSelected);
			
			
			

			request.setAttribute("idTipoDocumentoSelected",idTipoDocSelected);
			request.setAttribute("accionModo", "editar");
			miForm.setModo("insertar");
			//pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", true);

			
			
			
			
			try {
				String permisoFicheros = testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
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
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			BusinessManager bm = getBusinessManager();
			
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo objectVo = voService.getForm2Vo(definirDocumentacionDesignaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));			

			documentacionService.insert(objectVo);
			
			
			//Insertamos primero el fichero para quedarnos con su referencia
			if(objectVo.getFichero()!=null && objectVo.getFichero().length>0){
				documentacionService.uploadFile(objectVo);
				objectVo.setIdfichero(objectVo.getIdfichero());
				documentacionService.updateSelective(objectVo);
			}
			
			
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
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			BusinessManager bm = getBusinessManager();
			
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo objectVo = voService.getForm2Vo(definirDocumentacionDesignaForm);
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
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			definirDocumentacionDesignaForm.setIdInstitucion(usr.getLocation());
			if(ocultos!=null){
				String idDocumentacion = (String)ocultos.get(0);
				String idTipoDocumento = (String)ocultos.get(1);
				definirDocumentacionDesignaForm.setIdDocumentacion(idDocumentacion);
				definirDocumentacionDesignaForm.setIdTipoDocumento(idTipoDocumento);
			
			}		
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo objectVo = voService.getForm2Vo(definirDocumentacionDesignaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			
			documentacionService.delete(objectVo);	
			if(objectVo.getIdfichero()!=null){
				documentacionService.deleteFile(objectVo);
			}
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoRefresco("messages.deleted.success", request);
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DefinirDocumentacionDesignaForm miForm = (DefinirDocumentacionDesignaForm) formulario;
		
//		numero = (String)request.getParameter("NUMERO");
//		anio = (String)request.getParameter("ANIO");
//		idTurno = (String)request.getParameter("IDTURNO");
//		
		
		
		
		miForm.setAnio((String)request.getParameter("ANIO"));
		miForm.setNumero((String)request.getParameter("NUMERO"));
		miForm.setIdTurno((String)request.getParameter("IDTURNO"));
		miForm.setIdInstitucion((String)request.getParameter("IDINSTITUCION"));
		
		request.getSession().removeAttribute("DATABACKUP");
		BusinessManager bm = getBusinessManager();
		DocumentacionDesignaService documentacioDesignaService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idinstitucion", miForm.getIdInstitucion());
		map.put("numero", miForm.getNumero());
		map.put("anio", miForm.getAnio());
		map.put("idturno", miForm.getIdTurno());
		map.put("idLenguaje", usr.getLanguage());
		
		try {

			List<DocumentacionDesignaVo> lista = documentacioDesignaService.getList(map);
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
		
		DefinirDocumentacionDesignaForm miForm = (DefinirDocumentacionDesignaForm) formulario;
		BusinessManager bm = getBusinessManager();
		DocumentacionDesignaService documentacioDesignaService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);

		Map<String, Object> map = new HashMap<String, Object>();
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		map.put("idinstitucion", miForm.getIdInstitucion());
		map.put("numero", miForm.getNumero());
		map.put("idturno", miForm.getIdTurno());
		map.put("anio", miForm.getAnio());
		map.put("idLenguaje", usr.getLanguage());
		try {

			List<DocumentacionDesignaVo> lista = documentacioDesignaService.getList(map);
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
	protected String downloadFicheros(ActionMapping mapping,	MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		
		try {			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idinstitucion", definirDocumentacionDesignaForm.getIdInstitucion());
			map.put("numero", definirDocumentacionDesignaForm.getNumero());
			map.put("anio", definirDocumentacionDesignaForm.getAnio());
			map.put("idturno", definirDocumentacionDesignaForm.getIdTurno());
			map.put("idactuacion", definirDocumentacionDesignaForm.getIdActuacion());
			map.put("idLenguaje", usr.getLanguage());
			
			File file =  documentacionService.getFicheros(documentacionService.getList(map));
			 
			 
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
	protected String downloadFichero(ActionMapping mapping,	MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		
		try {			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");		
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			DocumentacionDesignaVo objectVo = voService.getForm2Vo(definirDocumentacionDesignaForm);
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
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> voService = new DocumentacionDesignaVoService();
			
			DocumentacionDesignaVo objectVo = voService.getForm2Vo(definirDocumentacionDesignaForm);
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			documentacionService.borrarFichero(objectVo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.deleted.success", request);
	}


	
	
	
}