package com.siga.beans;

import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.*;
import com.siga.general.SIGAException;

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
    
    /**
     * Devuelve los roles y perfiles de un usuario
     *  
     * @param idInstitucion
     * @param idUsuario
     * @return
     * @throws ClsExceptions
     * @throws SIGAException
     */
    public Vector<HashMap<String, String>> getPerfilesRolesUsuarios(String idInstitucion, String idUsuario) throws ClsExceptions, SIGAException{
    	   	
		StringBuffer query = new StringBuffer();
		query.append("select rol.codigoext codrol, ");
		query.append(" (select wm_concat(uefepe.idperfil) ");
		query.append(" from adm_usuarios_efectivos_perfil uefepe ");
		query.append(" where uefepe.idinstitucion(+) = uefe.idinstitucion ");
		query.append(" and uefepe.idusuario(+) = uefe.idusuario ");
		query.append(" and uefepe.idrol = rol.idrol) perfiles");
		query.append(" from adm_usuario_efectivo uefe, adm_rol rol ");
		query.append(" where rol.idrol = uefe.idrol ");
		query.append(" and uefe.idinstitucion=" + idInstitucion);
		query.append(" and uefe.idusuario=" + idUsuario);
    	
    	Vector<HashMap<String, String>> perfiles=this.selectGenerico(query.toString());
    	
		return perfiles;
    } 
    
    /**
     * Inserta un nuevo perfil para un rol de un usuario
     * @author josebd
     * @param idUsuario
     * @param idInstitucion
     * @param codRol
     * @param codPerfil
     * @return
     * @throws ClsExceptions
     */
    public boolean setPerfilRolUsuario(String idUsuario, String idInstitucion, String codRol, String codPerfil) throws ClsExceptions{
    	StringBuffer sql = new StringBuffer();
    	sql.append("insert into adm_usuarios_efectivos_perfil (idusuario, idinstitucion, idrol, idperfil, usumodificacion, fechamodificacion) ");
    	sql.append("values ("+idUsuario+", "+idInstitucion+", (select idrol from adm_rol where codigoext='"+codRol+"'), '"+codPerfil+"', "+usrbean.getUserName()+", sysdate)");
    	return this.insertSQL(sql.toString());
    }
    
    /**
     * Borra un perfil para un rol de un usuario de la tabla adm_usuarios_efectivos_perfil, creando un historico en adm_usuarios_efectivos_hist con
     * los datos del registro borrado
     * @author josebd 
     * @param idUsuario
     * @param idInstitucion
     * @param codRol
     * @param codPerfil
     * @return
     * @throws ClsExceptions
     */
    public boolean deletePerfilRolUsuario(String idUsuario, String idInstitucion, String codRol, String codPerfil) throws ClsExceptions{
    	
    	StringBuffer sqlHistorico = new StringBuffer();
    	sqlHistorico.append("insert into adm_usuarios_efectivos_hist (idusuario, idinstitucion, idrol, idperfil, usucreacion, fechacreacion, usumodificacion, fechamodificacion) ");
    	sqlHistorico.append("(select idusuario, idinstitucion, idrol, idperfil, usumodificacion, fechamodificacion,"+usrbean.getUserName()+",sysdate from adm_usuarios_efectivos_perfil ");
    	sqlHistorico.append(" where idinstitucion = "+idInstitucion);
    	sqlHistorico.append("   and idusuario = "+idUsuario);
    	sqlHistorico.append("  and idperfil = '" +codPerfil+"'");
    	sqlHistorico.append("  and idrol = (select idrol from adm_rol where codigoext='"+codRol+"'))");
    	    	
    	StringBuffer sql = new StringBuffer();
    	sql.append("delete adm_usuarios_efectivos_perfil");
    	sql.append(" where idinstitucion = "+idInstitucion);
    	sql.append("   and idusuario = "+idUsuario);
    	sql.append("  and idperfil = '" +codPerfil+"'");
    	sql.append("  and idrol = (select idrol from adm_rol where codigoext='"+codRol+"')");
    	return this.insertSQL(sqlHistorico.toString()) && deleteSQL(sql.toString());
    }
    
}