package com.siga.facturacion.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.CenBancos;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.vo.BancoVo;
import org.redabogacia.sigaservices.app.vo.fac.CuentaBancariaVo;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.GenParametrosBean;
import com.siga.censo.action.CuentasBancariasAction;
import com.siga.comun.VoUiService;
import com.siga.facturacion.form.CuentasBancariasForm;
import com.siga.facturacion.form.service.CuentaBancariaVoService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class GestionCuentasBancariasAction extends MasterAction {
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
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultaBancos")){
						getConsultaBancos (mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("buscar")){
						mapDestino = buscar (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("borrar")){
						mapDestino = borrar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultar")){
						mapDestino = consultar(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("editar")){
						mapDestino = editar(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("modificar")){
						mapDestino = modificar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("refrescar")){
						mapDestino = refrescar(mapping, miForm, request, response);
					}		
					
					
					else {
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

	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		cuentasBancariasForm.setIdInstitucion(usrBean.getLocation());
		BusinessManager bm = getBusinessManager();
		CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
		List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancosConCuentasBancarias(new Integer(usrBean.getLocation()));
		request.setAttribute("listaBancos", bancosList);

		
		
		return "inicio";
	}
	protected String refrescar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "exito";
	}
	
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		
		String forward = "listado";

		 
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			List<CuentaBancariaVo> cuentaBancariaVos =cuentasBancariasService.getCuentasBancarias(voService.getForm2Vo(cuentasBancariasForm));
			List<CuentasBancariasForm> cuentasBancarias = voService.getVo2FormList(cuentaBancariaVos);
			
			cuentasBancariasForm.setModo("abrir");
			request.setAttribute("cuentasBancarias", cuentasBancarias);

//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos("");
//			request.setAttribute("listaBancos", bancosList);
			
		} catch (Exception e){
			
			throwExcp("messages.general.errorExcepcion", e, null); 
			
		}
		return forward;
	}
	protected String nuevo (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		
		
//			BusinessManager bm = getBusinessManager();
//			org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos( new BancoVo());
//			request.setAttribute("listaBancos", bancosList);
		cuentasBancariasForm.setModo("insertar");
			
		
		return "editar";
	}
	
	protected String consultar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			request.setAttribute("seriesFacturacion", cuentasBancariasService.getSeriesCuentaBancaria(cuentaBancariaVo));
			
			cuentasBancariasForm.setModo("abrir");
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
			
//			BancoVo banco = new BancoVo();
//			banco.setCodigo(cuentaBancariaVo.getCodBanco());
//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos(banco);
//			request.setAttribute("listaBancos", bancosList);
			
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "editar";
	}
	/**
	 * Lo comenntamos porque no se usa
	 */
	
	protected String editar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =cuentasBancariasService.getCuentaBancaria(voService.getForm2Vo(cuentasBancariasForm));
			cuentasBancariasForm = voService.getVo2Form(cuentaBancariaVo);
			cuentasBancariasForm.setModo("modificar");
			request.setAttribute("seriesFacturacion", cuentasBancariasService.getSeriesCuentaBancaria(cuentaBancariaVo));
			request.setAttribute("CuentasBancariasForm", cuentasBancariasForm);
//			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos( new BancoVo());
//			request.setAttribute("listaBancos", bancosList);
			
			
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "editar";
	}
	
	protected String borrar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService bts = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo = bts.getCuentaBancaria(cuentaBancariaVo);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			bts.delete(cuentaBancariaVo);
			request.removeAttribute("modal");
			request.removeAttribute("sinrefresco");
			request.setAttribute("mensaje","messages.deleted.success");
			forward = "exito";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}

	
	protected String insertar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			
			GenParametrosService parametrosService = (GenParametrosService)bm.getService(GenParametrosService.class);
			GenParametros genParametros = new GenParametros();
			
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			String sjcs = (String) cuentaBancariaVo.getSjcs();
			String idIntitucion = (String) cuentaBancariaVo.getIdinstitucion().toString();
			String idModulo = "FCS";
			String parametro = "BANCOS_CODIGO_ABONO";
			String valor= (String) cuentaBancariaVo.getCodBanco();
			String idRecurso = "administracion.parametro.bancosCodigoAbono";	

			valor=UtilidadesString.replaceAllIgnoreCase(valor,"\u00A0","\u0020");	
			if (valor.trim()!=null && !valor.trim().equals("")) {
			 
			  valor = valor.trim();	
			}else{
			 valor="\u0020";
			}			
			
			if (sjcs.equals("1")){

				genParametros.setIdinstitucion(new Short(idIntitucion));
				genParametros.setModulo(idModulo);
				genParametros.setParametro(parametro);
				genParametros.setValor(valor);
				genParametros.setIdrecurso(idRecurso);	
				if (parametrosService.update(genParametros)!=1){
					throw new ClsExceptions ("messages.updated.error");
				}				
				cuentasBancariasService.insert(cuentaBancariaVo);
			}
			forward = exitoModal("messages.updated.success",request);
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	protected String modificar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			
			GenParametrosService parametrosService = (GenParametrosService)bm.getService(GenParametrosService.class);
			GenParametros genParametros = new GenParametros();
			
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			VoUiService<CuentasBancariasForm, CuentaBancariaVo> voService = new CuentaBancariaVoService();
			CuentaBancariaVo cuentaBancariaVo =  voService.getForm2Vo(cuentasBancariasForm);
			cuentaBancariaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			
			String sjcs = (String) cuentaBancariaVo.getSjcs();
			String idIntitucion = (String) cuentaBancariaVo.getIdinstitucion().toString();
			String idModulo = "FCS";
			String parametro = "BANCOS_CODIGO_ABONO";
			String valor= (String) cuentaBancariaVo.getCodBanco();
			String codBanco = (String) cuentaBancariaVo.getCodBanco();
			String idRecurso = "administracion.parametro.bancosCodigoAbono";		
			
			valor=UtilidadesString.replaceAllIgnoreCase(valor,"\u00A0","\u0020");	
			if (valor.trim()!=null && !valor.trim().equals("")) {
			 
			  valor = valor.trim();	
			}else{
			 valor="\u0020";
			}			
			
			if (sjcs.equals("1")){

				genParametros.setIdinstitucion(new Short(idIntitucion));
				genParametros.setModulo(idModulo);
				genParametros.setParametro(parametro);
				genParametros.setValor(valor);
				genParametros.setIdrecurso(idRecurso);

				if (parametrosService.update(genParametros)!=1){
					throw new ClsExceptions ("messages.updated.error");
				}
				cuentasBancariasService.update(cuentaBancariaVo);
			} else {
				CuentasBancariasForm cuentasBancariasFormulario = new CuentasBancariasForm();
				Date fechaModificacion = null;
				valor = "0";
				cuentasBancariasFormulario.setIdInstitucion(cuentasBancariasForm.getIdInstitucion());
				cuentasBancariasFormulario.setSjcs("1");
				List<CuentaBancariaVo> cuentaBancariaVos =cuentasBancariasService.getCuentasBancarias(voService.getForm2Vo(cuentasBancariasFormulario));
				CuentaBancariaVo cuentaBancariaVoAux = new CuentaBancariaVo();
				for (int i= 0; i < cuentaBancariaVos.size(); i++){
					cuentaBancariaVoAux = (CuentaBancariaVo) cuentaBancariaVos.get(i);
					if (!(cuentaBancariaVoAux.getCodBanco().equals(codBanco))){
						if (fechaModificacion == null){
							fechaModificacion = (Date) cuentaBancariaVoAux.getFechamodificacion();
							valor = cuentaBancariaVoAux.getCodBanco();
						} else {
							if ((fechaModificacion.compareTo(cuentaBancariaVoAux.getFechamodificacion())) != 1){
								fechaModificacion = cuentaBancariaVoAux.getFechamodificacion();
								valor = cuentaBancariaVoAux.getCodBanco();
							}
						}
					}
				}

				genParametros.setIdinstitucion(new Short(idIntitucion));
				genParametros.setModulo(idModulo);
				genParametros.setParametro(parametro);
				genParametros.setValor(valor);
				genParametros.setIdrecurso(idRecurso);

				if (parametrosService.update(genParametros)!=1){
					throw new ClsExceptions ("messages.updated.error");
				}
				cuentasBancariasService.update(cuentaBancariaVo);
			}
			forward = exitoModal("messages.updated.success",request);
			
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	protected void getConsultaBancos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		CuentasBancariasForm cuentasBancariasForm = (CuentasBancariasForm) formulario;
		try {
			
			BusinessManager bm = getBusinessManager();
			CuentasBancariasService cuentasBancariasService = (CuentasBancariasService)bm.getService(CuentasBancariasService.class);
			
			BancoVo banco = new BancoVo();
			banco.setNombre(cuentasBancariasForm.getBancoAjaxIn());
			List<CenBancos> bancosList = (ArrayList<CenBancos>)cuentasBancariasService.getBancos(banco);
			JSONObject json = new JSONObject();
			JSONArray lisatBancosJsonArray = new JSONArray();
			for (CenBancos cenBancos : bancosList) {
				JSONObject obj = new JSONObject();
				obj.put("codigobanco", cenBancos.getCodigo());
	            obj.put("descripcionbanco", cenBancos.getNombre());
	            lisatBancosJsonArray.put(obj);
			}
			json.put("listaBancos", lisatBancosJsonArray);
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
		    response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
	}
	
	
}