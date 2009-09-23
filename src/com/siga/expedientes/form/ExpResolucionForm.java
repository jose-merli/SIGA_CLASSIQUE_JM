/*
 * Created on Jan 20, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la gestión de los datos de la resolución de un expediente
 */
public class ExpResolucionForm extends MasterForm {
	private static final long serialVersionUID = 8820456842673037381L;
	private String resDescripcion="";
    private String sancionPrescrita="";
    private String actuacionesPrescritas="";
    private String sancionFinalizada="";
    private boolean visible;
    private boolean visibleEnFicha;
    private boolean sancionado;
    private String anotacionesCanceladas="";
    private String resultadoInforme = "", fechaResolucion = "";
    private String tituloVentana="";
    
	
	public String getActuacionesPrescritas() {
		return actuacionesPrescritas;
	}
	public void setActuacionesPrescritas(String actuacionesPrescritas) {
		this.actuacionesPrescritas = actuacionesPrescritas;
	}
	public String getAnotacionesCanceladas() {
		return anotacionesCanceladas;
	}
	public void setAnotacionesCanceladas(String anotacionesCanceladas) {
		this.anotacionesCanceladas = anotacionesCanceladas;
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
	public boolean isSancionado() {
		return sancionado;
	}
	public void setSancionado(boolean sancionado) {
		this.sancionado = sancionado;
	}
	public String getResDescripcion() {
		return resDescripcion;
	}
	public void setResDescripcion(String resDescripcion) {
		this.resDescripcion = resDescripcion;
	}	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isVisibleEnFicha() {
		return visibleEnFicha;
	}
	public void setVisibleEnFicha(boolean visibleEnFicha) {
		this.visibleEnFicha = visibleEnFicha;
	}
	public String getFechaResolucion() {
		return fechaResolucion;
	}
	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	
	public String getResultadoInforme() {
		return resultadoInforme;
	}
	public void setResultadoInforme(String resultadoInforme) {
		this.resultadoInforme = resultadoInforme;
	}
	public String getTituloVentana(){
		return tituloVentana;
	}
	public void setTituloVentana(String titulo){
		this.tituloVentana=titulo;
	}
}
