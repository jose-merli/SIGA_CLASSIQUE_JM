/*
 * VERSIONES:
 * 
 * carlos.vidal - 08-03-2005 - Creacion
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.*;
import com.siga.Utilidades.*;
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
			
			// Obteneción emisión factura
			String select1 = " SELECT 1 AS idtabla, " +
							   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.emisionFactura'," + this.usrbean.getLanguage() + ") AS tabla, " +
							   "(select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 7) AS estado, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION   + " AS FECHA, "   +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS IMPORTE, " +
							   "'' AS DEVUELTA, '' AS tarjeta, " +
							   " 0 AS idabono_idcuenta, '' AS numeroabono, 0 AS idpago, " +
							   "'' AS NOMBREBANCO";

			String from1 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA; 

			String where1 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = '" + idFactura + "'";

			String consulta1 = select1 + from1 + where1;	

			// Obteneción confirmacion factura
			String select10 = " SELECT 1 AS idtabla, " +
							   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.confirmacionFactura'," + this.usrbean.getLanguage() + ") AS tabla, " +
							   " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro'," + this.usrbean.getLanguage() + ") AS estado, " +
							   " NVL("+FacFacturacionProgramadaBean.T_NOMBRETABLA+".FECHAREALCONFIRM, "+FacFacturacionProgramadaBean.T_NOMBRETABLA + "." +FacFacturacionProgramadaBean.C_FECHACONFIRMACION   + ") AS FECHA, "   +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS IMPORTE, " +
							   "'' AS DEVUELTA, '' AS tarjeta, " +
							   " 0 AS idabono_idcuenta, '' AS numeroabono, 0 AS idpago, " +
							   "'' AS NOMBREBANCO";

			String from10 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA +" , " + FacFacturacionProgramadaBean.T_NOMBRETABLA; 

			String where10 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
							" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + 
							" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + " = " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION +
							" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = '" + idFactura + "'";
			
			String consulta10 = select10 + from10 + where10;				
			
			// Obtención de anticipos aplicados a una factura
			String select2 = " SELECT 2 AS idtabla, " +
							 " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.aplicarAnticipo'," + this.usrbean.getLanguage() + ") AS tabla, " +
							 " case" +
							 " when (" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " > " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + ")" +
							 " THEN " + " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro'," + this.usrbean.getLanguage() + ") " + 
							 " ELSE (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 1) end as estado, " +
							 FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION   + " AS FECHA, "   +
							 FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
							 FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " AS IMPORTE, " +
							   "'' AS DEVUELTA, '' AS tarjeta, 0 AS idabono_idcuenta, '' AS numeroabono," +
							   "0 AS idpago, '' AS NOMBREBANCO";

			String from2 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA; 

			String where2 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = '" + idFactura + "'" +
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " > 0 ";

			String consulta2 = select2 + from2 + where2;			
						
			// Obtención pagos por caja
			String select3 = " SELECT 9 AS idtabla, " +
					 		 " F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagosCaja'," + this.usrbean.getLanguage() + ") AS tabla, " +
					 		 " case" +
					 		 " when ((" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " - " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + ") > " +
					 		" (select sum(pagocaja2 " + "." + FacPagosPorCajaBean.C_IMPORTE + ") as importepagado" +
					 		 " from " + FacPagosPorCajaBean.T_NOMBRETABLA + " pagocaja2 " + 
					 		 " where pagocaja2."    + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND pagocaja2."   + FacPagosPorCajaBean.C_IDFACTURA + " = '" + idFactura + "'" +
								" AND pagocaja2."   + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " <=  " +
								FacPagosPorCajaBean.T_NOMBRETABLA  + "." + FacPagosPorCajaBean.C_IDPAGOPORCAJA +
							"))" + 
					 		 " THEN (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 2) " +
					 		 " ELSE (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 1) end as estado, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHA  + " AS FECHA, "   +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IMPORTE + " AS IMPORTE, " +
							   "'' AS DEVUELTA, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_TARJETA + " AS TARJETA, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " AS IDABONO_IDCUENTA, " +
							   " (SELECT FAC_ABONO.NUMEROABONO FROM FAC_ABONO WHERE FAC_ABONO.IDINSTITUCION = FAC_PAGOSPORCAJA.IDINSTITUCION AND FAC_ABONO.IDABONO = FAC_PAGOSPORCAJA.IDABONO) AS NUMEROABONO, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " AS IDPAGO, "+
							   "'' AS NOMBREBANCO";

			String from3 = 	" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA + ", " + FacFacturaBean.T_NOMBRETABLA; 

			String where3 = " WHERE " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = '" + idFactura + "'" +
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " is null ";

			String consulta3 = select3 + from3 + where3;
			
			// Otención pagos por banco
			String select4 = " SELECT 9 AS idtabla, " +
					" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagoBanco'," + this.usrbean.getLanguage() + ") AS tabla, " +
					" (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 1) AS estado, " + 
							 " cargos." +  FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA, "+
							 "cargos." + FacDisqueteCargosBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +							   
							 " incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " +
							 " incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " AS DEVUELTA, " +
							 "'' AS TARJETA, " +
							 " incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							   "'' AS NUMEROABONO, " +
							   "0 AS IDPAGO, "+
							   "(select banco.nombre || 'nº ' || cuenta.cbo_codigo || ' ' || cuenta.codigosucursal ||' ' ||cuenta.digitocontrol ||' ' || LPAD(SUBSTR(cuenta.numerocuenta, 7), 10, '*') "+ 
                               " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" cuenta,"+CenBancosBean.T_NOMBRETABLA+" banco"+
                               " where cuenta."+CenCuentasBancariasBean.C_CBO_CODIGO+"=banco."+CenBancosBean.C_CODIGO+
                               " and  cuenta."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
                               " and  cuenta."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
                               " and cuenta."+CenCuentasBancariasBean.C_IDCUENTA + "=" + "incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA+") as NOMBREBANCO";

			String from4 = 	" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " +  FacDisqueteCargosBean.T_NOMBRETABLA + " cargos, " + FacFacturaBean.T_NOMBRETABLA + " factura "; 

			String where4 = " WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = cargos." + FacDisqueteCargosBean.C_IDINSTITUCION +
	                        " AND incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = cargos." + FacDisqueteCargosBean.C_IDDISQUETECARGOS+
	                        " and incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
	                        " AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA +
				            " AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'";

			String consulta4 = select4 + from4 + where4;

			// Obtención devoluciones
			String select5 = " SELECT 9 AS idtabla, " + 
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.devolucion'," + this.usrbean.getLanguage() + ") || '. ' || lineadevolucion.DESCRIPCIONMOTIVOS AS tabla, " +
					 		 " case" +
					 		 " when (incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " is null " +
					 		 " and   incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " is null) " +
					 		 " THEN (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 2) " +
					 		 " when (incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " is null " +
					 		 " and   incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " is not null) " +
					 		 " THEN (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 5) " +
					 		 " when (incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDRENEGOCIACION + " is not null " +
					 		 " and   incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " is not null) " +
					 		 " THEN (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 5) " +					 		 
					 		 " ELSE (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 2) " +
					 		 " end as estado, " +			
							 "devoluciones." + FacDisqueteDevolucionesBean.C_FECHAGENERACION   + " AS FECHA, "   +
							 "devoluciones." + FacDisqueteDevolucionesBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   + 					 		 
				  			 "incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " + 
							 "incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " AS DEVUELTA, " +
							 "'' AS TARJETA, " +
							 "incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							 "'' AS NUMEROABONO, " +
							 "0 AS IDPAGO, "+
							 "(select banco.nombre || 'nº ' || cuenta.cbo_codigo || ' ' || cuenta.codigosucursal ||' ' ||cuenta.digitocontrol ||' ' || LPAD(SUBSTR(cuenta.numerocuenta, 7), 10, '*') "+
                             " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" cuenta,"+CenBancosBean.T_NOMBRETABLA+" banco"+
                             " where cuenta."+CenCuentasBancariasBean.C_CBO_CODIGO+"=banco."+CenBancosBean.C_CODIGO+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
                             " and cuenta."+CenCuentasBancariasBean.C_IDCUENTA + "=" + "incluidadisquete."+ FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA+") as NOMBREBANCO";

			String from5 = 	" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + " incluidadisquete, " +
							FacLineaDevoluDisqBancoBean.T_NOMBRETABLA + " lineadevolucion, " +
							FacDisqueteDevolucionesBean.T_NOMBRETABLA + " devoluciones, " +
							FacFacturaBean.T_NOMBRETABLA + " factura ";
			
			String where5 = " WHERE incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + 
							" AND incluidadisquete."   + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE + " = lineadevolucion." + FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE + 
							" AND lineadevolucion."    + FacLineaDevoluDisqBancoBean.C_IDINSTITUCION + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDINSTITUCION +
							" AND lineadevolucion."  + FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES + " = devoluciones." + FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES + 
							" AND incluidadisquete."   + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
							" AND incluidadisquete." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND incluidadisquete."   + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'";
			
			String consulta5 = select5 + from5 + where5;
			
			// Obtención renegociaciones
			String select6 = " SELECT 9 AS idtabla, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.renegociacion'," + this.usrbean.getLanguage() + ") || ' ' || renegociacion.comentario AS tabla, " +
					 		 " case" +
					 		 " when (renegociacion." + FacRenegociacionBean.C_IDCUENTA + " is null) " +
					 		 " THEN (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 2) " +					 		 
					 		 " ELSE (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 5) " +
					 		 " end as estado, " +			
							 "renegociacion." + FacRenegociacionBean.C_FECHARENEGOCIACION   + " AS FECHA, "   + 
							 "renegociacion." + FacRenegociacionBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   + 					 		 
				  			 "renegociacion." + FacRenegociacionBean.C_IMPORTE + " AS IMPORTE, " + 
							 "'' AS DEVUELTA, '' AS tarjeta, " +
							 " 0 AS idabono_idcuenta, '' AS numeroabono, 0 AS idpago, " +
							 "(select banco.nombre || 'nº ' || cuenta.cbo_codigo || ' ' || cuenta.codigosucursal ||' ' ||cuenta.digitocontrol ||' ' || LPAD(SUBSTR(cuenta.numerocuenta, 7), 10, '*') "+
                             " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" cuenta,"+CenBancosBean.T_NOMBRETABLA+" banco, "+ FacRenegociacionBean.T_NOMBRETABLA + " renegocia2 " +
                             " where cuenta."+CenCuentasBancariasBean.C_CBO_CODIGO+"=banco."+CenBancosBean.C_CODIGO+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
                             " and renegociacion."+FacRenegociacionBean.C_IDINSTITUCION + "=" + "renegocia2."+ FacRenegociacionBean.C_IDINSTITUCION +
                             " and renegociacion."+FacRenegociacionBean.C_IDFACTURA + "=" + "renegocia2."+ FacRenegociacionBean.C_IDFACTURA +
                             " and renegociacion."+FacRenegociacionBean.C_IDRENEGOCIACION + "=" + "renegocia2."+ FacRenegociacionBean.C_IDRENEGOCIACION +
                             " and cuenta."+CenCuentasBancariasBean.C_IDCUENTA + "=" + "renegocia2."+ FacRenegociacionBean.C_IDCUENTA+") as NOMBREBANCO";

			String from6 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura, " +
							FacRenegociacionBean.T_NOMBRETABLA + " renegociacion " ;
			
			String where6 = " WHERE renegociacion."   + FacRenegociacionBean.C_IDINSTITUCION + " = factura." + FacFacturaBean.C_IDINSTITUCION +
							" AND renegociacion." + FacRenegociacionBean.C_IDFACTURA + " = factura." + FacFacturaBean.C_IDFACTURA + 
							" AND renegociacion." + FacRenegociacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND renegociacion."   + FacRenegociacionBean.C_IDFACTURA + " = '" + idFactura + "'";
			
			String consulta6 = select6 + from6 + where6;	
			
			// Obtención devoluciones
			String select7 = " SELECT 9 AS idtabla, " +
							" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion'," + this.usrbean.getLanguage() + ") AS tabla, " +
					 		 " (select F_SIGA_GETRECURSO_ETIQUETA (" + FacEstadoFacturaBean.T_NOMBRETABLA +  
							   "." + FacEstadoFacturaBean.C_DESCRIPCION + "," + this.usrbean.getLanguage() + ") from "  + FacEstadoFacturaBean.T_NOMBRETABLA +
							   " where " + FacEstadoFacturaBean.C_IDESTADO + " = 8) AS estado, " +		
							 "abono." + FacAbonoBean.C_FECHA   + " AS FECHA, "   +
					 		 "abono." + FacAbonoBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
				  			 "abono." + FacAbonoBean.C_IMPTOTALABONADO + " AS IMPORTE, " + 
							 "'' AS DEVUELTA, " +
							 "'' AS TARJETA, " +
							 "factura." + FacFacturaBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							 "abono." + FacAbonoBean.C_NUMEROABONO + " AS NUMEROABONO, " +
							 "0 AS IDPAGO, "+
							 "(select banco.nombre"+
                             " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" cuenta,"+CenBancosBean.T_NOMBRETABLA+" banco"+
                             " where cuenta."+CenCuentasBancariasBean.C_CBO_CODIGO+"=banco."+CenBancosBean.C_CODIGO+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
                             " and  cuenta."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
                             " and cuenta."+CenCuentasBancariasBean.C_IDCUENTA + "=" + "factura."+ FacFacturaBean.C_IDCUENTA+") as NOMBREBANCO";

			String from7 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + " factura,  "+
							FacAbonoBean.T_NOMBRETABLA + " abono  ";
			
			String where7 = " WHERE factura." + FacFacturaBean.C_IDINSTITUCION + " = abono." + FacAbonoBean.C_IDINSTITUCION + 
							" AND factura."   + FacFacturaBean.C_IDFACTURA + " = abono." + FacAbonoBean.C_IDFACTURA +  
							" AND factura." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND factura."   + FacFacturaBean.C_IDFACTURA + " = '" + idFactura + "'";
			
			String consulta7 = select7 + from7 + where7;			

			String consulta = "SELECT idtabla, TABLA, ESTADO, FECHA, FECHA_ORDEN, IMPORTE, DEVUELTA, TARJETA, IDABONO_IDCUENTA, NUMEROABONO, IDPAGO, NOMBREBANCO FROM ( " + 
							   consulta1 + " UNION " + consulta10 + " UNION " + consulta2 + " UNION " + consulta3 + " UNION " + consulta4 + " UNION " + consulta5 + " UNION " + consulta6 + " UNION " + consulta7 + 
							   " ) ORDER BY idtabla ASC, FECHA_ORDEN ASC, FECHA ASC, IDPAGO ASC"; 

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
