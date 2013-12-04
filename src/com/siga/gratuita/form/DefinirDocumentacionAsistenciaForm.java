package com.siga.gratuita.form;

import com.siga.general.form.FicheroForm;

/**
 * @author Carlos Ruano Martínez 
 * @date 25/11/2013
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class DefinirDocumentacionAsistenciaForm extends FicheroForm {

	private String	idTipoDocumento;	
	
	private String	idDocumento;
	
	private String	idInstitucion;
	
	private String anio;
	
	private String numero;
	
	private String observaciones;
	
	private String idActuacion;
	
	private String fechaEntrada;
	
	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	public String getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}
	
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getIdActuacion() {
		return idActuacion;
	}
	public void setIdActuacion(String idActuacion) {
		this.idActuacion = idActuacion;
	}
	public String getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
}
