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
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import javax.transaction.UserTransaction;


import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorCaseSensitiveBind;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.certificados.Plantilla;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.facturacion.form.BusquedaFacturaForm;
import com.siga.facturacion.form.ConsultaMorososForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFactura;


/**
*
* Clase que gestiona la tabla CenHistorico de la BBDD
* 
*/
public class FacFacturaAdm extends MasterBeanAdministrador {
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
	protected String[] getCamposBean() {
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
							FacFacturaBean.C_FECHAMODIFICACION,
							FacFacturaBean.C_USUMODIFICACION,
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
							FacFacturaBean.C_ESTADO};
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
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

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
			bean.setFechaMod(UtilidadesHash.getString(hash,FacFacturaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,FacFacturaBean.C_USUMODIFICACION));			
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
			UtilidadesHash.set(htData,FacFacturaBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(htData,FacFacturaBean.C_USUMODIFICACION,b.getUsuMod());			
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
		}
		catch (Exception e) {
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
			sql ="SELECT (MAX(IDFACTURA) + 1) AS IDFACTURA FROM " + nombreTabla +
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
	            		sql += ComodinBusquedas.prepararSentenciaCompletaUPPERBind(":"+contador,FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA);
						//sql+=FacFacturaBean.T_NOMBRETABLA +"."+ FacFacturaBean.C_NUMEROFACTURA + "= :"+contador;
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
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA +
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
	public PaginadorCaseSensitiveBind getFacturas (BusquedaFacturaForm datos, Integer idInstitucion, String idioma)  throws ClsExceptions,SIGAException {
		
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
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IMPTOTAL  + " as IMPTOTAL, " +
							FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO  + ", " +
							FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " as DESCRIPCION_ESTADO, " +
							FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_DESCRIPCION + " as DESC_ESTADO_ORIGINAL, " +
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_DESCRIPCION + "";
			
							//"F_SIGA_DESCESTADOSFACTURA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as DESCRIPCION_ESTADO, " +
							//"PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL ";
							
			String from = " FROM " + 
							FacFacturaBean.T_NOMBRETABLA + ", " + CenPersonaBean.T_NOMBRETABLA+ ", " + FacEstadoFacturaBean.T_NOMBRETABLA + ", " + FacFacturacionProgramadaBean.T_NOMBRETABLA;
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :" +contador; 
					 where+=" AND " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION +
							" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_ESTADO+ " = " + FacEstadoFacturaBean.T_NOMBRETABLA + "." + FacEstadoFacturaBean.C_IDESTADO;

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
				// where += " AND F_SIGA_ESTADOSFACTURA(" + idInstitucion + "," + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") = " + datos.getBuscarIdEstado();
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
	
			String orderBy = " ORDER BY " // + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + ", "
										  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE 		+ ", "
										  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 	+ ", "
										  + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 	+ ", "
										  + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_FECHAREALGENERACION + " DESC, " 
			                              + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + " DESC ";
	
			String consulta = select + from + where + orderBy;
			selectContar+= from + where;
			PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(consulta,selectContar,codigosBind);				
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
		
		Vector resultados = new Vector (); 
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
							"PKG_SIGA_TOTALESFACTURA.TOTALNETO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTALNETO, " + 
							"PKG_SIGA_TOTALESFACTURA.TOTALIVA(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTALIVA, " +
							"PKG_SIGA_TOTALESFACTURA.TOTAL(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTAL, " +
							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(" + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + ", " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") as TOTALPAGADO, " + 
							FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_DESCRIPCION + " ";
 
			String from = 	" FROM " + 
							FacFacturaBean.T_NOMBRETABLA + ", " + FacSerieFacturacionBean.T_NOMBRETABLA + ", " + FacFacturacionProgramadaBean.T_NOMBRETABLA;
			
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion.toString());
			String where = 	" WHERE " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = :"+contador;
			contador++;
			codigosBind.put(new Integer(contador),idPersona.toString());
					 where+=" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPERSONA + " = :"+contador;
			if (dias!=null){		 
			 contador++;
			 codigosBind.put(new Integer(contador),dias.toString());
			 where+=" AND SYSDATE - " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " < :"+contador;
			}				 
			     	 where+=" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +
							" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + 
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION + " = "+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDSERIEFACTURACION +
							" AND " + FacFacturacionProgramadaBean.T_NOMBRETABLA + "." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDPROGRAMACION;
	
			String orderBy = " ORDER BY " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_FECHAEMISION + " DESC ";
	
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
                            "   and "+ CenCuentasBancariasBean.T_NOMBRETABLA+"."+CenCuentasBancariasBean.C_IDCUENTA+"="+ FacFacturaBean.T_NOMBRETABLA+"."+FacFacturaBean.C_IDCUENTADEUDOR+") CODIGOENTIDADDEUDOR";

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
   				//Es uniqeu key por lo que habra solo un registro
   				FacAbonoBean beanAbono = (FacAbonoBean)vAbonos.get(0);
   				resultado.put(FacAbonoBean.C_NUMEROABONO, beanAbono.getNumeroAbono()); 
   				sEstado += " ("+beanAbono.getNumeroAbono()+")";
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

	

	/**
	 * Almacenar: genera y guarda las facturas relacionadas con una detreminada serie de facturacion ya confirmada 
	 * @param institucion - identificador de la institucion
	 * @param serieFacturacion - identificador de la serie de facturacion
	 * @param idProgramacion - identificador de progamacion de la serie
	 * @param usuario - identificador del usuario
	 * @param bGenerarEnvios: indica si se enviaran de forma automatica las facturas
	 * @param log Clase de log para mensajes
	 * @return int - Devuelve: - 0 si todo está correcto.
	 * 						   - 1 si ha existido un error en el procesado de la factura.
	 * 						   - 2 si ha existido un error en el procesado del envío. 
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public int almacenarOldParaBorrar (HttpServletRequest request, Integer institucion, Long serieFacturacion, Long idProgramacion, 
			              boolean bGenerarEnvios, SIGALogging log,UserTransaction tx)  throws ClsExceptions,SIGAException 
	{
		boolean correcto=true;
		Vector facturas=new Vector();
		Vector plantillas=new Vector();
		String plantilla="";
		int tamanho=27;
		File ficFOP=null;
		int salida = 0;
		
		try {
			// Obtengo las facturas a almacenar
			facturas=getSerieFacturacionConfirmada(institucion.toString(),serieFacturacion.toString(),idProgramacion.toString());
			
			ClsLogging.writeFileLog("ALMACENAR >> "+institucion.toString()+" "+serieFacturacion.toString()+" "+idProgramacion.toString(),10);
			
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.usrbean);
			CenClienteAdm cliAdm = new CenClienteAdm(this.usrbean);
			plantillas=plantillaAdm.getPlantillaSerieFacturacion(institucion.toString(),serieFacturacion.toString());
			plantilla=plantillas.firstElement().toString();
						
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			//ReadProperties rp = new ReadProperties("SIGA.properties");
			Plantilla plantillaMng = new Plantilla(this.usrbean);
			
			
			// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
		    String rutaTemporal = rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
    		String barraTemporal = "";
    		String nombreFicheroTemporal = "";
    		if (rutaTemporal.indexOf("/") > -1){ 
    			barraTemporal = "/";
    		}
    		if (rutaTemporal.indexOf("\\") > -1){ 
    			barraTemporal = "\\";
    		}    		
    		rutaTemporal += barraTemporal+institucion.toString();
			File rutaFOP=new File(rutaTemporal);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaFOP.exists()){
				if(!rutaFOP.mkdirs()){

					// ESCRIBO EN EL LOG
		    		
					throw new ClsExceptions("La ruta de acceso a los ficheros temporales no puede ser creada");					
				}
			}
    		
			// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
		    String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
		    String idserieidprogramacion = serieFacturacion.toString()+"_" + idProgramacion.toString();
    		String barraAlmacen = "";
    		String nombreFicheroAlmacen = "";
    		if (rutaAlmacen.indexOf("/") > -1){ 
    			barraAlmacen = "/";
    		}
    		if (rutaAlmacen.indexOf("\\") > -1){ 
    			barraAlmacen = "\\";
    		}    		
    		rutaAlmacen += barraAlmacen+institucion.toString()+barraAlmacen+idserieidprogramacion;
			File rutaPDF=new File(rutaAlmacen);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaPDF.exists()){
				if(!rutaPDF.mkdirs()){

					// ESCRIBO EN EL LOG
		    		
					throw new ClsExceptions("La ruta de acceso a los ficheros PDF no puede ser creada");					
				}
			}
			
			// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
		    String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+rp.returnProperty("facturacion.directorioPlantillaFacturaJava");
		    String barraPlantilla="";
    		if (rutaPlantilla.indexOf("/") > -1){
    			barraPlantilla = "/";
    		}
    		if (rutaPlantilla.indexOf("\\") > -1){
    			barraPlantilla = "\\";
    		}
    		rutaPlantilla += barraPlantilla+institucion.toString()+barraPlantilla+plantilla;
			File rutaModelo=new File(rutaPlantilla);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaModelo.exists()){

				// ESCRIBO EN EL LOG
	    		
				throw new ClsExceptions("La ruta de acceso a la plantilla de la factura no existe");					
			}
    		
			Hashtable factura = null;
			
			ClsLogging.writeFileLog("ALMACENAR >> TERMINA DE OBTENER PLANTILLAS Y DATOS GENERALES",10);

			// recorro todas las facturas para ir creando lo sinformes pertinentes
    		Enumeration listaFacturas = facturas.elements();
    		boolean pdfok=true;
    		
			ClsLogging.writeFileLog("ALMACENAR >> NUMERO DE FACTURAS: "+facturas.size(),10);
    		
			String idFactura="";
    		while (correcto && listaFacturas.hasMoreElements()){

    			try {

	    			if (tx!=null) {
	    				tx.begin();
	    			}
	    			
	    			Hashtable facturaHash=(Hashtable)listaFacturas.nextElement();
	    			idFactura=(String)facturaHash.get(FacFacturaBean.C_IDFACTURA);
	    			String idInstitucionAux=(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION);
	    			String idPersona=(String)facturaHash.get(FacFacturaBean.C_IDPERSONA);
	    			CenColegiadoAdm admCol = new CenColegiadoAdm(this.usrbean);
	    			Hashtable htCol = admCol.obtenerDatosColegiado(idInstitucionAux,idPersona,this.usrbean.getLanguage());
	    			String nColegiado = "";
	    			if (htCol!=null && htCol.size()>0) {
	    			    nColegiado = (String)htCol.get("NCOLEGIADO_LETRADO");
	    			}
	    			// Obtenemos el lenguaje del cliente 
	    			String lenguaje = cliAdm.getLenguaje(institucion.toString(),idPersona); 
	    			String numFactura=(String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA);
	
	    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA: "+idFactura,10);
	    			
	    			// TRY del proceso de generacion de la factura en PDF
	    			try {
	    				
	    				// PROCESO DE CREAR EL PDF
	    				
	    				// RGG 15/02/2007 CAMBIOS PARA INFORME MASTER REPOR
	    				InformeFactura inf = new InformeFactura(this.usrbean);
	    				File filePDF = inf.generarFactura(request,lenguaje.toUpperCase(),this.usrbean.getLocation(),idFactura, nColegiado);

	    				if (filePDF==null) {
	    					throw new ClsExceptions("Error al generar la factura. Fichero devuelto es nulo.");				
	    				} else {
	    					correcto = true;
	    				}
						
	    				
	    				// TENEMOS LA FACTURA y la mandamos a firmar
	    				firmarPDF(filePDF,String.valueOf(institucion));
	    				
		    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FACTURA GENERADA EN PDF",10);
	    				
	    				/*
		    			// Creo el fichero temporal FOP
		        		nombreFicheroTemporal=rutaTemporal+barraTemporal+idFactura+".fo";
		        		ficFOP = new File(nombreFicheroTemporal);
		
		    			// Obtengo la informacion a incluir en las facturas PDF
		    			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.usrbean);
		    			factura = facturaAdm.getDatosInformeFactura(institucion.toString(),idFactura);
		    			FacLineaFacturaAdm adminLF = new FacLineaFacturaAdm(this.usrbean);
		    			Vector desglose=adminLF.getLineasImpresion((String)factura.get(FacFacturaBean.C_IDINSTITUCION),(String)factura.get(FacFacturaBean.C_IDFACTURA));
		    			
		    			// Fijacion del numero de paginas de la factura
		        		if (desglose.size()<=tamanho){
		        	    	correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFactura1-1.fo",desglose.size());
		        	    	if (!correcto) {
	            	    		throw new ClsExceptions("Error de plantilla de factura: "+ rutaPlantilla+barraPlantilla+"plantillaFactura1-1.fo");			        	    		
		        	    	}
		        		}
		        		
		        		if (desglose.size()>tamanho){
		        			int numLineas=tamanho+6;
		        			if (desglose.size()<numLineas){
		        				numLineas=desglose.size();
		        			}
		        	    
		        			correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFactura1-N.fo",numLineas);
		        	    	for (int i=1; i<=numLineas; i++){
		        	    		desglose.removeElementAt(0);
		        	    	}
		        	    	
		        	    	while ((correcto)&&(desglose.size()>tamanho+4)){
		            			numLineas=tamanho+9;
		            			if (desglose.size()<numLineas){
		            				numLineas=desglose.size();
		            			}
		            	    
		            			correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFacturaI-N.fo",numLineas);
		            	    	if (!correcto) {
		            	    		throw new ClsExceptions("Error de plantilla de factura: "+ rutaPlantilla+barraPlantilla+"plantillaFacturaI-N.fo");			        	    		
		            	    	}
		            	    	for (int i=1; i<=numLineas; i++){
		            	    		desglose.removeElementAt(0);
		            	    	}
		        	    	}
		        	    	
		        	    	if (correcto){
		            	    
		        	    		correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFacturaN-N.fo",desglose.size());
		            	    	if (!correcto) {
		            	    		throw new ClsExceptions("Error de plantilla de factura: "+ rutaPlantilla+barraPlantilla+"plantillaFacturaN-N.fo");			        	    		
		            	    	}
		        	    	}
		        		}

		    			// Obtencion fichero PDF
		        		File ficPDF=null;
		    	    	if (correcto){
							
		    	    		// genero la factura PDF
		    	    		ficPDF=new File(rutaAlmacen+barraAlmacen+(String)factura.get(FacFacturaBean.C_IDFACTURA)+".pdf"); 
							try {
								plantillaMng.convertFO2PDF(ficFOP, ficPDF, rutaPlantilla);
							} catch (ClsExceptions ce) {
								throw ce; 
							} finally {
								// borro el fichero temporal.
								if (ficFOP.exists()) { 
									ficFOP.delete();
								}
							}
		    	    	}

		    			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> FIN DE PLANTILLAS FACTURA: "+correcto,10);

		        		*/
	    				

	    			} catch (Exception ee) {
			    		
	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE FOP A PDF: "+ee.getLocalizedMessage(),10);
	    				
	    				// ESCRIBO EN EL LOG
						log.writeLogFactura("PDF",idPersona,numFactura,"Error en el proceso de generación de facturas PDF: "+ee.getLocalizedMessage());
	    	    		salida=1;
	    	    		throw ee;
	    			}
	    			
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> VAMOS A VER SI ENVIARMOS: ENVIAR:"+bGenerarEnvios+" CORRECTO:"+correcto,10);

		    	    	
	    			// Envio de facturas
	    			if (bGenerarEnvios && correcto){
	    				
	    				try {
	    					
		    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE ENVIO",10);

		    				//Obtenemos el bean del envio: 
		    				EnvEnviosAdm envAdm = new EnvEnviosAdm(this.usrbean);
		    				CenPersonaAdm admPersona = new CenPersonaAdm(this.usrbean);
		    				String descripcion = "Envio facturas - " + admPersona.obtenerNombreApellidos((String)facturaHash.get(FacFacturaBean.C_IDPERSONA));
		    				Envio envio = new Envio(this.usrbean,descripcion);
		
		    				// Bean envio
		    				EnvEnviosBean enviosBean = envio.enviosBean;
		    				
		    				// RGG
		    				GenParametrosAdm paramAdm = new GenParametrosAdm(this.usrbean);
		    				String preferencia = paramAdm.getValor(institucion.toString(),"ENV","TIPO_ENVIO_PREFERENTE","1");
		    				Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
		    	            enviosBean.setIdTipoEnvios(valorPreferencia);
		
		    				// Preferencia del tipo de envio si el usuario tiene uno:
		    				CenDireccionesAdm direccionAdm = new CenDireccionesAdm(this.usrbean);
		    				Hashtable direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),preferencia);
		    				
		    				if (direccion==null || direccion.size()==0) {
		    					 direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"2");// si no hay direccion preferente mail, buscamos la de despacho
		    					 if (direccion==null || direccion.size()==0) {
		    					 	direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"3");// si no hay direccion de despacho, buscamos la de correo.
		    					 	if (direccion==null || direccion.size()==0) {
		    					 		direccion=direccionAdm.getEntradaDireccionEspecifica((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),(String)facturaHash.get(FacFacturaBean.C_IDINSTITUCION),"");// si no hay direccion de despacho, buscamos cualquier direccion.
		    					 		if (direccion==null || direccion.size()==0) {
		    					 			ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> NO TIENE DIRECCION PREFERENTE "+preferencia,10);
		    		    					throw new ClsExceptions("No se ha encontrado dirección de la persona para el tipo de envio preferente: "+preferencia); 			
		    					 		}
		    					 	}
		    					 }
		    					
		    				}
		    				// RGG
		    				//if (direccion.get(CenDireccionesBean.C_PREFERENTE)!=null){
		    				//	String preferencia=(String)direccion.get(CenDireccionesBean.C_PREFERENTE);
		        			//	Integer valorPreferencia = Envio.calculaTipoEnvio(preferencia);
		        	        //    enviosBean.setIdTipoEnvios(valorPreferencia);
		    				//} 
		    				
		    				// Recojo una plantilla valida cualquiera:
		    				EnvPlantillasEnviosAdm plantillasEnviosAdm = new EnvPlantillasEnviosAdm(this.usrbean);
		    				Vector plantillasValidas=plantillasEnviosAdm.getIdPlantillasValidos(institucion.toString(),enviosBean.getIdTipoEnvios().toString());
		    				//Si no hay plantillas:
		    				if (plantillasValidas.isEmpty()){
		    					throw new ClsExceptions("No se han encontrado plantillas para el envio de facturas.");
		    				} else {
			    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> TENEMOS PLANTILLAS",10);

		    					//Tenemos plantillas:
		        				enviosBean.setIdPlantillaEnvios(new Integer((String)plantillasValidas.firstElement()));
		        				
		        				idFactura=(String)facturaHash.get(FacFacturaBean.C_IDFACTURA);
		    	    			
		    	    			// Obtenemos el lenguaje del cliente 
		    	    			
		    	    			
		    		   			
		    		  			
		        				// Creacion documentos
		        				Documento documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+UtilidadesString.validarNombreFichero((String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA))+".pdf","Factura "+nColegiado+"-"+UtilidadesString.validarNombreFichero((String)facturaHash.get(FacFacturaBean.C_NUMEROFACTURA))+".pdf");
		        				if(UtilidadesHash.getString(facturaHash,FacFacturaBean.C_NUMEROFACTURA)==null ||UtilidadesHash.getString(facturaHash,FacFacturaBean.C_NUMEROFACTURA).equals("")){
		        					documento = new Documento(rutaAlmacen+barraAlmacen+nColegiado+"-"+(String)facturaHash.get(FacFacturaBean.C_IDFACTURA)+".pdf","Factura "+nColegiado+"-"+(String)facturaHash.get(FacFacturaBean.C_IDFACTURA)+".pdf");	
		        				}
		        				Vector documentos = new Vector(1);
		        				documentos.add(documento);
		        				
		        				// Genera el envio:
	        					envio.generarEnvio((String)facturaHash.get(FacFacturaBean.C_IDPERSONA),documentos);
	    	    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ENVIO GENERADO OK",10);

		    				}
	
	    				} catch (Exception eee) {
	    		    		
	    					String mensaje ="";
	    					if (eee instanceof SIGAException) 
	    						mensaje = ((SIGAException)eee).getLiteral();
	    					else mensaje = eee.getLocalizedMessage();
	    					
	    					mensaje = UtilidadesString.getMensajeIdioma(this.usrbean, mensaje);
	    					
		    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> ERROR EN PROCESO DE ENVIO: "+mensaje,10);
	    		    		// ESCRIBO EN EL LOG
	    					log.writeLogFactura("ENVIO",idPersona,numFactura,"Error en el proceso de envío de facturas: "+mensaje);
	    					salida=2;
	    					throw eee;	
	        	    		
	    				}
	    			} 			
    		
	    			if (tx!=null) {
	    				tx.commit();
	    			}

    			} catch (Exception tot) {
    				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> CATCH GENERAL",10);
    				if (tx!=null) {
    					try {
    						tx.rollback();
    					} catch (Exception ee) {}
    				}
    			}
    			
				ClsLogging.writeFileLog("ALMACENAR "+idFactura+" >> PROCESO DE FACTURA OK ",10);

    		} // bucle
    		
		}catch (Exception e) {
			
			ClsLogging.writeFileLog("ALMACENAR >> ERROR GENERAL EN LA FUNCION ALMACENAR: "+e.getLocalizedMessage(),10);

			// ESCRIBO EN EL LOG un mensaje general con la descripcion de la excepcion
			log.writeLogFactura("PDF/ENVIO","N/A","N/A","Error general en el proceso de generación/envío de facturas PDF: "+e.getLocalizedMessage());

			
			if (ficFOP!=null && ficFOP.exists()){
				ficFOP.delete();
			}
		
			salida=3;
		}
		ClsLogging.writeFileLog("ALMACENAR >> SALIDA="+salida,10);
		return salida;
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
	public PaginadorCaseSensitiveBind  selectMorosos (UsrBean user, ConsultaMorososForm form) throws ClsExceptions 
	{

		
		// Acceso a BBDD
		boolean isFacturasPendientes = (form.getFacturasImpagadasDesde()!=null && !form.getFacturasImpagadasDesde().equals(""))
										||(form.getFacturasImpagadasHasta()!=null && !form.getFacturasImpagadasHasta().equals("")); 
		RowsContainer rc = null;
		
		try { 
			rc = new RowsContainer(); 				        
	        
			int contador=0;
			Hashtable codigos = new Hashtable();
			
	        String sql = "SELECT ";
	        
		    sql += FacFacturaBean.C_IDINSTITUCION+", ";	
		    sql += FacFacturaBean.C_IDPERSONA+", ";
		    sql += " NCOLEGIADO AS "+CenColegiadoBean.C_NCOLEGIADO+", ";
		    sql += "NOMBRE, ";
		    
		    
//		    sql += "MIN("+FacFacturaBean.C_FECHAEMISION+"), ";
//		    sql += "MAX("+FacFacturaBean.C_FECHAEMISION+"), ";
//		    sql += "COUNT("+FacFacturaBean.C_NUMEROFACTURA+"), ";
//		    sql += "SUM(DEUDA) FROM ";
		    //sql += FacFacturaBean.C_FECHAEMISION+", ";
		    sql += "trunc("+FacFacturaBean.C_FECHAEMISION+") "+FacFacturaBean.C_FECHAEMISION+", ";
		   // sql += FacFacturaBean.C_FECHAEMISION+", ";
		    sql += FacFacturaBean.C_NUMEROFACTURA+", ";
		    sql += "DEUDA,"+FacFacturaBean.C_IDFACTURA+", COMUNICACIONES" ;
		    if(isFacturasPendientes)
		    	sql +=" ,FACTURASPENDIENTES ";
		    
		    sql +=" FROM ";
		    sql += "(SELECT F."+FacFacturaBean.C_IDINSTITUCION+", ";
		    sql += "F."+FacFacturaBean.C_NUMEROFACTURA+", ";
		    sql += "F."+FacFacturaBean.C_IDFACTURA+", ";
		    sql += "F."+FacFacturaBean.C_FECHAEMISION+", ";
		    sql += "F."+FacFacturaBean.C_IDPERSONA+", ";
		    sql += "F."+FacFacturaBean.C_IMPTOTALPORPAGAR+" DEUDA, ";
		   // sql += "C."+CenColegiadoBean.C_NCOLEGIADO+", ";
		    contador++;
		    codigos.put(new Integer(contador),this.usrbean.getLocation());
		    sql += "f_siga_calculoncolegiado(:"+contador+",F."+FacFacturaBean.C_IDPERSONA+") as "+CenColegiadoBean.C_NCOLEGIADO+", ";
		    //sql += "DECODE(C.COMUNITARIO,'1',C.NCOMUNITARIO,C.NCOLEGIADO) as NUMCOLEGIADO, ";
		    sql += "P."+CenPersonaBean.C_NOMBRE+" || ' ' || P."+CenPersonaBean.C_APELLIDOS1+ " || ' ' || P."+CenPersonaBean.C_APELLIDOS2+" NOMBRE ";
		    contador++;
		    codigos.put(new Integer(contador),this.usrbean.getLocation());
		    sql += ", F_SIGA_GETCOMFACTURA(:"+contador+",F."+FacFacturaBean.C_IDPERSONA+",F."+FacFacturaBean.C_IDFACTURA+",0)  COMUNICACIONES,";
		    sql += "(SELECT count("+EnvComunicacionMorososBean.C_IDINSTITUCION+") FROM "+EnvComunicacionMorososBean.T_NOMBRETABLA+" ECM ";
		    sql += "WHERE ECM."+EnvComunicacionMorososBean.C_IDFACTURA+" = F."+FacFacturaBean.C_IDFACTURA;
		    contador++;
		    codigos.put(new Integer(contador),this.usrbean.getLocation());
		    sql += " AND ECM."+EnvComunicacionMorososBean.C_IDINSTITUCION+" = :"+contador+" ";
		    sql += "AND ECM."+EnvComunicacionMorososBean.C_IDPERSONA+" = F."+FacFacturaBean.C_IDPERSONA+") NCOMUNICACIONES ";
		    if(isFacturasPendientes){
		    	sql += ",(SELECT count(F2."+FacFacturaBean.C_IDINSTITUCION+") ";
				sql += "FROM "+FacFacturaBean.T_NOMBRETABLA+" F2 ";
				sql += "WHERE  ";
				contador++;
			    codigos.put(new Integer(contador),this.usrbean.getLocation());
			    sql += "F2."+FacFacturaBean.C_IDINSTITUCION+" = :"+contador+" ";
				sql += "AND F2."+EnvComunicacionMorososBean.C_IDPERSONA+" = F."+EnvComunicacionMorososBean.C_IDPERSONA;
				sql += " AND F2."+FacFacturaBean.C_NUMEROFACTURA+">'0'";
			    sql += " AND F2."+FacFacturaBean.C_IMPTOTALPORPAGAR+" > 0 ";
				sql += ") FACTURASPENDIENTES ";
		    }
		    
		    
		    sql += " FROM "+FacFacturaBean.T_NOMBRETABLA+" F, "+CenPersonaBean.T_NOMBRETABLA+" P , "+CenClienteBean.T_NOMBRETABLA+" C ";
		    contador++;
		    codigos.put(new Integer(contador),this.usrbean.getLocation());
		    sql += " WHERE F."+FacFacturaBean.C_IDINSTITUCION+"=:"+contador;
		    sql += " and C."+CenClienteBean.C_IDINSTITUCION+"=F."+FacFacturaBean.C_IDINSTITUCION;
		    sql += " and C."+CenClienteBean.C_IDPERSONA+"= F."+CenPersonaBean.C_IDPERSONA+ " ";
		    sql += " AND P."+CenPersonaBean.C_IDPERSONA+"=F."+FacFacturaBean.C_IDPERSONA+ " ";
		    
		   
		    String letrado = form.getLetrado();
		    if(letrado!=null && !letrado.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),letrado);
			    sql += " and F."+FacFacturaBean.C_IDPERSONA;
			    sql += " = :" + contador ;
		    }
		    String nombre = form.getInteresadoNombre();
		    if(nombre!=null && !nombre.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),"%"+nombre.toLowerCase()+"%");
			    sql += " AND lower(P."+CenPersonaBean.C_NOMBRE+") like :" + contador ;
		    }
		    String apellidos = form.getInteresadoApellidos();
		    if(apellidos!=null && !apellidos.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),"%"+apellidos.toLowerCase()+"%");
		    	sql += " AND lower(P."+CenPersonaBean.C_APELLIDOS1+"||' '||P."+CenPersonaBean.C_APELLIDOS2+") like :"+contador;
			    
		    }
		    
		    
		    String estadoColegial = form.getCmbEstadoColegial();
		    if(estadoColegial!=null && !estadoColegial.equalsIgnoreCase("")){
		    	//Se hace esto(grrr) para coger los dos estados colegiales
		        contador++;
			    codigos.put(new Integer(contador),estadoColegial);
		    	sql += " AND :"+contador+" like " ;
		    	sql += "'%'||F_SIGA_GETTIPOCLIENTE(P."+CenPersonaBean.C_IDPERSONA+", " ;
		        contador++;
			    codigos.put(new Integer(contador),user.getLocation());
		    	sql += ":"+contador;
		    	sql += ", SYSDATE)||'%'";
			    
		    }
		    
		    String fDesde = form.getFechaDesde(); 
			String fHasta = form.getFechaHasta();
		    if ((fDesde!= null && !fDesde.trim().equals("")) || (fHasta != null && !fHasta.trim().equals(""))) {
					if (!fDesde.equals(""))
						fDesde = GstDate.getApplicationFormatDate("", fDesde); 
					if (!fHasta.equals(""))
						fHasta = GstDate.getApplicationFormatDate("", fHasta);
					
			         // -------------- dateBetweenDesdeAndHastaBind
			         Vector v = GstDate.dateBetweenDesdeAndHastaBind("F."+FacFacturaBean.C_FECHAEMISION, fDesde, fHasta, contador, codigos);
			         Integer in = (Integer)v.get(0);
			         String st = (String)v.get(1);
			         contador = in.intValue();
			         // --------------

					sql += " AND " + st;
			}
		    String numeroFactura = form.getNumeroFactura();
		    if(numeroFactura!=null && !numeroFactura.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),numeroFactura);
		    	sql += " AND F."+FacFacturaBean.C_NUMEROFACTURA+"=:"+contador;
		    }else{
		    	sql += " AND F."+FacFacturaBean.C_NUMEROFACTURA+">'0'";
		    }
		    
		    
		    
	    	sql += " AND F."+FacFacturaBean.C_IMPTOTALPORPAGAR + " > 0";
		    sql += " ) ";
		   
		    
		    
		    
//		    sql += " GROUP BY "+FacFacturaBean.C_IDINSTITUCION+", "+FacFacturaBean.C_IDPERSONA+", "+CenColegiadoBean.C_NCOLEGIADO+", "+CenPersonaBean.C_NOMBRE;
	    	// modificado por miguel.villegas
//		    if (form.getFacturasImpagadas()!=null && !form.getFacturasImpagadas().equals("")){
//		    	sql += " HAVING COUNT("+FacFacturaBean.C_NUMEROFACTURA+")>='"+form.getFacturasImpagadas()+"' ";
//			    if (form.getImporteAdeudado()!=null && !form.getImporteAdeudado().equals("")){
//			    	sql += " AND SUM(DEUDA)>="+form.getImporteAdeudado();		    	
//			    }
//		    }
//		    else{
//			    if (form.getImporteAdeudado()!=null && !form.getImporteAdeudado().equals("")){
//			    	sql += " HAVING SUM(DEUDA)>="+form.getImporteAdeudado();		    	
//			    }	
//		    }
	    	// fin modificacion		    
		    boolean isAnd = false;
		    String nComunicacionesDesde = form.getNumeroComunicacionesDesde();
		    if(nComunicacionesDesde!=null && !nComunicacionesDesde.equalsIgnoreCase("")){
		        contador++;
			    codigos.put(new Integer(contador),nComunicacionesDesde);
		    	sql += " WHERE NCOMUNICACIONES >= :"+contador;
			    isAnd = true;
		    }
		    String nComunicacionesHasta = form.getNumeroComunicacionesHasta();
		    if(nComunicacionesHasta!=null && !nComunicacionesHasta.equalsIgnoreCase("")){
		    	if(isAnd)
		    		sql += " AND ";
		    	else
		    		sql += " WHERE ";
		    	
		    	contador++;
			    codigos.put(new Integer(contador),nComunicacionesHasta);
		    	sql += " NCOMUNICACIONES <= :"+contador;
			    isAnd = true;
		    }
		    
		    if(isFacturasPendientes){
		    	
		    	if(form.getFacturasImpagadasDesde()!=null && !form.getFacturasImpagadasDesde().equals("")){
		    		if(isAnd)
			    		sql += " AND ";
			    	else
			    		sql += " WHERE ";

		    		contador++;
				    codigos.put(new Integer(contador),form.getFacturasImpagadasDesde());
			    	sql += " FACTURASPENDIENTES >= :"+contador;
				    isAnd = true;
		    	}
		    	
		    	if(form.getFacturasImpagadasHasta()!=null && !form.getFacturasImpagadasHasta().equals("")){
		    		if(isAnd)
			    		sql += " AND ";
			    	else
			    		sql += " WHERE ";
			    	
		    		contador++;
				    codigos.put(new Integer(contador),form.getFacturasImpagadasHasta());
			    	sql += " FACTURASPENDIENTES <= :"+contador;

				    isAnd = true;
		    	}
		    	 
		    	
		    }
		    if ((form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals(""))
					||(form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals(""))){
		    	
		    	if(form.getImporteAdeudadoDesde()!=null && !form.getImporteAdeudadoDesde().equals("")){
		    		if(isAnd)
			    		sql += " AND ";
			    	else
			    		sql += " WHERE ";
		    		
		    		contador++;
				    codigos.put(new Integer(contador),form.getImporteAdeudadoDesde());
			    	sql += " DEUDA>= :"+contador;

				    isAnd = true;
		    	}
		    	
		    	if(form.getImporteAdeudadoHasta()!=null && !form.getImporteAdeudadoHasta().equals("")){
		    		if(isAnd)
			    		sql += " AND ";
			    	else
			    		sql += " WHERE ";
			    	
		    		contador++;
				    codigos.put(new Integer(contador),form.getImporteAdeudadoHasta());
			    	sql += " DEUDA<= :"+contador;

				    isAnd = true;
		    	}
		    	
		    }
		    PaginadorCaseSensitiveBind paginador = new PaginadorCaseSensitiveBind(sql,codigos);				
			int totalRegistros = paginador.getNumeroTotalRegistros();
			
			if (totalRegistros==0){					
				paginador =null;
			}
			return paginador;
		    	
		    
		    //sql += " ORDER BY NCOLEGIADO ";
		    //sql += " ORDER BY "+CenColegiadoBean.C_NCOLEGIADO+", NOMBRE";
			
			//ClsLogging.writeFileLog("FacFacturaAdm, sql: "+sql,3);
			
			//if (rc.queryBind(sql,codigos)) {
				//for (int i = 0; i < rc.size(); i++)	{
					//Row fila = (Row) rc.get(i);										
					//datos.add(fila);					
				//}
			//}
		}
		
		catch (Exception e) { 	
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
	public Vector selectFacturasMoroso(String idInstitucion, String idPersona, 
			String fechaDesde, String fechaHasta,ArrayList alFacturas,
			String idFactura,boolean isComunicacionLarga, boolean isInforme)  
		throws ClsExceptions,SIGAException {
		
	    Vector datos = new Vector();
	    int contador=0;
	    Hashtable codigos =new Hashtable();
	    
	    try {
		    
			String select = "SELECT " +
				"to_char("+FacFacturaBean.C_FECHAEMISION+",'DD/MM/RRRR') AS "+FacFacturaBean.C_FECHAEMISION+", "+
			
					
					FacFacturaBean.C_NUMEROFACTURA	+ ", " +
					FacFacturaBean.C_IDFACTURA	+ ", " +
					"NETO,TOTALIVA,TOTAL,PAGADO,DEUDA,COMUNICACIONES,"+
					CenColegiadoBean.C_NCOLEGIADO+",NOMBRE,"+CenPersonaBean.C_NIFCIF+", CONCEPTO_DESCSERIEFACT"+
					" FROM (SELECT " + 
							"F." + FacFacturaBean.C_FECHAEMISION + ", " +
							"F." + FacFacturaBean.C_NUMEROFACTURA	+ ", " +
							"F." + FacFacturaBean.C_IDFACTURA	+ ", " +
							"F." + FacFacturaBean.C_IMPTOTALNETO	+ " AS NETO, " +
							"F." + FacFacturaBean.C_IMPTOTALIVA	+ " AS TOTALIVA, " +
							"F." + FacFacturaBean.C_IMPTOTAL	+ " AS TOTAL, " +
							"F." + FacFacturaBean.C_IMPTOTALPAGADO	+ " AS PAGADO, " +
							"F." + FacFacturaBean.C_IMPTOTALPORPAGAR	+ " AS DEUDA " +
							
//							"PKG_SIGA_TOTALESFACTURA.TOTALNETO(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDFACTURA + ") as NETO, " +
//							"PKG_SIGA_TOTALESFACTURA.TOTALIVA(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDFACTURA + ") as TOTALIVA, " +
//							"PKG_SIGA_TOTALESFACTURA.TOTAL(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDFACTURA + ") as TOTAL, " +
//							"PKG_SIGA_TOTALESFACTURA.TOTALPAGADO(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDFACTURA + ") as PAGADO, " +
//							"PKG_SIGA_TOTALESFACTURA.PENDIENTEPORPAGAR(F." + FacFacturaBean.C_IDINSTITUCION + ", F." + FacFacturaBean.C_IDFACTURA + ") as DEUDA" +
							
							",F_SIGA_GETCOMFACTURA(F."+FacFacturaBean.C_IDINSTITUCION+",F."+FacFacturaBean.C_IDPERSONA+",F."+FacFacturaBean.C_IDFACTURA;
							if(isComunicacionLarga)
								select+=",1";
							else
								select+=",0";
							
							select+=") as COMUNICACIONES,";
							contador++;
							codigos.put(new Integer(contador),idInstitucion);				
							select+=" f_siga_calculoncolegiado(:"+contador+",F."+FacFacturaBean.C_IDPERSONA+") as "+CenColegiadoBean.C_NCOLEGIADO+", "+
							"P."+CenPersonaBean.C_NOMBRE+" || ' ' || P."+CenPersonaBean.C_APELLIDOS1+ " || ' ' || P."+CenPersonaBean.C_APELLIDOS2+" NOMBRE,P."+CenPersonaBean.C_NIFCIF+" "+CenPersonaBean.C_NIFCIF+", ";
							
							select+= " (select s.descripcion " +
						                 " from  " + FacSerieFacturacionBean.T_NOMBRETABLA + " S, " +
						                 		     FacFacturacionProgramadaBean.T_NOMBRETABLA + " N " +
						                " where S." + FacSerieFacturacionBean.C_IDINSTITUCION + " = N." + FacFacturacionProgramadaBean.C_IDINSTITUCION +
						                  " and N." + FacFacturacionProgramadaBean.C_IDINSTITUCION + " = F." + FacFacturaBean.C_IDINSTITUCION +
						                  " and N." + FacFacturacionProgramadaBean.C_IDPROGRAMACION + " = F." + FacFacturaBean.C_IDPROGRAMACION +
						                  " and S." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = F." + FacFacturaBean.C_IDSERIEFACTURACION +
						                  " and F." + FacFacturaBean.C_IDSERIEFACTURACION + " = N." + FacFacturacionProgramadaBean.C_IDSERIEFACTURACION +
						                  ") as CONCEPTO_DESCSERIEFACT";
							
			String from = 	" FROM " + 
							FacFacturaBean.T_NOMBRETABLA +" F, "+CenPersonaBean.T_NOMBRETABLA+" P ";;
			contador++;
			codigos.put(new Integer(contador),idInstitucion);
				
			String where = 	" WHERE F." + FacFacturaBean.C_IDINSTITUCION + " =:" + contador;
			where+=			" AND P."+CenPersonaBean.C_IDPERSONA+"=F."+FacFacturaBean.C_IDPERSONA+" ";
			if(idPersona!=null){
				contador++;
				codigos.put(new Integer(contador),idPersona);
				where += " AND F." + FacFacturaBean.C_IDPERSONA + " =:" + contador;
			}
			/*if (fechaDesde!=null && !fechaDesde.equals("")){
							where += " AND F." + FacFacturaBean.C_FECHAEMISION + " >= TO_DATE('" + GstDate.getApplicationFormatDate("",fechaDesde) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
			}
			if (fechaHasta!=null && !fechaHasta.equals("")){
							where += " AND F." + FacFacturaBean.C_FECHAEMISION + " <= TO_DATE('" + GstDate.getApplicationFormatDate("",fechaHasta) + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
			}*/
			
		       String fDesdeInc = fechaDesde; 
			   String fHastaInc = fechaHasta;
				if ((fDesdeInc != null && !fDesdeInc.trim().equals("")) || (fHastaInc != null && !fHastaInc.trim().equals(""))) {
					
					if (!fDesdeInc.equals(""))
						fDesdeInc = GstDate.getApplicationFormatDate("", fDesdeInc); 
					if (!fHastaInc.equals(""))
						fHastaInc = GstDate.getApplicationFormatDate("", fHastaInc);
					Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("F." + FacFacturaBean.C_FECHAEMISION, fDesdeInc, fHastaInc,contador,codigos);
					
					contador=new Integer(vCondicion.get(0).toString()).intValue();
					where +=" and " + vCondicion.get(1) ;
					
				}
			//Si no viene filtrado por factura metemos en la calusula que el numero de factura no sea vacio
			if(idFactura!=null){
				contador++;
				codigos.put(new Integer(contador),idFactura);
				where +=" AND F." + FacFacturaBean.C_IDFACTURA+ " =:"+contador+" )";
			}else{
				where +=" AND F." + FacFacturaBean.C_NUMEROFACTURA + " > '0' )";
			}				
					
			where += " WHERE DEUDA > 0";
			
			String orderBy = " ORDER BY "  + FacFacturaBean.C_FECHAEMISION + " DESC";
	
			String consulta = select + from + where + orderBy;
	
			RowsContainer rc = new RowsContainer();			
		
			
			if (rc.queryBind(consulta,codigos)) {
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
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
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
	public Vector getSerieFacturacionConfirmada(String institucion, String seriefacturacion, String idProgramacion) throws ClsExceptions,SIGAException {
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

							
							//sql += " AND " + "F_SIGA_ESTADOSFACTURA("+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +","+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") <> 1" +
							//" AND " +
							//"F_SIGA_ESTADOSFACTURA("+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION +","+ FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + ") <> 7" +
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
	public Hashtable getDatosImpresionInformeFactura (String idInstitucion, String idFactura)  throws ClsExceptions {
		
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
							"lpad(substr("+CenCuentasBancariasBean.T_NOMBRETABLA + "." + CenCuentasBancariasBean.C_NUMEROCUENTA + ",7),10,'*') NUMEROCUENTA " +
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
				
				direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"2");
				if (direccion.size()==0){// si no existe direccion de despacho, mostramos la de correo
					direccion=direccionAdm.getEntradaDireccionEspecifica(idPersona,(String)factura.get(FacFacturaBean.C_IDINSTITUCION),"3");
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
					
					resultado=factura.get(CenCuentasBancariasBean.C_CBO_CODIGO)+(String)factura.get("NUMEROCUENTA");
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
	
	/** 
	 * Obtiene los importes apuntados en compras para una institución y factura, y la cuenta contable del producto o servicio. 
	 * @param  institucion - identificador de la institucion
	 * @param  idFactura - Identificador de factura	 	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector obtenerCuentasValorPYS (String institucion, String idFactura) throws ClsExceptions {
		
		   Vector datos=new Vector();
		   Hashtable codigosBind = new Hashtable();
		   int contador = 0;
		   
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
					contador++;
					codigosBind.put(new Integer(contador),idFactura);
					sql +="  and    a.IDFACTURA=:"+contador;
					contador++;
					codigosBind.put(new Integer(contador), institucion);
					sql +="  and    a.IDINSTITUCION = :"+contador;
					sql +="  union all  ";
					sql +="  select NVL(ROUND(a2.CANTIDAD * a2.PRECIOUNITARIO, 2), 0) NETO, c2.CUENTACONTABLE CUENTA "; 
					sql +="  from   fac_lineafactura a2, fac_facturacionsuscripcion b2, pys_serviciosinstitucion c2";
					sql +="  where  a2.IDFACTURA = b2.IDFACTURA";
					sql +="  and    a2.NUMEROLINEA = b2.NUMEROLINEA";
					sql +="  and    a2.IDINSTITUCION = b2.IDINSTITUCION";
					sql +="  and    b2.IDINSTITUCION = c2.IDINSTITUCION  ";
					sql +="  and    b2.IDTIPOSERVICIOS = c2.IDTIPOSERVICIOS"; 
					sql +="  and    b2.IDSERVICIO = c2.IDSERVICIO";
					sql +="  and    b2.IDSERVICIOSINSTITUCION = c2.IDSERVICIOSINSTITUCION";
					contador++;
					codigosBind.put(new Integer(contador), idFactura);
					sql +="  and    a2.IDFACTURA= :"+contador;
					contador++;
					codigosBind.put(new Integer(contador), institucion);
                    sql +="  and    a2.IDINSTITUCION = :" +contador;
                    

	            if (rc.findBind(sql, codigosBind)) {
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
/*
	public String getRutaFactura (FacFacturaBean facBean)  throws ClsExceptions,SIGAException {

		boolean correcto=true;
		Vector facturas=new Vector();
		Vector plantillas=new Vector();
		String plantilla="";
		int tamanho=27;
		File ficFOP=null;
		int salida = 0;
		String nombre = "";
		
		try {
		
			ReadProperties rp = new ReadProperties("SIGA.properties");

			String idFactura=facBean.getIdFactura().toString();
			String idPersona=facBean.getIdPersona().toString();
			String numFactura=facBean.getNumeroFactura();
		
			String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
			String barraAlmacen = "";
			if (rutaAlmacen.indexOf("/") > -1){ 
				barraAlmacen = "/";
			}
			if (rutaAlmacen.indexOf("\\") > -1){ 
				barraAlmacen = "\\";
			}    		
			rutaAlmacen += barraAlmacen+facBean.getIdInstitucion().toString();

			nombre = rutaAlmacen+barraAlmacen+idFactura+".pdf";
			File ficPDF=new File(nombre); 
			if (ficPDF.exists()) {
				return nombre;
			}
			
			/// ES NECESARIO GENERARLA ANTES 
			
			// Obtengo la plantilla a utilizar
			FacPlantillaFacturacionAdm plantillaAdm = new FacPlantillaFacturacionAdm(this.usrbean);
			plantillas=plantillaAdm.getPlantillaSerieFacturacion(facBean.getIdInstitucion().toString(),facBean.getIdSerieFacturacion().toString());
			plantilla=plantillas.firstElement().toString();

			Plantilla plantillaMng = new Plantilla();
			
			// Obtencion de la ruta donde se almacenan temporalmente los ficheros formato FOP			
			String rutaTemporal = rp.returnProperty("facturacion.directorioFisicoTemporalFacturasJava")+rp.returnProperty("facturacion.directorioTemporalFacturasJava");
			String barraTemporal = "";
			String nombreFicheroTemporal = "";
			if (rutaTemporal.indexOf("/") > -1){ 
				barraTemporal = "/";
			}
			if (rutaTemporal.indexOf("\\") > -1){ 
				barraTemporal = "\\";
			}    		
			rutaTemporal += barraTemporal+facBean.getIdInstitucion().toString();
			File rutaFOP=new File(rutaTemporal);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaFOP.exists()){
				if(!rutaFOP.mkdirs()){
					throw new SIGAException("messages.envio.facturacion.rutaNoCreada");					
				}
			}
			
			String nombreFicheroAlmacen = "";
			File rutaPDF=new File(rutaAlmacen);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaPDF.exists()){
				if(!rutaPDF.mkdirs()){
					throw new SIGAException("messages.envio.facturacion.rutaPDFNoCreada");					
				}
			}
			
			// Obtencion de la ruta de donde se obtiene la plantilla adecuada			
			String rutaPlantilla = rp.returnProperty("facturacion.directorioFisicoPlantillaFacturaJava")+rp.returnProperty("facturacion.directorioPlantillaFacturaJava");
			String barraPlantilla="";
			if (rutaPlantilla.indexOf("/") > -1){
				barraPlantilla = "/";
			}
			if (rutaPlantilla.indexOf("\\") > -1){
				barraPlantilla = "\\";
			}
			rutaPlantilla += barraPlantilla+facBean.getIdInstitucion().toString()+barraPlantilla+plantilla;
			File rutaModelo=new File(rutaPlantilla);
			//Comprobamos que exista la ruta y sino la creamos
			if (!rutaModelo.exists()){
				throw new SIGAException("messages.envio.facturacion.rutaPlantillaNoExiste");					
			}
			
			Hashtable factura = null;
			
				
			// Creo el fichero temporal FOP
			nombreFicheroTemporal=rutaTemporal+barraTemporal+idFactura+".fo";
			ficFOP = new File(nombreFicheroTemporal);
	
			// Obtengo la informacion a incluir en las facturas PDF
			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.usrbean);
			factura = facturaAdm.getDatosInformeFactura(facBean.getIdInstitucion().toString(),idFactura);
			FacLineaFacturaAdm adminLF = new FacLineaFacturaAdm(this.usrbean);
			Vector desglose=adminLF.getLineasImpresion((String)factura.get(FacFacturaBean.C_IDINSTITUCION),(String)factura.get(FacFacturaBean.C_IDFACTURA));
			
			// Fijacion del numero de paginas de la factura
			if (desglose.size()<=tamanho){
		    	correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFactura1-1.fo",desglose.size());
		    	if (!correcto) {
		    		throw new SIGAException("messages.envio.facturacion.errorPlantillaFactura");			        	    		
		    	}
			}
			
			if (desglose.size()>tamanho){
				int numLineas=tamanho+6;
				if (desglose.size()<numLineas){
					numLineas=desglose.size();
				}
		    
				correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFactura1-N.fo",numLineas);
		    	for (int i=1; i<=numLineas; i++){
		    		desglose.removeElementAt(0);
		    	}
		    	
		    	while ((correcto)&&(desglose.size()>tamanho+4)){
	  			numLineas=tamanho+9;
	  			if (desglose.size()<numLineas){
	  				numLineas=desglose.size();
	  			}
	  	    
	  			correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFacturaI-N.fo",numLineas);
	  	    	if (!correcto) {
	  	    		throw new SIGAException("messages.envio.facturacion.errorPlantillaFactura");			        	    		
	  	    	}
	  	    	for (int i=1; i<=numLineas; i++){
	  	    		desglose.removeElementAt(0);
	  	    	}
		    	}
		    	
		    	if (correcto){
	  	    
		    		correcto=plantillaMng.obtencionPaginaFactura(ficFOP,factura,desglose,rutaPlantilla+barraPlantilla,"plantillaFacturaN-N.fo",desglose.size());
	  	    	if (!correcto) {
	  	    		throw new SIGAException("messages.envio.facturacion.errorPlantillaFactura");			        	    		
	  	    	}
		    	}
			}
	
			// Obtencion fichero PDF
		  	if (correcto){
					
		  		// genero la factura PDF
		  		ficPDF=new File(rutaAlmacen+barraAlmacen+(String)factura.get(FacFacturaBean.C_IDFACTURA)+".pdf"); 
				try {
					plantillaMng.convertFO2PDF(ficFOP, ficPDF, rutaPlantilla);
				} catch (ClsExceptions ce) {
					throw ce; 
				} finally {
					// borro el fichero temporal.
					if (ficFOP.exists()) { 
						ficFOP.delete();
					}
				}
		  	}
		
		} catch (SIGAException se) {
			throw se;
		} catch (Exception eee) {
			throw new ClsExceptions(eee,"Error general al generar una factura en PDF: "+eee.toString());
		}

		return nombre;
		
	}

*/

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
            		            " fac_lineafactura.iva AS IVA_PORCENTAJE, " +
            		            " (fac_lineafactura.preciounitario * fac_lineafactura.cantidad *(fac_lineafactura.iva/100)) as IVA, " +
            		            " (fac_lineafactura.preciounitario * fac_lineafactura.cantidad) + (fac_lineafactura.preciounitario * fac_lineafactura.cantidad * (fac_lineafactura.iva/100)) as TOTAL_FACTURA " +

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
            		Float porcentajeIVA  = UtilidadesHash.getFloat(fila, "IVA_PORCENTAJE");
            		Hashtable a = (Hashtable) totalesXiva.get(porcentajeIVA);
            		a = TotalX_IVA_almacena(a, porcentajeIVA.floatValue(),
            								   UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue(),
            				                   UtilidadesHash.getFloat(fila, "IVA").floatValue(), 
											   UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue());
            		totalesXiva.put(porcentajeIVA, a);
            		
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
	public RowsContainer getRowsFacturasEmitidas(String institucion, String fechaDesde, String fechaHasta) throws ClsExceptions{
		RowsContainer rc = new RowsContainer(); 
		try {
	
			fechaDesde = GstDate.getApplicationFormatDate("", fechaDesde); 
			fechaHasta = GstDate.getApplicationFormatDate("", fechaHasta); 
	
			Hashtable codigos = new Hashtable();
			int contador=0;
	
	
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
	//		" fac_lineafactura.cantidad AS CANTIDAD, " +
			" fac_lineafactura.iva AS IVA_PORCENTAJE, " +
			" (fac_lineafactura.preciounitario * fac_lineafactura.cantidad *(fac_lineafactura.iva/100)) as IVA, " +
			" (fac_lineafactura.preciounitario * fac_lineafactura.cantidad) + (fac_lineafactura.preciounitario * fac_lineafactura.cantidad * (fac_lineafactura.iva/100)) as TOTAL_FACTURA " +
	
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
	
	
	
			if(!rc.findBind(sql, codigos))
				throw new ClsExceptions("FacFacturaAdm.getRowsFacturasEmitidas().No se han obtenido resultados");
				
				
	
			
		}
		catch (ClsExceptions e) {
			throw new ClsExceptions (e.getMsg());
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "FacFacturaAdm.getRowsFacturasEmitidas().Error al ejecutar consulta.");
		}
		return rc;
	
	
	}

	
	private Hashtable TotalX_IVA_almacena (Hashtable h, float porcentajeIVA, float baseImponible, float iva, float totalFactura) 
	{
		try {
			if (h == null) {
				h = new Hashtable();
				UtilidadesHash.set(h, "POR_IVA_IVA_PORCENTAJE", new Double(porcentajeIVA));
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
	
	
	
	public Vector getFacturasSerieFacturacion(FacSerieFacturacionBean serieFacturacion)   throws ClsExceptions 
	{
	    Vector salida = new Vector();
	    try {
	        Hashtable ht = new Hashtable();
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDINSTITUCION,serieFacturacion.getIdInstitucion());
	        UtilidadesHash.set(ht, FacFacturaBean.C_IDSERIEFACTURACION,serieFacturacion.getIdSerieFacturacion());
	        salida = this.select(ht);
		} 
	    catch (Exception e) {
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


	/** 
	 * Actualiza estado factura
	 * 
	 * @param idinstitucion (Integer) IDINSTITUCION
	 * @param idfactura (String) IDFACTURA
	 * @param idusuario (Integer) USUARIO
	 * @throws ClsExceptions
	 */	
	public void actualizarEstadoFactura(Integer idinstitucion, String idfactura, Integer usuario) throws ClsExceptions{
		try 
		{
		    FacFacturaAdm facturaAdm = new FacFacturaAdm(this.usrbean);
		    Hashtable ht = new Hashtable();
		    ht.put(FacFacturaBean.C_IDINSTITUCION,idinstitucion);
		    ht.put(FacFacturaBean.C_IDFACTURA,idfactura);
		    Vector v = facturaAdm.selectByPK(ht);
		    String nuevoEstado = ""; 
		    if (v!=null && v.size()>0) {
		        FacFacturaBean facturaBean = (FacFacturaBean) v.get(0);
		        if (facturaBean.getImpTotalPorPagar().intValue()<=0) {
		            // Está pagada
		            nuevoEstado = "1";
		        } else {
		            // Pendiente de pago
		            if (facturaBean.getIdCuenta()==null && facturaBean.getIdCuentaDeudor()==null) {
			            // pendiente pago por caja 
		                nuevoEstado = "2";
		            } else {
		                Hashtable renegociacion = this.getRenegociacionFactura(idinstitucion.toString(),idfactura);
		                if (renegociacion==null) {
		                    // La factura esta pendiente de enviar a banco
		                    nuevoEstado="5";
		                } else {
		                    if (renegociacion.get("IDRENEGOCIACION")==null || ((String)renegociacion.get("IDRENEGOCIACION")).trim().equals("")) {
		                        //La factura esta devuelta y pendiente de renegociacion
		                        nuevoEstado="4";
		                    } else {
		                        // La factura esta renegociada y pendiente de enviar a banco
		                        nuevoEstado="3";
		                    }
		                }
			            // pendiente pago por banco 
		                nuevoEstado = "5";
		            }
		        }
		        
		        facturaBean.setEstado(new Integer(nuevoEstado));
		        if (!facturaAdm.update(facturaBean)) {
		            throw new ClsExceptions("Error al actualizar el estado: "+facturaAdm.getError());
		        }
		        
		    } else {
		        throw new ClsExceptions("No se ha encontrado la factura buscada: "+idinstitucion+ " "+idfactura);
		    }
		}
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en la actualización del estado de la factura.");
		}
	}

	public void actualizarEstadoFactura(FacFacturaBean facturaBean, Integer usuario) throws ClsExceptions{
		try 
		{
		    String nuevoEstado="";
		    double cero=0;
		    if (facturaBean.getImpTotalPorPagar().doubleValue()<=cero) {
	            // Está pagada
	            nuevoEstado = "1";
	        } else {
	            // Pendiente de pago
	            if (facturaBean.getIdCuenta()==null && facturaBean.getIdCuentaDeudor()==null) {
		            // pendiente pago por caja 
	                nuevoEstado = "2";
	            } else {
	                Hashtable renegociacion = this.getRenegociacionFactura(facturaBean.getIdInstitucion().toString(),facturaBean.getIdFactura().toString());
	                if (renegociacion==null) {
	                    // La factura esta pendiente de enviar a banco
	                    nuevoEstado="5";
	                } else {
	                    if (renegociacion.get("IDRENEGOCIACION")==null || ((String)renegociacion.get("IDRENEGOCIACION")).trim().equals("")) {
	                        //La factura esta devuelta y pendiente de renegociacion
	                        nuevoEstado="4";
	                    } else {
	                        // La factura esta renegociada y pendiente de enviar a banco
	                        nuevoEstado="3";
	                    }
	                }
		            // pendiente pago por banco 
	                nuevoEstado = "5";
	            }
	        }
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
		catch (Exception e){
			throw new ClsExceptions(e,"Excepcion en la actualización del estado de la factura.");
		}
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
}
