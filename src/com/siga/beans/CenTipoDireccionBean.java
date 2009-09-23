/*
 * Created on Nov 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenTipoDireccionBean extends MasterBean{
	
	/* Variables */
	private Integer idTipoDireccion;
	private String 	descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_TIPODIRECCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPODIRECCION	= "IDTIPODIRECCION";	
	static public final String C_DESCRIPCION		= "DESCRIPCION";
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

	//	 Metodos GET
	public String getDescripcion() 		{return descripcion;	}
	public Integer getIdTipoDireccion() {return idTipoDireccion;}
	
	//	 Metodos SET
	public void setDescripcion(String descripcion)			{this.descripcion = descripcion;	}	
	public void setIdTipoDireccion(Integer idTipoDireccion) {this.idTipoDireccion = idTipoDireccion;}
}
