package com.siga.beans;
/**
* @author ruben.fernandez
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/

public class CenInfluenciaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idPartido;
	private Integer	idInstitucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CEN_INFLUENCIA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 	  "IDINSTITUCION";
	static public final String	C_IDPARTIDO = 		  "IDPARTIDO";
			
	
	/*Metodos SET*/
	
	public void setIdPartido				(Integer valor)	{ this.idPartido = valor;}
	public void setIdInstitucion			(Integer valor)	{ this.idInstitucion = valor;}
	
	/*Metodos GET*/
	
	public Integer getIdPartido				()	{ return this.idPartido;}
	public Integer getIdInstitucion			()	{ return this.idInstitucion;}
			
}