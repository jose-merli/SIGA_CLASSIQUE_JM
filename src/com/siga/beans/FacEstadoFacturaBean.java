/*
 * VERSIONES:
 * 
 * 31/01/2007 Creacion RGG AtosOrigin 
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_ESTADOCONFIRMFACT <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicha tabla 
 */
package com.siga.beans;

public class FacEstadoFacturaBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5693487041363786446L;
	static public final String ID_ESTADO_PAGADA		= "1";
	/* Variables */
	private Integer idEstado;
	private String 	descripcion;
	private String lenguaje;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_ESTADOFACTURA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADO		= "IDESTADO";
	static public final String C_DESCRIPCION		= "DESCRIPCION";	
	static public final String C_LENGUAJE	= "LENGUAJE";

	// Metodos SET
	public void setIdEstado (Integer id) 	{ this.idEstado = id; }
	public void setDescripcion (String s) 	{ this.descripcion = s; }
	public void setLenguaje (String s) 	{ this.lenguaje = s; }
	
	// Metodos GET
	public Integer getIdEstado () 	{ return this.idEstado; }
	public String getDescripcion () 	{ return this.descripcion; }
	public String getLenguaje () 	{ return this.lenguaje; }
}
