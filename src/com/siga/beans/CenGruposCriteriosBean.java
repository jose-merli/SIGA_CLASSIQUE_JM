/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

public class CenGruposCriteriosBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion, idGruposCriterios, idConsulta;
	
	private String 	nombre, sentencia;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_GRUPOSCRITERIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDGRUPOSCRITERIOS	= "IDGRUPOSCRITERIOS";
	static public final String C_NOMBRE				= "NOMBRE";
	static public final String C_SENTENCIA			= "SENTENCIA";
	static public final String C_IDCONSULTA			= "IDCONSULTA";

	// Metodos SET
	public void setIdInstitucion (Integer id) 		{ this.idInstitucion = id; }
	public void setIdGruposCriterios (Integer id)	{ this.idGruposCriterios = id; }
	public void setNombre (String s)				{ this.nombre = s; }
	public void setSentencia (String s)				{ this.sentencia = s; }
	public void setIdConsulta (Integer id)			{ this.idConsulta = id; }

	// Metodos GET
	public Integer getIdInstitucion 	()	{ return this.idInstitucion; }
	public Integer getIdGruposCriterios ()	{ return this.idGruposCriterios; }
	public String  getNombre    		()	{ return this.nombre; }
	public String  getSentencia    		()	{ return this.sentencia; }
	public Integer  getIdConsulta  		()	{ return this.idConsulta; }
}
