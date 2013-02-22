package com.siga.general;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class SIGAInfoDirectorioAuthAction extends Action {

	private static String ID_INSTITUCION_PERMITIDA = "2000";
	
	public SIGAInfoDirectorioAuthAction() {
		super();
	}
	
	public final ActionForward execute(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response){
		String 			result	="success";
		HttpSession 	ses		= request.getSession();
		SIGACGAEContext cont 	= new SIGACGAEContext();
		UsrBean 		bean	= null;

		try {
			bean=cont.rellenaContexto(request, getServlet());
		} catch (SIGAException e) {
			ClsLogging.writeFileLogError("Acceso denegado: Error en rellenaContexto: "+e.getLiteral("1"), e, 3);
			request.setAttribute("mensaje",e.getLiteral());
			return mapping.findForward("accesodenegado");
		}
		
		if (bean==null || !ID_INSTITUCION_PERMITIDA.equals(bean.getLocation())) {
			ClsLogging.writeFileLog("Acceso denegado: UsrBean nulo", 3);
			return mapping.findForward("accesodenegado");
		}
		
		ses.setAttribute("USRBEAN", bean);
		cont.initStyles(bean.getLocation(), ses);
		
		try {
			String IPServidor = UtilidadesString.obtenerIPServidor(request); 
			request.getSession().setAttribute("IPSERVIDOR",IPServidor);
			ClsLogging.writeFileLog("IP DEL SERVIDOR="+IPServidor,3);
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al obtener la IP "+ e.toString(), e, 3);
		}
		
		String idsession = (String)ses.getId();
		long fcses = ses.getCreationTime();
        if (idsession != null && idsession.length() > 0) {
        	SimpleDateFormat fm = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
        	String fecha = fm.format(new Date(fcses));
        	String idSesion = idsession.substring(0, 5)+fecha;
        	MDC.put("idSesion", idSesion);
        }
        
        return mapping.findForward(result);
	}

}
