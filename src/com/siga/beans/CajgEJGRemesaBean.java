package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_EJGREMESA
 * 
 * @author fernando.gómez
 * @since 17/09/2008
 */

public class CajgEJGRemesaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idEjgRemesa;
	private Integer idInstitucion;
	private Integer anio;
	private Integer numero;
	private Integer idTipoEJG;
	private Integer	idInstitucionRemesa;
	private Integer idRemesa;
	private Integer numeroIntercambio;
	
	
	/*
	 *  Nombre de Tabla*/
	
	
	static public String T_NOMBRETABLA = "CAJG_EJGREMESA";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_IDEJGREMESA	=				"IDEJGREMESA";
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_ANIO=							"ANIO";
	static public final String	C_NUMERO=						"NUMERO";
	static public final String	C_IDTIPOEJG=					"IDTIPOEJG";
	static public final String	C_IDINSTITUCIONREMESA=			"IDINSTITUCIONREMESA";
	static public final String	C_IDREMESA =					"IDREMESA";
	static public final String	C_NUMEROINTERCAMBIO = "NUMEROINTERCAMBIO";
	
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}
	public Integer getIdInstitucionRemesa() {
		return idInstitucionRemesa;
	}
	public void setIdInstitucionRemesa(Integer idInstitucionRemesa) {
		this.idInstitucionRemesa = idInstitucionRemesa;
	}
	public Integer getIdRemesa() {
		return idRemesa;
	}
	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}
	
	/**
	 * @return the numeroIntercambio
	 */
	public Integer getNumeroIntercambio() {
		return numeroIntercambio;
	}
	/**
	 * @param numeroIntercambio the numeroIntercambio to set
	 */
	public void setNumeroIntercambio(Integer numeroIntercambio) {
		this.numeroIntercambio = numeroIntercambio;
	}
	/**
	 * @return the idEjgRemesa
	 */
	public Integer getIdEjgRemesa() {
		return idEjgRemesa;
	}
	/**
	 * @param idEjgRemesa the idEjgRemesa to set
	 */
	public void setIdEjgRemesa(Integer idEjgRemesa) {
		this.idEjgRemesa = idEjgRemesa;
	}
	
}