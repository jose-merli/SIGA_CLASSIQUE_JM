/*
 * Created on Jan 27, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de Documentos de un expediente
 */
public class ExpDocumentosBean extends MasterBean {

	//	Variables
	private Integer idInstitucion;
	private Integer idInstitucion_TipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idDocumento;
	private String descripcion;
	private String ruta;
	private String regEntrada;
	private String regSalida;
	private Integer idFase;
	private Integer idEstado;
	private String fechaModificacion;
	private Integer usuModificacion;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_IDDOCUMENTO ="IDDOCUMENTO";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_RUTA ="RUTA";
	static public final String C_REGENTRADA ="REGENTRADA";
	static public final String C_REGSALIDA ="REGSALIDA";
	static public final String C_IDFASE ="IDFASE";
	static public final String C_IDESTADO ="IDESTADO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
		
	
	static public final String T_NOMBRETABLA = "EXP_DOCUMENTO";
	
	
	public Integer getIdInstitucion_TipoExpediente() {
		return idInstitucion_TipoExpediente;
	}
	public void setIdInstitucion_TipoExpediente(
			Integer idInstitucion_TipoExpediente) {
		this.idInstitucion_TipoExpediente = idInstitucion_TipoExpediente;
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
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
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
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
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
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
