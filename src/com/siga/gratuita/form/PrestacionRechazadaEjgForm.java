
package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author jorgeta 
 * @date   27/06/2013
 *
 * La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class PrestacionRechazadaEjgForm extends MasterForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3343814598117241919L;
	String solicitante;
	String ejgNumEjg;
	String ejgAnio;
	String ejgNumero;
	String ejgIdTipo;
	String ejgIdInstitucion;
	String prestacionId;
	String prestacionDescripcion;
	String idsInsertarRechazadas;
	String idsBorrarRechazadas;

	
	
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
	
	public String getPrestacionId() {
		return prestacionId;
	}
	public void setPrestacionId(String prestacionId) {
		this.prestacionId = prestacionId;
	}
	public String getPrestacionDescripcion() {
		return prestacionDescripcion;
	}
	public void setPrestacionDescripcion(String prestacionDescripcion) {
		this.prestacionDescripcion = prestacionDescripcion;
	}
	public String getEjgIdInstitucion() {
		return ejgIdInstitucion;
	}
	public void setEjgIdInstitucion(String ejgIdInstitucion) {
		this.ejgIdInstitucion = ejgIdInstitucion;
	}
	public String getIdsInsertarRechazadas() {
		return idsInsertarRechazadas;
	}
	public void setIdsInsertarRechazadas(String idsInsertarRechazadas) {
		this.idsInsertarRechazadas = idsInsertarRechazadas;
	}
	public String getIdsBorrarRechazadas() {
		return idsBorrarRechazadas;
	}
	public void setIdsBorrarRechazadas(String idsBorrarRechazadas) {
		this.idsBorrarRechazadas = idsBorrarRechazadas;
	}
	

}
