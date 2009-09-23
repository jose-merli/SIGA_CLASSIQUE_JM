/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvProgramIRPFBean extends EnvProgramBean {
    
	//Variables
    
private Integer anyoIRPF;
private String plantillas;
private Long idPersona;
	
	
	
	static public final String C_ANIOIRPF = "ANIOIRPF";
	static public final String C_PLANTILLAS = "PLANTILLAS";
	static public final String T_NOMBRETABLA = "ENV_PROGRAMIRPF";
	static public final String C_IDPERSONA = "IDPERSONA";
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getAnyoIRPF() {
		return anyoIRPF;
	}

	public void setAnyoIRPF(Integer anyoIRPF) {
		this.anyoIRPF = anyoIRPF;
	}

	public String getPlantillas() {
		return plantillas;
	}

	public void setPlantillas(String plantillas) {
		this.plantillas = plantillas;
	}


}
