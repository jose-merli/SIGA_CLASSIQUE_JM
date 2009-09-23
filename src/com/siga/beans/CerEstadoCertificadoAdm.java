package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CerEstadoCertificadoAdm extends MasterBeanAdministrador
{
	public CerEstadoCertificadoAdm(UsrBean usuario)
	{
	    super(CerEstadoCertificadoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO,
		        		   CerEstadoCertificadoBean.C_DESCRIPCION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerEstadoCertificadoBean bean = null;

		try
		{
			bean = new CerEstadoCertificadoBean();

			bean.setIdEstadoCertificado(UtilidadesHash.getInteger(hash, CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerEstadoCertificadoBean.C_DESCRIPCION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del Hashtable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CerEstadoCertificadoBean b = (CerEstadoCertificadoBean) bean;

			UtilidadesHash.set(htData, CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO, b.getIdEstadoCertificado());
			UtilidadesHash.set(htData, CerEstadoCertificadoBean.C_DESCRIPCION, b.getDescripcion());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el Hashtable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }
}