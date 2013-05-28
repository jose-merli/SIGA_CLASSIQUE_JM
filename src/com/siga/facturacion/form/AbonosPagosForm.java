/*
 * VERSIONES:
 * 
 * miguel.villegas - 16-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de pago de los abonos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;



public class AbonosPagosForm extends MasterForm{
	

	private String idAbono="";
	private String idPersona="";
	private String idInstitucion="";
	private String pagoPendiente="";		
	private String importe="";	
	private String numeroCuenta="";
	private String idFactura="";	
	
	private String idFacturaCompensadora="";
	private String numFacturaCompensadora="";
	private String estadoFacturaCompensadora="";
	private String fechaFacturaCompensadora="";
	private String importeFacturaCompensadora="";
	

	/**
	 * @return Returns the idAbono.
	 */
	public String getIdAbono() {
		return idAbono;
	}
	/**
	 * @param idAbono The idAbono to set.
	 */
	public void setIdAbono(String idAbono) {
		this.idAbono = idAbono;
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
	 * @return Returns the pagoPendiente.
	 */
	public String getPagoPendiente() {
		return pagoPendiente;
	}
	/**
	 * @param pagoPendiente The pagoPendiente to set.
	 */
	public void setPagoPendiente(String pagoPendiente) {
		this.pagoPendiente = pagoPendiente;
	}
	
	/**
	 * @return Returns the importe.
	 */
	public String getImporte() {
		return importe;
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(String importe) {
		this.importe = importe;
	}
	/**
	 * @return Returns the numeroCuenta.
	 */
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	/**
	 * @param numeroCuenta The numeroCuenta to set.
	 */
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getNumFacturaCompensadora() {
		return numFacturaCompensadora;
	}
	public void setNumFacturaCompensadora(String numFacturaCompensadora) {
		this.numFacturaCompensadora = numFacturaCompensadora;
	}
	public String getFechaFacturaCompensadora() {
		return fechaFacturaCompensadora;
	}
	public void setFechaFacturaCompensadora(String fechaFacturaCompensadora) {
		this.fechaFacturaCompensadora = fechaFacturaCompensadora;
	}
	public String getImporteFacturaCompensadora() {
		return importeFacturaCompensadora;
	}
	public void setImporteFacturaCompensadora(String importeFacturaCompensadora) {
		this.importeFacturaCompensadora = importeFacturaCompensadora;
	}
	public String getEstadoFacturaCompensadora() {
		return estadoFacturaCompensadora;
	}
	public void setEstadoFacturaCompensadora(String estadoFacturaCompensadora) {
		this.estadoFacturaCompensadora = estadoFacturaCompensadora;
	}
	public String getIdFacturaCompensadora() {
		return idFacturaCompensadora;
	}
	public void setIdFacturaCompensadora(String idFacturaCompensadora) {
		this.idFacturaCompensadora = idFacturaCompensadora;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdFactura() {
		return idFactura;
	}
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}	
}