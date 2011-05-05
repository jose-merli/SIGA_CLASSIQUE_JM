/*
 * Created on Mar 10, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.form;

import java.util.Vector;

import com.siga.general.MasterForm;

/**
 * Formulario de permisos sobre consultas
 */
public class PermisosConsultaForm extends MasterForm {

//	private String modo="";
    private String modal="";
    private String grupos="";
    private String gruposAntiguos="";
    private String idConsulta="";
    private String idInstitucion_Consulta="";
    public Vector datosSelec= null;
    public Vector datosTodos= null;
    
    
    
	public Vector getDatosSelec() {
		return datosSelec;
	}
	public void setDatosSelec(Vector datosSelec) {
		this.datosSelec = datosSelec;
	}
	public Vector getDatosTodos() {
		return datosTodos;
	}
	public void setDatosTodos(Vector datosTodos) {
		this.datosTodos = datosTodos;
	}
	public String getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(String idConsulta) {
		this.idConsulta = idConsulta;
	}
	public String getIdInstitucion_Consulta() {
		return idInstitucion_Consulta;
	}
	public void setIdInstitucion_Consulta(String idInstitucion_Consulta) {
		this.idInstitucion_Consulta = idInstitucion_Consulta;
	}
	public String getGrupos() {
		return grupos;
	}
	public void setGrupos(String grupos) {
		this.grupos = grupos;
	}
	public String getGruposAntiguos() {
		return gruposAntiguos;
	}
	public void setGruposAntiguos(String gruposAntiguos) {
		this.gruposAntiguos = gruposAntiguos;
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
