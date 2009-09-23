/*
 * Created on 29-nov-2004
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
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.CenEstadoSolicitudBean;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenEstadoSolicitudAdm extends MasterBeanAdministrador 
{
	public CenEstadoSolicitudAdm(UsrBean usuario) {
		super(CenEstadoSolicitudBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenEstadoSolicitudBean.C_DESCRIPCION, 	CenEstadoSolicitudBean.C_FECHAMODIFICACION,
							CenEstadoSolicitudBean.C_IDESTADO, 		CenEstadoSolicitudBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenEstadoSolicitudBean.C_IDESTADO};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenEstadoSolicitudBean bean = null;
		
		try {
			bean = new CenEstadoSolicitudBean();
			bean.setDescripcion((String)hash.get(CenEstadoSolicitudBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenEstadoSolicitudBean.C_FECHAMODIFICACION));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, CenEstadoSolicitudBean.C_IDESTADO));
			bean.setUsuMod((UtilidadesHash.getInteger(hash, CenEstadoSolicitudBean.C_USUMODIFICACION)));
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
			CenEstadoSolicitudBean b = (CenEstadoSolicitudBean) bean;
			UtilidadesHash.set(htData, CenEstadoSolicitudBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CenEstadoSolicitudBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenEstadoSolicitudBean.C_IDESTADO, String.valueOf(b.getIdEstado()));
			UtilidadesHash.set(htData, CenEstadoSolicitudBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenEstadoSolicitudBean.C_DESCRIPCION,this.usrbean.getLanguage()) + ", " +
									  CenEstadoSolicitudBean.C_FECHAMODIFICACION + ", " +
									  CenEstadoSolicitudBean.C_IDESTADO + ", " +
									  CenEstadoSolicitudBean.C_USUMODIFICACION + " " +
						   " FROM " + CenEstadoSolicitudBean.T_NOMBRETABLA;
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenEstadoSolicitudBean.C_DESCRIPCION,this.usrbean.getLanguage()) + ", " +
			  CenEstadoSolicitudBean.C_FECHAMODIFICACION + ", " +
			  CenEstadoSolicitudBean.C_IDESTADO + ", " +
			  CenEstadoSolicitudBean.C_USUMODIFICACION + " " +
			  " FROM " + CenEstadoSolicitudBean.T_NOMBRETABLA;
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
