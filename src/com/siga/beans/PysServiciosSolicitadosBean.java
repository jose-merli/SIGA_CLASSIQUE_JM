/*
 * Created on 03-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class PysServiciosSolicitadosBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoServicios, idPeriodicidad, idPreciosServicios, idCuenta, idFormaPago, cantidad;
	private Long 	idServicio, idServicioInstitucion, idPeticion,  idPeticionAlta, idPersona;
	private String 	aceptado;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_SERVICIOSSOLICITADOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDTIPOSERVICIOS		= "IDTIPOSERVICIOS";
	static public final String C_IDPERIODICIDAD 		= "IDPERIODICIDAD";
	static public final String C_IDPRECIOSSERVICIOS	 	= "IDPRECIOSSERVICIOS";
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDFORMAPAGO			= "IDFORMAPAGO";
	static public final String C_IDCUENTA				= "IDCUENTA";
	static public final String C_CANTIDAD 				= "CANTIDAD";
	static public final String C_IDSERVICIO				= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION	= "IDSERVICIOSINSTITUCION";
	static public final String C_IDPETICION				= "IDPETICION";
	static public final String C_ACEPTADO				= "ACEPTADO";


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
	 * @return Returns the idPeriodicidad.
	 */
	public Integer getIdPeriodicidad() {
		return idPeriodicidad;
	}
	/**
	 * @param idPeriodicidad The idPeriodicidad to set.
	 */
	public void setIdPeriodicidad(Integer idPeriodicidad) {
		this.idPeriodicidad = idPeriodicidad;
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
	 * @return Returns the idPreciosServicios.
	 */
	public Integer getIdPreciosServicios() {
		return idPreciosServicios;
	}
	/**
	 * @param idPreciosServicios The idPreciosServicios to set.
	 */
	public void setIdPreciosServicios(Integer idPreciosServicios) {
		this.idPreciosServicios = idPreciosServicios;
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
}
