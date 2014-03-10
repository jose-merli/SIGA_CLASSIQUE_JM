package com.siga.censo.mediadores.form;

import java.util.List;

import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterForm;

public class MediadoresImportForm extends MasterForm {
	
	private static final long serialVersionUID = -1964626954368394170L;
	
	private String idColegio = null;
	
	private List<InstitucionVO> instituciones;
	private String nombreColegio = null;	
		
	public void reset() {
		idColegio = null;
		nombreColegio = null;
	}

	public String getIdColegio() {
		return idColegio;
	}
	public void setIdColegio(String idColegio) {
		this.idColegio = idColegio;
	}
	
	public List<InstitucionVO> getInstituciones() {
		return instituciones;
	}
	public void setInstituciones(List<InstitucionVO> instituciones) {
		this.instituciones = instituciones;
	}
	public String getNombreColegio() {
		return nombreColegio;
	}
	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}

}
