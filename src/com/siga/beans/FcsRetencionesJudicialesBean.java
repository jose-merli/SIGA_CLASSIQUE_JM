//VERSIONES:
//julio.vicente 31-03-2005 creacion
//
package com.siga.beans;


public class FcsRetencionesJudicialesBean extends MasterBean {
	
	/* Variables */
	private Integer  idPersona, idRetencion, idTramoLec, idDestinatario, idInstitucion;
	private String   fechaAlta, fechaInicio, fechaFin, tipoRetencion, observaciones, salarioAnual,contabilizado,descDestinatario,esDeTurno;
	private Double 	 importe;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_RETENCIONES_JUDICIALES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDRETENCION			= "IDRETENCION";
	static public final String C_IDDESTINATARIO			= "IDDESTINATARIO";
	static public final String C_FECHAALTA				= "FECHAALTA";
	static public final String C_FECHAINICIO			= "FECHAINICIO";
	static public final String C_FECHAFIN				= "FECHAFIN";
	static public final String C_TIPORETENCION			= "TIPORETENCION";
	static public final String C_OBSERVACIONES			= "OBSERVACIONES";
	static public final String C_IMPORTE				= "IMPORTE";
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_CONTABILIZADO			= "CONTABILIZADO";
	static public final String C_DESCDESTINATARIO		= "DESCDESTINATARIO";
	static public final String C_ESDETURNO		        = "ESDETURNO";
	
	// Métodos GET
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}	
	public String getFechaAlta() {
		return fechaAlta;
	}
	public String getContabilizado() {
		return contabilizado;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public Integer getIdDestinatario() {
		return idDestinatario;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public Integer getIdRetencion() {
		return idRetencion;
	}
	
	public Double getImporte() {
		return importe;
	}
	public String getObservaciones() {
		return observaciones;
	}
	
	public String getTipoRetencion() {
		return tipoRetencion;
	}
	public String getDescDestinatario() {
		return descDestinatario;
	}
	
	//	 Métodos SET	
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}
	public void setIdDestinatario(Integer idDestinatario) {
		this.idDestinatario = idDestinatario;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public void setIdRetencion(Integer idRetencion) {
		this.idRetencion = idRetencion;
	}
	
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public void setTipoRetencion(String tipoRetencion) {
		this.tipoRetencion = tipoRetencion;
	}
	public void setDescDestinatario(String descDestinatario) {
		this.descDestinatario = descDestinatario;
	}
	/**
	 * @return Returns the esDeTurno.
	 */
	public String getEsDeTurno() {
		return esDeTurno;
	}
	/**
	 * @param esDeTurno The esDeTurno to set.
	 */
	public void setEsDeTurno(String esDeTurno) {
		this.esDeTurno = esDeTurno;
	}
	FcsDestinatariosRetencionesBean destinatarioRetencion = null;
	String descTipoRetencion;
	public FcsDestinatariosRetencionesBean getDestinatarioRetencion() {
		return destinatarioRetencion;
	}
	public void setDestinatarioRetencion(
			FcsDestinatariosRetencionesBean destinatarioRetencion) {
		this.destinatarioRetencion = destinatarioRetencion;
	}
	public String getDescTipoRetencion() {
		return descTipoRetencion;
	}
	public void setDescTipoRetencion(String descTipoRetencion) {
		this.descTipoRetencion = descTipoRetencion;
	}
	
	
}
