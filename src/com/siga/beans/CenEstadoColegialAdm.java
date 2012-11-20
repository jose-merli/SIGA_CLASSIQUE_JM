/*
 * Created on 25-oct-2004
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
public class CenEstadoColegialAdm extends MasterBeanAdministrador {

	public CenEstadoColegialAdm(UsrBean usu) {
		super(CenEstadoColegialBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenEstadoColegialBean.C_DESCRIPCION, 	CenEstadoColegialBean.C_FECHAMODIFICACION,
							CenEstadoColegialBean.C_IDESTADO, CenEstadoColegialBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenEstadoColegialBean.C_IDESTADO};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenEstadoColegialBean bean = null;
		
		try {
			bean = new CenEstadoColegialBean();
			bean.setDescripcion((String)hash.get(CenEstadoColegialBean.C_DESCRIPCION));
			bean.setFechaMod((String)hash.get(CenEstadoColegialBean.C_FECHAMODIFICACION));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, CenEstadoColegialBean.C_IDESTADO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenEstadoColegialBean.C_USUMODIFICACION));
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
			CenEstadoColegialBean b = (CenEstadoColegialBean) bean;
			htData.put(CenEstadoColegialBean.C_DESCRIPCION, b.getDescripcion());
			htData.put(CenEstadoColegialBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(CenEstadoColegialBean.C_IDESTADO, String.valueOf(b.getIdEstado()));
			htData.put(CenEstadoColegialBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenEstadoColegialBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " + 
									  CenEstadoColegialBean.C_FECHAMODIFICACION + ", " +
									  CenEstadoColegialBean.C_IDESTADO + ", " +
									  CenEstadoColegialBean.C_USUMODIFICACION + " " +
						   " FROM " + CenEstadoColegialBean.T_NOMBRETABLA;
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
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CenEstadoColegialBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " + 
			  CenEstadoColegialBean.C_FECHAMODIFICACION + ", " +
			  CenEstadoColegialBean.C_IDESTADO + ", " +
			  CenEstadoColegialBean.C_USUMODIFICACION + " " +
			  	" FROM " + CenEstadoColegialBean.T_NOMBRETABLA;
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
	public Vector getEstadoColegial (String idInstitucion, String idPersona, String idioma) throws ClsExceptions  
	{
		try {

			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;
			StringBuffer sql = new StringBuffer();
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" select d.idestado ,f_siga_getrecurso(e.descripcion,:");
			sql.append(keyContador);
			sql.append(") ESTADO_COLEGIAL ");
			sql.append(", TO_CHAR(d.fechaestado, 'dd-mm-yyyy') FECHA_ESTADO_COLEGIAL ");
			sql.append(" from cen_datoscolegialesestado d,cen_estadocolegial e ");
			sql.append(" where d.idestado = e.idestado ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" and d.idinstitucion = :");
			sql.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" and d.idpersona = :");
			sql.append(keyContador);
			sql.append(" and d.fechaestado =  ");
			sql.append(" (select max(fechaestado) ");
			sql.append(" from cen_datoscolegialesestado where ");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" idinstitucion = :");
			sql.append(keyContador);
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" and idpersona = :");
			sql.append(keyContador);
			sql.append(" AND trunc(fechaestado) <= trunc(sysdate)) ");
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre el estado colegial");
		}
	}

		public Vector getEstadosColegiales (String idInstitucion, String idPersona, String idioma) throws ClsExceptions {
		Vector datos = new Vector();
		
		try {
			String sql = " SELECT f_siga_getrecurso(ec.descripcion, " + idioma + ") ESTADO_DESCRIPCION, "
					+ " to_char(dce.fechaestado, 'dd/mm/yyyy') ESTADO_FECHA, "
					+ " dce.observaciones ESTADO_OBSERVACIONES "
					+ " from cen_datoscolegialesestado dce, "
					+ " cen_estadocolegial ec "
					+ " where dce.idestado = ec.idestado "
					+ " and dce.idinstitucion = " + idInstitucion
					+ " and dce.idpersona = " + idPersona
					+ " ORDER BY dce.fechaestado DESC";
					
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}					
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre estados colegiales");
		}
		
		return datos;
	}
}
