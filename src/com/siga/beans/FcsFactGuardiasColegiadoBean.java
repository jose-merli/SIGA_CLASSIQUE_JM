//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_GUARDIASCOLEGIADO
* 
* @author AtosOrigin
* @since 09/03/2005 
*/

public class FcsFactGuardiasColegiadoBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Long		idApunte;
	private Integer		idTurno;
	private Integer		idCalendarioGuardias;
	private Integer		idGuardia;
	private String		fechaInicio;
	private Long		idPersona;
	private Double		precioAplicado;
	
	
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_GUARDIASCOLEGIADO";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_IDTURNO =					"IDTURNO";
	static public final String 	C_IDCALENDARIOGUARDIAS =	"IDCALENDARIOGUARDIAS";
	static public final String 	C_PRECIOAPLICADO = 			"PRECIOAPLICADO";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDGUARDIA = 				"IDGUARDIA";
	static public final String 	C_IDAPUNTE =			 		"IDAPUNTE";
	static public final String 	C_FECHAINICIO =			 	"FECHAINICIO";
	
	
	
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
	
	
	public Long getIdApunte() {
		return idApunte;
	}
	public void setIdApunte(Long idApunte) {
		this.idApunte = idApunte;
	}
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
}