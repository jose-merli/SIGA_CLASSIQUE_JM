/*
 * VERSIONES:
 * 
 * miguel.villegas - 31-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean empleado como soporte para generar <br/>
 * el emisor en el fichero de abonos. 
 */
package com.siga.beans;

public class FicheroEmisorAbonoBean extends MasterBean{

	/* Variables */
	private Integer identificador;
	private String codigoBanco;
	private String codigoSucursal;
	private String numeroCuenta;
	private String nif;
	private String nombre;
	private String domicilio;
	private String plaza;
	private Long identificadorDisquete;
	

	/* Metodos */
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	public Integer getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}	
	public Long getIdentificadorDisquete() {
		return identificadorDisquete;
	}
	public void setIdentificadorDisquete(Long identificador) {
		this.identificadorDisquete = identificador;
	}	
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getPlaza() {
		return plaza;
	}
	public void setPlaza(String plaza) {
		this.plaza = plaza;
	}
}
