/*
 * VERSIONES:
 * 
 * miguel.villegas - 07-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de abonos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;


public class GenerarAbonosForm extends MasterForm{
	

	private String idAbono="";
	private String idFactura="";
	private String fechaAbonoDesde="";
	private String fechaAbonoHasta="";
	private String fechaFacturaDesde="";
	private String fechaFacturaHasta="";
	private String idPersonaBusqueda="";
	private String busquedaCliente="";
	private String formaPagoBusqueda="";
	private String pagadoBusqueda="";
	private String tipoAbonoBusqueda="";
	private String contabilizadoBusqueda="";
	private String numeroAbono="";
	
	// Metodos set
	
	// Formulario Busqueda Productos	
	
	public void setBusquedaIdAbono(String id){
		this.idAbono=id;
	}
	
	public void setBusquedaIdFactura(String id){
		this.idFactura=id;		
	}
	
	public void setFechaAbonoDesde(String fecha){
		this.fechaAbonoDesde=fecha;
	}
	
	public void setFechaAbonoHasta(String fecha){
		this.fechaAbonoHasta=fecha;		
	}
	
	public void setFechaFacturaDesde(String fecha){
		this.fechaFacturaDesde=fecha;
	}
	
	public void setFechaFacturaHasta(String fecha){
		this.fechaFacturaHasta=fecha;		
	}
	
	public void setIdPersonaBusqueda(String id){
		this.idPersonaBusqueda=id;		
	}
	
	public void setBusquedaCliente(String cliente){
		this.busquedaCliente=cliente;		
	}
	
	public void setFormaPagoBusqueda(String pago){
		this.formaPagoBusqueda=pago;		
	}
	
	public void setPagadoBusqueda(String pagado){
		this.pagadoBusqueda=pagado;		
	}
	
	public void setContabilizadoBusqueda(String cont){
		this.contabilizadoBusqueda=cont;		
	}
	
	public void setTipoAbonoBusqueda(String tipo){
		this.tipoAbonoBusqueda=tipo;		
	}
			
	// Metodos get	
	
	// Formulario Busqueda Productos
	
	public String getBusquedaIdAbono(){
		return this.idAbono;
	}
	
	public String getBusquedaIdFactura(){
		return this.idFactura;		
	}
	
	public String getFechaAbonoDesde(){
		return this.fechaAbonoDesde;
	}
	
	public String getFechaAbonoHasta(){
		return this.fechaAbonoHasta;		
	}
	
	public String getFechaFacturaDesde(){
		return this.fechaFacturaDesde;
	}
	
	public String getFechaFacturaHasta(){
		return this.fechaFacturaHasta;		
	}
	
	public String getIdPersonaBusqueda(){
		return this.idPersonaBusqueda;		
	}
	
	public String getBusquedaCliente(){
		return this.busquedaCliente;		
	}
	
	public String getFormaPagoBusqueda(){
		return this.formaPagoBusqueda;		
	}
	
	public String getPagadoBusqueda(){
		return this.pagadoBusqueda;		
	}
	
	public String getContabilizadoBusqueda(){
		return this.contabilizadoBusqueda;		
	}
	
	public String getTipoAbonoBusqueda(){
		return this.tipoAbonoBusqueda;		
	}
	
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
}