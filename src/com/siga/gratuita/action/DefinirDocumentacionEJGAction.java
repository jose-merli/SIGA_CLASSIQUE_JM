package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.services.scs.DocumentacionEjgService;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDocumentacionEJGForm;
import com.siga.gratuita.form.service.DocumentacionEjgVoService;

import es.satec.businessManager.BusinessManager;

/**
 * Maneja las acciones que se pueden realizar sobre la tabla
 * SCS_DOCUMENTACIONEJG
 */
public class DefinirDocumentacionEJGAction extends MasterAction {

	
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

	
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			String idDocumentacion = (String)ocultos.get(0);
			String idDocumento = (String)ocultos.get(1);
			String idTipoDocumento = (String)ocultos.get(2);
			String presentador = (String)ocultos.get(3);
			String numEjg = (String)ocultos.get(4);
			
			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdDocumento(idDocumento);
			definirDocumentacionEJGForm.setIdTipoDocumento(idTipoDocumento);
			definirDocumentacionEJGForm.setPresentador(presentador);
			definirDocumentacionEJGForm.setNumEjg(numEjg);

			
	
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(voService.getForm2Vo(definirDocumentacionEJGForm));
			definirDocumentacionEJGForm =voService.getVo2Form(documentacionEjgVo);
			definirDocumentacionEJGForm.setNumEjg(numEjg);
			definirDocumentacionEJGForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionEJGForm",definirDocumentacionEJGForm );
			
			List<String> presentadorSelected = new ArrayList<String>();
			presentadorSelected.add(definirDocumentacionEJGForm.getPresentador());
			request.setAttribute("presentadorSelected",presentadorSelected );
			
			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(definirDocumentacionEJGForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected );
			request.setAttribute("idTipoDocumentoJson", UtilidadesString.createJsonString("idTipoDocumento", definirDocumentacionEJGForm.getIdTipoDocumento())) ;
			
			List<String> idDocumentoSelected = new ArrayList<String>();
			idDocumentoSelected.add(definirDocumentacionEJGForm.getIdDocumento());
			request.setAttribute("idDocumentoSelected",idDocumentoSelected );
			request.setAttribute("accionModo", "editar");


			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_FicherosDocumentacionEjg.do",null,request);
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
	
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			String idDocumentacion = (String)ocultos.get(0);
			String idDocumento = (String)ocultos.get(1);
			String idTipoDocumento = (String)ocultos.get(2);
			String presentador = (String)ocultos.get(3);
			String numEjg = (String)ocultos.get(4);
			
			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdDocumento(idDocumento);
			definirDocumentacionEJGForm.setIdTipoDocumento(idTipoDocumento);
			definirDocumentacionEJGForm.setPresentador(presentador);
			definirDocumentacionEJGForm.setNumEjg(numEjg);

			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(voService.getForm2Vo(definirDocumentacionEJGForm));
			definirDocumentacionEJGForm =voService.getVo2Form(documentacionEjgVo);
			definirDocumentacionEJGForm.setNumEjg(numEjg);
			definirDocumentacionEJGForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionEJGForm",definirDocumentacionEJGForm );
			
			List<String> presentadorSelected = new ArrayList<String>();
			presentadorSelected.add(definirDocumentacionEJGForm.getPresentador());
			request.setAttribute("presentadorSelected",presentadorSelected );
			
			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(definirDocumentacionEJGForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected",idTipoDocumentoSelected );
			request.setAttribute("idTipoDocumentoJson", UtilidadesString.createJsonString("idTipoDocumento", definirDocumentacionEJGForm.getIdTipoDocumento())) ;
			
			List<String> idDocumentoSelected = new ArrayList<String>();
			idDocumentoSelected.add(definirDocumentacionEJGForm.getIdDocumento());
			request.setAttribute("idDocumentoSelected",idDocumentoSelected );
			
			
			request.setAttribute("accionModo", "ver");
			
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_FicherosDocumentacionEjg.do",null,request);
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
			UsrBean usr = (UsrBean) request.getSession()
					.getAttribute("USRBEAN");
			Hashtable miHash = new Hashtable();

			miHash.put("ANIO", miForm.getAnio());
			miHash.put("NUMERO", miForm.getNumero());
			miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
			miHash.put("IDINSTITUCION", usr.getLocation());
			ScsEJGAdm admEjg = new ScsEJGAdm(this.getUserBean(request));
			Vector v3 = admEjg.selectByPK(miHash);
			if (v3 != null && v3.size() > 0) {
				ScsEJGBean b = (ScsEJGBean) v3.get(0);
				miHash.put("NUMEJG", b.getNumEJG());
				miForm.setNumEjg(b.getNumEJG());
			}
			
			
			request.setAttribute("presentadorSelected",new ArrayList<String>() );
			request.setAttribute("idTipoDocumentoSelected",new ArrayList<String>() );
			request.setAttribute("idTipoDocumentoJson", "") ;
			request.setAttribute("idDocumentoSelected",new ArrayList<String>() );
			request.setAttribute("accionModo", "editar");
			miForm.setModo("insertar");
			try {
				String permisoFicheros = testAccess(request.getContextPath()+"/JGR_FicherosDocumentacionEjg.do",null,request);
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
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = voService.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			documentacionEjgService.insert(documentacionEjgVo);
			
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.updated.success", request);
	}
	/**
	 * Metodo que modifica el registro de la tabla e inserta el fichero, si lo ha selecionado, en el sistema de ficheros	
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = voService.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			DocumentacionEjgVo documentacionEjgVoOld = (DocumentacionEjgVo) documentacionEjgVo.clone();
			documentacionEjgVoOld.setIdtipodocumento(Short.parseShort(definirDocumentacionEJGForm.getIdTipoDocumentoAnterior()));
			documentacionEjgVoOld.setIddocumento(Short.parseShort(definirDocumentacionEJGForm.getIdDocumentoAnterior()));
			documentacionEjgVoOld.setPresentador(Long.parseLong(definirDocumentacionEJGForm.getPresentadorAnterior()));
			
			documentacionEjgService.update(documentacionEjgVoOld,documentacionEjgVo);
			
			

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
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

			
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			
			definirDocumentacionEJGForm.setIdInstitucion(usr.getLocation());
			definirDocumentacionEJGForm.setIdDocumentacion((String)ocultos.get(0));
			definirDocumentacionEJGForm.setIdTipoDocumento((String)ocultos.get(2));
			definirDocumentacionEJGForm.setIdDocumento((String)ocultos.get(1));
			definirDocumentacionEJGForm.setPresentador((String)ocultos.get(3));
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = voService.getForm2Vo(definirDocumentacionEJGForm);
			
			documentacionEjgService.borrar(documentacionEjgVo);
			
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		
		
		

		return exitoRefresco("messages.deleted.success", request);
	}

	
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
				.getUserBean(request));
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();

		miHash.put("ANIO", miForm.getAnio());
		miHash.put("NUMERO", miForm.getNumero());
		miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
		miHash.put("IDINSTITUCION", usr.getLocation());
		
		request.setAttribute("DATOSEJG", miHash);

		try {
			
			ScsEJGAdm admEjg = new ScsEJGAdm(this.getUserBean(request));
			Vector v3 = admEjg.selectByPK(miHash);
			if (v3 != null && v3.size() > 0) {
				ScsEJGBean b = (ScsEJGBean) v3.get(0);
				miHash.put("NUMEJG", b.getNumEJG());
				request.setAttribute("NUMEJG",b.getNumEJG());
			}
			
			
			v = admBean.buscar(miHash);
			request.setAttribute("resultado", v);
			request.setAttribute("accion", formulario.getModo());
			//aalg: Inc_10313.
			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),"DEJG",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;				
			}

			request.setAttribute("informeUnico", informeUnico);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		/*
		 * "DATABACKUP" se usa habitualmente así que en primero lugar borramos
		 * esta variable
		 */
		request.getSession().removeAttribute("DATABACKUP");

		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
				.getUserBean(request));
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();

		miHash.put("ANIO", request.getParameter("ANIO").toString());
		miHash.put("NUMERO", request.getParameter("NUMERO").toString());
		miHash.put("IDTIPOEJG", request.getParameter("IDTIPOEJG").toString());
		miHash.put("IDINSTITUCION", request.getParameter("IDINSTITUCION")
				.toString());
		String numEjg = request.getParameter("codigoDesignaNumEJG");
		miHash.put("NUMEJG", numEjg);
		
		
		try {
			v = admBean.buscar(miHash);
			request.setAttribute("resultado", v);
			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),"DEJG",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;
				
			}

			request.setAttribute("informeUnico", informeUnico);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";

	}
	protected String downloadFichero(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();			
			DocumentacionEjgVo documentacionEjgVo = voService.getForm2Vo(definirDocumentacionEJGForm);
			File file = documentacionEjgService.getFile(documentacionEjgVo);
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getPath());
			request.setAttribute("accion", "");
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return forward;
		
		
	}
	protected String borrarFichero(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();		
			DocumentacionEjgVo documentacionEjgVo = voService.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsumodificacion(Integer.parseInt(usr.getUserName()));
			documentacionEjgService.borrarFichero(documentacionEjgVo);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.deleted.success", request);
	}


	
	
	
}