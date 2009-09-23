/*
 * VERSIONES:
 * raul.ggonzalez - 31-01-2006 - Creación
 */

package com.siga.beans;

/**
 * Clase Bean de la tabla SCS_SUBZONAPARTIDO
 * @author AtosOrigin 21-01-2005
 */

public class ScsSubZonaPartidoBean extends MasterBean{

	/* Variables */
	private Integer	idInstitucion;
	private Integer	idZona;
	private Integer	idSubZona;
	private Integer idPartido;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_SUBZONAPARTIDO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDZONA					= "IDZONA";
	static public final String C_IDSUBZONA				= "IDSUBZONA";
	static public final String C_IDPARTIDO				= "IDPARTIDO";

	// Metodos SET
	public void setIdPartido (Integer id) { this.idPartido = id; }
	public void setIdInstitucion (Integer id) { this.idInstitucion = id; }
	public void setIdSubZona (Integer id) { this.idSubZona = id; }
	public void setIdZona (Integer id) { this.idZona = id; }

	// Metodos GET
	public Integer getIdPartido ()	{ return this.idPartido; }
	public Integer getIdInstitucion  ()	{ return this.idInstitucion; }
	public Integer getIdSubZona  ()	{ return this.idSubZona; }
	public Integer getIdZona() {
		return idZona;
	}
}