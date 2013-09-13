package com.siga.censo.ws.form;

import java.util.List;

import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterForm;

public class EdicionRemesaForm extends MasterForm {
	
	private Long idcensows = null;
	private Short idinstitucion = null;	
	private String numeroPeticion = null;
	private String fechapeticion = null;
	
	private String nombreColegio = null;	
	private String coderror = null;
	private String descerror = null;
	
	private String numeroColegiado = null;
	private String nombre = null;
	private String primerApellido = null;
	private String segundoApellido = null;
	private String idTipoIdentificacion = null;
	private String identificacion = null;
	private String idIncidencias = null;
	
	private List<ValueKeyVO> tiposIdentificacion;
	private Integer incidencias;
	
	public void reset() {
		idcensows = null;
		idinstitucion = null;	
		numeroPeticion = null;
		fechapeticion = null;
		
		nombreColegio = null;	
		coderror = null;
		descerror = null;
		
		numeroColegiado = null;
		nombre = null;
		primerApellido = null;
		segundoApellido = null;
		idTipoIdentificacion = null;
		identificacion = null;
		idIncidencias = null;
	}
		
	
	
	public String getNumeroPeticion() {
		return numeroPeticion;
	}
	public void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	
	public Long getIdcensows() {
		return idcensows;
	}
	public void setIdcensows(Long idcensows) {
		this.idcensows = idcensows;
	}
	public String getNombreColegio() {
		return nombreColegio;
	}
	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}
	
	public String getFechapeticion() {
		return fechapeticion;
	}
	public void setFechapeticion(String fechapeticion) {
		this.fechapeticion = fechapeticion;
	}
	public String getCoderror() {
		return coderror;
	}
	public void setCoderror(String coderror) {
		this.coderror = coderror;
	}
	public String getDescerror() {
		return descerror;
	}
	public void setDescerror(String descerror) {
		this.descerror = descerror;
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
	public String getIdIncidencias() {
		return idIncidencias;
	}
	public void setIdIncidencias(String idIncidencias) {
		this.idIncidencias = idIncidencias;
	}


	public List<ValueKeyVO> getTiposIdentificacion() {
		return tiposIdentificacion;
	}


	public void setTiposIdentificacion(List<ValueKeyVO> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}



	public Short getIdinstitucion() {
		return idinstitucion;
	}



	public void setIdinstitucion(Short idinstitucion) {
		this.idinstitucion = idinstitucion;
	}



	public Integer getIncidencias() {
		return incidencias;
	}



	public void setIncidencias(Integer incidencias) {
		this.incidencias = incidencias;
	}
}
