package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


public class FacHistoricoFacturaAdm extends MasterBeanAdministrador {
	
	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacHistoricoFacturaAdm(UsrBean usu) {
		super(FacHistoricoFacturaBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	public String[] getCamposBean() {
		String [] campos = {FacHistoricoFacturaBean.C_IDFACTURA,
							FacHistoricoFacturaBean.C_IDINSTITUCION,
							FacHistoricoFacturaBean.C_FECHAMODIFICACION,
							FacHistoricoFacturaBean.C_IDHISTORICO,
							FacHistoricoFacturaBean.C_USUMODIFICACION,
							FacHistoricoFacturaBean.C_IDTIPOACCION,
							FacHistoricoFacturaBean.C_IDFORMAPAGO,
							FacHistoricoFacturaBean.C_IDPERSONA,
							FacHistoricoFacturaBean.C_IDCUENTA,
							FacHistoricoFacturaBean.C_IDPERSONADEUDOR,
							FacHistoricoFacturaBean.C_IDCUENTADEUDOR,
							FacHistoricoFacturaBean.C_IMPTOTALANTICIPADO,
							FacHistoricoFacturaBean.C_IMPTOTALPAGADOPORCAJA,
							FacHistoricoFacturaBean.C_IMPTOTALPAGADOSOLOCAJA,
							FacHistoricoFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA,
							FacHistoricoFacturaBean.C_IMPTOTALPAGADOPORBANCO,
							FacHistoricoFacturaBean.C_IMPTOTALPAGADO,
							FacHistoricoFacturaBean.C_IMPTOTALPORPAGAR,
							FacHistoricoFacturaBean.C_IMPTOTALCOMPENSADO,
							FacHistoricoFacturaBean.C_ESTADO,
							FacHistoricoFacturaBean.C_IDPAGOPORCAJA,
							FacHistoricoFacturaBean.C_IDDISQUETECARGOS,
							FacHistoricoFacturaBean.C_IDFACTURAINCLUIDAENDISQUETE,
							FacHistoricoFacturaBean.C_IDDISQUETEDEVOLUCIONES,
							FacHistoricoFacturaBean.C_IDRECIBO,
							FacHistoricoFacturaBean.C_IDRENEGOCIACION,
							FacHistoricoFacturaBean.C_IDABONO,
							FacHistoricoFacturaBean.C_COMISIONIDFACTURA};
							
		return campos;
	}
	
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacHistoricoFacturaBean.C_IDFACTURA,
							FacHistoricoFacturaBean.C_IDINSTITUCION,
							FacHistoricoFacturaBean.C_IDHISTORICO
							};
		return claves;
	}


	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}	

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacHistoricoFacturaBean bean = null;
		
		try {
			bean = new FacHistoricoFacturaBean();
			bean.setIdFactura (UtilidadesHash.getString(hash,FacHistoricoFacturaBean.C_IDFACTURA));			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDINSTITUCION));
			bean.setFechaModificacion((UtilidadesHash.getString(hash,FacHistoricoFacturaBean.C_FECHAMODIFICACION)));
			bean.setIdHistorico(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDHISTORICO));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_USUMODIFICACION));
			bean.setIdTipoAccion(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDTIPOACCION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDFORMAPAGO));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDPERSONA));
			bean.setIdCuenta(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDCUENTA));
			bean.setIdPersonaDeudor(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDPERSONADEUDOR));
			bean.setIdCuentaDeudor(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDCUENTADEUDOR));
			bean.setImpTotalAnticipado(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALANTICIPADO));	
			bean.setImpTotalPagadoPorCaja(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOPORCAJA));	
			bean.setImpTotalPagadoSoloCaja(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA));
			bean.setImpTotalPagadoSoloTarjeta(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA));
			bean.setImpTotalPagadoPorBanco(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOPORBANCO));
			bean.setImpTotalPagado(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADO));
			bean.setImpTotalPorPagar(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPORPAGAR));
			bean.setImpTotalPorPagar(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALCOMPENSADO));
			bean.setEstado(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_ESTADO));
			bean.setIdPagoPorCaja(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDPAGOPORCAJA));
			bean.setIdDisqueteCargos(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDDISQUETECARGOS));
			bean.setIdFacturaIncluidaEnDisquete(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDFACTURAINCLUIDAENDISQUETE));
			bean.setIdDisqueteDevoluciones(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDDISQUETEDEVOLUCIONES));
			bean.setIdRecibo(UtilidadesHash.getString(hash,FacHistoricoFacturaBean.C_IDRECIBO));
			bean.setIdRenegociacion(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDRENEGOCIACION));
			bean.setIdAbono(UtilidadesHash.getInteger(hash,FacHistoricoFacturaBean.C_IDABONO));
			bean.setComisionIdFactura(UtilidadesHash.getString(hash,FacHistoricoFacturaBean.C_COMISIONIDFACTURA));
			
		} catch (Exception e) { 
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			FacHistoricoFacturaBean b = (FacHistoricoFacturaBean) bean;
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDFACTURA, b.getIdFactura());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDINSTITUCION, b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_FECHAMODIFICACION, b.getFechaModificacion()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDHISTORICO, b.getIdHistorico()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_USUMODIFICACION, b.getUsuModificacion()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDTIPOACCION, b.getIdTipoAccion()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDFORMAPAGO, b.getIdFormaPago()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDPERSONA, b.getIdPersona()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDCUENTA, b.getIdCuenta()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDPERSONADEUDOR, b.getIdPersonaDeudor()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDCUENTADEUDOR, b.getIdCuentaDeudor()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALANTICIPADO, b.getImpTotalAnticipado()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPAGADOPORCAJA, b.getImpTotalPagadoPorCaja()); 
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPAGADOSOLOCAJA, b.getImpTotalPagadoSoloCaja());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA, b.getImpTotalPagadoSoloTarjeta());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPAGADOPORBANCO, b.getImpTotalPagadoPorBanco());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPAGADO, b.getImpTotalPagado());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALPORPAGAR, b.getImpTotalPorPagar());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IMPTOTALCOMPENSADO, b.getImpTotalCompensado());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_ESTADO, b.getEstado());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDPAGOPORCAJA, b.getIdPagoPorCaja());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDDISQUETECARGOS, b.getIdDisqueteCargos());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDFACTURAINCLUIDAENDISQUETE, b.getIdFacturaIncluidaEnDisquete());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDDISQUETEDEVOLUCIONES, b.getIdDisqueteDevoluciones());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDRECIBO, b.getIdRecibo());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDRENEGOCIACION, b.getIdRenegociacion());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_IDABONO, b.getIdAbono());
			UtilidadesHash.set(htData,FacHistoricoFacturaBean.C_COMISIONIDFACTURA, b.getComisionIdFactura());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Insertar registro en facHistorico
	 */	
	public boolean insertarHistoricoFacturacion (FacHistoricoFacturaBean factura) throws ClsExceptions, SIGAException 
	{

		String sql;
		boolean resultado= Boolean.FALSE;
		try { 
			
			sql="  INSERT INTO "+nombreTabla+" (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO, "+
											 "IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA," +
											 " IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO,IDRENEGOCIACION,IDABONO,COMISIONIDFACTURA) " +
											 " VALUES ("+factura.getIdInstitucion()+","+factura.getIdFactura()+","+"nvl((select max(his2.IDHISTORICO) from "+nombreTabla+" his2 where his2.IDINSTITUCION = "+factura.getIdInstitucion()+" and his2.IDFACTURA = "+factura.getIdFactura()+"), 0)+1,"+factura.getFechaModificacion()+","+
											 factura.getUsuModificacion()+","+factura.getIdTipoAccion()+","+factura.getIdFormaPago()+","+factura.getIdPersona()+","+factura.getIdCuenta()+","+
											 factura.getIdPersonaDeudor()+","+factura.getIdCuentaDeudor()+","+factura.getImpTotalAnticipado()+","+factura.getImpTotalPagadoPorCaja()+","+
											 factura.getImpTotalPagadoSoloCaja()+","+factura.getImpTotalPagadoSoloTarjeta()+","+factura.getImpTotalPagadoPorBanco()+","+
											 factura.getImpTotalPagado()+","+factura.getImpTotalPorPagar()+","+factura.getImpTotalCompensado()+","+factura.getEstado()+","+factura.getIdRenegociacion()+
											 ","+factura.getIdAbono()+","+factura.getComisionIdFactura()+")";
                
			
			resultado = this.insertSQL(sql);
		} catch (Exception e) {
		       	throw new ClsExceptions (e, "Error al insertar el histórico de facturación en BBDD");
	    }
        
		return resultado;
	}

	
	/** 
	 * Insertar registro en facHistorico
	 */	
	public boolean insertarHistoricoFacParametros (String idInstitucion, String idFactura, Integer idTipoAccion,Integer idPagoPorCaja, Integer idDisqueteCargos, Integer idFacturaIncluidaEnDisquete,
			Integer idDisqueteDevoluciones,String idRecibo, Integer idRenegociacion, Integer idAbono, String comisionIdFactura) throws ClsExceptions, SIGAException 
	{

		String sql;
		boolean resultado= Boolean.FALSE;
		try { 
			
			sql="  INSERT INTO "+nombreTabla+" (IDINSTITUCION,IDFACTURA, IDHISTORICO,FECHAMODIFICACION, USUMODIFICACION, IDTIPOACCION, IDFORMAPAGO, "+
											 " IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA," +
											 " IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO, IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO";
											 if(idPagoPorCaja !=null && idPagoPorCaja>0){
												 sql+=",IDPAGOPORCAJA";
											 }
											 if(idDisqueteCargos !=null && idDisqueteCargos>0){
												 sql+=",IDDISQUETECARGOS";
											 }
											 if(idFacturaIncluidaEnDisquete !=null && idFacturaIncluidaEnDisquete>0){
												 sql+=",IDFACTURAINCLUIDAENDISQUETE";
											 }
											 if(idDisqueteDevoluciones !=null && idDisqueteDevoluciones>0){
												 sql+=",IDDISQUETEDEVOLUCIONES";
											 }
											 if(idRecibo !=null && !"".equalsIgnoreCase(idRecibo)){
												 sql+=",IDRECIBO";
											 }
											 if(idRenegociacion !=null && idRenegociacion>0){
												 sql+=",IDRENEGOCIACION";
											 }
											 if(idAbono !=null && idAbono>0){
												 sql+=",IDABONO";
											 }
											 if(comisionIdFactura !=null && !"".equalsIgnoreCase(comisionIdFactura)){
												 sql+=",COMISIONIDFACTURA";
											 }
								   sql+= " )SELECT IDINSTITUCION, IDFACTURA,nvl((select max(his2.IDHISTORICO) from "+nombreTabla+" his2 where his2.IDINSTITUCION = FAC_FACTURA.IdInstitucion and his2.IDFACTURA = FAC_FACTURA.IdFactura), 0)+1,SYSDATE,USUMODIFICACION,"+idTipoAccion+",IDFORMAPAGO, " +
										 " IDPERSONA, IDCUENTA, IDPERSONADEUDOR, IDCUENTADEUDOR, IMPTOTALANTICIPADO, IMPTOTALPAGADOPORCAJA, IMPTOTALPAGADOSOLOCAJA, "+
										 " IMPTOTALPAGADOSOLOTARJETA, IMPTOTALPAGADOPORBANCO,IMPTOTALPAGADO, IMPTOTALPORPAGAR, IMPTOTALCOMPENSADO, ESTADO";
										   if(idPagoPorCaja !=null && idPagoPorCaja>0){
												 sql+=","+idPagoPorCaja;
											 }
											 if(idDisqueteCargos !=null && idDisqueteCargos>0){
												 sql+=","+idDisqueteCargos;
											 }
											 if(idFacturaIncluidaEnDisquete !=null && idFacturaIncluidaEnDisquete>0){
												 sql+=","+idFacturaIncluidaEnDisquete;
											 }
											 if(idDisqueteDevoluciones !=null && idDisqueteDevoluciones>0){
												 sql+=","+idDisqueteDevoluciones;
											 }
											 if(idRecibo !=null && !"".equalsIgnoreCase(idRecibo)){
												 sql+=","+idRecibo;
											 }
											 if(idRenegociacion !=null && idRenegociacion>0){
												 sql+=","+idRenegociacion;
											 }
											 if(idAbono !=null && idAbono>0){
												 sql+=","+idAbono;
											 }
											 if(comisionIdFactura !=null && !"".equalsIgnoreCase(comisionIdFactura)){
												 sql+=","+comisionIdFactura;
											 }
										   			
								   sql+= " FROM FAC_FACTURA WHERE IDINSTITUCION="+idInstitucion+" AND IDFACTURA="+idFactura;
											 
			resultado = this.insertSQL(sql);
		} catch (Exception e) {
		       	throw new ClsExceptions (e, "Error al insertar el histórico de facturación en BBDD");
	    }
        
		return resultado;
	}
	
	/** 
	 * Borra registros en facHistorico
	 */	
	public boolean borrarHistoricoFacturas (String idInstitucion,String idFactura, Integer idAbono, Integer estado) throws ClsExceptions, SIGAException 
	{

		String sql;
		boolean resultado= Boolean.FALSE;
		try { 
			sql="DELETE "+nombreTabla+" WHERE IDINSTITUCION="+idInstitucion;  
			if(idFactura!=null && !"".equalsIgnoreCase(idFactura))
				sql+=" AND IDFACTURA = "+idFactura;
			if(estado != null && estado>0)
				sql+=" AND ESTADO = "+estado;
			if(idAbono != null && idAbono>0)	
				sql+=" AND IDABONO = "+idAbono;
			
			resultado = this.deleteSQL(sql);
		} catch (Exception e) {
		       	throw new ClsExceptions (e, "Error al borrar el histórico de facturación en BBDD");
	    }
        
		return resultado;
	}
	
	  //Obtener informacion del histórico de la factura en estado confirmada para mostrarselo en la ventan de consulta
    public Vector obtenerInformacionHistorico (Long idSerieFacturacion,Long idProgramacion,Integer idInstitucion) throws ClsExceptions{
    	String sql = "	SELECT f_siga_getrecurso(FP.DESCRIPCION, 1)	AS FORMA_PAGO,   	"+
				 "      F_SIGA_CALCULAFORMATO(SUM(fac.imptotal - fac_hist.imptotalanticipado)) AS IMPORTE, "+
				 "      F_SIGA_CALCULAFORMATO(SUM(fac_hist.imptotalanticipado)) AS ANTICIPADO, "+
				 "      COUNT(*) AS NUM_FACTURAS									"+
				 " 	FROM PYS_FORMAPAGO FP, FAC_FACTURA FAC, FAC_FACTURACIONPROGRAMADA PROG,FAC_HISTORICOFACTURA FAC_HIST "+
				 " 	WHERE FP.IDFORMAPAGO = FAC.IDFORMAPAGO						"+
				 "  	AND FAC.IDINSTITUCION = PROG.IDINSTITUCION				"+
				 " 		AND FAC.IDSERIEFACTURACION = PROG.IDSERIEFACTURACION	"+
				 "		AND FAC.IDPROGRAMACION = PROG.IDPROGRAMACION			"+
				 "      AND FAC_HIST.IDINSTITUCION = FAC.IDINSTITUCION          "+
				 "  	AND FAC_HIST.IDFACTURA = FAC.IDFACTURA                  "+
				 "		AND PROG.IDSERIEFACTURACION = " + idSerieFacturacion     +
				 "		AND PROG.IDPROGRAMACION = 	  " + idProgramacion		 +
				 " 		AND PROG.IDINSTITUCION =      " + idInstitucion			 +
				 " 		AND FAC_HIST.IDTIPOACCION = 2 "+
				 " GROUP BY FP.DESCRIPCION	";
    	
    	
    	 return selectGenerico(sql);
    }
    
    
    /**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
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
			throw new ClsExceptions (e, "Excepcion en FacFacturacionProgramadaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
    


}