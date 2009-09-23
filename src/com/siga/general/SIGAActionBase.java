/*
 * Created on 01-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionServlet;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

/**
 * @author esdras.martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SIGAActionBase extends Action { 

	/**
	 * 
	 */
	public SIGAActionBase() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 *  Funcion que recupera el userbean
	 *  @param HttpServletRequest
	 *  @return UsrBean
	 */
	protected UsrBean getUserBean(HttpServletRequest request) {
		return (UsrBean)(request.getSession().getAttribute(ClsConstants.USERBEAN));
	}

	/** Funcion testSession
	 *  Comprueba que el usuario ha firmado correctamente en la aplicación. En caso contrario, 
	 * lanza una excepción informando de la incidencia  
	 *  @param HttpServletRequest
	 *  @param HttpServletResponse
	 *  @exception ClsException 
	 * */
	protected final void testSession(HttpServletRequest request, 
		      HttpServletResponse response, ActionServlet config) throws ClsExceptions {
		try {
			if (getUserBean(request)==null) {
				ClsExceptions e=new ClsExceptions("Usuario no válido. Es necesario firmar antes de utilizar la Aplicación (SIGA)");
				e.setErrorCode("USERNOVALID");
				throw e;
			}
		} catch (ClsExceptions e) {
			throw e;
		} catch (Exception el) {
			ClsExceptions e=new ClsExceptions(el,"Usuario no válido. Es necesario firmar antes de utilizar la Aplicación (SIGA)");
			e.setErrorCode("USERNOVALID");
			throw e;
		}
	}

}
