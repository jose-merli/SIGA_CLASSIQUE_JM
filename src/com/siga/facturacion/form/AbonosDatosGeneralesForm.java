/*
 * VERSIONES:
 * 
 * miguel.villegas - 10-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de datos generales de abonos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;



public class AbonosDatosGeneralesForm extends MasterForm{
	

	private String idAbono="";
	private String idInstitucion="";
	private String fecha="";
	private String contabilizada="";
	private String estado="";
	private String idPersona="";
	private String cliente="";
	private String motivos="";
	private String importeNeto="";
	private String importeIva="";
	private String total="";
	private String numeroAbono="";
	private String observaciones="";
	private String idFactura="";
	
				
	/**
	 * @return Returns the numeroAbono.
	 */
	public String getNumeroAbono() {
		return numeroAbono;
	}
	/**
	 * @param numeroAbono The numeroAbono to set.
	 */
	public void setNumeroAbono(String numeroAbono) {
		this.numeroAbono = numeroAbono;
	}
	/**
	 * @return Returns the contabilizada.
	 */
	public String getContabilizada() {
		return contabilizada;
	}
	/**
	 * @param contabilizada The contabilizada to set.
	 */
	public void setContabilizada(String contabilizada) {
		this.contabilizada = contabilizada;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
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
	 * @return Returns the cliente.
	 */
	public String getCliente() {
		return cliente;
	}
	/**
	 * @param cliente The cliente to set.
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
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
	 * @return Returns the idPersona.
	 */
	public String getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the importeIva.
	 */
	public String getImporteIva() {
		return importeIva;
	}
	/**
	 * @param importeIva The importeIva to set.
	 */
	public void setImporteIva(String importeIva) {
		this.importeIva = importeIva;
	}
	/**
	 * @return Returns the importeNeto.
	 */
	public String getImporteNeto() {
		return importeNeto;
	}
	/**
	 * @param importeNeto The importeNeto to set.
	 */
	public void setImporteNeto(String importeNeto) {
		this.importeNeto = importeNeto;
	}
	/**
	 * @return Returns the motivos.
	 */
	public String getMotivos() {
		return motivos;
	}
	/**
	 * @param motivos The motivos to set.
	 */
	public void setMotivos(String motivos) {
		this.motivos = motivos;
	}
	/**
	 * @return Returns the total.
	 */
	public String getTotal() {
		return total;
	}
	/**
	 * @param total The total to set.
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String valor) {
		this.observaciones = valor;
	}
	public String getIdFactura() {
		return idFactura;
	}
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}	
}