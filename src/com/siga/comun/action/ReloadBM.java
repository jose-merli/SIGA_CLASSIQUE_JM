package com.siga.comun.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.SIGAReferences;

import es.satec.businessManager.BusinessManager;

/**
 * Este action recarga la configuracion del BussinesManager
 */
public class ReloadBM extends BaseAction{

	
	public ActionForward executeInternal(ActionMapping mapping, ActionForm formulario,
		      HttpServletRequest request, HttpServletResponse response) {
		try {
			setBusinessManager(BusinessManager.getInstance(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.ATOS_BUSINESS_CONFIG)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward("");
	}	

}
