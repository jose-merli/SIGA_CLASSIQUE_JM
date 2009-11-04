/*
 * Created on Jan 25, 2005
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * Bean de la tabla de partes de un expediente
 */
public class ExpPartesBean extends MasterBean {

//	Variables
	private Integer idInstitucion;
	private Integer idInstitucion_TipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idParte;
	private Integer idPersona;
	private Integer idRol;
	private String fechaModificacion;
	private String idDireccion;
	private Integer usuModificacion;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_IDPARTE ="IDPARTE";
	static public final String C_IDPERSONA ="IDPERSONA";
	static public final String C_IDDIRECCION ="IDDIRECCION";
	static public final String C_IDROL ="IDROL";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
		
	
	static public final String T_NOMBRETABLA = "EXP_PARTE";
	
	
	public Integer getIdInstitucion_TipoExpediente() {
		return idInstitucion_TipoExpediente;
	}
	public void setIdInstitucion_TipoExpediente(
			Integer idInstitucion_TipoExpediente) {
		this.idInstitucion_TipoExpediente = idInstitucion_TipoExpediente;
	}
	public Integer getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(Integer anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdParte() {
		return idParte;
	}
	public void setIdParte(Integer idParte) {
		this.idParte = idParte;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdRol() {
		return idRol;
	}
	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public Integer getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(Integer numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public String getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}
}
