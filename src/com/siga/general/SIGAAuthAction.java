package com.siga.general;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.atos.utils.*;

import javax.servlet.http.*;

import org.apache.log4j.MDC;
import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.ws.action.EdicionColegiadoAction;

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

		SIGACGAEContextCAS cont= new SIGACGAEContextCAS();
		UsrBean bean=null;
		
		try{
			bean = cont.rellenaContexto(request, getServlet());
		} catch (SIGAException e)
		{
			ClsLogging.writeFileLogError("Acceso denegado: Error al autenticar con CAS: "+e.getLiteral("1"), e, 3);
			request.setAttribute("mensaje",e.getLiteral());
			ClsLogging.writeFileLog("Acceso denegado: Error al autenticar con CAS: ");
			return mapping.findForward("accesodenegado");
		}


		if (bean==null)
		{
			ClsLogging.writeFileLog("Acceso denegado: UsrBean nulo", 3);
			ClsLogging.writeFileLog("Acceso denegado:UsrBean nulo ");
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
		
	    result="topMenu";
		ses.setAttribute(SIGAConstants.MENU_POSITION_REF, SIGAConstants.MENU_TOP);

		String idsession = (String)ses.getId();
		long fcses = ses.getCreationTime();
        if (idsession != null && idsession.length() > 0)
        {
        	SimpleDateFormat fm = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
        	String fecha = fm.format(new Date(fcses));
        	String idSesion = idsession.substring(0, 5)+fecha;
            // Put the principal's name into the message diagnostic
            // context. May be shown using %X{username} in the layout
            // pattern.
        	MDC.put("idSesion", idSesion);
            
        }
	        
	        getVersionActualApp(request);
		return mapping.findForward(result);
	}
	
	/**
	 * Obtenemos la versi�n de SIGA. 
	 * A partir de ahora con despliegues desde Jenkins este dato se almacena en ficheros .properties.
	 */
	public static void getVersionActualApp(HttpServletRequest request)
	{
		try {
			
			GenParametrosAdm paramAdm = new GenParametrosAdm((UsrBean)request.getSession().getAttribute(ClsConstants.USERBEAN));

			ResourceBundle rb = ResourceBundle.getBundle("versionSIGA");
			String version = rb.getString("version");
			String proyecto = rb.getString("proyecto");

			String entornoDespliegue = (proyecto == null ? "SIGA" : proyecto) + "_"
					+ paramAdm.getValor("0", "ADM", SIGAConstants.PARAMETRO_ENTORNO, "");

			version = (version == null ? (entornoDespliegue) : (entornoDespliegue + "_" + version));

			request.setAttribute("versionSiga", (version == null ? "" : version));
		} catch (Exception e) {
			request.setAttribute("versionSiga", "");
			// ClsLogging.writeFileLog("Error al obtener la versi�n de SIGA desplegada.", 1);
		}
	}

}