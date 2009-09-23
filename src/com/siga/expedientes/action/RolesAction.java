/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpRolesAdm;
import com.siga.beans.ExpRolesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.expedientes.form.RolesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de Roles de expediente.<br/>
 * Gestiona la edicion, borrado y creación de las Roles. 
 *
 */
public class RolesAction extends MasterAction {
        
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            HttpSession ses=request.getSession();
        	UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        RolesForm form = (RolesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();
        
        String where = " WHERE ";
        
        where += ExpRolesBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpRolesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
        try {
	//      Recupero el bean de Tipo de expediente para mostrar el nombre
	        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
	        Vector tipoExp = tipoExpedienteAdm.select(where);
	        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
	        request.setAttribute("nombreExp", beantipoexp.getNombre());
	        
	        Vector datos = rolesAdm.select(where);        
	        request.setAttribute("datos", datos); 
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
		return "abrir";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response) {
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        RolesForm form = (RolesForm) formulario;
        ExpRolesBean nuevoRol = new ExpRolesBean();
        nuevoRol.setNombre("");        
        nuevoRol.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
        
        Vector datos=new Vector();
        datos.add(nuevoRol);
        request.setAttribute("datos", datos);
        request.setAttribute("editable", "1");

		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    ExpRolesAdm rolesAdm = new ExpRolesAdm(this.getUserBean(request));
	    RolesForm form = (RolesForm) formulario;
	    
	    try {
		    //Rellenamos el nuevo Bean	    
		    ExpRolesBean rol = new ExpRolesBean();	    
		    
		    rol.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    rol.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    rol.setIdRol(rolesAdm.getNewIdRol(form.getIdTipoExpediente(),userBean));
		    rol.setNombre(form.getNombre());
		    
			//Ahora procedemos a insertarlo
		    rolesAdm.insert(rol);
	        request.setAttribute("descOperation","messages.inserted.success");
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }

	    request.setAttribute("modal","1");
	    return "refresh";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    RolesForm form = (RolesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(ExpRolesBean.C_NOMBRE, form.getNombre());
	    
	    request.removeAttribute("DATABACKUP");
	    
	    try {
        rolesAdm.update(hashNew, hashOld);
        request.setAttribute("descOperation","messages.updated.success");
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
	    
	    /*request.setAttribute("modal","1");
	    return "refresh";*/
	    return exitoModal("messages.updated.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		RolesForm form = (RolesForm)formulario;
		try {
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    Hashtable hash = new Hashtable();
	    	    
	    hash.put(ExpRolesBean.C_IDINSTITUCION, userBean.getLocation());
	    hash.put(ExpRolesBean.C_IDROL, (String)vOcultos.elementAt(0));	    
	    hash.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,form.getIdTipoExpediente());
	    
	    if (!rolesAdm.delete(hash)){
        	throw new SIGAException("messages.elementoenuso.error");
        	//throw new ClsExceptions("mensaje: "+estadosAdm.getError());
        }
	          
	        request.setAttribute("descOperation","messages.deleted.success");
	    } catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,null);
	    	//throwExcp("messages.elementoenuso.error",exc,null);

	    }

	    request.setAttribute("urlAction", "/SIGA/EXP_TiposExpedientes_Roles.do");
	    return "refresh";
	}
	
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException
	{
        RolesForm form = (RolesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
        

		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idrol = (String)vOcultos.elementAt(0);
        String institucion = (String)userBean.getLocation();

        String where = " WHERE ";
        
        where += ExpRolesBean.C_IDROL + " = '" + idrol + "' AND ";
        where += ExpRolesBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where += ExpRolesBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "'";
        
        Vector datos = null;
        try {
            datos = rolesAdm.select(where);
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(ExpRolesBean.C_IDROL, idrol);
            hashBackUp.put(ExpRolesBean.C_IDINSTITUCION, institucion);
            hashBackUp.put(ExpFasesBean.C_IDTIPOEXPEDIENTE, form.getIdTipoExpediente());            
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}
