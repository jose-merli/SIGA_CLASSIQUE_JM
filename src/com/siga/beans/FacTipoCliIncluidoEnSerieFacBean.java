/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class FacTipoCliIncluidoEnSerieFacBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idInstitucionGrupo, idGrupo;
	
	private Long idSerieFacturacion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_TIPOCLIINCLUIDOENSERIEFAC";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		 = "IDINSTITUCION";
	static public final String C_IDINSTITUCION_GRUPO = "IDINSTITUCION_GRUPO";
	static public final String C_IDSERIEFACTURACION  = "IDSERIEFACTURACION";
	static public final String C_IDGRUPO 			 = "IDGRUPO";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
	public void setIdGrupo (Integer id)				{ this.idGrupo = id; }
	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
	public Integer getIdGrupo				()	{ return this.idGrupo; }
	public Integer getIdInstitucionGrupo() {
		return idInstitucionGrupo;
	}
	public void setIdInstitucionGrupo(Integer idInstitucionGrupo) {
		this.idInstitucionGrupo = idInstitucionGrupo;
	}
}
