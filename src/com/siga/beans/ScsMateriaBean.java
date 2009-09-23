package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_MATERIA
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */

public class ScsMateriaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer		idArea;
	private Integer		idMateria;
	private String		nombre;
	private String 		contenido;
	private Integer 	idInstitucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_MATERIA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDAREA = 					"IDAREA";
	static public final String	C_IDMATERIA = 				"IDMATERIA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String  C_CONTENIDO = 				"CONTENIDO";	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
		
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de area 
	 * 
	 * @param valor Identificador del area de la materia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdArea					(Integer valor)		{ this.idArea = valor;}
	/**
	 * Almacena en el Bean el identificador de materia 
	 * 
	 * @param valor Identificador de la materia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdMateria				(Integer valor)		{ this.idMateria = valor;}
	/**
	 * Almacena en el Bean el nombre de materia 
	 * 
	 * @param valor NOmbre de la materia. De tipo "String". 
	 * @return void 
	 */
	public void setNombre					(String valor)		{ this.nombre = valor;}
	/**
	 * Almacena en el Bean el contenido de materia 
	 * 
	 * @param valor Contenido de la materia. De tipo "String". 
	 * @return void 
	 */
	public void setContenido				(String valor)		{ this.contenido = valor;}
	/**
	 * Almacena en el Bean el identificador de institucion 
	 * 
	 * @param valor Identificador de institucion de la materia. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del area
	 * 
	 * @return Identificador del area
	 */
	public Integer getIdArea					()	{ return this.idArea;}
	/**
	 * Recupera del Bean el identificador de la materia
	 * 
	 * @return Identificador de la materia
	 */
	public Integer getIdMateria					()	{ return this.idMateria;}
	/**
	 * Recupera del Bean el nombre de la materia
	 * 
	 * @return Nombre de la materia
	 */
	public String getNombre						()	{ return this.nombre;}
	/**
	 * Recupera del Bean el contenido de la materia
	 * 
	 * @return Contenido de la materia
	 */
	public String getContenido					()  { return this.contenido;}
	/**
	 * Recupera del Bean la institucion de la materia
	 * 
	 * @return Identificador de la institucion de la materia
	 */
	public Integer getIdInstitucion 			() 	{ return this.idInstitucion;}	
}