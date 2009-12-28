/*
 * Created on 17/09/2008
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author angel corral
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgRespuestaEJGRemesaAdm extends MasterBeanAdministrador {
	
	public CajgRespuestaEJGRemesaAdm (UsrBean usu) {
		super (CajgRespuestaEJGRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgRespuestaEJGRemesaBean.C_IDRESPUESTA,
				CajgRespuestaEJGRemesaBean.C_IDEJGREMESA,
				CajgRespuestaEJGRemesaBean.C_CODIGO,
				CajgRespuestaEJGRemesaBean.C_DESCRIPCION,
				CajgRespuestaEJGRemesaBean.C_ABREVIATURA,
				CajgRespuestaEJGRemesaBean.C_FECHA,
				CajgRespuestaEJGRemesaBean.C_FECHAMODIFICACION,	CajgRespuestaEJGRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgRespuestaEJGRemesaBean.C_IDRESPUESTA};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgRespuestaEJGRemesaBean bean = null;
		try{
			bean = new CajgRespuestaEJGRemesaBean();
			bean.setIdRespuesta(UtilidadesHash.getInteger(hash,CajgRespuestaEJGRemesaBean.C_IDRESPUESTA));
			bean.setIdEjgRemesa(UtilidadesHash.getInteger(hash,CajgRespuestaEJGRemesaBean.C_IDEJGREMESA));
			bean.setCodigo(UtilidadesHash.getString(hash,CajgRespuestaEJGRemesaBean.C_CODIGO));
			bean.setDescripcion(UtilidadesHash.getString(hash,CajgRespuestaEJGRemesaBean.C_DESCRIPCION));
			bean.setAbreviatura(UtilidadesHash.getString(hash,CajgRespuestaEJGRemesaBean.C_ABREVIATURA));
			bean.setFecha(UtilidadesHash.getString(hash,CajgRespuestaEJGRemesaBean.C_FECHA));
			
			bean.setFechaMod(UtilidadesHash.getString (hash,CajgRemesaBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CajgRemesaBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			CajgRespuestaEJGRemesaBean b = (CajgRespuestaEJGRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_IDRESPUESTA, b.getIdRespuesta());
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_IDEJGREMESA, b.getIdEjgRemesa());
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_ABREVIATURA, b.getAbreviatura());
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_FECHA, b.getFecha());			
			
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgRespuestaEJGRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	public int getNextVal() throws ClsExceptions {
		int nextVal = -1;
		String campo = CajgRespuestaEJGRemesaBean.C_IDRESPUESTA;
		String tabla = CajgRespuestaEJGRemesaBean.T_NOMBRETABLA;
		
		String sql = "SELECT NVL(MAX(" + campo + "), 0) AS " + campo +
				" FROM " + tabla;
		
		RowsContainer rc = new RowsContainer();

		if (rc.find(sql)) {
			Row r = (Row) rc.get(0);
			nextVal = Integer.parseInt(r.getString(campo));
			nextVal++;
		}
		return nextVal;
	}
	
}