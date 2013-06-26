

package com.siga.gratuita.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.MasterAction;
import com.siga.general.SIGAException;



public class SolicitudEJGAction extends MasterAction 
{

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response)throws SIGAException {
		
		String mapDestino = "exception";
		
		try {
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
	 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
	 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
			  String[] pestanasOcultas=new String [1];
			  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_EJG;
			  request.setAttribute("pestanasOcultas",pestanasOcultas);
	 		}
			
	 		
	 		Map mapSolicitud = new HashMap();
	 		Enumeration enumParametros=request.getParameterNames();
	 		while (enumParametros.hasMoreElements()) {
				String parametro = (String) enumParametros.nextElement();
				mapSolicitud.put(parametro, request.getParameter(parametro));
			}
	 		
			
	 		
	 		request.setAttribute("SOLEJG",mapSolicitud);
	 		
	 		
	 		
	 		
	 		mapDestino ="inicio"; 
			
						
			
		}
		 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
		return mapping.findForward(mapDestino);
	}
	
	
	
	
	
	
	
}