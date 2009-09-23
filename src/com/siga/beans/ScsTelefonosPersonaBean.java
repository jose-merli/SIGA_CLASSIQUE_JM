/*
 * Fecha creación: 01/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTelefonosPersonaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	numeroTelefono;
	private String	nombreTelefono;
	private Integer	idTelefono;
	private Integer	idPersona;	
	private Integer idInstitucion;

	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TELEFONOSPERSONA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String  C_NUMEROTELEFONO = 					"NUMEROTELEFONO";
	static public final String  C_NOMBRETELEFONO = 					"NOMBRETELEFONO";
	static public final String  C_IDTELEFONO 	 = 					"IDTELEFONO";
	static public final String  C_IDPERSONA 	 = 					"IDPERSONA";
	static public final String  C_IDINSTITUCION  =	 				"IDINSTITUCION";
	
		
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador del telefono de una persona del SOJ
	 * 
	 * @param valor Identificador telefono 
	 * @return void 
	 */
	public void setIdTelefono					(Integer valor)	{ this.idTelefono = valor;}
	
	/**
	 * Almacena en el Bean el número de telefono de una persona
	 * 
	 * @param valor Número telefono 
	 * @return void 
	 */
	public void setNumeroTelefono				(Integer valor)	{ this.numeroTelefono = valor;}
	
	/**
	 * Almacena en el Bean el nombre del telefono
	 * 
	 * @param valor Nombre telefono 
	 * @return void 
	 */
	public void setNombreTelefono				(String valor)	{ this.nombreTelefono = valor;}
	
	/**
	 * Almacena en el Bean el identificador del abogado del SOJ
	 * 
	 * @param valor Identificador abogado SOJ 
	 * @return void 
	 */
	public void setIdPersona					(Integer valor)	{ this.idPersona = valor;}

	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un SOJ
	 * 
	 * @param valor Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del abogado del SOJ 
	 * @return Integer
	 */
	public Integer getIdPersona				()	{ return this.idPersona;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece un SOJ 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;}
	
	/**
	 * Recupera del Bean el identificador del telefono de una persona del SOJ 
	 * @return Integer 
	 */
	public Integer getIdTelefono			()	{ return this.idTelefono;}
	
	/**
	 * Recupera del Bean el número de telefono de una persona
	 * @return void 
	 */
	public Integer getNumeroTelefono		()	{ return this.numeroTelefono;}
	
	/**
	 * Recupera del Bean el nombre del telefono 
	 * @return String 
	 */
	public String getNombreTelefono			()	{ return this.nombreTelefono;}
	
}