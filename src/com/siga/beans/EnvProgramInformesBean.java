/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

import java.util.ArrayList;

public class EnvProgramInformesBean extends EnvProgramBean {
    
	//Variables
    
private String claves;
private String plantillas;

private String idTipoInforme;
private ArrayList destinatarios;
private ArrayList plantillasInformes;



	static public final String C_PLANTILLAS = "PLANTILLAS";
	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_CLAVES = "CLAVES";
	static public final String T_NOMBRETABLA = "ENV_PROGRAMINFORMES";
	
	static public final String SEQ_ENV_PROGRAMINFORMES = "SEQ_ENV_PROGRAMINFORMES";
	
	public String getClaves() {
		return claves;
	}

	public void setClaves(String claves) {
		this.claves = claves;
	}

	public String getIdTipoInforme() {
		return idTipoInforme;
	}

	public void setIdTipoInforme(String idTipoInforme) {
		this.idTipoInforme = idTipoInforme;
	}

	public String getPlantillas() {
		return plantillas;
	}

	public void setPlantillas(String plantillas) {
		this.plantillas = plantillas;
	}

	public ArrayList getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(ArrayList destinatarios) {
		this.destinatarios = destinatarios;
	}

	public ArrayList getPlantillasInformes() {
		return plantillasInformes;
	}

	public void setPlantillasInformes(ArrayList plantillasInformes) {
		this.plantillasInformes = plantillasInformes;
	}


}
