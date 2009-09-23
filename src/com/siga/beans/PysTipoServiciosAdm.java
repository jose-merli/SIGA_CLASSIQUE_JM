/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;


public class PysTipoServiciosAdm extends MasterBeanAdministrador {
	
	PysTipoServiciosAdm (UsrBean usu) {
		super (PysTipoServiciosBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {PysTipoServiciosBean.C_IDTIPOSERVICIOS, 		
	    		PysTipoServiciosBean.C_DESCRIPCION,
	    		PysTipoServiciosBean.C_CODIGOEXT,
							PysTipoServiciosBean.C_FECHAMODIFICACION,
							PysTipoServiciosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {PysTipoServiciosBean.C_IDTIPOSERVICIOS};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysTipoServiciosBean bean = null;
		
		try {
			bean = new PysTipoServiciosBean();
			bean.setIdTipoServicios	(UtilidadesHash.getInteger(hash, PysTipoServiciosBean.C_IDTIPOSERVICIOS));
			bean.setDescripcion		(UtilidadesHash.getString(hash, PysTipoServiciosBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, PysTipoServiciosBean.C_CODIGOEXT));
			bean.setFechaMod		(UtilidadesHash.getString(hash, PysTipoServiciosBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, PysTipoServiciosBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			PysTipoServiciosBean b = (PysTipoServiciosBean) bean;
			UtilidadesHash.set(htData, PysTipoServiciosBean.C_IDTIPOSERVICIOS, b.getIdTipoServicios());
			UtilidadesHash.set(htData, PysTipoServiciosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, PysTipoServiciosBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, PysTipoServiciosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, PysTipoServiciosBean.C_USUMODIFICACION, b.getUsuMod());
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
	
	/** Funcion select (String where)
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " select " + PysTipoServiciosBean.C_CODIGOEXT + ", " + UtilidadesMultidioma.getCampoMultidioma(PysTipoServiciosBean.C_DESCRIPCION,this.usrbean.getLanguage()) + ", " + PysTipoServiciosBean.C_DESCRIPCION + ", " + PysTipoServiciosBean.C_FECHAMODIFICACION + ", " + PysTipoServiciosBean.C_IDTIPOSERVICIOS + ", " + PysTipoServiciosBean.C_USUMODIFICACION + " FROM "+ PysTipoServiciosBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	
	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " select " + PysTipoServiciosBean.C_CODIGOEXT + ", " + UtilidadesMultidioma.getCampoMultidioma(PysTipoServiciosBean.C_DESCRIPCION,this.usrbean.getLanguage()) + ", " + PysTipoServiciosBean.C_DESCRIPCION + ", " + PysTipoServiciosBean.C_FECHAMODIFICACION + ", " + PysTipoServiciosBean.C_IDTIPOSERVICIOS + ", " + PysTipoServiciosBean.C_USUMODIFICACION + " FROM "+ PysTipoServiciosBean.T_NOMBRETABLA;
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	
}
