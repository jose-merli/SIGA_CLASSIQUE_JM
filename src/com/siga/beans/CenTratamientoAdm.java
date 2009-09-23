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
public class CenTratamientoAdm extends MasterBeanAdministrador {

	public CenTratamientoAdm(UsrBean usu) {
		super(CenTratamientoBean.T_NOMBRETABLA, usu);
	}


	protected String[] getCamposBean() {
		String [] campos = {CenTratamientoBean.C_DESCRIPCION, 	CenTratamientoBean.C_FECHAMODIFICACION,CenTratamientoBean.C_CODIGOEXT,
							CenTratamientoBean.C_IDTRATAMIENTO,	CenTratamientoBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTratamientoBean.C_IDTRATAMIENTO};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenTratamientoBean bean = null;
		
		try {
			bean = new CenTratamientoBean();
			bean.setDescripcion((String)hash.get(CenTratamientoBean.C_DESCRIPCION));
			bean.setCodigoExt((String)hash.get(CenTratamientoBean.C_CODIGOEXT));
			bean.setFechaMod((String)hash.get(CenTratamientoBean.C_FECHAMODIFICACION));
			bean.setIdTratamiento(UtilidadesHash.getInteger(hash, CenTratamientoBean.C_IDTRATAMIENTO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenTratamientoBean.C_USUMODIFICACION));
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
			CenTratamientoBean b = (CenTratamientoBean) bean;
			htData.put(CenTratamientoBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTratamientoBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenTratamientoBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTratamientoBean.C_IDTRATAMIENTO, String.valueOf(b.getIdTratamiento()));
			htData.put(CenTratamientoBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
			String sql = " SELECT "+CenTratamientoBean.C_CODIGOEXT+", "+UtilidadesMultidioma.getCampoMultidioma(CenTratamientoBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+CenTratamientoBean.C_FECHAMODIFICACION+", "+CenTratamientoBean.C_IDTRATAMIENTO+", "+CenTratamientoBean.C_USUMODIFICACION +" FROM "+CenTratamientoBean.T_NOMBRETABLA;
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
			String sql = " SELECT "+CenTratamientoBean.C_CODIGOEXT+", "+UtilidadesMultidioma.getCampoMultidioma(CenTratamientoBean.C_DESCRIPCION,this.usrbean.getLanguage())+", "+CenTratamientoBean.C_FECHAMODIFICACION+", "+CenTratamientoBean.C_IDTRATAMIENTO+", "+CenTratamientoBean.C_USUMODIFICACION +" FROM "+CenTratamientoBean.T_NOMBRETABLA;
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
