/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de consultas
 */
public class ConConsultaBean extends MasterBean {
	
	//	Variables
	private Integer idInstitucion;
	private Integer idConsulta;
	private String descripcion;
	private String general;
	private String tipoConsulta;
	private String sentencia;
	private String bases;
	private String experta;
	private Integer idTabla;
	private Integer idModulo;
	private String fechaModificacion;
	private Integer orden;
	private Integer usuModificacion;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_GENERAL ="GENERAL";
	static public final String C_TIPOCONSULTA ="TIPOCONSULTA";
	static public final String C_SENTENCIA ="SENTENCIA";
	static public final String C_BASES ="BASES";
	static public final String C_IDTABLA ="IDTABLA";
	static public final String C_ORDEN ="ORDEN";
	static public final String C_IDMODULO ="IDMODULO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	static public final String C_ESEXPERTA ="ESEXPERTA";
	
	
	static public final String T_NOMBRETABLA = "CON_CONSULTA";
	
	
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public String getBases() {
		return bases;
	}
	public void setBases(String bases) {
		this.bases = bases;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEsExperta() {
		return experta;
	}
	public void setEsExperta(String experta) {
		this.experta = experta;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getGeneral() {
		return general;
	}
	public void setGeneral(String general) {
		this.general = general;
	}
	public Integer getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getIdTabla() {
		return idTabla;
	}
	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}
	public String getSentencia() {
		return sentencia;
	}
	public void setSentencia(String sentencia) {
		this.sentencia = sentencia;
	}
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
