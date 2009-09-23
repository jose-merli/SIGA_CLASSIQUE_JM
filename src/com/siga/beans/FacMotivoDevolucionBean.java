/*
 * VERSIONES:
 * RGG 03/01/2007 Creacion
 */
package com.siga.beans;

/**
 * Bean de la tabla FAC_MOTIVODEVOLUCION
 * @author RGG AtosOrigin
 *
 */
public class FacMotivoDevolucionBean extends MasterBean {

	/* Variables */	
	private String 	idMotivo, codigo, nombre;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_MOTIVODEVOLUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDMOTIVO		= "IDMOTIVO";
	static public final String C_CODIGO		= "CODIGO";
	static public final String C_NOMBRE		= "NOMBRE";

	// Metodos SET
	public void setIdMotivo(String idMotivo) {this.idMotivo = idMotivo;}	
	public void setCodigo(String codigo) {this.codigo = codigo;}	
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	//Metodos GET
	public String getIdMotivo() {return idMotivo;}	
	public String getCodigo() {return codigo;}	
	public String getNombre() {return nombre;}
	
}
