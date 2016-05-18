package com.siga.censo.ws.form;

import com.siga.general.MasterForm;


public class NuevaColumnaPerfilForm extends MasterForm{

	private static final long serialVersionUID = 1L;
	private String nombreColumna = null;
	private String tipoColumna = null;
	private String idColegioInsertar = null;
	
	public String getNombreColumna() {
		return nombreColumna;
	}
	public void setNombreColumna(String nombreColumna) {
		this.nombreColumna = nombreColumna;
	}
	public String getTipoColumna() {
		return tipoColumna;
	}
	public void setTipoColumna(String tipoColumna) {
		this.tipoColumna = tipoColumna;
	}
	public String getIdColegioInsertar() {
		return idColegioInsertar;
	}
	public void setIdColegioInsertar(String idColegioInsertar) {
		this.idColegioInsertar = idColegioInsertar;
	}
	
	
	
}
