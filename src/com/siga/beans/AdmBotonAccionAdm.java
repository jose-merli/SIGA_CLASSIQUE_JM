package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmBotonAccionAdm extends MasterBeanAdministrador
{
    public AdmBotonAccionAdm(UsrBean usuario)
	{
	    super(AdmBotonAccionBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmBotonAccionBean.C_IDINSTITUION ,
		        		   AdmBotonAccionBean.C_IDBOTON,
		        		   AdmBotonAccionBean.C_ACTIVO,
		        		   AdmBotonAccionBean.C_DESCRIPCION,
		        		   AdmBotonAccionBean.C_MODAL,
		        		   AdmBotonAccionBean.C_MODO,
		        		   AdmBotonAccionBean.C_NOMBREBOTON,
		        		   AdmBotonAccionBean.C_NOMBREPARAMETRO,
		        		   AdmBotonAccionBean.C_TRANSACCION,
		        		   AdmBotonAccionBean.C_VALORPARAMETRO,
						   AdmBotonAccionBean.C_FECHAMODIFICACION, 
						   AdmBotonAccionBean.C_USUMODIFICACION
						   };

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmBotonAccionBean.C_IDINSTITUION, AdmBotonAccionBean.C_IDBOTON};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmBotonAccionBean.C_IDINSTITUION ,
     		   AdmBotonAccionBean.C_IDBOTON,
    		   AdmBotonAccionBean.C_ACTIVO,
    		   AdmBotonAccionBean.C_DESCRIPCION,
    		   AdmBotonAccionBean.C_MODAL,
    		   AdmBotonAccionBean.C_ACTIVO,
    		   AdmBotonAccionBean.C_MODO,
    		   AdmBotonAccionBean.C_NOMBREBOTON,
    		   AdmBotonAccionBean.C_NOMBREPARAMETRO,
    		   AdmBotonAccionBean.C_TRANSACCION,
    		   AdmBotonAccionBean.C_VALORPARAMETRO,
			   AdmBotonAccionBean.C_FECHAMODIFICACION, 
			   AdmBotonAccionBean.C_USUMODIFICACION};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    AdmBotonAccionBean bean = null;

		try
		{
			bean = new AdmBotonAccionBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmBotonAccionBean.C_IDINSTITUION));
			bean.setIdBoton(UtilidadesHash.getInteger(hash, AdmBotonAccionBean.C_IDBOTON));
			bean.setActivo(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_ACTIVO));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_DESCRIPCION));
			bean.setModal(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_MODAL));
			bean.setModo(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_MODO));
			bean.setNombreBoton(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_NOMBREBOTON));
			bean.setParametro(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_NOMBREPARAMETRO));
			bean.setTransaccion(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_TRANSACCION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmBotonAccionBean.C_USUMODIFICACION));
			bean.setValorParametro(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_VALORPARAMETRO));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmBotonAccionBean.C_FECHAMODIFICACION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			AdmBotonAccionBean b = (AdmBotonAccionBean) bean;

			UtilidadesHash.set(htData, AdmBotonAccionBean.C_IDINSTITUION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_IDBOTON, b.getidBoton());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_ACTIVO, b.getActivo());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_MODAL, b.getModal());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_MODO, b.getModo());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_NOMBREBOTON, b.getNombreBoton());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_NOMBREPARAMETRO, b.getParametro());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_TRANSACCION, b.getTransaccion());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_VALORPARAMETRO, b.getValorParametro());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmBotonAccionBean.C_USUMODIFICACION, b.getUsuMod());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        String[] ordenCampos = {AdmBotonAccionBean.C_DESCRIPCION};
        
        return ordenCampos;
    }
}