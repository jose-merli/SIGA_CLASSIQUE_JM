/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;

public class PysTiposProductosAdm extends MasterBeanAdministrador {
	
	PysTiposProductosAdm (UsrBean usu) {
		super (PysTiposProductosBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {PysTiposProductosBean.C_IDTIPOPRODUCTO, 		
	    		PysTiposProductosBean.C_DESCRIPCION,
	    		PysTiposProductosBean.C_CODIGOEXT,
							PysTiposProductosBean.C_FECHAMODIFICACION,
							PysTiposProductosBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {PysTiposProductosBean.C_IDTIPOPRODUCTO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		PysTiposProductosBean bean = null;
		
		try {
			bean = new PysTiposProductosBean();
			bean.setIdTipoProducto	(UtilidadesHash.getInteger(hash, PysTiposProductosBean.C_IDTIPOPRODUCTO));
			bean.setDescripcion		(UtilidadesHash.getString(hash, PysTiposProductosBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, PysTiposProductosBean.C_CODIGOEXT));
			bean.setFechaMod		(UtilidadesHash.getString(hash, PysTiposProductosBean.C_FECHAMODIFICACION));
			bean.setUsuMod			(UtilidadesHash.getInteger(hash, PysTiposProductosBean.C_USUMODIFICACION));
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
			PysTiposProductosBean b = (PysTiposProductosBean) bean;
			UtilidadesHash.set(htData, PysTiposProductosBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData, PysTiposProductosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, PysTiposProductosBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, PysTiposProductosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, PysTiposProductosBean.C_USUMODIFICACION, b.getUsuMod());
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
	
	
}
