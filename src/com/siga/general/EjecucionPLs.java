/*
 * Created on May 12, 2005
 */
package com.siga.general;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.facturacionSJCS.form.DatosGeneralesPagoForm;


/**
 * @author david.sanchezp
 * Métodos estaticos que implementan la llamada a los procedimientos de ejecucion de los distintos PLs realizados.
 * Todos los metodos devuelven un array con los valores de salida definidos en el PL.
 * Normalmente en la posicion 0 se devuelve el CODRETORNO del PL que si vale 0 indica que ha finalizado correctamente. 
 * Pero esto depende de como se definio el PL.
 * @version 1.0
 * @since 12-05-2005 
 */

public class EjecucionPLs {

	/**
	 * PL para obtener el importe del Colegiado.
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLObtenerImporteColegiado(String idInstitucion, String idPago, String idPersona) throws ClsExceptions
	{
		Object[] param_in  = new Object[3]; 		// Parametros de entrada del PL
		String resultado[] = new String[14]; 	// Parametros de salida del PL
	
		try {
			// Obtenemos los importes a abonar, desglosados
			param_in[0] = idInstitucion;
			param_in[1] = idPago;
			param_in[2] = idPersona;
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_OBTENER_IMPORT_COLEG (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 14, param_in);

			// Datos de vuelta
			//  P_IMPORTETURNOS 
			//	P_IMPORTEGUARDIAS 
			//	P_IMPORTESOJ 
			//	P_IMPORTEEJG 
			//	P_IMPORTEMOVIMIENTOS 
			//	P_IMPORTERETENCIONES 
			//	P_IRPFTURNOS 
			//	P_IRPFGUARDIAS
			//	P_IRPFSOJ 
			//	P_IRPFEJG 
			//	P_IRPFMOVIMIENTOS 
			//	P_IDPERSONASOCIEDAD 
			//	P_CODRETORNO 
			//	P_DATOSERROR 
		} 
		catch (Exception e) { 
	    	resultado[12] = "1"; 		// ERROR P_CODRETORNO
	    	resultado[13] = "ERROR"; 	// ERROR P_DATOSERROR
		}
	    return resultado;
	}

	/**
	 * PL para el pago de Turnos de Oficio con Criterio por Puntos:
	 * @param importeTurnos
	 * @param miform
	 * @param usuModificacion
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLTurnosPuntos(String importeTurnos, DatosGeneralesPagoForm miform, String usuModificacion) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			//EXEC PROC_FCS_PAG_TURNSOFIC_PNTOS
			//Parametros de entrada del PL
			resultado = new String[3];
			if (Double.parseDouble(importeTurnos) == 0) {
				resultado[0] = "0";
	        	resultado[1] = "0"; 
	        	resultado[2] = "FIN SIN EJECUCION DEL PL";         	
			} else {
				param_in = new Object[6];
				param_in[0] = miform.getIdInstitucion();
				param_in[1] = miform.getIdPagosJG();
				param_in[2] = miform.getIdFacturacion();
				param_in[3] = importeTurnos;
				param_in[4] = usuModificacion;
				param_in[5] = miform.getValoresFacturacion();		        
		        //Ejecucion del PL
		        resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAG_TURNSOFIC_PNTOS (?,?,?,?,?,?,?,?,?)}", 3, param_in);
			}
		} catch (Exception e){
			resultado[0] = "0";
	    	resultado[1] = "1"; //ERROR P_CODRETORNO
	    	resultado[2] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}


	/**
 	 * PL para el pago de Turnos de Oficio con Criterio por Facturacion:
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws SIGAException
	 */
	public static Double ejecutarPL_PagoTurnosOficio (Integer idInstitucion, Integer idPago, Integer usuario) throws SIGAException
	{
		try {
			Object[] paramIn = new Object[3];
			paramIn[0] = idInstitucion.toString(); // IDINSTITUCION
			paramIn[1] = idPago.toString(); // IDPAGO 
			paramIn[2] = usuario.toString(); // USUARIO
			
	    	String resultado[] = new String[3];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAGO_TURNOS_OFI(?,?,?,?,?,?)}", 3, paramIn);
	    	if (!resultado[1].equalsIgnoreCase("0")) {
	    		//ClsLogging.writeFileLog("Error en PL = "+(String)resultado[3],3);
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el Pago de Turnos de Justicia Gratuita."+
	    				"\nError en PL = "+(String)resultado[3]);
	    	}
	    	return new Double(resultado[0]);
	    	
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}

	/**
 	 * PL para el pago de Guardias con Criterio por Facturacion:
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws SIGAException
	 */
	public static Double ejecutarPL_PagoGuardias (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
	{
		try {
			Object[] paramIn = new Object[3];
			paramIn[0] = idInstitucion.toString(); // IDINSTITUCION
			paramIn[1] = idFacturacion.toString(); // IDFACTURACION 
			paramIn[2] = usuario.toString(); // USUARIO
			
	    	String resultado[] = new String[3];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAGO_GUARDIAS(?,?,?,?,?,?)}", 3, paramIn);
	    	if (!resultado[1].equalsIgnoreCase("0")) {
	    		//ClsLogging.writeFileLog("Error en PL = "+(String)resultado[3],3);
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el Pago de Guardias de Justicia Gratuita"+
	    				"\nError en PL = "+(String)resultado[3]);
	    	}
	    	return new Double(resultado[0]);
	    	
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}

	/**
 	 * PL para el pago de EJG con Criterio por Facturacion:
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws SIGAException
	 */
	public static Double ejecutarPL_PagoEJG (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
	{
		try {
			Object[] paramIn = new Object[3];
			paramIn[0] = idInstitucion.toString(); // IDINSTITUCION
			paramIn[1] = idFacturacion.toString(); // IDFACTURACION 
			paramIn[2] = usuario.toString(); // USUARIO
			
	    	String resultado[] = new String[3];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAGO_EJG(?,?,?,?,?,?)}", 3, paramIn);
	    	if (!resultado[1].equalsIgnoreCase("0")) {
	    		//ClsLogging.writeFileLog("Error en PL = "+(String)resultado[3],3);
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el Pago de Expedientes EJG de Justicia Gratuita"+
	    				"\nError en PL = "+(String)resultado[3]);
	    	}
	   	
	    	return new Double(resultado[0]);
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}

	/**
 	 * PL para el pago de SOJ con Criterio por Facturacion:
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws SIGAException
	 */
	public static Double ejecutarPL_PagoSOJ (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
	{
		try {
			Object[] paramIn = new Object[3];
			paramIn[0] = idInstitucion.toString(); // IDINSTITUCION
			paramIn[1] = idFacturacion.toString(); // IDFACTURACION
			paramIn[2] = usuario.toString(); // USUARIO
			
	    	String resultado[] = new String[3];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAGO_SOJ(?,?,?,?,?,?)}", 3, paramIn);
	    	if (!resultado[1].equalsIgnoreCase("0")) {
	    		//ClsLogging.writeFileLog("Error en PL = "+(String)resultado[3],3);
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el Pago de Expedientes SOJ de Justicia Gratuita"+
	    				"\nError en PL = "+(String)resultado[3]);
	    	}
	   	
	    	return new Double(resultado[0]);
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}

	
	/**
	 * PL para actualizar el idPagosJG de los movimientos varios.
	 * @param miform
	 * @param usuModificacion
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLMovimientos(String idInstitucion, String idPago) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			//EXEC PROC_FCS_PAGAR_MOVIMIENTOS
			//Parametros de entrada del PL
			param_in = new Object[2];
			param_in[0] = idInstitucion;
			param_in[1] = idPago;
	        resultado = new String[2];
	        //Ejecucion del PL
	        resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_PAGAR_MOVIMIENTOS (?,?,?,?)}", 2, param_in);
		} catch (Exception e){
	    	resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL para aplicar las retenciones.
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLRetenciones(String idInstitucion, String idPago) throws ClsExceptions{
		Object[] param_in = new Object[2]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idPago;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_APLICAR_RETENCIONES_IRPF (?,?,?,?)}", 2, param_in);
		} catch (Exception e) { 
	    	resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL para aplicar las retenciones judiciales.
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLRetencionesJudiciales(String idInstitucion, String idPago, String idPersona) throws ClsExceptions{
		Object[] param_in = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
			param_in[0] = idInstitucion;			
			param_in[1] = idPago;
			param_in[2] = idPersona;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_RETENCIONES_SJCS.PROC_FCS_RETEN_JUDICIALES (?,?,?,?,?)}", 2, param_in);			
		} catch (Exception e) { 
	    	resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * Ejecuta el PL para calcular el IRPF al generar abonos.
	 * @param idinstitucion
	 * @param idpersona
	 * @param importe
	 * @param irpf
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLCalcularIRPF(String idinstitucion, String idpersona, String importe, String irpf) throws ClsExceptions {
		Object[] param_in = new Object[4];
		// parametros de entrada
		param_in[0] = idinstitucion;
		param_in[1] = idpersona; 
		param_in[2] = importe;
		param_in[3] = irpf;
		
		String resultadoPl[] = new String[4];
		resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS_RETENCION.PROC_FCS_CALCULAR_IRPF(?,?,?,?,?,?,?,?)}", 4, param_in);
		return resultadoPl;
	}

	/**
	 * 
	 * @param idinstitucion
	 * @param idpersona
	 * @param esSociedad
	 * @param irpf
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLCalcularIRPF_Pagos(String idinstitucion, String idpersona, boolean esSociedad) throws ClsExceptions 
	{
		Object[] param_in = new Object[3];
		param_in[0] = idpersona; 
		param_in[1] = (esSociedad == true?"1":"0"); 
		param_in[2] = idinstitucion;
		
		String resultadoPl[] = new String[3];
		resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_CALCULAR_IRPF(?,?,?,?,?,?)}", 3, param_in);
		return resultadoPl;
	}

	
	/**
	 * Ejecuta el PL de retenciones por todos los usuarios.
	 * @param idInstitucion
	 * @param idPago
	 * @param fechaInicio
	 * @param fechaFin
	 * @param usuarioModificacion
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLRetencionesVarias(String idInstitucion, String idPago, String fechaInicio, String fechaFin, String usuarioModificacion) throws ClsExceptions{
		Object[] param_in = new Object[5]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idPago;
			param_in[2] = fechaInicio;
			param_in[3] = fechaFin;
			param_in[4] = usuarioModificacion;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS_RETENCION.PROC_FCS_RETEN_JUDIC_VARIOS (?,?,?,?,?,?,?)}", 2, param_in);
		} catch (Exception e) { 
	    	resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	/**
	 * PL que calcula la retencion por persona usado en generar el abono del pago.
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLRetencionPersona(String idInstitucion, String idPago, String idPersona) throws ClsExceptions{
		Object[] param_in = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idPago;
			param_in[2] = idPersona;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACT_SJCS_RETENCION.PROC_FCS_RETENCION_PERSONA (?,?,?,?,?)}", 2, param_in);
		} catch (Exception e) { 
	    	resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL que calcula los Totales de un Pago.<br>
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLTotalesPago(String idInstitucion, String idPago) throws ClsExceptions {
		Object[] param_in = new Object[2]; //Parametros de entrada del PL
		String resultado[] = new String[12]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idPago;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_TOTALES_PAGO (?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", 12, param_in);
		} catch (Exception e) {
			resultado[0]  = "0"; //P_TOTALPAGADO 
			resultado[1]  = "0"; //P_TOTALFACTURADO
			resultado[2]  = "0"; //P_TOTALTURNOS
			resultado[3]  = "0"; //P_TOTALGUARDIAS
			resultado[4]  = "0"; //P_TOTALASISTENCIAS
			resultado[5]  = "0"; //P_TOTALACTUACIONES
			resultado[6]  = "0"; //P_TOTALSOJ
			resultado[7]  = "0"; //P_TOTALEJG
			resultado[8]  = "0"; //P_TOTALMOVIMIENTOSVARIOS
			resultado[9]  = "0"; //P_RETENCIONES
	    	resultado[10] = "1"; //ERROR P_CODRETORNO
	    	resultado[11] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL que calcula los datos de números de Turnos, Guardias, Asistencias presentar en el Informe de Certificados de Pagos.
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLNumerosCertificadoPago(String idInstitucion, String idFacturacion) throws ClsExceptions {
		Object[] param_in = new Object[2]; //Parametros de entrada del PL
		String resultado[] = new String[8]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idFacturacion;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_NUMEROS_CERT_PAGO (?,?,?,?,?,?,?,?,?,?)}", 8, param_in);
		} catch (Exception e) {
			resultado[0] = "0"; //P_NUMTURNOS
			resultado[1] = "0"; //P_NUMASISTENCIASINF
			resultado[2] = "0"; //P_NUMGUARDIAS_SUP_ASIS
			resultado[3] = "0"; //P_NUMASISTENCIASPOST
			resultado[4] = "0"; //P_NUMGUARDIASINF
			resultado[5] = "0"; //P_NUMGUARDIASSUP
	    	resultado[6] = "1"; //ERROR P_CODRETORNO
	    	resultado[7] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL que calcula los Totales de un Informe de Certificado de Pagos.
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLTotalesCertificadoPagos(String idInstitucion, String idFacturacion) throws ClsExceptions {
		Object[] param_in = new Object[2]; //Parametros de entrada del PL
		String resultado[] = new String[7]; //Parametros de salida del PL
	
		try {
	 		//Parametros de entrada del PL
	        param_in[0] = idInstitucion;			
			param_in[1] = idFacturacion;
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_TOTAL_CERTIF_PAGO (?,?,?,?,?,?,?,?,?)}", 7, param_in);
		} catch (Exception e) {
			resultado[0] = "0"; //P_TOTAL FACTURACION
			resultado[1] = "0"; //P_TOTAL TURNOS
			resultado[2] = "0"; //P_TOTAL ASISTENCIAS
			resultado[3] = "0"; //P_TOTAL MOVIMIENTOSVARIOS
			resultado[4] = "0"; //P_TOTAL CON MOVIMIENTOS INCLUIDO
			resultado[5] = "1"; //ERROR P_CODRETORNO
	    	resultado[6] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	
	/**
	 * PL que da de baja un servicio
	 * @param idInstitucion
	 * @param idTipoServicio
	 * @param idServio
	 * @param idServicioInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_BajaServicio (String idInstitucion, String idTipoServicio, String idServio, String idServicioInstitucion, String fechaEfectiva) throws ClsExceptions {

		Object[] paramIn = new Object[5]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idTipoServicio;
	        paramIn[2] = idServio;
	        paramIn[3] = idServicioInstitucion;
	        paramIn[4] = fechaEfectiva;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_BAJA_SERVICIO (?,?,?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL que genera una suscripcion automatica
	 * @param idInstitucion
	 * @param idTipoServicio
	 * @param idServio
	 * @param idServicioInstitucion
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_SuscripcionAutomatica (String idInstitucion, String idTipoServicio, String idServio, String idServicioInstitucion, String fechaEfectiva, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[6]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idTipoServicio;
	        paramIn[2] = idServio;
	        paramIn[3] = idServicioInstitucion;
	        paramIn[4] = fechaEfectiva;
	        paramIn[5] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_SUSCRIPCION_AUTO (?,?,?,?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}

	/**
	 * PL que elimina una suscripcion automatica
	 * @param idInstitucion
	 * @param idTipoServicio
	 * @param idServio
	 * @param idServicioInstitucion
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_EliminarSuscripcion (String idInstitucion, String idTipoServicio, String idServio, String idServicioInstitucion, String usuario, String alta, String fechaAlta, String incluirBajaManual) throws ClsExceptions {

		Object[] paramIn = new Object[8]; //Parametros de entrada del PL
		String resultado[] = new String[3]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idTipoServicio;
	        paramIn[2] = idServio;
	        paramIn[3] = idServicioInstitucion;
	        paramIn[4] = usuario;
	        paramIn[5] = alta;
	        paramIn[6] = fechaAlta;
	        paramIn[7] = incluirBajaManual;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_ELIMINAR_SUSCRIPCION(?,?,?,?,?,?,?,?,?,?,?)}", 
													3, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	/**
	 * PL que realiza una revision de letrado
	 * @param idInstitucion
	 * @param idPersona
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_RevisionSuscripcionesLetrado (String idInstitucion, String idPersona, String fecha, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[4]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idPersona;
	        paramIn[2] = fecha;
	        paramIn[3] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_LETRADO (?,?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPL_Revision_Auto (String idInstitucion,  String fecha, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = fecha;
	        paramIn[2] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_AUTO (?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	public static String[] ejecutarPL_RevisionAnticiposLetrado (String idInstitucion, String idPersona, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idPersona;
	        paramIn[2] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PROC_SIGA_ACT_ANTICIPOSCLIENTE (?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}	
	
	public static String[] ejecutarPL_RevisionCuentaBanco (String idInstitucion, String idPersona, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idPersona;
	        paramIn[2] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_REVISION_CUENTABANCO (?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPL_ActualizarCuentaBanco (String idInstitucion, String idPersona,String idCuenta, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[4]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idPersona;
	        paramIn[2] = idCuenta;
	        paramIn[3] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_ACTUALIZAR_CUENTABANCO (?,?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_NUMREGISTRO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	/**
	 * PL que almacena los letrados para hacer guardias en una tabla temporal con un indice posicion + idinstitucion.
	 * @param idInstitucion
	 * @param idPersona
	 * @param usuario
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_OrdenaColegiadosGuardia (Integer idInstitucion, Integer idTurno, Integer idGuardia) {

		Object[] paramIn = new Object[4]; //Parametros de entrada del PL
		String resultado[] = new String[3]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion.toString();
	        paramIn[1] = idTurno.toString();
	        paramIn[2] = idGuardia.toString();

	        // RGG 05/09/2008 No aplica saltos y compensaciones. Lo hace el algoritmo. 
	        //paramIn[3] = "1"; // con saltos y compensaciones
	        paramIn[3] = "0"; // con saltos y compensaciones

	        // Ejecucion del PL pkg_siga_ordenacion.ordena_colegiados_guardia:
			resultado = ClsMngBBDD.callPLProcedure("{call  pkg_siga_ordenacion.ordena_colegiados_guardia (?,?,?,?,?,?,?)}", 
													3, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "0"; 	// P_CONTADOR
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR
	    	resultado[2] = "ERROR"; // ERROR P_DATOSERROR
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPLExportarTurnosOficio(String idInstitucion, String idFacturacion,String idPago, String idPersona, String pathFichero, String fichero, String cabeceras, String idioma) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[8];
			param_in[0] = idInstitucion;
			param_in[1] = idFacturacion;
			param_in[2] = (idPago == null?"":idPago); 		// IDPAGO
			param_in[3] = (idPersona == null?"":idPersona); // IDPERSONA
			param_in[4] = pathFichero;
			param_in[5] = fichero;
			param_in[6] = cabeceras;
			param_in[7] = idioma;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_TURNOS_OFI (?,?,?,?,?,?,?,?,?,?)}", 2, param_in);
			
		} catch (Exception e){
			resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPLExportarGuardias(String idPersona, String idInstitucion, String idFacturacion, 
			String idPago, String pathFicheros, String nombreFichero, String cabeceras, String idioma) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[8];
			param_in[0] = idInstitucion; 					// IDINSTITUCION
			param_in[1] = idFacturacion; 					// IDFACTURACION
			param_in[2] = (idPago == null?"":idPago); 		// IDPAGO
			param_in[3] = (idPersona == null?"":idPersona); // IDPERSONA
			param_in[4] = pathFicheros;						// PATH_FICHEROS 
			param_in[5] = nombreFichero;
			param_in[6] = cabeceras;
			param_in[7] = idioma;

			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.proc_fcs_exportar_guardias (?,?,?,?,?,?,?,?,?,?)}", 2, param_in);
			
		} catch (Exception e){
			resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPLExportarSoj(String idInstitucion, String idFacturacion,String idPago, String idPersona, String pathFichero, String fichero, String cabeceras) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[7];
			param_in[0] = idInstitucion;
			param_in[1] = idFacturacion;
			param_in[2] = (idPago == null?"":idPago); 		// IDPAGO
			param_in[3] = (idPersona == null?"":idPersona); // IDPERSONA
			param_in[4] = pathFichero;
			param_in[5] = fichero;
			param_in[6] = cabeceras;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_SOJ (?,?,?,?,?,?,?,?,?)}", 2, param_in);
			
		} catch (Exception e){
			resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPLExportarEjg(String idInstitucion, String idFacturacion,String idPago, String idPersona, String pathFichero, String fichero, String cabeceras) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[7];
			param_in[0] = idInstitucion;
			param_in[1] = idFacturacion;
			param_in[2] = (idPago == null?"":idPago); 		// IDPAGO
			param_in[3] = (idPersona == null?"":idPersona); // IDPERSONA
			param_in[4] = pathFichero;
			param_in[5] = fichero;
			param_in[6] = cabeceras;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_EJG (?,?,?,?,?,?,?,?,?)}", 2, param_in);
			
		} catch (Exception e){
			resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}

	
	// PROC_SIGA_ACTESTADOABONO
	public static String[] ejecutarProcPROC_SIGA_ACTESTADOABONO(String idInstitucion, String idAbono, String usu) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[3];
			param_in[0] = idInstitucion;
			param_in[1] = idAbono;
			param_in[2] = usu;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PROC_SIGA_ACTESTADOABONO (?,?,?,?,?)}", 2, param_in);
			
		} catch (Exception e){
			resultado[0] = "1"; //ERROR P_CODRETORNO
	    	resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return resultado;
	}	
	
	public static String [] ejecutarPL_ActualizarDatosLetrado (Integer idInstitucion, Long idPersona, Integer idTipoCambio, Long idDireccion, Integer usuMod) 
	{
		Object[] param_in; 			//Parametros de entrada del PL
		String resultado[] = null; 	//Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in  = new Object[5];
			param_in[0] = idPersona.toString();
			param_in[1] = idInstitucion.toString();
			param_in[2] = idTipoCambio.toString();
			param_in[3] = (idDireccion == null)?"":idDireccion.toString();
			param_in[4] = usuMod.toString();
			
			// Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_CENSO.ACTUALIZARDATOSLETRADO (?,?,?,?,?,?,?)}", 2, param_in);
		} 
		catch (Exception e){
			resultado[0] = "-1";     
	    	resultado[1] = "ERROR al ejecutar el procedimiento pkg_siga_censo.ActualizarDatosLetrado";
		}
	    
	    return resultado;
	}
	//TODO aaa Revisar como se debe hacer la llamada al PL
	
	public static String ejecutarPLPKG_SIGA_FECHA_EN_LETRA(String fecha, String tipo, String idioma) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String fechaEnLetra= null; //Parametros de salida del PL
	
		try {
			StringBuffer query = new StringBuffer();
			query.append("SELECT  PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra ('");
			query.append(fecha);
			query.append("','");
			query.append(tipo);
			query.append("',");
			query.append(idioma);
			query.append(") FECHA from dual ");
			
			Hashtable hashQuery = UtilidadesBDAdm.ejecutaQuery(query.toString());
			fechaEnLetra = (String) hashQuery.get("FECHA");
			
			
			////resultado = null;
			/*param_in = new Object[3];
			param_in[0] = fecha;
			param_in[1] = tipo;
			param_in[2] = idioma;
		        
			//Ejecucion del PL
		    
			resultado = ClsMngBBDD.callPLFunction("call PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra (?,?,?,?)", param_in);
			*/
			
		} catch (Exception e){
			//resultado = e.getMessage(); //ERROR P_CODRETORNO
	    	throw new ClsExceptions(e.getMessage());//resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return fechaEnLetra;
	}
	public static String ejecutarF_SIGA_NUMEROENLETRA (String numero, String idioma) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String numeroEnLetra= null; //Parametros de salida del PL
	
		try {
			StringBuffer query = new StringBuffer();
			query.append("SELECT  f_siga_numeroenletra ('");
			query.append(numero);
			query.append("',");
			query.append(idioma);
			query.append(") NUMERO from dual ");
			
			Hashtable hashQuery = UtilidadesBDAdm.ejecutaQuery(query.toString());
			numeroEnLetra = (String) hashQuery.get("NUMERO");
			
			
			////resultado = null;
			/*param_in = new Object[3];
			param_in[0] = fecha;
			param_in[1] = tipo;
			param_in[2] = idioma;
		        
			//Ejecucion del PL
		    
			resultado = ClsMngBBDD.callPLFunction("call PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra (?,?,?,?)", param_in);
			*/
			
		} catch (Exception e){
			//resultado = e.getMessage(); //ERROR P_CODRETORNO
	    	throw new ClsExceptions(e.getMessage());//resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return numeroEnLetra;
	}
	

	/**
	 * PL para aplicar las retenciones.
	 * @param idInstitucion
	 * @param idPago
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarF_SIGA_OBTENER_ANTICIPOS2(
			String idinstitucion, String idTipoClave, String idClave,
			String idClaveInstitucion, String tipo, String idPeticion,
			String idPersona) throws ClsExceptions{
		Object[] param_in; 			//Parametros de entrada del PL
		String resultado[] = null; 	//Parametros de salida del PL
	
		try {
			resultado = new String[1];
			param_in  = new Object[7];
			param_in[0] = idinstitucion;
			param_in[1] = idTipoClave;
			param_in[2] = idClave;
			param_in[3] = idClaveInstitucion;
			param_in[4] = tipo;
			param_in[5] = idPeticion;
			param_in[6] = idPersona;
			
			// Ejecucion del PL
		    resultado = ClsMngBBDD.callPLFunction(
		    		"{call F_SIGA_OBTENER_ANTICIPOS (?,?,?,?,?,?,?)}", 
		    		1, param_in);
		    if("".equals(resultado[0])){
				resultado = new String[2];
				resultado[0] = "-1";     
		    	resultado[1] = "ERROR. F_SIGA_OBTENER_ANTICIPOS no devuelve valores";
		    	return resultado;

		    }
		    
		    return resultado[0].split("#");
		} 		
		catch (Exception e){
			resultado = new String[2];
			resultado[0] = "-1";     
	    	resultado[1] = "ERROR al ejecutar F_SIGA_OBTENER_ANTICIPOS";
	    	return resultado;
		}
	    	    
	}

public static String[] ejecutarF_SIGA_OBTENER_ANTICIPOS (
		String idinstitucion, String idTipoClave, String idClave,
		String idClaveInstitucion, String tipo, String idPeticion,
		String idPersona) throws ClsExceptions{

	String resultado= null; //Parametros de salida del PL

	try {
		StringBuffer query = new StringBuffer();
		query.append("SELECT F_SIGA_OBTENER_ANTICIPOS (");
		query.append(idinstitucion);
		query.append(",");
		query.append(idTipoClave);
		query.append(",");
		query.append(idClave);
		query.append(",");
		query.append(idClaveInstitucion);
		query.append(",");
		query.append("'"+tipo+"'");
		query.append(",");
		query.append(idPeticion);
		query.append(",");
		query.append("'"+idPersona+"'");
		query.append(") RESULTADO from dual ");
		
		Hashtable hashQuery = UtilidadesBDAdm.ejecutaQuery(query.toString());
		
		resultado = (String) hashQuery.get("RESULTADO");
		
		if ("".equals(resultado)){
			return null;
		}
		
		return resultado.split("#");

	} catch (Exception e){
    	throw new ClsExceptions(e.getMessage());       	
	}
      
}


public static String[] ejecutarF_SIGA_COMPROBAR_ANTICIPAR (
		String idinstitucion, String idTipoClave, String idClave,
		String idClaveInstitucion, String tipo, String idPeticion,
		String idPersona, String precioIva) throws ClsExceptions{

	String resultado= null; //Parametros de salida del PL

	try {
		StringBuffer query = new StringBuffer();
		query.append("SELECT F_SIGA_COMPROBAR_ANTICIPAR (");
		query.append(idinstitucion);
		query.append(",");
		query.append(idTipoClave);
		query.append(",");
		query.append(idClave);
		query.append(",");
		query.append(idClaveInstitucion);
		query.append(",");
		query.append("'"+tipo+"'");
		query.append(",");
		query.append(idPeticion);
		query.append(",");
		query.append("'"+idPersona+"'");
		query.append(",");		
		query.append("'"+precioIva+"'");
		query.append(") RESULTADO from dual ");
		
		Hashtable hashQuery = UtilidadesBDAdm.ejecutaQuery(query.toString());
		
		resultado = (String) hashQuery.get("RESULTADO");
		
		if ("".equals(resultado)){
			return null;
		}
		
		return resultado.split("#");

	} catch (Exception e){
    	throw new ClsExceptions(e.getMessage());       	
	}
      
}

	/**
	 * Devuelve una lista de idFacturacion separada por "," de todas aquellas facturaciones cuyas fechas "desde/hasta" 
	 * se encuentren en el intervalo que marcan la fecha "desde" menor y la fecha "hasta" mayor de las facturaciones
	 * <code>idFacturacionIni</code> e <code>idFacturacionFin</code>
	 * @param idInstitucion
	 * @param idFacturacionIni
	 * @param idFacturacionFin
	 * @return
	 * @throws ClsExceptions
	 */
	public static String ejecutarFuncFacturacionesIntervalo (String idInstitucion, String idFacturacionIni, String idFacturacionFin) throws ClsExceptions{
		RowsContainer rc = null;
		Hashtable miHash = new Hashtable();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idFacturacionIni);
		codigos.put(new Integer(3),idFacturacionFin);
		String resultado = null;
	
		String consulta = "select PKG_SIGA_FACTURACION_SJCS.FUNC_FACTURACIONES_INTERVALO(:1,:2,:3) FACTURACIONES FROM DUAL ";
		rc = new RowsContainer(); 
		if (rc.queryBind(consulta,codigos)) {
			Row fila = (Row) rc.get(0);
			miHash = fila.getRow();            
			resultado = (String)miHash.get("FACTURACIONES");            
		}
	
		return resultado;
	}


}
