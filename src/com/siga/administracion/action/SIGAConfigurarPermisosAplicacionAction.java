package com.siga.administracion.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.CenInstitucion;
import org.redabogacia.sigaservices.app.services.cen.CenInstitucionService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.form.SIGAConfigurarPermisosAplicacionForm;
import com.siga.beans.AdmGestionPermisosAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class SIGAConfigurarPermisosAplicacionAction extends MasterAction
{
	
	private static String ACCION_GET_PROCESOS = "GETPROCESOS";
	private static String ACCION_GET_PERMISOS = "GETPERMISOS";
	private static String ACCION_SET_PERMISOS = "SETPERMISOS";
	
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			miForm = (MasterForm) formulario;
			if (miForm == null)
				return mapping.findForward(mapDestino);
			
			String accion = miForm.getModo();
			
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);						
			}else if(accion.equalsIgnoreCase(ACCION_GET_PROCESOS)){
				return null;
			}else if(accion.equalsIgnoreCase(ACCION_GET_PERMISOS)){
				getPermisos(mapping, miForm, request, response);
				return null;
			}else if(accion.equalsIgnoreCase(ACCION_SET_PERMISOS)){
				setPermisos(mapping, miForm, request, response);
				return null;
			}
			
			return mapping.findForward(mapDestino);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		request.setAttribute("procesos", getProcesos(this.getUserBean(request)));
		
		// Ponemos modo debug para los administradores
		request.setAttribute("debug", this.getUserBean(request).isEntradaCombos());
		UsrBean usr = this.getUserBean(request);
		String permiso= usr.getAccessForProcessNumber("84", request);
		
		if(permiso.equalsIgnoreCase(SIGAConstants.ACCESS_FULL)){
			request.setAttribute("permiso", true);
		}else{
			request.setAttribute("permiso", false);
		}
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAConfigurarPermisosAplicacionForm form = (SIGAConfigurarPermisosAplicacionForm)formulario;
        
        request.getSession().setAttribute("idPerfil", form.getIdPerfil());

        return "buscar";
	}
    
    private String getProcesos(UsrBean usrBean) throws SIGAException {
		String forward="exception";
		Vector procesos = null;
		JSONObject json = new JSONObject();
		try {
			
			AdmGestionPermisosAdm permisosAdm = new AdmGestionPermisosAdm(usrBean);
			procesos = permisosAdm.getProcesos();
			
			json.put("procesos", procesos);
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return json.toString(); 
	}
    
    private void getPermisos(ActionMapping mapping, MasterForm form,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		String perfil="";
		SIGAConfigurarPermisosAplicacionForm miForm = (SIGAConfigurarPermisosAplicacionForm)form;
		try {
			perfil=miForm.getIdPerfil();
			String idInstitucion = usrBean.getLocation();
			if(perfil.equals("CJG")){
				BusinessManager bm = BusinessManager.getInstance();
				CenInstitucionService cenInstitucionService = (CenInstitucionService)bm.getService(CenInstitucionService.class);
				CenInstitucion cenInstitucion = new CenInstitucion();
				cenInstitucion.setIdinstitucion(Short.valueOf(idInstitucion));
				CenInstitucion comision =  cenInstitucionService.getComision(cenInstitucion);
				if(comision!=null && comision.getIdinstitucion()!=null && !comision.getIdinstitucion().equals(idInstitucion)){
					idInstitucion = comision.getIdinstitucion().toString();
				}
				
			}
			AdmGestionPermisosAdm permisosAdm = new AdmGestionPermisosAdm(usrBean);
			
			String inicio=(String)request.getParameter("inicio");
			String cantidad=(String)request.getParameter("cantidad");
			
			Vector permisos = permisosAdm.getPermisosPagina(idInstitucion, perfil, inicio, cantidad);
			JSONObject json = new JSONObject();
			
			json.put("permisos", permisos);
			
			response.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
	}
    
    private void setPermisos(ActionMapping mapping, MasterForm form,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException, Exception {
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		String perfil="";
		AdmGestionPermisosAdm permisosAdm = new AdmGestionPermisosAdm(usrBean);
		JSONObject json = new JSONObject();
		try {
			perfil=(String)request.getParameter("perfil");
			String idInstitucion = usrBean.getLocation();
			if(perfil.equals("CJG")){
				BusinessManager bm = BusinessManager.getInstance();
				CenInstitucionService cenInstitucionService = (CenInstitucionService)bm.getService(CenInstitucionService.class);
				CenInstitucion cenInstitucion = new CenInstitucion();
				cenInstitucion.setIdinstitucion(Short.valueOf(idInstitucion));
				CenInstitucion comision =  cenInstitucionService.getComision(cenInstitucion);
				if(comision!=null && comision.getIdinstitucion()!=null && !comision.getIdinstitucion().equals(idInstitucion)){
					idInstitucion = comision.getIdinstitucion().toString();
				}
				
			}
			
			String permisos=(String)request.getParameter("permisos");
			JsonParser parser = new JsonParser();
			JsonArray arrayPermisos = parser.parse(permisos).getAsJsonArray();
			JsonArray permiso;
			for (int i = 0; i < arrayPermisos.size(); i++) {
				// Cada permiso viene como [idproceso,permiso]
				permiso = arrayPermisos.get(i).getAsJsonArray();
				permisosAdm.setPermiso(idInstitucion, perfil, permiso.get(0).getAsString(), permiso.get(1).toString());
			}
			json.put("msg", UtilidadesString.getMensajeIdioma(usrBean.getLanguage(), "messages.updated.success"));

		} catch (Exception e) {
			
			json.put("msg", UtilidadesString.getMensajeIdioma(usrBean.getLanguage(), "messages.updated.error"));
			
		}
		response.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
	}
}