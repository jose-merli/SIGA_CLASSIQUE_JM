/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvProgramIRPFBean extends EnvProgramBean
{
	// Nombres de los campos
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_PERIODO = "PERIODO";
	static public final String C_ANIOIRPF = "ANIOIRPF";
	static public final String C_PLANTILLAS = "PLANTILLAS";
	static public final String T_NOMBRETABLA = "ENV_PROGRAMIRPF";

	
	// Atributos
	private Long idPersona;
	private Integer periodo;
	private Integer anyoIRPF;
	private String plantillas;
	
	
	// GETTERS
	public Long getIdPersona() { return idPersona; }
	public Integer getPeriodo() { return periodo; }
	public Integer getAnyoIRPF() { return anyoIRPF; }
	public String getPlantillas() { return plantillas; }
	
	
	// SETTERS
	public void setIdPersona(Long idPersona) { this.idPersona = idPersona; }
	public void setPeriodo(Integer periodo) { this.periodo = periodo; }
	public void setAnyoIRPF(Integer anyoIRPF) { this.anyoIRPF = anyoIRPF; }
	public void setPlantillas(String plantillas) { this.plantillas = plantillas; }

}
