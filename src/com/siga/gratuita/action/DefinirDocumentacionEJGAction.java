package com.siga.gratuita.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo;
import org.redabogacia.sigaservices.app.vo.scs.EstadoEjgVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsPersonaJGBean;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDocumentacionEJGForm;
import com.siga.gratuita.form.DefinirEstadosEJGForm;
import com.siga.gratuita.form.service.DocumentacionEjgVoService;
import com.siga.gratuita.service.SIGADocumentacionEjgService;
import com.siga.gratuita.vos.SIGADocumentacionEjgVo;

import es.satec.businessManager.BusinessManager;

/**
 * Maneja las acciones que se pueden realizar sobre la tabla SCS_DOCUMENTACIONEJG
 */
public class DefinirDocumentacionEJGAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;
		try {

			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if (modo != null)
						accion = modo;
					if (accion != null && accion.equalsIgnoreCase("downloadFichero")) {
						mapDestino = downloadFichero(mapping, miForm, request, response);
					} else if (accion != null && accion.equalsIgnoreCase("borrarfichero")) {
						mapDestino = borrarFichero(mapping, miForm, request, response);
					} 
					else if (accion != null && accion.equalsIgnoreCase("envioDocumento")){
						return  mapping.findForward (envioDocumento(mapping, miForm, request, response));
					}
					
					else {
						return super.executeInternal(mapping, formulario, request, response);
					}

				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			String idDocumentacion = (String) ocultos.get(0);
			String idInstitucion = (String) ocultos.get(1);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdInstitucion(idInstitucion);

			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(this.getForm2Vo(definirDocumentacionEJGForm),usr);
			definirDocumentacionEJGForm = this.getVo2Form(documentacionEjgVo);
			definirDocumentacionEJGForm.setModo("modificar");
			request.setAttribute("DefinirDocumentacionEJGForm", definirDocumentacionEJGForm);

			List<String> presentadorSelected = new ArrayList<String>();
			presentadorSelected.add(documentacionEjgVo.getIdPresentador());
			request.setAttribute("presentadorSelected", presentadorSelected);
			String idPres = "";
			if (documentacionEjgVo.getIdPresentador() != null) {
				idPres = documentacionEjgVo.getIdPresentador().substring(documentacionEjgVo.getIdPresentador().indexOf("_") + 1);
			}
			String paramsPresentadorJSON = "{\"idpresentador\":\"" + idPres + "\"}";
			request.setAttribute("paramsPresentadorJSON", paramsPresentadorJSON);

			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(definirDocumentacionEJGForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected", idTipoDocumentoSelected);
			request.setAttribute("idTipoDocumentoJson", UtilidadesString.createJsonString("idTipoDocumento", definirDocumentacionEJGForm.getIdTipoDocumento()));

			List<String> idDocumentoSelected = new ArrayList<String>();
			idDocumentoSelected.add(definirDocumentacionEJGForm.getIdDocumento());
			request.setAttribute("idDocumentoSelected", idDocumentoSelected);
			request.setAttribute("accionModo", "editar");

			// pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", false);
			try {
				String permisoFicheros = testAccess(request.getContextPath() + "/JGR_FicherosDocumentacionEjg.do", null, request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			} finally {
				// hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);
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
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			String idDocumentacion = (String) ocultos.get(0);
			String idDocumento = (String) ocultos.get(1);

			definirDocumentacionEJGForm.setIdDocumentacion(idDocumentacion);
			definirDocumentacionEJGForm.setIdDocumento(idDocumento);

			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = documentacionEjgService.getDocumentacionEjg(this.getForm2Vo(definirDocumentacionEJGForm),usr);
			definirDocumentacionEJGForm = this.getVo2Form(documentacionEjgVo);
			definirDocumentacionEJGForm.setModo("abrir");
			request.setAttribute("DefinirDocumentacionEJGForm", definirDocumentacionEJGForm);

			List<String> presentadorSelected = new ArrayList<String>();
			presentadorSelected.add(documentacionEjgVo.getIdPresentador());
			request.setAttribute("presentadorSelected", presentadorSelected);
			String idPres = "";
			if (documentacionEjgVo.getIdPresentador() != null) {
				idPres = documentacionEjgVo.getIdPresentador().substring(documentacionEjgVo.getIdPresentador().indexOf("_") + 1);
			}
			String paramsPresentadorJSON = "{\"idpresentador\":\"" + idPres + "\"}";
			request.setAttribute("paramsPresentadorJSON", paramsPresentadorJSON);

			List<String> idTipoDocumentoSelected = new ArrayList<String>();
			idTipoDocumentoSelected.add(definirDocumentacionEJGForm.getIdTipoDocumento());
			request.setAttribute("idTipoDocumentoSelected", idTipoDocumentoSelected);
			request.setAttribute("idTipoDocumentoJson", UtilidadesString.createJsonString("idTipoDocumento", definirDocumentacionEJGForm.getIdTipoDocumento()));

			List<String> idDocumentoSelected = new ArrayList<String>();
			idDocumentoSelected.add(definirDocumentacionEJGForm.getIdDocumento());
			request.setAttribute("idDocumentoSelected", idDocumentoSelected);

			request.setAttribute("accionModo", "ver");
			// pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", false);
			try {
				String permisoFicheros = testAccess(request.getContextPath() + "/JGR_FicherosDocumentacionEjg.do", null, request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			} finally {
				// hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con " . .Fiesta" para que redirija a la pantalla de inserción.
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Hashtable miHash = new Hashtable();

			miHash.put("ANIO", miForm.getAnio());
			miHash.put("NUMERO", miForm.getNumero());
			miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
			miHash.put("IDINSTITUCION", miForm.getIdInstitucion());
			ScsEJGAdm admEjg = new ScsEJGAdm(this.getUserBean(request));
			Vector v3 = admEjg.selectByPK(miHash);
			if (v3 != null && v3.size() > 0) {
				ScsEJGBean b = (ScsEJGBean) v3.get(0);
				miHash.put("NUMEJG", b.getNumEJG());
				miForm.setNumEjg(b.getNumEJG());
			}

			request.setAttribute("presentadorSelected", new ArrayList<String>());
			request.setAttribute("idTipoDocumentoSelected", new ArrayList<String>());
			request.setAttribute("idTipoDocumentoJson", "");
			String paramsPresentadorJSON = "{\"idpresentador\":\"-1\"}";
			request.setAttribute("paramsPresentadorJSON", paramsPresentadorJSON);
			request.setAttribute("idDocumentoSelected", new ArrayList<String>());
			request.setAttribute("accionModo", "editar");
			miForm.setModo("insertar");
			// pasamos si es obligatorio el archivo
			request.setAttribute("fileRequired", false);
			try {
				String permisoFicheros = testAccess(request.getContextPath() + "/JGR_FicherosDocumentacionEjg.do", null, request);
				request.setAttribute("permisoFicheros", permisoFicheros);
			} catch (ClsExceptions e) {
				throw new SIGAException(e.getMsg());
			} finally {
				// hacemos lo siguiente para setear el permiso de esta accion
				testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

			String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
			int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
			if (definirDocumentacionEJGForm.getTheFile() != null && definirDocumentacionEJGForm.getTheFile().getFileSize() > maxSizebytes) {
				throw new SIGAException("messages.general.file.maxsize", new String[] { (maxsize) });
			}

			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsuMod(Integer.parseInt(usr.getUserName()));

			if (documentacionEjgVo.getIdDocumento() != null) {
				// Insertamos primero el fichero para quedarnos con su referencia
				if (documentacionEjgVo.getFichero() != null && documentacionEjgVo.getFichero().length > 0) {
					documentacionEjgService.uploadFile(documentacionEjgVo,usr);
				}
			}

			documentacionEjgService.insert(documentacionEjgVo,usr);

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
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			UserTransaction tx = (UserTransaction) usr.getTransaction();
			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsuMod(Integer.parseInt(usr.getUserName()));
			SIGADocumentacionEjgVo documentacionEjgVoOld = (SIGADocumentacionEjgVo) documentacionEjgVo.clone();
			documentacionEjgVoOld.setIdTipoDocumento(definirDocumentacionEJGForm.getIdTipoDocumentoAnterior());
			documentacionEjgVoOld.setIdDocumento(definirDocumentacionEJGForm.getIdDocumentoAnterior());
			if (definirDocumentacionEJGForm.getIdPresentadorAnterior() != null && !definirDocumentacionEJGForm.getIdPresentadorAnterior().equals("")) {
				String[] idsPresentador = definirDocumentacionEJGForm.getIdPresentadorAnterior().split("IDMAESTROPRESENTADOR_");
				if (idsPresentador.length > 1)
					documentacionEjgVoOld.setIdPresentadorMaestro(idsPresentador[1]);
				else {
					idsPresentador = definirDocumentacionEJGForm.getIdPresentadorAnterior().split("IDPERSONAJG_");
					documentacionEjgVoOld.setPresentador(idsPresentador[1]);
				}
			}
			
			tx.begin();
			
			// Subimos primero el fichero
			if (documentacionEjgVo.getFichero() != null) {
				ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
				int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
				if (definirDocumentacionEJGForm.getTheFile() != null && definirDocumentacionEJGForm.getTheFile().getFileSize() > maxSizebytes) {
					throw new SIGAException("messages.general.file.maxsize", new String[] { (maxsize) });
				}

				documentacionEjgService.uploadFile(documentacionEjgVo,usr);
			}

			documentacionEjgService.update(documentacionEjgVoOld, documentacionEjgVo,usr);

			if (documentacionEjgVo.isBorrarFichero()) {
				documentacionEjgService.deleteFile(documentacionEjgVoOld,usr);
			}
			
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.updated.success", request);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
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
			UserTransaction tx = (UserTransaction) usr.getTransaction();
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;

			definirDocumentacionEJGForm.setIdInstitucion(usr.getLocation());
			definirDocumentacionEJGForm.setIdDocumentacion((String) ocultos.get(0));

			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);

			tx.begin();
			
			documentacionEjgService.borrar(documentacionEjgVo,usr);
			if (documentacionEjgVo.getIdFichero() != null) {
				documentacionEjgService.deleteFile(documentacionEjgVo,usr);
			}
			
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoRefresco("messages.deleted.success", request);
	}

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		ScsEJGAdm admi = new ScsEJGAdm(this.getUserBean(request));
		String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
		Hashtable hTitulo = admi.getTituloPantallaEJG(miForm.getIdInstitucion(), miForm.getAnio(), miForm.getNumero(), miForm.getIdTipoEJG(), longitudNumEjg);
		StringBuffer solicitante = new StringBuffer();
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE));
		solicitante.append(" ");
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1));
		solicitante.append(" ");
		solicitante.append((String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2));
		miForm.setSolicitante(solicitante.toString());
		miForm.setNumEjg((String) hTitulo.get(ScsEJGBean.C_NUMEJG));

		request.setAttribute("accion", formulario.getModo());
		return abrir(this.getUserBean(request), miForm, request);
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		miForm.setIdInstitucion(request.getParameter("IDINSTITUCION").toString());
		miForm.setAnio(request.getParameter("ANIO").toString());
		miForm.setIdTipoEJG(request.getParameter("IDTIPOEJG").toString());
		miForm.setNumero(request.getParameter("NUMERO"));
		miForm.setNumEjg(request.getParameter("codigoDesignaNumEJG"));
		miForm.setSolicitante(request.getParameter("solicitante").toString());

		GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));

		String prefijoExpedienteCajg;
		try {
			prefijoExpedienteCajg = paramAdm.getValor(miForm.getIdInstitucion(), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG", prefijoExpedienteCajg);
		} catch (ClsExceptions e) {
			throw new SIGAException(e.getMessage());
		}

		request.getSession().removeAttribute("DATABACKUP");
		return abrir(this.getUserBean(request), miForm, request);

	}

	protected String abrir(UsrBean usrBean, DefinirDocumentacionEJGForm definirDocumentacionEJGForm, HttpServletRequest request) throws SIGAException {
		try {
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
				
						SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			definirDocumentacionEJGForm.setComisionAJG(usr.isComision()?"1":"0");
			
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			boolean isColegioConfiguradoEnvioCAJG = ejgService.isColegioConfiguradoEnvioCAJG(documentacionEjgVo.getIdInstitucion().shortValue()); 
			
						
			if(isColegioConfiguradoEnvioCAJG) {
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
					if(documentacionEjgVo.getIdInstitucion().shortValue()==2055 ) {
						if( b.getNumeroCAJG()!=null)
							request.setAttribute("idExpedienteExt", b.getNumeroCAJG());
					}else if(b.getIdExpedienteExt()!=null)
						request.setAttribute("idExpedienteExt", b.getIdExpedienteExt());
						
				}
			}else {
				request.setAttribute("NUMEJG", definirDocumentacionEJGForm.getNumEjg());
			}
			List<SIGADocumentacionEjgVo> documentacionEjgVoList = documentacionEjgService.getListadoDocumentacionEJG(documentacionEjgVo,usrBean.getLanguage(),isColegioConfiguradoEnvioCAJG,usr);
			request.setAttribute("resultado", documentacionEjgVoList);

			String informeUnico = ClsConstants.DB_TRUE;
			AdmInformeAdm adm = new AdmInformeAdm(usrBean);
			Vector informeBeans = adm.obtenerInformesTipo(usrBean.getLocation(), "DEJG", null, null);
			if (informeBeans != null && informeBeans.size() > 1) {
				informeUnico = ClsConstants.DB_FALSE;
			}

			request.setAttribute("informeUnico", informeUnico);

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}

		return "inicio";
	}

	protected String downloadFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);
			File file = documentacionEjgService.getFile(documentacionEjgVo,usr);
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getPath());
			request.setAttribute("accion", "");

		} catch (BusinessException e) {
			throwExcp(e.getMessage(), e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return forward;

	}

	protected String borrarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			UserTransaction tx = (UserTransaction) usr.getTransaction();
			DefinirDocumentacionEJGForm definirDocumentacionEJGForm = (DefinirDocumentacionEJGForm) formulario;
			SIGADocumentacionEjgService documentacionEjgService = new SIGADocumentacionEjgService();	
			VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> voService = new DocumentacionEjgVoService();
			SIGADocumentacionEjgVo documentacionEjgVo = this.getForm2Vo(definirDocumentacionEJGForm);
			documentacionEjgVo.setUsuMod(Integer.parseInt(usr.getUserName()));
			Long idFicheroOld = documentacionEjgVo.getIdFichero();
			
			tx.begin();
			
			documentacionEjgService.borrarFichero(documentacionEjgVo,usr);
			documentacionEjgVo.setIdFichero(idFicheroOld);
			if (idFicheroOld != null)
				documentacionEjgService.deleteFile(documentacionEjgVo,usr);
			
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoModal("messages.deleted.success", request);
	}
	
	public SIGADocumentacionEjgVo getForm2Vo(DefinirDocumentacionEJGForm objectForm) {
		SIGADocumentacionEjgVo documentacionEjgVo = new SIGADocumentacionEjgVo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if (objectForm.getIdInstitucion() != null && !objectForm.getIdInstitucion().equals("")) {
				documentacionEjgVo.setIdInstitucion(Integer.valueOf(objectForm.getIdInstitucion()));
			}
			if (objectForm.getAnio() != null && !objectForm.getAnio().equals(""))
				documentacionEjgVo.setAnio(Integer.valueOf(objectForm.getAnio()));
			if (objectForm.getIdTipoEJG() != null && !objectForm.getIdTipoEJG().equals(""))
				documentacionEjgVo.setIdTipoEJG(Integer.valueOf(objectForm.getIdTipoEJG()));
			if (objectForm.getNumero() != null && !objectForm.getNumero().equals(""))
				documentacionEjgVo.setNumero(Integer.valueOf(objectForm.getNumero()));
			if (objectForm.getIdDocumentacion() != null && !objectForm.getIdDocumentacion().equals(""))
				documentacionEjgVo.setIdDocumentacion(Integer.valueOf(objectForm.getIdDocumentacion()));
			if (objectForm.getIdDocumento() != null && !objectForm.getIdDocumento().equals(""))
				documentacionEjgVo.setIdDocumento(objectForm.getIdDocumento());
			if (objectForm.getIdTipoDocumento() != null && !objectForm.getIdTipoDocumento().equals(""))
				documentacionEjgVo.setIdTipoDocumento(objectForm.getIdTipoDocumento());

			if (objectForm.getIdPresentador() != null && !objectForm.getIdPresentador().equals("")) {
				String[] idsPresentador = objectForm.getIdPresentador().split("IDMAESTROPRESENTADOR_");
				if (idsPresentador.length > 1)
					documentacionEjgVo.setIdPresentadorMaestro(idsPresentador[1]);
				else {
					idsPresentador = objectForm.getIdPresentador().split("IDPERSONAJG_");
					documentacionEjgVo.setPresentador(idsPresentador[1]);
				}
			} else if (objectForm.getPresentador() != null && !objectForm.getPresentador().equals(""))
				documentacionEjgVo.setPresentador(objectForm.getPresentador());
			
			if (objectForm.getNumEjg() != null && !objectForm.getNumEjg().equals(""))
				documentacionEjgVo.setNumEjg(objectForm.getNumEjg());
			if (objectForm.getIdFichero() != null && !objectForm.getIdFichero().equals(""))
				documentacionEjgVo.setIdFichero(Long.valueOf(objectForm.getIdFichero()));
			if (objectForm.getRegEntrada() != null && !objectForm.getRegEntrada().equals(""))
				documentacionEjgVo.setRegEntrada(objectForm.getRegEntrada());
			if (objectForm.getRegSalida() != null && !objectForm.getRegSalida().equals(""))
				documentacionEjgVo.setRegSalida(objectForm.getRegSalida());
			if (objectForm.getDocumentacion() != null && !objectForm.getDocumentacion().equals(""))
				documentacionEjgVo.setDocumentacion(objectForm.getDocumentacion());

			if (objectForm.getFechaLimite() != null && !objectForm.getFechaLimite().equals(""))
				documentacionEjgVo.setFechaLimite(objectForm.getFechaLimite());

			if (objectForm.getFechaEntrega() != null && !objectForm.getFechaEntrega().equals("")) {
				documentacionEjgVo.setFechaEntrega(objectForm.getFechaEntrega());
			}

			if (objectForm.getTheFile() != null && objectForm.getTheFile().getFileData() != null && objectForm.getTheFile().getFileData().length > 0) {
				documentacionEjgVo.setFichero(objectForm.getTheFile().getFileData());
				documentacionEjgVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
				if (objectForm.getTheFile().getFileName().lastIndexOf(".") != -1)
					documentacionEjgVo.setExtensionArchivo(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".") + 1));

			} else {
				documentacionEjgVo.setNombreArchivo(objectForm.getNombreArchivo());
				documentacionEjgVo.setDirectorioArchivo(objectForm.getDirectorioArchivo());
				documentacionEjgVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
				documentacionEjgVo.setExtensionArchivo(objectForm.getExtensionArchivo());
				if (objectForm.getFechaArchivo() != null && !objectForm.getFechaArchivo().equals("")) {
					documentacionEjgVo.setFechaArchivo(sdf.parse(objectForm.getFechaArchivo()));
				}
			}
			documentacionEjgVo.setBorrarFichero(Boolean.parseBoolean(objectForm.getBorrarFichero()));
			if(objectForm.getComisionAJG()!=null && !objectForm.getComisionAJG().equals(""))
				documentacionEjgVo.setComisionAJG(new Short(objectForm.getComisionAJG()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentacionEjgVo;
	}
	
	public DefinirDocumentacionEJGForm getVo2Form(SIGADocumentacionEjgVo objectVo) {
		DefinirDocumentacionEJGForm definirDocumentacionEJGForm = new DefinirDocumentacionEJGForm();
		definirDocumentacionEJGForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		definirDocumentacionEJGForm.setAnio(objectVo.getAnio().toString());
		definirDocumentacionEJGForm.setIdTipoEJG(objectVo.getIdTipoEJG().toString());
		definirDocumentacionEJGForm.setNumero(objectVo.getNumero().toString());
		definirDocumentacionEJGForm.setIdDocumentacion(objectVo.getIdDocumentacion().toString());
		definirDocumentacionEJGForm.setIdDocumento(objectVo.getIdDocumento().toString());
		definirDocumentacionEJGForm.setIdTipoDocumento(objectVo.getIdTipoDocumento().toString());
		definirDocumentacionEJGForm.setIdPresentador(objectVo.getIdPresentador());
		
		if (objectVo.getFechaEntrega() != null && !objectVo.getFechaEntrega().equals(""))
			definirDocumentacionEJGForm.setFechaEntrega(objectVo.getFechaEntrega());
		if (objectVo.getFechaLimite() != null && !objectVo.getFechaLimite().equals(""))
			definirDocumentacionEJGForm.setFechaLimite(objectVo.getFechaLimite());
		if (objectVo.getRegEntrada() != null && !objectVo.getRegEntrada().equals(""))
			definirDocumentacionEJGForm.setRegEntrada(objectVo.getRegEntrada());
		if (objectVo.getRegSalida() != null && !objectVo.getRegSalida().equals(""))
			definirDocumentacionEJGForm.setRegSalida(objectVo.getRegSalida());
		if (objectVo.getDocumentacion() != null && !objectVo.getDocumentacion().equals(""))
			definirDocumentacionEJGForm.setDocumentacion(objectVo.getDocumentacion());

		if (objectVo.getNumEjg() != null && !objectVo.getNumEjg().equals(""))
			definirDocumentacionEJGForm.setNumEjg(objectVo.getNumEjg());
		
		if (objectVo.getIdFichero() != null && !objectVo.getIdFichero().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			definirDocumentacionEJGForm.setIdFichero(objectVo.getIdFichero().toString());
			definirDocumentacionEJGForm.setExtensionArchivo(objectVo.getExtensionArchivo());
			definirDocumentacionEJGForm.setDirectorioArchivo(objectVo.getDirectorioArchivo());
			definirDocumentacionEJGForm.setNombreArchivo(objectVo.getNombreArchivo());
			definirDocumentacionEJGForm.setDescripcionArchivo(objectVo.getDescripcionArchivo());
			if(objectVo.getFechaArchivo() != null && !objectVo.getFechaArchivo().equals("")){
				definirDocumentacionEJGForm.setFechaArchivo(sdf.format(objectVo.getFechaArchivo()));
			}
		}
		if(objectVo.getComisionAJG()!=null)
			definirDocumentacionEJGForm.setComisionAJG(objectVo.getComisionAJG().toString());

		return definirDocumentacionEJGForm;
	}	
	protected String envioDocumento (ActionMapping mapping,
			   MasterForm formulario,
			   HttpServletRequest request,
					   HttpServletResponse response) throws SIGAException
		
		{
		
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario; 
		
		try {
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);
			DocumentacionEjgVo documentacionEjgVo = new DocumentacionEjgVo();
			documentacionEjgVo.setIdtipoejg( Short.valueOf(miForm.getIdTipoEJG() ));
			documentacionEjgVo.setAnio( Short.valueOf(miForm.getAnio ()));
			documentacionEjgVo.setNumero( Long.valueOf(miForm.getNumero ()));
			documentacionEjgVo.setIdinstitucion( Short.valueOf( miForm.getIdInstitucion()));
			
			documentacionEjgVo.setIddocumentacion(Integer.valueOf(miForm.getIdDocumentacion()));
			if(miForm.getIdInstitucion().equalsIgnoreCase("2055")) {
				ejgService.envioDocumento(documentacionEjgVo, AppConstants.OPERACION.ASIGNA_ENVIO_DOCUMENTO, usr.getLanguageInstitucion());
			}else if(miForm.getIdInstitucion().equalsIgnoreCase("2032")) {
				ejgService.envioDocumento(documentacionEjgVo, AppConstants.OPERACION.GV_ENVIO_DOCUMENTO, usr.getLanguageInstitucion());
			}else
				ejgService.envioDocumento(documentacionEjgVo, AppConstants.OPERACION.PERICLES_ENVIA_COMUNICACION, usr.getLanguageInstitucion());
		} catch (BusinessException e) {
			throwExcp ("messages.general.error", e, null);
		}
		return exitoRefresco("messages.inserted.success", request);
	}
}