/*
 * VERSIONES:
 * 
 * miguel.villegas - 29-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CENTIPOSSEGURO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenTiposSeguroBean extends MasterBean {

	/* Variables */
	private Integer idTipoSeguro;
	private String 	nombre;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOSSEGURO";

	/* Nombre campos de la tabla */
	static public final String C_IDTIPOSSEGURO = "IDTIPOSSEGURO";
	static public final String C_NOMBRE	= "NOMBRE";
	
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

	// Metodos SET
	public void setIdTiposSeguro (Integer id) 	{ this.idTipoSeguro = id; }
	public void setNombre (String s)	{ this.nombre = s; }

	// Metodos GET
	public Integer getIdTiposSeguro 		()	{ return this.idTipoSeguro; }
	public String getNombre    ()	{ return this.nombre; }
}
