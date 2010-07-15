package com.siga.general;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.AjaxMultipleCollectionXmlBuilder;
import com.siga.Utilidades.SIGAReferences;
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
			try {
				testSession(request,response,this.getServlet());
			} catch (ClsExceptions e) {

				ClsLogging.writeFileLog("USRBEAN nulo",request,5);
				return mapping.findForward("inicioGlobal");
			}

			String access=testAccess(request);
			if (!access.equals(SIGAConstants.ACCESS_READ) && 
					!access.equals(SIGAConstants.ACCESS_FULL)) {
				ClsLogging.writeFileLog("Acceso denegado",request,3);
				return mapping.findForward("accesodenegado");
			}

			HttpSession ses = request.getSession();
			if (ses.isNew())
			{
				ClsLogging.writeFileLogError("Sesión nueva",request,3);
				return mapping.findForward("inicioGlobal");
			}
			usrbean=(UsrBean)ses.getAttribute("USRBEAN");
			if (usrbean==null)
			{
				ClsLogging.writeFileLog("USRBEAN nulo",request,5);
				return mapping.findForward("inicioGlobal");
			}
			// RGG 03-03-2005 FIN CAMBIO 

		} catch(ClsExceptions e)		{
			SIGAException ce = new SIGAException(e);
			ce.prepare(request);
			return mapping.findForward("exception");
		} catch (Exception e) { 
			SIGAException ce = new SIGAException(e);
			ce.prepare(request);
			return mapping.findForward("exception");
		}
		try {
			AuxForm miForm = (AuxForm) formulario;
			if (miForm != null) {
				aux = miForm.getAccion() + "/" + miForm.getModo();
				sali = " URL:" +request.getRequestURL() + "?" + aux + " +  usuario:" + usrbean.getUserName()  + " " + usrbean.getUserDescription() + " + idPersona:" + usrbean.getIdPersona() + " +  Transaccion:" + usrbean.getStrutsTrans() + " + sesionID:" +request.getSession().getId();
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
					throw new SIGAException("messages.general.error",e);
				}
			}

			return executeInternal(mapping,formulario,request,response);

		} catch (SIGAException se) {
			//ClsLogging.writeFileLogError(se.getMessage(),se,3);
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
		}
	}

	private boolean isBaseForm(ActionForm formulario) {
		if (formulario == null)
			return false;
		
		Class<?> superClass = formulario.getClass().getSuperclass();
		while (superClass != AuxForm.class) {
			if (superClass.equals(BaseForm.class))
				return true;
			superClass = superClass.getSuperclass();
		}
		
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
//		UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
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
		////////////////
		if (usrbean==null) { 
			ClsExceptions e=new ClsExceptions("Usuario inválido. Es necesario firmar antes de utilizar la aplicación");
			e.setErrorCode("USERNOVALID");
			throw e;
		}
		String access=SIGAConstants.ACCESS_DENY;
		
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
	private void informacionSesion (HttpServletRequest request) 
	{
		int loglevel = 10;
		try {
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			//			ReadProperties rp=new ReadProperties("SIGA.properties");
			try
			{
				// Obtenemos el loglevel de properties
				loglevel = Integer.parseInt(rp.returnProperty("LOG.level").trim());
			} catch (Exception nfe){ }

			// Si el loglevel es 11 entonces muestra el valor de la session
			if (loglevel>=11) {
				HttpSession ses = request.getSession();
				Enumeration enum1 = ses.getAttributeNames();
				System.out.println("-----------------------------------");
				System.out.println("-------- datos de sesion ----------");
				while (enum1.hasMoreElements()) {
					String clave =(String) enum1.nextElement();
					Object valor = ses.getAttribute(clave);
					if (valor instanceof String) {
						System.out.println(clave + ": " + valor.toString());
					} else 
						if (valor instanceof Integer) {
							System.out.println(clave + ": " + ((Integer)valor).toString());
						} else 
							if (valor instanceof Long) {
								System.out.println(clave + ": " + ((Long)valor).toString());
							} else 
								if (valor instanceof Double) {
									System.out.println(clave + ": " + ((Double)valor).toString());
								} else 
									if (valor instanceof Hashtable) {
										Hashtable ht = (Hashtable)valor;
										System.out.println(clave + " (Hashtable)");
										Enumeration enum2 = ht.keys();
										while (enum2.hasMoreElements()) {
											String clave2 =(String) enum2.nextElement();
											Object valor2 = ht.get(clave2);
											System.out.println("  - "+clave2 + ": " + valor2.toString() + "(" + valor2.getClass().getName()+")");
										}
									} else 
										if (valor instanceof RowsContainer) {
											RowsContainer rc = (RowsContainer)valor;
											System.out.println(clave + " (RowsContainer)");
											Vector v = rc.getAll();
											for (int i=0;i<v.size();i++) {
												System.out.println("  - "+v.get(i).toString());
											}
										} else 
											if (valor instanceof Vector) {
												Vector v = (Vector)valor;
												System.out.println(clave + " (Vector)");
												for (int i=0;i<v.size();i++) {
													System.out.println("  - "+v.get(i).toString()  + "(" + v.get(i).getClass().getName()+")");
												}
											} else 
												if (valor instanceof ArrayList) {
													ArrayList v = (ArrayList)valor;
													System.out.println(clave + " (ArrayList)");
													for (int i=0;i<v.size();i++) {
														System.out.println("  - "+v.get(i).toString()  + "(" + v.get(i).getClass().getName()+")");
													}
												} else 
													if (valor instanceof UsrBean) {
														UsrBean u = (UsrBean)valor;
														System.out.println(clave + " (UsrBean)");
													} else 
														if (valor instanceof MasterForm) {
															System.out.println(clave + ": "+ "(" + valor.getClass().getName()+")");
														} else {
															System.out.println(clave + ": "+ "(" + valor.getClass().getName()+")");
														}

					System.out.println(". . . . . ");

				}
				System.out.println("-----------------------------------");

			}
		}
		catch (Exception e){ 

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
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero","true");

			
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

