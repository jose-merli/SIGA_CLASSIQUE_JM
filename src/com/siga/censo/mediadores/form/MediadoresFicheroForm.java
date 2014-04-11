package com.siga.censo.mediadores.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;

public class MediadoresFicheroForm extends MasterForm {
		
	private static final long serialVersionUID = 5606999734167186215L;
	
	private Long idmediadorexportfichero;	
    private String nombrefichero;
    private String idtipoexport;
    private String tipoExport;
    private String fechacreacion;

	public Long getIdmediadorexportfichero() {
		return idmediadorexportfichero;
	}

	public void setIdmediadorexportfichero(Long idmediadorexportfichero) {
		this.idmediadorexportfichero = idmediadorexportfichero;
	}

	public String getNombrefichero() {
		return nombrefichero;
	}

	public void setNombrefichero(String nombrefichero) {
		this.nombrefichero = nombrefichero;
	}

	public String getIdtipoexport() {
		return idtipoexport;
	}

	public void setIdtipoexport(String idtipoexport) {
		this.idtipoexport = idtipoexport;
	}

	public String getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	public String getTipoExport() {
		return tipoExport;
	}

	public void setTipoExport(String tipoExport) {
		this.tipoExport = tipoExport;
	}

	

}
