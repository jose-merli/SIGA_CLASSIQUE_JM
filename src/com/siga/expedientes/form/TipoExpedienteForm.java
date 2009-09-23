/*
 * Created on Dec 22, 2004
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la administración de los tipos de expediente.
 *
 */
public class TipoExpedienteForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
	private String nombre;
	private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String esGeneral="N";
	private String idTipoExpediente;
	private String refresh=null;

	public String getEsGeneral() {
		return esGeneral;
	}
	public void setEsGeneral(String esGeneral) {
		this.esGeneral = esGeneral;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
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
    public String getRefresh() {
        return refresh;
    }
    public void setRefresh(String refresh) {
        this.refresh = "";
    }
}
