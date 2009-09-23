/*
 * Created on Jan 18, 2005
 * @author juan.grau
 *
 *
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la documentación de un expediente
 */
public class ExpAlertaForm extends MasterForm {
	private static final long serialVersionUID = 7034405470028411661L;

    private String modal="";    
    private String fase="";    
    private String idFase="";    
    private String estado="";    
    private String idEstado="";    
    private String texto="";    
    private String fechaAlerta="";    
    private String tituloVentana="";    
    
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	public String getIdFase() {
		return idFase;
	}
	public void setIdFase(String idFase) {
		this.idFase = idFase;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFase() {
		return fase;
	}
	public void setFase(String fase) {
		this.fase = fase;
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
    public String getFechaAlerta() {
        return fechaAlerta;
    }
    public void setFechaAlerta(String fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }
	public String getTituloVentana(){
		return tituloVentana;
	}
	public void setTituloVentana(String titulo){
		this.tituloVentana=titulo;
	}
}
