/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
 */

/**
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;

public class GruposFijosForm extends MasterForm{

	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getIdGrupoClienteFijo() {
		return idGrupoClienteFijo;
	}
	public void setIdGrupoClienteFijo(String idGrupoClienteFijo) {
		this.idGrupoClienteFijo = idGrupoClienteFijo;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(String idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	private String idGrupo = null;
	private String idInstitucion = null;
	private String idSerieFacturacion = null;
	private String idGrupoClienteFijo = null;
	
}
