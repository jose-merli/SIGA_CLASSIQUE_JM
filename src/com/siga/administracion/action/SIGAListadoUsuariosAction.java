package com.siga.administracion.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.siga.administracion.form.*;

public class SIGAListadoUsuariosAction extends MasterAction
{
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
        String descripcion = form.getDescripcion();
        String activo = form.getActivoConsulta();

        String where = " WHERE ";
        
        where += AdmUsuariosBean.C_IDINSTITUCION + " = " + institucion;
        where += (descripcion!=null && !descripcion.equals("")) ? " AND "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),AdmUsuariosBean.C_DESCRIPCION ) : "";
        where += (activo!=null && !activo.equals("")) ? " AND " + AdmUsuariosBean.C_ACTIVO + " = '" + activo + "'" : "";

        Vector datos = usuariosAdm.selectNLS(where);
        
        request.setAttribute("datos", datos);
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
        SIGAListadoUsuariosForm form = (SIGAListadoUsuariosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmUsuariosAdm usuariosAdm = new AdmUsuariosAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(AdmUsuariosBean.C_ACTIVO, form.getActivo());
	    
        if (usuariosAdm.update(hashNew, hashOld))
        {
            request.setAttribute("mensaje","messages.updated.success");
            request.removeAttribute("DATABACKUP");

            String mensaje = "El Usuario \"" + (String)hashNew.get(AdmUsuariosBean.C_DESCRIPCION) + "\" (NIF: " +
            	             (String)hashNew.get(AdmUsuariosBean.C_NIF)+") ha sido ";	
			
            if (form.getActivo().equalsIgnoreCase("S"))
            {
                mensaje+="activado";
			}
            
            else 
            {
				mensaje+="desactivado";
			}
            
			CLSAdminLog.escribirLogAdmin(request,mensaje);
        }
        
        else
        {
            request.setAttribute("mensaje","messages.updated.error");
        }

        request.setAttribute("modal","1");
	    
        return "exito";
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

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions
	{
        SIGAListadoUsuariosForm form = (SIGAListadoUsuariosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        AdmUsuariosBean usuariosBean = new AdmUsuariosBean();
        
		Vector vVisibles = form.getDatosTablaVisibles(0);
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idUsuario = (String)vOcultos.elementAt(0);
        String idInstitucion = (String)vOcultos.elementAt(1);
        String descripcion = (String)vOcultos.elementAt(2);
        String idLenguaje = (String)vOcultos.elementAt(3);
        String nif = (String)vOcultos.elementAt(4);
        String activo = (String)vOcultos.elementAt(5);
        String grupos = (String)vOcultos.elementAt(6);
        String fechaAlta = (String)vOcultos.elementAt(7);
        String usuMod = (String)vOcultos.elementAt(8);
        String fechaMod = (String)vOcultos.elementAt(9);
        
        usuariosBean.setIdUsuario(new Integer(idUsuario));
        usuariosBean.setIdInstitucion(new Integer(idInstitucion));
        usuariosBean.setDescripcion(descripcion);
        usuariosBean.setIdLenguaje(idLenguaje);
        usuariosBean.setNIF(nif);
        usuariosBean.setActivo(activo);
        usuariosBean.setGrupos(grupos);
        usuariosBean.setFechaAlta(fechaAlta);
        usuariosBean.setUsuMod(new Integer(usuMod));
        usuariosBean.setFechaMod(fechaMod);

        Vector datos = new Vector();
        datos.add(usuariosBean);
        datos.add(descripcion);
        
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(AdmUsuariosBean.C_IDUSUARIO, idUsuario);
            hashBackUp.put(AdmUsuariosBean.C_IDINSTITUCION, idInstitucion);
            hashBackUp.put(AdmUsuariosBean.C_DESCRIPCION, descripcion);
            hashBackUp.put(AdmUsuariosBean.C_IDLENGUAJE, idLenguaje);
            hashBackUp.put(AdmUsuariosBean.C_NIF, nif);
            hashBackUp.put(AdmUsuariosBean.C_ACTIVO, activo);
            hashBackUp.put(AdmUsuariosBean.C_FECHA_ALTA, fechaAlta);
            hashBackUp.put(AdmUsuariosBean.C_USUMODIFICACION, usuMod);
            hashBackUp.put(AdmUsuariosBean.C_FECHAMODIFICACION, fechaMod);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}