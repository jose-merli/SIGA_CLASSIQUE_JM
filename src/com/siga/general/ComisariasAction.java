package com.siga.general;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.siga.beans.ScsComisariaAdm;

public class ComisariasAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {				    		
			String modo = (String) request.getParameter("modo");

			if (modo!=null && modo.equalsIgnoreCase("getAjaxComisaria")){
				getAjaxComisaria (request, response);
				
			} else if (modo!=null && modo.equalsIgnoreCase("getAjaxComisaria2")){
				getAjaxComisaria2 (request, response);

			} else if (modo!=null && modo.equalsIgnoreCase("getAjaxComisaria3")){
				getAjaxComisaria3 (request, response);
			}
	
		} catch (SIGAException es) {
			throw es;
			
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void getAjaxComisaria (HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
		String codigoExt = "";
		
		String idCombo = request.getParameter("idCombo");
		
		String sql = "SELECT C.CODIGOEXT FROM SCS_COMISARIA C " + 
			" WHERE UPPER(C.IDCOMISARIA||','||C.IDINSTITUCION) = UPPER('"+idCombo+"')";
			
		ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));		
		Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
		
		if (resultadoComisaria!=null && resultadoComisaria.size()>0)
			codigoExt =  (String)((Hashtable)resultadoComisaria.get(0)).get("CODIGOEXT");
				
		JSONObject json = new JSONObject();		
		json.put("codigoExt", codigoExt);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 			
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxComisaria2 (HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
		ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));		
		String idCombo = request.getParameter("idCombo");
		String codigoExt = "";
		JSONObject json = new JSONObject();
		
		String sql = "SELECT SC.CODIGOEXT "+
				" FROM SCS_COMISARIA SC "+
				" WHERE SC.IDCOMISARIA = '"+idCombo+"' "+
				" AND (SC.IDINSTITUCION="+this.getUserBean(request).getLocation() + " OR SC.IDINSTITUCION=2000)";;
		Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
		
		if (resultadoComisaria!=null && resultadoComisaria.size()>0)
			codigoExt = (String)((Hashtable)resultadoComisaria.get(0)).get("CODIGOEXT");
							
		json.put("codigoExt", codigoExt);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 			
	}		
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxComisaria3 (HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, Exception {
		ScsComisariaAdm comisariaAdm= new ScsComisariaAdm(this.getUserBean(request));		
		String idCodigo = request.getParameter("codigo");
		String idComisaria = "";
		JSONObject json = new JSONObject();
		
		String sql = "SELECT SC.IDCOMISARIA "+
				" FROM SCS_COMISARIA SC "+
				" WHERE UPPER(SC.CODIGOEXT)=UPPER('"+idCodigo+"') "+
				" AND (SC.IDINSTITUCION="+this.getUserBean(request).getLocation() + " OR SC.IDINSTITUCION=2000)";;
		Vector resultadoComisaria = comisariaAdm.selectGenerico(sql);
		
		if (resultadoComisaria!=null && resultadoComisaria.size()>0)
			idComisaria = (String)((Hashtable)resultadoComisaria.get(0)).get("IDCOMISARIA");
							
		json.put("idComisaria", idComisaria);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 			
	}	
}