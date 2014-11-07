package com.siga.administracion.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import com.atos.utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.siga.general.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.*;
import com.siga.beans.AdmGestionPermisosAdm;
import com.siga.censo.form.AlterMutuaForm;
import com.siga.censo.service.AlterMutuaService;

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
				getProcesos(mapping, miForm, request, response);
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
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAConfigurarPermisosAplicacionForm form = (SIGAConfigurarPermisosAplicacionForm)formulario;
        
        request.getSession().setAttribute("idPerfil", form.getIdPerfil());

        return "buscar";
	}
    
    private void getProcesos(ActionMapping mapping, MasterForm form,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			AdmGestionPermisosAdm permisosAdm = new AdmGestionPermisosAdm(usrBean);
			Vector procesos = permisosAdm.getProcesos();
			
			JSONObject json = new JSONObject();
			
			json.put("procesos", procesos);
			
			response.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("X-JSON", json.toString());
			response.getWriter().write(json.toString()); 
			
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return;
	}
    
    private void getPermisos(ActionMapping mapping, MasterForm form,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		String perfil="";
		SIGAConfigurarPermisosAplicacionForm miForm = (SIGAConfigurarPermisosAplicacionForm)form;
		try {
			perfil=miForm.getIdPerfil();
			AdmGestionPermisosAdm permisosAdm = new AdmGestionPermisosAdm(usrBean);
			Vector permisos = permisosAdm.getPermisos(usrBean.getLocation(), perfil);
			
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
		return;
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
			String permisos=(String)request.getParameter("permisos");
			JsonParser parser = new JsonParser();
			JsonArray arrayPermisos = parser.parse(permisos).getAsJsonArray();
			JsonArray permiso;
			for (int i = 0; i < arrayPermisos.size(); i++) {
				// Cada permiso viene como [idproceso,permiso]
				permiso = arrayPermisos.get(i).getAsJsonArray();
				permisosAdm.setPermiso(usrBean.getLocation(), perfil, permiso.get(0).getAsString(), permiso.get(1).toString());
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

		return;
	}
}