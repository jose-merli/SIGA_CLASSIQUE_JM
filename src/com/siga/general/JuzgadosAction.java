package com.siga.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.autogen.model.ScsTipofundamentos;
import org.redabogacia.sigaservices.app.services.scs.ScsTipoFundamentosService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsJuzgadoProcedimientoAdm;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.gratuita.form.MaestroDesignasForm;

import es.satec.businessManager.BusinessManager;

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
			} else if (modo!=null && modo.equalsIgnoreCase("getAjaxTiposFundamento")){
				getAjaxTiposFundamento (request, response);
			}  else if (modo!=null && modo.equalsIgnoreCase("getAjaxModulos")){
				getAjaxModulos(request, response) ;
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
	
	protected void getAjaxTiposFundamento (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idCombo = request.getParameter("idCombo");		
		BusinessManager bm = getBusinessManager();
		UsrBean usrBean = this.getUserBean(request);
		ScsTipoFundamentosService tipoFundamentosService = (ScsTipoFundamentosService)bm.getService(ScsTipoFundamentosService.class);
		String[] tiposResolucion =  idCombo.split(",");
		

		
		List<ScsTipofundamentos> tiposFundamentos = new ArrayList<ScsTipofundamentos>();
		for (int i = 0; i < tiposResolucion.length; i++) {
			List<ScsTipofundamentos> tiposFundamentosResolucion = (ArrayList<ScsTipofundamentos>) tipoFundamentosService.getTiposFundamento(Integer.parseInt(usrBean.getLanguage()),new  Short(tiposResolucion[i]),new Integer(usrBean.getLocation()));
			 tiposFundamentos.addAll(tiposFundamentosResolucion);
		}
		//Esto de abajo seria lo optimo pero no tengo tiempo para ver porque da error cuando hay mas de un registro(me puedo imaginar que la capa de mybatis trate el dato como un short y cumo se le pasauna cadena...)
//		List<ScsTipofundamentos> tiposFundamentos = (ArrayList<ScsTipofundamentos>) tipoFundamentosService.getTiposFundamentoResolucionesMultiples(Integer.parseInt(usrBean.getLanguage()),idCombo,new Integer(usrBean.getLocation()));
		
		
		JSONArray jsonArray = new JSONArray();
		for (ScsTipofundamentos tipoFundamento : tiposFundamentos) {
			JSONObject json = new JSONObject();	
			json.put("idFundamento",tipoFundamento.getIdfundamento().toString());
			json.put("descripcion", tipoFundamento.getDescripcion());
			jsonArray.put(json);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fundamentos", jsonArray);
		
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", jsonObject.toString());
		response.getWriter().write(jsonObject.toString()); 		
	}
	protected void getAjaxModulos (
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		UsrBean usr = this.getUserBean(request);
		//Recogemos el parametro enviado por ajax
		
		String idJuzgado = "-1";
		if(request.getParameter("idJuzgado")!=null && !request.getParameter("idJuzgado").equals("")){
			idJuzgado = request.getParameter("idJuzgado");
		}
		
		String idProcedimiento = "-1";
		if(request.getParameter("procedimiento")!=null && !request.getParameter("procedimiento").equals("")){
			idProcedimiento = request.getParameter("procedimiento");
		}
		String fecha = "SYSDATE";
		
		//Comprobamos el valor del parametro 
		GenParametrosAdm adm = new GenParametrosAdm (usr);
		String filtrarModulos = adm.getValor((String)usr.getLocation(),"SCS","FILTRAR_MODULOS_PORFECHA_DESIGNACION", "");
		if(filtrarModulos.equalsIgnoreCase("S")){
			fecha = "'" + request.getParameter("fecha") + "'";
		}
		
		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsProcedimientosBean> modulosList = null;
		if(idJuzgado!= null && !idJuzgado.equals("-1")&& !idJuzgado.equals("")){
			ScsJuzgadoProcedimientoAdm admModulos = new ScsJuzgadoProcedimientoAdm(usr);
			modulosList = admModulos.getModulos(new Integer(idJuzgado),new Integer(idProcedimiento),new Integer(usr.getLocation()),true, fecha);
		}
		if(modulosList==null){
			modulosList = new ArrayList<ScsProcedimientosBean>();
			
		}
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsProcedimientosBean>(), modulosList,response);
		
	}
	
}