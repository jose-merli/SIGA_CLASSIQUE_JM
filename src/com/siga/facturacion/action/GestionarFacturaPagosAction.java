/*
 * Created on 16-mar-2005
 *
 */
package com.siga.facturacion.action;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.bea.common.security.xacml.IOException;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacHistoricoFacturaAdm;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.FacPagosPorCajaBean;
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
			String medioPago = (String)v.get(1);
			
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
			String ultimaFechaRequest = (String)request.getParameter("ultimaFecha");
			modo = miForm.getModo();
			request.setAttribute("idInstitucion", 		miForm.getIdInstitucion());
			request.setAttribute("idFactura", 			miForm.getIdFactura());
			//request.setAttribute("importePendiente", 	miForm.getDatosPagosCajaImportePendiente());
			request.setAttribute("importePendiente", 	admFac.getImportePendientePago(miForm.getIdInstitucion().toString(),miForm.getIdFactura()));
			
			if(ultimaFechaRequest != null && !ultimaFechaRequest.equals("")){
				ultimaFechaRequest = GstDate.getFormatedDateShort(ClsConstants.DATE_FORMAT_SHORT_SPANISH,ultimaFechaRequest);	
			}
			
			request.setAttribute("ultimaFecha", ultimaFechaRequest);

		} catch (Exception e) { 
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
		UsrBean usr = this.getUserBean(request);
		UserTransaction t = usr.getTransaction();
		try {
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			FacPagosPorCajaAdm pagosAdm = new FacPagosPorCajaAdm(this.getUserBean(request));
			FacHistoricoFacturaAdm historicoFacturaAdm = new FacHistoricoFacturaAdm (this.getUserBean(request));
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
		        Double impTotalPagadoPorCajaFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPagadoPorCaja().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue(), 2);
				Double impTotalPagadoSoloCajaFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPagadoSoloCaja().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue(),2);
				Double impTotalPagadoFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPagado().doubleValue()+miForm.getDatosPagosCajaImporteCobrado().doubleValue(),2);
		    	Double impTotalPorPagarFac = UtilidadesNumero.redondea(facturaBean.getImpTotalPorPagar().doubleValue()-miForm.getDatosPagosCajaImporteCobrado().doubleValue(),2);
		    	
		    	facturaBean.setImpTotalPagadoPorCaja(new Double(impTotalPagadoPorCajaFac));
		        facturaBean.setImpTotalPagadoSoloCaja(new Double(impTotalPagadoSoloCajaFac));
		        facturaBean.setImpTotalPagado(new Double(impTotalPagadoFac));
		        facturaBean.setImpTotalPorPagar(new Double(impTotalPorPagarFac));
		        
		        if (facturaBean.getImpTotalPorPagar() >= 0) {			
		        	
		        	SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			        Date dFecha = GstDate.convertirFechaHora(pagoBean.getFecha());
			        String sFecha = sdf.format(dFecha);
			        
			        Date dFechaEmision = GstDate.convertirFechaHora(facturaBean.getFechaEmision());
			        String sFechaEmision = sdf.format(dFechaEmision);
		        	
		        	if ((GstDate.compararFechas(sFecha, sFechaEmision) >= 0 )) { 		        	
			        	if(pagosAdm.insert(pagoBean)) {				        
			        		if (facturaAdm.update(facturaBean)) {
			        			// Vamos a modificar el valor de estado
			        			facturaAdm.consultarActNuevoEstadoFactura(facturaBean, this.getUserName(request),true);
			        			boolean resultado = Boolean.FALSE;
			        			try{
			        				
			        				resultado=historicoFacturaAdm.insertarHistoricoFacParametros(String.valueOf(facturaBean.getIdInstitucion()),facturaBean.getIdFactura(),4,pagoBean.getIdPagoPorCaja(), 
			        								null, null,null,null, null, null, null);

			        				 if(!resultado){
			        						ClsLogging.writeFileLog("### No se ha insertado en el histórico de la facturación ", 7);
			        				 }
			        			} catch (Exception e) {
			        				ClsLogging.writeFileLogError("@@@ ERROR: No se ha insertado el histórico de la facturación",e,3);
			        			}
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
		        		return exito(UtilidadesString.getMensajeIdioma(usr, "facturacion.pagosFactura.PagoCaja.Error.Fecha"), request);		        		 
		        	}				
		        } 
		        
		    } else {
				throw new ClsExceptions("No se ha encontrado la factura: "+pagosAdm.getError());
			}
		    
		} catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, t); 
		}
		
		return exitoModal("messages.inserted.success", request);
	}

	/**
	 * Funcion que calcula las formas de renegociacion en Facturacion > Facturas > Pagos
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
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			
			modo = miForm.getModo();
			Integer idInstitucion = miForm.getIdInstitucion();
			String idFactura = miForm.getIdFactura();
			
			// Obtenemos la factura
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FacFacturaBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FacFacturaBean.C_IDFACTURA, idFactura);
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm (user);			
			Vector vFactura = facturaAdm.selectByPK(claves);
			
			if (vFactura==null || vFactura.size()<1) {
				throw new SIGAException("Error al no obtener datos de la factura " + idFactura);				
			}
			
			// JPT: Encuentra la factura
			FacFacturaBean beanFactura = (FacFacturaBean) vFactura.get(0); 
			request.setAttribute("beanFactura", beanFactura);
			
			Integer numeroFacturasPorBanco = 0;
			
			// JPT: Comprueba si es una factura por banco y tiene cuenta
			if (beanFactura.getEstado() != new Integer(ClsConstants.ESTADO_FACTURA_CAJA) && 
					(
						(beanFactura.getIdCuentaDeudor() != null && !beanFactura.getIdCuentaDeudor().equals("")) || 
						(beanFactura.getIdCuenta() != null && !beanFactura.getIdCuenta().equals(""))
					)) {
			
				// JPT: Obtiene la cuenta bancaria de la factura
				Hashtable hCuentaBancaria = new Hashtable();
				UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDINSTITUCION, beanFactura.getIdInstitucion());
				if (beanFactura.getIdCuentaDeudor() != null && !beanFactura.getIdCuentaDeudor().equals("")){
					UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDCUENTA, beanFactura.getIdCuentaDeudor());
					UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDPERSONA, beanFactura.getIdPersonaDeudor());
				}else{
					UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDCUENTA, beanFactura.getIdCuenta());
					UtilidadesHash.set(hCuentaBancaria, CenCuentasBancariasBean.C_IDPERSONA, beanFactura.getIdPersona());
				}
				
				CenCuentasBancariasAdm cuentasAdm = new CenCuentasBancariasAdm (user);
				Vector vCuentas = cuentasAdm.selectByPK(hCuentaBancaria);
				if (vCuentas != null && vCuentas.size() == 1) {
					CenCuentasBancariasBean beanCuentaBancaria = (CenCuentasBancariasBean) vCuentas.get(0);
					request.setAttribute("beanCuentaBancaria", beanCuentaBancaria);
				}
				
				numeroFacturasPorBanco = 1;				
			}
			request.setAttribute("numeroFacturasPorBanco", numeroFacturasPorBanco);
			
			// JPT - Renegociacion 118: Obtiene el identificador de la persona
			Long idPersona = (beanFactura.getIdCuentaDeudor()!=null && !beanFactura.getIdCuentaDeudor().equals("")) ? beanFactura.getIdPersonaDeudor() : beanFactura.getIdPersona();

			// JPT - Renegociacion 118: Consulta las cuentas bancarias activas de cargos de la persona
			CenCuentasBancariasAdm cuentasBancariasAdm = new CenCuentasBancariasAdm(user);			
			List listaCuentasCargoActivas = cuentasBancariasAdm.getCuentasCargo(idPersona, idInstitucion);
			request.setAttribute("numeroCuentasPersona", listaCuentasCargoActivas.size());	
			
			String ultimaFechaRequest = (String)request.getParameter("ultimaFecha");
			if(ultimaFechaRequest != null && !ultimaFechaRequest.equals("")){
				ultimaFechaRequest = GstDate.getFormatedDateShort(ClsConstants.DATE_FORMAT_SHORT_SPANISH,ultimaFechaRequest);	
			}			
			request.setAttribute("ultimaFecha", ultimaFechaRequest);
			
		} catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.facturacion"}, e, null); 
		}				
		return modo;
	}
	
	/**
	 * Funcion que realiza la renegociacion:
	 * - Facturacion > Gestion de Cobros y Recobros
	 * - Facturacion > Facturas > Pagos
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String renegociar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		this.nombreFichero = "";
		
		String forward = "exception";	
		UserTransaction tx 	= null;
		String strFacturas[]  = null;
		
		Vector resultadoErroresFichero = new Vector();
		Vector resultadoNumFacturas = new Vector();

		try {			
			GestionarFacturaForm miForm = (GestionarFacturaForm) formulario;
			String nuevaFormaPago = miForm.getDatosPagosRenegociarNuevaFormaPago();
			String observaciones = miForm.getDatosPagosRenegociarObservaciones();
			String fechaRenegociacion = miForm.getDatosRenegociarFecha();
			String datosFacturas = miForm.getDatosFacturas();
			
			UsrBean usr = this.getUserBean(request);
			Integer idInstitucion = new Integer(usr.getLocation());
			
			String idcuenta = "0";
			
			if (miForm.getDatosPagosRenegociarIdCuenta() != null){
				idcuenta = miForm.getDatosPagosRenegociarIdCuenta().toString();
			}
			
			if ("0".equals(idcuenta)) {
				idcuenta = miForm.getIdCuentaUnica();
			}
			
			if (datosFacturas == null) {
				strFacturas = miForm.getIdFactura().split("##");
			} else { 
				strFacturas = datosFacturas.split("##");
			}
			
			// JPT: Oracle no admite listas de más de mil elementos
			if (strFacturas.length>1000) {
				throw new SIGAException("facturacion.consultamorosos.error.renegociarMilFacturas");
			};			
			
			// JPT: Pone las facturas en formato de lista
			String listaIdsFacturas = "(";
			for (int i=0; i<strFacturas.length; i++) {
				String idFactura = strFacturas[i];					
				if (i == 0) {				
					listaIdsFacturas += idFactura;
				} else {
					listaIdsFacturas += "," + idFactura;
				}					
			}
			listaIdsFacturas += ")";			

			Facturacion facturacion = new Facturacion(usr);			
			
			// JPT: Obtiene las facturas a renegociar
			FacFacturaAdm facturaAdm = new FacFacturaAdm(usr);
			Vector vFacturasRenegociar = facturaAdm.getFacturasRenegociar(idInstitucion, listaIdsFacturas);
			
			String smsError = "";
			if (vFacturasRenegociar.size() == 0) {				
				if (miForm.getModo().equalsIgnoreCase("insertarRenegociar")) { // Facturacion > Facturas > Pagos
					forward = "pagoRenegociar";
					request.setAttribute("modo", "pagoRenegociar");
					
				} else { // Facturacion > Gestion de Cobros y Recobros
					forward = "renegociar";
					request.setAttribute("modo", "renegociarDevolucion");
				}
				smsError = "facturacion.renegociar.aviso.noEstadoCorrecto";
				return exito(smsError, request);
			}			
			
			tx = usr.getTransactionPesada();
			tx.begin();
			
			// JPT: Recorro todas las facturas a renegociar
			for (int i = 0; i < vFacturasRenegociar.size(); i++) {
				
				// Obtengo la factura actual
				FacFacturaBean facturaBean = (FacFacturaBean) vFacturasRenegociar.get(i);
        		
        		int resultadoRenegociacion = facturacion.insertarRenegociar(
        				facturaBean, 
        				nuevaFormaPago, 
        				idcuenta, 
        				observaciones, 
        				fechaRenegociacion, 
        				false);
        		
        		if (resultadoRenegociacion > 0) {
        			switch (resultadoRenegociacion) {
        				case 1: smsError = "facturacion.renegociar.aviso.mismaformacaja"; break;
        				case 2: smsError = "facturacion.renegociar.aviso.formapago"; break;
        				case 3: smsError = "facturacion.renegociar.aviso.noRenegocia"; break;
        				case 4: smsError = "facturacion.renegociar.aviso.noRenegocia"; break;
        				case 5: smsError = "facturacion.renegociar.aviso.cuentaNoExiste"; break;        				
        			}
        			
        			resultadoNumFacturas.add(facturaBean.getNumeroFactura());
					resultadoErroresFichero.add(smsError);
        		}
			}
						
			if (miForm.getModo().equalsIgnoreCase("insertarRenegociar")) { // Facturacion > Facturas > Pagos
				forward = "pagoRenegociar";
				if (smsError.equals("")) {
					tx.commit();
					request.setAttribute("modo", "renegociar");
					return exitoModal("messages.updated.success",request);
					
				} else {
					tx.rollback();
					request.setAttribute("modo", "pagoRenegociar");
					return exito(smsError, request);
				}
			
			} else { // Facturacion > Gestion de Cobros y Recobros
				forward = "renegociar";				
				tx.commit();
				if (smsError.equals("")) {
					request.setAttribute("modo", "renegociarDevolucion");
					return exitoModal("messages.updated.success",request);
					
				} else {
					request.setAttribute("modo", "renegociar");				
				
					// Descargamos el fichero
					File fichero = this.obtenerFichero(idInstitucion, usr, resultadoNumFacturas, resultadoErroresFichero);
			
					this.nombreFichero = fichero.getName().toString();
					
					ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
					String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
					String rutaServidor = rp.returnProperty("facturacion.directorioFisicoRenegociacionesFallidasJava");			
					rutaServidor = sRutaFisicaJava + rutaServidor;
			
					String barra = "";
					if (rutaServidor.indexOf("/") > -1) { 
						barra = "/";
					}
				
					if (rutaServidor.indexOf("\\") > -1){ 
						barra = "\\";
					}
					rutaServidor += barra + idInstitucion.toString();
				
					// JPT: Esto hace saltar la funcion refrescarLocal() de renegociar.jsp
					return exitoRefresco("facturacion.renegociar.aviso.errorFichero", request);
				}
			}

		} catch (Exception e) { 
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
	protected File obtenerFichero(Integer idInstitucion, UsrBean usr, Vector resultadoNumFacturas, Vector resultadoErroresFichero) throws IOException ,SIGAException {
	
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String sRutaFisicaJava = rp.returnProperty("facturacion.directorioFisicoPrevisionesJava");
		String rutaServidor 	= rp.returnProperty("facturacion.directorioFisicoRenegociacionesFallidasJava");
		String sPrefijo 		= rp.returnProperty("facturacion.prefijo.ficherosRenegociaciones");
		String sExtension 		= rp.returnProperty("facturacion.extension.ficherosRenegociaciones");
		File fichero = null;
        String fechaActual;	
		
		try{
//		Generamos el nombre del fichero.
		rutaServidor = sRutaFisicaJava + rutaServidor;
		FileHelper.mkdirs(rutaServidor);
		
		String barra = "";
		if (rutaServidor.indexOf("/") > -1){ 
			barra = "/";
		}
		if (rutaServidor.indexOf("\\") > -1){ 
			barra = "\\";
		}
		rutaServidor += barra + idInstitucion.toString();
		FileHelper.mkdirs(rutaServidor);
		
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

		celdas = filas.createCell(1);
		celdas.setCellValue(new HSSFRichTextString(UtilidadesString.getMensajeIdioma(usr, "facturacion.consultamorosos.literal.incidencia")));
		celdas.setCellStyle(estiloCeldaTitulo);
		celdas.setCellType(HSSFCell.CELL_TYPE_STRING);			
		
		for (int i = 0; i < resultadoNumFacturas.size(); i++) {										
			String numeroFactura = (String) resultadoNumFacturas.get(i);;
			String incidenciaFactura = UtilidadesString.getMensajeIdioma(usr, resultadoErroresFichero.get(i).toString());
			
			filas = hoja.createRow(1+i);
			
			celdas = filas.createCell(0);
			celdas.setCellValue(new HSSFRichTextString(numeroFactura));					
			celdas.setCellStyle(estiloCeldaTexto);
			celdas.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			celdas = filas.createCell(1);
			celdas.setCellValue(new HSSFRichTextString(incidenciaFactura));					
			celdas.setCellStyle(estiloCeldaTexto);
			celdas.setCellType(HSSFCell.CELL_TYPE_STRING);			
		}
	
	for (short j=0; j<1; j++)
		hoja.autoSizeColumn(j);


	
	// ************************
	// * FIN GENERACION EXCEL *
	// ************************

	
	// Descargamos el fichero
	fichero = new File(nombreFichero);
	
	if(fichero==null || fichero.exists()){
		throw new SIGAException("messages.general.error.ficheroNoExiste"); 
	} else {
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
