/*
 * PeriodoEfectivo.java
 *
 * Creado: 6 de febrero de 2006
 * Modificado: 7 de mayo de 2007
 */

package com.siga.gratuita.util.calendarioSJCS.Entity;


//Clases de manejo de fechas
import java.util.Date;

//Clases de Colecciones
import java.util.TreeSet;
import java.util.Vector;
import java.util.Iterator;


/**
 * @author AAG
 */
public class PeriodoEfectivo
{
    //ATRIBUTOS
    private TreeSet ConjuntoDias;
    
    
    //CONSTRUCTORES
    /** Crea una nueva instancia de PeriodoEfectivo */
    public PeriodoEfectivo ()
    {
        ConjuntoDias = new TreeSet();
    }
    /** Crea una nueva instancia de PeriodoEfectivo */
    public PeriodoEfectivo (TreeSet ConjuntoDias)
    {
        this.ConjuntoDias = ConjuntoDias;
    }
    
    
    //GETTERS
    public boolean contains (Date fecha) {
        return (ConjuntoDias.contains (fecha));
    }
    
    public Iterator iterator () {
        return ConjuntoDias.iterator ();
    }
    
    
    //SETTERS
    public void clear () {
        ConjuntoDias.clear ();
    }
    
    public void add (Date fecha) {
        ConjuntoDias.add (fecha);
    }
    
    
    //METODOS
    /** Indica si este PeriodoEfectivo coincide con otro */
    public boolean equals (Object o) {
        if (! (o instanceof PeriodoEfectivo))
            return false;
        
        Vector v1, v2;
        v1 = new Vector (); v1.addAll (this.ConjuntoDias);
        v2 = new Vector (); v2.addAll (((PeriodoEfectivo) o).ConjuntoDias);
        
        return v1.equals (v2);
    } //equals ()
    
}
