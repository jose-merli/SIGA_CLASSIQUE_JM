/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

/**
 * Administrador del bean de la tabla ConBaseConsulta
 */
public class ConBaseConsultaAdm extends MasterBeanAdministrador {
	
	public ConBaseConsultaAdm(UsrBean usuario)
	{
	    super(ConBaseConsultaBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConBaseConsultaBean.C_IDBASE,
				ConBaseConsultaBean.C_DESCRIPCION,
				ConBaseConsultaBean.C_NOMBRE,
				ConBaseConsultaBean.C_FECHAMODIFICACION,
				ConBaseConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConBaseConsultaBean.C_IDBASE};

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

		ConBaseConsultaBean bean = null;

		try
		{
			bean = new ConBaseConsultaBean();
						
			bean.setIdBase(UtilidadesHash.getInteger(hash, ConBaseConsultaBean.C_IDBASE));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConBaseConsultaBean.C_DESCRIPCION));
			bean.setNombre(UtilidadesHash.getString(hash, ConBaseConsultaBean.C_NOMBRE));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConBaseConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConBaseConsultaBean.C_USUMODIFICACION));
		
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

			ConBaseConsultaBean b = (ConBaseConsultaBean) bean;

			UtilidadesHash.set(htData, ConBaseConsultaBean.C_IDBASE, b.getIdBase());
			UtilidadesHash.set(htData, ConBaseConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConBaseConsultaBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, ConBaseConsultaBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConBaseConsultaBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

}
