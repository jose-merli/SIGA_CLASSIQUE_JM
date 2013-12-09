/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 29-03-2005 - Inicio
 * jose.barrientos - 28-02-2009 - Añadido el tratamiento del campo sjcs
 */
package com.siga.facturacion.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FicheroBancarioAbonosForm extends MasterForm{

	private String sjcs;
	private String idInstitucion="";
	
	//Campos Busqueda
	private String fechaDesde;
	private String fechaHasta;
	private String codigoBanco;
	private String abonosDesde;
	private String abonosHasta;
	private String importesDesde;
	private String importesHasta;

	public String getSjcs() {
		return sjcs;
	}

	public void setSjcs(String sjcs) {
		this.sjcs = sjcs;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getAbonosDesde() {
		return abonosDesde;
	}

	public void setAbonosDesde(String abonosDesde) {
		this.abonosDesde = abonosDesde;
	}

	public String getAbonosHasta() {
		return abonosHasta;
	}

	public void setAbonosHasta(String abonosHasta) {
		this.abonosHasta = abonosHasta;
	}

	public String getImportesDesde() {
		return importesDesde;
	}

	public void setImportesDesde(String importesDesde) {
		this.importesDesde = importesDesde;
	}

	public String getImportesHasta() {
		return importesHasta;
	}

	public void setImportesHasta(String importesHasta) {
		this.importesHasta = importesHasta;
	} 
	
		/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			fechaDesde= "";
			fechaHasta= "";
			codigoBanco= "";
			abonosDesde= "";
			abonosHasta= "";
			importesDesde= "";
			importesHasta= "";
			
			// resetea el formulario
			super.reset(mapping, request);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
