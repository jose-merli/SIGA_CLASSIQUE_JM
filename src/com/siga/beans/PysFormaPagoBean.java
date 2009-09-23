/*
 * VERSIONES:
 * 
 * daniel.casla	- 4-11-2004 - Inicio
 * miguel.villegas - 25-11-2004 - Continuacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSFORMAPAGO <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;


public class PysFormaPagoBean extends MasterBean{

	/* Variables */
	private Integer idFormaPago;
	private String 	descripcion;
	private String 	internet;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_FORMAPAGO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_INTERNET	= "INTERNET";	

	// Metodos SET
	public void setIdFormaPago (Integer id) 	{ this.idFormaPago= id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }
	public void setInternet (String i)	{ this.internet = i; }	
		

	// Metodos GET
	public Integer getIdFormaPago 	()	{ return this.idFormaPago; }
	public String getDescripcion    ()	{ return this.descripcion; }
	public String getInternet ()	{ return this.internet; }	
}
