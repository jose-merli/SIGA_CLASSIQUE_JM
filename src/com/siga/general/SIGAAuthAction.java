package com.siga.general;

import com.atos.utils.*;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;

public class SIGAAuthAction extends Action
{
	public SIGAAuthAction()
	{
		super();
	}
	
	public final ActionForward execute(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)
	{
		String result="topMenu";

		HttpSession ses= request.getSession();

		SIGACGAEContext cont=null;
		cont=new SIGACGAEContext();
		UsrBean bean=null;

		try
		{
			bean=cont.rellenaContexto(request, getServlet());
		} catch (SIGAException e)
		{
			ClsLogging.writeFileLogError("Acceso denegado: Error en rellenaContexto: "+e.getLiteral("1"), e, 3);
			request.setAttribute("mensaje",e.getLiteral());
			return mapping.findForward("accesodenegado");
		}

		if (bean==null)
		{
			ClsLogging.writeFileLog("Acceso denegado: UsrBean nulo", 3);
			return mapping.findForward("accesodenegado");
		}

		ses.setAttribute("USRBEAN", bean);
		cont.initStyles(bean.getLocation(), ses);
		
		try {
			// RGG 13/01/2007 cambio para obtener IP
			String IPServidor = UtilidadesString.obtenerIPServidor(request); 
			request.getSession().setAttribute("IPSERVIDOR",IPServidor);
			ClsLogging.writeFileLog("IP DEL SERVIDOR="+IPServidor,3);
			
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al obtener la IP "+ e.toString(), e, 3);
		}
		

/*		if(menuPosition.equals("1"))
		{
			result="leftMenu";
			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_LEFT);
		}

		else
		{  */
		    result="topMenu";
			ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_TOP);
//		}

		return mapping.findForward(result);
	}
}