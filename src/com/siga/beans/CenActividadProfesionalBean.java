/*
 * VERSIONES:
 * FERNANDO.GOMEZ - 25-04-2008 - Creación
 */
package com.siga.beans;

public class CenActividadProfesionalBean extends MasterBean{

	/* Variables */
	private Integer idActividadProfesional;
	private String 	descripcion,codigoext;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ACTIVIDADPROFESIONAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDACTIVIDADPROFESIONAL		= "IDACTIVIDADPROFESIONAL";
	static public final String C_DESCRIPCION				= "DESCRIPCION";
	static public final String C_CODIGOEXT					= "CODIGOEXT";

	// Metodos SET
	public void setIdActividadProfesional (Integer id) { this.idActividadProfesional = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }
	public void setCodigoext (String s)	{ this.codigoext = s; }
	// Metodos GET
	public Integer getIdActividadProfesional  ()	{ return this.idActividadProfesional; }
	public String getDescripcion()	{ return this.descripcion; }
	public String getCodigoext()	{ return this.codigoext; }
	
}
