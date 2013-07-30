/*
 * Created on Mar 21, 2005
 * @author emilio.grau
 *
 */
package com.siga.facturacion.form;


import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;



import com.siga.general.MasterForm;

/**
 * Formulario para consulta de morosos
 */
public class ConsultaMorososForm extends MasterForm {

	private String fechaDesde = "";
	private String fechaHasta = "";
	private String facturasImpagadasDesde = "";
	private String facturasImpagadasHasta = "";
	private String importeAdeudadoDesde = "";
	private String importeAdeudadoHasta = "";
	private String idPersona = "";
	private String numColegiado = "";
	private String nombre = "";
	private String modelo = "";
	private String numeroFactura = "";
	private String cmbEstadoColegial = "";
	private String cmbEstadosFactura = "";
	
	private String numeroComunicacionesDesde = "";
	private String numeroComunicacionesHasta = "";
	String letrado;
	String numeroNifTagBusquedaPersonas;
	String nombrePersona;
	Vector lineasComunicaciones = new Vector();
	private Integer idInstitucion;
	private Integer idEnvioDoc;
	private Integer idDocumento;
	private String pathDocumento;
	private String  interesadoNombre;
	private String  interesadoApellidos;
	//private FormFile theFile;

	public String getInteresadoNombre() {
		return interesadoNombre;
	}

	public void setInteresadoNombre(String interesadoNombre) {
		this.interesadoNombre = interesadoNombre;
	}

	public String getInteresadoApellidos() {
		return interesadoApellidos;
	}

	public void setInteresadoApellidos(String interesadoApellidos) {
		this.interesadoApellidos = interesadoApellidos;
	}

	/*public FormFile getTheFile() {
        return theFile;
    }
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }*/
	public String getNumeroNifTagBusquedaPersonas() {
		return numeroNifTagBusquedaPersonas;
	}

	public void setNumeroNifTagBusquedaPersonas(
			String numeroNifTagBusquedaPersonas) {
		this.numeroNifTagBusquedaPersonas = numeroNifTagBusquedaPersonas;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getLetrado() {
		return letrado;
	}

	public void setLetrado(String letrado) {
		this.letrado = letrado;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumColegiado() {
		return numColegiado;
	}

	public void setNumColegiado(String numColegiado) {
		this.numColegiado = numColegiado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	
	public String getFacturasImpagadasDesde() {
		return facturasImpagadasDesde;
	}

	public void setFacturasImpagadasDesde(String facturasImpagadasDesde) {
		this.facturasImpagadasDesde = facturasImpagadasDesde;
	}

	public String getFacturasImpagadasHasta() {
		return facturasImpagadasHasta;
	}

	public void setFacturasImpagadasHasta(String facturasImpagadasHasta) {
		this.facturasImpagadasHasta = facturasImpagadasHasta;
	}

	public String getImporteAdeudadoDesde() {
		return importeAdeudadoDesde;
	}

	public void setImporteAdeudadoDesde(String importeAdeudadoDesde) {
		this.importeAdeudadoDesde = importeAdeudadoDesde;
	}

	public String getImporteAdeudadoHasta() {
		return importeAdeudadoHasta;
	}

	public void setImporteAdeudadoHasta(String importeAdeudadoHasta) {
		this.importeAdeudadoHasta = importeAdeudadoHasta;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getCmbEstadoColegial() {
		return cmbEstadoColegial;
	}

	public void setCmbEstadoColegial(String cmbEstadoColegial) {
		this.cmbEstadoColegial = cmbEstadoColegial;
	}

	
	public Vector getLineasComunicaciones() {
		return lineasComunicaciones;
	}

	public void setLineasComunicaciones(Vector lineasComunicaciones) {
		this.lineasComunicaciones = lineasComunicaciones;
	}

	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public Integer getIdEnvioDoc() {
		return idEnvioDoc;
	}

	public void setIdEnvioDoc(Integer idEnvioDoc) {
		this.idEnvioDoc = idEnvioDoc;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	

	public String getPathDocumento() {
		return pathDocumento;
	}

	public void setPathDocumento(String pathDocumento) {
		this.pathDocumento = pathDocumento;
	}

	public String getNumeroComunicacionesDesde() {
		return numeroComunicacionesDesde;
	}

	public void setNumeroComunicacionesDesde(String numeroComunicacionesDesde) {
		this.numeroComunicacionesDesde = numeroComunicacionesDesde;
	}

	public String getNumeroComunicacionesHasta() {
		return numeroComunicacionesHasta;
	}

	public void setNumeroComunicacionesHasta(String numeroComunicacionesHasta) {
		this.numeroComunicacionesHasta = numeroComunicacionesHasta;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{	
		this.fechaDesde = "";
		this.fechaHasta = "";
		this.facturasImpagadasDesde = "";
		this.facturasImpagadasHasta = "";
		this.importeAdeudadoDesde = "";
		this.importeAdeudadoHasta = "";
		this.idPersona = "";
		this.numColegiado = "";
		this.nombre = "";
		this.modelo = "";
		this.numeroFactura = "";
		this.cmbEstadoColegial = "";
		
		this.numeroComunicacionesDesde = "";
		this.numeroComunicacionesHasta = "";
		this.letrado = null;
		this.numeroNifTagBusquedaPersonas= null;
		this.nombrePersona = null;
		this.lineasComunicaciones = new Vector();
		this.idInstitucion= null;
		this.idEnvioDoc = null;
		this.idDocumento= null;
		this.pathDocumento= null;
		this.interesadoNombre= null;
		this.interesadoApellidos= null;
		
		super.reset(mapping, request);
		
	}

	public String getCmbEstadosFactura() {
		return cmbEstadosFactura;
	}

	public void setCmbEstadosFactura(String cmbEstadosFactura) {
		this.cmbEstadosFactura = cmbEstadosFactura;
	}

}
