/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class FacTiposProduIncluEnFactuBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoProducto, idProducto;
	
	private Long idSerieFacturacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_TIPOSPRODUINCLUENFACTU";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION = "IDSERIEFACTURACION";
	static public final String C_IDTIPOPRODUCTO 	= "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO 	= "IDPRODUCTO";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
	public void setIdTipoProducto (Integer id)		{ this.idTipoProducto = id; }
	public void setIdProducto (Integer id)		{ this.idProducto = id; }
	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
	public Integer getIdTipoProducto		()	{ return this.idTipoProducto; }
	public Integer getIdProducto		()	{ return this.idProducto; }
}
