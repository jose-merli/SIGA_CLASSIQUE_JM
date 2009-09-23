/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

import java.util.ArrayList;

public class EnvDestProgramInformesBean extends EnvProgramInformesBean {
    
	//Variables
    
private Long idPersona;

private Integer idInstitucionPersona;
ArrayList clavesDestinatario;
	
	static public final String T_NOMBRETABLA = "ENV_DESTPROGRAMINFORMES";
	static public final String C_IDPERSONA = "IDPERSONA";
	//static public final String C_CLAVES = "CLAVES";
	static public final String C_IDINSTITUCION_PERSONA = "IDINSTITUCION_PERSONA";
	
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdInstitucionPersona() {
		return idInstitucionPersona;
	}
	public void setIdInstitucionPersona(Integer idInstitucionPersona) {
		this.idInstitucionPersona = idInstitucionPersona;
	}
	public ArrayList getClavesDestinatario() {
		return clavesDestinatario;
	}
	public void setClavesDestinatario(ArrayList clavesDestinatario) {
		this.clavesDestinatario = clavesDestinatario;
	}
	
	
	

}
