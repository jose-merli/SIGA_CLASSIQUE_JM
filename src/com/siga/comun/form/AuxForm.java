package com.siga.comun.form;

import org.apache.struts.action.ActionForm;

public abstract class AuxForm extends ActionForm{

	private static final long serialVersionUID = -2893863138419994702L;
	
	private String accion = "";
	private String modo = "";
	private String id;
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getAccion() {
		return accion;
	}
	
	public void setModo(String modo) {
		this.modo = modo;
	}
	public String getModo() {
		return modo;
	}
	
}
