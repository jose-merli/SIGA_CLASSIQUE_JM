/*
 * VERSIONES:
 * nuria.rgonzalez - 16-03-2005 - Creación
 */
package com.siga.beans;

public class FacLineaFacturaBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, cantidad, idFormaPago;	
	private Long 	numeroLinea, numeroOrden;
	private Double 	importeAnticipado, precioUnitario;
	private Float 	iva;	
	private String 	idFactura, descripcion, ctaProductoServicio, ctaIva;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_LINEAFACTURA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDFACTURA 				= "IDFACTURA";
	static public final String C_NUMEROLINEA 			= "NUMEROLINEA";
	static public final String C_NUMEROORDEN 			= "NUMEROORDEN";
	static public final String C_CANTIDAD 				= "CANTIDAD";
	static public final String C_IMPORTEANTICIPADO 		= "IMPORTEANTICIPADO";
	static public final String C_DESCRIPCION 			= "DESCRIPCION";
	static public final String C_PRECIOUNITARIO 		= "PRECIOUNITARIO";
	static public final String C_IVA 					= "IVA";
	static public final String C_CTAPRODUCTOSERVICIO	= "CTAPRODUCTOSERVICIO";
	static public final String C_CTAIVA 				= "CTAIVA";
	static public final String C_IDFORMAPAGO			= "IDFORMAPAGO";
	
//	 	Metodos GET	
	public Integer getCantidad() 						       {return cantidad;}	
	public String getDescripcion() 							   {return descripcion;}	
	public String getIdFactura() 							   {return idFactura;}	
	public Integer getIdInstitucion() 						   {return idInstitucion;}	
	public Double getImporteAnticipado()					   {return importeAnticipado;}	
	public Float getIva() 									   {return iva;}	
	public Long getNumeroLinea()							   {return numeroLinea;}	
	public Long getNumeroOrden() 							   {return numeroOrden;}	
	public Double getPrecioUnitario() 						   {return precioUnitario;}
	public String getCtaProductoServicio() 					   {return ctaProductoServicio;}	
	public String getCtaIva() 								   {return ctaIva;}	
	public Integer getIdFormaPago() 						   {return idFormaPago;}

	//		Metodos SET	
	public void setCantidad(Integer cantidad) 				   {this.cantidad = cantidad;}	
	public void setDescripcion(String descripcion) 			   {this.descripcion = descripcion;}	
	public void setIdFactura(String idFactura) 				   {this.idFactura = idFactura;}	
	public void setIdInstitucion(Integer idInstitucion) 	   {this.idInstitucion = idInstitucion;}	
	public void setImporteAnticipado(Double importeAnticipado) {this.importeAnticipado = importeAnticipado;}
	public void setIva(Float iva) 							   {this.iva = iva;}	
	public void setNumeroLinea(Long numeroLinea) 			   {this.numeroLinea = numeroLinea;}	
	public void setNumeroOrden(Long numeroOrden) 			   {this.numeroOrden = numeroOrden;}	
	public void setPrecioUnitario(Double precioUnitario)	   {this.precioUnitario = precioUnitario;	}
	public void setCtaProductoServicio(String valor) 		   {this.ctaProductoServicio = valor;}	
	public void setCtaIva(String valor) 				       {this.ctaIva = valor;}	
	public void setIdFormaPago(Integer idFormaPago) 		   {this.idFormaPago = idFormaPago;}
}
