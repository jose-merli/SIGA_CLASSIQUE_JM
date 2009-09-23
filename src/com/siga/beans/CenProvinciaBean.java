/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenProvinciaBean extends MasterBean{

	/* Variables */
	private String 	idProvincia, nombre;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_PROVINCIAS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPROVINCIA	= "IDPROVINCIA";
	static public final String C_NOMBRE			= "NOMBRE";
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
	public void setidProvincia (String s)	{ this.idProvincia = s; }
	public void setNombre (String s)		{ this.nombre = s; }

	// Metodos GET
	public String getIdProvincia 		()	{ return this.idProvincia; }
	public String getNombre 			()	{ return this.nombre; }
}
