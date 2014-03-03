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

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenPoblacionesBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.censo.form.MutualidadForm;
import com.siga.censo.service.MutualidadService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.ws.mutualidad.RespuestaMutualidad;

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
						if(mapping.getPath().equals("/CEN_MutualidadFichaPlan") || mapping.getPath().equals("/CEN_MutualidadPlanProfesional"))
							accion = "consultaPlanMutualidad";
						else if(mapping.getPath().equals("/CEN_MutualidadFichaSeguro") || mapping.getPath().equals("/CEN_MutualidadSeguroGratuito"))
							accion = "consultaSeguroUniversal";
					}
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicioFicha (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxCuotaCapitalCobertura")){
						getAjaxCuotaCapitalCobertura(mapping, miForm, request, response);
						return null;
					}else if ( accion.equalsIgnoreCase("nuevo")||
							accion.equalsIgnoreCase("consultaSeguroUniversal")||
							accion.equalsIgnoreCase("consultaPlanMutualidad")){
						mapDestino = abrir (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizaEstado")){
						mapDestino = actualizaEstado (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editar") || accion.equalsIgnoreCase("ver") ){
						mapDestino = inicioSolicitud(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizaEstadoMutualista")){
						mapDestino = actualizaEstadoMutualista (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("actualizaEstados")){
						mapDestino = actualizaEstados (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getAjaxPoblaciones")){
						getAjaxPoblaciones (mapping, miForm, request, response);
						return null;
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
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		BusinessManager bm = getBusinessManager();
		MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
		boolean ficha = true;
		try {
			
			String idPersona = (String)request.getParameter("idPersona");
			String idSolicitud = (String)request.getParameter("IDSOLICITUD");
			String accion = (String)request.getParameter("accion");
			UsrBean usr = this.getUserBean(request);
			
			// En funcion de la pestaña ponemos el tipo de solicitud
			if(mapping.getPath().equals("/CEN_MutualidadFichaPlan") || mapping.getPath().equals("/CEN_MutualidadPlanProfesional"))
				mutualidadForm.setIdTipoSolicitud("P");
			else if(mapping.getPath().equals("/CEN_MutualidadFichaSeguro") || mapping.getPath().equals("/CEN_MutualidadSeguroGratuito"))
				mutualidadForm.setIdTipoSolicitud("S");
			if(mapping.getPath().equals("/CEN_MutualidadSeguroGratuito")||mapping.getPath().equals("/CEN_MutualidadPlanProfesional"))
				ficha = false;
			// Si no es de ninguna pestaña ya vendra seteado en el formulario
			
			// Si no tenemos el numero de identificacion en el formulario es porque estamos en la ficha 
			// Solo vendran rellenos desde la solicitud de incorporacion porque se hace en la jsp
			if(idPersona!=null){
				// Aqui hay que recuperar los datos de la base de datos 
				mutualidadForm = mutualidadService.getDatosPersonaFicha(mutualidadForm, usr);
			}else{
				mutualidadForm = mutualidadService.getDatosSolicitudIncorporacion(mutualidadForm,idSolicitud ,usr);
			}
			
			// Aqui ya nos garantizamos que tenemos toda la informacion de la persona en el formulario
			// tanto desde la solicitud de incorporacion como desde la ficha

			CenSolicitudIncorporacionBean solIncBean = new CenSolicitudIncorporacionBean();
			solIncBean.setIdSolicitud(Long.parseLong(mutualidadForm.getIdSolicitudIncorporacion()));
			solIncBean.setIdInstitucion(Integer.parseInt(mutualidadForm.getIdInstitucion()));
			// Aquí he hecho un truco, si la solicitud procede de la ficha y no de una sol de incorporacion
			// el idSolicitudIncorporacion será el idPersona. De esta forma tambien nos garantizamos que
			// una persona solo pueda hacer una solicitud de alta en la mutualidad
			
			// Por defecto estaremos en modo insertar, a no ser que se encuentre una solicitud anterior
			mutualidadForm.setModo("insertar");
			
			//if(ficha){
				// Buscamos las solicitudes que pueda tener pendientes
				List<CenSolicitudMutualidadBean> solicitudMutualidadBeans=mutualidadService.getSolicitudesMutualidad(solIncBean, this.getUserBean(request));
				if(solicitudMutualidadBeans!=null && solicitudMutualidadBeans.size()>0){
					for(CenSolicitudMutualidadBean solBean:solicitudMutualidadBeans){
						if(mutualidadForm.getIdTipoSolicitud().equalsIgnoreCase(solBean.getIdTipoSolicitud()) 
							&& solBean.getIdSolicitudAceptada()!=null && !solBean.getIdSolicitudAceptada().toString().equalsIgnoreCase("0")) {
							// Nos quedaremos con la ultima peticion que sea del tipo que estamos buscando
							if(idPersona==null)idPersona=idSolicitud;
							mutualidadForm = mutualidadService.getSolicitudMutualidad(mutualidadForm, idPersona, solBean.getIdTipoSolicitud(), usr);
							mutualidadForm.setModo("consulta");
						}
					}
				}
				if(!mutualidadForm.getModo().equalsIgnoreCase("consulta")) {
					// Desde la ficha no se puede solicitar el alta del seguro gratuito
					// jbd // A peticion de la mutualidad dejamos abierto el seguro gratuito durante dos meses
					//if(ficha && mutualidadForm.getIdTipoSolicitud().equalsIgnoreCase("S")) {
					//	mutualidadForm.setModo("consulta");
					//}else {
						// No hay solicitudes previas. Debemos asegurarnos que pueda hacer la solicitud.
						// Con estado mutualista sabremos si tiene una solicitud previa, o no cumple las condiciones
						RespuestaMutualidad respuestaSolicitudAlta = mutualidadService.isPosibilidadSolicitudAlta(mutualidadForm.getNumeroIdentificacion(), mutualidadForm.getFechaNacimiento(),usr);
						if (!respuestaSolicitudAlta.isPosibleAlta() || 
							(!mutualidadForm.getIdTipoIdentificacion().equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_IDENTIFICACION_NIF)) && 
							 !mutualidadForm.getIdTipoIdentificacion().equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE)))){
							mutualidadForm.setModo("consulta");
							//mutualidadForm.setEstadoMutualista(respuestaSolicitudAlta.getValorRespuesta());
						}
					//}
				}
			//}
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

			mutualidadService.setMutualidadForm(mutualidadForm, this.getUserBean(request));
//			mutualidadService.setMutualidadFormDefecto(mutualidadForm);
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "edicion";
	}

	protected String editar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		
		String idSolicitud = request.getParameter("IDSOLICITUD");
		
		
		try {
			mutualidadForm = this.getRellenarFormulario(mutualidadForm, idSolicitud, this.getUserBean(request));
			
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
			mutualidadForm.setIdTipoSolicitud("P");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "pestanas";
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
			String[] parametros = {mutualidadForm.getIdEstado(),mutualidadForm.getEstado(), mutualidadForm.getPDF()};
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
			String[] parametros = {mutualidadForm.getEstadoMutualista(),mutualidadForm.getPDF()};
			request.setAttribute("parametrosArray", parametros);
			
		
		request.setAttribute("mensaje","messages.updated.success");
		request.setAttribute("modal","");
		 
		forward = "exitoParametros";
			

		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return forward;
	}
	
	protected String actualizaEstados (ActionMapping mapping, 		
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
			mutualidadService.actualizaEstadoSolicitud(mutualidadForm, this.getUserBean(request));
			
			String[] parametros = {mutualidadForm.getEstadoMutualista(), mutualidadForm.getEstado(), mutualidadForm.getPDF()};		
			
			request.setAttribute("parametrosArray", parametros);
			//request.setAttribute("mensaje","messages.updated.success");
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
			datosMutualidad.put("accion", request.getParameter("accion"));
			request.setAttribute("datosMutualidad", datosMutualidad);
			//mutualidadService.setMutualidadForm(mutualidadForm);

			//mutualidadForm.setModo("insertar");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "inicioFicha";
	}

	protected String inicioSolicitud (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		MutualidadForm mutualidadForm = (MutualidadForm) formulario;
		try {
			Hashtable<String, String> datosMutualidad = new Hashtable<String, String>();
			datosMutualidad.put("IDSOLICITUD", request.getParameter("IDSOLICITUD"));
//			datosMutualidad.put("accion",mutualidadForm.getAccion());
			request.setAttribute("SOLINC", datosMutualidad);
			//mutualidadService.setMutualidadForm(mutualidadForm);

			//mutualidadForm.setModo("insertar");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "pestanas";
	}
		
	protected MutualidadForm getMutualidadForm (MutualidadForm mutualidadForm, String idPersona, String idTipoSolicitud,UsrBean usrBean) throws ClsExceptions, SIGAException 
	{
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			mutualidadForm = mutualidadService.getSolicitudMutualidad(mutualidadForm,idPersona, idTipoSolicitud, usrBean);
			
			return mutualidadForm;

			
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
			if(mapping.getPath().equalsIgnoreCase("/CEN_MutualidadSeguroGratuito")||mapping.getPath().equalsIgnoreCase("/CEN_MutualidadPlanProfesional")) {
				mutualidadForm.setOrigenSolicitud(CenSolicitudMutualidadBean.SOLICITUD_INCORPORACION);
			}else {
				mutualidadForm.setOrigenSolicitud(CenSolicitudMutualidadBean.FICHA_COLEGIAL);
			}

			try{
				//Vamos a parsear el IBAN para formar la CCC antigua. Esto va a fallar como venga un IBAN extranjero, que quede claro
				if(mutualidadForm.getIban()!=null && !mutualidadForm.getIban().equals("") && mutualidadForm.getIban().substring(0,2).equals("ES")){
					mutualidadForm.setCboCodigo(mutualidadForm.getIban().substring(4,8));
					mutualidadForm.setCodigoSucursal(mutualidadForm.getIban().substring(8,12));
					mutualidadForm.setDigitoControl(mutualidadForm.getIban().substring(12,14));		
					mutualidadForm.setNumeroCuenta(mutualidadForm.getIban().substring(14));				
				}else{
					throw new SIGAException ("Solo se admiten códigos IBAN españoles");
				}
			} catch (Exception e){
				throw new SIGAException ("Solo se admiten códigos IBAN españoles");
			}			
			
			RespuestaMutualidad respuesta = mutualidadService.insertarSolicitudMutualidad(mutualidadForm, usrBean);
			
			if(mutualidadForm.getIdSolicitudAceptada()!=null && !mutualidadForm.getIdSolicitudAceptada().equals("") && !mutualidadForm.getIdSolicitudAceptada().equals("0")){
				String[] parametros = {mutualidadForm.getIdSolicitud(),mutualidadForm.getIdSolicitudAceptada(),mutualidadForm.getEstado(),""+CenSolicitudMutualidadBean.ESTADO_SOLICITADO};
				request.setAttribute("parametrosArray", parametros);
			}else{
				String[] parametros = {mutualidadForm.getIdSolicitud(),"",CenSolicitudMutualidadBean.ESTADO_PTERESPUESTA,""+CenSolicitudMutualidadBean.ESTADO_INICIAL};
				request.setAttribute("parametrosArray", parametros);
				
			}
			if(respuesta.getMensajeError()!=null && !respuesta.getMensajeError().equalsIgnoreCase("")){
				forward = exito(respuesta.getMensajeError(),request);
			}else{
				if(mapping.getPath().equalsIgnoreCase("/CEN_Mutualidad")) {
					request.setAttribute("modal","");
					forward = "exitoParametros";
				}else {
					forward = exitoRefresco("messages.updated.success",request);
				}
			}
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
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
	
	private MutualidadForm getRellenarFormulario(MutualidadForm form, String idSolicitud, UsrBean usr) throws Exception {
			BusinessManager bm = getBusinessManager();
			MutualidadService mutualidadService = (MutualidadService)bm.getService(MutualidadService.class);
			form = mutualidadService.getDatosSolicitudIncorporacion(form, idSolicitud, usr);
			
			return form;
	}
	
}