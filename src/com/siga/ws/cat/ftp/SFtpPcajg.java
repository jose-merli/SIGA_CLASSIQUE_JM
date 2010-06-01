/**
 * 
 */
package com.siga.ws.cat.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.atos.utils.ClsLogging;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.siga.general.SIGAException;

/**
 * @author angelcpe
 *
 */
public class SFtpPcajg extends FtpPcajgAbstract {
	
	
	protected SFtpPcajg(){
		super();
	}
	
	private ChannelSftp chan;
	private Session session;

	/**
	 * 
	 * @param usr
	 * @param idInstitucion
	 * @param modo
	 * @return
	 * @throws Exception
	 */
	public void connect() throws Exception{
				
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
	}

	public void disconnect() {
		ClsLogging.writeFileLog("Desconectando de SFTP...", 3);
		if (chan != null && chan.isConnected()) {
			chan.disconnect();
		}
		if (session != null && session.isConnected()){
			session.disconnect();
		}		
		ClsLogging.writeFileLog("Desconectado de SFTP", 3);
	}

	@Override
	public void download(String name, OutputStream os) throws Exception {
		chan.cd(getFtpDirectorioIN());	
		chan.get(getFtpDirectorioOUT() + "/" + name, os);
	}

	@Override
	public List<String> ls() throws Exception {
		List<String> listaFicheros = new ArrayList<String>();
		Vector<LsEntry> lsEntrys = chan.ls(getFtpDirectorioOUT());
		for (LsEntry lsEntry:lsEntrys) {
			listaFicheros.add(lsEntry.getFilename());
		}
		return listaFicheros;
	}

	@Override
	public void upload(String name, InputStream is) throws Exception {
		chan.cd(getFtpDirectorioIN());
		chan.put(is, name);
	}

	@Override
	public void moveToHIST(String fileName) throws Exception {
		chan.rename(getFtpDirectorioOUT() + "/" + fileName, getFtpDirectorioOUT() + "/" + HIST + "/" + fileName);		
	}

}
