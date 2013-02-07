package com.siga.general;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;

public class JuzgadosAction extends MasterAction{
	
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

			if (modo!=null && modo.equalsIgnoreCase("getAjaxJuzgado2")){
				getAjaxJuzgado2 (request, response);
				
			} else if (modo!=null && modo.equalsIgnoreCase("getAjaxJuzgado3")){
				getAjaxJuzgado3 (request, response);

			} else if (modo!=null && modo.equalsIgnoreCase("getAjaxJuzgado4")){
				getAjaxJuzgado4 (request, response);
			}
	
		} catch (SIGAException es) {
			throw es;
			
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
		return null;
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
	protected void getAjaxJuzgado2 (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idCombo = request.getParameter("idCombo");		
		ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
		String codigoExt2 ="";
		JSONObject json = new JSONObject();	
		
		String where = " WHERE UPPER(IDJUZGADO||','||IDINSTITUCION)=UPPER('"+idCombo+"') ";
		Vector resultadoJuzgado = juzgadoAdm.select(where);
		
		if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
			ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			codigoExt2 = juzgadoBean.getCodigoExt2();
		}						
		json.put("codigoExt2", codigoExt2);
		
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
	protected void getAjaxJuzgado3 (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idCombo = request.getParameter("idCombo");
		ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
		String codigoExt2 ="";
		JSONObject json = new JSONObject();
		
		String where = " WHERE IDJUZGADO='"+idCombo+"' "+
				" AND (IDINSTITUCION="+this.getUserBean(request).getLocation() + " OR IDINSTITUCION=2000) ";
		Vector resultadoJuzgado = juzgadoAdm.select(where);
		
		if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
			ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			codigoExt2 = juzgadoBean.getCodigoExt2();
		}
		json.put("codigoExt2", codigoExt2);
		
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
	protected void getAjaxJuzgado4 (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codigoExt2 = request.getParameter("codigo");
		ScsJuzgadoAdm juzgadoAdm= new ScsJuzgadoAdm(this.getUserBean(request));
		String idJuzgado="";
		JSONObject json = new JSONObject();	
		
		
		String where = " WHERE UPPER(CODIGOEXT2)=UPPER('"+codigoExt2+"') " +
				" AND (IDINSTITUCION="+this.getUserBean(request).getLocation() + " OR IDINSTITUCION=2000) ";
		Vector resultadoJuzgado = juzgadoAdm.select(where);
		
		if (resultadoJuzgado!=null && resultadoJuzgado.size()>0) {
			ScsJuzgadoBean juzgadoBean = (ScsJuzgadoBean) resultadoJuzgado.get(0);
			idJuzgado = juzgadoBean.getIdJuzgado().toString();
		}
		json.put("idJuzgado", idJuzgado);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}
}