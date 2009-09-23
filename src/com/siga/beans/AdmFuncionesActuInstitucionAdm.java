/*
 * Created on Nov 18, 2004
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
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdmFuncionesActuInstitucionAdm extends MasterBeanAdministrador{

	/**
	 * @param tabla
	 * @param usuario
	 */
	public AdmFuncionesActuInstitucionAdm(String tabla, UsrBean usuario) {
		super(tabla, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String [] campos = {AdmFuncionesActuInstitucionBean.C_AUTOMATICO, 	AdmFuncionesActuInstitucionBean.C_FECHAMODIFICACION,
				AdmFuncionesActuInstitucionBean.C_IDFUNCION, AdmFuncionesActuInstitucionBean.C_IDINSTITUCION,
				AdmFuncionesActuInstitucionBean.C_USUMODIFICACION};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String [] claves = {AdmFuncionesActuInstitucionBean.C_IDFUNCION, AdmFuncionesActuInstitucionBean.C_IDINSTITUCION};
		return claves;
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

		AdmFuncionesActuInstitucionBean bean = null;
		
		try {
			bean = new AdmFuncionesActuInstitucionBean();
			bean.setAutomatico((String)hash.get(AdmFuncionesActuInstitucionBean.C_AUTOMATICO));
			bean.setFechaMod((String)hash.get(AdmFuncionesActuInstitucionBean.C_FECHAMODIFICACION));
			bean.setIdFuncion(UtilidadesHash.getInteger(hash, AdmFuncionesActuInstitucionBean.C_IDFUNCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmFuncionesActuInstitucionBean.C_IDINSTITUCION));
			bean.setUsuMod((UtilidadesHash.getInteger(hash, AdmFuncionesActuInstitucionBean.C_USUMODIFICACION)));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			AdmFuncionesActuInstitucionBean b = (AdmFuncionesActuInstitucionBean) bean;
			UtilidadesHash.set(htData, AdmFuncionesActuInstitucionBean.C_AUTOMATICO, b.getAutomatico());
			UtilidadesHash.set(htData, AdmFuncionesActuInstitucionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmFuncionesActuInstitucionBean.C_IDFUNCION, String.valueOf(b.getIdFuncion()));
			UtilidadesHash.set(htData, AdmFuncionesActuInstitucionBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			UtilidadesHash.set(htData, AdmFuncionesActuInstitucionBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

}
