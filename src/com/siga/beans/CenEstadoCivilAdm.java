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
import com.siga.Utilidades.UtilidadesMultidioma;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenEstadoCivilAdm extends MasterBeanAdministrador {

	public CenEstadoCivilAdm(UsrBean usu) {
		super(CenEstadoCivilBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenEstadoCivilBean.C_DESCRIPCION, 	CenEstadoCivilBean.C_FECHAMODIFICACION, 	CenEstadoCivilBean.C_CODIGOEXT,
							CenEstadoCivilBean.C_IDESTADO, CenEstadoCivilBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenEstadoCivilBean.C_IDESTADO};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenEstadoCivilBean bean = null;
		
		try {
			bean = new CenEstadoCivilBean();
			bean.setDescripcion((String)hash.get(CenEstadoCivilBean.C_DESCRIPCION));
			bean.setCodigoExt((String)hash.get(CenEstadoCivilBean.C_CODIGOEXT));
			bean.setFechaMod((String)hash.get(CenEstadoCivilBean.C_FECHAMODIFICACION));
			bean.setIdEstadoCivil(UtilidadesHash.getInteger(hash, CenEstadoCivilBean.C_IDESTADO));
			bean.setUsuMod((UtilidadesHash.getInteger(hash, CenEstadoCivilBean.C_USUMODIFICACION)));
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
			CenEstadoCivilBean b = (CenEstadoCivilBean) bean;
			UtilidadesHash.set(htData, CenEstadoCivilBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CenEstadoCivilBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, CenEstadoCivilBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenEstadoCivilBean.C_IDESTADO, String.valueOf(b.getIdEstadoCivil()));
			UtilidadesHash.set(htData, CenEstadoCivilBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
			String sql = " SELECT " + CenEstadoCivilBean.C_CODIGOEXT + ", " + 
									  UtilidadesMultidioma.getCampoMultidioma(CenEstadoCivilBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
									  CenEstadoCivilBean.C_FECHAMODIFICACION + ", " +
									  CenEstadoCivilBean.C_IDESTADO + ", " +
									  CenEstadoCivilBean.C_USUMODIFICACION + " " +
						   " FROM " + CenEstadoCivilBean.T_NOMBRETABLA;

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
			String sql = " SELECT " + CenEstadoCivilBean.C_CODIGOEXT + ", " + 
			  UtilidadesMultidioma.getCampoMultidioma(CenEstadoCivilBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			  CenEstadoCivilBean.C_FECHAMODIFICACION + ", " +
			  CenEstadoCivilBean.C_IDESTADO + ", " +
			  CenEstadoCivilBean.C_USUMODIFICACION + " " +
			  " FROM " + CenEstadoCivilBean.T_NOMBRETABLA;
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
