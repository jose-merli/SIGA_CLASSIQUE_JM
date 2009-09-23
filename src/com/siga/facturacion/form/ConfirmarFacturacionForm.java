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
//	private String generarEnvios = "";
	
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
	
/*
	public String getGenerarEnvios() {
		return this.generarEnvios;
	}
	public void setGenerarEnvios(String _generarEnvios) {
		this.generarEnvios = _generarEnvios;
	}
*/
}
