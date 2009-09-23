/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;

public class FacGrupCritIncluidosEnSerieBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idGruposCriterios, idInstitucionGrup;
	
	private Long idSerieFacturacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_GRUPCRITINCLUIDOSENSERIE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION = "IDSERIEFACTURACION";
	static public final String C_IDGRUPOSCRITERIOS	= "IDGRUPOSCRITERIOS";
	static public final String C_IDINSTITUCION_GRUP	= "IDINSTITUCION_GRUP";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdInstitucionGrup (Integer id)		{ this.idInstitucionGrup = id; }
	public void setIdSerieFacturacion (Long id)	{ this.idSerieFacturacion = id; }
	public void setIdGruposCriterios (Integer id)	{ this.idGruposCriterios = id; }
	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Integer getIdInstitucionGrup 		()	{ return this.idInstitucionGrup; }
	public Long getIdSerieFacturacion	()	{ return this.idSerieFacturacion; }
	public Integer getIdGruposCriterios		()	{ return this.idGruposCriterios; }
}
