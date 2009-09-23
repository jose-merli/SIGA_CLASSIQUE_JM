package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_CONTRARIOSASISTENCIA
 * 
 * @author Luis Miguel Sánchez PIÑA
 * @version David Sanchez Pina: modificaion del tipo de 2 campos de tipo number(10) en base de datos.
 * @since 20/02/2006
 */

public class ScsContrariosAsistenciaBean extends MasterBean
{
	
	/*
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	anio;
	private Long numero;	
	private Long idPersona;
	private String	observaciones;
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_CONTRARIOSASISTENCIA";
	
	
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_NUMERO =					"NUMERO";
	static public final String	C_ANIO =					"ANIO";
	static public final String	C_IDPERSONA	=			 	"IDPERSONA";
	static public final String	C_OBSERVACIONES	=			"OBSERVACIONES";
		
	
	/*
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion de la asistencia 
	 * 
	 * @param valor Identificador de la institucion de la asistencia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion (Integer valor)	{ this.idInstitucion = 	valor;}
	
	/**
	 * Almacena en el Bean el numero de la asistencia 
	 * 
	 * @param valor Numero de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setNumero(Long valor)				{ this.numero =	valor;}

	/**
	 * Almacena en el Bean el anho de la asistencia 
	 * 
	 * @param valor Anho de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio	(Integer valor)	{ this.anio =	valor;}

	/**
	 * Almacena en el Bean la fecha de entrada de la designa 
	 * 
	 * @param valor Fecha de entrada de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setIdPersona(Long valor)			{ this.idPersona =	valor;}

	/** Almacena en el Bean el Observaciones de la designa 
	 * 
	 * @param valor Observaciones del IdTipoDesignaColegiado de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones(String valor)			{ this.observaciones =	valor;}
	
	
	
	/*
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la institucion de la designa
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion ()	{return this.idInstitucion;}

	/**
	 * Recupera del Bean el identificador el numero de la designa
	 * 
	 * @return Numero
	 */
	public Long getNumero()	{return this.numero;}

	/**
	 * Recupera del Bean el anho de la designa
	 * 
	 * @return Anho de la institucion
	 */
	public Integer getAnio	()	{return this.anio;}
	
	/**
	 * Recupera del Bean la fecha de entrada de la designa
	 * 
	 * @return FechaEntrada 
	 */
	public Long getIdPersona()			{ return this.idPersona;}

	/**
	 * Recupera del Bean las observaciones de la designa
	 * 
	 * @return Observaciones de la institucion
	 */
	public String getObservaciones()			{ return this.observaciones;}
}