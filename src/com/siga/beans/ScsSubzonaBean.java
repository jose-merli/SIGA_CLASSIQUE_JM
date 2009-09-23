package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_SUBZONA
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */

public class ScsSubzonaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer		idSubzona;
	private Integer		idZona;
	private String		nombre;
	private String		municipios;
	private Integer 	contenido;
	private Integer 	idInstitucion;
	private Integer 	idPartido;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_SUBZONA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDSUBZONA = 				"IDSUBZONA";
	static public final String	C_IDZONA = 					"IDZONA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_MUNICIPIOS = 				"MUNICIPIOS";
	static public final String  C_CONTENIDO = 				"CONTENIDO";	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPARTIDO	=	 			"IDPARTIDO";
		
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la subzona
	 * 
	 * @param valor Identificador la subzona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdSubzona				(Integer valor)		{ this.idSubzona = valor;}
	/**
	 * Almacena en el Bean el identificador de la zona
	 * 
	 * @param valor Identificador la zona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdZona					(Integer valor)		{ this.idZona = valor;}
	/**
	 * Almacena en el Bean el nombre de la subzona
	 * 
	 * @param valor Nombre la subzona. De tipo "String". 
	 * @return void 
	 */
	public void setNombre					(String valor)		{ this.nombre = valor;}
	/**
	 * Almacena en el Bean los municipios
	 * 
	 * @param valor Municipios de la subzona. De tipo "String". 
	 * @return void 
	 */
	public void setMunicipios				(String valor)		{ this.municipios = valor;}
	/**
	 * Almacena en el Bean el contenido
	 * 
	 * @param valor Contenido de la subzona. De tipo "String". 
	 * @return void 
	 */
	public void setContenido				(Integer valor)		{ this.contenido = valor;}
	/**
	 * Almacena en el Bean el identificador de la institucion
	 * 
	 * @param valor Identificador de la institucion de la subzona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) 	{ this.idInstitucion = valor;}
	/**
	 * Almacena en el Bean el identificador del partido
	 * 
	 * @param valor Identificador del partido de la subzona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdPartido		 		(Integer valor) 	{ this.idPartido = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la subzona
	 * 
	 * @return Identificador de la subzona
	 */
	public Integer getIdSubzona					()	{ return this.idSubzona;}
	/**
	 * Recupera del Bean el identificador de la zona
	 * 
	 * @return Identificador de la zona
	 */
	public Integer getIdZona					()	{ return this.idZona;}
	/**
	 * Recupera del Bean el nombre de la subzona
	 * 
	 * @return Nombre de la subzona
	 */
	public String getNombre						()	{ return this.nombre;}
	/**
	 * Recupera del Bean los municipios 
	 * 
	 * @return Municipios de la subzona
	 */
	public String getMunicipios					()	{ return this.municipios;}
	/**
	 * Recupera del Bean el contenido
	 * 
	 * @return Contenido de la subzona
	 */
	public Integer getContenido					()  { return this.contenido;}
	/**
	 * Recupera del Bean el identificador de la institucion
	 * 
	 * @return Identificador de la institucion de la subzona
	 */
	public Integer getIdInstitucion 			() 	{ return this.idInstitucion;}	
	/**
	 * Recupera del Bean el identificador del partido
	 * 
	 * @return Identificador del partido de la subzona
	 */
	public Integer getIdPartido		 			() 	{ return this.idPartido;}	
}