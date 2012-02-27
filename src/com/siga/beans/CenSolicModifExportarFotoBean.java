/*
 * VERSIONES:
 * 
 * miguel.villegas - 24-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_SOLICITMODIFDATOSBASICOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenSolicModifExportarFotoBean extends MasterBean {

	/* Variables */
	private Long idSolicitud;
	private String motivo;
	private String exportarFoto;
	private Integer idInstitucion;	
	private Long idPersona;
	private String fechaAlta;	
	private Integer idEstadoSolic;	
		
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICMODIFEXPORTARFOTO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD 		= "IDSOLICITUD";
	static public final String C_MOTIVO 			= "MOTIVO";	
	static public final String C_EXPORTARFOTO		= "EXPORTARFOTO";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";	

	// Metodos SET
	public void setIdSolicitud (Long id) 	{ this.idSolicitud = id; }
	public void setMotivo (String m)	{ this.motivo = m; }	
	public void setExportarFoto (String p)	{ this.exportarFoto = p; }	
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdPersona (Long id) 	{ this.idPersona = id; }	
	public void setFechaAlta (String fecha)	{ this.fechaAlta = fecha; }	
	public void setIdEstadoSolic (Integer id)	{ this.idEstadoSolic = id; }	

	// Metodos GET
	public Long getIdSolicitud () 	{ return this.idSolicitud; }
	public String getMotivo ()	{ return this.motivo; }	
	public String getExportarFoto()	{ return this.exportarFoto; }	
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdPersona () 	{ return this.idPersona; }	
	public String getFechaAlta ()	{ return this.fechaAlta; }	
	public Integer getIdEstadoSolic ()	{ return this.idEstadoSolic; }	
	
}
