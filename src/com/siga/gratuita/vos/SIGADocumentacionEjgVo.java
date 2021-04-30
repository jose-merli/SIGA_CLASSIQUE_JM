package com.siga.gratuita.vos;

import java.util.Date;

import org.redabogacia.sigaservices.app.exceptions.BusinessException;

import com.siga.beans.ScsDocumentacionEJGBean;

public class SIGADocumentacionEjgVo extends ScsDocumentacionEJGBean implements Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2477638746584725039L;
	private String fechaLimite;
	private String fechaEntrega;
	private String numEjg;
	private String idPresentador;
	private String descPresentador;
	private String documentoAbreviatura;
	private String descripcionArchivo;
	private String nombreArchivo;
	private String directorioArchivo;
	private String pathArchivo;
	private String extensionArchivo;
	private Date fechaArchivo;
	byte[] fichero;
	boolean borrarFichero = false;
	private Short numIntercambiosOk;

	public SIGADocumentacionEjgVo() {

	}

	public String getNumEjg() {
		return numEjg;
	}

	public void setNumEjg(String numEjg) {
		this.numEjg = numEjg;
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

	public Date getFechaArchivo() {
		return fechaArchivo;
	}

	public void setFechaArchivo(Date fechaArchivo) {
		this.fechaArchivo = fechaArchivo;
	}

	public byte[] getFichero() {
		return fichero;
	}

	public void setFichero(byte[] fichero) {
		this.fichero = fichero;
	}

	public boolean isBorrarFichero() {
		return borrarFichero;
	}

	public void setBorrarFichero(boolean borrarFichero) {
		this.borrarFichero = borrarFichero;
	}

	public Object clone() throws BusinessException {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new BusinessException("No se puede clonar");
		}
		return obj;
	}

	public String getDescripcionArchivo() {
		return descripcionArchivo;
	}

	public void setDescripcionArchivo(String descripcionArchivo) {
		this.descripcionArchivo = descripcionArchivo;
	}

	public String getPathArchivo() {
		return pathArchivo;
	}

	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}

	public String getDirectorioArchivo() {
		return directorioArchivo;
	}

	public void setDirectorioArchivo(String directorioArchivo) {
		this.directorioArchivo = directorioArchivo;
	}

	public String getIdPresentador() {
		return idPresentador;
	}

	public void setIdPresentador(String idPresentador) {
		this.idPresentador = idPresentador;
	}

	public String getDescPresentador() {
		return descPresentador;
	}

	public void setDescPresentador(String descPresentador) {
		this.descPresentador = descPresentador;
	}

	public String getDocumentoAbreviatura() {
		return documentoAbreviatura;
	}

	public void setDocumentoAbreviatura(String documentoAbreviatura) {
		this.documentoAbreviatura = documentoAbreviatura;
	}

	public String getFechaLimite() {
		return fechaLimite;
	}

	public void setFechaLimite(String fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public String getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Short getNumIntercambiosOk() {
		return numIntercambiosOk;
	}

	public void setNumIntercambiosOk(Short numIntercambiosOk) {
		this.numIntercambiosOk = numIntercambiosOk;
	}

}
