/*
 * VERSIONES:
 * 
 * miguel.villegas - 8-11-2004 - Creacion
 * Modificada el 12-9-2005 por david.sanchezp para incluir la clave nueva idinstitucion.
 */

/**
 * Clase que recoge y establece los valores del bean PYSPRODUCTOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class PysProductosBean extends MasterBean{

	/* Variables */
	private Integer idTipoProducto;
	private Long 	idProducto;
	private String 	descripcion;
	private PysTiposProductosBean tipoProducto;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PRODUCTOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPRODUCTO		= "IDPRODUCTO";
	static public final String C_IDTIPOPRODUCTO	= "IDTIPOPRODUCTO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";

	// Metodos SET
	public void setIdProducto (Long id) 	{ this.idProducto = id; }
	public void setIdTipoProducto (Integer id) 	{ this.idTipoProducto = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }
	public void setTipoProducto (PysTiposProductosBean b) { this.tipoProducto = b; }

	// Metodos GET
	public Long getIdProducto 	()	{ return this.idProducto; }
	public Integer getIdTipoProducto 	()	{ return this.idTipoProducto; }
	public String getDescripcion    ()	{ return this.descripcion; }
	public PysTiposProductosBean getTipoProducto    ()	{ return this.tipoProducto; }
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
}
