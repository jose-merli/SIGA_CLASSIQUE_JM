/*
 * Created on Mar 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.util.calendarioSJCS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author A203486
 *
 * Esta clase mantiene la estructura de datos calculada en las iteraciones del algoritmo que calcula el calendario de guardias.
 * 
 */
public class DatosIteracionCalendarioSJCS {

	//Atributo error:
	//		- 0: Todo Correcto
	//		- 1: NO Hay Suficientes Letrados
	//		- 2: Hay Incompatibilidad de Guardias	
	//		- 3: NO hay Separacion suficiente
	//		- 4: Error al provocarse una excepcion en el desarrollo del metodo.
	private int error;
	
	//Puntero que apunta al siguiente letrado:
	private int punteroListaLetrados;
	
	//Letrado seleccionado en la iteracion
	private LetradoInscripcion letradoSeleccionado;
	
	//Lista de letrados principal calculada a partir del PL:
	private ArrayList arrayListaLetrados;
	
	//Lista de letrados pendientes:
	private ArrayList arrayListaLetradosPendientes;
	
	private List<LetradoInscripcion> compensacionesBajaTemporal;

	public DatosIteracionCalendarioSJCS() {
		this.error = 0;
		this.punteroListaLetrados = 0;
		this.letradoSeleccionado = null;
		this.arrayListaLetrados = null;
		this.arrayListaLetradosPendientes = null;
	}
	
	
	
	/**
	 * @return Returns the arrayListaLetrados.
	 */
	public ArrayList getArrayListaLetrados() {
		return arrayListaLetrados;
	}
	/**
	 * @param arrayListaLetrados The arrayListaLetrados to set.
	 */
	public void setArrayListaLetrados(ArrayList arrayListaLetrados) {
		this.arrayListaLetrados = arrayListaLetrados;
	}
	/**
	 * @return Returns the arrayListaLetradosPendientes.
	 */
	public ArrayList getArrayListaLetradosPendientes() {
		return arrayListaLetradosPendientes;
	}
	/**
	 * @param arrayListaLetradosPendientes The arrayListaLetradosPendientes to set.
	 */
	public void setArrayListaLetradosPendientes(
			ArrayList arrayListaLetradosPendientes) {
		this.arrayListaLetradosPendientes = arrayListaLetradosPendientes;
	}
	/**
	 * @return Returns the letradoSeleccionado.
	 */
	public LetradoInscripcion getLetradoSeleccionado() {
		return letradoSeleccionado;
	}
	/**
	 * @param letradoSeleccionado The letradoSeleccionado to set.
	 */
	public void setLetradoSeleccionado(LetradoInscripcion letradoSeleccionado) {
		this.letradoSeleccionado = letradoSeleccionado;
	}
	/**
	 * @return Returns the punteroListaLetrados.
	 */
	public int getPunteroListaLetrados() {
		return punteroListaLetrados;
	}
	/**
	 * @param punteroListaLetrados The punteroListaLetrados to set.
	 */
	public void setPunteroListaLetrados(int punteroListaLetrados) {
		this.punteroListaLetrados = punteroListaLetrados;
	}
	
	/**
	 * @return Returns the error.
	 */
	public int getError() {
		return error;
	}
	/**
	 * @param error The error to set.
	 */
	public void setError(int error) {
		this.error = error;
	}



	public List<LetradoInscripcion> getCompensacionesBajaTemporal() {
		return compensacionesBajaTemporal;
	}



	public void setCompensacionesBajaTemporal(List<LetradoInscripcion> compensacionesBajaTemporal) {
		this.compensacionesBajaTemporal = compensacionesBajaTemporal;
	}



	
	


	


	
}