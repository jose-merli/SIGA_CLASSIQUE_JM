//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_ACTUACIONASISTENCIA
* 
* @author AtosOrigin
* @since 29/03/2005 
*/

public class FcsFactActuacionAsistenciaBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Long		idApunte;
	private Long		idActuacion;
	private Integer		anio;
	private Long		numero;
	private Long		idPersona;
	private Double		precioAplicado;
	private String		fechaActuacion;
	private String		fechaJustificacion;
	
	
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_ACTUACIONASISTENCIA";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_IDACTUACION = 			"IDACTUACION";
	static public final String 	C_NUMERO = 					"NUMERO";
	static public final String 	C_ANIO = 					"ANIO";
	static public final String 	C_PRECIOAPLICADO = 			"PRECIOAPLICADO";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDAPUNTE =			 	"IDAPUNTE";
	static public final String 	C_FECHA_ACTUACION =			"FECHAACTUACION";
	static public final String 	C_FECHA_JUSTIFICACION =		"FECHAJUSTIFICACION";
	
	
	
	
	
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
	 * @return Returns the idActuacion.
	 */
	public Long getIdActuacion() {
		return idActuacion;
	}
	/**
	 * @param idActuacion The idActuacion to set.
	 */
	public void setIdActuacion(Long idActuacion) {
		this.idActuacion = idActuacion;
	}
	public String getFechaActuacion() {
		return fechaActuacion;
	}
	public void setFechaActuacion(String fechaActuacion) {
		this.fechaActuacion = fechaActuacion;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public Long getIdApunte() {
		return idApunte;
	}
	public void setIdApunte(Long idApunte) {
		this.idApunte = idApunte;
	}
	
}