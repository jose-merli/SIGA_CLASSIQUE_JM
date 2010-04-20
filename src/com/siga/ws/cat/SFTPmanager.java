/**
 * 
 */
package com.siga.ws.cat;

import java.util.Properties;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;

/**
 * @author angelcpe
 *
 */
public class SFTPmanager {
	
	private ChannelSftp chan;
	private Session session;
	
	private String ftpIP;
	private String ftpPuerto;
	private String ftpUser;
	private String ftpPass;
	
	private String ftpDirectorioIN;
	private String ftpDirectorioOUT;
	
	/**
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 * 
	 */
	public SFTPmanager(UsrBean usr, String idInstitucion) throws ClsExceptions, SIGAException {
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);		
				
		ftpIP = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_IP", null);
		ftpPuerto = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_PUERTO", null);
		ftpUser = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_USER", null);
		ftpPass = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_PASS", null);
		
		ftpDirectorioIN = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_DIRECTORIO_IN", null);
		ftpDirectorioOUT = admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_DIRECTORIO_OUT", null);
	}
	
	

	/**
	 * 
	 * @param usr
	 * @param idInstitucion
	 * @param modo
	 * @return
	 * @throws Exception
	 */
	public ChannelSftp connect() throws Exception{
				
		if (getFtpIP() == null || getFtpIP().trim().equals("")
				|| getFtpPuerto() == null
				|| getFtpUser() == null
				|| getFtpPass() == null) {
			ClsLogging.writeFileLog("Revise la configuración FTP del colegio", 3);
			throw new SIGAException("messages.cajg.error.revisarConfiguracion");
		}
		
		JSch jsch = new JSch();
		
		ClsLogging.writeFileLog("Conectando con FTP...", 3);
		ClsLogging.writeFileLog("IP: " + getFtpIP(), 3);
		ClsLogging.writeFileLog("Usuario: " + getFtpUser(), 3);		
		ClsLogging.writeFileLog("Puerto: " + getFtpPuerto(), 3);
		ClsLogging.writeFileLog("Pass: " + getFtpPass(), 3);
		
		
		session = jsch.getSession(getFtpUser(), getFtpIP(), Integer.parseInt(getFtpPuerto()));
		session.setPassword(getFtpPass());
		
		// El SFTP requiere un intercambio de claves
		// con esta propiedad le decimos que acepte la clave
		// sin pedir confirmación
		Properties prop = new Properties();
		prop.put("StrictHostKeyChecking", "no");
		session.setConfig(prop);
		session.connect();
		

		// Abrimos el canal de sftp y conectamos
		chan = (ChannelSftp) session.openChannel("sftp");
		chan.connect();
		ClsLogging.writeFileLog("Conetado a SFTP", 3);
				
		return chan;
	}

	public void disconnect() {
		ClsLogging.writeFileLog("Desconectando de SFTP", 3);
		if (chan != null && chan.isConnected()) {
			chan.disconnect();
		}
		if (session != null && session.isConnected()){
			session.disconnect();
		}		
		ClsLogging.writeFileLog("Desconectado de SFTP", 3);
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
	public String getFtpPuerto() {
		return ftpPuerto;
	}



	/**
	 * @param ftpPuerto the ftpPuerto to set
	 */
	public void setFtpPuerto(String ftpPuerto) {
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
	 * @return the ftpDirectorioIN
	 */
	public String getFtpDirectorioIN() {
		return ftpDirectorioIN;
	}



	/**
	 * @param ftpDirectorioIN the ftpDirectorioIN to set
	 */
	public void setFtpDirectorioIN(String ftpDirectorioIN) {
		this.ftpDirectorioIN = ftpDirectorioIN;
	}



	/**
	 * @return the ftpDirectorioOUT
	 */
	public String getFtpDirectorioOUT() {
		return ftpDirectorioOUT;
	}



	/**
	 * @param ftpDirectorioOUT the ftpDirectorioOUT to set
	 */
	public void setFtpDirectorioOUT(String ftpDirectorioOUT) {
		this.ftpDirectorioOUT = ftpDirectorioOUT;
	}

}
