/*
 * Created on Apr 20, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del bean de la tabla de módulos
 */
public class ConModuloAdm extends MasterBeanAdministrador {

	public ConModuloAdm(UsrBean usuario)
	{
	    super(ConModuloBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConModuloBean.C_IDMODULO,
				ConModuloBean.C_NOMBRE,
				ConModuloBean.C_FECHAMODIFICACION,
				ConModuloBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConModuloBean.C_IDMODULO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ConModuloBean bean = null;

		try
		{
			bean = new ConModuloBean();
						
			bean.setIdModulo(UtilidadesHash.getInteger(hash, ConModuloBean.C_IDMODULO));
			bean.setNombre(UtilidadesHash.getString(hash, ConModuloBean.C_NOMBRE));	
			bean.setFechaMod(UtilidadesHash.getString(hash, ConModuloBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConModuloBean.C_USUMODIFICACION));
		
		}

		catch (Exception e)
		{
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

		try
		{
			htData = new Hashtable();

			ConModuloBean b = (ConModuloBean) bean;

			UtilidadesHash.set(htData, ConModuloBean.C_IDMODULO, b.getIdModulo());
			UtilidadesHash.set(htData, ConModuloBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ConModuloBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConModuloBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	


}
