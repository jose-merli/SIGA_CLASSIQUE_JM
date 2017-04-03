package com.siga.gratuita.form;

import java.util.Date;

import com.siga.censo.form.CargaMasivaCVForm;
import com.siga.censo.form.CargaMasivaForm;


/**
 * 
 * @author jorgeta
 *
 */
public class CargaMasivaProcuradoresForm extends CargaMasivaForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String idCargaMasivaProcuradores;
	public String getIdCargaMasivaProcuradores() {
		return idCargaMasivaProcuradores;
	}

	public void setIdCargaMasivaProcuradores(String idCargaMasivaProcuradores) {
		this.idCargaMasivaProcuradores = idCargaMasivaProcuradores;
	}

	private String numEjg;
    private String codigoDesignaAbogado;
	private String numColProcurador;
	private String nombreProcurador;
	private String numDesignaProcurador;
    private String fechaDesignaProcurador;
    private String observaciones;
	
	
	public void clear() {
		setModo("");
	}

	public CargaMasivaProcuradoresForm clone() {
		CargaMasivaProcuradoresForm miForm = new CargaMasivaProcuradoresForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
		miForm.setTheFile(this.getTheFile());
		return miForm;
	}

	public String getNumEjg() {
		return numEjg;
	}

	public void setNumEjg(String numEjg) {
		this.numEjg = numEjg;
	}

	public String getCodigoDesignaAbogado() {
		return codigoDesignaAbogado;
	}

	public void setCodigoDesignaAbogado(String codigoDesignaAbogado) {
		this.codigoDesignaAbogado = codigoDesignaAbogado;
	}

	public String getNumColProcurador() {
		return numColProcurador;
	}

	public void setNumColProcurador(String numColProcurador) {
		this.numColProcurador = numColProcurador;
	}

	public String getNombreProcurador() {
		return nombreProcurador;
	}

	public void setNombreProcurador(String nombreProcurador) {
		this.nombreProcurador = nombreProcurador;
	}

	public String getNumDesignaProcurador() {
		return numDesignaProcurador;
	}

	public void setNumDesignaProcurador(String numDesignaProcurador) {
		this.numDesignaProcurador = numDesignaProcurador;
	}

	public String getFechaDesignaProcurador() {
		return fechaDesignaProcurador;
	}

	public void setFechaDesignaProcurador(String fechaDesignaProcurador) {
		this.fechaDesignaProcurador = fechaDesignaProcurador;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * @return the colegiadoNif
	 */
	
	
	
	
}