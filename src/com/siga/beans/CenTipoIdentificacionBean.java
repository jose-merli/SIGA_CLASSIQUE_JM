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
public class CenTipoIdentificacionBean extends MasterBean{

	/* Variables */
	private Integer idTipoIdentificacion;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPOIDENTIFICACION";

	/* Nombre campos de la tabla */
	static public final String C_IDTIPOIDENTIFICACION 	= "IDTIPOIDENTIFICACION";
	static public final String C_DESCRIPCION			= "DESCRIPCION";
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
	public void setIdTipoIdentificacion (Integer id)	{ this.idTipoIdentificacion = id; }
	public void setDescripcion (String s)			{ this.descripcion = s; }

	// Metodos GET
	public Integer getIdTipoIdentificacion 	()	{ return this.idTipoIdentificacion; }
	public String getDescripcion    	()	{ return this.descripcion; }
}
