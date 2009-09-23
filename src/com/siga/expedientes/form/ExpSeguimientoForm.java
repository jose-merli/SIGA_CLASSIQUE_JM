/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.censo.form.ExpedientesForm;
import com.siga.general.MasterForm;

/**
 * Formulario para las anotaciones de un expediente
 */
public class ExpSeguimientoForm extends MasterForm {
	
//	private String modo="";
    private String modal="";
    private String fecha="";
    private String idTipoAnotacion="";
    private String tipoAnotacion="";
    private String fase="";    
    private String idFase="";    
    private String estado="";    
    private String idEstado="";  
    private String usuario="";
    private String idUsuario="";
    private String descripcion="";  
    private String regentrada="";    
    private String regsalida="";
    
     
    
    private String idInstitucion="";
	private String idInstitucionTipoExpediente=""; 	
	private String idTipoExpediente="";
	private String numeroExpediente=""; 	
	private String anioExpediente="";
	private String fechaDesde="";
	private String fechaHasta="";
	
//	Propiedad que recojo del combo de Fases y parseo en las propiedades correspondientes
	private String comboFases;

	private String comboEstados="";
	
	private Boolean editable=Boolean.FALSE;
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdInstitucionTipoExpediente() {
		return idInstitucionTipoExpediente;
	}
	public void setIdInstitucionTipoExpediente(String idInstitucionTipoExpediente) {
		this.idInstitucionTipoExpediente = idInstitucionTipoExpediente;
	}
	public String getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(String idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public String getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public String getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(String anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
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
	public String getIdTipoAnotacion() {
		return idTipoAnotacion;
	}
	public void setIdTipoAnotacion(String idTipoAnotacion) {
		this.idTipoAnotacion = idTipoAnotacion;
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
	public String getTipoAnotacion() {
		return tipoAnotacion;
	}
	public void setTipoAnotacion(String tipoAnotacion) {
		this.tipoAnotacion = tipoAnotacion;
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
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getComboEstados() {
		return comboEstados;
	}
	public void setComboEstados(String comboEstados) {
		this.comboEstados = comboEstados;
		if (comboEstados!=null && !comboEstados.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboEstados,",");
	    	st.nextToken();//idinstitucion_tipoexpediente
	    	st.nextToken();//idtipoexpediente
	    	st.nextToken();//idfase
	    	this.setIdEstado((st.nextToken()));        	
	    }else{        	
	    	this.setIdEstado("");  
	    }
	}
	public String getComboFases() {
		return comboFases;
	}
	public void setComboFases(String comboFases) {
		this.comboFases = comboFases;
	    if (comboFases!=null && !comboFases.equals("")){
	    	StringTokenizer st = new StringTokenizer(comboFases,",");
	    	st.nextToken();//idinstitucion_tipoexpediente
	    	st.nextToken();//idtipoexpediente
	    	this.setIdFase(st.nextToken());        	
	    }else{        	
	    	//this.setIdFase("");  
	    }
	}
	
	
}
