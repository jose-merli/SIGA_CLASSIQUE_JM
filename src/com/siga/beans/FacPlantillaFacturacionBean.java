/*
 * VERSIONES:
 * yolanda.garcia - 16-11-2004 - Creación
 */

package com.siga.beans;


public class FacPlantillaFacturacionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idPlantilla;
	
	private String 	descripcion, plantillaPDF;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_PLANTILLAFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 	= "IDINSTITUCION";
	static public final String C_IDPLANTILLA 	= "IDPLANTILLA";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_PLANTILLAPDF	= "PLANTILLAPDF";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdPlantilla (Integer id)			{ this.idPlantilla = id; }
	public void setDescripcion (String d)			{ this.descripcion = d; }
	public void setPlantillaPDF (String d)			{ this.plantillaPDF = d; }
	
	// Metodos GET
	public Integer getIdInstitucion 	()	{ return this.idInstitucion; }
	public Integer getIdPlantilla		()	{ return this.idPlantilla; }
	public String getDescripcion		()	{ return this.descripcion; }
	public String getPlantillaPDF		()	{ return this.plantillaPDF; }
}
