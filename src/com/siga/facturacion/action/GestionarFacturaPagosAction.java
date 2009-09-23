/*
 * Created on 16-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.facturacion.form.GestionarFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class GestionarFacturaPagosAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#executeInternal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
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
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}

				// Pago por caja
				if (accion.equalsIgnoreCase("pagoPorCaja")){
					mapDestino = pagoPorCaja(mapping, miForm, request, response);
					break;
				}
				
				if (accion.equalsIgnoreCase("insertarPagoPorCaja")){
					mapDestino = insertarPagoPorCaja(mapping, miForm, request, response);
					break;
				}


				// Pago por tarjeta
				if (accion.equalsIgnoreCase("pagoPorTarjeta")){
					mapDestino = pagoPorTarjeta(mapping, miForm, request, response);
					break;
				}
				
				// Renegociar
				if (accion.equalsIgnoreCase("pagoRenegociar")){
					mapDestino = pagoRenegociar(mapping, miForm, request, response);
					break;
				}

				if (accion.equalsIgnoreCase("insertarRenegociar")){
					mapDestino = insertarRenegociar(mapping, miForm, request, response);
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
			throw new SIGAException("messages.general.error", e , new String[] {"modulo.facturacion"}); 
		}
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		try{
			// Obtengo los diferentes parametros recibidos
			String accion =  (String)request.getParameter("accion");
			String idFactura = (String)request.getParameter("idFactura");
			Integer idInstitucion = new Integer((String)request.getParameter("idInstitucion"));
			Long idPersona=new Long ((String)request.getParameter("idPersona"));

			String volver = null;
			try {				
				if (request.getParameter("botonVolver")==null)
					volver = "NO";
				else
					volver = (String)request.getParameter("botonVolver");
			} catch (Exception e){
				volver = "NO";
			}			
			
			// Datos de pagos
			FacPagosPorCajaAdm pagosAdm = new FacPagosPorCajaAdm(this.getUserBean(request));
			Vector vPagos = pagosAdm.getPagos(idInstitucion, idFactura, idPersona);
			
			// Totales de los pagos 
			Hashtable hTotales = pagosAdm.getTotalesPagos(idInstitucion, idFactura); 

			// Obtenermos el estado y el numero de la factura
			FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			Integer estadoFactura = facturaAdm.getEstadoFactura(idInstitucion, idFactura);

			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
			Vector vFacturas = facturaAdm.selectByPK(claves);
			String numFactura = ((FacFacturaBean)vFacturas.get(0)).getNumeroFactura();
			
			// Pasamos los datos a la pagina JSP
			Hashtable datos = new Hashtable ();
			datos.put("PAGOS",vPagos);
			datos.put("TOTALES", hTotales);
			request.getSession().setAttribute("DATABACKUP", datos);
			request.setAttribute("idInstitucion", idInstitucion);
			request.setAttribute("idFactura", idFactura);
			request.setAttribute("estadoFactura", estadoFactura);
			request.setAttribute("numeroFactura", numFactura);
			request.setAttribute("modoRegistroBusqueda", accion);
			request.setAttribute("volver", volver);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return "inicio";
	}

	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String modo = "";
		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			modo = miForm.getModo().toLowerCase();
			Vector v = miForm.getDatosTablaOcultos(0);
			Integer idPago = new Integer ((String)v.get(0));
			Integer idInstitucion = miForm.getIdInstitucion();
			String idFactura = miForm.getIdFactura();
			Hashtable claves = new Hashtable();
			
			// Datos de la factura
			claves.clear();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
			FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			v = facturaAdm.select(claves);
			FacFacturaBean facturaBean = null;
			if (v != null) 
				facturaBean = (FacFacturaBean) v.get(0);
			
			// Datos del pago
			claves.clear();
			UtilidadesHash.set(claves, FacPagosPorCajaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacPagosPorCajaBean.C_IDFACTURA, idFactura);
			UtilidadesHash.set(claves, FacPagosPorCajaBean.C_IDPAGOPORCAJA, idPago);
			FacPagosPorCajaAdm pagoAdm = new FacPagosPorCajaAdm (this.getUserBean(request));
			v = pagoAdm.select(claves);
			FacPagosPorCajaBean pagoBean = null;
			if (v != null) 
				pagoBean = (FacPagosPorCajaBean) v.get(0);
			
			// Nombre instirucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm (this.getUserBean(request));
			String nombreInstitucion = institucionAdm.getNombreInstitucion(idInstitucion.toString());

			// Nombre Persona
			CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
			String nombrePersona = personaAdm.obtenerNombreApellidos(facturaBean.getIdPersona().toString());
			
			// Numero Colegiado
			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			String numeroColegiado = colegiadoAdm.getIdentificadorColegiado(colegiadoAdm.getDatosColegiales(facturaBean.getIdPersona(), idInstitucion));
			
			// Medio de pago
			v = miForm.getDatosTablaVisibles(0);
			String medioPago = (String) v.get(1);
			
			request.setAttribute("nombreInstitucion", nombreInstitucion);
			request.setAttribute("nombrePersona", nombrePersona);
			request.setAttribute("numeroColegiado", numeroColegiado);
			request.setAttribute("factura", facturaBean);
			request.setAttribute("pago", pagoBean);
			request.setAttribute("medioPago", medioPago);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}
	
	/**
	 * Funcion que recupera los datos para la visualizacion de un pago por caja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String pagoPorCaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
	
		String modo = "";
		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
			modo = miForm.getModo();
			request.setAttribute("idInstitucion", 		miForm.getIdInstitucion());
			request.setAttribute("idFactura", 			miForm.getIdFactura());
			//request.setAttribute("importePendiente", 	miForm.getDatosPagosCajaImportePendiente());
			request.setAttribute("importePendiente", 	admFac.getImportePendientePago(miForm.getIdInstitucion().toString(),miForm.getIdFactura()));
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}

	/**
	 * Inserta en BD un nuevo pago por caja
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String insertarPagoPorCaja(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		UserTransaction t = this.getUserBean(request).getTransaction();
		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			FacPagosPorCajaAdm pagosAdm = new FacPagosPorCajaAdm(this.getUserBean(request));
			FacPagosPorCajaBean pagoBean = new FacPagosPorCajaBean();
			pagoBean.setContabilizado("N");
			pagoBean.setFecha(miForm.getDatosPagosCajaFecha());
			pagoBean.setIdFactura(miForm.getIdFactura());
			pagoBean.setIdInstitucion(miForm.getIdInstitucion());
			pagoBean.setIdPagoPorCaja(pagosAdm.getNuevoID(miForm.getIdInstitucion(), miForm.getIdFactura()));
			pagoBean.setImporte(miForm.getDatosPagosCajaImporteCobrado());
			pagoBean.setObservaciones(miForm.getDatosPagosCajaObservaciones());
			pagoBean.setTarjeta("N");

			t.begin();
			if(pagosAdm.insert(pagoBean)) {
			    FacFacturaBean facturaBean = null;
				FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));
			    Hashtable ht = new Hashtable();
			    ht.put(FacFacturaBean.C_IDINSTITUCION,pagoBean.getIdInstitucion());
			    ht.put(FacFacturaBean.C_IDFACTURA,pagoBean.getIdFactura());
			    Vector v = facturaAdm.selectByPK(ht);
			    if (v!=null && v.size()>0) {
			        facturaBean = (FacFacturaBean) v.get(0);
			        
			        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
			        facturaBean.setImpTotalPagadoPorCaja(new Double(facturaBean.getImpTotalPagadoPorCaja().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue()));
			        facturaBean.setImpTotalPagadoSoloCaja(new Double(facturaBean.getImpTotalPagadoSoloCaja().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue()));
			        facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue()));
			        facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()-miForm.getDatosPagosCajaImporteCobrado().doubleValue()));
			        
			        if (facturaAdm.update(facturaBean)) {
				        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
						facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
			        } else {
			            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
			        }
					
					t.commit();

			    } else {
			        throw new ClsExceptions("No se ha encontrado la factura buscada: "+pagoBean.getIdInstitucion()+ " "+pagoBean.getIdFactura());
			    }
				
			}
			else {
			    throw new ClsExceptions("Error al insertar el pago y/o actualizar los importes y el estado de la factura: "+pagosAdm.getError()); 
			}
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, t); 
		}				
		return exitoModal("messages.inserted.success", request);
	}

	/**
	 * Funcion que recupera los datos para la visualizacion de una renegociacion de un pago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String pagoRenegociar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String modo = "";
		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			modo = miForm.getModo();
			Integer idInstitucion = miForm.getIdInstitucion();
			String idFactura = miForm.getIdFactura();
			
			// Obtenemos la factura
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
			FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			Vector vFactura = facturaAdm.select(claves);
			FacFacturaBean facturaBean = null;
			if (vFactura != null) 
				facturaBean = (FacFacturaBean) vFactura.get(0); 
			
			// Obtenemos el estado de la factura
			//Integer estadoFactura = facturaAdm.getEstadoFactura(idInstitucion, idFactura);
			Integer estadoFactura = facturaBean.getEstado();
			
			// Obtenemos el numero de cuenta
			String numeroCuenta = "";
			String codigoEntidad="";
			claves.clear();
			UtilidadesHash.set(claves, CenCuentasBancariasBean.C_IDINSTITUCION, facturaBean.getIdInstitucion());
			if ( facturaBean.getIdCuentaDeudor()!=null &&  !facturaBean.getIdCuentaDeudor().equals("")){
				UtilidadesHash.set(claves, CenCuentasBancariasBean.C_IDCUENTA, facturaBean.getIdCuentaDeudor());
				UtilidadesHash.set(claves, CenCuentasBancariasBean.C_IDPERSONA, facturaBean.getIdPersonaDeudor());
			}else{
				UtilidadesHash.set(claves, CenCuentasBancariasBean.C_IDCUENTA, facturaBean.getIdCuenta());
				UtilidadesHash.set(claves, CenCuentasBancariasBean.C_IDPERSONA, facturaBean.getIdPersona());
			}
			
			CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (this.getUserBean(request));
			Vector vCuentas = cuentasAdm.select(claves);
			if ((vCuentas != null) && (vCuentas.size() == 1)) {
				numeroCuenta = ((CenCuentasBancariasBean) vCuentas.get(0)).getNumeroCuenta();
				codigoEntidad=((CenCuentasBancariasBean) vCuentas.get(0)).getCbo_Codigo();
			}

			FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
			request.setAttribute("factura", 			facturaBean);
			request.setAttribute("importePendiente",	admFac.getImportePendientePago(miForm.getIdInstitucion().toString(),miForm.getIdFactura()));
			request.setAttribute("estadoFactura", 		estadoFactura);
			request.setAttribute("numeroCuenta", 		numeroCuenta);
			request.setAttribute("codigoEntidad", 		codigoEntidad);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}

	/**
	 * Inserta en BD una nueva renegociacion de un pago
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */
	protected String insertarRenegociar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction t = this.getUserBean(request).getTransaction();

		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			Integer idInstitucion 	= miForm.getIdInstitucion();
			String idFactura 		= miForm.getIdFactura();
			Integer estadoFactura 	= miForm.getDatosPagosRenegociarEstadoFactura();
			String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();  
			Integer idCuenta 		= null;
			int idFormaPago			= 0;
			boolean actualizarFacturaEnDisco = false;
			
			// Recuperamos la factura asociada a la renegociacion
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
			FacFacturaAdm facturaAdm = new FacFacturaAdm (this.getUserBean(request));
			Vector vFacturas = facturaAdm.selectByPK(claves);
			FacFacturaBean facturaBean = (FacFacturaBean) vFacturas.get(0);

			do {
				if (estadoFactura.intValue() == ConsPLFacturacion.PENDIENTE_COBRO){ // 2
					if (nuevaFormaPago.equalsIgnoreCase("porCaja")) {
						return exitoModal("messages.inserted.success", request);
					}
					if (nuevaFormaPago.equalsIgnoreCase("porBanco")) {
					    idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
						idCuenta = miForm.getDatosPagosRenegociarIdCuenta();
						break;
					}
				}
				if (estadoFactura.intValue() == ConsPLFacturacion.RENEGOCIADA) {
					if (nuevaFormaPago.equalsIgnoreCase("mismaCuenta")) {
						return exitoModal("messages.inserted.success", request);
					}
					if (nuevaFormaPago.equalsIgnoreCase("porCaja")) {
						idCuenta = null;
						idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
						break;
					}
					if (nuevaFormaPago.equalsIgnoreCase("porBanco")) {
					    idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
						idCuenta = miForm.getDatosPagosRenegociarIdCuenta();
						break;
					}
				}
				if(estadoFactura.intValue() == ConsPLFacturacion.DEVUELTA) {
					actualizarFacturaEnDisco = true;

					if (nuevaFormaPago.equalsIgnoreCase("mismaCuenta")) {
					    idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
						idCuenta = facturaBean.getIdCuenta();
						break;
					}
					if (nuevaFormaPago.equalsIgnoreCase("porCaja")) {
					    idFormaPago=ClsConstants.TIPO_FORMAPAGO_METALICO;
						idCuenta = null;
						break;
					}
					if (nuevaFormaPago.equalsIgnoreCase("porBanco")) {
					    idFormaPago = ClsConstants.TIPO_FORMAPAGO_FACTURA;
						idCuenta = miForm.getDatosPagosRenegociarIdCuenta();
						break;
					}
				}
				
				
				// Nunca deberia llegar aqui
				throwExcp("Operacion no permitida", new String[] {"modulo.facturacion"},new ClsExceptions("Operacion no permitida"), null);					
			
			} while (false);

			
			
			// Insertamos un nuevo registro en Fac_Renegociacion
			FacRenegociacionAdm renegociacionAdm = new FacRenegociacionAdm (this.getUserBean(request));
			FacRenegociacionBean renegociacionBean = new FacRenegociacionBean ();
			renegociacionBean.setComentario(miForm.getDatosPagosRenegociarObservaciones());
			renegociacionBean.setFechaRenegociacion("sysdate");
			renegociacionBean.setIdCuenta(idCuenta);
			renegociacionBean.setIdFactura(idFactura);
			renegociacionBean.setIdInstitucion(idInstitucion);
			renegociacionBean.setIdPersona(facturaBean.getIdPersona());
			renegociacionBean.setIdRenegociacion(renegociacionAdm.getNuevoID(idInstitucion, idFactura));
			renegociacionBean.setImporte(miForm.getDatosPagosRenegociarImportePendiente());
			
			t.begin();
			if(!renegociacionAdm.insert(renegociacionBean)) {
				throw new ClsExceptions("Error al insertar la renegociación: "+renegociacionAdm.getError());
			    //throwExcp("messages.general.error",new String[] {"modulo.facturacion"},new ClsExceptions("messages.inserted.error"),t); 
			}

			// Actualizamos la tabla FacFacturaInclidaEnDisquete
			if (actualizarFacturaEnDisco) {
				FacFacturaIncluidaEnDisqueteAdm facturaDiscoAdm = new FacFacturaIncluidaEnDisqueteAdm (this.getUserBean(request)); 
				if (!facturaDiscoAdm.updateRenegociacion(renegociacionBean.getIdInstitucion(), renegociacionBean.getIdFactura(), renegociacionBean.getIdRenegociacion())) {
					throw new ClsExceptions("Error al actualizar la factura incluida en disquete: "+facturaDiscoAdm.getError());
					//throwExcp("messages.general.error",new String[] {"modulo.facturacion"},new ClsExceptions("messages.inserted.error"),t); 
				}
			}

			// Aquí hay que actualizar el estado de la factura según haya quedado finalmente la factura
			// Actualizamos el idCuenta de la tabla FacFactura
			facturaBean.setIdCuenta(idCuenta);
			
	        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
			// Esto no paga nada por lo que no cambia nada
//	        facturaBean.setImpTotalPagadoPorBanco(new Double(facturaBean.getImpTotalPagadoPorCaja().doubleValue()+miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
//	        facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()+miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
//	        facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()-miForm.getDatosPagosRenegociarImportePendiente().doubleValue()));
	        // vamos a poner a pelo el estado renegociada.
			
			facturaBean.setEstado(new Integer(3)); // renegociada
			if (idFormaPago!=0) {
			    facturaBean.setIdFormaPago(new Integer(idFormaPago)); 
			}
	        
			if (facturaAdm.update(facturaBean)) {
		        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
				//facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
			    // Atencion, por un problema de la capa basica, los valores nulos no se actualizan.
			    // actualizo ahora la cuenta.
			    if (idCuenta==null) {
			        if (!facturaAdm.insertSQL("UPDATE FAC_FACTURA SET IDCUENTA=NULL WHERE IDINSTITUCION="+idInstitucion+" AND IDFACTURA="+idFactura)) {
			            throw new ClsExceptions("Error al actualizar la cuenta a nulo: "+facturaAdm.getError());        
			        }
			    }
			    
	        } else {
	            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
	        }
			
			t.commit();

			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, t); 
		}				
		return exitoModal("messages.inserted.success", request);
	}

	/**
	 * Funcion que recupera los datos para la visualizacion de un pago por tarjeta
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @exception  SIGAException  Errores de aplicación
	 */

	protected String pagoPorTarjeta(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		return "notImplemented";
	}
}
