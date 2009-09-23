/*
 * Created on 17-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.administracion.*;
import com.siga.censo.form.SolicitudIncorporacionAccesoDirectoForm;


/**
 * @author daniel.campos
  */
public class SolicitudIncorporacionAccesoDirectoAction extends Action {
	
	
	/** 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 */
	
	public final ActionForward execute (ActionMapping mapping,   	ActionForm formulario,
							      		HttpServletRequest request, HttpServletResponse response) {
		try {
			
			SolicitudIncorporacionAccesoDirectoForm miform = (SolicitudIncorporacionAccesoDirectoForm)formulario;
			String accion = miform.getModo();
			if (accion != null) {
				if (accion.equalsIgnoreCase("cargaSolicitudIncorporacion")){
					return mapping.findForward("abrir");
				}
			}
			
			// 1. Rellenamos un USRBEAN nuevo, porque no tenemos en este momento
			HttpSession ses = request.getSession();
			
			String location = request.getParameter("idInstitucion");
			
			UsrBean usrbean = UsrBean.UsrBeanAutomatico(location);
			/*
			usrbean.setLocation (location);
			*/
			usrbean.setUserName ("-1");
			usrbean.setUserDescription("NUEVO_USUARIO");
			
			ses.setAttribute("USRBEAN", usrbean);
			ses.setAttribute(Globals.LOCALE_KEY, new java.util.Locale("es", "es"));
			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_TOP);

//			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_LEFT); 
			initStyles(location, ses);
		}
		catch (Exception e) {
			return mapping.findForward("exception");
		}
		return mapping.findForward("inicio");
	}
	
	/**
	 * <p>Este método es temporal, consiste en que a partir de la institución 
	 * que se haya elegido al entrar en la aplicación, se selecciona una hoja de estilos u otra,
	 * así como el logotipo correspondiente.</p>
	 * <p>Más adelante, las hojas de estilos se generarán dinámicamente y el logotipo
	 * se cargará desde base de datos en la tabla de instituciones, por lo que éste
	 * método será innecesario.</p>     
	 * @param location Código de localización
	 * @param ses Objeto de Sesion.
	 */
	private void initStyles(String location, HttpSession ses)
	{
		String iconsPath = "/" + ClsConstants.PATH_DOMAIN + "/" + ClsConstants.RELATIVE_PATH_LOGOS;
		String icon = "logoconsejo2.gif";
		try
		{
			SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz(location);
		
			java.util.Properties stylesheet = interfazGestor.getInterfaceOptions();
			icon = interfazGestor.getLogoImg();
			ses.setAttribute(SIGAConstants.STYLESHEET_REF, stylesheet);
			ses.setAttribute(SIGAConstants.PATH_LOGO, iconsPath+"/"+icon);
		}
		catch(com.atos.utils.ClsExceptions ex)
		{
			com.atos.utils.ClsLogging.writeFileLogError(ex.getMessage(), ex,3);
		}		
	}
}
