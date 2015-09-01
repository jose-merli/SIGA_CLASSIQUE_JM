package com.siga.beans;


/**
 * Implementa las operaciones sobre el bean de la tabla SCS_ESTADOEJG
 * 
 * @author ruben.fernandez
 * @since 14/02/2005
 * @version 11-09-2008 adrianag
 */
public class ScsEstadoEJGBean extends MasterBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1552169374083397742L;

	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "SCS_ESTADOEJG";
	
	// Nombre de campos de la tabla
	static public final String	C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String	C_IDTIPOEJG			= "IDTIPOEJG";
	static public final String	C_ANIO 				= "ANIO";
	static public final String	C_NUMERO			= "NUMERO";
	static public final String	C_IDESTADOPOREJG	= "IDESTADOPOREJG";
	static public final String	C_IDESTADOEJG		= "IDESTADOEJG";
	static public final String	C_FECHAINICIO		= "FECHAINICIO";
	static public final String	C_OBSERVACIONES		= "OBSERVACIONES";
	static public final String	C_AUTOMATICO		= "AUTOMATICO";
	static public final String	C_PROPIETARIOCOMISION		= "PROPIETARIOCOMISION";
	static public final String	C_FECHABAJA		= "FECHABAJA";
	
	
	//////////////////// ATRIBUTOS ////////////////////
	private Integer idInstitucion;
	private Integer idTipoEJG;	
	private Integer anio;
	private Integer numero;
	private Integer idEstadoPorEJG;
	private Integer idEstadoEJG;
	private String 	fechaInicio;
	private String 	observaciones;
	private String 	automatico;
	private String 	propietarioComision;
	
	
	//////////////////// SETTERS ////////////////////
	public void setIdInstitucion	(Integer valor) {this.idInstitucion		= valor;}
	public void setIdTipoEJG		(Integer valor) {this.idTipoEJG			= valor;}
	public void setAnio 			(Integer valor) {this.anio				= valor;}
	public void setNumero 			(Integer valor) {this.numero			= valor;}
	public void setIdEstadoPorEJG	(Integer valor) {this.idEstadoPorEJG	= valor;}
	public void setIdEstadoEJG		(Integer valor) {this.idEstadoEJG		= valor;}
	public void setFechaInicio		(String	 valor) {this.fechaInicio		= valor;}
	public void setObservaciones	(String	 valor) {this.observaciones		= valor;}
	public void setAutomatico		(String  valor) {this.automatico 		= valor;}
	public void setPropietarioComisino (String	 valor) {this.propietarioComision = valor;}
	
	
	//////////////////// GETTERS ////////////////////
	public Integer	getIdInstitucion	() {return this.idInstitucion;}
	public Integer	getIdTipoEJG		() {return this.idTipoEJG;}
	public Integer	getAnio 			() {return this.anio;}
	public Integer	getNumero 			() {return this.numero;}
	public Integer	getIdEstadoPorEJG	() {return this.idEstadoPorEJG;}
	public Integer	getIdEstadoEJG		() {return this.idEstadoEJG;}
	public String	getFechaInicio		() {return this.fechaInicio;}
	public String	getObservaciones	() {return this.observaciones;}
	public String 	getAutomatico		() {return this.automatico;}
	public String	getPropietarioComision	() {return this.propietarioComision;}
	
}