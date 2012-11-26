package com.siga.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.i2064.je.error.ErrorNegocioWS;
import com.siga.ws.i2064.je.error.ErrorValidacionXML;

public abstract class InformeXML {
	protected static String PCAJG_WS_JE_URL = "PCAJG_WS_JE_URL";
	protected static String MODULO_SCS = "SCS";
	private static String DIRECTORIO_INCIDENCAIS = "informeIncidenciasWS";
	private String cabeceraLog = null;
	
	private BufferedWriter bw = null;
	
	protected String getUrlWsJE(String idInstitucion, UsrBean usrBean) throws ClsExceptions {
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
		return admParametros.getValor(idInstitucion.toString(), MODULO_SCS, PCAJG_WS_JE_URL, "");
	}
	
	public static File getFileInformeIncidencias(String idInstitucion, String idFacturacion) {
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		File file = new File(rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
			+ ClsConstants.FILE_SEP + DIRECTORIO_INCIDENCAIS
			+ ClsConstants.FILE_SEP + idInstitucion
			+ ClsConstants.FILE_SEP + idFacturacion);
		
		file.mkdirs();
		file = new File (file, "InformeIncididencias.csv");
		
		return file;
	}
	
	protected String getDirectorioSalida(String directorio, String idInstitucion) {
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
			+ ClsConstants.FILE_SEP
			+ directorio + ClsConstants.FILE_SEP
			+ (idInstitucion.equals("0") ? "2000" : idInstitucion) + ClsConstants.FILE_SEP;
		return rutaAlm;
	}
	
	/**
	 * 
	 * @param informe 
	 * @param bufferedWriter
	 * @param texto
	 * @param msg 
	 * @param usrBean 
	 * @throws IOException
	 * @throws ClsExceptions 
	 */
	protected void escribeLog(String idInstitucion, String idFacturacion, UsrBean usrBean, String texto) throws IOException, ClsExceptions {
		if (bw == null) {
			FileWriter fileWriter = new FileWriter(getFileInformeIncidencias(idInstitucion, idFacturacion));
			bw = new BufferedWriter(fileWriter);
			if (cabeceraLog != null) {
				bw.write(cabeceraLog);
				bw.newLine();
			}
		}				
		
		bw.write(texto);
		bw.newLine();
		bw.flush();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	protected boolean closeLogFile() {
		boolean abierto = false;
		if (bw != null) {
			abierto = true;
			try {
				bw.flush();			
				bw.close();
				bw = null;
			} catch (IOException e) {
				ClsLogging.writeFileLogError("Error al cerrar el fichero de LOG", e, 3);
			}
		}
		return abierto;
	}
	
	protected String getNombreFichero(String nombreSalida, String idInstitucion, UsrBean usrBean) throws ClsExceptions {
		return nombreSalida + "_" + idInstitucion + "_" + usrBean.getUserName() + "_"
			+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
	}
	
	public abstract File execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception;
	public abstract void envioWS(String idInstitucion, String idFacturacion);

	protected String getCabeceraLog() {
		return cabeceraLog;
	}

	protected void setCabeceraLog(String cabeceraLog) {
		this.cabeceraLog = cabeceraLog;
	}

}
