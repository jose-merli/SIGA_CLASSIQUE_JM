package com.siga.censo.mediadores.form;

import java.util.Date;

import com.siga.general.MasterForm;

public class MediadoresImportConsultaFicheroForm extends MasterForm {
				
	private static final long serialVersionUID = 5748333502066990914L;	
	
	
	private Long idmediadorficherocsv;
	private String nombreColegio;
	private String fechaCarga;

	public Long getIdmediadorficherocsv() {
		return idmediadorficherocsv;
	}

	public void setIdmediadorficherocsv(Long idmediadorficherocsv) {
		this.idmediadorficherocsv = idmediadorficherocsv;
	}

	public String getNombreColegio() {
		return nombreColegio;
	}

	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}

	public String getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	
}
