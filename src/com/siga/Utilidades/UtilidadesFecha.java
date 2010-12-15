package com.siga.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;

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
	
	public static String getFechaApruebaDeFormato(String fecha) throws ClsExceptions
	{
		String fechaFormateada = new String(fecha);
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		SimpleDateFormat sdf2 = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);

		if (fecha != null && !fecha.equals("")) {
			try {
				sdf.parse(fecha);
			} catch (ParseException e) {
				try {
					Date date = sdf2.parse(fecha);
					fechaFormateada = sdf.format(date);
				} catch (ParseException e2) {
					throw new ClsExceptions(e, "No se ha podido formatear la fecha " + fecha);
				}
			}
		}

		return fechaFormateada;
	}
	
}
