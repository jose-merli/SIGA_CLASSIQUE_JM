package com.siga.censo.mediadores.form;

import org.apache.struts.upload.FormFile;

import com.siga.general.MasterForm;

public class MediadoresExportForm extends MasterForm {	
	
	private static final long serialVersionUID = -8544128427499813660L;
	
	private Long idmediadorexportfichero = null;
	private FormFile file = null;

	public Long getIdmediadorexportfichero() {
		return idmediadorexportfichero;
	}

	public void setIdmediadorexportfichero(Long idmediadorexportfichero) {
		this.idmediadorexportfichero = idmediadorexportfichero;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

}
