package com.siga.ws;

import java.io.File;

import com.atos.utils.UsrBean;
import com.siga.beans.AdmInformeBean;

public abstract class InformeXML {
	public abstract File execute(AdmInformeBean informe, String idInstitucion, String idFacturacion, UsrBean usrBean) throws Exception;
}
