/*
 * VERSIONES:
 * 
 * daniel.casla	- 4-11-2004 - Inicio
 * miguel.villegas - 25-11-2004 - Continuacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSFORMAPAGOPRODUCTO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class PysFormaPagoProductoBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoProducto;
	private Long idProducto;
	private Long idProductoInstitucion;
	private Integer idFormaPago;
	private String 	internet;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_FORMAPAGOPRODUCTO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDTIPOPRODUCTO	= "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO	= "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION	= "IDPRODUCTOINSTITUCION";
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";
	static public final String C_INTERNET	= "INTERNET";

	// Metodos SET
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdTipoProducto (Integer id) 	{ this.idTipoProducto= id; }
	public void setIdProducto(Long id) 	{ this.idProducto= id; }
	public void setIdProductoInstitucion(Long id) 	{ this.idProductoInstitucion= id; }
	public void setIdFormaPago (Integer id) 	{ this.idFormaPago= id; }
	public void setInternet (String s)	{ this.internet = s; }

	// Metodos GET
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Integer getIdTipoProducto () 	{ return this.idTipoProducto; }
	public Long getIdProducto() 	{ return this.idProducto;}
	public Long getIdProductoInstitucion() 	{ return this.idProductoInstitucion;}
	public Integer getIdFormaPago () 	{ return this.idFormaPago;}
	public String getInternet ()	{ return this.internet;}
}
