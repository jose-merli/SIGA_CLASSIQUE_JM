/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creaci�n
 */

package com.siga.beans;

public class CenGruposClienteBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5211338729479965853L;
	/* Variables */
	private Integer idGrupo;
	private String 	nombre;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_GRUPOSCLIENTE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDGRUPO		= "IDGRUPO";
	static public final String C_NOMBRE			= "NOMBRE";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";

	// Metodos SET
	public void setIdGrupo (Integer id) { this.idGrupo = id; }
	public void setNombre (String s)	{ this.nombre = s; }

	// Metodos GET
	public Integer getIdGrupo  ()	{ return this.idGrupo; }
	public String getNombre    ()	{ return this.nombre; }
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
}
