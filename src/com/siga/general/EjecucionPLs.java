/*
 * Created on May 12, 2005
 */
package com.siga.general;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesBDAdm;


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
 	 * PL para el pago de Turnos de Oficio con Criterio por Facturacion:
	 * @param idInstitucion
	 * @param idFacturacion
	 * @return
	 * @throws SIGAException
	 */
	public static String[] ejecutarPL_PagoTurnosOficio (Integer idInstitucion, Integer idPago, Integer usuario) throws SIGAException
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
	    	return resultado;
	    	
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
	public static String[] ejecutarPL_PagoGuardias (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
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
	    	return resultado;
	    	
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
	public static String[] ejecutarPL_PagoEJG (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
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
	   	
	    	return resultado;
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
	public static String[] ejecutarPL_PagoSOJ (Integer idInstitucion, Integer idFacturacion, Integer usuario) throws SIGAException
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
	   	
	    	return resultado;
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}

	

	/**
	 * PL para aplicar las retenciones judiciales.
	 * @param idInstitucion
	 * @param idPago
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPLAplicarRetencionesJudiciales(
			String idInstitucion, String idPago,
			String idPersona, String importeNeto, String usuMod,String idioma) throws ClsExceptions{
		
		Object[] param_in = new Object[6]; //Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL

		try {
	 		//Parametros de entrada del PL
			param_in[0] = idInstitucion;			
			param_in[1] = idPago;
			param_in[2] = idPersona;
			param_in[3] = importeNeto;
			param_in[4] = usuMod;
			param_in[5] = idioma;
			
	 		//Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure(
					"{call PKG_SIGA_RETENCIONES_SJCS.PROC_FCS_APLICAR_RETENC_JUDIC (?,?,?,?,?,?,?,?)}", 
					2, param_in);			
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
		resultadoPl = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_PAGOS_SJCS.PROC_FCS_CALCULAR_IRPF(?,?,?,?,?,?,?)}", 4, param_in);
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
	
	
	
	
	public static String ejecutarPLExportarTurno(String idInstitucion,
			String idFacturacionDesde, String idFacturacionHasta, String idPersona,
			String pathFichero, String fichero, String idioma)
			throws ClsExceptions
	{
		Object[] param_in; // Parametros de entrada del PL
		String resultado[] = null; // Parametros de salida del PL

		try {
	    	param_in = new Object[7];
			param_in[0] = idInstitucion;
			param_in[1] = idFacturacionDesde;
			param_in[2] = (idFacturacionHasta == null ? "" : idFacturacionHasta);
			param_in[3] = (idPersona == null ? "" : idPersona);
			param_in[4] = pathFichero;
			param_in[5] = fichero;
			param_in[6] = idioma;

			// Ejecucion del PL
			resultado = ClsMngBBDD
					.callPLProcedure(
							"{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_TURNOS_OFI (?,?,?,?,?,?,?,?,?)}",
							2, param_in);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}

		} catch (Exception e) {
    		throw new ClsExceptions ("Error al exportar datos: " + e.getMessage());			
		}

		// Resultado del PL
		return resultado[0];
	} // ejecutarPLExportarTurno()
	
	public static String ejecutarPLExportarGuardias(String idInstitucion,
			String idFacturacionDesde, String idFacturacionHasta, String idPersona,
			String pathFichero, String fichero, String idioma)
			throws ClsExceptions
	{
		Object[] param_in; // Parametros de entrada del PL
		String resultado[] = null; // Parametros de salida del PL

		try {
	    	param_in = new Object[7];
			param_in[0] = idInstitucion;
			param_in[1] = idFacturacionDesde;
			param_in[2] = (idFacturacionHasta == null ? "" : idFacturacionHasta);
			param_in[3] = (idPersona == null ? "" : idPersona);
			param_in[4] = pathFichero;
			param_in[5] = fichero;
			param_in[6] = idioma;

			// Ejecucion del PL
			resultado = ClsMngBBDD
					.callPLProcedure(
							"{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_GUARDIAS (?,?,?,?,?,?,?,?,?)}",
							2, param_in);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}

		} catch (Exception e) {
    		throw new ClsExceptions ("Error al exportar datos: " + e.getMessage());			
		}

		// Resultado del PL
		return resultado[0];
	} // ejecutarPLExportarGuardias()
	
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

	
	public static String ejecutarFuncFacturacionesIntervaloGrupos (String idInstitucion, String idFacturacionIni, String idFacturacionFin, String grupoFacturacion) throws ClsExceptions{
		RowsContainer rc = null;
		Hashtable miHash = new Hashtable();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idFacturacionIni);
		codigos.put(new Integer(3),idFacturacionFin);
		String grupoFacturaciones = null;
		String[] resul=grupoFacturacion.split(",");
		grupoFacturaciones = resul[0];
		codigos.put(new Integer(4),grupoFacturaciones);
		String resultado = null;
		//Func_Factura_Inter_Grupos
		String consulta = "select PKG_SIGA_FACTURACION_SJCS.FUNC_FACTURA_INTER_GRUPOS(:1,:2,:3,:4) FACTURACIONES FROM DUAL ";
		rc = new RowsContainer(); 
		if (rc.queryBind(consulta,codigos)) {
			Row fila = (Row) rc.get(0);
			miHash = fila.getRow();            
			resultado = (String)miHash.get("FACTURACIONES");            
		}
	
		return resultado;
	}

		/**
	 * Devuelve una lista de idPagos separada por "," de todas aquellas facturaciones cuyas fechas "desde/hasta" 
	 * se encuentren en el intervalo que marcan la fecha "desde" menor y la fecha "hasta" mayor de las facturaciones
	 * <code>idPagosIni</code> e <code>idPagosFin</code>
	 * @param idInstitucion
	 * @param idPagosIni
	 * @param idPagosFin
	 * @return
	 * @throws ClsExceptions
	 */
	public static String ejecutarFuncPagosIntervalo (String idInstitucion, String idPagosIni, String idPagosFin) throws ClsExceptions{
		RowsContainer rc = null;
		Hashtable miHash = new Hashtable();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idPagosIni);
		codigos.put(new Integer(3),idPagosFin);
		String resultado = null;
	
		String consulta = "select PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO_GRUPOFACT(:1, :2, :3, -1) PAGOS FROM DUAL ";
		//String consulta = "select PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO(:1,:2,:3) PAGOS FROM DUAL ";
		rc = new RowsContainer(); 
		if (rc.queryBind(consulta,codigos)) {
			Row fila = (Row) rc.get(0);
			miHash = fila.getRow();            
			resultado = (String)miHash.get("PAGOS");            
		}
	
		return resultado;
	}
	
	/**
	 * Devuelve una lista de idPagos separada por "," de todas aquellas facturaciones cuyas fechas "desde/hasta" 
	 * se encuentren en el intervalo que marcan la fecha "desde" menor y la fecha "hasta" mayor de las facturaciones
	 * <code>idPagosIni</code> e <code>idPagosFin</code>
	 * @param idInstitucion
	 * @param idPagosIni
	 * @param idPagosFin
	 * @return
	 * @throws ClsExceptions
	 */
	public static String ejecutarFuncPagosIntervaloGrupoFacturacion (String idInstitucion, String idPagosIni, String idPagosFin, String grupoFacturacion) throws ClsExceptions{
		RowsContainer rc = null;
		Hashtable miHash = new Hashtable();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idPagosIni);
		codigos.put(new Integer(3),idPagosFin);
		codigos.put(new Integer(4),grupoFacturacion);
		String resultado = null;
	
		String consulta = "select PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO_GRUPOFACT(:1,:2,:3,:4) PAGOS FROM DUAL ";
		rc = new RowsContainer(); 
		if (rc.queryBind(consulta,codigos)) {
			Row fila = (Row) rc.get(0);
			miHash = fila.getRow();            
			resultado = (String)miHash.get("PAGOS");            
		}
	
		return resultado;
	}
	
	public static String ejecutarFuncion (Hashtable<Integer,Object> htCodigos, 
			String funcion) throws ClsExceptions{
		RowsContainer rc  = new RowsContainer(); 
		
		String resultado = null;
	
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(funcion);
		sql.append("(");
		for(Integer codigo:htCodigos.keySet()){
			sql.append(":");
			sql.append(codigo);
			sql.append(",");
			
		}
		//QUITAMOS LA ULTIMA ,
		sql = new StringBuffer(sql.substring(0,sql.length()-1));
		sql.append(")");
		sql.append(" AS RESULTADO FROM DUAL");
		
	
		
			if (rc.queryBind(sql.toString(),htCodigos)) {
				Row fila = (Row) rc.get(0);
				Hashtable miHash = fila.getRow();            
				resultado = (String)miHash.get("RESULTADO");            
			}
		
		
		
	
		return resultado;
	}
	public static String [] ejecutarPL_RevocarCertificados(Integer idInstitucion, String nif) 
	{
		Object[] param_in; 			//Parametros de entrada del PL
		String resultado[] = null; 	//Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in  = new Object[2];
			param_in[0] = idInstitucion.toString();
			param_in[1] = nif;
			
			// Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call Pkg_Siga_Administracion.Revocarcertificados (?,?,?,?)}", 2, param_in);

		} 
		catch (Exception e){
			resultado[0] = "-1";     
	    	resultado[1] = "ERROR al ejecutar el procedimiento pkg_siga_censo.ActualizarDatosLetrado";
		}
	    
	    return resultado;
	} // ejecutarPL_RevocarCertificados()
	
	public static String[] ejecutarPL_mueveDatosPersona(String idPersonaD, String idPersonaO) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[2];
			param_in[0] = idPersonaO;
			param_in[1] = idPersonaD;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_FUSION_PERSONAS.MueveCosasDePersonaAPersona(?,?,?,?)}", 2, param_in);
			if(resultado[0].equalsIgnoreCase("-1")){
		    	throw new ClsExceptions(resultado[1]);
		    }
		} catch (Exception e){
			resultado[0] = "-1"; //ERROR P_CODRETORNO
	    	resultado[1] = resultado[1]; //ERROR P_DATOSERROR       
	    	throw new ClsExceptions(e.getMessage());
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPL_borraPersona(String idPersonaO) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[1];
			param_in[0] = idPersonaO;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_FUSION_PERSONAS.BorraPersona_simple(?,?,?)}", 2, param_in);
			if(resultado[0].equalsIgnoreCase("-1")){
		    	throw new ClsExceptions(resultado[1]);
		    }
		} catch (Exception e){
			resultado[0] = "-1"; //ERROR P_CODRETORNO
	    	resultado[1] = resultado[1]; //ERROR P_DATOSERROR        
	    	throw new ClsExceptions(e.getMessage());
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	public static String[] ejecutarPL_borraCliente(String idPersonaO, String idInstitucion) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[2];
			param_in[0] = idPersonaO;
			param_in[1] = idInstitucion;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_FUSION_PERSONAS.BorraCliente_simple(?,?,?,?)}", 2, param_in);
			if(resultado[0].equalsIgnoreCase("-1")){
		    	throw new ClsExceptions(resultado[1]);
		    }
		} catch (Exception e){
			resultado[0] = "-1"; //ERROR P_CODRETORNO
	    	resultado[1] = resultado[1]; //ERROR P_DATOSERROR    
	    	throw new ClsExceptions(e.getMessage());
		}
	    
	    //Resultado del PL        
	    return resultado;
	}

	public static String[] ejecutarPL_copiaCliente(String idPersonaD, String idPersonaO, String idInstitucion) throws ClsExceptions{
		Object[] param_in; //Parametros de entrada del PL
		String resultado[] = null; //Parametros de salida del PL
	
		try {
			resultado = new String[2];
			param_in = new Object[3];
			param_in[0] = idPersonaO;
			param_in[1] = idPersonaD;
			param_in[2] = idInstitucion;
		        
			//Ejecucion del PL
		    resultado = ClsMngBBDD.callPLProcedure("{call PKG_FUSION_PERSONAS.CopiaClienteAotraPersona(?,?,?,?,?)}", 2, param_in);
			if(resultado[0].equalsIgnoreCase("-1")){
		    	throw new ClsExceptions(resultado[1]);
		    }
		} catch (Exception e){
			resultado[0] = "-1"; //ERROR P_CODRETORNO
	    	resultado[1] = resultado[1]; //ERROR P_DATOSERROR     
	    	throw new ClsExceptions(e.getMessage());
		}
	    
	    //Resultado del PL        
	    return resultado;
	}
	
	/**
 	 * PL para el cálculo de la situación de ejercicio según la fecha actual
	 * @return
	 * @throws SIGAException
	 */
	public static String[] ejecutarPL_CalculoSituacionEjercicio () throws SIGAException
	{
		try {
			Object[] paramIn = new Object[0];
	    	String resultado[] = new String[2];
	    	resultado = ClsMngBBDD.callPLProcedure("{call PROC_CALC_SITUACIONEJERCICIO(?,?)}", 2, paramIn);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		//ClsLogging.writeFileLog("Error en PL = "+(String)resultado[3],3);
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el cálculo de la situación de ejercicio."+
	    				"\nError en PL = "+(String)resultado[1]);
	    	}
	    	return resultado;
	    	
		}catch (Exception e) {
			throw new SIGAException ("error.messages.application",e);
		}
	}
	
	public static String[] ejecutarPL_CrearPermutasCabeceras(String idInstitucion, String idNumero) throws ClsExceptions{
		String paramOut[] = null; 
	
		try {
			paramOut = new String[2]; //Parametros de salida del PL
			Object paramIn[] = new Object[2]; //Parametros de entrada del PL
			paramIn[0] = idInstitucion;
			paramIn[1] = idNumero;
		        
			//Ejecucion del PL
		    paramOut = ClsMngBBDD.callPLProcedure("{call PROC_CREAR_PERMUTAS_CABECERAS(?,?,?,?)}", 2, paramIn);
			if (!paramOut[0].equalsIgnoreCase("0")) {
		    	throw new ClsExceptions(paramOut[1]);
		    }
			
		} catch (Exception e){ 
	    	throw new ClsExceptions(e.getMessage());
		}
	    
	    //Resultado del PL        
	    return paramOut;
	}	
}
