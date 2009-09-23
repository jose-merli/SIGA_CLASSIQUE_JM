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
public class CenTipoColegiacionAdm extends MasterBeanAdministrador {

	public CenTipoColegiacionAdm(UsrBean usu) {
		super(CenTipoColegiacionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenTipoColegiacionBean.C_DESCRIPCION, 	CenTipoColegiacionBean.C_FECHAMODIFICACION,
							CenTipoColegiacionBean.C_IDCOLEGIACION, CenTipoColegiacionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTipoColegiacionBean.C_IDCOLEGIACION};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions{

		CenTipoColegiacionBean bean = null;
		
		try {
			bean = new CenTipoColegiacionBean();
			bean.setDescripcion((String)hash.get(CenTipoColegiacionBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenTipoColegiacionBean.C_FECHAMODIFICACION));
			bean.setIdTipoColegiacion(UtilidadesHash.getInteger(hash, CenTipoColegiacionBean.C_IDCOLEGIACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTipoColegiacionBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions{
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenTipoColegiacionBean b = (CenTipoColegiacionBean) bean;
			htData.put(CenTipoColegiacionBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTipoColegiacionBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTipoColegiacionBean.C_IDCOLEGIACION, String.valueOf(b.getIdTipoColegiacion()));
			htData.put(CenTipoColegiacionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenTipoColegiacionBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
									  CenTipoColegiacionBean.C_FECHAMODIFICACION + ", " +
									  CenTipoColegiacionBean.C_IDCOLEGIACION + ", " +
									  CenTipoColegiacionBean.C_USUMODIFICACION +
						   " FROM " + CenTipoColegiacionBean.T_NOMBRETABLA;
			
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenTipoColegiacionBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			  CenTipoColegiacionBean.C_FECHAMODIFICACION + ", " +
			  CenTipoColegiacionBean.C_IDCOLEGIACION + ", " +
			  CenTipoColegiacionBean.C_USUMODIFICACION +
			  	" FROM " + CenTipoColegiacionBean.T_NOMBRETABLA;
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
