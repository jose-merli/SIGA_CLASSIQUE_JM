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
	private Integer idPersonaUltimoAnterior;
	
	
	/** Nombre de Tabla */
	static public String T_NOMBRETABLA = "SCS_CALENDARIOGUARDIAS";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION	=			"IDINSTITUCION";
	static public final String 	C_IDTURNO	=				"IDTURNO";
	static public final String 	C_IDGUARDIA	=				"IDGUARDIA";
	static public final String 	C_IDCALENDARIOGUARDIAS	=	"IDCALENDARIOGUARDIAS";
	static public final String 	C_FECHAINICIO =				"FECHAINICIO";
	static public final String 	C_FECHAFIN =				"FECHAFIN";
	static public final String  C_OBSERVACIONES = 			"OBSERVACIONES";
	static public final String  C_IDPERSONA_ULTIMOANTERIOR ="IDPERSONA_ULTIMOANTERIOR";
	
	
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
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion	=			valor;}
	public void setIdTurno					(Integer valor)	{ this.idTurno =					valor;}
	public void setIdGuardia				(Integer valor)	{ this.idGuardia =					valor;}
	public void setIdCalendarioGuardias		(Integer valor)	{ this.idCalendarioGuardias = 		valor;}
	public void setFechaInicio				(String  valor)	{ this.fechaInicio =	 			valor;}
	public void setFechaFin					(String  valor)	{ this.fechaFin = 					valor;}
	public void setObservaciones			(String  valor)	{ this.observaciones = 				valor;}
	public void setIdPersonaUltimoAnterior(Integer idPersonaUltimoAnterior) {this.idPersonaUltimoAnterior = idPersonaUltimoAnterior;}
	
	
	// Getters
	public Integer getIdInstitucion			()	{ return this.idInstitucion;			}
	public Integer getIdTurno				()	{ return this.idTurno;					}
	public Integer getIdGuardia				()	{ return this.idGuardia;				}
	public Integer getIdCalendarioGuardias	()	{ return this.idCalendarioGuardias;		}
	public String  getFechaInicio			()	{ return this.fechaInicio;				}
	public String  getFechaFin				()	{ return this.fechaFin;					}
	public String  getObservaciones			()	{ return this.observaciones;			}
	public Integer getIdPersonaUltimoAnterior() {return idPersonaUltimoAnterior;}
	
	
	public String toString() {
		return "Cal. " + this.idTurno + "." + this.idGuardia + "." + this.idCalendarioGuardias + "-" + this.fechaInicio + "-" + this.fechaFin;
	}
	
}