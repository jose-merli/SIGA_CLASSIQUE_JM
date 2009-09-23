/*
 * Created on 04-feb-2005
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
public class PysEstadoPeticionBean extends MasterBean {

	/* Variables */
	private Integer idEstado;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_ESTADOPETICION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADOPETICION	= "IDESTADOPETICION";	
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	
	
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
}
