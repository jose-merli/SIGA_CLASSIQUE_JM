package com.siga.facturacion.form;

import com.siga.general.MasterForm;

/**
 * Form del mantenimiento de FAC_SUFIJO
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class SufijosForm extends MasterForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3423393979380517529L;
	private String sufijo = null;
	private Integer idsufijo = null;
	private String concepto = null;
	
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getSufijo() {
		return sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	public Integer getIdSufijo() {
		return idsufijo;
	}
	public void setIdSufijo(Integer idsufijo) {
		this.idsufijo = idsufijo;
	}
	
}
