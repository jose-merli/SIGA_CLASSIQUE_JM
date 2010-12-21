package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_CALENDARIOGUARDIAS
 * @since 06/12/2004
 */
public class ScsCalendarioGuardiasBean extends MasterBean
{
	// Atributos
	private Integer	idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer	idCalendarioGuardias;
	private String	fechaInicio;
	private String	fechaFin;
	private String  observaciones;
	private Long idPersonaUltimoAnterior;
	private String	fechaSuscUltimoAnterior;
	
	
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_CALENDARIOGUARDIAS";
	
	// Nombre de campos de la tabla
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	static public final String C_IDTURNO					= "IDTURNO";
	static public final String C_IDGUARDIA					= "IDGUARDIA";
	static public final String C_IDCALENDARIOGUARDIAS		= "IDCALENDARIOGUARDIAS";
	static public final String C_FECHAINICIO				= "FECHAINICIO";
	static public final String C_FECHAFIN					= "FECHAFIN";
	static public final String C_OBSERVACIONES				= "OBSERVACIONES";
	static public final String C_IDPERSONA_ULTIMOANTERIOR	= "IDPERSONA_ULTIMOANTERIOR";
	static public final String C_FECHASUSC_ULTIMOANTERIOR	= "FECHASUSC_ULTIMOANTERIOR";
	
	
	/** Constructor */
	public ScsCalendarioGuardiasBean() {
		
	}
	public ScsCalendarioGuardiasBean(Integer idInstitucion, Integer idTurno, Integer idGuardia,
			Integer idCalendarioGuardias, String fechaInicio, String fechaFin, String observaciones)
	{
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idCalendarioGuardias = idCalendarioGuardias;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.observaciones = observaciones;
	}	
	
	
	// Setters
	public void setIdInstitucion			(Integer idInstitucion)				{this.idInstitucion				= idInstitucion;}
	public void setIdTurno					(Integer idTurno)					{this.idTurno					= idTurno;}
	public void setIdGuardia				(Integer idGuardia)					{this.idGuardia					= idGuardia;}
	public void setIdCalendarioGuardias		(Integer idCalendarioGuardias)		{this.idCalendarioGuardias		= idCalendarioGuardias;}
	public void setFechaInicio				(String  fechaInicio)				{this.fechaInicio				= fechaInicio;}
	public void setFechaFin					(String  fechaFin)					{this.fechaFin					= fechaFin;}
	public void setObservaciones			(String  observaciones)				{this.observaciones				= observaciones;}
	public void setIdPersonaUltimoAnterior	(Long    idPersonaUltimoAnterior)	{this.idPersonaUltimoAnterior	= idPersonaUltimoAnterior;}
	public void setFechaSuscUltimoAnterior	(String	 fechaSuscUltimoAnterior)	{this.fechaSuscUltimoAnterior	= fechaSuscUltimoAnterior;}
	
	
	// Getters
	public Integer getIdInstitucion				() {return idInstitucion;}
	public Integer getIdTurno					() {return idTurno;}
	public Integer getIdGuardia					() {return idGuardia;}
	public Integer getIdCalendarioGuardias		() {return idCalendarioGuardias;}
	public String  getFechaInicio				() {return fechaInicio;}
	public String  getFechaFin					() {return fechaFin;}
	public String  getObservaciones				() {return observaciones;}
	public Long	   getIdPersonaUltimoAnterior	() {return idPersonaUltimoAnterior;}
	public String  getFechaSuscUltimoAnterior	() {return fechaSuscUltimoAnterior;}
	
	
	public String toString() {
		return "Cal. " + this.idTurno + "." + this.idGuardia + "." + this.idCalendarioGuardias + "-" + this.fechaInicio + "-" + this.fechaFin;
	}
	
}