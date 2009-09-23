
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class CenDocumentacionModalidadBean extends MasterBean {

	/* Variables */
	private Integer idModalidad, idInstitucion;
	private String descripcion, codigoExt;  

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DOCUMENTACIONMODALIDAD";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";                          
	static public final String C_IDMODALIDAD        = "IDMODALIDAD";                         
	static public final String C_DESCRIPCION        = "DESCRIPCION";                     
	static public final String C_CODIGOEXT          = "CODIGOEXT";                      
	
	public String getCodigoExt() {
		return codigoExt;
	}
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdModalidad() {
		return idModalidad;
	}
	public void setIdModalidad(Integer idModalidad) {
		this.idModalidad = idModalidad;
	}
}
