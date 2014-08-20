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
	private Double comisionImporte, comisionIVA;
	private String comisionDescripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_BANCOINSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_BANCOS_CODIGO				= "BANCOS_CODIGO";	
	static public final String C_COD_BANCO		 			= "COD_BANCO";
	static public final String C_COD_SUCURSAL		 		= "COD_SUCURSAL";
	static public final String C_FECHABAJA					= "FECHABAJA";
	static public final String C_NUMEROCUENTA				= "NUMEROCUENTA";
	static public final String C_ASIENTOCONTABLE			= "ASIENTOCONTABLE";
	static public final String C_DIGITOCONTROL				= "DIGITOCONTROL";
	static public final String C_SJCS						= "SJCS";
	static public final String C_IDSUFIJOSJCS				= "IDSUFIJOSJCS";
	static public final String C_IBAN						= "IBAN";
	static public final String C_COMISIONIMPORTE			= "COMISIONIMPORTE";
	static public final String C_COMISIONDESCRIPCION		= "COMISIONDESCRIPCION";
	static public final String C_COMISIONIVA				= "COMISIONIVA";		

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
	
	public Integer getIdsufijosjcs() {
		return idsufijosjcs;
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
	
	public void setIdsufijosjcs(Integer idsufijosjcs) {
		this.idsufijosjcs = idsufijosjcs;
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
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Double getComisionImporte() {
		return comisionImporte;
	}
	public void setComisionImporte(Double comisionImporte) {
		this.comisionImporte = comisionImporte;
	}
	public Double getComisionIVA() {
		return comisionIVA;
	}
	public void setComisionIVA(Double comisionIVA) {
		this.comisionIVA = comisionIVA;
	}
	public String getComisionDescripcion() {
		return comisionDescripcion;
	}
	public void setComisionDescripcion(String comisionDescripcion) {
		this.comisionDescripcion = comisionDescripcion;
	}
}
