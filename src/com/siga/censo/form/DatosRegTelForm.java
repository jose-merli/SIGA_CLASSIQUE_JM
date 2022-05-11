package com.siga.censo.form;

import com.siga.general.MasterForm;

public class DatosRegTelForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4528020964872260832L;
	private String identificadorDs;
	private String titleDs;
	private String posicionDs;
	private boolean creaCollection = false;
	boolean colegioConfiguradoEnvioCAJG;
		
	public boolean isCreaCollection() {
		return creaCollection;
	}

	public void setCreaCollection(boolean creaCollection) {
		this.creaCollection = creaCollection;
	}

	public String getIdentificadorDs() {
		return identificadorDs;
	}

	public void setIdentificadorDs(String identificadorDs) {
		this.identificadorDs = identificadorDs;
	}

	public String getTitleDs() {
		return titleDs;
	}

	public void setTitleDs(String titleDs) {
		this.titleDs = titleDs;
	}

	public String getPosicionDs() {
		return posicionDs;
	}

	public void setPosicionDs(String posicionDs) {
		this.posicionDs = posicionDs;
	}

	public boolean isColegioConfiguradoEnvioCAJG() {
		return colegioConfiguradoEnvioCAJG;
	}

	public void setColegioConfiguradoEnvioCAJG(boolean colegioConfiguradoEnvioCAJG) {
		this.colegioConfiguradoEnvioCAJG = colegioConfiguradoEnvioCAJG;
	}
}
