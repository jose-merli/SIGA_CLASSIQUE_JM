package com.siga.eejg;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.UtilidadesString;

public class SchedulerException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8062579983221144923L;
	String msgMultiidioma ;
	public SchedulerException (String msgMultiidioma) {
		this.msgMultiidioma = msgMultiidioma;
		
	}
	protected String getMessage(String idioma) {
		String msg = UtilidadesString.getMensajeIdioma(idioma,msgMultiidioma);
		return msg;		
	}
	public String getMessage() {
		String msg = UtilidadesString.getMensajeIdioma(ClsConstants.LENGUAJE_ESP,msgMultiidioma);
		return msg;		
	}
	

}
