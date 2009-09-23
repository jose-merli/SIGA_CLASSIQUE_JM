/*
 * nuria.rgonzalez - 22-12-2004 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_DATOSCV <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenDatosCVBean extends MasterBean{
	/* Variables */
	private Integer idInstitucion, idCV, idTipoCV,idInstitucion_subt1,idInstitucion_subt2;
	private Long 	idPersona, creditos;
	private String 	fechaInicio, fechaFin, descripcion, certificado, fechaMovimiento, fechabaja,idTipoCVSubtipo1,idTipoCVSubtipo2;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DATOSCV";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDCV				= "IDCV";
	static public final String C_FECHAINICIO		= "FECHAINICIO";
	static public final String C_FECHAFIN			= "FECHAFIN";
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	static public final String C_CERTIFICADO		= "CERTIFICADO";
	static public final String C_CREDITOS			= "CREDITOS";
	static public final String C_IDTIPOCV			= "IDTIPOCV";
	static public final String C_FECHAMOVIMIENTO	= "FECHAMOVIMIENTO";
	static public final String C_FECHABAJA			= "FECHABAJA";
	static public final String C_IDTIPOCVSUBTIPO1   = "IDTIPOCVSUBTIPO1";
	static public final String C_IDTIPOCVSUBTIPO2	= "IDTIPOCVSUBTIPO2";
	static public final String C_IDINSTITUCION_SUBT1= "IDINSTITUCION_SUBT1";
	static public final String C_IDINSTITUCION_SUBT2= "IDINSTITUCION_SUBT2";

	
	//	 Metodos SET
	/**
	 * @param certificado obtiene el certificado.
	 */
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}
	/**
	 * @param creditos obtiene el creditos.
	 */
	public void setCreditos(Long creditos) {
		this.creditos = creditos;
	}
	/**
	 * @param descripcion obtiene el descripcion.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @param fechaFin obtiene el fechaFin.
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	/**
	 * @param fechaInicio obtiene el fechaInicio.
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	/**
	 * @param fechaMovimiento obtiene el fechaMovimiento.
	 */
	public void setFechaMovimiento(String fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	/**
	 * @param idCV obtiene el idCV.
	 */
	public void setIdCV(Integer idCV) {
		this.idCV = idCV;
	}
	/**
	 * @param idInstitucion obtiene el idInstitucion.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @param idPersona obtiene el idPersona.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @param idTipoCV obtiene el idTipoCV.
	 */
	public void setIdTipoCV(Integer idTipoCV) {
		this.idTipoCV = idTipoCV;
	}
	
	//	 Metodos GET

	/**
	 * @return Returns the certificado.
	 */
	public String getCertificado() {
		return certificado;
	}
	/**
	 * @return Devuelve los creditos.
	 */
	public Long getCreditos() {
		return creditos;
	}
	/**
	 * @return Devuelve la descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @return Devuelve la fechaFin.
	 */
	public String getFechaFin() {
		return fechaFin;
	}
	/**
	 * @return Devuelve la fechaInicio.
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}
	/**
	 * @return Devuelve la fechaMovimiento.
	 */
	public String getFechaMovimiento() {
		return fechaMovimiento;
	}
	/**
	 * @return Devuelve el idCV.
	 */
	public Integer getIdCV() {
		return idCV;
	}
	/**
	 * @return Devuelve el idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @return Devuelve el idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @return Devuelve el idTipoCV.
	 */
	public Integer getIdTipoCV() {
		return idTipoCV;
	}
	/**
	 * @return Returns the fechabaja.
	 */
	public String getFechaBaja() {
		return fechabaja;
	}
	/**
	 * @param fechabaja The fechabaja to set.
	 */
	public void setFechaBaja(String fechabaja) {
		this.fechabaja = fechabaja;
	}
	/**
	 * @return Returns the idInstitucion_subt1.
	 */
	public Integer getIdInstitucion_subt1() {
		return idInstitucion_subt1;
	}
	/**
	 * @param idInstitucion_subt1 The idInstitucion_subt1 to set.
	 */
	public void setIdInstitucion_subt1(Integer idInstitucion_subt1) {
		this.idInstitucion_subt1 = idInstitucion_subt1;
	}
	/**
	 * @return Returns the idInstitucion_subt2.
	 */
	public Integer getIdInstitucion_subt2() {
		return idInstitucion_subt2;
	}
	/**
	 * @param idInstitucion_subt2 The idInstitucion_subt2 to set.
	 */
	public void setIdInstitucion_subt2(Integer idInstitucion_subt2) {
		this.idInstitucion_subt2 = idInstitucion_subt2;
	}
	/**
	 * @return Returns the idTipoCVSubtipo1.
	 */
	public String getIdTipoCVSubtipo1() {
		return idTipoCVSubtipo1;
	}
	/**
	 * @param idTipoCVSubtipo1 The idTipoCVSubtipo1 to set.
	 */
	public void setIdTipoCVSubtipo1(String idTipoCVSubtipo1) {
		this.idTipoCVSubtipo1 = idTipoCVSubtipo1;
	}
	/**
	 * @return Returns the idTipoCVSubtipo2.
	 */
	public String getIdTipoCVSubtipo2() {
		return idTipoCVSubtipo2;
	}
	/**
	 * @param idTipoCVSubtipo2 The idTipoCVSubtipo2 to set.
	 */
	public void setIdTipoCVSubtipo2(String idTipoCVSubtipo2) {
		this.idTipoCVSubtipo2 = idTipoCVSubtipo2;
	}
}
