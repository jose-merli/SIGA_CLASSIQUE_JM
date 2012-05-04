/*
 * Created on Dec 1, 2004
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
import com.siga.general.SIGAException;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenPaisAdm extends MasterBeanAdministrador {

	/**
	 * @param tabla
	 * @param usuario
	 */
	public CenPaisAdm(UsrBean usu){
		super(CenPaisBean.T_NOMBRETABLA, usu);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {CenPaisBean.C_FECHAMODIFICACION, CenPaisBean.C_IDPAIS, CenPaisBean.C_CODIGOEXT,				
					CenPaisBean.C_NOMBRE,	CenPaisBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {CenPaisBean.C_IDPAIS};
		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenPaisBean bean = null;
		
		try {
			bean = new CenPaisBean();
			bean.setFechaMod((String)hash.get(CenPaisBean.C_FECHAMODIFICACION));			
			bean.setIdPais((String)hash.get(CenPaisBean.C_IDPAIS));		
			bean.setCodigoExt((String)hash.get(CenPaisBean.C_CODIGOEXT));		
			bean.setNombre((String)hash.get(CenPaisBean.C_NOMBRE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenPaisBean.C_USUMODIFICACION));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {

		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenPaisBean b = (CenPaisBean) bean;
			htData.put(CenPaisBean.C_FECHAMODIFICACION, b.getFechaMod());		
			htData.put(CenPaisBean.C_IDPAIS, b.getIdPais());
			htData.put(CenPaisBean.C_CODIGOEXT, b.getCodigoExt());
			htData.put(CenPaisBean.C_NOMBRE, b.getNombre());
			htData.put(CenPaisBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	public String getDescripcion(String id) throws ClsExceptions,SIGAException {
		String salida="";
		
		// Acceso a BBDD	
		try {
			Hashtable ht = new Hashtable();
			ht.put(CenPaisBean.C_IDPAIS,id);
			Vector v = this.selectByPK(ht);
			if (v!=null && v.size()>0) {
				CenPaisBean b = (CenPaisBean) v.get(0);
				salida = b.getNombre();
			}			
		}	
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en getDescripcion()");
		}
		return salida;	
	}
	
	public Vector select(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenPaisBean.C_CODIGOEXT + ", " +
									  CenPaisBean.C_FECHAMODIFICACION + ", " +
									  CenPaisBean.C_IDPAIS + ", " +
									  UtilidadesMultidioma.getCampoMultidioma(CenPaisBean.C_NOMBRE, this.usrbean.getLanguage()) + ", " +
									  CenPaisBean.C_USUMODIFICACION + 
							" FROM " + CenPaisBean.T_NOMBRETABLA;
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
			String sql = " SELECT " + CenPaisBean.C_CODIGOEXT + ", " +
			  CenPaisBean.C_FECHAMODIFICACION + ", " +
			  CenPaisBean.C_IDPAIS + ", " +
			  UtilidadesMultidioma.getCampoMultidioma(CenPaisBean.C_NOMBRE, this.usrbean.getLanguage()) + ", " +
			  CenPaisBean.C_USUMODIFICACION + 
			  " FROM " + CenPaisBean.T_NOMBRETABLA;
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

	public String getCodigoExt(String idPais) throws ClsExceptions {
		String codigoExt="";
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + CenPaisBean.C_CODIGOEXT + " FROM " + CenPaisBean.T_NOMBRETABLA;
			sql += "  where idpais = " + idPais;
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				CenPaisBean registro = (CenPaisBean) this.hashTableToBeanInicial(fila.getRow()); 
				if (registro != null) 
					codigoExt = registro.getCodigoExt();
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return codigoExt;
	}	
}
