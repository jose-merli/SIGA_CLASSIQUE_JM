package com.siga.beans;

/**
 * Bean de la tabla SCS_PONENTE
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsPonenteBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion, idPonente;
	private String	nombre=null;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PONENTE";
	
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPONENTE = 				"IDPONENTE";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdPonente() {
		return idPonente;
	}
	public void setIdPonente(Integer idPonente) {
		this.idPonente = idPonente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	

}