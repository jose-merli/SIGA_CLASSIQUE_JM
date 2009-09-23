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
 */
public class CenDocumentacionModalidadAdm extends MasterBeanAdministrador
{
	public CenDocumentacionModalidadAdm(UsrBean usuario) 
	{
		super(CenDocumentacionModalidadBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenDocumentacionModalidadBean.C_CODIGOEXT,			CenDocumentacionModalidadBean.C_DESCRIPCION,
							CenDocumentacionModalidadBean.C_FECHAMODIFICACION,	CenDocumentacionModalidadBean.C_IDINSTITUCION,
							CenDocumentacionModalidadBean.C_IDMODALIDAD,		CenDocumentacionModalidadBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenDocumentacionModalidadBean.C_IDINSTITUCION, CenDocumentacionModalidadBean.C_IDMODALIDAD};
		return claves;
	}

	protected String[] getOrdenCampos() {
		String [] campos = {CenDocumentacionModalidadBean.C_DESCRIPCION, CenDocumentacionModalidadBean.C_IDMODALIDAD};
		return campos;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions 
	{
		CenDocumentacionModalidadBean bean = null;
		
		try {
			bean = new CenDocumentacionModalidadBean();
			bean.setCodigoExt(UtilidadesHash.getString(hash, CenDocumentacionModalidadBean.C_CODIGOEXT));
			bean.setDescripcion(UtilidadesHash.getString(hash, CenDocumentacionModalidadBean.C_DESCRIPCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenDocumentacionModalidadBean.C_IDINSTITUCION));
			bean.setIdModalidad(UtilidadesHash.getInteger(hash, CenDocumentacionModalidadBean.C_IDMODALIDAD));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenDocumentacionModalidadBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenDocumentacionModalidadBean.C_USUMODIFICACION));
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
			CenDocumentacionModalidadBean b = (CenDocumentacionModalidadBean) bean;
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_CODIGOEXT, 			b.getCodigoExt());
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_DESCRIPCION,			b.getDescripcion());
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_IDMODALIDAD, 		b.getIdModalidad());
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenDocumentacionModalidadBean.C_USUMODIFICACION, 	b.getUsuMod());
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
			String sql = " SELECT " + CenDocumentacionModalidadBean.C_CODIGOEXT + ", " 
			                        + UtilidadesMultidioma.getCampoMultidioma(CenDocumentacionModalidadBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " 
									+ CenDocumentacionModalidadBean.C_FECHAMODIFICACION + ", " 
									+ CenDocumentacionModalidadBean.C_IDINSTITUCION + ", " 
									+ CenDocumentacionModalidadBean.C_IDMODALIDAD + ", " 
									+ CenDocumentacionModalidadBean.C_USUMODIFICACION +
						   " FROM " + CenDocumentacionModalidadBean.T_NOMBRETABLA;
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
			String sql = " SELECT " + CenDocumentacionModalidadBean.C_CODIGOEXT + ", " 
						            + UtilidadesMultidioma.getCampoMultidioma(CenDocumentacionModalidadBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " 
									+ CenDocumentacionModalidadBean.C_FECHAMODIFICACION + ", " 
									+ CenDocumentacionModalidadBean.C_IDINSTITUCION + ", " 
									+ CenDocumentacionModalidadBean.C_IDMODALIDAD + ", " 
									+ CenDocumentacionModalidadBean.C_USUMODIFICACION +
						   " FROM " + CenDocumentacionModalidadBean.T_NOMBRETABLA;
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
