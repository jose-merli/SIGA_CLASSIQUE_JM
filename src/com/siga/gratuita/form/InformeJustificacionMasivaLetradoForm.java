package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class InformeJustificacionMasivaLetradoForm extends MasterForm 
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
	
	public String getAnio() {
		return UtilidadesHash.getString(this.datos, "anio");
	}
	public void setAnio(String mostrarTodas) {
		UtilidadesHash.set(this.datos, "anio", mostrarTodas);
	}
	
	public String getMostrarTodas() {
		return UtilidadesHash.getString(this.datos, "mostrarTodas");
	}
	public void setMostrarTodas(String mostrarTodas) {
		UtilidadesHash.set(this.datos, "mostrarTodas", mostrarTodas);
	}
	
}