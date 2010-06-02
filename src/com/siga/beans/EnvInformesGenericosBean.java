/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvInformesGenericosBean extends EnvProgramInformesBean {
    
	//Variables
    

private String idPlantilla;

	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	
	static public final String T_NOMBRETABLA = "ENV_INFORMESGENERICOS";
	

	public String getIdPlantilla() {
		return idPlantilla;
	}

	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

}
