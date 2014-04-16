package com.siga.censo.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;
import com.siga.general.form.FicheroForm;

public class AnexosCuentasBancariasForm extends FicheroForm {
	/* Variables */	
	private String idInstitucion, idPersona, idCuenta, idMandato, idAnexo, fechaCreacion, origen, descripcion;
	private String firmaFecha, firmaLugar, firmaDocumento, firmaFechaHora, firmaFechaMinutos;
	
	public String getIdInstitucion() {
		return idInstitucion;
	}

	public String getFirmaFechaHora() {
		return firmaFechaHora;
	}

	public void setFirmaFechaHora(String firmaFechaHora) {
		this.firmaFechaHora = firmaFechaHora;
	}

	public String getFirmaFechaMinutos() {
		return firmaFechaMinutos;
	}

	public void setFirmaFechaMinutos(String firmaFechaMinutos) {
		this.firmaFechaMinutos = firmaFechaMinutos;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getIdMandato() {
		return idMandato;
	}

	public void setIdMandato(String idMandato) {
		this.idMandato = idMandato;
	}

	public String getIdAnexo() {
		return idAnexo;
	}

	public void setIdAnexo(String idAnexo) {
		this.idAnexo = idAnexo;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFirmaFecha() {
		return firmaFecha;
	}

	public void setFirmaFecha(String firmaFecha) {
		this.firmaFecha = firmaFecha;
	}

	public String getFirmaLugar() {
		return firmaLugar;
	}

	public void setFirmaLugar(String firmaLugar) {
		this.firmaLugar = firmaLugar;
	}

	public String getFirmaDocumento() {
		return firmaDocumento;
	}

	public void setFirmaDocumento(String firmaDocumento) {
		this.firmaDocumento = firmaDocumento;
	}
}