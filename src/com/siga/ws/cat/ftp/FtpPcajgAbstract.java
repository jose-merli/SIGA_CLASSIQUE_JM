package com.siga.ws.cat.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;

public abstract class FtpPcajgAbstract {
	
	protected static String HIST = "HIST";
	private String ftpIP;
	private String ftpPuerto;
	private String ftpUser;
	private String ftpPass;
	
	private String ftpDirectorioIN;
	private String ftpDirectorioOUT;	
		
	public abstract void connect() throws Exception;	
	public abstract void disconnect() throws Exception;
	public abstract void upload(String name, InputStream is) throws Exception;
	public abstract List<String> ls() throws Exception;
	public abstract void download(String name, OutputStream os) throws Exception;
	public abstract void moveToHIST(String fileName) throws Exception;
	
	protected FtpPcajgAbstract(){}


	protected String getFtpIP() {
		return ftpIP;
	}

	protected void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}

	protected String getFtpPuerto() {
		return ftpPuerto;
	}

	protected void setFtpPuerto(String ftpPuerto) {
		this.ftpPuerto = ftpPuerto;
	}

	protected String getFtpUser() {
		return ftpUser;
	}

	protected void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	protected String getFtpPass() {
		return ftpPass;
	}

	protected void setFtpPass(String ftpPass) {
		this.ftpPass = ftpPass;
	}

	protected String getFtpDirectorioIN() {
		return ftpDirectorioIN;
	}

	protected void setFtpDirectorioIN(String ftpDirectorioIN) {
		this.ftpDirectorioIN = ftpDirectorioIN;
	}

	protected String getFtpDirectorioOUT() {
		return ftpDirectorioOUT;
	}

	protected void setFtpDirectorioOUT(String ftpDirectorioOUT) {
		this.ftpDirectorioOUT = ftpDirectorioOUT;
	}
}
