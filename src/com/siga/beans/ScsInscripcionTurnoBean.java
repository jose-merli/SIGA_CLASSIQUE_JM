package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_INSCRIPCIONTURNO
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */



public class ScsInscripcionTurnoBean extends MasterBean{
	
	/* Variables */ 
	
	private Long 	idPersona;
	private Integer idInstitucion;
	private Integer idTurno;
	private String	fechaSolicitud;
	private String	fechaValidacion;
	private String	fechaSolicitudBaja;
	private String	fechaBaja;
	private String  observacionesSolicitud;
	private String  observacionesValidacion;
	private String  observacionesBaja;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_INSCRIPCIONTURNO";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDTURNO = 				"IDTURNO";
	static public final String 	C_FECHASOLICITUD = 			"FECHASOLICITUD";
	static public final String 	C_FECHAVALIDACION = 		"FECHAVALIDACION";
	static public final String 	C_FECHABAJA = 				"FECHABAJA";
	static public final String 	C_FECHASOLICITUDBAJA =		"FECHASOLICITUDBAJA";
	static public final String 	C_OBSERVACIONESSOLICITUD = 	"OBSERVACIONESSOLICITUD";
	static public final String 	C_OBSERVACIONESVALIDACION = "OBSERVACIONESVALIDACION";
	static public final String 	C_OBSERVACIONESBAJA = 		"OBSERVACIONESBAJA";
	
	
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de persona 
	 * 
	 * @param valor Identificador de la persona apuntada al turno. De tipo "Long". 
	 * @return void 
	 */
	public void setIdPersona	 			(Long valor) 	{ this.idPersona = valor;}
	/**
	 * Almacena en el Bean el identificador de la institucion 
	 * 
	 * @param valor Identificador de la institucion a la que pertenece el turno. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = valor;}
	/**
	 * Almacena en el Bean el identificador del turno
	 * 
	 * @param valor Identificador del turno. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno					(Integer valor)		{ this.idTurno = valor;}
	/**
	 * Almacena en el Bean la fecha de solicitud
	 * 
	 * @param valor Fecha de solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaSolicitud			(String valor)	{ this.fechaSolicitud = valor;}
	/**
	 * Almacena en el Bean la fecha de validacion
	 * 
	 * @param valor Fecha de validacion de la solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaValidacion			(String valor)	{ this.fechaValidacion = valor;}
	/**
	 * Almacena en el Bean la fecha de baja
	 * 
	 * @param valor Fecha de validacion de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaBaja				(String valor)	{ this.fechaBaja = valor;}
	/**
	 * Almacena en el Bean la fecha de solicitud de la baja
	 * 
	 * @param valor Fecha de solicitud de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setFechaSolicitudBaja				(String valor)	{ this.fechaSolicitudBaja = valor;}
	/**
	 * Almacena en el Bean las observaciones de solicitud
	 * 
	 * @param valor Observaciones de solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesSolicitud	(String valor)	{ this.observacionesSolicitud = valor;}
	/**
	 * Almacena en el Bean las observaciones de validacion
	 * 
	 * @param valor Observaciones de validacion de la solicitud de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesValidacion	(String valor)	{ this.observacionesValidacion = valor;}
	/**
	 * Almacena en el Bean las observaciones de baja
	 * 
	 * @param valor Observaciones de validacion de baja de la persona al turno. De tipo "String". 
	 * @return void 
	 */
	public void setObservacionesBaja		(String valor)	{ this.observacionesBaja = valor;}
	
	
	/*Metodos GET*/
	
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
	 * Recupera del Bean la fecha de solicitud
	 * 
	 * @return Fecha de solicitud del turno
	 */
	public String getFechaSolicitud			()	{ return this.fechaSolicitud;}
	/**
	 * Recupera del Bean la fecha de validacion
	 * 
	 * @return Fecha de validacion del turno
	 */
	public String getFechaValidacion		()	{ return this.fechaValidacion;}
	/**
	 * Recupera del Bean la fecha de baja
	 * 
	 * @return Fecha de solicitud baja del turno
	 */
	public String getFechaSolicitudBaja				()	{ return this.fechaSolicitudBaja;}
	/**
	 * Recupera del Bean la fecha de baja
	 * 
	 * @return Fecha de baja del turno
	 */
	public String getFechaBaja				()	{ return this.fechaBaja;}
	/**
	 * Recupera del Bean las observaciones de solicitud
	 * 
	 * @return Observaciones de solicitud del turno
	 */
	public String getObservacionesSolicitud	()	{ return this.observacionesSolicitud;}
	/**
	 * Recupera del Bean las observaciones de validacion
	 * 
	 * @return Observaciones de validacion del turno
	 */
	public String getObservacionesValidacion()	{ return this.observacionesValidacion;}
	/**
	 * Recupera del Bean las observaciones de baja
	 * 
	 * @return Observaciones de baja del turno
	 */
	public String getObservacionesBaja		()	{ return this.observacionesBaja;}	
	
}