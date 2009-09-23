/*
 * Created on Mar 29, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvListaCorreosEnviosBean extends MasterBean {
    
	//Variables
	private Integer idListaCorreo;
	private Integer idEnvio;
	private Integer idInstitucion;
	
	// Nombre campos de la tabla 
	static public final String C_IDLISTACORREO = "IDLISTACORREO";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	
	static public final String T_NOMBRETABLA = "ENV_LISTACORREOSENVIOS";	

    public Integer getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }
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
}
