/*
 * Created on 07-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysCompraBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoProducto, idFormaPago, cantidad, idCuenta, idCuentaDeudor;
	private Long 	idProducto, idPeticion, idProductoInstitucion, numeroLinea, idPersona, idPersonaDeudor;
	private String 	aceptado, descripcion, idFactura, fecha, fechaBaja,noFacturable;	
	private Float 	iva;
	private Double  importeUnitario, importeAnticipado; 
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA 						= "PYS_COMPRA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION        		= "IDINSTITUCION";
	static public final String C_IDPETICION					= "IDPETICION";
	static public final String C_IDTIPOPRODUCTO				= "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO					= "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION		= "IDPRODUCTOINSTITUCION";
	static public final String C_DESCRIPCION				= "DESCRIPCION";
	static public final String C_FECHA 						= "FECHA";
	static public final String C_FECHABAJA 					= "FECHABAJA";
	static public final String C_CANTIDAD					= "CANTIDAD";
	static public final String C_IMPORTEUNITARIO			= "IMPORTEUNITARIO";
	static public final String C_IMPORTEANTICIPADO			= "IMPORTEANTICIPADO";
	static public final String C_PORCENTAJEIVA				= "PORCENTAJEIVA";
	static public final String C_IDFORMAPAGO				= "IDFORMAPAGO";
	static public final String C_ACEPTADO					= "ACEPTADO";
	static public final String C_NUMEROLINEA				= "NUMEROLINEA";
	static public final String C_IDFACTURA					= "IDFACTURA";
	static public final String C_IDCUENTA					= "IDCUENTA";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_IDCUENTADEUDOR				= "IDCUENTADEUDOR";
	static public final String C_IDPERSONADEUDOR			= "IDPERSONADEUDOR";
	static public final String C_NOFACTURABLE				= "NOFACTURABLE";
	

	
	/**
	 * @return Returns NOFACTURABLE.
	 */
	public String getNoFacturable() {
		return noFacturable;
	}
	/**
	 * @param NOFACTURABLE to set.
	 */
	public void setNoFacturable(String noFacturable) {
		this.noFacturable = noFacturable;
	}
	
	/**
	 * @return Returns the aceptado.
	 */
	public String getAceptado() {
		return aceptado;
	}
	/**
	 * @param aceptado The aceptado to set.
	 */
	public void setAceptado(String aceptado) {
		this.aceptado = aceptado;
	}
	/**
	 * @return Returns the cantidad.
	 */
	public Integer getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the fechaBaja.
	 */
	public String getFechaBaja() {
		return fechaBaja;
	}
	/**
	 * @param fecha The fechaBaja to set.
	 */
	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	/**
	 * @return Returns the idFactura.
	 */
	public String getIdFactura() {
		return idFactura;
	}
	/**
	 * @param idFactura The idFactura to set.
	 */
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}
	/**
	 * @return Returns the idFormaPago.
	 */
	public Integer getIdFormaPago() {
		return idFormaPago;
	}
	/**
	 * @param idFormaPago The idFormaPago to set.
	 */
	public void setIdFormaPago(Integer idFormaPago) {
		this.idFormaPago = idFormaPago;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPeticion.
	 */
	public Long getIdPeticion() {
		return idPeticion;
	}
	/**
	 * @param idPeticion The idPeticion to set.
	 */
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	/**
	 * @return Returns the idProducto.
	 */
	public Long getIdProducto() {
		return idProducto;
	}
	/**
	 * @param idProducto The idProducto to set.
	 */
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	/**
	 * @return Returns the idProductoInstitucion.
	 */
	public Long getIdProductoInstitucion() {
		return idProductoInstitucion;
	}
	/**
	 * @param idProductoInstitucion The idProductoInstitucion to set.
	 */
	public void setIdProductoInstitucion(Long idProductoInstitucion) {
		this.idProductoInstitucion = idProductoInstitucion;
	}
	/**
	 * @return Returns the idTipoProducto.
	 */
	public Integer getIdTipoProducto() {
		return idTipoProducto;
	}
	/**
	 * @param idTipoProducto The idTipoProducto to set.
	 */
	public void setIdTipoProducto(Integer idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}
	/**
	 * @return Returns the importeUnitario.
	 */
	public Double getImporteUnitario() {
		return importeUnitario;
	}
	/**
	 * @param importeUnitario The importeUnitario to set.
	 */
	public void setImporteUnitario(Double importeUnitario) {
		this.importeUnitario = importeUnitario;
	}
	/**
	 * @return Returns the importeUnitarioAnticipado.
	 */
	public Double getImporteAnticipado() {
		return importeAnticipado;
	}
	/**
	 * @param importeUnitarioAnticipado The importeUnitarioAnticipado to set.
	 */
	public void setImporteAnticipado(Double importeAnticipado) {
		this.importeAnticipado = importeAnticipado;
	}
	/**
	 * @return Returns the iva.
	 */
	public Float getIva() {
		return iva;
	}
	/**
	 * @param iva The iva to set.
	 */
	public void setIva(Float iva) {
		this.iva = iva;
	}
	/**
	 * @return Returns the numeroLinea.
	 */
	public Long getNumeroLinea() {
		return numeroLinea;
	}
	/**
	 * @param numeroLinea The numeroLinea to set.
	 */
	public void setNumeroLinea(Long numeroLinea) {
		this.numeroLinea = numeroLinea;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the idPersona
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	
	public Integer getIdCuentaDeudor() {
		return idCuentaDeudor;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuentaDeudor(Integer idCuentaDeudor) {
		this.idCuentaDeudor = idCuentaDeudor;
	}
	/**
	 * @return Returns the idPersona
	 */
	public Long getIdPersonaDeudor() {
		return idPersonaDeudor;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersonaDeudor(Long idPersonaDeudor) {
		this.idPersonaDeudor = idPersonaDeudor;
	}
}
