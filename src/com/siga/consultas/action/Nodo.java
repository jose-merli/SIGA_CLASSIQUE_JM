
package com.siga.consultas.action;

import java.util.*;

import com.siga.general.SIGAException;

/*
 * Created on 11-abr-2005
 *
 */

/**
 * @author daniel.campos
 * Clase que representa un tabla de la B.D.
 */


public class Nodo
{
	private String sNombre;							// Tabla
	private String sPadre;							// Tabla padre
	private long distanciaOrigen;
	private Vector vArcos = null;
	private boolean visitado = false;

	/**
	 * Nodo
	 * Constructor
	 * @param nombre de la tabla
	 */
	public Nodo(String sNombre)
	{
	    this.sNombre = sNombre;
	    this.sPadre = "";
	    this.distanciaOrigen = 0;
	    this.vArcos = new Vector ();
	}
	
	/**
	 * Clone
	 * Crea una copia del objeto
	 * return el objeto copia
	 */
	public Object clone () {
		try {
			Nodo n = new Nodo("");
		    n.sNombre = this.sNombre;
		    n.sPadre = this.sPadre;
		    n.distanciaOrigen = this.distanciaOrigen;
		    n.visitado = this.visitado;
		    n.vArcos = new Vector ();
		    for (int i = 0; i < this.vArcos.size(); i++) {
		    	Arco a = (Arco)((Arco)this.vArcos.get(i)).clone();
		    	n.vArcos.add(a);
		    }
		    return n;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * getHijos
	 * Obtiene sus tablas relacionadas
	 * @return
	 * @throws SIGAException
	 */
	public Vector getHijos () throws SIGAException {
		try {
			Vector hijos = new Vector();
			
			for (int i = 0 ; i < this.getArcos().size(); i++) {
				Arco a = (Arco) this.getArcos().get(i);
				Nodo n = new Nodo (a.getNodoDestino());
				n.setPadre(this.getNombre());

				Hashtable h = new Hashtable ();
				h.put("NODO", n);
				h.put("PESO", new Long(a.getPeso()));
				
				hijos.add(h);
			}
			return hijos;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al obtener las tablas asociada");
		}
	}

	public String getNombre () {
		return this.sNombre;
	}
	
	public Vector getArcos () {
		return (Vector)this.vArcos;
	}

	public void setArcos (Vector a) {
		this.vArcos = a;
	}

	public void insertarArco (Arco a) {
		this.vArcos.add(a);
	}
	
	public String getPadre() {
		return sPadre;
	}

	public void setPadre(String padre) {
		this.sPadre = padre;
	}
	
	public long getDistanciaOrigen() {
		return this.distanciaOrigen;
	}

	public void setDistanciaOrigen(long distanciaOrigen) {
		this.distanciaOrigen = distanciaOrigen;
	}
	public boolean isVisitado() {
		return this.visitado;
	}
	
	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}
}