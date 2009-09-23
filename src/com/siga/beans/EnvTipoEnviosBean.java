/*
 * Created on Mar 15, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvTipoEnviosBean extends MasterBean {
    
	//Variables
    private String nombre;
	private Integer idTipoEnvios;
	
	// Nombre campos de la tabla 
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";	
	
	static public final String T_NOMBRETABLA = "ENV_TIPOENVIOS";	

    public Integer getIdTipoEnvios() {
        return idTipoEnvios;
    }
    public void setIdTipoEnvios(Integer idTipoEnvios) {
        this.idTipoEnvios = idTipoEnvios;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
