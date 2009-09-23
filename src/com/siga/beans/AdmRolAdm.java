package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmRolAdm extends MasterBeanAdministrador
{
	public AdmRolAdm(UsrBean usuario)
	{
	    super(AdmRolBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmRolBean.C_IDROL,
							AdmRolBean.C_CODIGOEXT,
							AdmRolBean.C_DESCRIPCION,
						   AdmRolBean.C_FECHAMODIFICACION, 
						   AdmRolBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmRolBean.C_IDROL};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmRolBean bean = null;

		try
		{
			bean = new AdmRolBean();
			
			bean.setIdRol(UtilidadesHash.getString(hash, AdmRolBean.C_IDROL));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmRolBean.C_DESCRIPCION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, AdmRolBean.C_CODIGOEXT));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmRolBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmRolBean.C_USUMODIFICACION));
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

			AdmRolBean b = (AdmRolBean) bean;

			UtilidadesHash.set(htData, AdmRolBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, AdmRolBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, AdmRolBean.C_CODIGOEXT, b.getCodigoExt());
			UtilidadesHash.set(htData, AdmRolBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmRolBean.C_USUMODIFICACION, b.getUsuMod());
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