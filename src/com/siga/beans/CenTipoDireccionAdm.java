/*
 * Created on 21-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
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
public class CenTipoDireccionAdm extends MasterBeanAdministrador {

	public CenTipoDireccionAdm(UsrBean usu) {
		super(CenTipoDireccionBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenTipoDireccionBean.C_DESCRIPCION, 	CenTipoDireccionBean.C_FECHAMODIFICACION,
				            CenTipoDireccionBean.C_IDTIPODIRECCION, CenTipoDireccionBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenTipoDireccionBean.C_IDTIPODIRECCION};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions{

		CenTipoDireccionBean bean = null;
		
		try {
			bean = new CenTipoDireccionBean();
			bean.setDescripcion((String)hash.get(CenTipoDireccionBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenTipoDireccionBean.C_FECHAMODIFICACION));
			bean.setIdTipoDireccion(UtilidadesHash.getInteger(hash, CenTipoDireccionBean.C_IDTIPODIRECCION));
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
			CenTipoDireccionBean b = (CenTipoDireccionBean) bean;
			htData.put(CenTipoDireccionBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenTipoDireccionBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenTipoDireccionBean.C_IDTIPODIRECCION, String.valueOf(b.getIdTipoDireccion()));
			htData.put(CenTipoDireccionBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenTipoDireccionBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
									  CenTipoDireccionBean.C_FECHAMODIFICACION + ", " +
									  CenTipoDireccionBean.C_IDTIPODIRECCION + ", " +
									  CenTipoDireccionBean.C_USUMODIFICACION +
						   " FROM " + CenTipoDireccionBean.T_NOMBRETABLA;
			sql += " WHERE " + CenTipoDireccionBean.C_IDTIPODIRECCION + " <= " + ClsConstants.TIPO_DIRECCION_TRASPASO_OJ;
			sql += " ORDER BY " + CenTipoDireccionBean.C_DESCRIPCION + " ASC ";
			
			//sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()): UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenTipoDireccionBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			CenTipoDireccionBean.C_FECHAMODIFICACION + ", " +
			CenTipoDireccionBean.C_IDTIPODIRECCION + ", " +
			CenTipoDireccionBean.C_USUMODIFICACION +
			  	" FROM " + CenTipoDireccionBean.T_NOMBRETABLA+
			  	"ORDER BY "+CenTipoDireccionBean.C_DESCRIPCION+" ASC ";
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
