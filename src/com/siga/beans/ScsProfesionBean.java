/*
 * VERSIONES:
 * 
 * miguel.villegas - 11-05-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean SCSPROFESION <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class ScsProfesionBean extends MasterBean{

	/* Variables */
	private Integer idProfesion;
	private String 	descripcion;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_PROFESION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPROFESION	= "IDPROFESION";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////
	
	// Metodos SET
	public void setIdProfesion (Integer id) 	{ this.idProfesion = id; }
	public void setDescripcion (String desc)	{ this.descripcion = desc; }	

	// Metodos GET
	public Integer getIdProfesion () 	{ return this.idProfesion; }
	public String  getDescripcion ()	{ return this.descripcion; }	
}
