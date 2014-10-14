/*
 * VERSIONES:
 * MARIA JIMENEZ 13/10/2014 - Creación
 */
package com.siga.beans;

public class CenCargoBean extends MasterBean{

	/* Variables */
	private Integer idCargo;
	private String 	descripcion,codigoext;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_CARGO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDCARGO		= "IDCARGO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_CODIGOEXT		= "CODIGOEXT";

	// Metodos SET
	public void setIdCargo (Integer id) { this.idCargo = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }
	public void setCodigoext (String s)	{ this.codigoext = s; }
	// Metodos GET
	public Integer getIdCargo  ()	{ return this.idCargo; }
	public String getDescripcion()	{ return this.descripcion; }
	public String getCodigoext()	{ return this.codigoext; }
	
}
