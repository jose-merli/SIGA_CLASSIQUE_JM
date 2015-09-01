/*
 * Created on May 12, 2005
 */
package com.siga.general;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;


/**
 * @author david.sanchezp
 * M�todos estaticos que implementan la llamada a los procedimientos de ejecucion de los distintos PLs realizados.
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
	public static String[] ejecutarPL_EliminarSuscripcion(String idInstitucion,
			String idTipoServicio,
			String idServio,
			String idServicioInstitucion,
			String alta,
			String fechaAlta,
			String incluirBajaManual) throws ClsExceptions
	{
		int nParamIn = 7, nParamOut = 3;
		Object[] paramIn = new Object[nParamIn]; // Parametros de entrada del PL
		String resultado[] = new String[nParamOut]; // Parametros de salida del PL

		try {
			// Parametros de entrada del PL
			paramIn[0] = idInstitucion;
			paramIn[1] = idTipoServicio;
			paramIn[2] = idServio;
			paramIn[3] = idServicioInstitucion;
			paramIn[4] = alta;
			paramIn[5] = fechaAlta;
			paramIn[6] = incluirBajaManual;

			// Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_ELIMINAR_SUSCRIPCION(?,?,?,?,?,?,?,?,?,?)}", 
					nParamOut,
					paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; // P_NUMREGISTRO
			resultado[1] = "ERROR"; // ERROR P_DATOSERROR
		}
		// Resultado del PL
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
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_TURNOS_OFI (?,?,?,?,?,?,?,?,?)}", 2, param_in);
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
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SIGA_FACTURACION_SJCS.PROC_FCS_EXPORTAR_GUARDIAS (?,?,?,?,?,?,?,?,?)}", 2, param_in);
	    	if (!resultado[0].equalsIgnoreCase("0")) {
	    		ClsLogging.writeFileLog("Error en PL = "+(String)resultado[1],3);
	    	}

		} catch (Exception e) {
    		throw new ClsExceptions ("Error al exportar datos: " + e.getMessage());			
		}

		// Resultado del PL
		return resultado[0];
	} // ejecutarPLExportarGuardias()
	
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
			
			/*Object[] param_in = new Object[3]; //Parametros de entrada del PL
			param_in[0] = fecha;
			param_in[1] = tipo;
			param_in[2] = idioma;
		        
			//Ejecucion del PL
			ClsMngBBDD.callPLFunction("call PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra (?,?,?,?)", param_in);*/
			
		} catch (Exception e){
	    	throw new ClsExceptions(e.getMessage());//resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return fechaEnLetra;
	}
	public static String ejecutarF_SIGA_NUMEROENLETRA (String numero, String idioma) throws ClsExceptions{
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
			
			/*Object[] param_in = new Object[3]; //Parametros de entrada del PL
			param_in[0] = fecha;
			param_in[1] = tipo;
			param_in[2] = idioma;
		        
			//Ejecucion del PL
			ClsMngBBDD.callPLFunction("call PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra (?,?,?,?)", param_in);*/
			
		} catch (Exception e){
	    	throw new ClsExceptions(e.getMessage());//resultado[1] = "ERROR"; //ERROR P_DATOSERROR        	
		}
	    
	    //Resultado del PL        
	    return numeroEnLetra;
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
	public static String ejecutarFuncFacturacionesIntervalo (String idInstitucion, String idFacturacionIni, String idFacturacionFin) throws ClsExceptions {
		Object[] paramIn = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[1]; //Parametros de salida del PL
		
		// Parametros de entrada del PL
		paramIn[0] = idInstitucion;
		paramIn[1] = idFacturacionIni;
		paramIn[2] = idFacturacionFin;

		resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_FACTURACION_SJCS.FUNC_FACTURACIONES_INTERVALO(?,?,?)}", 0, paramIn);
		
		String resultadoFinal = "";
		if (resultado!=null && resultado[0]!=null) {
			resultadoFinal = resultado[0];
		}
		
		return resultadoFinal;				
	}

	
	public static String ejecutarFuncFacturacionesIntervaloGrupos (String idInstitucion, String idFacturacionIni, String idFacturacionFin, String grupoFacturacion) throws ClsExceptions {
		Object[] paramIn = new Object[4]; //Parametros de entrada del PL
		String resultado[] = new String[1]; //Parametros de salida del PL
		
		// Parametros de entrada del PL
		paramIn[0] = idInstitucion;
		paramIn[1] = idFacturacionIni;
		paramIn[2] = idFacturacionFin;
		
		String[] resul = grupoFacturacion.split(",");
		paramIn[3] = resul[0];

		resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_FACTURACION_SJCS.FUNC_FACTURA_INTER_GRUPOS(?,?,?,?)}", 0, paramIn);
		
		String resultadoFinal = "";
		if (resultado!=null && resultado[0]!=null) {
			resultadoFinal = resultado[0];
		}
		
		return resultadoFinal;			
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
		Object[] paramIn = new Object[3]; //Parametros de entrada del PL
		String resultado[] = new String[1]; //Parametros de salida del PL
		
		// Parametros de entrada del PL
		paramIn[0] = idInstitucion;
		paramIn[1] = idPagosIni;
		paramIn[2] = idPagosFin;

		resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO_GRUPOFACT(?,?,?,-1)}", 0, paramIn);
		//resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO(?,?,?)}", 0, paramIn);
		
		String resultadoFinal = "";
		if (resultado!=null && resultado[0]!=null) {
			resultadoFinal = resultado[0];
		}
		
		return resultadoFinal;		
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
	public static String ejecutarFuncPagosIntervaloGrupoFacturacion (String idInstitucion, String idPagosIni, String idPagosFin, String grupoFacturacion) throws ClsExceptions {
		Object[] paramIn = new Object[4]; //Parametros de entrada del PL
		String resultado[] = new String[1]; //Parametros de salida del PL
		
		// Parametros de entrada del PL
		paramIn[0] = idInstitucion;
		paramIn[1] = idPagosIni;
		paramIn[2] = idPagosFin;
		paramIn[3] = grupoFacturacion;

		resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_PAGOS_SJCS.FUNC_PAGOS_INTERVALO_GRUPOFACT(?,?,?,?)}", 0, paramIn);
		
		String resultadoFinal = "";
		if (resultado!=null && resultado[0]!=null) {
			resultadoFinal = resultado[0];
		}
		
		return resultadoFinal;				
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
 	 * PL para el c�lculo de la situaci�n de ejercicio seg�n la fecha actual
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
	    		throw new ClsExceptions ("Ha ocurrido un error al ejecutar el c�lculo de la situaci�n de ejercicio."+
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
	
	/**
	 * PL que actualiza la cuenta de abono/cargo de las cosas pendientes asociadas a la cuenta 
	 * pasada como par�metro. 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idCuenta
	 * @param usuario
	 * @return 1 si se ha modificado el tipo de pago a met�lico (caso de borrado, 
	 * 			 no existe la cuenta m�s reciente) 
	 * 		   2 si se actualiza la cuenta con la cuenta m�s reciente (actualizar)	
	 * @throws ClsExceptions
	 */
	public static String[] ejecutarPL_Act_Cuenta_Banco_Pend (String idInstitucion, String idPersona, String idCuenta, String usuario) throws ClsExceptions {

		Object[] paramIn = new Object[4]; 	//Parametros de entrada del PL
		String resultado[] = new String[2]; //Parametros de salida del PL
	
		try {
	 		// Parametros de entrada del PL
	        paramIn[0] = idInstitucion;
	        paramIn[1] = idPersona;
	        paramIn[2] = idCuenta;
	        paramIn[3] = usuario;

	        // Ejecucion del PL
			resultado = ClsMngBBDD.callPLProcedure("{call PKG_SERVICIOS_AUTOMATICOS.PROCESO_ACT_CUENTA_BANCO_PEND (?,?,?,?,?,?)}", 
													2, 
													paramIn);
		} catch (Exception e) {
			resultado[0] = "1"; 	// P_CODRETORNO
	    	resultado[1] = "ERROR"; // ERROR P_DATOSERROR        	
		}
	    //Resultado del PL        
	    return resultado;
	}
	
	/**
	 * Funcion que suma los dias h�biles a una fecha pasada por par�metro
	 * @param fecha
	 * @param dias
	 * @return Fecha sumada en formato (dd/mm/yyyy)
	 * @throws ClsExceptions
	 */
	public static String ejecutarSumarDiasHabiles (String fecha, String dias) throws ClsExceptions {
		RowsContainer rc = null;
		Hashtable miHash = new Hashtable();
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),fecha);
		codigos.put(new Integer(2),dias);
		String resultado = null;
	
		String consulta = "select F_SUMARDIASHABILES(:1,:2) FECHA FROM DUAL ";
		rc = new RowsContainer(); 
		if (rc.queryBind(consulta,codigos)) {
			Row fila = (Row) rc.get(0);
			miHash = fila.getRow();            
			resultado = GstDate.getFormatedDateShort("ES",(String)miHash.get("FECHA"));            
		}
	
		return resultado;
	}
	
	/**
	 * Funci�n que comprueba si el dia actual es h�bil
	 * @param fecha
	 * @return Boolean que indica si es h�bil
	 * @throws ClsExceptions
	 */
	public static Integer ejecutarEsDiaHabil (String fecha) throws ClsExceptions {
		Integer resultado = 0;
		String sql = " SELECT F_ESDIAHABIL(TO_DATE('" + fecha + "', 'DD/MM/YYYY')) AS ESHABIL FROM DUAL";
		
		RowsContainer rc = new RowsContainer(); 
		if (rc.query(sql)) {
			Row fila = (Row) rc.get(0);
			resultado = UtilidadesHash.getInteger(fila.getRow(), "ESHABIL");
		}
		
		return resultado;
	}	
	
	/**
	 * Funci�n que muestra la referencia final del mandato en SEPA
	 * @param cadena
	 * @return String que indica el resultado final en SEPA
	 * @throws ClsExceptions
	 */
	public static String ejecutarRevisarCaracteresSEPA (String cadena) throws ClsExceptions {			
		Object[] paramIn = new Object[1]; //Parametros de entrada del PL
		String resultado[] = new String[1]; //Parametros de salida del PL
		
		// Parametros de entrada del PL
		paramIn[0] = cadena;

		resultado = ClsMngBBDD.callPLFunction("{? = call PKG_SIGA_CARGOS.F_RevisarCaracteresSEPA(?)}", 0, paramIn);
		
		String resultadoFinal = "";
		if (resultado!=null && resultado[0]!=null) {
			resultadoFinal = resultado[0];
		}
		
		return resultadoFinal;
	}
	
	/**
	 * Obtiene las facturas relacionadas por comision de la factura actual
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 */
	public static String obtenerListaFacturasComision (String idInstitucion, String idFactura) throws ClsExceptions {
		String resultado = idFactura;
		String sql = " SELECT F_SIGA_GETLISTFACTCOMISION(" + idInstitucion + ", '" + idFactura + "', 1) AS LISTFACTCOMISION FROM DUAL";
		
		RowsContainer rc = new RowsContainer(); 
		if (rc.query(sql)) {
			Row fila = (Row) rc.get(0);
			resultado = fila.getString("LISTFACTCOMISION");
		}
		
		return resultado;
	}		
}
