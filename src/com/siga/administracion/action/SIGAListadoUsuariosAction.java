package com.siga.administracion.action;

import java.io.IOException;
import java.util.*;

import com.atos.utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.ActaComisionForm;
import com.siga.ws.CajgConfiguracion;

import javax.servlet.http.*;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts.action.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.siga.administracion.form.*;

public class SIGAListadoUsuariosAction extends MasterAction
{
	
	@Override
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			if (accion == null || accion.equalsIgnoreCase("")
					|| accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscar")) {
				mapDestino = buscar(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("modificar")) {
				mapDestino = modificar(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("borrar")) {
				mapDestino = borrar(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("getPerfilesUsuario")) {
				getPerfiles(mapping, miForm, request, response);
				return null;
			} else if (accion.equalsIgnoreCase("setDatosUsuario")) {
				setDatosUsuario(mapping, miForm, request, response);
				return null;
			} else {
				return super.executeInternal(mapping, formulario, request,
						response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				// mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}

				throw new ClsExceptions("El ActionMapping no puede ser nulo",
						"", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.censo" }); // o el recurso del modulo
														// que sea
		}
		return mapping.findForward(mapDestino);
	}


	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAListadoUsuariosForm form = (SIGAListadoUsuariosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();       
        
        //Vector datos = usuariosAdm.selectNLS(where);
        Vector datos = usuariosAdm.getBusquedaUsuarios(userBean.getLocation(), form.getDescripcion(), form.getIdRolBusqueda(),form.getIdGrupoBusqueda(), form.getNIF(), form.getActivoConsulta());
        
        
        // Añadimos a la request los perfiles de la institución 
        AdmPerfilAdm perfilAdm = new AdmPerfilAdm(this.getUserBean(request));
        Vector perfiles = perfilAdm.getPerfiles(institucion);
        
        AdmRolAdm rolAdm = new AdmRolAdm(this.getUserBean(request));
        Vector roles=rolAdm.select();
        
        request.setAttribute("datos", datos);
        request.setAttribute("perfiles", perfiles);
        request.setAttribute("roles", roles);
        
        return "buscar";
	}


	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
        SIGAListadoUsuariosForm form = (SIGAListadoUsuariosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm (this.getUserBean(request));
        
	    
	    AdmUsuariosBean bean = new AdmUsuariosBean();
	    bean.setIdInstitucion(Integer.valueOf(form.getIdInstitucion()));
	    bean.setIdUsuario(Integer.valueOf(form.getIdUsuario()));
	    bean.setActivo(form.getActivo().equalsIgnoreCase("on")?"S":"N");
	    bean.setCodigoExt(form.getCodigoExt());
	    
	    String [] campos={AdmUsuariosBean.C_ACTIVO,AdmUsuariosBean.C_CODIGOEXT};
	    String [] claves=null;

        if (usuariosAdm.updateDirect(bean, claves, campos)){
        	//usuariosAdm.updateGruposUsuario(bean.getIdUsuario().toString(), bean.getIdInstitucion().toString(), form.getGruposUsuario());
            return exitoRefresco("messages.updated.success", request);
        }else{
            return exito("messages.updated.error", request);
        }
	    
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAListadoUsuariosForm form = (SIGAListadoUsuariosForm)formulario;
	    
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    
	    Hashtable hash = new Hashtable();
	    
	    hash.put(AdmUsuariosBean.C_IDUSUARIO, (String)vOcultos.elementAt(0));
	    hash.put(AdmUsuariosBean.C_IDINSTITUCION, userBean.getLocation());
	    
	    if (usuariosAdm.delete(hash))
	    {
	        request.setAttribute("mensaje","messages.deleted.success");

            String mensaje = "El Usuario \"" + (String)vOcultos.elementAt(2) + "\" (NIF: " +
            	             (String)vOcultos.elementAt(4)+") ha sido borrado";	
            
            CLSAdminLog.escribirLogAdmin(request,mensaje);
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","error.messages.deleted");
	    }

	    request.setAttribute("urlAction", "/SIGA/ADM_GestionarUsuarios.do?modo=buscar");
        request.setAttribute("hiddenFrame", "1");
	    
	    return "exito";
	}

	
	private void getPerfiles(ActionMapping mapping, MasterForm miForm,
			HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, IOException {
		
		
		AdmUsuariosEfectivosPerfilAdm perfilesAdm = new AdmUsuariosEfectivosPerfilAdm(this.getUserBean(request));
		
		String idUsuario = (String)request.getParameter("idUsuario");
		String idInstitucion = (String)request.getParameter("idInstitucion");
		if (idUsuario==null||idUsuario.trim().equalsIgnoreCase("")||idInstitucion==null||idInstitucion.trim().equalsIgnoreCase(""))
			throw new SIGAException("No se han podido recuperar los datos del usuario");	
				
		Vector<HashMap<String,String>> perfiles = perfilesAdm.getPerfilesRolesUsuarios(idInstitucion, idUsuario);
		
		JSONObject json = new JSONObject();				
		try {
			json.put("perfilesrol", perfiles);
		} catch (JSONException e) {
			throw new SIGAException("No se han podido recuperar los datos del usuario");
		}
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
		 response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
		
	}
	
	private void setDatosUsuario(ActionMapping mapping, MasterForm miForm,
			HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, IOException, JSONException {
		
		AdmUsuariosEfectivosPerfilAdm perfilesAdm = new AdmUsuariosEfectivosPerfilAdm(this.getUserBean(request));
		
		String idUsuario = (String)request.getParameter("idUsuario");
		String idInstitucion = (String)request.getParameter("idInstitucion");
		boolean activo = Boolean.parseBoolean(request.getParameter("activo"));
		String codigoExt = (String)request.getParameter("codigoExt");
		String perfilesAlta = (String)request.getParameter("perfilesAlta");
		String perfilesBaja = (String)request.getParameter("perfilesBaja");
		
		if (idUsuario==null||idUsuario.trim().equalsIgnoreCase("")||idInstitucion==null||idInstitucion.trim().equalsIgnoreCase(""))
			throw new SIGAException("No se han podido recuperar los datos del usuario");	
					
		AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm(this.getUserBean(request));
		AdmUsuariosBean bean = new AdmUsuariosBean();
	    bean.setIdInstitucion(Integer.valueOf(idInstitucion));
	    bean.setIdUsuario(Integer.valueOf(idUsuario));
	    bean.setActivo(activo?"S":"N");
	    bean.setCodigoExt(codigoExt);
		
	    String [] campos={AdmUsuariosBean.C_ACTIVO,AdmUsuariosBean.C_CODIGOEXT};
	    String [] claves=null;

	    JSONObject json = new JSONObject();
	    
        if (usuariosAdm.updateDirect(bean, claves, campos)){
        	json.put("msg", "Usuario actualizado correctamente");
        }else{
        	json.put("msg", "No se ha podido actualizar el usuario");
        }
        
		JsonParser parser = new JsonParser();
		JsonArray arrayPerfilesAlta = parser.parse(perfilesAlta).getAsJsonArray();
		JsonArray rolPerfil;
		for (int i = 0; i < arrayPerfilesAlta.size(); i++) {
			// Cada perfil viene como [codRol,codPerfil]
			rolPerfil=arrayPerfilesAlta.get(i).getAsJsonArray();
			perfilesAdm.setPerfilRolUsuario(idUsuario, idInstitucion, rolPerfil.get(0).getAsString(), rolPerfil.get(1).getAsString());
		}
		
		JsonArray arrayPerfilesBaja = parser.parse(perfilesBaja).getAsJsonArray();
		for (int i = 0; i < arrayPerfilesBaja.size(); i++) {
			// Cada perfil viene como [codRol,codPerfil]
			rolPerfil=arrayPerfilesBaja.get(i).getAsJsonArray();
			perfilesAdm.deletePerfilRolUsuario(idUsuario, idInstitucion, rolPerfil.get(0).getAsString(), rolPerfil.get(1).getAsString());
		}
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
		 response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
	}
	
}