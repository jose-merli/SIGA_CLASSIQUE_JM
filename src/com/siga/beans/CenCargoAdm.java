/*
 * VERSIONES:
 * FERNANDO.GOMEZ - 25-04-2008 - Creación
 */

package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;


public class CenCargoAdm extends MasterBeanAdministrador {
	
	public CenCargoAdm (UsrBean usu) {
		super (CenCargoBean.T_NOMBRETABLA, usu);
	}
	
	protected String[] getCamposBean() {
		String [] campos = {CenCargoBean.C_IDCARGO, 		
							CenCargoBean.C_DESCRIPCION,
							CenCargoBean.C_CODIGOEXT,
							CenCargoBean.C_FECHAMODIFICACION,
							CenCargoBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenCargoBean.C_IDCARGO};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenCargoBean bean = null;
		
		try {
			bean = new CenCargoBean();
			bean.setIdCargo(UtilidadesHash.getInteger(hash, CenCargoBean.C_IDCARGO));
			bean.setDescripcion (UtilidadesHash.getString(hash, CenCargoBean.C_DESCRIPCION));
			bean.setCodigoext (UtilidadesHash.getString(hash, CenCargoBean.C_CODIGOEXT));
			bean.setFechaMod	  (UtilidadesHash.getString(hash, CenCargoBean.C_FECHAMODIFICACION));
			bean.setUsuMod		  (UtilidadesHash.getInteger(hash, CenCargoBean.C_USUMODIFICACION));
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
			CenCargoBean b = (CenCargoBean) bean;
			UtilidadesHash.set(htData, CenCargoBean.C_IDCARGO, b.getIdCargo());
			UtilidadesHash.set(htData, CenCargoBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CenCargoBean.C_CODIGOEXT, b.getCodigoext());
			UtilidadesHash.set(htData, CenCargoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenCargoBean.C_USUMODIFICACION, b.getUsuMod());
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
