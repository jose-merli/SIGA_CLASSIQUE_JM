/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class PysTiposProductosBean extends MasterBean{

	/* Variables */
	private Integer idTipoProducto;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_TIPOSPRODUCTOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOPRODUCTO	= "IDTIPOPRODUCTO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////

	// Metodos SET
	public void setIdTipoProducto (Integer id) 	{ this.idTipoProducto = id; }
	public void setDescripcion (String s)		{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdTipoProducto ()	{ return this.idTipoProducto; }
	public String  getDescripcion    ()	{ return this.descripcion; }
}
