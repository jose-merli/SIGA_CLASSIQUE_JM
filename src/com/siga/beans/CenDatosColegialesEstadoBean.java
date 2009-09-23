/*
 * Created on 22-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class CenDatosColegialesEstadoBean extends MasterBean {

	/* Variables */
	private Long idPersona;
	private Integer idInstitucion, idEstado;
	
	private String 	fechaEstado, observaciones;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DATOSCOLEGIALESESTADO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA 			= "IDPERSONA";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDESTADO			= "IDESTADO";
	static public final String C_FECHAESTADO		= "FECHAESTADO";
	static public final String C_OBSERVACIONES		= "OBSERVACIONES";	

	/**
	 * @return Returns the fechaEstado.
	 */
	public String getFechaEstado() {
		return fechaEstado;
	}
	/**
	 * @param fechaEstado The fechaEstado to set.
	 */
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	/**
	 * @return Returns the idEstado.
	 */
	public Integer getIdEstado() {
		return idEstado;
	}
	/**
	 * @param idEstado The idEstado to set.
	 */
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
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
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
