/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 22-03-2005 - Inicio
 */
package com.siga.facturacion.form;

import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FicheroBancarioPagosForm extends MasterForm{

	private String fechaCargo = "";
	
	
	public String getFechaCargo() {
		return fechaCargo;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
}
