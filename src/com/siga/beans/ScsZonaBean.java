package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_SUBZONA
 * 
 * @author ruben.fernandez
 * @since 26/10/2004
 */

public class ScsZonaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer		idZona;
	private String		nombre;
	private String		municipios;
	private Integer 	idInstitucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_ZONA";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDZONA = 					"IDZONA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_MUNICIPIOS = 				"MUNICIPIOS";
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
		
	
	/*Metodos SET*/
	
	/**
	 * Almacena en el Bean el identificador de la zona
	 * 
	 * @param valor Identificador la zona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdZona					(Integer valor)	{ this.idZona = valor;}
	/**
	 * Almacena en el Bean el nombre de la zona
	 * 
	 * @param valor Nombre de la zona. De tipo "String". 
	 * @return void 
	 */
	public void setNombre					(String valor)	{ this.nombre = valor;}
	/**
	 * Almacena en el Bean los municipios
	 * 
	 * @param valor Municipios de la zona. De tipo "String". 
	 * @return void 
	 */
	public void setMunicipios				(String valor)	{ this.municipios = valor;}
	/**
	 * Almacena en el Bean el identificador de institucion
	 * 
	 * @param valor Identificador de institucion de la zona. De tipo "Integer". 
	 * @return void 
	 */
	public void setIdInstitucion 			(Integer valor) { this.idInstitucion = valor;}
	
	
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de la zona
	 * 
	 * @return Identificador de la zona
	 */
	public Integer getIdZona				()	{ return this.idZona;}
	/**
	 * Recupera del Bean el nombre de la zona
	 * 
	 * @return Nombre de la zona
	 */
	public String getNombre					()	{ return this.nombre;}
	/**
	 * Recupera del Bean los municipios
	 * 
	 * @return Municipios de la zona
	 */
	public String getMunicipios				()	{ return this.municipios;}
	/**
	 * Recupera del Bean el identificador de la institucion
	 * 
	 * @return Identificador de la institucion a la que pertenece la zona
	 */
	public Integer getIdInstitucion 		() 	{ return this.idInstitucion;}	
}