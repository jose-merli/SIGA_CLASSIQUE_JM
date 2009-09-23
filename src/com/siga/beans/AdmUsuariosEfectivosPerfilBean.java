package com.siga.beans;

public class AdmUsuariosEfectivosPerfilBean extends MasterBean
{
	/* Variables */
	private String idUsuario;
	private String idInstitucion;
	private String idRol;
	private String idPerfil;

	/* Nombre campos de la tabla */
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDROL = "IDROL";
	static public final String C_IDPERFIL = "IDPERFIL";

	static public final String T_NOMBRETABLA = "ADM_USUARIOS_EFECTIVOS_PERFIL";
	
	// Métodos SET
	public void setIdUsuario (String idUsuario)
	{
		this.idUsuario = idUsuario;
	}
	
	public void setIdInstitucion (String idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

	public void setIdRol (String idRol)
	{
		this.idRol = idRol;
	}

	public void setIdPerfil (String idPerfil)
	{
		this.idPerfil = idPerfil;
	}

	// Métodos GET
	public String getIdUsuario ()
	{
		return idUsuario;
	}
	
	public String getIdInstitucion ()
	{
		return idInstitucion;
	}

	public String getIdRol ()
	{
		return idRol;
	}

	public String getIdPerfil ()
	{
		return idPerfil;
	}
}
