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
public class PysSuscripcionBean extends MasterBean {
	/* Variables */
	private Integer idInstitucion, idTipoServicios, cantidad, idSuscripcion, idFormaPago, idCuenta;
	private Long 	idServicio, idPeticion, idServicioInstitucion, idPersona;
	private String 	descripcion, idFactura, fechaBaja, fechaSuscripcion;	
	private Float 	iva;
	private Double  importeUnitario, importeAnticipado; 
	/* Nombre tabla */
	static public String T_NOMBRETABLA 						= "PYS_SUSCRIPCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION        		= "IDINSTITUCION";
	static public final String C_IDPETICION					= "IDPETICION";
	static public final String C_IDTIPOSERVICIOS			= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO					= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION		= "IDSERVICIOSINSTITUCION";
	static public final String C_IDSUSCRIPCION				= "IDSUSCRIPCION";
	static public final String C_IDPERSONA					= "IDPERSONA";
	static public final String C_DESCRIPCION				= "DESCRIPCION";
	static public final String C_FECHABAJA 					= "FECHABAJA";
	static public final String C_FECHASUSCRIPCION 			= "FECHASUSCRIPCION";
	static public final String C_CANTIDAD					= "CANTIDAD";
	static public final String C_IMPORTEUNITARIO			= "IMPORTEUNITARIO";
	static public final String C_IMPORTEANTICIPADO			= "IMPORTEANTICIPADO";
	static public final String C_IDFORMAPAGO				= "IDFORMAPAGO";
	static public final String C_IDCUENTA					= "IDCUENTA";
	
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
	 * @return Returns the id Suscripcion
	 */
	public Integer getIdSuscripcion() {
		return idSuscripcion;
	}
	/**
	 * @param cantidad The idSuscripcion to set.
	 */
	public void setIdSuscripcion(Integer idSuscripcion) {
		this.idSuscripcion= idSuscripcion;
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
	 * @return Returns the fechaBaja.
	 */
	public String getFechaBaja() {
		return fechaBaja;
	}
	/**
	 * @param fecha The fecha to set.
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
	 * @return Returns the idCuenta.
	 */
	public Integer getIdCuenta() {
		return this.idCuenta;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the idPersona.
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
	 * @return Returns the idServicio.
	 */
	public Long getIdServicio() {
		return idServicio;
	}
	/**
	 * @param idServicio The idServicio to set.
	 */
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}
	/**
	 * @return Returns the idServicioInstitucion.
	 */
	public Long getIdServicioInstitucion() {
		return idServicioInstitucion;
	}
	/**
	 * @param idServicioInstitucion The idServicioInstitucion to set.
	 */
	public void setIdServicioInstitucion(Long idServicioInstitucion) {
		this.idServicioInstitucion = idServicioInstitucion;
	}
	/**
	 * @return Returns the idTipoServicios.
	 */
	public Integer getIdTipoServicios() {
		return idTipoServicios;
	}
	/**
	 * @param idTipoServicios The idTipoServicios to set.
	 */
	public void setIdTipoServicios(Integer idTipoServicios) {
		this.idTipoServicios = idTipoServicios;
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
	 * @return Returns the fechaSuscripcion.
	 */
	public String getFechaSuscripcion() {
		return fechaSuscripcion;
	}
	/**
	 * @param fechaSuscripcion The fechaSuscripcion to set.
	 */
	public void setFechaSuscripcion(String fechaSuscripcion) {
		this.fechaSuscripcion = fechaSuscripcion;
	}
	
	/**
	 * @return Returns the importeAnticipado.
	 */
	public Double getImporteAnticipado() {
		return importeAnticipado;
	}
	/**
	 * @param importeAnticipado The importeAnticipado to set.
	 */
	public void setImporteAnticipado(Double importeAnticipado) {
		this.importeAnticipado = importeAnticipado;
	}
}
