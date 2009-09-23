/*
 * Created on 05-ene-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author RGG
 *
 * Clase que recoge y establece los valores del bean ADM_TIPOINFORME
 */


public class AdmTipoInformeBean extends MasterBean {

	/* Variables */	
	private String 	idTipoInforme, descripcion, idTipoInformePadre, idTipoFormato;
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "ADM_TIPOINFORME";
	
	/* Nombre campos de la tabla */
	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_DESCRIPCION    = "DESCRIPCION";
	static public final String C_IDTIPOINFORMEPADRE = "IDTIPOINFORMEPADRE";
	static public final String C_TIPOFORMATO = "TIPOFORMATO";


	
	// Metodos SET
	public void setIdTipoInforme(String idcontador) {this.idTipoInforme = idcontador;}	
	public void setDescripcion(String nombre) {this.descripcion= nombre;}
	public void setIdTipoInformePadre(String valor) {this.idTipoInformePadre = valor;}
	public void setTipoFormato(String valor) {this.idTipoFormato = valor;}
	
	
	//Metodos GET
	public String getIdTipoInforme() {return idTipoInforme;}	
	public String getDescripcion() {return descripcion;}
	public String getIdTipoInformePadre() {return idTipoInformePadre;}
	public String getTipoFormato() {return idTipoFormato;}
	
	
	
}

