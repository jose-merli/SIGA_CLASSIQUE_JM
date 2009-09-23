package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_CALENDARIOGUARDIAS
 * 
 * @author ruben.fernandez
 * @since 06/12/2004
 */


public class ScsCalendarioGuardiasBean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private Integer	idCalendarioGuardias;
	private String	fechaInicio;
	private String	fechaFin;
	private String  observaciones;
	private Integer idPersonaUltimoAnterior;
	

	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_CALENDARIOGUARDIAS";
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION	=			"IDINSTITUCION";
	static public final String 	C_IDTURNO	=				"IDTURNO";
	static public final String 	C_IDGUARDIA	=				"IDGUARDIA";
	static public final String 	C_IDCALENDARIOGUARDIAS	=	"IDCALENDARIOGUARDIAS";
	static public final String 	C_FECHAINICIO =				"FECHAINICIO";
	static public final String 	C_FECHAFIN =				"FECHAFIN";
	static public final String  C_OBSERVACIONES = 			"OBSERVACIONES";
	static public final String  C_IDPERSONA_ULTIMOANTERIOR ="IDPERSONA_ULTIMOANTERIOR";

	/**
	 * Metodos SET*/
	/**
	 * Almacena en el Bean la institucion a la que pertenece un calendario de guardias 
	 * 
	 * @param valor Institucion calendario de guardias. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion	=			valor;}
	/**
	 * Almacena en el Bean el turno al que pertenece un calendario de guardias 
	 * 
	 * @param valor Identificador turno. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)	{ this.idTurno =					valor;}
	/**
	 * Almacena en el Bean el identificador de una guardia que pertenece al calendario de guardias
	 * 
	 * @param valor Identificador guardia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdGuardia				(Integer valor)	{ this.idGuardia =					valor;}
	/**
	 * Almacena en el Bean el identificador de un calendario de guardias 
	 * 
	 * @param valor Identificador calendario de guardias. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdCalendarioGuardias		(Integer valor)	{ this.idCalendarioGuardias = 		valor;}
	/**
	 * Almacena en el Bean la fecha de inicio de un calendario de guardias 
	 * 
	 * @param valor Fecha de inicio de calendario de guardias. De tipo "String". 
	 * @return void 
	 */
	public void setFechaInicio				(String  valor)	{ this.fechaInicio =	 			valor;}
	/**
	 * Almacena en el Bean la fecha de fin de un calendario de guardias 
	 * 
	 * @param valor Fecha de fin de calendario de guardias. De tipo "String". 
	 * @return void 
	 */
	public void setFechaFin					(String  valor)	{ this.fechaFin = 					valor;}
	/**
	 * Almacena en el Bean las observaciones de un calendario de guardias 
	 * 
	 * @param valor observaciones de calendario de guardias. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones			(String  valor)	{ this.observaciones = 				valor;}
	
	
	/**
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el Identificador de la institucion a la que pertenece el calendario de guardias 
	 * 
	 * @return Identificador de la institucion a la que pertenece el calendario de guardias. De tipo "Integer" 
	 */
	public Integer getIdInstitucion			()	{ return this.idInstitucion;			}
	/**
	 * Recupera del Bean el Identificador del tunro al que pertenece el calendario de guardias 
	 * 
	 * @return Identificador del turno al que pertenece el calendario de guardias. De tipo "Integer" 
	 */
	public Integer getIdTurno				()	{ return this.idTurno;					}
	/**
	 * Recupera del Bean el Identificador de la guardia que pertenece al calendario de guardias 
	 * 
	 * @return Identificador de la guardia que pertenece al calendario de guardias. De tipo "Integer" 
	 */
	public Integer getIdGuardia				()	{ return this.idGuardia;				}
	/**
	 * Recupera del Bean el Identificador del calendario de la guardia
	 * 
	 * @return Identificador del calendario de la guardia. De tipo "Integer" 
	 */
	public Integer getIdCalendarioGuardias	()	{ return this.idCalendarioGuardias;		}
	/**
	 * Recupera del Bean la fecha de inicio del calendario de la guardia
	 * 
	 * @return Fecha de inicio del calendario de la guardia. De tipo "String" 
	 */
	public String  getFechaInicio			()	{ return this.fechaInicio;				}
	/**
	 * Recupera del Bean la fecha de fin del calendario de la guardia
	 * 
	 * @return Fecha de fin del calendario de la guardia. De tipo "String" 
	 */
	public String  getFechaFin				()	{ return this.fechaFin;					}
	/**
	 * Recupera del Bean las observaciones del calendario de la guardia
	 * 
	 * @return observaciones del calendario de la guardia. De tipo "String" 
	 */
	public String  getObservaciones			()	{ return this.observaciones;			}
	public Integer getIdPersonaUltimoAnterior() {
		return idPersonaUltimoAnterior;
	}
	public void setIdPersonaUltimoAnterior(Integer idPersonaUltimoAnterior) {
		this.idPersonaUltimoAnterior = idPersonaUltimoAnterior;
	}
	
}