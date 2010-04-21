package com.siga.comun.decorator;

import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;


public class ShortDateDecorator implements DisplaytagColumnDecorator
{
    private static FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");

    public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException
    {
        Date date = (Date) columnValue;
        if (null == date)
        	return date;
        return dateFormat.format(date);
    }
}
