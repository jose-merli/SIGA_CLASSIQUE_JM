/*
 * VERSIONES:
 * 
 * 31/01/2007 Creacion RGG AtosOrigin 
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_ESTADOCONFIRMFACT <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicha tabla 
 */
package com.siga.beans;

public class FacEstadoConfirmFactBean extends MasterBean{

	/* Variables */
	private Integer idEstado;
	private String 	alias;	
	private String 	descripcion;
	private String tipo;
	private String lenguaje;

	/* COnstantes */
	
	static public final Integer CONFIRM_PROGRAMADA = new Integer(1);  
	static public final Integer CONFIRM_PENDIENTE = new Integer(2);  
	static public final Integer CONFIRM_FINALIZADA = new Integer(3);  
	static public final Integer CONFIRM_FINALIZADAERRORES = new Integer(4);  
	
	static public final Integer PDF_NOAPLICA = new Integer(5);  
	static public final Integer PDF_PROGRAMADA = new Integer(6);  
	static public final Integer PDF_PENDIENTE = new Integer(7);  
	static public final Integer PDF_PROCESANDO = new Integer(8);  
	static public final Integer PDF_FINALIZADA = new Integer(9);  
	static public final Integer PDF_FINALIZADAERRORES = new Integer(10);  

	static public final Integer ENVIO_NOAPLICA = new Integer(11);  
	static public final Integer ENVIO_PROGRAMADA = new Integer(12);  
	static public final Integer ENVIO_PENDIENTE = new Integer(13);  
	static public final Integer ENVIO_PROCESANDO = new Integer(14);  
	static public final Integer ENVIO_FINALIZADA = new Integer(15);  
	static public final Integer ENVIO_FINALIZADAERRORES = new Integer(16);  
	
	static public final Integer CONFIRM_PROCESANDO = new Integer(17); 

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_ESTADOCONFIRMFACT";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADO		= "IDESTADO";
	static public final String C_ALIAS		= "ALIAS";	
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_TIPO			= "TIPO";
	static public final String C_LENGUAJE			= "LENGUAJE";

	// Metodos SET
	public void setIdEstado (Integer id) 	{ this.idEstado = id; }
	public void setAlias (String s) 	{ this.alias = s; }
	public void setDescripcion (String s) 	{ this.descripcion = s; }
	public void setTipo (String s) 	{ this.tipo = s; }
	public void setLenguaje (String s) 	{ this.lenguaje = s; }
	
	// Metodos GET
	public Integer getIdEstado () 	{ return this.idEstado; }
	public String getAlias () 	{ return this.alias; }
	public String getDescripcion () 	{ return this.descripcion; }
	public String getTipo () 	{ return this.tipo; }
	public String getLenguaje () 	{ return this.lenguaje; }
}
