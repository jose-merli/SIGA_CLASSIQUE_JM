package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.censo.form.MutualidadForm;
import com.siga.censo.service.MutualidadService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionMutualidadAction extends MasterAction {
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
					else{
						if(mapping.getPath().equals("/CEN_MutualidadFichaPlan"))
							accion = "consultaPlanMutualidad";
						else if(mapping.getPath().equals("/CEN_MutualidadFichaSeguro"))
							accion = "consultaSeguroUniversal";
					}
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicioFicha (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxCuotaCapitalCobertura")){
					
						getAjaxCuotaCapitalCobertura(mapping, miForm, request, response);
						return null;
						
					}else if ( accion.equalsIgnoreCase("getAjaxPoblaciones")){
						getAjaxPoblaciones (mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("consultaPlanMutualidad")){
						mapDestino = consultaPlanMutualidad (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultaSeguroUniversal")){
						mapDestino = consultaSeguroUniversal (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevo")){
						mapDestino = nuevo (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizaEstado")){
						mapDestino = actualizaEstado (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizaEstadoMutualista")){
						mapDestino = actualizaEstadoMutualista (mapping, miForm, request, response);
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
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	protected void getAjaxPoblaciones (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		
		//Recogemos el parametro enviado por ajax
		String idProvincia = request.getParameter("idProvincia");

		List<CenPoblacionesBean> alPoblaciones = null;
		if(idProvincia!= null && !idProvincia.equals("")){
			CenPoblacionesAdm poblacionesAdm = new CenPoblacionesAdm(this.getUserBean(request));
			alPoblaciones = poblacionesAdm.getPoblaciones(idProvincia);
		}
		if(alPoblaciones==null){
			alPoblaciones = new ArrayList<CenPoblacionesBean>();
			
		}
		
		respuestaAjax(new AjaxCollectionXmlBuilder<CenPoblacionesBean>(), alPoblaciones,response);
		
	}
	protected String nuevo (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		try {
			List<String> alIdsPais = new ArrayList<String>();
			if(mutualidadForm.getIdPais()!=null && !mutualidadForm.getIdPais().equals("")){

				alIdsPais.add(mutualidadForm.getIdPais());
			}
			request.setAttribute("idPaisSeleccionado",alIdsPais);
			List<CenProvinciaBean> provinciaBeans = null;
//			if(mutualidadForm.getIdPais()!= null && mutualidadForm.getIdPais().equals("191")){
				CenProvinciaAdm provinciaAdm = new CenProvinciaAdm(this.getUserBean(request));
				provinciaBeans = provinciaAdm.getProvincias("191");
				mutualidadForm.setProvincias(provinciaBeans);
//			}else{
//				mutualidadForm.setProvincias(new ArrayList<CenProvinciaBean>());
//			}
			List<CenPoblacionesBean> alPoblaciones = null;
			if(mutualidadForm.getIdProvincia()!= null && !mutualidadForm.getIdProvincia().equals("")){
				CenPoblacionesAdm poblacionesAdm = new CenPoblacionesAdm(this.getUserBean(request));
				alPoblaciones = poblacionesAdm.getPoblaciones(mutualidadForm.getIdProvincia());
				mutualidadForm.setPoblaciones(alPoblaciones);
			}else{
				mutualidadForm.setPoblaciones(new ArrayList<CenPoblacionesBean>());
			}

			List<String> alIdsBanco = new ArrayList<String>();
			if(mutualidadForm.getIdBanco()!=null && !mutualidadForm.getIdBanco().equals("")){

				alIdsBanco.add(mutualidadForm.getIdBanco());
			}
			request.setAttribute("idBancoSeleccionado",alIdsBanco);
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);

			mutualidadService.setMutualidadForm(mutualidadForm, this.getUserBean(request));

			mutualidadForm.setModo("insertar");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "edicion";
	}
	protected String actualizaEstado (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		String forward = "exception";
		try {
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			mutualidadService.actualizaEstadoSolicitud(mutualidadForm, this.getUserBean(request));
			String[] parametros = {mutualidadForm.getIdEstado(),mutualidadForm.getEstado()};
			request.setAttribute("parametrosArray", parametros);
			
		
		//request.setAttribute("mensaje","messages.updated.success");
		request.setAttribute("modal","");
		 
		forward = "exitoParametros";
			

		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return forward;
	}
	protected String actualizaEstadoMutualista (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		String forward = "exception";
		try {
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			mutualidadService.actualizaEstadoMutualista(mutualidadForm, this.getUserBean(request));
			String[] parametros = {mutualidadForm.getEstadoMutualista()};
			request.setAttribute("parametrosArray", parametros);
			
		
		request.setAttribute("mensaje","messages.updated.success");
		request.setAttribute("modal","");
		 
		forward = "exitoParametros";
			

		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return forward;
	}
	
	protected String inicioFicha (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		try {
			Hashtable<String, String> datosMutualidad = new Hashtable<String, String>();
			datosMutualidad.put("idPersona", request.getParameter("idPersona"));
			request.setAttribute("datosMutualidad", datosMutualidad);
			//mutualidadService.setMutualidadForm(mutualidadForm);

			//mutualidadForm.setModo("insertar");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "inicioFicha";
	}
	protected MutualidadForm getMutualidadForm (MutualidadForm mutualidadForm, String idPersona, String idTipoSolicitud,UsrBean usrBean) throws ClsExceptions, SIGAException 
	{
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			mutualidadForm = mutualidadService.getSolicitudMutualidad(mutualidadForm,idPersona, idTipoSolicitud, usrBean);
			
			return mutualidadForm;

			
	}
	protected String consultaSeguroUniversal (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		String idPersona = request.getParameter("idPersona");
		mutualidadForm =getMutualidadForm(mutualidadForm,idPersona,CenSolicitudMutualidadBean.TIPOSOLICITUD_SEGUROUNIVERSAL, this.getUserBean(request));
		return consulta(mapping, mutualidadForm, request, response);
		
	}
	protected String consultaPlanMutualidad (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		String idPersona = request.getParameter("idPersona");
		mutualidadForm =getMutualidadForm(mutualidadForm,idPersona,CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL, this.getUserBean(request));
		return consulta(mapping, mutualidadForm, request, response);
		
	}
	
	protected String consulta (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		
			
			List<String> alIdsPais = new ArrayList<String>();
			if(mutualidadForm.getIdPais()!=null && !mutualidadForm.getIdPais().equals("")){

				alIdsPais.add(mutualidadForm.getIdPais());
			}
			request.setAttribute("idPaisSeleccionado",alIdsPais);
			List<CenProvinciaBean> provinciaBeans = null;
//			if(mutualidadForm.getIdPais()!= null && mutualidadForm.getIdPais().equals("191")){
				CenProvinciaAdm provinciaAdm = new CenProvinciaAdm(this.getUserBean(request));
				provinciaBeans = provinciaAdm.getProvincias("191");
				mutualidadForm.setProvincias(provinciaBeans);
//			}else{
//				mutualidadForm.setProvincias(new ArrayList<CenProvinciaBean>());
//			}
			List<CenPoblacionesBean> alPoblaciones = null;
			if(mutualidadForm.getIdProvincia()!= null && !mutualidadForm.getIdProvincia().equals("")){
				CenPoblacionesAdm poblacionesAdm = new CenPoblacionesAdm(this.getUserBean(request));
				alPoblaciones = poblacionesAdm.getPoblaciones(mutualidadForm.getIdProvincia());
				mutualidadForm.setPoblaciones(alPoblaciones);
			}else{
				mutualidadForm.setPoblaciones(new ArrayList<CenPoblacionesBean>());
			}
			List<String> alIdsBanco = new ArrayList<String>();
			if(mutualidadForm.getIdBanco()!=null && !mutualidadForm.getIdBanco().equals("")){

				alIdsBanco.add(mutualidadForm.getIdBanco());
			}
			request.setAttribute("idBancoSeleccionado",alIdsBanco);
			
			
			//mutualidadService.setMutualidadForm(mutualidadForm);

			mutualidadForm.setModo("consulta");
		
		return "consulta";
	}
	
	
	
	
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		UsrBean usrBean = this.getUserBean(request);
		MutualidadForm mutualidadForm 	= (MutualidadForm)formulario;

		String forward="exception";
		try {

			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			mutualidadService.insertarSolicitudMutualidad(mutualidadForm, usrBean);
//			String[] parametros = {mutualidadForm.getIdSolicitud(),mutualidadForm.getEstado()};
			if(mutualidadForm.getIdSolicitudAceptada()!=null && !mutualidadForm.getIdSolicitudAceptada().equals("")){
				String[] parametros = {mutualidadForm.getIdSolicitud(),mutualidadForm.getIdSolicitudAceptada(),mutualidadForm.getEstado(),""+CenSolicitudMutualidadBean.ESTADO_SOLICITADO};
				request.setAttribute("parametrosArray", parametros);
			}else{
				String[] parametros = {mutualidadForm.getIdSolicitud(),"",CenSolicitudMutualidadBean.ESTADO_PTERESPUESTA,""+CenSolicitudMutualidadBean.ESTADO_INICIAL};
				request.setAttribute("parametrosArray", parametros);
				
			}
			request.setAttribute("mensaje","messages.inserted.success");
			request.setAttribute("modal","");
			 
			forward = "exitoParametros";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
		

		return forward;



	}
	protected void getAjaxCuotaCapitalCobertura (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		
		//Recogemos el parametro enviado por ajax
		String idCobertura = request.getParameter("idCobertura");
		String idSexo = request.getParameter("idSexo");
		String fechaNacimiento = request.getParameter("fechaNacimiento");
		mutualidadForm.setIdCobertura(idCobertura);
		mutualidadForm.setIdSexo(idSexo);
		mutualidadForm.setFechaNacimiento(fechaNacimiento);
		BusinessManager bm = getBusinessManager();
		MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
		mutualidadService.setCobertura(mutualidadForm,this.getUserBean(request));
		
		//
		List<String> listaParametros = new ArrayList<String>();
		listaParametros.add(mutualidadForm.getCuotaCobertura());
		listaParametros.add(mutualidadForm.getCapitalCobertura());
		respuestaAjax(new AjaxXmlBuilder(), listaParametros,response );
	}
	
	
	
}