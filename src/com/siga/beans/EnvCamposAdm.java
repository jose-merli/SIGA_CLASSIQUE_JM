package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class EnvCamposAdm extends MasterBeanAdministrador
{
	public static final String K_TIPOCAMPO_F="F";
	public static final String K_TIPOCAMPO_E="E";
	public static final String K_TIPOCAMPO_A="A";
	public static final String K_TIPOCAMPO_S="S";
	
	public EnvCamposAdm(UsrBean usuario)
	{
	    super(EnvCamposBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvCamposBean.C_IDCAMPO,
		        		   EnvCamposBean.C_TIPOCAMPO,
		        		   EnvCamposBean.C_NOMBRE,
		        		   EnvCamposBean.C_NOMBRESALIDA,
		        		   EnvCamposBean.C_CAPTURARDATOS,
		        		   EnvCamposBean.C_FECHAMODIFICACION, 
						   EnvCamposBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvCamposBean.C_IDCAMPO, EnvCamposBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvCamposBean bean = null;

		try
		{
			bean = new EnvCamposBean();
			
			bean.setIdCampo(UtilidadesHash.getInteger(hash, EnvCamposBean.C_IDCAMPO));
			bean.setTipoCampo(UtilidadesHash.getString(hash, EnvCamposBean.C_TIPOCAMPO));
			bean.setNombre(UtilidadesHash.getString(hash, EnvCamposBean.C_NOMBRE));
			bean.setNombreSalida(UtilidadesHash.getString(hash, EnvCamposBean.C_NOMBRESALIDA));
			bean.setCapturarDatos(UtilidadesHash.getString(hash, EnvCamposBean.C_CAPTURARDATOS));
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

			EnvCamposBean b = (EnvCamposBean) bean;

			UtilidadesHash.set(htData, EnvCamposBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, EnvCamposBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, EnvCamposBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, EnvCamposBean.C_NOMBRESALIDA, b.getNombreSalida());
			UtilidadesHash.set(htData, EnvCamposBean.C_CAPTURARDATOS, b.getCapturarDatos());
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