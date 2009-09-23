
package com.siga.administracion.form;

import com.siga.general.MasterForm;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParametrosGeneralesForm extends MasterForm {
	
	String idModulo = "";
	String checkParametrosGenerales = "";
	String datosModificados = "";
	
	/**
	 * @return Returns the checkParametrosGenerales.
	 */
	public String getCheckParametrosGenerales() {
		return checkParametrosGenerales;
	}
	/**
	 * @param checkParametrosGenerales The checkParametrosGenerales to set.
	 */
	public void setCheckParametrosGenerales(String checkParametrosGenerales) {
		this.checkParametrosGenerales = checkParametrosGenerales;
	}
	/**
	 * @return Returns the idModulo.
	 */
	public String getIdModulo() {
		return idModulo;
	}
	/**
	 * @param idModulo The idModulo to set.
	 */
	public void setIdModulo(String idModulo) {
		this.idModulo = idModulo;
	}
	
	/**
	 * @return Returns the datosModificados.
	 */
	public String getDatosModificados() {
		return datosModificados;
	}
	/**
	 * @param datosModificados The datosModificados to set.
	 */
	public void setDatosModificados(String datosModificados) {
		this.datosModificados = datosModificados;
	}
}
