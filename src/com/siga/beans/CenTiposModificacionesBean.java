/*
 * VERSIONES:
 * 
 * miguel.villegas - 19-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CENTIPOSMODIFICACIONES <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenTiposModificacionesBean extends MasterBean{

	/* Variables */
	private Integer idTipoModificacion;
	private String 	descripcion;		
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOSMODIFICACIONES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOMODIFICACION	= "IDTIPOMODIFICACION";
	static public final String C_DESCRIPCION	= "DESCRIPCION";		

	// Metodos SET
	public void setIdTipoModificacion (Integer id)	{ this.idTipoModificacion = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }		

	// Metodos GET
	public Integer getIdTipoModificacion ()	{ return this.idTipoModificacion; }
	public String getDescripcion ()	{ return this.descripcion; }	
	
}
