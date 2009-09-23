/*
 * @author RGG
 *
 */
package com.siga.expedientes.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.expedientes.form.PestanaConfigurableForm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * 
 * Clase action para el mantenimiento de campos de expediente.<br/>
 * Gestiona la edicion, borrado y creación de los campos. 
 *
 */
public class PestanaConfigurableAction2 extends PestanaConfigurableAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        UsrBean userBean=(UsrBean)ses.getAttribute("USRBEAN");
        
        PestanaConfigurableForm form = (PestanaConfigurableForm)formulario;
        form.setIdPestanaConf("2");
        return super.abrir(mapping, formulario, request, response);
	}
    
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    HttpSession ses=request.getSession();
            
        PestanaConfigurableForm form = (PestanaConfigurableForm)formulario;
        form.setIdPestanaConf("2");
        return super.modificar(mapping, formulario, request, response);

	}
}
