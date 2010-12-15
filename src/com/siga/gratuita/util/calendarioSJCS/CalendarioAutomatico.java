/*
 * Created on Mar 9, 2006
 * Modified on April 7, 2008
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.gratuita.util.calendarioSJCS.Entity.CalendarioEfectivo;

import java.util.Calendar;

/**
 * @author A203486
 * @author adrianag
 *
 * Esta Clase implementa un paso intermedio para instanciar en el metodo <br>
 * "obtenerMatrizDiasGuardia ()" la Clase "CalendarioEfectivo" del CGAE que <br>
 * rellena la lista de dias de guardia posibles y que permitira generar <br>
 * el calendario automatico por medio de la clase "CalendarioSJCS".
 * 
 * En su constructor se inicializaran todos los atributos de clase <br>
 * necesarios para realizar la llamada al metodo del CGAE.
 * 
 * Es necesario tener primero una instancia de "CalendarioSJCS" ya que en <br>
 * ella se calculan los datos propios de la guardia.
 * 
 * Desde la clase "CalendarioSJCS" se puede llamar al metodo <br>
 * "calcularMatrizPeriodosDiasGuardia ()" que usa esta clase para calcular <br>
 * el periodo de guardias.   
 */
public class CalendarioAutomatico
{
	//////////////////// ATRIBUTOS ////////////////////
	/** Este ArrayList tiene la misma estructura que el del CGAE y contiene
	 * los dias posibles para esa guardia calculado por el metodo del CGAE
	 */
	private ArrayList arrayDiasGuardiaCGAE;
	
	/** Este es el Calendario con los atributos propios de SJCS necesario para 
	 * llamar al metodo del CGAE que calcula los dias posibles de una guardia
	 */
	private CalendarioSJCS calendarioSJCS;

	//Datos necesatios para la clase del CGAE que genera
	//la lista de guardias posibles
	private String fechaInicio;
	private String fechaFin;
	private int duracion;
    private int unidadesDuracion;
    private int periodo;
    private int unidadesPeriodo;
    private Vector seleccionLaborables = null;
    private Vector seleccionFestivos = null;
    private Vector listaFestivos = null;
	
	
	//////////////////// CONSTRUCTORES ////////////////////
	public CalendarioAutomatico (CalendarioSJCS calendarioSJCS)
	{
		//variables
		ScsGuardiasTurnoBean beanGuardiasTurno;
		String semana;
		
		//controles globales
		this.calendarioSJCS = calendarioSJCS;
		beanGuardiasTurno = calendarioSJCS.getBeanGuardiasTurno ();
		
		//FECHAS:
		try {
			this.fechaInicio = UtilidadesFecha.getFechaApruebaDeFormato(new String(calendarioSJCS.getFechaInicio()));
			this.fechaFin = UtilidadesFecha.getFechaApruebaDeFormato(new String(calendarioSJCS.getFechaFin()));
		} catch (Exception e) {
			this.fechaInicio = "";
			this.fechaFin = "";
		}
		
		//DURACION:
		this.duracion = beanGuardiasTurno.getDiasGuardia ().intValue ();
		this.unidadesDuracion = this.convertirUnidadesDuracion 
				(beanGuardiasTurno.getTipodiasGuardia ());

		//PERIODO:
		if (beanGuardiasTurno.getDiasPeriodo ()!=null)
			this.periodo = beanGuardiasTurno.getDiasPeriodo ().intValue ();
		else
			this.periodo = 0;
		this.unidadesPeriodo = this.convertirUnidadesDuracion
				(beanGuardiasTurno.getTipoDiasPeriodo ());
		
		//Seleccion de laborables:
		this.seleccionLaborables = new Vector ();
		semana = beanGuardiasTurno.getSeleccionLaborables();
		for (int i=0; i<semana.length(); i++)
			this.seleccionLaborables.add (new Integer 
					(this.convertirUnidadesDiasSemana (semana.charAt (i))));
		
		//Seleccion de festivos:
		this.seleccionFestivos = new Vector ();
		semana = beanGuardiasTurno.getSeleccionFestivos ();
		for (int i=0; i<semana.length(); i++)
			this.seleccionFestivos.add (new Integer 
					(this.convertirUnidadesDiasSemana (semana.charAt (i))));
		
		//FESTIVOS:
		this.listaFestivos = 
				(Vector) calendarioSJCS.getVDiasFestivos ().clone ();
	} //CalendarioAutomatico ()
	
	
	//////////////////// METODOS ////////////////////
	/** Convierte de nuestro formato para el combo de tipo de dias de guardia 
	 * (Duracion) al usado por el CGAE
	 */
	private int convertirUnidadesDuracion (String tipoDiasGuardia) {
		int unidades = 0;
		
		try {
			switch (tipoDiasGuardia.charAt(0)) {
				case 'D': unidades = Calendar.DAY_OF_YEAR; break;//6
				case 'S': unidades = Calendar.WEEK_OF_YEAR; break;//3
				case 'Q': unidades = CalendarioEfectivo.QUINCENA; break;//1524
				case 'M': unidades = Calendar.MONTH; break;//2
			}
		} catch (Exception e){
			unidades = 0;
		}
		return unidades;
	}

	/** Convierte de nuestro formato para los checkBox 
	 * de los dias seleccionados del campo SEMANA en los usados por el CGAE.
	 */
	public static int convertirUnidadesDiasSemana (char dia) {
		int unidades = 0;
		
		try {
			switch (dia) {
				case 'D': unidades = Calendar.SUNDAY; break;//1
				case 'L': unidades = Calendar.MONDAY; break;//2
				case 'M': unidades = Calendar.TUESDAY; break;//3
				case 'X': unidades = Calendar.WEDNESDAY; break;//4
				case 'J': unidades = Calendar.THURSDAY; break;//5
				case 'V': unidades = Calendar.FRIDAY; break;//6
				case 'S': unidades = Calendar.SATURDAY; break;//7
			}
		} catch (Exception e){
			unidades = 1;
		}
		return unidades;
	}
	
	/** Metodo para obtener una matriz con las guardias disponibles 
	 * segun las condiciones de la guardia.
	 */
	public ArrayList obtenerMatrizDiasGuardia() throws ClsExceptions {
		CalendarioEfectivo calendarioCGAE;
		ArrayList arrayPeriodosDiasGuardia = new ArrayList();
		
		try {
			//Imprimo para ver los datos de la llamada:
			this.imprimirDatosCalendario();
			
			//Instancio el Calendario Efectivo del CGAE que calcula 
			//los dias disponibles para hacer guardias:
			calendarioCGAE = new CalendarioEfectivo(this.fechaInicio,
												    this.fechaFin,
												    this.duracion,
												    this.unidadesDuracion,
												    this.periodo,
												    this.unidadesPeriodo,
												    this.seleccionLaborables,
												    this.seleccionFestivos,
												    this.listaFestivos);
			
            //Carga en la lista de salida de los periodos efectivos
            arrayPeriodosDiasGuardia = calendarioCGAE.getListaPeriodos();
		} catch (Exception e) {
			throw new ClsExceptions(e,"Excepcion en obtenerMatrizDiasGuardia()");
		}
		return arrayPeriodosDiasGuardia;
	}

	/** Muestra en el LOG la configuracion del Calendario */
	private void imprimirDatosCalendario ()
	{		
		ClsLogging.writeFileLog("*** DATOS DEL CALENDARIO:",10);
		ClsLogging.writeFileLog("> FECHA INICIO:"+this.fechaInicio,10);
		ClsLogging.writeFileLog("> FECHA FIN:"+this.fechaFin,10);		
		ClsLogging.writeFileLog("> DURACION:"+this.duracion,10);
		ClsLogging.writeFileLog("> UNIDADES DURACION:"+this.unidadesDuracion,10);
		ClsLogging.writeFileLog("> PERIODO:"+this.periodo,10);
		ClsLogging.writeFileLog("> UNIDADES PERIODO:"+this.unidadesPeriodo,10);
		ClsLogging.writeFileLog("> SELECCION LABORABLES:"+this.seleccionLaborables.toString(),10);
		ClsLogging.writeFileLog("> SELECCION FESTIVOS:"+this.seleccionFestivos.toString(),10);
		ClsLogging.writeFileLog("***",10);
	} //imprimirDatosCalendario ()
	
	
	//////////////////// GETTERS ////////////////////
	public CalendarioSJCS getCalendarioSJCS()	{return calendarioSJCS;}
	public String getFechaInicio()				{return fechaInicio;}
	public String getFechaFin()					{return fechaFin;}
	public int getDuracion()					{return duracion;}
	public int getUnidadesDuracion()			{return unidadesDuracion;}
	public int getPeriodo()						{return periodo;}
	public int getUnidadesPeriodo()				{return unidadesPeriodo;}
	public Vector getSeleccionLaborables()		{return seleccionLaborables;}
	public Vector getSeleccionFestivos()		{return seleccionFestivos;}
	public Vector getListaFestivos()			{return listaFestivos;}
	public ArrayList getVDiasGuardia()			{return arrayDiasGuardiaCGAE;}
	
	
	//////////////////// SETTERS ////////////////////
	public void setCalendarioSJCS		(CalendarioSJCS calendarioSJCS)	{this.calendarioSJCS = calendarioSJCS;}
	public void setFechaInicio			(String fechaInicio)			{this.fechaInicio = fechaInicio;}
	public void setFechaFin				(String fechaFin)				{this.fechaFin = fechaFin;}
	public void setDuracion				(int duracion)					{this.duracion = duracion;}
	public void setUnidadesDuracion		(int unidadesDuracion)			{this.unidadesDuracion = unidadesDuracion;}
	public void setPeriodo				(int periodo)					{this.periodo = periodo;}
	public void setUnidadesPeriodo		(int unidadesPeriodo)			{this.unidadesPeriodo = unidadesPeriodo;}
	public void setSeleccionLaborables	(Vector seleccionLaborables)	{
		this.seleccionLaborables.clear ();
		for (int i=0; i<seleccionLaborables.size(); i++)
			this.seleccionLaborables.add (seleccionLaborables.get (i));
	}
	public void setSeleccionFestivos	(Vector seleccionFestivos)		{
		this.seleccionFestivos.clear ();
		for (int i=0; i<seleccionFestivos.size(); i++)
			this.seleccionFestivos.add (seleccionFestivos.get (i));
	}
	public void setListaFestivos		(Vector listaFestivos)			{
		this.listaFestivos.clear ();
		for (int i=0; i<listaFestivos.size(); i++)
			this.listaFestivos.add (listaFestivos.get (i));
	}
	public void setVDiasGuardia			(ArrayList diasGuardia)			{
		this.arrayDiasGuardiaCGAE.clear ();
		for (int i=0; i<diasGuardia.size(); i++)
			this.arrayDiasGuardiaCGAE.add (diasGuardia.get (i));
	}

}
