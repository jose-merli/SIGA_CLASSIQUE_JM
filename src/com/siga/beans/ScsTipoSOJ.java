/*
 * Fecha creación: 21/01/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTipoSOJ extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoSOJ;		
	private String 	descripcion;	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPOSOJ";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOSOJ 	= 					"IDTIPOSOJ";	 
	static public final String  C_DESCRIPCION 	= 					"DESCRIPCION";
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
	public void setIdTipoSOJ			(Integer valor)	{ this.idTipoSOJ = valor;}

	/**
	 * Almacena en el Bean la descipción del tipo de SOJ
	 * 
	 * @param valor Descripción del SOJ
	 * @return void 
	 */
	public void setDescripcion			(String valor)	{ this.descripcion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un SOJ 
	 * @return Identificador de un area. 
	 */
	public Integer getIdTipoSOJ				()	{ return this.idTipoSOJ;}
	
	/**
	 * Recupera del Bean la descipción de un SOJ
	 * @return String
	 */
	public String getDescripcion			()	{ return this.descripcion;}
	
}