/*
 * Created on 25-oct-2004
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
public class CenEstadoColegialBean extends MasterBean {

	/* Variables */
	private Integer idEstado;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ESTADOCOLEGIAL";

	/* Nombre campos de la tabla */
	static public final String C_IDESTADO 		= "IDESTADO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";

	// Metodos SET
	public void setIdEstado (Integer id) 	{ this.idEstado = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdEstado 		()	{ return this.idEstado; }
	public String getDescripcion    ()	{ return this.descripcion; }
}
