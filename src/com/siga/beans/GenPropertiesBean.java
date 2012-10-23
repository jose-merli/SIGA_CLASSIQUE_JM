/*
 * Created on 20-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import  com.atos.utils.ClsConstants;

/**
 * @author jose.barrientos
 *
 */
public class GenPropertiesBean extends MasterBean {
	
	/* Variables */
	private String 	fichero, parametro, valor;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "GEN_PROPERTIES";

	/* Nombre campos de la tabla */
	static public final String C_FICHERO		= "FICHERO";
	static public final String C_PARAMETRO		= "PARAMETRO";
	static public final String C_VALOR			= "VALOR";
	
	
	/**
	 * @return Returns the fichero.
	 */
	public String getFichero() {
		return fichero;
	}
	/**
	 * @param fichero The fichero to set.
	 */
	public void setFichero(String fichero) {
		this.fichero = fichero;
	}
	/**
	 * @return Returns the parametro.
	 */
	public String getParametro() {
		return parametro;
	}
	/**
	 * @param parametro The parametro to set.
	 */
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
