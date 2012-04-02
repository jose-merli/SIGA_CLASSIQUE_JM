package com.siga.ws.mutualidad;

import java.util.List;

import com.siga.comun.vos.ValueKeyVO;

public class RespuestaMutualidad {
	
	private boolean correcto = false;
	private boolean posibleAlta;
	private String mensajeError = "";
	private Integer idSolicitud;
	private Double cuota=0.0;
	private Double capital=0.0;
	private List<ValueKeyVO> beneficiarios;
	private List<ValueKeyVO> asistencia;
	private List<ValueKeyVO> tiposIdentificador;
	private List<ValueKeyVO> tiposDomicilio;
	private List<ValueKeyVO> tiposDireccion;
	private List<ValueKeyVO> sexos;
	private List<ValueKeyVO> ejerciente;
	private List<ValueKeyVO> periodicidades;
	private List<ValueKeyVO> estadosCiviles;
	private List<ValueKeyVO> coberturas;
	private String valorRespuesta;
	
	private byte[] pdf;
	private String rutaPDF;


	public void setCorrecto(boolean b) { 
		this.correcto = b; 
	}

	public void setMensajeError(String valorRespuesta) {
		this.mensajeError = valorRespuesta;
	}

	public void setIdSolicitud(Integer idSol) {
		this.idSolicitud=idSol;
	}

	public boolean isCorrecto() {
		return correcto;
	}
	
	public boolean isPosibleAlta() {
		return posibleAlta;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	public Double getCuota() {
		return cuota;
	}

	public Double getCapital() {
		return capital;
	}
	
	public String getValorRespuesta() {
		return valorRespuesta;
	}
	
	public void setCuota(Double cuota) {
		this.cuota = cuota;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
	}

	public List<ValueKeyVO> getBeneficiarios() {
		return beneficiarios;
	}

	public List<ValueKeyVO> getAsistencia() {
		return asistencia;
	}

	public List<ValueKeyVO> getTiposIdentificador() {
		return tiposIdentificador;
	}

	public List<ValueKeyVO> getTiposDomicilio() {
		return tiposDomicilio;
	}

	public List<ValueKeyVO> getTiposDireccion() {
		return tiposDireccion;
	}

	public List<ValueKeyVO> getSexos() {
		return sexos;
	}

	public List<ValueKeyVO> getEjerciente() {
		return ejerciente;
	}

	public List<ValueKeyVO> getPeriodicidades() {
		return periodicidades;
	}

	public List<ValueKeyVO> getEstadosCiviles() {
		return estadosCiviles;
	}

	public List<ValueKeyVO> getCoberturas() {
		return coberturas;
	}
	
	public byte[] getPDF() {
		return pdf;
	}
	
	public String getRutaPDF() {
		return rutaPDF;
	}

	public void setBeneficiarios(List<ValueKeyVO> map) {
		this.beneficiarios = map;
	}

	public void setAsistencia(List<ValueKeyVO> map) {
		this.asistencia = map;
	}

	public void setTiposIdentificador(List<ValueKeyVO> map) {
		this.tiposIdentificador = map;
	}

	public void setTiposDomicilio(List<ValueKeyVO> map) {
		this.tiposDomicilio = map;
	}

	public void setTiposDireccion(List<ValueKeyVO> map) {
		this.tiposDireccion = map;
	}

	public void setSexos(List<ValueKeyVO> map) {
		this.sexos = map;
	}

	public void setEjerciente(List<ValueKeyVO> map) {
		this.ejerciente = map;
	}

	public void setPeriodicidades(List<ValueKeyVO> map) {
		this.periodicidades = map;
	}

	public void setEstadosCiviles(List<ValueKeyVO> map) {
		this.estadosCiviles = map;
	}

	public void setCoberturas(List<ValueKeyVO> map) {
		this.coberturas = map;
	}

	public void setValorRespuesta(String valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}

	public void setPDF(byte[] pdf) {
		this.pdf = pdf;
	}

	public void setPosibleAlta(boolean b) {
		this.posibleAlta=b;
	}

	public void setRutaPDF(String ruta) {
		this.rutaPDF=ruta;
	}

	
}
