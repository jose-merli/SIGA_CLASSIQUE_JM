/*
 * Created on Mar 15, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvEstadoEnvioBean extends MasterBean {
    
	//Variables
    private String nombre;
	private Integer idEstado;
	
	// Nombre campos de la tabla 
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDESTADO = "IDESTADO";	
	
	static public final String T_NOMBRETABLA = "ENV_ESTADOENVIO";	

    public Integer getIdEstado() {
        return idEstado;
    }
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
