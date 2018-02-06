package com.siga.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;

public class UtilidadesFecha {

	public static String FORMATO_FECHA_ES = "dd/MM/yyyy";

	/**
	 * Turns a date into an string, using the format as parameter.
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String getString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (null != date)
			return sdf.format(date);
		else
			return null;
	}

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
	
	/**
	 * Devuelve la misma fecha pasada como parametro pero en formato Java (ClsConstants.DATE_FORMAT_JAVA), lista para insertar en BD.
	 * <br>
	 * <br>
	 * Internamente primero comprueba si esta en dicho formato. Si no lo esta, prueba con el formato corto espanyol ClsConstants.DATE_FORMAT_SHORT_SPANISH.
	 * <br>
	 * Finalmente devuelve la fecha en formato Java (ClsConstants.DATE_FORMAT_JAVA) o bien lanza un error si no ha coincidido con ninguno de los formatos.
	 * 
	 * @param fecha
	 * @return
	 * @throws ClsExceptions
	 */
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
	
	/**
	 * Convierte un string con formato dd/MM/yyyy o yyyy/MM/dd HH:mm:ss a calendar
	 * @param strDate dd/MM/yyyy o yyyy/MM/dd HH:mm:ss
	 * @return
	 */
	public static Calendar stringToCalendar(String strDate){
		SimpleDateFormat sdf;
		Calendar cal = Calendar.getInstance();
		Date date;
		try {
			if(strDate.length()<=10){
				sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			}else{
				sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			}
			date = sdf.parse(strDate);
			cal.setTime(date);
		} catch (ParseException e) {
			cal = null;
		}
		return cal;
	}
	
	/**
	 * Valida si una fecha es laborable .
	 * Devuelve true si es Laborable y false si es Festivo.
	 * 
	 * @param String fecha: fecha a validar.
	 * @return boolean True si es una fecha laborable. False si es festivo. 
	 */
	public static boolean esFechaLaborable(String fecha) {
		boolean laborable = false;		
		
		try {
			//Recupero del String fechaInicial con formato dd/mm/yyyy la fecha como Date
			String jsdf = ClsConstants.DATE_FORMAT_SHORT_SPANISH;//"dd/MM/yyyy";//Java Short Date Format
			SimpleDateFormat formateo = new SimpleDateFormat(jsdf);
			Date date = new Date();
			date = formateo.parse(fecha);
	
			//Calendario
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(date);
	
			//Compruebo que no sea Sabado o Domingo
			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				laborable = false;
				
			}else {
				laborable = true;
			}
		
		} catch (Exception e) {
			laborable = false;
		}
		
		return laborable;
	}

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
	
	public static Date getFirstDayOfMonth(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime((Date)date.clone());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime((Date)date.clone());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static Date getDaysOfMonthBackwards(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime((Date)date.clone());
		cal.add(Calendar.DATE, (-1) * cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static Date getToday() throws ParseException {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		return today.getTime();
	}
	
	public static String getToday(String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(getToday());
	}
	
	/**
	 * Returns a date which is previous in a 'number of days' to the 'date' provided
	 * @param date
	 * @param numberOfDays
	 * @return
	 */
	public static Date subDays(Date date, int numberOfDays) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime((Date)date.clone());
		cal.add(Calendar.DATE, (-1) * numberOfDays);
		return cal.getTime();
	}
	
	/**
	 * Returns a date which is later in a 'number of days' to the 'date' provided
	 * @param date
	 * @param numberOfDays
	 * @return
	 */
	public static Date addDays(Date date, int numberOfDays) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime((Date)date.clone());
		cal.add(Calendar.DATE, numberOfDays);
		return cal.getTime();
	}

}
