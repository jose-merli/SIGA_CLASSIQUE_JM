/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 14-03-2005 - Inicio
 */
package com.siga.facturacion.form;

import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfirmarFacturacionForm extends MasterForm{
	private String fechaCargo, estadoConfirmacion, estadoPDF, estadoEnvios, archivadas;
	private String facturacionRapida, idSerieFacturacion, idProgramacion;
	private String fechaEntrega="", fechaUnica="", fechaFRST="", fechaRCUR="", fechaCOR1="", fechaB2B="", fechaTipoUnica="";
	private String fechaDesdeConfirmacion,fechaHastaConfirmacion,fechaDesdeGeneracion,fechaHastaGeneracion;
//	private String generarEnvios = "";
	
	/**
	 * @return the fechaDesdeConfirmacion
	 */
	public String getFechaDesdeConfirmacion() {
		return fechaDesdeConfirmacion;
	}
	/**
	 * @param fechaDesdeConfirmacion the fechaDesdeConfirmacion to set
	 */
	public void setFechaDesdeConfirmacion(String fechaDesdeConfirmacion) {
		this.fechaDesdeConfirmacion = fechaDesdeConfirmacion;
	}
	/**
	 * @return the fechaHastaConfirmacion
	 */
	public String getFechaHastaConfirmacion() {
		return fechaHastaConfirmacion;
	}
	/**
	 * @param fechaHastaConfirmacion the fechaHastaConfirmacion to set
	 */
	public void setFechaHastaConfirmacion(String fechaHastaConfirmacion) {
		this.fechaHastaConfirmacion = fechaHastaConfirmacion;
	}
	/**
	 * @return the fechaDesdeGeneracion
	 */
	public String getFechaDesdeGeneracion() {
		return fechaDesdeGeneracion;
	}
	/**
	 * @param fechaDesdeGeneracion the fechaDesdeGeneracion to set
	 */
	public void setFechaDesdeGeneracion(String fechaDesdeGeneracion) {
		this.fechaDesdeGeneracion = fechaDesdeGeneracion;
	}
	/**
	 * @return the fechaHastaGeneracion
	 */
	public String getFechaHastaGeneracion() {
		return fechaHastaGeneracion;
	}
	/**
	 * @param fechaHastaGeneracion the fechaHastaGeneracion to set
	 */
	public void setFechaHastaGeneracion(String fechaHastaGeneracion) {
		this.fechaHastaGeneracion = fechaHastaGeneracion;
	}
	public String getFechaCargo() {
		return fechaCargo;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	
	public void setEstadoConfirmacion(String dato) {
		this.estadoConfirmacion = dato;
	}
	public void setEstadoPDF(String dato) {
		this.estadoPDF = dato;
	}
	public void setEstadoEnvios(String dato) {
		this.estadoEnvios = dato;
	}
	public void setArchivadas(String dato) {
		this.archivadas = dato;
	}

	public String getEstadoConfirmacion() {
		return estadoConfirmacion;
	}
	public String getEstadoPDF() {
		return estadoPDF;
	}
	public String getEstadoEnvios() {
		return estadoEnvios;
	}
	public String getArchivadas() {
		return archivadas;
	}
	public String getFacturacionRapida() {
		return facturacionRapida;
	}
	public void setFacturacionRapida(String facturacionRapida) {
		this.facturacionRapida = facturacionRapida;
	}
	public String getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(String idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	public String getIdProgramacion() {
		return idProgramacion;
	}
	public void setIdProgramacion(String idProgramacion) {
		this.idProgramacion = idProgramacion;
	}
	public String getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	public String getFechaUnica() {
		return fechaUnica;
	}
	public void setFechaUnica(String fechaUnica) {
		this.fechaUnica = fechaUnica;
	}
	public String getFechaFRST() {
		return fechaFRST;
	}
	public void setFechaFRST(String fechaFRST) {
		this.fechaFRST = fechaFRST;
	}
	public String getFechaRCUR() {
		return fechaRCUR;
	}
	public void setFechaRCUR(String fechaRCUR) {
		this.fechaRCUR = fechaRCUR;
	}
	public String getFechaCOR1() {
		return fechaCOR1;
	}
	public void setFechaCOR1(String fechaCOR1) {
		this.fechaCOR1 = fechaCOR1;
	}
	public String getFechaB2B() {
		return fechaB2B;
	}
	public void setFechaB2B(String fechaB2B) {
		this.fechaB2B = fechaB2B;
	}
	public String getFechaTipoUnica() {
		return fechaTipoUnica;
	}
	public void setFechaTipoUnica(String fechaTipoUnica) {
		this.fechaTipoUnica = fechaTipoUnica;
	}
}