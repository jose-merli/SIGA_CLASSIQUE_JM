package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class InformeJustificacionMasivaForm extends MasterForm 
{
	
	public String getFecha() {
		return UtilidadesHash.getString(this.datos, "Fecha");
	}
	public void setFecha(String fecha) {
		UtilidadesHash.set(this.datos, "Fecha", fecha);
	}
	public String getLetrado() {
		return UtilidadesHash.getString(this.datos, "letrado");
	}
	public void setLetrado(String letrado) {
		UtilidadesHash.set(this.datos, "letrado", letrado);
	}
	
	
	public String getFechaDesde() {
		return UtilidadesHash.getString(this.datos, "fechaDesde");
	}
	public void setFechaDesde(String fechaDesde) {
		UtilidadesHash.set(this.datos, "fechaDesde", fechaDesde);
	}
	public String getFechaHasta() {
		return UtilidadesHash.getString(this.datos, "fechaHasta");
	}
	public void setFechaHasta(String fechaHasta) {
		UtilidadesHash.set(this.datos, "fechaHasta", fechaHasta);
	}
	
	
	public String getMostrarTodas() {
		return UtilidadesHash.getString(this.datos, "mostrarTodas");
	}
	public void setMostrarTodas(String mostrarTodas) {
		UtilidadesHash.set(this.datos, "mostrarTodas", mostrarTodas);
	}
	
	public String getAnio() {
		return UtilidadesHash.getString(this.datos, "anio");
	}
	public void setAnio(String mostrarTodas) {
		UtilidadesHash.set(this.datos, "anio", mostrarTodas);
	}
	
	public String getInteresadoApellidos() {
		return UtilidadesHash.getString(this.datos, "interesadoApellidos");
		
	}
	public void setInteresadoApellidos(String interesadoApellidos) {
		UtilidadesHash.set(this.datos, "interesadoApellidos", interesadoApellidos);
		
	}
	
	public String getInteresadoNombre() {
		return UtilidadesHash.getString(this.datos, "interesadoNombre");
		
	}
	public void setInteresadoNombre(String interesadoNombre) {
		UtilidadesHash.set(this.datos, "interesadoNombre", interesadoNombre);
		
	}
	public String getIdioma() {
		return UtilidadesHash.getString(this.datos, "idioma");
	}
	public void setIdioma(String idioma) {
		UtilidadesHash.set(this.datos, "idioma", idioma);
	}
	String numeroNifTagBusquedaPersonas;
	String nombrePersona;
	
	public String getNombrePersona() {
		return UtilidadesHash.getString(this.datos, "nombrePersona");
	}
	public void setNombrePersona(String nombrePersona) {
		UtilidadesHash.set(this.datos, "nombrePersona", nombrePersona);
	}
	public String getNumeroNifTagBusquedaPersonas() {
		return UtilidadesHash.getString(this.datos, "numeroNifTagBusquedaPersonas");
	}
	public void setNumeroNifTagBusquedaPersonas(
			String numeroNifTagBusquedaPersonas) {
		UtilidadesHash.set(this.datos, "numeroNifTagBusquedaPersonas", numeroNifTagBusquedaPersonas);
	}
}