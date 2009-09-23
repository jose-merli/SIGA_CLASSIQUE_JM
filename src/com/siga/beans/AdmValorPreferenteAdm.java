package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
//import com.siga.general.SIGAException;

public class AdmValorPreferenteAdm extends MasterBeanAdministrador
{
	public AdmValorPreferenteAdm(UsrBean usuario)
	{
	    super(AdmValorPreferenteBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmValorPreferenteBean.C_IDINSTITUCION,
						   AdmValorPreferenteBean.C_IDBOTON,
						   AdmValorPreferenteBean.C_CAMPO,
						   AdmValorPreferenteBean.C_VALOR};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmValorPreferenteBean.C_IDINSTITUCION,AdmValorPreferenteBean.C_VALOR,AdmValorPreferenteBean.C_CAMPO};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmValorPreferenteBean.C_IDINSTITUCION,
				   			AdmValorPreferenteBean.C_IDBOTON,
							AdmValorPreferenteBean.C_CAMPO,
							AdmValorPreferenteBean.C_VALOR};
		
		return campos;
    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmValorPreferenteBean bean = null;

		try
		{
			bean = new AdmValorPreferenteBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmValorPreferenteBean.C_IDINSTITUCION));
			bean.setIdBoton(UtilidadesHash.getInteger(hash, AdmValorPreferenteBean.C_IDBOTON));
			bean.setCampo(UtilidadesHash.getString(hash, AdmValorPreferenteBean.C_CAMPO));
			bean.setValor(UtilidadesHash.getString(hash, AdmValorPreferenteBean.C_VALOR));
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

			AdmValorPreferenteBean b = (AdmValorPreferenteBean) bean;

			UtilidadesHash.set(htData, AdmValorPreferenteBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmValorPreferenteBean.C_IDBOTON, b.getIdBoton());
			UtilidadesHash.set(htData, AdmValorPreferenteBean.C_CAMPO, b.getCampo());
			UtilidadesHash.set(htData, AdmValorPreferenteBean.C_VALOR, b.getValor());
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