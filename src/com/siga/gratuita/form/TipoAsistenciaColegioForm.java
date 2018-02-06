package com.siga.gratuita.form;

import java.util.List;

import com.siga.general.MasterForm;


/**
 * 
 * @author jorgeta
 * La imaginación es mas importante que el conocimiento
 * @date 04/01/2018
 *
 */
public class TipoAsistenciaColegioForm extends MasterForm{
	
	
	private static final long serialVersionUID = 1L;
	
	private String idInstitucion;
    private String idTipoAsistenciaColegio; 
	private String descripcion;             
	private String importe;                 
	private String importeMaximo;           
	private String importeNoEditable;                 
	private String importeMaximoNoEditable;
    public String getImporteNoEditable() {
		return importeNoEditable;
	}
	public void setImporteNoEditable(String importeNoEditable) {
		this.importeNoEditable = importeNoEditable;
	}
	public String getImporteMaximoNoEditable() {
		return importeMaximoNoEditable;
	}
	public void setImporteMaximoNoEditable(String importeMaximoNoEditable) {
		this.importeMaximoNoEditable = importeMaximoNoEditable;
	}
	private String visibleMovil;            
    private String bloqueado;
    private String usuModificacion;
    private String fechaModificacion;
    private String tipoGuardia; 
    private List<String> tiposGuardia;
    private String descripcionTiposGuardia;
	
    
    
	public String getDescripcionTiposGuardia() {
		return descripcionTiposGuardia;
	}
	public void setDescripcionTiposGuardia(String descripcionTiposGuardia) {
		this.descripcionTiposGuardia = descripcionTiposGuardia;
	}
	public List<String> getTiposGuardia() {
		return tiposGuardia;
	}
	public void setTiposGuardia(List<String> tiposGuardia) {
		this.tiposGuardia = tiposGuardia;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getTipoGuardia() {
		return tipoGuardia;
	}
	public void setTipoGuardia(String tipoGuardia) {
		this.tipoGuardia = tipoGuardia;
	}
	
	public String getIdTipoAsistenciaColegio() {
		return idTipoAsistenciaColegio;
	}
	public void setIdTipoAsistenciaColegio(String idTipoAsistenciaColegio) {
		this.idTipoAsistenciaColegio = idTipoAsistenciaColegio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getImporteMaximo() {
		return importeMaximo;
	}
	public void setImporteMaximo(String importeMaximo) {
		this.importeMaximo = importeMaximo;
	}
	public String getVisibleMovil() {
		return visibleMovil;
	}
	public void setVisibleMovil(String visibleMovil) {
		this.visibleMovil = visibleMovil;
	}
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(String usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	
	
}