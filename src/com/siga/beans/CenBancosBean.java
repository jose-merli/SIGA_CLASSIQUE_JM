/*
 * nuria.rgonzalez - 21-03-2005 - Creacion
 *	
 */

package com.siga.beans;

/**
 * Clase que recoge y establece los valores del bean CEN_BANCOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
public class CenBancosBean extends MasterBean {

	/* Variables */	
	private String 	codigo, nombre;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_BANCOS";
	
	/* Nombre campos de la tabla */
	static public final String C_CODIGO		= "CODIGO";
	static public final String C_NOMBRE		= "NOMBRE";

	// Metodos SET
	public void setCodigo(String codigo) {this.codigo = codigo;}	
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	//Metodos GET
	public String getCodigo() {return codigo;}	
	public String getNombre() {return nombre;}
	
}
