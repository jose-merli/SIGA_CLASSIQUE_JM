/*
 * VERSIONES:
 * 
 * 31/01/2007 Creacion RGG AtosOrigin 
 *	
 */

/**
 */
package com.siga.beans;

public class EnvEstatEnvioBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3821690488839056081L;
	/* Variables */
	private Integer idInstitucion;
	private Integer idEnvio;
	private Integer	idTipoEnvio;
	private Long idEstatEnvio;
	private Long idPersona;
	
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "ENV_ESTAT_ENVIO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDENVIO		= "IDENVIO";	
	static public final String C_IDTIPOENVIO	= "IDTIPOENVIOS";
	static public final String C_IDPERSONA	= "IDPERSONA";
	static public final String C_IDESTATENVIO	= "IDESTATENVIO";
	
	static public final String SEQ_ENV_ESTAT_ENVIO = "SEQ_ENV_ESTAT_ENVIO";

	// Metodos SET
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdEnvio (Integer s) 	{ this.idEnvio = s; }
	public void setIdTipoEnvio (Integer s) 	{ this.idTipoEnvio= s; }
	public void setIdPersona (Long s) 	{ this.idPersona = s; }
	public void setIdEstatEnvio(Long s) {	this.idEstatEnvio = s;}
	
	// Metodos GET
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Integer getIdEnvio () 	{ return this.idEnvio; }
	public Integer getIdTipoEnvio () 	{ return this.idTipoEnvio; }
	public Long getIdPersona () 	{ return this.idPersona; }
	public Long getIdEstatEnvio() {	return idEstatEnvio; }
}
