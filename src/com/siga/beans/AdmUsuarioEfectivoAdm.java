package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmUsuarioEfectivoAdm extends MasterBeanAdministrador
{
	public AdmUsuarioEfectivoAdm(UsrBean usuario)
	{
	    super(AdmUsuarioEfectivoBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmUsuarioEfectivoBean.C_IDUSUARIO,
		        		   AdmUsuarioEfectivoBean.C_IDINSTITUCION,
		        		   AdmUsuarioEfectivoBean.C_IDROL,
		        		   AdmUsuarioEfectivoBean.C_NUMSERIE,
						   AdmUsuarioEfectivoBean.C_FECHAMODIFICACION, 
						   AdmUsuarioEfectivoBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmUsuarioEfectivoBean.C_IDUSUARIO, AdmUsuarioEfectivoBean.C_IDINSTITUCION, AdmUsuarioEfectivoBean.C_IDROL};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmUsuarioEfectivoBean.C_IDUSUARIO,
     		   			   AdmUsuarioEfectivoBean.C_IDINSTITUCION,
     		   			   AdmUsuarioEfectivoBean.C_IDROL,
     		   			   AdmUsuarioEfectivoBean.C_NUMSERIE,
     		   			   AdmUsuarioEfectivoBean.C_FECHAMODIFICACION, 
     		   			   AdmUsuarioEfectivoBean.C_USUMODIFICACION};
		
		return campos;

    }

	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    AdmUsuarioEfectivoBean bean = null;

		try
		{
			bean = new AdmUsuarioEfectivoBean();
			
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, AdmUsuarioEfectivoBean.C_IDUSUARIO));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmUsuarioEfectivoBean.C_IDINSTITUCION));
			bean.setIdRol(UtilidadesHash.getString(hash, AdmUsuarioEfectivoBean.C_IDROL));
			bean.setNumSerie(UtilidadesHash.getString(hash, AdmUsuarioEfectivoBean.C_NUMSERIE));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmUsuarioEfectivoBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmUsuarioEfectivoBean.C_USUMODIFICACION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			AdmUsuarioEfectivoBean b = (AdmUsuarioEfectivoBean) bean;

			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_IDROL, b.getIdRol());
			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_NUMSERIE, b.getNumSerie());
			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmUsuarioEfectivoBean.C_USUMODIFICACION, b.getUsuMod());
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
        String[] ordenCampos = {AdmUsuarioEfectivoBean.C_IDUSUARIO, AdmUsuarioEfectivoBean.C_NUMSERIE};
        
        return ordenCampos;
    }
}