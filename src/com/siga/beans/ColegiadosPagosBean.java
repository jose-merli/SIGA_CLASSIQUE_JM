/*
 * Creada el 13/04/2005
 * @author ruben.fernandez
 *
 */
package com.siga.beans;

import java.io.Serializable;

//Este bean sirve para implementar cada fila de Criterios de Pago con todos sus campos.
public class ColegiadosPagosBean implements Serializable{
    
	//Variables
	private String idPersona = null;
	private String ncolegiado = null;
	private String nombreColegiado = null;
	private String marcado = null;
	private String idInstitucion = null;
	
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the colegiado.
	 */
	public String getNombreColegiado() {
		return nombreColegiado;
	}
	/**
	 * @param colegiado The colegiado to set.
	 */
	public void setNombreColegiado(String colegiado) {
		this.nombreColegiado = colegiado;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public String getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the marcado.
	 */
	public String getMarcado() {
		return marcado;
	}
	/**
	 * @param marcado The marcado to set.
	 */
	public void setMarcado(String marcado) {
		this.marcado = marcado;
	}
	/**
	 * @return Returns the nColegiado.
	 */
	public String getNcolegiado() {
		return ncolegiado;
	}
	/**
	 * @param colegiado The nColegiado to set.
	 */
	public void setNcolegiado(String colegiado) {
		ncolegiado = colegiado;
	}

}