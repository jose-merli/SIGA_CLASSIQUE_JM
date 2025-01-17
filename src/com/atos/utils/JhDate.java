package com.atos.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.redabogacia.sigaservices.app.util.JhFormat;

/**
 * Clase utilizada para realizar varias operaciones de formateo de fechas.<br>
 * @version: 1.0
 */

public class JhDate{
	public static final int HCVNDay = 4;
	public static final int HCVNHour = 3;
	public static final int HCVNMinute = 2;
	public static final int HCVNMonth = 5;
	//Campos constantes
	public static final int HCVNSecond = 1;
	public static final int HCVNYear = 6;

	//Campos de instancia
	private int Day;
	private final String [] diassemana={"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
	private int Horas;
	private final String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	private int Minutos;
	private int Month;
	private int Segundos;
	private int Year;
/**
 * Construye una clase jhDate con la fecha y hora por defecto.
 * La hora est� en formato 24 horas.
 */
public JhDate() {
	GregorianCalendar fecha = new GregorianCalendar();
	this.setDay(fecha.get(GregorianCalendar.DAY_OF_MONTH));
	this.setMonth(fecha.get(GregorianCalendar.MONTH)+1);
	this.setYear(fecha.get(GregorianCalendar.YEAR));
	this.setHoras(fecha.get(GregorianCalendar.HOUR_OF_DAY));
	this.setMinutos(fecha.get(GregorianCalendar.MINUTE));
	this.setSegundos(fecha.get(GregorianCalendar.SECOND));
}
/**
 * Construye un objeto jhDate con la fecha pasada por par�metros BigDecimal
 * @param year BigDecimal
 * @param month BigDecimal
 * @param day BigDecimal
 */
public JhDate(BigDecimal year, BigDecimal month, BigDecimal day) throws Exception{
	insertadatos(year.intValue(), month.intValue(), day.intValue());
}
/**
 * Construye un objeto jhDate con la fecha y hora pasados por par�metros BigDecimal
 * @param year BigDecimal
 * @param month BigDecimal
 * @param day BigDecimal
 * @param hour BigDecimal
 * @param minute BigDecimal
 */
public JhDate(BigDecimal year, BigDecimal month, BigDecimal day, BigDecimal hour, BigDecimal minute) throws Exception{
	insertadatos(year.intValue(), month.intValue(), day.intValue(), hour.intValue(), minute.intValue());
}
/**
 * Construye un objeto jhDate con la fecha pasada como par�metros.
 * @param year int
 * @param month int
 * @param day int
 */
public JhDate(Date fecha){
	Calendar calendar = new GregorianCalendar();
	calendar.setTime(fecha);
	try {
		insertadatos(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	} catch(Exception e) {}
}
/**
 * Construye un objeto jhDate con la fecha pasada como par�metros.
 * @param year int
 * @param month int
 * @param day int
 */
public JhDate(int year, int month, int day) throws Exception{
	insertadatos(year, month, day);
}
/**
 * Construye un objeto jhDate con la fecha y hora pasados por par�metros int
 * @param year int
 * @param month int
 * @param day int
 * @param hour int
 * @param hour minute
 */
public JhDate(int year, int month, int day, int hour, int minute) throws Exception{
	insertadatos(year, month, day, hour, minute);
}
/**
 * Construye un objeto jhDate con la hora actual y la fecha pasada como par�metro seg�n el formato indicado
 * @param fecha String
 * @param formato int
 * El formato puede ser de tres tipos:
 *		1- ddmmaaaa
 *		2- aaaammdd
 *		3- aaaa-mm-dd hh:mm:ss.m (Valor recuperado desde el SQLServer)
 *		4- aaaamm
 * El String fecha puede contener o no el s�mbolo "/".
 */

public JhDate(String fecha, int formato) throws Exception{
	this(fecha,"/",formato);
}

/**
 * Construye un objeto jhDate con la hora actual y la fecha pasada como par�metro seg�n el formato indicado
 * @param fecha String
 * @param formato int
 * El formato puede ser de tres tipos:
 *		1- ddmmaaaa
 *		2- aaaammdd
 *		3- aaaa-mm-dd hh:mm:ss.m (Valor recuperado desde el SQLServer)
 *		4- aaaamm
 * El String fecha puede contener o no el s�mbolo "/".
 */

public JhDate(String fecha, String separador, int formato) throws Exception{
	int dia=0;
	int mes=0;
	int ano=0;
	int hora=0;
	int minuto=0;

	if (formato !=1 && formato!= 2 && formato != 3 && formato != 4)
		throw new Exception("Format not valid. 1- ddmmaaaa, 2-aaaammdd, 3-SQLServer, 4-aaaamm");
	if (fecha.trim().length()!= 6 && fecha.trim().length()!= 8 && fecha.trim().length()!= 10 && fecha.trim().length() > 30)
		throw new Exception("Longitud de fecha no v�lida");

	switch (formato){
		case 1:{
			//La fecha est� en formato ddmmaaaa
			if (fecha.indexOf(separador) == -1){
				//No hay barras
				dia = new BigDecimal(fecha.substring(0,2)).intValue();
				mes = new BigDecimal(fecha.substring(2,4)).intValue();
				ano = new BigDecimal(fecha.substring(4,8)).intValue();
			}else{ //Hay barras y va en formato dd/mm/aaaa
				dia = new BigDecimal(fecha.substring(0,2)).intValue();
				mes = new BigDecimal(fecha.substring(3,5)).intValue();
				ano = new BigDecimal(fecha.substring(6,10)).intValue();
			}
			break;
		}
		case 2:{
			//La fecha est� en formato aaaammdd
			if (fecha.indexOf(separador) == -1){
				//No hay barras
				ano = new BigDecimal(fecha.substring(0,4)).intValue();
				mes = new BigDecimal(fecha.substring(4,6)).intValue();
				dia = new BigDecimal(fecha.substring(6,8)).intValue();
			}else{ //Hay barras y va en formato aaaa/mm/dd
				ano = new BigDecimal(fecha.substring(0,4)).intValue();
				mes = new BigDecimal(fecha.substring(5,7)).intValue();
				dia = new BigDecimal(fecha.substring(8,10)).intValue();
			}
			break;
		}
		case 3:{
			//La fecha llega desde el SQLServer
			//2002-01-01 00:00:00.0
			ano = new java.math.BigDecimal(fecha.substring(0,4)).intValue();
			mes = new java.math.BigDecimal(fecha.substring(5,7)).intValue();
			dia = new java.math.BigDecimal(fecha.substring(8,10)).intValue();
			hora = new java.math.BigDecimal(fecha.substring(11,13)).intValue();
			minuto = new java.math.BigDecimal(fecha.substring(14,16)).intValue();
			break;
		}
		case 4:{
			//La fecha est� en formato aaaamm
			if (fecha.indexOf(separador) == -1){
				//No hay barras
				ano = new BigDecimal(fecha.substring(0,4)).intValue();
				mes = new BigDecimal(fecha.substring(4,6)).intValue();
				dia = new BigDecimal(1).intValue();
			}else{ //Hay barras y va en formato aaaa/mm
				ano = new BigDecimal(fecha.substring(0,4)).intValue();
				mes = new BigDecimal(fecha.substring(5,7)).intValue();
				dia = new BigDecimal(1).intValue();
			}
			break;
		}
	}
	try{
		insertadatos(ano, mes, dia, hora, minuto);
	}catch (Exception e){
		insertadatos(ano, mes, dia);
	}
}

/**
 * Incrementa la fecha
 * @param campo int
 * <p>Indica el campo a incrementar:
 * 	<ul>
 * 		<li>1: Segundos</li>
 * 		<li>2: Minutos</li>
 * 		<li>3: Horas </li>
 * 		<li>4: D�as</li>
 * 		<li>5: Meses</li>
 * 		<li>6: Anhos</li>
 * 	</ul>
 * </p>
 * @param incremento int
 */
public void add(int campo, int incremento){
	try{
		Calendar calendar = new GregorianCalendar();
		calendar.set(this.Year, ((this.Month) - 1), this.Day, this.Horas, this.Minutos, this.Segundos);
		switch (campo){
			case 1:{
				//Anhadimos segundos, todav�a no lo necesitamos
				break;
			}
			case 2:{
				//Anhadimos minutos
				calendar.add(Calendar.MINUTE, incremento);
				break;
			}
			case 3:{
				//Anhadimos horas
				calendar.add(Calendar.HOUR_OF_DAY, incremento);
				break;
			}
			case 4:{
				//Anhadimos d�as
				calendar.add(Calendar.DATE, incremento);
				break;
			}
			case 5:{
				//Anhadimos meses
				calendar.add(Calendar.MONTH, incremento);
				break;
			}
			case 6:{
				//Anhadimos anhos
				calendar.add(Calendar.YEAR, incremento);
				break;
			}
		}
		//Anhado el incremento
		insertadatos(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	} catch (Exception e){}
}

public void addDays(int days) {
	try {
		Calendar calendar = new GregorianCalendar();
		calendar.set(this.Year, this.Month, this.Day, this.Horas, this.Minutos, this.Segundos);
		calendar.add(Calendar.DATE, days);
		insertadatos(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	} catch (Exception e) {}
}

/**
 * Compara dos fechas entre s�
 * Par�metros de entrada:
 		- fecha: variable de tipo Date que recibe la funci�n
   Par�metros de salida:
   		- -1: si la fecha es mayor que la pasada por par�metro
   		-  0: si la fecha introducida es igual
   		-  1: si la fecha es menor que la pasada por par�metro
 */
public int equals(JhDate fecha2) {
	int dia = fecha2.getDay();
	int mes = fecha2.getMonth();
	int ano = fecha2.getYear();

	int hora = fecha2.getHoras();
	int minuto = fecha2.getMinutos();

	//Miramos si las 2 fechas son igual
	if ((ano == this.getYear()) && (mes == this.getMonth()) && (dia == this.getDay()) && (hora == this.getHoras()) && (minuto == this.getMinutos())) return 0;

	//Comparamos el anho
	if (ano > this.getYear()) return 1;
	if (ano < this.getYear()) return -1;

	//Si el anho es igual comparamos los meses
	if (mes > this.getMonth()) return 1;
	if (mes < this.getMonth()) return -1;

	//Si son del mismo anho y mes comparamos el d�a
	if (dia > this.getDay()) return 1;
	if (dia < this.getDay()) return -1;

	//Si son del mismo d�a comparamos las horas
	if (hora > this.getHoras()) return 1;
	if (hora < this.getHoras()) return -1;

	//Si son de la misma hora comparamos los minutos
	if (minuto > this.getMinutos()) return 1;
	if (minuto < this.getMinutos()) return -1;

	return 2;
}
/**
 * Devuelve la fecha en el formato indicado y poniendo barras o no
 * @param formato int
 * @param barras boolean
 * @return java.lang.String
 * Formatos posibles:
 *	- 1: ddmmaaaa
 *	- 2: aaaammdd
 *	- 3: aaaamm
 *	- 4: Formato SQLServer
 */
public String getDate(int formato, boolean barras) throws Exception{
	if (formato != 1 && formato!=2 && formato!=3 && formato !=4)
		throw new Exception("Format invalid");

	String fecha = "";
	switch (formato) {
		case 1:{
			if (barras){
				fecha += JhFormat.fill(new BigDecimal(this.getDay()).toString(),'0',2,1);
				fecha += "/";
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
				fecha += "/";
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
			}else{
				fecha += JhFormat.fill(new BigDecimal(this.getDay()).toString(),'0',2,1);
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
			}
			break;
		}
		case 2:{
			if (barras){
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
				fecha += "/";
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
				fecha += "/";
				fecha += JhFormat.fill(new BigDecimal(this.getDay()).toString(),'0',2,1);
			}else{
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
				fecha += JhFormat.fill(new BigDecimal(this.getDay()).toString(),'0',2,1);
			}
			break;
		}
		case 3:{
			if (barras){
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
				fecha += "/";
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
			}else{
				fecha += JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
				fecha += JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
			}
			break;
		}
		case 4:{
			//La fecha va al SQLServer
			//2002-01-01 00:00:00.0
			fecha = JhFormat.fill(new BigDecimal(this.getYear()).toString(),'0',4,1);
			fecha+= "-";
			fecha+= JhFormat.fill(new BigDecimal(this.getMonth()).toString(),'0',2,1);
			fecha+= "-";
			fecha+= JhFormat.fill(new BigDecimal(this.getDay()).toString(),'0',2,1);
			fecha+= " ";
			fecha+= JhFormat.fill(new BigDecimal(this.getHoras()).toString(),'0',2,1);
			fecha+= ":";
			fecha+=JhFormat.fill(new BigDecimal(this.getMinutos()).toString(),'0',2,1);
			fecha+=":";
			fecha+=JhFormat.fill(new BigDecimal(this.getSegundos()).toString(),'0',2,1);
			fecha+=".0";
			break;
		}
	}

	return fecha;

}

public int getDay() {
	return Day;
}

public int getHoras() {
	return Horas;
}
/**
 * Nos devuelve la hora con descripci�n larga
 * @return java.lang.String
 * @param formato int
 * El formato puede tener los siguientes valores:
 *	- 1: hh:mm:ss (24 horas)
 *	- 2: hh:mm (24 horas)
 *	- 3: hh:mm:ss (AM/PM)
 *	- 4: hh:mm (AM/PM)
 *	- 5: hhmm
 */

public String getHour(int formato) {
	switch (formato){
		case 1:
		{
			String horas = JhFormat.fill(String.valueOf(this.getHoras()),'0',2,1);
			String minutos = JhFormat.fill(String.valueOf(this.getMinutos()),'0',2,1);
			String segundos = JhFormat.fill(String.valueOf(this.getSegundos()),'0',2,1);
			return (horas + ":" + minutos + ":" + segundos);
		}
		case 2:
		{
			String horas = JhFormat.fill(String.valueOf(this.getHoras()),'0',2,1);
			String minutos = JhFormat.fill(String.valueOf(this.getMinutos()),'0',2,1);
			return (horas + ":" + minutos);
		}
		case 3:
		{
			int hora = this.getHoras();
			String tipo = "AM";
			if (hora > 12){
				hora -= 12;
				tipo = "PM";
			}
			String minutos = JhFormat.fill(String.valueOf(this.getMinutos()),'0',2,1);
			String segundos = JhFormat.fill(String.valueOf(this.getSegundos()),'0',2,1);
			return (hora + ":" + minutos + ":" + segundos + " " + tipo);
		}
		case 4:{
			int hora = this.getHoras();
			String tipo = "AM";
			if (hora > 12){
				hora -= 12;
				tipo = "PM";
			}
			String minutos = JhFormat.fill(String.valueOf(this.getMinutos()),'0',2,1);
			return (hora + ":" + minutos + " " + tipo);
		}
		case 5:{
			return (JhFormat.fill(String.valueOf(this.getHoras()),'0',2,1)+JhFormat.fill(String.valueOf(this.getMinutos()),'0',2,1));
		}
	}

	return "";
}
/**
 * Nos devuelve la fecha con descripci�n larga
 * @return java.lang.String
 * @param formato int
 * El formato puede tener los siguientes valores:
 *	- 1: "Lunes 17 de Febrero de 2002
 *	- 2: "17 de Febrero de 2002"
 */

public String getLongDate(int formato) {
	String fecha = "";

	GregorianCalendar dia2= new GregorianCalendar(this.getYear(),this.getMonth()-1,this.getDay());
	int dayofweek = dia2.get(GregorianCalendar.DAY_OF_WEEK);
	if (formato == 1){
		fecha = diassemana[dayofweek] + " " + this.getDay() + " de " + meses[this.getMonth()-1] + " de " + this.getYear();
	}else if (formato == 2){
		fecha = this.getDay() + " de " + meses[this.getMonth()-1] + " de " + this.getYear();
	}

	return fecha;
}

public int getMinutos() {
	return Minutos;
}

public int getMonth() {
	return Month;
}

public int getSegundos() {
	return Segundos;
}
/**
 * Devuelve el tiempo transcurrido entre 2 fechas
 * @return long
 * @param int campo
 * Los valores de campo pueden ser:
 *	- 1: D�as transcurridos.
 *	- 2: Meses transcurridos.
 *	- 3: Anhos transcurridos
 *	- 4: Horas transcurridas
 *	- 5: Segundos transcurridos
 */
public long getTiempoTranscurrido(JhDate fecha2, int campo) {
	long milisegundos = fecha2.getTime();
	long milisegundosactuales = this.getTime();
	long diferencia = milisegundosactuales - milisegundos;
	if (diferencia < 0)
		diferencia = diferencia * -1;
	switch (campo){
		case 1: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			return diferencia;
		}
		case 2: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			diferencia /= 30;
			return diferencia;
		}
		case 3: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			diferencia /= 365;
			return diferencia;
		}
		case 4: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			return diferencia;
		}
		case 5: {
			diferencia /= 1000;
			diferencia /= 60;
			return diferencia;
		}
		case 6: {
			diferencia /= 1000;
			return diferencia;
		}
	}
	return diferencia;
}
/**
 * Inserte aqu� la descripci�n del m�todo.
 * @return java.lang.String
 */
public long getTime() {
	GregorianCalendar fecha = new GregorianCalendar(this.getYear(), this.getMonth(), this.getDay(), this.getHoras(),this.getMinutos(),this.getSegundos());
	Date aux = fecha.getTime();
	return aux.getTime();
}

public int getYear() {
	return Year;
}

public void insertadatos(int year, int month, int day) throws Exception{
	if (!validaFecha(year, month, day)){
		throw new Exception("The Date is not valid");
	}
	this.setYear(year);
	this.setMonth(month);
	this.setDay(day);
	this.setHoras(00);
	this.setMinutos(00);
	this.setSegundos(00);
}
/**
 * Construye un objeto jhDate con la fecha y hora pasados por par�metros int
 * @param year int
 * @param month int
 * @param day int
 * @param hour int
 * @param hour minute
 */
public void insertadatos(int year, int month, int day, int hour, int minute) throws Exception{
	if (!validaFecha(year, month, day)){
		throw new Exception("The Date is not valid");
	}
	if (!validaHora(hour, minute)){
		throw new Exception("The Hour is not valid");
	}
	this.setYear(year);
	this.setMonth(month);
	this.setDay(day);
	this.setHoras(hour);
	this.setMinutos(minute);
	this.setSegundos(0);
}
/**
 * Compara dos fechas entre s�
 * Par�metros de entrada:
 		- fecha: variable de tipo Date que recibe la funci�n
   Par�metros de salida:
   		- -1: si la fecha introducida es menor
   		-  0: si la fecha introducida es igual
   		-  1: si la fecha introducida es mayor
 */
public boolean isBetween(JhDate fechainicio, JhDate fechafin) {
	if (this.equals(fechainicio) != 1 && this.equals(fechafin)!=-1)
		return true;
	else
		return false;
}

public void setDay(int newDay) {
	Day = newDay;
}

public void setHoras(int newHoras) {
	Horas = newHoras;
}

public void setMinutos(int newMinutos) {
	Minutos = newMinutos;
}

public void setMonth(int newMonth) {
	Month = newMonth;
}

public void setSegundos(int newSegundos) {
	Segundos = newSegundos;
}

public void setYear(int newYear) {
	Year = newYear;
}

/**
 * Valida la fecha
 * @param ano int	Anho a validar
 * @param mes int	Mes a validar
 * @param dia int	D�a a validar
 * @return boolean Indica si la fecha es v�lida
 */
public boolean validaFecha(int ano, int mes, int dia) {
	if (ano == 0 && mes == 0 && dia == 0) return true;
	if (ano < 1000) return false;
	if ((mes==4) || (mes==6) ||(mes==9) || (mes==11))
		if ((dia<1) || (dia>30)) return false;
	if ((mes==1)||(mes==3)||(mes==5)||(mes==7)||(mes==8)||(mes==10)||(mes==12))
			if ((dia < 1) || (dia>31)) return false;
	if ((mes==2))//Tratamos los anhos bisiestos
		if ( ((ano % 4) == 0) && (((ano % 100)!= 0) || ((ano % 400) == 0))  ){
			if ((dia<1) || (dia>29)){
				return false;
			}
		}else{
			if ((dia<1) || (dia>28)){
				return false;
			}
		}
	if (mes <1 || mes>12) return false;
	return true;

}
/**
 * Realiza la validaci�n de las horas.
 * @return boolean	Indica si la hora es v�lida o no
 * @param hora int	Hora a validar
 * @param minuto int	Minutos a validar
 */
public boolean validaHora(int hora, int minuto) {
	if (hora == 0 && minuto == 0) return true;
	if ((hora > 23) || (hora<0) || (minuto > 59) || (minuto < 0))
		return false;
	else
		return true;

}
}
