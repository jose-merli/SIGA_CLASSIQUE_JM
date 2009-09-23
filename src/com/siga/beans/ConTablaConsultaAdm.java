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
public class ConTablaConsultaAdm extends MasterBeanAdministrador {

	public ConTablaConsultaAdm(UsrBean usuario)
	{
	    super(ConTablaConsultaBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConTablaConsultaBean.C_IDTABLA,
				ConTablaConsultaBean.C_DESCRIPCION,
				ConTablaConsultaBean.C_IDBASE,
				ConTablaConsultaBean.C_FECHAMODIFICACION,
				ConTablaConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConTablaConsultaBean.C_IDTABLA};

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

		ConTablaConsultaBean bean = null;

		try
		{
			bean = new ConTablaConsultaBean();
						
			bean.setIdTabla(UtilidadesHash.getInteger(hash, ConTablaConsultaBean.C_IDTABLA));
			bean.setDescripcion(UtilidadesHash.getString(hash, ConTablaConsultaBean.C_DESCRIPCION));
			bean.setIdBase(UtilidadesHash.getInteger(hash, ConTablaConsultaBean.C_IDBASE));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConTablaConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConTablaConsultaBean.C_USUMODIFICACION));
		
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

			ConTablaConsultaBean b = (ConTablaConsultaBean) bean;

			UtilidadesHash.set(htData, ConTablaConsultaBean.C_IDTABLA, b.getIdTabla());
			UtilidadesHash.set(htData, ConTablaConsultaBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ConTablaConsultaBean.C_IDBASE, b.getIdBase());
			UtilidadesHash.set(htData, ConTablaConsultaBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConTablaConsultaBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}


}
