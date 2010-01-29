package com.siga.beans.eejg;

import com.siga.beans.MasterBean;
/**
 * 
 * @author jorgeta
 *
 */
public class ScsEejgXmlBean extends MasterBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8540074629939146417L;
	Long idPeticion;
	Integer idXml;
	Integer estado;
	String envioRespuesta;
	String xml;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_EEJG_XML";
	
	/*Nombre de campos de la tabla*/
	
	static public final String 	C_IDXML = "IDXML";
	static public final String 	C_IDPETICION = "IDPETICION";
	static public final String 	C_ESTADO = "ESTADO";
	
	static public final String 	C_XML = "XML";
	static public final String 	C_ENVIORESPUESTA = "ENVIORESPUESTA";
	public Long getIdPeticion() {
		return idPeticion;
	}
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	public Integer getIdXml() {
		return idXml;
	}
	public void setIdXml(Integer idXml) {
		this.idXml = idXml;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getEnvioRespuesta() {
		return envioRespuesta;
	}
	public void setEnvioRespuesta(String envioRespuesta) {
		this.envioRespuesta = envioRespuesta;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	
	
	
}