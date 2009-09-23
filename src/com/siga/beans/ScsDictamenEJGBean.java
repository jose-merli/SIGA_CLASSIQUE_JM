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
public class ScsDictamenEJGBean extends MasterBean{
	/* Variables */
	private String 	idInstitucion,idDictamen,idTipoDictamen,idFundamento,abreviatura,descripcion,codigoExt;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_DICTAMENEJG";

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDTIPODICTAMEN			= "IDTIPODICTAMEN";
	static public final String C_ABREVIATURA			= "ABREVIATURA";	
	static public final String C_DESCRIPCION			= "DESCRIPCION";
	static public final String C_IDDICTAMEN				= "IDDICTAMEN";
	static public final String C_IDFUNDAMENTO			= "IDFUNDAMENTO";
	static public final String C_CODIGOEXT				= "CODIGOEXT";
	
	

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
	public String getIdDictamen() {
		return idDictamen;
	}
	public void setIdDictamen(String idDictamen) {
		this.idDictamen = idDictamen;
	}
	public String getIdFundamento() {
		return idFundamento;
	}
	public void setIdFundamento(String idFundamento) {
		this.idFundamento = idFundamento;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdTipoDictamen() {
		return idTipoDictamen;
	}
	public void setIdTipoDictamen(String idTipoDictamen) {
		this.idTipoDictamen = idTipoDictamen;
	}
	public String getCodigoExt() {
		return codigoExt;
	}
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
}
