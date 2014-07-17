/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 * jose.barrientos - 28-02-2009 - Añadido el campo sjcs
 */

package com.siga.beans;


public class FacBancoInstitucionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idsufijo, idsufijosjcs;
	private String 	bancosCodigo, codBanco, codSucursal, numeroCuenta, fechaBaja, asientoContable, digitoControl, sjcs,nosjcs, iban;	
	private Double 	impComisionPropiaCargo, impComisionAjenaCargo, impComisionPropiaAbono, impComisionAjenaAbono;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_BANCOINSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_BANCOS_CODIGO				= "BANCOS_CODIGO";	
	static public final String C_COD_BANCO		 			= "COD_BANCO";
	static public final String C_COD_SUCURSAL		 		= "COD_SUCURSAL";
	static public final String C_FECHABAJA					= "FECHABAJA";
	static public final String C_NUMEROCUENTA				= "NUMEROCUENTA";
	static public final String C_IMPCOMISIONPROPIACARGO		= "IMPCOMISIONPROPIACARGO";
	static public final String C_IMPCOMISIONAJENACARGO		= "IMPCOMISIONAJENACARGO";
	static public final String C_IMPCOMISIONPROPIAABONO		= "IMPCOMISIONPROPIAABONO";
	static public final String C_IMPCOMISIONAJENAABONO		= "IMPCOMISIONAJENAABONO";
	static public final String C_ASIENTOCONTABLE			= "ASIENTOCONTABLE";
	static public final String C_DIGITOCONTROL				= "DIGITOCONTROL";
	static public final String C_SJCS						= "SJCS";
	static public final String C_NOSJCS 					= "NOSJCS";
	static public final String C_IDSUFIJO					= "IDSUFIJO";
	static public final String C_IDSUFIJOSJCS				= "IDSUFIJOSJCS";
	static public final String C_IBAN						= "IBAN";
	
	

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
	
	public Integer getIdsufijo() {
		return idsufijo;
	}
	
	public Integer getIdsufijosjcs() {
		return idsufijosjcs;
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
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public String getAsientoContable() {
		return asientoContable;
	}

	public String getSJCS() {
		return sjcs;
	}
	
	public String getNoSJCS() {
		return nosjcs;
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
	public void setIdsufijo(Integer idsufijo) {
		this.idsufijo = idsufijo;
	}
	public void setIdsufijosjcs(Integer idsufijosjcs) {
		this.idsufijosjcs = idsufijosjcs;
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
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
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
	public void setNoSJCS(String nosjcs) {
		this.nosjcs = nosjcs;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	
}
