/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvEnvioProgramadoBean extends MasterBean {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6085759869075160328L;
	//Variables
    private Integer idInstitucion;
	private Integer idEnvio;
	private String estado;
	
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private Integer idPlantilla;
	
	private Short comisionAJG;
	
	private String nombre;
	private String acuseRecibo;
	
	private String fechaProgramada;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_ESTADO="ESTADO";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_ACUSERECIBO = "ACUSERECIBO";
	static public final String C_COMISIONAJG = "COMISIONAJG";
	
	static public final String C_NOMBRE = "NOMBRE";	
	static public final String C_FECHAPROGRAMADA = "FECHAPROGRAMADA";	
	
	static public final String SEQ_ENV_ENVIOPROGRAMADO = "SEQ_ENV_ENVIOPROGRAMADO";
	
	static public final String T_NOMBRETABLA = "ENV_ENVIOPROGRAMADO";

	EnvProgramInformesBean programInformes;
	public Integer getIdInstitucion() {
		return idInstitucion;
	}


	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}


	public Integer getIdEnvio() {
		return idEnvio;
	}


	public void setIdEnvio(Integer idEnvio) {
		this.idEnvio = idEnvio;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
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


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getFechaProgramada() {
		return fechaProgramada;
	}


	public void setFechaProgramada(String fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}


	public EnvProgramInformesBean getProgramInformes() {
		return programInformes;
	}


	public void setProgramInformes(EnvProgramInformesBean programInformes) {
		this.programInformes = programInformes;
	}


	public String getAcuseRecibo() {
		return acuseRecibo;
	}


	public void setAcuseRecibo(String acuseRecibo) {
		this.acuseRecibo = acuseRecibo;
	}


	public Short getComisionAJG() {
		return comisionAJG;
	}


	public void setComisionAJG(Short comisionAJG) {
		this.comisionAJG = comisionAJG;
	}	


}
