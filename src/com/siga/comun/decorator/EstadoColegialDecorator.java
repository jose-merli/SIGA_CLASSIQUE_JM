package com.siga.comun.decorator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;


public class EstadoColegialDecorator implements DisplaytagColumnDecorator
{
    private static String SIN_ESTADO = "censo.busquedaClientes.literal.sinEstadoColegial";

    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException
    {
		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		if (null == columnValue){
			return UtilidadesString.getMensajeIdioma(usrbean.getLanguage(), SIN_ESTADO);
		}
    	
		return columnValue;
    }
    
}

