/*
 * VERSIONES:
 * 
 * daniel.casla - 04-11-2004 - Creacion
 * miguel.villegas.casla - 07-12-2004 - Incorporacion select 
 *	
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsConstants;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
*
* Clase que gestiona la tabla PYS_FORMAPAGOPRODUCTO de la BBDD
* 
*/

public class PysFormaPagoProductoAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysFormaPagoProductoAdm(UsrBean usu) {
		super(PysFormaPagoProductoBean.T_NOMBRETABLA, usu);
	}

	/** 
	 * Funcion que devuelve los campos de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */	
	protected String[] getCamposBean() {
		String [] campos = {PysFormaPagoProductoBean.C_IDINSTITUCION,
							PysFormaPagoProductoBean.C_IDTIPOPRODUCTO ,
							PysFormaPagoProductoBean.C_IDPRODUCTO,
							PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION,
							PysFormaPagoProductoBean.C_IDFORMAPAGO,
							PysFormaPagoProductoBean.C_INTERNET,
							PysFormaPagoProductoBean.C_FECHAMODIFICACION,
							PysFormaPagoProductoBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla
	 * @return  String[]  Claves de la tabla  
	 */	
	protected String[] getClavesBean() {
		String [] claves = {PysFormaPagoProductoBean.C_IDINSTITUCION,
							PysFormaPagoProductoBean.C_IDTIPOPRODUCTO ,
							PysFormaPagoProductoBean.C_IDPRODUCTO,
							PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION,
							PysFormaPagoProductoBean.C_IDFORMAPAGO};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysFormaPagoProductoBean bean = null;
		
		try {
			bean = new PysFormaPagoProductoBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_IDINSTITUCION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getLong(hash,PysFormaPagoProductoBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getLong(hash,PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdFormaPago(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_IDFORMAPAGO));
			bean.setInternet(UtilidadesHash.getString (hash,PysFormaPagoProductoBean.C_INTERNET));
			bean.setFechaMod(UtilidadesHash.getString (hash,PysFormaPagoProductoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysFormaPagoProductoBean.C_USUMODIFICACION));			
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
			PysFormaPagoProductoBean b = (PysFormaPagoProductoBean) bean;
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_IDINSTITUCION,b.getIdInstitucion()); 
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_IDPRODUCTO,b.getIdProducto());
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION,b.getIdProductoInstitucion());
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_IDFORMAPAGO,b.getIdFormaPago());
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_INTERNET,b.getInternet()); 
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysFormaPagoProductoBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	
	/** 
	 * Recoge el numero de productos relacionados con los parametros indicados <br/>
	 * y que tienen asociado como forma de pago una factura
	 * @param  institucion - identificador de la institucion 
	 * @param  tipo - identificador del tipo de producto
	 * @param  producto - identificador del producto	 
	 * @param  productoInstitucion - identificador del producto institucion	  
	 * @return  Vector - Fila seleccionada  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getProductoPorFactura (String institucion, String tipo, String producto, String productoInstitucion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT COUNT (*) FROM " + PysFormaPagoProductoBean.T_NOMBRETABLA +  
							" WHERE " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDTIPOPRODUCTO + "=" + tipo +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTO + "=" + producto +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION + "=" + productoInstitucion +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDFORMAPAGO + "=" + String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA); 
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener la informacion sobre un producto con forma de pago factura asociada.");
	       		}	
		   }
	       return datos;                        
	    }
	
	/** 
	 * Recoge las formas de pago asociadas a un producto <br/>
	 * @param  institucion - identificador de la institucion 
	 * @param  tipo - identificador del tipo de producto
	 * @param  producto - identificador del producto	 
	 * @param  productoInstitucion - identificador del producto institucion	  
	 * @return  Vector - Formas de pago asociadas  
	 * @exception  ClsExceptions, SIGAException
	 */
	public Vector getFormasPagoProducto (String institucion, String tipo, String producto, String productoInstitucion) throws ClsExceptions, SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            String sql ="SELECT " + 
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+  PysFormaPagoProductoBean.C_IDFORMAPAGO +
	            			" FROM " + PysFormaPagoProductoBean.T_NOMBRETABLA +  
							" WHERE " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDTIPOPRODUCTO + "=" + tipo +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTO + "=" + producto +
							" AND " +
							PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDPRODUCTOINSTITUCION + "=" + productoInstitucion +
					//		" AND " +
					//		PysFormaPagoProductoBean.T_NOMBRETABLA +"."+ PysFormaPagoProductoBean.C_IDFORMAPAGO + "=" + String.valueOf(ClsConstants.TIPO_FORMAPAGO_FACTURA)+
							" AND ROWNUM < 2";
							
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  datos.add(fila);
	               }
	            } 
	       }
		   catch (Exception e) {
	       		if (e instanceof SIGAException){
	       			throw (SIGAException)e;
	       		}
	       		else{
	       			throw new ClsExceptions(e,"Error al obtener las formas de pago asociadas a un producto.");
	       		}	
		   }
	       return datos;                        
	    }
	
}
