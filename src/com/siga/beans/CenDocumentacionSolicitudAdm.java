/*
 * Created on 22-nov-2004
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
public class CenDocumentacionSolicitudAdm extends MasterBeanAdministrador{

	public CenDocumentacionSolicitudAdm(UsrBean usuario) 
	{
		super(CenDocumentacionSolicitudBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenDocumentacionSolicitudBean.C_DESCRIPCION,
							CenDocumentacionSolicitudBean.C_CODIGOEXT,
							CenDocumentacionSolicitudBean.C_FECHAMODIFICACION,
							CenDocumentacionSolicitudBean.C_IDDOCUMENTACION,
							CenDocumentacionSolicitudBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenDocumentacionSolicitudBean.C_IDDOCUMENTACION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] campos = {CenDocumentacionSolicitudBean.C_DESCRIPCION,
		        			CenDocumentacionSolicitudBean.C_IDDOCUMENTACION};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CenDocumentacionSolicitudBean bean = null;
		
		try {
			bean = new CenDocumentacionSolicitudBean();
			bean.setDescripcion(UtilidadesHash.getString(hash, CenDocumentacionSolicitudBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, CenDocumentacionSolicitudBean.C_CODIGOEXT));
			bean.setIdDocumentacion(UtilidadesHash.getInteger(hash, CenDocumentacionSolicitudBean.C_IDDOCUMENTACION));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDocumentacionSolicitudBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDocumentacionSolicitudBean.C_USUMODIFICACION));
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
			CenDocumentacionSolicitudBean b = (CenDocumentacionSolicitudBean) bean;
			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_DESCRIPCION, 		b.getDescripcion());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_CODIGOEXT, 		b.getCodigoExt());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_IDDOCUMENTACION, 	b.getIdDocumentacion());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenDocumentacionSolicitudBean.C_USUMODIFICACION, 	b.getUsuMod());
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenDocumentacionSolicitudBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " + 
									  CenDocumentacionSolicitudBean.C_CODIGOEXT + ", " + 
									  CenDocumentacionSolicitudBean.C_FECHAMODIFICACION + ", " + 
									  CenDocumentacionSolicitudBean.C_IDDOCUMENTACION + ", " +
									  CenDocumentacionSolicitudBean.C_USUMODIFICACION +
						   " FROM " + CenDocumentacionSolicitudBean.T_NOMBRETABLA + " ";
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenDocumentacionSolicitudBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " + 
			  CenDocumentacionSolicitudBean.C_CODIGOEXT + ", " + 
			  CenDocumentacionSolicitudBean.C_FECHAMODIFICACION + ", " + 
			  CenDocumentacionSolicitudBean.C_IDDOCUMENTACION + ", " +
			  CenDocumentacionSolicitudBean.C_USUMODIFICACION +
			  " FROM " + CenDocumentacionSolicitudBean.T_NOMBRETABLA + " ";
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
