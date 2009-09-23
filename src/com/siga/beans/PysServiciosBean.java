/*
 * VERSIONES:
 * 
 * nuria.rgonzalez - 02-02-5 - Creacion
 * Modificada el 12-9-2005 por david.sanchezp para incluir la clave nueva idinstitucion.
 */

/**
*
* Clase que gestiona la tabla PYS_SERVICIOS de la BBDD
* 
*/
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysServiciosBean extends MasterBean{

	/* Variables */
	private Integer idTipoServicios;
	private Long idServicio;
	private String 	descripcion;
	private PysTipoServiciosBean tipoServicio;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_SERVICIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSERVICIO			= "IDSERVICIO";
	static public final String C_IDTIPOSERVICIOS	= "IDTIPOSERVICIOS";
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";

//	 Metodos SET
	public void setDescripcion(String descripcion) 			{this.descripcion = descripcion;	}
	public void setIdServicio(Long idServicio) 				{this.idServicio = idServicio;}
	public void setIdTipoServicios(Integer idTipoServicios) {this.idTipoServicios = idTipoServicios;	}
	public void setTipoServicio (PysTipoServiciosBean b) { this.tipoServicio = b; }
	
//	 Metodos GET
	public String getDescripcion() 		{return descripcion;}
	public Long getIdServicio() 		{return idServicio;	}
	public Integer getIdTipoServicios() {return idTipoServicios;}
	public PysTipoServiciosBean getTipoServicio   ()	{ return this.tipoServicio; }
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
}
