/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario con los datos de las partes de un expediente
 */
public class ExpPartesForm extends MasterForm {
	
//	private String modo="";
    private String modal="";
	private String nombre="";
	private String primerApellido="";
	private String segundoApellido="";
	private String nif="";
	private String numColegiado="";
	private String idRol="";
	private String rolSel="";
	private String idPersona="";
	
	
	public String getRolSel() {
		return rolSel;
	}
	public void setRolSel(String rolSel) {
		this.rolSel = rolSel;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdRol() {
		return idRol;
	}
	public void setIdRol(String idRol) {
		this.idRol = idRol;
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
	public String getNumColegiado() {
		return numColegiado;
	}
	public void setNumColegiado(String colegiado) {
		numColegiado = colegiado;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	
	private String tituloVentana = "";
	
    /**
     * @return Returns the tituloVentana.
     */
    public String getTituloVentana() {
        return tituloVentana;
    }
    /**
     * @param tituloVentana The tituloVentana to set.
     */
    public void setTituloVentana(String tituloVentana) {
        this.tituloVentana = tituloVentana;
    }
}
