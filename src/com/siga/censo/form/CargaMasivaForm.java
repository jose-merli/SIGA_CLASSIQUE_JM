package com.siga.censo.form;


import org.apache.struts.upload.FormFile;

import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;
/**
 * 
 * @author jorgeta 
 * @date   04/05/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Es la institucion de consulta
	 */
	private String idInstitucion;
	private String idCargaMasivaCV;
	private String fechaCarga;
	private String usuario;
	private String nombreFichero;
	private String numRegistros;
	private FilaExtElement[] elementosFila;
	private FormFile theFile;
	
	private String colegiadoNumero;
	private String personaNif;
	private String personaNombre;
	private String error;
	
	
	private String idFichero;
	private String idFicheroLog;
	
	private String codIdioma; 
	
	
	/**
	 * @return the fechaCarga
	 */
	public String getFechaCarga() {
		return fechaCarga;
	}
	/**
	 * @param fechaCarga the fechaCarga to set
	 */
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	/**
	 * @return the idInstitucion
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public void clear() {
		setModo("");
	}

	public CargaMasivaForm clone() {
		CargaMasivaForm miForm = new CargaMasivaForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
		miForm.setTheFile(this.getTheFile());
		return miForm;
	}
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the nombreFichero
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}
	/**
	 * @param nombreFichero the nombreFichero to set
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	/**
	 * @return the idCargaMasivaCV
	 */
	public String getIdCargaMasivaCV() {
		return idCargaMasivaCV;
	}
	/**
	 * @param idCargaMasivaCV the idCargaMasivaCV to set
	 */
	public void setIdCargaMasivaCV(String idCargaMasivaCV) {
		this.idCargaMasivaCV = idCargaMasivaCV;
	}
	/**
	 * @return the numRegistros
	 */
	public String getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros the numRegistros to set
	 */
	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}
	public FilaExtElement[] getElementosFila() {
		elementosFila = new FilaExtElement[2];
		elementosFila[0]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
		elementosFila[1]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
		return elementosFila;
	}
	/**
	 * @return the theFile
	 */
	public FormFile getTheFile() {
		return theFile;
	}
	/**
	 * @param theFile the theFile to set
	 */
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}
	/**
	 * @return the colegiadoNumero
	 */
	public String getColegiadoNumero() {
		return colegiadoNumero;
	}
	/**
	 * @param colegiadoNumero the colegiadoNumero to set
	 */
	public void setColegiadoNumero(String colegiadoNumero) {
		this.colegiadoNumero = colegiadoNumero;
	}
	/**
	 * @return the colegiadoNif
	 */
	
	
	
	/**
	 * @return the personaNif
	 */
	public String getPersonaNif() {
		return personaNif;
	}
	/**
	 * @param personaNif the personaNif to set
	 */
	public void setPersonaNif(String personaNif) {
		this.personaNif = personaNif;
	}
	/**
	 * @return the personaNombre
	 */
	public String getPersonaNombre() {
		return personaNombre;
	}
	/**
	 * @param personaNombre the personaNombre to set
	 */
	public void setPersonaNombre(String personaNombre) {
		this.personaNombre = personaNombre;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return the idFichero
	 */
	public String getIdFichero() {
		return idFichero;
	}
	/**
	 * @param idFichero the idFichero to set
	 */
	public void setIdFichero(String idFichero) {
		this.idFichero = idFichero;
	}
	/**
	 * @return the idFicheroLog
	 */
	public String getIdFicheroLog() {
		return idFicheroLog;
	}
	/**
	 * @param idFicheroLog the idFicheroLog to set
	 */
	public void setIdFicheroLog(String idFicheroLog) {
		this.idFicheroLog = idFicheroLog;
	}
	/**
	 * @return the codIdioma
	 */
	public String getCodIdioma() {
		return codIdioma;
	}
	/**
	 * @param codIdioma the codIdioma to set
	 */
	public void setCodIdioma(String codIdioma) {
		this.codIdioma = codIdioma;
	}
	
}