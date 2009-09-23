//VERSIONES:
//julio.vicente 31-03-2005 creacion
//
package com.siga.beans;


public class FcsCobrosRetencionJudicialBean extends MasterBean {
	 
	/* Variables */
	private Integer  idInstitucion, idPersona, idRetencion, idCobro, idPagosJG;
	private String   fechaRetencion;
	private Double   importeRetenido;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_COBROS_RETENCIONJUDICIAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDPERSONA				= "IDPERSONA";	
	static public final String C_IDRETENCION			= "IDRETENCION";
	static public final String C_IDCOBRO				= "IDCOBRO";
	static public final String C_IDPAGOSJG				= "IDPAGOSJG";
	static public final String C_FECHARETENCION			= "FECHARETENCION";
	static public final String C_IMPORTERETENIDO		= "IMPORTERETENIDO";	

	// Métodos GET
	
	public String getFechaRetencion() {
		return fechaRetencion;
	}
	public Integer getIdCobro() {
		return idCobro;
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
	public Integer getIdRetencion() {
		return idRetencion;
	}
	public Double getImporteRetenido() {
		return importeRetenido;
	}
	
	//	 Métodos SET
	
	public void setFechaRetencion(String fechaRetencion) {
		this.fechaRetencion = fechaRetencion;
	}
	public void setIdCobro(Integer idCobro) {
		this.idCobro = idCobro;
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
	public void setIdRetencion(Integer idRetencion) {
		this.idRetencion = idRetencion;
	}
	public void setImporteRetenido(Double importeRetenido) {
		this.importeRetenido = importeRetenido;
	}
	
	FcsRetencionesJudicialesBean retencionJudicial = null;
	private CenPersonaBean persona = null;
	String abonoRelacionado = null;
	String pagoRelacionado = null;

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
	public FcsRetencionesJudicialesBean getRetencionJudicial() {
		return retencionJudicial;
	}
	public void setRetencionJudicial(FcsRetencionesJudicialesBean retencionJudicial) {
		this.retencionJudicial = retencionJudicial;
	}
	
	
}
