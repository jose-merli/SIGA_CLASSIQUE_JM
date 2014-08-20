/*
 * VERSIONES:
 * miguel.villegas - 2-02-2005 - Creacion	
 */

/**
 * Clase que recoge y establece los valores del bean PYSTIPOIVA <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.beans;


public class PysTipoIvaBean extends MasterBean{

	/* Variables */
	private Integer idTipoIva;
	private String 	descripcion;
	private String 	valor;
	private String 	subCtaTipo;
	private String descripciontipo;
	private String codigoExt;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_TIPOIVA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOIVA		= "IDTIPOIVA";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_VALOR			= "VALOR";
	static public final String C_SUBCTATIPO = "SUBCTATIPO";
	static public final String C_DESCRIPCIONTIPO = "DESCRIPCIONTIPO";
	static public final String C_CODIGOEXT = "CODIGOEXT";

	// Metodos SET
	public void setIdTipoIva (Integer id) 	{ this.idTipoIva = id; }
	public void setDescripcion (String s)		{ this.descripcion = s; }
	public void setValor 	   (String s)		{ this.valor = s; }
	public void setSubCtaTipo 	   (String s)		{ this.subCtaTipo = s; }

	// Metodos GET
	public Integer getIdTipoIva ()	{ return this.idTipoIva; }
	public String  getDescripcion    ()	{ return this.descripcion; }
	public String  getValor    ()	{ return this.valor; }
	public String  getSubCtaTipo    ()	{ return this.subCtaTipo; }
	
	public String getDescripciontipo() {
		return descripciontipo;
	}
	public void setDescripciontipo(String descripciontipo) {
		this.descripciontipo = descripciontipo;
	}
	
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}	
}
