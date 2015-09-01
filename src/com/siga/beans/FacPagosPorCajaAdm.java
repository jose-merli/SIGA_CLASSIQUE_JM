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
		
		StringBuilder consulta = new StringBuilder();  
		if (esProcesoMasivo) {
			consulta.append("SELECT TO_CHAR(MAX(FECHA), '");
			consulta.append(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			consulta.append("') AS ULTIMAFECHA FROM ( ");
		} else {
			consulta.append("SELECT IDTABLA, TABLA, ESTADO, FECHA, FECHA_ORDEN, IMPORTE, IDFACTURA, ANULACIONCOMISION, DEVUELTA, TARJETA, IDABONO_IDCUENTA, NUMEROABONO, IDPAGO, NOMBREBANCO FROM ( ");
		}		
		
		// Obtencion emision factura		
		if (esProcesoMasivo) {
			consulta.append(" SELECT facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(" AS FECHA ");			
		} else {			
			consulta.append(" SELECT 0 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.emisionFactura',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" || ' (' || facturaActual.");
			consulta.append(FacFacturaBean.C_NUMEROFACTURA);
			consulta.append(" || ') ' AS TABLA, ");
			consulta.append(" ( "); 
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_ENREVISION);
			consulta.append(") AS ESTADO, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(" AS FECHA, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(" NVL(facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(" - facturaAnterior.");
			consulta.append(FacFacturaBean.C_IMPTOTALPAGADO);
			consulta.append(", facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(") AS IMPORTE, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" 0 AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, "); 
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" '' AS NOMBREBANCO ");
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" facturaActual, ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" facturaAnterior ");
		consulta.append(" WHERE facturaActual.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");
		consulta.append(" AND facturaAnterior.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append("(+) = facturaActual.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" AND facturaAnterior.");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append("(+) = facturaActual.");
		consulta.append(FacFacturaBean.C_COMISIONIDFACTURA);    

		// Obtencion confirmacion factura
		if (esProcesoMasivo) {		
			consulta.append(" UNION SELECT DECODE(facturaActual.");
			consulta.append(FacFacturaBean.C_COMISIONIDFACTURA);
			consulta.append(", NULL, ");
			consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturacionProgramadaBean.C_FECHACONFIRMACION);
			consulta.append(", ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(") AS FECHA ");
			
		} else {
			consulta.append(" UNION SELECT 1 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.confirmacionFactura',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			
			consulta.append(" DECODE(NVL(facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(" - facturaAnterior.");
			consulta.append(FacFacturaBean.C_IMPTOTALPAGADO);
			consulta.append(", facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append("),0,(");			
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_PAGADA); 
			consulta.append(" ),"); 			
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(")) AS ESTADO, ");
			consulta.append(" DECODE(facturaActual.");
			consulta.append(FacFacturaBean.C_COMISIONIDFACTURA);
			consulta.append(", NULL, ");
			consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturacionProgramadaBean.C_FECHACONFIRMACION);
			consulta.append(", ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(") AS FECHA, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(" NVL(facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(" - facturaAnterior.");
			consulta.append(FacFacturaBean.C_IMPTOTALPAGADO);
			consulta.append(", facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(") AS IMPORTE, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" 0 AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, "); 
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" '' AS NOMBREBANCO ");  
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" facturaActual, "); 
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" facturaAnterior, ");
		consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA); 
		consulta.append(" WHERE facturaActual.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionProgramadaBean.C_IDINSTITUCION);
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_IDSERIEFACTURACION);
		consulta.append(" = ");
		consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionProgramadaBean.C_IDSERIEFACTURACION); 
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_IDPROGRAMACION);
		consulta.append(" = ");
		consulta.append(FacFacturacionProgramadaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturacionProgramadaBean.C_IDPROGRAMACION);
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");
		consulta.append(" AND facturaAnterior.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append("(+) = facturaActual.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" AND facturaAnterior.");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append("(+) = facturaActual.");
		consulta.append(FacFacturaBean.C_COMISIONIDFACTURA);   		
		consulta.append(" AND facturaActual.");
		consulta.append(FacFacturaBean.C_NUMEROFACTURA);
		consulta.append(" IS NOT NULL ");
		
		// Obtención de anticipos aplicados a una factura
		if (esProcesoMasivo) {
			consulta.append(" UNION SELECT ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(" AS FECHA ");			
		
		} else {
			consulta.append(" UNION SELECT 2 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.aplicarAnticipo',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			consulta.append(" CASE ");
			consulta.append(" WHEN (");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(" > ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IMPTOTALANTICIPADO);
			consulta.append(") THEN "); 
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.pendienteCobro',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") "); 
			consulta.append(" ELSE ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_PAGADA);
			consulta.append(" ) ");
			consulta.append(" END AS ESTADO, ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(" AS FECHA, ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IMPTOTALANTICIPADO);
			consulta.append(" AS IMPORTE, ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" 0 AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, "); 
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" '' AS NOMBREBANCO ");
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA); 
		consulta.append(" WHERE ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");
		consulta.append(" AND ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturaBean.C_IMPTOTALANTICIPADO);
		consulta.append(" > 0 ");
					
		// Obtencion pagos por caja
		if (esProcesoMasivo) {
			consulta.append(" UNION SELECT ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_FECHA);
			consulta.append(" AS FECHA ");
			
		} else  {
			consulta.append(" UNION SELECT 4 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagosCaja',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			consulta.append(" CASE ");
			consulta.append(" WHEN ( ");
			consulta.append(" ( ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IMPTOTAL);
			consulta.append(" - ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IMPTOTALANTICIPADO);
			consulta.append(") > ");
			consulta.append(" ( "); 
			consulta.append(" SELECT SUM(pagocaja2.");
			consulta.append(FacPagosPorCajaBean.C_IMPORTE);
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(" pagocaja2 "); 
			consulta.append(" WHERE pagocaja2.");
			consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion); 
			consulta.append(" AND pagocaja2.");
			consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
			consulta.append(" = ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AND pagocaja2.");
			consulta.append(FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			consulta.append(" <= ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			consulta.append(" ) "); 
			consulta.append(" ) THEN ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_CAJA); 
			consulta.append(" ) ");
			consulta.append(" ELSE ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_PAGADA); 
			consulta.append(" ) ");
			consulta.append(" END AS ESTADO, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_FECHA);
			consulta.append(" AS FECHA, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IMPORTE);
			consulta.append(" AS IMPORTE, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_TARJETA);
			consulta.append(" AS TARJETA, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDABONO);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" ( ");
			consulta.append(" SELECT ");
			consulta.append(FacAbonoBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacAbonoBean.C_NUMEROABONO);
			consulta.append(" FROM ");
			consulta.append(FacAbonoBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacAbonoBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacAbonoBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
			consulta.append(" AND ");
			consulta.append(FacAbonoBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacAbonoBean.C_IDABONO);
			consulta.append(" = ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDABONO);
			consulta.append(" ) AS NUMEROABONO, ");
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			consulta.append(" AS IDPAGO, ");
			consulta.append("'' AS NOMBREBANCO ");
		}
		consulta.append(" FROM ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(", "); 
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" WHERE ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturaBean.C_IDINSTITUCION); 
		consulta.append(" AND ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
		consulta.append(" = ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append(" AND ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");
		consulta.append(" AND ");
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(".");
		consulta.append(FacPagosPorCajaBean.C_IDABONO);
		consulta.append(" IS NULL ");
		
		// Otencion pagos por banco
		if (esProcesoMasivo) {
			consulta.append(" UNION SELECT cargos.");
			consulta.append(FacDisqueteCargosBean.C_FECHACREACION);
			consulta.append(" AS FECHA ");			
		} else  {
			consulta.append(" UNION SELECT 4 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.pagoBanco',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_PAGADA); 
			consulta.append(" ) AS ESTADO, "); 
			consulta.append(" cargos.");
			consulta.append(FacDisqueteCargosBean.C_FECHACREACION);
			consulta.append(" AS FECHA, ");
			consulta.append(" cargos.");
			consulta.append(FacDisqueteCargosBean.C_FECHACREACION);
			consulta.append(" AS FECHA_ORDEN, ");						   
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IMPORTE);
			consulta.append(" AS IMPORTE, ");
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA);
			consulta.append(" AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, ");
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA);
			consulta.append(" ) as NOMBREBANCO");
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA);
		consulta.append(" incluidadisquete, ");  
		consulta.append(FacDisqueteCargosBean.T_NOMBRETABLA);
		consulta.append(" cargos, "); 
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" factura "); 
		consulta.append(" WHERE incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = cargos.");
		consulta.append(FacDisqueteCargosBean.C_IDINSTITUCION);
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS);
		consulta.append(" = cargos.");
		consulta.append(FacDisqueteCargosBean.C_IDDISQUETECARGOS);
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDFACTURA);
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");   

		// Obtencion devoluciones
		if (esProcesoMasivo) {
			consulta.append(" UNION SELECT devoluciones.");
			consulta.append(FacDisqueteDevolucionesBean.C_FECHAGENERACION);
			consulta.append(" AS FECHA ");					
		} else {
			consulta.append(" UNION SELECT 4 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.devolucion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") || ' (' || lineadevolucion.DESCRIPCIONMOTIVOS || ')' AS TABLA, ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_DEVUELTA);
			consulta.append(" ) AS ESTADO, ");			
			consulta.append(" devoluciones.");
			consulta.append(FacDisqueteDevolucionesBean.C_FECHAGENERACION);
			consulta.append(" AS FECHA, ");
			consulta.append(" devoluciones.");
			consulta.append(FacDisqueteDevolucionesBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, "); 					 		 
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IMPORTE);
			consulta.append(" AS IMPORTE, "); 
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA);
			consulta.append(" AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, ");
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = incluidadisquete.");
			consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA);
			consulta.append(" ) as NOMBREBANCO");									
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA);
		consulta.append(" incluidadisquete, "); 
		consulta.append(FacLineaDevoluDisqBancoBean.T_NOMBRETABLA);
		consulta.append(" lineadevolucion, "); 
		consulta.append(FacDisqueteDevolucionesBean.T_NOMBRETABLA);
		consulta.append(" devoluciones, "); 
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" factura "); 
		consulta.append(" WHERE incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = lineadevolucion.");
		consulta.append(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS);
		consulta.append(" = lineadevolucion.");
		consulta.append(FacLineaDevoluDisqBancoBean.C_IDDISQUETECARGOS); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE);
		consulta.append(" = lineadevolucion.");
		consulta.append(FacLineaDevoluDisqBancoBean.C_IDFACTURAINCLUIDAENDISQUETE); 
		consulta.append(" AND lineadevolucion.");
		consulta.append(FacLineaDevoluDisqBancoBean.C_IDINSTITUCION);
		consulta.append(" = devoluciones.");
		consulta.append(FacDisqueteDevolucionesBean.C_IDINSTITUCION);
		consulta.append(" AND lineadevolucion.");
		consulta.append(FacLineaDevoluDisqBancoBean.C_IDDISQUETEDEVOLUCIONES);
		consulta.append(" = devoluciones.");
		consulta.append(FacDisqueteDevolucionesBean.C_IDDISQUETEDEVOLUCIONES); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDFACTURA); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND incluidadisquete.");
		consulta.append(FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");   			
		
		// Obtencion renegociaciones
		if (esProcesoMasivo) {
			consulta.append(" UNION SELECT renegociacion.");
			consulta.append(FacRenegociacionBean.C_FECHARENEGOCIACION);
			consulta.append(" AS FECHA ");			
		} else {
			consulta.append(" UNION SELECT 4 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.renegociacion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") || ' ' || renegociacion.comentario AS TABLA, ");
			consulta.append(" CASE ");
			consulta.append(" WHEN (renegociacion.");
			consulta.append(FacRenegociacionBean.C_IDCUENTA);
			consulta.append(" is null) THEN ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_CAJA);
			consulta.append(" ) ");
			consulta.append(" ELSE ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_BANCO);
			consulta.append(" ) ");
			consulta.append(" END as ESTADO, ");						
			consulta.append(" renegociacion.");
			consulta.append(FacRenegociacionBean.C_FECHARENEGOCIACION);
			consulta.append(" AS FECHA, "); 
			consulta.append(" renegociacion.");
			consulta.append(FacRenegociacionBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, "); 					 		 
			consulta.append(" renegociacion.");
			consulta.append(FacRenegociacionBean.C_IMPORTE);
			consulta.append(" AS IMPORTE, "); 
			consulta.append(" renegociacion.");
			consulta.append(FacRenegociacionBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, '' AS TARJETA, ");
			consulta.append(" 0 AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, ");
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco, "); 
			consulta.append(FacRenegociacionBean.T_NOMBRETABLA);
			consulta.append(" renegocia2 ");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND renegociacion.");
			consulta.append(FacRenegociacionBean.C_IDINSTITUCION);
			consulta.append(" = renegocia2.");
			consulta.append(FacRenegociacionBean.C_IDINSTITUCION);
			consulta.append(" AND renegociacion.");
			consulta.append(FacRenegociacionBean.C_IDFACTURA);
			consulta.append(" = renegocia2.");
			consulta.append(FacRenegociacionBean.C_IDFACTURA);
			consulta.append(" AND renegociacion.");
			consulta.append(FacRenegociacionBean.C_IDRENEGOCIACION);
			consulta.append(" = renegocia2.");
			consulta.append(FacRenegociacionBean.C_IDRENEGOCIACION);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = renegocia2.");
			consulta.append(FacRenegociacionBean.C_IDCUENTA); 
			consulta.append(") as NOMBREBANCO");
		}
		consulta.append(" FROM ");
		consulta.append(FacFacturaBean.T_NOMBRETABLA);
		consulta.append(" factura,"); 
		consulta.append(FacRenegociacionBean.T_NOMBRETABLA);
		consulta.append(" renegociacion "); 
		consulta.append(" WHERE renegociacion.");
		consulta.append(FacRenegociacionBean.C_IDINSTITUCION);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDINSTITUCION);
		consulta.append(" AND renegociacion.");
		consulta.append(FacRenegociacionBean.C_IDFACTURA);
		consulta.append(" = factura.");
		consulta.append(FacFacturaBean.C_IDFACTURA); 
		consulta.append(" AND renegociacion.");
		consulta.append(FacRenegociacionBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND renegociacion.");
		consulta.append(FacRenegociacionBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");   	
		
		// Obtencion anulaciones
		if (!esProcesoMasivo) { // JPT: Si esta anulada no se puede renegociar, ni devolver (ganamos rendimiento)
			consulta.append(" UNION SELECT 5 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_ANULADA);
			consulta.append(" ) AS ESTADO, ");					
			consulta.append(" abono.");
			consulta.append(FacAbonoBean.C_FECHA);
			consulta.append(" AS FECHA, ");
			consulta.append(" abono.");
			consulta.append(FacAbonoBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			//Tiene que salir el importe por el que se anulo la fac (si hay compensaciones previas no se corresponde con el imp. total abonado)
			consulta.append(" abono.");
			consulta.append(FacAbonoBean.C_IMPTOTAL);
			consulta.append(" AS IMPORTE, ");
			consulta.append(" factura.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" factura.");
			consulta.append(FacFacturaBean.C_IDCUENTA);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" abono.");
			consulta.append(FacAbonoBean.C_NUMEROABONO);
			consulta.append(" AS NUMEROABONO, ");
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = factura.");
			consulta.append(FacFacturaBean.C_IDCUENTA);
			consulta.append(" ) as NOMBREBANCO");
			consulta.append(" FROM ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(" factura, "); 
			consulta.append(FacAbonoBean.T_NOMBRETABLA);
			consulta.append(" abono, "); 
			consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
			consulta.append(" pgCaja "); 
			consulta.append(" WHERE factura.");
			consulta.append(FacFacturaBean.C_IDINSTITUCION);
			consulta.append(" = abono.");
			consulta.append(FacAbonoBean.C_IDINSTITUCION); 
			consulta.append(" AND factura.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" = abono.");
			consulta.append(FacAbonoBean.C_IDFACTURA);  
			consulta.append(" AND factura.");
			consulta.append(FacFacturaBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion); 
			consulta.append(" AND factura.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" IN (");
			consulta.append(listaFacturasComision);
			consulta.append(") ");
		}
		
		// Obtencion anulaciones de comision (si la encuentra siempre es la ultima linea de la factura)
		if (!esProcesoMasivo) { // JPT: Si esta anulada no se puede renegociar, ni devolver (ganamos rendimiento)
			consulta.append(" UNION SELECT 5 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") || ");
			consulta.append(" ' (' || F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.anulacionComision',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") || ')' AS TABLA, ");
			consulta.append(" ( ");
			consulta.append(" SELECT F_SIGA_GETRECURSO_ETIQUETA (");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(".");
			consulta.append(FacEstadoFacturaBean.C_DESCRIPCION);
			consulta.append(",");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") ");
			consulta.append(" FROM ");
			consulta.append(FacEstadoFacturaBean.T_NOMBRETABLA);
			consulta.append(" WHERE ");
			consulta.append(FacEstadoFacturaBean.C_IDESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_ANULADA);
			consulta.append(" ) AS ESTADO, ");					
			consulta.append(" facturaPosterior.");
			consulta.append(FacFacturaBean.C_FECHAEMISION);
			consulta.append(" AS FECHA, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_IMPTOTALPORPAGAR);
			consulta.append(" AS IMPORTE, "); 
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '1' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" facturaActual.");
			consulta.append(FacFacturaBean.C_IDCUENTA);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" '' AS NUMEROABONO, "); 
			consulta.append(" 0 AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = facturaActual.");
			consulta.append(FacFacturaBean.C_IDCUENTA);
			consulta.append(" ) as NOMBREBANCO");
			consulta.append(" FROM ");
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(" facturaActual, "); 
			consulta.append(FacFacturaBean.T_NOMBRETABLA);
			consulta.append(" facturaPosterior ");	
			consulta.append(" WHERE facturaActual.");
			consulta.append(FacFacturaBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion); 
			consulta.append(" AND facturaActual.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" IN (");
			consulta.append(listaFacturasComision);
			consulta.append(") ");
			consulta.append(" AND facturaPosterior.");
			consulta.append(FacFacturaBean.C_IDINSTITUCION);
			consulta.append(" = facturaActual.");
			consulta.append(FacFacturaBean.C_IDINSTITUCION);
			consulta.append(" AND facturaPosterior.");
			consulta.append(FacFacturaBean.C_COMISIONIDFACTURA);
			consulta.append(" = facturaActual.");
			consulta.append(FacFacturaBean.C_IDFACTURA);
			consulta.append(" AND facturaActual.");
			consulta.append(FacFacturaBean.C_ESTADO);
			consulta.append(" = ");
			consulta.append(ClsConstants.ESTADO_FACTURA_ANULADA);
		}
		
		//Abonos SJCS->compensaciones factura
		if (esProcesoMasivo) { 
			consulta.append(" UNION SELECT pc.");
			consulta.append(FacPagoAbonoEfectivoBean.C_FECHA);
			consulta.append(" AS FECHA ");			
		} else {
			consulta.append(" UNION SELECT 4 AS IDTABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.accion.compensacion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS TABLA, ");
			consulta.append(" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosFactura.estado.compensacion',");
			consulta.append(this.usrbean.getLanguage());
			consulta.append(") AS ESTADO, ");					
			consulta.append(" pc.");
			consulta.append(FacPagoAbonoEfectivoBean.C_FECHA);
			consulta.append(" AS FECHA, ");
			consulta.append(" pc.");
			consulta.append(FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION);
			consulta.append(" AS FECHA_ORDEN, ");
			consulta.append(" pc.");
			consulta.append(FacPagoAbonoEfectivoBean.C_IMPORTE);
			consulta.append(" AS IMPORTE, "); 
			consulta.append(" pc.");
			consulta.append(FacAbonoBean.C_IDFACTURA);
			consulta.append(" AS IDFACTURA, ");
			consulta.append(" '' AS ANULACIONCOMISION, ");
			consulta.append(" '' AS DEVUELTA, ");
			consulta.append(" '' AS TARJETA, ");
			consulta.append(" ab.");
			consulta.append(FacAbonoBean.C_IDCUENTA);
			consulta.append(" AS IDABONO_IDCUENTA, ");
			consulta.append(" ab.");
			consulta.append(FacAbonoBean.C_NUMEROABONO);
			consulta.append(" AS NUMEROABONO, ");
			consulta.append(" pc.");
			consulta.append(FacPagosPorCajaBean.C_IDPAGOPORCAJA);
			consulta.append(" AS IDPAGO, ");
			consulta.append(" ( ");
			consulta.append(" SELECT banco.nombre || ' nº ' || cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IBAN); 
			consulta.append(" FROM ");
			consulta.append(CenCuentasBancariasBean.T_NOMBRETABLA);
			consulta.append(" cuenta,"); 
			consulta.append(CenBancosBean.T_NOMBRETABLA);
			consulta.append(" banco");
			consulta.append(" WHERE cuenta.");
			consulta.append(CenCuentasBancariasBean.C_CBO_CODIGO);
			consulta.append(" = banco.");
			consulta.append(CenBancosBean.C_CODIGO);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDINSTITUCION);
			consulta.append(" = ");
			consulta.append(idInstitucion);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDPERSONA);
			consulta.append(" = ");
			consulta.append(idPersona);
			consulta.append(" AND cuenta.");
			consulta.append(CenCuentasBancariasBean.C_IDCUENTA);
			consulta.append(" = ab.");
			consulta.append(FacAbonoBean.C_IDCUENTA);
			consulta.append(" ) as NOMBREBANCO");				
		}
		consulta.append(" FROM ");
		consulta.append(FacAbonoBean.T_NOMBRETABLA);
		consulta.append(" ab, "); 
		consulta.append(FacPagosPorCajaBean.T_NOMBRETABLA);
		consulta.append(" pc, "); 
		consulta.append(FacPagoAbonoEfectivoBean.T_NOMBRETABLA);
		consulta.append(" aef "); 
		consulta.append(" WHERE pc.");
		consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
		consulta.append(" = aef.");
		consulta.append(FacPagoAbonoEfectivoBean.C_IDINSTITUCION); 
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
		consulta.append(" = ab.");
		consulta.append(FacAbonoBean.C_IDINSTITUCION); 
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDINSTITUCION);
		consulta.append(" = ");
		consulta.append(idInstitucion); 
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
		consulta.append(" IN (");
		consulta.append(listaFacturasComision);
		consulta.append(") ");
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDABONO);
		consulta.append(" = aef.");
		consulta.append(FacPagoAbonoEfectivoBean.C_IDABONO); 
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDABONO);
		consulta.append(" = ab.");
		consulta.append(FacAbonoBean.C_IDABONO); 
		consulta.append(" AND pc.");
		consulta.append(FacPagosPorCajaBean.C_IDPAGOABONO);
		consulta.append(" = aef.");
		consulta.append(FacPagoAbonoEfectivoBean.C_IDPAGOABONO); 
		consulta.append(" AND (ab.");
		consulta.append(FacAbonoBean.C_IDFACTURA);
		consulta.append(" <> pc.");
		consulta.append(FacPagosPorCajaBean.C_IDFACTURA);
		consulta.append(" OR ab.");
		consulta.append(FacAbonoBean.C_IDFACTURA);
		consulta.append(" is null) ");
		consulta.append(" AND ab.");
		consulta.append(FacAbonoBean.C_IDPAGOSJG);
		consulta.append(" IS NOT NULL ");
		
		if (esProcesoMasivo) {
			consulta.append(" ) ");			
		} else {
			consulta.append(" ) ORDER BY TO_NUMBER(IDFACTURA) ASC, IDTABLA ASC, TO_CHAR(FECHA, 'YYYYMMDD') ASC, FECHA_ORDEN ASC, IDPAGO ASC, DEVUELTA ASC");						
		}
		
		return consulta.toString();
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
		    String consulta = "SELECT " + FacFacturaBean.C_IMPTOTAL + " AS TOTAL_FACTURA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOPORBANCO + " AS TOTAL_PAGADOPORBANCO, " +
					    		FacFacturaBean.C_IMPTOTALCOMPENSADO + " AS TOTAL_COMPENSADO, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA + " AS TOTALPAGADOSOLOCAJA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOPORCAJA + " AS TOTALPAGADOPORCAJA, " +
					    		FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA + " AS TOTALPAGADOPORTARJETA, " +
					    		FacFacturaBean.C_IMPTOTALANTICIPADO + " AS TOTAL_ANTICIPADO, " +
					    		FacFacturaBean.C_IDFORMAPAGO + " AS FORMAPAGOFACTURA " +
					    	" FROM " + FacFacturaBean.T_NOMBRETABLA +
					    	" WHERE " + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion +
					    		" AND " + FacFacturaBean.C_IDFACTURA + " =  " + idFactura;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta) && rc.size() == 1) {
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				return aux;
			}
			
		} catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   			
	   		} else if (e instanceof ClsExceptions){
   				throw (ClsExceptions)e;
   				
   			} else {
   				throw new ClsExceptions(e, "Error al obtener los datos de las facturas.");
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
