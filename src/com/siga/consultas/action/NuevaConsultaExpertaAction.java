/*
 * Created on Apr 20, 2005
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.consultas.action;

import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.ConConsultaAdm;
import com.siga.consultas.form.RecuperarConsultasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NuevaConsultaExpertaAction extends MasterAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		
		String tipoConsulta = request.getParameter("tipoConsulta");
		
		if (tipoConsulta==null){
			tipoConsulta=ConConsultaAdm.TIPO_CONSULTA_GEN;
		}
		
		request.setAttribute("accion","nuevo");
		Hashtable htParametros=new Hashtable();
		htParametros.put("idInstitucion",userBean.getLocation());
		htParametros.put("tipoConsulta",tipoConsulta);
	    htParametros.put("editable", "1");
	    htParametros.put("accion", "nuevo");
	   
	    htParametros.put("consultaExperta", "1");
	    
	    
	    
		
		if (request.getParameter("noReset")==null){
			if (request.getSession().getAttribute("DATABACKUP")!=null){
		    	if (request.getSession().getAttribute("DATABACKUP").getClass().getName().equals("java.util.HashMap")){
		    		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    		RecuperarConsultasForm f = (RecuperarConsultasForm)databackup.get("RecuperarConsultasForm");
		    		if (f!=null){
		    			databackup.remove("RecuperarConsultasForm");
		    		}
		    		String buscar = (String)databackup.get("buscar");
		    		if (buscar!=null){
		    			databackup.remove("buscar");
		    		}
		    	}
			}
		}else{
			if (request.getSession().getAttribute("DATABACKUP")!=null){
		    	if (request.getSession().getAttribute("DATABACKUP").getClass().getName().equals("java.util.HashMap")){
		    		HashMap databackup = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    		
		    		String buscar = (String)databackup.get("buscar");
		    		if (buscar!=null){
		    			htParametros.put("buscar","true");
		    		}
		    	}
			}
		}
		
		request.setAttribute("consulta", htParametros);
		return "inicio";
	}
}
