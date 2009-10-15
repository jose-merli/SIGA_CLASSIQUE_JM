/*
 * Created on Feb 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de Anotaciones de un expediente
 */
public class ExpAnotacionBean extends MasterBean {
	
	//	Variables
	private Integer idInstitucion;
	private Integer idInstitucion_TipoExpediente;
	private Integer idTipoExpediente;
	private Integer idTipoAnotacion;
	private Integer idAnotacion;
	private String fechaAnotacion;
	private String fechaFinEstado;
	private String fechaInicioEstado;
	private String descripcion;
	private String regEntrada;
	private String regSalida;
	private Integer idFase;
	private Integer idEstado;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idUsuario;
	private Integer idInstitucion_Usuario;
	private String fechaModificacion;
	private String automatico;
	private Integer usuModificacion;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";
	static public final String C_IDTIPOANOTACION ="IDTIPOANOTACION";
	static public final String C_IDANOTACION ="IDANOTACION";
	static public final String C_FECHAANOTACION ="FECHAANOTACION";
	static public final String C_FECHAINICIOESTADO ="FECHAINICIOESTADO";
	static public final String C_FECHAFINESTADO ="FECHAFINESTADO";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_REGENTRADA ="REGENTRADA";
	static public final String C_REGSALIDA ="REGSALIDA";
	static public final String C_IDFASE ="IDFASE";
	static public final String C_IDESTADO ="IDESTADO";
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_IDUSUARIO ="IDUSUARIO";
	static public final String C_IDINSTITUCION_USUARIO ="IDINSTITUCION_USUARIO";
	static public final String C_AUTOMATICO ="AUTOMATICO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
		
	
	static public final String T_NOMBRETABLA = "EXP_ANOTACION";
	
	
	public Integer getIdInstitucion_Usuario() {
		return idInstitucion_Usuario;
	}
	public void setIdInstitucion_Usuario(Integer idInstitucion_Usuario) {
		this.idInstitucion_Usuario = idInstitucion_Usuario;
	}
	public Integer getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(Integer anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechaAnotacion() {
		return fechaAnotacion;
	}
	public void setFechaAnotacion(String valor) {
		this.fechaAnotacion = valor;
	}
	public String getFechaInicioEstado() {
		return fechaInicioEstado;
	}
	public void setFechaInicioEstado(String valor) {
		this.fechaInicioEstado = valor;
	}
	public String getFechaFinEstado() {
		return fechaFinEstado;
	}
	public void setFechaFinEstado(String valor) {
		this.fechaFinEstado = valor;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdAnotacion() {
		return idAnotacion;
	}
	public void setIdAnotacion(Integer idAnotacion) {
		this.idAnotacion = idAnotacion;
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
	public Integer getIdInstitucion_TipoExpediente() {
		return idInstitucion_TipoExpediente;
	}
	public void setIdInstitucion_TipoExpediente(
			Integer idInstitucion_TipoExpediente) {
		this.idInstitucion_TipoExpediente = idInstitucion_TipoExpediente;
	}
	public Integer getIdTipoAnotacion() {
		return idTipoAnotacion;
	}
	public void setIdTipoAnotacion(Integer idTipoAnotacion) {
		this.idTipoAnotacion = idTipoAnotacion;
	}
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(Integer numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public String getRegEntrada() {
		return regEntrada;
	}
	public void setRegEntrada(String regEntrada) {
		this.regEntrada = regEntrada;
	}
	public String getRegSalida() {
		return regSalida;
	}
	public void setRegSalida(String regSalida) {
		this.regSalida = regSalida;
	}
	public String getAutomatico() {
		return automatico;
	}
	public void setAutomatico(String valor) {
		this.automatico = valor;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
