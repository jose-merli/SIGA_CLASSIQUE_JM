/*
 * nuria.rgonzalez - 22-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_TIPOSCV <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenTiposCVBean extends MasterBean
{
	/* Variables */
	private Integer idTipoCV;
	private String 	descripcion;	
	private String  codigoExt;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOSCV";
	
	/* Nombre campos de la tabla */	
	static public final String C_CODIGOEXT   = "CODIGOEXT";
	static public final String C_IDTIPOCV	 = "IDTIPOCV";	
	static public final String C_DESCRIPCION = "DESCRIPCION";
	
	public String getCodigoExt() {
		return codigoExt;
	}
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getIdTipoCV() {
		return idTipoCV;
	}
	public void setIdTipoCV(Integer idTipoCV) {
		this.idTipoCV = idTipoCV;
	}
}
