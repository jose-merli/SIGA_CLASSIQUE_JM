/*
 * Fecha creación: 17/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.form.FicheroForm;

/**
 * Maneja el formulario que mantiene la tabla SCS_DefinirDocumentacionEJG
 */
public class DefinirDocumentacionEJGForm extends FicheroForm {
	private FormFile theFile;
	private String borrarFichero;
	private String  fechaLimite;
	private String 	documentacion;
	private String	fechaEntrega;
	private String	idTipoEJG;
	private String	anio;
	private String	numero;
	private String	idDocumentacion;
	private String	idInstitucion;	
	private String	regEntrada;
	private String	regSalida;
	private String	presentador;
	private String	idDocumento;
	private String	idTipoDocumento;
	private String	presentadorAnterior;
	private String	idDocumentoAnterior;
	private String	idTipoDocumentoAnterior;
	
	
	
	
	public FormFile getTheFile() {
		return theFile;
	}
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}
	public String getBorrarFichero() {
		return borrarFichero;
	}
	public void setBorrarFichero(String borrarFichero) {
		this.borrarFichero = borrarFichero;
	}
	public String getFechaLimite() {
		return fechaLimite;
	}
	public void setFechaLimite(String fechaLimite) {
		this.fechaLimite = fechaLimite;
	}
	public String getDocumentacion() {
		return documentacion;
	}
	public void setDocumentacion(String documentacion) {
		this.documentacion = documentacion;
	}
	public String getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public String getIdTipoEJG() {
		return idTipoEJG;
	}
	public void setIdTipoEJG(String idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
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
	public String getIdDocumentacion() {
		return idDocumentacion;
	}
	public void setIdDocumentacion(String idDocumentacion) {
		this.idDocumentacion = idDocumentacion;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getRegEntrada() {
		return regEntrada;
	}
	public void setRegEntrada(String regEntrada) {
		this.regEntrada = regEntrada;
	}
	public String getRegSalida() {
		return regSalida;
	}
	public void setRegSalida(String regSalida) {
		this.regSalida = regSalida;
	}
	public String getPresentador() {
		return presentador;
	}
	public void setPresentador(String presentador) {
		this.presentador = presentador;
	}
	public String getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	public String getPresentadorAnterior() {
		return presentadorAnterior;
	}
	public void setPresentadorAnterior(String presentadorAnterior) {
		this.presentadorAnterior = presentadorAnterior;
	}
	public String getIdDocumentoAnterior() {
		return idDocumentoAnterior;
	}
	public void setIdDocumentoAnterior(String idDocumentoAnterior) {
		this.idDocumentoAnterior = idDocumentoAnterior;
	}
	public String getIdTipoDocumentoAnterior() {
		return idTipoDocumentoAnterior;
	}
	public void setIdTipoDocumentoAnterior(String idTipoDocumentoAnterior) {
		this.idTipoDocumentoAnterior = idTipoDocumentoAnterior;
	}
		
	


	
}