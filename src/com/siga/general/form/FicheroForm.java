package com.siga.general.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;

/**
 * @author jorgeta 
 * @date   30/08/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class FicheroForm extends MasterForm {
	private FormFile theFile;
	private String directorioArchivo;
	private String nombreArchivo;
	private String descripcionArchivo;
	private String extensionArchivo;
	private String fechaArchivo;
	
	
	private String idInstitucion;
	
	//Para la documentacion EJG
	private String idTipoEJG;
	private String anio;
	private String numero;
	private String idDocumentacion;
	private String idDocumento;
	private String idTipoDocumento;
	private String presentador;
	private String numEjg;
	
	private String idFichero;
	
	
	public FormFile getTheFile() {
		return theFile;
	}
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getExtensionArchivo() {
		return extensionArchivo;
	}
	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	public String getFechaArchivo() {
		return fechaArchivo;
	}
	public void setFechaArchivo(String fechaArchivo) {
		this.fechaArchivo = fechaArchivo;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
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
	public String getPresentador() {
		return presentador;
	}
	public void setPresentador(String presentador) {
		this.presentador = presentador;
	}
	public String getNumEjg() {
		return numEjg;
	}
	public void setNumEjg(String numEjg) {
		this.numEjg = numEjg;
	}
	public String getIdFichero() {
		return idFichero;
	}
	public void setIdFichero(String idFichero) {
		this.idFichero = idFichero;
	}
	public String getDescripcionArchivo() {
		return descripcionArchivo;
	}
	public void setDescripcionArchivo(String descripcionArchivo) {
		this.descripcionArchivo = descripcionArchivo;
	}
	public String getDirectorioArchivo() {
		return directorioArchivo;
	}
	public void setDirectorioArchivo(String directorioArchivo) {
		this.directorioArchivo = directorioArchivo;
	}
	
}
