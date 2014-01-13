/*
 * Fecha creación: 14/02/2005
 * Autor: julio.vicente
 *
 */

package com.siga.beans;

public class ScsMaestroEstadosEJGBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idEstadoEJG;		
	private String 	descripcion;	
	
	private String bloqueado;
	private Short orden;
	private String visiblecomision; 
	private String codigoejis;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_MAESTROESTADOSEJG";
	
	
	
	/*Nombre de campos de la tabla*/
	
	static public final String	C_IDESTADOEJG 	= 					"IDESTADOEJG"; 
	static public final String  C_DESCRIPCION 	= 					"DESCRIPCION";
	static public final String  C_BLOQUEADO 	= 					"BLOQUEADO";
	static public final String  C_ORDEN 	= 					"ORDEN";
	static public final String  C_VISIBLECOMISION 	= 					"VISIBLECOMISION";
	static public final String  C_CODIGOEJIS 	= 					"CODIGOEJIS";
	
	
	
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
	 * Almacena en el Bean la descipción del estado de un EJG
	 * 
	 * @param valor Descripción del EJG
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
	 * Recupera del Bean la descipción del estado de un EJG
	 * @return String
	 */
	public String getDescripcion			()	{ return this.descripcion;}
	
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public Short getOrden() {
		return orden;
	}
	public void setOrden(Short orden) {
		this.orden = orden;
	}
	public String getVisiblecomision() {
		return visiblecomision;
	}
	public void setVisiblecomision(String visiblecomision) {
		this.visiblecomision = visiblecomision;
	}
	public String getCodigoejis() {
		return codigoejis;
	}
	public void setCodigoejis(String codigoejis) {
		this.codigoejis = codigoejis;
	}
	
}