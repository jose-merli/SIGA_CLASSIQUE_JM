/*
 * Created on 23-mar-2005
 *
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class FacRenegociacionBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idRenegociacion, idCuenta;
	private Long idPersona;
	private Double importe;
	private String 	idFactura, fechaRenegociacion, comentario;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_RENEGOCIACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDFACTURA          = "IDFACTURA";
	static public final String C_IDRENEGOCIACION 	= "IDRENEGOCIACION";
	static public final String C_IDCUENTA 			= "IDCUENTA";
	static public final String C_FECHARENEGOCIACION = "FECHARENEGOCIACION";
	static public final String C_IMPORTE 			= "IMPORTE";
	static public final String C_COMENTARIO 		= "COMENTARIO";
	static public final String C_IDPERSONA 			= "IDPERSONA";
	
	
	/**
	 * @return Returns the comentario.
	 */
	public String getComentario() {
		return comentario;
	}
	/**
	 * @return Returns the fechaRenegociacion.
	 */
	public String getFechaRenegociacion() {
		return fechaRenegociacion;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @return Returns the idFactura.
	 */
	public String getIdFactura() {
		return idFactura;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @return Returns the idRenegociacion.
	 */
	public Integer getIdRenegociacion() {
		return idRenegociacion;
	}
	/**
	 * @return Returns the importe.
	 */
	public Double getImporte() {
		return importe;
	}
	/**
	 * @param comentario The comentario to set.
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	/**
	 * @param fechaRenegociacion The fechaRenegociacion to set.
	 */
	public void setFechaRenegociacion(String fechaRenegociacion) {
		this.fechaRenegociacion = fechaRenegociacion;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @param idFactura The idFactura to set.
	 */
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @param idRenegociacion The idRenegociacion to set.
	 */
	public void setIdRenegociacion(Integer idRenegociacion) {
		this.idRenegociacion = idRenegociacion;
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}
}
