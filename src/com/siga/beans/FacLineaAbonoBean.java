/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_LINEAABONO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class FacLineaAbonoBean extends MasterBean{

	/* Variables */
	private Long idAbono;
	private Integer idInstitucion;
	private Long numeroLinea;
	private String 	descripcionLinea;	
	private Integer	cantidad;
	private Double precioUnitario;
	private Float iva;
	private String idFactura;
	private Long lineaFactura;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_LINEAABONO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDABONO		= "IDABONO";	
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_NUMEROLINEA	= "NUMEROLINEA";	
	static public final String C_DESCRIPCIONLINEA= "DESCRIPCIONLINEA";
	static public final String C_CANTIDAD		= "CANTIDAD";
	static public final String C_PRECIOUNITARIO	= "PRECIOUNITARIO";	
	static public final String C_IVA			= "IVA";		
	static public final String C_IDFACTURA			= "IDFACTURA";		
	static public final String C_LINEAFACTURA			= "LINEAFACTURA";		

	// Metodos SET
	public void setIdAbono (Long id)	{ this.idAbono = id; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setNumeroLinea (Long n) 	{ this.numeroLinea = n; }
	public void setDescripcionLinea (String desc) 	{ this.descripcionLinea = desc; }
	public void setCantidad (Integer cant) 	{ this.cantidad = cant; }
	public void setPrecioUnitario (Double precio)	{ this.precioUnitario = precio; }
	public void setIva (Float iva) 	{ this.iva = iva; }	
	public void setLineaFactura (Long valor) 	{ this.lineaFactura = valor; }
	public void setIdFactura (String valor) 	{ this.idFactura = valor; }

	// Metodos GET
	public Long getIdAbono ()	{ return this.idAbono; }
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getNumeroLinea () 	{ return this.numeroLinea; }
	public String getDescripcionLinea () 	{ return this.descripcionLinea; }
	public Integer getCantidad () 	{ return this.cantidad; }
	public Double getPrecioUnitario ()	{ return this.precioUnitario; }
	public Float getIva () 	{ return this.iva; }	
	public Long getLineaFactura () 	{ return this.lineaFactura; }
	public String getIdFactura () 	{ return this.idFactura; }
}
