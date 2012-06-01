package com.siga.beans;

import es.satec.siga.util.SigaSequence;

public class EcomColaBean extends MasterBean {
	private SigaSequence idEcomCola = new SigaSequence("SEQ_ECOM_COLA");
	private Integer idInstitucion;
	private Integer idEstadoCola;
	private Integer idOperacion;
	private Integer reintento;
	private String fechaCreacion;
		
		
	static public String T_NOMBRETABLA = "ECOM_COLA";
	
	static public final String C_IDECOMCOLA = "IDECOMCOLA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDESTADOCOLA = "IDESTADOCOLA";
	static public final String C_IDOPERACION = "IDOPERACION";
	static public final String C_REINTENTO = "REINTENTO";
	static public final String C_FECHACREACION = "FECHACREACION";
	
	
	public SigaSequence getIdEcomCola() {
		return idEcomCola;
	}
	public void setIdEcomCola(SigaSequence idEcomCola) {
		this.idEcomCola = idEcomCola;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdEstadoCola() {
		return idEstadoCola;
	}
	public void setIdEstadoCola(Integer idEstadoCola) {
		this.idEstadoCola = idEstadoCola;
	}
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public Integer getReintento() {
		return reintento;
	}
	public void setReintento(Integer reintento) {
		this.reintento = reintento;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}	
	
}
