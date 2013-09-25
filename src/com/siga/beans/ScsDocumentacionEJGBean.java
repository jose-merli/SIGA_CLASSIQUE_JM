/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsDocumentacionEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private String  fechaLimite;
	private String 	documentacion;
	private String	fechaEntrega;
	private Integer	idTipoEJG;
	private Integer	anio;
	private Integer	numero;
	private Integer	idDocumentacion;
	private Integer	idInstitucion;	
	private String	regEntrada;
	private String	regSalida;
	private String	presentador;
	private String	idDocumento;
	private String	idTipoDocumento;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_DOCUMENTACIONEJG";
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_FECHALIMITE 		= 				"FECHALIMITE";	
	static public final String 	C_DOCUMENTACION 	= 				"DOCUMENTACION";
	static public final String 	C_FECHAENTREGA 		= 				"FECHAENTREGA";
	static public final String	C_IDTIPOEJG 		=				"IDTIPOEJG";
	static public final String 	C_ANIO 				= 				"ANIO";
	static public final String 	C_NUMERO 			= 				"NUMERO";			
	static public final String  C_IDINSTITUCION 	= 				"IDINSTITUCION";
	static public final String 	C_IDDOCUMENTACION 	= 				"IDDOCUMENTACION";
	static public final String  C_REGENTRADA 	= 					"REGENTRADA";
	static public final String 	C_REGSALIDA 	= 					"REGSALIDA";
	static public final String 	C_PRESENTADOR 	= 					"PRESENTADOR";	
	static public final String 	C_IDDOCUMENTO 	= 					"IDDOCUMENTO";
	static public final String 	C_IDTIPODOCUMENTO 	= 				"IDTIPODOCUMENTO";	
	static public final String 	C_IDFICHERO 	= 				"IDFICHERO";
		
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el registro de entrada de la documentación de un EJG
	 * 
	 * @param valor Documentación EJG 
	 * @return void 
	 */
	public void setRegEntrada				(String regEntrada)	{ this.regEntrada = regEntrada;}	/**
	 * Almacena en el Bean el registro de salida de la documentación de un EJG
	 * 
	 * @param valor Documentación EJG 
	 * @return void 
	 */
	public void setRegSalida				(String regSalida)	{ this.regSalida = regSalida;}
	
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
	 * Almacena en el Bean la descripción de la documentación de un EJG
	 * 
	 * @param valor Documentación EJG 
	 * @return void 
	 */
	public void setDocumentacion				(String valor)	{ this.documentacion = valor;}
	
	/**
	 * Almacena en el Bean el identficicador de la documentación de un EJG
	 * 
	 * @param valor Identificador documentación EJG 
	 * @return void 
	 */
	public void setIdDocumentacion				(Integer valor)	{ this.idDocumentacion = valor;}
		
	/**
	 * Almacena en el Bean la fecha de entrega de documentación de un EJG
	 * 
	 * @param valor Fecha entrega EJG 
	 * @return void 
	 */
	public void setFechaEntrega					(String valor)	{ this.fechaEntrega = valor;}
	
	/**
	 * Almacena en el Bean la fecha límite de entrega de documentación de un EJG
	 * 
	 * @param valor Fecha límite entrega documentación EJG 
	 * @return void 
	 */
	public void setFechaLimite					(String valor)	{ this.fechaLimite = valor;}
	
	
	/**
	 * Almacena en el Bean la institucion a la que pertenece un EJG
	 * 
	 * @param valor Institucion EJG 
	 * @return void 
	 */
	public void setIdInstitucion 				(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el registro de entrada de un EJG
	 * @return String 
	 */
	public String getRegEntrada			()	{ return this.regEntrada;}	/**
	 * Recupera del Bean el registro de salida de un EJG
	 * @return String 
	 */
	public String getRegSalida			()	{ return this.regSalida;}
	
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
	 * Recupera del Bean la fecha de entrega de un EJG
	 * @return String 
	 */
	public String getFechaEntrega			()	{ return this.fechaEntrega;}
	
	/**
	 * Recupera del Bean la fecha límite de entrega de un EJG
	 * @return String 
	 */
	public String getFechaLimite			()	{ return this.fechaLimite;}
	
	/**
	 * Recupera del Bean la institucion a la que pertenece un EJG 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;}
	
	/**
	 * Recupera del Bean la documentación del EJG 
	 * @return String
	 */
	public String getDocumentacion 			() { return this.documentacion;}
	
	/**
	 * Recupera del Bean el identificador de la documentación del EJG 
	 * @return Integer
	 */
	public Integer getIdDocumentacion 		() { return this.idDocumentacion;}
	public String getPresentador() {
		return presentador;
	}
	public void setPresentador(String presentador) {
		this.presentador = presentador;
	}
	public String getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	Long idFichero;


	public Long getIdFichero() {
		return idFichero;
	}
	public void setIdFichero(Long idFichero) {
		this.idFichero = idFichero;
	}
}