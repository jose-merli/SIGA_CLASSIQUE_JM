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
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpRolesAdm;
import com.siga.beans.ExpRolesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
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
        
        
        String where = " WHERE ";
        
        where += ExpRolesBean.C_IDINSTITUCION + " = " + userBean.getLocation() +" AND "+ExpRolesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
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
	    UserTransaction tx = null;
	    
	    try {
		    //Rellenamos el nuevo Bean	    
		    ExpRolesBean rol = new ExpRolesBean();	    
		    
		    tx = userBean.getTransaction();
			tx.begin();

		    
		    rol.setIdInstitucion(Integer.valueOf(form.getIdInstitucion()));
		    rol.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    rol.setIdRol(rolesAdm.getNewIdRol(form.getIdTipoExpediente(),userBean));
		    String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+rol.getIdRol());
		    rol.setNombre((idRecurso!=null)?""+idRecurso:form.getNombre());

			//Ahora procedemos a insertarlo
		    if (!rolesAdm.insert(rol)) {
		    	throw new ClsExceptions("Error al insertar rol. "+rolesAdm.getError());
		    }
		    
		    ///////////////////////////////////////////
        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
    		if (idRecurso != null) {
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+rol.getIdRol());
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(ExpRolesBean.C_NOMBRE);
	        	recCatalogoBean.setDescripcion(form.getNombre());
	        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(ExpRolesBean.T_NOMBRETABLA);
	        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) { 
	        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
	        	}
        	}
        	///////////////////////////////////////////
		    
		    tx.commit();
	        
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
        }

	    /*request.setAttribute("descOperation","messages.inserted.success");
	    request.setAttribute("modal","1");
	    return "refresh";*/
	    return exitoModal("messages.inserted.success",request);
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
        UserTransaction tx = null;
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        Hashtable hashNew = (Hashtable)hashOld.clone();

	    try {
        
	        Hashtable codigos = new Hashtable();
	        codigos.put(new Integer(1),form.getIdInstitucion());
	        codigos.put(new Integer(2),form.getIdTipoExpediente());
	        codigos.put(new Integer(3),form.getIdRol());
	        String sSQL = "SELECT NOMBRE FROM EXP_ROLPARTE WHERE IDINSTITUCION=:1 AND IDTIPOEXPEDIENTE=:2 AND IDROL=:3";
	        
		    request.removeAttribute("DATABACKUP");
		    RowsContainer rc = new RowsContainer();
	    
	    	tx = userBean.getTransaction();
        	tx.begin();
        	if (rc.findForUpdateBind(sSQL,codigos)) {

		        Row row = (Row)rc.get(0);
		        row.setCompareData(row.getRow());
		        
		        String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+form.getIdRol());
    			if (idRecurso == null) {
    				hashNew.put(ExpRolesBean.C_NOMBRE, form.getNombre());
    			} else {
    				hashNew.put(ExpRolesBean.C_NOMBRE, idRecurso);
    			}

    			if (!rolesAdm.updateDirect(hashNew, rolesAdm.getClavesBean(), rolesAdm.getCamposBean())) {
		        	throw new ClsExceptions("Error al actualizar el RolParte. "+rolesAdm.getError());
		        }
		        
	        	///////////////////////////////////////////
	        	// Multiidioma: Actualizamos los recursos en gen_recursos_catalogos
    			// Long idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
    			if (idRecurso != null) {
	    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(form.getIdInstitucion()),  form.getIdTipoExpediente()+"_"+form.getIdRol());
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setDescripcion(form.getNombre());
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	if(!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) { 
		        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
		        	}
    			}
	        	///////////////////////////////////////////
		        
        	}
		    else {
		    	throw new ClsExceptions("No se encuentra el elemento a modificar");	    	
		    }

	    	tx.commit();

	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
        }
	    
	    return exitoModal("messages.updated.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		RolesForm form = (RolesForm)formulario;
		UserTransaction tx = null;
		try {
			
		    form.setModal("false");
		    
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
		    tx = userBean.getTransaction();
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
		    	    
		    hash.put(ExpRolesBean.C_IDINSTITUCION, userBean.getLocation());
		    hash.put(ExpRolesBean.C_IDROL, (String)vOcultos.elementAt(0));	    
		    hash.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,form.getIdTipoExpediente());
		    
		    tx.begin();
		    
		    if (!rolesAdm.delete(hash)){
	        	throw new SIGAException("messages.elementoenuso.error");
	        	//throw new ClsExceptions("mensaje: "+estadosAdm.getError());
	        }

		    ///////////////////////////////////////////
        	// Multiidioma: Borramos los recursos en gen_recursos_catalogos
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(userBean.getLocation()),  form.getIdTipoExpediente()+"_"+(String)vOcultos.elementAt(0));
			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        		throw new ClsExceptions ("Error al eliminar recursos catalogos. "+admRecCatalogos.getError());
	        	}
			}
        	///////////////////////////////////////////
			
			tx.commit();
			
	    } catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,tx);
	    }

        request.setAttribute("descOperation","messages.deleted.success");
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

        String where = " WHERE ";
        
        where += ExpRolesBean.C_IDROL + " = '" + idrol + "' AND ";
        where += ExpRolesBean.C_IDINSTITUCION + " = '" + userBean.getLocation() + "' AND ";
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
            hashBackUp.put(ExpRolesBean.C_IDINSTITUCION, userBean.getLocation());
            hashBackUp.put(ExpFasesBean.C_IDTIPOEXPEDIENTE, form.getIdTipoExpediente());            
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}
