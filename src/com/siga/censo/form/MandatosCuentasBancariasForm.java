package com.siga.censo.form;

import com.siga.general.form.FicheroForm;

public class MandatosCuentasBancariasForm  extends FicheroForm {	
	

	/* Variables */	
	private String idInstitucion, idPersona, idCuenta, idMandato, tipoMandato, fechaCreacion, usuCreacion, refMandatoSepa, tipoPago, esquema, autorizacionB2B;
	private String acreedorTipoId, acreedorId, acreedorNombre, acreedorDomicilio, acreedorCodigoPostal, acreedorIdPais, acreedorPais, acreedorIdProvincia, acreedorProvincia, acreedorIdPoblacion, acreedorPoblacion;
	private String deudorTipoId, deudorId, deudorNombre, deudorDomicilio, deudorCodigoPostal, deudorIdPais, deudorPais, deudorIdProvincia, deudorProvincia, deudorIdPoblacion, deudorPoblacion, deudorPoblacionExtranjera;
	private String firmaFecha, firmaLugar, idFichero;
	private String fechaUso, iban, bic, banco;		
	private String modoMandato, nombrePersona, numero;
	private String[] modosMandato = {"mandatoCuentaBancaria", "ficherosMandatoCuentaBancaria"};		

	public String getUsuCreacion() {
		return usuCreacion;
	}

	public void setUsuCreacion(String usuCreacion) {
		this.usuCreacion = usuCreacion;
	}
	
	public String getIdFichero() {
		return idFichero;
	}	

	public void setIdFichero(String idFichero) {
		this.idFichero = idFichero;
	}	

	public String getDeudorPoblacionExtranjera() {
		return deudorPoblacionExtranjera;
	}

	public String getAcreedorTipoId() {
		return acreedorTipoId;
	}

	public void setAcreedorTipoId(String acreedorTipoId) {
		this.acreedorTipoId = acreedorTipoId;
	}

	public String getDeudorTipoId() {
		return deudorTipoId;
	}

	public void setDeudorTipoId(String deudorTipoId) {
		this.deudorTipoId = deudorTipoId;
	}

	public void setDeudorPoblacionExtranjera(String deudorPoblacionExtranjera) {
		this.deudorPoblacionExtranjera = deudorPoblacionExtranjera;
	}

	public String getTipoMandato() {
		return tipoMandato;
	}

	public void setTipoMandato(String tipoMandato) {
		this.tipoMandato = tipoMandato;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getRefMandatoSepa() {
		return refMandatoSepa;
	}

	public void setRefMandatoSepa(String refMandatoSepa) {
		this.refMandatoSepa = refMandatoSepa;
	}

	public String getAcreedorIdPais() {
		return acreedorIdPais;
	}

	public void setAcreedorIdPais(String acreedorIdPais) {
		this.acreedorIdPais = acreedorIdPais;
	}

	public String getAcreedorIdProvincia() {
		return acreedorIdProvincia;
	}

	public void setAcreedorIdProvincia(String acreedorIdProvincia) {
		this.acreedorIdProvincia = acreedorIdProvincia;
	}

	public String getAcreedorIdPoblacion() {
		return acreedorIdPoblacion;
	}

	public void setAcreedorIdPoblacion(String acreedorIdPoblacion) {
		this.acreedorIdPoblacion = acreedorIdPoblacion;
	}

	public String getDeudorIdPais() {
		return deudorIdPais;
	}

	public void setDeudorIdPais(String deudorIdPais) {
		this.deudorIdPais = deudorIdPais;
	}

	public String getDeudorIdProvincia() {
		return deudorIdProvincia;
	}

	public void setDeudorIdProvincia(String deudorIdProvincia) {
		this.deudorIdProvincia = deudorIdProvincia;
	}

	public String getDeudorIdPoblacion() {
		return deudorIdPoblacion;
	}

	public void setDeudorIdPoblacion(String deudorIdPoblacion) {
		this.deudorIdPoblacion = deudorIdPoblacion;
	}

	public String getFechaUso() {
		return fechaUso;
	}

	public void setFechaUso(String fechaUso) {
		this.fechaUso = fechaUso;
	}

	public void setModosMandato(String[] modosMandato) {
		this.modosMandato = modosMandato;
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

	public String getAutorizacionB2B() {
		return autorizacionB2B;
	}

	public void setAutorizacionB2B(String autorizacionB2B) {
		this.autorizacionB2B = autorizacionB2B;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public String getModoMandato() {
		return modoMandato;
	}

	public void setModoMandato(String modoMandato) {
		this.modoMandato = modoMandato;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getEsquema() {
		return esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	public String getAcreedorId() {
		return acreedorId;
	}

	public void setAcreedorId(String acreedorId) {
		this.acreedorId = acreedorId;
	}

	public String getAcreedorNombre() {
		return acreedorNombre;
	}

	public void setAcreedorNombre(String acreedorNombre) {
		this.acreedorNombre = acreedorNombre;
	}

	public String getAcreedorDomicilio() {
		return acreedorDomicilio;
	}

	public void setAcreedorDomicilio(String acreedorDomicilio) {
		this.acreedorDomicilio = acreedorDomicilio;
	}

	public String getAcreedorCodigoPostal() {
		return acreedorCodigoPostal;
	}

	public void setAcreedorCodigoPostal(String acreedorCodigoPostal) {
		this.acreedorCodigoPostal = acreedorCodigoPostal;
	}

	public String getAcreedorPoblacion() {
		return acreedorPoblacion;
	}

	public void setAcreedorPoblacion(String acreedorPoblacion) {
		this.acreedorPoblacion = acreedorPoblacion;
	}

	public String getAcreedorPais() {
		return acreedorPais;
	}

	public void setAcreedorPais(String acreedorPais) {
		this.acreedorPais = acreedorPais;
	}

	public String getAcreedorProvincia() {
		return acreedorProvincia;
	}

	public void setAcreedorProvincia(String acreedorProvincia) {
		this.acreedorProvincia = acreedorProvincia;
	}

	public String getDeudorId() {
		return deudorId;
	}

	public void setDeudorId(String deudorId) {
		this.deudorId = deudorId;
	}

	public String getDeudorNombre() {
		return deudorNombre;
	}

	public void setDeudorNombre(String deudorNombre) {
		this.deudorNombre = deudorNombre;
	}

	public String getDeudorDomicilio() {
		return deudorDomicilio;
	}

	public void setDeudorDomicilio(String deudorDomicilio) {
		this.deudorDomicilio = deudorDomicilio;
	}

	public String getDeudorCodigoPostal() {
		return deudorCodigoPostal;
	}

	public void setDeudorCodigoPostal(String deudorCodigoPostal) {
		this.deudorCodigoPostal = deudorCodigoPostal;
	}

	public String getDeudorPoblacion() {
		return deudorPoblacion;
	}

	public void setDeudorPoblacion(String deudorPoblacion) {
		this.deudorPoblacion = deudorPoblacion;
	}

	public String getDeudorPais() {
		return deudorPais;
	}

	public void setDeudorPais(String deudorPais) {
		this.deudorPais = deudorPais;
	}

	public String getDeudorProvincia() {
		return deudorProvincia;
	}

	public void setDeudorProvincia(String deudorProvincia) {
		this.deudorProvincia = deudorProvincia;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}
	
	public String[] getModosMandato () {return this.modosMandato;}		
}