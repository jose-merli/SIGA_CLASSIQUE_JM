/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.beans;

/**
 * Bean de la tabla ConOperacionConsulta
 */
public class ConOperacionConsultaBean extends MasterBean {
	
	private Integer idOperacion;
	private String descripcion;
	private String tipoOperador;
	private String simbolo;
	private String fechaModificacion;
	private Integer usuModificacion;

	static public final String IS_NULL ="is null";

	//	 Nombre campos de la tabla 
	static public final String C_IDOPERACION ="IDOPERACION";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_TIPOOPERADOR ="TIPOOPERADOR";
	static public final String C_SIMBOLO ="SIMBOLO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_OPERACIONCONSULTA";
	
	

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
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	public String getTipoOperador() {
		return tipoOperador;
	}
	public void setTipoOperador(String tipoOperador) {
		this.tipoOperador = tipoOperador;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
