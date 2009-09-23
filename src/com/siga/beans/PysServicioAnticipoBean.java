/*
 * VERSIONES:
 * 
 * jose.barrientos	- 28-11-2008 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSSERVICIOANTICIPO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;


public class PysServicioAnticipoBean extends MasterBean{
	
	/* Variables */
	private Integer idInstitucion, 	
					idAnticipo, 
					idTipoServicio,
					idServicio, 	
					idServiciosInstitucion;
	private Long 	idPersona;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_SERVICIOANTICIPO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDANTICIPO = "IDANTICIPO";
	static public final String C_IDTIPOSERVICIOS = "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO = "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION = "IDSERVICIOSINSTITUCION";
	
	/* Metodos SET */
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion= id; }
	public void setIdPersona (Long id) 		{ this.idPersona= id; }
	public void setIdAnticipo (Integer id) 		{ this.idAnticipo= id; }
	public void setIdTipoServicio (Integer id) 	{ this.idTipoServicio= id; }
	public void setIdServicio (Integer id) 		{ this.idServicio= id; }
	public void setIdServiciosInstitucion (Integer id) 	{ this.idServiciosInstitucion= id; }
	
	/* Metodos GET */
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdPersona () 		{ return this.idPersona; }
	public Integer getIdAnticipo () 	{ return this.idAnticipo; }
	public Integer getIdTipoServicio () { return this.idTipoServicio; }
	public Integer getIdServicio () 	{ return this.idServicio; }
	public Integer getIdServiciosInstitucion ()	{ return this.idServiciosInstitucion; }
}