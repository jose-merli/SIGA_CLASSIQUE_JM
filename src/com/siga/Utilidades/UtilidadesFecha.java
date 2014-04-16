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
			//Calendario
			Calendar calendario = Calendar.getInstance();
			if(fecha!=null && !fecha.equals("")){
				Date date = new Date();
				date = formateo.parse(fecha);
				calendario.setTime(date);
			}

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
	 * Devuelve la fecha Fin a partir de la fecha de inicio y los dias de margen.
	 * Devuelve true si es Laborable y false si es Festivo.
	 * 
	 * @param String fechaInicio: Fecha de Inicio en el formato DD/MM/YYYY.
	 * @param String dias: dias de margen entre la Fecha de Inicio y la Fecha Fin. Si es 0 devuelve la Fecha de Inicio.
	 * @return String con la Fecha Fin en el formato DD/MM/YYYY. 
	 */
	public static String obtenerFechaFinLaborable(String fechaFin, int diasRestantes) {
		//Busco la fecha fin a partir de la fecha inicio seleccionada. Sigo buscando mientras tenga diasRestantes > 0
		while (diasRestantes > 0) {				
			fechaFin = UtilidadesFecha.sumarDias(fechaFin,1);
			if (esFechaLaborable(fechaFin)) 
				diasRestantes--;				
		}
		
		return fechaFin;
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

}
