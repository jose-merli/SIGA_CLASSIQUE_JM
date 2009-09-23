/*
 * CalendarioEfectivo.java
 *
 * Creado: 6 de febrero de 2006
 * Modificado: 7 de mayo de 2007
 */

package com.siga.gratuita.util.calendarioSJCS.Entity;


//Clases de manejo de fechas
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//Clases de manejo de conjuntos y listas
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;


/**
 * @author AAG
 */
public class CalendarioEfectivo
{
    //ATRIBUTOS DE CLASE
    /** Tipo de Unidad que representa los días desde el 1 hasta el 15, y desde
     * el 16 hasta el último día de mes
     */
    public final static int QUINCENA = 1524;
    
    
    //ATRIBUTOS
    /* Atributos de Entrada */
    private Date fechaInicio;
    private Date fechaFin;
    private int duracion;
    private int unidadesDuracion;
    private int periodo;
    private int unidadesPeriodo;
    
    private TreeSet seleccionLaborables = null;
    private TreeSet seleccionFestivos = null;
    private TreeSet listaFestivos = null;
    
    /* Atributos de Salida */
    private ArrayList listaPeriodos;
    private int Punteroperiodo;
    
    
    //CONSTRUCTORES
    /** Crea una nueva instancia de CalendarioEfectivo */
    public CalendarioEfectivo (String fechaInicio, String fechaFin, 
                              int duracion, int unidadesDuracion, 
                              int periodo, int unidadesPeriodo, 
                              Vector seleccionLaborables, 
                              Vector seleccionFestivos, Vector listaFestivos)
    {
        Iterator iter = null;
        SimpleDateFormat datef = new SimpleDateFormat ("dd/MM/yyyy");
        
        //Carga de datos de Entrada
        try {
            //Carga de rango de generacion del calendario
            this.fechaInicio = datef.parse (fechaInicio);
            this.fechaFin = datef.parse (fechaFin);
            
            //Carga de duracion y periodo
            this.duracion = duracion;
            this.unidadesDuracion = unidadesDuracion;
            this.periodo = periodo;
            this.unidadesPeriodo = unidadesPeriodo;
            
            //Carga de Dias Laborables
            this.seleccionLaborables = new TreeSet ();
            iter = seleccionLaborables.iterator ();
            while (iter.hasNext ())
                this.seleccionLaborables.add (((Integer) iter.next ()));
            
            //Carga de Dias Festivos
            this.seleccionFestivos = new TreeSet ();
            iter = seleccionFestivos.iterator ();
            while (iter.hasNext ())
                this.seleccionFestivos.add (((Integer) iter.next ()));
            
            //Carga de Festivos
            this.listaFestivos = new TreeSet ();
            iter = listaFestivos.iterator ();
            while (iter.hasNext ())
                this.listaFestivos.add (datef.parse ((String) iter.next ()));
        }
        catch (java.text.ParseException pe) {
            pe.printStackTrace ();
        }
        
        //Generacion de las fechas efectivas
        if (periodo == 0)
            generarCalendarioEfectivoSinPeriodo ();
        else
            generarCalendarioEfectivo ();
        
        //Inicializacion para la obtencion de periodos de forma secuencial
        Punteroperiodo = -1;
    } //CalendarioEfectivo
    
    
    //GETTERS
    public ArrayList getListaPeriodos () {
        return listaPeriodos;
    } //getListaPeriodos ()
    
    
    //METODOS
    /** Genera el Calendario Efectivo si se aplica periodo */
    private void generarCalendarioEfectivo ()
    {
        Date fechaInicioSiguientePeriodo;
        Date punteroFecha;
        PeriodoEfectivo periodoActual;
        int tiempoPendiente;
        
        //INICIO
        listaPeriodos = new ArrayList ();
        
        fechaInicioSiguientePeriodo = inicializarUnidadDeTiempo 
                (unidadesPeriodo, fechaInicio, fechaFin);
        fechaInicioSiguientePeriodo = inicializarUnidadDeTiempo 
                (unidadesDuracion, fechaInicioSiguientePeriodo, fechaFin);
        
        //Se deja el puntero al principio del periodo a tratar
        punteroFecha = (Date) fechaInicioSiguientePeriodo.clone ();
        
        while (!fechaInicioSiguientePeriodo.after (fechaFin)) //fechaFin incluido
        {
            //Inicio de la generacion de un nuevo periodo
            periodoActual = new PeriodoEfectivo ();
            
            //Se deja el puntero al principio del periodo a tratar
            punteroFecha = (Date) fechaInicioSiguientePeriodo.clone ();
            
            //Se apunta cuando comenzara el siguiente periodo
            fechaInicioSiguientePeriodo = incrementar 
                    (fechaInicioSiguientePeriodo, unidadesPeriodo, periodo);
            fechaInicioSiguientePeriodo = inicializarUnidadDeTiempo 
                    (unidadesDuracion, fechaInicioSiguientePeriodo, fechaFin);
            
            //Tiempo de duracion pendiente
            tiempoPendiente = duracion;
            
            //Generacion de Fechas Efectivas
            while ((tiempoPendiente > 0) &&
                   ((!punteroFecha.after (fechaFin) &&
                    punteroFecha.before (fechaInicioSiguientePeriodo)) ||
                    unidadesPeriodo != Calendar.DAY_OF_YEAR))
            {
                if (cumple (punteroFecha)) {
                    //Adicion de la fecha efectiva al periodo actual
                    periodoActual.add ((Date) punteroFecha.clone ());
                    
                    //Disminucion de la duracion pendiente
                    if (unidadesDuracion == Calendar.DAY_OF_YEAR)
                        tiempoPendiente--;
                }
                
                //Disminucion de la duracion pendiente
                if (manyanaTerminaLaUnidadDeTiempo 
                        (unidadesDuracion, punteroFecha))
                {
                    tiempoPendiente--;
                    if (punteroFecha.after (fechaFin))
                        break;
                    else {
                        punteroFecha = incrementar 
                                (punteroFecha, Calendar.DATE, 1);
                        if (punteroFecha.after (fechaFin))
                            break;
                    }
                }
                else               
                    //Avance del puntero hasta la siguiente fecha
                    punteroFecha = incrementar (punteroFecha, Calendar.DATE, 1);
                
                if (!punteroFecha.before (fechaInicioSiguientePeriodo))
                    break;
            } //while
            
            // Adicion del periodo generado a la lista de periodos con
            //post-procesamiento comprobando que se completa el periodo
            anyadirPeriodoAListaDePeriodos 
                    (punteroFecha, periodoActual, tiempoPendiente, fechaFin);
            
        } //while
    } //generarCalendarioEfectivo ()
    
    /** Genera el Calendario Efectivo si no se aplica periodo */
    private void generarCalendarioEfectivoSinPeriodo ()
    {
        Date punteroFecha;
        PeriodoEfectivo periodoActual;
        int tiempoPendiente;
        
        //INICIO
        listaPeriodos = new ArrayList ();
        
        //Se deja el puntero al principio del periodo a tratar
        punteroFecha = (Date) fechaInicio.clone ();
        punteroFecha = inicializarUnidadDeTiempo 
                (unidadesDuracion, punteroFecha, fechaFin);
        
        while (!punteroFecha.after (fechaFin)) //fechaFin incluido
        {
            //Inicio de la generacion de un nuevo periodo
            periodoActual = new PeriodoEfectivo ();
            
            //Tiempo de duracion pendiente
            tiempoPendiente = duracion;
            
            //Generacion de Fechas Efectivas
            while ((tiempoPendiente > 0) &&
                   (!punteroFecha.after (fechaFin) ||
                    unidadesDuracion != Calendar.DAY_OF_YEAR))
            {
                if (cumple (punteroFecha)) {
                    //Adicion de la fecha efectiva al periodo actual
                    periodoActual.add ((Date) punteroFecha.clone ());
                    
                    //Disminucion de la duracion pendiente
                    if (unidadesDuracion == Calendar.DAY_OF_YEAR)
                        tiempoPendiente--;
                }
                
                //Disminucion de la duracion pendiente
                if (manyanaTerminaLaUnidadDeTiempo 
                        (unidadesDuracion, punteroFecha))
                {
                    tiempoPendiente--;
                    if (punteroFecha.after (fechaFin))
                        break;
                    else {
                        punteroFecha = incrementar 
                                (punteroFecha, Calendar.DATE, 1);
                        if (punteroFecha.after (fechaFin))
                            break;
                    }
                }
                else
                    //Avance del puntero hasta la siguiente fecha
                    punteroFecha = incrementar (punteroFecha, Calendar.DATE, 1);
            } //while
            
            // Adicion del periodo generado a la lista de periodos con
            //post-procesamiento comprobando que se completa el periodo
            anyadirPeriodoAListaDePeriodos 
                    (punteroFecha, periodoActual, tiempoPendiente, fechaFin);
        } //while
    } //generarCalendarioEfectivoSinPeriodo ()
    
    /** Devuelve la fecha en la que debe comenzar segun la unidad de tiempo */
    private Date inicializarUnidadDeTiempo
            (int unidades, Date fechaInicio, Date fechaFin)
    {
        Date fechaInicioSiguientePeriodo = (Date) fechaInicio.clone ();
        
        //Para operar con la fecha:
        Calendar cal = Calendar.getInstance ();
        cal.setTime (fechaInicioSiguientePeriodo);
        
        if (unidades != Calendar.DAY_OF_YEAR)
            while (! haTerminadoLaUnidadDeTiempo (unidades, cal))
                cal.add (Calendar.DATE, 1);
        
        return cal.getTime ();
    } //inicializarUnidadDeTiempo ()
    
    /** Adelanta una 'fecha' dada en un 'incremento' de 'unidades'.
     * 'unidades' puede ser una de las recogidas en java.util.Calendar
     * (DATE, WEEK_OF_YEAR, MONTH, YEAR) o CalendarioEfectivo.QUINCENA
     */
    private Date incrementar (Date fecha, int unidades, int incremento) {
        Calendar c = Calendar.getInstance ();
        c.setTime (fecha);
        
        if (unidades == Calendar.WEEK_OF_YEAR) {
            c.add (unidades, incremento);
            c.set (Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        else if (unidades == Calendar.MONTH) {
            c.add (unidades, incremento);
            c.set (Calendar.DAY_OF_MONTH, 1);
        }
        else if (unidades == CalendarioEfectivo.QUINCENA && 
                c.get (Calendar.DAY_OF_MONTH) < 16 &&
                incremento % 2 == 0) {
            c.add (Calendar.MONTH, incremento/2);
            c.set (Calendar.DAY_OF_MONTH, 1);
        }
        else if (unidades == CalendarioEfectivo.QUINCENA && 
                c.get (Calendar.DAY_OF_MONTH) < 16 &&
                incremento % 2 != 0) {
            c.add (Calendar.MONTH, incremento - (incremento/2) -1);
            c.set (Calendar.DAY_OF_MONTH, 16);
        }
        else if (unidades == CalendarioEfectivo.QUINCENA &&
                incremento % 2 == 0) {
            c.add (Calendar.MONTH, incremento/2);
            c.set (Calendar.DAY_OF_MONTH, 16);
        }
        else if (unidades == CalendarioEfectivo.QUINCENA &&
                incremento % 2 != 0) {
            c.add (Calendar.MONTH, incremento - (incremento/2));
            c.set (Calendar.DAY_OF_MONTH, 1);
        }
        else
            c.add (unidades, incremento);
        
        return c.getTime ();
    } //incrementar ()
    
    /** Comprueba si la unidad de tiempo del primer argumento
     * termina el dia de la fecha del segundo argumento
     */
    private boolean manyanaTerminaLaUnidadDeTiempo 
            (int unidades, Date punteroFecha)
    {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (punteroFecha);
        cal.add (Calendar.DATE, 1);
        
        return haTerminadoLaUnidadDeTiempo (unidades, cal);
    } //manyanaTerminaLaUnidadDeTiempo ()
    
    /** Comprueba si la unidad de tiempo del primer argumento
     * termina el dia del calendario del segundo argumento
     */
    private boolean haTerminadoLaUnidadDeTiempo (int unidades, Calendar cal)
    {
        if (unidades == Calendar.WEEK_OF_YEAR &&
            cal.get (Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
                return true;
        else if (unidades == CalendarioEfectivo.QUINCENA &&
            (cal.get (Calendar.DAY_OF_MONTH) == 1 ||
             cal.get (Calendar.DAY_OF_MONTH) == 16))
                return true;
        else if (unidades == Calendar.MONTH &&
            cal.get (Calendar.DAY_OF_MONTH) == 1)
                return true;
        
        return false;
    } //haTerminadoLaUnidadDeTiempo ()
    
    /** Anyade el Periodo actual a la lista si se ha completado el periodo */
    private void anyadirPeriodoAListaDePeriodos (Date punteroFecha,
            PeriodoEfectivo periodoActual, int tiempoPendiente, Date fechaFin)
    {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (punteroFecha);
        
        if (tiempoPendiente <= 0 || punteroFecha.after (fechaFin)) {
            listaPeriodos.add (periodoActual);
            return;
        }
        if (unidadesPeriodo == Calendar.DAY_OF_YEAR) {
            listaPeriodos.add (periodoActual);
            return;
        }
        
        if (haTerminadoLaUnidadDeTiempo (unidadesPeriodo, cal))
            listaPeriodos.add (periodoActual);
        else
            ; //No se anyade el periodo actual por no haberse completado
    } //anyadirPeriodoAListaDePeriodos ()
    
    /** Comprueba que una 'fecha' dada cumple las condiciones especificadas */
    public boolean cumple (Date fecha) {
        boolean estaEnSeleccion;
        Calendar c = Calendar.getInstance ();
        c.setTime (fecha);
        
        //Comprobacion del Tipo
        estaEnSeleccion = false;
        
        if (esFestivo (fecha) && this.seleccionFestivos.contains
                (new Integer (c.get (Calendar.DAY_OF_WEEK))))
            estaEnSeleccion = true;
        
        if (! esFestivo (fecha) && this.seleccionLaborables.contains
                (new Integer (c.get (Calendar.DAY_OF_WEEK))))
            estaEnSeleccion = true;
        
        return estaEnSeleccion;
    } //cumple ()
    
    /** Comprueba que una 'fecha' dada es dia festivo */
    private boolean esFestivo (Date fecha) {
        return (listaFestivos.contains (fecha) || esDomingo (fecha));
    } //esFestivo ()
    
    /** Comprueba que una 'fecha' dada es domingo */
    private boolean esDomingo (Date fecha) {
        Calendar c = Calendar.getInstance ();
        c.setTime (fecha);
        int diaSemana = c.get (Calendar.DAY_OF_WEEK);
        
        return ((diaSemana == Calendar.SUNDAY));
    } //esDomingo ()
    
    
    /** Devuelve el numero de dias entre la fechaInicio y la fechaFin */
    public long numeroDiasCalendario () {
        return ( (fechaFin.getTime () - fechaInicio.getTime ()) /
                (1000 * 60 * 60 * 24) + 1);
    } //numeroDiasCalendario ()
    
    /** Devuelve si existen mas periodos posibles */
    public boolean hasNext () {
        return (Punteroperiodo < listaPeriodos.size () -1);
    } //hasNext ()
    
    /** Devuelve la lista de fechas efectivas de un periodo */
    public PeriodoEfectivo next () {
        Punteroperiodo++;
        return ((PeriodoEfectivo) listaPeriodos.get (Punteroperiodo));
    } //next()
    
}
