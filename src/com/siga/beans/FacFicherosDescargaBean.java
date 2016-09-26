package com.siga.beans;

import java.io.File;

public class FacFicherosDescargaBean extends MasterBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private File fichero;
	private String nombreFacturaFichero;
	private Integer formatoDescarga;
	
	
	public File getFichero() {
		return fichero;
	}
	public void setFichero(File fichero) {
		this.fichero = fichero;
	}
	public String getNombreFacturaFichero() {
		return nombreFacturaFichero;
	}
	public void setNombreFacturaFichero(String nombreFacturaFichero) {
		this.nombreFacturaFichero = nombreFacturaFichero;
	}
	public Integer getFormatoDescarga() {
		return formatoDescarga;
	}
	public void setFormatoDescarga(Integer formatoDescarga) {
		this.formatoDescarga = formatoDescarga;
	}    
	
}
