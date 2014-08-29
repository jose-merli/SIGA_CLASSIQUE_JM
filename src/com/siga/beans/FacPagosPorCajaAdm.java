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
	
	
	public Vector getPagos (Integer idInstitucion, String idFactura, Long idPersona)  throws ClsExceptions,SIGAException {
		try {
			
            String fromFacturasTotal = " ( " +
       				" SELECT FACTURAS.IDFACTURA, " +
   						" ROWNUM AS CONTADOR " +
					" FROM ( " +
						" SELECT DISTINCT IDFACTURA " +
						" FROM FAC_FACTURA " +
						" WHERE IDINSTITUCION = " + idInstitucion +
						" START WITH IDFACTURA = " + idFactura +       
						" CONNECT BY PRIOR COMISIONIDFACTURA = IDFACTURA " + 
						" ORDER BY IDFACTURA ASC " +
					" ) FACTURAS " +
				" ) FACTURAS_TOTAL "; 			
			
			// Obteneción emisión factura
			String select1 = " SELECT 0 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
							   	" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.emisionFactura'," + this.usrbean.getLanguage() + ") " +
							   		" || ' (' || " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " || ') ' AS TABLA, " +
							   	" ( " + 
							   		" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
							   		" FROM " + FacEstadoFacturaBean.T_NOMBRETABLA +
							   		" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_ENREVISION +
							   	") AS ESTADO, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " AS FECHA, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS IMPORTE, " +
							   " '' AS DEVUELTA, " +
							   " '' AS TARJETA, " +
							   " 0 AS IDABONO_IDCUENTA, " +
							   " '' AS NUMEROABONO, " + 
							   " 0 AS IDPAGO, " +
							   " '' AS NOMBREBANCO ";    
			String from1 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + 
								", " + fromFacturasTotal; 
			String where1 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA ";    
			String consulta1 = select1 + from1 + where1;	

			// Obteneción confirmacion factura
			String select10 = " SELECT 1 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
							   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.confirmacionFactura'," + this.usrbean.getLanguage() + ") AS TABLA, " +
							   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro'," + this.usrbean.getLanguage() + ") AS ESTADO, " +
							   " NVL(" + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".FECHAREALCONFIRM, " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ") AS FECHA, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS IMPORTE, " +
							   " '' AS DEVUELTA, " +
							   " '' AS TARJETA, " +
							   " 0 AS IDABONO_IDCUENTA, " +
							   " '' AS NUMEROABONO, " + 
							   " 0 AS IDPAGO, " +
							   " '' AS NOMBREBANCO ";  
			String from10 = " FROM " + FacFacturaBean.T_NOMBRETABLA + 
								", " + FacFacturacionProgramadaBean.T_NOMBRETABLA + 
								", " + fromFacturasTotal; 
			String where10 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + 
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA "; 			
			String consulta10 = select10 + from10 + where10;				
			
			// Obtención de anticipos aplicados a una factura
			String select2 = " SELECT 2 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
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
				 				" '' AS DEVUELTA, " +
								   " '' AS TARJETA, " +
								   " 0 AS IDABONO_IDCUENTA, " +
								   " '' AS NUMEROABONO, " + 
								   " 0 AS IDPAGO, " +
								   " '' AS NOMBREBANCO ";  
			String from2 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + 
								", " + fromFacturasTotal;  
			String where2 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA " +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " > 0 ";
			String consulta2 = select2 + from2 + where2;			
						
			// Obtención pagos por caja
			String select3 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
					 		 	" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagosCaja'," + this.usrbean.getLanguage() + ") AS TABLA, " +
					 		 	" CASE " +
					 		 		" WHEN ( " +
					 		 			" ( " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " - " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + ") > " +
				 		 				" ( " + 
				 		 					" SELECT SUM(pagocaja2." + FacPagosPorCajaBean.C_IMPORTE + ") " +
				 		 					" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + " pagocaja2 " + 
				 		 					" WHERE pagocaja2." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
				 		 						" AND pagocaja2." + FacPagosPorCajaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA " +
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
			String from3 = 	" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + 
								", " + FacFacturaBean.T_NOMBRETABLA + 
								", " + fromFacturasTotal; 
			String where3 = " WHERE " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA " +
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " IS NULL ";
			String consulta3 = select3 + from3 + where3;
			
			// Otención pagos por banco
			String select4 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagoBanco'," + this.usrbean.getLanguage() + ") AS TABLA, " +
								" ( " +
									" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
									" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
									" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_PAGADA + 
								" ) AS ESTADO, " + 
								" cargos." +  FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA, " +
								" cargos." + FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA_ORDEN, " +							   
								" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " +
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
			String from4 = 	" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete" +
								", " +  FacDisqueteCargosBean.T_NOMBRETABLA + " cargos" +
								", " + FacFacturaBean.T_NOMBRETABLA + " factura " + 
								", " + fromFacturasTotal; 
			String where4 = " WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = cargos." + FacDisqueteCargosBean.C_IDINSTITUCION +
	                        	" AND incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = cargos." + FacDisqueteCargosBean.C_IDDISQUETECARGOS +
	                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
	                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA +
	                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
	                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA ";
			String consulta4 = select4 + from4 + where4;

			// Obtención devoluciones
			String select5 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.devolucion'," + this.usrbean.getLanguage() + ") || '. ' || lineadevolucion.DESCRIPCIONMOTIVOS AS TABLA, " +
								" CASE " +
									" WHEN (incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " is not null) THEN " +
										" ( " +
											" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
											" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
											" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_BANCO +
										" ) " +
									" ELSE " +
										" ( " +
											" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
											" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
											" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_CAJA +
										" ) " +
									" END as ESTADO, " +			
									" devoluciones." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + " AS FECHA, " +
									" devoluciones." + FacDisqueteDevolucionesBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " + 					 		 
									" incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " + 
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
			String from5 = 	" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete " +
								", " + FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + " lineadevolucion " +
								", " + FacDisqueteDevolucionesBean.T_NOMBRETABLA + " devoluciones " +
								", " + FacFacturaBean.T_NOMBRETABLA + " factura " + 
								", " + fromFacturasTotal; 			
			String where5 = " WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + 
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS + 
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE + 
								" AND lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDINSTITUCION +
								" AND lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES + 
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA ";			
			String consulta5 = select5 + from5 + where5;
			
			// Obtención renegociaciones
			String select6 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
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
			String from6 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura " +
								"," + FacRenegociacionBean.T_NOMBRETABLA + " renegociacion " + 
								", " + fromFacturasTotal; 
			String where6 = " WHERE renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
								" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
								" AND renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA ";		
			String consulta6 = select6 + from6 + where6;	
			
			// Obtención devoluciones
			String select7 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion'," + this.usrbean.getLanguage() + ") AS TABLA, " +
								" ( " +
									" SELECT F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") " +
									" FROM "  + FacEstadoFacturaBean.T_NOMBRETABLA +
									" WHERE " + FacEstadoFacturaBean.C_IDESTADO + " = " + ClsConstants.ESTADO_FACTURA_ANULADA +
								" ) AS ESTADO, " +					
								" abono." + FacAbonoBean.C_FECHA + " AS FECHA, " +
								" abono." + FacAbonoBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
								" abono." + FacAbonoBean.C_IMPTOTALABONADO + " AS IMPORTE, " + 
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
			String from7 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura " +
								", " + FacAbonoBean.T_NOMBRETABLA + " abono " + 	
								", " + fromFacturasTotal;
			String where7 = " WHERE factura." + FacFacturaBean.C_IDINSTITUCION + " = abono." + FacAbonoBean.C_IDINSTITUCION + 
								" AND factura." + FacFacturaBean.C_IDFACTURA + " = abono." + FacAbonoBean.C_IDFACTURA +  
								" AND factura." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND factura."   + FacFacturaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA ";				
			String consulta7 = select7 + from7 + where7;		
			
			//Abonos SJCS->compensaciones factura
			String select8 = " SELECT 9 + 10 * FACTURAS_TOTAL.CONTADOR AS IDTABLA, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.compensacion'," + this.usrbean.getLanguage() + ") AS TABLA, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.compensacion'," + this.usrbean.getLanguage() + ") AS ESTADO, " +					
								" pc." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA, " +
						 		" pc." + FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
					  			" pc." + FacPagoAbonoEfectivoBean.C_IMPORTE + " AS IMPORTE, " + 
								" '' AS DEVUELTA, " +
								" '' AS TARJETA, " +
								" ab." + FacAbonoBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
								" ab." + FacAbonoBean.C_NUMEROABONO + " AS NUMEROABONO, " +
								" pc." + FacPagosPorCajaBean.C_IDPAGOABONO + " AS IDPAGO, "+
								" ( " +
									" SELECT banco.nombre || ' nº ' || cuenta." + CenCuentasBancariasBean.C_IBAN + 
									" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + " cuenta," + 
										CenBancosBean.T_NOMBRETABLA + " banco" +
									" WHERE cuenta." + CenCuentasBancariasBean.C_CBO_CODIGO + " = banco." + CenBancosBean.C_CODIGO +
										" AND cuenta." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion +
										" AND cuenta." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona +
										" AND cuenta." + CenCuentasBancariasBean.C_IDCUENTA + " = ab." + FacAbonoBean.C_IDCUENTA +
								" ) as NOMBREBANCO";								
			String from8 = 	" FROM " + FacAbonoBean.T_NOMBRETABLA + " ab " +
								", " + FacPagosPorCajaBean.T_NOMBRETABLA + " pc " +
								", " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA + " aef " + 	
								", " + fromFacturasTotal;
			String where8 = " WHERE pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = aef." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + 
								" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = ab." + FacAbonoBean.C_IDINSTITUCION + 
								" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND pc." + FacPagosPorCajaBean.C_IDFACTURA + " = FACTURAS_TOTAL.IDFACTURA " +
								" AND pc." + FacPagosPorCajaBean.C_IDABONO+ " = aef." + FacPagoAbonoEfectivoBean.C_IDABONO + 
								" AND pc." + FacPagosPorCajaBean.C_IDABONO+ " = ab." + FacAbonoBean.C_IDABONO + 
								" AND pc." + FacPagosPorCajaBean.C_IDPAGOABONO+ " = aef." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + 
								" AND ab." + FacAbonoBean.C_IDPAGOSJG + " IS NOT NULL ";
			String consulta8 = select8 + from8 + where8;	
			
			String consulta = "SELECT idtabla, TABLA, ESTADO, FECHA, FECHA_ORDEN, IMPORTE, DEVUELTA, TARJETA, IDABONO_IDCUENTA, NUMEROABONO, IDPAGO, NOMBREBANCO FROM ( " + 
							   consulta1 + " UNION " + consulta10 + " UNION " + consulta2 + " UNION " + consulta3 + " UNION " + consulta4 + " UNION " + consulta5 + " UNION " + consulta6 + " UNION " + consulta7 + 
							   " UNION " + consulta8 + ") ORDER BY idtabla ASC, TO_CHAR(FECHA, 'YYYYMMDD') ASC, FECHA_ORDEN ASC, IDPAGO ASC"; 
			
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				Vector resultados = new Vector (); 
				for (int i = 0; i < rc.size(); i++)	{
					Hashtable aux = (Hashtable)((Row) rc.get(i)).getRow();
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
			// Obteneción emisión factura
			String consulta1 = " SELECT " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " AS FECHA " +
								" FROM " + FacFacturaBean.T_NOMBRETABLA +	
								" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " IN " + listaIdsFacturas;    

			// Obteneción confirmacion factura
			String consulta10 = " SELECT NVL(" + FacFacturacionProgramadaBean.T_NOMBRETABLA + ".FECHAREALCONFIRM, " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ") AS FECHA " + 
								" FROM " + FacFacturaBean.T_NOMBRETABLA + ", " + 
									FacFacturacionProgramadaBean.T_NOMBRETABLA +
								" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + 
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION +
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " IN " + listaIdsFacturas; 		
			
			// Obtención de anticipos aplicados a una factura
			String consulta2 = " SELECT " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " AS FECHA " +
								" FROM " + FacFacturaBean.T_NOMBRETABLA +
								" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " IN " + listaIdsFacturas +
									" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " > 0 ";			
						
			// Obtención pagos por caja
			String consulta3 = " SELECT " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHA + " AS FECHA " +
								" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + ", " + 
									FacFacturaBean.T_NOMBRETABLA +
								" WHERE " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
									" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
									" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " IN " + listaIdsFacturas +
									" AND " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " IS NULL ";
			
			// Otención pagos por banco
			String consulta4 = " SELECT cargos." +  FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA " +
								" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " +  
									FacDisqueteCargosBean.T_NOMBRETABLA + " cargos, " + 
									FacFacturaBean.T_NOMBRETABLA + " factura " +
								" WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = cargos." + FacDisqueteCargosBean.C_IDINSTITUCION +
		                        	" AND incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = cargos." + FacDisqueteCargosBean.C_IDDISQUETECARGOS +
		                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
		                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA +
		                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
		                        	" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " IN " + listaIdsFacturas;

			// Obtención devoluciones
			String consulta5 = " SELECT devoluciones." + FacDisqueteDevolucionesBean.C_FECHAGENERACION + " AS FECHA " +
								" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " + 
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
									" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " IN " + listaIdsFacturas; 
			
			// Obtención renegociaciones
			String consulta6 = " SELECT renegociacion." + FacRenegociacionBean.C_FECHARENEGOCIACION + " AS FECHA " +
								" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura," + 
									FacRenegociacionBean.T_NOMBRETABLA + " renegociacion " + 
								" WHERE renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
									" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
									" AND renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " IN " + listaIdsFacturas;	
			
			// Obtención devoluciones
			String consulta7 = " SELECT abono." + FacAbonoBean.C_FECHA + " AS FECHA " +
								" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura, " + 
									FacAbonoBean.T_NOMBRETABLA + " abono " +
								" WHERE factura." + FacFacturaBean.C_IDINSTITUCION + " = abono." + FacAbonoBean.C_IDINSTITUCION + 
									" AND factura." + FacFacturaBean.C_IDFACTURA + " = abono." + FacAbonoBean.C_IDFACTURA +  
									" AND factura." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND factura."   + FacFacturaBean.C_IDFACTURA + " IN " + listaIdsFacturas;				
			
			//Abonos SJCS->compensaciones factura
			String consulta8 = " SELECT pc." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA " +
								" FROM " + FacAbonoBean.T_NOMBRETABLA + " ab, " + 
									FacPagosPorCajaBean.T_NOMBRETABLA + " pc, " + 
									FacPagoAbonoEfectivoBean.T_NOMBRETABLA + " aef " +
								" WHERE pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = aef." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + 
									" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = ab." + FacAbonoBean.C_IDINSTITUCION + 
									" AND pc." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
									" AND pc." + FacPagosPorCajaBean.C_IDFACTURA + " IN " + listaIdsFacturas +
									" AND pc." + FacPagosPorCajaBean.C_IDABONO+ " = aef." + FacPagoAbonoEfectivoBean.C_IDABONO + 
									" AND pc." + FacPagosPorCajaBean.C_IDABONO+ " = ab." + FacAbonoBean.C_IDABONO + 
									" AND pc." + FacPagosPorCajaBean.C_IDPAGOABONO+ " = aef." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + 
									" AND ab." + FacAbonoBean.C_IDPAGOSJG + " IS NOT NULL ";	
			
			String consulta = "SELECT MAX(TO_CHAR(FECHA, '" + ClsConstants.DATE_FORMAT_SHORT_SPANISH + "')) AS ULTIMAFECHA FROM ( " + 
							   		consulta1 + " UNION " + 
							   		consulta10 + " UNION " + 
							   		consulta2 + " UNION " + 
							   		consulta3 + " UNION " + 
							   		consulta4 + " UNION " + 
							   		consulta5 + " UNION " + 
							   		consulta6 + " UNION " + 
							   		consulta7 + " UNION " + 
							   		consulta8 + 
							   ")"; 
			
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
