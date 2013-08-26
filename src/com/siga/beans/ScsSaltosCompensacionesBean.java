package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_SALTOSCOMPENSACIONES
 */
public class ScsSaltosCompensacionesBean extends MasterBean
{
	// Campos
	private Integer	idInstitucion;
	private Integer idTurno;
	private Long	idSaltosTurno;
	private Long	idPersona;
	private String	saltoCompensacion;
	private String	fecha;
	private Integer idGuardia;
	private String  motivos;
	private String  fechaCumplimiento;
	private Integer idCalendarioGuardias;
	private Integer idCalendarioGuardiasCreacion;
	private Integer tipoManual;
	
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_SALTOSCOMPENSACIONES";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION	=		"IDINSTITUCION";
	static public final String 	C_IDTURNO	=			"IDTURNO";
	static public final String 	C_IDSALTOSTURNO	=		"IDSALTOSTURNO";
	static public final String 	C_IDGUARDIA	=			"IDGUARDIA";
	static public final String 	C_IDPERSONA	=			"IDPERSONA";
	static public final String 	C_SALTOCOMPENSACION	=	"SALTOOCOMPENSACION";
	static public final String 	C_FECHA =				"FECHA";
	static public final String 	C_MOTIVOS =				"MOTIVOS";
	static public final String  C_FECHACUMPLIMIENTO = 	"FECHACUMPLIMIENTO";
	static public final String  C_IDCALENDARIOGUARDIAS = "IDCALENDARIOGUARDIAS";
	static public final String  C_IDCALENDARIOGUARDIASCREACION = "IDCALENDARIOGUARDIASCREACION";
	static public final String  C_TIPOMANUAL = "TIPOMANUAL";
	
	
	// Constructor
	public ScsSaltosCompensacionesBean(Integer idInstitucion,
									   Integer idTurno,
									   Long idPersona,
									   String saltoCompensacion,
									   String fecha) {
		super();
		
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idPersona = idPersona;
		this.saltoCompensacion = saltoCompensacion;
		this.fecha = fecha;
	}
	/** Constructor de nuevo salto o compensacion */
	public ScsSaltosCompensacionesBean(Integer idInstitucion,
									   Integer idTurno,
									   Integer idGuardia,
									   Integer idCalendarioGuardia,
									   Long idPersona,
									   String saltoCompensacion,
									   String fecha) {
		super();
		
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idCalendarioGuardiasCreacion = idCalendarioGuardia;
		this.idPersona = idPersona;
		this.saltoCompensacion = saltoCompensacion;
		this.fecha = fecha;
	}
	/** Constructor de salto o compensacion ya existente */
	public ScsSaltosCompensacionesBean(Integer idInstitucion,
									   Integer idTurno,
									   Integer idGuardia,
									   Long idPersona,
									   String saltoCompensacion) {
		super();
		
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idGuardia = idGuardia;
		this.idPersona = idPersona;
		this.saltoCompensacion = saltoCompensacion;
	}

	public ScsSaltosCompensacionesBean() {
		super();
	}
		
	
	// GETTERS
	public Integer getIdInstitucion					()	{ return idInstitucion; }
	public Integer getIdTurno						()	{ return idTurno; }
	public Long getIdSaltosTurno					()	{ return idSaltosTurno; }
	public Integer getIdGuardia						()	{ return idGuardia; }
	public Long getIdPersona						()	{ return idPersona; }
	public String getSaltoCompensacion				()	{ return saltoCompensacion; }
	public String getFecha							()	{ return fecha; }
	public String getMotivos						()	{ return motivos; }
	public String getFechaCumplimiento				()	{ return fechaCumplimiento; }
	public Integer getIdCalendarioGuardias			()	{ return idCalendarioGuardias; }
	public Integer getIdCalendarioGuardiasCreacion	()	{ return idCalendarioGuardiasCreacion; }
	public Integer getTipoManual					()	{ return tipoManual; }
	
	// SETTERS
	public void setIdInstitucion				(Integer valor)	{ this.idInstitucion				= valor; }
	public void setIdTurno						(Integer valor)	{ this.idTurno						= valor; }
	public void setIdSaltosTurno				(Long valor)	{ this.idSaltosTurno				= valor; }
	public void setIdGuardia					(Integer valor)	{ this.idGuardia					= valor; }
	public void setIdPersona					(Long valor)	{ this.idPersona					= valor; }
	public void setSaltoCompensacion			(String  valor)	{ this.saltoCompensacion			= valor; }
	public void setFecha						(String  valor)	{ this.fecha						= valor; }
	public void setMotivos						(String  valor)	{ this.motivos						= valor; }
	public void setFechaCumplimiento			(String  valor)	{ this.fechaCumplimiento			= valor; }
	public void setIdCalendarioGuardias			(Integer valor) { this.idCalendarioGuardias			= valor; }
	public void setIdCalendarioGuardiasCreacion	(Integer valor)	{ this.idCalendarioGuardiasCreacion	= valor; }
	public void setTipoManual					(Integer valor)	{ this.tipoManual					= valor; }

}