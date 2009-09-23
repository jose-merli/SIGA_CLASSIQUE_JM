package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class PCAJGListadoTablasMaestrasForm extends MasterForm {

	private String modal;

	private String identificador;
	private String descripcion;
	private String codigo;
	private String abreviatura;
	private String campoFKSIGA;
	private String[] campoFKSIGAvalue;
	private String nombreTablaMaestra;
	private String tablaRelacion;

	private String aliasTabla = "";	
	

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public String getNombreTablaMaestra() {
		return nombreTablaMaestra;
	}

	public void setNombreTablaMaestra(String nombreTablaMaestra) {
		this.nombreTablaMaestra = nombreTablaMaestra;
	}

	public String getAliasTabla() {
		return aliasTabla;
	}

	public void setAliasTabla(String aliasTabla) {
		this.aliasTabla = aliasTabla;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the campoFKSIGA
	 */
	public String getCampoFKSIGA() {
		return campoFKSIGA;
	}

	/**
	 * @param campoFKSIGA the campoFKSIGA to set
	 */
	public void setCampoFKSIGA(String campoFKSIGA) {
		this.campoFKSIGA = campoFKSIGA;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the campoFKSIGAvalue
	 */
	public String[] getCampoFKSIGAvalue() {
		return campoFKSIGAvalue;
	}

	/**
	 * @param campoFKSIGAvalue the campoFKSIGAvalue to set
	 */
	public void setCampoFKSIGAvalue(String[] campoFKSIGAvalue) {
		this.campoFKSIGAvalue = campoFKSIGAvalue;
	}

	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * @return the tablaRelacion
	 */
	public String getTablaRelacion() {
		return tablaRelacion;
	}

	/**
	 * @param tablaRelacion the tablaRelacion to set
	 */
	public void setTablaRelacion(String tablaRelacion) {
		this.tablaRelacion = tablaRelacion;
	}

}