/*
 * Created on 21-oct-2004
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
public class CenTratamientoBean extends MasterBean {

	/* Variables */
	private Integer idTratamiento;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TRATAMIENTO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTRATAMIENTO 	= "IDTRATAMIENTO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";

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
	public void setIdTratamiento (Integer id) { this.idTratamiento = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdTratamiento  ()	{ return this.idTratamiento; }
	public String getDescripcion ()	{ return this.descripcion; }
}
