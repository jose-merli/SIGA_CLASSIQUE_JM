package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;

public class AdmUsuariosAdm extends MasterBeanAdministrador
{
    public AdmUsuariosAdm(UsrBean usuario)
	{
	    super(AdmUsuariosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {AdmUsuariosBean.C_IDUSUARIO,
		        		   AdmUsuariosBean.C_DESCRIPCION,
		        		   AdmUsuariosBean.C_IDLENGUAJE,
		        		   AdmUsuariosBean.C_IDINSTITUCION,
		        		   AdmUsuariosBean.C_NIF,
		        		   AdmUsuariosBean.C_ACTIVO,
		        		   AdmUsuariosBean.C_FECHA_ALTA,
						   AdmUsuariosBean.C_FECHAMODIFICACION, 
						   AdmUsuariosBean.C_USUMODIFICACION,
						   AdmUsuariosBean.F_GRUPOS,
						   AdmUsuariosBean.C_CODIGOEXT};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {AdmUsuariosBean.C_IDUSUARIO, AdmUsuariosBean.C_IDINSTITUCION};

		return claves;
	}

    protected String[] getCamposActualizablesBean ()
    {
		String[] campos = {AdmUsuariosBean.C_IDUSUARIO,
     		   			   AdmUsuariosBean.C_DESCRIPCION,
     		   			   AdmUsuariosBean.C_IDLENGUAJE,
     		   			   AdmUsuariosBean.C_IDINSTITUCION,
     		   			   AdmUsuariosBean.C_NIF,
     		   			   AdmUsuariosBean.C_ACTIVO,
     		   			   AdmUsuariosBean.C_FECHA_ALTA,
     		   			   AdmUsuariosBean.C_FECHAMODIFICACION, 
     		   			   AdmUsuariosBean.C_USUMODIFICACION,
						   AdmUsuariosBean.C_CODIGOEXT};
		
		return campos;

    }

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    AdmUsuariosBean bean = null;

		try
		{
			bean = new AdmUsuariosBean();
			
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, AdmUsuariosBean.C_IDUSUARIO));
			bean.setDescripcion(UtilidadesHash.getString(hash, AdmUsuariosBean.C_DESCRIPCION));
			bean.setIdLenguaje(UtilidadesHash.getString(hash, AdmUsuariosBean.C_IDLENGUAJE));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, AdmUsuariosBean.C_IDINSTITUCION));
			bean.setNIF(UtilidadesHash.getString(hash, AdmUsuariosBean.C_NIF));
			bean.setActivo(UtilidadesHash.getString(hash, AdmUsuariosBean.C_ACTIVO));
			bean.setFechaAlta(UtilidadesHash.getString(hash, AdmUsuariosBean.C_FECHA_ALTA));
			bean.setGrupos(UtilidadesHash.getString(hash, AdmUsuariosBean.C_IDS_GRUPOS));
			bean.setFechaMod(UtilidadesHash.getString(hash, AdmUsuariosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, AdmUsuariosBean.C_USUMODIFICACION));
			bean.setCodigoExt(UtilidadesHash.getString(hash, AdmUsuariosBean.C_CODIGOEXT));
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

			AdmUsuariosBean b = (AdmUsuariosBean) bean;

			UtilidadesHash.set(htData, AdmUsuariosBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_IDLENGUAJE, b.getIdLenguaje());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_NIF, b.getNIF());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_ACTIVO, b.getActivo());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_FECHA_ALTA, b.getFechaAlta());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_IDS_GRUPOS, b.getGrupos());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, AdmUsuariosBean.C_CODIGOEXT, b.getCodigoExt());
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
        String[] ordenCampos = {AdmUsuariosBean.C_DESCRIPCION, AdmUsuariosBean.C_NIF};
        
        return ordenCampos;
    }
	
	public Vector getDatosUsuario(String idUsuario, String idInstitucion)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT U." + AdmUsuariosBean.C_DESCRIPCION + " NOMBRE_USUARIO, ");
		sql.append("        U." + AdmUsuariosBean.C_NIF + " NIF_USUARIO, ");
		sql.append("        U." + AdmUsuariosBean.C_CODIGOEXT + " CODIGOEXT, ");
		sql.append("        I." + CenInstitucionBean.C_NOMBRE + " NOMBRE_INSTITUCION, ");
		sql.append("        F_SIGA_ROLES_USUARIO(u." + AdmUsuariosBean.C_IDINSTITUCION + ", ");
		sql.append("                             u." + AdmUsuariosBean.C_IDUSUARIO + ") NOMBRE_GRUPO ");
		sql.append("   FROM " + AdmUsuariosBean.T_NOMBRETABLA + " u, " + CenInstitucionBean.T_NOMBRETABLA + " i ");
		sql.append("  WHERE " + "u." + AdmUsuariosBean.C_IDUSUARIO + " = " + idUsuario);
		sql.append("    AND " + "u." + AdmUsuariosBean.C_IDINSTITUCION + " = " + idInstitucion);
		sql.append("    AND " + "u." + AdmUsuariosBean.C_IDINSTITUCION + " = i." + CenInstitucionBean.C_IDINSTITUCION);

		try {
			return this.selectGenerico(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new Vector();
		}

	}
    
}