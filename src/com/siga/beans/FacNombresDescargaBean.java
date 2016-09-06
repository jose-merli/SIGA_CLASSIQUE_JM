/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class FacNombresDescargaBean extends MasterBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2655791590606811864L;

	/* Variables */
	private Integer id;
	
	private String 	nombre;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "fac_nombres_descarga_fac";
	
	/* Nombre campos de la tabla */
	static public final String C_ID 	= "ID";
	static public final String C_NOMBRE 	= "NOMBRE";
	
	// Metodos SET
	public void setId (Integer id)		{ this.id = id; }
	public void setNombre (String d)			{ this.nombre = d; }
	
	
	// Metodos GET
	public Integer getId	()	{ return this.id; }
	public String getNombre		()	{ return this.nombre; }
}
