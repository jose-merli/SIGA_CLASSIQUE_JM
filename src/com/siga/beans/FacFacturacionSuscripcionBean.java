/*
 * VERSIONES:
 * nuria.rgonzalez - 16-03-2005 - Creación
 */
package com.siga.beans;

public class FacFacturacionSuscripcionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoServicio, idServicio, idServiciosInstitucion, idSuscripcion, idFacturacionSuscripcion;	
	private Long 	numeroLinea;	
	private String 	idFactura, fechaInicio, fechaFin, descripcion;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_FACTURACIONSUSCRIPCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_IDFACTURA 					= "IDFACTURA";
	static public final String C_NUMEROLINEA 				= "NUMEROLINEA";
	static public final String C_IDTIPOSERVICIOS 			= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO 				= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION 	= "IDSERVICIOSINSTITUCION";
	static public final String C_IDSUSCRIPCION 				= "IDSUSCRIPCION";
	static public final String C_IDFACTURACIONSUSCRIPCION 	= "IDFACTURACIONSUSCRIPCION";
	static public final String C_FECHAINICIO 				= "FECHAINICIO";
	static public final String C_FECHAFIN 					= "FECHAFIN";
	static public final String C_DESCRIPCION 				= "C_DESCRIPCION";	

	// Metodos GET	
	public String getDescripcion() 				{return descripcion;}	
	public String getFechaFin() 				{return fechaFin;}
	public String getFechaInicio() 				{return fechaInicio;}
	public String getIdFactura() 				{return idFactura;}
	public Integer getIdFacturacionSuscripcion(){return idFacturacionSuscripcion;}
	public Integer getIdInstitucion() 			{return idInstitucion;}
	public Integer getIdServicio() 				{return idServicio;}
	public Integer getIdServiciosInstitucion() 	{return idServiciosInstitucion;}
	public Integer getIdSuscripcion() 			{return idSuscripcion;}
	public Integer getIdTipoServicio() 			{return idTipoServicio;}
	public Long getNumeroLinea() 				{return numeroLinea;}
	
	// Metodos SET	
	
	public void setDescripcion(String descripcion) 			{this.descripcion = descripcion;}
	public void setFechaFin(String fechaFin)				{this.fechaFin = fechaFin;}
	public void setFechaInicio(String fechaInicio) 			{this.fechaInicio = fechaInicio;}
	public void setIdFactura(String idFactura) 				{this.idFactura = idFactura;}
	public void setIdFacturacionSuscripcion(Integer idFacturacionSuscripcion) {this.idFacturacionSuscripcion = idFacturacionSuscripcion;}
	public void setIdInstitucion(Integer idInstitucion) 	{this.idInstitucion = idInstitucion;}
	public void setIdServicio(Integer idServicio) 			{this.idServicio = idServicio;}
	public void setIdServiciosInstitucion(Integer idServiciosInstitucion) {this.idServiciosInstitucion = idServiciosInstitucion;}
	public void setIdSuscripcion(Integer idSuscripcion) 	{this.idSuscripcion = idSuscripcion;}
	public void setIdTipoServicio(Integer idTipoServicio) 	{this.idTipoServicio = idTipoServicio;}
	public void setNumeroLinea(Long numeroLinea) 			{this.numeroLinea = numeroLinea;}
}
