
package com.siga.administracion.form;

import com.siga.general.MasterForm;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuditoriaUsuariosForm extends MasterForm 
{
	String nombre, fechaDesde, fechaHasta, tipoAccion, fechaEntrada, fechaEfectiva, motivo, usuarioAutomatico;
	
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipoAccion() {
		return tipoAccion;
	}
	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}
	public String getFechaEfectiva() {
		return fechaEfectiva;
	}
	public void setFechaEfectiva(String fechaEfectiva) {
		this.fechaEfectiva = fechaEfectiva;
	}
	public String getFechaEntrada() {
		return fechaEntrada;
	}
	public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getUsuarioAutomatico() {
		return usuarioAutomatico;
	}
	public void setUsuarioAutomatico(String usuarioAutomatico) {
		this.usuarioAutomatico = usuarioAutomatico;
	}
}
