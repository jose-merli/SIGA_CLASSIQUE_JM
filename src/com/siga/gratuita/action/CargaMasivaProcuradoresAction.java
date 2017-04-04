package com.siga.gratuita.action;

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
import org.redabogacia.sigaservices.app.services.cen.impl.CargaMasivaDatosCVImpl;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.services.scs.CargaMasivaProcuradores;
import org.redabogacia.sigaservices.app.services.scs.impl.CargaMasivaDatosProcuradoresImpl;
import org.redabogacia.sigaservices.app.vo.scs.CargaMasivaDatosProcuradoresVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.comun.VoUiService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CargaMasivaProcuradoresForm;
import com.siga.gratuita.form.service.CargaMasivaDatosProcuradoresVoService;
/**
 * 
 * @author jorgeta
 *
 */
public class CargaMasivaProcuradoresAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		CargaMasivaProcuradoresForm miForm = null;
		try { 
			do {
				miForm = (CargaMasivaProcuradoresForm) formulario;
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
		CargaMasivaProcuradoresForm miForm = (CargaMasivaProcuradoresForm) formulario;
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
		
		CargaMasivaProcuradoresForm miForm = (CargaMasivaProcuradoresForm) formulario;
		miForm.clear();
		UsrBean usrBean = this.getUserBean(request);
		miForm.setIdInstitucion(usrBean.getLocation());
		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) request.getSession().getAttribute(identificadorFormularioBusqueda);
		if(cargaMasivaProcuradoresForm!=null){
			miForm.setFechaCarga(cargaMasivaProcuradoresForm.getFechaCarga());
			miForm.setIdInstitucion(cargaMasivaProcuradoresForm.getIdInstitucion());
		}
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
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
		String idInstitucion = request.getParameter("idInstitucion");
		String fechaCarga = request.getParameter("fechaCarga");
		cargaMasivaProcuradoresForm.setIdInstitucion(idInstitucion);
		if(fechaCarga!=null)
			cargaMasivaProcuradoresForm.setFechaCarga(fechaCarga);

		String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
		request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaProcuradoresForm.clone());

		CargaMasivaProcuradores cargaMasivaDatosCV = (CargaMasivaProcuradores) getBusinessManager().getService(CargaMasivaProcuradores.class);
		VoUiService<CargaMasivaProcuradoresForm, CargaMasivaDatosProcuradoresVo> voService = new CargaMasivaDatosProcuradoresVoService();
		List<CargaMasivaDatosProcuradoresVo> cargaMasivaDatosProcuradoresVos =  cargaMasivaDatosCV.getCargasMasivas(voService.getForm2Vo(cargaMasivaProcuradoresForm));
		request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosProcuradoresVos));
		
		return "listado";


	}



	private String downloadExcelError (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "descargaFichero";
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
		CargaMasivaProcuradores cargaMasivaDatosCV = (CargaMasivaProcuradores) getBusinessManager().getService(CargaMasivaProcuradores.class);
		UsrBean usrBean = this.getUserBean(request);

		File errorFile;
		try {
			CargaMasivaDatosProcuradoresVo cargaMasivaDatosProcuradoresVo = new CargaMasivaDatosProcuradoresVo();
			cargaMasivaDatosProcuradoresVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			cargaMasivaDatosProcuradoresVo.setExcelBytes(SIGAServicesHelper.getBytes(cargaMasivaProcuradoresForm.getRutaFichero()));
			cargaMasivaDatosProcuradoresVo.setCodIdioma(usrBean.getLanguage());
			
			errorFile = cargaMasivaDatosCV.getErrorExcelFile(cargaMasivaDatosProcuradoresVo);
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
			CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaProcuradoresForm.getIdFichero()), Short.valueOf(cargaMasivaProcuradoresForm.getIdInstitucion()));
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
			CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
			cargaMasivaProcuradoresForm.setCodIdioma(this.getUserBean(request).getLanguage());
			FicherosService ficherosService = (FicherosService)  getBusinessManager().getService(FicherosService.class);
			File file = ficherosService.getFile(Long.valueOf(cargaMasivaProcuradoresForm.getIdFicheroLog()), Short.valueOf(cargaMasivaProcuradoresForm.getIdInstitucion()));
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
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
		CargaMasivaProcuradores cargaMasivaDatosCV = (CargaMasivaProcuradores) getBusinessManager().getService(CargaMasivaProcuradores.class);
		UsrBean usrBean = this.getUserBean(request);
		try {

			//			List<CargaMasivaDatosProcuradoresVo> cargaMasivaDatosCVList = cargaMasivaDatosCV.parseExcelFile(SIGAServicesHelper.getBytes(cargaMasivaProcuradoresForm.getRutaFichero()),Short.valueOf(usrBean.getLocation()));
			CargaMasivaDatosProcuradoresVo cargaMasivaDatosProcuradoresVo = new CargaMasivaDatosProcuradoresVo();
			cargaMasivaDatosProcuradoresVo.setCodIdioma(usrBean.getLanguage());
			cargaMasivaDatosProcuradoresVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
			String nombreFichero = cargaMasivaProcuradoresForm.getNombreFichero().substring(cargaMasivaProcuradoresForm.getNombreFichero().lastIndexOf("\\")+1); 
			cargaMasivaDatosProcuradoresVo.setNombreFichero(nombreFichero);
			cargaMasivaDatosProcuradoresVo.setUsuario(usrBean.getUserName());
			cargaMasivaDatosProcuradoresVo.setExcelBytes(SIGAServicesHelper.getBytes(cargaMasivaProcuradoresForm.getRutaFichero()));

			cargaMasivaDatosCV.processExcelFile(cargaMasivaDatosProcuradoresVo); 

		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}


		return exitoRefresco("messages.inserted.success",request);


	}
	private String downloadExample (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		CargaMasivaProcuradores cargaMasivaDatosCV = (CargaMasivaProcuradores) getBusinessManager().getService(CargaMasivaProcuradores.class); 
		Vector<Hashtable<String, Object>> datosVector = new Vector<Hashtable<String,Object>>();
		Hashtable<String, Object> datosHashtable =  new Hashtable<String, Object>();
		
		
		
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_CODIGODESIGNAABOGADO,"nnnn/nnnnn");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMEJG,"nnnn/nnnnnn");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMCOLPROCURADOR,"nnnnnn");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_FECHADESIGPROCURADOR,"dd/mm/yyyy");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMDESIGNAPROCURADOR,"nnnnnn");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_OBSERVACIONES,"aaaaaaaaaaaaa");
		
		
		datosVector.add(datosHashtable);
		datosHashtable =  new Hashtable<String, Object>();
		
		
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_CODIGODESIGNAABOGADO,"Requerido");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMEJG,"Opcional");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMCOLPROCURADOR,"Requerido");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_FECHADESIGPROCURADOR,"Requerido");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_NUMDESIGNAPROCURADOR,"Opcional");
		datosHashtable.put(CargaMasivaDatosProcuradoresVo.C_OBSERVACIONES,"Opcional");
		
		
		
		datosVector.add(datosHashtable);
			 
			 
		File exampleFile =  cargaMasivaDatosCV.createExcelFile(CargaMasivaDatosProcuradoresImpl.CAMPOSEJEMPLO,datosVector);
		request.setAttribute("nombreFichero", exampleFile.getName());
		request.setAttribute("rutaFichero", exampleFile.getPath());
		request.setAttribute("accion", "");
		return "descargaFichero";

	}

	private String parseExcelFile (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		CargaMasivaProcuradoresForm cargaMasivaProcuradoresForm = (CargaMasivaProcuradoresForm) formulario;
		CargaMasivaProcuradores cargaMasivaDatosCV = (CargaMasivaProcuradores) getBusinessManager().getService(CargaMasivaProcuradores.class);
		UsrBean usrBean = this.getUserBean(request);
		cargaMasivaProcuradoresForm.setCodIdioma(usrBean.getLanguage());
		try {
			if(cargaMasivaProcuradoresForm.getTheFile()!=null && cargaMasivaProcuradoresForm.getTheFile().getFileData()!=null && cargaMasivaProcuradoresForm.getTheFile().getFileData().length>0){
				File file = SIGAServicesHelper.createTemporalFile(cargaMasivaProcuradoresForm.getTheFile().getFileData(), "xls"); 
				cargaMasivaProcuradoresForm.setRutaFichero(file.getAbsolutePath());
				cargaMasivaProcuradoresForm.setNombreFichero(cargaMasivaProcuradoresForm.getTheFile().getFileName());
				CargaMasivaDatosProcuradoresVo cargaMasivaDatosProcuradoresVo = new CargaMasivaDatosProcuradoresVo();
				cargaMasivaDatosProcuradoresVo.setIdInstitucion(Short.valueOf(usrBean.getLocation()));
				cargaMasivaDatosProcuradoresVo.setExcelBytes(cargaMasivaProcuradoresForm.getTheFile().getFileData());
				
				List<CargaMasivaDatosProcuradoresVo> cargaMasivaDatosCVList = cargaMasivaDatosCV.parseExcelFile(cargaMasivaDatosProcuradoresVo);
				VoUiService<CargaMasivaProcuradoresForm, CargaMasivaDatosProcuradoresVo> voService = new CargaMasivaDatosProcuradoresVoService();
				request.setAttribute("listado", voService.getVo2FormList(cargaMasivaDatosCVList));
				String identificadorFormularioBusqueda = getIdBusqueda(super.dataBusqueda,getClass().getName());
				request.getSession().setAttribute(identificadorFormularioBusqueda,cargaMasivaProcuradoresForm.clone());
				

			}
		}catch (BusinessException e) {
			throwExcp(e.getMessage(), e,null);
		}  catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita"}, e, null);
		}

		return "listadoResumen";


	}







}