package com.siga.general;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmUsuariosAdm;

public class UsuariosAction extends MasterAction{
	
	/** 
	 *  Funcion que devuelve la información de un usuario
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {				    		
			String modo = (String) request.getParameter("modo");

			if (modo!=null && modo.equalsIgnoreCase("getAjaxObtenerInfoUsuario")){
				getAjaxObtenerInfoUsuario (request, response);
			} 
		} catch (SIGAException es) {
			throw es;
			
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
		return null;
	}
	
	/**
	 * Obtiene la información del usuario
	 * @param request
	 * @param response
	 */
	protected void getAjaxObtenerInfoUsuario(HttpServletRequest request, HttpServletResponse response) throws Exception {	

		// Controles generales
		UsrBean usr = this.getUserBean(request);
		Hashtable datosUsuario;
		ArrayList<String> aOptionsListadoDocumentacion = new ArrayList<String>();
		JSONObject json = new JSONObject();;

		AdmUsuariosAdm usuAdm = new AdmUsuariosAdm(usr);
		Vector v = usuAdm.getDatosUsuario(usr.getUserName(), usr.getLocation());
		
		if(v != null && v.size()>0){	
			datosUsuario = (Hashtable)v.get(0);
			json.put("Encontrado", "SI");
			json.put("Nombre", datosUsuario.get("NOMBRE_USUARIO"));
			json.put("DNI", datosUsuario.get("NIF_USUARIO"));
			json.put("Grupo", datosUsuario.get("NOMBRE_GRUPO"));
			json.put("Institucion", datosUsuario.get("NOMBRE_INSTITUCION"));
			json.put("FechaAcceso", datosUsuario.get("FECHA_REGISTRO"));
	
				
		}else{
			json.put("Encontrado", "NO");
		}
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 	
	}
		
	
}