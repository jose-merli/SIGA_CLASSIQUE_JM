/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTipoDictamenenEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoDictamenEJG;		
	private String 	descripcion;	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPODICTAMENEJG";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPODICTAMENEJG = 					"IDTIPODICTAMENEJG"; 
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
	 * Almacena en el Bean el identificador del dictamen de un EJG
	 * 
	 * @param valor Identificador dictamen EJG. 
	 * @return void 
	 */
	public void setIdTipoDictamenEJG	(Integer valor)	{ this.idTipoDictamenEJG = valor;}

	/**
	 * Almacena en el Bean la descipción del dictamen de un EJG
	 * 
	 * @param valor Descripción del dictamen EJG
	 * @return void 
	 */
	public void setDescripcion			(String valor)	{ this.descripcion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del dictámen de un EJG 
	 * @return Integer 
	 */
	public Integer getIdTipoDictamenEJG		()	{ return this.idTipoDictamenEJG;}
	
	/**
	 * Recupera del Bean la descipción del dictámen de un EJG
	 * @return String
	 */
	public String getDescripcion			()	{ return this.descripcion;}
	
}