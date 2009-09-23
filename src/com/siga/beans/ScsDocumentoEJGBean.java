/*
 * nuria.rgonzalez - 01-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_PAIS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class ScsDocumentoEJGBean extends MasterBean{
	/* Variables */
	private String 	idInstitucion,idTipoDocumentoEJG,idDocumentoEJG,abreviatura,descripcion;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_DOCUMENTOEJG";

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDTIPODOCUMENTOEJG		= "IDTIPODOCUMENTOEJG";
	static public final String C_ABREVIATURA			= "ABREVIATURA";	
	static public final String C_DESCRIPCION			= "DESCRIPCION";
	static public final String C_IDDOCUMENTOEJG			= "IDDOCUMENTOEJG";
	
	

	public static String getT_NOMBRETABLA() {
		return T_NOMBRETABLA;
	}
	public static void setT_NOMBRETABLA(String t_nombretabla) {
		T_NOMBRETABLA = t_nombretabla;
	}
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdTipoDocumentoEJG() {
		return idTipoDocumentoEJG;
	}
	public void setIdTipoDocumentoEJG(String idTipoDocumentoEJG) {
		this.idTipoDocumentoEJG = idTipoDocumentoEJG;
	}
	public String getIdDocumentoEJG() {
		return idDocumentoEJG;
	}
	public void setIdDocumentoEJG(String idDocumentoEJG) {
		this.idDocumentoEJG = idDocumentoEJG;
	}
}
