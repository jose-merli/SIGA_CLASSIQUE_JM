/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 22-03-2005 - Inicio
 */
package com.siga.facturacion.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FicheroBancarioPagosForm extends MasterForm{

	private String fechaCargo = "";
	private String idInstitucion;
	
	//Campos Busqueda
	private String descripcion;
	private String fechaDesde;
	private String fechaHasta;
	private String codigoBanco;
	private String recibosDesde;
	private String recibosHasta;
	private String importesDesde;
	private String importesHasta;	
	private String origen;
	private String idSerieFacturacion;
	
	public String getFechaCargo() {
		return fechaCargo;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getRecibosDesde() {
		return recibosDesde;
	}
	public void setRecibosDesde(String recibosDesde) {
		this.recibosDesde = recibosDesde;
	}
	public String getRecibosHasta() {
		return recibosHasta;
	}
	public void setRecibosHasta(String recibosHasta) {
		this.recibosHasta = recibosHasta;
	}
	public String getImportesDesde() {
		return importesDesde;
	}
	public void setImportesDesde(String importesDesde) {
		this.importesDesde = importesDesde;
	}
	public String getImportesHasta() {
		return importesHasta;
	}
	public void setImportesHasta(String importesHasta) {
		this.importesHasta = importesHasta;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(String idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	
	/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			fechaDesde= "";
			fechaHasta= "";
			codigoBanco= "";
			descripcion= "";
			recibosDesde= "";
			recibosHasta= "";
			importesDesde= "";
			importesHasta= "";
			origen = "";
			idSerieFacturacion = "";
			
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}		
}
