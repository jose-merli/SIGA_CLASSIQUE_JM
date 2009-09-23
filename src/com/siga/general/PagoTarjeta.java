/*
 * Created on 02-feb-2005
 *	carlos.vidal
 * 
 */
package com.siga.general;

import com.atos.utils.UsrBean;

/**
 * @author carlos.vidal
 * 
 */

/**
 * Clase PagoTarjeta
 * <BR>
 * Representa un carro de la compra con sus elementos
 */

public class PagoTarjeta 
{
	private String operacion;
	private String importe;
	private String tarjeta;
	private String caducidad;    
	private String fecha;
	private String exponente;
	private String moneda;
	private String numeroFactura;
	private String idInstitucion;
	private String idFactura;
	private String idPagoPorCaja;
	private String descripcion;
	private UsrBean usrBean;//necesario para el TPV
	
	/**
	 * PagoTarjeta ()
	 * Constructor
	 */
	public PagoTarjeta () {
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
	 * @return Returns the tarjeta.
	 */
	public String getTarjeta() {
		return tarjeta;
	}
	/**
	 * @param importe The tarjeta to set.
	 */
	public void setTarjeta(String _tarjeta) {
		this.tarjeta = _tarjeta;
	}
	/**
	 * @return Returns the caducidad.
	 */
	public String getCaducidad() {
		return caducidad;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param importe The caducidad to set.
	 */
	public void setCaducidad(String _caducidad) {
		this.caducidad = _caducidad;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @return Returns the operacion.
	 */
	public String getOperacion() {
		return operacion;
	}
	/**
	 * @param  The Fecha to set.
	 */
	public void setFecha(String _fecha) {
		this.fecha = _fecha;
	}
	/**
	 * @return Returns the Exponente.
	 */
	public String getExponente() {
		return exponente;
	}
	/**
	 * @param importe The Exponente to set.
	 */
	public void setExponente(String _exponente) {
		this.exponente = _exponente;;
	}
	/**
	 * @return Returns the moneda.
	 */
	public String getMoneda() {
		return moneda;
	}
	/**
	 * @param return The moneda to set.
	 */
	public void setMoneda(String _moneda) {
		this.moneda = _moneda;
	}
	/**
	 * @return Returns the numeroFactura.
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param set The numeroFactura to set.
	 */
	public void setNumeroFactura(String _numeroFactura) {
		this.numeroFactura = _numeroFactura;
	}
	/**
	 * @param set The operacion to set.
	 */
	public void setOperacion(String _operacion) {
		this.operacion = _operacion;
	}
	/**
	 * @return Returns the usrBean.
	 */
	public UsrBean getUsrBean() {
		return usrBean;
	}
	/**
	 * @param usrBean The usrBean to set.
	 */
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}
	/**
	 * @return Returns the IdInstitucio.
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param importe The IdInstitucio to set.
	 */
	public void setIdInstitucion(String _idInstitucion) {
		this.idInstitucion = _idInstitucion;
	}
	/**
	 * @return Returns the idFactura.
	 */
	public String getIdFactura() {
		return idFactura;
	}
	/**
	 * @param importe The idFactura to set.
	 */
	public void setIdFactura(String _idFactura) {
		this.idFactura = _idFactura;
	}
	/**
	 * @return Returns the idpagoPorCaja.
	 */
	public String getIdPagoPorCaja() {
		return idPagoPorCaja;
	}
	/**
	 * @param The idpagoPorCaja to set.
	 */
	public void setIdPagoPorCaja(String _idPagoPorCaja) {
		this.idPagoPorCaja = _idPagoPorCaja;
	}
	/**
	 * @param iThe descripcion to set.
	 */
	public void setDescripcion(String _descripcion) {
		this.descripcion = _descripcion;
	}

}
