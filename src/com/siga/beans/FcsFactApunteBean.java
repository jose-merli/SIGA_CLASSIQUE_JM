//VERSIONES:
//ruben.fernandez 29-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_FACT_GUARDIASCOLEGIADO
* 
* @author AtosOrigin
* @since 09/03/2005 
*/

public class FcsFactApunteBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Long		idApunte;
	private Long		idPersona;
	private Integer		idTurno;
	private Integer		idGuardia;
	private String		fechaInicio;
	private Long		idHito;
	private Integer		motivo;
	private Integer		numActuaciones;
	private Integer		numAsistencias;
	private Double		precioAplicado;
	private Integer		numActuacionesTotal;
	private Integer		numAsistenciasTotal;
	
	
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_APUNTE";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_IDAPUNTE =			 	"IDAPUNTE";
	static public final String 	C_IDPERSONA = 				"IDPERSONA";
	static public final String 	C_IDTURNO = 				"IDTURNO";
	static public final String 	C_IDGUARDIA =				"IDGUARDIA";
	static public final String 	C_FECHAINICIO =				"FECHAINICIO";
	static public final String 	C_IDHITO =					"IDHITO";
	static public final String 	C_MOTIVO =					"MOTIVO";
	static public final String 	C_NUMACTUACIONES =			"NUMACTUACIONES";
	static public final String 	C_NUMASISTENCIAS =			"NUMASISTENCIAS";
	static public final String 	C_PRECIOAPLICADO = 			"PRECIOAPLICADO";
	static public final String 	C_NUMACTUACIONESTOTAL =		"NUMACTUACIONESTOTAL";
	static public final String 	C_NUMASISTENCIASTOTAL =		"NUMASISTENCIASTOTAL";
	
	
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Long getIdApunte() {
		return idApunte;
	}
	public void setIdApunte(Long idApunte) {
		this.idApunte = idApunte;
	}
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public Integer getIdGuardia() {
		return idGuardia;
	}
	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	public Long getIdHito() {
		return idHito;
	}
	public void setIdHito(Long idHito) {
		this.idHito = idHito;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdTurno() {
		return idTurno;
	}
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	public Integer getMotivo() {
		return motivo;
	}
	public void setMotivo(Integer motivo) {
		this.motivo = motivo;
	}
	public Integer getNumActuaciones() {
		return numActuaciones;
	}
	public void setNumActuaciones(Integer numActuaciones) {
		this.numActuaciones = numActuaciones;
	}
	public Integer getNumActuacionesTotal() {
		return numActuacionesTotal;
	}
	public void setNumActuacionesTotal(Integer numActuacionesTotal) {
		this.numActuacionesTotal = numActuacionesTotal;
	}
	public Integer getNumAsistencias() {
		return numAsistencias;
	}
	public void setNumAsistencias(Integer numAsistencias) {
		this.numAsistencias = numAsistencias;
	}
	public Integer getNumAsistenciasTotal() {
		return numAsistenciasTotal;
	}
	public void setNumAsistenciasTotal(Integer numAsistenciasTotal) {
		this.numAsistenciasTotal = numAsistenciasTotal;
	}
	public Double getPrecioAplicado() {
		return precioAplicado;
	}
	public void setPrecioAplicado(Double precioAplicado) {
		this.precioAplicado = precioAplicado;
	}
	
	
	
	
}