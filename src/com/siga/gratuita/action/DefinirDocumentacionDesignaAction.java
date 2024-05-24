package com.siga.gratuita.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.services.scs.DocumentacionDesignaService;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionDesignaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsDesignaBean;
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
					}else if(accion!=null && accion.equalsIgnoreCase("descargarFichero")){ 
						mapDestino = descargarFichero(mapping, miForm, request, response);
					}else if(accion!=null && accion.equalsIgnoreCase("borrarFicheroFichaColegial")){ 
						mapDestino = borrarFicheroFichaColegial(mapping, miForm, request, response);
					}else if(accion!=null && accion.equalsIgnoreCase("getAjaxObtenerListadoDocumentacion")){ 
						getAjaxObtenerListadoDocumentacion(request, response);
						return null;			
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
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String permisosActicionesValidadas = paramAdm.getValor(usr.getLocation (), "SCS", "SCS_PERMISOS_ACTUACIONES_VALIDADAS", "0");
			request.setAttribute("SCS_PERMISOS_ACTUACIONES_VALIDADAS", permisosActicionesValidadas);
			
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
						
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String permisosActicionesValidadas = paramAdm.getValor(usr.getLocation (), "SCS", "SCS_PERMISOS_ACTUACIONES_VALIDADAS", "0");
			request.setAttribute("SCS_PERMISOS_ACTUACIONES_VALIDADAS", permisosActicionesValidadas);

			miForm.setModo("ver");
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
			GenParametrosAdm paramAdm = new GenParametrosAdm (usr);
			String permisosActicionesValidadas = paramAdm.getValor(usr.getLocation (), "SCS", "SCS_PERMISOS_ACTUACIONES_VALIDADAS", "0");
			request.setAttribute("SCS_PERMISOS_ACTUACIONES_VALIDADAS", permisosActicionesValidadas);
			

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

			//Insertamos primero el fichero para quedarnos con su referencia
			documentacionService.uploadFile(objectVo);

			objectVo.setIdfichero(objectVo.getIdfichero());
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
	protected String borrarFicheroFichaColegial(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			definirDocumentacionDesignaForm.setIdInstitucion(usr.getLocation());
		
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
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxObtenerListadoDocumentacion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 String composicionTablaFicheros ="";
		 JSONObject json = new JSONObject();
		 UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");	
		 SimpleDateFormat formateo = new SimpleDateFormat("dd/MM/yyyy");
		
		 BusinessManager bm = getBusinessManager();
		 DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
		 FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
		
		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("idinstitucion",  request.getParameter("idInstitucion"));
		 map.put("numero", request.getParameter("numero"));
		 map.put("anio", request.getParameter("anio"));
		 map.put("idturno", request.getParameter("idTurno"));
		 map.put("idactuacion", request.getParameter("numeroActuacion"));
		 map.put("idLenguaje", usr.getLanguage());
		 
		
		//Obtenemos los datos de la designa
		 
		ScsActuacionDesignaAdm designaAdm = new ScsActuacionDesignaAdm (this.getUserBean(request));	 
	   //Mostrar Las Actuaciones.
		Hashtable hashDatosDesigna= new Hashtable();			
		UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_IDINSTITUCION, request.getParameter("idInstitucion"));
		UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_ANIO, request.getParameter("anio"));
		UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_NUMERO, request.getParameter("numero"));
		UtilidadesHash.set(hashDatosDesigna,ScsDesignaBean.C_IDTURNO, request.getParameter("idTurno"));
		UtilidadesHash.set(hashDatosDesigna,"VISIBLE",request.getParameter("numeroActuacion"));		
		
		Hashtable actuacion =(Hashtable)(designaAdm.getDesignaActuaciones(hashDatosDesigna, request)).get(0);
		 
		List<DocumentacionDesignaVo> listaDocumentacion = documentacionService.getList(map);
		FicheroVo ficheroVo;
		 
		 if(listaDocumentacion != null && listaDocumentacion.size()>0){
			 for (int i = 0; i < listaDocumentacion.size(); i++)	{
				DocumentacionDesignaVo DocumentacionAuxiliar = (DocumentacionDesignaVo) listaDocumentacion.get(i);
				//Obtenemos el fichero, para obtener el usuarioModificacion de gen_fichero que es el que necesitamos.
				ficheroVo = new FicheroVo();
				ficheroVo.setIdfichero(DocumentacionAuxiliar.getIdfichero());
				ficheroVo.setIdinstitucion(DocumentacionAuxiliar.getIdinstitucion());
				ficheroVo = ficherosService.getFichero(ficheroVo);  
		    	composicionTablaFicheros +="<tr><td>"+DocumentacionAuxiliar.getNombreTipoDoc()+"</td><td>"+DocumentacionAuxiliar.getDescripcionActuacion()+"</td><td>"+
				formateo.format(DocumentacionAuxiliar.getFechaentrada())+"</td>";
	    		if(DocumentacionAuxiliar.getObservaciones() != null && !"".equalsIgnoreCase(DocumentacionAuxiliar.getObservaciones())){
	    			composicionTablaFicheros +="<td>"+DocumentacionAuxiliar.getObservaciones()+"</td>";
	    		}else{
	    			composicionTablaFicheros +="<td>&nbsp;</td>";
	    		}
	    		String actuacionValidada = UtilidadesHash.getString(actuacion, "VALIDADA")!=null && UtilidadesHash.getString(actuacion, "VALIDADA").equalsIgnoreCase("1")?"1":"0";
		 		if((UtilidadesHash.getString(actuacion, "IDFACTURACION").isEmpty() && actuacionValidada.equals("0") ) && (usr.getUserName().equalsIgnoreCase(String.valueOf(ficheroVo.getUsumodificacion())))){
		 			composicionTablaFicheros +="<td ><img id='iconoboton_borrar1' src='/SIGA/html/imagenes/bborrar_off.gif' style='cursor:pointer;'  name='borrar_1' border='0' " +
			 		"onClick='return borrarFicheroFichaColegial("+DocumentacionAuxiliar.getIdinstitucion()+","+DocumentacionAuxiliar.getIdfichero()+","+DocumentacionAuxiliar.getIddocumentaciondes()+");' onMouseOut='MM_swapImgRestore()' onMouseOver='MM_swapImage('borrar_1','','/SIGA/html/imagenes/bborrar_on.gif',1)'>";
		 		}else{
		 			composicionTablaFicheros +="<td ><img id='iconoboton_borrar2' src='/SIGA/html/imagenes/bborrar_disable.gif' style='cursor:pointer;' name='borrar_2' border='0'>";
		 		}
		 		composicionTablaFicheros +="<img id='iconoboton_download1' src='/SIGA/html/imagenes/bdownload_off.gif' style='cursor:pointer;' " +
		 		"name='iconoFila' border='0' onClick='return downloadFichero("+DocumentacionAuxiliar.getIdinstitucion()+","+DocumentacionAuxiliar.getIdfichero()+");' " +
		 		"onMouseOut='MM_swapImgRestore()' onMouseOver='MM_swapImage('download_1','','/SIGA/html/imagenes/bdownload_on.gif',1)'>" +
		 		"</td></tr>";
		 		
		    	
		    	
		    	
			
		   }
			// Devuelvo la lista de series de facturacion
		    	ArrayList<String> aOptionsListadoDocumentacion = new ArrayList<String>();
		    	aOptionsListadoDocumentacion.add(composicionTablaFicheros);
		    	json.put("aOptionsListadoDocumentacion", aOptionsListadoDocumentacion);
		    	
		    	// json.
				response.setContentType("text/x-json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Content-Type", "application/json");
			    response.setHeader("X-JSON", json.toString());
				response.getWriter().write(json.toString()); 	
	     }else {
	    	 throw new Exception("No hay doumentación asociada");
	     }
	}
	protected String descargarFichero(ActionMapping mapping,	MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		
		try {			
			//Para obtener el fichero, primero obtengo el objeto FicheroVo a partir del idFichero y idInstitución, una vez se tenga la información del fichero se obtiene un objeto File con la información.
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			BusinessManager bm = getBusinessManager();
			DefinirDocumentacionDesignaForm definirDocumentacionDesignaForm = (DefinirDocumentacionDesignaForm) formulario;
			FicherosService ficherosService = (FicherosService)getBusinessManager().getService(FicherosService.class);
			DocumentacionDesignaService documentacionService = (DocumentacionDesignaService) bm.getService(DocumentacionDesignaService.class);
			FicheroVo ficheroVo = new FicheroVo();
			ficheroVo.setIdfichero(Long.parseLong(definirDocumentacionDesignaForm.getIdFichero()));
			ficheroVo.setIdinstitucion(Short.parseShort(definirDocumentacionDesignaForm.getIdInstitucion()));
			ficheroVo = ficherosService.getFichero(ficheroVo);
		
			DocumentacionDesignaVo objectVo = new DocumentacionDesignaVo();
			objectVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			objectVo.setDirectorioArchivo(ficheroVo.getDirectorio());
			objectVo.setNombreArchivo(ficheroVo.getNombre());
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