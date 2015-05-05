package com.siga.censo.action;

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

import org.redabogacia.sigaservices.app.services.cen.CargaMasivaGF;
import org.redabogacia.sigaservices.app.services.cen.impl.CargaMasivaDatosGFImpl;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.vo.cen.CargaMasivaDatosGFVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.censo.form.CargaMasivaGFForm;
import com.siga.censo.form.service.CargaMasivaDatosGFVoService;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
/**
 * 
 * @author jorgeta 
 * @date   04/05/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaGFAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		CargaMasivaGFForm miForm = null;
		try { 
			do {
				miForm = (CargaMasivaGFForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;

					if (accion == null || accion.equals("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxBusqueda")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("parseExcelFile")){
						mapDestino = parseExcelFile (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("downloadExample")){
						mapDestino = downloadExample (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("downloadExcelError")){
						mapDestino = downloadExcelError (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("downloadExcelProcessed")){
						mapDestino = downloadExcelProcessed(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("downloadExcelLog")){
						mapDestino = downloadExcelLog(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("processExcelFile")){
						mapDestino = processExcelFile (mapping, miForm, request, response);
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
		CargaMasivaGFForm miForm = (CargaMasivaGFForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().removeAttribute(identificadorFormularioBusqueda);

		return "inicio";
	}
	private String volver (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException{
		
		CargaMasivaGFForm miForm = (CargaMasivaGFForm) formulario;
		miForm.clear();
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		miForm.setFechaCarga(cargaMasivaGFForm.getFechaCarga());
		miForm.setIdInstitucion(cargaMasivaGFForm.getIdInstitucion());
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
		CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
		String idInstitucion = request.getParameter("idInstitucion");
		String fechaCarga = request.getParameter("fechaCarga");
		cargaMasivaGFForm.setIdInstitucion(idInstitucion);
		if(fechaCarga!=null)
			cargaMasivaGFForm.setFechaCarga(fechaCarga);

		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaGFForm.clone());

		CargaMasivaGF cargaMasivaDatosGF = (CargaMasivaGF) getBusinessManager().getService(CargaMasivaGF.class);
		VoUiService<CargaMasivaGFForm, CargaMasivaDatosGFVo> voService = new CargaMasivaDatosGFVoService();
		List<CargaMasivaDatosGFVo> cargaMasivaDatosGFVos =  cargaMasivaDatosGF.getCargasMasivas(voService.getForm2Vo(cargaMasivaGFForm));
		request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosGFVos));
		
		return "listado";


	}



	private String downloadExcelError (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
		CargaMasivaGF cargaMasivaDatosGF = (CargaMasivaGF) getBusinessManager().getService(CargaMasivaGF.class);
		UsrBean usrBean = this.getUserBean(request);

		File errorFile;
		try {
			CargaMasivaDatosGFVo cargaMasivaDatosGFVo = new CargaMasivaDatosGFVo();
			cargaMasivaDatosGFVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			cargaMasivaDatosGFVo.setExcelBytes(SIGAServicesHelper.getBytes(cargaMasivaGFForm.getRutaFichero()));
			cargaMasivaDatosGFVo.setCodIdioma(usrBean.getLanguage());
			
			errorFile = cargaMasivaDatosGF.getErrorExcelFile(cargaMasivaDatosGFVo);
			request.setAttribute("nombreFichero", errorFile.getName());
			request.setAttribute("rutaFichero", errorFile.getPath());
			request.setAttribute("accion", "");
		} catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}
		return forward;

	}
	private String downloadExcelProcessed (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaGFForm.getIdFichero()), Short.valueOf(cargaMasivaGFForm.getIdInstitucion()));
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


	protected String downloadExcelLog(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		try {
			CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
			cargaMasivaGFForm.setCodIdioma(this.getUserBean(request).getLanguage());
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaGFForm.getIdFicheroLog()), Short.valueOf(cargaMasivaGFForm.getIdInstitucion()));
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

	private String processExcelFile (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
		CargaMasivaGF cargaMasivaDatosGF = (CargaMasivaGF) getBusinessManager().getService(CargaMasivaGF.class);
		UsrBean usrBean = this.getUserBean(request);
		try {

			//			List<CargaMasivaDatosGFVo> cargaMasivaDatosGFList = cargaMasivaDatosGF.parseExcelFile(SIGAServicesHelper.getBytes(cargaMasivaGFForm.getRutaFichero()),Short.valueOf(usrBean.getLocation()));
			CargaMasivaDatosGFVo cargaMasivaDatosGFVo = new CargaMasivaDatosGFVo();
			cargaMasivaDatosGFVo.setCodIdioma(usrBean.getLanguage());
			cargaMasivaDatosGFVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			String nombreFichero = cargaMasivaGFForm.getNombreFichero().substring(cargaMasivaGFForm.getNombreFichero().lastIndexOf("\\")+1); 
			cargaMasivaDatosGFVo.setNombreFichero(nombreFichero);
			cargaMasivaDatosGFVo.setUsuario(usrBean.getUserName());
			cargaMasivaDatosGFVo.setExcelBytes(SIGAServicesHelper.getBytes(cargaMasivaGFForm.getRutaFichero()));

			cargaMasivaDatosGF.processExcelFile(cargaMasivaDatosGFVo); 

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}


		return exitoRefresco("messages.inserted.success",request);


	}
	private String downloadExample (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		CargaMasivaGF cargaMasivaDatosGF = (CargaMasivaGF) getBusinessManager().getService(CargaMasivaGF.class); 
		Vector<Hashtable<String, Object>> datosVector = new Vector<Hashtable<String,Object>>();
		Hashtable<String, Object> datosHashtable =  new Hashtable<String, Object>();
		datosHashtable.put(CargaMasivaDatosGFVo.COLEGIADONUMERO,"nnnnnn");
		datosHashtable.put(CargaMasivaDatosGFVo.PERSONANIF,"nnnnnnnna" );
		datosHashtable.put(CargaMasivaDatosGFVo.C_IDGRUPO,"nnn" );
		datosHashtable.put(CargaMasivaDatosGFVo.ACCION,"A/B" );
		
		
		datosVector.add(datosHashtable);
		datosHashtable =  new Hashtable<String, Object>();
		datosHashtable.put(CargaMasivaDatosGFVo.COLEGIADONUMERO,"Opcional. Si nulo nif/cif requerido");
		datosHashtable.put(CargaMasivaDatosGFVo.PERSONANIF,"Opcional. Si nulo colegiadonumero requerido" );
		datosHashtable.put(CargaMasivaDatosGFVo.C_IDGRUPO, "Requerido");
		datosHashtable.put(CargaMasivaDatosGFVo.ACCION, "Requerido");
		
		
		datosVector.add(datosHashtable);
			 
			 
		File exampleFile =  cargaMasivaDatosGF.createExcelFile(CargaMasivaDatosGFImpl.CAMPOSEJEMPLO,datosVector);
		request.setAttribute("nombreFichero", exampleFile.getName());
		request.setAttribute("rutaFichero", exampleFile.getPath());
		request.setAttribute("accion", "");
		return "descargaFichero";

	}

	private String parseExcelFile (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaGFForm cargaMasivaGFForm = (CargaMasivaGFForm) formulario;
		CargaMasivaGF cargaMasivaDatosGF = (CargaMasivaGF) getBusinessManager().getService(CargaMasivaGF.class);
		UsrBean usrBean = this.getUserBean(request);
		cargaMasivaGFForm.setCodIdioma(usrBean.getLanguage());
		
		try {
			if(cargaMasivaGFForm.getTheFile()!=null && cargaMasivaGFForm.getTheFile().getFileData()!=null && cargaMasivaGFForm.getTheFile().getFileData().length>0){
				File file = SIGAServicesHelper.createTemporalFile(cargaMasivaGFForm.getTheFile().getFileData(), "xls"); 
				cargaMasivaGFForm.setRutaFichero(file.getAbsolutePath());
				
				CargaMasivaDatosGFVo cargaMasivaDatosGFVo = new CargaMasivaDatosGFVo();
				cargaMasivaDatosGFVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
				cargaMasivaDatosGFVo.setExcelBytes(cargaMasivaGFForm.getTheFile().getFileData());
				cargaMasivaDatosGFVo.setUsuario(usrBean.getUserName());

				List<CargaMasivaDatosGFVo> cargaMasivaDatosGFList = cargaMasivaDatosGF.parseExcelFile(cargaMasivaDatosGFVo);
				VoUiService<CargaMasivaGFForm, CargaMasivaDatosGFVo> voService = new CargaMasivaDatosGFVoService();
				request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosGFList));
				String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
				request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaGFForm.clone());
				

			}
		}catch (BusinessException e) {
			return error(e.getMessage(),new ClsExceptions(e.toString()),request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}

		return "listadoResumen";


	}







}