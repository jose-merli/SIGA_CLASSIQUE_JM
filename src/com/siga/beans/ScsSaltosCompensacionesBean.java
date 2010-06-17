package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_SALTOSCOMPENSACIONES
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 * @version 22/03/2006: david.sanchezp nuevo campo idcalendario guardias
 */

public class ScsSaltosCompensacionesBean extends MasterBean{
	public ScsSaltosCompensacionesBean(Integer idInstitucion, Integer idTurno,
			Long idPersona, String saltoCompensacion, String fecha) {
		super();
		this.idInstitucion = idInstitucion;
		this.idTurno = idTurno;
		this.idPersona = idPersona;
		this.saltoCompensacion = saltoCompensacion;
		this.fecha = fecha;
	}
	public ScsSaltosCompensacionesBean() {
		super();
	}
	/**
	 *  Variables */ 
	
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
	
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_SALTOSCOMPENSACIONES";
	
	
	
	/**
	 * Nombre de campos de la tabla*/

	
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
		
	
	/**
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion
	 * 
	 * @param valor Identificador la isntitucion del salto de compensaciones. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion	=			valor;}
	/**
	 * Almacena en el Bean el identificador del turno
	 * 
	 * @param valor Identificador de turno del salto de compensaciones. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)	{ this.idTurno =					valor;}
	/**
	 * Almacena en el Bean el identificador del salto de compenasciones
	 * 
	 * @param valor Identificador del salto de compensaciones. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdSaltosTurno			(Long valor)	{ this.idSaltosTurno =				valor;}
	/**
	 * Almacena en el Bean el identificador de la guardia
	 * 
	 * @param valor Identificador de la guardia del salto de compensaciones. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdGuardia				(Integer valor)	{ this.idGuardia =					valor;}
	/**
	 * Almacena en el Bean el identificador de la persona
	 * 
	 * @param valor Identificador de la persona del salto de compensaciones. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdPersona				(Long valor)	{ this.idPersona =			 		valor;}
	/**
	 * Almacena en el Bean el valor del salto de compensaciones
	 * 
	 * @param valor Valor del salto de compensaciones. De tipo "String". 
	 * @return void 
	 */
	public void setSaltoCompensacion		(String  valor)	{ this.saltoCompensacion = 			valor;}
	/**
	 * Almacena en el Bean la fecha del salto de compensaciones
	 * 
	 * @param valor Fecha del salto de compensaciones. De tipo "String". 
	 * @return void 
	 */
	public void setFecha					(String  valor)	{ this.fecha =			 			valor;}
	/**
	 * Almacena en el Bean los motivos del salto de compensaciones
	 * 
	 * @param valor Motivos del salto de compensaciones. De tipo "String". 
	 * @return void 
	 */
	public void setMotivos					(String  valor)	{ this.motivos = 					valor;}
	/**
	 * Almacena en el Bean la fecha del cumplimiento del salto de compensaciones
	 * 
	 * @param valor Fecha del cumplimiento del salto de compensaciones. De tipo "String". 
	 * @return void 
	 */
	public void setFechaCumplimiento		(String  valor)	{ this.fechaCumplimiento =			valor;}
	
	
	/**
	 * Metodos GET
	 * */
	
	/**
	 * Recupera del Bean el identificador de la institucion del salto de compensaciones
	 * 
	 * @return Identificador de la institucion del salto de compensaciones
	 */
	public Integer getIdInstitucion			()	{ return this.idInstitucion;}
	/**
	 * Recupera del Bean el identificador del turno
	 * 
	 * @return Identificador del turno del salto de compensaciones
	 */
	public Integer getIdTurno				()	{ return this.idTurno;}
	/**
	 * Recupera del Bean el identificador del salto de compensaciones
	 * 
	 * @return Identificador del salto de compensaciones
	 */
	public Long getIdSaltosTurno			()	{ return this.idSaltosTurno;}
	/**
	 * Recupera del Bean el identificador de la guardia
	 * 
	 * @return Identificador de la guardia del salto de compensaciones
	 */
	public Integer getIdGuardia				()	{ return this.idGuardia;}
	/**
	 * Recupera del Bean el identificador de la persona
	 * 
	 * @return Identificador de la persona del salto de compensaciones
	 */
	public Long getIdPersona				()	{ return this.idPersona;}
	/**
	 * Recupera del Bean el valor del salto de compensaciones
	 * 
	 * @return Valor del salto de compensaciones
	 */
	public String getSaltoCompensacion		()	{ return this.saltoCompensacion;}
	/**
	 * Recupera del Bean la fecha
	 * 
	 * @return Fecha del salto de compensaciones
	 */
	public String getFecha					()	{ return this.fecha;}
	/**
	 * Recupera del Bean el motivo del salto de compensaciones
	 * 
	 * @return Motivo del salto de compensaciones
	 */
	public String getMotivos				()	{ return this.motivos;}
	/**
	 * Recupera del Bean la fecha de cumplimiento
	 * 
	 * @return Fecha de cumplimiento del salto de compensaciones
	 */
	public String getFechaCumplimiento		()	{ return this.fechaCumplimiento;}
	

	/**
	 * @return Returns the idCalendarioGuardias.
	 */
	public Integer getIdCalendarioGuardias() {
		return idCalendarioGuardias;
	}
	/**
	 * @param idCalendarioGuardias The idCalendarioGuardias to set.
	 */
	public void setIdCalendarioGuardias(Integer idCalendarioGuardias) {
		this.idCalendarioGuardias = idCalendarioGuardias;
	}
	

	/**
	 * @return Returns the idCalendarioGuardiasCreacion.
	 */
	public Integer getIdCalendarioGuardiasCreacion() {
		return idCalendarioGuardiasCreacion;
	}
	/**
	 * @param idCalendarioGuardiasCreacion The idCalendarioGuardiasCreacion to set.
	 */
	public void setIdCalendarioGuardiasCreacion(Integer idCalendarioGuardiasCreacion) {
		this.idCalendarioGuardiasCreacion = idCalendarioGuardiasCreacion;
	}
	
	
}