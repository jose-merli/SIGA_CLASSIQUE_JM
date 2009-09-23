
package com.siga.consultas.action;

import java.util.Vector;

import com.siga.general.SIGAException;

/*
 * Created on 11-abr-2005
 *
 */

/**
 * @author daniel.campos
 * Clase que implementa una cola
 *
 */

public class Cola {
	
	Vector elementos = null;
	
	/**
	 * Cola
	 * Constructor que crea una cola con los datos que se le pasan
	 * @param elementos que formaran la cola
	 * @param primer elemento de la cola
	 */
	public Cola (Vector nodos, Nodo origen) {
		try {
			boolean flag = true;
			Vector vCola = new Vector ();
			for (int i = 0; i < nodos.size(); i++) {
				Nodo n = (Nodo)((Nodo)nodos.get(i)).clone();
				n.setDistanciaOrigen(Long.MAX_VALUE);
				if (flag && n.getNombre().equalsIgnoreCase(origen.getNombre())) { 
					n.setDistanciaOrigen(0);
					flag = false;
				}
				vCola.add(n);
			}
			this.elementos = vCola;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * extraerMinimoElemento
	 * Saca de la cola el elemento minimo
	 * @return elemento minimo
	 * @throws SIGAException
	 */
	protected  Nodo extraerMinimoElemento () throws SIGAException {
		try {
			int min = -1;
			long distancia = Long.MAX_VALUE;
			for (int i = 0; i < this.elementos.size(); i++) {
				Nodo n = (Nodo) this.elementos.get(i);
				if (n.getDistanciaOrigen() < distancia) {
					distancia = n.getDistanciaOrigen();
					min = i;
				}
			}
			if (min >= 0) {
				return (Nodo) this.elementos.remove(min);
			}
			return null;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al obtener el elemento minimo");
		}
	}

	/**
	 * actualizarNodo
	 * Actuliza los datos del elemento de la cola
	 * @param nodo a actualizar
	 * @param padre del nodo
	 * @param nuvo peso
	 * @return true si ok, false si error
	 * @throws SIGAException
	 */
	protected boolean actualizarNodo (Nodo nodo, Nodo padre, long peso) throws SIGAException {
		try {
			for (int i = 0; i < this.elementos.size(); i++) {
				Nodo n = (Nodo) this.elementos.get(i);
				if (n.getNombre().equalsIgnoreCase(nodo.getNombre())) {
					if (n.getDistanciaOrigen() > padre.getDistanciaOrigen() + peso) {
						this.elementos.remove(n);
						n.setPadre(padre.getNombre());
						n.setDistanciaOrigen(padre.getDistanciaOrigen() + peso);
						this.elementos.add(n);
					}
					return true;
				}
			}
			return true;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al actualizar en el elemento");
		}
	}
}
