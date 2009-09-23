package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_AREA
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */
public class ScsAreaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idArea;
	private String	nombre;
	private String 	contenido;
	private Integer idInstitucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_AREA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDAREA = 					"IDAREA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String  C_CONTENIDO = 				"CONTENIDO";	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
		
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el identificador de un area
	 * 
	 * @param valor Identificador area. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdArea					(Integer valor)	{ this.idArea = valor;}
	/**
	 * Almacena en el Bean el identificador de un area
	 * 
	 * @param valor Nombre area. De tipo "String". 
	 * @return void 
	 */
	public void setNombre					(String valor)	{ this.nombre = valor;}
	/**
	 * Almacena en el Bean el contenido de un area
	 * 
	 * @param valor Contenido area. De tipo "String". 
	 * @return void 
	 */
	public void setContenido				(String valor)	{ this.contenido = valor;}
	/**
	 * Almacena en el Bean la institucion a la que pertenece un area
	 * 
	 * @param valor Institucion area. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un area  
	 * 
	 * @return Identificador de un area. De tipo "Integer" 
	 */
	public Integer getIdArea				()	{ return this.idArea;}
	/**
	 * Recupera del Bean el nombre de un area  
	 * 
	 * @return Nombre de un area. De tipo "String" 
	 */
	public String getNombre					()	{ return this.nombre;}
	/**
	 * Recupera del Bean el contenido de un area  
	 * 
	 * @return Contenido de un area. De tipo "String" 
	 */
	public String getContenido				()  { return this.contenido;}
	/**
	 * Recupera del Bean el Identificador de la institucion a la que pertenece el area
	 * 
	 * @return Identificador de la institucion a la que pertenece el area. De tipo "Integer" 
	 */
	public Integer getIdInstitucion 		() 	{ return this.idInstitucion;}	
}