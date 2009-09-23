/*
 * nuria.rgonzalez - 01-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_PAIS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenPaisBean extends MasterBean{
	/* Variables */
	private String 	idPais, nombre;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_PAIS";

	/* Nombre campos de la tabla */
	static public final String C_IDPAIS			= "IDPAIS";	
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

	
	public String getIdPais() 				{	return idPais;	}	
	public String getNombre() 				{	return nombre;	}
	/**
	 * @param idPais The idPais to set.
	 */
	public void setIdPais(String idPais) 	{ this.idPais = idPais;	}	
	public void setNombre(String nombre) 	{ this.nombre = nombre;	}
}
