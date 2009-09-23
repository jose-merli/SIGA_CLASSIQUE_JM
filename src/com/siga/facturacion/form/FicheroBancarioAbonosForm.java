/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 29-03-2005 - Inicio
 * jose.barrientos - 28-02-2009 - Añadido el tratamiento del campo sjcs
 */
package com.siga.facturacion.form;

import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FicheroBancarioAbonosForm extends MasterForm{

	private String sjcs;

	public String getSjcs() {
		return sjcs;
	}

	public void setSjcs(String sjcs) {
		this.sjcs = sjcs;
	} 
	
	
}
