package com.siga.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	/** Suma dias a la fecha pasada como argumento */
	public static String sumarDias(String fecha, int dias){
		String salida = "";

		try {
			//Recupero del String fechaInicial con formato dd/mm/yyyy la fecha como Date
			String jsdf = ClsConstants.DATE_FORMAT_SHORT_SPANISH;//"dd/MM/yyyy";//Java Short Date Format
			SimpleDateFormat formateo = new SimpleDateFormat(jsdf);
			Date date = new Date();
			date = formateo.parse(fecha);
	
			//Calendario
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(date);

			//Nueva fecha calculada
			Date siguiente = new Date();
			int dia = 0;
			for (int i=0; i<dias; i++) {
				dia = calendario.get(Calendar.DAY_OF_MONTH);
				calendario.set(Calendar.DAY_OF_MONTH,dia+1);
			}
			siguiente = calendario.getTime();
			
			//Formateo la fecha como un String
			salida = formateo.format(siguiente);
			
		}
		catch (Exception e) {
		}

		return salida;
	}
	/**
	 * Convierte un string con formato dd/MM/yyyy o yyyy/MM/dd HH:mm:ss a calendar
	 * @param strDate dd/MM/yyyy o yyyy/MM/dd HH:mm:ss
	 * @return
	 */
	public static Calendar stringToCalendar(String strDate){
		SimpleDateFormat sdf;
		if(strDate.length()<=10){
			sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		}else{
			sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		}
		Calendar cal = Calendar.getInstance();
		try {
			Date date;
			date = sdf.parse(strDate);
			cal.setTime(date);
		} catch (ParseException e) {
			cal = null;
		}
		return cal;
	}

}
