package com.siga.beans;

/**
 * Bean de la tabla SCS_DECLARACION
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsDeclaracionBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idDeclaracion;
	private String	descripcion=null;
	

	/**
	 * @return Returns the idDeclaracion.
	 */
	public Integer getIdDeclaracion() {
		return idDeclaracion;
	}
	/**
	 * @param idDeclaracion The idDeclaracion to set.
	 */
	public void setIdDeclaracion(Integer idDeclaracion) {
		this.idDeclaracion = idDeclaracion;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_DELITO";
	
	
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDDECLARACION = 	"IDDECLARACION";
	static public final String 	C_DESCRIPCION = 	"DESCRIPCION";
	
	

}