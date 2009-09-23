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
public class ScsAcreditacionProcedimientoAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public ScsAcreditacionProcedimientoAdm(UsrBean usuario) {
		super(ScsAcreditacionProcedimientoBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsAcreditacionProcedimientoBean.C_FECHAMODIFICACION,
							ScsAcreditacionProcedimientoBean.C_IDACREDITACION,
							ScsAcreditacionProcedimientoBean.C_IDINSTITUCION,
							ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO,
							ScsAcreditacionProcedimientoBean.C_PORCENTAJE,
							ScsAcreditacionProcedimientoBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsAcreditacionProcedimientoBean.C_IDACREDITACION,
							ScsAcreditacionProcedimientoBean.C_IDINSTITUCION,
							ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO};
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
		ScsAcreditacionProcedimientoBean bean = null;
		try{
			bean = new ScsAcreditacionProcedimientoBean();
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsAcreditacionProcedimientoBean.C_FECHAMODIFICACION));
			bean.setIdAcreditacion(UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_IDACREDITACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_IDINSTITUCION));
			bean.setIdProcedimiento(UtilidadesHash.getString(hash, ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO));
			bean.setPorcentaje(UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_PORCENTAJE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_USUMODIFICACION));
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
			ScsAcreditacionProcedimientoBean b = (ScsAcreditacionProcedimientoBean) bean;
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_IDACREDITACION, b.getIdAcreditacion());
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO, b.getIdProcedimiento());
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_PORCENTAJE, b.getPorcentaje());
			UtilidadesHash.set(hash, ScsAcreditacionProcedimientoBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

}
