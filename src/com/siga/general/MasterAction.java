/*
 * VERSIONES:
 * daniel.campos - 10-10-2004 - Creación
 * raul.ggonzalez - 15-12-2004 - Se anhaden comentarios javadoc
 * raul.ggonzalez - 02-02-2005 - Se anhade que las funciones de modos lancen tambien SIGAException
 * luismiguel.sanchez - 14/02/2005 - Se anhade la funcionalidad para descargar ficheros.
 * luismiguel.sanchez - 15/02/2005 - Se comenta la funcionalidad de descargar ficheros, pues de momento
 *                                   es un caso puntual y se ha implementado en su executeInternal. Se
 *                                   mantiene comentado por si hiciera falta en un futuro.
 * raul.ggonzalez - 03-03-2005 - Se anhade control de sesion para que salte al inicio	
 *
 */

/**
 * Clase maestra para de los action. Esta clase encarga de atender a la peticiones
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */

package com.siga.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.struts.action.*;

import com.atos.utils.*;
import com.siga.general.SIGAException;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;


public abstract class MasterAction extends SIGAActionBase {
	
	/** Constante con el mapping "notImplemented" */
	public static final String mapSinDesarrollar = "notImplemented";
	public final String paginador = "DATAPAGINADOR";
	public final String paginadorModal = "DATAPAGINADORMODAL";
	public final String paginadorPenstania = "DATAPAGINADORPESTANIA";
	protected final String separador = "||";
	//public final String sPrefijoDownload = "download:/";
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
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
			//ClsLogging.writeFileLog("MasterAction modo="+((MasterForm)formulario).getModo(),10);
			
			MasterForm miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion = miForm.getModo();
				aux=accion;
				sali = " URL:" +request.getRequestURL() + "?" + accion + " +  usuario:" + usrbean.getUserName()  + " " + usrbean.getUserDescription() + " + idPersona:" + usrbean.getIdPersona() + " +  Transaccion:" + usrbean.getStrutsTrans() + " + sesionID:" +request.getSession().getId();
				ClsLogging.writeFileLog("++++++TRANSACCION++++++++  " + sali,10);
				// RGG 21/09/2007 Muestra la informacion de la session
				// SOLAMENTE CUANDO DEGUB LEVEL DE clog.properties A 11
				informacionSesion(request);
				
			}

			// Comprueba la utilizacion de la fila seleccionada en la tablas
			this.compruebaFilaSeleccionada(miForm, request);

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
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		String mapDestino = "exception";
		MasterForm miForm = null;
		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				
				String accion = miForm.getModo();
				
				// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}
				
				// AbrirAvanzada
				if (accion.equalsIgnoreCase("abrirAvanzada")){
					mapDestino = abrirAvanzada(mapping, miForm, request, response);
					break;
				}
				
				if (accion.equalsIgnoreCase("buscarInicio")){
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscar(mapping, miForm, request, response);
					break;
				}
				
				// Buscar
				if (accion.equalsIgnoreCase("buscar")){
					mapDestino = buscar(mapping, miForm, request, response);
					break;
				}
				
				// Editar
				if (accion.equalsIgnoreCase("editar")){
					mapDestino = editar(mapping, miForm, request, response);
					break;
				}
				
				// Ver
				if (accion.equalsIgnoreCase("ver")){
					mapDestino = ver(mapping, miForm, request, response);
					break;
				}
				
				// Nuevo
				if (accion.equalsIgnoreCase("nuevo")){
					mapDestino = nuevo(mapping, miForm, request, response);
					break;
				}
				
				// Insertar
				if (accion.equalsIgnoreCase("insertar")){
					mapDestino = insertar(mapping, miForm, request, response);
					break;
				}
				
				// Modificar
				if (accion.equalsIgnoreCase("modificar")){
					mapDestino = modificar(mapping, miForm, request, response);
					break;
				}
				
				// Borrar
				if (accion.equalsIgnoreCase("borrar")){
					mapDestino = borrar(mapping, miForm, request, response);
					break;
				}
				
				// BuscarPor
				if (accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscarPor(mapping, miForm, request, response);
					break;
				}
//   			 BuscarPor
				if (accion.equalsIgnoreCase("buscarInit")){
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = buscarPor(mapping, miForm, request, response);
					break;
				}
				
				// abrirConParametros
				if (accion.equalsIgnoreCase("abrirConParametros")){
					mapDestino = abrirConParametros(mapping, miForm, request, response);
					break;
				}
				
				// download
				if (accion.equalsIgnoreCase("download")){
					mapDestino = download(mapping, miForm, request, response);
					break;
				}
				
			} while (false);
			
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//throw new ClsExceptions("El ActionMapping no puede ser nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			
			// Si no es para descargar un fichero, actuamos como siempre.
			//if (!mapDestino.toLowerCase().startsWith(sPrefijoDownload))
			//{
			return mapping.findForward(mapDestino);
			//}
			
			// Si es para descargar un fichero, mandamos la correspondiente respuesta.
			//
			// Previamente en el action correspondiente habría que haber hecho lo siguiente (2 cosas):
			// response.setHeader("Content-disposition", "attachment; filename=nombre_del_fichero_sin_ruta");
			// return "download://ruta_relativa_al_fichero_dentro_del_contexto_de_la_aplicacion"; 
			//
			// Ejemplo:
			// response.setHeader("Content-disposition", "attachment; filename=hola.xls");
			// return "download://html/jsp/general/hola.xls"; 
			//else
			//{
			//    response.setContentType("application/x-file-download");
			//    
			//    return new ActionForward (mapDestino.substring(mapDestino.indexOf(sPrefijoDownload)+sPrefijoDownload.length()));
			//}
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e){
			throw new SIGAException("error.messages.application",e);
		}
		/*
		 } catch(ClsExceptions e) {
		 throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		 } catch (Exception e){
		 throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
		 }
		 */
	}
	
	
	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		return "inicio";
			}
	
	/** 
	 *  Funcion que atiende la accion abrirAvanzada
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String abrirAvanzada (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion abrirConParametros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 * Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String editar (ActionMapping mapping, 		
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String ver (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String nuevo (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String insertar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String modificar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String borrar (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		return mapSinDesarrollar;
		
	}
	
	/** 
	 *  Funcion que atiende la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String buscarPor (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que atiende la accion download
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String download (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que recupera el userName
	 *  @param HttpServletRequest
	 *  @return Integer con el userName
	 */
	protected Integer getUserName(HttpServletRequest request) {
		return new Integer (this.getUserBean(request).getUserName());
	}
	
	/** 
	 *  Funcion que recupera el perfil
	 *  @param HttpServletRequest
	 *  @return String con el perfil
	 */
	/*	protected String getProfileName(HttpServletRequest request) {
	 return (String) this.getUserBean(request).getProfile();
	 } */
	
	/** 
	 *  Funcion que recupera la institucion
	 *  @param HttpServletRequest
	 *  @return Integer con el ID de la institucion
	 */
	protected Integer getIDInstitucion(HttpServletRequest request) {
		return new Integer(this.getUserBean(request).getLocation());
	}
	
	/** 
	 *  Funcion que recupera el idioma
	 *  @param HttpServletRequest
	 *  @return String con el idioma
	 * 
	 */
	protected String getLenguaje(HttpServletRequest request) {
		return (String) this.getUserBean(request).getLanguage();
	}
	
	/** 
	 *  Funcion que prepara la salida en caso de error
	 *  @param mensaje en formato key de recurso
	 *  @param excepcion de tipo clsexception
	 *  @param request para preparar la exception
	 *  @return String con el forward
	 */
	protected String error(String mensaje, ClsExceptions e, HttpServletRequest request) {
		e.prepare(request);
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje", mensaje);
		} else {
			request.setAttribute("mensaje", e.getMessage());
		}
		request.setAttribute("sinrefresco","");
		return "exito"; 
	} 
	
	/** 
	 *  Funcion que prepara la salida en caso de error en una pantalla modal	
	 *  @param mensaje en formato key de recurso
	 *  @param excepcion de tipo clsexception
	 *  @param request para preparar la exception
	 *  @return String con el forward
	 */
	protected String errorModal(String mensaje, ClsExceptions e, HttpServletRequest request) {
		e.prepare(request);
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje", mensaje);
		} else {
			request.setAttribute("mensaje", e.getMessage());
		}
		request.setAttribute("sinrefresco","");
		request.setAttribute("modal","");
		return "exito"; 
	} 
	
	/** 
	 *  Funcion que prepara la salida en caso de exito con refresco
	 *  @param mensaje en formato key de recurso
	 *  @param request para enviar los datos
	 *  @return String con el forward
	 */
	protected String exitoRefresco(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		return "exito"; 
	}
	/** 
	 *  Funcion que prepara la salida en caso de exito sin refresco
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exito(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("sinrefresco","");
		return "exito"; 
	}
	
	/** 
	 *  Funcion que prepara la salida en caso de exito en una ventana modal
	 *  que siempre va a refrescar la ventana padre
	 *  @param mensaje en formato key de recurso
	 *  @param request para envias los datos
	 *  @return String con el forward
	 */
	protected String exitoModal(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("modal","");
		return "exito"; 
	}
	
	protected String exitoModalSinRefresco(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		request.setAttribute("sinrefresco","");
		request.setAttribute("modal","");
		return "exito"; 
	}
	
	/** Funcion testAccess
	 *  Comprueba los derechos de acceso sobre procesos del usuario 
	 *   Devuelve el acceso sobre el proceso invocado 
	 *  @param HttpServletRequest
	 *  @exception ClsException 
	 * @exception  SIGAException  Errores de aplicación
	 * */
	
	private String testAccess(HttpServletRequest request) throws ClsExceptions, SIGAException{
		UsrBean usrbean=(UsrBean)request.getSession().getAttribute(ClsConstants.USERBEAN);
//		UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		// Si venimos por SolicitarIncorporacionAccesoDirectoAction no comprobamos el tipo de acceso porque 
		// no vamos a entrar a la aplicacion, sola a una parte concreta: SolicitarIncorporacion 
		String uri = request.getRequestURI();
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
		String proceso=request.getParameter("process");
		String access=SIGAConstants.ACCESS_DENY;
		
		if (proceso==null) {
			uri=request.getRequestURI();
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
	
	
	protected void throwExcp(String mensaje, Exception e, UserTransaction tx) throws SIGAException {
		throwExcp(mensaje, null,e, tx);	
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
	
	protected void throwExcpNoAlert(String mensaje, Exception e, UserTransaction tx) throws SIGAException {
		throwExcpNoAlert(mensaje, null,e, tx);	
	}
	
	protected void throwExcpNoAlert(String mensaje, String[] params, Exception e, UserTransaction tx) throws SIGAException {
		try {
			if (tx!=null) {
				tx.rollback();
			}
		} catch (Exception el) {
			e.printStackTrace();
		}
		if (e!=null && e instanceof SIGAException) {
			SIGAException se = (SIGAException)e;
			se.setParams(params);
			se.setHiddenFrame(false);
			throw se; 
		}
		if (e!=null) {
			SIGAException se = new SIGAException(mensaje,e,params);
			// RGG Indico que es clsExceptions para  mostrar el codigo de error  
			se.setClsException(true);
			se.setHiddenFrame(false);
			throw se;
		}
	}
	
	private void compruebaFilaSeleccionada (MasterForm formulario, HttpServletRequest request) 
	{
		try {
			String accion = formulario.getModo();

			do {
				// Si venimos desde el menu, borramos la fila seleccionada
				String noreset = (String)request.getParameter("noReset");
				if ((accion == null || accion.equalsIgnoreCase("")) && (noreset != null && !UtilidadesString.stringToBoolean(noreset)) ){
					request.getSession().removeAttribute("FILA_SELECCIONADA");
					break;
				}

				String filaSeleccionada = formulario.getFilaSelD();

				// Si la accion es borrar tampoco necesitaremos seleccionar la fila
				if (accion.equalsIgnoreCase("borrar") && (filaSeleccionada != null && !filaSeleccionada.equals(""))){
					request.getSession().removeAttribute("FILA_SELECCIONADA");
					break;
				}
		
				// Si nos dicen que limpiemos la variable, borramos la fila seleccionada 
				if (UtilidadesString.stringToBoolean(formulario.getLimpiarFilaSeleccionada())) {
					request.getSession().removeAttribute("FILA_SELECCIONADA");
					formulario.setLimpiarFilaSeleccionada("");
					break;
				}
				
				// Si tiene valor la fila, lo guardamos en session
				if (filaSeleccionada != null && !filaSeleccionada.equals("")) {
					request.getSession().setAttribute("FILA_SELECCIONADA", filaSeleccionada);
					break;
				}
				
			} while (false);
			
			// Borramos la variable del formulario por si el formulario estuviera en session 
			formulario.setFilaSelD("");
		}
		catch (Exception e){ }
	}


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
	protected String getIdPaginador(String tipoPaginador,String nameClass){
		StringBuffer idPaginador = new StringBuffer();
		idPaginador.append(tipoPaginador);
		idPaginador.append("_");
		idPaginador.append(nameClass.substring(nameClass.lastIndexOf(".")+1).toUpperCase());
		return idPaginador.toString();
		
		
	}
	protected void borrarPaginador(HttpServletRequest request,String paginador) throws SIGAException {
		//Cada vez que se da al boton buscar, se borra el paginador guardado en sesion para luego cargarlo con nuevos criterios
		request.getSession().removeAttribute(paginador);
		request.getSession().removeAttribute("DATAPAGINADOR");
		
	}
	protected HashMap getPaginador(HttpServletRequest request,String paginador) throws SIGAException {
		//Devuelve el paginador de la sesion
		HashMap databackup = (HashMap)request.getSession().getAttribute(paginador);
		return databackup;
		
	}
	protected void setPaginador(HttpServletRequest request,String paginador,HashMap databackup) throws SIGAException {
		//Mete el paginador en sesion
		request.getSession().setAttribute(paginador,databackup);
		
	}
	protected void aniadeClavesBusqueda(String[] clavesBusqueda,ArrayList alClavesBusqueda){
		 
		Hashtable registro = null;

		for (int k=0;k<alClavesBusqueda.size();k++){
			registro = (Hashtable) alClavesBusqueda.get(k);
			aniadeClaveBusqueda(clavesBusqueda, registro);
			
		}

	}
	
	
	protected void aniadeClaveBusqueda(String[] clavesBusqueda, Hashtable registro){
		StringBuffer clave = new StringBuffer();
		for (int i = 0; i < clavesBusqueda.length; i++) {
			String claveBusqueda = clavesBusqueda[i]; 
			clave.append((String)registro.get(claveBusqueda));
			if(i!=clavesBusqueda.length-1)
				clave.append(separador);
			
		}
		registro.put("CLAVE",clave.toString());

	}
	protected ArrayList actualizarSelecionados(String[]clavesBusqueda,String seleccionados, ArrayList alClaves){
		
    	alClaves.clear();
		String[] aSeleccionados = null;
		if(seleccionados!=null && !seleccionados.equals("")){
			aSeleccionados = seleccionados.split(",");
		}

	    	
	    	if( aSeleccionados!=null){
		    	for (int i = 0; i < aSeleccionados.length; i++) {
		    		String registro = aSeleccionados[i];
		    		String[] ids = UtilidadesString.split(registro, separador);
		    		Hashtable registroBusqueda = new Hashtable();
		    		for (int j = 0; j < clavesBusqueda.length; j++) {
		    			String id = ids[j];
		    			registroBusqueda.put(clavesBusqueda[j],id);
					}
		    		
		    		aniadeClaveBusqueda(clavesBusqueda, registroBusqueda);
		    		if(!alClaves.contains(registroBusqueda))
		    			alClaves.add(registroBusqueda);
		    		
		    		
		    		
				}
	    	}

    	
    	return alClaves;
	}
	protected void respuestaAjax(AjaxXmlBuilder ajaxXmlBuilder,
			List<String> list, HttpServletResponse response) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			ajaxXmlBuilder.addItem((String)list.get(i));
			
		}
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter pw = response.getWriter();
	    pw.write(ajaxXmlBuilder.toString());
	    pw.close();
		
		
	}
	
	@SuppressWarnings("unchecked")
	protected void respuestaAjax(AjaxCollectionXmlBuilder builder, Collection collection, HttpServletResponse response) throws IOException{
		builder.addItems(collection);
		response.setContentType("text/xml");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter pw = response.getWriter();
	    pw.write(builder.toString());
	    pw.close();
		
	}
	

}
