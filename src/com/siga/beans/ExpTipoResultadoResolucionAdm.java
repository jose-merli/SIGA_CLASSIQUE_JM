package com.siga.beans;

import java.util.Hashtable;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.atos.utils.UsrBean;


/**
 * Administrador del bean de Tipo Resultado Resolucion
 */
public class ExpTipoResultadoResolucionAdm extends MasterBeanAdministrador {
	
	public ExpTipoResultadoResolucionAdm(UsrBean usuario)
	{
	    super(ExpExpedienteBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ExpTipoResultadoResolucionBean.C_IDTIPORESULTADO,
			ExpTipoResultadoResolucionBean.C_IDINSTITUCION,
			ExpTipoResultadoResolucionBean.C_DESCRIPCION,
			ExpTipoResultadoResolucionBean.C_FECHAMODIFICACION,
			ExpTipoResultadoResolucionBean.C_USUMODIFICACION,			
			ExpTipoResultadoResolucionBean.C_CODEXT};

		return campos;
	}

	
	protected String[] getClavesBean() {
		
		String[] claves = {ExpTipoResultadoResolucionBean.C_IDTIPORESULTADO, 
				ExpTipoResultadoResolucionBean.C_IDINSTITUCION};

		return claves;
	}	 
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ExpTipoResultadoResolucionBean bean = null;

		try
		{
			bean = new ExpTipoResultadoResolucionBean();
						
			bean.setIdTipoResultado(UtilidadesHash.getInteger(hash, ExpTipoResultadoResolucionBean.C_IDTIPORESULTADO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ExpTipoResultadoResolucionBean.C_IDINSTITUCION));
			bean.setDescripcion(UtilidadesHash.getString(hash, ExpTipoResultadoResolucionBean.C_DESCRIPCION));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ExpTipoResultadoResolucionBean.C_FECHAMODIFICACION));
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ExpTipoResultadoResolucionBean.C_USUMODIFICACION));
			bean.setCodExt(UtilidadesHash.getString(hash, ExpTipoResultadoResolucionBean.C_CODEXT));
			
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
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ExpTipoResultadoResolucionBean b = (ExpTipoResultadoResolucionBean) bean;

			
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_IDTIPORESULTADO, b.getIdTipoResultado());
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_USUMODIFICACION, b.getUsuModificacion());
			UtilidadesHash.set(htData, ExpTipoResultadoResolucionBean.C_CODEXT, b.getCodExt());

		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}


	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}