//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_PAGO_ASISTENCIA
* 
* @author AtosOrigin
* @since 29/03/2005 
*/

public class FcsPagoAsistenciaBean extends MasterBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9172788455602252063L;
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idPagosJG;
	private Integer		numero;
	private Integer		anio;
	private Float		importePagado;
	private Float		importeIRPF;
	private Integer		porcentajeIRPF;
	private Long		idPersona;
	private Long     idPersonaSociedad;

	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_PAGO_ASISTENCIA";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPAGOSJG =	 			"IDPAGOSJG";
	static public final String 	C_NUMERO = 					"NUMERO";
	static public final String 	C_ANIO = 					"ANIO";
	static public final String 	C_IMPORTEPAGADO = 			"IMPORTEPAGADO";
	static public final String 	C_IMPORTEIRPF = 			"IMPORTEIRPF";
	static public final String 	C_PORCENTAJEIRPF =	 		"PORCENTAJEIRPF";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDPERSONASOCIEDAD = 		"IDPERSONASOCIEDAD";
	
	
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
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
	 * @return Returns the numero.
	 */
	public Integer getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
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
	public Long getIdPersonaSociedad() {
		return idPersonaSociedad;
	}
	/**
	 * @param idPersonaSociedad The idPersonaSociedad to set.
	 */
	public void setIdPersonaSociedad(Long idPersonaSociedad) {
		this.idPersonaSociedad = idPersonaSociedad;
	}
}