package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_CONFIGURACION
 * 
 * @author angel.corral
 * @since 15/10/2009
 */

public class CajgConfiguracionBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idInstitucion;
	private Integer tipoCAJG;
	private String	ftpIP;
	private Integer ftpPuerto;
	private String ftpUser;
	private String ftpPass;
	private String ftpDirectorio;
	private String wsURL;
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CAJG_CONFIGURACION";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_TIPO_PCAJG = "TIPO_PCAJG";
	static public final String C_FTP_IP = "FTP_IP";
	static public final String C_FTP_PUERTO = "FTP_PUERTO";
	static public final String C_FTP_USER = "FTP_USER";
	static public final String C_FTP_PASS = "FTP_PASS";
	static public final String C_FTP_DIRECTORIO = "FTP_DIRECTORIO";
	static public final String C_WS_URL = "WS_URL";
	
	/**
	 * @return the idInstitucion
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return the tipoCAJG
	 */
	public Integer getTipoCAJG() {
		return tipoCAJG;
	}
	/**
	 * @param tipoCAJG the tipoCAJG to set
	 */
	public void setTipoCAJG(Integer tipoCAJG) {
		this.tipoCAJG = tipoCAJG;
	}
	/**
	 * @return the ftpIP
	 */
	public String getFtpIP() {
		return ftpIP;
	}
	/**
	 * @param ftpIP the ftpIP to set
	 */
	public void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}
	/**
	 * @return the ftpPuerto
	 */
	public Integer getFtpPuerto() {
		return ftpPuerto;
	}
	/**
	 * @param ftpPuerto the ftpPuerto to set
	 */
	public void setFtpPuerto(Integer ftpPuerto) {
		this.ftpPuerto = ftpPuerto;
	}
	/**
	 * @return the ftpUser
	 */
	public String getFtpUser() {
		return ftpUser;
	}
	/**
	 * @param ftpUser the ftpUser to set
	 */
	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	/**
	 * @return the ftpPass
	 */
	public String getFtpPass() {
		return ftpPass;
	}
	/**
	 * @param ftpPass the ftpPass to set
	 */
	public void setFtpPass(String ftpPass) {
		this.ftpPass = ftpPass;
	}
	/**
	 * @return the ftpDirectorio
	 */
	public String getFtpDirectorio() {
		return ftpDirectorio;
	}
	/**
	 * @param ftpDirectorio the ftpDirectorio to set
	 */
	public void setFtpDirectorio(String ftpDirectorio) {
		this.ftpDirectorio = ftpDirectorio;
	}
	/**
	 * @return the wsURL
	 */
	public String getWsURL() {
		return wsURL;
	}
	/**
	 * @param wsURL the wsURL to set
	 */
	public void setWsURL(String wsURL) {
		this.wsURL = wsURL;
	}
	
	
	
}