/*
 * Fecha creación: 7/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

import com.siga.gratuita.form.DefinirTelefonosJGForm;

public class ScsTelefonosPersonaJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idInstitucion;
	private Integer idPersona;
	private Integer idTelefono;
	private String  nombreTelefono;
	private String  numeroTelefono;	
	private String  preferentesms;

	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TELEFONOSPERSONA";
	
		
	/*Nombre de campos de la tabla*/	
	static public final String  C_IDPERSONA = 					"IDPERSONA";
	static public final String  C_IDINSTITUCION = 				"IDINSTITUCION";
	static public final String  C_IDTELEFONO = 					"IDTELEFONO";
	static public final String  C_NOMBRETELEFONO = 				"NOMBRETELEFONO";
	static public final String  C_NUMEROTELEFONO = 				"NUMEROTELEFONO";
	static public final String  C_PREFERENTESMS= 				"PREFERENTESMS";	
		
	
	/*Metodos SET*/	
	
	/**
	 * Almacena en el Bean el sms del solicitante.
	 * 
	 * @param valor preferenteSms solicitante
	 * @return void 
	 */
		public void setpreferenteSms			(String valor)	{ this.preferentesms = valor;}
	
	
	/**
	 * Almacena en el Bean el identificador del solicitante del SOJ
	 * 
	 * @param valor Identificador solicitante SOJ 
	 * @return void 
	 */
	public void setIdPersona				(Integer valor)	{ this.idPersona = valor;}
	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un SOJ
	 * 
	 * @param valor Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) { this.idInstitucion = valor;}
	
	/**
	 * Almacena en el Bean el identificador del teléfono
	 * 
	 * @param valor Identificador teléfono 
	 * @return void 
	 */
	public void setIdTelefono	 			(Integer valor) { this.idTelefono = valor;}
	
	/**
	 * Almacena en el Bean el nombre del teléfono
	 * 
	 * @param valor Nombre teléfono 
	 * @return void 
	 */
	public void setNombreTelefono	 		(String valor) { this.nombreTelefono = valor;}
	
	/**
	 * Almacena en el Bean el número del teléfono
	 * 
	 * @param valor Número teléfono 
	 * @return void 
	 */
	public void setNumeroTelefono	 		(String valor) { this.numeroTelefono = valor;}
	
	
	/*Metodos GET*/	
	
	/**
	 * Recupera del Bean el sms de la persona 
	 * @return Integer
	 */
	public String getpreferenteSms	()	{ return this.preferentesms;}
	
	/**
	 * Recupera del Bean el identificador de la persona 
	 * @return Integer
	 */
	public Integer getIdPersona			()	{ return this.idPersona;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece 
	 * @return Integer
	 */
	public Integer getIdInstitucion 	() { return this.idInstitucion;}
	
	/**
	 * Recupera del Bean el identificador del teléfono 
	 * @return String
	 */
	public Integer getIdTelefono	 	() { return this.idTelefono;}
	
	/**
	 * Recupera del Bean el nombre del teléfono
	 * @return String
	 */
	public String getNombreTelefono	 	() { return this.nombreTelefono;}
	
	/**
	 * Recupera del Bean el número del teléfono 
	 * @return String 
	 */
	public String  getNumeroTelefono	() { return this.numeroTelefono;}
	
	public DefinirTelefonosJGForm getDefinirTelefonosJGForm(){
		DefinirTelefonosJGForm definirTelefono = new DefinirTelefonosJGForm();
		definirTelefono.setNombreTelefonoJG(this.nombreTelefono);
		definirTelefono.setNumeroTelefonoJG(this.numeroTelefono);	
		definirTelefono.setPreferenteSms(this.preferentesms);
		return 	definirTelefono;
		
	}
	
	
}