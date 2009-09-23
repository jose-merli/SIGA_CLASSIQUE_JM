package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_INSCRIPCIONGUARDIA
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */

public class ScsInscripcionGuardiaBean extends MasterBean{
	
	/**
	 *  Variables 
	 * */ 
	private Integer idInstitucion;
	private Long 	idPersona;
	private Integer idTurno;
	private Integer idGuardia;
	private String	fechaSuscripcion;
	private String	fechaBaja;
	private String  observacionesSuscripcion;
	private String  observacionesBaja;
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "SCS_INSCRIPCIONGUARDIA";
	
	
	/**
	 * Nombre de campos de la tabla
	 * */
	
	static public final String 	C_IDPERSONA = 					"IDPERSONA";
	static public final String 	C_IDINSTITUCION = 				"IDINSTITUCION";
	static public final String 	C_IDTURNO = 					"IDTURNO";
	static public final String 	C_IDGUARDIA = 					"IDGUARDIA";
	static public final String 	C_FECHASUSCRIPCION =	 		"FECHASUSCRIPCION";
	static public final String 	C_FECHABAJA = 					"FECHABAJA";
	static public final String 	C_OBSERVACIONESSUSCRIPCION = 	"OBSERVACIONESSUSCRIPCION";
	static public final String 	C_OBSERVACIONESBAJA = 			"OBSERVACIONESBAJA";
	
	
	
	/**
	 * Metodos SET
	 * */
	
	/**
	 * Almacena en el Bean el identificador de persona 
	 * 
	 * @param valor Identificador de la persona apuntada a la guardia. De tipo "Long". 
	 * @return void 
	 */
	public void setIdPersona	 			(Long 	 valor) 	{ this.idPersona = 					valor;}
	/**
	 * Almacena en el Bean el identificador de la institucion 
	 * 
	 * @param valor Identificador de la institucion a la que pertenece la guardia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = 				valor;}
	/**
	 * Almacena en el Bean el identificador del turno
	 * 
	 * @param valor Identificador del turno de la guardia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)		{ this.idTurno = 					valor;}
	/**
	 * Almacena en el Bean el identificador de la guardia
	 * 
	 * @param valor Identificador de la guardia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdGuardia				(Integer valor)		{ this.idGuardia = 					valor;}
	/**
	 * Almacena en el Bean la fecha de suscripcion a la guardia
	 * 
	 * @param valor Fecha de suscripcion de la persona a la guardia. De tipo "String". 
	 * @return void 
	 */
	public void setFechaSuscripcion			(String  valor)		{ this.fechaSuscripcion = 			valor;}
	/**
	 * Almacena en el Bean la fecha de baja de la guardia
	 * 
	 * @param valor Fecha de baja de la persona de la guardia. De tipo "String". 
	 * @return void 
	 */
	public void setFechaBaja				(String  valor)		{ this.fechaBaja = 					valor;}
	/**
	 * Almacena en el Bean las observaciones de la suscripcion
	 * 
	 * @param valor Observaciones de la suscripcion a la guardia. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesSuscripcion	(String  valor)		{ this.observacionesSuscripcion =	valor;}
	/**
	 * Almacena en el Bean las observaciones de la baja
	 * 
	 * @param valor Observaciones de la baja de la guardia. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesBaja		(String  valor)		{ this.observacionesBaja = 			valor;}
	
	
	/**
	 * Metodos GET
	 * */
	
	/**
	 * Recupera del Bean el identificador de la persona
	 * 
	 * @return Identificador de la persona
	 */
	public Long getIdPersona		 			() 	{ return this.idPersona;}
	/**
	 * Recupera del Bean el identificador de la institucion
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion 			() 	{ return this.idInstitucion;}
	/**
	 * Recupera del Bean el identificador del turno
	 * 
	 * @return Identificador del turno
	 */
	public Integer getIdTurno					()	{ return this.idTurno;}
	/**
	 * Recupera del Bean el identificador de la guardia
	 * 
	 * @return Identificador de la guardia
	 */
	public Integer getIdGuardia					()	{ return this.idGuardia;}
	/**
	 * Recupera del Bean fecha de la suscripcion a la guardia
	 * 
	 * @return Fecha de suscripcion a la guardia
	 */
	public String getFechaSuscripcion			()	{ return this.fechaSuscripcion;}
	/**
	 * Recupera del Bean fecha de la baja a la guardia
	 * 
	 * @return Fecha de baja a la guardia
	 */
	public String getFechaBaja					()	{ return this.fechaBaja;}
	/**
	 * Recupera del Bean las observaciones de la suscripcion a la guardia
	 * 
	 * @return Observaciones de la suscripcion a la guardia
	 */
	public String getObservacionesSuscripcion	()	{ return this.observacionesSuscripcion;}
	/**
	 * Recupera del Bean las observaciones de la baja de la guardia
	 * 
	 * @return Observaciones de la baja de la guardia
	 */
	public String getObservacionesBaja			()	{ return this.observacionesBaja;}	
	
}