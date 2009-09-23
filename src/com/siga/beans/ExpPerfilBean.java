/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import java.io.Serializable;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpPerfilBean implements Serializable{
    
	//Variables
	private String idPerfil;
	private String nombrePerfil;
	private String derechoAcceso;
	
	
    public String getDerechoAcceso() {
        return derechoAcceso;
    }
    public void setDerechoAcceso(String derechoAcceso) {
        this.derechoAcceso = derechoAcceso;
    }
    public String getIdPerfil() {
        return idPerfil;
    }
    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }
    public String getNombrePerfil() {
        return nombrePerfil;
    }
    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }
}
