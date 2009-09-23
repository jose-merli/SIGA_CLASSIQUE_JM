package com.siga.beans;

/**
 * Bean de la tabla SCS_JUZGADOPROCEDIMIENTO
 * 
 * @author david.sanchezp
 * @since 08/02/2006
 */
public class ScsMateriaJurisdiccionBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion, idArea, idMateria, idJurisdiccion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_MATERIAJURISDICCION";
	
	
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDAREA        = 		    "IDAREA";
	static public final String 	C_IDMATERIA     = 		    "IDMATERIA";
	static public final String 	C_IDJURISDICCION = 			"IDJURISDICCION";
	
		
	public void setIdInstitucion(Integer idInstitucion) {
		 this.idInstitucion = idInstitucion;
	}
	
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	
	public void setIdJurisdiccion(Integer idJurisdiccion) {
		this.idJurisdiccion= idJurisdiccion;
	}
	
	
	public void setIdMateria(Integer idMateria) {
		this.idMateria= idMateria;
	}
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	
	public Integer getIdArea() {
		return idArea;
	}
	
	public Integer getIdMateria() {
		return idMateria;
	}
	
	public Integer getIdJurisdiccion() {
		return idJurisdiccion;
	}
	
	
}