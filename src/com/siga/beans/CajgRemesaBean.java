package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_REMESA
 * 
 * @author fernando.gómez
 * @since 17/09/2008
 */

public class CajgRemesaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idInstitucion;
	private Integer idRemesa;
	private String	prefijo;
	private String numero;
	private String sufijo;
	private String descripcion;
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CAJG_REMESA";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_IDREMESA =					"IDREMESA";
	static public final String	C_PREFIJO	=					"PREFIJO";
	static public final String	C_NUMERO=						"NUMERO";
	static public final String	C_SUFIJO	=					"SUFIJO";
	static public final String	C_DESCRIPCION	=				"DESCRIPCION";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdRemesa() {
		return idRemesa;
	}
	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}
	public String getPrefijo() {
		return prefijo;
	}
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getSufijo() {
		return sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	
}