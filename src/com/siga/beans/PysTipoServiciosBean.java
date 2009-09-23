/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class PysTipoServiciosBean extends MasterBean{

	/* Variables */
	private Integer idTipoServicios;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_TIPOSERVICIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOSERVICIOS	= "IDTIPOSERVICIOS";
	static public final String C_DESCRIPCION		= "DESCRIPCION";

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
	public void setIdTipoServicios (Integer id) 	{ this.idTipoServicios = id; }
	public void setDescripcion (String s)			{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdTipoServicios 	()	{ return this.idTipoServicios; }
	public String  getDescripcion    	()	{ return this.descripcion; }
}
