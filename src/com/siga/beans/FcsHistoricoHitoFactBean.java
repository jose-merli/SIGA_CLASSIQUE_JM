//VERSIONES:
//julio.vicente 15-04-2005 creación
//
package com.siga.beans;


public class FcsHistoricoHitoFactBean extends MasterBean {
		
	/* Variables */
	private Integer idFacturacion, idGuardia, idTurno, idHito, idInstitucion;
	private String  pagoFacturacion;
	private Double  precioHito;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_HISTORICO_HITOFACT";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDFACTURACION		= "IDFACTURACION";
	static public final String C_IDGUARDIA			= "IDGUARDIA";
	static public final String C_IDTURNO			= "IDTURNO";
	static public final String C_IDHITO				= "IDHITO";
	static public final String C_PAGOFACTURACION	= "PAGOOFACTURACION";
	static public final String C_PRECIOHITO			= "PRECIOHITO";


	// Métodos GET
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	public Integer getIdGuardia() {
		return idGuardia;
	}
	public Integer getIdHito() {
		return idHito;
	}
	public Integer getIdTurno() {
		return idTurno;
	}
	public String getPagoFacturacion() {
		return pagoFacturacion;
	}
	public Double getPrecioHito() {
		return precioHito;
	}

	//	 Métodos SET
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	public void setIdHito(Integer idHito) {
		this.idHito = idHito;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	public void setPagoFacturacion(String pagoFacturacion) {
		this.pagoFacturacion = pagoFacturacion;
	}
	public void setPrecioHito(Double precioHito) {
		this.precioHito = precioHito;
	}
}
