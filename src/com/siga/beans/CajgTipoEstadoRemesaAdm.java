/*
 * Created on 17/09/2008
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
 * @author fernando.gomez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajgTipoEstadoRemesaAdm extends MasterBeanAdministrador {

	public CajgTipoEstadoRemesaAdm (UsrBean usu) {
		super (CajgTipoEstadoRemesaBean.T_NOMBRETABLA, usu);
	}	


	protected String[] getCamposBean() {
		String [] campos = {CajgTipoEstadoRemesaBean.C_IDESTADO, CajgTipoEstadoRemesaBean.C_DESCRIPCION,
							CajgTipoEstadoRemesaBean.C_FECHAMODIFICACION,	CajgTipoEstadoRemesaBean.C_USUMODIFICACION};
		return campos;
	}


	protected String[] getClavesBean() {
		String[] campos = { CajgTipoEstadoRemesaBean.C_IDESTADO};
		return campos;
	}


	protected String[] getOrdenCampos() {
		return getClavesBean();
	}

	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		CajgTipoEstadoRemesaBean bean = null;
		try{
			bean = new CajgTipoEstadoRemesaBean();
			
			bean.setIdEstado(UtilidadesHash.getInteger(hash,CajgTipoEstadoRemesaBean.C_IDESTADO));
			bean.setDescripcion(UtilidadesHash.getString(hash,CajgTipoEstadoRemesaBean.C_DESCRIPCION));
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
			CajgTipoEstadoRemesaBean b = (CajgTipoEstadoRemesaBean) bean;
			
			UtilidadesHash.set(hash, CajgTipoEstadoRemesaBean.C_IDESTADO, b.getIdEstado());
			UtilidadesHash.set(hash, CajgTipoEstadoRemesaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(hash, CajgTipoEstadoRemesaBean.C_FECHAMODIFICACION, b.getFechaMod());	
			UtilidadesHash.set(hash, CajgTipoEstadoRemesaBean.C_USUMODIFICACION, b.getUsuMod());	
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}
	
	
}