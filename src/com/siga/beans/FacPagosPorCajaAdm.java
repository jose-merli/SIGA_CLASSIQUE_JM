/*
 * VERSIONES:
 * 
 * carlos.vidal - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.EjecucionPLs;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacPagosPorCajaAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacPagosPorCajaAdm(UsrBean usu) {
		super(FacPagosPorCajaBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {	
				FacPagosPorCajaBean.C_IDINSTITUCION	   	,
				FacPagosPorCajaBean.C_IDFACTURA         ,
				FacPagosPorCajaBean.C_IDPAGOPORCAJA     ,
				FacPagosPorCajaBean.C_FECHA             ,
				FacPagosPorCajaBean.C_CONTABILIZADO     ,
				FacPagosPorCajaBean.C_FECHAMODIFICACION ,
				FacPagosPorCajaBean.C_USUMODIFICACION   ,
				FacPagosPorCajaBean.C_IMPORTE           ,
				FacPagosPorCajaBean.C_TARJETA           ,
				FacPagosPorCajaBean.C_IDABONO           ,
				FacPagosPorCajaBean.C_IDPAGOABONO,
				FacPagosPorCajaBean.C_OBSERVACIONES,
				FacPagosPorCajaBean.C_NUMEROAUTORIZACION,
				FacPagosPorCajaBean.C_TIPOAPUNTE,
				FacPagosPorCajaBean.C_REFERENCIA};

		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacPagosPorCajaBean.C_IDINSTITUCION	   	,
				FacPagosPorCajaBean.C_IDFACTURA         ,
				FacPagosPorCajaBean.C_IDPAGOPORCAJA     ,
				};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacPagosPorCajaBean bean = null;
		
		try {
			bean = new FacPagosPorCajaBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDINSTITUCION));
			bean.setIdFactura(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_IDFACTURA));
			bean.setIdPagoPorCaja(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDPAGOPORCAJA));
			bean.setFecha(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_FECHA));
			bean.setContabilizado(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_CONTABILIZADO));
			bean.setImporte(UtilidadesHash.getDouble(hash,FacPagosPorCajaBean.C_IMPORTE));
			bean.setObservaciones(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_OBSERVACIONES));
			bean.setTarjeta(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_TARJETA));
			bean.setTipoApunte(UtilidadesHash.getString(hash,FacPagosPorCajaBean.C_TIPOAPUNTE));
			bean.setIdAbono(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDABONO));
			bean.setIdPagoAbono(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_IDPAGOABONO));		
			bean.setNumeroAutorizacion(UtilidadesHash.getInteger(hash,FacPagosPorCajaBean.C_NUMEROAUTORIZACION));		
			bean.setReferencia(UtilidadesHash.getLong(hash,FacPagosPorCajaBean.C_REFERENCIA));		
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/** 
	 * Obtiene la tabla hash a partir del bean introducido
	 * @param  bean - bean con los valores de la tabla 
	 * @return  Hashtable - Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacPagosPorCajaBean b = (FacPagosPorCajaBean) bean;
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDINSTITUCION   ,b.getIdInstitucion     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDFACTURA       ,b.getIdFactura         ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDPAGOPORCAJA   ,b.getIdPagoPorCaja     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_FECHA           ,b.getFecha             ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_CONTABILIZADO   ,b.getContabilizado     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IMPORTE         ,b.getImporte           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_TARJETA         ,b.getTarjeta           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_TIPOAPUNTE      ,b.getTipoApunte		());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDABONO         ,b.getIdAbono           ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_IDPAGOABONO     ,b.getIdPagoAbono       ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_OBSERVACIONES   ,b.getObservaciones     ());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_NUMEROAUTORIZACION,b.getNumeroAutorizacion());
			UtilidadesHash.set(htData,FacPagosPorCajaBean.C_REFERENCIA   	,b.getReferencia		());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene el valor IDABONO, <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Integer - Identificador del abono  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Integer getNuevoID (Integer institucion, String idFactura) throws ClsExceptions, SIGAException 
	{
		Integer resultado = null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT NVL(MAX(" + FacPagosPorCajaBean.C_IDPAGOPORCAJA + "), 0) + 1 AS " + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " FROM " + nombreTabla +
				" WHERE " + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + institucion + 
				" AND " + FacPagosPorCajaBean.C_IDFACTURA + " = '" + idFactura + "'";   
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get(FacPagosPorCajaBean.C_IDPAGOPORCAJA).equals("")) {
					resultado = new Integer(1);
				}
				else 
					resultado = UtilidadesHash.getInteger(prueba, FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoID' en B.D.");		
		}
		return resultado;
	}
	
	/**
	 * JPT: Creo una sentencia que vaya a reutilizar
	 * Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @param idPersona
	 * @param esProcesoMasivo
	 * @return
	 * @throws SIGAException
	 */
	public String getQueryPagos (Integer idInstitucion, String idFactura, Long idPersona, boolean esProcesoMasivo)  throws ClsExceptions, SIGAException {
		
		/* JPT: Cambios al aplicar comisiones en facturas (anula la anterior y crea una nueva con la comision)
		 * - Si es masivo ya tiene el listado final de facturas
		 * - En otro caso (pestaña de pagos de una factura), hay que obtener una lista con las facturas relacionadas por comision de la factura actual
		 */
		String listaFacturasComision = (esProcesoMasivo ? idFactura : EjecucionPLs.obtenerListaFacturasComision(idInstitucion.toString(), idFactura));
		
		// Obteneción emisión factura
		String consulta1 = "";    
		if (esProcesoMasivo) {
			consulta1 = " SELECT facturaActual." + FacFacturaBean.C_FECHAEMISION + " AS FECHA ";			
		} else {
			consulta1 = " SELECT 0 AS IDTABLA, " +
				   	" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.emisionFactura'," + this.usrbean.getLanguage() + ") " +
				   		" || ' (' || facturaActual." + FacFacturaBean.C_NUMEROFACTURA + " || ') ' AS TABLA, " +
				   	" ( " + 
				   		" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
				   		" FROM " + FacEstadoFacturaBean.T_NOMBRETABLA +
				   		" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_ENREVISION +
				   	") AS ESTADO, " +
				   " facturaActual." + FacFacturaBean.C_FECHAEMISION + " AS FECHA, " +
				   " facturaActual." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
				   " NVL(facturaActual." + FacFacturaBean.C_IMPTOTAL + " - facturaAnterior." + FacFacturaBean.C_IMPTOTALPAGADO + ", facturaActual." + FacFacturaBean.C_IMPTOTAL + ") AS IMPORTE, " +
				   " facturaActual." + FacFacturaBean.C_IDFACTURA + " AS IDFACTURA, " +
				   " '' AS ANULACIONCOMISION, " +
				   " '' AS DEVUELTA, " +
				   " '' AS TARJETA, " +
				   " 0 AS IDABONO_IDCUENTA, " +
				   " '' AS NUMEROABONO, " + 
				   " 0 AS IDPAGO, " +
				   " '' AS NOMBREBANCO ";
		}
		consulta1 += " FROM " + FacFacturaBean.T_NOMBRETABLA + " facturaActual, " +
						FacFacturaBean.T_NOMBRETABLA + " facturaAnterior " +
					" WHERE facturaActual." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
						" AND facturaActual." + FacFacturaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
						" AND facturaAnterior." + FacFacturaBean.C_IDINSTITUCION + "(+) = facturaActual." + FacFacturaBean.C_IDINSTITUCION +
						" AND facturaAnterior." + FacFacturaBean.C_IDFACTURA + "(+) = facturaActual." + FacFacturaBean.C_COMISIONIDFACTURA;    

		// Obteneción confirmacion factura
		String consulta10 = "";    
		if (esProcesoMasivo) {		
			consulta10 = " UNION SELECT DECODE(facturaActual." + FacFacturaBean.C_COMISIONIDFACTURA + ", NULL, " +
					   			FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ", " +
					   			" facturaActual." + FacFacturaBean.C_FECHAEMISION + ") AS FECHA ";
			
		} else {
			consulta10 = " UNION SELECT 1 AS IDTABLA, " +
						   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.confirmacionFactura'," + this.usrbean.getLanguage() + ") AS TABLA, " +
						   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro'," + this.usrbean.getLanguage() + ") AS ESTADO, " +
						   " DECODE(facturaActual." + FacFacturaBean.C_COMISIONIDFACTURA + ", NULL, " +
						   		FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ", " +
						   		" facturaActual." + FacFacturaBean.C_FECHAEMISION + ") AS FECHA, " +
						   " facturaActual." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
						   " NVL(facturaActual." + FacFacturaBean.C_IMPTOTAL + " - facturaAnterior." + FacFacturaBean.C_IMPTOTALPAGADO + ", facturaActual." + FacFacturaBean.C_IMPTOTAL + ") AS IMPORTE, " +
						   " facturaActual." + FacFacturaBean.C_IDFACTURA + " AS IDFACTURA, " +
						   " '' AS ANULACIONCOMISION, " +
						   " '' AS DEVUELTA, " +
						   " '' AS TARJETA, " +
						   " 0 AS IDABONO_IDCUENTA, " +
						   " '' AS NUMEROABONO, " + 
						   " 0 AS IDPAGO, " +
						   " '' AS NOMBREBANCO ";  
		}
		consulta10 += " FROM " + FacFacturaBean.T_NOMBRETABLA + " facturaActual, " + 
							FacFacturaBean.T_NOMBRETABLA + " facturaAnterior, " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + 
						" WHERE facturaActual." + FacFacturaBean.C_IDINSTITUCION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
							" AND facturaActual." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + 
							" AND facturaActual." + FacFacturaBean.C_IDPROGRAMACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION +
							" AND facturaActual." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND facturaActual." + FacFacturaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
							" AND facturaAnterior." + FacFacturaBean.C_IDINSTITUCION + "(+) = facturaActual." + FacFacturaBean.C_IDINSTITUCION +
							" AND facturaAnterior." + FacFacturaBean.C_IDFACTURA + "(+) = facturaActual." + FacFacturaBean.C_COMISIONIDFACTURA;   			
		
		// Obtención de anticipos aplicados a una factura
		String consulta2 = "";    
		if (esProcesoMasivo) {
			consulta2 = " UNION SELECT " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " AS FECHA ";			
		
		} else {
			consulta2 = " UNION SELECT 2 AS IDTABLA, " +
						 	" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.aplicarAnticipo'," + this.usrbean.getLanguage() + ") AS TABLA, " +
						 	" CASE " +
						 		" WHEN (" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " > " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + ") THEN " + 
						 			" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro'," + this.usrbean.getLanguage() + ") " + 
					 			" ELSE " +
						 			" ( " +
						 				" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
						 				" FROM " + FacEstadoFacturaBean.T_NOMBRETABLA +
						 				" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_PAGADA +
						 			" ) " +
			 				" END AS ESTADO, " +
			 				FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " AS FECHA, " +
			 				FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
			 				FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " AS IMPORTE, " +
			 				FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " AS IDFACTURA, " +
			 				" '' AS ANULACIONCOMISION, " +
			 				" '' AS DEVUELTA, " +
							   " '' AS TARJETA, " +
							   " 0 AS IDABONO_IDCUENTA, " +
							   " '' AS NUMEROABONO, " + 
							   " 0 AS IDPAGO, " +
							   " '' AS NOMBREBANCO ";
		}
		consulta2 += " FROM " + FacFacturaBean.T_NOMBRETABLA + 
					" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
						" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
						" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " > 0 ";
					
		// Obtención pagos por caja
		String consulta3 = "";    
		if (esProcesoMasivo) {
			consulta3 = " UNION SELECT " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHA + " AS FECHA ";
			
		} else  {
			consulta3 = " UNION SELECT 4 AS IDTABLA, " +
				 		 	" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagosCaja'," + this.usrbean.getLanguage() + ") AS TABLA, " +
				 		 	" CASE " +
				 		 		" WHEN ( " +
				 		 			" ( " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " - " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + ") > " +
			 		 				" ( " + 
			 		 					" SELECT SUM(pagocaja2." + FacPagosPorCajaBean.C_IMPORTE + ") " +
			 		 					" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + " pagocaja2 " + 
			 		 					" WHERE pagocaja2." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
			 		 						" AND pagocaja2." + FacPagosPorCajaBean.C_IDFACTURA + " = " + FacPagosPorCajaBean.T_NOMBRETABLA  + "." + FacFacturaBean.C_IDFACTURA +
			 		 						" AND pagocaja2." + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " <= " + FacPagosPorCajaBean.T_NOMBRETABLA  + "." + FacPagosPorCajaBean.C_IDPAGOPORCAJA +
	 		 						" ) " + 
 		 						" ) THEN " +
	 		 						" ( " +
		 		 						" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
		 		 						" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
		 		 						" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_CAJA + 
	 		 						" ) " +
 		 						" ELSE " +
	 		 						" ( " +
	 		 							" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
	 		 							" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
	 		 							" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_PAGADA + 
 		 							" ) " +
	 		 					" END AS ESTADO, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHA + " AS FECHA, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IMPORTE + " AS IMPORTE, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " AS IDFACTURA, " +
	 		 					" '' AS ANULACIONCOMISION, " +
	 		 					" '' AS DEVUELTA, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_TARJETA + " AS TARJETA, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " AS IDABONO_IDCUENTA, " +
	 		 					" ( " +
	 		 						" SELECT " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO +
	 		 						" FROM " + FacAbonoBean.T_NOMBRETABLA +
	 		 						" WHERE " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +  " = " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION +
	 		 							" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " = " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO +
	 		 					" ) AS NUMEROABONO, " +
	 		 					FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " AS IDPAGO, "+
	 		 					"'' AS NOMBREBANCO ";
		}
		consulta3 += " FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + ", " + 
							FacFacturaBean.T_NOMBRETABLA +
						" WHERE " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
							" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
							" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
							" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " IS NULL ";
		
		// Otención pagos por banco
		String consulta4 = "";    
		if (esProcesoMasivo) {
			consulta4 = " UNION SELECT cargos." +  FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA ";			
		} else  {
			consulta4 = " UNION SELECT 4 AS IDTABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagoBanco'," + this.usrbean.getLanguage() + ") AS TABLA, " +
							" ( " +
								" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
								" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
								" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_PAGADA + 
							" ) AS ESTADO, " + 
							" cargos." +  FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA, " +
							" cargos." + FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA_ORDEN, " +							   
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " +
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " AS IDFACTURA, " +
							" '' AS ANULACIONCOMISION, " +
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " AS DEVUELTA, " +
							" '' AS TARJETA, " +
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							" '' AS NUMEROABONO, " +
							" 0 AS IDPAGO, "+
							" ( " +
								" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
									CenBancosBean.T_NOMBRETABLA + " banco" +
								" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
									" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
									" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
									" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA +
							" ) as NOMBREBANCO";
		}
		consulta4 += " FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " +  
							FacDisqueteCargosBean.T_NOMBRETABLA + " cargos, " + 
							FacFacturaBean.T_NOMBRETABLA + " factura " + 
						" WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = cargos." + FacDisqueteCargosBean.C_IDINSTITUCION +
                        	" AND incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = cargos." + FacDisqueteCargosBean.C_IDDISQUETECARGOS +
                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA +
                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") ";   

		// Obtención devoluciones
		String consulta5 = "";    
		if (esProcesoMasivo) {
			consulta5 = " UNION SELECT devoluciones." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + " AS FECHA ";					
		} else {
			consulta5 = " UNION SELECT 4 AS IDTABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.devolucion'," + this.usrbean.getLanguage() + ") || ' (' || lineadevolucion.DESCRIPCIONMOTIVOS || ')' AS TABLA, " +
							" ( " +
								" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
								" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
								" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_DEVUELTA +
							" ) AS ESTADO, " +			
							" devoluciones." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + " AS FECHA, " +
							" devoluciones." + FacDisqueteDevolucionesBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " + 					 		 
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " + 
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " AS IDFACTURA, " +
							" '' AS ANULACIONCOMISION, " +
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " AS DEVUELTA, " +
							" '' AS TARJETA, " +
							" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							" '' AS NUMEROABONO, " +
							" 0 AS IDPAGO, " +
							" ( " +
								" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
									CenBancosBean.T_NOMBRETABLA + " banco" +
								" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
									" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
									" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
									" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA +
							" ) as NOMBREBANCO";									
		}
		consulta5 += " FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " + 
							FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + " lineadevolucion, " + 
							FacDisqueteDevolucionesBean.T_NOMBRETABLA + " devoluciones, " + 
							FacFacturaBean.T_NOMBRETABLA + " factura " + 
						" WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE + 
							" AND lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDINSTITUCION +
							" AND lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") ";   			
		
		// Obtención renegociaciones
		String consulta6 = "";    
		if (esProcesoMasivo) {
			consulta6 = " UNION SELECT renegociacion." + FacRenegociacionBean.C_FECHARENEGOCIACION + " AS FECHA ";			
		} else {
			consulta6 = " UNION SELECT 4 AS IDTABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.renegociacion'," + this.usrbean.getLanguage() + ") || ' ' || renegociacion.comentario AS TABLA, " +
							" CASE " +
								" WHEN (renegociacion." + FacRenegociacionBean.C_IDCUENTA + " is null) THEN " +
									" ( " +
										" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
										" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
										" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_CAJA +
									" ) " +
								" ELSE " +
									" ( " +
										" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
										" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
										" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_BANCO +
									" ) " +
							" END as ESTADO, " +						
							" renegociacion." + FacRenegociacionBean.C_FECHARENEGOCIACION + " AS FECHA, " + 
							" renegociacion." + FacRenegociacionBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " + 					 		 
							" renegociacion." + FacRenegociacionBean.C_IMPORTE + " AS IMPORTE, " + 
							" renegociacion." + FacRenegociacionBean.C_IDFACTURA + " AS IDFACTURA, " +
							" '' AS ANULACIONCOMISION, " +
							" '' AS DEVUELTA, '' AS TARJETA, " +
							" 0 AS IDABONO_IDCUENTA, " +
							" '' AS NUMEROABONO, " +
							" 0 AS IDPAGO, " +
							" ( " +
								" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
									CenBancosBean.T_NOMBRETABLA + " banco, " + 
									FacRenegociacionBean.T_NOMBRETABLA + " renegocia2 " +
								" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
									" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
									" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
									" AND renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = renegocia2." + FacRenegociacionBean.C_IDINSTITUCION +
									" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = renegocia2." + FacRenegociacionBean.C_IDFACTURA +
									" AND renegociacion." + FacRenegociacionBean.C_IDRENEGOCIACION + " = renegocia2." + FacRenegociacionBean.C_IDRENEGOCIACION +
									" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = renegocia2." + FacRenegociacionBean.C_IDCUENTA + 
							") as NOMBREBANCO";
		}
		consulta6 += " FROM " + FacFacturaBean.T_NOMBRETABLA + " factura," + 
							FacRenegociacionBean.T_NOMBRETABLA + " renegociacion " + 
						" WHERE renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
							" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
							" AND renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") ";   	
		
		// Obtención anulaciones
		String consulta7 = "";    
		if (!esProcesoMasivo) { // JPT: Si esta anulada no se puede renegociar, ni devolver (ganamos rendimiento)
			consulta7 = " UNION SELECT 4 AS IDTABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion'," + this.usrbean.getLanguage() + ") AS TABLA, " +
							" ( " +
								" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
								" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
								" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_ANULADA +
							" ) AS ESTADO, " +					
							" abono." + FacAbonoBean.C_FECHA + " AS FECHA, " +
							" abono." + FacAbonoBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
							" abono." + FacAbonoBean.C_IMPTOTALABONADO + " AS IMPORTE, " + 
							" factura." + FacFacturaBean.C_IDFACTURA + " AS IDFACTURA, " +
							" '' AS ANULACIONCOMISION, " +
							" '' AS DEVUELTA, " +
							" '' AS TARJETA, " +
							" factura." + FacFacturaBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							" abono." + FacAbonoBean.C_NUMEROABONO + " AS NUMEROABONO, " +
							" 0 AS IDPAGO, "+
							" ( " +
								" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
									CenBancosBean.T_NOMBRETABLA + " banco" +
								" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
									" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
									" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
									" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = factura." + FacFacturaBean.C_IDCUENTA +
							" ) as NOMBREBANCO";			
		}
		consulta7 += " FROM " + FacFacturaBean.T_NOMBRETABLA + " factura, " + 
							FacAbonoBean.T_NOMBRETABLA + " abono " + 
						" WHERE factura." + FacFacturaBean.C_IDINSTITUCION + " = abono." + FacAbonoBean.C_IDINSTITUCION + 
							" AND factura." + FacFacturaBean.C_IDFACTURA + " = abono." + FacAbonoBean.C_IDFACTURA +  
							" AND factura." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND factura."   + FacFacturaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") ";   				
		
		// Obtención anulaciones de comision (si la encuentra siempre es la ultima linea de la factura)
		String consulta71 = "";    
		if (!esProcesoMasivo) { // JPT: Si esta anulada no se puede renegociar, ni devolver (ganamos rendimiento)
			consulta71 = " UNION SELECT 5 AS IDTABLA, " +
				" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion'," + this.usrbean.getLanguage() + ") || " +
					" ' (' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacionComision'," + this.usrbean.getLanguage() + ") || ')' AS TABLA, " +
				" ( " +
					" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
					" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
					" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_ANULADA +
				" ) AS ESTADO, " +					
				" facturaPosterior." + FacFacturaBean.C_FECHAEMISION + " AS FECHA, " +
				" facturaActual." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
				" facturaActual." + FacFacturaBean.C_IMPTOTALPORPAGAR + " AS IMPORTE, " + 
				" facturaActual." + FacFacturaBean.C_IDFACTURA + " AS IDFACTURA, " +
				" '1' AS ANULACIONCOMISION, " +
				" '' AS DEVUELTA, " +
				" '' AS TARJETA, " +
				" facturaActual." + FacFacturaBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
				" '' AS NUMEROABONO, " + 
				" 0 AS IDPAGO, "+
				" ( " +
					" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
					" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
						CenBancosBean.T_NOMBRETABLA + " banco" +
					" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
						" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
						" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = facturaActual." + FacFacturaBean.C_IDCUENTA +
				" ) as NOMBREBANCO";		
		}
		consulta71 += " FROM " + FacFacturaBean.T_NOMBRETABLA + " facturaActual, " + 
							FacFacturaBean.T_NOMBRETABLA + " facturaPosterior " +	
						" WHERE facturaActual." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND facturaActual." + FacFacturaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
							" AND facturaPosterior." + FacFacturaBean.C_IDINSTITUCION + " = facturaActual." + FacFacturaBean.C_IDINSTITUCION +
							" AND facturaPosterior." + FacFacturaBean.C_COMISIONIDFACTURA + " = facturaActual." + FacFacturaBean.C_IDFACTURA +
							" AND facturaActual." + FacFacturaBean.C_ESTADO + " = " + ClsConstants.ESTADO_FACTURA_ANULADA;			
		
		//Abonos SJCS->compensaciones factura
		String consulta8 = "";    
		if (esProcesoMasivo) { 
			consulta8 = " UNION SELECT pc." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA ";			
		} else {
			consulta8 = " UNION SELECT 4 AS IDTABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.compensacion'," + this.usrbean.getLanguage() + ") AS TABLA, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.compensacion'," + this.usrbean.getLanguage() + ") AS ESTADO, " +					
							" pc." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA, " +
					 		" pc." + FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
				  			" pc." + FacPagoAbonoEfectivoBean.C_IMPORTE + " AS IMPORTE, " + 
				  			" ab." + FacAbonoBean.C_IDFACTURA + " AS IDFACTURA, " +
				  			" '' AS ANULACIONCOMISION, " +
							" '' AS DEVUELTA, " +
							" '' AS TARJETA, " +
							" ab." + FacAbonoBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							" ab." + FacAbonoBean.C_NUMEROABONO + " AS NUMEROABONO, " +
							" pc." + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " AS IDPAGO, "+
							" ( " +
								" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
								" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
									CenBancosBean.T_NOMBRETABLA + " banco" +
								" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
									" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
									" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
									" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = ab." + FacAbonoBean.C_IDCUENTA +
							" ) as NOMBREBANCO";				
		}
		consulta8 += " FROM " + FacAbonoBean.T_NOMBRETABLA + " ab, " + 
							FacPagosPorCajaBean.T_NOMBRETABLA + " pc, " + 
							FacPagoAbonoEfectivoBean.T_NOMBRETABLA + " aef " + 
						" WHERE pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = aef." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + 
							" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = ab." + FacAbonoBean.C_IDINSTITUCION + 
							" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND pc." + FacPagosPorCajaBean.C_IDFACTURA + " IN (" + listaFacturasComision + ") " +
							" AND pc." + FacPagosPorCajaBean.C_IDABONO + " = aef." + FacPagoAbonoEfectivoBean.C_IDABONO + 
							" AND pc." + FacPagosPorCajaBean.C_IDABONO + " = ab." + FacAbonoBean.C_IDABONO + 
							" AND pc." + FacPagosPorCajaBean.C_IDPAGOABONO + " = aef." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + 
							" AND (ab." +FacAbonoBean.C_IDFACTURA + " <> pc." + FacPagosPorCajaBean.C_IDFACTURA + " OR ab." + FacAbonoBean.C_IDFACTURA + " is null) " +
							" AND ab." + FacAbonoBean.C_IDPAGOSJG + " IS NOT NULL ";
		
		String consultaFinal = consulta1 + consulta10 + consulta2 + consulta3 + consulta4 + consulta5 + consulta6 + consulta7 + consulta71 + consulta8;
		
		if (esProcesoMasivo) {
			consultaFinal = "SELECT TO_CHAR(MAX(FECHA), '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "') AS ULTIMAFECHA FROM ( " + consultaFinal + " ) ";			
		} else {
			consultaFinal = "SELECT IDTABLA, TABLA, ESTADO, FECHA, FECHA_ORDEN, IMPORTE, IDFACTURA, ANULACIONCOMISION, DEVUELTA, TARJETA, IDABONO_IDCUENTA, NUMEROABONO, IDPAGO, NOMBREBANCO FROM ( " +
								consultaFinal +					
							" ) ORDER BY IDFACTURA ASC, IDTABLA ASC, TO_CHAR(FECHA, 'YYYYMMDD') ASC, FECHA_ORDEN ASC, IDPAGO ASC";						
		}
		
		return consultaFinal;
	}
	
	/**
	 * Obtiene las lineas de los pagos de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @param idPersona
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getPagos (Integer idInstitucion, String idFactura, Long idPersona)  throws ClsExceptions,SIGAException {
		try {
			// JPT: Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
			String consulta = getQueryPagos(idInstitucion, idFactura, idPersona, false);
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				Vector resultados = new Vector (); 
				
				Double pendiente=new Double(0);
				Double devolucionReneg=new Double(0);
				Double importePago=new Double(0);
				Double auxPendiente=new Double(0);
				String tabla="";
				
				for (int i = 0; i < rc.size(); i++)	{
					Hashtable aux = (Hashtable)((Row) rc.get(i)).getRow();
					
					tabla=UtilidadesHash.getString(aux,"TABLA").trim();
					
					if(tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.emisionFactura")) ||  
					   tabla.startsWith((UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.confirmacionFactura")))){
						
						pendiente =  UtilidadesHash.getDouble(aux,"IMPORTE");
						
						if (pendiente.doubleValue() < 0.0) {
							pendiente = new Double(0.0);
						}
						aux.remove("IMPORTE");
						aux.put("IMPORTE",new Double(0.00));
						aux.put("IMPORTEPENDIENTE", pendiente);
	
					} else {
						if (tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.devolucion")) ||  
						    tabla.startsWith((UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.renegociacion")))) {
							
							pendiente =  UtilidadesHash.getDouble(aux,"IMPORTE");
						
							if (pendiente.doubleValue() < 0.0) {
								pendiente = new Double(0.0);
							}
							aux.remove("IMPORTE");
							aux.put("IMPORTE",new Double(0.00));
							aux.put("IMPORTEPENDIENTE", pendiente);
							
						} else {
							
							importePago=UtilidadesHash.getDouble(aux,"IMPORTE");//formateo
							
							pendiente = new Double (pendiente.doubleValue() - importePago.doubleValue());	
							if (pendiente.doubleValue() < 0.0) {
								pendiente = new Double(0.0);
							}
							auxPendiente = new Double (UtilidadesNumero.redondea(pendiente.doubleValue(),2));
							aux.put("IMPORTEPENDIENTE", auxPendiente);
							aux.remove("IMPORTE");
							aux.put("IMPORTE",importePago);
						}
					}

					//Si es una compensación se calcula el estado de la misma forma que para los abonos
					if(tabla.startsWith(UtilidadesString.getMensajeIdioma(this.usrbean, "facturacion.pagosFactura.accion.compensacion"))){
						
						FacFacturaBean facBean = new FacFacturaBean();
						
						facBean.setImpTotalPorPagar(auxPendiente);
						
						if(aux.get("IDABONO_IDCUENTA")!=null)
							facBean.setIdCuenta(UtilidadesHash.getInteger(aux,"IDABONO_IDCUENTA"));		
						
						facBean.setIdInstitucion(idInstitucion);
						facBean.setIdFactura(idFactura);
						
						FacFacturaAdm facFactAdm= new FacFacturaAdm(this.usrbean);
						String estado=facFactAdm.consultarActNuevoEstadoFactura(facBean,Integer.parseInt(this.usrbean.getUserName()),false);						
						aux.remove("ESTADO");
						aux.put("ESTADO", UtilidadesString.getMensajeIdioma(this.usrbean, estado));
						
						tabla=UtilidadesHash.getString(aux,"TABLA").trim()+" ("+aux.get("NUMEROABONO")+")";
						aux.remove("TABLA");
						aux.put("TABLA",tabla);
						
					}
					
					resultados.add(aux);
					
					
				}
				return resultados;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener los datos de los pagos.");
	   			}
	   		}	
	    }
		return new Vector ();
	}
	
	/**
	 * Obtiene la ultima fecha de pago de una lista de facturas
	 * @param idInstitucion
	 * @param listaIdsFacturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getUltimaFechaPagosFacturas (Integer idInstitucion, String listaIdsFacturas) throws ClsExceptions, SIGAException {
		String resultado = null;
		
		try {
			// JPT: Obtiene la sentencia sql que obtiene todas las lineas de los pagos de una factura
			String consulta = getQueryPagos(idInstitucion, listaIdsFacturas, null, true);
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				if (rc.size()>0) {
					Row fila = (Row) rc.get(0);
					resultado = fila.getString("ULTIMAFECHA");
				}
			}
			
		} catch (Exception e) {
	    	throw new ClsExceptions(e, "Error al obtener la última fecha de pago de las facturas");	    
	    }
		
		return resultado;
	}
		
	public Hashtable getTotalesPagos (Integer idInstitucion, String idFactura)  throws ClsExceptions,SIGAException {
		try {
		    
		    Hashtable codigos = new Hashtable();


		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idFactura);
		    codigos.put(new Integer(3),idInstitucion.toString());
		    codigos.put(new Integer(4),idFactura);

		    String consulta =	"SELECT FAC_FACTURA.IMPTOTAL as TOTAL_FACTURA, " +
			   "FAC_FACTURA.IMPTOTALPAGADOPORBANCO as TOTAL_PAGADOPORBANCO, " +
			   "FAC_FACTURA.IMPTOTALCOMPENSADO as TOTAL_COMPENSADO, " +
			   "FAC_FACTURA.IMPTOTALPAGADOSOLOCAJA as TOTALPAGADOSOLOCAJA, " +
			   "FAC_FACTURA.IMPTOTALPAGADOPORCAJA as TOTALPAGADOPORCAJA, " +
			   "FAC_FACTURA.IMPTOTALPAGADOSOLOTARJETA as TOTALPAGADOPORTARJETA, " +
			   "FAC_FACTURA.IMPTOTALANTICIPADO as TOTAL_ANTICIPADO, ";
		    consulta+= " (SELECT " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFORMAPAGO + 
			" FROM " + FacFacturaBean.T_NOMBRETABLA +
			" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "=:1"+
			" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "=:2"+
			") AS FORMAPAGOFACTURA";
			consulta += " FROM FAC_FACTURA ";
			consulta += " WHERE FAC_FACTURA.IDINSTITUCION=:1";
			consulta += " AND FAC_FACTURA.IDFACTURA=:2";
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta,codigos)) {
				if (rc.size() > 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				return aux;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener los datos de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

	public Vector ejecutaSelectBind(String select, Hashtable codigos) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}

}
