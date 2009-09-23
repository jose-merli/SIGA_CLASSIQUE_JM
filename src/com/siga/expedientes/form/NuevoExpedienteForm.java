/*
 * Created on Feb 2, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import java.util.StringTokenizer;

import com.siga.general.MasterForm;

/**
 * Formulario para modal de nuevo expediente
 */
public class NuevoExpedienteForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
    private String idInstitucion="";
    private String idTipoExpediente="";
    private String comboTipoExpediente="";
    
    
    
	public String getComboTipoExpediente() {
		return comboTipoExpediente;
	}
	public void setComboTipoExpediente(String comboTipoExpediente) {
		this.comboTipoExpediente = comboTipoExpediente;
		if (comboTipoExpediente!=null && !comboTipoExpediente.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboTipoExpediente,",");
	    	this.setIdInstitucion(st.nextToken());
	    	this.setIdTipoExpediente(st.nextToken());
	    }else{        	
	    	this.setIdInstitucion("");
	    	this.setIdTipoExpediente(""); 
	    }
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(String idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public String getModal() {
		return modal;
	}
	public void setModal(String modal) {
		this.modal = modal;
	}
//	public String getModo() {
//		return modo;
//	}
//	public void setModo(String modo) {
//		this.modo = modo;
//	}
}
