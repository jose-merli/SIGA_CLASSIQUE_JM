package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 25/01/2006
 *
 */
public class PestanaDelitoDesignaForm extends MasterForm {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3381420576284183681L;
	private Integer idDelito, anio, numero, idTurno;
	private String idInstitucion;
	
	
	/**
	 * @return the idInstitucion
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idDelito.
	 */
	public Integer getIdDelito() {
		return idDelito;
	}
	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the numero.
	 */
	public Integer getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	/**
	 * @param idDelito The idDelito to set.
	 */
	public void setIdDelito(Integer idDelito) {
		this.idDelito = idDelito;
	}
	/**
	 * @return Returns the idTurno.
	 */
	public Integer getIdTurno() {
		return idTurno;
	}
	/**
	 * @param idTurno The idTurno to set.
	 */
	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
}