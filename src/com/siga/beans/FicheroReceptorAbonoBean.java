/*
 * VERSIONES:
 * 
 * miguel.villegas - 31-03-2005 - Creacion
 * jose.barrientos -  9-12-2008 - Añadidos los campos domicilio, poblacion y motivo
 * jose.barrientos -  26-02-2009 - Añadido el campo concepto
 *	
 */

/**
 * Clase que recoge y establece los valores del bean empleado como soporte para generar <br/>
 * el receptor en el fichero de abonos. 
 */
package com.siga.beans;

public class FicheroReceptorAbonoBean extends MasterBean{

	/* Variables */
	private Double importe;
	private Long identificador;
	private String codigoBanco;
	private String codigoSucursal;
	private String numeroCuenta;
	private String dni;
	private String nombre;
	private String digitosControl;
	private String domicilio;
	private String poblacion;
	private String nombrePago;
	private String concepto;
	
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
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public Long getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Long identificador) {
		this.identificador = identificador;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
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
	public String getDigitosControl() {
		return digitosControl;
	}
	public void setDigitosControl(String control) {
		this.digitosControl = control;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getNombrePago() {
		return nombrePago;
	}
	public void setNombrePago(String nombrePago) {
		this.nombrePago = nombrePago;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
}
