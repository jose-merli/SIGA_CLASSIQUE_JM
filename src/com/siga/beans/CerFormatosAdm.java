package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CerFormatosAdm extends MasterBeanAdministrador
{
	public static String K_TODO_MAYUSCULAS="1";
	public static String K_PRIMERA_MAYUSCULA="2";
	public static String K_TODO_MINUSCULAS="3";
	public static String K_PRIMERAS_MAYUSCULAS="4";
	
	
	public CerFormatosAdm(UsrBean usuario)
	{
	    super(CerFormatosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerFormatosBean.C_IDFORMATO,
		        		   CerFormatosBean.C_DESCRIPCION,
		        		   CerFormatosBean.C_TIPOCAMPO,
		        		   CerFormatosBean.C_FORMATO};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerFormatosBean.C_IDFORMATO, CerFormatosBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerFormatosBean bean = null;

		try
		{
			bean = new CerFormatosBean();
			
			bean.setIdFormato(UtilidadesHash.getInteger(hash, CerFormatosBean.C_IDFORMATO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerFormatosBean.C_DESCRIPCION));
			bean.setTipoCampo(UtilidadesHash.getString(hash, CerFormatosBean.C_TIPOCAMPO));
			bean.setFormato(UtilidadesHash.getString(hash, CerFormatosBean.C_FORMATO));
			
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

			CerFormatosBean b = (CerFormatosBean) bean;

			UtilidadesHash.set(htData, CerFormatosBean.C_IDFORMATO, b.getIdFormato());
			UtilidadesHash.set(htData, CerFormatosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CerFormatosBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, CerFormatosBean.C_FORMATO, b.getFormato());
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