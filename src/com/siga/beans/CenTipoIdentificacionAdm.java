/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenTipoIdentificacionAdm extends MasterBeanAdministrador{

	public CenTipoIdentificacionAdm(UsrBean usu) {
		super(CenTipoIdentificacionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenTipoIdentificacionBean.C_DESCRIPCION, 			CenTipoIdentificacionBean.C_FECHAMODIFICACION,	CenTipoIdentificacionBean.C_CODIGOEXT,
							CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION, 	CenTipoIdentificacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTipoIdentificacionBean bean = null;
		
		try {
			bean = new CenTipoIdentificacionBean();
			bean.setDescripcion((String)hash.get(CenTipoIdentificacionBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenTipoIdentificacionBean.C_FECHAMODIFICACION));
			bean.setCodigoExt((String)hash.get(CenTipoIdentificacionBean.C_CODIGOEXT));
			bean.setIdTipoIdentificacion(UtilidadesHash.getInteger(hash, CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTipoIdentificacionBean.C_USUMODIFICACION));
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
			CenTipoIdentificacionBean b = (CenTipoIdentificacionBean) bean;
			htData.put(CenTipoIdentificacionBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTipoIdentificacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTipoIdentificacionBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION, String.valueOf(b.getIdTipoIdentificacion()));
			htData.put(CenTipoIdentificacionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenTipoIdentificacionBean.C_CODIGOEXT + ", " +
									  CenTipoIdentificacionBean.C_DESCRIPCION + ", " +
									  CenTipoIdentificacionBean.C_FECHAMODIFICACION + ", " +
									  CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION + ", " +
									  CenTipoIdentificacionBean.C_USUMODIFICACION + 
						   " FROM " + CenTipoIdentificacionBean.T_NOMBRETABLA;
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
			String sql = " SELECT " + CenTipoIdentificacionBean.C_CODIGOEXT + ", " +
			  CenTipoIdentificacionBean.C_DESCRIPCION + ", " +
			  CenTipoIdentificacionBean.C_FECHAMODIFICACION + ", " +
			  CenTipoIdentificacionBean.C_IDTIPOIDENTIFICACION + ", " +
			  CenTipoIdentificacionBean.C_USUMODIFICACION + 
			  " FROM " + CenTipoIdentificacionBean.T_NOMBRETABLA;
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
