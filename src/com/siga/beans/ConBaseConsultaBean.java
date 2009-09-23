/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla ConBaseConsulta
 */
public class ConBaseConsultaBean extends MasterBean {
	
	private Integer idBase;
	private String descripcion;
	private String nombre;
	private String fechaModificacion;
	private Integer usuModificacion;

	
	//	 Nombre campos de la tabla 
	static public final String C_IDBASE ="IDBASE";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_NOMBRE ="NOMBRE";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_BASECONSULTA";
	
	
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
	public Integer getIdBase() {
		return idBase;
	}
	public void setIdBase(Integer idBase) {
		this.idBase = idBase;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
