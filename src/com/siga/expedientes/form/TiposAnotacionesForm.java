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
public class TiposAnotacionesForm extends MasterForm {
    
//    private String modo="";
    private String modal="";
    private String idTipoAnotacion;
	private String nombre;
	private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String idTipoExpediente;
	private String idEstado;
	private String idFase;
	private String mensaje="";
	
	//Propiedad que recojo del combo de Fases y parseo en las propiedades correspondientes
	private String idInst_idExp_idFase="";
	
	//Propiedad que recojo del combo de Estados y parseo en las propiedades correspondientes
	private String idInst_idExp_idFase_idEstado="";
	
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
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
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public String getIdTipoAnotacion() {
        return idTipoAnotacion;
    }
    public void setIdTipoAnotacion(String idTipoAnotacion) {
        this.idTipoAnotacion = idTipoAnotacion;
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
    public String getIdInst_idExp_idFase() {
        return idInst_idExp_idFase;
    }
    public void setIdInst_idExp_idFase(String idInst_idExp_idFase) {
        this.idInst_idExp_idFase = idInst_idExp_idFase;
        if (idInst_idExp_idFase.equals("")){
            setIdFase("");
        } else {
	        String vars[] = idInst_idExp_idFase.split(",");
	        setIdFase(vars[2]);
        }
    }
    public String getIdInst_idExp_idFase_idEstado() {
        return idInst_idExp_idFase_idEstado;
    }
    public void setIdInst_idExp_idFase_idEstado(
            String idInst_idExp_idFase_idEstado) {
        this.idInst_idExp_idFase_idEstado = idInst_idExp_idFase_idEstado;
        if (idInst_idExp_idFase_idEstado.equals("")){
            setIdEstado("");
        } else {
	        String vars[] = idInst_idExp_idFase_idEstado.split(",");
	        setIdEstado(vars[3]);
        }
    }
}
