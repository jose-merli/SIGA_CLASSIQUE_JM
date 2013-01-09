
package com.siga.gratuita.form;

import com.siga.general.MasterForm;

public class DatosEconomicosEJGForm extends MasterForm {	
	private String anio;
	private String datoEJG;
	private String idtipoejg;
	private String numero;		
	private String[] modos = {"ingresos","bieninmueble","bienmueble","cargaeconomica","irpf20"};
	
	public String getDatoEJG() {
		return datoEJG;
	}

	public void setDatoEJG(String datoEJG) {
		this.datoEJG = datoEJG;
	}	
	
	public String getIdtipoejg() {
		return idtipoejg;
	}

	public void setIdtipoejg(String idtipoejg) {
		this.idtipoejg = idtipoejg;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}	

	public String[] getModos () {return this.modos;}
}