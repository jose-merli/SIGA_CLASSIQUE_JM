package com.siga.beans;

import java.util.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmPerfilAdm extends MasterBeanAdministrador
{
	public AdmPerfilAdm(UsrBean usuario)
	{
	    super(AdmPerfilBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmPerfilBean.C_IDPERFIL,
						   AdmPerfilBean.C_DESCRIPCION,
						   AdmPerfilBean.C_NIVELPERFIL,
						   AdmPerfilBean.C_IDINSTITUCION,
						   AdmPerfilBean.C_FECHAMODIFICACION, 
						   AdmPerfilBean.C_USUMODIFICACION,
						   AdmPerfilBean.F_ROLES};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmPerfilBean.C_IDPERFIL, AdmPerfilBean.C_IDINSTITUCION};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmPerfilBean.C_IDPERFIL,
     		   			   AdmPerfilBean.C_DESCRIPCION,
     		   			   AdmPerfilBean.C_NIVELPERFIL,
     		   			   AdmPerfilBean.C_IDINSTITUCION,
     		   			   AdmPerfilBean.C_FECHAMODIFICACION, 
     		   			   AdmPerfilBean.C_USUMODIFICACION};
		
		return campos;
    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		AdmPerfilBean bean = null;

		try
		{
			bean = new AdmPerfilBean();
			
			bean.setIdPerfil(UtilidadesHash.getString(hash, AdmPerfilBean.C_IDPERFIL));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmPerfilBean.C_DESCRIPCION));
			bean.setNivelPerfil(UtilidadesHash.getInteger(hash, AdmPerfilBean.C_NIVELPERFIL));
			bean.setIdInstitucion(UtilidadesHash.getString(hash, AdmPerfilBean.C_IDINSTITUCION));
			bean.setRoles(UtilidadesHash.getString(hash, AdmPerfilBean.C_ROLES));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmPerfilBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmPerfilBean.C_USUMODIFICACION));
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

			AdmPerfilBean b = (AdmPerfilBean) bean;

			UtilidadesHash.set(htData, AdmPerfilBean.C_IDPERFIL, b.getIdPerfil());
			UtilidadesHash.set(htData, AdmPerfilBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, AdmPerfilBean.C_NIVELPERFIL, b.getNivelPerfil());
			UtilidadesHash.set(htData, AdmPerfilBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmPerfilBean.C_ROLES, b.getRoles());
			UtilidadesHash.set(htData, AdmPerfilBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmPerfilBean.C_USUMODIFICACION, b.getUsuMod());
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
    
    /*public boolean borrarRoles(String idInstitucion)
    {
        try
        {
            return true;
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }*/
}