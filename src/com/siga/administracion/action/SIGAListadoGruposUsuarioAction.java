package com.siga.administracion.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import org.apache.struts.action.*;
import com.siga.administracion.form.*;

public class SIGAListadoGruposUsuarioAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAListadoGruposUsuarioForm form = (SIGAListadoGruposUsuarioForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmPerfilAdm gruposUsuarioAdm = new AdmPerfilAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();

        String where = " WHERE ";
        
        where += AdmPerfilBean.C_IDINSTITUCION + " = " + institucion;

        Vector datos = gruposUsuarioAdm.select(where);
        
        request.setAttribute("datos", datos);
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, false);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, true);
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
        SIGAListadoGruposUsuarioForm form = (SIGAListadoGruposUsuarioForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmPerfilAdm gruposUsuarioAdm = new AdmPerfilAdm (this.getUserBean(request));
        AdmPerfilBean gruposUsuarioBean = new AdmPerfilBean();
        
        String sIdPerfil = form.getPerfil();
        String sDescPerfil = form.getDescripcion();
        String sIdInstitucion = userBean.getLocation();
        
        gruposUsuarioBean.setIdPerfil(sIdPerfil);
        gruposUsuarioBean.setDescripcion(sDescPerfil);
        gruposUsuarioBean.setIdInstitucion(sIdInstitucion);
        gruposUsuarioBean.setNivelPerfil(new Integer(0));
        
        UserTransaction tx = userBean.getTransaction();
        
        try
        {
            tx.begin();
        }

        catch(Exception e)
        {
            throw new ClsExceptions(e, "Error al abrir la transacción");
        }
        
        if (gruposUsuarioAdm.insert(gruposUsuarioBean))
        {
        	String sql="";
        	Row row = new Row();

            Hashtable htRoles = new Hashtable();
            htRoles.put(AdmPerfilRolBean.C_IDPERFIL, sIdPerfil);
            htRoles.put(AdmPerfilRolBean.C_IDINSTITUCION, sIdInstitucion);
            
            row.load(htRoles);

        	sql = "SELECT * FROM "+ AdmPerfilRolBean.T_NOMBRETABLA+ " WHERE ";
        	sql += AdmPerfilRolBean.C_IDPERFIL + " = '" + sIdPerfil + "' AND ";
        	sql += AdmPerfilRolBean.C_IDINSTITUCION + " = " + sIdInstitucion; 

        	if (row.findForUpdate(sql)) 
        	{
        	    row.delete(AdmPerfilRolBean.T_NOMBRETABLA, new String[] {AdmPerfilRolBean.C_IDPERFIL, AdmPerfilRolBean.C_IDINSTITUCION});
       	  	}
            
        	try
        	{
        	    String misRoles = form.getRoles();
        	    
        	    if (misRoles!=null)
        	    {
        	        StringTokenizer st = new StringTokenizer(misRoles, ",");
        	        
        	        while (st.hasMoreTokens())
        	        {
        	            String rol = st.nextToken();
        	              
        	            Object[] fields = new Object[] {};
        	            Vector vFields = new Vector();
  
        	            row = new Row();
  
        	            row.setValue(AdmPerfilRolBean.C_IDINSTITUCION, sIdInstitucion);
        	            row.setValue(AdmPerfilRolBean.C_IDPERFIL, sIdPerfil);
        	            row.setValue(AdmPerfilRolBean.C_IDROL, rol);
        	            row.setValue(AdmPerfilRolBean.C_USUMODIFICACION, userBean.getUserName());
        	            row.setValue(AdmPerfilRolBean.C_FECHAMODIFICACION, "SYSDATE");

        	            vFields.add(AdmPerfilRolBean.C_IDINSTITUCION);
        	            vFields.add(AdmPerfilRolBean.C_IDPERFIL);
        	            vFields.add(AdmPerfilRolBean.C_IDROL);
        	            vFields.add(AdmPerfilRolBean.C_USUMODIFICACION);
        	            vFields.add(AdmPerfilRolBean.C_FECHAMODIFICACION);

        	            fields = vFields.toArray();
        	            row.add(AdmPerfilRolBean.T_NOMBRETABLA, fields);
        	        }
        	    }
        	    
        	    tx.commit();
        	}
        	
        	catch(Exception e)
        	{
        	    throw new ClsExceptions(e, "Error al completar la transacción");
        	}
            
            request.setAttribute("mensaje","messages.updated.success");
            
            String mensaje = "El Grupo \"" + sDescPerfil + "\"" + " ha sido insertado";	

            CLSAdminLog.escribirLogAdmin(request,mensaje);
        }
        
        request.setAttribute("modal","1");

        return "exito";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAListadoGruposUsuarioForm form = (SIGAListadoGruposUsuarioForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        AdmPerfilAdm gruposUsuarioAdm = new AdmPerfilAdm (this.getUserBean(request));

        UserTransaction tx = userBean.getTransaction();
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(AdmPerfilBean.C_DESCRIPCION, form.getDescripcion());
	    
	    try
	    {
	        tx.begin();
	    }
	    
	    catch(Exception e)
	    {
	        throw new ClsExceptions(e, "Error al abrir la transacción");
	    }

    	try
    	{
	        if (gruposUsuarioAdm.update(hashNew, hashOld))
	        {
	        	String sql="";
	        	Row row = new Row();
	
	        	String sIdPerfil=form.getPerfil();
	        	String sIdInstitucion=userBean.getLocation();;
	
	            Hashtable htRoles = new Hashtable();
	            htRoles.put(AdmPerfilRolBean.C_IDPERFIL, sIdPerfil);
	            htRoles.put(AdmPerfilRolBean.C_IDINSTITUCION, sIdInstitucion);
	            
	            row.load(htRoles);

        	    String misRoles = form.getRoles();
        	    String misRolesAntiguos = form.getRolesAntiguos();
        	    
        	    if (misRoles!=null)
        	    {
        	        StringTokenizer st = new StringTokenizer(misRoles, ",");
        	        
        	        while (st.hasMoreTokens())
        	        {
        	            boolean bProcesar=true;
        	            
        	            String rol = st.nextToken();
        	            
        	            StringTokenizer st2 = new StringTokenizer(misRolesAntiguos, ",");
        	            
        	            while (st2.hasMoreTokens())
        	            {
        	                String rolAux = st2.nextToken();
        	                
        	                if (rol.equals(rolAux))
        	                {
        	                    bProcesar=false;        	                    
        	                }
        	            }
        	              
        	            if (bProcesar)
        	            {
	        	            Object[] fields = new Object[] {};
	        	            Vector vFields = new Vector();
	  
	        	            row = new Row();
	  
	        	            row.setValue(AdmPerfilRolBean.C_IDINSTITUCION, sIdInstitucion);
	        	            row.setValue(AdmPerfilRolBean.C_IDPERFIL, sIdPerfil);
	        	            row.setValue(AdmPerfilRolBean.C_IDROL, rol);
	        	            row.setValue(AdmPerfilRolBean.C_GRUPO_POR_DEFECTO, "N");
	        	            row.setValue(AdmPerfilRolBean.C_USUMODIFICACION, userBean.getUserName());
	        	            row.setValue(AdmPerfilRolBean.C_FECHAMODIFICACION, "SYSDATE");
	
	        	            vFields.add(AdmPerfilRolBean.C_IDINSTITUCION);
	        	            vFields.add(AdmPerfilRolBean.C_IDPERFIL);
	        	            vFields.add(AdmPerfilRolBean.C_IDROL);
	        	            vFields.add(AdmPerfilRolBean.C_GRUPO_POR_DEFECTO);
	        	            vFields.add(AdmPerfilRolBean.C_USUMODIFICACION);
	        	            vFields.add(AdmPerfilRolBean.C_FECHAMODIFICACION);
	
	        	            fields = vFields.toArray();
	        	            row.add(AdmPerfilRolBean.T_NOMBRETABLA, fields);
        	            }
        	        }
        	    }

        	    if (misRolesAntiguos!=null)
        	    {
        	        String rolesBorrar="";
        	        StringTokenizer st = new StringTokenizer(misRolesAntiguos, ",");
        	        AdmPerfilRolBean bean=new AdmPerfilRolBean();
        	        AdmPerfilRolAdm adm=new AdmPerfilRolAdm(this.getUserBean(request));
        	               	        
        	        
        	        while (st.hasMoreTokens())
        	        {
        	            boolean bProcesar=true;
        	            
        	            String rol = st.nextToken();
        	            
        	            StringTokenizer st2 = new StringTokenizer(misRoles, ",");
        	            
        	            while (st2.hasMoreTokens())
        	            {
        	                String rolAux = st2.nextToken();
        	                
        	                if (rol.equals(rolAux))
        	                {
        	                    bProcesar=false;        	                    
        	                }
        	            }
        	              
        	            if (bProcesar)
        	            {
        	                bean.setIdPerfil(sIdPerfil);
        	                bean.setIdInstitucion(sIdInstitucion);
        	                bean.setIdRol(rol);

        	                adm.delete(bean);
        	            }
        	        }
        	    }

        	    tx.commit();

        	    request.setAttribute("mensaje","messages.updated.success");
        	    
                String mensaje = "El Grupo con ID=\"" + sIdPerfil + "\"" + " ha sido modificado";	

                CLSAdminLog.escribirLogAdmin(request,mensaje);
                
                request.removeAttribute("DATABACKUP");
	        }
        	
	        else
	        {
	            try
	            {
	                tx.rollback();
	            }
	            
	            catch(Exception e)
	            {
	                throw new ClsExceptions(e, "Error al abortar la transacción");
	            }
	            
	            request.setAttribute("mensaje","messages.updated.error");
	        }
    	}

    	catch(Exception e)
    	{
    	    throw new ClsExceptions(e, "Error al completar la transacción");
    	}
        
        request.setAttribute("modal","1");
	    
        return "exito";
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAListadoGruposUsuarioForm form = (SIGAListadoGruposUsuarioForm)formulario;
	    
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    AdmPerfilAdm gruposUsuarioAdm = new AdmPerfilAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    Hashtable hash = new Hashtable();
	    
	    hash.put(AdmPerfilBean.C_IDPERFIL, (String)vOcultos.elementAt(0));
	    hash.put(AdmPerfilBean.C_IDINSTITUCION, userBean.getLocation());
	    
	    if (gruposUsuarioAdm.delete(hash))
	    {
	        request.setAttribute("mensaje","messages.deleted.success");
	        
            String mensaje = "El Grupo \"" + (String)vOcultos.elementAt(1) + "\"" + " ha sido borrado";	

            CLSAdminLog.escribirLogAdmin(request,mensaje);
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","error.messages.deleted");
	    }

	    request.setAttribute("urlAction", "/SIGA/ADM_GestionarGruposUsuario.do?modo=buscar");
        request.setAttribute("hiddenFrame", "1");
	    
	    return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
        SIGAListadoGruposUsuarioForm form = (SIGAListadoGruposUsuarioForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        AdmPerfilBean gruposUsuarioBean = new AdmPerfilBean();
        AdmRolAdm admRol = new AdmRolAdm(this.getUserBean(request));

        Vector datosRoles = admRol.select();
        Vector datos = new Vector();

        String idPerfil = "";
        String descripcion = "";
        String nivelPerfil = "0";
        String fechaMod = "";
        String usuMod = "-1";
        String idInstitucion = "";
        String roles = "";

        if (!bNuevo)
        {
			Vector vVisibles = form.getDatosTablaVisibles(0);
			Vector vOcultos = form.getDatosTablaOcultos(0);		
	
	        idPerfil = ((String)vOcultos.elementAt(0)).trim();
	        descripcion = ((String)vOcultos.elementAt(1)).trim();
	        nivelPerfil = ((String)vOcultos.elementAt(2)).trim();
	        fechaMod = ((String)vOcultos.elementAt(3)).trim();
	        usuMod = ((String)vOcultos.elementAt(4)).trim();
	        idInstitucion = ((String)vOcultos.elementAt(5)).trim();
	        roles = ((String)vOcultos.elementAt(6)).trim();
        }

        gruposUsuarioBean.setIdPerfil(idPerfil);
        gruposUsuarioBean.setDescripcion(descripcion);
        gruposUsuarioBean.setNivelPerfil(new Integer(nivelPerfil));
        gruposUsuarioBean.setFechaMod(fechaMod);
        gruposUsuarioBean.setUsuMod(new Integer(usuMod));
        gruposUsuarioBean.setIdInstitucion(idInstitucion);
        gruposUsuarioBean.setRoles(roles);
        
        datos.add(gruposUsuarioBean);
        datos.add(datosRoles);
        
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(AdmPerfilBean.C_IDPERFIL, idPerfil);
            hashBackUp.put(AdmPerfilBean.C_DESCRIPCION, descripcion);
            hashBackUp.put(AdmPerfilBean.C_NIVELPERFIL, nivelPerfil);
            hashBackUp.put(AdmPerfilBean.C_FECHAMODIFICACION, fechaMod);
            hashBackUp.put(AdmPerfilBean.C_USUMODIFICACION, usuMod);
            hashBackUp.put(AdmPerfilBean.C_IDINSTITUCION, idInstitucion);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}