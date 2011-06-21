/* VERSIONES:
 * ruben.fernandez - 09/03/2005 - Creacion
 * Modificado por david.sanchezp el 05/05/2005 - Modificaciones varias.
 */

/**
 * Clase action para la visualizacion y mantenimiento de los datos de facturaci�n para una Instituci�n.<br/>
 */

package com.siga.facturacionSJCS.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsEstadosPagosBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.facturacionSJCS.form.DatosDetallePagoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;


public class DatosDetallePagoAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		
		//recogemos el formulario
		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		
		//recogemos el modo de acceso desde el parametro de las pestanhas
		String accion = (String)request.getParameter("accion");
		try{
			//modos normales de acceso, se redirigen a abrir
			if ((accion != null)&&(((accion.equalsIgnoreCase("nuevo"))||accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("consulta")))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}
			else if ((miForm.getModo()!=null)&&(miForm.getModo().equalsIgnoreCase("abrirVolver")))
				return mapping.findForward(abrirVolver (mapping, miForm, request, response));
			else if ((miForm.getModo()!=null) && (miForm.getModo().equalsIgnoreCase("detalleLetrado")))
				return mapping.findForward(detalleLetrado (mapping, miForm, request, response));
			else if ((miForm.getModo()!=null) && (miForm.getModo().equalsIgnoreCase("detalleConcepto")))
				return mapping.findForward(detalleConcepto (mapping, miForm, request, response));
			else
				return super.executeInternal(mapping, formulario, request, response);
		}
		catch(SIGAException e){
			throw e;
		}
		catch(Exception e){
			return mapping.findForward("exception");
		}
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;					
	}
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return mapSinDesarrollar;
	}
	
	/** 
	 *  Funcion que atiende la accion abrirVolver.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrirVolver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward="inicio";
		//Recuperamos el USRBEAN
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
		
		//Recogemos los par�metros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idPago = (String)miform.getIdPago();
		String accion = "editar";
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//vector que pasaremos como resultado a la jsp
		Vector resultado = new Vector();
		
		//variable para recoger el nombre del colegio
		String nombreInstitucion = "";
		
		
		//comprobamos que el estado no sea ABIERTO
		boolean abierta = false;
		try{
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Integer idInstitucionInt = new Integer (idInstitucion);
			Integer idPagoInt = new Integer (idPago);
			Hashtable hash = (Hashtable)pagoAdm.getEstadoPago(idInstitucionInt,idPagoInt);
			if (((String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG)).equalsIgnoreCase(ClsConstants.ESTADO_PAGO_ABIERTO))
				abierta = true;
		}catch(Exception e){}
		
		if (!abierta){
			//Creamos una clausula where que nos servir� para consultar por idPago e idInstitucion
			String consultaPago = " where idInstitucion = " + idInstitucion +" and idPago =" + idPago + " ";
			
			try 
			{
				//Recuperamos los importes del pago, por cada persona
				resultado = this.getDetallePago(usr.getLocation(),idPago , request);
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				resultado = new Vector();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				throwExcp("Error: DatosDetallePagoAction",e,null);
			}
			//pasamos el resultado por la request
			request.setAttribute("resultado",resultado);
		}else{
			//pasamos por la request una variables para indicar que el estado es abierta
			request.setAttribute("estado","abierta");
		}
		
		try{
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(usr.getLocation().toString());
		}catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
		}
		
		
		//pasamos el nombre de la instituci�n, y los identificadores del pago y la institucion
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		request.setAttribute("idPagosJG",idPago);
		request.setAttribute("idInstitucion",idInstitucion);
		
		return forward;
	}
	
	/** 
	 * M�todo que atiende la accion abrir al pulsar sobre la pestanha de Detalle en el Mantenimiento del Pago.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String forward="inicio";
		
		//Recogemos los par�metros de las pestanhas 
		String idInstitucion = (String)request.getParameter("idInstitucion");
		String idPago = (String)request.getParameter("idPagosJG");
		String accion = (String)request.getParameter("accion");
		
		//Si estamos en abrir depues de haber insertado las pestanhas no nos pasa el modo
		if (accion==null){
			accion = "Edicion";
		}
		
		//Estado del Pago:
		int estado = 0;
		
		//comprobamos que el estado no sea ABIERTO
		boolean abierta = false;
		
		//Si es una nueva no tenemos idpago:
		if (idPago==null || idPago.equals(""))
			request.setAttribute("NUEVO","SI");
		
		try{
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Integer idInstitucionInt = new Integer (idInstitucion);
			Integer idPagoInt = new Integer (idPago);
			Hashtable hash = (Hashtable)pagoAdm.getEstadoPago(idInstitucionInt,idPagoInt);
			estado = Integer.parseInt((String)hash.get(FcsEstadosPagosBean.C_IDESTADOPAGOSJG));
			if (estado == Integer.parseInt(ClsConstants.ESTADO_PAGO_ABIERTO))
				abierta = true;
		}
		catch(Exception e){abierta = false;}
		
		if (!abierta){
			
			//	Recuperamos los importes del pago, por cada persona
			Vector resultado = new Vector();
			Hashtable totales = new Hashtable();
			try 
			{	
				resultado = (Vector)this.getDetallePago(idInstitucion, idPago, request);
				totales   = (Hashtable)this.getTotalesDetallePago(resultado, request);
			}
			catch(ClsExceptions e){
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction " + e.getMessage(), e,3);
				resultado = new Vector();
				totales   = new Hashtable();
			}
			catch(Exception e)
			{	
				ClsLogging.writeFileLogError("Error: DatosDetallePagoAction"+e.getMessage(), e,3);
				throwExcp("Error: DatosDetallePagoAction",e,null);
			}
			//pasamos el resultado por la request
			request.setAttribute("resultado",resultado);
			//pasamos los totales por la request
			request.setAttribute("totales",totales);
			
			//pasamos por la request una variables para indicar que el estado NO es abierta
			request.setAttribute("estado","NOabierta");
		}
		else {
			//pasamos por la request una variables para indicar que el estado es abierta
			request.setAttribute("estado","abierta");
		}
		
		String nombreInstitucion = "";
		try {
			//Consultamos el nombre de la institucion
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.getUserBean(request));
			nombreInstitucion = (String)institucionAdm.getNombreInstitucion(idInstitucion);
		}
		catch(ClsExceptions e){
			ClsLogging.writeFileLogError("Error: No se ha podido recuperar el nombre del Colegio", e,3);
		}
		
		//pasamos el nombre de la instituci�n, y los identificadores del pago y la institucion
		request.setAttribute("nombreInstitucion",nombreInstitucion);
		request.setAttribute("idPagosJG",idPago);
		request.setAttribute("idInstitucion",idInstitucion);
		request.setAttribute("estadoPago",Integer.toString(estado));
		request.setAttribute("modoOriginal",accion);
		return forward;
	}
	
	/**
	 * Funcion que obtiene los idPersona de los colegiados que intervienen en un pago,
	 * y consulta los detalles de pago para cada persona,
	 * y por cada persona, obtiene:
	 * 
	 * -IDPERSONA
	 * -NCOLEGIADO
	 * -NOMBRECOLEGIADO
	 * -IMPORTETOTAL
	 * -IMPORTERETENCION
	 * -IMPORTESJCS
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	protected Vector getDetallePago (String idInstitucion, String idPago, HttpServletRequest request) throws ClsExceptions
	{
		try {
			
			// Obtenemos los datos de los detalles de los pagos
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			// inc6307 // jbd // Cambio el getDetallePago por una version extendida que incluye el nombre y ncolegiado
							  // para ahorrarnos conexiones a bbdd.
							  // El vector pagos ya trae el nombre y apellidos
			// Vector pagos = pagoAdm.getDetallePago(new Integer(idInstitucion), new Integer(idPago), this.getUserBean(request).getLanguage());
			Vector pagos = pagoAdm.getDetallePagoExt(new Integer(idInstitucion), new Integer(idPago), this.getUserBean(request).getLanguage());
			
			return pagos;
		}
		catch (Exception e) {
			return null;
		}
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
	protected String detalleLetrado (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			String idPago = "", idInstitucion = "", idFacturacion = "";
			
			UsrBean usuario = this.getUserBean(request);
			
			DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
			idPago        = miform.getIdPago();
			idInstitucion = miform.getIdInstitucion();
			
			// Recuperamos el idFacturacion asociado
			FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = pagosJGAdm.selectByPK(claves);
			if ((v != null) && (v.size() == 1)) {
				idFacturacion = "" + ((FcsPagosJGBean)v.get(0)).getIdFacturacion();
			}
			
			// Recuperamos los datos de importes del pago a mostrar
			Vector resultado = new Vector();
			resultado = (Vector)this.getDetallePago(idInstitucion, idPago, request);
			
			// Generarmos el fichero XSL
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			String nombreFichero = paramAdm.getValor("" + idInstitucion, "FCS", "PATH_PREVISIONES", null);
			nombreFichero += File.separator + "PAGOS_LETRADO_" + idInstitucion + "_" + idFacturacion + "_" + idPago + ".XLS";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero));
			
			String cadena = UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.nColegiado") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.colegiado") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeSJCS") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeMovimientosVarios") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosFacturacion.literal.importeBruto") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.tipoIRPF") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.importeIRPF") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.importeRetenciones") + "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.importe")+ "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.datosPagos.literal.destinatario")+ "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "factSJCS.detalleFacturacion.literal.formadepago")+ "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "facturacion.pagosFactura.MedioPago.Banco")+ "\t" +
			UtilidadesString.getMensajeIdioma(usuario, "censo.datosCuentaBancaria.literal.cuenta");
			
			// cambio a formato DOS
			cadena += "\r\n";
			
			bw.write(cadena);
			
			if (resultado==null || resultado.size()==0) {			
//				cadena = UtilidadesString.getMensajeIdioma(usuario, "messages.noRecordFound");
//				bw.write(cadena);
			} 
			else { 
				for (int i = 0; i < resultado.size(); i++) {
					Hashtable fila = (Hashtable) resultado.get(i);
					String nombreColegiado = UtilidadesHash.getString(fila, "NOMBREPERSONA");
					String ncolegiado      = UtilidadesHash.getString(fila, "NCOLEGIADO");	
					String tipoIrpf        = UtilidadesHash.getString(fila, "TIPOIRPF");
					String irpf            = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"),2);
					String importeRetenciones = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"),2);
					String importeTotalSJCS   = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS"),2);
					String importeTotalMovimientoVarios = UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS"),2);
					String destinatario = UtilidadesHash.getString(fila, "DESTINATARIO");
					String formaPago    = UtilidadesHash.getString(fila, "FORMADEPAGO");
					String banco    = UtilidadesHash.getString(fila, "NOMBREBANCO");
					String codigoCuenta    = UtilidadesHash.getString(fila, "NUMEROCUENTA");
					float totalBrutos = Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios);
					if (totalBrutos<0) totalBrutos=0; 
					float totalTotal = Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios)+Float.parseFloat(irpf)+Float.parseFloat(importeRetenciones);
					if (totalTotal<0) totalTotal=0; 
					
					
					
					/*cadena = ncolegiado + "\t" + nombreColegiado + "\t" + importeTotalSJCS + " � \t" + importeTotalMovimientoVarios + " � \t" + importeRetenciones + " � \t" + totalBrutos + " � \t" + irpf + " � \n";*/
					cadena = ncolegiado + "\t" + nombreColegiado + "\t" + importeTotalSJCS.replace('.',',') + "\t" + importeTotalMovimientoVarios.replace('.',',') + "\t" + UtilidadesNumero.redondea(String.valueOf(totalBrutos),2).replace('.',',') + "\t" + tipoIrpf.replace('.',',') + "\t" + irpf.replace('.',',') + "\t" + importeRetenciones.replace('.',',') + "\t" + UtilidadesNumero.redondea(String.valueOf(totalTotal),2).replace('.',',') +"\t" + destinatario + "\t" + formaPago + "\t" + banco + "\t"+ codigoCuenta + "\t";
					// cambio a formato DOS
					cadena += "\r\n";
					
					bw.write(cadena);
				}
			}
			bw.close();
			
			// Descargamos el fichero
			File fichero = new File(nombreFichero);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "descargaFichero";		
	}
	
	protected String detalleConcepto (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			String idPago = "", idInstitucion = "", idFacturacion = "";
			
			DatosDetallePagoForm miform = (DatosDetallePagoForm)formulario;
			idPago        = miform.getIdPago();
			idInstitucion = miform.getIdInstitucion();
			
			// Recuperamos el idFacturacion asociado
			FcsPagosJGAdm pagosJGAdm = new FcsPagosJGAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable();
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(claves, FcsPagosJGBean.C_IDPAGOSJG, idPago);
			Vector v = pagosJGAdm.selectByPK(claves);
			if ((v != null) && (v.size() == 1)) {
				idFacturacion = "" + ((FcsPagosJGBean)v.get(0)).getIdFacturacion();
			}
			
			request.setAttribute("idInstitucion", idInstitucion);
			request.setAttribute("idFacturacion", idFacturacion);
			request.setAttribute("idPago", idPago);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		return "detalleConceptoDescargas";		
	}
	
	/**
	 * Funcion que obtiene los sumatorios de los importes del detalle pago   
	 * 
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 */
	protected Hashtable getTotalesDetallePago (Vector pagos, HttpServletRequest request) throws ClsExceptions
	{
		Hashtable totales = new Hashtable();
//		BigDecimal 
		Double total = 0.0, totalIrpf = 0.0, totalBrutos = 0.0, totalRetencion=0.0, totalTotal=0.0;
		Double importeIrpf=0.0, importeTotalSJCS=0.0, importeRetenciones=0.0, importeTotalMovimientoVarios=0.0,importeTotalTotal=0.0;
		boolean existeMV=false;
		Hashtable fila;
		
		try {
			
			// Obtenemos los datos de los detalles de los pagos
			FcsPagosJGAdm pagoAdm = new FcsPagosJGAdm (this.getUserBean(request));
			//Vector pagos = pagoAdm.getDetallePago(new Integer(idInstitucion), new Integer(idPago), this.getUserBean(request).getLanguage());			
			
			// Obtenemos el nombre de las personas del pago
			FcsFacturacionJGAdm facturacionAdm = new FcsFacturacionJGAdm (this.getUserBean(request));
			if (pagos != null) {
				for (int i = 0; i < pagos.size(); i++) {
					fila = (Hashtable) pagos.get(i);
			
					importeIrpf        = UtilidadesNumero.redondea(UtilidadesHash.getDouble(fila, "TOTALIMPORTEIRPF"),2);
					importeRetenciones = UtilidadesNumero.redondea(UtilidadesHash.getDouble(fila, "IMPORTETOTALRETENCIONES"),2);
					importeTotalSJCS   = UtilidadesNumero.redondea(UtilidadesHash.getDouble(fila, "TOTALIMPORTESJCS"),2);
					importeTotalMovimientoVarios = UtilidadesNumero.redondea(UtilidadesHash.getDouble(fila, "IMPORTETOTALMOVIMIENTOS"),2);
					if (!existeMV && importeTotalMovimientoVarios!=0){
						existeMV=true;
					}
		
					float aux = Float.parseFloat(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS")) + Float.parseFloat(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS")) + Float.parseFloat(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"))  + Float.parseFloat(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"));
					importeTotalTotal = UtilidadesNumero.redondea((new Double(aux)),2);
		
					totalBrutos = importeTotalSJCS + importeTotalMovimientoVarios;
					if (totalBrutos<0) totalBrutos=0.0;
					total      += totalBrutos;
					totalIrpf  += importeIrpf;
					totalRetencion  += importeRetenciones;
					if ( importeTotalTotal<0) importeTotalTotal=0.0;
					totalTotal  += importeTotalTotal;
					
				}
			}
			totales.put("totalBruto", UtilidadesNumero.redondea(total, 2));
			totales.put("totalIrpf", UtilidadesNumero.redondea(totalIrpf, 2));
			totales.put("totalTotal", UtilidadesNumero.redondea(totalTotal, 2));
			totales.put("totalRetencion",UtilidadesNumero.redondea(totalRetencion, 2));
			return totales;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
