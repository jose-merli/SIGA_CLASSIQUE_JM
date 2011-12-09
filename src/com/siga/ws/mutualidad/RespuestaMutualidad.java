package com.siga.ws.mutualidad;

import java.util.Map;

public class RespuestaMutualidad {
	
	private boolean correcto = false;
	private String mensajeError = "";
	private Integer idSolicitud;
	private Double cuota=0.0;
	private Double capital=0.0;
	private Map<String, String> beneficiarios;
	private Map<String, String> asistencia;
	private Map<String, String> tiposIdentificador;
	private Map<String, String> tiposDomicilio;
	private Map<String, String> tiposDireccion;
	private Map<String, String> sexos;
	private Map<String, String> ejerciente;
	private Map<String, String> periodicidades;
	private Map<String, String> estadosCiviles;
	private Map<String, String> coberturas;
	private String valorRespuesta;


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

	public Map<String, String> getBeneficiarios() {
		return beneficiarios;
	}

	public Map<String, String> getAsistencia() {
		return asistencia;
	}

	public Map<String, String> getTiposIdentificador() {
		return tiposIdentificador;
	}

	public Map<String, String> getTiposDomicilio() {
		return tiposDomicilio;
	}

	public Map<String, String> getTiposDireccion() {
		return tiposDireccion;
	}

	public Map<String, String> getSexos() {
		return sexos;
	}

	public Map<String, String> getEjerciente() {
		return ejerciente;
	}

	public Map<String, String> getPeriodicidades() {
		return periodicidades;
	}

	public Map<String, String> getEstadosCiviles() {
		return estadosCiviles;
	}

	public Map<String, String> getCoberturas() {
		return coberturas;
	}

	public void setBeneficiarios(Map<String, String> map) {
		this.beneficiarios = map;
	}

	public void setAsistencia(Map<String, String> map) {
		this.asistencia = map;
	}

	public void setTiposIdentificador(Map<String, String> map) {
		this.tiposIdentificador = map;
	}

	public void setTiposDomicilio(Map<String, String> map) {
		this.tiposDomicilio = map;
	}

	public void setTiposDireccion(Map<String, String> map) {
		this.tiposDireccion = map;
	}

	public void setSexos(Map<String, String> map) {
		this.sexos = map;
	}

	public void setEjerciente(Map<String, String> map) {
		this.ejerciente = map;
	}

	public void setPeriodicidades(Map<String, String> map) {
		this.periodicidades = map;
	}

	public void setEstadosCiviles(Map<String, String> map) {
		this.estadosCiviles = map;
	}

	public void setCoberturas(Map<String, String> map) {
		this.coberturas = map;
	}

	public void setValorRespuesta(String valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}

	
}
