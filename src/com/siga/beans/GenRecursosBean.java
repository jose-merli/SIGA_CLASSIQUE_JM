package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla GEN_RECURSOS
 * 
 * @author carlos.vidal
 * @since 26/10/2004
 */



public class GenRecursosBean extends MasterBean{
	
	/* Variables */ 
	
	private String idRecurso;
	private String descripcion;
	private Integer error;
	private String idLenguaje;
	private String idPropiedad;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "GEN_RECURSOS";
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDRECURSO 	= "IDRECURSO";
	static public final String 	C_DESCRIPCION 	= "DESCRIPCION";
	static public final String	C_ERROR			= "ERROR";
	static public final String 	C_IDLENGUAJE 	= "IDLENGUAJE";
	static public final String 	C_IDPROPIEDAD 	= "IDPROPIEDAD";
	
	/*Metodos SET&GET*/
	public void setIdRecurso(String valor) 	{ this.idRecurso = valor;}
	public String getIdRecurso()			{ return this.idRecurso;}
	
	public void setDescripcion(String valor){ this.descripcion = valor;}
	public String getDescripcion()			{ return this.descripcion;}
	
	public void setError(Integer valor) 	{ this.error = valor;}
	public Integer getError()				{ return this.error;}
	
	public void setIdLenguaje(String valor) { this.idLenguaje = valor;}
	public String getIdLenguaje()			{ return this.idLenguaje;}
	
	public void setIdPropiedad(String valor){ this.idPropiedad = valor;}
	public String getIdPropiedad()			{ return this.idPropiedad;}
}