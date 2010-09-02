/*
 * CalendarioEfectivo.java
 *
 * Creado: 6 de febrero de 2006
 * Modificado: 7 de mayo de 2007
 */

package com.siga.gratuita.util.calendarioSJCS.Entity;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author AAG
 */
public class CalendarioEfectivo {
	// ATRIBUTOS DE CLASE
	/**
	 * Tipo de Unidad que representa los días desde el 1 hasta el 15, y desde el
	 * 16 hasta el último día de mes
	 */
	public final static int QUINCENA = 1524;

	// ATRIBUTOS
	// Atributos de Entrada
	private Date fechaInicio;
	private Date fechaFin;
	private int duracion;
	private int unidadesDuracion;
	private int periodo;
	private int unidadesPeriodo;

	private TreeSet seleccionLaborables = null;
	private TreeSet seleccionFestivos = null;
	private TreeSet listaFestivos = null;

	// Atributos de Salida
	private ArrayList listaPeriodos;
	private int Punteroperiodo;

	// CONSTRUCTORES
	/** Crea una nueva instancia de CalendarioEfectivo */
	public CalendarioEfectivo(String fechaInicio, String fechaFin,
			int duracion, int unidadesDuracion, int periodo,
			int unidadesPeriodo, Vector seleccionLaborables,
			Vector seleccionFestivos, Vector listaFestivos) {
		Iterator iter = null;
		SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");

		// Carga de datos de Entrada
		try {
			// Carga de rango de generacion del calendario
			this.fechaInicio = datef.parse(fechaInicio);
			this.fechaFin = datef.parse(fechaFin);

			// Carga de duracion y periodo
			this.duracion = duracion;
			this.unidadesDuracion = unidadesDuracion;
			this.periodo = periodo;
			this.unidadesPeriodo = unidadesPeriodo;

			// Carga de Dias Laborables
			this.seleccionLaborables = new TreeSet();
			iter = seleccionLaborables.iterator();
			while (iter.hasNext())
				this.seleccionLaborables.add(((Integer) iter.next()));

			// Carga de Dias Festivos
			this.seleccionFestivos = new TreeSet();
			iter = seleccionFestivos.iterator();
			while (iter.hasNext())
				this.seleccionFestivos.add(((Integer) iter.next()));

			// Carga de Festivos
			this.listaFestivos = new TreeSet();
			iter = listaFestivos.iterator();
			while (iter.hasNext())
				this.listaFestivos.add(datef.parse((String) iter.next()));
		} catch (java.text.ParseException pe) {
			pe.printStackTrace();
		}

		// Generacion de las fechas efectivas
		if (periodo == 0)
			generarCalendarioEfectivoSinPeriodo();
		else
			generarCalendarioEfectivo();

		// Inicializacion para la obtencion de periodos de forma secuencial
		Punteroperiodo = -1;
	} // CalendarioEfectivo

	// GETTERS
	public ArrayList getListaPeriodos() {
		return listaPeriodos;
	} // getListaPeriodos ()

	// METODOS
	/** Genera el Calendario Efectivo si se aplica periodo */
	private void generarCalendarioEfectivo() {
		Date inicioPeriodo;
		PeriodoEfectivo periodoActual;
		Date punteroFecha;
		int tiempoPendiente;

		// INICIO

		// obteniendo primer dia del periodo desde el inicio
		inicioPeriodo = inicializarUnidadDeTiempo(this.unidadesPeriodo,
				this.fechaInicio);

		// recorriendo los periodos
		while (!inicioPeriodo.after(this.fechaFin)) // fechaFin incluido
		{
			periodoActual = new PeriodoEfectivo();

			// obteniendo primer dia de la duracion desde el inicio del periodo
			punteroFecha = inicializarUnidadDeTiempo(this.unidadesDuracion,
					inicioPeriodo);
			tiempoPendiente = this.duracion;

			// calculando siguiente inicio de periodo
			inicioPeriodo = calcularInicioSiguiente(this.unidadesPeriodo,
					inicioPeriodo, this.periodo, this.fechaFin);

			// recorriendo los dias
			while (tiempoPendiente > 0 && punteroFecha.before(inicioPeriodo)) {
				if (cumple(punteroFecha)) {
					// anyadiendo fecha efectiva al periodo actual
					periodoActual.add((Date) punteroFecha.clone());

					// si termina unidad de tiempo, restando duracion pendiente
					if (manyanaTerminaLaUnidadDeTiempo(this.unidadesDuracion,
							punteroFecha))
						tiempoPendiente--;
				}

				// avanzando puntero hasta la siguiente fecha
				punteroFecha = incrementar(punteroFecha, Calendar.DATE, 1);
			} // while

			// anyadiendo periodo a la lista
			if (this.listaPeriodos == null)
				this.listaPeriodos = new ArrayList();
			if (periodoActual.iterator().hasNext())
				this.listaPeriodos.add(periodoActual);

		} // while
	} // generarCalendarioEfectivo ()

	/** Genera el Calendario Efectivo si no aplica periodo */
	private void generarCalendarioEfectivoSinPeriodo() {
		Date inicioSiguiente;
		PeriodoEfectivo periodoActual;
		Date punteroFecha;
		int tiempoPendiente;

		// INICIO

		// obteniendo primer dia del periodo desde el inicio
		punteroFecha = inicializarUnidadDeTiempo(this.unidadesDuracion,
				this.fechaInicio);
		inicioSiguiente = calcularInicioSiguiente(this.unidadesDuracion,
				this.fechaFin, this.duracion, this.fechaFin);

		// recorriendo los periodos
		while (punteroFecha.before(inicioSiguiente))
		{
			periodoActual = new PeriodoEfectivo();
			tiempoPendiente = this.duracion;

			// calculando siguiente inicio de periodo
			inicioSiguiente = calcularInicioSiguiente(this.unidadesDuracion,
					inicioSiguiente, this.duracion, this.fechaFin);

			// recorriendo los dias
			while (tiempoPendiente > 0 && punteroFecha.before(inicioSiguiente)) {
				if (cumple(punteroFecha)) {
					// anyadiendo fecha efectiva al periodo actual
					periodoActual.add((Date) punteroFecha.clone());

					// si termina unidad de tiempo, restando duracion pendiente
					if (manyanaTerminaLaUnidadDeTiempo(this.unidadesDuracion,
							punteroFecha))
						tiempoPendiente--;
				}
				// si termina unidad de tiempo y las unidades no son dias, restando duracion pendiente
				else if (manyanaTerminaLaUnidadDeTiempo(this.unidadesDuracion,
						punteroFecha) && this.unidadesDuracion != Calendar.DAY_OF_YEAR)
					tiempoPendiente--;

				// avanzando puntero hasta la siguiente fecha
				punteroFecha = incrementar(punteroFecha, Calendar.DATE, 1);
			} // while

			// anyadiendo periodo a la lista
			if (this.listaPeriodos == null)
				this.listaPeriodos = new ArrayList();
			if (periodoActual.iterator().hasNext())
				this.listaPeriodos.add(periodoActual);

		} // while
	} // generarCalendarioEfectivoSinPeriodo ()

	/** Devuelve la fecha en la que debe comenzar segun la unidad de tiempo */
	private Date inicializarUnidadDeTiempo(int unidades, Date fechaInicio) {
		Date fechaInicioSiguientePeriodo = (Date) fechaInicio.clone();

		// Para operar con la fecha:
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaInicioSiguientePeriodo);

		while (!haTerminadoLaUnidadDeTiempo(unidades, cal))
			cal.add(Calendar.DATE, 1);

		return cal.getTime();
	} // inicializarUnidadDeTiempo ()

	/**
	 * Devuelve la fecha del siguiente inicio segun la unidad de tiempo y la
	 * cantidad
	 */
	private Date calcularInicioSiguiente(int unidades,
			Date fechaInicioAnterior, int duracion, Date fechaFin) {
		// empezando por la fecha inicio anterior
		Date fechaInicioSiguientePeriodo = (Date) fechaInicioAnterior.clone();

		// Para operar con la fecha:
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaInicioSiguientePeriodo);

		while (duracion > 0 && !cal.getTime().after(fechaFin)) {
			do {
				cal.add(Calendar.DATE, 1);
			} while (!haTerminadoLaUnidadDeTiempo(unidades, cal));

			duracion--;
		}

		return cal.getTime();
	}

	/**
	 * Comprueba si la unidad de tiempo del primer argumento termina el dia de
	 * la fecha del segundo argumento
	 */
	private boolean manyanaTerminaLaUnidadDeTiempo(int unidades,
			Date punteroFecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(punteroFecha);
		cal.add(Calendar.DATE, 1);

		return haTerminadoLaUnidadDeTiempo(unidades, cal);
	} // manyanaTerminaLaUnidadDeTiempo ()

	/**
	 * Comprueba si la unidad de tiempo del primer argumento termina el dia del
	 * calendario del segundo argumento
	 */
	private boolean haTerminadoLaUnidadDeTiempo(int unidades, Calendar cal) {
		switch (unidades) {
		case Calendar.WEEK_OF_YEAR:
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				return true;
			else
				return false;
		case CalendarioEfectivo.QUINCENA:
			if (cal.get(Calendar.DAY_OF_MONTH) == 1
					|| cal.get(Calendar.DAY_OF_MONTH) == 16)
				return true;
			else
				return false;
		case Calendar.MONTH:
			if (cal.get(Calendar.DAY_OF_MONTH) == 1)
				return true;
			else
				return false;
		default:
			return true;
		}
	} // haTerminadoLaUnidadDeTiempo ()

	/**
	 * Adelanta una 'fecha' dada en un 'incremento' de 'unidades'. 'unidades'
	 * puede ser una de las recogidas en java.util.Calendar (DATE, WEEK_OF_YEAR,
	 * MONTH, YEAR) o CalendarioEfectivo.QUINCENA
	 */
	private Date incrementar(Date fecha, int unidades, int incremento) {
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);

		if (unidades == Calendar.WEEK_OF_YEAR) {
			c.add(unidades, incremento);
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else if (unidades == Calendar.MONTH) {
			c.add(unidades, incremento);
			c.set(Calendar.DAY_OF_MONTH, 1);
		} else if (unidades == CalendarioEfectivo.QUINCENA
				&& c.get(Calendar.DAY_OF_MONTH) < 16 && incremento % 2 == 0) {
			c.add(Calendar.MONTH, incremento / 2);
			c.set(Calendar.DAY_OF_MONTH, 1);
		} else if (unidades == CalendarioEfectivo.QUINCENA
				&& c.get(Calendar.DAY_OF_MONTH) < 16 && incremento % 2 != 0) {
			c.add(Calendar.MONTH, incremento - (incremento / 2) - 1);
			c.set(Calendar.DAY_OF_MONTH, 16);
		} else if (unidades == CalendarioEfectivo.QUINCENA
				&& incremento % 2 == 0) {
			c.add(Calendar.MONTH, incremento / 2);
			c.set(Calendar.DAY_OF_MONTH, 16);
		} else if (unidades == CalendarioEfectivo.QUINCENA
				&& incremento % 2 != 0) {
			c.add(Calendar.MONTH, incremento - (incremento / 2));
			c.set(Calendar.DAY_OF_MONTH, 1);
		} else
			c.add(unidades, incremento);

		return c.getTime();
	} // incrementar ()

	/** Comprueba que una 'fecha' dada cumple las condiciones especificadas */
	public boolean cumple(Date fecha) {
		boolean estaEnSeleccion;
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);

		// Comprobacion del Tipo
		estaEnSeleccion = false;

		if (esFestivo(fecha)
				&& this.seleccionFestivos.contains(new Integer(c
						.get(Calendar.DAY_OF_WEEK))))
			estaEnSeleccion = true;

		if (!esFestivo(fecha)
				&& this.seleccionLaborables.contains(new Integer(c
						.get(Calendar.DAY_OF_WEEK))))
			estaEnSeleccion = true;

		return estaEnSeleccion;
	} // cumple ()

	/** Comprueba que una 'fecha' dada es dia festivo */
	private boolean esFestivo(Date fecha) {
		return (listaFestivos.contains(fecha) || esDomingo(fecha));
	} // esFestivo ()

	/** Comprueba que una 'fecha' dada es domingo */
	private boolean esDomingo(Date fecha) {
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		int diaSemana = c.get(Calendar.DAY_OF_WEEK);

		return ((diaSemana == Calendar.SUNDAY));
	} // esDomingo ()

	/** Devuelve el numero de dias entre la fechaInicio y la fechaFin */
	public long numeroDiasCalendario() {
		return ((fechaFin.getTime() - fechaInicio.getTime())
				/ (1000 * 60 * 60 * 24) + 1);
	} // numeroDiasCalendario ()

	/** Devuelve si existen mas periodos posibles */
	public boolean hasNext() {
		return (Punteroperiodo < listaPeriodos.size() - 1);
	} // hasNext ()

	/** Devuelve la lista de fechas efectivas de un periodo */
	public PeriodoEfectivo next() {
		Punteroperiodo++;
		return ((PeriodoEfectivo) listaPeriodos.get(Punteroperiodo));
	} // next()

}
