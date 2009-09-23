/*
 * FechaEfectiva.java
 *
 * Created on 6 de febrero de 2006, 14:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.siga.gratuita.util.calendarioSJCS.Entity;

//Clases para el calendario
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author adrianag
 */
public class FechaEfectiva implements Comparable {
    private Date fecha;
    private int tipoCalendario;
    
    /**
     * Creates a new instance of FechaEfectiva
     */
    public FechaEfectiva(Date fecha, int tipoCalendario) {
        this.fecha = fecha;
        this.tipoCalendario = tipoCalendario;
    }
    
    public String getFechaString() {
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
        return datef.format(fecha);
    }
    
    /** Metodo para sobreescribir de la interfaz Comparable.
     * Permite que se inserten las Fechas Efectivas en Arboles
     */
    public int compareTo(FechaEfectiva fecha) {
        if (this.fecha.before(fecha.fecha))
            return -1;
        else if (this.fecha.after(fecha.fecha))
            return +1;
        else
            return 0;
    }

	/* 
	 * Este metodo lo implementamos ya que usamos el J2SE 1.4 en vez del 1.5
	 */
	public int compareTo(Object f) {
		int salida = 0;
		FechaEfectiva fechaEfectiva = null;
		
		try {
			fechaEfectiva = (FechaEfectiva)f;
			salida = this.fecha.compareTo(fechaEfectiva.fecha);	
		} catch (Exception e) {
			salida = 1;
		}		
		return salida;
	}
    
}
