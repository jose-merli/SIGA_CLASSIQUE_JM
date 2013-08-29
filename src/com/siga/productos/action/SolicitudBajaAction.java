/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 27-01-2005 - Inicio
 */
package com.siga.productos.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.beans.PysProductosSolicitadosAdm;
import com.siga.beans.PysProductosSolicitadosBean;
import com.siga.beans.PysServiciosSolicitadosAdm;
import com.siga.beans.PysServiciosSolicitadosBean;
import com.siga.general.Articulo;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.SolicitudBajaForm;

/**
 * Clase action para la solicitud de Baja de Productos y Servicios solicitados.<br/>
 * Gestiona abrir, solicitar, buscarArticulos insertProductos, insertServicios, getProductos  
 */
public class SolicitudBajaAction extends MasterAction{
	
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
				
				// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);	
					
				}else if (accion.equalsIgnoreCase("buscarArticulos")){
					mapDestino = buscarArticulos(mapping, miForm, request, response);
				
				}else if (accion.equalsIgnoreCase("solicitar")){
					mapDestino = solicitar(mapping, miForm, request, response);			
					
				} else if (accion.equalsIgnoreCase("baja")){
					mapDestino = "baja";
					
				} else {
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
			
		} catch (SIGAException es) { 
		    throw es; 
		} catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.producto"}); 
		} 

		return mapping.findForward(mapDestino);
	}

	/** 
	 *  Funcion que atiende la accion abrir. Muestra todos los productos y servicios solicitados del usuario 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{	
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			Integer idInstitucion=Integer.valueOf(user.getLocation());
			Long idPersona=null;
			Integer usuario = this.getUserName(request);				
			
			String nombre = null;
			String numero = "";	
			String nif = "";
			
			//Comprobamos si es letrado el usuario que entra.
			boolean esLetrado=user.isLetrado();
	//		boolean esLetrado=false;
			Hashtable hash = new Hashtable();
			
			if (esLetrado){
				idPersona=new Long (user.getIdPersona());
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean personaBean = personaAdm.getIdentificador(idPersona);
				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
				CenColegiadoBean bean = colegiadoAdm.getDatosColegiales(idPersona, idInstitucion);			
				
				if(personaBean != null){	// Existen datos de la persona
					numero = colegiadoAdm.getIdentificadorColegiado(bean);			
					nombre = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
					nif = personaBean.getNIFCIF();
				}else{
					idPersona=null;
				}
				
				hash = getProductosServicios(this.getUserBean(request), idPersona, idInstitucion, false);
				
			}
			UtilidadesHash.set(hash, "idPersona", idPersona);
			
			request.getSession().setAttribute("DATABACKUP", hash);	
			request.setAttribute("nombrePersona", nombre);
			request.setAttribute("numero", numero);
			request.setAttribute("nif", nif);			
			request.setAttribute("idInstitucion", idInstitucion);				
		} 
		  catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
		
		return "abrir";
	}
	
	/** 
	 *  Funcion que atiende la accion solicitar. Realiza una solicitud de borrado del articulo seleccionado
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String solicitar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		SolicitudBajaForm form = (SolicitudBajaForm) formulario;
		
		Long idPersona = form.getIdPersona();	
		UserTransaction tx = null;
		try{
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");		
			Integer idInstitucion= this.getIDInstitucion(request);
			
			PysPeticionCompraSuscripcionAdm  PCSAdm = new PysPeticionCompraSuscripcionAdm(this.getUserBean(request));
						
			Vector ocultos = new Vector();
			ocultos = (Vector)form.getDatosTablaOcultos(0);	
			Long idPeticionAlta = Long.valueOf((String)ocultos.elementAt(0));
			
			int claseArticulo = Integer.parseInt((String)ocultos.elementAt(4));
				
			boolean exitoInsertarArticulo=true;
			Long idPeticionBaja;
									
			tx=user.getTransaction();	
			
			tx.begin();					
			idPeticionBaja = PCSAdm.getNuevoID(idInstitucion);
			
		//	 Petición de baja		
			//if(!PCSAdm.insertPeticionBaja(form.getIdPersona(), idInstitucion, idPeticionBaja, idPeticionAlta, fechaEfectiva)){
			if(!PCSAdm.insertPeticionBaja(form.getIdPersona(), idInstitucion, idPeticionBaja, idPeticionAlta)){
				exitoInsertarArticulo = false;
			}	
			
			else if(claseArticulo == Articulo.CLASE_PRODUCTO){		
				exitoInsertarArticulo = insertProducto(idInstitucion, this.getUserBean(request), idPeticionBaja, form.getIdPersona(), ocultos);
				
			}else{
				exitoInsertarArticulo = insertServicio(idInstitucion, this.getUserBean(request), idPeticionBaja, form.getIdPersona(), ocultos);
			}		
			
			
				
			if(exitoInsertarArticulo){
				tx.commit();		
		
				return exitoRefresco("messages.updated.success",request);
			}
			else{
				tx.rollback();
				//modo = error("messages.updated.error",new ClsExceptions(PCSAdm.getError()),request);
			}		
		}catch (SIGAException se) { 
			try {
				tx.rollback();
			} catch (Exception e) {
				throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx);
			}
			return exitoRefresco(se.getLiteral(), request);
		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 		
	  return "solicitar";	
	}
			
	/** 
	 *  Funcion que atiende la accion buscarArticulos. Muestra todos los productos y servicios solicitados del usuario que se ha seleccionado mediante la funcionalidad 'buscar'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarArticulos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try{
			
			SolicitudBajaForm form = (SolicitudBajaForm) formulario;		
			
			
			Integer idInstitucion = this.getIDInstitucion(request);
			Long idPersona = form.getIdPersona();	
					
			Hashtable hash = new Hashtable();
			hash = getProductosServicios(this.getUserBean(request), idPersona, idInstitucion, true);
			
			UtilidadesHash.set(hash, "idPersona", idPersona);
			request.getSession().setAttribute("DATABACKUP", hash);	
			
			// Recuperamos el parametro APROBAR_SOLICITUD_COMPRA y lo metemos en la request
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			request.setAttribute("paramAprobarSolicitud", paramAdm.getValor(idInstitucion.toString(), "PYS", "APROBAR_SOLICITUD_COMPRA", ""));

		
		}catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		}
		
		return "buscarArticulos";
	 }
	
	/**
	 * Obtiene los productos y servicios que tiene solicitados el usuario cuyo idPersona se corresponda con el parámetro.	 
	 * @param idInstitucion - identificador de la institución
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @return  Hashtable contiene todos los productos y servicios solicitado
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected Hashtable getProductosServicios(UsrBean usuario, Long idPersona, Integer idInstitucion, boolean peticionBaja)  throws SIGAException {
		Hashtable hash = new Hashtable();
		try{			
			PysProductosSolicitadosAdm productosAdm = new PysProductosSolicitadosAdm(usuario);
			PysServiciosSolicitadosAdm serviciosAdm = new PysServiciosSolicitadosAdm(usuario);
	
			String tipoPeticion = ClsConstants.TIPO_PETICION_COMPRA_ALTA;
			Hashtable hDatos = new Hashtable();	
			
			UtilidadesHash.set(hDatos, PysProductosSolicitadosBean.C_IDPERSONA, idPersona); 
			UtilidadesHash.set(hDatos, PysPeticionCompraSuscripcionBean.C_TIPOPETICION, tipoPeticion);
			UtilidadesHash.set(hDatos, "ES_SOLICITUD_BAJA", "true");
			
			
			Vector vAux = null;
			vAux = productosAdm.getProductosSolicitados(hDatos, idInstitucion, peticionBaja);
			if(vAux != null) {
				hash.put("vProductos", vAux);
			}
			vAux = serviciosAdm.getServiciosSolicitados(hDatos, idInstitucion, peticionBaja);
			if(vAux != null) {
				hash.put("vServicios", vAux);
	 		}
		}catch (Exception e) { 
			   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
			}
			return hash;
	}
	
	/**
	 * Inserta el producto en la tabla Pys_ProductosSolicitados	con el parametro Baja
	 * @param idInstitucion - identificador de la Institucion a la que pertenece el usuario
	 * @param idPeticion - identificador de la petición de compra a la que está asociado  
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @param ocultos - contiene los datos que identifican al producto seleccionado
	 * @return  boolean  Informa si la operación ha sido realizada con exito
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean insertProducto(Integer idInstitucion, UsrBean usuario, Long idPeticion, Long idPersona, Vector ocultos) throws SIGAException {
		
		boolean ok = false;		

		try{
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(usuario);
			String aprobarSolicitud=parametrosAdm.getValor(usuario.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
			Hashtable hash = new Hashtable();
			PysProductosSolicitadosAdm adm = new PysProductosSolicitadosAdm(usuario);	
			PysProductosSolicitadosBean bean = new PysProductosSolicitadosBean();
			String fechaEfectiva = null;
			
			Long idPeticionAlta = Long.valueOf((String)ocultos.elementAt(0));
			Integer idTipoArticulo = Integer.valueOf((String)ocultos.elementAt(1));
			Long idArticulo = Long.valueOf((String)ocultos.elementAt(2));
			Long idArticuloInstitucion = Long.valueOf((String)ocultos.elementAt(3));
			if (ocultos.size()>5){
				fechaEfectiva = (String)ocultos.elementAt(5);
			}
			
			
			
			String sWhere = " where " + PysProductosSolicitadosBean.C_IDINSTITUCION + "=" + idInstitucion + " and " +
							PysProductosSolicitadosBean.C_IDPETICION + "=" + idPeticionAlta + " and " +
							PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + "=" + idTipoArticulo + " and " +
							PysProductosSolicitadosBean.C_IDPRODUCTO + "=" + idArticulo + " and " +
							PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + "=" + idArticuloInstitucion; 

			Vector vProducto = adm.select(sWhere);
			if ((vProducto != null) && (vProducto.size() > 0)) {
				bean = (PysProductosSolicitadosBean)vProducto.elementAt(0);
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDINSTITUCION, bean.getIdInstitucion());	
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO, bean.getIdTipoProducto());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTO, bean.getIdProducto());			
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION, bean.getIdProductoInstitucion());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDPERSONA, idPersona);
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDCUENTA, bean.getIdCuenta());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO, bean.getIdFormaPago());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_PORCENTAJEIVA, bean.getPorcentajeIVA());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_CANTIDAD, bean.getCantidad());
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_NOFACTURABLE, bean.getNoFacturable());
				/*GenParametrosAdm parametrosAdm = new GenParametrosAdm(usuario);
				String aprobarSolicitud=parametrosAdm.getValor(usuario.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
				if(!usuario.isLetrado()&&aprobarSolicitud.equals("S")){
				  UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_BAJA);
				}else{*/
				  UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);	
				//}
				UtilidadesHash.set(hash, PysProductosSolicitadosBean.C_VALOR, bean.getValor());					
			
				ok = adm.insert(hash);	
				
				if(!usuario.isLetrado()&&aprobarSolicitud.equals("S")){
					//fechaEfectiva=form.getFechaEfectivaCompra();
					 adm.confirmarProducto( bean.getIdInstitucion(), idPeticion, bean.getIdTipoProducto(), bean.getIdProducto(), bean.getIdProductoInstitucion(), null,fechaEfectiva);
						
				
				}
			}
			
			
			
			
			
			
		}catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null); 
		} 
			return ok;
	}	
		
	/**
	 * Inserta el servicio en la tabla PysServiciosSolicitados	con el parametro Baja
	 * @param idInstitucion - identificador de la Institucion a la que pertenece el usuario
	 * @param idPeticion - identificador de la petición de compra a la que está asociado  
	 * @param idPersona - identificador de la persona para la que se realiza la peticion 
	 * @param usuario - identificador del usuario que realiza la peticion  
	 * @param ocultos - contiene los datos que identifican al servicio seleccionado
	 * @return  boolean  Informa si la operación ha sido realizada con exito
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected boolean insertServicio(Integer idInstitucion, UsrBean usuario, Long idPeticion, Long idPersona, Vector ocultos) throws SIGAException {

		boolean ok = false;
		
		try{
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(usuario);
			String aprobarSolicitud=parametrosAdm.getValor(usuario.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
			Hashtable hash = new Hashtable();
			PysServiciosSolicitadosAdm adm = new PysServiciosSolicitadosAdm(usuario);	
			PysServiciosSolicitadosBean bean = new PysServiciosSolicitadosBean();
			String fechaEfectiva = null;
			
			Long idPeticionAlta = Long.valueOf((String)ocultos.elementAt(0));
			Integer idTipoArticulo = Integer.valueOf((String)ocultos.elementAt(1));
			Long idArticulo = Long.valueOf((String)ocultos.elementAt(2));
			Long idArticuloInstitucion = Long.valueOf((String)ocultos.elementAt(3));
			if (ocultos.size()>5){
				fechaEfectiva = (String)ocultos.elementAt(5);
			}
			
			String sWhere = " where " + PysServiciosSolicitadosBean.C_IDINSTITUCION + "=" + idInstitucion + " and " +
							PysServiciosSolicitadosBean.C_IDPETICION + "=" + idPeticionAlta + " and " +
							PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS + "=" + idTipoArticulo + " and " +
							PysServiciosSolicitadosBean.C_IDSERVICIO + "=" + idArticulo + " and " +
							PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION + "=" + idArticuloInstitucion; 
		
			Vector vProducto = adm.select(sWhere);
			if ((vProducto != null) && (vProducto.size() > 0)) {
				bean = (PysServiciosSolicitadosBean)vProducto.elementAt(0);				
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION , bean.getIdInstitucion());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS , bean.getIdTipoServicios());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIO , bean.getIdServicio());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION, bean.getIdServicioInstitucion());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPETICION, idPeticion);
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERSONA, idPersona);
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDCUENTA, bean.getIdCuenta());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO, bean.getIdFormaPago());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_CANTIDAD, bean.getCantidad());
				/*GenParametrosAdm parametrosAdm = new GenParametrosAdm(usuario);
				String aprobarSolicitud=parametrosAdm.getValor(usuario.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
				if(!usuario.isLetrado()&&aprobarSolicitud.equals("S")){
					  UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_BAJA);
				}else{*/
					  UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_ACEPTADO, ClsConstants.PRODUCTO_PENDIENTE);	
				//}
				
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPERIODICIDAD, bean.getIdPeriodicidad());
				UtilidadesHash.set(hash, PysServiciosSolicitadosBean.C_IDPRECIOSSERVICIOS, bean.getIdPreciosServicios());
				ok = adm.insert(hash);
				
				if(!usuario.isLetrado()&&aprobarSolicitud.equals("S")){
					//fechaEfectiva=form.getFechaEfectivaCompra();
					 adm.confirmarServicio( bean.getIdInstitucion(), idPeticion, bean.getIdTipoServicios(), bean.getIdServicio(), bean.getIdServicioInstitucion(), null,null,fechaEfectiva);
						
				
				}
			}
			
			

			
				
				
			
		}catch (SIGAException se) {
			throw (se);
		}catch (Exception e) { 
		   throwExcp("messages.general.error",new String[] {"modulo.producto"},e,null);
		} 
		return ok;
	}	
	

}