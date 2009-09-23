package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class CerCamposCertificadosAdm extends MasterBeanAdministrador
{
	public static String T_ALFANUMERICO="A";
	public static String T_NUMERICO="N";
	public static String T_FECHA="F";
	
	public CerCamposCertificadosAdm(UsrBean usuario)
	{
	    super(CerCamposCertificadosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO,
		        		   CerCamposCertificadosBean.C_TIPOCAMPO,
		        		   CerCamposCertificadosBean.C_NOMBRE,
		        		   CerCamposCertificadosBean.C_NOMBRESALIDA,
		        		   CerCamposCertificadosBean.C_CAPTURARDATOS};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO, CerCamposCertificadosBean.C_TIPOCAMPO};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerCamposCertificadosBean bean = null;

		try
		{
			bean = new CerCamposCertificadosBean();
			
			bean.setIdCampoCertificado(UtilidadesHash.getInteger(hash, CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO));
			bean.setTipoCampo(UtilidadesHash.getString(hash, CerCamposCertificadosBean.C_TIPOCAMPO));
			bean.setNombre(UtilidadesHash.getString(hash, CerCamposCertificadosBean.C_NOMBRE));
			bean.setNombreSalida(UtilidadesHash.getString(hash, CerCamposCertificadosBean.C_NOMBRESALIDA));
			bean.setCapturarDatos(UtilidadesHash.getString(hash, CerCamposCertificadosBean.C_CAPTURARDATOS));
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

			CerCamposCertificadosBean b = (CerCamposCertificadosBean) bean;

			UtilidadesHash.set(htData, CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO, b.getIdCampoCertificado());
			UtilidadesHash.set(htData, CerCamposCertificadosBean.C_TIPOCAMPO, b.getTipoCampo());
			UtilidadesHash.set(htData, CerCamposCertificadosBean.C_NOMBRE, b.getNombre());
			UtilidadesHash.set(htData, CerCamposCertificadosBean.C_NOMBRESALIDA, b.getNombreSalida());
			UtilidadesHash.set(htData, CerCamposCertificadosBean.C_CAPTURARDATOS, b.getCapturarDatos());
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