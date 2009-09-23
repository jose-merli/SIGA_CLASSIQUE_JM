/*
 * VERSIONES:
 * 
 * miguel.villegas - 19-04-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores de la tabla Cen_Plazas <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla correspondiente 
 */
package com.siga.beans;

public class CenPlazasBean extends MasterBean{

	/* Variables */
	
	private String 	codPlaza;
	private String 	nombre;
	private String  codProvincia;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_PLAZAS";
	
	/* Nombre campos de la tabla */
	static public final String C_COD_PLAZA		= "COD_PLAZA";
	static public final String C_NOMBRE			= "NOMBRE";
	static public final String C_COD_PROVINCIA		= "COD_PROVINCIA";
		
	

	// Metodos SET
	
	public void setCodPlaza(String codPlaza) {
		this.codPlaza = codPlaza;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}
	
	// Metodos GET
		
	public String getCodPlaza() {
		return codPlaza;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getCodProvincia() {
		return this.codProvincia;
	}
}
