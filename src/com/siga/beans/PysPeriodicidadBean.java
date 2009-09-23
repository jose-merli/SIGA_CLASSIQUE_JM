/*
 * VERSIONES:
 * miguel.villegas - 4-2-2005 - Creación
 */

package com.siga.beans;


public class PysPeriodicidadBean extends MasterBean{

	/* Variables */
	private Integer idPeriodicidad;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PERIODICIDAD";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERIODICIDAD	= "IDPERIODICIDAD";
	static public final String C_DESCRIPCION		= "DESCRIPCION";

	// Metodos SET
	public void setIdPeriodicidad (Integer id) 	{ this.idPeriodicidad = id; }
	public void setDescripcion (String s)			{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdPeriodicidad 	()	{ return this.idPeriodicidad; }
	public String  getDescripcion    	()	{ return this.descripcion; }
}
