package com.siga.beans;

import org.redabogacia.sigaservices.app.autogen.model.GenFichero;

/**
 * @author Carlos Ruano Martínez 
 * @date 11/06/2015
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class GenFicheroBean extends MasterBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 643697222569449399L;
	GenFichero fichero = new GenFichero();

	public GenFichero getFichero() {
		return fichero;
	}

	public void setFichero(GenFichero fichero) {
		this.fichero = fichero;
	}
	
}
