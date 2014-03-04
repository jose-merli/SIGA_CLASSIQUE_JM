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
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
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
import com.siga.beans.ScsPersonaJGBean;
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
			String idInstitucion = (String)ocultos.get(1);
			
			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdInstitucion(idInstitucion);
//			definirDocumentacionEJGForm.setNumEjg(numEjg);

			
	
			
			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(voService.getForm2Vo(definirDocumentacionEJGForm));
			definirDocumentacionEJGForm =voService.getVo2Form(documentacionEjgVo);
//			definirDocumentacionEJGForm.setNumEjg(numEjg);
			definirDocumentacionEJGForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionEJGForm",definirDocumentacionEJGForm );
			
			List<String> presentadorSelected = new ArrayList<String>();
			
				presentadorSelected.add(documentacionEjgVo.getIdPresentador());
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
			
			
			
			
			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdDocumento(idDocumento);

			BusinessManager bm = getBusinessManager();
			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			DocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(voService.getForm2Vo(definirDocumentacionEJGForm));
			definirDocumentacionEJGForm =voService.getVo2Form(documentacionEjgVo);
			definirDocumentacionEJGForm.setModo("abrir");
			request.setAttribute("DefinirDocumentacionEJGForm",definirDocumentacionEJGForm );
			
			List<String> presentadorSelected = new ArrayList<String>();
			presentadorSelected.add(documentacionEjgVo.getIdPresentador());
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
			if(definirDocumentacionEJGForm.getIdPresentadorAnterior()!=null && !definirDocumentacionEJGForm.getIdPresentadorAnterior().equals("")){
				String[] idsPresentador = definirDocumentacionEJGForm.getIdPresentadorAnterior().split("IDMAESTROPRESENTADOR_");
				if(idsPresentador.length>1)
					documentacionEjgVoOld.setIdmaestropresentador(Short.valueOf(idsPresentador[1]));
				else{
					idsPresentador = definirDocumentacionEJGForm.getIdPresentador().split("IDPERSONAJG_");
					documentacionEjgVoOld.setPresentador(Long.valueOf(idsPresentador[1]));
				}
			}
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
		
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		ScsEJGAdm admi = new ScsEJGAdm(this.getUserBean(request)); 
		Hashtable hTitulo = admi.getTituloPantallaEJG(miForm.getIdInstitucion(),	miForm.getAnio(),miForm.getNumero(), miForm.getIdTipoEJG());
		StringBuffer solicitante = new StringBuffer();
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE));
		solicitante.append(" ");
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1));
		solicitante.append(" ");
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2));
		miForm.setSolicitante(solicitante.toString());
		miForm.setNumEjg((String)hTitulo.get(ScsEJGBean.C_NUMEJG));
		
		
		request.setAttribute("accion", formulario.getModo());
		return abrir(this.getUserBean(request),miForm, request);

	}

	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
					throws SIGAException {
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		miForm.setIdInstitucion(request.getParameter("IDINSTITUCION").toString());
		miForm.setAnio(request.getParameter("ANIO").toString());
		miForm.setIdTipoEJG(request.getParameter("IDTIPOEJG").toString());
		miForm.setNumero(request.getParameter("NUMERO"));
		miForm.setNumEjg(request.getParameter("codigoDesignaNumEJG"));
		miForm.setSolicitante(request.getParameter("solicitante").toString());
		
		
		
		
		request.getSession().removeAttribute("DATABACKUP");
		return abrir(this.getUserBean(request),miForm, request);


	}

	
	protected String abrir( UsrBean usrBean,DefinirDocumentacionEJGForm definirDocumentacionEJGForm,HttpServletRequest request) throws SIGAException {

		try {

			Vector v = new Vector();

			if(definirDocumentacionEJGForm.getNumEjg()!=null && !definirDocumentacionEJGForm.getNumEjg().equals(""))
				request.setAttribute("NUMEJG",definirDocumentacionEJGForm.getNumEjg());
			else{
				Hashtable miHash = new Hashtable();

				miHash.put("ANIO", definirDocumentacionEJGForm.getAnio());
				miHash.put("NUMERO", definirDocumentacionEJGForm.getNumero());
				miHash.put("IDTIPOEJG", definirDocumentacionEJGForm.getIdTipoEJG());
				miHash.put("IDINSTITUCION", definirDocumentacionEJGForm.getIdInstitucion());
				ScsEJGAdm admEjg = new ScsEJGAdm(this.getUserBean(request));
				Vector v3 = admEjg.selectByPK(miHash);
				if (v3 != null && v3.size() > 0) {
					ScsEJGBean b = (ScsEJGBean) v3.get(0);
					definirDocumentacionEJGForm.setNumEjg(b.getNumEJG());
				}

			}

			BusinessManager bm = getBusinessManager();

			DocumentacionEjgService documentacionEjgService = (DocumentacionEjgService) bm.getService(DocumentacionEjgService.class);

			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			List<DocumentacionEjgVo> documentacionEjgVoList = documentacionEjgService.getListadoDocumentacionEJG(voService.getForm2Vo(definirDocumentacionEJGForm),usrBean.getLanguage());

			request.setAttribute("resultado", documentacionEjgVoList);

			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(usrBean);
			Vector informeBeans=adm.obtenerInformesTipo(usrBean.getLocation(),"DEJG",null, null);
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
			

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
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