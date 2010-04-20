package com.siga.ws;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.cat.PCAJGGeneraXML;
import com.siga.ws.cat.PCAJGxmlResponse;

/**
 * @author angel.corral
 *
 */
public class CajgConfiguracion  {

	public static SIGAWSClientAbstract getSIGAWSClientAbstract (Integer idInstitucion, int respuesta) throws ClsExceptions {
		int tipoCAJG = getTipoCAJG(idInstitucion);
		SIGAWSClientAbstract obj  = null;
		if (tipoCAJG == 2) {
			if (respuesta == 0) {
				obj = new PCAJGGeneraXML();
			} else if (respuesta == 1) {
				obj = new PCAJGxmlResponse();
			}
		} else if (tipoCAJG == 3) {
			obj = new PCAJG();
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
}