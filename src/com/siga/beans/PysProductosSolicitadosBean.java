/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 27-01-2005 - Inicio
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 * modified by miguel.villegas (anhade 2 campos)
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysProductosSolicitadosBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion, idInstitucionOrigen, idTipoProducto, idCuenta, idFormaPago, cantidad, idTipoEnvios, metodoSolicitud;
	private Long 	idProducto, idProductoInstitucion, idPeticion, idPersona, idDireccion;
	private String 	aceptado,noFacturable, fechaSolicitud;	
	private Double 	valor;
	private Float 	iva;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PRODUCTOSSOLICITADOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION        	= "IDINSTITUCION";
	static public final String C_IDINSTITUCIONORIGEN	= "IDINSTITUCIONORIGEN";
	static public final String C_IDTIPOPRODUCTO			= "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO				= "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION	= "IDPRODUCTOINSTITUCION";
	static public final String C_IDPETICION				= "IDPETICION";
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDCUENTA				= "IDCUENTA";
	static public final String C_IDFORMAPAGO			= "IDFORMAPAGO";
	static public final String C_CANTIDAD				= "CANTIDAD";
	static public final String C_ACEPTADO				= "ACEPTADO";
	static public final String C_VALOR					= "VALOR";
	static public final String C_PORCENTAJEIVA			= "PORCENTAJEIVA";
	static public final String C_IDTIPOENVIOS			= "IDTIPOENVIOS";
	static public final String C_IDDIRECCION			= "IDDIRECCION";
	static public final String C_NOFACTURABLE			= "NOFACTURABLE";
	static public final String C_FECHASOLICITUD			= "FECHARECEPCIONSOLICITUD";
	static public final String C_METODOSOLICITUD		= "METODORECEPCIONSOLICITUD";
	
	// Metodos SET	
	public void setNoFacturable(String noFacturable) 		{this.noFacturable = noFacturable;}
	public void setAceptado(String aceptado) 				{this.aceptado = aceptado;}
	public void setCantidad(Integer cantidad) 				{this.cantidad = cantidad;}
	public void setIdCuenta(Integer idCuenta) 				{this.idCuenta = idCuenta;}
	public void setIdFormaPago(Integer idFormaPago) 		{this.idFormaPago = idFormaPago;}
	public void setIdInstitucion(Integer idInstitucion) 	{this.idInstitucion = idInstitucion;}
	public void setIdInstitucionOrigen(Integer idInstitucionOrigen) 	{this.idInstitucionOrigen = idInstitucionOrigen;}
	public void setIdPersona(Long idPersona) 				{this.idPersona = idPersona;}
	public void setIdPeticion(Long idPeticion) 				{this.idPeticion = idPeticion;}
	public void setIdProducto(Long idProducto) 				{this.idProducto = idProducto;}
	public void setIdProductoInstitucion(Long idProductoInstitucion) {this.idProductoInstitucion = idProductoInstitucion;}
	public void setIdTipoProducto(Integer idTipoProducto)	{this.idTipoProducto = idTipoProducto;}
	public void setValor(Double valor)						{this.valor = valor;}	
	public void setPorcentajeIVA(Float iva)					{this.iva = iva;}
	public void setIdTipoEnvios(Integer tipoEnvios)			{this.idTipoEnvios = tipoEnvios;}	
	public void setIdDireccion(Long direccion)				{this.idDireccion = direccion;}
	public void setMetodoSolicitud(Integer metodoSolicitud)	{this.metodoSolicitud = metodoSolicitud;}	
	public void setFechaSolicitud(String fechaSolicitud)	{this.fechaSolicitud = fechaSolicitud;}
	
	// Metodos GET
	
	public String getNoFacturable() 		{return noFacturable;}
	public String getAceptado() 			{return aceptado;}
	public Integer getCantidad() 			{return cantidad;}
	public Integer getIdCuenta() 			{return idCuenta;}
	public Integer getIdFormaPago() 		{return idFormaPago;}
	public Integer getIdInstitucion() 		{return idInstitucion;}
	public Integer getIdInstitucionOrigen() {return idInstitucionOrigen;}
	public Long getIdPersona() 				{return idPersona;}
	public Long getIdPeticion() 			{return idPeticion;}
	public Long getIdProducto() 			{return idProducto;}
	public Long getIdProductoInstitucion() 	{return idProductoInstitucion;}
	public Integer getIdTipoProducto() 		{return idTipoProducto;}
	public Double getValor() 				{return valor;}
	public Float getPorcentajeIVA()			{return iva;}
	public Integer getIdTipoEnvios()		{return this.idTipoEnvios;}	
	public Long getIdDireccion()			{return this.idDireccion;}
	public String getFechaSolicitud()		{return this.fechaSolicitud;}	
	public Integer getMetodoSolicitud()		{return this.metodoSolicitud;}
}
