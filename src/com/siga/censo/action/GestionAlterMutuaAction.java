package com.siga.censo.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenProvinciaAdm;
import com.siga.beans.CenProvinciaBean;
import com.siga.beans.CenSolicitudAlterMutuaAdm;
import com.siga.beans.GenCatalogosWSAdm;
import com.siga.censo.form.AlterMutuaForm;
import com.siga.censo.service.AlterMutuaService;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.ws.alterMutua.AlterMutuaHelper;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionAlterMutuaAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getAccion();
					String modo = request.getParameter("modo");
					
					if(modo!=null) accion = modo;
					
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editar") || accion.equalsIgnoreCase("ver")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertar (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getTarifa")){
						mapDestino = getTarifa (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getEstado")){
						mapDestino = getEstado(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("getEstadoColegiado")){
						mapDestino = getEstadoColegiado(mapping, miForm, request, response);;
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
		
	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception 
			{
		UsrBean usrBean = this.getUserBean(request);
		AlterMutuaForm alterForm = (AlterMutuaForm)formulario;
		String forward = "";
		String idPersona = "";
		String idSolicitud = "";
		boolean mostrarSolicitud = false;
		try {
			Hashtable<String, String> datosAlter = new Hashtable<String, String>();
			
			if(request.getParameter("IDSOLICITUD")==null && request.getParameter("idPersona")!=null ){
				CenColegiadoAdm colAmd = new CenColegiadoAdm(usrBean);
				idPersona = request.getParameter("idPersona").toString();
				datosAlter.put("idPersona", idPersona);
				request.setAttribute("SOLINC", datosAlter);
				CenColegiadoBean colBean = colAmd.getDatosColegiales(Long.valueOf(idPersona), Integer.valueOf(usrBean.getLocation()));
				CenSolicitudAlterMutuaAdm alterAdm = new CenSolicitudAlterMutuaAdm(usrBean);
				Vector v = alterAdm.getSolicitudes(idPersona,null);
			}else if(request.getParameter("IDSOLICITUD")!=null){
				datosAlter.put("IDSOLICITUD", request.getParameter("IDSOLICITUD"));
				request.setAttribute("SOLINC", datosAlter);
				idSolicitud=request.getParameter("IDSOLICITUD");
			}
			mostrarSolicitud = true;
			alterForm.setIdPersona(idPersona);
			alterForm.setIdSolicitud(idSolicitud);
			alterForm = this.getRellenarFormulario(alterForm, this.getUserBean(request));
			
			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterForm = alterService.getEstadoColegiado(alterForm, this.getUserBean(request));
			
			if(!alterForm.isError() && (alterForm.getIdSolicitudalter()==null || alterForm.getIdSolicitudalter().equalsIgnoreCase(""))){
				forward = abrirConsulta(alterForm, usrBean);
			}else if(mostrarSolicitud){
				forward = "pestanas";
			}else{
				idPersona = request.getParameter("idPersona").toString();
				alterForm.setIdPersona(idPersona);
				forward = abrirConsulta(alterForm, usrBean);
			}
			//mutualidadForm.setModo("insertar");
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		UsrBean usr = this.getUserBean(request);
		
		String idSolicitud = request.getParameter("IDSOLICITUD");
		String idPersona = request.getParameter("idPersona");
		
		AlterMutuaForm alterForm = (AlterMutuaForm) formulario;
		try {
			
			if(mapping.getPath().endsWith("Alternativa"))
				alterForm.setPropuesta(AlterMutuaHelper.TipoPropuesta.Alternativa);
			else
				alterForm.setPropuesta(AlterMutuaHelper.TipoPropuesta.Oferta);
			alterForm.setIdSolicitud(idSolicitud);
			alterForm.setIdPersona(idPersona);
			alterForm = this.getRellenarFormulario(alterForm, this.getUserBean(request));
			
			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterForm = alterService.getPropuestas(alterForm, usr);
			
			List<String> alIdsPais = new ArrayList<String>();
			if(alterForm.getIdPais()!=null && !alterForm.getIdPais().equals("")){
				alIdsPais.add(alterForm.getIdPais());
			}
			request.setAttribute("idPaisSeleccionado",alIdsPais);
			List<CenProvinciaBean> provinciaBeans = null;
			CenProvinciaAdm provinciaAdm = new CenProvinciaAdm(usr);
			provinciaBeans = provinciaAdm.getProvincias("191");

			GenCatalogosWSAdm catAdm = new GenCatalogosWSAdm(usr); 

			ArrayList tiposDireccionList=new ArrayList();
			tiposDireccionList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPODIRECCION);
			alterForm.setTiposDireccion(tiposDireccionList);
			
			ArrayList idiomasList=new ArrayList();
			idiomasList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_IDIOMA);
			alterForm.setIdiomas(idiomasList);
			
			ArrayList tiposComunicacionList=new ArrayList();
			tiposComunicacionList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOCOMUNICACION);
			alterForm.setTiposComunicacion(tiposComunicacionList);
			
			ArrayList sexosList=new ArrayList();
			sexosList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_SEXO);
			alterForm.setSexos(sexosList);
			
			ArrayList tiposIdentificacionList=new ArrayList();
			tiposIdentificacionList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR);
			alterForm.setTiposIdentificacion(tiposIdentificacionList);
			
			ArrayList parentescoList=new ArrayList();
			parentescoList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_PARENTESCO);
			alterForm.setTiposParentesco(parentescoList);
			
			ArrayList estadoCivilList=new ArrayList();
			estadoCivilList = catAdm.getValores(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL);
			estadoCivilList.add(0,new ValueKeyVO("-1",""));
			alterForm.setEstadosCiviles(estadoCivilList);
			
			ArrayList paises=new ArrayList();
			paises = catAdm.getPaises();
			alterForm.setPaises(paises);
			
			ArrayList provincias = new ArrayList();
			provincias = catAdm.getProvincias();
			alterForm.setProvincias(provincias);

			alterForm.setModo("insertar");
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "abrir";
	}

	protected String insertar(	ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		UsrBean usrBean = this.getUserBean(request);
		AlterMutuaForm alterForm = (AlterMutuaForm)formulario;
		String forward="exception";
		try {

			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterService.setSolicitudAlta(alterForm, usrBean);
			
			JSONObject json = new JSONObject();
			json.put("error", alterForm.isError());
			json.put("idSolicitud", alterForm.getIdSolicitudalter());
			json.put("mensaje", alterForm.getMsgRespuesta());
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			 
			//forward = "exitoRefresco";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}

		return "";//exito(alterForm.getMsgRespuesta(),request);

	}
	
	protected String getTarifa(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		UsrBean usrBean = this.getUserBean(request);
		AlterMutuaForm alterForm = (AlterMutuaForm)formulario;
		String forward="exception";
		try {

			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterForm = alterService.getTarifaSolicitud(alterForm, usrBean);
			
			JSONObject json = new JSONObject();
			json.put("tarifa", UtilidadesString.formatoImporte(new Double( alterForm.getTarifaPropuesta() )));
			json.put("breve", alterForm.getBrevePropuesta());
			json.put("error", alterForm.isError());
			json.put("mensaje", alterForm.getMsgRespuesta());
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "";
	}
	
	protected String getEstado(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
					throws ClsExceptions,SIGAException  {
		UsrBean usrBean = this.getUserBean(request);
		AlterMutuaForm alterForm = (AlterMutuaForm)formulario;
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterForm = alterService.getEstadoSolicitud(alterForm, usrBean);
			
			JSONObject json = new JSONObject();
			json.put("error", alterForm.isError());
			json.put("mensaje", alterForm.getMsgRespuesta());
			json.put("ruta", alterForm.getRutaFichero());
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "";
	}

	protected String getEstadoColegiado(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
					throws ClsExceptions,SIGAException  {
		UsrBean usrBean = this.getUserBean(request);
		AlterMutuaForm alterForm = (AlterMutuaForm)formulario;
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			AlterMutuaService alterService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
			alterForm = alterService.getEstadoColegiado(alterForm, usrBean);
			
			JSONObject json = new JSONObject();
			json.put("error", alterForm.isError());
			json.put("mensaje", alterForm.getMsgRespuesta());
			response.setContentType("application/x-www-form-urlencoded;charset=ISO-8859-15");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "";
	}
	
	private AlterMutuaForm getRellenarFormulario(AlterMutuaForm form, UsrBean usr) throws Exception {
		BusinessManager bm = getBusinessManager();
		AlterMutuaService alterMutuaService = (AlterMutuaService)bm.getService(AlterMutuaService.class);
		form = alterMutuaService.getDatosSolicitud(form, usr);
		
		return form;
	}
	
	private String abrirConsulta(AlterMutuaForm form, UsrBean usr) throws Exception {
		
		AlterMutuaForm alterForm = (AlterMutuaForm)form;
		alterForm = this.getRellenarFormulario(alterForm, usr);

		return "consulta";
	}
	
}