package com.siga.beans;

import com.siga.censo.form.AnexosCuentasBancariasForm;

/**
 * Bean de la tabla CEN_ANEXOS_CUENTASBANCARIAS 
 */
public class CenAnexosCuentasBancariasBean extends MasterBean {

	/* Variables */	
	private String idInstitucion, idPersona, idCuenta, idMandato, idAnexo, fechaCreacion, usuCreacion, origen, descripcion;
	private String firmaFecha, firmaLugar, idFicheroFirma, fechaUso;

	public String getIdFicheroFirma() {
		return idFicheroFirma;
	}

	public void setIdFicheroFirma(String idFicheroFirma) {
		this.idFicheroFirma = idFicheroFirma;
	}

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ANEXOS_CUENTASBANCARIAS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDCUENTA = "IDCUENTA";
	static public final String C_IDMANDATO = "IDMANDATO";
	static public final String C_IDANEXO = "IDANEXO";
	static public final String C_FECHACREACION = "FECHACREACION";
	static public final String C_USUCREACION = "USUCREACION";
	static public final String C_FECHAORDEN = "FECHAORDEN";	
	static public final String C_ORIGEN = "ORIGEN";	
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_FIRMA_FECHA = "FIRMA_FECHA";
	static public final String C_FIRMA_LUGAR = "FIRMA_LUGAR";	
	static public final String C_IDFICHEROFIRMA = "IDFICHEROFIRMA";
	
	public CenAnexosCuentasBancariasBean(AnexosCuentasBancariasForm formAnexo) {
		this.idInstitucion = formAnexo.getIdInstitucion();
		this.idPersona = formAnexo.getIdPersona();
		this.idCuenta = formAnexo.getIdCuenta();
		this.idMandato = formAnexo.getIdMandato();
		this.idAnexo = formAnexo.getIdAnexo();
		this.fechaCreacion = formAnexo.getFechaCreacion();
		this.usuCreacion = formAnexo.getUsuCreacion();
		this.origen = formAnexo.getOrigen();
		this.descripcion = formAnexo.getDescripcion();
		this.firmaFecha = formAnexo.getFirmaFecha();
		this.firmaLugar = formAnexo.getFirmaLugar();
		this.idFicheroFirma = formAnexo.getIdFichero();
	}

	public CenAnexosCuentasBancariasBean() {
		super();
		this.idInstitucion = "";
		this.idPersona = "";
		this.idCuenta = "";
		this.idMandato = "";
		this.idAnexo = "";
		this.fechaCreacion = "";
		this.usuCreacion = ""; 
		this.origen = "";
		this.descripcion = "";
		this.firmaFecha = "";
		this.firmaLugar = "";
		this.idFicheroFirma = "";
		this.fechaUso = "";
	}
	
	
	public String getFechaUso() {
		return fechaUso;
	}

	public void setFechaUso(String fechaUso) {
		this.fechaUso = fechaUso;
	}	
	
	public String getIdInstitucion() {
		return idInstitucion;
	}

	public String getUsuCreacion() {
		return usuCreacion;
	}

	public void setUsuCreacion(String usuCreacion) {
		this.usuCreacion = usuCreacion;
	}

	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getIdMandato() {
		return idMandato;
	}

	public void setIdMandato(String idMandato) {
		this.idMandato = idMandato;
	}

	public String getIdAnexo() {
		return idAnexo;
	}

	public void setIdAnexo(String idAnexo) {
		this.idAnexo = idAnexo;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFirmaFecha() {
		return firmaFecha;
	}

	public void setFirmaFecha(String firmaFecha) {
		this.firmaFecha = firmaFecha;
	}

	public String getFirmaLugar() {
		return firmaLugar;
	}

	public void setFirmaLugar(String firmaLugar) {
		this.firmaLugar = firmaLugar;
	}
}