//VERSIONES:
//david.sanchezp Creacion: 17-03-2005 
//

package com.siga.facturacionSJCS.action;

import java.util.Hashtable;

import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.facturacionSJCS.UtilidadesFacturacionSJCS;
import com.siga.facturacionSJCS.form.MantenimientoPagoForm;
import com.siga.general.*;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;

import java.util.Vector;

/**
* Clase action form del caso de uso CONSULTAR PAGOS
* @author david.sanchezp 17-03-2005
*/
public class MantenimientoPagoAction extends MasterAction {

	/**
	 * Metodo que implementa el modo abrir. Con este metodo entramos a la primera pantalla del caso de uso del <br>
	 * Mantenimiento de Pagos.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

			// borro el formulario en session de Avanzada
			MantenimientoPagoForm miformSession = (MantenimientoPagoForm)request.getSession().getAttribute("mantenimientoPagoForm");

			// obtengo la visibilidad para el user
			String visibilidad = obtenerVisibilidadUsuario(request);
			request.setAttribute("SJCSInstitucionesVisibles",visibilidad);
			
			if (CenVisibilidad.tieneHijos(user.getLocation())) {
				request.setAttribute("SJCSTieneHijos","S");
			} else {
				request.removeAttribute("SJCSTieneHijos");
			}
			
			//Miro si tengo que buscar al cargar la pagina:
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			
			// para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("SJCSBusquedaPagoTipo","BPN"); // busqueda normal			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}

		// COMUN
		return "inicio";
	}

	/*
	 * Obtienen la visibilidad para el usuario 
	 */
	private String obtenerVisibilidadUsuario(HttpServletRequest req) throws SIGAException {
		try {
			UsrBean user = (UsrBean) req.getSession().getAttribute("USRBEAN");
			//Institucion del usuario:
			String idInstitucion=user.getLocation();
			//Visibilidad para la institucion del usuario:
			return CenVisibilidad.getVisibilidadInstitucion(idInstitucion);
		}
		catch (Exception e) {
			throw new SIGAException (e);
		}
	}


	/**
	 * Metodo que implementa el modo editar del boton editar de la primera pantalla del Mantenimiento de Pagos.<br>
	 * Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String idPagosJG="", idInstitucionUsuario="";
		String destino = "";
		Vector fila = null;
		
		try {
  	
	     	MantenimientoPagoForm miform = (MantenimientoPagoForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
	     	//Si vengo de la pantalla de busqueda:
			fila = miform.getDatosTablaOcultos(0);
			idPagosJG = (String)fila.get(0);
			idInstitucionUsuario = (String)fila.get(2);
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosPago = new Hashtable();
			datosPago.put("accion","edicion");
			datosPago.put("idPagosJG",idPagosJG);
			datosPago.put("idInstitucion",idInstitucionUsuario);
	
			request.setAttribute("datosPago", datosPago);		
	
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
	
		return destino;
	}


	/**
	 * Metodo que implementa el modo ver del boton ver de la primera pantalla del Mantenimiento de Pagos.<br>
	 * Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		String destino = "";
		
		try {
  	
	     	MantenimientoPagoForm miform = (MantenimientoPagoForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector fila = miform.getDatosTablaOcultos(0);
			String idPagosJG = (String)fila.get(0);
			String idInstitucionRegistro = (String)fila.get(1);
			String idInstitucionUsuario = (String)fila.get(2);
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
						
			Hashtable datosPago = new Hashtable();
			datosPago.put("accion","consulta");
			datosPago.put("idPagosJG",idPagosJG);
			datosPago.put("idInstitucion",idInstitucionRegistro);
	
			request.setAttribute("datosPago", datosPago);		
	
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
	
		return destino;
	}



	/**
	 * Metodo que implementa el modo nuevo. Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		String mensaje = "";

	     try {
			MantenimientoPagoForm miform = (MantenimientoPagoForm)formulario;
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			String modo = "nuevo";
	
			Hashtable datosPago = new Hashtable();
			datosPago.put("accion",modo);
			datosPago.put("idPagosJG","");
			datosPago.put("idInstitucion",user.getLocation());
			request.setAttribute("datosPago", datosPago);		
			destino="administracion";
	     } 	
	     catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
	     return destino;
	}

	/**
	 * Método que implementa el modo buscarPor. Realiza la busqueda de los pagos teniendo en cuenta la fecha <br>
	 * última del estado del pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
		 	// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// casting del formulario
			MantenimientoPagoForm miFormulario = (MantenimientoPagoForm)formulario;
			
			//Marcamos la busqueda para el boton volver:
			miFormulario.setBuscar("SI");
			
			// busqueda de clientes
			FcsPagosJGAdm adm = new FcsPagosJGAdm(this.getUserBean(request));
			
			Vector resultado = null;
			
			Hashtable criterios = new Hashtable();
			if (miFormulario.getIdInstitucion()!=null) criterios.put("idInstitucion",miFormulario.getIdInstitucion());
			if (miFormulario.getIdEstado()!=null) criterios.put("idEstado",miFormulario.getIdEstado());
			if (miFormulario.getFechaIni()!=null) criterios.put("fechaIni",miFormulario.getFechaIni());
			if (miFormulario.getFechaFin()!=null) criterios.put("fechaFin",miFormulario.getFechaFin());
			if (miFormulario.getNombre()!=null) criterios.put("nombre",miFormulario.getNombre());
//			if (miFormulario.getCriterioPago()!=null) criterios.put("criterioPago",miFormulario.getCriterioPago());
			
			resultado = adm.getPagos(criterios,user.getLocation());

			request.setAttribute("SJCSResultadoBusquedaPago",resultado);
			
			destino="resultado";
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
	}

	
	/** 
	 * Método que implementa la accion borrar del boton borrar de la primera pantalla del <br>
	 * Mantenimiento de pagos.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm(this.getUserBean(request));
		FcsMovimientosVariosAdm fcsMovimientosVariosAdm = new FcsMovimientosVariosAdm(this.getUserBean(request)); 
		UsrBean usr;
		String forward="";
		MantenimientoPagoForm miform = (MantenimientoPagoForm)formulario;
		Hashtable registro;
		UserTransaction tx = null;		
		Vector ocultos;
		String idEstadoPagos=null;
		
		try 
		{
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ocultos = miform.getDatosTablaOcultos(0);

			Integer idPago		  = new Integer ((String)ocultos.get(0));
			Integer idInstitucion = new Integer ((String)ocultos.get(1));

			//Antes de borrar  ponemos a null el IDPAGOJG si este es igual idPago:
			fcsMovimientosVariosAdm.desasignarPago(""+idInstitucion, ""+idPago);
			
			// Obtenemos el idFacturacion
			registro = new Hashtable();
			UtilidadesHash.set(registro, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(registro, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector vAux = pagosJGAdm.select(registro);
			if ((vAux == null) || (vAux.size() != 1)) {
				throw new ClsExceptions ("messages.general.error");
			}
			FcsPagosJGBean bean = (FcsPagosJGBean) vAux.get(0);
			Integer idFacturacion = bean.getIdFacturacion();
			
			// Recupero el nombre de los ficheros asociados a la facturacion
			Hashtable nombreFicheros = UtilidadesFacturacionSJCS.getNombreFicherosPago(idInstitucion, idFacturacion, idPago, null, this.getUserBean(request));
			
			//Borramos: se hace borrado en cascada de todos los apuntes, movimientos varios, estados, criterios pago y cobros de retenciones judiciales
			if(pagosJGAdm.delete(bean)) {
				
				// borrado fisico de ficheros del servidor web
				UtilidadesFacturacionSJCS.borrarFicheros(idInstitucion, nombreFicheros, this.getUserBean(request));
			}
			
			forward = exitoRefresco("messages.deleted.success",request);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null); 
		}
		return forward;
	}

	/**
	 * Metodo que implementa el modo editar del refresco de la pantalla de edicion de pagos.<br>
	 * Pasa los parametros necesarios a la pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String idPagosJG="", idInstitucionUsuario="";
		String destino = "";

		try {
	
	     	MantenimientoPagoForm miform = (MantenimientoPagoForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			//Si vengo de refrescar la insercion de un pago:
			idPagosJG = miform.getIdPagosJG();
			idInstitucionUsuario = miform.getIdInstitucion();
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			
			Hashtable datosPago = new Hashtable();
			datosPago.put("accion","edicion");
			datosPago.put("idPagosJG",idPagosJG);
			datosPago.put("idInstitucion",idInstitucionUsuario);
	
			request.setAttribute("datosPago", datosPago);		
	
			destino="administracion";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
	
		return destino;
	}
	
}
