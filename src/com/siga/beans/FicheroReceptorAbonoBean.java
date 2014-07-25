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
	private String referenciaInterna;
	private String iban;
	private String bic;
	private String codigoBanco;
	private String codigoSucursal;
	private String numeroCuenta;
	private String dni;
	private String nombre;
	private String digitosControl;
	private String domicilio;
	private String codigopostal;
	private String poblacion;
	private String provincia;
	private String pais;
	private String codIsoPais;
	private String sepa;
	private String nombrePago;
	private String concepto;
	private String numeroAbono;
	private String sufijo;
	private String propositoSEPA;
	private String propositoOtros;
	
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
	public String getCodigopostal()
	{
		return codigopostal;
	}
	public void setCodigopostal(String codigopostal)
	{
		this.codigopostal = codigopostal;
	}
	public String getProvincia()
	{
		return provincia;
	}
	public void setProvincia(String provincia)
	{
		this.provincia = provincia;
	}
	public String getPais()
	{
		return pais;
	}
	public void setPais(String pais)
	{
		this.pais = pais;
	}
	public String getIban()
	{
		return iban;
	}
	public void setIban(String iban)
	{
		this.iban = iban;
	}
	public String getBic()
	{
		return bic;
	}
	public void setBic(String bic)
	{
		this.bic = bic;
	}
	public String getReferenciaInterna()
	{
		return referenciaInterna;
	}
	public void setReferenciaInterna(String referenciaInterna)
	{
		this.referenciaInterna = referenciaInterna;
	}
	public String getCodIsoPais()
	{
		return codIsoPais;
	}
	public void setCodIsoPais(String codIsoPais)
	{
		this.codIsoPais = codIsoPais;
	}
	public String getNumeroAbono()
	{
		return numeroAbono;
	}
	public void setNumeroAbono(String numeroAbono)
	{
		this.numeroAbono = numeroAbono;
	}
	public String getSepa()
	{
		return sepa;
	}
	public void setSepa(String sepa)
	{
		this.sepa = sepa;
	}
	public String getSufijo() {
		return sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public String getPropositoSEPA() {
		return propositoSEPA;
	}
	public void setPropositoSEPA(String propositoSEPA) {
		this.propositoSEPA = propositoSEPA;
	}
	public String getPropositoOtros() {
		return propositoOtros;
	}
	public void setPropositoOtros(String propositoOtros) {
		this.propositoOtros = propositoOtros;
	}
	
}
