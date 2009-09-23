/*
 * VERSIONES:
 * 
 * miguel.villegas - 11-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de desglose de los abonos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;



public class AbonosLineasForm extends MasterForm{
	

	private String idAbono="";
	private String idInstitucion="";
	private String numeroLinea="";
	private String descripcion="";
	private String cantidad="";
	private String precio="";
	private String iva="";
	private String importeTotal="";
	
	
				
	/**
	 * @return Returns the cantidad.
	 */
	public String getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
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
	 * @return Returns the iva.
	 */
	public String getIva() {
		return iva;
	}
	/**
	 * @param iva The iva to set.
	 */
	public void setIva(String iva) {
		this.iva = iva;
	}
	/**
	 * @return Returns the numeroLinea.
	 */
	public String getNumeroLinea() {
		return numeroLinea;
	}
	/**
	 * @param numeroLinea The numeroLinea to set.
	 */
	public void setNumeroLinea(String numeroLinea) {
		this.numeroLinea = numeroLinea;
	}
	/**
	 * @return Returns the precio.
	 */
	public String getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	/**
	 * @return Returns the importeTotal.
	 */
	public String getImporteTotal() {
		return importeTotal;
	}
	/**
	 * @param precio The importeTotal to set.
	 */
	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}
}