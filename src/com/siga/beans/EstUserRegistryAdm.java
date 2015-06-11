package com.siga.beans;

/**
 * @author Carlos Ruano Martínez 
 * @date 10/06/2015
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Administrador del bean de la tabla de EstUserRegistryAdm
 */
public class EstUserRegistryAdm extends MasterBeanAdministrador {

	public EstUserRegistryAdm(UsrBean usuario) {
		super(EstUserRegistryBean.T_NOMBRETABLA, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {

		String[] campos = { EstUserRegistryBean.C_IDUSUARIO, EstUserRegistryBean.C_IDPERFIL, EstUserRegistryBean.C_FECHAREGISTRO, EstUserRegistryBean.C_IDINSTITUCION };

		return campos;
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	@Override
	protected String[] getClavesBean() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	@Override
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		EstUserRegistryBean bean = null;
		try {
			bean = new EstUserRegistryBean();
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, EstUserRegistryBean.C_IDUSUARIO));
			bean.setIdPerfil(UtilidadesHash.getString(hash, EstUserRegistryBean.C_IDPERFIL));
			bean.setFechaRegistro(UtilidadesHash.getString(hash, EstUserRegistryBean.C_FECHAREGISTRO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EstUserRegistryBean.C_IDINSTITUCION));

		}catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			EstUserRegistryBean b = (EstUserRegistryBean) bean;
			UtilidadesHash.set(htData, EstUserRegistryBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, EstUserRegistryBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, EstUserRegistryBean.C_FECHAREGISTRO, b.getFechaRegistro());
			UtilidadesHash.set(htData, EstUserRegistryBean.C_IDINSTITUCION, b.getIdInstitucion());

		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e, "Error al crear el hashTable a partir del bean");
		}
		return htData;
	}

	public boolean insertarRegistroUser(EstUserRegistryBean bean) {
		try {
			if (!this.insert(bean)) {
				throw new SIGAException("Error al realizar el insert en EstUserRegistry");
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
