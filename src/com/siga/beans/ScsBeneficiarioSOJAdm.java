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
public class ScsBeneficiarioSOJAdm extends MasterBeanAdministrador {

	/**
	 * @param usuario
	 */
	public ScsBeneficiarioSOJAdm(UsrBean usuario) {
		super(ScsBeneficiarioSOJBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsBeneficiarioSOJBean.C_IDINSTITUCION,
				            ScsBeneficiarioSOJBean.C_IDPERSONA,
							ScsBeneficiarioSOJBean.C_FECHAMODIFICACION,
							ScsBeneficiarioSOJBean.C_USUMODIFICACION,
							ScsBeneficiarioSOJBean.C_IDTIPOCONOCE,
							ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB,
							ScsBeneficiarioSOJBean.C_SOLICITAJG,
							ScsBeneficiarioSOJBean.C_SOLICITAINFOJG,
							ScsBeneficiarioSOJBean.C_NUMVECESSOJ
							};
		return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsBeneficiarioSOJBean.C_IDINSTITUCION,
				            ScsBeneficiarioSOJBean.C_IDPERSONA};
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
		ScsBeneficiarioSOJBean bean = null;
		try{
			bean = new ScsBeneficiarioSOJBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_IDPERSONA));
			bean.setIdTipoConoce(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_IDTIPOCONOCE));
			bean.setIdTipoGrupoLab(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB));
			bean.setSolicitaJG(UtilidadesHash.getString(hash, ScsBeneficiarioSOJBean.C_SOLICITAJG));
			bean.setSolicitaInfoSOJ(UtilidadesHash.getString(hash, ScsBeneficiarioSOJBean.C_SOLICITAINFOJG));
			bean.setNVecesSOJ(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_NUMVECESSOJ));
			bean.setFechaMod(UtilidadesHash.getString(hash, ScsBeneficiarioSOJBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ScsBeneficiarioSOJBean.C_USUMODIFICACION));
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
			ScsBeneficiarioSOJBean b = (ScsBeneficiarioSOJBean) bean;
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_IDPERSONA, b.getIdPersona());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_IDTIPOCONOCE, b.getIdTipoConoce());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_IDTIPOGRUPOLAB, b.getIdTipoGrupoLab());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_SOLICITAJG, b.getSolicitaJG());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_SOLICITAINFOJG, b.getSolicitaInfoSOJ());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_NUMVECESSOJ, b.getNVecesSOJ());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(hash, ScsBeneficiarioSOJBean.C_USUMODIFICACION, b.getUsuMod());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	

}
