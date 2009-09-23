/*
 * VERSIONES:
 * 
 * miguel.villegas - 19-04-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores de la tabla Cen_Sucursales <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla correspondiente 
 */
package com.siga.beans;

public class CenSucursalesBean extends MasterBean{

	/* Variables */
	
	private String 	bancosCodigo;
	private String 	codSucursal;
	private String 	nombre;
	private String 	domicilio;
	private String  codigoPostal;
	private String 	codPlaza;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SUCURSALES";
	
	/* Nombre campos de la tabla */
	static public final String C_BANCOS_CODIGO	= "BANCOS_CODIGO";
	static public final String C_COD_SUCURSAL	= "COD_SUCURSAL";
	static public final String C_NOMBRE			= "NOMBRE";
	static public final String C_DOMICILIO		= "DOMICILIO";
	static public final String C_CODIGOPOSTAL	= "CODIGOPOSTAL";
	static public final String C_COD_PLAZA		= "COD_PLAZA";		
	

	// Metodos SET
	
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public void setCodPlaza(String codPlaza) {
		this.codPlaza = codPlaza;
	}
	public void setCodSucursal(String codSucursal) {
		this.codSucursal = codSucursal;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	// Metodos GET
		
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public String getCodPlaza() {
		return codPlaza;
	}
	public String getCodSucursal() {
		return codSucursal;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public String getNombre() {
		return nombre;
	}
}
