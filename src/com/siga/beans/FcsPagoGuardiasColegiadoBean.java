//VERSIONES:
//ruben.fernandez 04-04-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_PAGO_GUARDIASCOLEGIADO
* 
* @author AtosOrigin
* @since 04/04/2005 
*/

public class FcsPagoGuardiasColegiadoBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idPagosJG;
	private Integer		idTurno;
	private Integer		idCalendarioGuardias;
	private Integer		idGuardia;
	private String		fechaInicio;
	private Float		importeIRPF;
	private Float		importePagado;
	private Integer		porcentajeIRPF;
	private Integer		idPersona;
	private Integer     idPersonaSociedad;

	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_PAGO_GUARDIASCOLEGIADO";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPAGOSJG =	 			"IDPAGOSJG";
	static public final String 	C_IDTURNO =					"IDTURNO";
	static public final String 	C_IDCALENDARIOGUARDIAS =	"IDCALENDARIOGUARDIAS";
	static public final String 	C_IMPORTEPAGADO =	 		"IMPORTEPAGADO";
	static public final String 	C_IMPORTEIRPF =		 		"IMPORTEIRPF";
	static public final String 	C_PORCENTAJEIRPF =		 	"PORCENTAJEIRPF";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDGUARDIA = 				"IDGUARDIA";
	static public final String 	C_FECHAINICIO =			 	"FECHAINICIO";
	static public final String 	C_IDPERSONASOCIEDAD = 		"IDPERSONASOCIEDAD";
	
	
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
	 * @return Returns the idPagosJG.
	 */
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param idPagosJG The idPagosJG to set.
	 */
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Integer getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
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
	public Float getImporteIRPF() {
		return importeIRPF;
	}
	/**
	 * @param importeIRPF The importeIRPF to set.
	 */
	public void setImporteIRPF(Float importeIRPF) {
		this.importeIRPF = importeIRPF;
	}
	/**
	 * @return Returns the importePagado.
	 */
	public Float getImportePagado() {
		return importePagado;
	}
	/**
	 * @param importePagado The importePagado to set.
	 */
	public void setImportePagado(Float importePagado) {
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
	 * @return Returns the idPersonaSociedad.
	 */
	public Integer getIdPersonaSociedad() {
		return idPersonaSociedad;
	}
	/**
	 * @param idPersonaSociedad The idPersonaSociedad to set.
	 */
	public void setIdPersonaSociedad(Integer idPersonaSociedad) {
		this.idPersonaSociedad = idPersonaSociedad;
	}
}