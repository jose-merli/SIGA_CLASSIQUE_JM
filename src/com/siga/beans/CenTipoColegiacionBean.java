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
public class CenTipoColegiacionBean extends MasterBean {

	/* Variables */
	private Integer idTipoColegiacion;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOCOLEGIACION";

	/* Nombre campos de la tabla */
	static public final String C_IDCOLEGIACION 	= "IDTIPOCOLEGIACION";
	static public final String C_DESCRIPCION	= "DESCRIPCION";

	// Metodos SET
	public void setIdTipoColegiacion (Integer id) { this.idTipoColegiacion = id; }
	public void setDescripcion (String s)	{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdTipoColegiacion ()	{ return this.idTipoColegiacion; }
	public String getDescripcion    ()	{ return this.descripcion; }
}
