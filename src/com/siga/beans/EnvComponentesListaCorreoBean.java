/*
 * Created on Dec 27, 2004
 * @author jmgrau
 */
package com.siga.beans;


public class EnvComponentesListaCorreoBean extends MasterBean {
    
	//Variables
	private Integer idListaCorreo;
	private Integer idInstitucion;
	private Integer idPersona;	
	
	// Nombre campos de la tabla 
	static public final String C_IDLISTACORREO = "IDLISTACORREO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA="IDPERSONA";
	
	static public final String T_NOMBRETABLA = "ENV_COMPONENTESLISTACORREO";	

    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdListaCorreo() {
        return idListaCorreo;
    }
    public void setIdListaCorreo(Integer idListaCorreo) {
        this.idListaCorreo = idListaCorreo;
    }
    public Integer getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }
}
