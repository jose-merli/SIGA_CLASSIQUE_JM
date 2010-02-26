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
			
			// Select tabla pagos
			String select1 = " SELECT 'PAGOS_POR_CAJA' AS TABLA, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_FECHA   + " AS FECHA, "   +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IMPORTE + " AS IMPORTE, " +
							   "'' AS DEVUELTA, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_TARJETA + " AS TARJETA, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDABONO + " AS IDABONO_IDCUENTA, " +
							   " (SELECT FAC_ABONO.NUMEROABONO FROM FAC_ABONO WHERE FAC_ABONO.IDINSTITUCION = FAC_PAGOSPORCAJA.IDINSTITUCION AND FAC_ABONO.IDABONO = FAC_PAGOSPORCAJA.IDABONO) AS NUMEROABONO, " +
							   FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDPAGOPORCAJA + " AS IDPAGO, "+
							   "'' AS NOMBREBANCO";

			String from1 = 	" FROM " + FacPagosPorCajaBean.T_NOMBRETABLA; 

			String where1 = " WHERE " + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacPagosPorCajaBean.T_NOMBRETABLA + "." + FacPagosPorCajaBean.C_IDFACTURA + " = '" + idFactura + "'";

			String consulta1 = select1 + from1 + where1;
			
			// Select tabla factura en disco
			String select2 = " SELECT 'FACTURA_EN_DISCO' AS TABLA, " + 
							   FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_FECHACREACION + " AS FECHA, "+							   
							   FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IMPORTE + " AS IMPORTE, " +
							   FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_DEVUELTA + " AS DEVUELTA, " +
							   "'' AS TARJETA, " +
							   FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA + " AS IDABONO_IDCUENTA, " +
							   "'' AS NUMEROABONO, " +
							   "0 AS IDPAGO, "+
							   "(select banco.nombre"+
                               " from "+CenCuentasBancariasBean.T_NOMBRETABLA+" cuenta,"+CenBancosBean.T_NOMBRETABLA+" banco"+
                               " where cuenta."+CenCuentasBancariasBean.C_CBO_CODIGO+"=banco."+CenBancosBean.C_CODIGO+
                               " and  cuenta."+CenCuentasBancariasBean.C_IDINSTITUCION+"="+idInstitucion+
                               " and  cuenta."+CenCuentasBancariasBean.C_IDPERSONA+"="+idPersona+
                               " and cuenta."+CenCuentasBancariasBean.C_IDCUENTA+"="+FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA+"."+ FacFacturaIncluidaEnDisqueteBean.C_IDCUENTA+") as NOMBREBANCO";

			String from2 = 	" FROM " + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "," +  FacDisqueteCargosBean.T_NOMBRETABLA;

			String where2 = " WHERE "  + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " +  FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDINSTITUCION +
	                        " AND "+ FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS + " = " +  FacDisqueteCargosBean.T_NOMBRETABLA + "." + FacDisqueteCargosBean.C_IDDISQUETECARGOS+
				            " AND "+ FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacFacturaIncluidaEnDisqueteBean.T_NOMBRETABLA + "." + FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA + " = '" + idFactura + "'";

			String consulta2 = select2 + from2 + where2;

			// Select para obetner el medio de pado anticipado
			String select3 = " SELECT 'ANTICIPADO' AS TABLA, " +
							   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION   + " AS FECHA, "   +
				  			   //"PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") AS IMPORTE, " + 
				  			   FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " AS IMPORTE, " + 
							   "'' AS DEVUELTA, " +
							   "'' AS TARJETA, " +
							   "0 AS IDABONO_IDCUENTA, " +
							   "'' AS NUMEROABONO, " +
							   "0 AS IDPAGO, "+
							   "'' AS NOMBREBANCO";

			String from3 = 	" FROM " + FacFacturaBean.T_NOMBRETABLA; 
			
			String where3 = " WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
							" AND "   + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = '" + idFactura + "'";
			
			String consulta3 = select3 + from3 + where3;

			String consulta = "SELECT TABLA, FECHA, IMPORTE, DEVUELTA, TARJETA, IDABONO_IDCUENTA, NUMEROABONO, IDPAGO, NOMBREBANCO FROM ( " + 
							   consulta1 + " UNION " + consulta2 + " UNION " + consulta3 + 
							   " ) ORDER BY FECHA ASC, IDPAGO ASC"; 

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
