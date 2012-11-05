/*
 * 
 * @author Carlos Ruano
 *
 */
package com.siga.envios.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants.EstadosEntradaEnviosEnum;
import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisional;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesigna;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.TransformBeanToForm;
import com.siga.beans.AdmInformeBean;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.envios.form.EntradaEnviosForm;
import com.siga.envios.service.EntradaEnviosService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

import es.satec.businessManager.BusinessManager;


/**
 * Action para la b�squeda de expedientes (simple y avanzada)
 */
public class EntradaEnviosAction extends MasterAction {


	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		try	{
			
			miForm = (MasterForm) formulario;

			if (miForm != null){
				String accion = miForm.getModo();

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
				} else if(accion.equalsIgnoreCase("procesar")){ 
					mapDestino = procesar(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("descargar")) {
					mapDestino = descargar(mapping, miForm, request, response);		
				} else if (accion.equalsIgnoreCase("abrirModalOpcionBorrado")) {
					mapDestino = abrirModalOpcionBorrado(mapping, miForm, request, response);						
				} else if (accion.equalsIgnoreCase("borrarRelacionEJG")) {
					mapDestino = borrarRelacionEJG(mapping, miForm, request, response);		
				} else if (accion.equalsIgnoreCase("borrarRelacionDesigna")) {
					mapDestino = borrarRelacionDesigna(mapping, miForm, request, response);						
				} else if (accion.equalsIgnoreCase("respuestaTelematica")) {
					mapDestino = finalizar(mapping, miForm, request, response);		
				} else{ 
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

			if (mapDestino == null) { 
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

			return mapping.findForward(mapDestino);
		
		} catch (SIGAException es){ 
			throw es; 
		
		} catch (Exception e){ 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		} 
	}
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			EnvEntradaEnviosWithBLOBs entradaEnviosWithBLOBs = entradaEnviosService.getEntradaEnviosWithBlobs(entradaEnviosForm);
			EnvioInformesGenericos informe = new EnvioInformesGenericos();
			AdmInformeBean infBean = new AdmInformeBean();
			File xslTransform = null;
			infBean.setDirectorio("carta_oficio");
			String intercambio="";
			
			//Si el estado fuera SIN LEER se actualiza directamente a LEIDO
			if(entradaEnviosWithBLOBs.getIdestado()!= null && entradaEnviosWithBLOBs.getIdestado().toString().equals(""+EstadosEntradaEnviosEnum.ESTADO_SIN_LEER.getCodigo())){
				entradaEnviosService.actualizarEstado(new Long(entradaEnviosForm.getIdEnvio()), new Short(entradaEnviosForm.getIdInstitucion()),EstadosEntradaEnviosEnum.ESTADO_LEIDO.getCodigo());
				entradaEnviosWithBLOBs.setIdestado(EstadosEntradaEnviosEnum.ESTADO_LEIDO.getCodigo());
			}
			
			EntradaEnviosForm entradaEnvioFormulario = TransformBeanToForm.getEntradaEnviosForm(entradaEnviosWithBLOBs);
			
			if(entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
				infBean.setNombreFisico("sgp_ca_env_sol_abg_pro");
				xslTransform = informe.getPlantillaGenerica(infBean, userBean.getLanguage(), "xsl");
				intercambio = MasterReport.convertXML2HTML(new ByteArrayInputStream(entradaEnviosWithBLOBs.getXml().getBytes("ISO-8859-15")),xslTransform);
				entradaEnviosService.updateFormularioDatosSolDesignaProvisional(entradaEnvioFormulario);
			} else if(entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
				infBean.setNombreFisico("sgp_ca_resp_sol_susp_proc");
				xslTransform = informe.getPlantillaGenerica(infBean, userBean.getLanguage(), "xsl");
				intercambio = MasterReport.convertXML2HTML(new ByteArrayInputStream(entradaEnviosWithBLOBs.getXml().getBytes("ISO-8859-15")), xslTransform);
				entradaEnviosService.updateFormularioDatosRespuestaSuspensionProcedimiento(entradaEnvioFormulario);
			}
			
			request.setAttribute("entradaEnvio",entradaEnvioFormulario);
			request.setAttribute("intercambio",intercambio);
			
		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "consulta";
	}
	
	protected String procesar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			entradaEnviosService.procesar(entradaEnviosForm,userBean,true);
			return ver(mapping, formulario, request, response);
			

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
		
	}
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			entradaEnviosService.procesar(entradaEnviosForm,userBean,false);
			return ver(mapping, formulario, request, response);
			

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
	}
	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try{
			HttpSession ses=request.getSession();
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			JSONObject json = new JSONObject();
			JSONArray estadosJsonArray = new JSONArray();
			JSONArray tiposIntercambioJsonArray = new JSONArray();
			
			//Se cargan los valores iniciales de los campos dle formulario
			entradaEnviosForm.setIdInstitucion(userBean.getLocation());
			JSONObject obj = null;
			
			
			for (TipoIntercambioEnum tipoIntercambioEnum : TipoIntercambioEnum.values()) {
				obj = new JSONObject();
				//Se insertan solamente (en principio) los codigos 4 y 10
				if(tipoIntercambioEnum.getCodigo().equals("05") || tipoIntercambioEnum.getCodigo().equals("06")){
					obj.put("idTipoIntercambioTelematico", tipoIntercambioEnum.getCodigo());
					obj.put("nombre", tipoIntercambioEnum.getDescripcion().split(":")[1]);
					tiposIntercambioJsonArray.put(obj);
				}
			}
			for (EstadosEntradaEnviosEnum estadosEntradaEnviosEnum: EstadosEntradaEnviosEnum.values()) {
				obj = new JSONObject();
				obj.put("idEstado", estadosEntradaEnviosEnum.getCodigo());
		        obj.put("nombre", estadosEntradaEnviosEnum.getDescripcion());
				estadosJsonArray.put(obj);
			}
			
			json.put("tiposIntercambio", tiposIntercambioJsonArray);
			json.put("estado", estadosJsonArray);
			request.setAttribute("json",json);
			
			String[] parametrosComboRemitente = {userBean.getLocation()};
			request.setAttribute("parametrosComboRemitente",parametrosComboRemitente);

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "inicio";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException{
		
		EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
		List<EntradaEnviosForm> listEntradaEnviosForm = new ArrayList<EntradaEnviosForm>();

		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			List<EnvEntradaEnvios> entradaEnvios = entradaEnviosService.getEntradaEnvios(entradaEnviosForm);
			
			for(EnvEntradaEnvios entrada: entradaEnvios){
				EntradaEnviosForm entradaEnvioFormulario = TransformBeanToForm.getEntradaEnviosForm(entrada);
				if(entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
					entradaEnviosService.updateFormularioDatosSolDesignaProvisional(entradaEnvioFormulario);
				} else if(entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
					entradaEnviosService.updateFormularioDatosRespuestaSuspensionProcedimiento(entradaEnvioFormulario);
				}				
				listEntradaEnviosForm.add(entradaEnvioFormulario);
			}

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		request.setAttribute("listEntradaEnviosForm", listEntradaEnviosForm);        

		return "resultado";
	}

	protected String descargar (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException{
		EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
		List<EntradaEnviosForm> listEntradaEnviosForm = new ArrayList<EntradaEnviosForm>();
		String idEnvio = entradaEnviosForm.getIdEnvio();
		String idInstitucion = entradaEnviosForm.getIdInstitucion();
		
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
    		File pdfIntercambio = entradaEnviosService.getPdfIntercambio(idEnvio, idInstitucion);
    		
    		if (pdfIntercambio == null) {								
    			throw new SIGAException("messages.general.error.ficheroNoExiste");
    		}				
	 		
    		request.setAttribute("rutaFichero", pdfIntercambio.getPath());
			request.setAttribute("nombreFichero", pdfIntercambio.getName());
			request.setAttribute("borrarFichero", "true");

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		return "descargaFichero";
	}
	
	protected String borrarRelacionEJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			entradaEnviosService.borrarRelacionEJG(entradaEnviosForm,userBean);
			return ver(mapping, formulario, request, response);
			

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
	}
	
	protected String borrarRelacionDesigna(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			entradaEnviosService.borrarRelacionDesigna(entradaEnviosForm,userBean);
			return ver(mapping, formulario, request, response);
			

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
	}	
	
	protected String finalizar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			String idEnvioRelacionado = (String)request.getAttribute("idEnvioRelacionado");
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			BusinessManager businessManager =  BusinessManager.getInstance();
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			entradaEnviosService.relacionarEnvio(new Long(entradaEnviosForm.getIdEnvio()), new Short(entradaEnviosForm.getIdInstitucion()),new Long(idEnvioRelacionado));
			entradaEnviosService.actualizarEstado(new Long(entradaEnviosForm.getIdEnvio()), new Short(entradaEnviosForm.getIdInstitucion()),EstadosEntradaEnviosEnum.ESTADO_FINALIZADO.getCodigo());			
			return ver(mapping, formulario, request, response);

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
	}	

	protected String abrirModalOpcionBorrado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			EntradaEnviosForm entradaEnviosForm = (EntradaEnviosForm)formulario;
			String mensaje = "";
			if(entradaEnviosForm.getCaso()!= null && entradaEnviosForm.getCaso().equals("1")){
				request.setAttribute("mensajeradio","Eliminar la asignaci�n y el EJG que se ha creado.");
			}else{
				request.setAttribute("mensajeradio","Eliminar la asignaci�n y la designaci�n que se ha creado.");
			}
			
			return "abrirModalOpcionBorrado";

		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}

		return "exception";
	}
}