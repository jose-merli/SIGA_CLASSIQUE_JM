//VERSIONES:
//ruben.fernandez 21/03/2005 creacion
//
package com.siga.beans;


public class FcsMovimientosVariosBean extends MasterBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8394960345283333072L;
	/* Variables */
	private Integer idInstitucion;
	private Integer idMovimiento;
	private Long idPersona;
	private String  descripcion;
	private String  motivo;
	private String  fechaAlta;
	private Double  cantidad;
	private String  contabilizado;
	private Integer idFacturacion;
	private Integer idGrupoFacturacion;

	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_MOVIMIENTOSVARIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDMOVIMIENTO 		= "IDMOVIMIENTO";
	static public final String C_IDPERSONA 			= "IDPERSONA";
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	static public final String C_MOTIVO 			= "MOTIVO";
	static public final String C_FECHAALTA 			= "FECHAALTA";
	static public final String C_CANTIDAD 			= "CANTIDAD";
	static public final String C_CONTABILIZADO		= "CONTABILIZADO";
	static public final String C_IDFACTURACION 			= "IDFACTURACION";
	
	static public final String C_IDGRUPOFACTURACION 		= "IDGRUPOFACTURACION";

	
	public String getContabilizado() {
		return contabilizado;
	}
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}
	/**
	 * @return Returns the cantidad.
	 */
	public Double getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(Double cantidad) {
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
	 * @return Returns the fechaAlta.
	 */
	public String getFechaAlta() {
		return fechaAlta;
	}
	/**
	 * @param fechaAlta The fechaAlta to set.
	 */
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
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
	 * @return Returns the idMovimiento.
	 */
	public Integer getIdMovimiento() {
		return idMovimiento;
	}
	/**
	 * @param idMovimiento The idMovimiento to set.
	 */
	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
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
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * @return the idFacturacion
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion the idFacturacion to set
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	/**
	 * @return the idGrupoFacturacion
	 */
	public Integer getIdGrupoFacturacion() {
		return idGrupoFacturacion;
	}
	/**
	 * @param idGrupoFacturacion the idGrupoFacturacion to set
	 */
	public void setIdGrupoFacturacion(Integer idGrupoFacturacion) {
		this.idGrupoFacturacion = idGrupoFacturacion;
	}
}