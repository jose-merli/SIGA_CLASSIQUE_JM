/*
 * VERSIONES:
 * 
 * miguel.villegas - 21-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSPRODUCTOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenTipoCambioBean extends MasterBean{

	/* Variables */
	private Integer idTipoCambio;
	private String 	descripcion;

	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOCAMBIO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOCAMBIO	= "IDTIPOCAMBIO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";	
	

	// Metodos SET
	public void setIdTipoCambio (Integer id) 	{ this.idTipoCambio = id; }
	public void setDescripcion (String s) 	{ this.descripcion = s; }
	

	// Metodos GET
	public Integer getIdTipoCambio () 	{ return this.idTipoCambio; }
	public String getDescripcion () 	{ return this.descripcion; }
	
}
