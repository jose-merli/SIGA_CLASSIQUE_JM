/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsTipoEJGColegioBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idTipoEJGColegio;		
	private String 	descripcion;
	private String 	idInstitucion;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_TIPOEJGCOLEGIO";	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOEJGCOLEGIO 	= 					"IDTIPOEJGCOLEGIO";	 
	static public final String  C_DESCRIPCION 		= 					"DESCRIPCION";
	static public final String  C_IDINSTITUCION		= 					"IDINSTITUCION";
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
	 * Almacena en el Bean el identificador de un EJG
	 * 
	 * @param valor Identificador EJG. 
	 * @return void 
	 */
	public void setIdTipoEJGColegio		(Integer valor)	{ this.idTipoEJGColegio = valor;}

	/**
	 * Almacena en el Bean la descipción del tipo de EJG
	 * 
	 * @param valor Descripción del EJG
	 * @return void 
	 */
	public void setDescripcion			(String valor)	{ this.descripcion = valor;}
	
	/**
	 * Almacena en el Bean la institución del EJG
	 * 
	 * @param valor Institución del EJG
	 * @return void 
	 */
	public void setIdInstitucion		(String valor)	{ this.idInstitucion = valor;}
		
	/*Metodos GET*/
	
	/**
	 * Recupera del Bean el identificador de un EJG 
	 * @return Identificador de un EJG. 
	 */
	public Integer getIdTipoEJGColegio	()	{ return this.idTipoEJGColegio;}
	
	/**
	 * Recupera del Bean la descipción de un EJG
	 * @return String
	 */
	public String getDescripcion		()	{ return this.descripcion;}
	
	/**
	 * Recupera del Bean la institución de un EJG
	 * @return String
	 */
	public String getIdInstitucion		()	{ return this.idInstitucion;}
	
}