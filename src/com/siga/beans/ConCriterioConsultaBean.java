/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 * 
 */
package com.siga.beans;

/**
 * Bean de la tabla ConCriterioConsulta
 */
public class ConCriterioConsultaBean extends MasterBean {

	
	private Integer idInstitucion;
	private String orden;
	private String operador;
	private String separadorInicio;
	private String valor;
	private String valorDesc;
	private String separadorFin;
	private Integer idConsulta;
	private Integer idOperacion;
	private Integer idCampo;
	private String abrirPar;
	private String cerrarPar;
	private String fechaModificacion;
	private Integer usuModificacion;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_ORDEN ="ORDEN";
	static public final String C_OPERADOR ="OPERADOR";
	static public final String C_SEPARADORINICIO ="SEPARADORINICIO";
	static public final String C_VALOR ="VALOR";
	static public final String C_VALORDESC ="VALORDESC";
	static public final String C_SEPARADORFIN ="SEPARADORFIN";
	static public final String C_IDCONSULTA ="IDCONSULTA";
	static public final String C_IDOPERACION ="IDOPERACION";
	static public final String C_IDCAMPO ="IDCAMPO";
	static public final String C_ABRIRPAR ="ABRIRPAR";
	static public final String C_CERRARPAR ="CERRARPAR";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_CRITERIOCONSULTA";
	
	
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdCampo() {
		return idCampo;
	}
	public void setIdCampo(Integer idCampo) {
		this.idCampo = idCampo;
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
	public Integer getIdOperacion() {
		return idOperacion;
	}
	public void setIdOperacion(Integer idOperacion) {
		this.idOperacion = idOperacion;
	}
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getOrden() {
		return orden;
	}
	public String getAbrirPar() {
		return abrirPar;
	}
	public String getCerrarPar() {
		return cerrarPar;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public void setAbrirPar(String v) {
		this.abrirPar = v;
	}
	public void setCerrarPar(String v) {
		this.cerrarPar = v;
	}
	public String getSeparadorFin() {
		return separadorFin;
	}
	public void setSeparadorFin(String separadorFin) {
		this.separadorFin = separadorFin;
	}
	public String getSeparadorInicio() {
		return separadorInicio;
	}
	public void setSeparadorInicio(String separadorInicio) {
		this.separadorInicio = separadorInicio;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValorDesc() {
		return valorDesc;
	}
	public void setValorDesc(String valorDesc) {
		this.valorDesc = valorDesc;
	}
}
