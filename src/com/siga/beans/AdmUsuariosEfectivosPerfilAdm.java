package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmUsuariosEfectivosPerfilAdm extends MasterBeanAdministrador
{
	public AdmUsuariosEfectivosPerfilAdm(UsrBean usuario)
	{
	    super(AdmUsuariosEfectivosPerfilBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO,
						   AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION,
						   AdmUsuariosEfectivosPerfilBean.C_IDROL,
						   AdmUsuariosEfectivosPerfilBean.C_IDPERFIL,
						   AdmUsuariosEfectivosPerfilBean.C_FECHAMODIFICACION, 
						   AdmUsuariosEfectivosPerfilBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO, AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION, AdmUsuariosEfectivosPerfilBean.C_IDROL, AdmUsuariosEfectivosPerfilBean.C_IDPERFIL};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO,
     		   			   AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION,
     		   			   AdmUsuariosEfectivosPerfilBean.C_IDROL,
     		   			   AdmUsuariosEfectivosPerfilBean.C_IDPERFIL,
						   AdmUsuariosEfectivosPerfilBean.C_FECHAMODIFICACION, 
     		   			   AdmUsuariosEfectivosPerfilBean.C_USUMODIFICACION};
		
		return campos;
    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmUsuariosEfectivosPerfilBean bean = null;

		try
		{
			bean = new AdmUsuariosEfectivosPerfilBean();
			
			bean.setIdUsuario(UtilidadesHash.getString(hash, AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO));
			bean.setIdInstitucion(UtilidadesHash.getString(hash, AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION));
			bean.setIdRol(UtilidadesHash.getString(hash, AdmUsuariosEfectivosPerfilBean.C_IDROL));
			bean.setIdPerfil(UtilidadesHash.getString(hash, AdmUsuariosEfectivosPerfilBean.C_IDPERFIL));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmUsuariosEfectivosPerfilBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmUsuariosEfectivosPerfilBean.C_USUMODIFICACION));
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

			AdmUsuariosEfectivosPerfilBean b = (AdmUsuariosEfectivosPerfilBean) bean;

			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmUsuariosEfectivosPerfilBean.C_USUMODIFICACION, b.getUsuMod());
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