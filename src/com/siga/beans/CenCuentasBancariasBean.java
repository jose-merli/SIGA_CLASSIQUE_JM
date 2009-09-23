/*
 * nuria.rgonzalez - 13-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_CUENTASBANCARIAS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenCuentasBancariasBean extends MasterBean {

	/* Variables */
	private Integer	idInstitucion, idCuenta; 
	private Long	idPersona;
	
	private String 	abonoCargo, abonoSJCS, cbo_Codigo, codigoSucursal, digitoControl, numeroCuenta,
					titular, fechaBaja, cuentaContable;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_CUENTASBANCARIAS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDCUENTA			= "IDCUENTA";
	static public final String C_ABONOCARGO			= "ABONOCARGO";
	static public final String C_ABONOSJCS			= "ABONOSJCS";
	static public final String C_CBO_CODIGO			= "CBO_CODIGO";
	static public final String C_CODIGOSUCURSAL		= "CODIGOSUCURSAL";
	static public final String C_DIGITOCONTROL		= "DIGITOCONTROL";
	static public final String C_NUMEROCUENTA		= "NUMEROCUENTA";
	static public final String C_TITULAR			= "TITULAR";	
	static public final String C_FECHABAJA			= "FECHABAJA";
	static public final String C_CUENTACONTABLE		= "CUENTACONTABLE";	
	/**
	 * @return Devuelve abonoCargo.
	 */
	public String getAbonoCargo() {
		return abonoCargo;
	}
	/**
	 * @return Devuelve cbo_Codigo.
	 */
	public String getCbo_Codigo() {
		return cbo_Codigo;
	}
	/**
	 * @return Devuelve codigoSucursal.
	 */
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	/**
	 * @return Devuelve cuentaContable.
	 */
	public String getCuentaContable() {
		return cuentaContable;
	}
	/**
	 * @return Devuelve digitoControl.
	 */
	public String getDigitoControl() {
		return digitoControl;
	}
	/**
	 * @return Devuelve fechaBaja.
	 */
	public String getFechaBaja() {
		return fechaBaja;
	}
	/**
	 * @return Devuelve idCuenta.
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @return Devuelve idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @return Devuelve idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @return Devuelve numeroCuenta.
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * @return Devuelve abonoSJCS.
	 */
	public String getAbonoSJCS() {
		return abonoSJCS;
	}
	/**
	 * @return Devuelve titular.
	 */
	public String getTitular() {
		return titular;
	}
	/**
	 * @param abonoCargo obtiene el abonoCargo.
	 */
	public void setAbonoCargo(String abonoCargo) {
		this.abonoCargo = abonoCargo;
	}
	/**
	 * @param cbo_Codigo obtiene el cbo_Codigo.
	 */
	public void setCbo_Codigo(String cbo_Codigo) {
		this.cbo_Codigo = cbo_Codigo;
	}
	/**
	 * @param codigoSucursal obtiene el codigoSucursale.
	 */
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	/**
	 * @param cuentaContable obtiene la cuentaContable.
	 */
	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	/**
	 * @param digitoControl obtiene el digitoControl.
	 */
	public void setDigitoControl(String digitoControl) {
		this.digitoControl = digitoControl;
	}
	/**
	 * @param fechaBaja obtiene la fechaBaja.
	 */
	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	/**
	 * @param idCuenta obtiene el idCuenta.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @param idInstitucion obtiene el idInstitucion.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @param idPersona obtiene el idPersona.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @param numeroCuenta obtiene el numeroCuenta.
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	/**
	 * @param sociedad obtiene el abonoSJCS.
	 */
	public void setAbonoSJCS(String abonoSJCS) {
		this.abonoSJCS = abonoSJCS;
	}
	/**
	 * @param titular obtiene el titular.
	 */
	public void setTitular(String titular) {
		this.titular = titular;
	}
}