package com.siga.beans;

public class GenPeriodoBean extends MasterBean
{
	// Campos 
	private Integer	idPeriodo;
	private String	nombre;
	private Integer	diaInicio;
	private Integer	mesInicio;
	private Integer	anioInicio;
	private Integer	diaFin;
	private Integer	mesFin;
	private Integer	anioFin;

	// Nombre de Tabla
	static public String T_NOMBRETABLA = "GEN_PERIODO";
	
	// Nombre de los campos
	static public final String C_IDPERIODO = "IDPERIODO";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_DIAINICIO = "DIAINICIO";
	static public final String C_MESINICIO = "MESINICIO";
	static public final String C_ANIOINICIO = "ANIOINICIO";
	static public final String C_DIAFIN = "DIAFIN";
	static public final String C_MESFIN = "MESFIN";
	static public final String C_ANIOFIN = "ANIOFIN";

	// GETTERS
	public Integer getIdPeriodo() {return idPeriodo;}
	public String getNombre() {return nombre;}
	public Integer getDiaInicio() {return diaInicio;}
	public Integer getMesInicio() {return mesInicio;}
	public Integer getAnioInicio() {return anioInicio;}
	public Integer getDiaFin() {return diaFin;}
	public Integer getMesFin() {return mesFin;}
	public Integer getAnioFin() {return anioFin;}
	
	// SETTERS
	public void setIdPeriodo(Integer idPeriodo) {this.idPeriodo = idPeriodo;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setDiaInicio(Integer diaInicio) {this.diaInicio = diaInicio;}
	public void setMesInicio(Integer mesInicio) {this.mesInicio = mesInicio;}
	public void setAnioInicio(Integer anioInicio) {this.anioInicio = anioInicio;}
	public void setDiaFin(Integer diaFin) {this.diaFin = diaFin;}
	public void setMesFin(Integer mesFin) {this.mesFin = mesFin;}
	public void setAnioFin(Integer anioFin) {this.anioFin = anioFin;}
	
}
