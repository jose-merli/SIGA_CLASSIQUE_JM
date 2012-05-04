package com.siga.beans;


public class GenCatalogosWSBean extends MasterBean
{
	// Variables 
	private String catalogo;
	private String conjunto;
	private String valor;
	private String idInterno;
	private String idExterno;
	private String refInterna;
	private String recurso;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "GEN_CATALOGOS_WS";

	// Nombre de campos de la tabla
	static public final String 	C_CATALOGO	= "CATALOGO";
	static public final String 	C_CONJUNTO 	= "CONJUNTO";
	static public final String 	C_VALOR 	= "VALOR";
	static public final String 	C_IDINTERNO = "IDINTERNO";
	static public final String 	C_IDEXTERNO = "IDEXTERNO";
	static public final String 	C_REFINTERNA	= "REFINTERNA";
	static public final String 	C_RECURSO	= "RECURSO";
	
	public String getCatalogo() {
		return catalogo;
	}
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	public String getConjunto() {
		return conjunto;
	}
	public void setConjunto(String conjunto) {
		this.conjunto = conjunto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getIdInterno() {
		return idInterno;
	}
	public void setIdInterno(String idInterno) {
		this.idInterno = idInterno;
	}
	public String getIdExterno() {
		return idExterno;
	}
	public void setIdExterno(String idExterno) {
		this.idExterno = idExterno;
	}
	public String getRefInterna() {
		return refInterna;
	}
	public void setRefInterna(String refInterna) {
		this.refInterna = refInterna;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	
}