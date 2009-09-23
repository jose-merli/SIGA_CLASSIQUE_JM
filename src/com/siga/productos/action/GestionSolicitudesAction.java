/*
 * Created on 03-feb-2005
 * Modified by miguel.villegas confirmar() on 04-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.productos.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.Articulo;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.GestionSolicitudesForm;
import java.util.*;

/**
 * @author daniel.campos
 *
 */
public class GestionSolicitudesAction extends MasterAction {
	
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#executeInternal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/**
	 * Redefinicion de la funcion executeInternal para controlar las nuevas acciones confirmar y denegar
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {		
			String mapDestino = "exception";
			MasterForm miForm = null;
		    
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
			
				String accion = miForm.getModo();
				if (accion == null) {
					mapDestino = "inicio";
					break;
				}
				else if (accion.equalsIgnoreCase("buscarInit")){
					//borrarPaginador(request, paginador+miForm.getClass().getName());
					String idPaginador = getIdPaginador(super.paginador,getClass().getName());
					borrarPaginador(request, idPaginador);
					mapDestino = buscarPor(mapping, miForm, request, response);	
				}
				// Confirmar
				else if (accion.equalsIgnoreCase("confirmar")){
					mapDestino = confirmar(mapping, miForm, request, response);
					break;
				} // Denegar
				else if (accion.equalsIgnoreCase("denegar")){
					mapDestino = denegar(mapping, miForm, request, response);
					break;
				}
				// Obtener el numero de cuenta
				else if (accion.equalsIgnoreCase("modificarCuenta")){
					mapDestino = modificarCuenta(mapping, miForm, request, response);
					break;
				}
				// Obtener el numero de cuenta
				else if (accion.equalsIgnoreCase("mostrarAnticipos")){
					mapDestino = mostrarAnticipos(mapping, miForm, request, response);
					break;
				}
				// Obtener el numero de cuenta
				else if (accion.equalsIgnoreCase("guardarAnticipos")){
					mapDestino = guardarAnticipos(mapping, miForm, request, response);
					break;
				}

				return super.executeInternal(mapping, formulario, request, response);
				
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 				
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		}
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			//throw new SIGAException("Error en la Aplicación",e);
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.productos"}); 
		}
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		GestionSolicitudesForm miForm = (GestionSolicitudesForm) formulario;
		PysPeticionCompraSuscripcionAdm peticionAdm = new PysPeticionCompraSuscripcionAdm (this.getUserBean(request));
		String idPaginador = getIdPaginador(super.paginador,getClass().getName());
		request.setAttribute(ClsConstants.PARAM_PAGINACION,idPaginador);
		try {
			HashMap databackup=getPaginador(request, idPaginador);
			if (databackup!=null){ 

				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
					
					
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	
				databackup=new HashMap();
				
				PaginadorBind paginador = peticionAdm.getPeticionesPaginador(miForm, this.getIDInstitucion(request));
				int totalRegistros = paginador.getNumeroTotalRegistros();
				if (totalRegistros==0){					
					paginador =null;
				}
				databackup.put("paginador",paginador);
				if (paginador!=null){ 
					Vector datos = paginador.obtenerPagina(1);
					
					
					databackup.put("datos",datos);
					setPaginador(request, idPaginador, databackup);
				} 	

				



			}
		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			 return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
		} 
		
		
		return "buscarPor";
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			String idSolicitud="";
			String idSolicitudCertificado="";
	       
	        if (request.getParameter("idPeticion")!=null){
	        	idSolicitud=(String)request.getParameter("idPeticion");
	        }
	        request.setAttribute("idPeticion",idSolicitud);
	        
	        if (request.getParameter("idSolicitudCertificado")!=null){
	        	idSolicitudCertificado=(String)request.getParameter("idSolicitudCertificado");
	        }
	        request.setAttribute("idSolicitudCertificado",idSolicitudCertificado);
	        
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"},e,null); 
		} 
		return "inicio";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			GestionSolicitudesForm miForm = (GestionSolicitudesForm) formulario;
			
			Long 	idPeticion    = new Long ((String)miForm.getDatosTablaOcultos(0).get(0));
			Integer idInstitucion = new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			Long 	idPersona     = new Long ((String)miForm.getDatosTablaOcultos(0).get(3));
			/** INC-2782**/
			String  tipoSolicitud =(String)miForm.getDatosTablaOcultos(0).get(4);
			request.setAttribute("tipoSolicitud", tipoSolicitud);
			/*******/
			
			Hashtable hash = new Hashtable ();
			UtilidadesHash.set(hash, PysPeticionCompraSuscripcionBean.C_IDPETICION, idPeticion);
			PysPeticionCompraSuscripcionAdm peticionAdm = new PysPeticionCompraSuscripcionAdm (this.getUserBean(request));
			Vector peticion = peticionAdm.getPeticionDetalle (hash, idInstitucion);
			request.setAttribute("peticion", peticion);
			
			// Obtengo los datos de la persona, su Numero de colegiado y su nombre
			CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));			
			String nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPersona));		
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
			CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(idPersona, idInstitucion);
			String numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
			request.setAttribute("nColegiado", numero);
			request.setAttribute("nombreUsuario", nombre);
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("idPeticion", idPeticion);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return "editar";
	}
	
	/**
	 * Funcion que confirma un producto o un servicio de una peticion 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String confirmar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		String forward = "";
		UserTransaction t = this.getUserBean(request).getTransaction();
		UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

		try {
			boolean rc = false;
			Integer idCuenta=null;
			String productoOServicio=null;
			Long idPeticion=null;
			Integer idInstitucion=null;
			Long idArticulo=null;
			Long idArticuloInstitucion=null;
			Integer idTipoArticulo=null;
			double importeAnticipado=0;
			String fechaEfectiva="0";
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
			String aprobarSolicitud=parametrosAdm.getValor(user.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
			
			do {
				if(!user.isLetrado() && aprobarSolicitud.equals("S") && 
				   formulario.getModo().equals("finalizarCompra") || formulario.getModo().equals("okCompraTPV")){
				
					Articulo a=null;
					
					a=(Articulo)request.getAttribute("ARTICULO");
					fechaEfectiva=(String)a.getFechaEfectiva();
					
					if(a.getClaseArticulo()==1) {
						productoOServicio = "producto";
					}
					else{
						productoOServicio = "servicio";
					}
					
					idPeticion = a.getIdPeticion();
					idInstitucion = a.getIdInstitucion();
					idArticulo = a.getIdArticulo();
					idArticuloInstitucion = a.getIdArticuloInstitucion();
					idTipoArticulo = a.getIdTipo();
					importeAnticipado = 0; //preguntar.... siempre 0, no?
					
					//// MODIFICADO POR MAV //////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// getIdCuenta obtenia un valor que no se coprrespondia con el asociado realmente al formulario										//
					// Por cierto, form devuelve "null" en lugar de null ??																				//
					// Integer idCuenta = miForm.getIdCuenta();																							//
					if (a.getPrecio()!=null){	//
						idCuenta = a.getIdCuenta();
					}		
				}else{
					GestionSolicitudesForm miForm = (GestionSolicitudesForm) formulario;
					
					productoOServicio = (String)(miForm.getDatosTablaOcultos(0).get(0));
					idPeticion = new Long((String)(miForm.getDatosTablaOcultos(0).get(1)));
					idInstitucion = new Integer((String)(miForm.getDatosTablaOcultos(0).get(2)));
					idArticulo = new Long((String)(miForm.getDatosTablaOcultos(0).get(3)));
					idArticuloInstitucion = new Long((String)(miForm.getDatosTablaOcultos(0).get(4)));
					idTipoArticulo = new Integer((String)(miForm.getDatosTablaOcultos(0).get(5)));
					importeAnticipado = (miForm.getImporteAnticipado()).doubleValue();
					//fechaEfectiva = (String)(request.getParameter("fechaEfectivaCompra"+Integer.parseInt(miForm.getFilaSelD())).trim());
					
					fechaEfectiva=(String)(request.getParameter("fechaEfectiva"));
					
					
					
					//// MODIFICADO POR MAV //////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// getIdCuenta obtenia un valor que no se coprrespondia con el asociado realmente al formulario										//
					// Por cierto, form devuelve "null" en lugar de null ??																				//
					// Integer idCuenta = miForm.getIdCuenta();																							//
					if (((miForm.getDatosTablaOcultos(0).get(8))!=null)&&!((String)(miForm.getDatosTablaOcultos(0).get(8))).equalsIgnoreCase("null")){	//
						idCuenta = new Integer((String)(miForm.getDatosTablaOcultos(0).get(9)));														//
					}			
				}
			
																														//
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				t.begin();

				// Producto
				if (productoOServicio.equalsIgnoreCase("producto")) {
					PysProductosSolicitadosAdm productoSolAdm = new PysProductosSolicitadosAdm (this.getUserBean(request));
					rc = productoSolAdm.confirmarProducto(idInstitucion, idPeticion, idTipoArticulo, idArticulo, idArticuloInstitucion, new Double(importeAnticipado),fechaEfectiva);
					break;
				}

				// Servicio
				if (productoOServicio.equalsIgnoreCase("servicio")){
					PysServiciosSolicitadosAdm servicioSolAdm = new PysServiciosSolicitadosAdm (this.getUserBean(request));
					rc = servicioSolAdm.confirmarServicio(idInstitucion, idPeticion, idTipoArticulo, idArticulo, idArticuloInstitucion, idCuenta, new Double(importeAnticipado),fechaEfectiva/* (String)request.getParameter("fechaEfectiva")*/);
					break;
				}
			}
			while (false);
			
			if (rc == true) {
				t.commit();
//				request.setAttribute("siCierroVentanaRefresco", "si");
				forward = exitoRefresco("messages.updated.success",request);
				
			}
			else {
				throw new ClsExceptions ("Error al confirmar el producto/servicio");
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"}, e, t); 
		}
		return forward;
	}

	/**
	 * Funcion que deniega un producto o un servicio de una peticion
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String denegar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String forward = "";
		UserTransaction t = this.getUserBean(request).getTransaction();

		try {
			boolean rc = false;
			String mensaje[] = new String[1];
			
			do {
				GestionSolicitudesForm miForm = (GestionSolicitudesForm) formulario;
			
				String productoOServicio = (String)(miForm.getDatosTablaOcultos(0).get(0));
				Long idPeticion = new Long((String)(miForm.getDatosTablaOcultos(0).get(1)));
				Integer idInstitucion = new Integer((String)(miForm.getDatosTablaOcultos(0).get(2)));
				Long idArticulo = new Long((String)(miForm.getDatosTablaOcultos(0).get(3)));
				Long idArticuloInstitucion = new Long((String)(miForm.getDatosTablaOcultos(0).get(4)));
				Integer idTipoArticulo = new Integer((String)(miForm.getDatosTablaOcultos(0).get(5)));

				t.begin();

				// Producto
				if (productoOServicio.equalsIgnoreCase("producto")) {
					PysProductosSolicitadosAdm productoSolAdm = new PysProductosSolicitadosAdm (this.getUserBean(request));
					rc = productoSolAdm.denegarProducto(idInstitucion, idPeticion, idTipoArticulo, idArticulo, idArticuloInstitucion, mensaje);
					break;
				}

				// Servicio
				if (productoOServicio.equalsIgnoreCase("servicio")){
					PysServiciosSolicitadosAdm servicioSolAdm = new PysServiciosSolicitadosAdm (this.getUserBean(request));
					rc = servicioSolAdm.denegarServicio(idInstitucion, idPeticion, idTipoArticulo, idArticulo, idArticuloInstitucion, mensaje);
					break;
				}
			}
			while (false);
			
			if (rc == true) {
				t.commit();
//				request.setAttribute("siCierroVentanaRefresco", "si");
				forward = exitoRefresco(mensaje[0], request);
			}
			else {
				throw new ClsExceptions ("Error al denegar el producto/servicio");
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, t); 
		}
		return forward;
	}
	
	/**
	 * Funcion que abre una ventana para introducir el nº de Cuenta
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String modificarCuenta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String forward = "modificarCuenta";	

		try {
			GestionSolicitudesForm miForm = (GestionSolicitudesForm) formulario;
			Long  idPersona=miForm.getIdPersona();	
			Integer idinstitucion = this.getIDInstitucion(request);
			
			request.setAttribute("idInstitucion", idinstitucion.toString());
			request.setAttribute("idPersona", idPersona.toString());					
		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		}
		return forward;
	}
	
	
	
	/**
	 * Recupera el importe anticipado y el precio de una solicitud de compra y los
	 * envia a la página de anticipar importe.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String mostrarAnticipos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String forward = "mostrarAnticipos";	

		try {
			Integer idinstitucion = this.getIDInstitucion(request);
			String idTipoClave = (String) request.getParameter("idTipoClave");
			String idClave = (String) request.getParameter("idClave");
			String idClaveInstitucion = (String) request.getParameter("idClaveInstitucion");
			String tipo = (String) request.getParameter("tipo");
			String idPeticion = (String) request.getParameter("idPeticion");
			String idPersona = (String) request.getParameter("idPersona");
			
			String [] resultado = EjecucionPLs.ejecutarF_SIGA_OBTENER_ANTICIPOS(
							idinstitucion.toString(),
							idTipoClave,
							idClave,
							idClaveInstitucion,
							tipo,
							idPeticion,
							idPersona);
			if (resultado == null){
				throw new SIGAException("update.compare.diferencias");
			}

			request.setAttribute("totalAnticipado", resultado[1]);
			request.setAttribute("precioSolicitud", resultado[0]);
			request.setAttribute("idTipoClave", idTipoClave);
			request.setAttribute("idClave", idClave);
			request.setAttribute("idClaveInstitucion", idClaveInstitucion);
			request.setAttribute("tipo", tipo);
			request.setAttribute("idPeticion", idPeticion);
			request.setAttribute("idPersona", idPersona);
			
				
		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		}
		return forward;
	}

	/**
	 * Comprueba que la suma del total anticipado y el nuevo anticipo no superan
	 * el precio de la solicitud de compra y guarda, en su caso, el nuevo total anticipado.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String guardarAnticipos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UsrBean userBean = null;
		UserTransaction tx = null;
		
	    try {
		    userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
		    
			Integer idinstitucion = this.getIDInstitucion(request);
			String idTipoClave = (String) request.getParameter("idTipoClave");
			String idClave = (String) request.getParameter("idClave");
			String idClaveInstitucion = (String) request.getParameter("idClaveInstitucion");
			String tipo = (String) request.getParameter("tipo");
			String idPeticion = (String) request.getParameter("idPeticion");

			String totalAnticipado = (String) request.getParameter("totalAnticipado");
			String precioSolicitud = (String) request.getParameter("precioSolicitud");
			String nuevoImporteAnticipado = (String) request.getParameter("nuevoImporteAnticipado");

			//guarda en BD el importe anticipado actualizado
			if ("S".equals(tipo)){
				PysSuscripcionAdm suscripcion = new PysSuscripcionAdm (this.getUserBean(request));
				
				//Recuperar el bean de BD
				Hashtable claves = new Hashtable();
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDINSTITUCION, idinstitucion);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDTIPOSERVICIOS, idTipoClave);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDSERVICIOSINSTITUCION, idClaveInstitucion);
				UtilidadesHash.set(claves, PysSuscripcionBean.C_IDSERVICIO, idClave);

				tx.begin();
				
				Vector vectorBeans = suscripcion.selectForUpdate(claves);
				PysSuscripcionBean bean = (PysSuscripcionBean)vectorBeans.get(0);

				
				//Inicializar el nuevo importe anticipado
				Double nuevoTotalAnticipado = validaImporteAnticipado(
						bean.getImporteAnticipado(), precioSolicitud, nuevoImporteAnticipado, totalAnticipado);
				bean.setImporteAnticipado(nuevoTotalAnticipado);

				if(!suscripcion.update(bean)){
					throw new SIGAException ("Error actualizando compra: " + suscripcion.getError());
				}
				tx.commit();
			}
			else{
				PysCompraAdm compra = new PysCompraAdm(this.getUserBean(request));
				
				//Recuperar el bean de BD
				Hashtable claves = new Hashtable();
				UtilidadesHash.set(claves, PysCompraBean.C_IDINSTITUCION, idinstitucion);
				UtilidadesHash.set(claves, PysCompraBean.C_IDTIPOPRODUCTO, idTipoClave);
				UtilidadesHash.set(claves, PysCompraBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(claves, PysCompraBean.C_IDPRODUCTOINSTITUCION, idClaveInstitucion);
				UtilidadesHash.set(claves, PysCompraBean.C_IDPRODUCTO, idClave);

				tx.begin();
				
				Vector vectorBeans = compra.selectForUpdate(claves);
				PysCompraBean bean = (PysCompraBean)vectorBeans.get(0);
				//Inicializar el nuevo importe anticipado
				Double nuevoTotalAnticipado = validaImporteAnticipado(
						bean.getImporteAnticipado(), precioSolicitud, nuevoImporteAnticipado,totalAnticipado);
				bean.setImporteAnticipado(nuevoTotalAnticipado);

				if(!compra.update(bean)){
					throw new SIGAException ("Error actualizando compra: " + compra.getError());
				}
				tx.commit();
			}	


		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, tx); 
		}
		
		return 	exitoModal("", request);
	}

	private Double validaImporteAnticipado(Double totalAnticipadoBD,
			String precioSolicitud, String nuevoImporteAnticipado, String totalAnticipado)
			throws SIGAException {

		//valida que el dato del importeAnticipado en BD y en el formulario son consistentes
		//para que se vuelva a cargar la ventana si no lo son.
		if (Double.valueOf(totalAnticipado).doubleValue() != totalAnticipadoBD.doubleValue() ){
			throw new SIGAException("update.compare.diferencias");
		}
		//calcula el nuevo importe anticipado
		double nuevoTotalAnticipado;
		try{
			nuevoTotalAnticipado = totalAnticipadoBD.doubleValue()+Double.valueOf(nuevoImporteAnticipado).doubleValue();
		}
		catch(Exception e){
			throw new SIGAException("messages.pys.solicitudCompra.errorAnticiparImporteNoValido");
		}
		//valida que el nuevo total anticipado no supere al precio de la solicitud		
		if (Double.valueOf(precioSolicitud).doubleValue() < nuevoTotalAnticipado){
			throw new SIGAException("messages.pys.solicitudCompra.errorAnticiparImporteSuperior");
		}
		return new Double(nuevoTotalAnticipado);
	}

	
}
