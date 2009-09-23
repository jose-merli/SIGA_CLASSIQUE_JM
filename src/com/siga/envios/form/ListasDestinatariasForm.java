/*
 * Created on Mar 29, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la definición de listas destinatarias.
 */
public class ListasDestinatariasForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
    private String idEnvio;
	private String idListaCorreo;

    
    public String getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }    
    public String getIdListaCorreo() {
        return idListaCorreo;
    }
    public void setIdListaCorreo(String idListaCorreo) {
        this.idListaCorreo = idListaCorreo;
    }
    public String getModal() {
        return modal;
    }
    public void setModal(String modal) {
        this.modal = modal;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }

}
