/*
 * VERSIONES:
 * 
 * miguel.villegas - 19-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CENESTADOSOLICITUDESMODIF <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenEstadoSolicitudModifBean extends MasterBean{

	/* Variables */
	private Integer idEstadoSolic;
	private String 	descripcion;			
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ESTADOSOLICITUDMODIF";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADOSOLIC	= "IDESTADOSOLIC";
	static public final String C_DESCRIPCION	= "DESCRIPCION";

	// Metodos SET
	public void setIdEstadoSolic (Integer id)	{ this.idEstadoSolic = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }	

	// Metodos GET
	public Integer getIdEstadoSolic ()	{ return this.idEstadoSolic; }
	public String getDescripcion ()	{ return this.descripcion; }		
	
}
