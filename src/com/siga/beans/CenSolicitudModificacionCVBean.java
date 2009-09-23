/*
 * nuria.rgonzalez - 11-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_SOLICITUDMODIFICACIONCV <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenSolicitudModificacionCVBean extends MasterBean{
	/* Variables */
	private Integer idInstitucion, idCV, idTipoCV,idEstadoSolic,idInstitucion_subt1,idInstitucion_subt2;
	private Long 	idSolicitud, idPersona;
	private String 	motivo, fechaInicio, fechaFin, descripcion, fechaAlta,idTipoCVSubtipo1,idTipoCVSubtipo2;		
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITUDMODIFICACIONCV";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD		= "IDSOLICITUD";
	static public final String C_MOTIVO				= "MOTIVO";
	static public final String C_FECHAINICIO		= "FECHAINICIO";
	static public final String C_FECHAFIN			= "FECHAFIN";
	static public final String C_DESCRIPCION		= "DESCRIPCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";	
	static public final String C_IDCV				= "IDCV";		
	static public final String C_IDTIPOCV			= "IDTIPOCV";
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_IDTIPOCVSUBTIPO1   = "IDTIPOCVSUBTIPO1";
	static public final String C_IDTIPOCVSUBTIPO2	= "IDTIPOCVSUBTIPO2";
	static public final String C_IDINSTITUCION_SUBT1= "IDINSTITUCION_SUBT1";
	static public final String C_IDINSTITUCION_SUBT2= "IDINSTITUCION_SUBT2";
	
	//	 Metodos SET
	
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
	 * @param idSolicitud obtiene el idSolicitud.
	 */
	public void setIdSolicitud(Long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	/**
	 * @param idTipoCV obtiene el idTipoCV.
	 */
	public void setIdTipoCV(Integer idTipoCV) {
		this.idTipoCV = idTipoCV;
	}	
	/**
	 * @param motivo obtiene el motivo.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	/**
	 *  @return Devuelve la FechaAlta.
	 */
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	/**
	 *  @return Devuelve el idEstado
	 */
	public void setIdEstadoSolic(Integer idEstadoSolic) {
		this.idEstadoSolic = idEstadoSolic;
	}
	
	//	 Metodos GET

	
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
	 * @return Devuelve el idSolicitud.
	 */
	public Long getIdSolicitud() {
		return idSolicitud;
	}
	/**
	 * @return Devuelve el idTipoCV.
	 */
	public Integer getIdTipoCV() {
		return idTipoCV;
	}	
	
	/**
	 * @return Devuelve el motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	public Integer getIdEstadoSolic()	{	
		return idEstadoSolic;
	}
	
	public String getFechaAlta()		{
		return fechaAlta;		
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

