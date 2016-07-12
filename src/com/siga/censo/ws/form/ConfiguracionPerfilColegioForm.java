package com.siga.censo.ws.form;

import java.util.List;

import org.redabogacia.sigaservices.app.vo.ColegioPerfilColumnaVO;
import org.redabogacia.sigaservices.app.vo.ColumnaPerfilVO;

import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterForm;

public class ConfiguracionPerfilColegioForm extends MasterForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1688495257915289277L;
	
	private List<InstitucionVO> instituciones;
	private String idColegio = null;
	private String tipoColumna = null;
	private String idColegioInsertar =null;
	private List<ColegioPerfilColumnaVO> listadoPerfil;
	private List<ColumnaPerfilVO> tiposColumna;
	private String nombreColumna = null;
	private String idPerfil=null;
	
	
	
	public String getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(String idPerfil) {
		this.idPerfil = idPerfil;
	}
	public String getNombreColumna() {
		return nombreColumna;
	}
	public void setNombreColumna(String nombreColumna) {
		this.nombreColumna = nombreColumna;
	}
	public String getTipoColumna() {
		return tipoColumna;
	}
	public void setTipoColumna(String tipoColumna) {
		this.tipoColumna = tipoColumna;
	}
	public String getIdColegioInsertar() {
		return idColegioInsertar;
	}
	public void setIdColegioInsertar(String idColegioInsertar) {
		this.idColegioInsertar = idColegioInsertar;
	}
	public List<ColumnaPerfilVO> getTiposColumna() {
		return tiposColumna;
	}
	public void setTiposColumna(List<ColumnaPerfilVO> tiposColumna) {
		this.tiposColumna = tiposColumna;
	}
	
	public List<ColegioPerfilColumnaVO> getListadoPerfil() {
		return listadoPerfil;
	}
	public void setListadoPerfil(List<ColegioPerfilColumnaVO> listadoPerfil) {
		this.listadoPerfil = listadoPerfil;
	}
	public List<InstitucionVO> getInstituciones() {
		return instituciones;
	}
	public void setInstituciones(List<InstitucionVO> instituciones) {
		this.instituciones = instituciones;
	}
	public String getIdColegio() {
		return idColegio;
	}
	public void setIdColegio(String idColegio) {
		this.idColegio = idColegio;
	}
	
	

}
