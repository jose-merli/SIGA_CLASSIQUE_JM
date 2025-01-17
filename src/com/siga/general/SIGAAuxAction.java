package com.siga.general;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.AjaxMultipleCollectionXmlBuilder;
import com.siga.administracion.SIGAConstants;
import com.siga.comun.action.SessionForms;
import com.siga.comun.form.AuxForm;
import com.siga.comun.form.BaseForm;

import es.satec.businessManager.BusinessManager;


/**
 * Mantiene funcionalidad comun sobre:
 *  <ul>
 *  <li>comprobacion de permisos</li>
 *  <li>acciones basicas de AJAX</li>
 *  <li>datos del UserBean</li>
 *  </ul>
 */
public abstract class SIGAAuxAction extends SIGAActionBase{


	/** 
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 */
	public final ActionForward execute (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) {

		Date ini = new Date();
		String sali = "";
		UsrBean usrbean=null;
		String aux = "";
		
		try {
			// RGG 03-03-2005 cambio para controlar la sesion
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - INICIO", 3);
			try {
//				ClsLogging.writeFileLog("SIGAAuxAction.execute() - testSession", 3);
				testSession(request,response,this.getServlet());
			} catch (ClsExceptions e) {
				ClsLogging.writeFileLogError("ERROR en respuesta de testSession(): " + e.getMessage(), e, 3);
//				ClsLogging.writeFileLog("USRBEAN nulo", request, 5);
				return mapping.findForward("inicioGlobal");
			}
			
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - testAccess()", 3);
			String access=testAccess(request);
			if (!access.equals(SIGAConstants.ACCESS_READ) && !access.equals(SIGAConstants.ACCESS_FULL)) {
//				ClsLogging.writeFileLog("SIGAAuxAction.execute() - Entra if testAccess()", 3);
//				ClsLogging.writeFileLog("Acceso denegado", request, 3);
				ClsLogging.writeFileLog("Acceso denegado:!ACCESS_READ");
				return mapping.findForward("accesodenegado");
			}
			
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - Obtiene la sesion", 3);
			HttpSession ses = request.getSession();
			
			if (ses.isNew()){
//				ClsLogging.writeFileLog("SIGAAuxAction.execute() - La sesion es nueva", 3);
//				ClsLogging.writeFileLogError("Sesi�n nueva", request, 3);
				return mapping.findForward("inicioGlobal");
			}
			
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - Obtiene el USRBEAN", 3);
			usrbean=(UsrBean)ses.getAttribute("USRBEAN");
			
			if (usrbean==null){
				ClsLogging.writeFileLog("SIGAAuxAction.execute() - El USRBEAN es nulo", 3);
//				ClsLogging.writeFileLog("USRBEAN nulo",request,5);
				return mapping.findForward("inicioGlobal");
			}
			// RGG 03-03-2005 FIN CAMBIO 

		} catch(ClsExceptions e) {
			ClsLogging.writeFileLogError("ERROR - SIGAAuxAction.execute(): " + e.getMessage(), e, 3);
			SIGAException ce = new SIGAException(e);
			ce.prepare(request);
			return mapping.findForward("exception");
		} catch (Exception e) { 
			ClsLogging.writeFileLogError("ERROR - SIGAAuxAction.execute(): " + e.getMessage(), e, 3 );
			SIGAException ce = new SIGAException(e);
			ce.prepare(request);
			return mapping.findForward("exception");
		}
		
		try {
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - Comienza parte de formulario", 3);
			AuxForm miForm = (AuxForm) formulario;
			if (miForm != null) {
				aux = miForm.getAccion() + "/" + miForm.getModo();
				String url =  request.getRequestURL()!=null && !request.getRequestURL().toString().equals("null")?request.getRequestURL().toString():request.getRequestURI();
				sali = " URL:" +url + "?" + aux + " +  usuario:" + usrbean.getUserName()  + " " + usrbean.getUserDescription() + " + idPersona:" + usrbean.getIdPersona() + " +  Transaccion:" + usrbean.getStrutsTrans() + " + sesionID:" +request.getSession().getId();
				ClsLogging.writeFileLog("++++++TRANSACCION++++++++  " + sali,10);
				// RGG 21/09/2007 Muestra la informacion de la session
				// SOLAMENTE CUANDO DEGUB LEVEL DE clog.properties A 11
				informacionSesion(request);
			}

			//comprueba si tiene que actualizar o guardar el formulario en la sesion
			//para poder mostrar el formulario al pulsar un boton "Volver"
			if (isBaseForm(formulario)){
				try {
					SessionForms.check(request, (BaseForm) miForm);
				} catch (ClsExceptions e) {
					ClsLogging.writeFileLogError("ERROR -  - SIGAAuxAction.execute(): " + e.getMessage(), e, 3 );
					throw new SIGAException("messages.general.error",e);
				}
			}
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - Return llamando a executeInternal()", 3);
			return executeInternal(mapping,formulario,request,response);

		} catch (SIGAException se) {
			ClsLogging.writeFileLogError("ERROR - SIGAAuxAction.execute(): " + se.getMessage(), se, 3);

			if (formulario!=null && "TRUE".equalsIgnoreCase(((MasterForm)formulario).getModal()))  {
				request.setAttribute("exceptionTarget", "parent.modal");
			}
			se.prepare(request);
			return mapping.findForward("exception");
			
		} finally {
			Date fin = new Date();
			ClsLogging.writeFileLog("++++++    FIN    ++++++++  TIEMPO:" +new Long((fin.getTime()-ini.getTime())).toString() + " milisegundos. + " + sali,10);
			// Control de transacciones largas
			if ((fin.getTime()-ini.getTime())>3000) {
				Date dat = Calendar.getInstance().getTime();
				SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
				String fecha = sdfLong.format(dat);
				ClsLogging.writeFileLog(fecha + ",==> SIGA: Control de tiempo de transacciones (>3 seg.)," +request.getRequestURL() + "?" + aux +  "," + usrbean.getLocation() + "," + usrbean.getUserName() +","+new Long((fin.getTime()-ini.getTime())).toString(),10);
			}
//			ClsLogging.writeFileLog("SIGAAuxAction.execute() - FIN", 3);
		}
	}

	private boolean isBaseForm(ActionForm formulario) {
//		ClsLogging.writeFileLog("SIGAAuxAction.isBaseForm() - INICIO", 3);
		if (formulario == null)
			return false;
		
		Class<?> superClass = formulario.getClass().getSuperclass();
		while (superClass != AuxForm.class) {
			if (superClass.equals(BaseForm.class))
				return true;
			superClass = superClass.getSuperclass();
		}
//		ClsLogging.writeFileLog("SIGAAuxAction.isBaseForm() - FIN", 3);
		return false;
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	protected abstract ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException;

	private String testAccess(HttpServletRequest request) throws ClsExceptions, SIGAException{
		String uri = request.getRequestURI();
		String proceso=request.getParameter("process");
		return testAccess(uri,proceso,request);
	}
	protected String testAccess(String uri,String proceso,HttpServletRequest request) throws ClsExceptions, SIGAException{
		UsrBean usrbean=(UsrBean)request.getSession().getAttribute(ClsConstants.USERBEAN);
		
		// Si venimos por SolicitarIncorporacionAccesoDirectoAction no comprobamos el tipo de acceso porque 
		// no vamos a entrar a la aplicacion, sola a una parte concreta: SolicitarIncorporacion 
		if (uri != null) {
			if (uri.equals("/SIGA/SIN_SolicitudIncorporacion.do") 	&& 
					usrbean.getUserName().equals("-1")					&&
					usrbean.getUserDescription().equals("NUEVO_USUARIO")) {
				usrbean.setAccessType(SIGAConstants.ACCESS_FULL);
				return SIGAConstants.ACCESS_FULL;
			}
		}
		
		//ClsLogging.writeFileLog("Comprobacion de usrbean: " + uri);
		if (usrbean==null) { 
			ClsExceptions e=new ClsExceptions("Usuario inv�lido. Es necesario firmar antes de utilizar la aplicaci�n");
			e.setErrorCode("USERNOVALID");
			throw e;
		}
		String access=SIGAConstants.ACCESS_DENY;
		
		//ClsLogging.writeFileLog("Test de acceso a proceso: " + proceso);
		if (proceso==null) {
			if (uri==null) throw new ClsExceptions("URL no reconocida por SIGA");
			int idexofdo=uri.indexOf(".do");
			if (idexofdo==-1) throw new ClsExceptions("URL no reconocida por SIGA ("+uri+")");
			int indexofslash=uri.indexOf("/",1);
			if (indexofslash==-1) throw new ClsExceptions("URL no reconocida por SIGA ("+uri+")");
			proceso=uri.substring(indexofslash+1,idexofdo);
			access=usrbean.getAccessForProcessName(proceso);
		} else {
			access=usrbean.getAccessForProcessNumber(proceso);
		}
		return access;
	}

	/**
	 * 
	 * @param request
	 */
	private void informacionSesion (HttpServletRequest request){
		int loglevel = 10;
		try {
//			ClsLogging.writeFileLog("SIGAAuxAction.informacionSesion() - INICIO", 3);
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			//			ReadProperties rp=new ReadProperties("SIGA.properties");
			try{
				// Obtenemos el loglevel de properties
//				ClsLogging.writeFileLog("SIGAAuxAction.informacionSesion() - Obtiene el loglevel de properties", 3);
				loglevel = Integer.parseInt(rp.returnProperty("LOG.level").trim());
			} catch (Exception nfe){ }

			// Si el loglevel es 11 entonces muestra el valor de la session
			if (loglevel>=11) {
//				ClsLogging.writeFileLog("SIGAAuxAction.informacionSesion() - El loglevel es mayor o igual a 11", 3);
				HttpSession ses = request.getSession();
				Enumeration enum1 = ses.getAttributeNames();
				ClsLogging.writeFileLog("-----------------------------------", 3);
				ClsLogging.writeFileLog("-------- datos de sesion ----------", 3);
				while (enum1.hasMoreElements()) {
					String clave = (String) enum1.nextElement();
					Object valor = ses.getAttribute(clave);
					if (valor instanceof String) {
						ClsLogging.writeFileLog(clave + ": " + valor.toString(), 3);
					} else if (valor instanceof Integer) {
						ClsLogging.writeFileLog(clave + ": " + ((Integer) valor).toString(), 3);
					} else if (valor instanceof Long) {
						ClsLogging.writeFileLog(clave + ": " + ((Long) valor).toString(), 3);
					} else if (valor instanceof Double) {
						ClsLogging.writeFileLog(clave + ": " + ((Double) valor).toString(), 3);
					} else if (valor instanceof Hashtable) {
						Hashtable ht = (Hashtable) valor;
						ClsLogging.writeFileLog(clave + " (Hashtable)", 3);
						Enumeration enum2 = ht.keys();
						while (enum2.hasMoreElements()) {
							String clave2 = (String) enum2.nextElement();
							Object valor2 = ht.get(clave2);
							ClsLogging.writeFileLog("  - " + clave2 + ": " + valor2.toString() + "(" + valor2.getClass().getName()
									+ ")", 3);
						}
					} else if (valor instanceof RowsContainer) {
						RowsContainer rc = (RowsContainer) valor;
						ClsLogging.writeFileLog(clave + " (RowsContainer)", 3);
						Vector v = rc.getAll();
						for (int i = 0; i < v.size(); i++) {
							ClsLogging.writeFileLog("  - " + v.get(i).toString(), 3);
						}
					} else if (valor instanceof Vector) {
						Vector v = (Vector) valor;
						ClsLogging.writeFileLog(clave + " (Vector)", 3);
						for (int i = 0; i < v.size(); i++) {
							ClsLogging.writeFileLog("  - " + v.get(i).toString() + "(" + v.get(i).getClass().getName() + ")", 3);
						}
					} else if (valor instanceof ArrayList) {
						ArrayList v = (ArrayList) valor;
						ClsLogging.writeFileLog(clave + " (ArrayList)", 3);
						for (int i = 0; i < v.size(); i++) {
							ClsLogging.writeFileLog("  - " + v.get(i).toString() + "(" + v.get(i).getClass().getName() + ")", 3);
						}
					} else if (valor instanceof UsrBean) {
						UsrBean u = (UsrBean) valor;
						ClsLogging.writeFileLog(clave + " (UsrBean)", 3);
					} else if (valor instanceof MasterForm) {
						ClsLogging.writeFileLog(clave + ": " + "(" + valor.getClass().getName() + ")", 3);
					} else {
						ClsLogging.writeFileLog(clave + ": " + "(" + valor.getClass().getName() + ")", 3);
					}
					ClsLogging.writeFileLog(". . . . . ", 3);
				}
				ClsLogging.writeFileLog("-----------------------------------", 3);
			}
//			ClsLogging.writeFileLog("SIGAAuxAction.informacionSesion() - FIN", 3);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - SIGAAuxAction.informacionSesion(): " + e.getMessage(), e, 3);
		}
	}

	/*
	 * Metodos para recuperar informacion del UserBean
	 * 
	 */

	/** 
	 *  Funcion que recupera el userName
	 *  @param HttpServletRequest
	 *  @return Integer con el userName
	 */
	protected Integer getUserName(HttpServletRequest request) {
		return new Integer (getUserBean(request).getUserName());
	}

	/** 
	 *  Funcion que recupera la institucion
	 *  @param HttpServletRequest
	 *  @return Integer con el ID de la institucion
	 */
	protected Integer getIDInstitucion(HttpServletRequest request) {
		return new Integer(getUserBean(request).getLocation());
	}

	/** 
	 *  Funcion que recupera el idioma
	 *  @param HttpServletRequest
	 *  @return String con el idioma
	 * 
	 */
	protected String getLenguaje(HttpServletRequest request) {
		return getUserBean(request).getLanguage();
	}



	/*
	 * Metodos para las acciones que utilizan AJAX
	 */

	protected void respuestaComboHTMLoptionsHTML(StringBuilder comboHTMLOptions, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(comboHTMLOptions.toString());
	}
	
	protected void respuestaComboHTMLOptionsJson(StringBuilder comboHTMLOptions, HttpServletResponse response) throws IOException, JSONException {
		respuestaComboHTMLOptionsJson(comboHTMLOptions, response, false);
	}
	protected void respuestaComboHTMLOptionsJson(StringBuilder comboHTMLOptions, HttpServletResponse response, boolean bMaxItemLength) throws IOException, JSONException {
		final int MAX_ITEM_LONG = 250;
		JSONObject json = new JSONObject();
		JSONArray items = new JSONArray();
		
		if (bMaxItemLength){
			if (comboHTMLOptions.length() > MAX_ITEM_LONG){
				int i = 1;
				String itemsHTML = comboHTMLOptions.toString();
				items.put(itemsHTML.substring(0, MAX_ITEM_LONG));
				while (itemsHTML.length() > i*MAX_ITEM_LONG){
					int endIndex = (i+1)*MAX_ITEM_LONG;
					if (endIndex > itemsHTML.length() - 1){
						endIndex = itemsHTML.length() - 1;
					}
					items.put(itemsHTML.substring(i*MAX_ITEM_LONG + 1, endIndex));
					i++;
				}
				if (i*MAX_ITEM_LONG < itemsHTML.length()){
					items.put(itemsHTML.substring(i*MAX_ITEM_LONG + 1, itemsHTML.length() - 1));
				}
				
			} else {
				items.put(comboHTMLOptions.toString());
			}
			json.put("itemsHTML", items);
		} else {
			json.put("itemsHTML", comboHTMLOptions.toString());
		}
		respuestaJson(json, response);
	}
	
	protected void respuestacomboHTMLOptionsAndArrayJson(List<ParejaNombreID> list, HttpServletResponse response) throws IOException, JSONException {
		JSONObject json = new JSONObject();
		
		StringBuilder optionsHTML = new StringBuilder();
		JSONArray items = new JSONArray();
		for (int i=0;i<list.size();i++) {		
			JSONObject item = new JSONObject();
			ParejaNombreID parejaNombreID = (ParejaNombreID) list.get(i);
			item.put("name", parejaNombreID.getNombre());
			item.put("value", parejaNombreID.getIdNombre());
			items.put(item);
			optionsHTML.append("<option value='");
			optionsHTML.append(parejaNombreID.getIdNombre());
			optionsHTML.append("'>");
			optionsHTML.append(parejaNombreID.getNombre());
			optionsHTML.append("</option>");
		}
		
		json.put("items", items);
		json.put("itemsHTML", optionsHTML);
		respuestaJson(json, response);
	}
	
	protected void respuestaJson(JSONObject json, HttpServletResponse response) throws IOException{
		//response.setContentType("text/x-json;charset=ISO-8859-15");
		//response.setHeader("Content-Type", "application/json");
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");		
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString());
	}
	
	protected void respuestaAjaxParejaNombreID(AjaxXmlBuilder ajaxXmlBuilder, List<ParejaNombreID> list, HttpServletResponse response) throws IOException {
		ajaxXmlBuilder.addItems(list);
		respuestaAjax(ajaxXmlBuilder, response);
	}
	
	protected void respuestaAjaxString(AjaxXmlBuilder ajaxXmlBuilder, List<String> list, HttpServletResponse response) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			ajaxXmlBuilder.addItem(list.get(i),list.get(i));
		}
		respuestaAjax(ajaxXmlBuilder, response);
	}	
	
	protected void respuestaAjax(AjaxXmlBuilder ajaxXmlBuilder, List<String> list, HttpServletResponse response) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			ajaxXmlBuilder.addItem((String)list.get(i));
		}
		respuestaAjax(ajaxXmlBuilder, response);
	}
	
	protected void respuestaAjax(AjaxXmlBuilder ajaxXmlBuilder,  HttpServletResponse response) throws IOException {
		ajaxXmlBuilder.setEncoding("ISO-8859-15");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		ServletOutputStream out = response.getOutputStream();
		String strBuilder = ajaxXmlBuilder.toString();
		out.print(strBuilder);
		out.flush();
		out.close();
	}
	
	@SuppressWarnings("unchecked")
	protected void respuestaAjax(AjaxMultipleCollectionXmlBuilder builder, Collection<Collection> collection, HttpServletResponse response) throws IOException{
		builder.setEncoding("ISO-8859-15");
		builder.addItems(collection);
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    ServletOutputStream out = response.getOutputStream();
	    String strBuilder = builder.toString();
	    out.print(strBuilder);
	    out.flush();
	    out.close();
	}

	@SuppressWarnings("unchecked")
	protected void respuestaAjax(AjaxCollectionXmlBuilder builder, Collection collection, HttpServletResponse response) throws IOException{
		builder.setEncoding("ISO-8859-15");
		builder.addItems(collection);
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    ServletOutputStream out = response.getOutputStream();
	    String strBuilder = builder.toString();
	    out.print(strBuilder);
	    out.flush();
	    out.close();
	}
	
	protected String descargaFicheroGlobal (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		
		File fichero = null;
		String rutaFichero = null;
		MasterForm miform = null;
		
		try {
			//Obtenemos el formulario y sus datos:
			miform = (MasterForm)formulario;
			rutaFichero = miform.getRutaFichero();
			fichero = new File(rutaFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			//Si viene el nombre del fichero le ponemos el nombre que venga y sino le ponemos el nombre por defecto del archivo
			if(miform.getNombreFichero() != null && !"".equalsIgnoreCase(miform.getNombreFichero())){
				request.setAttribute("nombreFichero", miform.getNombreFichero());
			}else{
				request.setAttribute("nombreFichero", fichero.getName());
			}	
			request.setAttribute("rutaFichero", fichero.getPath());
			
			String sBorrarFichero = (String)request.getParameter("borrarFichero");			
			if (sBorrarFichero==null) {
				sBorrarFichero = (String)request.getAttribute("borrarFichero");
			}
			
			// Si viene a false, no se borra el fichero final
			if (sBorrarFichero==null || sBorrarFichero.equalsIgnoreCase("true")) {
				request.setAttribute("borrarFichero", "true");
			}
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFicheroGlobal";	
	}


	protected void throwExcp(String mensaje, String[] params, 
			Exception e, UserTransaction tx) throws SIGAException {
		try {
			if (tx!=null) {
				tx.rollback();
			}
		} catch (Exception el) {
			//el.printStackTrace();
		}
		if (e!=null && e instanceof SIGAException) {
			((SIGAException)e).setParams(params);
			throw (SIGAException)e; 
		}
		if (e!=null) {
			SIGAException se = new SIGAException(mensaje,e,params);
			// RGG Indico que es clsExceptions para  mostrar el codigo de error  
			se.setClsException(true);
			throw se;
		}
	}

	/**
	 *  
	 * @param idInstitucion
	 * @return <code>true</code> si <code>idInstitucion</code> es 'General', <code>false</code> en caso contrario
	 */
	public boolean institucionEsGeneral(String idInstitucion){
		//@TODO mover este metodo a una clase mas adecuada
		return "2000".equals(idInstitucion);
	}
	/**
	 *  
	 * @param idInstitucion
	 * @return <code>true</code> si <code>idInstitucion</code> es un Consejo, <code>false</code> en caso contrario
	 */
	public boolean institucionEsConsejo(String idInstitucion){
		//@TODO mover este metodo a una clase mas adecuada
		return idInstitucion != null && idInstitucion.startsWith("3");
	}
	
	public BusinessManager getBusinessManager() {
		return BusinessManager.getInstance();		
		
	}
}