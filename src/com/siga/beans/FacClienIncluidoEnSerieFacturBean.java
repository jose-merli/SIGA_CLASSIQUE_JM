/*
 * VERSIONES:
 * yolanda.garcia - 13-12-2004 - Creación
 */

package com.siga.beans;


public class FacClienIncluidoEnSerieFacturBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	
	private Long idPersona, idSerieFacturacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_CLIENINCLUIDOENSERIEFACTUR";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDPERSONA 			= "IDPERSONA";
	static public final String C_IDSERIEFACTURACION = "IDSERIEFACTURACION";
	
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdPersona (Long id)				{ this.idPersona = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
		
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdPersona				()	{ return this.idPersona; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
}
