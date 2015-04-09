package com.siga.censo.form;


import org.apache.struts.upload.FormFile;

import com.siga.administracion.SIGAConstants;
import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;
/**
 * 
 * @author jorgeta 
 * @date   06/04/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaCVForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Es la institucion de consulta
	 */
	String idInstitucion;
	String idCargaMasivaCV;
	String fechaCarga;
	String usuario;
	String nombreFichero;
	String numRegistros;
	private FilaExtElement[] elementosFila;
	FormFile theFile;
	
	String colegiadoNumero;
	String colegiadoNif;
	String colegiadoNombre;
	String tipoCVCod;
	String tipoCVNombre;
	String fechaInicio;
	String fechaFin;
	String subtipoCV1Cod;
	String subtipoCV1Nombre;
	String subtipoCV2Cod;
	String subtipoCV2Nombre;
	
	
	String creditos;
	String fechaVerificacion;
	String descripcion;
	
	
	


	
	
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

	public CargaMasivaCVForm clone() {
		CargaMasivaCVForm miForm = new CargaMasivaCVForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
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
	public String getColegiadoNif() {
		return colegiadoNif;
	}
	/**
	 * @param colegiadoNif the colegiadoNif to set
	 */
	public void setColegiadoNif(String colegiadoNif) {
		this.colegiadoNif = colegiadoNif;
	}
	/**
	 * @return the colegiadoNombre
	 */
	public String getColegiadoNombre() {
		return colegiadoNombre;
	}
	/**
	 * @param colegiadoNombre the colegiadoNombre to set
	 */
	public void setColegiadoNombre(String colegiadoNombre) {
		this.colegiadoNombre = colegiadoNombre;
	}
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