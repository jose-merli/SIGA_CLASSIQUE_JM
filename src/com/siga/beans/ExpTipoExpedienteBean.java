/*
 * Created on Dec 22, 2004
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpTipoExpedienteBean extends MasterBean {
	
	//Variables
	private String nombre;
	private Integer idInstitucion;
	private String esGeneral;
	private Integer idTipoExpediente;
	private Integer tiempoCaducidad;
	private Integer diasAntelacionCad;
	private Integer relacionEjg;
	private Integer enviarAvisos;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private Integer idPlantilla;
	
	
	
	// Nombre campos de la tabla 
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_ESGENERAL = "ESGENERAL";
	static public final String C_TIEMPOCADUCIDAD = "TIEMPOCADUCIDAD";
	static public final String C_DIASANTELACIONCAD = "DIASANTELACIONCAD";
	static public final String C_IDTIPOEXPEDIENTE = "IDTIPOEXPEDIENTE";
	static public final String C_RELACIONEJG = "RELACIONEJG";
	static public final String C_ENVIARAVISOS = "ENVIARAVISOS";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	
	
	static public final String T_NOMBRETABLA = "EXP_TIPOEXPEDIENTE";
	
	
	

	/**
	 * @return Returns the esGeneral.
	 */
	public String getEsGeneral() {
		return esGeneral;
	}
	/**
	 * @param esGeneral The esGeneral to set.
	 */
	public void setEsGeneral(String esGeneral) {
		this.esGeneral = esGeneral;
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
	 * @return Returns the idTipoExpediente.
	 */
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	/**
	 * @param idTipoExpediente The idTipoExpediente to set.
	 */
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	public Integer getTiempoCaducidad() {
		return tiempoCaducidad;
	}
	public void setTiempoCaducidad(Integer valor) {
		this.tiempoCaducidad = valor;
	}	

	public Integer getDiasAntelacionCad() {
		return diasAntelacionCad;
	}
	public void setDiasAntelacionCad(Integer valor) {
		this.diasAntelacionCad = valor;
	}
	public Integer getRelacionEjg() {
		return relacionEjg;
	}
	public void setRelacionEjg(Integer relacionEjg) {
		this.relacionEjg = relacionEjg;
	}
	public Integer getEnviarAvisos() {
		return enviarAvisos;
	}
	public void setEnviarAvisos(Integer enviarAvisos) {
		this.enviarAvisos = enviarAvisos;
	}
	public Integer getIdTipoEnvios() {
		return idTipoEnvios;
	}
	public void setIdTipoEnvios(Integer idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}
	public Integer getIdPlantillaEnvios() {
		return idPlantillaEnvios;
	}
	public void setIdPlantillaEnvios(Integer idPlantillaEnvios) {
		this.idPlantillaEnvios = idPlantillaEnvios;
	}
	public Integer getIdPlantilla() {
		return idPlantilla;
	}
	public void setIdPlantilla(Integer idPlantilla) {
		this.idPlantilla = idPlantilla;
	}	
	

}
