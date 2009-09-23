/*
 * Created on 15-oct-2004
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
public class CenDocumentacionSolicitudBean extends MasterBean {

	/* Variables */
	private Integer idDocumentacion;
	private String descripcion;  

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DOCUMENTACIONSOLICITUD";

	
	/* Nombre campos de la tabla */
	static public final String C_IDDOCUMENTACION 		= "IDDOCUMENTACION";
	static public final String C_DESCRIPCION	 		= "DESCRIPCION";


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
	public void setIdDocumentacion (Integer id)			{ this.idDocumentacion = id; }
	public void setDescripcion (String d)			{ this.descripcion = d; }
	
	// Metodos GET
	public Integer getIdDocumentacion 	() 	{ return this.idDocumentacion; }	
	public String getDescripcion	() 	{ return this.descripcion; } 		
}
