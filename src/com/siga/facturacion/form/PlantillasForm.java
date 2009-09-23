/*
 * VERSIONES:
 * 
 * miguel.villegas - 25-04-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de mantenimiento de plantillas <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;



public class PlantillasForm extends MasterForm{
	

	private String idInstitucion="";
	private String idPlantilla="";
	private String descripcion="";
	private String plantillaPDF="";

		
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return Returns the idPlantilla.
	 */
	public String getIdPlantilla() {
		return idPlantilla;
	}
	/**
	 * @param idPlantilla The idPlantilla to set.
	 */
	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	/**
	 * @return Returns the plantillaPDF.
	 */
	public String getPlantillaPDF() {
		return plantillaPDF;
	}
	/**
	 * @param plantillaPDF The plantillaPDF to set.
	 */
	public void setPlantillaPDF(String plantillaPDF) {
		this.plantillaPDF = plantillaPDF;
	}
}