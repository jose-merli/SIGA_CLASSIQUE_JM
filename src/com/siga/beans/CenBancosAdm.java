package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CenBancosAdm extends MasterBeanAdministrador
{
	public CenBancosAdm(UsrBean usuario)
	{
	    super(CenBancosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CenBancosBean.C_CODIGO,
							CenBancosBean.C_FECHAMODIFICACION,
							CenBancosBean.C_NOMBRE, 
							CenBancosBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CenBancosBean.C_CODIGO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		CenBancosBean bean = null;

		try
		{
			bean = new CenBancosBean();
			
			bean.setCodigo(UtilidadesHash.getString(hash, CenBancosBean.C_CODIGO));
			bean.setNombre(UtilidadesHash.getString(hash, CenBancosBean.C_NOMBRE));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenBancosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenBancosBean.C_USUMODIFICACION));
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

			CenBancosBean b = (CenBancosBean) bean;

			UtilidadesHash.set(htData, CenBancosBean.C_CODIGO, b.getCodigo());
			UtilidadesHash.set(htData, CenBancosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CenBancosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CenBancosBean.C_USUMODIFICACION, b.getUsuMod());
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
        return null;
    }
}