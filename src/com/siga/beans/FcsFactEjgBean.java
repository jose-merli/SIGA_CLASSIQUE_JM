//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_SOJ
* 
* @author AtosOrigin
* @since 29/03/2005 
*/

public class FcsFactEjgBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Integer		idTipoEjg;
	private Integer		anio;
	private Long		numero;
	private Long		idPersona;
	private Integer		idTurno;
	private Integer		idGuardia;
	private String		fechaApertura;
	private Double		precioAplicado;
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_EJG";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_NUMERO = 					"NUMERO";
	static public final String 	C_IDTIPOEJG =				"IDTIPOEJG";
	static public final String 	C_ANIO = 					"ANIO";
	static public final String 	C_PRECIOAPLICADO = 			"PRECIOAPLICADO";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_FECHA_APERTURA =			"FECHAAPERTURA";
	static public final String 	C_IDGUARDIA =			 	"IDGUARDIA";
	static public final String 	C_IDTURNO =			 		"IDTURNO";
	
	
	
	
	
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
	 * @return Returns the precioAplicado.
	 */
	public Double getPrecioAplicado() {
		return precioAplicado;
	}
	/**
	 * @param precioAplicado The precioAplicado to set.
	 */
	public void setPrecioAplicado(Double precioAplicado) {
		this.precioAplicado = precioAplicado;
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
	public String getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public Integer getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	public Integer getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
}