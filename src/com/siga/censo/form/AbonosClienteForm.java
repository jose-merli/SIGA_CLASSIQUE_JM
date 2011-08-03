/*
 * VERSIONES:
 * 
 * miguel.villegas - 21-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de tratamiento de abonos en la ficga cliente <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;


public class AbonosClienteForm extends MasterForm{
	

	private String idPersona="";
	private String idInstitucion="";
	private String idAbono="";
	private String numeroAbono="";
	private String incluirRegistrosConBajaLogica;
	
	/**
	 * @return Returns the numeroAbono.
	 */
	public String getNumeroAbono() {
		return numeroAbono;
	}
	/**
	 * @param numeroAbono The numeroAbono to set.
	 */
	public void setNumeroAbono(String numeroAbono) {
		this.numeroAbono = numeroAbono;
	}
	/**
	 * @return Returns the idAbono.
	 */
	public String getIdAbono() {
		return idAbono;
	}
	
	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	/**
	 * @param idAbono The idAbono to set.
	 */
	public void setIdAbono(String idAbono) {
		this.idAbono = idAbono;
	}
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
		
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	
}
