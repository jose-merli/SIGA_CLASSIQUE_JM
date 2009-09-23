package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_DESIGNALETRADO
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 */

public class ScsDefendidosDesignaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTurno;
	private Integer	anio;
	private Integer numero;
	private Integer idPersona;
	private String	observaciones;
	private String	nombreRepresentande;
	private String	calidad;
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_DEFENDIDOSDESIGNA";
	
	
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_IDTURNO =					"IDTURNO";
	static public final String	C_ANIO =					"ANIO";
	static public final String	C_NUMERO =					"NUMERO";
	static public final String	C_IDPERSONA	=			 	"IDPERSONA";
	static public final String	C_NOMBREREPRESENTANTE	=	"NOMBREREPRESENTANTE";
	static public final String	C_OBSERVACIONES	=			"OBSERVACIONES";
	static public final String	C_CALIDAD	=			"CALIDAD";
		
	
	/*
	 * Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la institucion de la designa 
	 * 
	 * @param valor Identificador de la institucion de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion (Integer valor)	{ this.idInstitucion = 	valor;}
	/**
	 * Almacena en el Bean el identificador del turno de la designa 
	 * 
	 * @param valor Identificador del turno de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdTurno	(Integer valor)	{ this.idTurno =	valor;}
	
	/**
	 * Almacena en el Bean el anho de la designa 
	 * 
	 * @param valor Anho de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setAnio	(Integer valor)	{ this.anio =	valor;}
	
	/**
	 * Almacena en el Bean el numero de la designa 
	 * 
	 * @param valor Numero de la designa. De tipo "Integer". 
	 * @return void 
	 */
	public void setNumero(Integer valor)				{ this.numero =	valor;}
	/**
	 * Almacena en el Bean la fecha de entrada de la designa 
	 * 
	 * @param valor Fecha de entrada de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setIdPersona(Integer valor)			{ this.idPersona =	valor;}
	/**
	 * Almacena en el Bean la fecha de fin de la designa 
	 * 
	 * @param valor Fecha de fin de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setNombreRepresentante(String valor)				{ this.nombreRepresentande =	valor;}
	/** Almacena en el Bean el Observaciones de la designa 
	 * 
	 * @param valor Observaciones del IdTipoDesignaColegiado de la designa. De tipo "String". 
	 * @return void 
	 */
	public void setObservaciones(String valor)			{ this.observaciones =	valor;}
	
	/** Almacena en el Bean la calidad
	 * 
	 * @param valor calidad 
	 * @return void 
	 */
	public void setCalidad(String valor)			{ this.calidad =	valor;}
	
	
	
	/*
	 * Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la institucion de la designa
	 * 
	 * @return Identificador de la institucion
	 */
	public Integer getIdInstitucion ()	{return this.idInstitucion;}
	/**
	 * Recupera del Bean el identificador del turno de la designa
	 * 
	 * @return Identificador del turno
	 */
	public Integer getIdTurno	()	{return this.idTurno;}
	
	/**
	 * Recupera del Bean el anho de la designa
	 * 
	 * @return Anho de la institucion
	 */
	public Integer getAnio	()	{return this.anio;}
	
	/**
	 * Recupera del Bean el identificador el numero de la designa
	 * 
	 * @return Numero
	 */
	public Integer getNumero()	{return this.numero;}
	/**
	 * Recupera del Bean la fecha de entrada de la designa
	 * 
	 * @return FechaEntrada 
	 */
	public Integer getIdPersona()			{ return this.idPersona;}
	/**
	 * Recupera del Bean la fecha de fin de la designa
	 * 
	 * @return FechaFin 
	 */
	public String getNombreRepresentante()				{ return this.nombreRepresentande;}
	/**
	 * Recupera del Bean las observaciones de la designa
	 * 
	 * @return Observaciones de la institucion
	 */
	public String getObservaciones()			{ return this.observaciones;}

	/**
	 * Recupera del Bean la calidad
	 * 
	 * @return calidad
	 */
	public String getCalidad()			{ return this.calidad;}
}