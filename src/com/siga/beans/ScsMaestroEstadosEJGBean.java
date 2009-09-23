/*
 * Fecha creaci�n: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsMaestroEstadosEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idEstadoEJG;		
	private String 	descripcion;	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_MAESTROESTADOSEJG";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDESTADOEJG 	= 					"IDESTADOEJG"; 
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
	 * Almacena en el Bean el identificador del estado de un EJG
	 * 
	 * @param valor Identificador estado EJG. 
	 * @return void 
	 */
	public void setIdEstadoEJG		(Integer valor)	{ this.idEstadoEJG = valor;}

	/**
	 * Almacena en el Bean la descipci�n del estado de un EJG
	 * 
	 * @param valor Descripci�n del EJG
	 * @return void 
	 */
	public void setDescripcion		(String valor)	{ this.descripcion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador del estado de un EJG 
	 * @return Identificador del estado de un EJG. 
	 */
	public Integer getIdEstadoEJG			()	{ return this.idEstadoEJG;}
	
	/**
	 * Recupera del Bean la descipci�n del estado de un EJG
	 * @return String
	 */
	public String getDescripcion			()	{ return this.descripcion;}
	
}