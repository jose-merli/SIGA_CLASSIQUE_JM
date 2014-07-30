package com.siga.beans;

/**
 * Bean de la tabla FAC_SUFIJO
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class FacSufijoBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idSufijo;
	private String 	sufijo=null, concepto=null, defecto=null;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_SUFIJO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDSUFIJO 			= "IDSUFIJO";
	static public final String C_SUFIJO				= "SUFIJO";
	static public final String C_CONCEPTO			= "CONCEPTO";
	static public final String C_DEFECTO			= "DEFECTO";

	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getSufijo() {
		return sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public Integer getIdSufijo() {
		return idSufijo;
	}
	public void setIdSufijo(Integer idSufijo) {
		this.idSufijo = idSufijo;
	}
	public String getDefecto() {
		return defecto;
	}
	public void setDefecto(String defecto) {
		this.defecto = defecto;
	}
}
