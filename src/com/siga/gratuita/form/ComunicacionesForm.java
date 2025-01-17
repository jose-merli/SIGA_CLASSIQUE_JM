
package com.siga.gratuita.form;

import java.util.List;

import com.siga.general.MasterForm;

/**
 * 
 * @author jorgeta 
 * @date   01/08/2013
 *
 * La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class ComunicacionesForm extends MasterForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7986809121711887046L;
	String solicitante;
	String anio;
	String codigoDesignaNumEJG;
	String ejgNumEjg;
	String ejgAnio;
	String ejgNumero;
	String ejgIdTipo;
	String ejgIdInstitucion;
	String designaAnio;
	String designaNumero;
	String designaCodigo;
	String designaIdTurno;
	String designaIdInstitucion;
	
	String comisionAJG;
	
	String idEnvio;
	String fecha;
	String tipo;
	String nombre;
	String asunto;
	String idInstitucion;
	String idPersona;
	List<String> documentos;
	
	
	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	public String getEjgAnio() {
		return ejgAnio;
	}
	public void setEjgAnio(String ejgAnio) {
		this.ejgAnio = ejgAnio;
	}
	public String getEjgNumero() {
		return ejgNumero;
	}
	public void setEjgNumero(String ejgNumero) {
		this.ejgNumero = ejgNumero;
	}
	
	public String getEjgNumEjg() {
		return ejgNumEjg;
	}
	public void setEjgNumEjg(String ejgNumEjg) {
		this.ejgNumEjg = ejgNumEjg;
	}
	public String getEjgIdTipo() {
		return ejgIdTipo;
	}
	public void setEjgIdTipo(String ejgIdTipo) {
		this.ejgIdTipo = ejgIdTipo;
	}
	

	public String getEjgIdInstitucion() {
		return ejgIdInstitucion;
	}
	public void setEjgIdInstitucion(String ejgIdInstitucion) {
		this.ejgIdInstitucion = ejgIdInstitucion;
	}
	public String getComisionAJG() {
		return comisionAJG;
	}
	public void setComisionAJG(String comisionAJG) {
		this.comisionAJG = comisionAJG;
	}
	public String getDesignaAnio() {
		return designaAnio;
	}
	public void setDesignaAnio(String designaAnio) {
		this.designaAnio = designaAnio;
	}
	public String getDesignaNumero() {
		return designaNumero;
	}
	public void setDesignaNumero(String designaNumero) {
		this.designaNumero = designaNumero;
	}
	public String getDesignaIdTurno() {
		return designaIdTurno;
	}
	public void setDesignaIdTurno(String designaIdTurno) {
		this.designaIdTurno = designaIdTurno;
	}
	public String getDesignaIdInstitucion() {
		return designaIdInstitucion;
	}
	public void setDesignaIdInstitucion(String designaIdInstitucion) {
		this.designaIdInstitucion = designaIdInstitucion;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getCodigoDesignaNumEJG() {
		return codigoDesignaNumEJG;
	}
	public void setCodigoDesignaNumEJG(String codigoDesignaNumEJG) {
		this.codigoDesignaNumEJG = codigoDesignaNumEJG;
	}
	public String getDesignaCodigo() {
		return designaCodigo;
	}
	public void setDesignaCodigo(String designaCodigo) {
		this.designaCodigo = designaCodigo;
	}
	public String getIdEnvio() {
		return idEnvio;
	}
	public String getFecha() {
		return fecha;
	}
	public String getTipo() {
		return tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public String getAsunto() {
		return asunto;
	}
	public List<String> getDocumentos() {
		return documentos;
	}
	public void setIdEnvio(String idEnvio) {
		this.idEnvio = idEnvio;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public void setDocumentos(List<String> documentos) {
		this.documentos = documentos;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	public ComunicacionesForm clone() {
		ComunicacionesForm miForm = new ComunicacionesForm();
		miForm.setIdInstitucion(this.idInstitucion);
		miForm.setIdPersona(this.idPersona);
		miForm.setIdEnvio(this.idEnvio);
		miForm.setFecha(this.fecha);
		miForm.setTipo(this.tipo);
		miForm.setNombre(this.nombre);
		miForm.setAsunto(this.asunto);
		miForm.setDocumentos(this.documentos);
        miForm.setDatosPaginador(this.getDatosPaginador());
		return miForm;
		
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

}
