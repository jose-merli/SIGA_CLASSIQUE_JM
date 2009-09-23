/*
 * Created on 29-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class CenEstadoSolicitudBean extends MasterBean {

	/* Variables */
	private Integer idEstado;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ESTADOSOLICITUD";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADO		= "IDESTADO";
	static public final String C_DESCRIPCION	= "DESCRIPCION";

	public String getDescripcion() 	{	return descripcion;	}
	public Integer getIdEstado() 	{	return idEstado;	}

	public void setDescripcion(String descripcion) 	{	this.descripcion = descripcion;	}
	public void setIdEstado(Integer idEstado) 		{	this.idEstado = idEstado;		}
}
