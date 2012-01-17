/*
 * @author RGG
 *
 */
package com.siga.expedientes.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpCampoConfAdm;
import com.siga.beans.ExpCampoConfBean;
import com.siga.expedientes.form.CamposConfigurablesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de campos de expediente.<br/>
 * Gestiona la edicion, borrado y creación de los campos. 
 *
 */
public class CamposConfigurablesAction extends MasterAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        ses.removeAttribute("DATABACKUP_CAMPOCONF");
        UsrBean userBean=(UsrBean)ses.getAttribute("USRBEAN");
        
        CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
	    if (form.getAccion().equals("ver")) {
	        userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
	    }
	    
	    ExpCampoConfAdm admCampo = new ExpCampoConfAdm(this.getUserBean(request));
	    Vector campos = admCampo.getCamposPlantilla(userBean.getLocation(),form.getIdTipoExpediente(),form.getIdCampo(),form.getIdPestanaConf());
	    request.setAttribute("datos",campos);
        
        return "abrir";
	}
    
    protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        ses.removeAttribute("DATABACKUP_CAMPOCONF");
        UsrBean userBean=(UsrBean)ses.getAttribute("USRBEAN");
        CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
        ExpCampoConfAdm adm = new ExpCampoConfAdm(this.getUserBean(request));
        form.setAccion("modificar");
        
        String institucion = userBean.getLocation();        
        
		Vector vOcultos = form.getDatosTablaOcultos(0);
		String idCampo = (String)vOcultos.get(0);

		ExpCampoConfBean bean = adm.getCampoConfigurable(institucion,form.getIdTipoExpediente(),form.getIdCampo(),form.getIdPestanaConf(),idCampo);
        if (bean==null) {
            throw new ClsExceptions("No se encuentra el elemento campo configurable.");
        } else {
            form.setNombre(bean.getNombre());
            form.setOrden(bean.getOrden().toString());
            form.setSeleccionado(bean.getSeleccionado().toString());
            form.setIdCampoConf(idCampo);
            form.setGeneral(bean.getGeneral().toString());
        }
        
        
        return "editar";
	}
    
    protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        ses.removeAttribute("DATABACKUP_CAMPOCONF");
        CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
        form.setAccion("insertar");
        
        return "editar";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    ExpCampoConfAdm adm = new ExpCampoConfAdm(this.getUserBean(request));
            
	    CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
                
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();     
        
	        ExpCampoConfBean bean = adm.getCampoConfigurable(userBean.getLocation() ,form.getIdTipoExpediente(),form.getIdCampo(),form.getIdPestanaConf(),form.getIdCampoConf());
	        if (bean==null) {
	            throw new ClsExceptions("No se encuentra el elemento campo configurable.");
	        } else {
	            bean.setNombre(form.getNombre());
	            bean.setSeleccionado((form.getSeleccionado()!=null && form.getSeleccionado().equals("1"))?new Integer(1):new Integer(0));
	            bean.setOrden(new Integer(form.getOrden()));
	            bean.setGeneral((form.getGeneral()!=null && form.getGeneral().equals("1"))?new Integer(1):new Integer(0));
	            if (adm.ordenRepetido(bean)){
	            	if (tx!=null)
	            		tx.rollback();
	            	return this.exito("messages.expedientes.ordenRepetido.error", request);
	            }
	            if (adm.nombreRepetido(bean)){
	            	if (tx!=null)
	            		tx.rollback();
	            	return this.exito("messages.expedientes.nombreRepetido.error", request);
	            }
	            if (!adm.updateDirect(bean)) {
	                throw new ClsExceptions("Error al actualizar el campo. "+adm.getError());
	            }
	        }
	        
	        tx.commit();
	               
	    } catch (Exception e) {
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
	    } 
        
 	    return this.exitoModal("messages.updated.success", request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    ExpCampoConfAdm adm = new ExpCampoConfAdm(this.getUserBean(request));
            
	    CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
                
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();     
        
	        Integer idCampoConf = adm.getNuevoId(userBean.getLocation() ,form.getIdTipoExpediente(),form.getIdCampo(),form.getIdPestanaConf());
	        ExpCampoConfBean bean = new ExpCampoConfBean();
	        bean.setIdInstitucion(new Integer(userBean.getLocation()));
	        bean.setIdTipoExpediente(new Integer(form.getIdTipoExpediente()));
	        bean.setIdPestanaConf(new Integer(form.getIdPestanaConf()));
	        bean.setIdCampo(new Integer(form.getIdCampo()));
	        bean.setIdCampoConf(idCampoConf);
	        bean.setNombre(form.getNombre().trim());
            bean.setSeleccionado((form.getSeleccionado()!=null && form.getSeleccionado().equals("1"))?new Integer(1):new Integer(0));
            bean.setOrden(new Integer(form.getOrden()));
            bean.setGeneral((form.getGeneral()!=null && form.getGeneral().equals("1"))?new Integer(1):new Integer(0));
            if (adm.ordenRepetido(bean)){
            	if (tx!=null)
            		tx.rollback();
            	return this.exito("messages.expedientes.ordenRepetido.error", request);
            }
            if (adm.nombreRepetido(bean)){
            	if (tx!=null)
            		tx.rollback();
            	return this.exito("messages.expedientes.nombreRepetido.error", request);
            }
            if (!adm.insert(bean)) {
                throw new ClsExceptions("Error al insertar el campo. "+adm.getError());
            }

            tx.commit();
	               
	    } catch (Exception e) {        
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
	    }      
        
 	    return this.exitoModal("messages.inserted.success", request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    ExpCampoConfAdm adm = new ExpCampoConfAdm(this.getUserBean(request));
           
	    CamposConfigurablesForm form = (CamposConfigurablesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
         
        Vector vOcultos = form.getDatosTablaOcultos(0);
		String idCampo = (String)vOcultos.get(0);
		
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();     
        
	        ExpCampoConfBean bean = adm.getCampoConfigurable(userBean.getLocation() ,form.getIdTipoExpediente(),form.getIdCampo(),form.getIdPestanaConf(),idCampo);
	        if (bean==null) {
	            throw new ClsExceptions("No se encuentra el elemento campo configurable.");
	        } else {
	            if (!adm.delete(bean)) {
	                throw new ClsExceptions("Error al eliminar el campo. "+adm.getError());
	            }
	        }
	        tx.commit();
	               
	    } catch (Exception e) {        
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
	    }      
        
 	    return this.exitoRefresco("messages.deleted.success", request);
	}
	
}

