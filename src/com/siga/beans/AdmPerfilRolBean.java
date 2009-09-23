package com.siga.beans;

public class AdmPerfilRolBean extends MasterBean
{
	/* Variables */
	private String idInstitucion;
	private String idPerfil;
	private String idRol;
	private String grupoPorDefecto;
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERFIL = "IDPERFIL";
	static public final String C_IDROL = "IDROL";
	static public final String C_GRUPO_POR_DEFECTO = "GRUPOPORDEFECTO";
	
	static public final String T_NOMBRETABLA = "ADM_PERFIL_ROL";
	
	// Métodos SET
	public void setIdInstitucion (String idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

	public void setIdPerfil (String idPerfil)
	{
		this.idPerfil = idPerfil;
	}

	public void setIdRol (String idRol)
	{
		this.idRol = idRol;
	}

	public void setGrupoPorDefecto (String grupoPorDefecto)
	{
		this.grupoPorDefecto = grupoPorDefecto;
	}

	// Métodos GET
	public String getIdInstitucion ()
	{
		return idInstitucion;
	}

	public String getIdPerfil ()
	{
		return idPerfil;
	}
	
	public String getIdRol ()
	{
		return idRol;
	}

	public String getGrupoPorDefecto ()
	{
		return grupoPorDefecto;
	}
}
