package com.siga.censo.vos;

import java.util.Date;

public class ClienteVO extends PersonaVO {

	private Date factFechaAltaHasta;
	private Date factFechaAltaDesde;
	private Date fechaNacimientoDesde;
	private Date fechaNacimientoHasta;
	private String codigoPostal;
	private String fax;
	private Integer gruposFijosIdGrupo;
	private Integer gruposFijosIdInstitucion;
	private Integer tipoApunteCV;
	private Integer idTipoApunteCVSubtipo1;
	private Integer tipoApunteCVSubtipo1IdInstitucion;
	private Integer idTipoApunteCVSubtipo2;
	private Integer tipoApunteCVSubtipo2IdInstitucion;
	private String domicilio;
	private String sexo;
	private String correoElectronico;
	private String telefono;
	private String idInstitucion;
	private Date fechaIncorporacionDesde;
	private String nombreInstitucion;
	private Integer estadoColegial;
	private String descEstadoColegial;
	
	public ClienteVO(){
	}

	public Date getFactFechaAltaHasta() {
		return factFechaAltaHasta;
	}

	public void setFactFechaAltaHasta(Date factFechaAltaHasta) {
		this.factFechaAltaHasta = factFechaAltaHasta;
	}

	public Date getFactFechaAltaDesde() {
		return factFechaAltaDesde;
	}

	public void setFactFechaAltaDesde(Date factFechaAltaDesde) {
		this.factFechaAltaDesde = factFechaAltaDesde;
	}

	public Date getFechaNacimientoDesde() {
		return fechaNacimientoDesde;
	}

	public void setFechaNacimientoDesde(Date fechaNacimientoDesde) {
		this.fechaNacimientoDesde = fechaNacimientoDesde;
	}

	public Date getFechaNacimientoHasta() {
		return fechaNacimientoHasta;
	}

	public void setFechaNacimientoHasta(Date fechaNacimientoHasta) {
		this.fechaNacimientoHasta = fechaNacimientoHasta;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getGruposFijosIdGrupo() {
		return gruposFijosIdGrupo;
	}

	public void setGruposFijosIdGrupo(Integer gruposFijosIdGrupo) {
		this.gruposFijosIdGrupo = gruposFijosIdGrupo;
	}

	public Integer getGruposFijosIdInstitucion() {
		return gruposFijosIdInstitucion;
	}
	
	public void setGruposFijosIdInstitucion(Integer gruposFijosIdInstitucion) {
		this.gruposFijosIdInstitucion = gruposFijosIdInstitucion;
	}

	public Integer getTipoApunteCV() {
		return tipoApunteCV;
	}

	public void setTipoApunteCV(Integer tipoApunteCV) {
		this.tipoApunteCV = tipoApunteCV;
	}

	public Integer getIdTipoApunteCVSubtipo1() {
		return idTipoApunteCVSubtipo1;
	}

	public void setIdTipoApunteCVSubtipo1(Integer idTipoApunteCVSubtipo1) {
		this.idTipoApunteCVSubtipo1 = idTipoApunteCVSubtipo1;
	}

	public Integer getIdTipoApunteCVSubtipo2() {
		return idTipoApunteCVSubtipo2;
	}

	public void setIdTipoApunteCVSubtipo2(Integer idTipoApunteCVSubtipo2) {
		this.idTipoApunteCVSubtipo2 = idTipoApunteCVSubtipo2;
	}
	
	public Integer getTipoApunteCVSubtipo1IdInstitucion() {
		return tipoApunteCVSubtipo1IdInstitucion;
	}

	public void setTipoApunteCVSubtipo1IdInstitucion(Integer tipoApunteCVSubtipo1IdInstitucion) {
		this.tipoApunteCVSubtipo1IdInstitucion = tipoApunteCVSubtipo1IdInstitucion;
	}

	public Integer getTipoApunteCVSubtipo2IdInstitucion() {
		return tipoApunteCVSubtipo2IdInstitucion;
	}
	
	public void setTipoApunteCVSubtipo2IdInstitucion(Integer tipoApunteCVSubtipo2IdInstitucion) {
		this.tipoApunteCVSubtipo2IdInstitucion = tipoApunteCVSubtipo2IdInstitucion;
	}


	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public Date getFechaIncorporacionDesde() {
		return fechaIncorporacionDesde;
	}

	public void setFechaIncorporacionDesde(Date fechaIncorporacionDesde) {
		this.fechaIncorporacionDesde = fechaIncorporacionDesde;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public Integer getEstadoColegial() {
		return estadoColegial;
	}

	public void setEstadoColegial(Integer estadoColegial) {
		this.estadoColegial = estadoColegial;
	}

	public void setDescEstadoColegial(String descEstadoColegial) {
		this.descEstadoColegial = descEstadoColegial;
	}

	public String getDescEstadoColegial() {
		return descEstadoColegial;
	}

	
}
