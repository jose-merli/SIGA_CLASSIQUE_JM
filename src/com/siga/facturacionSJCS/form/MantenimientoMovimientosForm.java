//VERSIONES
//ruben.fernandez : 21/03/2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.general.MasterForm;

public class MantenimientoMovimientosForm extends MasterForm {

	private String nif              = 	"NIF";
	private String ncolegiado       = 	"NCOLEGIADO";
	private String nombre           = 	"NOMBRE";
	private String pagoAsociado     = 	"PAGOASOCIADO";
	private String descripcion      = 	"DESCRIPCION";
	private String cantidad         = 	"CANTIDAD";
	private String motivo           = 	"MOTIVO";
	private String idPersona        = 	"IDPERSONA";
	private String idPagoJg         = 	"IDPAGOJG";
	private String idMovimiento     = 	"IDMOVIMIENTO";
	private String buscar           = 	"BUSCAR";
	private String checkHistorico   = 	"CHECKHISTORICO";
	private String checkHistoricoMovimiento;
	private String cantidadRestante = 	"CANTIDADRESTANTE";
	private String cantidadAplicada = 	"CANTIDADAPLICADA";
	private String fechaAlta        =   "FECHAALTA";
	

	/**
	 * @return Returns the cantidad.
	 */
	public String getCantidad() {
		return (String)datos.get(cantidad);
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(String valor) {
		this.datos.put(cantidad, valor);
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return (String)datos.get(descripcion);
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String valor) {
		this.datos.put(descripcion , valor);
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return (String)datos.get(motivo);
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String valor) {
		this.datos.put(motivo , valor);
	}
	/**
	 * @return Returns the ncolegiado.
	 */
	public String getNcolegiado() {
		return (String)datos.get(ncolegiado);
	}
	/**
	 * @param ncolegiado The ncolegiado to set.
	 */
	public void setNcolegiado(String valor) {
		this.datos.put(ncolegiado , valor);
	}
	/**
	 * @return Returns the nif.
	 */
	public String getNif() {
		return (String)datos.get(nif);
	}
	/**
	 * @param nif The nif to set.
	 */
	public void setNif(String valor) {
		this.datos.put(nif , valor);
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return (String)datos.get(nombre);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String valor) {
		this.datos.put(nombre , valor);
	}
	/**
	 * @return Returns the pagoAsociado.
	 */
	public String getPagoAsociado() {
		return (String)datos.get(pagoAsociado);
	}
	/**
	 * @param pagoAsociado The pagoAsociado to set.
	 */
	public void setPagoAsociado(String valor) {
		this.datos.put(pagoAsociado , valor);
	}
	/**
	 * @return Returns the IdPersona.
	 */
	public String getIdPersona() {
		return (String)datos.get(idPersona);
	}
	/**
	 * @param cantidad The IdPersona to set.
	 */
	public void setIdPersona(String valor) {
		this.datos.put(idPersona, valor);
	}
	/**
	 * @return Returns the IdPagoJg.
	 */
	public String getIdPagoJg() {
		return (String)datos.get(idPagoJg);
	}
	/**
	 * @param cantidad The IdPagoJg to set.
	 */
	public void setIdPagoJg(String valor) {
		this.datos.put(idPagoJg, valor);
	}
	/**
	 * @return Returns the IdMovimiento.
	 */
	public String getIdMovimiento() {
		return (String)datos.get(idMovimiento);
	}
	/**
	 * @param cantidad The IdMovimiento to set.
	 */
	public void setIdMovimiento(String valor) {
		this.datos.put(idMovimiento, valor);
	}
	/**
	 * @return Returns the cantidad.
	 */
	public String getBuscar() {
		return (String)datos.get(buscar);
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setBuscar(String valor) {
		this.datos.put(buscar, valor);
	}
	
	
	public String getCheckHistorico() {
		return checkHistorico;
	}
	public void setCheckHistorico(String checkHistorico) {
		this.checkHistorico = checkHistorico;
	}
	public String getCantidadRestante() {
		return cantidadRestante;
	}
	public void setCantidadRestante(String cantidadRestante) {
		this.cantidadRestante = cantidadRestante;
	}
	public String getCantidadAplicada() {
		return cantidadAplicada;
	}
	public void setCantidadAplicada(String cantidadAplicada) {
		this.cantidadAplicada = cantidadAplicada;
	}
	public String getCheckHistoricoMovimiento() {
		return checkHistoricoMovimiento;
	}
	public void setCheckHistoricoMovimiento(String checkHistoricoMovimiento) {
		this.checkHistoricoMovimiento = checkHistoricoMovimiento;
	}	
		
}