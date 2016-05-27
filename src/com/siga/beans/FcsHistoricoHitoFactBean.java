//VERSIONES:
//julio.vicente 15-04-2005 creación
//
package com.siga.beans;

public class FcsHistoricoHitoFactBean extends MasterBean {
		
	private static final long serialVersionUID = -3191043297926114549L;
	/* Variables */
	private Integer idFacturacion, idGuardia, idTurno, idHito, idInstitucion;
	private String  pagoFacturacion;
	private Double  precioHito;
	private String	diasAplicables;
	private boolean	agrupar;
	
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
	static public final String C_DIASAPLICABLES		= "DIASAPLICABLES";
	static public final String C_AGRUPAR			= "AGRUPAR";

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
	public String getDiasAplicables() {
		return diasAplicables;
	}
	public boolean getAgrupar() {
		return agrupar;
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
	public void setDiasAplicables(String diasAplicables) {
		this.diasAplicables = diasAplicables;
	}
	public void setAgrupar(boolean agrupar) {
		this.agrupar = agrupar;
	}
}
