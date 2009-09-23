/*
 * VERSIONES:
 * 
 * miguel.villegas - 22-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de devolucion <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;

import org.apache.struts.upload.FormFile;


public class DevolucionesForm extends MasterForm{
	

	private String idInstitucion="";
	protected FormFile ruta;
	private String comisiones="";
	private String idDisqueteDevoluciones="";

		
	/**
	 * @return Returns the idDisqueteDevoluciones.
	 */
	public String getIdDisqueteDevoluciones() {
		return idDisqueteDevoluciones;
	}
	/**
	 * @param idDisqueteDevoluciones The idDisqueteDevoluciones to set.
	 */
	public void setIdDisqueteDevoluciones(String idDisqueteDevoluciones) {
		this.idDisqueteDevoluciones = idDisqueteDevoluciones;
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
	 * @return Returns the ruta.
	 */
	public FormFile getRuta() {
		return ruta;
	}
	/**
	 * @param ruta The ruta to set.
	 */
	public void setRuta(FormFile ruta) {
		this.ruta = ruta;
	}
	
	/**
	 * @return Returns the comision.
	 */
	public String getComisiones() {
		return comisiones;
	}
	/**
	 * @param ruta The comisiones to set.
	 */
	public void setComisiones(String comisiones) {
		this.comisiones = comisiones;
	}
}