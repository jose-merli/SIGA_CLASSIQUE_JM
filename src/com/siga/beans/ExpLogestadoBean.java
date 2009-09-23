/*
 * Created on Mar 1, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla Exp_Logestado
 */
public class ExpLogestadoBean extends MasterBean {
	
//	Variables
	
	private Integer idInstitucion;
	private Integer idInstitucion_tipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private String fechaInicialEstado;
	private String fechaFinalEstado;
	private String nombreEstado;	
	private Integer idFase;
	private Integer idEstado;
	private String fechaModificacion;
	private Integer usuModificacion;
	

	// Nombre campos de la tabla 
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_FECHAINICIALESTADO ="FECHAINICIALESTADO";
	static public final String C_FECHAFINALESTADO ="FECHAFINALESTADO";	
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDFASE ="IDFASE";
	static public final String C_IDESTADO ="IDESTADO";
	static public final String C_NOMBREESTADO ="NOMBREESTADO";
	
	static public final String T_NOMBRETABLA = "EXP_LOGESTADO";
	
	

	public Integer getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(Integer anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getFechaFinalEstado() {
		return fechaFinalEstado;
	}
	public void setFechaFinalEstado(String fechaFinalEstado) {
		this.fechaFinalEstado = fechaFinalEstado;
	}
	public String getFechaInicialEstado() {
		return fechaInicialEstado;
	}
	public void setFechaInicialEstado(String fechaInicialEstado) {
		this.fechaInicialEstado = fechaInicialEstado;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public Integer getIdFase() {
		return idFase;
	}
	public void setIdFase(Integer idFase) {
		this.idFase = idFase;
	}
public Integer getIdInstitucion() {
	return idInstitucion;
}
public void setIdInstitucion(Integer idInstitucion) {
	this.idInstitucion = idInstitucion;
}
	public Integer getIdInstitucion_tipoExpediente() {
		return idInstitucion_tipoExpediente;
	}
	public void setIdInstitucion_tipoExpediente(
			Integer idInstitucion_tipoExpediente) {
		this.idInstitucion_tipoExpediente = idInstitucion_tipoExpediente;
	}
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public String getNombreEstado() {
		return nombreEstado;
	}
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	public Integer getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(Integer numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
