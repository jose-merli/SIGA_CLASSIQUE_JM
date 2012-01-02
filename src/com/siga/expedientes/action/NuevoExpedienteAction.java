/*
 * Created on Feb 2, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.siga.expedientes.form.NuevoExpedienteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para seleccionar el tipo de un nuevo expediente
 */
public class NuevoExpedienteAction extends MasterAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		request.getSession().setAttribute("copiaSession", "");	
	    return "avanzada";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
		try{
		    NuevoExpedienteForm form = (NuevoExpedienteForm)formulario;
		    
		    Hashtable datosTipoExpediente = new Hashtable();
			request.getSession().setAttribute("copiaSession", "");	

			datosTipoExpediente.put("idInstitucion_TipoExpediente",form.getIdInstitucion());
			datosTipoExpediente.put("idTipoExpediente",form.getIdTipoExpediente());
			datosTipoExpediente.put("modal",form.getModal());

			request.setAttribute("datosTipoExpediente", datosTipoExpediente);
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

	    return "seleccion";
	}
	
}
