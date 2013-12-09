/*
 * VERSIONES:
 * 
 * miguel.villegas - 22-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de devolucion <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;


public class DevolucionesForm extends MasterForm{
	
	private String idInstitucion="";
	protected FormFile ruta;
	private String comisiones="";
	private String idDisqueteDevoluciones="";
	private String datosPagosRenegociarObservaciones;
	private String datosPagosRenegociarNuevaFormaPago;
	private String datosFacturas;
	
	//Campos Busqueda
	private String tipoDevolucion;
	private String fechaDesde;
	private String fechaHasta;
	private String codigoBanco;
	private String comision;
	private String facturasDesde;
	private String facturasHasta;
	
	public String getDatosFacturas() {
		return datosFacturas;
	}
	public void setDatosFacturas(String datosFacturas) {
		this.datosFacturas = datosFacturas;
	}
	public String getDatosPagosRenegociarObservaciones() {
		return datosPagosRenegociarObservaciones;
	}
	public void setDatosPagosRenegociarObservaciones(
			String datosPagosRenegociarObservaciones) {
		this.datosPagosRenegociarObservaciones = datosPagosRenegociarObservaciones;
	}
	/**
	 * @return Returns the idDisqueteDevoluciones.
	 */
	public String getIdDisqueteDevoluciones() {
		return idDisqueteDevoluciones;
	}
	/**
	 * @param idDisqueteDevoluciones The idDisqueteDevoluciones to set.
	 */
	public void setIdDisqueteDevoluciones(String idDisqueteDevoluciones) {
		this.idDisqueteDevoluciones = idDisqueteDevoluciones;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the ruta.
	 */
	public FormFile getRuta() {
		return ruta;
	}
	/**
	 * @param ruta The ruta to set.
	 */
	public void setRuta(FormFile ruta) {
		this.ruta = ruta;
	}
	
	/**
	 * @return Returns the comision.
	 */
	public String getComisiones() {
		return comisiones;
	}
	/**
	 * @param ruta The comisiones to set.
	 */
	public void setComisiones(String comisiones) {
		this.comisiones = comisiones;
	}
	public String getDatosPagosRenegociarNuevaFormaPago() {
		return datosPagosRenegociarNuevaFormaPago;
	}
	public void setDatosPagosRenegociarNuevaFormaPago(
			String datosPagosRenegociarNuevaFormaPago) {
		this.datosPagosRenegociarNuevaFormaPago = datosPagosRenegociarNuevaFormaPago;
	}
	public String getTipoDevolucion() {
		return tipoDevolucion;
	}
	public void setTipoDevolucion(String tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
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
	public String getComision() {
		return comision;
	}
	public void setComision(String comision) {
		this.comision = comision;
	}
	public String getFacturasDesde() {
		return facturasDesde;
	}
	public void setFacturasDesde(String facturasDesde) {
		this.facturasDesde = facturasDesde;
	}
	public String getFacturasHasta() {
		return facturasHasta;
	}
	public void setFacturasHasta(String facturasHasta) {
		this.facturasHasta = facturasHasta;
	}
	
		/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			tipoDevolucion= "";
			fechaDesde= "";
			fechaHasta= "";
			codigoBanco= "";
			comision= "";
			facturasDesde= "";
			facturasHasta= "";
			
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}