package com.siga.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilidadesFecha {

	public static String FORMATO_FECHA_ES = "dd/MM/yyyy";

	public static Date getDate(String sDate, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (null != sDate)
			return sdf.parse(sDate);
		else
			return null;
	}

	public static Date getDate(String sDate, String format, String defaultValue)  {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (null != sDate)
			try {
				return sdf.parse(sDate);
			} catch (ParseException e) {
				return null;
			}
		else
			return null;
	}
	
}
