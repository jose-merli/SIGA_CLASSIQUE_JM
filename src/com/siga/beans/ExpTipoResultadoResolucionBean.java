/*
 * Created on Dec 27, 2004
 * @author leandro
 *
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de Tipo Resultado Resolucion
 */
public class ExpTipoResultadoResolucionBean extends MasterBean {

	//Variables
	
	private Integer idTipoResultado;
	private Integer idInstitucion;
	private String descripcion;
	private String fechaModificacion;
	private Integer usuModificacion;
	private String codExt;

	// Nombre campos de la tabla 
	static public final String C_IDTIPORESULTADO ="IDTIPORESULTADO";
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	static public final String C_CODEXT ="CODEXT";
	

	
	public Integer getIdTipoResultado() {
		return idTipoResultado;
	}
	public void setIdTipoResultado(Integer idTipoResultado) {
		this.idTipoResultado = idTipoResultado;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public String getCodExt() {
		return codExt;
	}
	public void setCodExt(String codExt) {
		this.codExt = codExt;
	}
	
	
	

}