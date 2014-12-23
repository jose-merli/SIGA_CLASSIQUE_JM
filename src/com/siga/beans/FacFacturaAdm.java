/*
 * VERSIONES:
 * 
 * miguel.villegas - 09-03-2005 - Creacion
 * nuria.rgonzalez - 17-03-05 - incorporación: Almacenar(), selectImporteFactura(Integer, String)
 * emilio.grau     - 21-03-05 - incorporacion selectMorosos(ConsultaMorososForm)
 * miguel.villegas - 08-04-05 - incorporacion getFacturasPendientes() getDatosInformeFactura()
 * miguel.villegas - 26-04-05 - implementa almacenar() y las relacionadas con ella
 *	
 */
package com.siga.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.facturacion.form.BusquedaFacturaForm;
import com.siga.facturacion.form.ConsultaMorososForm;
import com.siga.general.SIGAException;


/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacFacturaAdm extends MasterBeanAdministrador {
	public static String IDESTADO_FACTURA_ANULADA="8";
	private String incluirRegistrosConBajaLogica;

	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public FacFacturaAdm(UsrBean usu) {
		super(FacFacturaBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	public String[] getCamposBean() {
		String [] campos = {FacFacturaBean.C_IDFACTURA,
							FacFacturaBean.C_IDINSTITUCION,
							FacFacturaBean.C_FECHAEMISION,
							FacFacturaBean.C_NUMEROFACTURA,
							FacFacturaBean.C_CONTABILIZADA,
							FacFacturaBean.C_OBSERVACIONES,
							FacFacturaBean.C_IDSERIEFACTURACION,
							FacFacturaBean.C_IDPROGRAMACION,
							FacFacturaBean.C_IDFORMAPAGO,
							FacFacturaBean.C_IDPERSONA,
							FacFacturaBean.C_IDCUENTA,
							FacFacturaBean.C_CTACLIENTE,
							FacFacturaBean.C_IDPERSONADEUDOR,
							FacFacturaBean.C_IDCUENTADEUDOR,
							FacFacturaBean.C_OBSERVINFORME,
							FacFacturaBean.C_IMPTOTAL,
							FacFacturaBean.C_IMPTOTALANTICIPADO,
							FacFacturaBean.C_IMPTOTALCOMPENSADO,
							FacFacturaBean.C_IMPTOTALIVA,
							FacFacturaBean.C_IMPTOTALNETO,
							FacFacturaBean.C_IMPTOTALPAGADO,
							FacFacturaBean.C_IMPTOTALPAGADOPORBANCO,
							FacFacturaBean.C_IMPTOTALPAGADOPORCAJA,
							FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA,
							FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA,
							FacFacturaBean.C_IMPTOTALPORPAGAR,
							FacFacturaBean.C_ESTADO,
							FacFacturaBean.C_COMISIONIDFACTURA,
							FacFacturaBean.C_USUMODIFICACION,
							FacFacturaBean.C_FECHAMODIFICACION};
		return campos;
	}
	
	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */
	protected String[] getClavesBean() {
		String [] claves = {FacFacturaBean.C_IDFACTURA,
							FacFacturaBean.C_IDINSTITUCION};
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
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		FacFacturaBean bean = null;
		
		try {
			bean = new FacFacturaBean();
			bean.setIdFactura (UtilidadesHash.getString(hash,FacFacturaBean.C_IDFACTURA));			
			bean.setIdInstitucion (UtilidadesHash.getInteger(hash,FacFacturaBean.C_IDINSTITUCION));
			bean.setFechaEmision (UtilidadesHash.getString(hash,FacFacturaBean.C_FECHAEMISION));
			bean.setNumeroFactura (UtilidadesHash.getString(hash,FacFacturaBean.C_NUMEROFACTURA));
			bean.setContabilizada (UtilidadesHash.getString(hash,FacFacturaBean.C_CONTABILIZADA));
			bean.setObservaciones (UtilidadesHash.getString(hash,FacFacturaBean.C_OBSERVACIONES));
			bean.setObservinforme (UtilidadesHash.getString(hash,FacFacturaBean.C_OBSERVINFORME));
			bean.setIdSerieFacturacion (UtilidadesHash.getLong(hash,FacFacturaBean.C_IDSERIEFACTURACION));
			bean.setIdProgramacion (UtilidadesHash.getLong(hash,FacFacturaBean.C_IDPROGRAMACION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,FacFacturaBean.C_IDFORMAPAGO));
			bean.setIdCuenta (UtilidadesHash.getInteger(hash,FacFacturaBean.C_IDCUENTA));
			bean.setIdPersona (UtilidadesHash.getLong(hash,FacFacturaBean.C_IDPERSONA));
			bean.setCtaCliente(UtilidadesHash.getString(hash,FacFacturaBean.C_CTACLIENTE));
			bean.setIdCuentaDeudor(UtilidadesHash.getInteger(hash,FacFacturaBean.C_IDCUENTADEUDOR));
			bean.setIdPersonaDeudor (UtilidadesHash.getLong(hash,FacFacturaBean.C_IDPERSONADEUDOR));			
			bean.setImpTotal(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTAL));			
			bean.setImpTotalAnticipado(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALANTICIPADO));			
			bean.setImpTotalCompensado(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALCOMPENSADO));			
			bean.setImpTotalIva(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALIVA));			
			bean.setImpTotalNeto(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALNETO));			
			bean.setImpTotalPagado(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADO));			
			bean.setImpTotalPagadoPorBanco(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOPORBANCO));			
			bean.setImpTotalPagadoPorCaja(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOPORCAJA));			
			bean.setImpTotalPorPagar(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPORPAGAR));			
			bean.setImpTotalPagadoSoloCaja(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA));			
			bean.setImpTotalPagadoSoloTarjeta(UtilidadesHash.getDouble(hash,FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA));			
			bean.setEstado(UtilidadesHash.getInteger(hash,FacFacturaBean.C_ESTADO));
			bean.setComisionIdFactura(UtilidadesHash.getString(hash,FacFacturaBean.C_COMISIONIDFACTURA));
			bean.setFechaMod(UtilidadesHash.getString(hash,FacFacturaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacFacturaBean.C_USUMODIFICACION));
			
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
			FacFacturaBean b = (FacFacturaBean) bean;
			UtilidadesHash.set(htData,FacFacturaBean.C_IDFACTURA ,b.getIdFactura());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDINSTITUCION,b.getIdInstitucion ()); 
			UtilidadesHash.set(htData,FacFacturaBean.C_FECHAEMISION,b.getFechaEmision());
			UtilidadesHash.set(htData,FacFacturaBean.C_NUMEROFACTURA, b.getNumeroFactura());
			UtilidadesHash.set(htData,FacFacturaBean.C_CONTABILIZADA,b.getContabilizada());
			UtilidadesHash.set(htData,FacFacturaBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(htData,FacFacturaBean.C_OBSERVINFORME,b.getObservinforme());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDSERIEFACTURACION ,b.getIdSerieFacturacion());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDPROGRAMACION,b.getIdProgramacion ());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDFORMAPAGO,b.getIdFormaPago());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDCUENTA ,b.getIdCuenta());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDPERSONA,b.getIdPersona ());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDCUENTADEUDOR ,b.getIdCuentaDeudor());
			UtilidadesHash.set(htData,FacFacturaBean.C_IDPERSONADEUDOR,b.getIdPersonaDeudor());			
			UtilidadesHash.set(htData,FacFacturaBean.C_CTACLIENTE,b.getCtaCliente());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTAL,b.getImpTotal());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALANTICIPADO,b.getImpTotalAnticipado());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALCOMPENSADO,b.getImpTotalCompensado());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALIVA,b.getImpTotalIva());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALNETO,b.getImpTotalNeto());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPAGADO,b.getImpTotalPagado());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPAGADOPORBANCO,b.getImpTotalPagadoPorBanco());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPAGADOPORCAJA,b.getImpTotalPagadoPorCaja());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPORPAGAR,b.getImpTotalPorPagar());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA,b.getImpTotalPagadoSoloCaja());
			UtilidadesHash.set(htData,FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA,b.getImpTotalPagadoSoloTarjeta());
			UtilidadesHash.set(htData,FacFacturaBean.C_ESTADO,b.getEstado());
			UtilidadesHash.set(htData,FacFacturaBean.C_COMISIONIDFACTURA ,b.getComisionIdFactura());
			UtilidadesHash.set(htData,FacFacturaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,FacFacturaBean.C_USUMODIFICACION, b.getUsuMod());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/** 
	 * Obtiene el valor IDFACTURA, <br/>
	 * @param  institucion - identificador de la institucion 
	 * @return  Long - Identificador de la factura  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Long getNuevoID (String institucion) throws ClsExceptions, SIGAException 
	{

		Long resultado=null;
		
		try { 
            RowsContainer rc = new RowsContainer();

			String sql;
			sql ="SELECT (MAX(TO_NUMBER(IDFACTURA)) + 1) AS IDFACTURA FROM " + nombreTabla +
				" WHERE IDINSTITUCION =" + institucion;
		
			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDFACTURA").equals("")) {
					resultado=new Long(1);
				}
				else 
					resultado=UtilidadesHash.getLong(prueba, "IDFACTURA");;								
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
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  factura - identificador de la factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getFactura (String institucion, String factura) throws ClsExceptions,SIGAException {
		   
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CTACLIENTE + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFORMAPAGO + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" FROM " + FacFacturaBean.T_NOMBRETABLA + 
							" WHERE ";
	            contador++;
	            codigosBind.put(new Integer(contador), factura);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "= :"+contador;
						sql+=" AND ";
				contador++;
				codigosBind.put(new Integer(contador), institucion);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :"+contador;
														
	            if (rc.findBind(sql, codigosBind)) {
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
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de Facturas.");
	       }
	       return datos;                        
	    }

	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  factura - identificador de la factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getImpFactura (String institucion, String factura) throws ClsExceptions,SIGAException {
		   
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADO + 
							" FROM " + FacFacturaBean.T_NOMBRETABLA + 
							" WHERE ";
	            contador++;
	            codigosBind.put(new Integer(contador), factura);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "= :"+contador;
						sql+=" AND ";
				contador++;
				codigosBind.put(new Integer(contador), institucion);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :"+contador;
														
	            if (rc.findBind(sql, codigosBind)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de Facturas.");
	       }
	       return datos;                        
	    }	
	
	
	/** 
	 * Recoge toda informacion del registro seleccionado a partir de sus claves<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  factura - identificador de la factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getFacturaConDescEstado (String institucion, String factura) throws ClsExceptions,SIGAException {
		
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
			    			// RGG 26/05/2009 cambio de funciones de facturas
			    			FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " AS DESCESTADO ," +
							//" F_SIGA_DESCESTADOSFACTURA("+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+") AS DESCESTADO ," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFORMAPAGO + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CTACLIENTE + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" FROM " + FacFacturaBean.T_NOMBRETABLA + "," + FacEstadoFacturaBean.T_NOMBRETABLA +
							" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + "=" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO;
	            contador++;
	            codigosBind.put(new Integer(contador), factura);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "= :"+contador;
						sql+=" AND ";
				contador++;
				codigosBind.put(new Integer(contador), institucion);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :"+contador;
														
	            if (rc.findBind(sql, codigosBind)) {
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
	 * Recoge toda informacion del registro seleccionado a partir de su institucion y su numero<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  factura - numero de la factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getFacturaPorNumero (String institucion, String factura,boolean comprobarFacturaSinAbono) throws ClsExceptions,SIGAException {
		
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + "," +
							// RGG 26/05/2009 cambio de funciones de facturas
			    			FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " AS DESCESTADO ," +
							//" F_SIGA_DESCESTADOSFACTURA("+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+") AS DESCESTADO ," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFORMAPAGO + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CTACLIENTE + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" FROM " + FacFacturaBean.T_NOMBRETABLA + "," + FacEstadoFacturaBean.T_NOMBRETABLA +
							" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + "=" + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO + 
							" AND ";
	            contador++;
	            codigosBind.put(new Integer(contador), factura);
	            		//sql += ComodinBusquedas.prepararSentenciaCompletaUPPERBind(":"+contador,FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA);
						sql+="UPPER("+FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA + ")= :"+contador;
						sql+=" AND " ;
				contador++;
				codigosBind.put(new Integer(contador),institucion);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :"+contador;
				if(comprobarFacturaSinAbono){
					sql+= " AND ";
					sql+=FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA  
							+" NOT IN (select "+FacAbonoBean.T_NOMBRETABLA+"."+FacAbonoBean.C_IDFACTURA+" from "+FacAbonoBean.T_NOMBRETABLA
							+" where "+FacAbonoBean.T_NOMBRETABLA+"."+FacAbonoBean.C_IDINSTITUCION+"="+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION
							+" and "+FacAbonoBean.T_NOMBRETABLA+"."+FacAbonoBean.C_IDFACTURA+" = "+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+")";
					
				}
						
						
	            if (rc.findBind(sql, codigosBind)) {
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

	
	public Double getPkgSigaTotalesFactura(String tipoImporte,String institucion, String factura) throws ClsExceptions,SIGAException {
		Double salida = new Double(0.0);
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            Hashtable codigos = new Hashtable();
	            codigos.put(new Integer(1),institucion);
	            codigos.put(new Integer(2),factura);

	            // RGG 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
	            String sql ="SELECT "+tipoImporte+" AS IMPORTE FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2 ";
	            //String sql ="SELECT PKG_SIGA_TOTALESFACTURA."+tipoImporte+"(:1,:2) AS IMPORTE FROM DUAL";
/*
	            String sql ="SELECT " +
							" PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR("+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+") AS IMPORTE " +
							" FROM " + FacFacturaBean.T_NOMBRETABLA + 
							" WHERE " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "=" + factura;
*/														
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable ht = fila.getRow();
	                  salida = UtilidadesHash.getDouble(ht,"IMPORTE");
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de getPkgSigaTotalesFactura.");
	       }
	       return salida;                        
	    }

	
	
	public Double getImportePendientePago (String institucion, String factura) throws ClsExceptions,SIGAException {
		Double salida = new Double(0.0);
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            Hashtable codigos = new Hashtable();
	            codigos.put(new Integer(1),institucion);
	            codigos.put(new Integer(2),factura);

	            // RGG 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
	            String sql ="SELECT "+FacFacturaBean.C_IMPTOTALPORPAGAR+" AS IMPORTE FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2 ";
	            //String sql ="SELECT PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(:1,:2) AS IMPORTE FROM DUAL";
/*
	            String sql ="SELECT " +
							" PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR("+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+") AS IMPORTE " +
							" FROM " + FacFacturaBean.T_NOMBRETABLA + 
							" WHERE " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDFACTURA + "=" + factura;
*/														
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable ht = fila.getRow();
	                  salida = UtilidadesHash.getDouble(ht,"IMPORTE");
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una entrada de la tabla de abonos.");
	       }
	       return salida;                        
	    }

	/** 
	 * Recoge toda informacion del registro seleccionado a partir de su institucion y su numero<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  factura - numero de la factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getFacturaPorNumeroSimple (String institucion, String factura) throws ClsExceptions,SIGAException {
		
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + "," +
			    			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CTACLIENTE + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFORMAPAGO + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +"," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTADEUDOR + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONADEUDOR +
							
							" FROM " + FacFacturaBean.T_NOMBRETABLA + 
							" WHERE ";
	            contador++;
	            codigosBind.put(new Integer(contador),factura);
	            		sql+=" regexp_like(UPPER(FAC_FACTURA.NUMEROFACTURA),:"+contador+")";
	            		//sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA + "= :"+contador;
						sql+=" AND ";
				contador++;
				codigosBind.put(new Integer(contador),institucion);
						sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :"+contador;
														
	            if (rc.findBind(sql, codigosBind)) {
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
	 * getFacturas
	 * @param datos con los criterios d busqueda para realizar la consulta
	 * @param idInstitucion a la que pertenecen las factuas
	 * @param idioma en el que obtener algunas descripciones
	 * @return RowsContainer con las facturas que se han recuperado de BD
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public PaginadorBind getFacturas (BusquedaFacturaForm datos, Integer idInstitucion, String idioma)  throws ClsExceptions,SIGAException {
		
		Hashtable codigosBind = new Hashtable();
		int contador = 0;
		
		try {
			String selectContar=" SELECT 1 ";
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE 		+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION  + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL  	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPORPAGAR + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO  		+ ", " +
							FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " as DESCRIPCION_ESTADO, " +
							FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " as DESC_ESTADO_ORIGINAL, " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_DESCRIPCION;
							
			String from = " FROM " + FacFacturaBean.T_NOMBRETABLA + ", " + 
								CenPersonaBean.T_NOMBRETABLA+ ", " + 
								FacEstadoFacturaBean.T_NOMBRETABLA + ", " + 
								FacFacturacionProgramadaBean.T_NOMBRETABLA;
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :" +contador; 
					 where+=" AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION +
							" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = " + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO;

			// Numero factura OK
			if ((datos.getBuscarNumeroFactura() != null) && !(datos.getBuscarNumeroFactura().trim().equals(""))) {
				where += " AND ("+ComodinBusquedas.prepararSentenciaCompletaUPPER(datos.getBuscarNumeroFactura().trim(),FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA)+")" ;
			}

			// Fechas OK
			String fDesde = datos.getBuscarFechaDesde(); 
			String fHasta = datos.getBuscarFechaHasta();
			if ((fDesde != null && !fDesde.trim().equals("")) || (fHasta != null && !fHasta.trim().equals(""))) {
				if (!fDesde.equals(""))
					fDesde = GstDate.getApplicationFormatDate("", fDesde); 
				if (!fHasta.equals(""))
					fHasta = GstDate.getApplicationFormatDate("", fHasta);
				where += " AND " + GstDate.dateBetweenDesdeAndHasta(FacFacturaBean.C_FECHAEMISION, fDesde, fHasta);
			}
			
			// Persona OK
			if ((datos.getBuscarIdPersona() != null) && (datos.getBuscarIdPersona().intValue() != -1)){
				where += " AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + datos.getBuscarIdPersona() + " "; 
			}
			
			// Serie Facturacion OK
			if ((datos.getBuscarIdSerieFacturacion() != null) && (datos.getBuscarIdSerieFacturacion().intValue() != 0)) {
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION + " = " + datos.getBuscarIdSerieFacturacion() + " "; 
			}

			// Fecha Real Generacion OK
			String fGeneracion = datos.getBuscarFechaGeneracion(); 
			if ((fGeneracion != null) && !(fGeneracion.trim().equals(""))){
				String fGeneracionD = GstDate.getApplicationFormatDate("", fGeneracion);
				String fGeneracionH = GstDate.getApplicationFormatDate("", fGeneracion);
				where += " AND " + GstDate.dateBetweenDesdeAndHasta(FacFacturacionProgramadaBean.C_FECHAREALGENERACION, fGeneracionD, fGeneracionH); 
			}
			
			// Estado OK
			if ((datos.getBuscarIdEstado() != null) && (datos.getBuscarIdEstado().intValue() != 0)){
				// RGG 26/05/2009 Cambio funciones facturacion 
			    where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = " + datos.getBuscarIdEstado();
			}
			
			// Forma pago OK
			if ((datos.getBuscarFormaPago() != null) && !(datos.getBuscarFormaPago().trim().equals(""))) {
				String aux = "";
				if (datos.getBuscarFormaPago().equals(ClsConstants.TIPO_CARGO_BANCO)) aux = " NOT ";
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + " IS " + aux + " NULL "; 
			}
			
			// Confirmada
			if ((datos.getBuscarConfirmada() != null) && !(datos.getBuscarConfirmada().trim().equals(""))) {
				String aux = "";
				if (datos.getBuscarConfirmada().equals(ClsConstants.DB_TRUE)) aux = " NOT ";
//				where += " AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAPROGRAMACION + " IS " + aux + " NULL "; 
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " IS " + aux + " NULL "; 
			}
			
			// Contabilizada
			if ((datos.getBuscarContabilizada() != null) && !(datos.getBuscarContabilizada().trim().equals(""))) {
				String aux = "N";
				if (datos.getBuscarContabilizada().equals(ClsConstants.DB_TRUE)) aux = "S";
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_CONTABILIZADA + " = '" + aux + "'"; 
			}
			
//			Deudor OK
			if ((datos.getDeudor() != null) &&(!datos.getDeudor().equals(""))){
				
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONADEUDOR + " = " + datos.getDeudor() + " "; 
			}
			
			//String orderBy = " ORDER BY TO_NUMBER(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") DESC";
			String orderBy = " ORDER BY " // + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", "
					  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE 		+ ", "
					  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 	+ ", "
					  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 	+ ", "
					  + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " DESC, " 
					  + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " DESC ";			
	
			String consulta = select + from + where + orderBy;
			selectContar+= from + where;
			PaginadorBind paginador = new PaginadorBind(consulta,selectContar,codigosBind);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			return paginador;
	
			/*RowsContainer rc = new RowsContainer(); 
			if (rc.query(consulta)) {
				
				return rc;
			}*/
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
		
	}

	/**
	 * Obtiene las facturas de un cliente
	 * @param idInstitucion
	 * @param idPersona
	 * @param dias: Indica los ultimos 'n' dias de los que se obtendran las facturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getFacturasClientePeriodo (Integer idInstitucion, Long idPersona, Integer dias)  throws ClsExceptions,SIGAException {
		
		Vector resultados = new Vector (); 
		Hashtable codigosBind = new Hashtable();
		int contador=0;
		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION 	+ ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALNETO 	+ " as TOTALNETO, " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALIVA 	+ " AS TOTALIVA, " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL 	+ " AS TOTAL, " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADO 	+ " AS TOTALPAGADO, " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_DESCRIPCION + " ";
 
			String from = 	" FROM " + 
							FacFacturaBean.T_NOMBRETABLA + ", " + FacSerieFacturacionBean.T_NOMBRETABLA + ", " + FacFacturacionProgramadaBean.T_NOMBRETABLA;
			
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :"+contador;
			contador++;
			codigosBind.put(new Integer(contador),idPersona.toString());
					 where+=" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + " = :"+contador;
			contador++;
			codigosBind.put(new Integer(contador),dias.toString());
					 where+=" AND SYSDATE - " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " < :"+contador;
			     	 where+=" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = "+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION;
	
			String orderBy = " ORDER BY " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " DESC ";
	
			String consulta = select + from + where + orderBy;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta, codigosBind)) {
				for (int i = 0; i < rc.size(); i++)	{
				//nuevo iag
					
				  Row fila = (Row) rc.get(i);
                  Hashtable hfila = fila.getRow();

                  // RGG 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES 
//                  String idFactura = hfila.get("IDFACTURA").toString();
//                  ConsPLFacturacion pl=new ConsPLFacturacion();
//                  
//                  Hashtable codigos2 = new Hashtable();
//                  codigos2.put(new Integer(1), idInstitucion.toString());
//                  codigos2.put(new Integer(2), idFactura);
//                  
//                  hfila.put("TOTALNETO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALNETO"));
//                  hfila.put("TOTALIVA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALIVA"));
//                  hfila.put("TOTAL", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTAL"));
//                  hfila.put("TOTALPAGADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADO"));
//                  
                  fila.setRow(hfila);
				  resultados.add(fila);	
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
	   				throw new ClsExceptions(e, "Error al obtener los datos de las facturas del cliente");
	   			}
	   		}	
	    }
		return null;
	}

	
	public PaginadorBind getFacturasClientePeriodoPaginador (Integer idInstitucion, Long idPersona, Integer dias) throws ClsExceptions, SIGAException {
		PaginadorBind paginador=null;
		try {
			Hashtable codigos = new Hashtable();
			String select = getConsultaFacturasClientePeriodo(idInstitucion, idPersona, dias, codigos);
			paginador = new PaginadorBind(select,codigos);
	

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar consulta getServiciosSolicitadosPaginador.");
		}
		return paginador;  
		
		
		
	}
	
	
	/**
	 * Obtiene las facturas de un cliente
	 * @param idInstitucion
	 * @param idPersona
	 * @param dias: Indica los ultimos 'n' dias de los que se obtendran las facturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getConsultaFacturasClientePeriodo (Integer idInstitucion, Long idPersona, Integer dias, Hashtable codigosBind)  throws ClsExceptions,SIGAException {
		UsrBean user = this.usrbean;
		
		//Hashtable codigosBind = new Hashtable();
		int contador=0;
		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION 	+ ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALNETO + ", " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALIVA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPORPAGAR + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADO + ", " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_DESCRIPCION + ", " + 
							" ( " +
								" SELECT " + GenRecursosBean.C_DESCRIPCION +							
								" FROM " + GenRecursosBean.T_NOMBRETABLA +
								" WHERE " + GenRecursosBean.C_IDRECURSO + " = " + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION +
									" AND " + GenRecursosBean.C_IDLENGUAJE + " = " + user.getLanguage() +
							" ) AS DESCRIPCION_ESTADO ";
			
			String from = 	" FROM " + FacFacturaBean.T_NOMBRETABLA + ", " + 
								FacEstadoFacturaBean.T_NOMBRETABLA + ", " +
								FacSerieFacturacionBean.T_NOMBRETABLA + ", " + 								
								FacFacturacionProgramadaBean.T_NOMBRETABLA;
			
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :" + contador;
			
			contador++;
			codigosBind.put(new Integer(contador),idPersona.toString());
			where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + " = :" + contador;
			
			if (dias!=null){
				// jbd // Cambio la forma de calcular los ultimos 2 año, restando X dias a la fecha actual en vez de hacerlo en la consulta 
				Calendar fechaReferencia = Calendar.getInstance();
				fechaReferencia.add(Calendar.DAY_OF_MONTH, (-1*dias));
				contador++;
				codigosBind.put(new Integer(contador),UtilidadesString.formatoFecha( fechaReferencia.getTime(), ClsConstants.DATE_FORMAT_SHORT_ENGLISH));
				where += " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " > :" + contador;
			}				 
			    
			where += " AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
					" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
					" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
					" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = "+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
					" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION +
					" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = " + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO;;
	
			//String orderBy = " ORDER BY TO_NUMBER(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") DESC";
			String orderBy = " ORDER BY " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " DESC, " 
					  + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " DESC ";					
	
			String consulta = select + from + where + orderBy;
	
			return consulta;
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
	   				throw new ClsExceptions(e, "Error al obtener los datos de las facturas del cliente");
	   			}
	   		}	
	    }
	}

	
	
	/**
	 * Obtiene la descripcion detallada de una factura
	 * @param idInstitucion de la factura
	 * @param idFActura de la factura
	 * @param idioma en el que mostrar los literales de texto
	 * @return datos asociados a la factura
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	 public Hashtable getFacturaDatosGenerales (Integer idInstitucion, String idFActura, String idioma)  throws ClsExceptions,SIGAException {
		
 
		Hashtable codigosBind = new Hashtable();
		Hashtable resultado = new Hashtable();
		int contador=0;
		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + ", " +
							CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE 		+ ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_DESCRIPCION + ", " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + ", " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + "," +
							FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " as DESCRIPCION_ESTADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS TOTAL_FACTURA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " AS TOTAL_ANTICIPADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALCOMPENSADO + " AS TOTAL_COMPENSADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA + " AS TOTAL_PAGADOSOLOCAJA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOPORCAJA + " AS TOTAL_PAGADOPORCAJA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA + " AS TOTAL_PAGADOSOLOTARJETA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOPORBANCO + " AS TOTAL_PAGADOPORBANCO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADO + " AS TOTAL_PAGADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPORPAGAR + " AS TOTAL_PENDIENTEPAGAR," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALNETO + " AS TOTAL_NETO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALIVA + " AS TOTAL_IVA," +
							
							//"PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_FACTURA, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_ANTICIPADO, " + 
							//"PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_COMPENSADO, " + 
							//"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORCAJA, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOCAJA, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOTARJETA, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORBANCO, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADO, " +
							//"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PENDIENTEPAGAR, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTALNETO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_NETO, " +
							//"F_SIGA_DESCESTADOSFACTURA("+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION+","+FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA+") AS DESCRIPCION_ESTADO ," +
							//"PKG_SIGA_TOTALESFACTURA.TOTALIVA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_IVA, "+
							" (select "+CenPersonaBean.C_NOMBRE+"||' '||"+CenPersonaBean.C_APELLIDOS1+"||' '||"+CenPersonaBean.C_APELLIDOS2+
                            "  from "+CenPersonaBean.T_NOMBRETABLA+
                            "  where "+CenPersonaBean.C_IDPERSONA+"="+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDPERSONADEUDOR+") PERSONADEUDOR, "+
							" (select "+CenPersonaBean.C_NIFCIF +
                            "  from "+CenPersonaBean.T_NOMBRETABLA+
                            "  where "+CenPersonaBean.C_IDPERSONA+"="+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDPERSONADEUDOR+") NIFCIFDEUDOR, "+
                            FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDCUENTADEUDOR+", "+
                            " (select "+CenCuentasBancariasBean.C_NUMEROCUENTA+
							" from "+CenCuentasBancariasBean.T_NOMBRETABLA;
			contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());
                    select+=" where "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION+"=:"+contador;
                    select+="   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA+"="+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDPERSONADEUDOR+
                            "   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDCUENTA+"="+ FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDCUENTADEUDOR+") NUMEROCUENTADEUDOR, "+
                           
                            
                            " (select "+CenCuentasBancariasBean.C_CBO_CODIGO+
							" from "+CenCuentasBancariasBean.T_NOMBRETABLA;
            contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());
                    select+=" where "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION+"=:"+contador;
                    select+="   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA+"="+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDPERSONADEUDOR+
                            "   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDCUENTA+"="+ FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDCUENTADEUDOR+") CODIGOENTIDADDEUDOR, "+
                            
                     " (select "+CenCuentasBancariasBean.C_IBAN+
					 " from "+CenCuentasBancariasBean.T_NOMBRETABLA;                    
            contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());
                    select+=" where "+CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDINSTITUCION+"=:"+contador;
                    select+="   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDPERSONA+"="+FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDPERSONADEUDOR+
                            "   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDCUENTA+"="+ FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDCUENTADEUDOR+") IBANDEUDOR";                    
                    
			String from = 	" FROM " + 
							FacSerieFacturacionBean.T_NOMBRETABLA + ", " + 
							CenPersonaBean.T_NOMBRETABLA + ", " + 
							CenColegiadoBean.T_NOMBRETABLA + ", " + 
							FacFacturacionProgramadaBean.T_NOMBRETABLA + ", " +
							FacFacturaBean.T_NOMBRETABLA + ", " +
							FacEstadoFacturaBean.T_NOMBRETABLA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA;
							/*FacFacturaBean.T_NOMBRETABLA + " LEFT JOIN " + CenCuentasBancariasBean.T_NOMBRETABLA + 
							" ON " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA;*/
			contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :"+contador +
							" AND  " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO + " = " +  FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO; 
			contador++;
			codigosBind.put(new Integer(contador), idFActura);
					where+= " AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = :"+contador ;
					where+= " AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + 
							" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION;
			contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());					
					 where+=" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = :"+contador;
					 where+=" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA;
	
			String consulta = select + from + where;
	
			RowsContainer rc = new RowsContainer();
			FacAbonoAdm admAbono = null;
			if (rc.queryBind(consulta,codigosBind)) {
				if (rc.size() > 1) return null;
				resultado = ((Row) rc.get(0)).getRow();
				/*
				ConsPLFacturacion d = new ConsPLFacturacion(this.usuModificacion, idioma);
				Long idFac = new Long(UtilidadesHash.getString(aux, FacFacturaBean.C_IDFACTURA));
				UtilidadesHash.set(aux, "DESCRIPCION_ESTADO", d.obtenerEstadoFacAbo(idInstitucion.intValue(), idFac.longValue(), "0"));
				*/
			
				//Nuevo iag
                 String idFacturaFila = resultado.get("IDFACTURA").toString();

                 // rgg 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
//               ConsPLFacturacion pl=new ConsPLFacturacion();
//               
//               Hashtable codigos2 = new Hashtable();
//               codigos2.put(new Integer(1), idInstitucion.toString());
//               codigos2.put(new Integer(2), idFacturaFila);
//              	                  
//               resultado.put("TOTAL_FACTURA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTAL"));
//               resultado.put("TOTAL_ANTICIPADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO"));
//               resultado.put("TOTAL_COMPENSADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO"));
//               resultado.put("TOTAL_PAGADOPORCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA"));
//               resultado.put("TOTAL_PAGADOSOLOCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA"));
//               resultado.put("TOTAL_PAGADOSOLOTARJETA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA"));
//               resultado.put("TOTAL_PAGADOPORBANCO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO"));
//               resultado.put("TOTAL_PAGADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADO"));
//               resultado.put("TOTAL_PENDIENTEPAGAR", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR"));
//               resultado.put("TOTAL_NETO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALNETO"));
//               resultado.put("TOTAL_IVA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALIVA"));
               
               //String estado = pl.getFuncionEjecutada(codigos2, "F_SIGA_DESCESTADOSFACTURA");
               String idEstado = resultado.get("ESTADO").toString();
               String estado = resultado.get("DESCRIPCION_ESTADO").toString();
   			
   			
   			
   			//Miramos la descripcion del estado de la factura
//   			String estado = admFac.getDescEstadoFactura(idInstitucion,idFactura);
   			String sEstado =  UtilidadesString.getMensajeIdioma(idioma, estado);
   			if(idEstado.equals("8")){ // anulada
   				admAbono = new FacAbonoAdm(usrbean);
   				Hashtable htAbono = new Hashtable(); 
   				htAbono.put(FacAbonoBean.C_IDFACTURA,idFacturaFila);
   				htAbono.put(FacAbonoBean.C_IDINSTITUCION, idInstitucion);
   				Vector vAbonos = admAbono.select(htAbono);
   				
   				// JPT: Las facturas devueltas por comision, generan facturas anuladas sin abono, porque se crea una factura nueva
				if (vAbonos!=null && vAbonos.size()==1) {
					//Es unique key por lo que habra solo un registro
					FacAbonoBean beanAbono = (FacAbonoBean)vAbonos.get(0);
					resultado.put(FacAbonoBean.C_NUMEROABONO, beanAbono.getNumeroAbono()); 
					sEstado += " ("+beanAbono.getNumeroAbono()+")";
				}
   			}
   			
   			resultado.put("DESCRIPCION_ESTADO", sEstado);
               
			return resultado;	
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

	/**
	 * Obtiene la descripcion detallada de una factura
	 * @param idInstitucion de la factura
	 * @param idFActura de la factura
	 * @param idioma en el que mostrar los literales de texto
	 * @return datos asociados a la factura
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getFacturaDatosGeneralesTotalFactura (Integer idInstitucion, String idFActura, String idioma)  throws ClsExceptions,SIGAException {

		Hashtable codigosBind = new Hashtable();
		Hashtable resultado = new Hashtable();
		int contador=0;
		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + ", " +
							CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 	+ ", " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE 		+ ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_NOMBREABREVIADO + ", " +
							FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_DESCRIPCION + ", " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + ", " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHACONFIRMACION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL + " AS TOTAL_FACTURA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALANTICIPADO + " AS TOTAL_ANTICIPADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALCOMPENSADO + " AS TOTAL_COMPENSADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA + " AS TOTAL_PAGADOSOLOCAJA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOPORCAJA + " AS TOTAL_PAGADOPORCAJA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA + " AS TOTAL_PAGADOSOLOTARJETA," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADOPORBANCO + " AS TOTAL_PAGADOPORBANCO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPAGADO + " AS TOTAL_PAGADO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPORPAGAR + " AS TOTAL_PENDIENTEPAGAR," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALNETO + " AS TOTAL_NETO," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALIVA + " AS TOTAL_IVA," +
														
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES ;
/*
							"PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_ANTICIPADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_COMPENSADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOTARJETA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORBANCO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADO, " +*/
							//"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PENDIENTEPAGAR, " +
							/*"PKG_SIGA_TOTALESFACTURA.TOTALNETO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_NETO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALIVA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_IVA, "+
*/							
							//"PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_FACTURA ";

			String from = 	" FROM " + 
							FacSerieFacturacionBean.T_NOMBRETABLA + ", " + 
							CenPersonaBean.T_NOMBRETABLA + ", " + 
							CenColegiadoBean.T_NOMBRETABLA + ", " + 
							FacFacturacionProgramadaBean.T_NOMBRETABLA + ", " +
							FacFacturaBean.T_NOMBRETABLA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA;
							/*FacFacturaBean.T_NOMBRETABLA + " LEFT JOIN " + CenCuentasBancariasBean.T_NOMBRETABLA + 
							" ON " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA;*/
			
			contador++;
			codigosBind.put(new Integer(contador), idInstitucion.toString());	
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :"+contador ;
			contador++;
			codigosBind.put(new Integer(contador), idFActura);
					 where+=" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = :"+contador;
					 where+=" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION + 
							" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION ;
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
					 where+=" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = :"+contador;
					 where+=" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + "(+) = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA;
	
			String consulta = select + from + where;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(consulta,codigosBind)) {
				if (rc.size() > 1) return null;
				resultado = ((Row) rc.get(0)).getRow();
				/*
				ConsPLFacturacion d = new ConsPLFacturacion(this.usuModificacion, idioma);
				Long idFac = new Long(UtilidadesHash.getString(aux, FacFacturaBean.C_IDFACTURA));
				UtilidadesHash.set(aux, "DESCRIPCION_ESTADO", d.obtenerEstadoFacAbo(idInstitucion.intValue(), idFac.longValue(), "0"));
				*/
				
				//modificacion nueva iag
				
			   String idFacturaFila = resultado.get("IDFACTURA").toString();
               // rgg 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
//               ConsPLFacturacion pl=new ConsPLFacturacion();
//               
//               Hashtable codigos2 = new Hashtable();
//               codigos2.put(new Integer(1), idInstitucion.toString());
//               codigos2.put(new Integer(2), idFacturaFila);	
//				
//               resultado.put("TOTAL_ANTICIPADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO"));
//               resultado.put("TOTAL_COMPENSADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO"));
//               resultado.put("TOTAL_PAGADOPORCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA"));
//               resultado.put("TOTAL_PAGADOSOLOCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA"));
//               resultado.put("TOTAL_PAGADOSOLOTARJETA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA"));
//               resultado.put("TOTAL_PAGADOPORBANCO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO"));
//               resultado.put("TOTAL_PAGADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADO"));
//               resultado.put("TOTAL_PENDIENTEPAGAR", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR"));
//               resultado.put("TOTAL_NETO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALNETO"));
//               resultado.put("TOTAL_IVA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALIVA"));
//               resultado.put("TOTAL_FACTURA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTAL"));
               
			   return resultado;
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

	

	
	
	
	public boolean firmarPDF(File fIn,String idInstitucion)
	{
		try {
		    ClsLogging.writeFileLog("VOY A FIRMAR EL PDF: "+fIn.getAbsolutePath(),10);
		    String nombreFinal=fIn.getAbsolutePath();
			GregorianCalendar gcFecha = new GregorianCalendar();
			GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
			
            String sPathCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS_DIGITALES", "");
            String sNombreCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "NOMBRE_CERTIFICADOS_DIGITALES", "");
            String sClave = admParametros.getValor(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES", "");
            boolean tieneParametro = admParametros.tieneParametro(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES");
            String sIDDigital =""; 
            if (tieneParametro) {
                sIDDigital = sPathCertificadosDigitales + File.separator + idInstitucion + File.separator + sNombreCertificadosDigitales;
            } else {
                sIDDigital = sPathCertificadosDigitales + File.separator + sNombreCertificadosDigitales;
            }
	
			ClsLogging.writeFileLog(" - Path certificado digital: "+sPathCertificadosDigitales,10);
            ClsLogging.writeFileLog(" - Nombre certificado digital: "+sNombreCertificadosDigitales,10);
            ClsLogging.writeFileLog(" - Clave certificado digital: "+sClave,10);
            ClsLogging.writeFileLog(" - sIDDigital: "+sIDDigital,10);
	
			String sNombreFicheroSalida = fIn.getAbsolutePath()+".tmp";        //sNombreFicheroEntrada + ".tmp";
		    ClsLogging.writeFileLog(" Fichero temporal: "+sNombreFicheroSalida,10);
	
	        File fOut = new File(sNombreFicheroSalida);
	        
	        FileInputStream fisID = new FileInputStream(sIDDigital);
	        

	        KeyStore ks = KeyStore.getInstance("PKCS12");
	        ks.load(fisID, sClave.toCharArray());
	        
	        fisID.close();
	        
	        String sAlias = (String)ks.aliases().nextElement();
	
	        PrivateKey pKey = (PrivateKey)ks.getKey(sAlias, sClave.toCharArray());
	        
	        Certificate[] aCertificados = ks.getCertificateChain(sAlias);
	        
	        PdfReader reader = new PdfReader(fIn.getAbsolutePath());
	        
	        FileOutputStream fos = new FileOutputStream(sNombreFicheroSalida);	        
	        
	        
	        
	        PdfStamper stamper = PdfStamper.createSignature(reader, fos, '\0');
	        PdfSignatureAppearance psa = stamper.getSignatureAppearance();
	        
	        psa.setCrypto(pKey, aCertificados, null, PdfSignatureAppearance.WINCER_SIGNED);
	        
	        psa.setSignDate(gcFecha);
	
	        stamper.close();
	        fos.close();
	                
	        fIn.delete();
	        fOut.renameTo(new File(nombreFinal));
			return true;
		} catch (Exception e) {
			ClsLogging.writeFileLog("***************** ERROR DE FIRMA DIGITAL EN DOCUMENTO *************************", 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF de la institucion: " + idInstitucion, 3);
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
	        e.printStackTrace();
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
 			return false;
		}
	}
	
	/** Funcion selectImporteFactura, obtiene el importe pendiente por pagar de la factura.
	 * @param idInstitucion - identificador de la institución
	 * @param idFactura - identificador de la factura.
	 * @return Float: Cantidad pendiente por pagar de la factura
	 * */
	public Float selectImporteFactura(Integer idInstitucion, String idFactura) throws ClsExceptions,SIGAException {
		
		Float importe = null;
		// Acceso a BBDD
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idFactura);

//		  RGG 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
            String select ="SELECT "+FacFacturaBean.C_IMPTOTAL+" AS IMPORTE FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2 ";
            //String select =	"SELECT PKG_SIGA_TOTALESFACTURA.TOTAL(:1,:2) as IMPORTE FROM DUAL";

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				importe = UtilidadesHash.getFloat(aux, "IMPORTE");				
			}
		}
	   	catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return importe;
	}
	
	/** Funcion selectMorosos (UsrBean user, ConsultaMorososForm form)
	 * Búsqueda sobre la tabla de facturas para recuperar los morosos
	 * @param form
	 * @param user
	 * @return vector con los registros encontrados.  
	 * @exception ClsExceptions 
	 * */
	public PaginadorCaseSensitiveBind  selectMorosos (ConsultaMorososForm form) throws ClsExceptions {
		
		// Acceso a BBDD
		boolean isFacturasPendientes = (form.getFacturasImpagadasDesde()!=null && !form.getFacturasImpagadasDesde().equals(""))
										||(form.getFacturasImpagadasHasta()!=null && !form.getFacturasImpagadasHasta().equals("")); 
		try { 
			RowsContainer rc = new RowsContainer(); 				        
	        
			int contador=0;
			Hashtable codigos = new Hashtable();
			String nComunicacionesDesde = form.getNumeroComunicacionesDesde();
			 String nComunicacionesHasta = form.getNumeroComunicacionesHasta();
			 String sql="";
			 
			 if ((nComunicacionesDesde!=null && !nComunicacionesDesde.equalsIgnoreCase("")) || 
				 (nComunicacionesHasta!=null && !nComunicacionesHasta.equalsIgnoreCase("")) || 
				 (isFacturasPendientes) ||
				 (form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals("")) ||
				 (form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals(""))) {
		         sql = "SELECT " + FacFacturaBean.C_IDINSTITUCION + ", " + 
			        		 FacFacturaBean.C_IDPERSONA + ", " +
			        		 FacFacturaBean.C_IDFACTURA + ", " +
			        		 FacFacturaBean.C_NUMEROFACTURA + ", " +
			        		 " TRUNC(" + FacFacturaBean.C_FECHAEMISION + ") AS " + FacFacturaBean.C_FECHAEMISION + ", " +
			        		 " DEUDA, " +			        		 
			        		 " NCOLEGIADO AS " + CenColegiadoBean.C_NCOLEGIADO + ", " +			        		 
			        		 " NOMBRE, " +
			        		 " ORDEN_APELLIDOS, " +
			        		 " ORDEN_NOMBRE, " +
			        		 " COMUNICACIONES, " +			        		 
			        		 " ESTADO_FACTURA " +
			        		 (isFacturasPendientes ? ", FACTURASPENDIENTES " : "") +
		        		 " FROM ( ";
			 }
			 
		    sql += " SELECT F." + FacFacturaBean.C_IDINSTITUCION + ", " +
		    			" F." + FacFacturaBean.C_IDPERSONA + ", " +
		    			" F." + FacFacturaBean.C_IDFACTURA + ", " +
		    			" F." + FacFacturaBean.C_NUMEROFACTURA + ", " +		    			
		    			" F." + FacFacturaBean.C_FECHAEMISION + ", " +		    			
		    			" F." + FacFacturaBean.C_IMPTOTALPORPAGAR + " AS DEUDA, " +
		    			" F_SIGA_CALCULONCOLEGIADO(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDPERSONA + ") AS " + CenColegiadoBean.C_NCOLEGIADO + ", " +
		    			" P." + CenPersonaBean.C_NOMBRE + " || ' ' || P." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || P." + CenPersonaBean.C_APELLIDOS2 + " AS NOMBRE, " +
		    			" P." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || P." + CenPersonaBean.C_APELLIDOS2 + " AS ORDEN_APELLIDOS, " +
		    			" P." + CenPersonaBean.C_NOMBRE + " AS ORDEN_NOMBRE, " +
		    			" F_SIGA_GETCOMFACTURA(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDPERSONA + ", F." + FacFacturaBean.C_IDFACTURA + ", 0) AS COMUNICACIONES, " +
		    			" F_SIGA_GETRECURSO_ETIQUETA(ef." + FacEstadoFacturaBean.C_DESCRIPCION + ", " + this.usrbean.getLanguage() + ") AS ESTADO_FACTURA ";
		    
		    if ((nComunicacionesDesde!=null && !nComunicacionesDesde.equalsIgnoreCase("")) || 
		    		(nComunicacionesHasta!=null && !nComunicacionesHasta.equalsIgnoreCase(""))) {
		    	sql += ", ( " +
			    			" SELECT COUNT(" + EnvComunicacionMorososBean.C_IDINSTITUCION + ") " +
			    			" FROM " + EnvComunicacionMorososBean.T_NOMBRETABLA + " ECM " + 
			    			" WHERE ECM." + EnvComunicacionMorososBean.C_IDINSTITUCION + " = F." + FacFacturaBean.C_IDINSTITUCION +
			    				" AND ECM." + EnvComunicacionMorososBean.C_IDPERSONA + " = F." + FacFacturaBean.C_IDPERSONA +
			    				" AND ECM." + EnvComunicacionMorososBean.C_IDFACTURA + " = F." + FacFacturaBean.C_IDFACTURA +
		    			") AS NCOMUNICACIONES ";		    
		    }
		    
		    if(isFacturasPendientes){
		    	sql += ", ( " +
		    				" SELECT count(F2." + FacFacturaBean.C_IDINSTITUCION + ") " +
		    				" FROM " + FacFacturaBean.T_NOMBRETABLA + " F2 " +
		    				" WHERE F2." + FacFacturaBean.C_IDINSTITUCION + " = F." + FacFacturaBean.C_IDINSTITUCION +
		    					" AND F2." + FacFacturaBean.C_IDPERSONA + " = F." + FacFacturaBean.C_IDPERSONA +
								" AND F2." + FacFacturaBean.C_NUMEROFACTURA + " IS NOT NULL " +
			    				" AND F2." + FacFacturaBean.C_IMPTOTALPORPAGAR + " > 0 " +
	    				") AS FACTURASPENDIENTES ";
		    }
		    
		    contador++;
		    codigos.put(new Integer(contador),this.usrbean.getLocation());
		    sql += " FROM " + FacFacturaBean.T_NOMBRETABLA + " F, " +
		    			CenClienteBean.T_NOMBRETABLA + " C, " +
		    			CenPersonaBean.T_NOMBRETABLA + " P, " + 
		    			CenPersonaBean.T_NOMBRETABLA + " DEUDOR, " +		    			
		    			CenInstitucionBean.T_NOMBRETABLA + " I, " +
		    			FacEstadoFacturaBean.T_NOMBRETABLA + " EF " +
		    		" WHERE F." + FacFacturaBean.C_IDINSTITUCION + " = :" + contador + 	    				
	    				" AND F." + FacFacturaBean.C_ESTADO + " <> " + ClsConstants.ESTADO_FACTURA_ANULADA + // JPT: No se muestran las facturas anuladas
	    				" AND F." + FacFacturaBean.C_IMPTOTALPORPAGAR + " > 0 " +
	    				" AND C." + CenClienteBean.C_IDINSTITUCION + " = F." + FacFacturaBean.C_IDINSTITUCION +
		    			" AND C." + CenClienteBean.C_IDPERSONA + "= F." + CenPersonaBean.C_IDPERSONA +
		    			" AND P." + CenPersonaBean.C_IDPERSONA + " = F." + FacFacturaBean.C_IDPERSONA +
		    			" AND DEUDOR." + CenPersonaBean.C_IDPERSONA + "(+) = F." + FacFacturaBean.C_IDPERSONADEUDOR + 
		    			" AND I." + CenInstitucionBean.C_IDPERSONA  + "(+) = F." + FacFacturaBean.C_IDPERSONADEUDOR +
		    			" AND EF." + FacEstadoFacturaBean.C_IDESTADO + " = F." + FacFacturaBean.C_ESTADO;
		    
		    //Comento por ordenes de LP
		    //sql += " AND F." + FacFacturaBean.C_ESTADO + " <> " + FacFacturaAdm.IDESTADO_FACTURA_ANULADA;
		    
		   // FILTRO CLIENTE TAG BUSQUEDA
		    String letrado = form.getLetrado();
		    if(letrado!=null && !letrado.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),letrado);
			    sql += " AND F." + FacFacturaBean.C_IDPERSONA + " = :" + contador;
		    }
		    
		    // FILTRO CLIENTE NOMBRE Y APELLIDOS
		    String nombre = form.getInteresadoNombre();
		    if(nombre!=null && !nombre.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador), "%" + nombre.toLowerCase() + "%");
			    sql += " AND LOWER(P." + CenPersonaBean.C_NOMBRE + ") LIKE :" + contador;
		    }
		    
		    String apellidos = form.getInteresadoApellidos();
		    if(apellidos!=null && !apellidos.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador), "%" + apellidos.toLowerCase() + "%");
		    	sql += " AND LOWER(P." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || P." + CenPersonaBean.C_APELLIDOS2 + ") LIKE :" + contador;
		    }
		    
		    // FILTRO CLIENTE ESTADOCOLEGIAL
		    String estadoColegial = form.getCmbEstadoColegial();
		    if (estadoColegial!=null && !estadoColegial.equalsIgnoreCase("")) {
		    	//Se hace esto(grrr) para coger los dos estados colegiales
		        contador++;
			    codigos.put(new Integer(contador), estadoColegial);
		    	sql += " AND :" + contador +" LIKE '%' || F_SIGA_GETTIPOCLIENTE(C." + CenClienteBean.C_IDPERSONA + ", C." + CenClienteBean.C_IDINSTITUCION + ", SYSDATE) || '%' " +
		    			" AND NOT EXISTS ( " +
		    				" SELECT 1 " +
		    				" FROM " + CenNoColegiadoBean.T_NOMBRETABLA + " NC " +
		    				" WHERE NC." + CenNoColegiadoBean.C_IDINSTITUCION + " = C." + CenClienteBean.C_IDINSTITUCION + 
		    					" AND NC." + CenNoColegiadoBean.C_IDPERSONA + " = C." + CenClienteBean.C_IDPERSONA + 
		    			" ) "; 
		    }
		    
		    // FILTRO DEUDOR
		    String denominacion = form.getDenominacionDeudor();
		    if (denominacion!=null && !denominacion.equalsIgnoreCase("")) {
		        contador++;
			    codigos.put(new Integer(contador), denominacion);
			    sql += " AND I." + CenInstitucionBean.C_IDINSTITUCION + " = :" + contador;
		    }
		    
		    if (form.getCmbEstadosFactura()!=null && !form.getCmbEstadosFactura().equalsIgnoreCase("")) {
		    	//Se hace esto(grrr) para coger los dos estados colegiales
		        contador++;
			    codigos.put(new Integer(contador), form.getCmbEstadosFactura());
		    	sql += " AND EF." + FacEstadoFacturaBean.C_IDESTADO + " = :" + contador;
		    }
		    
		    String fDesde = form.getFechaDesde(); 
			String fHasta = form.getFechaHasta();
		    if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				if (!fDesde.equals(""))
					fDesde = GstDate.getApplicationFormatDate("", fDesde); 
				if (!fHasta.equals(""))
					fHasta = GstDate.getApplicationFormatDate("", fHasta);
					
			    Vector v = GstDate.dateBetweenDesdeAndHastaBind("F." + FacFacturaBean.C_FECHAEMISION, fDesde, fHasta, contador, codigos);
			    Integer in = (Integer)v.get(0);
			    String st = (String)v.get(1);
			    contador = in.intValue();

				sql += " AND " + st;
			}
		    
		    String numeroFactura = form.getNumeroFactura();
		    if (numeroFactura!=null && !numeroFactura.equalsIgnoreCase("")) {
		        contador++;
			    codigos.put(new Integer(contador), numeroFactura);
		    	sql += " AND F." + FacFacturaBean.C_NUMEROFACTURA + " = :" + contador;
		    } else {
		    	sql += " AND F." + FacFacturaBean.C_NUMEROFACTURA + " IS NOT NULL ";
		    }
	    	
	    	if ((nComunicacionesDesde!=null && !nComunicacionesDesde.equalsIgnoreCase("")) || 
	    		(nComunicacionesHasta!=null && !nComunicacionesHasta.equalsIgnoreCase("")) || 
				(isFacturasPendientes) ||
				(form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals("")) ||
				(form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals(""))) {
	    		sql += " ) WHERE 1 = 1 ";
	    		
		    	// fin modificacion			   	   
			    if (nComunicacionesDesde!=null && !nComunicacionesDesde.equalsIgnoreCase("")) {
			        contador++;
				    codigos.put(new Integer(contador), nComunicacionesDesde);
			    	sql += " AND NCOMUNICACIONES >= :"+contador;
			    }

			    if (nComunicacionesHasta!=null && !nComunicacionesHasta.equalsIgnoreCase("")) {
			    	contador++;
				    codigos.put(new Integer(contador), nComunicacionesHasta);
			    	sql += " AND NCOMUNICACIONES <= :" + contador;
			    }

			    if(isFacturasPendientes){		    	
			    	if(form.getFacturasImpagadasDesde()!=null && !form.getFacturasImpagadasDesde().equals("")){
			    		contador++;
					    codigos.put(new Integer(contador), form.getFacturasImpagadasDesde());
				    	sql += " AND FACTURASPENDIENTES >= :" + contador;
			    	}
			    	
			    	if(form.getFacturasImpagadasHasta()!=null && !form.getFacturasImpagadasHasta().equals("")){
			    		contador++;
					    codigos.put(new Integer(contador), form.getFacturasImpagadasHasta());
				    	sql += " AND FACTURASPENDIENTES <= :" + contador;
			    	}
			    }
			    
			    if ((form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals("")) ||
				    	(form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals(""))) {
			    	if (form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals("")) {
			    		contador++;
					    codigos.put(new Integer(contador), form.getImporteAdeudadoDesde());
				    	sql += " AND DEUDA >= :" + contador;
			    	}
			    	
			    	if (form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals("")) {
			    		contador++;
					    codigos.put(new Integer(contador), form.getImporteAdeudadoHasta());
				    	sql += " AND DEUDA <= :" + contador;
			    	}
			    }
	    	}
		    
		    sql += " ORDER BY TO_NUMBER(" + FacFacturaBean.C_IDFACTURA + ") DESC";   		 
		    
		    PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(sql,codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador = null;
			}
			
			return paginador;
			
		} catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
	}

	/**
	 * Obtiene las facturas pendientes de un colegiado en un rango de fechas
	 * @param idInstitucion de la factura
	 * @param idPersona del colegiado
	 * @param fechaDesde en formato dd/mm/yyyy
	 * @param fechaHasta en formato dd/mm/yyyy
	 * @return datos de las facturas pendientes
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector selectFacturasMoroso(String idInstitucion, String idPersona, String fechaDesde, String fechaHasta,ArrayList alFacturas,
			String idFactura,boolean isComunicacionLarga, boolean isInforme, String lenguaje) throws ClsExceptions, SIGAException {
		
	    Vector datos = new Vector();
	    int contador = 0;
	    Hashtable codigos = new Hashtable();
	    
	    try {
			String select = "SELECT to_char(FECHA_EMISION,'DD/MM/RRRR') AS " + FacFacturaBean.C_FECHAEMISION + ", " +
								" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(to_char(FECHA_EMISION,'DD/MM/RRRR'),'m',"+lenguaje+") AS FECHAEMISIONMESLETRA, " +					
								FacFacturaBean.C_NUMEROFACTURA	+ ", " +
								FacFacturaBean.C_IDFACTURA	+ ", " +
								" NETO, " +
								" TOTALIVA, " +
								" TOTAL, " +
								" PAGADO, " +
								" DEUDA, " +
								" DECODE(NUMCUENTA, NULL, '', CODBANCO || ' ' || CODSUCURSAL || ' ' || DIGCONTROL || ' ' || NUMCUENTA) AS NUM_CUENTA_BANCARIA, " +								
								" CODBANCO AS CODIGO_BANCO, " +
								" CODSUCURSAL AS CODIGO_SUCURSAL, " +
								" DIGCONTROL AS DIGITO_CONTROL, " +
								" NUMCUENTA AS NUMERO_CUENTA, " +
								" NOMBANCO AS NOMBRE_BANCO, " +
								" COMUNICACIONES, " +
								" COMUNICACIONESMESLETRA, " +								
								CenColegiadoBean.C_NCOLEGIADO + ", " +
								" NOMBRE, " +
								CenPersonaBean.C_NIFCIF + ", " +
								" CONCEPTO_DESCSERIEFACT, " +
								" DESCRIPCION_PROGRAMACION "
								+ (isInforme ? ", MOTIVO_DEVOLUCION" : "") +
							" FROM ( " +
								" SELECT F." + FacFacturaBean.C_FECHAEMISION + " AS FECHA_EMISION, " +
									" F." + FacFacturaBean.C_NUMEROFACTURA + ", " +
									" F." + FacFacturaBean.C_IDFACTURA + ", " +
									" F." + FacFacturaBean.C_IMPTOTALNETO + " AS NETO, " +
									" F." + FacFacturaBean.C_IMPTOTALIVA + " AS TOTALIVA, " +
									" F." + FacFacturaBean.C_IMPTOTAL + " AS TOTAL, " +
									" F." + FacFacturaBean.C_IMPTOTALPAGADO + " AS PAGADO, " +
									" F." + FacFacturaBean.C_IMPTOTALPORPAGAR + " AS DEUDA, " +
		                            " C." + CenCuentasBancariasBean.C_IBAN + " AS IBAN , " +
									" C." + CenCuentasBancariasBean.C_CBO_CODIGO + " AS CODBANCO , " +
		                            " C." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + " AS CODSUCURSAL, " +
		                            " C." + CenCuentasBancariasBean.C_DIGITOCONTROL + " AS DIGCONTROL, " +
		                            " C." + CenCuentasBancariasBean.C_NUMEROCUENTA + " AS NUMCUENTA, " +
		                            " B." + CenBancosBean.C_NOMBRE	+ " AS NOMBANCO , " +
									" F_SIGA_GETCOMFACTURA( " + 
		                            	" F." + FacFacturaBean.C_IDINSTITUCION + ", " +
		                            	" F."+FacFacturaBean.C_IDPERSONA + ", " +
		                            	" F." + FacFacturaBean.C_IDFACTURA + ", " + 
		                            	(isComunicacionLarga ? "1" : "0") +
		                            " ) AS COMUNICACIONES, " +							
		                            " F_SIGA_GETCOMFACTURAFECHALETRA( " +
		                            	" F." + FacFacturaBean.C_IDINSTITUCION + ", "+
		                            	" F." + FacFacturaBean.C_IDPERSONA + ", " +
		                            	" F." + FacFacturaBean.C_IDFACTURA + ", " +
		                            	(isComunicacionLarga ? "1" : "0") + ", " +
		                            	lenguaje + 
		                            " ) AS COMUNICACIONESMESLETRA, " +
		                            " F_SIGA_CALCULONCOLEGIADO(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDPERSONA + ") AS " + CenColegiadoBean.C_NCOLEGIADO + ", " +
		                            " P." + CenPersonaBean.C_NOMBRE + " || ' ' || P." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || P." + CenPersonaBean.C_APELLIDOS2 + " AS NOMBRE, " +
		                            " P." + CenPersonaBean.C_NIFCIF + ", " +
		                            " ( " +
		                            	" SELECT s.descripcion " +
		                            	" FROM " + FacSerieFacturacionBean.T_NOMBRETABLA + " S, " +
						                 	FacFacturacionProgramadaBean.T_NOMBRETABLA + " N " +
						                " WHERE S." + FacSerieFacturacionBean.C_IDINSTITUCION + " = N." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
						                  " AND N." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = F." + FacFacturaBean.C_IDINSTITUCION +
						                  " AND N." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = F." + FacFacturaBean.C_IDPROGRAMACION +
						                  " AND S." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = F." + FacFacturaBean.C_IDSERIEFACTURACION +
						                  " AND F." + FacFacturaBean.C_IDSERIEFACTURACION + " = N." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION +
				                  	" ) AS CONCEPTO_DESCSERIEFACT, " +
				                  	" FP." + FacFacturacionProgramadaBean.C_DESCRIPCION + " AS DESCRIPCION_PROGRAMACION" +
				                  	(isInforme ? ", F_SIGA_GETULTIMOMOTIVODEVFACT(" +
				                  						" F." + FacFacturaBean.C_IDINSTITUCION + ", " +
				                  						" F." + FacFacturaBean.C_IDPERSONA + ", " +				                  			
				                  						" F." + FacFacturaBean.C_IDFACTURA + ", " +				                  						
				                  						" F." + FacFacturaBean.C_COMISIONIDFACTURA + 
			                  						") AS MOTIVO_DEVOLUCION " : "") +
			                  	" FROM " + FacFacturaBean.T_NOMBRETABLA + " F, " + 
				                  	CenPersonaBean.T_NOMBRETABLA + " P, " +
				                  	CenCuentasBancariasBean.T_NOMBRETABLA + " C, " +
				                  	CenBancosBean.T_NOMBRETABLA + " B, " +
				                  	FacFacturacionProgramadaBean.T_NOMBRETABLA + " FP " +
			                  	" WHERE F." + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion +
			                  		" AND P." + CenPersonaBean.C_IDPERSONA + " = F." + FacFacturaBean.C_IDPERSONA +
			                  		" AND F." + FacFacturaBean.C_IDINSTITUCION + " = C." + CenCuentasBancariasBean.C_IDINSTITUCION + "(+) " +
			                  		" AND F." + FacFacturaBean.C_IDCUENTA + " = C." + CenCuentasBancariasBean.C_IDCUENTA + "(+) " +
			                  		" AND F." + FacFacturaBean.C_IDPERSONA + " = C." + CenCuentasBancariasBean.C_IDPERSONA + "(+) " +
			                  		" AND C." + CenCuentasBancariasBean.C_CBO_CODIGO + " = B." + CenBancosBean.C_CODIGO + "(+) " +
			                  		" AND F." + FacFacturaBean.C_IDINSTITUCION + " = FP." + FacFacturacionProgramadaBean.C_IDINSTITUCION + 
			                  		" AND F." + FacFacturaBean.C_IDSERIEFACTURACION + " = FP." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + 
			                  		" AND F." + FacFacturaBean.C_IDPROGRAMACION + " = FP." + FacFacturacionProgramadaBean.C_IDPROGRAMACION;
			
			if (idPersona!=null) {
				contador++;
				codigos.put(new Integer(contador), idPersona);
				select += " AND F." + FacFacturaBean.C_IDPERSONA + " = :" + contador;
			}
			
	       String fDesdeInc = fechaDesde; 
		   String fHastaInc = fechaHasta;
		   if ((fDesdeInc!=null && !fDesdeInc.trim().equals("")) || (fHastaInc!=null && !fHastaInc.trim().equals(""))) {					
				if (!fDesdeInc.equals(""))
					fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
				if (!fHastaInc.equals(""))
					fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
				Vector vCondicion = GstDate.dateBetweenDesdeAndHastaBind("F." + FacFacturaBean.C_FECHAEMISION, fDesdeInc, fHastaInc, contador, codigos);
					
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				select += " AND " + vCondicion.get(1) ;					
			}
		   
			//Si no viene filtrado por factura metemos en la calusula que el numero de factura no sea vacio
			if (idFactura!=null) {
				contador++;
				codigos.put(new Integer(contador), idFactura);
				select += " AND F." + FacFacturaBean.C_IDFACTURA + " = :" + contador;
			} else {
				select += " AND F." + FacFacturaBean.C_NUMEROFACTURA + " > '0'";
			}				
				
			select += " ) WHERE DEUDA > 0 " + 
						" ORDER BY FECHA_EMISION DESC";
	
			RowsContainer rc = new RowsContainer();			
			if (rc.queryBind(select,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);	
					if(alFacturas==null || alFacturas.contains(fila.getString(FacFacturaBean.C_IDFACTURA))){
						if(isInforme)
							datos.add(fila.getRow());
						else
							datos.add(fila);
					}
				}
			}
		} 
		
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   			
	   		} else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   				
	   			} else {
	   				throw new ClsExceptions(e, "Error al obtener las facturas de colegiado moroso.");
	   			}
	   		}	
	    }
	    
	    return datos;
	}
	
	/**
	 * Obtiene el estado de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Integer getEstadoFactura (Integer idInstitucion, String idFactura) throws ClsExceptions,SIGAException {
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idFactura);
		    
			String select =	"SELECT ESTADO FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				Integer num = UtilidadesHash.getInteger(aux, "ESTADO");
				return num;
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
	   				throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}


	
	/**
	 * Obtiene la descripcion del estado de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getDescEstadoFactura (Integer idInstitucion, String idFactura) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),idFactura);
	        
	        // RGG 26/05/2009 cambio de funciones de facturas
	        String select =	"SELECT FAC_ESTADOFACTURA.DESCRIPCION AS ESTADO_FACTURA FROM FAC_FACTURA, FAC_ESTADOFACTURA WHERE FAC_FACTURA.ESTADO=FAC_ESTADOFACTURA.IDESTADO AND FAC_FACTURA.IDINSTITUCION=:1 AND FAC_FACTURA.IDFACTURA=:2"; 
	        //String select =	"SELECT F_SIGA_DESCESTADOSFACTURA(:1, :2) as ESTADO_FACTURA FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "ESTADO_FACTURA");
				return num;
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
	   				throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 * Obtiene el el total de una factura
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Double getTotalFactura (Integer idInstitucion, String idFactura) throws ClsExceptions,SIGAException {
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idFactura);

//		  RGG 26/05/2009 CAMBIO DE FUNCIONES DE IMPORTES
            String select ="SELECT "+FacFacturaBean.C_IMPTOTAL+" AS TOTAL FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2 ";
            //String select =	"SELECT PKG_SIGA_TOTALESFACTURA.TOTAL(:1,:2) as TOTAL FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				Double num = UtilidadesHash.getDouble(aux, "TOTAL");
				return num;
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
	   				throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/** 
	 * Recoge las facturas incluidas en una serie de facturacion ya confirmada
	 * @param  institucion - identificador de la institucion
	 * @param  seriefacturacion - identificador de la serie de facturacion
	 * @param  idProgramacion - identificador de programacion
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getFacturasDeFacturacionProgramada(String institucion, String seriefacturacion, String idProgramacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            Hashtable codigos = new Hashtable();
	            int contador = 0;
	            String sql ="SELECT " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + "," +
	            			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + "," +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDESTADOPDF +
							" FROM " + FacFacturaBean.T_NOMBRETABLA +
								" INNER JOIN " + FacFacturacionProgramadaBean.T_NOMBRETABLA +
									" ON " + 
										FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=" +
										FacFacturacionProgramadaBean.T_NOMBRETABLA +"."+ FacFacturacionProgramadaBean.C_IDINSTITUCION +
										" AND " +
										FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDSERIEFACTURACION + "=" +
										FacFacturacionProgramadaBean.T_NOMBRETABLA +"."+ FacFacturacionProgramadaBean.C_IDSERIEFACTURACION +
										" AND " +
										FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDPROGRAMACION + "=" +
										FacFacturacionProgramadaBean.T_NOMBRETABLA +"."+ FacFacturacionProgramadaBean.C_IDPROGRAMACION +										
							" WHERE "; 
							contador++;
	            			codigos.put(new Integer(contador), institucion);
	            
	            			sql += FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "= :" + contador;
	            			sql += " AND "; 
							contador++;
	            			codigos.put(new Integer(contador), seriefacturacion);
	            			sql +=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDSERIEFACTURACION + "= :" + contador ;
	            			sql += " AND ";
							contador++;
	            			codigos.put(new Integer(contador), idProgramacion);
	            			sql +=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDPROGRAMACION + "= :" + contador ;
	            			sql += " ORDER BY " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " ASC"; 										
							
	            if (rc.findBind(sql,codigos)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila.getRow());
	               }
	            }
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener las facturas pendientes");
	       }
	       return datos;                        
	    }
	

	/** 
	 * Recoge las facturas pendientes de pago de una determinada persona comenzando por la que se indica como parametrosi es el caso<br/>
	 * @param  institucion - identificador de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idFactura - identificador de la factura asociada
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector getFacturasPendientes(String institucion, String idPersona, String idFactura,double pdtePagar) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
		   int contador = 0;	            
	       Hashtable codigos = new Hashtable();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " +
    						FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + "," +
	            			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + "," +
	            			FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTALPORPAGAR+ " AS PENDIENTE," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + //"," +
	            			//"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR("+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +","+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") AS PENDIENTE" +
							" FROM " + FacFacturaBean.T_NOMBRETABLA;
				            contador++;
							codigos.put(new Integer(contador),institucion);
							sql += " WHERE " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDINSTITUCION + "=:" + contador;
							contador++;
							codigos.put(new Integer(contador),idPersona);
							sql += " AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IDPERSONA + "=:" + contador;

							// RGG 26/05/2009 CAMBIO DE FUNCIONES DE FACTURAS
							sql += " AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_ESTADO + " NOT IN (1,7,8)  ";
							sql += " AND " + FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_IMPTOTALPORPAGAR + " > 0  ";

							sql += " ORDER BY " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " ASC";
							
	            if (rc.findBind(sql,codigos)) {
	            	//Variable que nos sirve para ir acumulando para saber cuando ya tiene facturas suficientes
	            	//para compensar el a
	            	double totalAcumulado = 0;
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable hfila = fila.getRow();
	                  String idFacturaFila = (String) hfila.get("IDFACTURA");
	                  
	                  
					  // RGG 26/05/2009 CAMBIO DE FUNCIONES DE FACTURAS (YA HAN QUEDADO EXCLUIDAS EN LA CONSULTA)
	                  //Integer estado = this.getEstadoFactura(new Integer(institucion),idFacturaFila);
	                  //Vamos a omitir las facturas pagadas o renegociadas  o anuladas
	                  //if (estado.intValue()==1 || estado.intValue()==7 || estado.intValue()==8) {
	                  //	  continue;
	                  //}
	                  Double ptePagarRow = new Double((String) hfila.get("PENDIENTE"));
	                  //Double ptePagarRow = this.getImportePendientePago(institucion, idFacturaFila);
	                  //Omitimos las facturas cuyo importe sea cero
	                  //if(ptePagarRow.doubleValue()==0)
	                  //	  continue;
	                  String idFacturaRow = (String)fila.getRow().get(FacFacturaBean.C_IDFACTURA);
	                  //hfila.put("PENDIENTE", ptePagarRow.toString());
	                  fila.setRow(hfila);
	                  totalAcumulado += ptePagarRow.doubleValue();
	                  
	                  //Si encontramos la factura la ponemos la primera
	                  //¿QUE SENTIDO TIENE COMPENSAR UN ABONO DE UNA FACTURA CON LA MISMA FACTURA?
	                  if(idFactura != null && !idFactura.trim().equals("") && idFacturaRow.equalsIgnoreCase(idFactura)){
			               datos.add(0,fila);
		                  
	                  }else{
		                  datos.add(fila);
	                  }
	                  //Si el total acumulado es mayor que lo pendiente por pagar nos salimos ya que tenemos las facturas
	                  //suficientes para compensar
	                  if(totalAcumulado >= pdtePagar)
	                	  break;
	                  
	               }
	            }
	       }
//	       catch (SIGAException e) {
//	       	throw e;
//	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener las facturas pendientes");
	       }
	       return datos;                        
	    }

	/**
	 * Obtiene los datos fundamentales que se mostrara en el informe de una factura
	 * @param idInstitucion de la factura
	 * @param idFActura de la factura
	 * @return datos asociados al informe
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getDatosInformeFactura (String idInstitucion, String idFactura)  throws ClsExceptions,SIGAException {
		
		Hashtable resultado = new Hashtable();
        Hashtable codigosBind = new Hashtable();
		int contador = 0;	            

		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + ", " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALNETO	+ " AS TOTAL_NETO, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALIVA	+ " AS TOTAL_IVA, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTAL	+ " AS TOTAL_FACTURA, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADO	+ " AS TOTAL_PAGADO, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALANTICIPADO	+ " AS TOTAL_ANTICIPADO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALCOMPENSADO	+ " AS TOTAL_COMPENSADO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOPORBANCO	+ " AS TOTAL_PAGADOPORBANCO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOPORCAJA	+ " AS TOTAL_PAGADOPORCAJA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA	+ " AS TOTAL_PAGADOSOLOCAJA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA	+ " AS TOTAL_PAGADOSOLOTARJETA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPORPAGAR	+ " AS TOTAL_PENDIENTEPAGAR, " +							
							"lpad(substr("+CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ",7),10,'*') NUMEROCUENTA " +
/*							"PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_FACTURA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_ANTICIPADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_COMPENSADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOTARJETA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORBANCO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADO, " +
							"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PENDIENTEPAGAR, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALNETO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_NETO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALIVA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_IVA " + */
							" FROM " + 
							FacFacturaBean.T_NOMBRETABLA + 
							" LEFT JOIN " + 
								CenCuentasBancariasBean.T_NOMBRETABLA + 
								" ON " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA +
							" WHERE ";
					contador++;
					codigosBind.put(new Integer(contador), idInstitucion);
					select+=FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :" +contador;
							select+=" AND ";
					contador++;
					codigosBind.put(new Integer(contador), idFactura);
					select+=FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = :" +contador;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigosBind)) {
				if (rc.size() > 1) return null;
					resultado = (Hashtable)((Row) rc.get(0)).getRow();
					
					//nuevo iag 
//				   String idFacturaFila = resultado.get(FacFacturaBean.C_IDFACTURA).toString();
//	               ConsPLFacturacion pl=new ConsPLFacturacion();
//	               
//	               Hashtable codigos2 = new Hashtable();
//	               codigos2.put(new Integer(1), idInstitucion);
//	               codigos2.put(new Integer(2), idFacturaFila);
//	               
//	               resultado.put("TOTAL_FACTURA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTAL"));
//	               resultado.put("TOTAL_ANTICIPADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO"));
//	               resultado.put("TOTAL_COMPENSADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO"));
//	               resultado.put("TOTAL_PAGADOSOLOCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA"));
//	               resultado.put("TOTAL_PAGADOSOLOTARJETA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA"));
//	               resultado.put("TOTAL_PAGADOPORCAJA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA"));
//	               resultado.put("TOTAL_PAGADOPORBANCO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO"));
//	               resultado.put("TOTAL_PAGADO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADO"));
//	               resultado.put("TOTAL_PENDIENTEPAGAR", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR"));
//	               resultado.put("TOTAL_NETO", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALNETO"));
//	               resultado.put("TOTAL_IVA", pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALIVA"));

				return resultado;
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
	   				throw new ClsExceptions(e, "Error al obtener los datos para el informe de una factura.");
	   			}
	   		}	
	    }
		return null;
	}

	/**
	 * Obtiene los datos fundamentales que se mostrara en el informe tipo MasterRepor de una factura
	 * @param idInstitucion de la factura
	 * @param idFActura de la factura
	 * @return datos asociados al informe
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Hashtable getDatosImpresionInformeFactura (String idInstitucion, String idFactura)  throws ClsExceptions,SIGAException {
		
		Hashtable nuevo= new Hashtable();
		Hashtable codigosBind = new Hashtable();
		int contador=0;
		
		try {
			String select = " SELECT " + 
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA 	+ ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVACIONES + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_OBSERVINFORME + "," +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA + ", " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTADEUDOR + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IBAN + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CBO_CODIGO + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_CODIGOSUCURSAL + ", " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_DIGITOCONTROL + ", " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALNETO	+ " AS IMPORTE_NETO, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALIVA	+ " AS IMPORTE_IVA, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTAL	+ " AS TOTAL_FACTURA, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADO	+ " AS TOTAL_PAGOS, " +
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALANTICIPADO	+ " AS ANTICIPADO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALCOMPENSADO	+ " AS COMPENSADO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOPORBANCO	+ " AS POR_BANCO, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOPORCAJA	+ " AS POR_CAJA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOSOLOCAJA	+ " AS POR_SOLOCAJA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPAGADOSOLOTARJETA	+ " AS POR_SOLOTARJETA, " +							
							FacFacturaBean.T_NOMBRETABLA + "."  + FacFacturaBean.C_IMPTOTALPORPAGAR	+ " AS PENDIENTE_PAGAR, " +							
							"lpad(substr("+CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ",7),10,'*') NUMEROCUENTA, " +
							CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_TITULAR + 
/*					        "PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_FACTURA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_ANTICIPADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_COMPENSADO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOSOLOTARJETA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORCAJA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADOPORBANCO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PAGADO, " +
							"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_PENDIENTEPAGAR, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALNETO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_NETO, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALIVA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL_IVA " + */
							" FROM " + 
							FacFacturaBean.T_NOMBRETABLA + 
							" LEFT JOIN " + 
								CenCuentasBancariasBean.T_NOMBRETABLA + 
								" ON " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
										 " AND " + 
										 CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_IDCUENTA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDCUENTA +
							" WHERE ";
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion);
					select+=FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :"+contador;
					select+=" AND ";
			contador++;
			codigosBind.put(new Integer(contador),idFactura);		
					select+=FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = :"+contador;
	
			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigosBind)) {
				if (rc.size() > 1) return null;
				Hashtable factura = ((Row) rc.get(0)).getRow();
				
				// RGG 15/02/2007 OBTENEMOS LOS VALORES, CALCULAMOS OTROS Y CAMBIAMOS LAS ETIQUETAS
				String kk="";

				CenInstitucionAdm institucionAdm = new CenInstitucionAdm(this.usrbean);
				nuevo.put("NOMBRE_COLEGIO",institucionAdm.getNombreInstitucion((String)factura.get(CenInstitucionBean.C_IDINSTITUCION)));
 
				String idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				// direccion institucion
				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
				if (direccion.get("DOMICILIO")!=null){
					nuevo.put("DIRECCION_COLEGIO",(String)direccion.get(CenDireccionesBean.C_DOMICILIO));
				}	

				direccion=direccionAdm.getEntradaDireccionEspecificaYUbicacion(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
				if (direccion.get(CenDireccionesBean.C_IDPAIS)!=null){
					if ((direccion.get(CenDireccionesBean.C_IDPAIS).equals("") || direccion.get(CenDireccionesBean.C_IDPAIS).equals(ClsConstants.ID_PAIS_ESPANA)) && (direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)!=null)&&(direccion.get("POBLACION")!=null) &&(direccion.get("PROVINCIA")!=null)){
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_COLEGIO",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACION")+", "+(String)direccion.get("PROVINCIA"));
					} else {
						CenPaisAdm admPais = new CenPaisAdm(this.usrbean);
						String pais = admPais.getDescripcion((String)direccion.get(CenDireccionesBean.C_IDPAIS));
						nuevo.put("CODIGO_POSTAL_POBLACION_PROVINCIA_COLEGIO",(String)direccion.get(CenDireccionesBean.C_CODIGOPOSTAL)+", "+(String)direccion.get("POBLACIONEXTRANJERA") + ", "+ pais);
					}
				}

				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
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

				idPersona=institucionAdm.getIdPersona((String)factura.get(CenInstitucionBean.C_IDINSTITUCION));
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
				nuevo.put("CIF_COLEGIO",personaAdm.obtenerNIF(idPersona));	

				// TRATAMIENTO PARA LA PERSONA DE LA FACTURA
				idPersona=(String)factura.get(CenInstitucionBean.C_IDPERSONA);
				
				// RGG ?? nuevo.put("CIF_COLEGIO",personaAdm.obtenerNombreApellidos(idPersona));
				
				String nombre = personaAdm.obtenerNombreApellidos(idPersona);
				if (nombre!=null) {
					nuevo.put("NOMBRE_CLIENTE",nombre);
				}else{
					nuevo.put("NOMBRE_CLIENTE","-");
				}
				direccion = new Hashtable();
				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),""+ClsConstants.TIPO_DIRECCION_FACTURACION);
				if (direccion.size()==0){
					throw new SIGAException("messages.censo.direcciones.facturacion");
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
						
				resultado=(String)factura.get(FacFacturaBean.C_NUMEROFACTURA);
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("NUMERO_FACTURA","-");
				} else {
					nuevo.put("NUMERO_FACTURA",resultado);
				}

				resultado=GstDate.getFormatedDateShort("",(String)factura.get(FacFacturaBean.C_FECHAEMISION));
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("FECHA_FACTURA","-");
				} else {
					nuevo.put("FECHA_FACTURA",resultado);
				}

				CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.usrbean);
				CenColegiadoBean datosColegiales = colegiadoAdm.getDatosColegiales(new Long(idPersona), new Integer((String)factura.get(FacFacturaBean.C_IDINSTITUCION)));
				resultado = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				if (resultado.equalsIgnoreCase("")){
					nuevo.put("NUMERO_COLEGIADO_FACTURA","-");
				} else {
					nuevo.put("NUMERO_COLEGIADO_FACTURA",resultado);
				}
                
				if (factura.get(FacFacturaBean.C_IDCUENTADEUDOR)!=null && (!((String)factura.get(FacFacturaBean.C_IDCUENTADEUDOR)).equalsIgnoreCase("")) ){
					resultado=UtilidadesString.getMensajeIdioma(this.usrbean,"facturacion.abonosPagos.boton.pagoDomiciliacionBanco");
				}else{
				  if ((factura.get(FacFacturaBean.C_IDCUENTA)==null)||(((String)factura.get(FacFacturaBean.C_IDCUENTA))).equalsIgnoreCase("")){
					resultado=UtilidadesString.getMensajeIdioma(this.usrbean,"facturacion.abonosPagos.boton.pagoCaja");
				  }else{
					
					resultado= UtilidadesString.mostrarIBANConAsteriscos((String)factura.get("IBAN"));					
					nuevo.put("TITULARCUENTA",(String)factura.get(CenCuentasBancariasBean.C_TITULAR));
				  }
				}  
				nuevo.put("FORMA_PAGO_FACTURA",resultado);
				
				resultado=(String)factura.get(FacFacturaBean.C_OBSERVACIONES);	
				nuevo.put("OBSERVACIONES",resultado);
				
				//nuevo iag 
				
			   String idFacturaFila = factura.get(FacFacturaBean.C_IDFACTURA).toString();
			   // formateo de valores
			   nuevo.put("ANTICIPADO", UtilidadesNumero.formato((String)factura.get("ANTICIPADO")));
			   nuevo.put("COMPENSADO", UtilidadesNumero.formato((String)factura.get("COMPENSADO")));
			   nuevo.put("POR_CAJA", UtilidadesNumero.formato((String)factura.get("POR_CAJA")));
			   nuevo.put("POR_SOLOCAJA", UtilidadesNumero.formato((String)factura.get("POR_SOLOCAJA")));
			   nuevo.put("POR_SOLOTARJETA", UtilidadesNumero.formato((String)factura.get("POR_SOLOTARJETA")));
			   nuevo.put("POR_BANCO", UtilidadesNumero.formato((String)factura.get("POR_BANCO")));
			   nuevo.put("TOTAL_FACTURA", UtilidadesNumero.formato((String)factura.get("TOTAL_FACTURA")));
			   nuevo.put("TOTAL_PAGOS", UtilidadesNumero.formato((String)factura.get("TOTAL_PAGOS")));
			   nuevo.put("PENDIENTE_PAGAR", UtilidadesNumero.formato((String)factura.get("PENDIENTE_PAGAR")));
			   nuevo.put("IMPORTE_NETO", UtilidadesNumero.formato((String)factura.get("IMPORTE_NETO")));
			   nuevo.put("IMPORTE_IVA", UtilidadesNumero.formato((String)factura.get("IMPORTE_IVA")));
			   
//               ConsPLFacturacion pl=new ConsPLFacturacion();
//               
//               Hashtable codigos2 = new Hashtable();
//               codigos2.put(new Integer(1), idInstitucion.toString());
//               codigos2.put(new Integer(2), idFacturaFila);	
//
//
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_ANTICIPADO"));//+" \u20AC";
////				nuevo.put("ANTICIPADO",resultado);
//				//resultado=(String)factura.get("TOTAL_ANTICIPADO");//+" \u20AC";
//				//nuevo.put("ANTICIPADO",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("ANTICIPADO", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALANTICIPADO")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_COMPENSADO"));//+" \u20AC";
////				nuevo.put("COMPENSADO",resultado);
//				//resultado=(String)factura.get("TOTAL_COMPENSADO");//+" \u20AC";
//				//nuevo.put("COMPENSADO",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("COMPENSADO", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALCOMPENSADO")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOPORCAJA"));//+" \u20AC";
////				nuevo.put("POR_CAJA",resultado);
//				//resultado=(String)factura.get("TOTAL_PAGADOPORCAJA");//+" \u20AC";
//				//nuevo.put("POR_CAJA",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("POR_CAJA", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORCAJA")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOSOLOCAJA"));//+" \u20AC";
////				nuevo.put("POR_SOLOCAJA",resultado);
////				resultado=(String)factura.get("TOTAL_PAGADOSOLOCAJA");//+" \u20AC";
////				nuevo.put("POR_SOLOCAJA",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("POR_SOLOCAJA", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOCAJA")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOSOLOTARJETA"));//+" \u20AC";
////				nuevo.put("POR_SOLOTARJETA",resultado);
//				//resultado=(String)factura.get("TOTAL_PAGADOSOLOTARJETA");//+" \u20AC";
//				//nuevo.put("POR_SOLOTARJETA",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("POR_SOLOTARJETA", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOSOLOTARJETA")));
//				
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADOPORBANCO"));//+" \u20AC";
////				nuevo.put("POR_BANCO",resultado);
//				//resultado=(String)factura.get("TOTAL_PAGADOPORBANCO");//+" \u20AC";
//				//nuevo.put("POR_BANCO",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("POR_BANCO", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADOPORBANCO")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_FACTURA"));//+" \u20AC";
////				nuevo.put("TOTAL_FACTURA",resultado);
//				//resultado=(String)factura.get("TOTAL_FACTURA");//+" \u20AC";
//				//nuevo.put("TOTAL_FACTURA",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("TOTAL_FACTURA", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTAL")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PAGADO"));//+" \u20AC";
////				nuevo.put("TOTAL_PAGOS",resultado);
//				//resultado=(String)factura.get("TOTAL_PAGADO");//+" \u20AC";
//				//nuevo.put("TOTAL_PAGOS",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("TOTAL_PAGOS", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALPAGADO")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_PENDIENTEPAGAR"));//+" \u20AC";
////				nuevo.put("PENDIENTE_PAGAR",resultado);
//				//resultado=(String)factura.get("TOTAL_PENDIENTEPAGAR");//+" \u20AC";
//				//nuevo.put("PENDIENTE_PAGAR",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("PENDIENTE_PAGAR", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_NETO"));//+" \u20AC";
////				nuevo.put("IMPORTE_NETO",resultado);
//				//resultado=(String)factura.get("TOTAL_NETO");//+" \u20AC";
//				//nuevo.put("IMPORTE_NETO",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("IMPORTE_NETO", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALNETO")));
//				
////				resultado=UtilidadesString.formatoImporte((String)factura.get("TOTAL_IVA"));//+" \u20AC";
////				nuevo.put("IMPORTE_IVA",resultado);
//				//resultado=(String)factura.get("TOTAL_IVA");//+" \u20AC";
//				//nuevo.put("IMPORTE_IVA",UtilidadesNumero.formato(resultado));
//				//nuevo iag 
//				nuevo.put("IMPORTE_IVA", UtilidadesNumero.formato(pl.getFuncionEjecutada(codigos2, "PKG_SIGA_TOTALESFACTURA.TOTALIVA")));				
			}
		}
		catch (SIGAException e) {
	    	throw e;
	    }
	    catch (Exception e) {
	    	throw new ClsExceptions(e, "Error al obtener los datos para el informe MasterRepor de una factura.");
	    }
		return nuevo;
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
			throw new ClsExceptions (e, "Excepcion en FacFacturaAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}

	public Hashtable getFacturasEmitidas (String institucion, String fechaDesde, String fechaHasta) throws ClsExceptions 
	{
		Hashtable salida = new Hashtable();
		try {

			fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
			fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta); 

		    Hashtable codigos = new Hashtable();
		    int contador=0;
			
			RowsContainer rc = new RowsContainer(); 
            String sql = " select fac_factura.numerofactura AS NUMERO_FACTURA, " +
            		            " to_char (fac_factura.fechaemision,'DD/MM/RRRR') AS FECHA, ";
            					contador++;
            					codigos.put(new Integer(contador),institucion);
            					
            		     sql += " F_SIGA_CALCULO_CUENTACLIENTE (:" + contador+ ", fac_factura.idfactura) AS SUBCUENTA, " +
            		            " cen_persona.nifcif as NIF, " +
            		            " cen_persona.nombre || ' ' || cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 as NOMBRE, " +
            		            // RGG CAMBIO DE OBSERVACIONES " fac_factura.observaciones as OBSERVACIONES, " +
            		            " fac_factura.observinforme as OBSERVACIONES, " +
            		            " (fac_lineafactura.preciounitario * fac_lineafactura.cantidad) AS BASE_IMPONIBLE, " +
//            		            " fac_lineafactura.cantidad AS CANTIDAD, " +
            		            " fac_lineafactura.iva AS VALOR_IVA, " +
            		            " ROUND(fac_lineafactura.cantidad * fac_lineafactura.preciounitario * fac_lineafactura.iva / 100, 2) as IVA, " +
            		            " ROUND(fac_lineafactura.cantidad * fac_lineafactura.preciounitario * (1 + fac_lineafactura.iva / 100), 2) as TOTAL_FACTURA " +
            		       " from fac_factura, fac_lineafactura , cen_cliente, cen_persona " +
						   
            		      " where fac_factura.idinstitucion =  fac_lineafactura.idinstitucion " +
            		        " and fac_factura.idfactura = fac_lineafactura.idfactura " +
            		        " and fac_factura.idinstitucion = cen_cliente.idinstitucion " +
            		        " and fac_factura.idpersona = cen_cliente.idpersona " +
            		        " and cen_cliente.idpersona = cen_persona.idpersona " +
            		        " and fac_factura.numerofactura is not null ";
        				    contador++;
        				    codigos.put(new Integer(contador),institucion);
        					
        		     sql +=" and fac_factura.idinstitucion = :" + contador + " ";
        				contador++;
        				codigos.put(new Integer(contador),fechaDesde);
    		     		
    		     sql += " and trunc(fac_factura.fechaemision) between trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) and ";
    					contador++;
    					codigos.put(new Integer(contador),fechaHasta);
    					
		     	 sql += " trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) ";
        		     
							//" and " + GstDate.dateBetweenDesdeAndHasta("fac_factura.fechaemision", fechaDesde, fechaHasta) + 
							
						//INC-4369 Se quiere obtener tambien los abonos (facturas rectificativas). Los importes tienen que salir con el signo negativo.	
							sql += " union ALL "+       
						   " select fac_abono.numeroabono AS NUMERO_FACTURA, "+
						   "    to_char(fac_abono.fecha, 'DD/MM/RRRR') AS FECHA, ";
							contador++;
							codigos.put(new Integer(contador),institucion);
							
						   sql+="    F_SIGA_CALCULO_CUENTACLIENTE(:"+contador+", fac_factura.idfactura) AS SUBCUENTA, "+
						   "    cen_persona.nifcif as NIF, "+
						   "    cen_persona.nombre || ' ' || cen_persona.apellidos1 || ' ' || "+
						   "    cen_persona.apellidos2 as NOMBRE, "+
						   "    fac_abono.observaciones as OBSERVACIONES, "+
						   "    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad)*-1 AS BASE_IMPONIBLE, "+
						   "    fac_lineaabono.iva AS IVA_PORCENTAJE, "+
						   "    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad * "+
						   "    (fac_lineaabono.iva / 100))*-1 as IVA, "+
						   "    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad)*-1 + "+
						   "    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad * "+
						   "   (fac_lineaabono.iva / 100))*-1 as TOTAL_FACTURA "+

						   " from fac_abono, fac_lineaabono, cen_cliente, cen_persona, fac_factura "+
						 "   where fac_abono.idinstitucion = fac_lineaabono.idinstitucion "+
						 "  and fac_abono.idfactura = fac_lineaabono.idfactura "+
						 "  and fac_abono.idinstitucion = cen_cliente.idinstitucion "+
						 "  and fac_abono.idpersona = cen_cliente.idpersona "+
						 "  and cen_cliente.idpersona = cen_persona.idpersona "+
						 "  and fac_abono.idinstitucion = fac_factura.idinstitucion "+
						 "  and fac_abono.idfactura = fac_factura.idfactura "+
						 "  and fac_abono.numeroabono is not null ";
						contador++;
						codigos.put(new Integer(contador),institucion);
    					
		    		     sql +="  and fac_abono.idinstitucion = :" + contador ;
		    		     
		    		     contador++;
		 				 codigos.put(new Integer(contador),fechaDesde);
				     		
				         sql += " and trunc(fac_abono.fecha) between trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) and ";
						 contador++;
						 codigos.put(new Integer(contador),fechaHasta);
							
			     	     sql += " trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) ";
			     	     sql +=" order by NUMERO_FACTURA ";

    				
            if (rc.findBind(sql, codigos)) {
            	double totalAcumuladoIVA = 0.0d,
					  totalAcumuladoTotalFactura = 0.0d,
					  totalAcumuladoBaseImponible = 0.0d;

        		Vector datos = new Vector();
        		Hashtable totalesXiva = new Hashtable();

            	for (int i = 0; i < rc.size(); i++){
            		Hashtable fila = ((Row)rc.get(i)).getRow();

            		// Calculo de total x iva
            		Float valorIVA  = UtilidadesHash.getFloat(fila, "VALOR_IVA");
            		Hashtable a = (Hashtable) totalesXiva.get(valorIVA);
            		a = TotalX_IVA_almacena(a, valorIVA.floatValue(),
            								   UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue(),
            				                   UtilidadesHash.getFloat(fila, "IVA").floatValue(), 
											   UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue());
            		totalesXiva.put(valorIVA, a);
            		
            		// Calculo de total acumulado
            		totalAcumuladoIVA           += UtilidadesHash.getFloat(fila, "IVA").floatValue();
            		totalAcumuladoTotalFactura  += UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue();
            		totalAcumuladoBaseImponible += UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue();

            		UtilidadesHash.set(fila, "TOTAL_ACUMULADO_IVA", UtilidadesNumero.formato(totalAcumuladoIVA));
            		UtilidadesHash.set(fila, "TOTAL_ACUMULADO_TOTAL_FACTURA", UtilidadesNumero.formato(totalAcumuladoTotalFactura));
            		UtilidadesHash.set(fila, "TOTAL_ACUMULADO_BASE_IMPONIBLE", UtilidadesNumero.formato(totalAcumuladoBaseImponible));

            		// Formato numeros
            		UtilidadesHash.set(fila, "IVA", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "IVA").floatValue()));
            		UtilidadesHash.set(fila, "IVA_PORCENTAJE", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "IVA_PORCENTAJE").floatValue()));
            		UtilidadesHash.set(fila, "TOTAL_FACTURA", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue()));
            		UtilidadesHash.set(fila, "BASE_IMPONIBLE", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue()));
            		
            		datos.add(fila);
            	}

                salida.put("REGISTROS", datos);
                salida.put("TOTALES_X_IVA", TotalX_IVA_ordena(totalesXiva));
            } 
       }
       catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return salida;
    }
	public RowsContainer getRowsFacturasEmitidas(String institucion, String fechaDesde, String fechaHasta) throws ClsExceptions, SIGAException{
		RowsContainer rc = new RowsContainer(); 
		try {
	
			fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
			fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta); 
	
			Hashtable codigos = new Hashtable();
			int contador=0;
	
	
			String sql = " select fac_factura.idfactura AS ID_FACTURA, "+
			" fac_factura.numerofactura AS NUMERO_FACTURA, " +
			" to_char (fac_factura.fechaemision,'DD/MM/RRRR') AS FECHA, ";
			contador++;
			codigos.put(new Integer(contador),institucion);
	
			sql += " F_SIGA_CALCULO_CUENTACLIENTE (:" + contador+ ", fac_factura.idfactura) AS SUBCUENTA, " +
			" cen_persona.nifcif as NIF, " +
			" cen_persona.nombre || ' ' || cen_persona.apellidos1 || ' ' || cen_persona.apellidos2 as NOMBRE, " +
			// RGG CAMBIO DE OBSERVACIONES " fac_factura.observaciones as OBSERVACIONES, " +
			" fac_factura.observinforme as OBSERVACIONES, " +
			" (fac_lineafactura.preciounitario * fac_lineafactura.cantidad) AS BASE_IMPONIBLE, " +
	//		" fac_lineafactura.cantidad AS CANTIDAD, " +
			" fac_lineafactura.iva AS IVA_PORCENTAJE, " +
			" ROUND(fac_lineafactura.cantidad * fac_lineafactura.preciounitario * fac_lineafactura.iva / 100, 2) as IVA, " +
			" ROUND(fac_lineafactura.cantidad * fac_lineafactura.preciounitario * (1 + fac_lineafactura.iva / 100), 2) as TOTAL_FACTURA " +
	
			" from fac_factura, fac_lineafactura , cen_cliente, cen_persona " +
	
			" where fac_factura.idinstitucion =  fac_lineafactura.idinstitucion " +
			" and fac_factura.idfactura = fac_lineafactura.idfactura " +
			" and fac_factura.idinstitucion = cen_cliente.idinstitucion " +
			" and fac_factura.idpersona = cen_cliente.idpersona " +
			" and cen_cliente.idpersona = cen_persona.idpersona " +
			" and fac_factura.numerofactura is not null ";
			contador++;
			codigos.put(new Integer(contador),institucion);
	
			sql +=" and fac_factura.idinstitucion = :" + contador + " ";
			contador++;
			codigos.put(new Integer(contador),fechaDesde);
	
			sql += " and trunc(fac_factura.fechaemision) between trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) and ";
			contador++;
			codigos.put(new Integer(contador),fechaHasta);
	
			sql += " trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) ";
	
			//" and " + GstDate.dateBetweenDesdeAndHasta("fac_factura.fechaemision", fechaDesde, fechaHasta) + 
	
			//INC-4369 Se quiere obtener tambien los abonos (facturas rectificativas). Los importes tienen que salir con el signo negativo.	
			sql += " union ALL "+       
			" select fac_factura.idfactura AS ID_FACTURA, "+
			" 	 fac_abono.numeroabono AS NUMERO_FACTURA, "+
			"    to_char(fac_abono.fecha, 'DD/MM/RRRR') AS FECHA, ";
			contador++;
			codigos.put(new Integer(contador),institucion);
	
			sql+="    F_SIGA_CALCULO_CUENTACLIENTE(:"+contador+", fac_factura.idfactura) AS SUBCUENTA, "+
			"    cen_persona.nifcif as NIF, "+
			"    cen_persona.nombre || ' ' || cen_persona.apellidos1 || ' ' || "+
			"    cen_persona.apellidos2 as NOMBRE, "+
			"    fac_abono.observaciones as OBSERVACIONES, "+
			"    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad)*-1 AS BASE_IMPONIBLE, "+
			"    fac_lineaabono.iva AS IVA_PORCENTAJE, "+
			"    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad * "+
			"    (fac_lineaabono.iva / 100))*-1 as IVA, "+
			"    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad)*-1 + "+
			"    (fac_lineaabono.preciounitario * fac_lineaabono.cantidad * "+
			"   (fac_lineaabono.iva / 100))*-1 as TOTAL_FACTURA "+
	
			" from fac_abono, fac_lineaabono, cen_cliente, cen_persona, fac_factura "+
			"   where fac_abono.idinstitucion = fac_lineaabono.idinstitucion "+
			"  and fac_abono.idfactura = fac_lineaabono.idfactura "+
			"  and fac_abono.idinstitucion = cen_cliente.idinstitucion "+
			"  and fac_abono.idpersona = cen_cliente.idpersona "+
			"  and cen_cliente.idpersona = cen_persona.idpersona "+
			"  and fac_abono.idinstitucion = fac_factura.idinstitucion "+
			"  and fac_abono.idfactura = fac_factura.idfactura "+
			"  and fac_abono.numeroabono is not null ";
			contador++;
			codigos.put(new Integer(contador),institucion);
	
			sql +="  and fac_abono.idinstitucion = :" + contador ;
	
			contador++;
			codigos.put(new Integer(contador),fechaDesde);
	
			sql += " and trunc(fac_abono.fecha) between trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) and ";
			contador++;
			codigos.put(new Integer(contador),fechaHasta);
	
			sql += " trunc(to_date(:"+contador+", 'YYYY/MM/DD HH24:mi:ss')) ";
			sql +=" order by FECHA asc";
	
	
	
			if(!rc.findBind(sql, codigos))
				throw new SIGAException("messages.informes.ficheroVacio");
				
				
	
		}
		catch (SIGAException e) {
			throw e;
			
		}
		catch (ClsExceptions e) {
			throw new ClsExceptions (e.getMsg());
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "FacFacturaAdm.getRowsFacturasEmitidas().Error al ejecutar consulta.");
		}
		return rc;
	
	
	}

	
	private Hashtable TotalX_IVA_almacena (Hashtable h, float valorIVA, float baseImponible, float iva, float totalFactura) 
	{
		try {
			if (h == null) {
				h = new Hashtable();
				UtilidadesHash.set(h, "POR_IVA_IVA_PORCENTAJE", new Double(valorIVA));
			}
			
			double aux = 0.0d;
	
			try {	
				Double d = UtilidadesHash.getDouble(h, "POR_IVA_BASE_IMPONIBLE");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(h, "POR_IVA_BASE_IMPONIBLE", new Double(aux + baseImponible));
			
			try {	
				Double d = UtilidadesHash.getDouble(h, "POR_IVA_IVA");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(h, "POR_IVA_IVA", new Double(aux + iva));

			try {	
				Double d = UtilidadesHash.getDouble(h, "POR_IVA_TOTAL_FACTURA");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(h, "POR_IVA_TOTAL_FACTURA", new Double(aux + totalFactura));
			
			return h;
		}
		catch (Exception e) {
			return h;
		}
	}
	
	private Vector TotalX_IVA_ordena (Hashtable h) 
	{
		Vector v = new Vector ();
		
		if (h == null)
			return v;
		
		Enumeration e = h.keys();
		while (e.hasMoreElements()) {
			Float clave = (Float) e.nextElement();
			Object valor = h.get(clave);

			int i;
			for (i = 0; i < v.size(); i++) {
				if (clave.floatValue() < ((Float)v.get(i)).floatValue()) {
					v.add(i, clave);
					break;
				}
			}
			if (i >= v.size()) v.add(clave);
		}

		Vector salida = new Vector();
		for (int i = 0; i < v.size(); i++) {
			Hashtable aux = (Hashtable)h.get(v.get(i));
//			if (i == 0) UtilidadesHash.set(aux, "POR_IVA_TEXTO_SUBTOTAL", "SUMA SUBTOTAL POR TIPO IVA");
			if (i == 0) UtilidadesHash.set(aux, "POR_IVA_TEXTO_SUBTOTAL", UtilidadesString.getMensajeIdioma(this.usrbean,"facturas.literal.sumaSubTotalPorIva"));
			else        UtilidadesHash.set(aux, "POR_IVA_TEXTO_SUBTOTAL", "");
			
    		// Formato numeros
    		UtilidadesHash.set(aux, "POR_IVA_IVA_PORCENTAJE", UtilidadesNumero.formato(UtilidadesHash.getDouble(aux, "POR_IVA_IVA_PORCENTAJE").doubleValue()));
    		UtilidadesHash.set(aux, "POR_IVA_BASE_IMPONIBLE", UtilidadesNumero.formato(UtilidadesHash.getDouble(aux, "POR_IVA_BASE_IMPONIBLE").doubleValue()));
    		UtilidadesHash.set(aux, "POR_IVA_IVA", UtilidadesNumero.formato(UtilidadesHash.getDouble(aux, "POR_IVA_IVA").doubleValue()));
    		UtilidadesHash.set(aux, "POR_IVA_TOTAL_FACTURA", UtilidadesNumero.formato(UtilidadesHash.getDouble(aux, "POR_IVA_TOTAL_FACTURA").doubleValue()));
			salida.add(aux);
		}
		return salida;
		
		
//		for (float i = 0.0f; i < 100.0; i+=0.01f) {
//			Hashtable a = (Hashtable)h.get(UtilidadesNumero.formato(i));
//			if (a != null)
//				v.add(a);
//		}
//		return v;
	}
	
	
	/**
	 * 
	 * @param beanFacturacionProgramada
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getFacturasProgramadas(FacFacturacionProgramadaBean beanFacturacionProgramada) throws ClsExceptions {
	    Vector salida = new Vector();
	    try {
	        Hashtable ht = new Hashtable();
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDINSTITUCION, beanFacturacionProgramada.getIdInstitucion());
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDSERIEFACTURACION, beanFacturacionProgramada.getIdSerieFacturacion());
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDPROGRAMACION, beanFacturacionProgramada.getIdProgramacion());
	        salida = this.select(ht);
	        
		} catch (Exception e) {
		    throw new ClsExceptions(e.toString());
	    }
	    
		return salida;
	}
	
	public Vector getFacturasFacturacionProgramada(FacFacturacionProgramadaBean facturacionProgramada)   throws ClsExceptions 
	{
	    Vector salida = new Vector();
	    try {
	        Hashtable ht = new Hashtable();
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDINSTITUCION,      facturacionProgramada.getIdInstitucion());
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDPROGRAMACION,     facturacionProgramada.getIdProgramacion());
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDSERIEFACTURACION, facturacionProgramada.getIdSerieFacturacion());
	        salida = this.select(ht);
		} 
	    catch (Exception e) {
		    throw new ClsExceptions(e.toString());
	    }
		return salida;
	}


	public String consultarActNuevoEstadoFactura(FacFacturaBean facturaBean, Integer usuario, boolean actualizar) throws ClsExceptions{
		
		String nuevoEstado="";
		String descEstado="";
				
		try {
		    double cero=0;
		    if (facturaBean.getImpTotalPorPagar().doubleValue()<=cero) {
	            // Está pagada
	            nuevoEstado = ClsConstants.ESTADO_FACTURA_PAGADA;
	        } else {
	            // Pendiente de pago
	        	// BNS 
	        	Hashtable ultimoFicheroBancarioDeFactura = this.getRenegociacionFactura(facturaBean.getIdInstitucion().toString(),facturaBean.getIdFactura().toString());
                if (ultimoFicheroBancarioDeFactura==null) {
                	if (facturaBean.getIdCuenta()==null && facturaBean.getIdCuentaDeudor()==null) {
    		            // pendiente pago por caja 
    	                nuevoEstado = ClsConstants.ESTADO_FACTURA_CAJA;
    	            } else {
	                    // La factura esta pendiente de enviar a banco
    	            	nuevoEstado= ClsConstants.ESTADO_FACTURA_BANCO;
    	            }
                } else {
                    if (ultimoFicheroBancarioDeFactura.get("IDRENEGOCIACION")==null || ((String)ultimoFicheroBancarioDeFactura.get("IDRENEGOCIACION")).trim().equals("")) {
                        //La factura esta devuelta y pendiente de renegociacion
                    	nuevoEstado = ClsConstants.ESTADO_FACTURA_DEVUELTA;
                    } else {
                        // La factura esta renegociada y pendiente de enviar a banco
                    	if (facturaBean.getIdCuenta()==null && facturaBean.getIdCuentaDeudor()==null) {
        		            // pendiente pago por caja 
        	                nuevoEstado = ClsConstants.ESTADO_FACTURA_CAJA;
        	            } else {
    	                    // La factura esta pendiente de enviar a banco
        	            	nuevoEstado= ClsConstants.ESTADO_FACTURA_BANCO;
        	            }
                    }
                }
	        }

		    if(actualizar){
		    	
			    Hashtable ht = new Hashtable();
			    ht.put(FacFacturaBean.C_IDINSTITUCION,facturaBean.getIdInstitucion());
			    ht.put(FacFacturaBean.C_IDFACTURA,facturaBean.getIdFactura());
			    Vector v = this.selectByPK(ht);
			    if (v!=null && v.size()>0) {
			        FacFacturaBean facturaLocalBean = (FacFacturaBean) v.get(0);
			        facturaLocalBean.setEstado(new Integer(nuevoEstado));
			        if (!this.update(facturaLocalBean)) {
			            throw new ClsExceptions("Error al actualizar el estado: "+this.getError());
			        }
			    }
		    }
		   
		    FacEstadoFacturaAdm facEstadoFacturaAdm = new FacEstadoFacturaAdm(this.usrbean);
		    
		    Hashtable hte = new Hashtable();
			hte.put(FacEstadoFacturaBean.C_IDESTADO,new Integer(nuevoEstado));
			Vector ve = facEstadoFacturaAdm.select(hte);
			if (ve!=null && ve.size()>0){ 
				FacEstadoFacturaBean facEstadoFacBean=(FacEstadoFacturaBean)ve.get(0);
				descEstado=facEstadoFacBean.getDescripcion();
			}
		    
		}catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en la consulta/actualización del estado de la factura.");
		}

		return descEstado;
	}

	public Hashtable getRenegociacionFactura (String institucion, String factura) throws ClsExceptions 
	{
		Hashtable salida = null;
		try {

		    Hashtable codigos = new Hashtable();
		    int contador=4;
			codigos.put(new Integer(1),institucion);
			codigos.put(new Integer(2),factura);
			codigos.put(new Integer(3),institucion);
			codigos.put(new Integer(4),factura);
			
			RowsContainer rc = new RowsContainer(); 
            String sql = " SELECT FAC_FACTURAINCLUIDAENDISQUETE.IDRENEGOCIACION " + 
       		 			 " FROM   FAC_DISQUETECARGOS, " + 
		                " FAC_FACTURAINCLUIDAENDISQUETE " + 
		                " WHERE  FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION = FAC_DISQUETECARGOS.IDINSTITUCION " + 
		                " AND    FAC_FACTURAINCLUIDAENDISQUETE.IDDISQUETECARGOS = FAC_DISQUETECARGOS.IDDISQUETECARGOS " + 
		                " AND    FAC_FACTURAINCLUIDAENDISQUETE.IDINSTITUCION  = :1 " + 
		                " AND    FAC_FACTURAINCLUIDAENDISQUETE.IDFACTURA = :2 " + 
		                " AND    FAC_DISQUETECARGOS.FECHACREACION IN (SELECT MAX(DISQ.FECHACREACION) " + 
                		" 											  FROM   FAC_DISQUETECARGOS DISQ, " + 
		                " 											  FAC_FACTURAINCLUIDAENDISQUETE FID " + 
		                " 											  WHERE  FID.IDINSTITUCION = DISQ.IDINSTITUCION " + 
		                " 											  AND    FID.IDDISQUETECARGOS = DISQ.IDDISQUETECARGOS " + 
		                " 											  AND    FID.IDINSTITUCION = :3 " + 
		                " 											  AND    FID.IDFACTURA = :4)";
            		            

    				
            if (rc.findBind(sql, codigos)) {
            	for (int i = 0; i < rc.size(); i++){
            		salida = ((Row)rc.get(i)).getRow();
            		break;
            	}
            } 

		}
       catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
       return salida;
    }
	
	/**
	 * Obtiene el Numerofactura
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getNumerofactura(Integer idInstitucion, String idFactura) throws ClsExceptions,SIGAException {
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    codigos.put(new Integer(2),idFactura);
            String select ="SELECT "+FacFacturaBean.C_NUMEROFACTURA+" AS NUMEROFACTURA FROM FAC_FACTURA WHERE IDINSTITUCION=:1 AND IDFACTURA=:2 ";

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String numeroFactura = UtilidadesHash.getString(aux, "NUMEROFACTURA");
				return numeroFactura;
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
	   				throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 *Comprueba si se han seleccionado personas diferentes
	 * @param form Formulario con los criterios
	 * @param idInstitucionAlta,usuario
	 * @return se muestra un resultado con un numero si tiene permiso.
	 * @throws ClsExceptions
	 */
	public Integer getSelectPersonas(Integer idInstitucion, String [] strFacturas) throws ClsExceptions,SIGAException {
		RowsContainer rc = new RowsContainer();
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    StringBuffer sql = new StringBuffer();
		    Hashtable codigosHashtable = new Hashtable();
			int contador = 0;
		    
			sql.append  ("Select Count(distinct ("+ FacFacturaBean.C_IDPERSONA + ")) As personas ");
			sql.append  ("  From  " + FacFacturaBean.T_NOMBRETABLA);
			sql.append  (" Where " + FacFacturaBean.C_IDINSTITUCION + " = :");
			contador ++;
			sql.append(contador);
			codigosHashtable.put(new Integer(contador),idInstitucion);
			sql.append  ("   And " + FacFacturaBean.C_IDFACTURA + " in ( "); 
			for (int i = 0; i < strFacturas.length; i++) {
				String factura = strFacturas[i];
				contador ++;
				sql.append(":");
				sql.append(contador);
				if(i!=strFacturas.length-1)
					sql.append(",");
				codigosHashtable.put(new Integer(contador),factura);
			}
			sql.append(" )");

			if (rc.findBind(sql.toString(),codigosHashtable) && rc.size() == 1) {
				Row fila = (Row) rc.get(0);
				return new Integer(fila.getString("PERSONAS"));
				
			} else {
				return 0;
			}
			
		} catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   			
	   		} else if (e instanceof ClsExceptions){
	   			throw (ClsExceptions)e;
	   			
	   		} else {
	   			throw new ClsExceptions(e, "Error al obtener si existen diferentes personas.");
	   		}	
	    }
	} // getSelectPersonas()			
	
	/**
	 *Comprueba si se han seleccionado personas diferentes
	 * @param form Formulario con los criterios
	 * @param idInstitucionAlta,usuario
	 * @return se muestra un resultado con un numero si tiene permiso.
	 * @throws ClsExceptions
	 */
	public String getCuentaPersona(Integer idInstitucion, Long idPersona) throws ClsExceptions,SIGAException {
		String diferentes = "";
		RowsContainer rc = new RowsContainer();
		try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion.toString());
		    StringBuffer sql = new StringBuffer();
		    Hashtable codigosHashtable = new Hashtable();
			int contador = 0;
		    
			sql.append  ("Select c." + CenCuentasBancariasBean.C_IDCUENTA + " as idcuenta");
			sql.append  ("  From  " + CenCuentasBancariasBean.T_NOMBRETABLA);
			sql.append  (" c,  " + CenBancosBean.T_NOMBRETABLA);
			sql.append  (" b  " );
			sql.append  (" Where c." + CenCuentasBancariasBean.C_CBO_CODIGO + " = b." + CenBancosBean.C_CODIGO);
			sql.append  (" and c." + CenCuentasBancariasBean.C_IDINSTITUCION + " = " + idInstitucion );
			sql.append  ("   And c." + CenCuentasBancariasBean.C_IDPERSONA + " = " + idPersona);
			sql.append  ("   And (c." + CenCuentasBancariasBean.C_FECHABAJA + " is null ");
			sql.append  ("   or c." + CenCuentasBancariasBean.C_FECHABAJA + " > sysdate) ");
			sql.append  ("   And c." + CenCuentasBancariasBean.C_ABONOCARGO + " in ('C', 'T') ");
			sql.append  ("   order by c." + CenCuentasBancariasBean.C_FECHAMODIFICACION + " desc "); 

			if (rc.findBind(sql.toString(),codigosHashtable) && rc.size() >= 1) {
				Row fila = (Row) rc.get(0);
				Hashtable resultado = fila.getRow();
				return diferentes = (String) resultado.get("IDCUENTA").toString();
			}
			else {
				return diferentes = "0";
			}
		} 	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener si existen diferentes personas.");
	   			}
	   		}	
	    }
	} // getSelectPersonas()		
	
	/**
	 * Obtiene las facturas a renegociar
	 * @param idInstitucion
	 * @param strFacturas
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public Vector getFacturasRenegociar (Integer idInstitucion, String listaFacturas) throws ClsExceptions {
		Vector vFacturas = new Vector();
		
		try {
			String where = " WHERE " + FacFacturaBean.C_ESTADO + " IN ( "+ ClsConstants.ESTADO_FACTURA_CAJA + ", " + ClsConstants.ESTADO_FACTURA_BANCO + ", " + ClsConstants.ESTADO_FACTURA_DEVUELTA  + ") " +
								" AND " + FacFacturaBean.C_IDINSTITUCION + " = " + idInstitucion + 
								" AND " + FacFacturaBean.C_IDFACTURA + " IN " + listaFacturas;  
		
			vFacturas = this.select(where);
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener las facturas a renegociar.");
		}
		
		return vFacturas;                        
	} //getFacturasRenegociar()
	
	/**
	 * Calcular un nuevo numero de factura
	 * @param idInstitucion
	 * @param idSerieFacturacion
	 * @return
	 * @throws ClsExceptions
	 */
	public Hashtable<String,Object> obtenerNuevoNumeroFactura (String idInstitucion, String idSerieFacturacion) throws ClsExceptions {
		
		Hashtable<String,Object> resultado = new Hashtable<String,Object>();
		
		try {
			String sql = "SELECT " + AdmContadorBean.T_NOMBRETABLA + ".*, " + 
							" LPAD(" + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_CONTADOR + " + 1, " + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_LONGITUDCONTADOR + ", '0') AS NUEVOCONTADOR " +
                    	" FROM " + AdmContadorBean.T_NOMBRETABLA + ", " +
                    		FacSerieFacturacionBean.T_NOMBRETABLA +
                    	" WHERE " + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_IDINSTITUCION + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +
                        	" AND " + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_IDCONTADOR + " = " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDCONTADOR +
                        	" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + idInstitucion + 
                        	" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + idSerieFacturacion;
			
			RowsContainer rc = new RowsContainer();
			if (rc.find(sql) && rc.size() == 1) {
				Row fila = (Row) rc.get(0);
				resultado = fila.getRow();
			}

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al calcular un nuevo numero de factura");
		}
		
		return resultado;                        
	} //obtenerNuevoNumeroFactura()
	
	/**
	 * Actualiza la factura renegociada
	 * @param facturaBean
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean actualizarFacturaRenegociacion (FacFacturaBean facturaBean) throws ClsExceptions {
		try {
			String sql = "UPDATE " + facturaBean.T_NOMBRETABLA +
						" SET " + facturaBean.C_IDCUENTA + " = " + facturaBean.getIdCuenta() + ", " +
							facturaBean.C_ESTADO + " = " + facturaBean.getEstado() + ", " +
							facturaBean.C_IDFORMAPAGO + " = " + facturaBean.getIdFormaPago() + ", " +
							facturaBean.C_FECHAMODIFICACION + " = SYSDATE, " +
							facturaBean.C_USUMODIFICACION + " = " + this.usrbean.getUserName() +
						" WHERE " + facturaBean.C_IDINSTITUCION + " = " + facturaBean.getIdInstitucion() + 
							" AND " + facturaBean.C_IDFACTURA + " = " + facturaBean.getIdFactura();
			
			return this.insertSQL(sql);					

		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al actualizar la factura renegociada");
		}                        
	} //actualizarFacturaRenegociacion()		
}