package com.siga.censo.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author RGG
 */
public class SancionesLetradoForm extends MasterForm{

	private String FechaArchivada;
	private String ChkArchivada;
	private String MostrarSanciones;
	private String MostrarTiposFechas;
	private String FechaInicioArchivada;
	private String FechaFinArchivada;
	
	

	

	//	metodos set de los campos del formulario
	public void setNombreInstitucionBuscar(String valor) {
		UtilidadesHash.set(this.datos, "NombreInstitucionBuscar", valor);
	}

	public void setIdInstitucionAlta(String valor) {
		UtilidadesHash.set(this.datos, "IdInstitucionAlta", valor);
	}

	public void setIdInstitucionSancion(String valor) {
		UtilidadesHash.set(this.datos, "IdInstitucionSancion", valor);
	}

	public void setIdPersona(String valor) {
		UtilidadesHash.set(this.datos, "IdPersona", valor);
	}

	public void setIdSancion(String valor) {
		UtilidadesHash.set(this.datos, "IdSancion", valor);
	}

	public void setColegiadoBuscar(String valor) {
		UtilidadesHash.set(this.datos, "ColegiadoBuscar", valor);
	}
	
	public void setTipoSancionBuscar(String valor) {
		UtilidadesHash.set(this.datos, "TipoSancionBuscar", valor);
	}
	
	public void setFechaInicioBuscar(String valor) {	
		UtilidadesHash.set(this.datos, "FechaInicioBuscar", valor);
	}
	
	public void setFechaFinBuscar(String valor) {		
		UtilidadesHash.set(this.datos, "FechaFinBuscar", valor); 
	}
	
	public void setFechaImposicionDesdeBuscar(String valor) {	
		UtilidadesHash.set(this.datos, "FechaImposicionDesdeBuscar", valor);
	}
	
	public void setFechaImposicionHastaBuscar(String valor) {		
		UtilidadesHash.set(this.datos, "FechaImposicionHastaBuscar", valor); 
	}
	
	public void setColegiado(String valor) {		
		UtilidadesHash.set(this.datos, "Colegiado", valor); 
	}
	
	public void setTipoSancion(String valor) {		
		UtilidadesHash.set(this.datos, "TipoSancion", valor); 
	}
	
	public void setFechaInicio(String valor) {		
		UtilidadesHash.set(this.datos, "FechaInicio", valor); 
	}
	
	public void setFechaFin(String valor) {		
		UtilidadesHash.set(this.datos, "FechaFin", valor); 
	}
	
	public void setNombreInstitucion(String valor) {		
		UtilidadesHash.set(this.datos, "NombreInstitucion", valor); 
	}
	
	public void setRefColegio(String valor) {		
		UtilidadesHash.set(this.datos, "RefColegio", valor); 
	}
	
	public void setRefCGAE(String valor) {		
		UtilidadesHash.set(this.datos, "RefCGAE", valor); 
	}
	
	public void setTexto(String valor) {		
		UtilidadesHash.set(this.datos, "Texto", valor); 
	}
	
	public void setObservaciones(String valor) {		
		UtilidadesHash.set(this.datos, "Observaciones", valor); 
	}
	
	public void setFechaResolucion(String valor) {		
		UtilidadesHash.set(this.datos, "FechaResolucion", valor); 
	}

	public void setFechaImposicion(String valor) {		
		UtilidadesHash.set(this.datos, "FechaImposicion", valor); 
	}

	public void setFechaAcuerdo(String valor) {		
		UtilidadesHash.set(this.datos, "FechaAcuerdo", valor); 
	}
	
	public void setRehabilitado(String valor) {		
		UtilidadesHash.set(this.datos, "Rehabilitado", valor); 
	}
	
	public void setFirmeza(String valor) {		
		UtilidadesHash.set(this.datos, "Firmeza", valor); 
	}
	
	public void setChkRehabilitado(String valor) {		
		UtilidadesHash.set(this.datos, "ChkRehabilitado", valor); 
	}
	
	public void setChkFirmeza(String valor) {		
		UtilidadesHash.set(this.datos, "ChkFirmeza", valor); 
	}
	
	public void setFechaArchivada(String fechaArchivada) {
		FechaArchivada = fechaArchivada;
	}	

	public void setChkArchivada(String chkArchivada) {
		ChkArchivada = chkArchivada;
	}
	
	public void setMostrarSanciones(String mostrarSanciones) {
		MostrarSanciones = mostrarSanciones;
	}
	
	public void setMostrarTiposFechas(String mostrarTiposFechas) {
		MostrarTiposFechas = mostrarTiposFechas;
	}

	public void setFechaInicioArchivada(String fechaInicioArchivada) {
		FechaInicioArchivada = fechaInicioArchivada;
	}
	
	public void setFechaFinArchivada(String fechaFinArchivada) {
		FechaFinArchivada = fechaFinArchivada;
	}

	
//	metodos get de los campos del formulario
	public String getNombreInstitucionBuscar() {
		return UtilidadesHash.getString(this.datos, "NombreInstitucionBuscar");
	}
	
	public String getIdInstitucionAlta() {
		return UtilidadesHash.getString(this.datos, "IdInstitucionAlta");
	}
	
	public String getIdInstitucionSancion() {
		return UtilidadesHash.getString(this.datos, "IdInstitucionSancion");
	}
	
	public String getIdPersona() {
		return UtilidadesHash.getString(this.datos, "IdPersona");
	}
	
	public String getIdSancion() {
		return UtilidadesHash.getString(this.datos, "IdSancion");
	}
	
	public String getColegiadoBuscar() {
		return UtilidadesHash.getString(this.datos, "ColegiadoBuscar");
	}
	
	public String getTipoSancionBuscar() {
		return UtilidadesHash.getString(this.datos, "TipoSancionBuscar");
	}
	
	public String getFechaInicioBuscar() {	
		return UtilidadesHash.getString(this.datos, "FechaInicioBuscar");
	}
	
	public String getFechaFinBuscar() {		
		return UtilidadesHash.getString(this.datos, "FechaFinBuscar"); 
	}
	
	public String getFechaImposicionDesdeBuscar() {	
		return UtilidadesHash.getString(this.datos, "FechaImposicionDesdeBuscar");
	}
	
	public String getFechaImposicionHastaBuscar() {		
		return UtilidadesHash.getString(this.datos, "FechaImposicionHastaBuscar"); 
	}
	
	public String getColegiado() {		
		return UtilidadesHash.getString(this.datos, "Colegiado"); 
	}
	
	public String getTipoSancion() {		
		return UtilidadesHash.getString(this.datos, "TipoSancion"); 
	}
	
	public String getFechaInicio() {		
		return UtilidadesHash.getString(this.datos, "FechaInicio"); 
	}
	
	public String getFechaFin() {		
		return UtilidadesHash.getString(this.datos, "FechaFin"); 
	}
	
	public String getNombreInstitucion() {		
		return UtilidadesHash.getString(this.datos, "NombreInstitucion"); 
	}
	
	public String getRefColegio() {		
		return UtilidadesHash.getString(this.datos, "RefColegio"); 
	}
	
	public String getRefCGAE() {		
		return UtilidadesHash.getString(this.datos, "RefCGAE"); 
	}
	
	public String getTexto() {		
		return UtilidadesHash.getString(this.datos, "Texto"); 
	}
	
	public String getObservaciones() {		
		return UtilidadesHash.getString(this.datos, "Observaciones"); 
	}
	
	public String getFechaResolucion() {		
		return UtilidadesHash.getString(this.datos, "FechaResolucion"); 
	}
	
	public String getFechaImposicion() {		
		return UtilidadesHash.getString(this.datos, "FechaImposicion"); 
	}
	
	public String getFechaAcuerdo() {		
		return UtilidadesHash.getString(this.datos, "FechaAcuerdo"); 
	}
	
	public String getRehabilitado() {		
		return UtilidadesHash.getString(this.datos, "Rehabilitado"); 
	}
	
	public String getFirmeza() {		
		return UtilidadesHash.getString(this.datos, "Firmeza"); 
	}
	
	public String getChkRehabilitado() {		
		return UtilidadesHash.getString(this.datos, "ChkRehabilitado"); 
	}
	
	public String getChkFirmeza() {		
		return UtilidadesHash.getString(this.datos, "ChkFirmeza"); 
	}
	
	public String getFechaArchivada() {
		return FechaArchivada;
	}
	
	public String getChkArchivada() {
		return ChkArchivada;
	}
	
	public String getMostrarSanciones() {
		return MostrarSanciones;
	}
	public String getMostrarTiposFechas() {
		return MostrarTiposFechas;
	}

		public String getFechaInicioArchivada() {
		return FechaInicioArchivada;
	}

		
	public String getFechaFinArchivada() {
		return FechaFinArchivada;
	}
}
