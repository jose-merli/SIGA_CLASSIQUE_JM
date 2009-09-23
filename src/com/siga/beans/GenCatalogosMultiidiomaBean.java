package com.siga.beans;


public class GenCatalogosMultiidiomaBean extends MasterBean
{
	// Variables 
	private Integer codigo;
	private String nombreTabla;
	private String campoTabla;
	private String codigoTabla;
	private String local;
	private String migrado;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "GEN_CATALOGOS_MULTIIDIOMA";

	// Nombre de campos de la tabla
	static public final String 	C_CODIGO 		= "CODIGO";
	static public final String 	C_NOMBRETABLA 	= "NOMBRETABLA";
	static public final String 	C_CAMPOTABLA 	= "CAMPOTABLA";
	static public final String 	C_LOCAL 		= "LOCAL";
	static public final String 	C_CODIGOTABLA 	= "CODIGOTABLA";
	static public final String 	C_MIGRADO 		= "MIGRADO";
	
	// Metodos SET - GET
	public String getCampoTabla() {
		return campoTabla;
	}
	public void setCampoTabla(String campoTabla) {
		this.campoTabla = campoTabla;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getCodigoTabla() {
		return codigoTabla;
	}
	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getMigrado() {
		return migrado;
	}
	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
}