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

public class CenSolicitModifDatosBasicosBean extends MasterBean {

	/* Variables */
	private Long idSolicitud;
	private String motivo;
	private String publicidad;
	private String guiaJudicial;	
	private String abonos;
	private String cargos;	
	private Integer idInstitucion;	
	private Long idPersona;
	private String idLenguaje;
	private String fechaAlta;	
	private Integer idEstadoSolic;	
		
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITMODIFDATOSBASICOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD 		= "IDSOLICITUD";
	static public final String C_MOTIVO 			= "MOTIVO";	
	static public final String C_PUBLICIDAD			= "PUBLICIDAD";
	static public final String C_GUIAJUDICIAL		= "GUIAJUDICIAL";	
	static public final String C_ABONOS 			= "ABONOS";	
	static public final String C_CARGOS				= "CARGOS";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDLENGUAJE			= "IDLENGUAJE";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";	

	// Metodos SET
	public void setIdSolicitud (Long id) 	{ this.idSolicitud = id; }
	public void setMotivo (String m)	{ this.motivo = m; }	
	public void setPublicidad (String p)	{ this.publicidad = p; }	
	public void setGuiaJudicial (String gj)	{ this.guiaJudicial = gj; }	
	public void setAbonos (String a)	{ this.abonos = a; }	
	public void setCargos (String c)	{ this.cargos = c; }	
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdPersona (Long id) 	{ this.idPersona = id; }	
	public void setIdLenguaje (String l)	{ this.idLenguaje = l; }
	public void setFechaAlta (String fecha)	{ this.fechaAlta = fecha; }	
	public void setIdEstadoSolic (Integer id)	{ this.idEstadoSolic = id; }	

	// Metodos GET
	public Long getIdSolicitud () 	{ return this.idSolicitud; }
	public String getMotivo ()	{ return this.motivo; }	
	public String getPublicidad ()	{ return this.publicidad; }	
	public String getGuiaJudicial ()	{ return this.guiaJudicial; }	
	public String getAbonos ()	{ return this.abonos; }	
	public String getCargos ()	{ return this.cargos; }	
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Long getIdPersona () 	{ return this.idPersona; }	
	public String getIdLenguaje ()	{ return this.idLenguaje; }
	public String getFechaAlta ()	{ return this.fechaAlta; }	
	public Integer getIdEstadoSolic ()	{ return this.idEstadoSolic; }	
	
}
