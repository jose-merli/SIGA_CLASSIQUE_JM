package com.siga.gratuita.form;

import java.util.List;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenClienteAdm;
import com.siga.general.MasterForm;

public class InformeJustificacionMasivaForm extends MasterForm 
{
	private String anio;
	
	private String datosJustificacion;
	private String datosBaja;
	private String estado;
	private String	actuacionesPendientes;
	private String idInstitucion ;
	boolean	aplicarAcreditacionesAnterior2005;
	private List<DesignaForm> designas;
	private String fecha;
	private String fechaJustificacionDesde;
	private String fechaJustificacionHasta;
	private String fechaDesde;
	private String fechaHasta;
	private String idioma;
	private String interesadoApellidos;
	private String interesadoNombre;
	private String idPersona;
	private String mostrarTodas;
	private String numeroNifTagBusquedaPersonas;
	private String nombrePersona;
	
	private String nombreColegiado;
	private String numColegiado;
	private String estadoColegial;
	
	private boolean fichaColegial = false;
	private boolean permitirSinResolucionJustifLetrado;

	
	
	public boolean isPermitirSinResolucionJustifLetrado() {
		return permitirSinResolucionJustifLetrado;
	}
	public void setPermitirSinResolucionJustifLetrado(
			boolean permitirSinResolucionJustifLetrado) {
		this.permitirSinResolucionJustifLetrado = permitirSinResolucionJustifLetrado;
	}
	public boolean getFichaColegial() {
		return fichaColegial;
	}
	public void setFichaColegial(boolean fichaColegial) {
		this.fichaColegial = fichaColegial;
	}
	public String getNumeroNifTagBusquedaPersonas() {
		return numeroNifTagBusquedaPersonas;
	}
	public void setNumeroNifTagBusquedaPersonas(String numeroNifTagBusquedaPersonas) {
		this.numeroNifTagBusquedaPersonas = numeroNifTagBusquedaPersonas;
	}
	public String getNombrePersona() {
		return nombrePersona;
	}
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	public String getDatosJustificacion() {
		return datosJustificacion;
	}
	public void setDatosJustificacion(String datosJustificacion) {
		this.datosJustificacion = datosJustificacion;
	}
	public String getDatosBaja() {
		return datosBaja;
	}
	public void setDatosBaja(String datosBaja) {
		this.datosBaja = datosBaja;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getActuacionesPendientes() {
		return actuacionesPendientes;
	}
	public void setActuacionesPendientes(String actuacionesPendientes) {
		this.actuacionesPendientes = actuacionesPendientes;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public boolean isAplicarAcreditacionesAnterior2005() {
		return aplicarAcreditacionesAnterior2005;
	}
	public void setAplicarAcreditacionesAnterior2005(
			boolean aplicarAcreditacionesAnterior2005) {
		this.aplicarAcreditacionesAnterior2005 = aplicarAcreditacionesAnterior2005;
	}
	public String getFechaJustificacionDesde() {
		return fechaJustificacionDesde;
	}
	public void setFechaJustificacionDesde(String fechaJustificacionDesde) {
		this.fechaJustificacionDesde = fechaJustificacionDesde;
	}
	public String getFechaJustificacionHasta() {
		return fechaJustificacionHasta;
	}
	public void setFechaJustificacionHasta(String fechaJustificacionHasta) {
		this.fechaJustificacionHasta = fechaJustificacionHasta;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getInteresadoApellidos() {
		return interesadoApellidos;
	}
	public void setInteresadoApellidos(String interesadoApellidos) {
		this.interesadoApellidos = interesadoApellidos;
	}
	public String getInteresadoNombre() {
		return interesadoNombre;
	}
	public void setInteresadoNombre(String interesadoNombre) {
		this.interesadoNombre = interesadoNombre;
	}
	public List<DesignaForm> getDesignas() {
		return designas;
	}
	public void setDesignas(List<DesignaForm> designas) {
		this.designas = designas;
	}
	public String getMostrarTodas() {
		return mostrarTodas;
	}
	public void setMostrarTodas(String mostrarTodas) {
		this.mostrarTodas = mostrarTodas;
	}
	public void clear(){
		datosJustificacion= null;
		datosBaja= null;
		estado= null;
		actuacionesPendientes= null;
		idInstitucion = null;
		designas= null;
		fecha= null;
		fechaJustificacionDesde= null;
		fechaJustificacionHasta= null;
		fechaDesde= null;
		fechaHasta= null;
		idioma= null;
		interesadoApellidos= null;
		interesadoNombre= null;
		idPersona = null;
		mostrarTodas= null;
		numeroNifTagBusquedaPersonas = null;
		nombrePersona = null;
		nombreColegiado = null;
		numColegiado = null;
		estadoColegial = null;
		fichaColegial = false;

	}
	public String getNombreColegiado() {
		return nombreColegiado;
	}
	public void setNombreColegiado(String nombreColegiado) {
		this.nombreColegiado = nombreColegiado;
	}
	public String getNumColegiado() {
		return numColegiado;
	}
	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}
	public String getEstadoColegial() {
		return estadoColegial;
	}
	public void setEstadoColegial(String estadoColegial) {
		this.estadoColegial = estadoColegial;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
}