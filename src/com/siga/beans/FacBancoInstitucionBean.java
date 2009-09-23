/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 * jose.barrientos - 28-02-2009 - Añadido el campo sjcs
 */

package com.siga.beans;


public class FacBancoInstitucionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	private String 	bancosCodigo, codBanco, nif, codSucursal, numeroCuenta, sufijo, fechaBaja, asientoContable, digitoControl, sjcs;	
	private Double 	impComisionPropiaCargo, impComisionAjenaCargo, impComisionPropiaAbono, impComisionAjenaAbono;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_BANCOINSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_BANCOS_CODIGO				= "BANCOS_CODIGO";	
	static public final String C_NIF				 		= "NIF";
	static public final String C_COD_BANCO		 			= "COD_BANCO";
	static public final String C_COD_SUCURSAL		 		= "COD_SUCURSAL";
	static public final String C_SUFIJO						= "SUFIJO";
	static public final String C_FECHABAJA					= "FECHABAJA";
	static public final String C_NUMEROCUENTA				= "NUMEROCUENTA";
	static public final String C_IMPCOMISIONPROPIACARGO		= "IMPCOMISIONPROPIACARGO";
	static public final String C_IMPCOMISIONAJENACARGO		= "IMPCOMISIONAJENACARGO";
	static public final String C_IMPCOMISIONPROPIAABONO		= "IMPCOMISIONPROPIAABONO";
	static public final String C_IMPCOMISIONAJENAABONO		= "IMPCOMISIONAJENAABONO";
	static public final String C_ASIENTOCONTABLE			= "ASIENTOCONTABLE";
	static public final String C_DIGITOCONTROL				= "DIGITOCONTROL";
	static public final String C_SJCS						= "SJCS";
	

	/* Métodos get */
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public String getCodBanco() {
		return codBanco;
	}
	public String getCodSucursal() {
		return codSucursal;
	}
	public String getFechaBaja() {
		return fechaBaja;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Double getImpComisionAjenaAbono() {
		return impComisionAjenaAbono;
	}
	public Double getImpComisionAjenaCargo() {
		return impComisionAjenaCargo;
	}
	public Double getImpComisionPropiaAbono() {
		return impComisionPropiaAbono;
	}
	public Double getImpComisionPropiaCargo() {
		return impComisionPropiaCargo;
	}
	public String getNif() {
		return nif;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public String getAsientoContable() {
		return asientoContable;
	}

	public String getSufijo() {
		return sufijo;
	}
	
	public String getSJCS() {
		return sjcs;
	}
		
	/* Métodos set */
	
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}
	public void setCodSucursal(String codSucursal) {
		this.codSucursal = codSucursal;
	}
	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setImpComisionAjenaAbono(Double impComisionAjenaAbono) {
		this.impComisionAjenaAbono = impComisionAjenaAbono;
	}
	public void setImpComisionAjenaCargo(Double impComisionAjenaCargo) {
		this.impComisionAjenaCargo = impComisionAjenaCargo;
	}
	public void setImpComisionPropiaAbono(Double impComisionPropiaAbono) {
		this.impComisionPropiaAbono = impComisionPropiaAbono;
	}
	public void setImpComisionPropiaCargo(Double impComisionPropiaCargo) {
		this.impComisionPropiaCargo = impComisionPropiaCargo;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public String getDigitoControl() {
		return digitoControl;
	}
	public void setDigitoControl(String digitoControl) {
		this.digitoControl = digitoControl;
	}
	public void setSJCS(String sjcs) {
		this.sjcs = sjcs;
	}
}
