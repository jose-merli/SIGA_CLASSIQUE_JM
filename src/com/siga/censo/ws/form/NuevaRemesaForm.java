package com.siga.censo.ws.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;

public class NuevaRemesaForm extends MasterForm {

	private static final long serialVersionUID = -8800840179733753678L;
	
	private String idColegioInsertar = null;
	private String idColegioActualizar = null;
	private String fechaExportacion = null;
	private FormFile file;
	
	
	public String getIdColegioActualizar() {
		return idColegioActualizar;
	}
	public void setIdColegioActualizar(String idColegioActualizar) {
		this.idColegioActualizar = idColegioActualizar;
	}
	public String getIdColegioInsertar() {
		return idColegioInsertar;
	}
	public void setIdColegioInsertar(String idColegioInsertar) {
		this.idColegioInsertar = idColegioInsertar;
	}
	public String getFechaExportacion() {
		return fechaExportacion;
	}
	public void setFechaExportacion(String fechaExportacion) {
		this.fechaExportacion = fechaExportacion;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	
}
