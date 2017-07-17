/*
 * VERSIONES:
 * miguel.villegas - 2-02-2005 - Creacion	
 */

/**
 * Clase que recoge y establece los valores del bean PYSTIPOIVA <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.beans;


public class PysTipoIvaTraspasoNavBean extends MasterBean{

	private static final long serialVersionUID = -7991498168866418648L;
	/* Variables */
	private Integer idTipoIva, idInstitucion;
	private String 	codigoTraspasoNav;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_TIPOIVA_TRASPASONAV";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOIVA			= "IDTIPOIVA";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_CODIGO_TRASPASONAV	= "CODIGO_TRASPASONAV";

	// Metodos SET
	public void setIdTipoIva (Integer id) 		{ this.idTipoIva = id; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setCodigoTraspasoNav (String s)	{ this.codigoTraspasoNav = s; }

	// Metodos GET
	public Integer getIdTipoIva ()			{ return this.idTipoIva; }
	public Integer getIdInstitucion ()		{ return this.idInstitucion; }
	public String  getCodigoTraspasoNav ()	{ return this.codigoTraspasoNav; }
	
}
