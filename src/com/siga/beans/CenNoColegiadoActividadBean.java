/*
 * VERSIONES:
 * FERNANDO.GOMEZ - 25-04-2008 - Creaci�n
 */

package com.siga.beans;


public class CenNoColegiadoActividadBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2495879153116119044L;
	/* Variables */
	private Long 	idPersona;
	private Integer	idInstitucion;
	private Integer idActividadProfesional;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_NOCOLEGIADO_ACTIVIDAD";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDACTIVIDADPROFESIONAL	= "IDACTIVIDADPROFESIONAL";

	// Metodos SET
	public void setIdPersona (Long id) { this.idPersona = id; }
	public void setIdInstitucion (Integer id) { this.idInstitucion = id; }
	public void setIdActividadProfesional (Integer id) { this.idActividadProfesional = id; }

	// Metodos GET
	public Long getIdPersona  ()	{ return this.idPersona; }
	public Integer getIdInstitucion  ()	{ return this.idInstitucion; }
	public Integer getIdActividadProfesional  ()	{ return this.idActividadProfesional; }
}