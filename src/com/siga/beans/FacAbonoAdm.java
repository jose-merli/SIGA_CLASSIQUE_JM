/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
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
public class FacAbonoAdm extends MasterBeanAdministrador {

	public static int DESTINATARIOABONO_SOCIEDAD = 0;
	public static int DESTINATARIOABONO_SJCS = 1;
	public static int DESTINATARIOABONO_NORMAL = 2;
	
	private static String baseSqlAbonosSJCSpendientes = new String ( 
		" FROM " + FacAbonoBean.T_NOMBRETABLA + "," + CenCuentasBancariasBean.T_NOMBRETABLA + "," + FcsPagosJGBean.T_NOMBRETABLA +
		" WHERE " +		FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + ":1 " +
		" AND " +		FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " IS NOT NULL " +
		" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
		" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDINSTITUCION +
		" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + "=" + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDPAGOSJG +
		" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
		" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
		" AND " +		FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_BANCOS_CODIGO + "= :2 "+
		" AND " +		"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = :3 " +
		" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR +"<> :4 ");

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacAbonoAdm(UsrBean usu) {
		super(FacAbonoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {FacAbonoBean.C_IDPERSONA,
							FacAbonoBean.C_IDINSTITUCION,
							FacAbonoBean.C_IDABONO,
							FacAbonoBean.C_FECHA,
							FacAbonoBean.C_CONTABILIZADA,
							FacAbonoBean.C_MOTIVOS,
							FacAbonoBean.C_IDFACTURA,
							FacAbonoBean.C_IDCUENTA,
							FacAbonoBean.C_IDPAGOSJG,
							FacAbonoBean.C_FECHAMODIFICACION,
							FacAbonoBean.C_USUMODIFICACION,
							FacAbonoBean.C_NUMEROABONO,
							FacAbonoBean.C_ESTADO,
							FacAbonoBean.C_IMPTOTAL,
							FacAbonoBean.C_IMPTOTALABONADO,
							FacAbonoBean.C_IMPTOTALABONADOEFECTIVO,
							FacAbonoBean.C_IMPTOTALABONADOPORBANCO,
							FacAbonoBean.C_IMPTOTALIVA,
							FacAbonoBean.C_IMPTOTALNETO,
							FacAbonoBean.C_IMPPENDIENTEPORABONAR,
							FacAbonoBean.C_OBSERVACIONES,
							FacAbonoBean.C_IDPERSONADEUDOR,
							FacAbonoBean.C_IDCUENTADEUDOR,
							FacAbonoBean.C_IDPERORIGEN};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacAbonoBean.C_IDABONO,
							FacAbonoBean.C_IDINSTITUCION};
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

		FacAbonoBean bean = null;
		
		try {
			bean = new FacAbonoBean();
			bean.setIdPersona (UtilidadesHash.getLong(hash,FacAbonoBean.C_IDPERSONA));			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,FacAbonoBean.C_IDINSTITUCION));
			bean.setIdAbono (UtilidadesHash.getLong(hash,FacAbonoBean.C_IDABONO));
			bean.setFecha (UtilidadesHash.getString(hash,FacAbonoBean.C_FECHA));
			bean.setMotivos (UtilidadesHash.getString(hash,FacAbonoBean.C_MOTIVOS));
			bean.setContabilizada (UtilidadesHash.getString(hash,FacAbonoBean.C_CONTABILIZADA));
			bean.setIdFactura (UtilidadesHash.getString(hash,FacAbonoBean.C_IDFACTURA));
			bean.setIdCuenta (UtilidadesHash.getInteger(hash,FacAbonoBean.C_IDCUENTA));
			bean.setIdPagosJG (UtilidadesHash.getInteger(hash,FacAbonoBean.C_IDPAGOSJG));
			bean.setFechaMod(UtilidadesHash.getString(hash,FacAbonoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacAbonoBean.C_USUMODIFICACION));	
			bean.setNumeroAbono(UtilidadesHash.getString(hash,FacAbonoBean.C_NUMEROABONO));
			bean.setEstado(UtilidadesHash.getInteger(hash,FacAbonoBean.C_ESTADO));
			bean.setImpPendientePorAbonar(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPPENDIENTEPORABONAR));
			bean.setImpTotal(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTAL));
			bean.setImpTotalAbonado(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTALABONADO));
			bean.setImpTotalAbonadoEfectivo(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTALABONADOEFECTIVO));
			bean.setImpTotalAbonadoPorBanco(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTALABONADOPORBANCO));
			bean.setImpTotalIva(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTALIVA));
			bean.setImpTotalNeto(UtilidadesHash.getDouble(hash,FacAbonoBean.C_IMPTOTALNETO));
			bean.setObservaciones(UtilidadesHash.getString(hash,FacAbonoBean.C_OBSERVACIONES));
			bean.setIdPersonaDeudor (UtilidadesHash.getLong(hash,FacAbonoBean.C_IDPERSONADEUDOR));
			bean.setIdCuentaDeudor (UtilidadesHash.getInteger(hash,FacAbonoBean.C_IDCUENTADEUDOR));
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
			FacAbonoBean b = (FacAbonoBean) bean;
			UtilidadesHash.set(htData,FacAbonoBean.C_IDPERSONA,b.getIdPersona ());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,FacAbonoBean.C_IDABONO, b.getIdAbono());
			UtilidadesHash.set(htData,FacAbonoBean.C_FECHA,b.getFecha());
			UtilidadesHash.set(htData,FacAbonoBean.C_MOTIVOS,b.getMotivos());
			UtilidadesHash.set(htData,FacAbonoBean.C_CONTABILIZADA,b.getContabilizada());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDFACTURA ,b.getIdFactura());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDCUENTA ,b.getIdCuenta());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDPAGOSJG ,b.getIdPagosJG());
			UtilidadesHash.set(htData,FacAbonoBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,FacAbonoBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(htData,FacAbonoBean.C_ESTADO,b.getEstado());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPPENDIENTEPORABONAR,b.getImpPendientePorAbonar());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTAL,b.getImpTotal());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTALABONADO,b.getImpTotalAbonado());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTALABONADOEFECTIVO,b.getImpTotalAbonadoEfectivo());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTALABONADOPORBANCO,b.getImpTotalAbonadoPorBanco());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTALIVA,b.getImpTotalIva());
			UtilidadesHash.set(htData,FacAbonoBean.C_IMPTOTALNETO,b.getImpTotalNeto());
			UtilidadesHash.set(htData,FacAbonoBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDPERSONADEUDOR,b.getIdPersonaDeudor ());
			UtilidadesHash.set(htData,FacAbonoBean.C_IDCUENTADEUDOR ,b.getIdCuentaDeudor());
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
	 * @return  Long - Identificador del abono  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		int contador = 0;	            
        Hashtable codigos = new Hashtable();
		try { 
            RowsContainer rc = new RowsContainer();
            	
			String sql;
			sql ="SELECT (MAX(IDABONO) + 1) AS IDABONO FROM " + nombreTabla;
				contador++;
				codigos.put(new Integer(contador),institucion);
				sql += " WHERE IDINSTITUCION =:" + contador;
		
			if (rc.findBind(sql,codigos)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDABONO").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDABONO");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return resultado;
	}
	
	

		/** 
	 * Obtiene el valor maximo de IDABONO, <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Identificador del abono  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getMaxID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT MAX(IDABONO) AS IDABONO FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDABONO").equals("")) {
					resultado=new Long(0);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDABONO");;								
			}
		}	
//		catch (SIGAException e) {
//			throw e;
//		}

		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al el maximo identificador de abono en BBDD");		
		}
		return resultado;
	}
	
	/** 
	 * Adecua los formatos de las fechas para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararFormatosFechas (Hashtable entrada)throws ClsExceptions,SIGAException 
	{
		String fecha;
				
		try {		
			fecha=GstDate.getApplicationFormatDate("",(String)entrada.get("FECHA"));
			entrada.put("FECHA",fecha);			
		}
//		catch (SIGAException e) {
//			throw e;
//		}
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al adecuar los formatos de las fechas.");		
		}
		return entrada;
	}
		
	/** 
	 * Obtiene aquellos abonos que cumplen con los criterios pasados como parametros<br/>
	 * @param  criterios - criterios fijados para la busqueda
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public PaginadorCaseSensitive getAbonos(Hashtable criterios) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
    						FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + "," +
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_CONTABILIZADA + "," +
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDFACTURA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA+ " AS FACTURACONTABILIZADA," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_ESTADO + " AS ESTADO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTAL + " AS TOTAL," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADO + " AS TOTALABONADO, " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + " " +
							
//	            			"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS ESTADO" + "," + 
//	            			"PKG_SIGA_TOTALESABONO.TOTAL("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTAL," +
//	            			"PKG_SIGA_TOTALESABONO.TOTALABONADO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALABONADO" +
							" FROM " + FacAbonoBean.T_NOMBRETABLA + "," + FacFacturaBean.T_NOMBRETABLA;
//							if ((!((String)criterios.get("FACTURAFECHAINICIO")).trim().equals("")) || (!((String)criterios.get("FACTURAFECHAINICIO")).trim().equals(""))) {
//								sql += " INNER JOIN " + FacFacturaBean.T_NOMBRETABLA +
//				 							" ON "+ 
//											FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDFACTURA + "=" +
//											FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA;
//							}
					sql += 	" WHERE " + 
							FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + (String)criterios.get(FacAbonoBean.C_IDINSTITUCION) +
							" AND " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "(+)=" + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION +
							" AND " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "(+)=" + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDFACTURA;
							
				
	            /*if (!((String)criterios.get(FacAbonoBean.C_IDABONO)).trim().equals("")){								 
					
	            	sql +=" AND "+ComodinBusquedas.prepararSentenciaCompletaUPPER(criterios.get(FacAbonoBean.C_IDABONO).toString().trim(),FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO);
				}*/
//	          Busqueda por numero de abono
	            if (criterios.get(FacAbonoBean.C_NUMEROABONO)!=null && !((String)criterios.get(FacAbonoBean.C_NUMEROABONO)).trim().equals("")){								 
					/*sql +=" AND " +
						  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO + "=" + (String)criterios.get(FacAbonoBean.C_IDABONO);*/
	            	sql +=" AND "+ComodinBusquedas.prepararSentenciaCompletaUPPER(criterios.get(FacAbonoBean.C_NUMEROABONO).toString().trim(),FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_NUMEROABONO);
				}
				// Busqueda por fecha de abono
				String abonoFechaInicio = "";
				String abonoFechaFin = "";
				if (!((String)criterios.get("ABONOFECHAINICIO")).trim().equals("")) 
	 				abonoFechaInicio = GstDate.getApplicationFormatDate("",(String)criterios.get("ABONOFECHAINICIO"));
				if (!((String)criterios.get("ABONOFECHAFIN")).trim().equals("")) 
	 				abonoFechaFin = GstDate.getApplicationFormatDate("",(String)criterios.get("ABONOFECHAFIN"));
				
				if ((!((String)criterios.get("ABONOFECHAINICIO")).trim().equals("")) || (!((String)criterios.get("ABONOFECHAINICIO")).trim().equals(""))) {
					sql += " AND " + GstDate.dateBetweenDesdeAndHasta(FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_FECHA,abonoFechaInicio,abonoFechaFin);
				}
				
				// Busqueda por cliente
	            if (!((String)criterios.get(FacAbonoBean.C_IDPERSONA)).trim().equals("")){								 
					sql +=" AND " +
						  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPERSONA + "=" + (String)criterios.get(FacAbonoBean.C_IDPERSONA);									 
				}
				
				// Busqueda por numero de factura
	            if (!((String)criterios.get(FacAbonoBean.C_IDFACTURA)).trim().equals("")){								 
					sql +=" AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=" + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION +
					  	  " AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "=" + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDFACTURA + 						
						 // " AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA + "= '" + (String)criterios.get(FacFacturaBean.C_IDFACTURA) + "' ";
					  	  " AND " +ComodinBusquedas.prepararSentenciaCompletaUPPER(criterios.get(FacFacturaBean.C_IDFACTURA).toString().trim(),FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA);
				}
				
				// Busqueda por fecha de factura
				String facturaFechaInicio = "";
				String facturaFechaFin = "";
				if (!((String)criterios.get("FACTURAFECHAINICIO")).trim().equals("")) 
	 				facturaFechaInicio = GstDate.getApplicationFormatDate("",(String)criterios.get("FACTURAFECHAINICIO"));
				if (!((String)criterios.get("FACTURAFECHAFIN")).trim().equals("")) 
	 				facturaFechaFin = GstDate.getApplicationFormatDate("",(String)criterios.get("FACTURAFECHAFIN"));
				
				if ((!((String)criterios.get("FACTURAFECHAINICIO")).trim().equals("")) || (!((String)criterios.get("FACTURAFECHAINICIO")).trim().equals(""))) {
					sql +=" AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION +
						  " AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDFACTURA + "=" + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + 						
						  " AND " + GstDate.dateBetweenDesdeAndHasta(FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_FECHAEMISION,facturaFechaInicio,facturaFechaFin);
				}
	            
				// Busqueda por forma de pago
	            if (!((String)criterios.get("FORMAPAGO")).trim().equals("")){
		            if (((String)criterios.get("FORMAPAGO")).trim().equals(ClsConstants.TIPO_CARGO_BANCO)){								 
						sql +=" AND " +
							  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDCUENTA + " is not null ";									 
					}
		            if (((String)criterios.get("FORMAPAGO")).trim().equals(ClsConstants.TIPO_CARGO_CAJA)){								 
						sql +=" AND " +
							  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDCUENTA + " is null ";									 
					}
	            }
	            
				// Busqueda por tipo de abono - nuevo MAV 06/05/05
	            if (!((String)criterios.get(FacAbonoBean.C_IDPAGOSJG)).trim().equals("")){
		            if (((String)criterios.get(FacAbonoBean.C_IDPAGOSJG)).trim().equals(ClsConstants.TIPO_ABONO_JUSTICIA_GRATUITA)){								 
						sql +=" AND " +
							  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " is not null ";									 
					}
		            if (((String)criterios.get(FacAbonoBean.C_IDPAGOSJG)).trim().equals(ClsConstants.TIPO_ABONO_FACTURACION)){								 
						sql +=" AND " +
							  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " is null ";									 
					}
	            }
	            
				// Busqueda por pagado
	            if (!((String)criterios.get("PAGADO")).trim().equals("")){
		            if (((String)criterios.get("PAGADO")).trim().equals(ClsConstants.DB_TRUE)){								 
						sql +=" AND " +
						FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_ESTADO+ "=1 ";
//							"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 1"; 									 
					}
		            if (((String)criterios.get("PAGADO")).trim().equals(ClsConstants.DB_FALSE)){								 
						sql +=" AND " +
							FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_ESTADO+ " IN (5,6) ";
//						  	  "(F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 5" + 
//						  	  " OR " +
//					  	      "F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 6)";
					}
	            }
	            
				// Busqueda por contabilizada
	            if (!((String)criterios.get(FacAbonoBean.C_CONTABILIZADA)).trim().equals("")){								 
					sql +=" AND " +
						  FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_CONTABILIZADA + "= '" + (String)criterios.get(FacAbonoBean.C_CONTABILIZADA)+ "'";									 
				}
				
				sql += " ORDER BY " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " DESC"; 										
							
	           
				PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(sql);				
				int totalRegistros = paginador.getNumeroTotalRegistros();
				
				if (totalRegistros==0){					
					paginador =null;
				}
	 
		       
				
				return paginador;
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener los abonos");
	       }
	                               
	    }
	

		
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  abono - identificador del abono	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getAbono (String institucion, String abono) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   int contador = 0;	            
	        Hashtable codigos = new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_CONTABILIZADA + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDFACTURA + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_ESTADO + " AS ESTADO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALNETO + " AS TOTALNETO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALIVA + " AS TOTALIVA," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTAL + " AS TOTAL," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS PENDIENTEPORABONAR," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADO + " AS TOTALABONADO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADOEFECTIVO + " AS TOTALABONADOEFECTIVO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADOPORBANCO + " AS TOTALABONADOPORBANCO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
	            			//"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS ESTADO" + "," +
							//"PKG_SIGA_TOTALESABONO.TOTALNETO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALNETO" + "," +
							//"PKG_SIGA_TOTALESABONO.TOTALIVA("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALIVA" + "," +
							//"PKG_SIGA_TOTALESABONO.TOTAL("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTAL" +
							"NVL(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + ",'') AS IDPAGOSJG" + 
							" FROM " + FacAbonoBean.T_NOMBRETABLA;
				            contador++;
							codigos.put(new Integer(contador),abono);
							sql += " WHERE " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO + "=:" + contador;
							contador++;
							codigos.put(new Integer(contador),institucion);
							sql +=" AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=:" + contador;
										
				if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable hfila = fila.getRow();
//	                  String idAbonoFila = (String) hfila.get("IDABONO");
//	                  ConsPLFacturacion pl=new ConsPLFacturacion();
//	                  
//	                  Hashtable codigos2 = new Hashtable();
//	                  codigos2.put(new Integer(1), institucion);
//	                  codigos2.put(new Integer(2), abono);
//	                  
//	                  hfila.put("ESTADO", pl.getFuncionEjecutada(codigos2, "F_SIGA_ESTADOSABONO"));
//	                  hfila.put("TOTALNETO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.TOTALNETO"));
//	                  hfila.put("TOTALIVA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.TOTALIVA"));
//	                  hfila.put("TOTAL", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.TOTAL"));

	                  fila.setRow(hfila);
	                  datos.add(fila);
	        
	               }
	            }
		     }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion sobre los abonos relacionados con un determinado usuario<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  idPersona - identificador de la persona asociada
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getAbonosCliente (String institucion, String idPersona) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_CONTABILIZADA + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDFACTURA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_ESTADO + " AS ESTADO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALNETO + " AS TOTALNETO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALIVA + " AS TOTALIVA," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADO + " AS TOTALABONADO," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTAL + " AS TOTAL " +
//	            			"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS ESTADO" + "," +
//	            			"PKG_SIGA_TOTALESABONO.TOTALNETO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALNETO" + "," +
//	            			"PKG_SIGA_TOTALESABONO.TOTALIVA("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALIVA" + "," +
//	            			"PKG_SIGA_TOTALESABONO.TOTALABONADO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALABONADO" + "," +
//	            			"PKG_SIGA_TOTALESABONO.TOTAL("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTAL" +
							" FROM " + FacAbonoBean.T_NOMBRETABLA + 
							" WHERE " +
							FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPERSONA + "=" + idPersona +
	            			" ORDER BY "+ 
	            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + " DESC";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge informacion sobre los abonos relacionados con un determinado usuario<br/> 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  idPersona - identificador de la persona asociada
	 * @param anyosAbono 
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public PaginadorBind getAbonosClientePaginador (String institucion, String idPersona, Integer anyosAbono,int destinatarioAbono ) throws ClsExceptions,SIGAException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" ( ");

			if(destinatarioAbono==DESTINATARIOABONO_SOCIEDAD || destinatarioAbono==DESTINATARIOABONO_SJCS){
				sql.append(" (SELECT A.IDABONO, A.NUMEROABONO, A.IDINSTITUCION, ");
				sql.append(" A.OBSERVACIONES,A.MOTIVOS,  A.FECHA, ");
				sql.append(" A.CONTABILIZADA, A.IDFACTURA, ");
				sql.append(" A.IDPERSONA, A.IDCUENTA, A.IDPAGOSJG, ");
				sql.append(" F_SIGA_ESTADOSABONO(A.IDINSTITUCION, A.IDABONO) AS ESTADO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALNETO(A.IDINSTITUCION, A.IDABONO) AS TOTALNETO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALIVA(A.IDINSTITUCION, A.IDABONO) AS TOTALIVA, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALABONADO(A.IDINSTITUCION, A.IDABONO) AS TOTALABONADO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTAL(A.IDINSTITUCION, A.IDABONO) AS TOTAL, ");
				sql.append(" PC.impret AS RET_JUDICIAL, ");
				sql.append(" PC.impmovvar AS MOV_VARIOS, ");
				sql.append(" (PC.impoficio + PC.impsoj + PC.impasistencia + PC.impejg) AS TOTAL_BRUTO,  ");  
				sql.append(" PC.IMPIRPF AS IRPF, ");				
				sql.append(" PC.IDPERORIGEN,PC.IDPERDESTINO ");
				
				sql.append(" FROM FAC_ABONO A, FCS_PAGO_COLEGIADO PC ");
				
				sql.append(" WHERE A.IDINSTITUCION = PC.IDINSTITUCION ");
				sql.append(" AND A.IDPAGOSJG = PC.IDPAGOSJG ");
				sql.append(" AND A.IDPERORIGEN = PC.IDPERORIGEN ");
				sql.append(" AND A.IDPERSONA = PC.IDPERDESTINO ");
				sql.append(" AND A.IDPERORIGEN = " + idPersona);
				sql.append(" AND A.IDINSTITUCION = " + institucion);
				if (anyosAbono!=null){		 
					sql.append(" AND SYSDATE - A.FECHA < ");
					sql.append(anyosAbono);
				}
				sql.append(" ) ");
			}
			
			if(destinatarioAbono==DESTINATARIOABONO_SOCIEDAD)
				sql.append(" UNION ");
			
			if(destinatarioAbono==DESTINATARIOABONO_SOCIEDAD || destinatarioAbono==DESTINATARIOABONO_NORMAL){
			
				sql.append(" (SELECT A.IDABONO, A.NUMEROABONO, A.IDINSTITUCION, "); 
				sql.append(" A.OBSERVACIONES,A.MOTIVOS,  A.FECHA, ");
				sql.append(" A.CONTABILIZADA, A.IDFACTURA, ");
				sql.append(" A.IDPERSONA, A.IDCUENTA, A.IDPAGOSJG, ");
				sql.append(" F_SIGA_ESTADOSABONO(A.IDINSTITUCION, A.IDABONO) AS ESTADO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALNETO(A.IDINSTITUCION, A.IDABONO) AS TOTALNETO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALIVA(A.IDINSTITUCION, A.IDABONO) AS TOTALIVA, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTALABONADO(A.IDINSTITUCION, A.IDABONO) AS TOTALABONADO, ");
				sql.append(" PKG_SIGA_TOTALESABONO.TOTAL(A.IDINSTITUCION, A.IDABONO) AS TOTAL, ");
				sql.append(" PC.impret AS RET_JUDICIAL, ");
				sql.append(" PC.impmovvar AS MOV_VARIOS, ");
				sql.append(" (PC.impoficio + PC.impsoj + PC.impasistencia + PC.impejg) AS TOTAL_BRUTO,  ");  
				sql.append(" PC.IMPIRPF AS IRPF, ");							
				sql.append(" A.IDPERSONA IDPERORIGEN,A.IDPERSONA IDPERDESTINO ");
				
				sql.append(" FROM FAC_ABONO A, FCS_PAGO_COLEGIADO PC ");
				
				sql.append(" WHERE A.IDINSTITUCION = PC.IDINSTITUCION ");
				sql.append(" AND A.IDPAGOSJG = PC.IDPAGOSJG ");
				sql.append(" AND A.IDPERSONA = PC.IDPERDESTINO ");				
				sql.append(" AND A.IDINSTITUCION = ");
				sql.append(institucion);				
				sql.append(" AND A.IDPERSONA = ");
				sql.append(idPersona);
				sql.append(" AND A.IDPAGOSJG IS NULL ");
				if (anyosAbono!=null){		 
					sql.append("  AND SYSDATE - A.FECHA < ");
					sql.append(anyosAbono);
				}				
				sql.append(" ) ");			
			}			
			sql.append(" ) ");			
			sql.append(" ORDER BY FECHA DESC"); 
	
			PaginadorBind paginador = new PaginadorBind(sql.toString(), new Hashtable());
			int totalRegistros = paginador.getNumeroTotalRegistros();

			if (totalRegistros==0){					
				paginador =null;
			}

			return paginador;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
		}
	}
	
	/** 
	 * Recoge informacion relativa a los pagos del registro pasado como parametro<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  abono - identificador del abono	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosPagosAbono (String institucion, String abono) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT 2 AS IDTABLA, " + 
	            				FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDABONO + " AS ABONO, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + " AS IDENTIFICADOR, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.pagosCaja'," + this.usrbean.getLanguage() + ") AS MODO, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IMPORTE + " AS IMPORTE, " +
								" '' AS NOMBRE_BANCO, null AS IDPAGOSJG " + 
							" FROM " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA + 
							" WHERE " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + abono +
								" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + institucion +
							
							" MINUS (" + 
				            "SELECT 2 AS IDTABLA, " + 
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDABONO + " AS ABONO, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + " AS IDENTIFICADOR, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.pagosCaja'," + this.usrbean.getLanguage() + ") AS MODO, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IMPORTE + " AS IMPORTE, " +
								" '' AS NOMBRE_BANCO, null AS IDPAGOSJG " + 
							" FROM " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "," + FacPagosPorCajaBean.T_NOMBRETABLA +
							" WHERE " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + abono +
							" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDINSTITUCION +
							" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDABONO +
							" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDPAGOABONO + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDPAGOABONO + ")" +		
							
							" UNION " +
				            "SELECT 2 AS IDTABLA, " + 
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDABONO + " AS ABONO, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IDPAGOABONO + " AS IDENTIFICADOR, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHA + " AS FECHA, " +
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_FECHAMODIFICACION   + " AS FECHA_ORDEN, "   +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.compensacion'," + this.usrbean.getLanguage() + ") || " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " AS MODO, " +								
								FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "." + FacPagoAbonoEfectivoBean.C_IMPORTE + " AS IMPORTE, " +
								" '' AS NOMBRE_BANCO,null AS IDPAGOSJG " + 
							" FROM " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA + "," + FacPagosPorCajaBean.T_NOMBRETABLA + "," + FacFacturaBean.T_NOMBRETABLA +
							" WHERE " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + abono +
								" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + institucion +
								" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDINSTITUCION + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDINSTITUCION +
								" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDABONO + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDABONO +
								" AND " + FacPagoAbonoEfectivoBean.T_NOMBRETABLA +"."+ FacPagoAbonoEfectivoBean.C_IDPAGOABONO + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDPAGOABONO +
								" AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDINSTITUCION +
								" AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "=" + FacPagosPorCajaBean.T_NOMBRETABLA +"."+ FacPagosPorCajaBean.C_IDFACTURA +
							
							" UNION " +
				            "SELECT 2 AS IDTABLA, " + 
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDABONO + " AS ABONO, " +
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IDDISQUETEABONO + " AS IDENTIFICADOR, " +
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_FECHAMODIFICACION + " AS FECHA, " +
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.pagosBanco'," + this.usrbean.getLanguage() + ") AS MODO, " +
								FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + "." + FacAbonoIncluidoEnDisqueteBean.C_IMPORTEABONADO + " AS IMPORTE, " +
								"(SELECT " + CenBancosBean.T_NOMBRETABLA + "." + CenBancosBean.C_NOMBRE + 
									" || ' nº ' || " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + 
									" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + ", " +
										CenBancosBean.T_NOMBRETABLA +
									" WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + " = " + CenBancosBean.T_NOMBRETABLA + "."+CenBancosBean.C_CODIGO +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + ") as NOMBRE_BANCO, null AS IDPAGOSJG " +
							" FROM " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA + ", " + 
										FacAbonoBean.T_NOMBRETABLA +
							" WHERE " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA +"."+ FacAbonoIncluidoEnDisqueteBean.C_IDABONO + "=" + abono +
								" AND " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA +"."+ FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION + "=" + institucion +
								" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " = " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA +"."+ FacAbonoIncluidoEnDisqueteBean.C_IDABONO +
								" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " = " + FacAbonoIncluidoEnDisqueteBean.T_NOMBRETABLA +"."+ FacAbonoIncluidoEnDisqueteBean.C_IDINSTITUCION +													
								
							" UNION " +
				            "SELECT 3 AS IDTABLA, " + 
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " AS ABONO, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + " AS IDENTIFICADOR, " +
								" SYSDATE AS FECHA, " +
								" SYSDATE AS FECHA_ORDEN, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.cambioBanco'," + this.usrbean.getLanguage() + ") AS MODO, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE, " +
								"(SELECT " + CenBancosBean.T_NOMBRETABLA + "." + CenBancosBean.C_NOMBRE + 
									" || ' nº ' || " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + 
									" FROM " + CenCuentasBancariasBean.T_NOMBRETABLA + ", " +
										CenBancosBean.T_NOMBRETABLA +
									" WHERE " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + " = " + CenBancosBean.T_NOMBRETABLA + "."+CenBancosBean.C_CODIGO +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA +
										" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + ") as NOMBRE_BANCO, " +
								"NVL(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + ",'') AS IDPAGOSJG " + 		
							" FROM " + FacAbonoBean.T_NOMBRETABLA + 
							" WHERE " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " = " + abono +
								" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " = " + institucion +
								" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + " IS NOT NULL " +
								" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " > 0 " +
							
								/*
							" UNION " +
				            "SELECT 3 AS IDTABLA, " + 
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " AS ABONO, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + " AS IDENTIFICADOR, " +
								" SYSDATE AS FECHA, " +
								" SYSDATE AS FECHA_ORDEN, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.pendienteCaja'," + this.usrbean.getLanguage() + ") AS MODO, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE " +
							" FROM " + FacAbonoBean.T_NOMBRETABLA + 
							" WHERE " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO + "=" + abono +
							" AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDCUENTA + " IS NULL" +
							" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR  + " > 0 " +
							*/
							
							" UNION " +
							" SELECT 1 AS IDTABLA, " + 
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " AS INSTITUCION, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " AS ABONO, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + " AS IDENTIFICADOR, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + " AS FECHA, " +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHAMODIFICACION + " AS FECHA_ORDEN, " +
								" F_SIGA_GETRECURSO_ETIQUETA('facturacion.pagosAbonos.accion.emisionPago'," + this.usrbean.getLanguage() + ") AS MODO, " +
								" 0 AS IMPORTE, " +
								" '' AS NOMBRE_BANCO, " + 
								"NVL(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + ",'') AS IDPAGOSJG " +
							" FROM " + FacAbonoBean.T_NOMBRETABLA + 
							" WHERE " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO + "=" + abono +
								" AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" ORDER BY IDTABLA ASC, FECHA ASC, FECHA_ORDEN ASC, IDENTIFICADOR ASC";
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre datos de pago asociados a abonos.");
	       }
	       return datos;                        
	    }
	

	
	/** 
	 * Recoge informaciones totales de los pagos asociados al registro pasado como parametro<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  abono - identificador del abono	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getTotalesPagos (String institucion, String abono) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   int contador = 0;	            
	        Hashtable codigos = new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS PENDIENTE," + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTAL + " AS TOTAL," + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALABONADO + " AS TOTALABONADO," + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + //"," +
    						//"PKG_SIGA_TOTALESABONO.TOTAL("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTAL" + "," +
							//"PKG_SIGA_TOTALESABONO.TOTALABONADO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS TOTALABONADO" + "," +
							//"PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS PENDIENTE" +
							" FROM " + FacAbonoBean.T_NOMBRETABLA;
				            contador++;
							codigos.put(new Integer(contador),abono);
							sql += " WHERE " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDABONO + "=:" + contador;
							contador++;
							codigos.put(new Integer(contador),institucion);
							sql += " AND " + FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=:" + contador;
				
				if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable hfila = fila.getRow();
//	                  String idAbonoFila = (String) hfila.get("IDABONO");
//	                  ConsPLFacturacion pl=new ConsPLFacturacion();
//	                  
//	                  Hashtable codigos2 = new Hashtable();
//	                  codigos2.put(new Integer(1), institucion);
//	                  codigos2.put(new Integer(2), abono);
//	                  	                  
//	                  hfila.put("TOTAL", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.TOTAL"));
//	                  hfila.put("TOTALABONADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.TOTALABONADO"));
//	                  hfila.put("PENDIENTE", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR"));

	                  fila.setRow(hfila);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge los abonos que van destinados al banco con comisiones ajenas más baratas<br/>
	 * o a los bancos con los que no trabaja la institucion 
	 * @param  institucion - identificador de la institucion
	 * @param  codigoBanco - identificador del banco que cobra menor comision ajena	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getAbonosBancosMenorComision (String institucion, String codigoBanco) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
			    			FacAbonoBean.C_IDINSTITUCION + "," +
							FacAbonoBean.C_IDABONO + "," +
							FacAbonoBean.C_IDPERSONA + "," +
							FacAbonoBean.C_NUMEROABONO + "," +
							FacAbonoBean.C_OBSERVACIONES + "," +
							FacAbonoBean.C_MOTIVOS + "," +
							FacAbonoBean.C_IDCUENTA + "," +
							CenCuentasBancariasBean.C_IBAN + "," +
							CenCuentasBancariasBean.C_CBO_CODIGO + "," +
			    			CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
			    			CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
			    			CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
			    			"IMPORTE" +
							" FROM (" +
								" SELECT " +				
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE," +
//		            			"PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS IMPORTE, " +
		            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES  +
								" FROM " + FacAbonoBean.T_NOMBRETABLA + "," + CenCuentasBancariasBean.T_NOMBRETABLA +
								" WHERE " +
								FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
								" AND " +
								FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_ESTADO + "=5" +
//		            			"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 5" +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
				    			" AND " +
								FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " is null " +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
								" MINUS " +
								" SELECT " +				
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
								FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE," +
//								"PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS IMPORTE, " +
		            			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES  +
								" FROM " + FacAbonoBean.T_NOMBRETABLA + "," + CenCuentasBancariasBean.T_NOMBRETABLA +
								" WHERE " +
								FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
								" AND " +
								FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_ESTADO + "=5" +
//		            			"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 5" +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
								" AND " +
				    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
								" AND " +
								CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "= ANY (" + 
									" SELECT " +				
									FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO +
									" FROM " + FacBancoInstitucionBean.T_NOMBRETABLA + 
									" WHERE " +
									FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_IDINSTITUCION + "=" + institucion +
									" AND " +
									FacBancoInstitucionBean.T_NOMBRETABLA +"."+ FacBancoInstitucionBean.C_BANCOS_CODIGO + "<>" + codigoBanco +
									" AND " +
					    			FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_FECHABAJA + " IS NULL)" +
								")" +
							" ORDER BY "+ 								
							CenCuentasBancariasBean.C_CBO_CODIGO + "," + CenCuentasBancariasBean.C_IBAN;

	            if (rc.findForUpdate(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge los abonos que van destinados a cada banco-sufijo
	 * @param  institucion - identificador de la institucion
	 * @param  codigoBanco - identificador del banco	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getAbonosBanco (Integer institucion, String codigoBanco, Integer Idsufijo) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +				
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE " + 
//	            			"PKG_SIGA_TOTALESABONO.PENDIENTEPORABONAR("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") AS IMPORTE" +
							" FROM " + 	FacAbonoBean.T_NOMBRETABLA + "," + CenCuentasBancariasBean.T_NOMBRETABLA + "," +
										FacFacturaBean.T_NOMBRETABLA + "," + FacSerieFacturacionBancoBean.T_NOMBRETABLA +
							" WHERE " +
							FacSerieFacturacionBancoBean.T_NOMBRETABLA+ "." + FacSerieFacturacionBancoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacSerieFacturacionBancoBean.T_NOMBRETABLA+ "." + FacSerieFacturacionBancoBean.C_IDSUFIJO + "="  + Idsufijo +
							" AND " +
							FacSerieFacturacionBancoBean.T_NOMBRETABLA+ "." + FacSerieFacturacionBancoBean.C_BANCOS_CODIGO + "="  + codigoBanco +
							" AND " +							
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + "=" + FacSerieFacturacionBancoBean.T_NOMBRETABLA+ "." + FacSerieFacturacionBancoBean.C_IDSERIEFACTURACION +
							" AND " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "=" + FacSerieFacturacionBancoBean.T_NOMBRETABLA + "." + FacSerieFacturacionBancoBean.C_IDINSTITUCION +
							" AND " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDFACTURA + "=" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
							" AND " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " +
							FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " IS NULL " +
							" AND " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_ESTADO + "= 5 " +
//	            			"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 5" +
							" AND " +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
							" AND " +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
							" AND " +
			    			FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
							" AND " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "=" + codigoBanco +
							" ORDER BY "+ 								
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN;

	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
//			catch (SIGAException e) {
//				throw e;
//			}
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return datos;                        
	    }

	/** 
	 * Obtiene los importes apuntados en compras para una institución y abono, y la cuenta contable del producto o servicio. 
	 * @param  institucion - identificador de la institucion
	 * @param  idAbono - Identificador de abono	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerCuentasValorPYS (String institucion, String idAbono) throws ClsExceptions {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql =" select NVL(ROUND(a.CANTIDAD * a.PRECIOUNITARIO, 2), 0) NETO, c.CUENTACONTABLE CUENTA "; 
	            	sql +=" from   fac_lineafactura a, pys_compra b, pys_productosinstitucion c";
		            sql +="  where  a.IDFACTURA = b.IDFACTURA";
			        sql +="  and    a.NUMEROLINEA = b.NUMEROLINEA";
				    sql +="  and    a.IDINSTITUCION = b.IDINSTITUCION";
					sql +="  and    b.IDINSTITUCION = c.IDINSTITUCION  ";
					sql +="  and    b.IDTIPOPRODUCTO = c.IDTIPOPRODUCTO ";
					sql +="  and    b.IDPRODUCTO = c.IDPRODUCTO";
					sql +="  and    b.IDPRODUCTOINSTITUCION = c.IDPRODUCTOINSTITUCION";
					sql +="  and    a.IDFACTURA IN (SELECT E.IDFACTURA FROM FAC_ABONO E"; 
					sql +="  					   WHERE E.IDINSTITUCION=a.IDINSTITUCION";
					sql +=" AND	 E.IDABONO = "+idAbono+")";
					sql +="  and    a.IDINSTITUCION = "+institucion;
					sql +=" union all ";
					sql +="   select NVL(ROUND(a2.CANTIDAD * a2.PRECIOUNITARIO, 2), 0) NETO, c2.CUENTACONTABLE CUENTA "; 
					sql +="  from   fac_lineafactura a2, fac_facturacionsuscripcion b2, pys_serviciosinstitucion c2";
					sql +="  where  a2.IDFACTURA = b2.IDFACTURA";
					sql +="  and    a2.NUMEROLINEA = b2.NUMEROLINEA";
					sql +="  and    a2.IDINSTITUCION = b2.IDINSTITUCION";
					sql +="  and    b2.IDINSTITUCION = c2.IDINSTITUCION  ";
					sql +="  and    b2.IDTIPOSERVICIOS = c2.IDTIPOSERVICIOS"; 
					sql +="  and    b2.IDSERVICIO = c2.IDSERVICIO";
					sql +="  and    b2.IDSERVICIOSINSTITUCION = c2.IDSERVICIOSINSTITUCION";
					sql +="  and    a2.IDFACTURA IN (SELECT E2.IDFACTURA FROM FAC_ABONO E2 ";
					sql +="  					   WHERE E2.IDINSTITUCION=a2.IDINSTITUCION";
					sql +=" 					   AND	 E2.IDABONO = "+idAbono+")";
                    sql +="  and    a2.IDINSTITUCION = " + institucion;
                    

	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
	       }
	       return datos;                        
	    }

	/**
	 * Obtiene los datos fundamentales que se mostrara en el informe tipo MasterRepor de un abono
	 * @param idInstitucion de la factura
	 * @param idAbono de la factura
	 * @return datos asociados al informe
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getDatosImpresionInformeAbono (String idInstitucion, String idAbono)  throws ClsExceptions {
		
		Hashtable nuevo= new Hashtable();
		
		try {
			String select = " SELECT " + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + ", " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO 	+ ", " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + ", " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_FECHA + ", " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + ", " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + ", " +
							//"(SELECT NUMEROFACTURA FROM FAC_FACTURA WHERE IDINSTITUCION="+FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " AND IDFACTURA="+FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDFACTURA+ ") AS NUMEROFACTURA, " +
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + ", " +
							"lpad(substr("+CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ",7),10,'*') NUMEROCUENTA, " +
							"-1*"+FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALNETO + " AS TOTAL_NETO, " +
							"-1*"+FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTALIVA + " AS TOTAL_IVA, " +
							"-1*"+FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPTOTAL + " AS TOTAL_ABONO " +
//							"-1*PKG_SIGA_TOTALESABONO.TOTALNETO(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + ", " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") as TOTAL_NETO, " +
//							"-1*PKG_SIGA_TOTALESABONO.TOTALIVA(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + ", " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") as TOTAL_IVA, " +
//							"-1*PKG_SIGA_TOTALESABONO.TOTAL(" + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + ", " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") as TOTAL_ABONO " +
							" FROM " + 
							FacAbonoBean.T_NOMBRETABLA + 
							" LEFT JOIN " + 
								CenCuentasBancariasBean.T_NOMBRETABLA + 
								" ON " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA +
							" WHERE " + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + " = " + idInstitucion +
							" AND " + 
							FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + " = " + idAbono;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(select)) {
				if (rc.size() > 1) return null;
				Hashtable abono = (Hashtable)((Row) rc.get(0)).getRow();
				
				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.usrbean);
				nuevo.put("NOMBRE_COLEGIO",institucionAdm.getNombreInstitucion((String)abono.get(CenInstitucionBean.C_IDINSTITUCION)));
 
				String idPersona=institucionAdm.getIdPersona((String)abono.get(CenInstitucionBean.C_IDINSTITUCION));
				// direccion institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)abono.get(FacAbonoBean.C_IDINSTITUCION),"3");
				if (direccion.get("DOMICILIO")!=null){
					nuevo.put("DIRECCION_COLEGIO",(String)direccion.get(CenDireccionesBean.C_DOMICILIO));
				}	

				direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,(String)abono.get(FacAbonoBean.C_IDINSTITUCION),"3");
				if (direccion.get(CenDireccionesBean.C_IDPAIS)!=null){
					if ((direccion.get(CenDireccionesBean.C_IDPAIS).equals("") || direccion.get(CenDireccionesBean.C_IDPAIS).equals(ClsConstants.ID_PAIS_ESPANA)) && (direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null)&&(direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_COLEGIO",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA"));
					} else {
						CenPaisAdm admPais = new CenPaisAdm(this.usrbean);
						String pais = admPais.getDescripcion((String)direccion.get(CenDireccionesBean.C_IDPAIS));
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_COLEGIO",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACIONEXTRANJERA") + ", "+ pais);
					}
				}

				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)abono.get(FacAbonoBean.C_IDINSTITUCION),"3");
				String resultado="";
				if (direccion.get("TELEFONO1")!=null){
					if (!((String)direccion.get("TELEFONO1")).equalsIgnoreCase("")){
						resultado+=(String)direccion.get(CenDireccionesBean.C_TELEFONO1);
						nuevo.put("TELEFONO1",(String)direccion.get(CenDireccionesBean.C_TELEFONO1));
					}	
				}
				if (direccion.get("TELEFONO2")!=null){
					if (!((String)direccion.get("TELEFONO2")).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_TELEFONO2);
					}
				}
				if (direccion.get("MOVIL")!=null){
					if (!((String)direccion.get("MOVIL")).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_MOVIL);
					}
				}
				if (direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)).equalsIgnoreCase("")){
						resultado+="  "+(String)direccion.get("CORREOELECTRONICO");
					}
				}
				nuevo.put("TELEFONOS_EMAIL_COLEGIO",resultado);

				idPersona=institucionAdm.getIdPersona((String)abono.get(CenInstitucionBean.C_IDINSTITUCION));
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
				nuevo.put("CIF_COLEGIO",personaAdm.obtenerNIF(idPersona));	

				// TRATAMIENTO PARA LA PERSONA DE LA ABONO
				idPersona=(String)abono.get(CenInstitucionBean.C_IDPERSONA);
				
				// RGG ?? nuevo.put("CIF_COLEGIO",personaAdm.obtenerNombreApellidos(idPersona));
				
				String nombre = personaAdm.obtenerNombreApellidos(idPersona);
				if (nombre!=null) {
					nuevo.put("NOMBRE_CLIENTE",nombre);
				}else{
					nuevo.put("NOMBRE_CLIENTE","-");
				}
				
				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)abono.get(FacAbonoBean.C_IDINSTITUCION),"8");
				if (direccion.size()==0){// si no existe direccion de despacho, mostramos la de correo
					direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)abono.get(FacAbonoBean.C_IDINSTITUCION),"3");
				}
				
				if (direccion.get(CenDireccionesBean.C_DOMICILIO)!=null&&!((String)direccion.get(CenDireccionesBean.C_DOMICILIO)).equals("")){
					nuevo.put("DIRECCION_CLIENTE",(String)direccion.get(CenDireccionesBean.C_DOMICILIO));
				}else{
					nuevo.put("DIRECCION_CLIENTE","-");
				}
				
				if (direccion.get(CenDireccionesBean.C_IDPAIS)!=null){
					if ((direccion.get(CenDireccionesBean.C_IDPAIS).equals("") || direccion.get(CenDireccionesBean.C_IDPAIS).equals(ClsConstants.ID_PAIS_ESPANA)) && (direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null)&&(direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_CLIENTE",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA"));
					}else{
						CenPaisAdm admPais = new CenPaisAdm(this.usrbean);
						String pais = admPais.getDescripcion((String)direccion.get(CenDireccionesBean.C_IDPAIS));
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_CLIENTE",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACIONEXTRANJERA") + ", "+ pais);
					}
				}else{
					nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_CLIENTE","-");
					
				}

				
				
				
				resultado="";
				
				if (direccion.get(CenDireccionesBean.C_TELEFONO1)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_TELEFONO1)).equalsIgnoreCase("")){
						resultado=(String)direccion.get(CenDireccionesBean.C_TELEFONO1);
					}else{
						resultado="-";
					}
				}else{
					resultado="-";
				}
				if (direccion.get(CenDireccionesBean.C_TELEFONO2)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_TELEFONO2)).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_TELEFONO2);
					}
				}
				if (direccion.get(CenDireccionesBean.C_MOVIL)!=null){
					if (!((String)direccion.get(CenDireccionesBean.C_MOVIL)).equalsIgnoreCase("")){
						resultado+=", "+(String)direccion.get(CenDireccionesBean.C_MOVIL);
					}
				}				
				nuevo.put("TELEFONOS_CLIENTE",resultado);
				
				
				if (direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)!=null&&!((String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO)).equals("")){
					nuevo.put("EMAIL_CLIENTE",(String)direccion.get(CenDireccionesBean.C_CORREOELECTRONICO));
				}else{
					nuevo.put("EMAIL_CLIENTE","-");
				}

				resultado=personaAdm.obtenerNIF(idPersona);	
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("NIFCIF_CLIENTE","-");
				} else {
					nuevo.put("NIFCIF_CLIENTE",resultado);
				}
						
				resultado=(String)abono.get(FacAbonoBean.C_NUMEROABONO);
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("NUMERO_ABONO","-");
				} else {
					nuevo.put("NUMERO_ABONO",resultado);
				}

				resultado=GstDate.getFormatedDateShort("",(String)abono.get(FacAbonoBean.C_FECHA));
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("FECHA_ABONO","-");
				} else {
					nuevo.put("FECHA_ABONO",resultado);
				}

				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.usrbean);
				CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(new Long(idPersona), new Integer((String)abono.get(FacAbonoBean.C_IDINSTITUCION)));
				resultado = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("NUMERO_COLEGIADO_ABONO","-");
				} else {
					nuevo.put("NUMERO_COLEGIADO_ABONO",resultado);
				}
                
				if (abono.get(FacAbonoBean.C_IDCUENTA)!=null && (!((String)abono.get(FacAbonoBean.C_IDCUENTA)).equalsIgnoreCase("")) ){
					resultado=UtilidadesString.getMensajeIdioma(this.usrbean,"facturacion.abonosPagos.boton.pagoDomiciliacionBanco");
				}else{
				  if ((abono.get(FacAbonoBean.C_IDCUENTA)==null)||(((String)abono.get(FacAbonoBean.C_IDCUENTA))).equalsIgnoreCase("")){
					resultado=UtilidadesString.getMensajeIdioma(this.usrbean,"facturacion.abonosPagos.boton.pagoCaja");
				  }else{
					resultado=(String)abono.get("IBAN");
				  }
				}  
				nuevo.put("FORMA_PAGO_ABONO",resultado);
				
				resultado=(String)abono.get("TOTAL_ABONO");//+" \u20AC";
				nuevo.put("TOTAL_ABONO",UtilidadesNumero.formato(resultado));

				resultado=(String)abono.get("TOTAL_NETO");//+" \u20AC";
				nuevo.put("IMPORTE_NETO",UtilidadesNumero.formato(resultado));

				resultado=(String)abono.get("TOTAL_IVA");//+" \u20AC";
				nuevo.put("IMPORTE_IVA",UtilidadesNumero.formato(resultado));
				
				/*
				if (abono.get("NUMEROFACTURA")!=null && !((String)abono.get("NUMEROFACTURA")).equals("")) {
					resultado=(String)abono.get("NUMEROFACTURA");
					resultado=UtilidadesString.getMensajeIdioma(this.usrbean.getLanguage(),"messages.facturaRectificativa.NumeroFactura") +" "+resultado;
					nuevo.put("OBSERVACIONES",resultado);
				} else {
				    nuevo.put("OBSERVACIONES","");
				}
				*/
				resultado=(String)abono.get("OBSERVACIONES");
				nuevo.put("OBSERVACIONES",resultado);
				
			}
		}
	    
	    catch (Exception e) {
	    	throw new ClsExceptions(e, "Error al obtener los datos para el informe MasterRepor de un abono.");
	    }
		return nuevo;
	}

	/** 
	 * Recoge los abonos de SJCS pendientes de pagar por banco
	 * @param  institucion - identificador de la institucion
	 * @param  codigoBanco - identificador del banco	 	  
	 * @param  tipoSEPA    - indica si estamos obteniendo los propósitos SEPA o los de otras Transferencias	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getAbonosBancoSjcs (String institucion, String codigoBanco, Integer idsufijo) throws ClsExceptions,SIGAException {
		Vector datos=new Vector();
		Hashtable codigos = new Hashtable();
		try {

			RowsContainer rc = new RowsContainer();
			String sql =
					"SELECT " +	FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_NUMEROABONO + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_OBSERVACIONES + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_MOTIVOS + "," +
					"       " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + "," +
					"       " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + "," +
					"       " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + "," +
					"       " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + "," +
					"       " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + "," +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR + " AS IMPORTE," +
					"       " + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_CONCEPTO + " , " +
					"       " + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDSUFIJO + " , " +
					"       " + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_ABREVIATURA + " as NOMBREPAGO, " +
					"       " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERORIGEN + " ";
							
			sql = sql +baseSqlAbonosSJCSpendientes;
			
			codigos.put(new Integer(1),institucion);
			codigos.put(new Integer(2),codigoBanco);
            codigos.put(new Integer(3),"5");
            codigos.put(new Integer(4),"0.0");

			sql += "AND "+FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDSUFIJO+ " = " + idsufijo;
			
			
			if (rc.findBind(sql, codigos)) {
				for (int i = 0; i < rc.size(); i++){
					Row fila = (Row) rc.get(i);
					datos.add(fila);
					}
				}
			}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
		}
		return datos;                        
	} //getAbonosBancoSjcs()
	
	/**
	 * Obtiene el resumen de retenciones IRPF dados los parametros
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @param periodo
	 * @param anyoRetencionIRPF
	 * @param idioma
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getRetencionesIRPF(String idInstitucion, String idPersona, String periodo, String anyoRetencionIRPF, String idioma) throws ClsExceptions, SIGAException {
		Vector datosObtenidos = new Vector();
		
		try {
			//construyendo consulta
			StringBuffer sql = new StringBuffer();
			Hashtable htCodigos = new Hashtable();
			int codigo = 0;
			

			sql.append(" SELECT IDPERSONASJCS, ");
//			TOTALIMPORTESJCS importe total (suejto mas exento)
			sql.append(" SUM(TOTALIMPORTESJCSSUJETO) + SUM(IMPORTETOTALMVTOSSUJETO)+SUM(TOTALIMPORTESJCSEXENTO) + SUM(IMPORTETOTALMVTOSEXENTO) TOTALIMPORTESJCS, ");
//			TOTALIMPORTESJCSEXENTO importe sjcs exentos de irpf mas movimientos varios exentos de irpf
			sql.append(" SUM(TOTALIMPORTESJCSEXENTO) + SUM(IMPORTETOTALMVTOSEXENTO) TOTALIMPORTESJCSEXENTO, ");
//			TOTALIMPORTESJCSSUJETO importe sjcs sujetos a irpf mas movimientos varios sujetos a irpf
			sql.append(" SUM(TOTALIMPORTESJCSSUJETO) + SUM(IMPORTETOTALMVTOSSUJETO) TOTALIMPORTESJCSSUJETO, ");
//			TOTALIMPORTEIRPF suma total de los importes retenidos
			sql.append(" ABS(SUM(TOTALIMPORTEIRPF)) TOTALIMPORTEIRPF ");
			sql.append(" FROM ( ");


			sql.append(" SELECT  TOTAL.IDPERSONASJCS, ");
			sql.append(" TOTAL.IDPAGOS, ");
			sql.append(" SUM(TOTAL.TOTALIMPORTESJCSSUJETO) AS TOTALIMPORTESJCSSUJETO, ");
			sql.append(" SUM(TOTAL.IMPORTETOTALRETENCIONESSUJETO) AS IMPORTETOTALRETENCIONESSUJETO, ");
			sql.append(" SUM(TOTAL.IMPORTETOTALMVTOSSUJETO) AS IMPORTETOTALMVTOSSUJETO, ");
			sql.append(" SUM(TOTAL.TOTALIMPORTEIRPF) AS TOTALIMPORTEIRPF, ");
			sql.append(" SUM(TOTAL.TOTALIMPORTESJCSEXENTO) AS TOTALIMPORTESJCSEXENTO, ");
			sql.append(" SUM(TOTAL.IMPORTETOTALMVTOSEXENTO) AS IMPORTETOTALMVTOSEXENTO ");
			sql.append(" FROM  ");
		               
			//La query sera la union de los pagos que han tenido retenciones mas los qu no han tenido retenciones
			//De este modo sacaremos el importe exento y el sujeto. Ademas sacarenmos el total en la query total de arriba
			
//			query para sacar los pagon cuya retencion sea mayor que cero
			sql.append(" (SELECT PC.IDINSTITUCION, ");
			sql.append(" PC.IDPAGOSJG as IDPAGOS, ");
			sql.append(" PC.IDPERDESTINO as IDPERSONASJCS, ");
			sql.append(" SUM(PC.IMPOFICIO + PC.IMPASISTENCIA + PC.IMPEJG + PC.IMPSOJ) AS TOTALIMPORTESJCSSUJETO, ");
			sql.append(" SUM(PC.IMPRET) AS IMPORTETOTALRETENCIONESSUJETO, ");
			sql.append(" SUM(PC.IMPMOVVAR) AS IMPORTETOTALMVTOSSUJETO,  -1 * ");
			sql.append(" ROUND(ABS(SUM((PC.IMPOFICIO + PC.IMPASISTENCIA + PC.IMPEJG + ");
			sql.append(" PC.IMPSOJ + PC.IMPMOVVAR) * PC.IMPIRPF / 100)), ");
			sql.append(" 2) AS TOTALIMPORTEIRPF, ");
			sql.append(" 0 AS TOTALIMPORTESJCSEXENTO, ");
			sql.append(" 0 AS IMPORTETOTALRETENCIONESEXENTO, ");
			sql.append(" 0 AS IMPORTETOTALMVTOSEXENTO ");
		         
			sql.append(" FROM FCS_PAGO_COLEGIADO PC ");
			sql.append(" WHERE PC.IDINSTITUCION =  ");
			sql.append(idInstitucion);
			sql.append(" AND PC.IDPAGOSJG = NVL(null, PC.IDPAGOSJG) ");
			if(idPersona!=null){
				sql.append(" AND PC.IDPERDESTINO = ");
				sql.append(idPersona);
			}
			sql.append(" AND NVL(PC.IMPIRPF, 0) <> 0 ");
			sql.append(" GROUP BY PC.IDPERDESTINO, ");
			sql.append(" PC.IDPAGOSJG, ");
			sql.append(" PC.IDINSTITUCION, ");
			sql.append(" PC.IMPIRPF, ");
			sql.append(" PC.IDCUENTA ");
			sql.append(" UNION ");
//			query para sacar los pagos cuya retencion sea cero
//		         ----------------------------------------------------
			sql.append(" SELECT PC.IDINSTITUCION, ");
			sql.append(" PC.IDPAGOSJG as IDPAGOS, ");
			sql.append(" PC.IDPERDESTINO as IDPERSONASJCS, ");
			
			sql.append(" 0 AS TOTALIMPORTESJCSSUJETO, ");
			sql.append(" 0 AS IMPORTETOTALRETENCIONESSUJETO, ");
			sql.append(" 0 AS IMPORTETOTALMVTOSSUJETO, ");
			sql.append(" 0 AS TOTALIMPORTEIRPF, ");
			sql.append(" SUM(PC.IMPOFICIO + PC.IMPASISTENCIA + PC.IMPEJG + PC.IMPSOJ) AS TOTALIMPORTESJCSEXENTO, ");
			sql.append(" SUM(PC.IMPRET) AS IMPORTETOTALRETENCIONESEXENTO, ");
			sql.append(" SUM(PC.IMPMOVVAR) AS IMPORTETOTALMVTOSEXENTO ");
		         
			sql.append(" FROM FCS_PAGO_COLEGIADO PC ");
			sql.append(" WHERE PC.IDINSTITUCION =  ");
			sql.append(idInstitucion);
			sql.append(" AND PC.IDPAGOSJG = NVL(null, PC.IDPAGOSJG) ");
			if(idPersona!=null){
				sql.append(" AND PC.IDPERDESTINO = ");
				sql.append(idPersona);
			}
			sql.append(" AND NVL(PC.IMPIRPF, 0) = 0 ");
			sql.append(" GROUP BY PC.IDPERDESTINO, ");
			sql.append(" PC.IDPAGOSJG, ");
			sql.append(" PC.IDINSTITUCION, ");
			sql.append(" PC.IMPIRPF, ");
			sql.append(" PC.IDCUENTA ");
			sql.append(" )TOTAL ");
			
			
			sql.append("	WHERE IDPAGOS IN (");
			sql.append("		SELECT FAC_ABONO.IDPAGOSJG ");
			sql.append("		FROM FAC_ABONO ");
			sql.append("		WHERE FAC_ABONO.IDINSTITUCION = TOTAL.IDINSTITUCION ");
			sql.append("			AND FAC_ABONO.IDPAGOSJG = TOTAL.IDPAGOS ");
			sql.append("			AND FAC_ABONO.IDPERSONA = TOTAL.IDPERSONASJCS ");
			
			//obteniendo los abonos dentro del periodo indicado
			sql.append("			AND TRUNC(Fecha) BETWEEN (");
			
			codigo++;
			htCodigos.put(new Integer(codigo), anyoRetencionIRPF);
			sql.append("				SELECT To_Date(PER.Diainicio || '/' || PER.Mesinicio || '/' || NVL(PER.Anioinicio, :" + codigo + "), 'dd/mm/yyyy') ");
			sql.append("				FROM Gen_Periodo PER ");
			
			codigo++;
			htCodigos.put(new Integer(codigo), periodo);
			sql.append("				WHERE PER.Idperiodo = :" + codigo);
			sql.append("			) AND ( ");
			
			codigo++;
			htCodigos.put(new Integer(codigo), anyoRetencionIRPF);
			sql.append("				SELECT To_Date(PER.Diafin || '/' || PER.Mesfin || '/' || NVL(PER.Anioinicio, :" + codigo + "), 'dd/mm/yyyy') ");
			sql.append("				FROM Gen_Periodo PER ");
			
			codigo++;
			htCodigos.put(new Integer(codigo), periodo);
			sql.append("				WHERE PER.Idperiodo = :" + codigo);
			sql.append("			) ");
			sql.append("	) ");
			sql.append("	GROUP BY TOTAL.IDPERSONASJCS, TOTAL.IDPAGOS ");
			sql.append(") ");			
			sql.append("GROUP BY IDPERSONASJCS ");

			//consultando en BD
			RowsContainer rc = new RowsContainer();
			if (rc.findBind(sql.toString(), htCodigos)) {

				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					Hashtable htRegistro = fila.getRow();
					
					if (idPersona != null) {						
						if (htRegistro.get("IDPERSONASJCS") == null || ((String) htRegistro.get("IDPERSONASJCS")).equals(""))
							htRegistro.put("IDPERSONASJCS", idPersona);
						if (htRegistro.get("TOTALIMPORTESJCS") == null || ((String) htRegistro.get("TOTALIMPORTESJCS"))	.equals(""))
							htRegistro.put("TOTALIMPORTESJCS", "0");
						if (htRegistro.get("TOTALIMPORTEIRPF") == null || ((String) htRegistro.get("TOTALIMPORTEIRPF")).equals(""))
							htRegistro.put("TOTALIMPORTEIRPF", "0");
						if (htRegistro.get("TOTALIMPORTESJCSEXENTO") == null || ((String) htRegistro.get("TOTALIMPORTESJCSEXENTO")).equals(""))
							htRegistro.put("TOTALIMPORTESJCSEXENTO", "0");
						if (htRegistro.get("TOTALIMPORTESJCSSUJETO") == null || ((String) htRegistro.get("TOTALIMPORTESJCSSUJETO")).equals(""))
							htRegistro.put("TOTALIMPORTESJCSSUJETO", "0");
					}
					datosObtenidos.add(htRegistro);
				}
			}
			
			// si se solicita para una persona se ponen totales 0 cuando no haya tenido ningun pago
			if (idPersona != null) {
				if (datosObtenidos == null || datosObtenidos.size() < 1) {
					datosObtenidos = new Vector();
					Hashtable htRegistro = new Hashtable();
					htRegistro.put("TOTALIMPORTESJCS", "0");
					htRegistro.put("TOTALIMPORTEIRPF", "0");
					htRegistro.put("IDPERSONASJCS", idPersona);
					htRegistro.put("TOTALIMPORTESJCSEXENTO", "0");
					htRegistro.put("TOTALIMPORTESJCSSUJETO", "0");
					
					datosObtenidos.add(htRegistro);
				}
			} else {
				if (datosObtenidos != null && datosObtenidos.size() < 1)
					throw new SIGAException("gratuita.retencionesIRPF.informe.mensaje.sinDatos");
			}
		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			throw new ClsExceptions(e, "FacAbonoAdm.getRetencionesIRPF");
		}
		
		return datosObtenidos;
	} //getRetencionesIRPF()
	
	// del merge (revisar)
	public boolean isAbonoConPago (String idAbono, Integer idInstitucion) throws ClsExceptions, SIGAException
	{
		boolean isAbonoConPago = false;
		try
		{
			Hashtable htCodigos = new Hashtable();
			int contador = 0;
	
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT FAC_PAGOABONOEFECTIVO.IDINSTITUCION AS INSTITUCION, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.IDABONO AS ABONO, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.IDPAGOABONO AS IDENTIFICADOR, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.FECHA AS FECHA, ");
			sql.append(" 'Caja' AS MODO, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.IMPORTE AS IMPORTE ");
			sql.append(" FROM FAC_PAGOABONOEFECTIVO ");
			sql.append(" WHERE FAC_PAGOABONOEFECTIVO.IDABONO = :");
			contador++;
			htCodigos.put(new Integer(contador), idAbono);
			sql.append(contador);
			sql.append(" AND FAC_PAGOABONOEFECTIVO.IDINSTITUCION = :");
			contador++;
			htCodigos.put(new Integer(contador), idInstitucion);
			sql.append(contador);
			
			sql.append(" MINUS  ");
			
			sql.append(" (SELECT FAC_PAGOABONOEFECTIVO.IDINSTITUCION AS INSTITUCION, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.IDABONO AS ABONO, ");
			sql.append(" FAC_PAGOABONOEFECTIVO.IDPAGOABONO AS IDENTIFICADOR, ");
		    sql.append(" FAC_PAGOABONOEFECTIVO.FECHA AS FECHA, ");
		    sql.append(" 'Caja' AS MODO, ");
		    sql.append(" FAC_PAGOABONOEFECTIVO.IMPORTE AS IMPORTE ");
		    sql.append(" FROM FAC_PAGOABONOEFECTIVO, FAC_PAGOSPORCAJA ");
		    sql.append(" WHERE FAC_PAGOABONOEFECTIVO.IDABONO =  :");
		    contador++;
			htCodigos.put(new Integer(contador), idAbono);
			sql.append(contador);
		    sql.append(" AND FAC_PAGOABONOEFECTIVO.IDINSTITUCION =  :");
		    contador++;
			htCodigos.put(new Integer(contador), idInstitucion);
			sql.append(contador);
		    sql.append(" AND FAC_PAGOABONOEFECTIVO.IDINSTITUCION = ");
		    sql.append(" FAC_PAGOSPORCAJA.IDINSTITUCION ");
		    sql.append(" AND FAC_PAGOABONOEFECTIVO.IDABONO = FAC_PAGOSPORCAJA.IDABONO ");
		    sql.append(" AND FAC_PAGOABONOEFECTIVO.IDPAGOABONO = ");
		    sql.append(" FAC_PAGOSPORCAJA.IDPAGOABONO) ");

		    sql.append(" UNION ");
		    
		    sql.append(" SELECT FAC_ABONOINCLUIDOENDISQUETE.IDINSTITUCION, ");
		    sql.append(" FAC_ABONOINCLUIDOENDISQUETE.IDABONO, ");
		    sql.append(" FAC_ABONOINCLUIDOENDISQUETE.IDDISQUETEABONO, ");
		    sql.append(" FAC_ABONOINCLUIDOENDISQUETE.FECHAMODIFICACION, ");
		    sql.append(" 'Banco', ");
		    sql.append(" FAC_ABONOINCLUIDOENDISQUETE.IMPORTEABONADO ");
		    sql.append(" FROM FAC_ABONOINCLUIDOENDISQUETE ");
		    sql.append(" WHERE FAC_ABONOINCLUIDOENDISQUETE.IDABONO = :");
		    contador++;
			htCodigos.put(new Integer(contador), idAbono);
			sql.append(contador);
		    sql.append(" AND FAC_ABONOINCLUIDOENDISQUETE.IDINSTITUCION = :");
		    contador++;
			htCodigos.put(new Integer(contador), idInstitucion);
			sql.append(contador);

						
			RowsContainer rc = findBind(sql.toString(), htCodigos);
			isAbonoConPago = rc!=null &&rc.size()>0; 
				
		}

		catch (Exception e)
		{
			throw new ClsExceptions (e, "FacAbonoAdm.isAbonoConPago");
		}

		return isAbonoConPago;
	}
	
	/** 
	 * Recoge los diferentes bancos-sufijos que hay para los abonos de SJCS pendientes de pagar por banco 
	 * @param  institucion - identificador de la institucion
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getBancosSufijosSJCS (String institucion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   Hashtable codigos = new Hashtable();
	       try {    	   

	    	   RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT DISTINCT "+FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDSUFIJO+","+ FacSufijoBean.T_NOMBRETABLA+"."+FacSufijoBean.C_SUFIJO +","+
	            			FacBancoInstitucionBean.T_NOMBRETABLA + ".*, "+
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_COD_BANCO+"|| '-' ||"+
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_COD_SUCURSAL+ "|| '-' ||"+ 
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_DIGITOCONTROL+"|| '-' ||"+
	            			FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_NUMEROCUENTA+" AS CUENTACONTABLE,"+
						    "(SELECT CEN.NOMBRE FROM CEN_BANCOS CEN WHERE CEN.CODIGO="+FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_COD_BANCO+") AS BANCO, "+
						    "(SELECT COUNT (1) FROM FAC_SERIEFACTURACION_BANCO SFB WHERE SFB.IDINSTITUCION=" +
						    	FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_IDINSTITUCION + " AND SFB.BANCOS_CODIGO=" + 
						    	FacBancoInstitucionBean.T_NOMBRETABLA + "."+FacBancoInstitucionBean.C_BANCOS_CODIGO + ") AS SELECCIONADO "+
				            " FROM " + FacAbonoBean.T_NOMBRETABLA + "," + CenCuentasBancariasBean.T_NOMBRETABLA + "," + FcsPagosJGBean.T_NOMBRETABLA + "," + FacBancoInstitucionBean.T_NOMBRETABLA + 
				            "," + FacSufijoBean.T_NOMBRETABLA +
							" WHERE " +		FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +		FacAbonoBean.T_NOMBRETABLA +"."+ FacAbonoBean.C_IDPAGOSJG + " IS NOT NULL " +
							" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION +
							" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION + "=" + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDINSTITUCION +
							" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPAGOSJG + "=" + FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDPAGOSJG +
							" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDCUENTA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA +
							" AND " +		FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDPERSONA + "=" + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA +
							" AND " +		FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_BANCOS_CODIGO + "= "+ FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_BANCOS_CODIGO + 
							" AND " +		FcsPagosJGBean.T_NOMBRETABLA + "." + FcsPagosJGBean.C_IDINSTITUCION + "= "+ FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION + 
							" AND " +		"F_SIGA_ESTADOSABONO("+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDINSTITUCION +","+ FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IDABONO + ") = 5 " +
							" AND " + FacAbonoBean.T_NOMBRETABLA + "." + FacAbonoBean.C_IMPPENDIENTEPORABONAR +">0" +
							" AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_IDINSTITUCION +"="+ institucion + 
							" AND " + FacBancoInstitucionBean.T_NOMBRETABLA + "." + FacBancoInstitucionBean.C_FECHABAJA + " IS NULL" +
							" AND " + FacSufijoBean.T_NOMBRETABLA  + "." +FacSufijoBean.C_IDSUFIJO + "=" + FcsPagosJGBean.T_NOMBRETABLA  + "." +FcsPagosJGBean.C_IDSUFIJO +
							" AND " + FacSufijoBean.T_NOMBRETABLA  + "." +FacSufijoBean.C_IDINSTITUCION + "=" + FcsPagosJGBean.T_NOMBRETABLA  + "." +FcsPagosJGBean.C_IDINSTITUCION;
							

	            if (rc.findBind(sql, codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos para sufijos");
	       }
	       return datos;                        
	} //getSufijosAbonosBancosSjcs()

}
