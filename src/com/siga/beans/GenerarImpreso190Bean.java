/*
 * Created on 05-ene-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author pilard
 *
 * Clase que recoge y establece los valores del bean ADM_CONTADOR (almacena la configuracion de contadores) <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario
 */
/*
 * pilar.duran - 05-01-2007 - Creacion
 *	
 */


public class GenerarImpreso190Bean extends MasterBean {

	/* Variables */	
	private String 	nombreFichero, idprovincia, telefono, nombre, apellido1, apellido2 ;
	private Integer idinstitucion, anio; 
	
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_CONF_IMPRESO190";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_ANIO = "ANIO";
	static public final String C_NOMBREFICHERO = "NOMBREFICHERO";
	static public final String C_IDPROVINCIA = "IDPROVINCIA";
	static public final String C_TELEFONO = "TELEFONO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_APELLIDO1 = "APELLIDO1";
	static public final String C_APELLIDO2 = "APELLIDO2";
		// Metodos SET
	public void setIdinstitucion(Integer idinstitucion) {this.idinstitucion = idinstitucion;}
	public void setAnio(Integer anio) {this.anio = anio;}	
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @return Returns the idinstitucion.
	 */
	public Integer getIdinstitucion() {
		return idinstitucion;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @return Returns the apellido1.
	 */
	public String getApellido1() {
		return apellido1;
	}
	/**
	 * @param apellido1 The apellido1 to set.
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	/**
	 * @return Returns the apellido2.
	 */
	public String getApellido2() {
		return apellido2;
	}
	/**
	 * @param apellido2 The apellido2 to set.
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	/**
	 * @return Returns the idprovincia.
	 */
	public String getIdprovincia() {
		return idprovincia;
	}
	/**
	 * @param idprovincia The idprovincia to set.
	 */
	public void setIdprovincia(String idprovincia) {
		this.idprovincia = idprovincia;
	}
	/**
	 * @return Returns the nombreFichero.
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}
	/**
	 * @param nombreFichero The nombreFichero to set.
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}

