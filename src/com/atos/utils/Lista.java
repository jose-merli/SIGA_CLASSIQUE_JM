/*
 * Created on 23-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.atos.utils;

import java.io.Serializable;

/**
 * @author Jose.Zulueta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Lista implements Serializable{
	private String funcion="";
	private String automatico="off";
	

	/**
	 * @return Returns the automatico.
	 */
	public String getAutomatico() {
		return automatico;
	}
	/**
	 * @param automatico The automatico to set.
	 */
	public void setAutomatico(String automatico) {
		this.automatico = automatico;
	}
	/**
	 * @return Returns the funcion.
	 */
	public String getFuncion() {
		return funcion;
	}
	/**
	 * @param funcion The funcion to set.
	 */
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
}
