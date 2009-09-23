/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class FacTiposServInclsEnFactBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoServicios, idServicio;
	
	private Long idSerieFacturacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_TIPOSSERVINCLSENFACT";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION = "IDSERIEFACTURACION";
	static public final String C_IDTIPOSERVICIOS 	= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO 	= "IDSERVICIO";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
	public void setIdTipoServicios (Integer id)		{ this.idTipoServicios = id; }
	public void setIdServicio (Integer id)		{ this.idServicio = id; }
	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
	public Integer getIdTipoServicios		()	{ return this.idTipoServicios; }
	public Integer getIdServicio		()	{ return this.idServicio; }
}
