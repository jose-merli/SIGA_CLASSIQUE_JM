/*
 * VERSIONES:
 * 
 * miguel.villegas - 4-02-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSFORMAPAGOSERVICIOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class PysFormaPagoServiciosBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoServicios;
	private Long idServicio;
	private Long idServiciosInstitucion;
	private Integer idFormaPago;
	private String 	internet;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_FORMAPAGOSERVICIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDTIPOSERVICIOS	= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO	= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION	= "IDSERVICIOSINSTITUCION";
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";
	static public final String C_INTERNET	= "INTERNET";

	// Metodos SET
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdTipoServicios (Integer id) 	{ this.idTipoServicios= id; }
	public void setIdServicio(Long id) 	{ this.idServicio= id; }
	public void setIdServiciosInstitucion(Long id) 	{ this.idServiciosInstitucion= id; }
	public void setIdFormaPago (Integer id) 	{ this.idFormaPago= id; }
	public void setInternet (String s)	{ this.internet = s; }

	// Metodos GET
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Integer getIdTipoServicios () 	{ return this.idTipoServicios; }
	public Long getIdServicio() 	{ return this.idServicio;}
	public Long getIdServiciosInstitucion() 	{ return this.idServiciosInstitucion;}
	public Integer getIdFormaPago () 	{ return this.idFormaPago;}
	public String getInternet ()	{ return this.internet;}
}
