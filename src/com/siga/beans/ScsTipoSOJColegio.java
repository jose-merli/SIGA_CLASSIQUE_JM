/*
 * Fecha creación: 21/01/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTipoSOJColegio extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoSOJColegio;		
	private String 	descripcion;
	private Integer	institucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPOSOJCOLEGIO";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String  C_INSTITUCION 		= 					"IDINSTITUCION";
	static public final String	C_IDTIPOSOJCOLEGIO 	= 					"IDTIPOSOJCOLEGIO";	
	static public final String  C_DESCRIPCION 		= 					"DESCRIPCION";	
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////
	
	/*Metodos SET*/
	/**
	 * Almacena en el Bean el identificador de un SOJ
	 * 
	 * @param valor Identificador SOJ. 
	 * @return void 
	 */
	public void setIdTipoSOJColegio			(Integer valor)	{ this.idTipoSOJColegio = valor;}

	/**
	 * Almacena en el Bean la descipción del tipo de SOJ
	 * 
	 * @param valor Descripción del SOJ
	 * @return void 
	 */
	public void setDescripcion				(String valor)	{ this.descripcion = valor;}
	
	/**
	 * Almacena en el Bean la institucion del SOJ
	 * 
	 * @param valor Institución del SOJ
	 * @return void 
	 */
	public void setInstitucion				(Integer valor)	{ this.institucion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un SOJ 
	 * @return Identificador de un area. 
	 */
	public Integer getIdTipoSOJColegio		()				{ return this.idTipoSOJColegio;}
	
	/**
	 * Recupera del Bean la descipción de un SOJ
	 * @return String
	 */
	public String getDescripcion			()				{ return this.descripcion;}
	
	/**
	 * Recupera del Bean la institucion del SOJ
	 * @return Integer
	 */
	public Integer setInstitucion			()				{ return this.institucion;}
	
}