package com.siga.servlets;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacPagosPorCajaAdm;
import com.siga.beans.FacPagosPorCajaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysPeticionCompraSuscripcionAdm;
import com.siga.beans.PysPeticionCompraSuscripcionBean;
import com.siga.general.CarroCompra;
import com.siga.general.Firma;
import com.siga.general.PagoTarjeta;
					   

/**
 * Servlet para el Control de la Confirmacion del Pago del TPV<br>
 * 1. Valida los datos recibidos<br>
 * 2. Si la validacion ha ido bien:<br>
 * 2.1. Recupera del contexto el carro de la compra y actualiza en nuestra Base de Datos la compra.<br>
 * 2.2. Devuelve una pagina HTML OK requerida por CECA para saber que hemos recibido su confirmacion de operacion realizada.<br>
 * 3. Si la validacion ha ido mal:<br>
 * 3.1. Devuelve una pagina HTML ERROR<br>
 * 
 * @author david.sanchezp
 * @since Mar 4, 2005 
 * 
 */
public class SIGATPVControl extends HttpServlet {
	
	public void init(){		
		com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: init()", 1);		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		PrintWriter out;	
		boolean finCorrecto = false;
		
		try {					
			com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: doPost()", 1);
	
			if (this.validarDatos(request)) {
				//Actualizamos en Base de Datos:
				finCorrecto = this.actualizarDatos(request);

				//Si hemos actualizado sin problemas:
				if (finCorrecto) {
					//Construyo la pagina OK de SIGA al TPV:
					response.setContentType("text/html");
					out = response.getWriter();
					out.println(this.pintarHtml("$*$OKY$*$"));
				    out.close();
				} else {
					//Construyo la pagina de Error de SIGA al TPV:
					response.setContentType("text/html");
					out = response.getWriter();
					out.println(this.pintarHtml("ERROR: los datos recibidos no son correctos."));
				    out.close();				
				}
			} else {
				//Construyo la pagina de Error de SIGA al TPV:
				response.setContentType("text/html");
				out = response.getWriter();
				out.println(this.pintarHtml("ERROR: los datos recibidos no son correctos."));
			    out.close();				
			}
			
			//Borramos del contexto la variable de aplicacion:				
			this.getServletContext().removeAttribute(request.getParameter("Num_operacion"));
		}
		catch (Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en el doPost()", e, 1);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException {
		PrintWriter out;
		
		try {
			com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: doGet()", 1);
			//Construyo la pagina de Error de SIGA al TPV:
			response.setContentType("text/html");
			out = response.getWriter();
			out.println(this.pintarHtml("ERROR: metodo no soportado."));
		    out.close();
		}
		catch (Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en el doGet()", e, 1);			
		}
	}
	
	public void destroy(){
		com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: destroy()", 1);
	}
	
	private String pintarHtml(String body){
		String pagina = "";
	
		pagina += "<HTML><HEAD><TITLE>Pagina de Error</TITLE></HEAD><BODY>";
		pagina += body;			
		pagina += "</BODY></HTML>";
		
		return pagina;
	}
	
	private boolean validarDatos(HttpServletRequest request){
		String merchantId="", acquirerBin="", terminalId="", operacion="", importe="", moneda="";
		String exponente="", referencia="", firma="", autorizacion="", where=null;
		String clave = null; //Clave para descifrar la firma
		String idInstitucion=null;
		boolean validar = true;
		//Administrador por defecto para solo consultas:
		
		try {
			com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: validarDatos()", 1);
			
			//Recogemos los datos del TPV:
			merchantId = request.getParameter("MerchantID");
			acquirerBin = request.getParameter("AcquirerBIN");
			terminalId = request.getParameter("TerminalID");
			operacion = request.getParameter("Num_operacion");
			importe = request.getParameter("Importe");
			moneda = request.getParameter("Tipo_moneda");
			exponente = request.getParameter("Exponente");
			referencia = request.getParameter("Referencia");
			firma = request.getParameter("Firma");
			autorizacion = request.getParameter("Num_aut");
			
			//Consulto en Base de Datos la clave y la institucion por el numero de operacion:
			idInstitucion = operacion.substring(1,5);
			
			GenParametrosAdm parametrosAdm = new GenParametrosAdm(UsrBean.UsrBeanAutomatico(idInstitucion));
			clave = parametrosAdm.getValor(idInstitucion,"PYS",ClsConstants.PASSWORD_FIRMA,null);
			//Validamos los datos:
			//1.El campo Referencia no debe ser vacio:
			if (referencia.trim().equals("")) 
				validar = false;
			else {
				//2. Validez de la firma:
				//Calculo la firma con los datos:
				String valorFirma = "";
				Firma firmaTPV = new Firma();
				firmaTPV.setClave(clave);
				firmaTPV.setMerchantId(merchantId);
				firmaTPV.setAcquirerBin(acquirerBin);
				firmaTPV.setTerminalId(terminalId);
				firmaTPV.setOperacion(operacion);
				firmaTPV.setImporte(importe);
				firmaTPV.setMoneda(moneda);
				firmaTPV.setExponente(exponente);
				firmaTPV.setReferencia(referencia);
				firmaTPV.setFirma(firmaTPV.calcularFirma());
				if (!firmaTPV.getFirma().equals(firma)) 
					validar = false;
				else {
					//Chequeo la variable de aplicacion con el numero de operacion: (tengo almacenado el carro en ella)
					if (this.getServletContext()==null || this.getServletContext().getAttribute(operacion) == null) 
						validar = false;
				}
			}
		}
		catch (Exception e) {
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en el validarDatos()",e, 1);
			validar = false;
		}
		return validar;
	}
	
	private boolean actualizarDatos(HttpServletRequest request){
		String operacion=""; //Numero de Operacion
		int tipo = 0;//Tipo de Operacion a realizar
		boolean finCorrecto = false;
		
		try {
			com.atos.utils.ClsLogging.writeFileLog("SIGATVPControl: actualizarDatos()", 1);
			
			//Recogemos el numero de la operacion del TPV:
			operacion = request.getParameter("Num_operacion");
					
			//Actualizamos la Compra en nuestra Base de Datos segun el tipo de operacion:
			tipo = new Integer(operacion.substring(0,1)).intValue();
			
			switch(tipo){
				//FACTURACION DE PRODUCTOS Y SERVICIOS
				case 1: finCorrecto = this.actualizarFacturacion1(request); break;
				//PAGO POR CAJA
				case 2: finCorrecto = this.actualizarFacturacion2(request); break;
				default: break;
			}			
		}
		catch (Exception e) {
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en el actualizarDatos()",e, 1);
			
		}
		return finCorrecto;
	}
	
	//FACTURACION DE PRODUCTOS Y SERVICIOS
	private boolean actualizarFacturacion1(HttpServletRequest request){
		String merchantId=null, acquirerBin=null, terminalId=null, operacion=null, importe=null, moneda=null;
		String exponente=null, referencia=null, firma=null, autorizacion=null, fecha=null;
		PysPeticionCompraSuscripcionAdm peticionAdm;
		Hashtable hashTemporal = new Hashtable();
		CarroCompra carro;
		UserTransaction tx = null;
		boolean finCorrecto = false;
		
		try {
			//Recogemos los datos del TPV:
			merchantId 	= request.getParameter("MerchantID");
			acquirerBin = request.getParameter("AcquirerBIN");
			terminalId = request.getParameter("TerminalID");
			operacion = request.getParameter("Num_operacion");
			importe = request.getParameter("Importe");
			moneda = request.getParameter("Tipo_moneda");
			exponente = request.getParameter("Exponente");
			firma = request.getParameter("Firma");
			autorizacion = request.getParameter("Num_aut");
			referencia = request.getParameter("Referencia");
			
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			fecha = operacion.substring(15,operacion.length());			
			Calendar calendario = Calendar.getInstance();
			calendario.setTimeInMillis(Long.parseLong(fecha));
			Date date = calendario.getTime();
			String fechaFormateada = sdf.format(date);
		
			//Recuperamos el carro del contexto:
			carro = (CarroCompra)this.getServletContext().getAttribute(request.getParameter("Num_operacion"));
			peticionAdm = new PysPeticionCompraSuscripcionAdm(carro.getUsrBean()); 
			tx = carro.getUsrBean().getTransaction(); 
			tx.begin();
			//Insertamos el carro en Base de Datos:
			Long idPeticion = peticionAdm.insertarCarro(carro);
			
			//UPDATE de la tabla Pys_PeticionCompraSuscripcion:
			hashTemporal.clear();
			hashTemporal.put(PysPeticionCompraSuscripcionBean.C_IDINSTITUCION,operacion.substring(1,5));
			hashTemporal.put(PysPeticionCompraSuscripcionBean.C_IDPETICION,idPeticion);
			hashTemporal.put(PysPeticionCompraSuscripcionBean.C_NUM_AUT,autorizacion);
			hashTemporal.put(PysPeticionCompraSuscripcionBean.C_NUM_OPERACION,operacion);
			hashTemporal.put(PysPeticionCompraSuscripcionBean.C_REFERENCIA,referencia);			
			String claves[] = {PysPeticionCompraSuscripcionBean.C_IDINSTITUCION,PysPeticionCompraSuscripcionBean.C_IDPETICION};
			String campos[] = {PysPeticionCompraSuscripcionBean.C_NUM_AUT,PysPeticionCompraSuscripcionBean.C_NUM_OPERACION,
							   PysPeticionCompraSuscripcionBean.C_REFERENCIA};
			peticionAdm.updateDirect(hashTemporal,claves,campos);
			tx.commit();
			finCorrecto = true; 
		}
		catch (Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en actualizarFacturacion1()",e, 1);
			try {
				tx.rollback();
			} catch(Exception e1) {
				
			}
		}
		return finCorrecto;
	}

	//PAGO POR CAJA
	private boolean actualizarFacturacion2(HttpServletRequest request){
		String merchantId=null, acquirerBin=null, terminalId=null, operacion=null, importe=null, moneda=null;
		String exponente=null, referencia=null, firma=null, autorizacion=null, fecha=null;
		FacPagosPorCajaAdm facPagosPorCajaAdm;
		Hashtable hashTemporal = new Hashtable();
		PagoTarjeta pagoTarjeta;
		UserTransaction tx = null;
		boolean finCorrecto = false;
		try {
			//Recogemos los datos del TPV:
			merchantId 	= request.getParameter("MerchantID");
			acquirerBin = request.getParameter("AcquirerBIN");
			terminalId 	= request.getParameter("TerminalID");
			operacion 	= request.getParameter("Num_operacion");
			importe 	= request.getParameter("Importe");
			moneda 		= request.getParameter("Tipo_moneda");
			exponente 	= request.getParameter("Exponente");
			firma 		= request.getParameter("Firma");
			autorizacion = request.getParameter("Num_aut");
			referencia 	= request.getParameter("Referencia");
			
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			fecha = operacion.substring(18,operacion.length());			
			Calendar calendario = Calendar.getInstance();
			calendario.setTimeInMillis(Long.parseLong(fecha));
			Date date = calendario.getTime();
			String fechaFormateada = sdf.format(date);
			//Insertamos la operacion en Base de Datos:
			pagoTarjeta 		= (PagoTarjeta)this.getServletContext().getAttribute(request.getParameter("Num_operacion"));
			Integer user     = new Integer(pagoTarjeta.getUsrBean().getUserName());
			facPagosPorCajaAdm 	= new FacPagosPorCajaAdm(pagoTarjeta.getUsrBean());
			Hashtable hash 		= new Hashtable();
			hash.put(FacPagosPorCajaBean.C_IDINSTITUCION,pagoTarjeta.getIdInstitucion());
			hash.put(FacPagosPorCajaBean.C_IDFACTURA	,pagoTarjeta.getIdFactura());
			hash.put(FacPagosPorCajaBean.C_IDPAGOPORCAJA,pagoTarjeta.getIdPagoPorCaja());
			hash.put(FacPagosPorCajaBean.C_FECHA		,fechaFormateada);
			hash.put(FacPagosPorCajaBean.C_IMPORTE		,pagoTarjeta.getImporte());
			hash.put(FacPagosPorCajaBean.C_OBSERVACIONES,pagoTarjeta.getDescripcion());
			hash.put(FacPagosPorCajaBean.C_NUMEROAUTORIZACION		,autorizacion);
			hash.put(FacPagosPorCajaBean.C_REFERENCIA	,referencia);
			hash.put(FacPagosPorCajaBean.C_CONTABILIZADO,"N");
			hash.put(FacPagosPorCajaBean.C_TARJETA		,"S");
			tx = pagoTarjeta.getUsrBean().getTransaction(); 
			tx.begin();	
			facPagosPorCajaAdm.insert(hash);
			// Llamamos a actualizar estado factura
			FacFacturaAdm facturaAdm = new FacFacturaAdm(pagoTarjeta.getUsrBean());
			
			FacFacturaBean facturaBean = null;
			Hashtable ht = new Hashtable();
		    ht.put(FacFacturaBean.C_IDINSTITUCION,pagoTarjeta.getIdInstitucion());
		    ht.put(FacFacturaBean.C_IDFACTURA,pagoTarjeta.getIdFactura());
		    Vector v = facturaAdm.selectByPK(ht);
		    if (v!=null && v.size()>0) {
		        facturaBean = (FacFacturaBean) v.get(0);
		        
		        // AQUI VAMOS A MODIFICAR LOS VALORES DE IMPORTES
		        facturaBean.setImpTotalPagadoPorCaja(new Double(facturaBean.getImpTotalPagadoPorCaja().doubleValue()+new Double(pagoTarjeta.getImporte()).doubleValue()));
		        facturaBean.setImpTotalPagadoSoloTarjeta(new Double(facturaBean.getImpTotalPagadoSoloTarjeta().doubleValue()+new Double(pagoTarjeta.getImporte()).doubleValue()));
		        facturaBean.setImpTotalPagado(new Double(facturaBean.getImpTotalPagado().doubleValue()+new Double(pagoTarjeta.getImporte()).doubleValue()));
		        facturaBean.setImpTotalPorPagar(new Double(facturaBean.getImpTotalPorPagar().doubleValue()-new Double(pagoTarjeta.getImporte()).doubleValue()));
		        
		        if (facturaAdm.update(facturaBean)) {
			        // AQUI VAMOS A MODIFICAR EL VALOR DE ESTADO
					facturaAdm.actualizarEstadoFactura(facturaBean, new Integer(pagoTarjeta.getUsrBean().getUserName()));
		        } else {
		            throw new ClsExceptions("Error al actualizar los importes de la factura: "+facturaAdm.getError());
		        }

				tx.commit();

		    } else {
		        throw new ClsExceptions("No se ha encontrado la factura buscada: "+pagoTarjeta.getIdInstitucion()+ " "+pagoTarjeta.getIdFactura());
		    }
						

			finCorrecto = true;
		}
		catch (Exception e){
			com.atos.utils.ClsLogging.writeFileLogError("SIGATVPControl: Excepcion en actualizarFacturacion2()",e, 1);
			try{tx.rollback();}
			catch(Exception e1){}
		}
		return finCorrecto;
	}
}
