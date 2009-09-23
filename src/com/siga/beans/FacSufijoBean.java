package com.siga.beans;

/**
 * Bean de la tabla FAC_SUFIJO
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class FacSufijoBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion;
	private String 	sufijo=null, concepto=null, varios=null; 
					
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_SUFIJO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_SUFIJO				= "SUFIJO";
	static public final String C_CONCEPTO			= "CONCEPTO";
	static public final String C_VARIOS				= "VARIOS";

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
	public String getVarios() {
		return varios;
	}
	public void setVarios(String varios) {
		this.varios = varios;
	}
}
