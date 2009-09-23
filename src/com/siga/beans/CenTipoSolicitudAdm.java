/*
 * Created on 21-oct-2004
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

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenTipoSolicitudAdm extends MasterBeanAdministrador{

	public CenTipoSolicitudAdm(UsrBean usu) {
		super(CenTipoSolicitudBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenTipoSolicitudBean.C_DESCRIPCION, 	CenTipoSolicitudBean.C_FECHAMODIFICACION,
							CenTipoSolicitudBean.C_IDTIPOSOLICITUD,	CenTipoSolicitudBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTipoSolicitudBean.C_IDTIPOSOLICITUD};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTipoSolicitudBean bean = null;
		
		try {
			bean = new CenTipoSolicitudBean();
			bean.setDescripcion((String)hash.get(CenTipoSolicitudBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenTipoSolicitudBean.C_FECHAMODIFICACION));
			bean.setIdTipoSolicitud(UtilidadesHash.getInteger(hash, CenTipoSolicitudBean.C_IDTIPOSOLICITUD));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTipoSolicitudBean.C_USUMODIFICACION));
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
			CenTipoSolicitudBean b = (CenTipoSolicitudBean) bean;
			htData.put(CenTipoSolicitudBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTipoSolicitudBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTipoSolicitudBean.C_IDTIPOSOLICITUD, String.valueOf(b.getIdTipoSolocitud()));
			htData.put(CenTipoSolicitudBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			//String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean());
			String sql=" SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTipoSolicitudBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+CenTipoSolicitudBean.C_FECHAMODIFICACION+", "+CenTipoSolicitudBean.C_IDTIPOSOLICITUD+", "+CenTipoSolicitudBean.C_USUMODIFICACION+" FROM "+CenTipoSolicitudBean.T_NOMBRETABLA;
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
			String sql=" SELECT "+UtilidadesMultidioma.getCampoMultidioma(CenTipoSolicitudBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+CenTipoSolicitudBean.C_FECHAMODIFICACION+", "+CenTipoSolicitudBean.C_IDTIPOSOLICITUD+", "+CenTipoSolicitudBean.C_USUMODIFICACION+" FROM "+CenTipoSolicitudBean.T_NOMBRETABLA;
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
