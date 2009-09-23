package com.siga.beans;


public class GenRecursosCatalogosBean extends MasterBean
{
	// Variables 
	private String idRecurso;
	private String descripcion;
	private String idLenguaje;
	private Integer idInstitucion;
	private String nombreTabla;
	private String campoTabla;
	private String idRecursoAlias;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "GEN_RECURSOS_CATALOGOS";
	
	// Nombre de campos de la tabla
	static public final String 	C_IDRECURSO 		= "IDRECURSO";
	static public final String 	C_DESCRIPCION 		= "DESCRIPCION";
	static public final String 	C_IDLENGUAJE 		= "IDLENGUAJE";
	static public final String 	C_IDINSTITUCION 	= "IDINSTITUCION";
	static public final String 	C_NOMBRETABLA 		= "NOMBRETABLA";
	static public final String 	C_CAMPOTABLA 		= "CAMPOTABLA";
	static public final String 	C_IDRECURSOALIAS 	= "IDRECURSOALIAS";
	
	// Metodos SET - GET
	public String getCampoTabla() {
		return campoTabla;
	}
	public void setCampoTabla(String campoTabla) {
		this.campoTabla = campoTabla;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdLenguaje() {
		return idLenguaje;
	}
	public void setIdLenguaje(String idLenguaje) {
		this.idLenguaje = idLenguaje;
	}
	public String getIdRecurso() {
		return idRecurso;
	}
	public void setIdRecurso(String idRecurso) {
		this.idRecurso = idRecurso;
	}
	public String getIdRecursoAlias() {
		return idRecursoAlias;
	}
	public void setIdRecursoAlias(String idRecursoAlias) {
		this.idRecursoAlias = idRecursoAlias;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
}