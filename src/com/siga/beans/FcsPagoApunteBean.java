/*
 * Created on 16-mar-2006
 *
 */
package com.siga.beans;

/**
 * @author S230298
 *
 */
public class FcsPagoApunteBean extends MasterBean {

	String 	fechaInicio;
	Double 	importePagado, 
			importeIRPF;
	Long 	idApunte, 
			idPersona, 
			idPersonaSociedad;
	Integer idInstitucion, 
			idPagosJG, 
			idFacturacion, 
			idTurno, 
			idGuardia, 
			idCalendarioGuardias, 
			porcentajeIRPF;
	
	// Nombre de Tabla
	static public String T_NOMBRETABLA = "FCS_PAGO_APUNTE";
	
	// Nombre de campos de la tabla
	static public final String 	C_FECHAINICIO			= "FECHAINICIO";			
	static public final String 	C_IDAPUNTE				= "IDAPUNTE";			
	static public final String 	C_IDCALENDARIOGUARDIAS	= "IDCALENDARIOGUARDIAS";			
	static public final String 	C_IDFACTURACION			= "IDFACTURACION";			
	static public final String 	C_IDGUARDIA				= "IDGUARDIA";			
	static public final String 	C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String 	C_IDPAGOSJG				= "IDPAGOSJG";			
	static public final String 	C_IDPERSONA				= "IDPERSONA";			
	static public final String 	C_IDPERSONASOCIEDAD		= "IDPERSONASOCIEDAD";			
	static public final String 	C_IDTURNO				= "IDTURNO";			
	static public final String 	C_IMPORTEIRPF			= "IMPORTEIRPF";			
	static public final String 	C_IMPORTEPAGADO			= "IMPORTEPAGADO";			
	static public final String 	C_PORCENTAJEIRPF		= "PORCENTAJEIRPF";
	
	
	/**
	 * @return Returns the fechaInicio.
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @param fechaInicio The fechaInicio to set.
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @return Returns the idApunte.
	 */
	public Long getIdApunte() {
		return idApunte;
	}
	/**
	 * @param idApunte The idApunte to set.
	 */
	public void setIdApunte(Long idApunte) {
		this.idApunte = idApunte;
	}
	/**
	 * @return Returns the idCalendarioGuardias.
	 */
	public Integer getIdCalendarioGuardias() {
		return idCalendarioGuardias;
	}
	/**
	 * @param idCalendarioGuardias The idCalendarioGuardias to set.
	 */
	public void setIdCalendarioGuardias(Integer idCalendarioGuardias) {
		this.idCalendarioGuardias = idCalendarioGuardias;
	}
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	/**
	 * @return Returns the idGuardia.
	 */
	public Integer getIdGuardia() {
		return idGuardia;
	}
	/**
	 * @param idGuardia The idGuardia to set.
	 */
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idPersonaSociedad.
	 */
	public Long getIdPersonaSociedad() {
		return idPersonaSociedad;
	}
	/**
	 * @param idPersonaSociedad The idPersonaSociedad to set.
	 */
	public void setIdPersonaSociedad(Long idPersonaSociedad) {
		this.idPersonaSociedad = idPersonaSociedad;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	/**
	 * @return Returns the importeIRPF.
	 */
	public Double getImporteIRPF() {
		return importeIRPF;
	}
	/**
	 * @param importeIRPF The importeIRPF to set.
	 */
	public void setImporteIRPF(Double importeIRPF) {
		this.importeIRPF = importeIRPF;
	}
	/**
	 * @return Returns the importePagado.
	 */
	public Double getImportePagado() {
		return importePagado;
	}
	/**
	 * @param importePagado The importePagado to set.
	 */
	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
	}
	/**
	 * @return Returns the porcentajeIRPF.
	 */
	public Integer getPorcentajeIRPF() {
		return porcentajeIRPF;
	}
	/**
	 * @param porcentajeIRPF The porcentajeIRPF to set.
	 */
	public void setPorcentajeIRPF(Integer porcentajeIRPF) {
		this.porcentajeIRPF = porcentajeIRPF;
	}
	/**
	 * @return Returns the udPagosJG.
	 */
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param udPagosJG The udPagosJG to set.
	 */
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
}
