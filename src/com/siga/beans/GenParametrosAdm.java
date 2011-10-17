/*
 * Created on 20-dic-2004
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

/**
 * @author daniel.campos
 */

public class GenParametrosAdm extends MasterBeanAdministrador {

	public GenParametrosAdm(UsrBean usuario) {
		super(GenParametrosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean() {
		String [] campos = {GenParametrosBean.C_FECHAMODIFICACION, 	GenParametrosBean.C_IDINSTITUCION,
							GenParametrosBean.C_MODULO, 			GenParametrosBean.C_PARAMETRO,
							GenParametrosBean.C_USUMODIFICACION, 	GenParametrosBean.C_VALOR,
							GenParametrosBean.C_IDRECURSO};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {GenParametrosBean.C_MODULO,GenParametrosBean.C_PARAMETRO, GenParametrosBean.C_IDINSTITUCION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		GenParametrosBean bean = null;
		
		try {
			bean = new GenParametrosBean();
			bean.setFechaMod(UtilidadesHash.getString(hash, GenParametrosBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, GenParametrosBean.C_IDINSTITUCION));
			bean.setModulo(UtilidadesHash.getString(hash, GenParametrosBean.C_MODULO));
			bean.setParametro(UtilidadesHash.getString(hash, GenParametrosBean.C_PARAMETRO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, GenParametrosBean.C_USUMODIFICACION));
			bean.setValor(UtilidadesHash.getString(hash, GenParametrosBean.C_VALOR));
			bean.setIdRecurso(UtilidadesHash.getString(hash, GenParametrosBean.C_IDRECURSO));
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
			GenParametrosBean b = (GenParametrosBean) bean;
			UtilidadesHash.set(htData, GenParametrosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, GenParametrosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, GenParametrosBean.C_MODULO, b.getModulo());
			UtilidadesHash.set(htData, GenParametrosBean.C_PARAMETRO, b.getParametro());
			UtilidadesHash.set(htData, GenParametrosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, GenParametrosBean.C_VALOR, b.getValor());
			UtilidadesHash.set(htData, GenParametrosBean.C_IDRECURSO, b.getIdRecurso());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}
	
	public Hashtable getValores(String idModulo, String idParametro) throws ClsExceptions	{
		
		Hashtable htData = new Hashtable();
		
		try {
			Hashtable hashParams = new Hashtable();
			UtilidadesHash.set(hashParams, GenParametrosBean.C_MODULO, idModulo);
			UtilidadesHash.set(hashParams, GenParametrosBean.C_PARAMETRO, idParametro);
			Vector auxV = this.select(hashParams);
			
			if (auxV != null && auxV.size() > 0) {
				for (int i = 0; i < auxV.size(); i++) {
					GenParametrosBean registro = (GenParametrosBean) auxV.get(i);
					htData.put(registro.getIdInstitucion().toString(), registro.getValor());
				}
			}
			return htData;
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener los valores del parámetro " + idParametro);
		}
	}
	
	
	public String getValor(String idInstitucion, String idModulo, String idParametro, String valorDefecto) throws ClsExceptions 
	{
		Hashtable htData = new Hashtable();
		String salida = valorDefecto;
		try {
			htData = getValores(idModulo, idParametro);
			// ya tengo todos los path de las instituciones
			// miro si existe para la mia
			if (htData.containsKey(idInstitucion)) {
				salida = (String) htData.get(idInstitucion);
			} else {
				salida = (String) htData.get("0");
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener el valor del parámetro " + idParametro);
		}
		if (salida==null) {
			return valorDefecto;
		} else {
			return salida;
		}
	}
	
	public boolean tieneParametro(String idInstitucion, String idModulo, String idParametro) throws ClsExceptions 
	{
		Hashtable htData = new Hashtable();
		boolean salida = false;
		try {
		    Hashtable hashParams = new Hashtable();
			UtilidadesHash.set(hashParams, GenParametrosBean.C_MODULO, idModulo);
			UtilidadesHash.set(hashParams, GenParametrosBean.C_PARAMETRO, idParametro);
			UtilidadesHash.set(hashParams, GenParametrosBean.C_IDINSTITUCION, idInstitucion);
			Vector auxV = this.select(hashParams);
			if (auxV!=null && auxV.size()>0) {
				salida=true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al comprobar la existencia del parámetro " + idParametro + " para la institucion "+idInstitucion);
		}
		return salida;
	}
	
	public Vector getParametrosPorInstitucionMasGenerales (Integer idInstitucion, String idModulo)
	{
		Vector datos = new Vector();

		try { 
			RowsContainer rc = new RowsContainer(); 
			String sql = UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()) +
						 " WHERE " + GenParametrosBean.C_IDINSTITUCION + " = " + idInstitucion +
						   " AND " + GenParametrosBean.C_MODULO + " = '" + idModulo + "'" +
					 " UNION " + 
					     UtilidadesBDAdm.sqlSelect(this.nombreTabla, this.getCamposBean()) +					  
						 " WHERE " + GenParametrosBean.C_IDINSTITUCION + " = 0 " + // Intitucion general
						    " AND " + GenParametrosBean.C_MODULO + " = '" + idModulo + "'" +
							" AND " + GenParametrosBean.C_PARAMETRO + " NOT IN (SELECT " + GenParametrosBean.C_PARAMETRO + 
																				" FROM " + GenParametrosBean.T_NOMBRETABLA +
																			   " WHERE " + GenParametrosBean.C_IDINSTITUCION + " = " + idInstitucion + ")";
			
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					GenParametrosBean registro = (GenParametrosBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			return new Vector();
		}
		return datos;
	}
}
