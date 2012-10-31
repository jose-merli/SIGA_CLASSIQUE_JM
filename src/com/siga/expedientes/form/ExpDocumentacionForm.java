/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 *
 */
package com.siga.expedientes.form;

import com.siga.censo.form.DatosRegTelForm;

/**
 * Formulario para la documentación de un expediente
 */
public class ExpDocumentacionForm extends DatosRegTelForm {
	
//    private String modo="";
    private String modal="";    
    private String fase="";    
    private String idFase="";    
    private String estado="";    
    private String idEstado="";    
    private String descripcion="";    
    private String ruta="";    
    private String regentrada="";    
    private String regsalida=""; 
    
    
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getRegentrada() {
		return regentrada;
	}
	public void setRegentrada(String regentrada) {
		this.regentrada = regentrada;
	}
	public String getRegsalida() {
		return regsalida;
	}
	public void setRegsalida(String regsalida) {
		this.regsalida = regsalida;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
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
