package com.siga.certificados.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGAComunicacionInterProfAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{

		try{
			UsrBean user = this.getUserBean(request);

			GenParametrosAdm parametrosAdm = new GenParametrosAdm(user);
			String url = parametrosAdm.getValor("2000", ClsConstants.MODULO_CERTIFICADOS, "URL_COMUNICACION_INTER_PROF", "");
			request.setAttribute("URL", url);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e, null); 
		}
		return "abrir";
	}



}