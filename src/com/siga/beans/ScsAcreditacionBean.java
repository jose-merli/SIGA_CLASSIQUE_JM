/*
 * Created on 01-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author s230298
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsAcreditacionBean extends MasterBean {

	/* Variables */	
	private String 	descripcion;
	private Integer idAcreditacion, idTipoAcreditacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_ACREDITACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDACREDITACION		= "IDACREDITACION";
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	static public final String C_IDTIPOACREDITACION	= "IDTIPOACREDITACION";

	
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
	 * @return Returns the idAcreditacion.
	 */
	public Integer getIdAcreditacion() {
		return idAcreditacion;
	}
	/**
	 * @param idAcreditacion The idAcreditacion to set.
	 */
	public void setIdAcreditacion(Integer idAcreditacion) {
		this.idAcreditacion = idAcreditacion;
	}
	
	/**
	 * @return Returns the idTipoAcreditacion.
	 */
	public Integer getIdTipoAcreditacion() {
		return idTipoAcreditacion;
	}
	/**
	 * @param idTipoAcreditacion The idTipoAcreditacion to set.
	 */
	public void setIdTipoAcreditacion(Integer idTipoAcreditacion) {
		this.idTipoAcreditacion = idTipoAcreditacion;
	}
}
