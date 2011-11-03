//VERSIONES:
//julio.vicente 31-03-2005 creacion
//
package com.siga.beans;


public class FcsAplicaMovimientosVariosBean extends MasterBean {
	 
	/* Variables */
	private Integer  idInstitucion; 
	private Integer  idPersona; 
	private Integer  idMovimiento; 
	private Integer  idAplicacion; 
	private Integer  idPagosJG;
	private Double   importeAplicado;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_APLICA_MOVIMIENTOSVARIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDPERSONA				= "IDPERSONA";	
	static public final String C_IDMOVIMIENTO			= "IDMOVIMIENTO";
	static public final String C_IDAPLICACION			= "IDAPLICACION";
	static public final String C_IDPAGOSJG				= "IDPAGOSJG";
	static public final String C_IMPORTEAPLICADO		= "IMPORTEAPLICADO";	

	// Métodos GET
	
	public Integer getIdAplicacion() {
		return idAplicacion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public Integer getIdMovimiento() {
		return idMovimiento;
	}
	public Double getImporteAplicado() {
		return importeAplicado;
	}
	
	//	 Métodos SET
	
	public void setIdAplicacion(Integer idAplicacion) {
		this.idAplicacion = idAplicacion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public void setIdMovimiewnto(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
	}
	
	/*FcsMovimientosVariosBean movimiento = null;
	private CenPersonaBean persona = null;
	String abonoRelacionado = null;
	String pagoRelacionado = null;
	String mes = null;
	String anio = null;
	String fechaDesdePago = null;
	String fechaHastaPago = null;
	

	
	public String getFechaDesdePago() {
		return fechaDesdePago;
	}
	public void setFechaDesdePago(String fechaDesdePago) {
		this.fechaDesdePago = fechaDesdePago;
	}
	public String getFechaHastaPago() {
		return fechaHastaPago;
	}
	public void setFechaHastaPago(String fechaHastaPago) {
		this.fechaHastaPago = fechaHastaPago;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public CenPersonaBean getPersona() {
		return persona;
	}
	public void setPersona(CenPersonaBean persona) {
		this.persona = persona;
	}
	
	public String getAbonoRelacionado() {
		return abonoRelacionado;
	}
	public void setAbonoRelacionado(String abonoRelacionado) {
		this.abonoRelacionado = abonoRelacionado;
	}
	public String getPagoRelacionado() {
		return pagoRelacionado;
	}
	public void setPagoRelacionado(String pagoRelacionado) {
		this.pagoRelacionado = pagoRelacionado;
	}
	public FcsMovimientosVariosBean getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(FcsMovimientosVariosBean movimiento) {
		this.movimiento = movimiento;
	}*/
	
}
