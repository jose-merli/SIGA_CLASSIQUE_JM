/*
 * VERSIONES:
 * yolanda.garcia - 24-01-2005 - Creación
 */

package com.siga.beans;


public class GenClientesTemporalBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, posicion;
	
	private Long contador, idPersona;
	
	private String 	fecha, salto;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "GEN_CLIENTESTEMPORAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_CONTADOR      = "CONTADOR";
	static public final String C_POSICION 	   = "POSICION";
	static public final String C_IDPERSONA 	   = "IDPERSONA";
	static public final String C_FECHA 	   	   = "FECHA";
	static public final String C_SALTO 	       = "SALTO";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)	{ this.idInstitucion = id; }
	public void setContador (Long id)			{ this.contador = id; }
	public void setPosicion (Integer id)		{ this.posicion = id; }
	public void setIdPersona (Long id)			{ this.idPersona = id; }
	public void setFecha (String f)				{ this.fecha = f; }
	public void setSalto (String s)	 			{ this.salto = s; }
	
	// Metodos GET
	public Integer getIdInstitucion 	()	{ return this.idInstitucion; }
	public Long getContador				()	{ return this.contador; }
	public Integer getPosicion			()	{ return this.posicion; }
	public Long getIdPersona			()	{ return this.idPersona; }
	public String  getFecha 			()	{ return this.fecha; }
	public String  getSalto				()	{ return this.salto; }
}
