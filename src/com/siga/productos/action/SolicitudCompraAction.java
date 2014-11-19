/*
 * VERSIONES: modificado por david.sanchez para incluir el pago con tarjeta. 
 * 
 * nuria.rodriguezg	- 27-01-2005 - Inicio
 * pilar.duran 01/02/2007- Se ha modificado el metod solicitar para la adquisicion del producto
 */
package com.siga.productos.action;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacFacturacionProgramadaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysFormaPagoAdm;
import com.siga.beans.PysFormaPagoBean;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosBean;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.beans.PysServiciosBean;
import com.siga.beans.PysServiciosInstitucionBean;
import com.siga.beans.PysServiciosSolicitadosBean;
import com.siga.beans.PysSuscripcionAdm;
import com.siga.beans.PysSuscripcionBean;
import com.siga.beans.PysTipoServiciosBean;
import com.siga.beans.PysTiposProductosBean;
import com.siga.facturacion.Facturacion;
import com.siga.general.Articulo;
import com.siga.general.CarroCompra;
import com.siga.general.CarroCompraAdm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;
import com.siga.productos.form.SolicitudCompraForm;


/**
 * Clase action para la solicitud de Compra de Productos y Servicios .<br/>
 * Gestiona la abrir, borrar, solicitar, borrarCarrito, continuar, modificarCuenta, desglosePago, finalizarCompra, insertProducto y servicio  
 */
public class SolicitudCompraAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) { 
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();
				
				// La primera vez que se carga el formulario Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					request.getSession().removeAttribute("resultBusqueda");
					request.getSession().removeAttribute("auxSolicitudCompraForm");
					mapDestino = abrir(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("abrirAlVolver")){
					request.getSession().setAttribute("volver","s");
					mapDestino = abrir(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("abrirAlVolverConf")){
					request.getSession().setAttribute("volver","s");
					mapDestino = continuar(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("solicitar")){
					mapDestino = solicitar(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("borrarCarrito")){
					mapDestino = borrarCarrito(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("continuar")){
					mapDestino = continuar(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("modificarCuenta")){
					mapDestino = modificarCuenta(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("finalizarCompra")){
					mapDestino = finalizarCompra(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("confirmarCompra")){
					mapDestino = confirmarCompra(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("pagarConTarjeta")){
					mapDestino = pagarConTarjeta(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("okCompraTPV")){
					mapDestino = okCompraTPV(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("guardarCertificado")){
					mapDestino = guardarCertificado(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("consultarCertificado")){
					mapDestino = consultarCertificado(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("actualizarCliente")){
					mapDestino = actualizarCliente(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("imprimirCompra")){
					mapDestino = imprimirCompra(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("buscarProducto")){
					mapDestino = buscarProducto(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("facturacionRapidaCompra")){
					mapDestino = facturacionRapidaCompra(mapping, miForm, request, response);
					
				}else if (accion.equalsIgnoreCase("mostrarCompra")){
					mapDestino = mostrarCompra(mapping, miForm, request, response);
					
				} else if (accion.equalsIgnoreCase("getAjaxSeleccionSerieFacturacion")) {
					getAjaxSeleccionSerieFacturacion(request, response);	     
					return null;
					
				}else {
					return super.executeInternal(mapping, formulario, request, response);
				}
				
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
			
		 }catch (SIGAException es) { 
		    throw es; 
		 }catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.productos"}); 
		 } 
		 
		   return mapping.findForward(mapDestino);   	
	}

	/** 
	 *  Funcion que atiende la accion abrir. Controla la primera vez que se entra en la funcionalidad.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{	
			UsrBean user = this.getUserBean(request);
			Integer idInstitucion=this.getIDInstitucion(request);
			
			String certificado =request.getParameter("certificado");
			//request.getSession().removeAttribute("resultBusqueda");
			
			if (certificado!=null && !certificado.equalsIgnoreCase("")){
				certificado=request.getParameter("certificado");
				request.getSession().setAttribute("deCertificado",certificado);
			}
			
           
			SolicitudCompraForm form = null;
			if(request.getSession().getAttribute("auxSolicitudCompraForm")!=null){
				form = (SolicitudCompraForm)request.getSession().getAttribute("auxSolicitudCompraForm");
				request.setAttribute("catalogo", form.getCatalogo());
				request.setAttribute("tipoProducto", form.getTipoProducto());
				if(form.getTipoProducto()!=null){
					request.setAttribute("categoriaProducto", form.getCategoriaProducto());
				}
		    	request.setAttribute("producto", form.getProducto());
				
			
				//Datos de los Servicios seleccionados en los combos:
				request.setAttribute("tipoServicio", form.getTipoServicio());
				if(form.getTipoServicio()!=null){
					request.setAttribute("categoriaServicio", form.getCategoriaServicio());
				}
				request.setAttribute("servicio", form.getServicio());
			
										
			}else{
				form = (SolicitudCompraForm) formulario;
			}
			
			Long idPersona 	= null;
			String nombre 	= "";
			String numero 	= "";	 
			String nif 		= "";
	
//			 Si es la primera vez que entramos
			if(request.getParameter("noReset") == null){
				
				// Si el usuario es letrado buscamos sus datos sino no mostramos informaci�n poruq debe seleccionar un letrado
				if (user.isLetrado()){					
					idPersona = new Long (user.getIdPersona());
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenPersonaBean personaBean = personaAdm.getIdentificador(idPersona);

					// Existen datos de la persona
					if(personaBean != null){	
						CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
						CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucion);			
						numero 	= colegiadoAdm.getIdentificadorColegiado(bean);			
						nombre 	= personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
						nif 	= personaBean.getNIFCIF();
					}
					else {
						idPersona = null;
					}
				}
				// Al ser la primera vez que entramos no debemos tener carrito
				request.setAttribute("existeCarro", "N");
				CarroCompraAdm.eliminarCarroCompra(request);
				
			}
			// Venimos de volver y tenemos que recargar los valores anteriores
			else{ 
				CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(), form.getIdInstitucionPresentador(), request);
				
				if(request.getParameter("ventana") != null && request.getParameter("ventana").equals("aceptacion")){
					// Antes de borrar tenemos que recoger los valores de la pagina y guardarlo en el carrito.
					ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
					for(int i=1; i < arrayListaArticulosOrdenada.size()+1; i++){											
						Articulo a = getOcultos(request, i);			
					}		
				}			
				nombre = request.getParameter("nombrePersona");
				numero = request.getParameter("numeroColegiado");
				nif = request.getParameter("nif");
				request.setAttribute("existeCarro", "S");
				idPersona = form.getIdPersona();
				request.setAttribute("idInstitucion", form.getIdInstitucion());
			}
			
			if (request.getParameter("idInstitucionPresentador")!=null ){
				
			    request.setAttribute("idInstitucionPresentador", new Integer(request.getParameter("idInstitucionPresentador")));
			}else{
				request.setAttribute("idInstitucionPresentador", null);
			}
			
			
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("nif", nif);
		    
					
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e, null); 
		} 
		return "abrir";
		
		
	}
	
	/** 
	 *  Funcion que atiende la accion borrar. Permite el borrado de un articulo del carrito
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(), form.getIdInstitucionPresentador(), request);
			Vector ocultos = new Vector();
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			
			Integer idTipoArticulo = Integer.valueOf((String)ocultos.elementAt(0));				
			Long idArticulo = Long.valueOf((String)ocultos.elementAt(1));
			Long idArticuloInstitucion = Long.valueOf((String)ocultos.elementAt(2));
			int claseArticulo = Integer.parseInt((String)ocultos.elementAt(3));
			
			if(request.getParameter("ventana").equals("aceptacion")){
				// Antes de borrar tenemos que recoger los valores de la pagina y guardarlo en el carrito.
				ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
				for(int i=1; i < arrayListaArticulosOrdenada.size()+1; i++){											
					Articulo a = getOcultos(request, i);			
				}		
			}						
			
			if (claseArticulo == Articulo.CLASE_PRODUCTO) {
				carro.borrarProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);
			
			}else{
				carro.borrarServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
			}
			if((carro.getArrayListaArticulosOrdenada()).size()>0){
				request.setAttribute("existeCarro", "S");
			}else{
				request.setAttribute("existeCarro", "N");
			}
			CarroCompraAdm.setCarroCompra(carro, request);
			request.setAttribute("nombrePersona", request.getParameter("nombrePersona"));
			request.setAttribute("numero", request.getParameter("numeroColegiado"));
			request.setAttribute("nif", request.getParameter("nif"));				
		
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		  
		return exitoRefresco("messages.deleted.success", request);
	}
			
	/** 
	 *  Funcion que atiende la accion solicitar. Inserta un articulo en el carrito.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String solicitar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		try{
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(),form.getIdInstitucionPresentador(), request);
			
			String categoria=(String)form.getCategoriaAux();
	  		String producto=(String)form.getProductoAux();
	  		String tipo=(String)form.getTipoAux();
			
			if(form.getConcepto()== null){	}
			else if(form.getConcepto().equalsIgnoreCase("Producto")){
//				Cuando el producto se selecciona despues de una busqueda
				if(request.getParameter("resultBusqueda")!=null && request.getParameter("resultBusqueda").equals("1")){ 
					
			  		
			         carro.insertarProducto(Long.valueOf(tipo), Long.valueOf(producto), Integer.valueOf(categoria));
			
				}else{// producto seleccionado directamente de los combos
				 
				  carro.insertarProducto(Long.valueOf(form.getCategoriaProducto()), form.getProducto(), form.getTipoProducto());
				}
			}else if(form.getConcepto().equalsIgnoreCase("Servicio")){
				// cuando el servicio se selecciona despues de una busqueda
				if(request.getParameter("resultBusqueda")!=null && request.getParameter("resultBusqueda").equals("1")){
			  		 carro.insertarServicio(Long.valueOf(tipo), Long.valueOf(producto), Integer.valueOf(categoria));
				}else{//servicio seleccionado directamente de los combos   
				     carro.insertarServicio(Long.valueOf(form.getCategoriaServicio()), form.getServicio(), form.getTipoServicio());
				}
			}		
			
			request.setAttribute("existeCarro", "S");			
			CarroCompraAdm.setCarroCompra(carro, request);		
			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		}		
		return "solicitar";		
	}
	
	/** 
	 *  Funcion que atiende la accion borrarCarrito. Elimina el carrito.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrarCarrito(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// String modo = "borrarCarrito";		
		try {
			CarroCompraAdm.eliminarCarroCompra(request);
			request.setAttribute("existeCarro", "N");
			
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			
//			Datos de los Productos seleccionados en los combos:
			request.setAttribute("catalogo", form.getCatalogo());
			request.setAttribute("tipoProducto", form.getTipoProducto());
			request.setAttribute("categoriaProducto", form.getCategoriaProducto());
	    	request.setAttribute("producto", form.getProducto());
			
		
			//Datos de los Servicios seleccionados en los combos:
			request.setAttribute("tipoServicio", form.getTipoServicio());
			request.setAttribute("categoriaServicio", form.getCategoriaServicio());
			request.setAttribute("servicio", form.getServicio());
			
			
			request.setAttribute("idPersona", form.getIdPersona());
			request.setAttribute("nombrePersona", form.getNombrePersona());
			request.setAttribute("numero", form.getNumeroColegiado());
			request.setAttribute("nif", form.getNif());
			request.setAttribute("idInstitucion", form.getIdInstitucion());
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		// return modo;
		return "abrir"; 
	 }
	
	/** 
	 * M�todo que atiende la accion actualizarCliente. Actualiza el Cliente con los datos del mismo.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String actualizarCliente(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			request.setAttribute("existeCarro", "S");
			
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			
			//Datos de los Productos seleccionados en los combos:
			request.setAttribute("catalogo", form.getCatalogo());
			request.setAttribute("tipoProducto", form.getTipoProducto());
			request.setAttribute("categoriaProducto", form.getCategoriaProducto());
	    	request.setAttribute("producto", form.getProducto());
			
		
			//Datos de los Servicios seleccionados en los combos:
			request.setAttribute("tipoServicio", form.getTipoServicio());
			request.setAttribute("categoriaServicio", form.getCategoriaServicio());
			request.setAttribute("servicio", form.getServicio());
			
			//Datos del Cliente seleccionado:
			request.setAttribute("idPersona", form.getIdPersona());
			request.setAttribute("nombrePersona", form.getNombrePersona());
			request.setAttribute("numero", form.getNumeroColegiado());
			request.setAttribute("nif", form.getNif());
			request.setAttribute("idInstitucion", form.getIdInstitucion());
			request.setAttribute("idInstitucionPresentador", form.getIdInstitucionPresentador());
			
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		// return modo;
		return "abrir"; 
	 }

	
	
	/** 
	 *  Funcion que atiende la accion continuar. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String continuar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			
		String modo = "continuar";	
		try{
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			UsrBean user = this.getUserBean(request);
			CenPersonaAdm per = new CenPersonaAdm(user);
			
			
			CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(),form.getIdInstitucionPresentador(), request);
			CarroCompraAdm.setCarroCompra(carro, request);
			String certificado=(String)request.getSession().getAttribute("deCertificado");
		    if (certificado==null ||certificado.equals("0")){
		    	certificado=request.getParameter("deCertificado");
		    }
		    if (certificado!=null && certificado.equals("1")) carro.setCompraCertificado(true);
			request.setAttribute("idInstitucion", form.getIdInstitucion());
			request.setAttribute("idInstitucionPresentador", form.getIdInstitucionPresentador());
			request.setAttribute("nombrePersona",  per.obtenerNombreApellidos(form.getIdPersona().toString()));
			request.setAttribute("numero", per.getPersonaColegiado(form.getIdPersona(), form.getIdInstitucion()).getColegiado().getNColegiado());
			request.setAttribute("nif",per.obtenerNIF(form.getIdPersona().toString()));
			request.setAttribute("existeCarro", "S");
			//request.setAttribute("deCertificado",certificado);
			request.getSession().setAttribute("CenBusquedaClientesTipo","P");
		}catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		}			
		return modo;		
	  }
	
	/** 
	 *  Funcion que atiende la accion modificarCuenta. Muestra una ventana para introducir el numero de cuenta
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificarCuenta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		try{
			CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);	
					
			PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));
				
			ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();			
			if(arrayListaArticulosOrdenada.size() == 0){
				return error("messages.pys.solicitudCompra.errorCarritoBorrado",new ClsExceptions(ppcsa.getError()),request);
			}
			
			request.setAttribute("existeCarro", "S");
			request.setAttribute("idPersona",carro.getIdPersona().toString());
			request.setAttribute("idInstitucion", carro.getIdinstitucion().toString());
		}catch (Exception e) { 
			   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		}
		return "modificarCuenta";
	  }
	
	/** 
	 *  Funcion que atiende la accion finalizarCompra. Genera una petici�n de compra e inserta cada uno de los articulos del carrito en las tablas pys_productossolicitados y pys_serviciossolicitados. Genera el comprobante de la compra.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String finalizarCompra(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));
				
		String modo = "comprobanteCompra";
		String idInstitucion=null, idPersona=null, fechaActual=null, operacion=null;
		UserTransaction tx = null;
		//String fechaEfectiva=null;

		try {	
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");				
				
			CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);
		
			//Almaceno el carro en sesion con el UsrBean y el NumOperacion:
			carro.setUsrBean(user);

			//Obtengo el numero de operacion y lo inserto en el carro. El codigo 1 es usado para Facturacion de Productos y Servicios:
			idInstitucion = rellenarConCeros(carro.getIdinstitucion().toString(),4); 
			idPersona = rellenarConCeros(carro.getIdPersona().toString(),10);
			fechaActual = String.valueOf(Calendar.getInstance().getTimeInMillis());;
			operacion = "1"+idInstitucion+idPersona+fechaActual;			
			carro.setNumOperacion(operacion);
			request.getSession().setAttribute(CarroCompraAdm.nombreCarro,carro);

			//---INICIO TRANSACCION---
			tx=user.getTransaction();	
			tx.begin();
			//Insertamos el carro en Base de Datos:
			Long idPeticion = ppcsa.insertarCarro(carro);
			carro.setIdPeticion(idPeticion);
			//---FIN TRANSACCION---
			tx.commit();
			
			if (carro.getCompraCertificado() || carro.tieneServicios()) {
				request.setAttribute("CERTIFICADO_noFactura","1");			
			}
			
			//Eliminamos de sesion el carro:
			//request.getSession().removeAttribute(CarroCompraAdm.nombreCarro);		
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
			String aprobarSolicitud=parametrosAdm.getValor(user.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
			
			if(!user.isLetrado()&&aprobarSolicitud.equals("S")){
				GestionSolicitudesAction gestionSolicitudes=new GestionSolicitudesAction();
				ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();
				Articulo a=null;
	
				if (arrayListaArticulosOrdenada.size()==0) 
					modo = exito("error",request);
				else {			
					//Recorremos los articulos del carro para confirmar la compra de cada uno de ellos					
					for(int i=0; i<arrayListaArticulosOrdenada.size(); i++){

						a=(Articulo)arrayListaArticulosOrdenada.get(i);
						a.setIdPeticion(idPeticion);						
						//request.setAttribute("FECHAEFECTIVA",fechaEfectiva);
						request.setAttribute("ARTICULO",a);
						if(a.getTipoCertificado()==null || "".equals(a.getTipoCertificado())){ //Si no es certificado
							gestionSolicitudes.confirmar(mapping,formulario,request,response);
							//si se confirma la compra, se mostrar� el bot�n de anticipar 
							//si el art�culo tiene como forma de pago "EN METALICO"
							if (a.getIdFormaPago() != null && a.getIdFormaPago().intValue() == 30){
								a.setAnticipar(Boolean.TRUE);
								a.setImporteAnticipado(new Double(0));								
							}
						}
						//mhg - INC_10528_SIGA Si alg�n producto de la compra es no facturable le pasamos un valor al jsp
						if(a.getIdFormaPago() == null)
							request.setAttribute("noFacturable",true);
					}
				}
			}

			//Preparamos los productos y servicios del comprobante:
			request.setAttribute("resultados",this.prepararComprobante(carro,request));			

		} catch (SIGAException siga) {
			throw siga;
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,tx); 
		} 
	
		return modo;	
	} 

	/** 
	 *  Funcion que genera el comprobante de la compra.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String imprimirCompra(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "comprobanteCompraImpresion";
		UserTransaction tx = null;

		try {	
			CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);
		
			//Preparamos los productos y servicios del comprobante:
			request.setAttribute("resultados",this.prepararComprobante(carro,request));			
			
			//Eliminamos de sesion el carro:
			//El atributo CarroCompra ya no se utiliza por lo que se puede borrar aqui
			//No lo borro para poder imprimir:
			//request.getSession().removeAttribute(CarroCompraAdm.nombreCarro);
		} catch (SIGAException siga) {
			throw siga;
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,tx); 
		} 
	
		return modo;	
	} 
	
	
	protected String buscarProducto(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			 
			
			SolicitudCompraForm form =  (SolicitudCompraForm) formulario;
			
			
			String select =""; 
			String where="";
			String consulta="";
			Vector resultado=new Vector();
			request.getSession().removeAttribute("resultBusqueda");
			request.getSession().removeAttribute("auxSolicitudCompraForm");
			
			/*CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(), request);
			
			if(form.getConcepto()== null){	}
			else if(form.getConcepto().equalsIgnoreCase("Producto")){				
				carro.insertarProducto(Long.valueOf(form.getCategoriaProducto()), form.getProducto(), form.getTipoProducto());
			}else if(form.getConcepto().equalsIgnoreCase("Servicio")){
				carro.insertarServicio(Long.valueOf(form.getCategoriaServicio()), form.getServicio(), form.getTipoServicio());
			}		
			
			request.setAttribute("existeCarro", "S");			
			CarroCompraAdm.setCarroCompra(carro, request);	*/
			
		 UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		 
		
		  if (!user.isLetrado()){//no es letrado
			if (!form.getConcepto().equalsIgnoreCase("Servicio") ){// Productos y Certificados
				select =" SELECT "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" IDTIPOPRODUCTO, "+PysProductosInstitucionBean.C_IDPRODUCTO+" IDPRODUCTO, "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+" IDPRODUCTOINSTITUCION, "+PysProductosInstitucionBean.C_IDINSTITUCION+", "+PysProductosInstitucionBean.C_DESCRIPCION;
				where=" WHERE "+PysProductosSolicitadosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion();	
				if (form.getTipoProducto()!=null && form.getTipoProducto().intValue()!=0 ){
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTiposProductosBean.T_NOMBRETABLA+" where "+PysTiposProductosBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto()+") CATEGORIA";
					where +=" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto();
					
				}else{
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTiposProductosBean.T_NOMBRETABLA+" where "+PysTiposProductosBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+") CATEGORIA";
					where +=" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO;
					
				}
				if (form.getCategoriaProducto()!=null && !form.getCategoriaProducto().equalsIgnoreCase("")){
					select+=", (select "+PysProductosBean.C_DESCRIPCION+"  from "+PysProductosBean.T_NOMBRETABLA+" where "+PysProductosBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto()+
					        " AND "+PysProductosBean.C_IDPRODUCTO+" = "+form.getCategoriaProducto()+
							" AND "+PysProductosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
					where +=" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+" = "+form.getCategoriaProducto();
				}else{
					select+=", (select "+PysProductosBean.C_DESCRIPCION+"  from "+PysProductosBean.T_NOMBRETABLA+" where "+PysProductosBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+
			        " AND "+PysProductosBean.C_IDPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDPRODUCTO+
					" AND "+PysProductosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
				}
				
				if (form.getProducto()!=null && !String.valueOf(form.getProducto()).equalsIgnoreCase("0")){
					where +=" AND  "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+" = "+form.getProducto();
				}else{
					 if (form.getNombreProducto()!=null && !form.getNombreProducto().equalsIgnoreCase("") ){
					 	where +=" AND "+ ComodinBusquedas.prepararSentenciaCompleta(form.getNombreProducto().trim(),PysProductosInstitucionBean.C_DESCRIPCION);
					 }
					
				}
				if (form.getConcepto().equalsIgnoreCase("Producto")){//producto
				where+=" and (TIPOCERTIFICADO IS NULL) AND FECHABAJA IS NULL";
				consulta=select+" from "+PysProductosInstitucionBean.T_NOMBRETABLA+where;
				}else{//certificado
					where+=" and ((TIPOCERTIFICADO <> 'D' AND TIPOCERTIFICADO <> 'M' AND TIPOCERTIFICADO <> 'B')) AND FECHABAJA IS NULL";
					consulta=select+" from "+PysProductosInstitucionBean.T_NOMBRETABLA+where;
				}	
					
				
					
				
			}else{//servicios
				select =" SELECT "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" IDTIPOPRODUCTO, "+PysServiciosInstitucionBean.C_IDSERVICIO+" IDPRODUCTO, "+PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION+" IDPRODUCTOINSTITUCION, "+PysServiciosInstitucionBean.C_IDINSTITUCION+", "+PysServiciosInstitucionBean.C_DESCRIPCION;
				where=" WHERE "+PysServiciosInstitucionBean.C_IDINSTITUCION+" = "+form.getIdInstitucion();	
				if (form.getTipoServicio()!=null && form.getTipoServicio().intValue()!=0 ){
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTipoServiciosBean.T_NOMBRETABLA+" where "+PysTipoServiciosBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio()+") CATEGORIA";
					where +=" AND "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio();
					
				}else{
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTipoServiciosBean.T_NOMBRETABLA+" where "+PysTipoServiciosBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+") CATEGORIA";
					where +=" AND "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS;
					
				}
				if (form.getCategoriaServicio()!=null && !form.getCategoriaServicio().equalsIgnoreCase("")){
					select+=", (select "+PysServiciosBean.C_DESCRIPCION+"  from "+PysServiciosBean.T_NOMBRETABLA+" where "+PysServiciosBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio()+
					        " AND "+PysServiciosBean.C_IDSERVICIO+" = "+form.getCategoriaServicio()+
							" AND "+PysServiciosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
					where +=" AND "+PysServiciosInstitucionBean.C_IDSERVICIO+" = "+form.getCategoriaServicio();
				}else{
					select+=", (select "+PysServiciosBean.C_DESCRIPCION+"  from "+PysServiciosBean.T_NOMBRETABLA+" where "+PysServiciosBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+
			        " AND "+PysServiciosBean.C_IDSERVICIO+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDSERVICIO+
					" AND "+PysServiciosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
				}
				
				if (form.getServicio()!=null && !String.valueOf(form.getServicio()).equalsIgnoreCase("0")){
					where +=" AND  "+PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION+" = "+form.getServicio();
				}else{
					 if (form.getNombreProducto()!=null && !form.getNombreProducto().equalsIgnoreCase("")){
					 	where +=" AND  "+ComodinBusquedas.prepararSentenciaCompleta(form.getNombreProducto().trim(),PysServiciosInstitucionBean.C_DESCRIPCION);
					 }
					
				}
				
				where+=" and AUTOMATICO = 0 AND FECHABAJA IS NULL";
				consulta=select+" from "+PysServiciosInstitucionBean.T_NOMBRETABLA+where;
				
			}
		 }else{//es letrado
		 	if (!form.getConcepto().equalsIgnoreCase("Servicio") ){//Productos y Certificados
				select =" SELECT "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" IDTIPOPRODUCTO, "+PysProductosInstitucionBean.C_IDPRODUCTO+" IDPRODUCTO, "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+" IDPRODUCTOINSTITUCION, "+PysProductosInstitucionBean.C_IDINSTITUCION+", "+PysProductosInstitucionBean.C_DESCRIPCION;
				where=" WHERE "+PysProductosSolicitadosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion();	
				if (form.getTipoProducto()!=null && form.getTipoProducto().intValue()!=0 ){
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTiposProductosBean.T_NOMBRETABLA+" where "+PysTiposProductosBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto()+") CATEGORIA";
					where +=" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto();
					
				}else{
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTiposProductosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTiposProductosBean.T_NOMBRETABLA+" where "+PysTiposProductosBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+") CATEGORIA";
					where +=" AND "+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO;
					
				}
				if (form.getCategoriaProducto()!=null && !form.getCategoriaProducto().equalsIgnoreCase("")){
					select+=", (select "+PysProductosBean.C_DESCRIPCION+"  from "+PysProductosBean.T_NOMBRETABLA+" where "+PysProductosBean.C_IDTIPOPRODUCTO+" = "+form.getTipoProducto()+
					        " AND "+PysProductosBean.C_IDPRODUCTO+" = "+form.getCategoriaProducto()+
							" AND "+PysProductosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
					where +=" AND "+PysProductosInstitucionBean.C_IDPRODUCTO+" = "+form.getCategoriaProducto();
				}else{
					select+=", (select "+PysProductosBean.C_DESCRIPCION+"  from "+PysProductosBean.T_NOMBRETABLA+" where "+PysProductosBean.C_IDTIPOPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDTIPOPRODUCTO+
			        " AND "+PysProductosBean.C_IDPRODUCTO+" = "+PysProductosInstitucionBean.T_NOMBRETABLA+"."+PysProductosInstitucionBean.C_IDPRODUCTO+
					" AND "+PysProductosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
				}
				
				if (form.getProducto()!=null && !String.valueOf(form.getProducto()).equalsIgnoreCase("0")){
					where +=" AND  "+PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION+" = "+form.getProducto();
				}else{
					 if (form.getNombreProducto()!=null && !form.getNombreProducto().equalsIgnoreCase("")){
					 	where +=" AND  "+ComodinBusquedas.prepararSentenciaCompleta(form.getNombreProducto().trim(),PysProductosInstitucionBean.C_DESCRIPCION);
					 }
					
				}
				if (form.getConcepto().equalsIgnoreCase("Producto")){
				where+=" and SOLICITARALTA = 1 and (TIPOCERTIFICADO IS NULL) AND FECHABAJA IS NULL";
				consulta=select+" from "+PysProductosInstitucionBean.T_NOMBRETABLA+where;
				}else{
					where+=" and SOLICITARALTA = 1 and ((TIPOCERTIFICADO <> 'D' AND TIPOCERTIFICADO <> 'M' AND TIPOCERTIFICADO <> 'B')) AND FECHABAJA IS NULL";
					consulta=select+" from "+PysProductosInstitucionBean.T_NOMBRETABLA+where;
				}	
					
				
					
				
			}else{//Servicios
				select =" SELECT "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" IDTIPOPRODUCTO, "+PysServiciosInstitucionBean.C_IDSERVICIO+" IDPRODUCTO, "+PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION+" IDPRODUCTOINSTITUCION, "+PysServiciosInstitucionBean.C_IDINSTITUCION+", "+PysServiciosInstitucionBean.C_DESCRIPCION;
				where=" WHERE "+PysServiciosInstitucionBean.C_IDINSTITUCION+" = "+form.getIdInstitucion();	
				if (form.getTipoServicio()!=null && form.getTipoServicio().intValue()!=0 ){
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTipoServiciosBean.T_NOMBRETABLA+" where "+PysTipoServiciosBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio()+") CATEGORIA";
					where +=" AND "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio();
					
				}else{
					select+=", (select "+UtilidadesMultidioma.getCampoMultidiomaSimple(PysTipoServiciosBean.C_DESCRIPCION,user.getLanguage())+"  from "+PysTipoServiciosBean.T_NOMBRETABLA+" where "+PysTipoServiciosBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+") CATEGORIA";
					where +=" AND "+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS;
					
				}
				if (form.getCategoriaServicio()!=null && !form.getCategoriaServicio().equalsIgnoreCase("")){
					select+=", (select "+PysServiciosBean.C_DESCRIPCION+"  from "+PysServiciosBean.T_NOMBRETABLA+" where "+PysServiciosBean.C_IDTIPOSERVICIOS+" = "+form.getTipoServicio()+
					        " AND "+PysServiciosBean.C_IDSERVICIO+" = "+form.getCategoriaServicio()+
							" AND "+PysServiciosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
					where +=" AND "+PysServiciosInstitucionBean.C_IDSERVICIO+" = "+form.getCategoriaServicio();
				}else{
					select+=", (select "+PysServiciosBean.C_DESCRIPCION+"  from "+PysServiciosBean.T_NOMBRETABLA+" where "+PysServiciosBean.C_IDTIPOSERVICIOS+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDTIPOSERVICIOS+
			        " AND "+PysServiciosBean.C_IDSERVICIO+" = "+PysServiciosInstitucionBean.T_NOMBRETABLA+"."+PysServiciosInstitucionBean.C_IDSERVICIO+
					" AND "+PysServiciosBean.C_IDINSTITUCION+" = "+form.getIdInstitucion()+") TIPO";
				}
				
				if (form.getServicio()!=null && !String.valueOf(form.getServicio()).equalsIgnoreCase("0")){
					where +=" AND  "+PysServiciosInstitucionBean.C_IDSERVICIOSINSTITUCION+" = "+form.getServicio();
				}else{
					 if (form.getNombreProducto()!=null && !form.getNombreProducto().equals("")){
					 	where +=" AND  "+ComodinBusquedas.prepararSentenciaCompleta(form.getNombreProducto().trim(),PysServiciosInstitucionBean.C_DESCRIPCION);
					 }
					
				}
				
				where+=" and SOLICITARALTA = 1 and AUTOMATICO = 0 AND FECHABAJA IS NULL";
				consulta=select+" from "+PysServiciosInstitucionBean.T_NOMBRETABLA+where;
				
			}
		  }
		 
			
		 	
		 
			
			
			RowsContainer rc=null;
			rc = new RowsContainer();
		
			if (rc.findNLS(consulta)) {
					
				   for (int i = 0; i < rc.size(); i++){
					 Row fila = (Row) rc.get(i);
	                  resultado.add(fila);
	                  
	               }
				   
			}	
			
			
		
			// subimos a sesion el resultado de la consulta porque si se cambia de cliente se quiere seguir manteniendo los resultados obtenidos
			request.getSession().setAttribute("resultBusqueda",resultado);
			request.getSession().setAttribute("auxSolicitudCompraForm",form);
				
			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		}		
		return "buscarProducto";		
	}
	
	/**
	 * Recoge los datos de pantalla y rellena los articulos 
	 * @param  request - objeto llamada HTTP 
	 * @param i - identificador del registro de la tabla que vamos a modificar
	 * @return  Articulo  Articulo modificado
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected Articulo getOcultos(HttpServletRequest request, int i) throws SIGAException {
		Integer idTipoArticulo;
		Long idArticulo, idArticuloInstitucion;
		int claseArticulo;
		String idInstitucion = "", idPersona;
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		CenCuentasBancariasAdm cb = new CenCuentasBancariasAdm(user); 
		
		CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);	
		Articulo a ;
		try{
			idTipoArticulo 			= Integer.valueOf(request.getParameter("oculto"+i+"_1"));
			idArticulo 				= Long.valueOf(request.getParameter("oculto"+i+"_2"));
			idArticuloInstitucion 	= Long.valueOf(request.getParameter("oculto"+i+"_3"));
			claseArticulo			= Integer.parseInt(request.getParameter("oculto"+i+"_4"));				
			idInstitucion			= carro.getIdinstitucion().toString();
			idPersona 				= carro.getIdPersona().toString();
			
			if(claseArticulo == Articulo.CLASE_PRODUCTO){
				a=carro.buscarArticuloProducto(idArticulo, idArticuloInstitucion, idTipoArticulo);

				/////////////////////////////
				// INC 4081  Pq se puede modificr el precio de los productos
				String aux = UtilidadesNumero.formatoCampo((String)request.getParameter("precio" + i));
				Double precio = new Double (aux.replaceAll(",","."));				
				a.setPrecio(precio);
				/////////////////////////////
			} else{
				a=carro.buscarArticuloServicio(idArticulo, idArticuloInstitucion, idTipoArticulo);
			}
			
			if((request.getParameter("oculto"+i+"_5")!= null)&& !request.getParameter("oculto"+i+"_5").equals("")){
				a.setIdCuenta(Integer.valueOf(request.getParameter("oculto"+i+"_5")));
			}
			if((request.getParameter("formaPago"+i)!= null)&& !request.getParameter("formaPago"+i).equals("")){
				a.setIdFormaPago(Integer.valueOf(request.getParameter("formaPago"+i)));
			}	
			if((request.getParameter("cantidad"+i)!= null) && !request.getParameter("cantidad"+i).equals("")){
				a.setCantidad(Integer.parseInt(request.getParameter("cantidad"+i).trim()));
			}
			if((request.getParameter("fechaEfectivaCompra"+i)!= null) && !request.getParameter("fechaEfectivaCompra"+i).equals("")){
				a.setFechaEfectiva(request.getParameter("fechaEfectivaCompra"+i).trim());
			}
			
			a.setFormaPago((String)request.getParameter("oculto"+i+"_6"));
			if(request.getParameter("cuenta"+i) != null && !request.getParameter("cuenta"+i).equals("")){
				a.setIdCuenta(Integer.parseInt(request.getParameter("cuenta"+i).trim()));
				String numeroCuenta = cb.getNumeroCuentaCompra(idInstitucion,idPersona,(String)request.getParameter("cuenta"+i));
				a.setNumeroCuenta(numeroCuenta);	
			}
			// Anhadido para productos de Certificacion			
			if((request.getParameter("certificado"+i+"_1")!= null)&& !request.getParameter("certificado"+i+"_1").equals("")){
				a.setTipoCertificado((String)request.getParameter("certificado"+i+"_1"));
				if((request.getParameter("certificado"+i+"_2")!= null)&& !request.getParameter("certificado"+i+"_2").equals("")){
					a.setIdTipoEnvios(Integer.valueOf(request.getParameter("certificado"+i+"_2")));
				}
				if((request.getParameter("certificado"+i+"_3")!= null)&& !request.getParameter("certificado"+i+"_3").equals("")){
					a.setIdDireccion(Long.valueOf(request.getParameter("certificado"+i+"_3")));
				}
			}			
			return a;
			
		}catch (Exception e) { 
			   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
			   
		} 	
		return null;
	}

	/** 
	 * Atiende la accion confirmarCompra. Lleva a la pantalla de Desglose Pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String confirmarCompra(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		String modo = "desglosePago";
		CarroCompra carro;		
		double varPrecioTotalTarjeta=0, precio=0, importe=0, iva=0, varIvaTotalTarjeta=0;
		Double subtotal;
		Articulo articulo;
		int claseArticulo;
		String pagoConTarjeta = "N";
		String importeDecimal="", importeEntero="";
		String claveTipo;
		SolicitudCompraForm form =  (SolicitudCompraForm) formulario;
		
		try {
			//Datos del carro de la compra:
			carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);	
			ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();
			
			if (arrayListaArticulosOrdenada.size()==0) { 
				modo = exito("error",request);
			} else {			
				// preparando tabla para controlar que el subtotales por cada tipo de un importe positivo
				// control exclusivo para evitar totales negativos ya que se han de permitir cantidades negativas
				Hashtable<String, Double> subtotalesPorTipo = new Hashtable<String, Double>();
				
				//Recorremos los articulos del carro para calcular el importe total del pago con tarjeta:
				//Tambien vamos actualizando el carro con los datos que me faltan.
				for(int i=1; i < arrayListaArticulosOrdenada.size()+1; i++){
					//Recupero el articulo con los datos de la pagina y lo almaceno en el carro:
					articulo = getOcultos(request, i);					
					claseArticulo = Integer.parseInt(request.getParameter("oculto"+i+"_4"));
					
					//FORMA DE PAGO CON TARJETA:
					if(articulo.getIdFormaPago() != null && articulo.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_TARJETA){
						pagoConTarjeta = "S";
						//Calculo el importe TOTAL del pago con Tarjeta:
						precio = articulo.getPrecio().doubleValue();
						iva = articulo.getValorIva().doubleValue();
						varIvaTotalTarjeta = (double)articulo.getCantidad() * ((precio / 100)) * iva;
						varPrecioTotalTarjeta = (double)articulo.getCantidad() * precio;
						importe += varIvaTotalTarjeta + varPrecioTotalTarjeta;
						if (claseArticulo == Articulo.CLASE_PRODUCTO) {
							carro.actualizarCantidadProducto(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getCantidad());					
						} else {
							carro.actualizarCantidadServicio(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getCantidad());
						}
					//FORMA DE PAGO CON CUENTA:
					} else {
						if (claseArticulo == Articulo.CLASE_PRODUCTO) {
							carro.actualizarCantidadProducto(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getCantidad());
							carro.actualizarFormaPagoProducto(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getIdFormaPago());
							carro.actualizarNCuentaProducto(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getNumeroCuenta());
						} else { 
							carro.actualizarCantidadServicio(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getCantidad());
							carro.actualizarFormaPagoServicio(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getIdFormaPago());
							carro.actualizarNCuentaServicio(articulo.getIdArticulo(),articulo.getIdArticuloInstitucion(),articulo.getIdTipo(),articulo.getNumeroCuenta());
						}					
					}
					
					// acumulando subtotales por tipo
					claveTipo = articulo.getIdTipo()+"_"+articulo.getIdArticulo()+"_"+articulo.getIdInstitucion()+"_"+articulo.getIdFormaPago();
					if ((subtotal = subtotalesPorTipo.get(claveTipo)) == null)
						subtotal = new Double(0);
					subtotal += articulo.getCantidad() * articulo.getPrecio() * (1 + iva/100);
					subtotalesPorTipo.put(claveTipo, subtotal);
				}
				
				// comprobando que los subtotales por tipo tienen importe positivo
				Enumeration<Double> enuSubtotalesPorTipo = subtotalesPorTipo.elements();
				while (enuSubtotalesPorTipo.hasMoreElements()) {
					subtotal = enuSubtotalesPorTipo.nextElement();
					if (subtotal < 0) { 
						request.setAttribute("textoError", "1");
						return continuar(mapping, formulario, request, response);
					}
				}
				
				if (pagoConTarjeta.equals("S")) {
					//Redondeo el importe:
					importe = UtilidadesNumero.redondea(importe,2);
					
					//FORMATEO DEL IMPORTE: calculo la parte entera y decimal del importe:
			    	importeEntero = String.valueOf(importe).substring(0,String.valueOf(importe).indexOf("."));
			    	importeDecimal = String.valueOf(importe).substring(String.valueOf(importe).indexOf(".")+1,String.valueOf(importe).length());
			    	if (importeDecimal.length() == 1)
			    		importeDecimal = String.valueOf(importe).substring(String.valueOf(importe).indexOf(".")+1,String.valueOf(importe).indexOf(".")+2)+"0";
			    	else			    				    		
			    		importeDecimal = String.valueOf(importe).substring(String.valueOf(importe).indexOf(".")+1,String.valueOf(importe).indexOf(".")+3);

					//Guardo el importe total de pago con tarjeta en el carro:
					carro.setImporte(importeEntero+importeDecimal);
				}
		    	
		    	//Envio si hay pago con tarjeta	    	
		    	request.setAttribute("PAGOTARJETA",pagoConTarjeta);
		    	
		    	//ALmaceno el nombre del comprador
		    	request.setAttribute("nombrePersona",form.getNombrePersona());
				
		    	//Almaceno el carro en sesion:				
				request.getSession().setAttribute(CarroCompraAdm.nombreCarro,carro);
				
				request.getSession().setAttribute("CenBusquedaClientesTipo","VC");
			}
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 	
		return modo;	
	}

	/** 
	 * Atiende la accion pagarConTarjeta. Lleva a la pantalla de Pago con el TPV.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String pagarConTarjeta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudCompraForm form = (SolicitudCompraForm) formulario;
		GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request)); 		
		
		String modo = "cargaTarjeta";
		UsrBean user = null;		
		CarroCompra carro;		 
		String urlTPV=null, merchantId=null, acquireBin=null, terminalId=null, operacion=null, fechaActual=null;
		String idInstitucion=null, idPersona=null, passwordFirma=null;
		
		try {
			user=(UsrBean)request.getSession().getAttribute("USRBEAN");							
			
			//Datos del carro de la compra:
			carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);				
			
			//Almaceno el usrBean en el carro
			carro.setUsrBean(user);
			
			//Obtengo el numero de operacion. El codigo 1 es usado para Facturacion de Productos y Servicios:
			idInstitucion = rellenarConCeros(carro.getIdinstitucion().toString(),4); 
			idPersona = rellenarConCeros(carro.getIdPersona().toString(),10);
			fechaActual = String.valueOf(Calendar.getInstance().getTimeInMillis());;
			operacion = "1"+idInstitucion+idPersona+fechaActual;			
			carro.setNumOperacion(operacion);
			request.setAttribute("OPERACION",operacion);
			request.setAttribute("IMPORTE",carro.getImporte());
			//Almaceno el carro en variable de Aplicacion (contexto) identificada con el numero de operacion:
			this.servlet.getServletContext().setAttribute(operacion,carro);

			urlTPV = parametrosAdm.getValor(idInstitucion.toString(),"PYS",ClsConstants.URL_TPV,null);
			merchantId = parametrosAdm.getValor(user.getLocation().toString(),"PYS",ClsConstants.MERCHANTID,null);
			acquireBin = parametrosAdm.getValor(user.getLocation().toString(),"PYS",ClsConstants.ACQUIREBIN,null);
			terminalId = parametrosAdm.getValor(user.getLocation().toString(),"PYS",ClsConstants.TERMINALID,null);
			passwordFirma = parametrosAdm.getValor(user.getLocation().toString(),"PYS",ClsConstants.PASSWORD_FIRMA,null);
			// Modificado por CVG
			request.setAttribute(ClsConstants.URL_TPV,urlTPV);
			request.setAttribute(ClsConstants.MERCHANTID,merchantId);
			request.setAttribute(ClsConstants.ACQUIREBIN,acquireBin);
			request.setAttribute(ClsConstants.TERMINALID,terminalId);
			request.setAttribute(ClsConstants.PASSWORD_FIRMA,passwordFirma);
			
			//Almaceno en el request los datos de la tarjeta:
			request.setAttribute("PAN",form.getPan());
			request.setAttribute("CADUCIDAD",form.getFechaCaducidad());
			request.setAttribute("EXPONENTE","2");
			request.setAttribute("MONEDA","978");
			//Calculo la ruta del dominio del servidor:
			StringBuffer ruta = request.getRequestURL();
			String sRuta = ruta.substring(0,ruta.lastIndexOf(("/"))+1); 
			request.setAttribute("URLOK",sRuta+"PYS_GenerarSolicitudes.do?modo=okCompraTPV&resultado=OK");
			request.setAttribute("URLKO",sRuta+"PYS_GenerarSolicitudes.do?modo=okCompraTPV&resultado=ERROR");			
			request.setAttribute("DESCRIPCION","Compra SIGA - TPV");
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 	
		return modo;	
	}

	/** 
	 * Atiende la accion okCompraTPV. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String okCompraTPV(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));  		
		
		String modo = "comprobanteCompra";
		UsrBean user = null;		
		CarroCompra carro;		 
		String where = null;
		String operacion=null, operacionOK=null;
		boolean error = false;
		
		try {
			user=(UsrBean)request.getSession().getAttribute("USRBEAN");							

			//Datos del carro de la compra:
			carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);				
			
			//Chequeo si operacionOK vale OK o ERROR:
			operacionOK = request.getParameter("resultado");
			if (operacionOK==null || operacionOK.equals("ERROR")) 
				error = true;
			else {
				//Chequeo si el carro tiene el NUM_OPERACION:
				if ((carro==null) || (carro.getNumOperacion()==null)) 
					error = true;
			}
			
			if (error) request.setAttribute("error","SI");
			else
			{
				//Obtengo los datos por el NUM_OPERACION del carro:
				operacion = carro.getNumOperacion();
				
				//Busco la peticion en Base de Datos por el NUM_OPERACION del carro:
				where  = "WHERE "+PysPeticionCompraSuscripcionBean.C_NUM_OPERACION+"='"+operacion+"'";
				Vector peticion = ppcsa.select(where); 
				if (peticion.size() == 0)
					request.setAttribute("error","SI");
				else {
					GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
					String aprobarSolicitud=parametrosAdm.getValor(user.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
					
					//se asigna el idpeticion al carro de la compra
					PysPeticionCompraSuscripcionBean peticionBean = (PysPeticionCompraSuscripcionBean) peticion.get(0);
					carro.setIdPeticion(peticionBean.getIdPeticion());

					if(!user.isLetrado()&&aprobarSolicitud.equals("S")){
						//fechaEfectiva=form.getFechaEfectivaCompra();
						GestionSolicitudesAction gestionSolicitudes=new GestionSolicitudesAction();
						ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();
						Articulo a=null;
			
						if (arrayListaArticulosOrdenada.size()==0){ 
							modo = exito("error",request);
						}
						else {			
							//Recorremos los articulos del carro para confirmar la compra de cada uno de ellos	
							//Asigna a cada art�culo el id petici�n
							for(int i=0; i<arrayListaArticulosOrdenada.size(); i++){
								a=(Articulo)arrayListaArticulosOrdenada.get(i);
								a.setIdPeticion(peticionBean.getIdPeticion());
								//request.setAttribute("FECHAEFECTIVA",fechaEfectiva);
								request.setAttribute("ARTICULO",a);
								if(a.getTipoCertificado()==null||a.getTipoCertificado()==""){ //Si no es certificado
									gestionSolicitudes.confirmar(mapping,formulario,request,response);
									
									//si se confirma la compra, se mostrar� el bot�n de anticipar 
									//si el art�culo tiene como forma de pago "EN METALICO"
									if (a.getIdFormaPago() != null && a.getIdFormaPago().intValue() == 30){
										a.setAnticipar(Boolean.TRUE);
										a.setImporteAnticipado(new Double(0));												
									}
									//Si la compra es por tarjeta, se habr� anticipado todo el importe
									if (a.getIdFormaPago() != null && a.getIdFormaPago().intValue() == 10){
										String tipo = Articulo.CLASE_PRODUCTO == a.getClaseArticulo() ? "P" : "S";
										if ("P".equals(tipo)){
											PysCompraAdm compraAdm = new PysCompraAdm(user);
											Hashtable hash = new Hashtable();
											UtilidadesHash.set(hash,PysCompraBean.C_IDINSTITUCION,a.getIdInstitucion());
											UtilidadesHash.set(hash,PysCompraBean.C_IDPETICION,a.getIdPeticion());
											UtilidadesHash.set(hash,PysCompraBean.C_IDTIPOPRODUCTO,a.getIdTipo());
											UtilidadesHash.set(hash,PysCompraBean.C_IDPRODUCTO,a.getIdArticulo());
											UtilidadesHash.set(hash,PysCompraBean.C_IDPRODUCTOINSTITUCION,a.getIdArticuloInstitucion());
											Vector compra = compraAdm.selectByPK(hash);
											if (compra!=null && compra.size()>0){
												PysCompraBean compraBean=(PysCompraBean)compra.get(0);
												a.setImporteAnticipado(compraBean.getImporteAnticipado());	
											}
										}
										else if ("S".equals(tipo)){
											PysSuscripcionAdm suscripcionAdm = new PysSuscripcionAdm(user);
											Hashtable hash = new Hashtable();
											UtilidadesHash.set(hash,PysSuscripcionBean.C_IDINSTITUCION,a.getIdInstitucion());
											UtilidadesHash.set(hash,PysSuscripcionBean.C_IDPETICION,a.getIdPeticion());
											UtilidadesHash.set(hash,PysSuscripcionBean.C_IDTIPOSERVICIOS,a.getIdTipo());
											UtilidadesHash.set(hash,PysSuscripcionBean.C_IDSERVICIO,a.getIdArticulo());
											UtilidadesHash.set(hash,PysSuscripcionBean.C_IDSERVICIOSINSTITUCION,a.getIdArticuloInstitucion());
											Vector suscripcion = suscripcionAdm.select(hash);
											if (suscripcion!=null && suscripcion.size()>0){
												PysSuscripcionBean suscripcionBean=(PysSuscripcionBean)suscripcion.get(0);
												a.setImporteAnticipado(suscripcionBean.getImporteAnticipado());	
											}
										}
									}
									
								}
							}
						}
					}
					
					//Preparamos los productos y servicios del comprobante:
					request.setAttribute("resultados",this.prepararComprobante(carro,request));					

					//Eliminamos: variable del contexto y el carro de sesion
					this.servlet.getServletContext().removeAttribute(operacion);
					//El atributo CarroCompra se utiliza de nuevo para la impresion
					// por lo que no se puede borrar aqui -> se traslada a imprimirCompra()
					//request.getSession().removeAttribute(CarroCompraAdm.nombreCarro);
				}
			}
		} catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 	
		return modo;	
	}
	
	protected String guardarCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;
			CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(), form.getIdInstitucionPresentador(), request);
		
			ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
				for(int i=1; i < arrayListaArticulosOrdenada.size()+1; i++){											
					Articulo a = getOcultos(request, i);			
				}		
			
			CarroCompraAdm.setCarroCompra(carro, request);			
			request.setAttribute("idInstitucion", form.getIdInstitucion());
			request.setAttribute("idInstitucionPresentador", form.getIdInstitucionPresentador());
			request.setAttribute("nombrePersona", request.getParameter("nombrePersona"));
			request.setAttribute("numero", request.getParameter("numeroColegiado"));
			request.setAttribute("nif", request.getParameter("nif"));
			request.setAttribute("existeCarro", "S");
		
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		  
		return "continuar";
	}
	
	protected String consultarCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try{
			
			SolicitudCompraForm form = (SolicitudCompraForm) formulario;			
			CarroCompra carro = CarroCompraAdm.getCarroCompra(form.getIdPersona(), form.getIdInstitucion(), form.getIdInstitucionPresentador(), request);
			CenDireccionesAdm direccionesAdm = new CenDireccionesAdm(this.getUserBean(request));
			EnvTipoEnviosAdm tipoEnvioAdm = new EnvTipoEnviosAdm(this.getUserBean(request));
			
			Vector certificado = new Vector();
			certificado = (Vector)form.getDatosTablaOcultos(0);	
			String idTipoEnvio 	= "";
			String idDireccion  = "";
	//		Integer idTipoEnvio = Integer.valueOf((String)certificado.elementAt(1));
	//		Long 	idDireccion = Long.valueOf((String)certificado.elementAt(2));
			
			String 	tipoEnvio 	= "";
			Hashtable hash 		= new Hashtable();
			if(certificado.size()>1){
				idTipoEnvio 	= (String)certificado.elementAt(1);
				idDireccion  	= (String)certificado.elementAt(2);
				String	sWhere		= " where " + EnvTipoEnviosBean.C_IDTIPOENVIOS + " = " + idTipoEnvio;
				Vector 	vTipoEnvio	= tipoEnvioAdm.select(sWhere);
				if (vTipoEnvio.size() > 0){
					Enumeration en 		= vTipoEnvio.elements();
					EnvTipoEnviosBean bean = (EnvTipoEnviosBean)en.nextElement();
					tipoEnvio 	= bean.getNombre();
				}				
				hash = direccionesAdm.selectDirecciones(form.getIdPersona(),form.getIdInstitucion(),Long.valueOf(idDireccion));			
			}
			
			request.getSession().setAttribute("DATABACKUP", hash);
			request.setAttribute("idTipoEnvio", tipoEnvio);	
		
		}
		catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		  
		return "consultarCertificado";
	}
			
	
	
	private String rellenarConCeros(String arg, int longitud){
		String salida = null;
		
		salida = arg;
		while (salida.length() < longitud) {
			salida = "0"+salida;
		}
		return salida; 
	}
	
	private Hashtable prepararComprobante(CarroCompra carro, HttpServletRequest request) throws SIGAException, ClsExceptions {
		PysPeticionCompraSuscripcionAdm ppcsa = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));
		PysPeticionCompraSuscripcionBean peticionBean = new PysPeticionCompraSuscripcionBean();
		CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
		CenPersonaBean personaBean = new CenPersonaBean(); 		
		CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		UsrBean user;
		
		Hashtable hash = new Hashtable();
		Hashtable registros = new Hashtable();
		Articulo articulo;
		int claseArticulo;
		String where = null;
		
		user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//IdPersona:
		registros.put("idPersona",carro.getIdPersona());
		
		//Obtengo el idPeticion y la fecha:		
		where = "WHERE "+PysPeticionCompraSuscripcionBean.C_NUM_OPERACION+"='"+carro.getNumOperacion()+"'";
		where += " AND "+PysPeticionCompraSuscripcionBean.C_IDINSTITUCION+"="+carro.getIdinstitucion();		
		peticionBean = (PysPeticionCompraSuscripcionBean)(ppcsa.select(where).get(0));		
		registros.put("idPeticion",peticionBean.getIdPeticion());
		registros.put("fecha",GstDate.getFormatedDateShort(user.getLanguage(),peticionBean.getFecha()));
			
		//Obtengo el nombre, nif:
		where = "WHERE "+CenPersonaBean.C_IDPERSONA+"="+carro.getIdPersona();			
		personaBean = (CenPersonaBean)(personaAdm.select(where).get(0));		
		registros.put("nombrePersona",personaBean.getNombre()+" "+personaBean.getApellido1()+" "+personaBean.getApellido2());		
		registros.put("nif",personaBean.getNIFCIF());		
		
		//Obtengo el numero de colegiado de la persona
		CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(carro.getIdPersona(), carro.getIdinstitucion());			
		registros.put("numeroColegiado", colegiadoAdm.getIdentificadorColegiado(bean));
	
		//Datos del carro de la compra:			
		ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();
		Vector vListaPyS = new Vector();
		
		for(int i=0; i < arrayListaArticulosOrdenada.size(); i++){
			articulo = (Articulo)(arrayListaArticulosOrdenada.get(i)); 
			claseArticulo = articulo.getClaseArticulo();
						
			if (claseArticulo == Articulo.CLASE_PRODUCTO) { 				
				hash = new Hashtable();
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, articulo.getIdInstitucion());	
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, articulo.getIdTipo());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, articulo.getIdArticulo());			
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, articulo.getIdArticuloInstitucion());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, peticionBean.getIdPeticion());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPERSONA, carro.getIdPersona());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, articulo.getIdCuenta());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, articulo.getIdFormaPago());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA, articulo.getIdIva());
				UtilidadesHash.set(hash, "VALORIVA", articulo.getValorIva());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, new Integer(articulo.getCantidad()));
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, articulo.getPrecio());
				UtilidadesHash.set(hash, "ANTICIPAR", articulo.getAnticipar());
				UtilidadesHash.set(hash, "IMPORTEANTICIPADO", articulo.getImporteAnticipado());
				//Otros datos:
				PysFormaPagoAdm formaPagoAdm = new PysFormaPagoAdm(this.getUserBean(request));
			     Hashtable hFormaPago =new Hashtable();
			     UtilidadesHash.set(hFormaPago,PysFormaPagoBean.C_IDFORMAPAGO,articulo.getIdFormaPago());
			     if (hFormaPago.get(PysFormaPagoBean.C_IDFORMAPAGO)!=null){
			         Vector v_formaPago=formaPagoAdm.selectByPK(hFormaPago);
				     if (v_formaPago!=null && v_formaPago.size()>0){
				       PysFormaPagoBean formaPagoBean=(PysFormaPagoBean)v_formaPago.get(0);
				       UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", UtilidadesMultidioma.getDatoMaestroIdioma(formaPagoBean.getDescripcion(),this.getUserBean(request)));	
				     }
			     }else{
			     	UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", UtilidadesString.getMensajeIdioma(this.getUserBean(request),"estados.compra.noFacturable"));
			     }
				UtilidadesHash.set(hash, "DESCRIPCION_ARTICULO", articulo.getIdArticuloInstitucionDescripcion());	
				//UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", articulo.getFormaPago());	
				UtilidadesHash.set(hash, "DESCRIPCION_CUENTA", articulo.getNumeroCuenta());
				
			 } else {				
			 	hash = new Hashtable();
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION , articulo.getIdInstitucion());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS , articulo.getIdTipo());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIO , articulo.getIdArticulo());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, articulo.getIdArticuloInstitucion());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPETICION, peticionBean.getIdPeticion());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERSONA, carro.getIdPersona());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDCUENTA, articulo.getIdCuenta());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO, articulo.getIdFormaPago());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_CANTIDAD , new Integer(articulo.getCantidad()));
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_ACEPTADO , ClsConstants.PRODUCTO_PENDIENTE);
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERIODICIDAD , articulo.getIdPeriodicidad());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS , articulo.getIdPrecios());
				UtilidadesHash.set(hash, "ANTICIPAR", articulo.getAnticipar());
				UtilidadesHash.set(hash, "IMPORTEANTICIPADO", articulo.getImporteAnticipado());

				// MAV 24/08/2005 Obtenci�n del IVA para mostrar en comprobante
				UtilidadesHash.set(hash, PysServiciosInstitucionBean.C_PORCENTAJEIVA , articulo.getIdIva());
				UtilidadesHash.set(hash, "VALORIVA", articulo.getValorIva());
				//Otros datos:
				UtilidadesHash.set(hash, "DESCRIPCION_ARTICULO", articulo.getIdArticuloInstitucionDescripcion());
				
				UtilidadesHash.set(hash, "SERVICIO_DESCRIPCION_PRECIO", articulo.getDescripcionPrecio());
				
				PysFormaPagoAdm formaPagoAdm = new PysFormaPagoAdm(this.getUserBean(request));
			     Hashtable hFormaPago =new Hashtable();
			     hFormaPago.put(PysFormaPagoBean.C_IDFORMAPAGO,articulo.getIdFormaPago());
			     Vector v_formaPago=formaPagoAdm.selectByPK(hFormaPago);
			     if (v_formaPago!=null && v_formaPago.size()>0){
			       PysFormaPagoBean formaPagoBean=(PysFormaPagoBean)v_formaPago.get(0);
			       UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", UtilidadesMultidioma.getDatoMaestroIdioma(formaPagoBean.getDescripcion(),this.getUserBean(request)));	
			     }
				//UtilidadesHash.set(hash, "DESCRIPCION_FORMAPAGO", articulo.getFormaPago());	
				UtilidadesHash.set(hash, "DESCRIPCION_CUENTA", articulo.getNumeroCuenta());									     
				UtilidadesHash.set(hash, "PRECIOSSERVICIOS", articulo.getPrecio()); 
				UtilidadesHash.set(hash, "IVA" , articulo.getIdIva());		
				UtilidadesHash.set(hash, "PERIODICIDAD" , articulo.getPeriodicidad());
			 }
				
			UtilidadesHash.set(hash, "CLASE" , claseArticulo);
			vListaPyS.add(hash);
		}
		
		registros.put("vListaPyS",vListaPyS);
		
		return registros;
	}
	
	/**
	 * Notas Jorge PT 118:
     * - Productos y Servicios > Solicitudes > Compra/Subscripci�n (facturacion rapida)
     * - Productos y Servicios > Gesti�n Solicitudes (facturacion rapida)
	 *  
	 * Funcion que atiende la accion facturacionRapidaCompra. Este proceso va a realizar la 
	 * aprobaci�n de la peticion (Creacion de compra) si no esta hecho ya y posteriormente 
	 * va a facturar de manera rapida buscando la serie de facturacion adecuada si existe.
	 * RECIBE CARRO DE LA COMPRA
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */					
	protected String facturacionRapidaCompra(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {	
	    UserTransaction tx = null;
	    String salida = "";
	    try {
			UsrBean usr = this.getUserBean(request);
			tx = usr.getTransaction();
			
		    // datos llamada
			SolicitudCompraForm formSolicitudCompra = (SolicitudCompraForm)formulario;
		    Vector vOcultos = formSolicitudCompra.getDatosTablaOcultos(0);
		    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
	    	String idPeticion = ((String)vOcultos.elementAt(1)).trim();
	    	String idSerieSeleccionada = ((String)vOcultos.elementAt(2)).trim();			
		    
		    // administradores
			PysCompraAdm admCompra = new PysCompraAdm(usr);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
			PysPeticionCompraSuscripcionAdm admPeticionCompraSuscripcion = new PysPeticionCompraSuscripcionAdm(usr);		    
		    FacFacturaAdm admFactura = new FacFacturaAdm(usr);
		    Facturacion facturacion = new Facturacion(usr);
		    InformeFactura informe = new InformeFactura(usr);
		    
		    //BNS: BLOQUEAMOS LAS TABLAS DE COMPRA Y FACTURA EN ESTA TRANSACCI�N PARA CONTROLAR LAS PETICIONES SIMULTANEAS
		    tx.begin();
		    admCompra.lockTable();
		    admFactura.lockTable();
		    
		    // Obtengo la peticion de compra
		    Hashtable<String, Object> hPeticionCompraSuscripcion = new Hashtable<String, Object>(); 
		    hPeticionCompraSuscripcion.put("IDINSTITUCION",idInstitucion);
		    hPeticionCompraSuscripcion.put("IDPETICION",idPeticion);
		    
		    Vector<?> vPeticionCompraSuscripcion = admPeticionCompraSuscripcion.selectByPK(hPeticionCompraSuscripcion);		    
		    PysPeticionCompraSuscripcionBean beanPeticionCompraSuscripcion = null;
		    if (vPeticionCompraSuscripcion!=null && vPeticionCompraSuscripcion.size()>0) {
		    	beanPeticionCompraSuscripcion = (PysPeticionCompraSuscripcionBean) vPeticionCompraSuscripcion.get(0);
		    }		    			
		        
	        // PASO 0: LOCALIZO LAS COMPRAS (SI NO EXISTEN LAS GENERO)
		    if (beanPeticionCompraSuscripcion.getIdEstadoPeticion().equals(new Integer(30))) { // Esta en estado baja		        
		        throw new SIGAException("messages.facturacionRapidaCompra.estadoBaja");
		        
		    } else if (beanPeticionCompraSuscripcion.getIdEstadoPeticion().equals(new Integer(10))) { // Esta en estado pendiente. Hay que aprobarla		        
		    	beanPeticionCompraSuscripcion = admPeticionCompraSuscripcion.aprobarPeticionCompra(beanPeticionCompraSuscripcion);
		    }
		    
		    Vector<PysCompraBean> compras = admCompra.obtenerComprasPorPeticion(beanPeticionCompraSuscripcion);
		    
	        // RGG 206/05/2009 cambio por si los productos son no facturables.
	        if (compras.size()==0) {
	            throw new SIGAException("messages.facturacionRapidaCompra.noElementosFacturables");
	        }
		    
/**************************************  INICIO PROCESO FACTURACI�N RAPIDA *********************************************************/
	        
	        String idFactura = "";
	        
	        // PASO 1: OBTENER FACTURA
	        PysCompraBean pysCompraBean = (PysCompraBean) compras.get(0);
	        if (pysCompraBean.getIdFactura()==null || pysCompraBean.getIdFactura().equals("")){// Si despues de hacer la facturacion se vuelve a pulsar el boton, mostrar� la factura asociada
	        		        	
	        	// Obtiene la serie candidata
			    FacSerieFacturacionBean beanSerieCandidata = null;
	        	if (idSerieSeleccionada==null || idSerieSeleccionada.equals("")) {
				    Vector<?> series =  admSerieFacturacion.obtenerSeriesAdecuadas(compras);
				    if (series==null || series.size()!=1) {
				    	// LIBERAMOS EL BLOQUEO DE LAS TABLAS Y LA TRANSACCI�N
				        tx.rollback();
				        throw new SIGAException("messages.facturacionRapidaCompra.noSerieAdecuada");
				        
				    } else if (series.size()==1) {
				    	beanSerieCandidata = (FacSerieFacturacionBean)series.get(0);
				    }
				    
		        } else { // Se ha seleccionado una serie			        			           
			        Hashtable<String,String> hSerieFacturacion = new Hashtable<String,String>();
			        hSerieFacturacion.put("IDINSTITUCION", idInstitucion);
			        hSerieFacturacion.put("IDSERIEFACTURACION", idSerieSeleccionada);
			        
		            Vector<?> vSerieFacturacion = admSerieFacturacion.selectByPK(hSerieFacturacion);
		            if (vSerieFacturacion!=null && vSerieFacturacion.size()>0) {
		            	beanSerieCandidata = (FacSerieFacturacionBean)vSerieFacturacion.get(0);
		            }
		        }		        	

			    // PASO 2: FACTURACION RAPIDA DESDE SERIE CANDIDATA (GENERACION)
	        	FacFacturacionProgramadaBean programacion = facturacion.procesarFacturacionRapidaCompras(beanPeticionCompraSuscripcion, compras, beanSerieCandidata);
			    
			    // Aqui obtengo las facturas programadas
			    Vector<?> vFacturas = admFactura.getFacturasProgramadas(programacion);
				if (vFacturas==null || vFacturas.size()==0) { // Control extra de existencia de factura				
				    throw new SIGAException("messages.facturacionRapida.noFactura");
				} 			    
			    
			    FacFacturaBean beanFactura = (FacFacturaBean)vFacturas.get(0);
			    idFactura = beanFactura.getIdFactura();
		        
			    // PASO 3: CONFIRMACION RAPIDA (en este caso la transacci�n se gestiona dentro la transaccion)
			    facturacion.confirmarProgramacionFactura(programacion, request, false, null, false, false, 0);
			    
			    if (Status.STATUS_ACTIVE  == tx.getStatus())
			    	tx.commit();
			    
			} else {
				// YA FACTURADA, SE DESCARGA LA FACTURA
				tx.commit();
				idFactura = pysCompraBean.getIdFactura();
				
				// Compruebo que existe la factura
				Hashtable<String,String> hFactura=new Hashtable<String,String>();
				hFactura.put(FacFacturaBean.C_IDINSTITUCION, pysCompraBean.getIdInstitucion().toString());
				hFactura.put(FacFacturaBean.C_IDFACTURA, idFactura);
				
	  			Vector<?> vFactura = admFactura.selectByPK(hFactura);
				if (vFactura==null || vFactura.size()==0) { // Control extra de existencia de factura				
				    throw new SIGAException("messages.facturacionRapida.noFactura");
				} 				
			}
				
	        // PASO 4: GENERAR PDF			
			File filePDF = informe.generarFacturaRapida(request, idInstitucion, idFactura);
			if (filePDF == null) {
				throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");
			}
			
			// PASO 5: DESCARGAR PDF
			request.setAttribute("nombreFichero", filePDF.getName());
			request.setAttribute("rutaFichero", filePDF.getPath());
			request.setAttribute("generacionOK", "OK");
			salida = "descarga";
				
		} catch (SIGAException es) {
			throwExcp (es.getLiteral(), new String[] {"modulo.certificados"}, es, tx);
			
		} catch (ArrayIndexOutOfBoundsException e){
			throwExcp("messages.facturacionRapida.error.Array",new String[] {"modulo.certificados"},e,tx);
			
		} catch (ClsExceptions es) {
			throwExcp (es.getMessage(), new String[] {"modulo.certificados"}, es, tx);	
			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e,tx); 
		}
		
		return salida;
	}
	
	/** 
	 * Recupera los datos de la compra, incluyendo el importe anticipado si corresponde. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action o error en caso de no completar con exito.
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String mostrarCompra(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "comprobanteCompra";
		UserTransaction tx = null;

		try {	
			UsrBean usr = this.getUserBean(request);
			tx=usr.getTransaction();
			CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);

			if (carro.getCompraCertificado() || carro.tieneServicios()) {
				request.setAttribute("CERTIFICADO_noFactura","1");			
			}
			
			//Recorremos los articulos del carro para obtener el importe anticipado.
			//Solo se comprueban los articulos que al finalizar la compra se han marcado
			//para poder anticipar importe.
			ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
			Articulo a = null;
			for(int i=0; i<arrayListaArticulosOrdenada.size(); i++){
				a=(Articulo)arrayListaArticulosOrdenada.get(i);
				//Si no es certificado y se puede anticipar
				if( (a.getTipoCertificado()==null || "".equals(a.getTipoCertificado())) && 
					a.getAnticipar() != null && a.getAnticipar().booleanValue()) {
					String tipo = Articulo.CLASE_PRODUCTO == a.getClaseArticulo() ? "P" : "S";
					String aux[] = EjecucionPLs.ejecutarF_SIGA_COMPROBAR_ANTICIPAR(
							a.getIdInstitucion().toString(), 
							a.getIdTipo().toString(), 
							a.getIdArticulo().toString(), 
							a.getIdArticuloInstitucion().toString(), 
							tipo, 
							a.getIdPeticion().toString(),
							carro.getIdPersona().toString(),
							a.getPrecio().toString() + "#" + a.getValorIva().toString());
					if (aux != null){
						a.setAnticipar(Boolean.valueOf("1".equals(aux[0])));
						a.setImporteAnticipado(Double.valueOf(aux[1]));
					}
				}
			}
			
			//Preparamos los productos y servicios del comprobante:
			request.setAttribute("resultados",this.prepararComprobante(carro,request));		
			request.setAttribute("mostrarCompra","S");	
	
		} catch (SIGAException siga) {
			throw siga;
		} catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,tx); 
		} 

		return modo;	
	} 

	/**
	 * Obtiene las series de facturacion que estan asociadas a los pys de la peticion
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void getAjaxSeleccionSerieFacturacion (HttpServletRequest request, HttpServletResponse response) throws Exception {			
		
		String idInstitucion = request.getParameter("idInstitucion");
		if (idInstitucion==null || idInstitucion.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la instituci�n");
		
		String idPeticion = request.getParameter("idPeticion");
		if (idPeticion==null || idPeticion.trim().equalsIgnoreCase(""))
			throw new SIGAException("Falta el identificador de la petici�n");
		
		JSONObject json = new JSONObject();
		UsrBean usr = this.getUserBean(request);		
		String idSerieFacturacion = "";	
		
		// Crea el bean de la peticion
		PysPeticionCompraSuscripcionBean beanPeticionCompraSuscripcion = new PysPeticionCompraSuscripcionBean();
		beanPeticionCompraSuscripcion.setIdInstitucion(Integer.valueOf(idInstitucion));
		beanPeticionCompraSuscripcion.setIdPeticion(Long.valueOf(idPeticion));
		
		// Obtiene las compras de la peticion
		PysCompraAdm admCompra = new PysCompraAdm(usr);
		Vector<PysCompraBean> vCompras = admCompra.obtenerComprasPorPeticion(beanPeticionCompraSuscripcion);
	    
	    // Compruebo si esta facturada la compra
		PysCompraBean beanCompra = (PysCompraBean) vCompras.get(0);
	    if (beanCompra.getIdFactura()!=null && !beanCompra.getIdFactura().trim().equals("")) {	
	    	idSerieFacturacion = "Facturado"; // JPT: Esto sirve para indicar que ya esta facturado
	    	
	    } else {
		
			// Busca las series de facturacion del producto
	    	FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(usr);
			Vector<FacSerieFacturacionBean> vSeriesFacturacion =  admSerieFacturacion.obtenerSeriesAdecuadas(vCompras);
			    		
		    // Compruebo que tiene una serie de facturacion
		    if (vSeriesFacturacion.size()==1) {
		    	FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(0);
		    	idSerieFacturacion = beanSerieFacturacion.getIdSerieFacturacion().toString();
		    		    
		    } else { // Tiene varias series de facturacion asociadas al producto
		    	
		    	// Indico la primera opcion del seleccionable
		    	String sOptionsSerieFacturacion = "<option value=''>" + UtilidadesString.getMensajeIdioma(usr, "general.combo.seleccionar") + "</option>";
		    	
		    	// Cargo el resto de opciones del seleccionable
		    	for (int i = 0; i < vSeriesFacturacion.size(); i++)	{
		    		FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) vSeriesFacturacion.get(i);
		    		sOptionsSerieFacturacion +="<option value='" +  beanSerieFacturacion.getIdSerieFacturacion() + "'>" + beanSerieFacturacion.getNombreAbreviado() + "</option>";
		    	}
		    	
		    	// Devuelvo la lista de series de facturacion
		    	ArrayList<String> aOptionsSeriesFacturacion = new ArrayList<String>();
		    	aOptionsSeriesFacturacion.add(sOptionsSerieFacturacion);
		    	json.put("aOptionsSeriesFacturacion", aOptionsSeriesFacturacion);
		    }
	    }
		
    	// Deuelve el identificador de la serie de facturacion
		json.put("idSerieFacturacion", idSerieFacturacion);
		
		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 		
	}			
}