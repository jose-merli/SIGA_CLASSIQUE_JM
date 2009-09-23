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
public class EstadosForm extends MasterForm {
    
//    private String modo="";
    private String modal="";
    private String idEstado;
	private String estado;
	private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String idTipoExpediente;
	private String idEstadoSiguiente;
	private String idFaseSiguiente;
	private String idFase;
	private String mensaje="";
	
	private boolean automatico;
	private boolean ejecucionSancion;
	private boolean estadoFinal;
	private boolean activarAlertas;
	
	private String sancionado;
	private String preVisible;
	private String preVisibleFicha;
	
	private String actPrescritas;
	private String sancionPrescrita;
	private String sancionFinalizada;
	private String anotCanceladas;
	private String postVisible;
	private String postVisibleFicha;

	private String diasAntelacion;

//	Propiedad que recojo del combo de Fases y parseo en las propiedades correspondientes
	private String idInst_idExp_idFase="";
	
	//Propiedad que recojo del combo de Estados y parseo en las propiedades correspondientes
	private String idInst_idExp_idFase_idEstadoSig="";
	

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(String idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
    public String getIdFaseSiguiente() {
        return this.idFaseSiguiente;
    }
    public void setIdFaseSiguiente(String siguientefase) {
        this.idFaseSiguiente = siguientefase;
    }
    public String getIdEstadoSiguiente() {
        return this.idEstadoSiguiente;
    }
    public void setIdEstadoSiguiente(String siguienteestado) {
        this.idEstadoSiguiente = siguienteestado;
    }
    public String getUsuModificacion() {
        return usuModificacion;
    }
    public void setUsuModificacion(String usuModificacion) {
        this.usuModificacion = usuModificacion;
    }
    public boolean getAutomatico() {
        return automatico;
    }
    public void setAutomatico(boolean automatico) {
        this.automatico = automatico;
    }
    public boolean getEjecucionSancion() {
        return ejecucionSancion;
    }
    public void setEjecucionSancion(boolean ejecucionSancion) {
        this.ejecucionSancion = ejecucionSancion;
    }
    public boolean getEstadoFinal() {
        return estadoFinal;
    }
    public void setEstadoFinal(boolean valor) {
        this.estadoFinal = valor;
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
    public String getIdInst_idExp_idFase_idEstadoSig() {
        return idInst_idExp_idFase_idEstadoSig;
    }
    public void setIdInst_idExp_idFase_idEstadoSig(
            String idInst_idExp_idFase_idEstado) {
        this.idInst_idExp_idFase_idEstadoSig = idInst_idExp_idFase_idEstado;
        if (idInst_idExp_idFase_idEstado.equals("")){
            setIdEstadoSiguiente("");
            setIdFaseSiguiente("");
        } else {
	        String vars[] = idInst_idExp_idFase_idEstado.split(",");
	        setIdFaseSiguiente(vars[2]);
	        setIdEstadoSiguiente(vars[3]);
        }
    }
    public String getActPrescritas() {
        return actPrescritas;
    }
    public void setActPrescritas(String actPrescritas) {
        this.actPrescritas = actPrescritas;
    }
    public String getAnotCanceladas() {
        return anotCanceladas;
    }
    public void setAnotCanceladas(String anotCanceladas) {
        this.anotCanceladas = anotCanceladas;
    }
    public String getPostVisible() {
        return postVisible;
    }
    public void setPostVisible(String postVisible) {
        this.postVisible = postVisible;
    }
    public String getPostVisibleFicha() {
        return postVisibleFicha;
    }
    public void setPostVisibleFicha(String postVisibleFicha) {
        this.postVisibleFicha = postVisibleFicha;
    }
    public String getPreVisible() {
        return preVisible;
    }
    public void setPreVisible(String preVisible) {
        this.preVisible = preVisible;
    }
    public String getPreVisibleFicha() {
        return preVisibleFicha;
    }
    public void setPreVisibleFicha(String preVisibleFicha) {
        this.preVisibleFicha = preVisibleFicha;
    }
    public String getSancionado() {
        return sancionado;
    }
    public void setSancionado(String sancionado) {
        this.sancionado = sancionado;
    }
    public String getSancionFinalizada() {
        return sancionFinalizada;
    }
    public void setSancionFinalizada(String sancionFinalizada) {
        this.sancionFinalizada = sancionFinalizada;
    }
    public String getSancionPrescrita() {
        return sancionPrescrita;
    }
    public void setSancionPrescrita(String sancionPrescrita) {
        this.sancionPrescrita = sancionPrescrita;
    }
    public boolean getActivarAlertas() {
        return activarAlertas;
    }
    public void setActivarAlertas(boolean valor) {
        this.activarAlertas = valor;
    }
    public String getDiasAntelacion() {
        return diasAntelacion;
    }
    public void setDiasAntelacion(String valor) {
        this.diasAntelacion = valor;
    }
}
