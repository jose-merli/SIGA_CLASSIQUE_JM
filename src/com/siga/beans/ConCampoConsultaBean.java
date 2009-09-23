/*
 * Created on Apr 13, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.beans;

/**
 * Bean de la tabla de campos de una consulta
 */
public class ConCampoConsultaBean extends MasterBean {

//	Variables
	private Integer idCampo;
	private String descripcion;
	private String nombreReal;
	private String nombreEnConsulta;
	private String observaciones;
	private String tipoCampo;
	private String selectAyuda;
	private String formato;
	private Integer idTipoCampo;
	private Integer idTabla;
	private String visibilidad;
	private Integer longitud;
	private Integer escala;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDCAMPO ="IDCAMPO";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_NOMBREREAL ="NOMBREREAL";
	static public final String C_NOMBREENCONSULTA ="NOMBREENCONSULTA";
	static public final String C_OBSERVACIONES ="OBSERVACIONES";
	static public final String C_TIPOCAMPO ="TIPOCAMPO";
	static public final String C_SELECTAYUDA ="SELECTAYUDA";
	static public final String C_FORMATO ="FORMATO";
	static public final String C_IDTIPOCAMPO ="IDTIPOCAMPO";
	static public final String C_IDTABLA ="IDTABLA";
	static public final String C_VISIBILIDAD ="VISIBILIDAD";
	static public final String C_LONGITUD ="LONGITUD";
	static public final String C_ESCALA ="ESCALA";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_CAMPOCONSULTA";
	
	
	public Integer getEscala() {
		return escala;
	}
	public void setEscala(Integer escala) {
		this.escala = escala;
	}
	public Integer getLongitud() {
		return longitud;
	}
	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public Integer getIdCampo() {
		return idCampo;
	}
	public void setIdCampo(Integer idCampo) {
		this.idCampo = idCampo;
	}
	public Integer getIdTabla() {
		return idTabla;
	}
	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}
	public Integer getIdTipoCampo() {
		return idTipoCampo;
	}
	public void setIdTipoCampo(Integer idTipoCampo) {
		this.idTipoCampo = idTipoCampo;
	}
	public String getNombreEnConsulta() {
		return nombreEnConsulta;
	}
	public void setNombreEnConsulta(String nombreEnConsulta) {
		this.nombreEnConsulta = nombreEnConsulta;
	}
	public String getNombreReal() {
		return nombreReal;
	}
	public void setNombreReal(String nombreReal) {
		this.nombreReal = nombreReal;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getSelectAyuda() {
		return selectAyuda;
	}
	public void setSelectAyuda(String selectAyuda) {
		this.selectAyuda = selectAyuda;
	}
	public String getTipoCampo() {
		return tipoCampo;
	}
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public String getVisibilidad() {
		return visibilidad;
	}
	public void setVisibilidad(String visibilidad) {
		this.visibilidad = visibilidad;
	}
}
