/*
 * Created on 16-03-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.util.Hashtable;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * @author pilar.duran
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenEstadoActividadAdm extends MasterBeanAdministrador {

	public CenEstadoActividadAdm(UsrBean usu) {
		super(CenEstadoActividadBean.T_NOMBRETABLA, usu);
	}

	protected String[] getCamposBean() {
		String [] campos = {CenEstadoActividadBean.C_DESCRIPCION, 	CenEstadoActividadBean.C_FECHAMODIFICACION, 	CenEstadoActividadBean.C_CODIGOEXT,
				CenEstadoActividadBean.C_IDESTADO, CenEstadoActividadBean.C_USUMODIFICACION};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenEstadoActividadBean.C_IDESTADO};
		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenEstadoActividadBean bean = null;
		
		try {
			bean = new CenEstadoActividadBean();
			bean.setDescripcion((String)hash.get(CenEstadoActividadBean.C_DESCRIPCION));
			bean.setCodigoExt((String)hash.get(CenEstadoActividadBean.C_CODIGOEXT));
			bean.setFechaMod((String)hash.get(CenEstadoActividadBean.C_FECHAMODIFICACION));
			bean.setIdEstado(UtilidadesHash.getInteger(hash, CenEstadoActividadBean.C_IDESTADO));
			bean.setUsuMod((UtilidadesHash.getInteger(hash, CenEstadoActividadBean.C_USUMODIFICACION)));
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
			CenEstadoActividadBean b = (CenEstadoActividadBean) bean;
			UtilidadesHash.set(htData, CenEstadoActividadBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CenEstadoActividadBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, CenEstadoActividadBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenEstadoActividadBean.C_IDESTADO, String.valueOf(b.getIdEstado()));
			UtilidadesHash.set(htData, CenEstadoActividadBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
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
