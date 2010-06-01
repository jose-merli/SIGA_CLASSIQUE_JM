package com.siga.ws.cat.ftp;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;

public class FtpPcajgFactory {

	private enum TIPO_FTP{
		FTP,
		SFTP
	};
	
	public static FtpPcajgAbstract getInstance(UsrBean usr, String idInstitucion) throws Exception {
		FtpPcajgAbstract ftpHelper = null;
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usr);
		String tipoFTP = admParametros.getValor(idInstitucion, "SCS", "PCAJG_TIPO_FTP", null);
		
		if (tipoFTP == null || TIPO_FTP.valueOf(tipoFTP.toUpperCase()) == null) {
			ClsLogging.writeFileLog("Revise la configuración FTP del colegio", 3);
			throw new SIGAException("messages.cajg.error.revisarConfiguracion");
		}
		
		if (TIPO_FTP.FTP.equals(TIPO_FTP.valueOf(tipoFTP.toUpperCase()))) {
			ftpHelper = new FtpPcajg();
		} else if (TIPO_FTP.SFTP.equals(TIPO_FTP.valueOf(tipoFTP.toUpperCase()))) {
			ftpHelper = new SFtpPcajg();
		} else {
			ClsLogging.writeFileLog("Revise la configuración FTP del colegio. El tipo de FTP no es ni FTP ni SFTP", 3);
			throw new SIGAException("messages.cajg.error.revisarConfiguracion");
		}
		recuperaParametrosBDD(ftpHelper, admParametros, idInstitucion);			
		
		return ftpHelper;
	}
	
	private static void recuperaParametrosBDD(FtpPcajgAbstract ftpHelper, GenParametrosAdm admParametros, String idInstitucion) throws Exception {
		try {
			ftpHelper.setFtpIP(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_IP", null));
			
			ftpHelper.setFtpPuerto(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_PUERTO", null));
			ftpHelper.setFtpUser(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_USER", null));
			ftpHelper.setFtpPass(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_PASS", null));
			
			ftpHelper.setFtpDirectorioIN(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_DIRECTORIO_IN", null));
			ftpHelper.setFtpDirectorioOUT(admParametros.getValor(idInstitucion, "SCS", "PCAJG_FTP_DIRECTORIO_OUT", null));
			
			if (ftpHelper.getFtpIP() == null || ftpHelper.getFtpIP().trim().equals("")
					|| ftpHelper.getFtpPuerto() == null
					|| ftpHelper.getFtpUser() == null
					|| ftpHelper.getFtpPass() == null) {
				ClsLogging.writeFileLog("Revise la configuración FTP del colegio", 3);
				throw new SIGAException("messages.cajg.error.revisarConfiguracion");
			}
		} catch (Exception e) {
			throw new Exception("Se ha producido un error al recuperar los parámetros de bdd", e);
		}
	}
}
