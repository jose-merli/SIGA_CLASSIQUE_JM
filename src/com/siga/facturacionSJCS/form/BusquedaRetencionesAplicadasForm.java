package com.siga.facturacionSJCS.form;

import java.util.Vector;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class BusquedaRetencionesAplicadasForm extends MasterForm 
{
	
	String idInstitucion;
	String idPersona;
	String idCobro;
	String idRetencion;
	String fechaDesdePago;
	String fechaHastaPago;
	
	
	public String getFechaDesdePago() {
		return fechaDesdePago;
	}
	public void setFechaDesdePago(String fechaDesdePago) {
		this.fechaDesdePago = fechaDesdePago;
	}
	public String getFechaHastaPago() {
		return fechaHastaPago;
	}
	public void setFechaHastaPago(String fechaHastaPago) {
		this.fechaHastaPago = fechaHastaPago;
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
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdCobro() {
		return idCobro;
	}
	public void setIdCobro(String idCobro) {
		this.idCobro = idCobro;
	}
	public String getIdRetencion() {
		return idRetencion;
	}
	public void setIdRetencion(String idRetencion) {
		this.idRetencion = idRetencion;
	}
	public String getLetrado() {
		return UtilidadesHash.getString(this.datos, "letrado");
	}
	public void setLetrado(String letrado) {
		UtilidadesHash.set(this.datos, "letrado", letrado);
	}
	public String getAbonoRelacionado() {
		return UtilidadesHash.getString(this.datos, "abonoRelacionado");
	}
	public void setAbonoRelacionado(String abonoRelacionado) {
		UtilidadesHash.set(this.datos, "abonoRelacionado", abonoRelacionado);
	}
	
	public String getPagoRelacionado() {
		return UtilidadesHash.getString(this.datos, "pagoRelacionado");
	}
	public void setPagoRelacionado(String pagoRelacionado) {
		UtilidadesHash.set(this.datos, "pagoRelacionado", pagoRelacionado);
	}
	
	public String getIdTipoRetencion() {
		return UtilidadesHash.getString(this.datos, "idTipoRetencion");
	}
	public void setIdTipoRetencion(String idTipoRetencion) {
		UtilidadesHash.set(this.datos, "idTipoRetencion", idTipoRetencion);
	}
	
	public String getIdDestinatario() {
		return UtilidadesHash.getString(this.datos, "idDestinatario");
	}
	public void setIdDestinatario(String idDestinatario) {
		UtilidadesHash.set(this.datos, "idDestinatario", idDestinatario);
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
	public Vector retencionesAplicadas = new Vector();

	public Vector getRetencionesAplicadas() {
		return retencionesAplicadas;
	}
	public void setRetencionesAplicadas(Vector retencionesAplicadas) {
		this.retencionesAplicadas = retencionesAplicadas;
	}

	

	
}