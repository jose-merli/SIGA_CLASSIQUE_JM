package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class PestanaDelitoEJGForm extends MasterForm {
	

	private Integer idDelito, anio, numero, idTipoEJG;
	
	
	/**
	 * @return Returns the idTipoEJG.
	 */
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	/**
	 * @param idTipoEJG The idTipoEJG to set.
	 */
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
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
}