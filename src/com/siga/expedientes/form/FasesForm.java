/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la administración de las fases de los expediente.
 */
public class FasesForm extends MasterForm {
    
//    private String modo="";
    private String modal="";
    private String idFase;
	private String nombre;
	private String descripcion;
	private String diasAntelacion;
	private String diasVencimiento;
	private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String idTipoExpediente;
	
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    public String getIdFase() {
        return idFase;
    }
    public void setIdFase(String idFase) {
        this.idFase = idFase;
    }
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUsuModificacion() {
        return usuModificacion;
    }
    public void setUsuModificacion(String usuModificacion) {
        this.usuModificacion = usuModificacion;
    }
    public String getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(String idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
        
    }
    public String getDiasAntelacion() {
        return diasAntelacion;
    }
    public void setDiasAntelacion(String valor) {
        this.diasAntelacion = valor;
        
    }
    public String getDiasVencimiento() {
        return diasVencimiento;
    }
    public void setDiasVencimiento(String valor) {
        this.diasVencimiento = valor;
        
    }
}
