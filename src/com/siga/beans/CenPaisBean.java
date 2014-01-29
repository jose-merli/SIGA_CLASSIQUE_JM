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
	private String idPais;
	private String nombre;
	private String codigoExt;
	private String codIso;
	private Integer longitudCuentaBancaria;
	private String sepa;

	/* Nombre tabla */
	static public String T_NOMBRETABLA 		= "CEN_PAIS";

	/* Nombre campos de la tabla */
	static public final String C_IDPAIS					= "IDPAIS";	
	static public final String C_NOMBRE					= "NOMBRE";
	static public final String C_CODIGOEXT				= "CODIGOEXT";
	static public final String C_CODISO					= "CODISO";
	static public final String C_LONGITUDCUENTABANCARIA	= "LONGITUDCUENTABANCARIA";
	static public final String C_SEPA					= "SEPA";
	
	/* Getters Y Setters */
	public String getIdPais() 				{	return idPais;	}	
	public void setIdPais(String idPais) 	{ this.idPais = idPais;	}	
	public String getNombre() 				{	return nombre;	}
	public void setNombre(String nombre) 	{ this.nombre = nombre;	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodIso()
	{
		return codIso;
	}
	public void setCodIso(String codIso)
	{
		this.codIso = codIso;
	}
	public Integer getLongitudCuentaBancaria()
	{
		return longitudCuentaBancaria;
	}
	public void setLongitudCuentaBancaria(Integer longitudCuentaBancaria)
	{
		this.longitudCuentaBancaria = longitudCuentaBancaria;
	}
	public String getSepa()
	{
		return sepa;
	}
	public void setSepa(String sepa)
	{
		this.sepa = sepa;
	}
}
