package com.siga.ws;

import com.atos.utils.ClsExceptions;
import com.siga.ws.i2064.je.SantiagoJE;

public class JustificacionEconomicaWS {

	public static InformeXML getInstance(String idInstitucion) throws NumberFormatException, ClsExceptions {
		InformeXML informeXML = null;
		if (CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO == CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion))) {
			informeXML = new SantiagoJE();
		}
		return informeXML;
	}

}
