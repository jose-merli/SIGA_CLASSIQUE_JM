//VERSIONES:
//ruben.fernandez 10-03-2005 creacion
//
package com.siga.beans;


public class FcsHitoGeneralBean extends MasterBean {
	
	/* Variables */
	private Integer  idHitoGeneral;
	private String descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_HITOGENERAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDHITOGENERAL			= "IDHITOGENERAL";
	static public final String C_DESCRIPCION			= "DESCRIPCION";
	
	
	
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
	 * @return Returns the idHitoGeneral.
	 */
	public Integer getIdHitoGeneral() {
		return idHitoGeneral;
	}
	/**
	 * @param idHitoGeneral The idHitoGeneral to set.
	 */
	public void setIdHitoGeneral(Integer idHitoGeneral) {
		this.idHitoGeneral = idHitoGeneral;
	}
}
