package com.siga.censo.mediadores.form;

import java.util.List;

import org.apache.struts.upload.FormFile;

import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterForm;

public class MediadoresImportConsultaForm extends MasterForm {
	
	private static final long serialVersionUID = -1964626954368394170L;
	
	private Long idmediadorficherocsv;
	
	private String idColegio = null;
	
	private List<InstitucionVO> instituciones;
	
	private String fechaCargaDesde;
	private String fechaCargaHasta;
	
	
			
	public void reset() {
		idColegio = null;	
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

	public String getFechaCargaDesde() {
		return fechaCargaDesde;
	}

	public void setFechaCargaDesde(String fechaCargaDesde) {
		this.fechaCargaDesde = fechaCargaDesde;
	}

	public String getFechaCargaHasta() {
		return fechaCargaHasta;
	}

	public void setFechaCargaHasta(String fechaCargaHasta) {
		this.fechaCargaHasta = fechaCargaHasta;
	}

	public Long getIdmediadorficherocsv() {
		return idmediadorficherocsv;
	}

	public void setIdmediadorficherocsv(Long idmediadorficherocsv) {
		this.idmediadorficherocsv = idmediadorficherocsv;
	}

	

}
