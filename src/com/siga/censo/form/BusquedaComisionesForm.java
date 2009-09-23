

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de los productos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;

public class BusquedaComisionesForm extends MasterForm{
	

	private String comision="";
	private String cargos="";	
	private String fechaCargo="";

	// Metodos get y set
	
	// Formulario Busqueda Comisiones	
	
	

	/**
	 * @return Returns the cargos.
	 */
	public String getCargos() {
		return cargos;
	}
	/**
	 * @param cargos The cargos to set.
	 */
	public void setCargos(String cargos) {
		this.cargos = cargos;
	}
	/**
	 * @return Returns the comision.
	 */
	public String getComision() {
		return comision;
	}
	/**
	 * @param comision The comision to set.
	 */
	public void setComision(String comision) {
		this.comision = comision;
	}
	/**
	 * @return Returns the fechaCargo.
	 */
	public String getFechaCargo() {
		return fechaCargo;
	}
	/**
	 * @param fechaCargo The fechaCargo to set.
	 */
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
}