//VERSIONES:
//ruben.fernandez 04-04-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_SOJ
* 
* @author AtosOrigin
* @since 04/04/2005 
*/

public class FcsPagoEjgBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Double		importePagado, importeIRPF;
	private Long		numero, idPersona, idPersonaSociedad;
	private Integer		anio, idInstitucion, idPagosJG, idTipoEjg, porcentajeIRPF, idFacturacion;
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_PAGO_EJG";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION	= 			"IDFACTURACION";			
	static public final String 	C_IDPAGOSJG =	 			"IDPAGOSJG";
	static public final String 	C_NUMERO = 					"NUMERO";
	static public final String 	C_IDTIPOEJG =				"IDTIPOEJG";
	static public final String 	C_ANIO = 					"ANIO";
	static public final String 	C_IMPORTEIRPF =		 		"IMPORTEIRPF";
	static public final String 	C_IMPORTEPAGADO =	 		"IMPORTEPAGADO";
	static public final String 	C_PORCENTAJEIRPF =		 	"PORCENTAJEIRPF";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDPERSONASOCIEDAD =		"IDPERSONASOCIEDAD";
	
	

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
	 * @return Returns the idTipoEjg.
	 */
	public Integer getIdTipoEjg() {
		return idTipoEjg;
	}
	/**
	 * @param idTipoEjg The idTipoEjg to set.
	 */
	public void setIdTipoEjg(Integer idTipoEjg) {
		this.idTipoEjg = idTipoEjg;
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
	 * @return Returns the numero.
	 */
	public Long getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Long numero) {
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
}