/*
 * VERSIONES:
 * 
 * miguel.villegas - 07-12-2004 - Creacion
 * Modificada el 12-9-2005 por david.sanchezp para incluir la clave nueva idinstitucion.
 */

/**
*
* Clase que gestiona la tabla PYS_PRODUCTOS de la BBDD
* 
*/
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class PysProductosAdm extends MasterBeanAdministrador {

	/** 
	 *  Constructor
	 * @param  usu - Usuario
	 */	
	public PysProductosAdm(UsrBean usu) {
		super(PysProductosBean.T_NOMBRETABLA, usu);
	}

	/** 
	 *  Funcion que devuelve los campos dela tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getCamposBean() {
		String [] campos = {PysProductosBean.C_IDTIPOPRODUCTO,
							PysProductosBean.C_IDPRODUCTO,
							PysProductosBean.C_DESCRIPCION,
							PysProductosBean.C_IDINSTITUCION,
							PysProductosBean.C_FECHAMODIFICACION,
							PysProductosBean.C_USUMODIFICACION};
		return campos;
	}

	/** 
	 *  Funcion que devuelve las claves de la tabla.
	 * @return  String[] Los campos ed la tabla   
	 */
	protected String[] getClavesBean() {
		String [] claves = {PysProductosBean.C_IDTIPOPRODUCTO,
							PysProductosBean.C_IDPRODUCTO,
							PysProductosBean.C_IDINSTITUCION};
		return claves;
	}

	/** 
	 * Obtiene el bean a partir de la tabla hash introducida
	 * @param  hash - tabla con los valores asignables al bean
	 * @return  PysFormaPagoBean  Bean de retorno  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysProductosBean bean = null;
		
		try {
			bean = new PysProductosBean();
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash,PysProductosBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getLong(hash,PysProductosBean.C_IDPRODUCTO));			
			bean.setDescripcion(UtilidadesHash.getString(hash,PysProductosBean.C_DESCRIPCION));
			bean.setFechaMod(UtilidadesHash.getString (hash,PysProductosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,PysProductosBean.C_USUMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,PysProductosBean.C_IDINSTITUCION));
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
			PysProductosBean b = (PysProductosBean) bean; 
			UtilidadesHash.set(htData,PysProductosBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData,PysProductosBean.C_IDPRODUCTO, b.getIdProducto());			
			UtilidadesHash.set(htData,PysProductosBean.C_DESCRIPCION,b.getDescripcion());
			UtilidadesHash.set(htData,PysProductosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData,PysProductosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData,PysProductosBean.C_IDINSTITUCION, b.getIdInstitucion());
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
	 * Realiza un select sobre la tabla con una clausula where pasada como parametro
	 * @param  where - clausula where
	 * @return  Vector - Resultado de la busqueda  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());

			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					
					
					//////////////
					//Antes :
					// MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow());
					// Ahota:
					PysProductosBean registro = (PysProductosBean) this.hashTableToBeanInicial(fila.getRow()); 
					Hashtable clave = new Hashtable ();
					UtilidadesHash.set(clave, PysTiposProductosBean.C_IDTIPOPRODUCTO, registro.getIdTipoProducto());
					PysTiposProductosAdm tipoAdm = new PysTiposProductosAdm(this.usrbean);
					Vector v = tipoAdm.select(clave);
					if ((v == null) || v.size() != 1) {
						registro.setTipoProducto(null);
					}
					else {
						registro.setTipoProducto((PysTiposProductosBean)v.get(0));
					}
					//////////////

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

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					
					//////////////
					//Antes :
					// MasterBean registro = (MasterBean) this.hashTableToBean(fila.getRow());
					// Ahota:
					PysProductosBean registro = (PysProductosBean) this.hashTableToBeanInicial(fila.getRow()); 
					Hashtable clave = new Hashtable ();
					UtilidadesHash.set(clave, PysTiposProductosBean.C_IDTIPOPRODUCTO, registro.getIdTipoProducto());
					PysTiposProductosAdm tipoAdm = new PysTiposProductosAdm(this.usrbean);
					Vector v = tipoAdm.select(clave);
					if ((v == null) || v.size() != 1) {
						registro.setTipoProducto(null);
					}
					else {
						registro.setTipoProducto((PysTiposProductosBean)v.get(0));
					}
					//////////////

					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#insert(com.siga.beans.MasterBean)
	 */
	public boolean insert(MasterBean bean) throws ClsExceptions {
		try {
			PysProductosBean producto = (PysProductosBean) bean;
			producto.setIdProducto(this.getNuevoID(producto));
			if (super.insert(producto)) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar el producto en B.D.");
		}
	}
	
	/**
	 * Obtiene un nuevo ID del producto que se le pasa
	 * @author daniel.campos 12-02-05
	 * @version 1	 
	 * @param Producto datos del producto.
	 * @return nuevo ID del producto.
	 */
	public Long getNuevoID (PysProductosBean bean)throws ClsExceptions, SIGAException 
	{
		RowsContainer rc = null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		try {		
			String sql = " SELECT (MAX(" + PysProductosBean.C_IDPRODUCTO + ") + 1) AS " + PysProductosBean.C_IDPRODUCTO + 
			  			 " FROM " + this.nombreTabla +
						 " WHERE " +  PysProductosBean.C_IDTIPOPRODUCTO + " = " + bean.getIdTipoProducto()+
						 " AND "+PysProductosBean.C_IDINSTITUCION+"="+bean.getIdInstitucion();


			if (rc.findForUpdate(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();
				Long idProducto = UtilidadesHash.getLong(prueba, PysProductosBean.C_IDPRODUCTO);
				if (idProducto == null) {
					return new Long(0);
				}
				else return idProducto;								
			}
		}	
		catch (ClsExceptions e) {		
			throw e;		
		}
	    catch (Exception e) {
       		if (e instanceof SIGAException){
       			throw (SIGAException)e;
       		}
       		else{
       			throw new ClsExceptions(e,"Error al ejecutar el 'getNuevoID' de producto.");
       		}	
	    }
		return null;
	}	
	
	


}