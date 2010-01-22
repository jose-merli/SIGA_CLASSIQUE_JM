/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTipoEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoEJG;		
	private String 	descripcion;	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPOEJG";
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOEJG 	= 					"IDTIPOEJG";	 
	static public final String  C_DESCRIPCION 	= 					"DESCRIPCION";
	/* cambio para codigo ext */
	private String codigoExt;
	private String bloqueado;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	static public final String C_BLOQUEADO = "BLOQUEADO";
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
	 * Almacena en el Bean el identificador de un EJG
	 * 
	 * @param valor Identificador EJG. 
	 * @return void 
	 */
	public void setIdTipoEJG			(Integer valor)	{ this.idTipoEJG = valor;}

	/**
	 * Almacena en el Bean la descipción del tipo de EJG
	 * 
	 * @param valor Descripción del EJG
	 * @return void 
	 */
	public void setDescripcion			(String valor)	{ this.descripcion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un EJG 
	 * @return Integer. 
	 */
	public Integer getIdTipoEJG				()	{ return this.idTipoEJG;}
	
	/**
	 * Recupera del Bean la descipción de un EJG
	 * @return String
	 */
	public String getDescripcion			()	{ return this.descripcion;}
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	
}