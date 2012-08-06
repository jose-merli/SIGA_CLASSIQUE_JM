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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesString;


public abstract class MasterAction extends SIGAAuxAction {
	
	/* Constante con el mapping "notImplemented" */
	public static final String mapSinDesarrollar = "notImplemented";
	public final String paginador = "DATAPAGINADOR";
	public final String paginadorModal = "DATAPAGINADORMODAL";
	public final String paginadorPenstania = "DATAPAGINADORPESTANIA";
	protected final String separador = "||";
	//public final String sPrefijoDownload = "download:/";
	
	
		
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
				
				compruebaFilaSeleccionada(miForm, request);
				
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
				if (accion.equalsIgnoreCase("descargaFicheroGlobal")){
					mapDestino = descargaFicheroGlobal(mapping, miForm, request, response);
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
				
				// consultaAplicacion
				if (accion.equalsIgnoreCase("consultaAplicacion")){
					mapDestino = consultaAplicacion(mapping, miForm, request, response);
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
	 *  Funcion que prepara la salida en caso de exito con descarga de archivo
	 *  @param mensaje en formato key de recurso
	 *  @param request para enviar los datos
	 *  @return String con el forward
	 */
	protected String exitoConDescarga(String mensaje, HttpServletRequest request) {
		if (mensaje!=null && !mensaje.equals("")) {
			request.setAttribute("mensaje",mensaje);
		}
		return "exitoDescarga"; 
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
	protected String consultaAplicacion (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		return mapSinDesarrollar;
	}
	
	
	
	protected void throwExcp(String mensaje, Exception e, UserTransaction tx) throws SIGAException {
		throwExcp(mensaje, null,e, tx);	
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
		registro.clear();
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

}
