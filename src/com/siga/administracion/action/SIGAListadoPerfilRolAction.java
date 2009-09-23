package com.siga.administracion.action;

import java.util.*;
import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.administracion.form.*;

public class SIGAListadoPerfilRolAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAListadoPerfilRolForm form = (SIGAListadoPerfilRolForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmPerfilRolAdm perfilRolAdm = new AdmPerfilRolAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();

        Vector datos = perfilRolAdm.selectConDescripciones(institucion);
        
        request.setAttribute("datos", datos);
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAListadoPerfilRolForm form = (SIGAListadoPerfilRolForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmPerfilRolAdm perfilRolAdm = new AdmPerfilRolAdm (this.getUserBean(request));
        
        String idInstitucion = userBean.getLocation();
        String idRol=form.getIdRol();
        String idPerfilPorDefecto=form.getGrupoPorDefecto();
        
        if (perfilRolAdm.setGrupoPorDefecto(userBean, idInstitucion, idRol, idPerfilPorDefecto))
        {
            request.setAttribute("mensaje","messages.updated.success");
            
            String mensaje = "El Grupo por defecto \"" + idPerfilPorDefecto + "\" ha sido asignado al Rol con ID=\"" + idRol + "\"";	

            CLSAdminLog.escribirLogAdmin(request,mensaje);
        }
        
        else
        {
            request.setAttribute("mensaje","messages.updated.error");
        }

        request.setAttribute("modal","1");
	    
        return "exito";

	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAListadoPerfilRolForm form = (SIGAListadoPerfilRolForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        AdmPerfilRolBean perfilRolBean = new AdmPerfilRolBean();
        
        String idInstitucion = userBean.getLocation();
        
        Vector vOcultos = form.getDatosTablaOcultos(0);

        String idRol = ((String)vOcultos.elementAt(0)).trim();
        String descRol = ((String)vOcultos.elementAt(1)).trim();
        String idPerfil = ((String)vOcultos.elementAt(2)).trim();
        String descPerfil = ((String)vOcultos.elementAt(3)).trim();
        String grupoPorDefecto = ((String)vOcultos.elementAt(4)).trim();
        
        Hashtable htDatos = new Hashtable();
        
        htDatos.put(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_IDROL, idRol);
        htDatos.put(AdmRolBean.T_NOMBRETABLA + "_" + AdmRolBean.C_DESCRIPCION, descRol);
        htDatos.put(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_IDPERFIL, idPerfil);
        htDatos.put(AdmPerfilBean.T_NOMBRETABLA + "_" + AdmPerfilBean.C_DESCRIPCION, descPerfil);
        htDatos.put(AdmPerfilRolBean.T_NOMBRETABLA + "_" + AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, grupoPorDefecto);

        Vector datos = new Vector();
        
        datos.add(htDatos);
        
        request.setAttribute("datos", datos);
        
		return "mostrar";
	}
}