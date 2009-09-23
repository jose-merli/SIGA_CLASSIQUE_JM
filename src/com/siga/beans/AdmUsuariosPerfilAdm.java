package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmUsuariosPerfilAdm extends MasterBeanAdministrador
{
	public AdmUsuariosPerfilAdm(UsrBean usuario)
	{
	    super(AdmUsuariosPerfilBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmUsuariosPerfilBean.C_IDUSUARIO,
		        		   AdmUsuariosPerfilBean.C_IDINSTITUCION,
		        		   AdmUsuariosPerfilBean.C_IDPERFIL};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmUsuariosPerfilBean.C_IDUSUARIO, AdmUsuariosPerfilBean.C_IDINSTITUCION, AdmUsuariosPerfilBean.C_IDPERFIL};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    AdmUsuariosPerfilBean bean = null;

		try
		{
			bean = new AdmUsuariosPerfilBean();
			
			bean.setUsuario(UtilidadesHash.getInteger(hash, AdmUsuariosPerfilBean.C_IDUSUARIO));
			bean.setInstitucion(UtilidadesHash.getInteger(hash, AdmUsuariosPerfilBean.C_IDINSTITUCION));
			bean.setPerfil(UtilidadesHash.getString(hash, AdmUsuariosPerfilBean.C_IDPERFIL));
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

			AdmUsuariosPerfilBean b = (AdmUsuariosPerfilBean) bean;

			UtilidadesHash.set(htData, AdmUsuariosPerfilBean.C_IDUSUARIO, b.getUsuario());
			UtilidadesHash.set(htData, AdmUsuariosPerfilBean.C_IDINSTITUCION, b.getInstitucion());
			UtilidadesHash.set(htData, AdmUsuariosPerfilBean.C_IDPERFIL, b.getPerfil());
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