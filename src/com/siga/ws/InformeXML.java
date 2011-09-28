package com.siga.ws;

import java.io.File;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.i2064.je.error.ErrorNegocioWS;
import com.siga.ws.i2064.je.error.ErrorValidacionXML;

public abstract class InformeXML {
	protected static String PCAJG_WS_JE_URL = "PCAJG_WS_JE_URL";
	protected static String MODULO_SCS = "SCS";
	private static String DIRECTORIO_INCIDENCAIS = "informeIncidenciasWS";
	
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
	
	public abstract File execute(String directorio, String nombreSalida, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception;
	public abstract void envioWS(String idInstitucion, String idFacturacion, UsrBean usrBean) throws ErrorValidacionXML, ErrorNegocioWS, Exception;

}
