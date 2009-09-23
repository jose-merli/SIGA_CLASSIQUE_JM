/*
 * Created on 01-feb-2006
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
 * @author s230298
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsJurisdiccionAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	ScsJurisdiccionAdm(UsrBean usuario) {
		super( ScsJurisdiccionBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsJurisdiccionBean.C_DESCRIPCION,
				ScsJurisdiccionBean.C_FECHAMODIFICACION,
				ScsJurisdiccionBean.C_CODIGOEXT,
							ScsJurisdiccionBean.C_IDJURISDICCION,
							ScsJurisdiccionBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsJurisdiccionBean.C_IDJURISDICCION};
		return campos;
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
		ScsJurisdiccionBean bean = null;
		try{
			bean = new ScsJurisdiccionBean();
			bean.setDescripcion(UtilidadesHash.getString(hash, ScsJurisdiccionBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, ScsJurisdiccionBean.C_CODIGOEXT));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsJurisdiccionBean.C_FECHAMODIFICACION));
			bean.setIdJurisdiccion(UtilidadesHash.getInteger(hash, ScsJurisdiccionBean.C_IDJURISDICCION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ScsJurisdiccionBean.C_USUMODIFICACION));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsJurisdiccionBean b = (ScsJurisdiccionBean) bean;
			UtilidadesHash.set(hash, ScsJurisdiccionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, ScsJurisdiccionBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(hash, ScsJurisdiccionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsJurisdiccionBean.C_IDJURISDICCION, b.getIdJurisdiccion());
			UtilidadesHash.set(hash, ScsJurisdiccionBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
}
