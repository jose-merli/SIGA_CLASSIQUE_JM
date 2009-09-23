/*
 * VERSIONES:
 * raul.ggonzalez - 21-01-2005 - Creación
 */

package com.siga.beans;

/**
 * Clase Bean de la tabla CEN_GRUPOSCLIENTE_CLIENTE
 * @author AtosOrigin 21-01-2005
 */

public class CenGruposClienteClienteBean extends MasterBean{

	/* Variables */
	private Long 	idPersona;
	private Integer	idInstitucion;
	private Integer	idInstitucionGrupo;
	private Integer idGrupo;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_GRUPOSCLIENTE_CLIENTE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDINSTITUCION_GRUPO	= "IDINSTITUCION_GRUPO";
	static public final String C_IDGRUPO				= "IDGRUPO";

	// Metodos SET
	public void setIdPersona (Long id) { this.idPersona = id; }
	public void setIdInstitucion (Integer id) { this.idInstitucion = id; }
	public void setIdGrupo (Integer id) { this.idGrupo = id; }

	// Metodos GET
	public Long getIdPersona  ()	{ return this.idPersona; }
	public Integer getIdInstitucion  ()	{ return this.idInstitucion; }
	public Integer getIdGrupo  ()	{ return this.idGrupo; }
	public Integer getIdInstitucionGrupo() {
		return idInstitucionGrupo;
	}
	public void setIdInstitucionGrupo(Integer idInstitucionGrupo) {
		this.idInstitucionGrupo = idInstitucionGrupo;
	}
}