package com.siga.ws.cat.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;


public class FtpPcajg extends FtpPcajgAbstract {

	FTPClient ftpClient = null; 
		
	@Override
	public void connect() throws Exception {
		ftpClient = new FTPClient();
		ftpClient.connect(getFtpIP(), Integer.parseInt(getFtpPuerto()));
		
		ClsLogging.writeFileLog("Conectando con FTP...", 3);
		ClsLogging.writeFileLog("IP: " + getFtpIP(), 3);
		ClsLogging.writeFileLog("Usuario: " + getFtpUser(), 3);		
		ClsLogging.writeFileLog("Puerto: " + getFtpPuerto(), 3);
		ClsLogging.writeFileLog("Pass: " + getFtpPass(), 3);
		
	    // After connection attempt, you should check the reply code to verify success.
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			ftpClient.disconnect();
			throw new ClsExceptions("FTP server ha rechazado la conexión");
		}
		
		ftpClient.login(getFtpUser(), getFtpPass());
	      
	}

	@Override
	public void disconnect() throws Exception {
		ClsLogging.writeFileLog("Desconectando de FTP...", 3);
		ftpClient.disconnect();		
		ClsLogging.writeFileLog("Desconectado de FTP", 3);
	}

	@Override
	public void download(String name, OutputStream os) throws Exception {
		ftpClient.changeWorkingDirectory(getFtpDirectorioOUT());
		ftpClient.retrieveFile(name, os);
	}

	@Override
	public List<String> ls() throws Exception {
		List<String> listaFicheros = new ArrayList<String>();
		ftpClient.changeWorkingDirectory(getFtpDirectorioOUT());
		for (String fileName : ftpClient.listNames()) {
			listaFicheros.add(fileName);
		}
		return listaFicheros;
	}

	@Override
	public void moveToHIST(String fileName) throws Exception {
		ftpClient.rename(getFtpDirectorioOUT() + "/" + fileName, getFtpDirectorioOUT() + "/" + HIST + "/" + fileName);		
	}

	@Override
	public void upload(String name, InputStream is) throws Exception {
		ftpClient.changeWorkingDirectory(getFtpDirectorioIN());
		ftpClient.storeFile(name, is);		
	}

}
