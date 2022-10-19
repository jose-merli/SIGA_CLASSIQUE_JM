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
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.pra.core.filters.security.helper.UsuariosTO;
import com.siga.Utilidades.UtilidadesString;

public class SIGAInfoDirectorioAuthAction extends Action {

	private static String ID_INSTITUCION_PERMITIDA = "2000";
	
	public SIGAInfoDirectorioAuthAction() {
		super();
	}
	
	public final ActionForward execute(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response){
		String 			result	="success";
		HttpSession 	ses		= request.getSession();
		SIGACGAEContextCAS cont 	= new SIGACGAEContextCAS();
		UsrBean 		bean	= null;


		boolean accesoAdmin=true;
		final String ENTORNO_DESARROLLO ="DESARROLLO";
		ReadProperties rproperties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		boolean desarrollo = rproperties.returnProperty("administracion.login.entorno").equalsIgnoreCase(ENTORNO_DESARROLLO);
		if(!desarrollo){
			String roles=(String)request.getHeader("CAS-roles");
			accesoAdmin=roles.contains("SIGA-Admin");
		}
		if (!accesoAdmin) {
			ClsLogging.writeFileLog("Acceso denegado: UsrBean nulo", 3);
			ClsLogging.writeFileLog("Acceso denegado:!UsrBean nulo 2");
			return mapping.findForward("accesodenegado");
		}
		
		
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
