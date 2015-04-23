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
import org.redabogacia.sigaservices.app.services.cen.CargaMasivaCV;
import org.redabogacia.sigaservices.app.services.cen.SubtiposCVService;
import org.redabogacia.sigaservices.app.services.cen.impl.CargaMasivaDatosCVImpl;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
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
			HttpServletResponse response) throws SIGAException{
		
		CargaMasivaCVForm miForm = (CargaMasivaCVForm) formulario;
		miForm.clear();
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

		CargaMasivaCV cargaMasivaDatosCV = (CargaMasivaCV) getBusinessManager().getService(CargaMasivaCV.class);
		VoUiService<CargaMasivaCVForm, CargaMasivaDatosCVVo> voService = new CargaMasivaDatosCVVoService();
		List<CargaMasivaDatosCVVo> cargaMasivaDatosCVVos =  cargaMasivaDatosCV.getCargasMasivasCV(voService.getForm2Vo(cargaMasivaCVForm));
		request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosCVVos));
		
		return "listado";


	}



	private String downloadExcelError (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
		CargaMasivaCV cargaMasivaDatosCV = (CargaMasivaCV) getBusinessManager().getService(CargaMasivaCV.class);
		UsrBean usrBean = this.getUserBean(request);

		File errorFile;
		try {
			CargaMasivaDatosCVVo cargaMasivaDatosCVVo = new CargaMasivaDatosCVVo();
			
			String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
			CargaMasivaCVForm cargaMasivaCVFormSesion = (CargaMasivaCVForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
			
			
			
			cargaMasivaDatosCVVo.setIdinstitucion(Short.valueOf(usrBean.getLocation()));
			cargaMasivaDatosCVVo.setExcelBytes(cargaMasivaCVFormSesion.getTheFile().getFileData());
			cargaMasivaDatosCVVo.setCodIdioma(usrBean.getLanguage());
			
			errorFile = cargaMasivaDatosCV.getErrorExcelFile(cargaMasivaDatosCVVo);
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
			CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaCVForm.getIdFichero()), Short.valueOf(cargaMasivaCVForm.getIdInstitucion()));
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
			CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
			cargaMasivaCVForm.setCodIdioma(this.getUserBean(request).getLanguage());
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaCVForm.getIdFicheroLog()), Short.valueOf(cargaMasivaCVForm.getIdInstitucion()));
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
		CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
		CargaMasivaCV cargaMasivaDatosCV = (CargaMasivaCV) getBusinessManager().getService(CargaMasivaCV.class);
		UsrBean usrBean = this.getUserBean(request);
		try {

			//			List<CargaMasivaDatosCVVo> cargaMasivaDatosCVList = cargaMasivaDatosCV.parseExcelFile(SIGAServicesHelper.getBytes(cargaMasivaCVForm.getRutaFichero()),Short.valueOf(usrBean.getLocation()));
			CargaMasivaDatosCVVo cargaMasivaDatosCVVo = new CargaMasivaDatosCVVo();
			cargaMasivaDatosCVVo.setCodIdioma(usrBean.getLanguage());
			cargaMasivaDatosCVVo.setIdinstitucion(Short.valueOf(usrBean.getLocation()));
			String nombreFichero = cargaMasivaCVForm.getRutaFichero().substring(cargaMasivaCVForm.getRutaFichero().lastIndexOf("\\")+1); 
			cargaMasivaDatosCVVo.setNombreFichero(nombreFichero);
			cargaMasivaDatosCVVo.setUsuario(usrBean.getUserName());
			cargaMasivaDatosCVVo.setExcelBytes(SIGAServicesHelper.getBytes(cargaMasivaCVForm.getRutaFichero()));

			cargaMasivaDatosCV.processExcelFile(cargaMasivaDatosCVVo); 

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}


		return exitoRefresco("messages.inserted.success",request);


	}
	private String downloadExample (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		CargaMasivaCV cargaMasivaDatosCV = (CargaMasivaCV) getBusinessManager().getService(CargaMasivaCV.class); 
		Vector<Hashtable<String, Object>> datosVector = new Vector<Hashtable<String,Object>>();
		Hashtable<String, Object> datosHashtable =  new Hashtable<String, Object>();
		datosHashtable.put(CargaMasivaDatosCVVo.COLEGIADONUMERO,"nnnnnn");
		datosHashtable.put(CargaMasivaDatosCVVo.PERSONANIF,"nnnnnnnna" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_FECHAINICIO,"nn/nn/nnnn" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_FECHAFIN, "nn/nn/nnnn" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_CREDITOS, "nnn");
		datosHashtable.put(CargaMasivaDatosCVVo.FECHAVERIFICACION, "nn/nn/nnnn");
		datosHashtable.put(CargaMasivaDatosCVVo.C_DESCRIPCION,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" );
		datosHashtable.put(CargaMasivaDatosCVVo.TIPOCVCOD, "aaa");
		datosHashtable.put(CargaMasivaDatosCVVo.SUBTIPOCV1COD,"aaa");
		datosHashtable.put(CargaMasivaDatosCVVo.SUBTIPOCV2COD,"aaa");
		datosVector.add(datosHashtable);
		datosHashtable =  new Hashtable<String, Object>();
		datosHashtable.put(CargaMasivaDatosCVVo.COLEGIADONUMERO,"Opcional. Si nulo nif requerido");
		datosHashtable.put(CargaMasivaDatosCVVo.PERSONANIF,"Opcional. Si nulo colegiadonumero requerido" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_FECHAINICIO,"Requerido" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_FECHAFIN, "Requerido" );
		datosHashtable.put(CargaMasivaDatosCVVo.C_CREDITOS, "Requerido");
		datosHashtable.put(CargaMasivaDatosCVVo.FECHAVERIFICACION, "Requerido");
		datosHashtable.put(CargaMasivaDatosCVVo.C_DESCRIPCION,"Requerido" );
		datosHashtable.put(CargaMasivaDatosCVVo.TIPOCVCOD, "Requerido");
		datosHashtable.put(CargaMasivaDatosCVVo.SUBTIPOCV1COD,"Opcional");
		datosHashtable.put(CargaMasivaDatosCVVo.SUBTIPOCV2COD,"Opcional");
		datosVector.add(datosHashtable);
			 
			 
		File exampleFile =  cargaMasivaDatosCV.createExcelFile(CargaMasivaDatosCVImpl.CAMPOSEJEMPLO,datosVector);
		request.setAttribute("nombreFichero", exampleFile.getName());
		request.setAttribute("rutaFichero", exampleFile.getPath());
		request.setAttribute("accion", "");
		return "descargaFichero";

	}

	private String parseExcelFile (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaCVForm cargaMasivaCVForm = (CargaMasivaCVForm) formulario;
		CargaMasivaCV cargaMasivaDatosCV = (CargaMasivaCV) getBusinessManager().getService(CargaMasivaCV.class);
		UsrBean usrBean = this.getUserBean(request);
		cargaMasivaCVForm.setCodIdioma(usrBean.getLanguage());
		try {
			if(cargaMasivaCVForm.getTheFile()!=null && cargaMasivaCVForm.getTheFile().getFileData()!=null && cargaMasivaCVForm.getTheFile().getFileData().length>0){
				
				CargaMasivaDatosCVVo cargaMasivaDatosCVVo = new CargaMasivaDatosCVVo();
				cargaMasivaDatosCVVo.setIdinstitucion(Short.valueOf(usrBean.getLocation()));
				cargaMasivaDatosCVVo.setExcelBytes(cargaMasivaCVForm.getTheFile().getFileData());

				List<CargaMasivaDatosCVVo> cargaMasivaDatosCVList = cargaMasivaDatosCV.parseExcelFile(cargaMasivaDatosCVVo);
				VoUiService<CargaMasivaCVForm, CargaMasivaDatosCVVo> voService = new CargaMasivaDatosCVVoService();
				request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosCVList));
				String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
				request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaCVForm.clone());

			}
		}catch (BusinessException e) {
			return error(e.getMessage(),new ClsExceptions(e.toString()),request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}

		return "listadoResumen";


	}







}