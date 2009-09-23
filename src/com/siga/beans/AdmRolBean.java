package com.siga.beans;

public class AdmRolBean extends MasterBean
{
	/* Variables */
	private String idRol;
	private String codigoExt;
	private String descripcion;

	/* Nombre campos de la tabla */
	static public final String C_IDROL = "IDROL";
	static public final String C_CODIGOEXT = "CODIGOEXT";
	static public final String C_DESCRIPCION = "DESCRIPCION";

	static public final String T_NOMBRETABLA = "ADM_ROL";
	
	// Métodos SET
	public void setIdRol (String idRol)
	{
		this.idRol = idRol;
	}
	
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	
	public void setDescripcion (String descripcion)
	{
		this.descripcion = descripcion;
	}

	// Métodos GET
	public String getIdRol ()
	{
		return idRol;
	}
	
	// Métodos GET
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	
	public String getDescripcion ()
	{
		return descripcion;
	}
}
