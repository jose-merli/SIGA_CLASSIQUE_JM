package com.siga.comun.decorator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;


public class SiNoDecorator implements DisplaytagColumnDecorator
{
	private static String YES = "general.yes";
	private static String NO = "general.no";

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException
	{
		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		if (UtilidadesString.stringToBoolean((String)columnValue)){
			return UtilidadesString.getMensajeIdioma(usrbean.getLanguage(), YES);
		}
		else {
			return UtilidadesString.getMensajeIdioma(usrbean.getLanguage(), NO);
		}
	}
	
}
