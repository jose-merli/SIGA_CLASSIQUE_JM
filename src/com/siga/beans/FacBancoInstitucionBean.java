/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creaci�n
 * jose.barrientos - 28-02-2009 - A�adido el campo sjcs
 */

package com.siga.beans;


public class FacBancoInstitucionBean extends MasterBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5350638827504206894L;
	/* Variables */
	private Integer idInstitucion, idsufijosjcs;
	private String 	bancosCodigo, codBanco, codSucursal, numeroCuenta, fechaBaja, asientoContable, digitoControl, sjcs, iban;	
	private Double comisionImporte, idTipoIva;
	private String comisionDescripcion, comisionCuentaContable;
	
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
	static public final String C_IDTIPOIVA					= "IDTIPOIVA";
	static public final String C_COMISIONCUENTACONTABLE		= "COMISIONCUENTACONTABLE";	

	/* M�todos get */
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
		
	/* M�todos set */
	
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
	public Double getIdTipoIva() {
		return idTipoIva;
	}
	public void setIdTipoIva(Double idTipoIva) {
		this.idTipoIva = idTipoIva;
	}
	public String getComisionDescripcion() {
		return comisionDescripcion;
	}
	public void setComisionDescripcion(String comisionDescripcion) {
		this.comisionDescripcion = comisionDescripcion;
	}
	public String getComisionCuentaContable() {
		return comisionCuentaContable;
	}
	public void setComisionCuentaContable(String comisionCuentaContable) {
		this.comisionCuentaContable = comisionCuentaContable;
	}	
}
