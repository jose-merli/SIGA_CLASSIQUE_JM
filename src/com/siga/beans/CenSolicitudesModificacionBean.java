/*
 * VERSIONES:
 * 
 * miguel.villegas - 04-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CENSOLICITUDESMODIFICACION <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenSolicitudesModificacionBean extends MasterBean{

	/* Variables */
	private Long idSolicitud;
	private String 	descripcion;	
	private Integer idInstitucion;
	private Long idPersona;	
	private Integer idTipoModificacion;
	private String fechaAlta;	
	private Integer idEstadoSolic;
	private Integer idInstitucionOrigen;
	private Integer idUsuModificacionOrigen;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITUDESMODIFICACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD	= "IDSOLICITUD";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_IDPERSONA		= "IDPERSONA";
	static public final String C_IDTIPOMODIFICACION	= "IDTIPOMODIFICACION";
	static public final String C_FECHAALTA	= "FECHAALTA";
	static public final String C_IDESTADOSOLIC	= "IDESTADOSOLIC";	
	static public final String C_IDINSTITUCIONORIGEN	= "IDINSTITUCIONORIGEN";
	static public final String C_USUMODIFICACIONORIGEN	= "USUMODIFICACIONORIGEN";	
	

	// Metodos SET
	public void setIdSolicitud (Long id) 	{ this.idSolicitud = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }	
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdPersona (Long id) 	{ this.idPersona = id; }	
	public void setIdTipoModificacion (Integer id)	{ this.idTipoModificacion = id; }
	public void setFechaAlta (String fecha)	{ this.fechaAlta = fecha; }	
	public void setIdEstadoSolic (Integer id)	{ this.idEstadoSolic = id; }	
	public void setIdInstitucionOrigen (Integer id)	{ this.idInstitucionOrigen = id; }	
	public void setUsuModificacionOrigen (Integer id)	{ this.idUsuModificacionOrigen = id; }	

	// Metodos GET
	public Long getIdSolicitud () 	{ return this.idSolicitud; }
	public String getDescripcion ()	{ return this.descripcion; }	
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdPersona () 	{ return this.idPersona; }
	public Integer getIdTipoModificacion ()	{ return this.idTipoModificacion; }
	public String getFechaAlta ()	{ return this.fechaAlta; }	
	public Integer getIdEstadoSolic ()	{ return this.idEstadoSolic; }	
	public Integer getIdInstitucionOrigen ()	{ return this.idInstitucionOrigen; }	
	public Integer getUsuModificacionOrigen ()	{ return this.idUsuModificacionOrigen; }	
}
