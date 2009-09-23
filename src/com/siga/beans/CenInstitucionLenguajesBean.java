package com.siga.beans;

public class CenInstitucionLenguajesBean extends MasterBean
{
	/* Variables */ 
	private Integer		idInstitucion;
	private String  	idLenguaje;
	
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "CEN_INSTITUCION_LENGUAJES";
	
	/*Nombre de campos de la tabla*/
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDLENGUAJE    = "IDLENGUAJE";
	
	/*Metodos SET*/
	public void setIdInstitucion (Integer valor){ this.idInstitucion = valor;}
	public void setIdLenguaje	 (String  valor){ this.idLenguaje = valor;}
		
	/*Metodos GET*/
	public Integer getIdInstitucion	()	{ return this.idInstitucion;}
	public String  getIdLenguaje	() 	{ return this.idLenguaje;}
}