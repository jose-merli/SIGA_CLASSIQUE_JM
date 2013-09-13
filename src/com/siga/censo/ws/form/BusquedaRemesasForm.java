package com.siga.censo.ws.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.comun.vos.InstitucionVO;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;

public class BusquedaRemesasForm extends MasterForm {
	
	private static final long serialVersionUID = -1964626954368394170L;
	
	private String idColegio = null;	
	private String numeroPeticion = null;
	private String fechaPeticionDesde = null;
	private String fechaPeticionHasta = null;
	private String numeroColegiado = null;
	private String nombre = null;
	private String primerApellido = null;
	private String segundoApellido = null;
	private String idTipoIdentificacion = null;
	private String identificacion = null;
	private boolean conIncidencia = false;
	private boolean conError = false;
	
	private List<InstitucionVO> instituciones;
	private List<ValueKeyVO> tiposIdentificacion;
	private String nombreColegio = null;
	
	public enum FILTRO_INCIDENCIAS {
		SIN_INCIDENCIAS
		, CON_INCIDENCIA
		, CON_INCIDENCIA_GENERAL
		, CON_INCIDENCIA_PARTICULAR
	}
		
	public void reset() {
		idColegio = null;	
		numeroPeticion = null;
		fechaPeticionDesde = null;
		fechaPeticionHasta = null;
		numeroColegiado = null;
		nombre = null;
		primerApellido = null;
		segundoApellido = null;
		idTipoIdentificacion = null;
		identificacion = null;
		conIncidencia = false;
		conError = false;
		nombreColegio = null;
	}




	public String getIdColegio() {
		return idColegio;
	}
	public void setIdColegio(String idColegio) {
		this.idColegio = idColegio;
	}
	public String getNumeroPeticion() {
		return numeroPeticion;
	}
	public void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	public String getFechaPeticionDesde() {
		return fechaPeticionDesde;
	}
	public void setFechaPeticionDesde(String fechaPeticionDesde) {
		this.fechaPeticionDesde = fechaPeticionDesde;
	}
	public String getFechaPeticionHasta() {
		return fechaPeticionHasta;
	}
	public void setFechaPeticionHasta(String fechaPeticionHasta) {
		this.fechaPeticionHasta = fechaPeticionHasta;
	}
	public String getNumeroColegiado() {
		return numeroColegiado;
	}
	public void setNumeroColegiado(String numeroColegiado) {
		this.numeroColegiado = numeroColegiado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getIdTipoIdentificacion() {
		return idTipoIdentificacion;
	}
	public void setIdTipoIdentificacion(String idTipoIdentificacion) {
		this.idTipoIdentificacion = idTipoIdentificacion;
	}
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	
	public List<InstitucionVO> getInstituciones() {
		return instituciones;
	}
	public void setInstituciones(List<InstitucionVO> instituciones) {
		this.instituciones = instituciones;
	}
	public String getNombreColegio() {
		return nombreColegio;
	}
	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}




	public List<ValueKeyVO> getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	public void setTiposIdentificacion(List<ValueKeyVO> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}




	public boolean isConIncidencia() {
		return conIncidencia;
	}




	public void setConIncidencia(boolean conIncidencia) {
		this.conIncidencia = conIncidencia;
	}




	public boolean isConError() {
		return conError;
	}




	public void setConError(boolean conError) {
		this.conError = conError;
	}

}
