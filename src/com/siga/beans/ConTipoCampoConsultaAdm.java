/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del Bean de la tabla ConTablaConsulta
 */
public class ConTipoCampoConsultaAdm extends MasterBeanAdministrador {

	public ConTipoCampoConsultaAdm(UsrBean usuario)
	{
	    super(ConTipoCampoConsultaBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConTipoCampoConsultaBean.C_IDTIPOCAMPO,
				ConTipoCampoConsultaBean.C_DESCRIPCION,
				ConTipoCampoConsultaBean.C_IDBASE,
				ConTipoCampoConsultaBean.C_FECHAMODIFICACION,
				ConTipoCampoConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConTipoCampoConsultaBean.C_IDTIPOCAMPO};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ConTipoCampoConsultaBean bean = null;

		try
		{
			bean = new ConTipoCampoConsultaBean();
						
			bean.setIdTipoCampo(UtilidadesHash.getInteger(hash, ConTipoCampoConsultaBean.C_IDTIPOCAMPO));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConTipoCampoConsultaBean.C_DESCRIPCION));
			bean.setIdBase(UtilidadesHash.getInteger(hash, ConTipoCampoConsultaBean.C_IDBASE));
			bean.setFechaMod(UtilidadesHash.getString(hash, ConTipoCampoConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuMod(UtilidadesHash.getInteger(hash, ConTipoCampoConsultaBean.C_USUMODIFICACION));
		
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

			ConTipoCampoConsultaBean b = (ConTipoCampoConsultaBean) bean;

			UtilidadesHash.set(htData, ConTipoCampoConsultaBean.C_IDTIPOCAMPO, b.getIdTipoCampo());
			UtilidadesHash.set(htData, ConTipoCampoConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConTipoCampoConsultaBean.C_IDBASE, b.getIdBase());
			UtilidadesHash.set(htData, ConTipoCampoConsultaBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, ConTipoCampoConsultaBean.C_USUMODIFICACION, b.getUsuMod());
			
		}

		catch (Exception e)
		{
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}


}
