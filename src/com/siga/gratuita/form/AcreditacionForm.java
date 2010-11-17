package com.siga.gratuita.form;

import java.util.List;

import com.siga.general.MasterForm;

/**
 * @author jorgeta
 *
 * @version 05/10/2010
 */

public class AcreditacionForm extends MasterForm
{

	private String descripcion;
	private String id;
	private String idTipo;
	private String porcentaje;
	private String validada;
	private String idJuzgado;
	private String idProcedimiento;
	private String idJurisdiccion;
	
	
	private int rowSpan;
	
	
	
	public int getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
	
	
	public String getValidada() {
		return validada;
	}
	public void setValidada(String validada) {
		this.validada = validada;
	}
	public String getDescripcion() {
		StringBuffer auxDescripcion = new StringBuffer();
		auxDescripcion.append(descripcion);
		if(porcentaje!=null){
			auxDescripcion.append("(");
			auxDescripcion.append(porcentaje);
			auxDescripcion.append("%)");
		}
		descripcion = auxDescripcion.toString();
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	public String getIdJuzgado() {
		return idJuzgado;
	}
	public void setIdJuzgado(String idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	public String getIdJurisdiccion() {
		return idJurisdiccion;
	}
	public void setIdJurisdiccion(String idJurisdiccion) {
		this.idJurisdiccion = idJurisdiccion;
	}
	
	
}