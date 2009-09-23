/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsDocumentacionSOJBean extends MasterBean{
	
	/* Variables */ 
	
	private String  fechaLimite;
	private String 	documentacion;
	private String	fechaEntrega;
	private String	fechaApertura;
	private Integer	idTipoSOJ;
	private Integer	anio;
	private Integer	numero;
	private Integer	idDocumentacion;
	private Integer	idInstitucion;
	private String	regEntrada;
	private String	regSalida;
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_DOCUMENTACIONSOJ";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_FECHALIMITE 		= 				"FECHALIMITE";	
	static public final String 	C_DOCUMENTACION 	= 				"DOCUMENTACION";
	static public final String 	C_FECHAENTREGA 		= 				"FECHAENTREGA";
	static public final String 	C_FECHAAPERTURA		= 				"FECHAAPERTURA";
	static public final String	C_IDTIPOSOJ 		=				"IDTIPOSOJ";
	static public final String 	C_ANIO 				= 				"ANIO";
	static public final String 	C_NUMERO 			= 				"NUMERO";			
	static public final String  C_IDINSTITUCION 	= 				"IDINSTITUCION";
	static public final String 	C_IDDOCUMENTACION 	= 				"IDDOCUMENTACION";
	static public final String  C_REGENTRADA 	= 					"REGENTRADA";
	static public final String 	C_REGSALIDA 	= 					"REGSALIDA";
		
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el registro de entrada de la documentación de un SOJ
	 * 
	 * @param valor Documentación SOJ 
	 * @return void 
	 */
	public void setRegEntrada				(String valor)	{ this.regEntrada = valor;}	
	/**
	 * Almacena en el Bean el registro de salida de la documentación de un SOJ
	 * 
	 * @param valor Documentación SOJ 
	 * @return void 
	 */
	public void setRegSalida				(String valor)	{ this.regSalida = valor;}
	
	
	/**
	 * Almacena en el Bean el identificador del tipo de SOJ
	 * 
	 * @param valor Identificador tipo SOH¡J 
	 * @return void 
	 */
	public void setIdTipoSOJ					(Integer valor)	{ this.idTipoSOJ = valor;}
	
	/**
	 * Almacena en el Bean el anho de un SOJ
	 * 
	 * @param valor Anho SOJ  
	 * @return void 
	 */
	public void setAnio							(Integer valor)	{ this.anio = valor;}
	
	/**
	 * Almacena en el Bean el número de un SOJ
	 * 
	 * @param valor Número SOJ 
	 * @return void 
	 */
	public void setNumero						(Integer valor)	{ this.numero = valor;}
	
	/**
	 * Almacena en el Bean la descripción de la documentación de un SOJ
	 * 
	 * @param valor Documentación SOJ 
	 * @return void 
	 */
	public void setDocumentacion				(String valor)	{ this.documentacion = valor;}
	
	/**
	 * Almacena en el Bean el identficicador de la documentación de un SOJ
	 * 
	 * @param valor Identificador documentación SOJ 
	 * @return void 
	 */
	public void setIdDocumentacion				(Integer valor)	{ this.idDocumentacion = valor;}
		
	/**
	 * Almacena en el Bean la fecha de entrega de documentación de un SOJ
	 * 
	 * @param valor Fecha entrega SOJ 
	 * @return void 
	 */
	public void setFechaEntrega					(String valor)	{ this.fechaEntrega = valor;}
	
	/**
	 * Almacena en el Bean la fecha límite de entrega de documentación de un SOJ
	 * 
	 * @param valor Fecha límite entrega documentación SOJ 
	 * @return void 
	 */
	public void setFechaLimite					(String valor)	{ this.fechaLimite = valor;}
	
	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un SOJ
	 * 
	 * @param valor Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion 				(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el registro de entrada de un SOJ
	 * @return String 
	 */
	public String getRegEntrada			()	{ return this.regEntrada;}	/**
	 * Recupera del Bean el registro de salida de un SOJ
	 * @return String 
	 */
	public String getRegSalida			()	{ return this.regSalida;}
	
	/**
	 * Recupera del Bean el identificador de un SOJ  
	 * 
	 * @return Identificador de un area. De tipo "Integer" 
	 */
	public Integer getIdTipoSOJ				()	{ return this.idTipoSOJ;}
	
	/**
	 * Recupera del Bean el anho de un SOJ
	 * @return Integer
	 */
	public Integer getAnio					()	{ return this.anio;}
	
	/**
	 * Recupera del Bean el número de un SOJreturn 
	 * @return Integer
	 */
	public Integer getNumero				()	{ return this.numero;}
	
	/**
	 * Recupera del Bean la fecha de entrega de un SOJ
	 * @return String 
	 */
	public String getFechaEntrega			()	{ return this.fechaEntrega;}
	
	/**
	 * Recupera del Bean la fecha límite de entrega de un SOJ
	 * @return String 
	 */
	public String getFechaLimite			()	{ return this.fechaLimite;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece un SOJ 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;}
	
	/**
	 * Recupera del Bean la documentación del SOJ 
	 * @return String
	 */
	public String getDocumentacion 			() { return this.documentacion;}
	
	/**
	 * Recupera del Bean el identificador de la documentación del SOJ 
	 * @return Integer
	 */
	public Integer getIdDocumentacion 		() { return this.idDocumentacion;}
}