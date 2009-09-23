/*
 * Created on 23F, 2009
 * @author jose.barrientos
 */
package com.siga.facturacionSJCS.action;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MantenimientoPagosSCJSAction extends MasterAction {

	/** 
	 * 
	 */
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		
		request.setAttribute("accion","nuevo");
		Hashtable htParametros=new Hashtable();
		htParametros.put("idInstitucion",userBean.getLocation());

		
		request.setAttribute("datos", htParametros);
		return "inicio";
	}
}
