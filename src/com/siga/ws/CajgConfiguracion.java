package com.siga.ws;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.c3009.PCAJGEnviaXmlFTP;
import com.siga.ws.cat.PCAJGGeneraXML;
import com.siga.ws.cat.PCAJGxmlResponse;
import com.siga.ws.i2055.SIGAWSClient;
import com.siga.ws.i2064.PCAJGGeneraXMLSantiago;
import com.siga.ws.i2083.PCAJGAragon;
import com.siga.ws.i2083.PCAJGAragon.SUBTIPOCAJGAragon;

/**
 * @author angel.corral
 *
 */
public class CajgConfiguracion  {
	public static final int TIPO_CAJG_ZONACOMUN = 1;
	public static final int TIPO_CAJG_CATALANES = 2;
	public static final int TIPO_CAJG_PCAJG_GENERAL = 3;
	public static final int TIPO_CAJG_WEBSERVICE_CANTABRIA = 4;
	public static final int TIPO_CAJG_WEBSERVICE_PAMPLONA = 11;
	public static final int TIPO_CAJG_TXT_ALCALA = 5;
	public static final int TIPO_CAJG_XML_SANTIAGO = 6;
	public static final int TIPO_CAJG_WEBSERVICE_PAISVASCO = 7;
	public static final int TIPO_CAJG_WEBSERVICE_GENERALITAT_VALENCIANA = 8;
	public static final int TIPO_CAJG_WEBSERVICE_EJIS_ANDALUCIA = 9;
	public static final int TIPO_CAJG_WEBSERVICE_EJIS_CANARIAS = 10;
	
	
	private static final String PCAJG_ENVIO_WEBSERVICE_TIPO_CAJG3 = "PCAJG_ENVIO_WEBSERVICE_TIPO_CAJG3";

	public static SIGAWSClientAbstract getSIGAWSClientAbstract (UsrBean usrBean, Integer idInstitucion, int respuesta) throws ClsExceptions {
		int tipoCAJG = getTipoCAJG(idInstitucion);
		SIGAWSClientAbstract obj  = null;
		if (tipoCAJG == TIPO_CAJG_CATALANES) {
			if (respuesta == 0) {
				obj = new PCAJGGeneraXML();
			} else if (respuesta == 1) {
				obj = new PCAJGxmlResponse();
			}
		}else if (tipoCAJG == TIPO_CAJG_WEBSERVICE_EJIS_CANARIAS) {
			if (respuesta == 0) {
				obj = new PCAJGEnviaXmlFTP();
			} else if (respuesta == 1) {
				obj = new PCAJGxmlResponse();
			}
		}
		
		else if (tipoCAJG == TIPO_CAJG_PCAJG_GENERAL) {
			GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
			String paramEnvioWS = admParametros.getValor(idInstitucion.toString(), "SCS", PCAJG_ENVIO_WEBSERVICE_TIPO_CAJG3, "");
			
			if ("1".equals(paramEnvioWS)) {
				obj = new PCAJGAragon(SUBTIPOCAJGAragon.ENVIO_WEBSERVICE);	
			} else {
				obj = new PCAJGAragon(SUBTIPOCAJGAragon.DESCARGA_FICHERO);
			}			
			
		} else if (tipoCAJG == TIPO_CAJG_WEBSERVICE_PAMPLONA) {
			obj = new SIGAWSClient();
		} else if (tipoCAJG == TIPO_CAJG_XML_SANTIAGO) {
			obj = new PCAJGGeneraXMLSantiago();
		} 
		
		return obj;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @return
	 * @throws NumberFormatException
	 * @throws ClsExceptions
	 */
	public static int getTipoCAJG(Integer idInstitucion) throws ClsExceptions {
				
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
		String tipoCAJG = admParametros.getValor(idInstitucion.toString(), "SCS", "PCAJG_TIPO", "1");		
		
		return Integer.parseInt(tipoCAJG);
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public static int getPCAJGGeneralTipo(Integer idInstitucion) throws ClsExceptions {
				
		UsrBean usrBean = new UsrBean();
		usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
		String tipo = admParametros.getValor(idInstitucion.toString(), "SCS", PCAJG_ENVIO_WEBSERVICE_TIPO_CAJG3, "0");		
		
		return Integer.parseInt(tipo);
	}
}