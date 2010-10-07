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

public class AdmInformeBean extends MasterBean {

	// Variables	
	private String idPlantilla, descripcion, alias, nombreFisico, directorio, idTipoInforme, visible, nombreSalida,
			preseleccionado, aSolicitantes, destinatarios, tipoformato;
	private Integer idInstitucion;
	static public final String TIPODESTINATARIO_CENPERSONA = "C";
	static public final String TIPODESTINATARIO_SCSPERSONAJG = "S";
	static public final String TIPODESTINATARIO_SCSJUZGADO = "J";
	static public final String TIPOFORMATO_WORD = "W";
	static public final String TIPOFORMATO_EXCEL = "X";
	
	// Nombre tabla
	static public String T_NOMBRETABLA = "ADM_INFORME";
	
	// Nombre campos de la tabla
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_ALIAS = "ALIAS";
	static public final String C_NOMBREFISICO = "NOBREFISICO";
	static public final String C_DIRECTORIO = "DIRECTORIO";
	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_VISIBLE = "VISIBLE";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_NOMBRESALIDA = "NOMBRESALIDA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_PRESELECCIONADO = "PRESELECCIONADO";
	static public final String C_ASOLICITANTES = "ASOLICITANTES";
	static public final String C_DESTINATARIOS = "DESTINATARIOS";
	static public final String C_TIPOFORMATO = "TIPOFORMATO";

	
	// Metodos SET
	public void setIdPlantilla(String valor) {this.idPlantilla = valor;}	
	public void setDescripcion(String valor) {this.descripcion = valor;}	
	public void setAlias(String valor) {this.alias = valor;}	
	public void setNombreFisico(String valor) {this.nombreFisico = valor;}	
	public void setDirectorio(String valor) {this.directorio = valor;}	
	public void setIdTipoInforme(String valor) {this.idTipoInforme = valor;}	
	public void setVisible(String valor) {this.visible = valor;}	
	public void setNombreSalida(String valor) {this.nombreSalida = valor;}	
	public void setIdInstitucion(Integer valor) {this.idInstitucion = valor;}	
	public void setPreseleccionado(String valor) {this.preseleccionado = valor;}
	public void setDestinatarios(String valor) {this.destinatarios = valor;}
	public void setASolicitantes(String solicitantes) {aSolicitantes = solicitantes;}	
	public void setTipoformato(String tipoformato) {this.tipoformato = tipoformato;}
	
	
	//Metodos GET
	public String getIdPlantilla() {return this.idPlantilla;}	
	public String getDescripcion() {return this.descripcion;}	
	public String getAlias() {return this.alias;}	
	public String getNombreFisico() {return this.nombreFisico;}	
	public String getDirectorio() {return this.directorio;}	
	public String getIdTipoInforme() {return this.idTipoInforme;}	
	public String getVisible() {return this.visible;}	
	public String getNombreSalida() {return this.nombreSalida;}	
	public String getPreseleccionado() {return this.preseleccionado;}	
	public String getDestinatarios() {return this.destinatarios;}
	public Integer getIdInstitucion() {return this.idInstitucion;}
	public String getASolicitantes() {return aSolicitantes;}
	public String getTipoformato() {return tipoformato;}
	
}
