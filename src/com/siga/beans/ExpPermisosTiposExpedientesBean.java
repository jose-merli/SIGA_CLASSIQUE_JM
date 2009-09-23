/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPermisosTiposExpedientesBean extends MasterBean {
    
	//Variables
	private Integer idInstitucion;
	private String idPerfil;
	private Integer idTipoExpediente;
	private Integer idInstitucionTipoExpediente;
	private Integer derechoAcceso;
	
	// Nombre campos de la tabla 
	static public final String C_IDPERFIL = "IDPERFIL";
	static public final String C_IDINSTITUCIONTIPOEXPEDIENTE = "IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOEXPEDIENTE="IDTIPOEXPEDIENTE";
	static public final String C_DERECHOACCESO="DERECHOACCESO";
	
	static public final String T_NOMBRETABLA = "EXP_PERMISOSTIPOSEXPEDIENTES";
	

    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdInstitucionTipoExpediente() {
        return idInstitucionTipoExpediente;
    }
    public void setIdInstitucionTipoExpediente(
            Integer idInstitucionTipoExpediente) {
        this.idInstitucionTipoExpediente = idInstitucionTipoExpediente;
    }
    public String getIdPerfil() {
        return idPerfil;
    }
    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public Integer getDerechoAcceso() {
        return derechoAcceso;
    }
    public void setDerechoAcceso(Integer derechoAcceso) {
        this.derechoAcceso = derechoAcceso;
    }
}
