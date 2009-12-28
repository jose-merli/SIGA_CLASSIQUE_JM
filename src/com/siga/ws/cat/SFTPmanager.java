/**
 * 
 */
package com.siga.ws.cat;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.siga.beans.CajgConfiguracionAdm;
import com.siga.beans.CajgConfiguracionBean;
import com.siga.general.SIGAException;

/**
 * @author angelcpe
 *
 */
public class SFTPmanager {

	private CajgConfiguracionBean cajgConfiguracionBean;
	private ChannelSftp chan;
	private Session session;
	
	/**
	 * @throws ClsExceptions 
	 * @throws SIGAException 
	 * 
	 */
	public SFTPmanager(UsrBean usr, String idInstitucion) throws ClsExceptions, SIGAException {
		CajgConfiguracionAdm cajgConfiguracionAdm = new CajgConfiguracionAdm(usr);
		Hashtable hash = new Hashtable();
		hash.put(CajgConfiguracionBean.C_IDINSTITUCION, idInstitucion);
		Vector vector = cajgConfiguracionAdm.selectByPK(hash);
		if (vector == null || vector.size() != 1) {
			throw new SIGAException("messages.cajg.error.revisarConfiguracion");
		}
		cajgConfiguracionBean = (CajgConfiguracionBean) vector.get(0);
	}
	
	public String getFtpDirectorioIN() {
		return cajgConfiguracionBean.getFtpDirectorioIN();
	}
	
	public String getFtpDirectorioOUT() {
		return cajgConfiguracionBean.getFtpDirectorioOUT();
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
				
		if (cajgConfiguracionBean.getFtpIP() == null || cajgConfiguracionBean.getFtpIP().trim().equals("")
				|| cajgConfiguracionBean.getFtpPuerto() == null
				|| cajgConfiguracionBean.getFtpUser() == null
				|| cajgConfiguracionBean.getFtpPass() == null) {
			ClsLogging.writeFileLog("Revise la configuración FTP del colegio", 3);
			throw new SIGAException("messages.cajg.error.revisarConfiguracion");
		}
		
		JSch jsch = new JSch();
		
		ClsLogging.writeFileLog("Conectando con FTP...", 3);
		ClsLogging.writeFileLog("IP: " + cajgConfiguracionBean.getFtpIP(), 3);
		ClsLogging.writeFileLog("Usuario: " + cajgConfiguracionBean.getFtpUser(), 3);		
		ClsLogging.writeFileLog("Puerto: " + cajgConfiguracionBean.getFtpPuerto(), 3);
		ClsLogging.writeFileLog("Pass: " + cajgConfiguracionBean.getFtpPass(), 3);
		
		
		session = jsch.getSession(cajgConfiguracionBean.getFtpUser(), cajgConfiguracionBean.getFtpIP(), cajgConfiguracionBean.getFtpPuerto().intValue());
		session.setPassword(cajgConfiguracionBean.getFtpPass());
		
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

}
