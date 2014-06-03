package com.siga.gratuita.form;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor.ROSE;

import com.siga.general.MasterForm;

/**
 * @author jorgeta
 *
 * @version 05/10/2010
 */

public class ActuacionDesignaForm extends MasterForm
{

	private List<AcreditacionForm> acreditaciones;
	private String categoria;
	private String numero;
	private String fechaJustificacion;
	private String validada;
	private String descripcion;
	private String idJuzgado;
	private String idProcedimiento;
	private String idJurisdiccion;
	private String multiplesComplementos;
	private AcreditacionForm acreditacion;
	private String descripcionProcedimiento;
	private String idFacturacion;
	private String descripcionFacturacion;
	private String permitirEditarActuacion;
	private String numeroProcedimiento;
	
	private boolean documentoJustificacion;
	
	
	/**
	 * @return the documentoJustificacion
	 */
	public boolean isDocumentoJustificacion() {
		return documentoJustificacion;
	}
	/**
	 * @param documentoJustificacion the documentoJustificacion to set
	 */
	public void setDocumentoJustificacion(boolean documentoJustificacion) {
		this.documentoJustificacion = documentoJustificacion;
	}
	public String getDescripcionProcedimiento() {
		return descripcionProcedimiento;
	}
	public void setDescripcionProcedimiento(String descripcionProcedimiento) {
		this.descripcionProcedimiento = descripcionProcedimiento;
	}
	public AcreditacionForm getAcreditacion() {
		return acreditacion;
	}
	public void setAcreditacion(AcreditacionForm acreditacion) {
		this.acreditacion = acreditacion;
	}
	public List<AcreditacionForm> getAcreditaciones() {
		return acreditaciones;
	}
	public void setAcreditaciones(List<AcreditacionForm> acreditaciones) {
		this.acreditaciones = acreditaciones;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	public String getValidada() {
		return validada;
	}
	public void setValidada(String validada) {
		this.validada = validada;
	}
	
	public String getDescripcion() {
		StringBuffer auxDescripcion = new StringBuffer();
		if(fechaJustificacion!=null && !fechaJustificacion.equals(""))
			auxDescripcion.append(fechaJustificacion);
		if(acreditacion!=null){
			auxDescripcion.append(" ");
			auxDescripcion.append(acreditacion.getDescripcion());
//			auxDescripcion.append("(");
//			auxDescripcion.append(acreditacion.getPorcentaje());
//			auxDescripcion.append("%)");
		}
		descripcion = auxDescripcion.toString();
		return descripcion;
	}
	public String getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	public String getMultiplesComplementos() {
		return multiplesComplementos;
	}
	public void setMultiplesComplementos(String multiplesComplementos) {
		this.multiplesComplementos = multiplesComplementos;
	}
	public String getIdJurisdiccion() {
		return idJurisdiccion;
	}
	public void setIdJurisdiccion(String idJurisdiccion) {
		this.idJurisdiccion = idJurisdiccion;
	}
	public String getIdFacturacion() {
		return idFacturacion;
	}
	public void setIdFacturacion(String idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	public String getDescripcionFacturacion() {
		return descripcionFacturacion;
	}
	public void setDescripcionFacturacion(String descripcionFacturacion) {
		this.descripcionFacturacion = descripcionFacturacion;
	}
	public String getPermitirEditarActuacion() {
		return permitirEditarActuacion;
	}
	public void setPermitirEditarActuacion(String permitirEditarActuacion) {
		this.permitirEditarActuacion = permitirEditarActuacion;
	}
	public String getNumeroProcedimiento() {
		return numeroProcedimiento;
	}
	public void setNumeroProcedimiento(String numeroProcedimiento) {
		this.numeroProcedimiento = numeroProcedimiento;
	}
	
	
	
	
	
}