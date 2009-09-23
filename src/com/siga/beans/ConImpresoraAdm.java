/*
 * Created on May 19, 2005
 * @author juan.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del bean de la tabla de impresoras
 */
public class ConImpresoraAdm extends MasterBeanAdministrador {

	public ConImpresoraAdm(UsrBean usuario)
	{
	    super(ConImpresoraBean.T_NOMBRETABLA, usuario);
	}
	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConImpresoraBean.C_IDIMPRESORA,
		        ConImpresoraBean.C_IDINSTITUCION,
				ConImpresoraBean.C_NOMBREIMPRESORA,
				ConImpresoraBean.C_FECHAMODIFICACION,
				ConImpresoraBean.C_USUMODIFICACION};
				
			return campos;
	}	

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConImpresoraBean.C_IDINSTITUCION,
		        			ConImpresoraBean.C_IDIMPRESORA};

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
		
		ConImpresoraBean bean = null;

		try
		{
			bean = new ConImpresoraBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConImpresoraBean.C_IDINSTITUCION));
			bean.setIdImpresora(UtilidadesHash.getInteger(hash, ConImpresoraBean.C_IDIMPRESORA));	
			bean.setNombreImpresora(UtilidadesHash.getString(hash, ConImpresoraBean.C_NOMBREIMPRESORA));	
			bean.setFechaMod(UtilidadesHash.getString(hash, ConImpresoraBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConImpresoraBean.C_USUMODIFICACION));		
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

			ConImpresoraBean b = (ConImpresoraBean) bean;

			UtilidadesHash.set(htData, ConImpresoraBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConImpresoraBean.C_IDIMPRESORA, b.getIdImpresora());
			UtilidadesHash.set(htData, ConImpresoraBean.C_NOMBREIMPRESORA, b.getNombreImpresora());
			UtilidadesHash.set(htData, ConImpresoraBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConImpresoraBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
}
