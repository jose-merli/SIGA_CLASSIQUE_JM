/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.form;

import com.siga.administracion.SIGAConstants;
import com.siga.consultas.CriterioDinamico;
import com.siga.general.MasterForm;

/**
 * Formulario para recuperar consultas
 */
public class RecuperarConsultasForm extends MasterForm {
	
	private String descripcion="";
	private String idModulo="";
	private boolean todos;
	private String idInstitucion="";
	private String idConsulta="";
	private String tipoConsulta="";
	private String tipoEnvio="";
	private CriterioDinamico [] criteriosDinamicos = new CriterioDinamico[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	
	
	
	public String getTipoEnvio() {
		return tipoEnvio;
	}
	public void setTipoEnvio(String tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public CriterioDinamico getCriteriosDinamicos(int index) {
		if (criteriosDinamicos[index]!=null){
			return criteriosDinamicos[index];
		}else{
			criteriosDinamicos[index]=new CriterioDinamico();
			return criteriosDinamicos[index];
		}
	}
	public void setCriteriosDinamicos(int index, CriterioDinamico value) {
		this.criteriosDinamicos[index] = value;
	}
	
	public String getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(String idConsulta) {
		this.idConsulta = idConsulta;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public boolean isTodos() {
		return todos;
	}
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(String idModulo) {
		this.idModulo = idModulo;
	}
	public CriterioDinamico[] getCriteriosDinamicos() {
		return criteriosDinamicos;
	}
	/*	public void setCriteriosDinamicos(CriterioDinamico[] criteriosDinamicos) {
		this.criteriosDinamicos = criteriosDinamicos;
	}
*/	
}
