/*
 * Created on Mar 10, 2005
 * @author emilio.grau
 *
 */
package com.siga.consultas.form;

import com.siga.administracion.SIGAConstants;
import com.siga.consultas.Campo;
import com.siga.general.MasterForm;

/**
 * Formulario para la edición de consultas
 */
public class EditarConsultaForm extends MasterForm {
	
	private String idInstitucion="";
	private String idConsulta="";
	private String descripcion="";
	private String tipoConsulta="";
	private String modulo="";
	private String moduloSel="";
	private boolean general;
	private String tema="";
	private String tipoCampo="";
	private String campo="";
	private String cabecera="";
	private String tablaPrioritaria="";
	private String tablas="";
	private Campo [] camposSalida = new Campo[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	private Campo [] camposOrden = new Campo[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	private Campo [] camposAgregacion = new Campo[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	private Campo [] criteriosDinamicos = new Campo[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	private String[] criterios = new String[SIGAConstants.TAMANYO_ARRAY_CONSULTA];
	private String criterioModif = "";
	private String consulta="";
	private String consultaExperta="";
	
	
	
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public String getConsultaExperta() {
		return consultaExperta;
	}
	public void setConsultaExperta(String consultaExperta) {
		this.consultaExperta = consultaExperta;
	}
	public String getCriterioModif() {
		return criterioModif;
	}
	public void setCriterioModif(String criterioModif) {
		this.criterioModif = criterioModif;
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
	public Campo[] getCamposSalida() {
		return camposSalida;
	}
	public void setCamposSalida(Campo[] camposSalida) {
		this.camposSalida = camposSalida;
	}	
	public Campo[] getCamposAgregacion() {
		return camposAgregacion;
	}
	public void setCamposAgregacion(Campo[] camposAgregacion) {
		this.camposAgregacion = camposAgregacion;
	}
	public Campo[] getCamposOrden() {
		return camposOrden;
	}
	public void setCamposOrden(Campo[] camposOrden) {
		this.camposOrden = camposOrden;
	}
	public String[] getCriterios() {
		return criterios;
	}
	public void setCriterios(String[] criterios) {
		this.criterios = criterios;
	}
	public Campo[] getCriteriosDinamicos() {
		return criteriosDinamicos;
	}
	public void setCriteriosDinamicos(Campo[] criteriosDinamicos) {
		this.criteriosDinamicos = criteriosDinamicos;
	}
	public String getTablas() {
		return tablas;
	}
	public void setTablas(String tablas) {
		this.tablas = tablas;
	}
	
	public String getCriterios(int index) {
		return criterios[index];		
	}
	public void setCriterios(int index, String value) {
		if (criterios[index]!=null){
			this.criterios[index] = value;
		}else{
			criterios[index]="";
			this.criterios[index] = value;
		}
	}
	public Campo getCamposSalida(int index) {
		if (camposSalida[index]!=null){
			return camposSalida[index];
		}else{
			camposSalida[index]=new Campo();
			return camposSalida[index];
		}	
	}
	public void setCamposSalida(int index, Campo value) {
		this.camposSalida[index] = value;
	}
	public Campo getCamposOrden(int index) {
		if (camposOrden[index]!=null){
			return camposOrden[index];
		}else{
			camposOrden[index]=new Campo();
			return camposOrden[index];
		}
	}
	public void setCamposOrden(int index, Campo value) {
		this.camposOrden[index] = value;
	}
	
	public Campo getCamposAgregacion(int index) {
		if (camposAgregacion[index]!=null){
			return camposAgregacion[index];
		}else{
			camposAgregacion[index]=new Campo();
			return camposAgregacion[index];
		}
	}
	public void setCamposAgregacion(int index, Campo value) {
		this.camposAgregacion[index] = value;		
	}
	public Campo getCriteriosDinamicos(int index) {
		if (criteriosDinamicos[index]!=null){
			return criteriosDinamicos[index];
		}else{
			criteriosDinamicos[index]=new Campo();
			return criteriosDinamicos[index];
		}
	}
	public void setCriteriosDinamicos(int index, Campo value) {
		this.criteriosDinamicos[index] = value;
	}
	public String getTipoCampo() {
		return tipoCampo;
	}
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	public boolean isGeneral() {
		return general;
	}
	public void setGeneral(boolean general) {
		this.general = general;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	public String getModuloSel() {
		return moduloSel;
	}
	public void setModuloSel(String moduloSel) {
		this.moduloSel = moduloSel;
	}
	public String getCabecera() {
		return cabecera;
	}
	public void setCabecera(String cabecera) {
		this.cabecera = cabecera;
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getSelectExperta() {
		return consulta;
	}
	public void setSelectExperta(String consulta) {
		this.consulta = consulta;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	public String getTablaPrioritaria() {
		return tablaPrioritaria;
	}
	public void setTablaPrioritaria(String tablaPrioritaria) {
		this.tablaPrioritaria = tablaPrioritaria;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}

		
}
