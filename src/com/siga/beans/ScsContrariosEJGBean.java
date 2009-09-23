/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsContrariosEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion;
	private Integer	idTipoEJG;
	private Integer	anio;
	private Integer	numero;
	private Integer	idPersona;
	private String	observaciones;
	private Long	idProcurador;
	private Integer	idInstitucionProcurador;	
			
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_CONTRARIOSEJG";
	
	
	/* Nombre de campos de la tabla */
	
	static public final String  C_IDINSTITUCION 	= 				"IDINSTITUCION";
	static public final String 	C_ANIO 				= 				"ANIO";
	static public final String 	C_NUMERO 			= 				"NUMERO";
	static public final String	C_IDTIPOEJG 		=				"IDTIPOEJG";
	static public final String 	C_IDPERSONA			= 				"IDPERSONA";
	static public final String	C_OBSERVACIONES		=				"OBSERVACIONES";
	static public final String 	C_IDINSTITUCION_PROCU = 			"IDINSTITUCION_PROCU";
	static public final String	C_IDPROCURADOR		=				"IDPROCURADOR";

		
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el identificador del tipo de EJG
	 * 
	 * @param valor Identificador tipo EJG 
	 * @return void 
	 */
	public void setIdTipoEJG					(Integer valor)	{ this.idTipoEJG = valor;}
	
	/**
	 * Almacena en el Bean el anho de un EJG
	 * 
	 * @param valor Anho EJG  
	 * @return void 
	 */
	public void setAnio							(Integer valor)	{ this.anio = valor;}
	
	/**
	 * Almacena en el Bean el número de un EJG
	 * 
	 * @param valor Número EJG 
	 * @return void 
	 */
	public void setNumero						(Integer valor)	{ this.numero = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la persona
	 * 
	 * @param valor Identificador persona 
	 * @return void 
	 */
	public void setIdPersona 					(Integer valor) { this.idPersona = valor;}
	
	/**
	 * Almacena en el Bean las observaciones 
	 * 
	 * @param valor Observaciones 
	 * @return void 
	 */
	public void setObservaciones				(String valor) { this.observaciones = valor;}	
	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un EJG
	 * 
	 * @param valor Institucion EJG 
	 * @return void 
	 */
	public void setIdInstitucion 				(Integer valor) { this.idInstitucion = valor;}
	
	/**
	 * Almacena en el Bean el procurador al que pertenece un EJG
	 * 
	 * @param valor Identificador del procurador 
	 * @return void 
	 */
	public void setIdProcurador(Long valor) {
		this.idProcurador = valor;
	}

	/**
	 * Almacena en el Bean la institucion a la que pertenece el procurador de un EJG
	 * 
	 * @param valor Identificador de la institucion del procurador 
	 * @return void 
	 */
	public void setIdInstitucionProcurador(Integer valor) {
		this.idInstitucionProcurador = valor;
	}
	
	/*Metodos GET*/
	

	/**
	 * Recupera del Bean el identificador de un EJG  
	 * 
	 * @return Identificador de un area. De tipo "Integer" 
	 */
	public Integer getIdTipoEJG				()	{ return this.idTipoEJG;}
	
	/**
	 * Recupera del Bean el anho de un EJG
	 * @return Integer
	 */
	public Integer getAnio					()	{ return this.anio;}
	
	/**
	 * Recupera del Bean el número de un EJGreturn 
	 * @return Integer
	 */
	public Integer getNumero				()	{ return this.numero;}

	
	/**
	 * Recupera del Bean el identificador de la persona 
	 * @return Integer
	 */
	public Integer getIdPersona	 			() { return this.idPersona;}

	/**
	 * Recupera del Bean las observaciones 
	 * @return String
	 */
	public String getObservaciones 			() { return this.observaciones;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece un EJG 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;}

	/**
	 * Recupera del Bean el procurador al que pertenece un EJG
	 * @return Integer
	 */
	public Long getIdProcurador() { return idProcurador; }

	/**
	 * Recupera del Bean la institucion a la que pertenece el procurador de un EJG 
	 * @return Integer
	 */
	public Integer getIdInstitucionProcurador() { return idInstitucionProcurador; }

	
	


	
}