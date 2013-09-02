/*
 * Created on 16-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.io.File;

import java.io.FileOutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.bea.common.security.xacml.IOException;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.facturacion.Facturacion;
import com.siga.facturacion.form.GestionarFacturaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author daniel.campos
 *
 */
public class GestionarFacturaPagosAction extends MasterAction {
	
	protected String nombreFichero = "";
	
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
				if (accion.equalsIgnoreCase("renegociar")){
					this.nombreFichero = "";
					mapDestino = renegociar(mapping, miForm, request, response);
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
				//	mapDestino = insertarRenegociar(mapping, miForm, request, response);
					this.nombreFichero = "";
					mapDestino = renegociar(mapping, miForm, request, response);
					break;
				}
				
				if (accion.equalsIgnoreCase("download")){
					mapDestino = download(mapping, miForm, request, response);
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
		        
		        if (facturaBean.getImpTotalPorPagar() >= 0) {
			
		        	if ((GstDate.compararFechas(pagoBean.getFecha(), facturaBean.getFechaEmision()) == 1 )) { 
		        			//|| (GstDate.compararFechas(pagoBean.getFecha(), facturaBean.getFechaEmision()) == 0 )) {
		        	
			        	if(pagosAdm.insert(pagoBean)) {
				        
			        		if (facturaAdm.update(facturaBean)) {
					        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
			        			facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
			        			t.commit();
			        		} else {
			        			t.rollback();
			        			throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
			        		}
			        	} else {
			        		t.rollback();
			        		throw new ClsExceptions("Error al insertar el pago de la factura: "+pagosAdm.getError()); 
			        	}
		        	} else if (GstDate.compararFechas(pagoBean.getFecha(), facturaBean.getFechaEmision()) == 0 ) {
			        	if(pagosAdm.insert(pagoBean)) {
					        
			        		if (facturaAdm.update(facturaBean)) {
					        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
			        			facturaAdm.actualizarEstadoFactura(facturaBean, this.getUserName(request));
			        			t.commit();
			        		} else {
			        			t.rollback();
			        			throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
			        		}
			        	} else {
			        		t.rollback();
			        		throw new ClsExceptions("Error al insertar el pago de la factura: "+pagosAdm.getError()); 
			        	}
		        	} else {	
		        		t.rollback();
		        		return exito("error en la fecha", request);
		        	}
				
		        } 
		    } else {
				throw new ClsExceptions("No se ha encontrado la factura: "+pagosAdm.getError());
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
			String cuentaBancariaFactura = "";
			String idCuentaUnica = "";
			String numerocuentaBancariaUnica = "";
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
				String idPersona = ((CenCuentasBancariasBean) vCuentas.get(0)).getIdPersona().toString();
				String idCuenta =((CenCuentasBancariasBean) vCuentas.get(0)).getIdCuenta().toString();
			    cuentaBancariaFactura = cuentasAdm.getNumeroCuentaFactura(idInstitucion.toString(), idPersona, idCuenta);
			}

			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) {
				cuentaBancariaFactura ="";
			}
			
			FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
			request.setAttribute("factura", 			facturaBean);
			request.setAttribute("importePendiente",	admFac.getImportePendientePago(miForm.getIdInstitucion().toString(),miForm.getIdFactura()));
			request.setAttribute("estadoFactura", 		estadoFactura);
			request.setAttribute("numeroCuenta", 		numeroCuenta);
			request.setAttribute("codigoEntidad", 		codigoEntidad);
			request.setAttribute("cuentaBancariaFactura", 		cuentaBancariaFactura);
			
			CenCuentasBancariasBean cuentaBancaria = cuentasAdm.getCuentaUnicaServiciosFactura(idInstitucion, idFactura);
			if(cuentaBancaria!=null){
				numerocuentaBancariaUnica = cuentasAdm.getNumeroCuentaFactura(idInstitucion.toString(), cuentaBancaria.getIdPersona().toString(), cuentaBancaria.getIdCuenta().toString());
				idCuentaUnica = cuentaBancaria.getIdCuenta().toString();
			}else{
				cuentaBancaria = new CenCuentasBancariasBean();	
			}

			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) {
				idCuentaUnica ="";
			}			
			
			request.setAttribute("idCuentaUnica",idCuentaUnica);
			request.setAttribute("cuentaUnicaServicios",cuentaBancaria);
			request.setAttribute("numerocuentaBancariaUnica",numerocuentaBancariaUnica);
			
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}
	
	
	protected String renegociar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String forward = "exception";
		UserTransaction tx 	= null;
		String strFacturas[]  = null;
		String personasDiferentes = "";
		Vector resultadoErrores = new Vector();
		Vector resultadoNumFacturas = new Vector();
		boolean comprobar = true;
		int indice = 0;
		File fichero = null;
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
		String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoRenegociacionesFallidasJava");		


		try {
			
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			UsrBean usr = this.getUserBean(request);
			Integer idInstitucion 	= new Integer(usr.getLocation());
			String nuevaFormaPago 	= miForm.getDatosPagosRenegociarNuevaFormaPago();
			Facturacion facturacion = new Facturacion(usr);
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
			String datosFacturas = miForm.getDatosFacturas();
			String idcuenta = "0";
			if (miForm.getDatosPagosRenegociarIdCuenta() != null){
				idcuenta = miForm.getDatosPagosRenegociarIdCuenta().toString();
			}
			if ("0".equals(idcuenta)) {
				idcuenta = miForm.getIdCuentaUnica();
			}
			if (datosFacturas == null) 
				strFacturas		= miForm.getIdFactura().split("##");
				else strFacturas = datosFacturas.split("##");
			
			Vector factDevueltasYRenegociadas = facturaAdm.getFacturasDevueltas(idInstitucion,strFacturas);
			personasDiferentes = facturaAdm.getSelectPersonas(idInstitucion, strFacturas);
			boolean isTodasRenegociadas = true;
			tx = usr.getTransactionPesada();
			tx.begin();
			for (int i = 0; i < factDevueltasYRenegociadas.size(); i++) {
				Row fila = (Row) factDevueltasYRenegociadas.get(i);
        		Hashtable<String, Object> htFila=fila.getRow();
        		String idFactura = UtilidadesHash.getString(htFila, FacFacturaBean.C_IDFACTURA);
        		Integer estadoFactura = UtilidadesHash.getInteger(htFila, FacFacturaBean.C_ESTADO);
        		Double impTotalPorPagar = UtilidadesHash.getDouble(htFila, FacFacturaBean.C_IMPTOTALPORPAGAR);
        		String numeroFactura = UtilidadesHash.getString(htFila, FacFacturaBean.C_NUMEROFACTURA);
				if (factDevueltasYRenegociadas.size() == 1){
					try{	
						facturacion.insertarRenegociar(new Integer(idInstitucion), idFactura, estadoFactura, 
								nuevaFormaPago, idcuenta,	impTotalPorPagar, 
								miForm.getDatosPagosRenegociarObservaciones(),true,false,null);
						
					}catch (SIGAException e) { 
						tx.rollback();
						if (e.getLiteral().equals(ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA)) 
							return forward=exito("facturacion.renegociar.aviso.cuentasDeBaja",request);
						else if (e.getLiteral().equals(ClsConstants.ERROR_RENEGOCIAR_PORCAJA)) 
							return forward=exito("facturacion.renegociar.aviso.mismaformacaja" ,request);
							else if (e.getLiteral().equals(ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE))
								return forward=exito("facturacion.renegociar.aviso.cuentaNoExiste",request);	
					}
						
					} else {
						try {
							facturacion.insertarRenegociar(new Integer(idInstitucion), idFactura, estadoFactura, 
										nuevaFormaPago, idcuenta,	impTotalPorPagar, 
											miForm.getDatosPagosRenegociarObservaciones(),true,true,null);
						} catch (SIGAException e) {
							isTodasRenegociadas = false;
							resultadoNumFacturas.add(numeroFactura);
							if (resultadoErrores.isEmpty()) {
								resultadoErrores.add(e.getLiteral());
							} else {
								while ((comprobar) && (indice < resultadoErrores.size())) {
									if (resultadoErrores.get(indice).equals(e.getLiteral())) 
										comprobar = false;
									else indice ++;
								}
								if (comprobar) {
									resultadoErrores.add(e.getLiteral());
								}
							}
							continue;
						}
					}

        		
			}
			tx.commit();
			
			if((isTodasRenegociadas) && (factDevueltasYRenegociadas.size() > 1)){
				forward = exito("facturacion.renegociacionMasiva.literal.procesoCorrectoRenegocia",request);
				
			}else if (isTodasRenegociadas == false) {
			
			// Descargamos el fichero
			fichero = this.obtenerFichero(idInstitucion, usr, resultadoNumFacturas);

			this.nombreFichero = fichero.getName().toString();
			
			rutaServidor = sRutaFisicaJava + rutaServidor;
			
			String barra = "";
			if (rutaServidor.indexOf("/") > -1){ 
				barra = "/";
			}
			if (rutaServidor.indexOf("\\") > -1){ 
				barra = "\\";
			}
			rutaServidor += barra + idInstitucion.toString();
		

				if (resultadoErrores.size() == 1){
					if (resultadoErrores.get(0) == ClsConstants.ERROR_RENEGOCIAR_CUENTABAJA)
						forward=exitoRefresco("facturacion.renegociar.aviso.cuentasDeBaja" ,request);
					else{
						if (resultadoErrores.get(0) == ClsConstants.ERROR_RENEGOCIAR_PORCAJA)
							forward=exitoRefresco("facturacion.renegociar.aviso.mismaformacaja",request);
						else {
							if (resultadoErrores.get(0) == ClsConstants.ERROR_RENEGOCIAR_CUENTANOEXISTE)
								forward=exitoRefresco("facturacion.renegociar.aviso.cuentaNoExiste",request);
						}
					}
				} else
					forward=exito("facturacion.renegociar.aviso.noRenegociadas",request);
				
			} else if (factDevueltasYRenegociadas.size() == 0) {
				forward=exito("facturacion.renegociar.aviso.noEstadoCorrecto",request);
			} else{
				return exitoModal("messages.inserted.success", request);
			}

		}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, tx); 
		}				
		return forward;
	}

	/** 
	 *  Funcion para generar el fichero de excel con las renegociaciones fallidas
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected File obtenerFichero(Integer idInstitucion, UsrBean usr, Vector resultadoNumFacturas) throws IOException ,SIGAException {
	
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
		String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoRenegociacionesFallidasJava");
		String sPrefijo 		= rp.returnProperty("facturacion.prefijo.ficherosRenegociaciones");
		String sExtension 		= rp.returnProperty("facturacion.extension.ficherosRenegociaciones");
		//String nombreFichero	= "";	
		File fichero = null;
        String fechaActual;	
		
		try{
//		Generamos el nombre del fichero.
		rutaServidor = sRutaFisicaJava + rutaServidor;
		File fDirectorio = new File(rutaServidor);
		
		if (!fDirectorio.exists()){
			fDirectorio.mkdirs();
		}
		
		String barra = "";
		if (rutaServidor.indexOf("/") > -1){ 
			barra = "/";
		}
		if (rutaServidor.indexOf("\\") > -1){ 
			barra = "\\";
		}
		rutaServidor += barra + idInstitucion.toString();
		File fDirectorio2 = new File(rutaServidor);
		
		if (!fDirectorio2.exists()){
			fDirectorio2.mkdirs();
		}
		
		Date fecha = new Date();
		
		String fechastr = String.valueOf(fecha.getDate()) + String.valueOf(fecha.getMonth() + 1) + String.valueOf(fecha.getYear() + 1900) + "_"; 
		
		fechaActual = String.valueOf(Calendar.getInstance().getTimeInMillis());	
		
		this.nombreFichero = barra + sPrefijo + fechastr + fechaActual + "." + sExtension;
		rutaServidor += this.nombreFichero;		
		
		// ***************************
		// * INICIO GENERACION EXCEL *
		// ***************************
		HSSFWorkbook libro = new HSSFWorkbook();						
		HSSFDataFormat df = libro.createDataFormat();			

		
		// GESTION DE FUENTES
		HSSFFont fuenteCabeceras = libro.createFont();
		HSSFFont fuenteNormal = libro.createFont();
		
		fuenteCabeceras.setFontHeightInPoints((short) 10);
		fuenteCabeceras.setColor((short) HSSFFont.COLOR_NORMAL);
		fuenteCabeceras.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		fuenteNormal.setFontHeightInPoints((short) 10);
		fuenteNormal.setColor((short) HSSFFont.COLOR_NORMAL);
		fuenteNormal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		
		// GESTION DE ESTILOS
		HSSFCellStyle estiloCeldaMoneda = libro.createCellStyle();
		HSSFCellStyle estiloCeldaPorcentaje = libro.createCellStyle();
		HSSFCellStyle estiloCeldaTexto = libro.createCellStyle();
		HSSFCellStyle estiloCeldaTitulo = libro.createCellStyle();

		estiloCeldaMoneda.setFont(fuenteNormal);
		estiloCeldaMoneda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		estiloCeldaMoneda.setDataFormat(df.getFormat("###,###,##0.00 €"));

		estiloCeldaPorcentaje.setFont(fuenteNormal);
		estiloCeldaPorcentaje.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		estiloCeldaPorcentaje.setDataFormat(df.getFormat("##0.00 %"));

		estiloCeldaTexto.setFont(fuenteNormal);
		estiloCeldaTexto.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));			
		
		estiloCeldaTitulo.setFont(fuenteCabeceras);
		estiloCeldaTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaTitulo.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		estiloCeldaTitulo.setFillPattern((short) HSSFCellStyle.SOLID_FOREGROUND);
		estiloCeldaTitulo.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex()); 						

		
		// GESTION DATOS
		HSSFSheet hoja = libro.createSheet();
		libro.setSheetName(0, "Renegociacion");

		HSSFRow filas = null;
		HSSFCell celdas = null;
		
		filas = hoja.createRow(0);
		
		celdas = filas.createCell(0);
		celdas.setCellValue(new HSSFRichTextString(UtilidadesString.getMensajeIdioma(usr, "facturacion.consultamorosos.literal.factura")));
		celdas.setCellStyle(estiloCeldaTitulo);
		celdas.setCellType(HSSFCell.CELL_TYPE_STRING);	
		
		for (int i = 0; i < resultadoNumFacturas.size(); i++) {										
			String numeroFactura = (String) resultadoNumFacturas.get(i);;
			
			filas = hoja.createRow(1+i);
			
			celdas = filas.createCell(0);
			celdas.setCellValue(new HSSFRichTextString(numeroFactura));					
			celdas.setCellStyle(estiloCeldaTexto);
			celdas.setCellType(HSSFCell.CELL_TYPE_STRING);
		}
	
	for (short i=0; i<1; i++)
		hoja.autoSizeColumn(i);


	
	// ************************
	// * FIN GENERACION EXCEL *
	// ************************

	
	// Descargamos el fichero
	fichero = new File(nombreFichero);
	
	if(fichero==null || fichero.exists()){
		throw new SIGAException("messages.general.error.ficheroNoExiste"); 
	} else {
		fichero.createNewFile();
		FileOutputStream out = new FileOutputStream(rutaServidor);
		libro.write(out);
		out.close();
	}	

	}catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}		
	return fichero;
	}
	
	/** 
	 *  Funcion que atiende la accion download. Descarga los ficheros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
					
		String directorioFisico = "facturacion.directorioFisicoPagosBancosJava";
		String directorio 		= "facturacion.directorioFisicoRenegociacionesFallidasJava";
		//String nombreFichero 	= "";
		String pathFichero		= "";
		String idInstitucion	= "";
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		
		try{		
			GestionarFacturaForm form 		= (GestionarFacturaForm)formulario;
			
			if (this.nombreFichero != "")  {
			
			    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				pathFichero 		= p.returnProperty(directorioFisico) + p.returnProperty(directorio);			
				
				String barra = "";
				if (pathFichero.indexOf("/") > -1){ 
					barra = "/";
				}
				if (pathFichero.indexOf("\\") > -1){ 
					barra = "\\";
				}
				
				idInstitucion			= this.getIDInstitucion(request).toString();	
				
		/*		String rutaServidor = pathFichero + barra + idInstitucion.toString();
				
				File fDirectorio2 = new File(rutaServidor);
				
				String resultado[] = fDirectorio2.list();
				
				if (resultado.length > 0) 
					nombreFichero = resultado[resultado.length - 1].toString();*/
				
				
			//	pathFichero += File.separator + idInstitucion + File.separator + nombreFichero;
				
				pathFichero += File.separator + idInstitucion + File.separator + this.nombreFichero;
				
				// Descargamos el fichero
				File fichero = new File(pathFichero);
				
				if(fichero==null || !fichero.exists()){
					return exito("messages.general.error.ficheroNoExiste",request);
				}

			} else {
				return exito("messages.general.error.ficheroNoExiste",request);
			}

			
		}catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}

		request.setAttribute("nombreFichero", nombreFichero);
		request.setAttribute("rutaFichero", pathFichero);
		
		return "descargaFichero";
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
