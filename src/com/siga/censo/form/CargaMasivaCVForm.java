package com.siga.censo.form;


/**
 * 
 * @author jorgeta 
 * @date   06/04/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaCVForm extends CargaMasivaForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String tipoCVCod;
	private String tipoCVNombre;
	private String fechaInicio;
	private String fechaFin;
	private String subtipoCV1Cod;
	private String subtipoCV1Nombre;
	private String subtipoCV2Cod;
	private String subtipoCV2Nombre;
	
	
	private String creditos;
	private String fechaVerificacion;
	private String descripcion;
	
	
	public void clear() {
		setModo("");
	}

	public CargaMasivaCVForm clone() {
		CargaMasivaCVForm miForm = new CargaMasivaCVForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
		miForm.setTheFile(this.getTheFile());
		return miForm;
	}
	
	/**
	 * @return the colegiadoNif
	 */
	
	
	/**
	 * @return the tipoCVCod
	 */
	public String getTipoCVCod() {
		return tipoCVCod;
	}
	/**
	 * @param tipoCVCod the tipoCVCod to set
	 */
	public void setTipoCVCod(String tipoCVCod) {
		this.tipoCVCod = tipoCVCod;
	}
	/**
	 * @return the tipoCVNombre
	 */
	public String getTipoCVNombre() {
		return tipoCVNombre;
	}
	/**
	 * @param tipoCVNombre the tipoCVNombre to set
	 */
	public void setTipoCVNombre(String tipoCVNombre) {
		this.tipoCVNombre = tipoCVNombre;
	}
	/**
	 * @return the subtipoCV1Cod
	 */
	public String getSubtipoCV1Cod() {
		return subtipoCV1Cod;
	}
	/**
	 * @param subtipoCV1Cod the subtipoCV1Cod to set
	 */
	public void setSubtipoCV1Cod(String subtipoCV1Cod) {
		this.subtipoCV1Cod = subtipoCV1Cod;
	}
	
	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return the fechaFin
	 */
	public String getFechaFin() {
		return fechaFin;
	}
	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * @return the creditos
	 */
	public String getCreditos() {
		return creditos;
	}
	/**
	 * @param creditos the creditos to set
	 */
	public void setCreditos(String creditos) {
		this.creditos = creditos;
	}
	/**
	 * @return the fechaVerificacion
	 */
	public String getFechaVerificacion() {
		return fechaVerificacion;
	}
	/**
	 * @param fechaVerificacion the fechaVerificacion to set
	 */
	public void setFechaVerificacion(String fechaVerificacion) {
		this.fechaVerificacion = fechaVerificacion;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the subtipoCV1Nombre
	 */
	public String getSubtipoCV1Nombre() {
		return subtipoCV1Nombre;
	}
	/**
	 * @param subtipoCV1Nombre the subtipoCV1Nombre to set
	 */
	public void setSubtipoCV1Nombre(String subtipoCV1Nombre) {
		this.subtipoCV1Nombre = subtipoCV1Nombre;
	}
	/**
	 * @return the subtipoCV2Cod
	 */
	public String getSubtipoCV2Cod() {
		return subtipoCV2Cod;
	}
	/**
	 * @param subtipoCV2Cod the subtipoCV2Cod to set
	 */
	public void setSubtipoCV2Cod(String subtipoCV2Cod) {
		this.subtipoCV2Cod = subtipoCV2Cod;
	}
	/**
	 * @return the subtipoCV2Nombre
	 */
	public String getSubtipoCV2Nombre() {
		return subtipoCV2Nombre;
	}
	/**
	 * @param subtipoCV2Nombre the subtipoCV2Nombre to set
	 */
	public void setSubtipoCV2Nombre(String subtipoCV2Nombre) {
		this.subtipoCV2Nombre = subtipoCV2Nombre;
	}
	

	
}