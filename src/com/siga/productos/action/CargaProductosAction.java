package com.siga.productos.action;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.services.pys.CargaMasivaProductos;
import org.redabogacia.sigaservices.app.vo.cen.CargaMasivaDatosVo;
import org.redabogacia.sigaservices.app.vo.pys.CargaMasivaDatosProductosVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.service.CargaMasivaDatosProductosVoService;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.CargaProductosForm;

public class CargaProductosAction extends MasterAction {

	/**
	 * Redefinicion de la funcion executeInternal para controlar las nuevas acciones confirmar y denegar
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			String mapDestino = "exception";
			MasterForm miForm = null;

			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}

				String accion = miForm.getModo();
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
					mapDestino = abrir(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("getAjaxBusqueda")) {
					mapDestino = getAjaxBusqueda(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("parseExcelFile")) {
					mapDestino = parseExcelFile(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("downloadExample")) {
					mapDestino = downloadExample(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("downloadExcelError")) {
					mapDestino = downloadExcelError(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("downloadExcelProcessed")) {
					mapDestino = downloadExcelProcessed(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("downloadExcelLog")) {
					mapDestino = downloadExcelLog(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("processExcelFile")) {
					mapDestino = processExcelFile(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("volver")) {
					mapDestino = volver(mapping, miForm, request, response);

				} else
					return super.executeInternal(mapping, formulario, request, response);

			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}
			return mapping.findForward(mapDestino);
			
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.productos" });
		}
	}

	/**
	 * Funcion que atiende la accion abrir.
	 * 
	 * @param mapping
	 *            - Mapeo de los struts
	 * @param formulario
	 *            - Action Form asociado a este Action
	 * @param request
	 *            - objeto llamada HTTP
	 * @param response
	 *            - objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception SIGAException
	 *                En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "abrir";
	}

	private String volver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaProductosForm miForm = (CargaProductosForm) formulario;
		miForm.clear();
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
		CargaProductosForm cargaMasivaProductosForm = (CargaProductosForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		if (cargaMasivaProductosForm != null) {
			miForm.setFechaCarga(cargaMasivaProductosForm.getFechaCarga());
			miForm.setIdInstitucion(cargaMasivaProductosForm.getIdInstitucion());
		}
		miForm.setModo("vuelta");
		return "abrir";
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
	private String getAjaxBusqueda(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaProductosForm miForm = (CargaProductosForm) formulario;
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			miForm.setIdInstitucion(idInstitucion);
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
			request.getSession().setAttribute(identificadorFormularioBusqueda, miForm.clone());

			CargaMasivaProductos cargaMasiva = (CargaMasivaProductos) getBusinessManager().getService(CargaMasivaProductos.class);
			VoUiService<CargaProductosForm, CargaMasivaDatosProductosVo> voService = new CargaMasivaDatosProductosVoService();
			List<CargaMasivaDatosProductosVo> cargaMasivaDatosProductosVos = cargaMasiva.getCargasMasivas(voService.getForm2Vo(miForm));
			request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosProductosVos));
		
		} catch (BusinessException e) {
			throwExcp("messages.general.error", new String[] { "modulo.productos" }, e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.productos" }, e, null);
		}

		return "listado";
	}

	private String downloadExcelError(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		CargaProductosForm miForm = (CargaProductosForm) formulario;
		CargaMasivaProductos cargaMasiva = (CargaMasivaProductos) getBusinessManager().getService(CargaMasivaProductos.class);
		UsrBean usrBean = this.getUserBean(request);
		File errorFile;
		try {
			CargaMasivaDatosProductosVo cargaMasivaDatosProdVo = new CargaMasivaDatosProductosVo();
			cargaMasivaDatosProdVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			cargaMasivaDatosProdVo.setExcelBytes(SIGAServicesHelper.getBytes(miForm.getRutaFichero()));
			cargaMasivaDatosProdVo.setCodIdioma(usrBean.getLanguage());

			errorFile = cargaMasiva.getErrorExcelFile(cargaMasivaDatosProdVo);
			request.setAttribute("nombreFichero", errorFile.getName());
			request.setAttribute("rutaFichero", errorFile.getPath());
			request.setAttribute("accion", "");

		} catch (BusinessException e) {
			throwExcp(e.getMessage(), e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return forward;

	}

	private String downloadExcelProcessed(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			CargaProductosForm miForm = (CargaProductosForm) formulario;
			FicherosService ficherosService = (FicherosService) getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(miForm.getIdFichero()), Short.valueOf(miForm.getIdInstitucion()));
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

	protected String downloadExcelLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			CargaProductosForm miForm = (CargaProductosForm) formulario;
			miForm.setCodIdioma(this.getUserBean(request).getLanguage());
			FicherosService ficherosService = (FicherosService) getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(miForm.getIdFicheroLog()), Short.valueOf(miForm.getIdInstitucion()));
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

	private String processExcelFile(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaProductosForm miForm = (CargaProductosForm) formulario;
		CargaMasivaProductos cargaMasiva = (CargaMasivaProductos) getBusinessManager().getService(CargaMasivaProductos.class);
		UsrBean usrBean = this.getUserBean(request);
		try {
			CargaMasivaDatosProductosVo cargaMasivaDatosProdVo = new CargaMasivaDatosProductosVo();
			cargaMasivaDatosProdVo.setCodIdioma(usrBean.getLanguage());
			cargaMasivaDatosProdVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			String nombreFichero = miForm.getNombreFichero().substring(miForm.getNombreFichero().lastIndexOf("\\") + 1);
			cargaMasivaDatosProdVo.setNombreFichero(nombreFichero);
			cargaMasivaDatosProdVo.setUsuario(usrBean.getUserName());
			cargaMasivaDatosProdVo.setExcelBytes(SIGAServicesHelper.getBytes(miForm.getRutaFichero()));
			cargaMasiva.processExcelFile(cargaMasivaDatosProdVo);

		} catch (BusinessException e) {
			throwExcp(e.getMessage(), e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoRefresco("messages.inserted.success", request);
	}

	private String downloadExample(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usrBean = this.getUserBean(request);
		CargaMasivaProductos cargaMasiva = (CargaMasivaProductos) getBusinessManager().getService(CargaMasivaProductos.class);
		Vector<Hashtable<String, Object>> datosVector = new Vector<Hashtable<String, Object>>();
		Hashtable<String, Object> datosHashtable = new Hashtable<String, Object>();
		try {
			datosHashtable.put(CargaMasivaDatosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.infoGeneral"));
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NUM_COLEGIADO_CLIENTE, "nnnnnn");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NIF_CLIENTE, "nnnnnnnna");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_APELLIDOS_CLIENTE, "aaaaaaaaaaa aaaaaaaaa");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NOMBRE_CLIENTE, "aaaaaaaaaaaaaaaaa");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_CANTIDAD_PRODUCTO, "nn");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NOMBRE_PRODUCTO, "aaaaaaaaaaaaaaaa");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_CATEGORIA_PRODUCTO, "nn");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_TIPO_PRODUCTO, "nn");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_PRODUCTO, "nn");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_FECHA_COMPRA, "dd/mm/yyyy");
			datosVector.add(datosHashtable);

			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info1"));
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NUM_COLEGIADO_CLIENTE, "Opcional. Si nulo NIF_CLIENTE requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NIF_CLIENTE, "Opcional. Si nulo NUM_COLEGIADO_CLIENTE requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_APELLIDOS_CLIENTE, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NOMBRE_CLIENTE, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_CANTIDAD_PRODUCTO, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_NOMBRE_PRODUCTO, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_CATEGORIA_PRODUCTO, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_TIPO_PRODUCTO, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_ID_PRODUCTO, "Requerido");
			datosHashtable.put(CargaMasivaDatosProductosVo.C_FECHA_COMPRA, "Opcional. Si nulo fecha de hoy");
			datosVector.add(datosHashtable);
			
			/** SEGUNDA CLAUSULA **/
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info2"));
			datosVector.add(datosHashtable);					
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info21"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info22"));
			datosVector.add(datosHashtable);		
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info23"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info231"));
			datosVector.add(datosHashtable);		
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info232"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info233"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info234"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info235"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info24"));
			datosVector.add(datosHashtable);				
			
			/** TERCERA CLAUSULA **/
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info3"));
			datosVector.add(datosHashtable);		

			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info31"));
			datosVector.add(datosHashtable);	
			
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info32"));
			datosVector.add(datosHashtable);		
			
			/** CUARTA CLAUSULA **/
			datosHashtable = new Hashtable<String, Object>();
			datosHashtable.put(CargaMasivaDatosProductosVo.C_INFO, UtilidadesString.getMensajeIdioma(usrBean, "pys.cargaProductos.info4"));
			datosVector.add(datosHashtable);			
			
			File exampleFile = cargaMasiva.createExcelFile(CargaMasivaProductos.CAMPOSEJEMPLO, datosVector);
			request.setAttribute("nombreFichero", exampleFile.getName());
			request.setAttribute("rutaFichero", exampleFile.getPath());
			request.setAttribute("accion", "");
			
		} catch (BusinessException e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return "descargaFichero";
	}

	private String parseExcelFile(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaProductosForm miForm = (CargaProductosForm) formulario;
		CargaMasivaProductos cargaMasiva = (CargaMasivaProductos) getBusinessManager().getService(CargaMasivaProductos.class);
		UsrBean usrBean = this.getUserBean(request);
		miForm.setCodIdioma(usrBean.getLanguage());

		try {
			if (miForm.getTheFile() != null && miForm.getTheFile().getFileData() != null && miForm.getTheFile().getFileData().length > 0) {
				File file = SIGAServicesHelper.createTemporalFile(miForm.getTheFile().getFileData(), "xls");
				miForm.setRutaFichero(file.getAbsolutePath());
				miForm.setNombreFichero(miForm.getTheFile().getFileName());
				CargaMasivaDatosProductosVo cargaMasivaDatosProdVo = new CargaMasivaDatosProductosVo();
				cargaMasivaDatosProdVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
				cargaMasivaDatosProdVo.setExcelBytes(miForm.getTheFile().getFileData());
				cargaMasivaDatosProdVo.setUsuario(usrBean.getUserName());

				List<CargaMasivaDatosProductosVo> cargaMasivaDatosGFList = cargaMasiva.parseExcelFile(cargaMasivaDatosProdVo);
				VoUiService<CargaProductosForm, CargaMasivaDatosProductosVo> voService = new CargaMasivaDatosProductosVoService();
				request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosGFList));
				String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda, getClass().getName());
				request.getSession().setAttribute(identificadorFormularioBusqueda, miForm.clone());
			}

		} catch (BusinessException e) {
			throwExcp(e.getMessage(), e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		return "listadoResumen";
	}

}
