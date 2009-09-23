package com.siga.gratuita.form;

import com.siga.general.MasterForm;

/**
 * @author david.sanchez
 * @since 25/01/2006
 *
 */
public class PestanaDelitoAsistenciaForm extends MasterForm {
	

	private Integer idDelito, anio, numero;
	private String delito=null;
	
	
	/**
	 * @return Returns the delito.
	 */
	public String getDelito() {
		return delito;
	}
	/**
	 * @param delito The delito to set.
	 */
	public void setDelito(String delito) {
		this.delito = delito;
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