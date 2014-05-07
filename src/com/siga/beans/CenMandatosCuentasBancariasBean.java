package com.siga.beans;

import com.siga.censo.form.MandatosCuentasBancariasForm;

/**
 * Bean de la tabla CEN_MANDATOS_CUENTASBANCARIAS 
 */
public class CenMandatosCuentasBancariasBean extends MasterBean {

	/* Variables */	
	private String idInstitucion, idPersona, idCuenta, idMandato, tipoMandato, fechaCreacion, usuCreacion, refMandatoSepa, tipoPago, esquema, autorizacionB2B;
	private String acreedorTipoId, acreedorId, acreedorNombre, acreedorDomicilio, acreedorCodigoPostal, acreedorIdPais, acreedorPais, acreedorIdProvincia, acreedorProvincia, acreedorIdPoblacion, acreedorPoblacion;
	private String deudorTipoId, deudorId, deudorNombre, deudorDomicilio, deudorCodigoPostal, deudorIdPais, deudorPais, deudorIdProvincia, deudorProvincia, deudorIdPoblacion, deudorPoblacion;
	private String firmaFecha, firmaLugar, idFicheroFirma;
	private String fechaUso, iban, bic, banco;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_MANDATOS_CUENTASBANCARIAS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDCUENTA = "IDCUENTA";
	static public final String C_IDMANDATO = "IDMANDATO";
	static public final String C_TIPOMANDATO = "TIPOMANDATO";
	static public final String C_FECHACREACION = "FECHACREACION";
	static public final String C_USUCREACION = "USUCREACION";
	static public final String C_REFMANDATOSEPA = "REFMANDATOSEPA";
	static public final String C_TIPOPAGO = "TIPOPAGO";	
	static public final String C_ESQUEMA = "ESQUEMA";
	static public final String C_AUTORIZACIONB2B = "AUTORIZACIONB2B";
	static public final String C_ACREEDOR_TIPOID = "ACREEDOR_TIPOID";
	static public final String C_ACREEDOR_ID = "ACREEDOR_ID";
	static public final String C_ACREEDOR_NOMBRE = "ACREEDOR_NOMBRE";
	static public final String C_ACREEDOR_DOMICILIO = "ACREEDOR_DOMICILIO";
	static public final String C_ACREEDOR_CODIGOPOSTAL = "ACREEDOR_CODIGOPOSTAL";
	static public final String C_ACREEDOR_IDPAIS = "ACREEDOR_IDPAIS";
	static public final String C_ACREEDOR_PAIS = "ACREEDOR_PAIS";
	static public final String C_ACREEDOR_IDPROVINCIA = "ACREEDOR_IDPROVINCIA";
	static public final String C_ACREEDOR_PROVINCIA = "ACREEDOR_PROVINCIA";
	static public final String C_ACREEDOR_IDPOBLACION = "ACREEDOR_IDPOBLACION";
	static public final String C_ACREEDOR_POBLACION = "ACREEDOR_POBLACION";
	static public final String C_DEUDOR_TIPOID = "DEUDOR_TIPOID";
	static public final String C_DEUDOR_ID = "DEUDOR_ID";
	static public final String C_DEUDOR_DOMICILIO = "DEUDOR_DOMICILIO";
	static public final String C_DEUDOR_CODIGOPOSTAL = "DEUDOR_CODIGOPOSTAL";
	static public final String C_DEUDOR_IDPAIS = "DEUDOR_IDPAIS";
	static public final String C_DEUDOR_PAIS = "DEUDOR_PAIS";
	static public final String C_DEUDOR_IDPROVINCIA = "DEUDOR_IDPROVINCIA";
	static public final String C_DEUDOR_PROVINCIA = "DEUDOR_PROVINCIA";
	static public final String C_DEUDOR_IDPOBLACION = "DEUDOR_IDPOBLACION";
	static public final String C_DEUDOR_POBLACION = "DEUDOR_POBLACION";
	static public final String C_FIRMA_FECHA = "FIRMA_FECHA";
	static public final String C_FIRMA_LUGAR = "FIRMA_LUGAR";
	static public final String C_FECHAUSO = "FECHAUSO";
	static public final String C_IDFICHEROFIRMA = "IDFICHEROFIRMA";
	
	static public final String C_DEUDOR_NOMBRE = "DEUDOR_NOMBRE";	
	static public final String C_IBAN = "IBAN";
	static public final String C_BIC = "BIC";
	static public final String C_BANCO = "BANCO";		
	
	public CenMandatosCuentasBancariasBean(MandatosCuentasBancariasForm formMandatos) {
		this.idInstitucion = formMandatos.getIdInstitucion();
		this.idPersona = formMandatos.getIdPersona();
		this.idCuenta = formMandatos.getIdCuenta();
		this.idMandato = formMandatos.getIdMandato();
		this.tipoMandato = formMandatos.getTipoMandato();
		this.fechaCreacion = formMandatos.getFechaCreacion();
		this.usuCreacion = formMandatos.getUsuCreacion();
		this.refMandatoSepa = formMandatos.getRefMandatoSepa();
		this.tipoPago = formMandatos.getTipoPago();
		this.esquema = formMandatos.getEsquema();
		this.autorizacionB2B = formMandatos.getAutorizacionB2B();
		this.acreedorTipoId = formMandatos.getAcreedorTipoId();
		this.acreedorId = formMandatos.getAcreedorId();
		this.acreedorNombre = formMandatos.getAcreedorNombre();
		this.acreedorDomicilio = formMandatos.getAcreedorDomicilio();
		this.acreedorCodigoPostal = formMandatos.getAcreedorCodigoPostal();
		this.acreedorIdPais = formMandatos.getAcreedorIdPais();
		this.acreedorPais = formMandatos.getAcreedorPais();
		this.acreedorIdProvincia = formMandatos.getAcreedorIdProvincia();
		this.acreedorProvincia = formMandatos.getAcreedorProvincia();
		this.acreedorIdPoblacion = formMandatos.getAcreedorIdPoblacion();
		this.acreedorPoblacion = formMandatos.getAcreedorPoblacion();
		this.deudorTipoId = formMandatos.getDeudorTipoId();
		this.deudorId = formMandatos.getDeudorId();
		this.deudorNombre = formMandatos.getDeudorNombre();
		this.deudorDomicilio = formMandatos.getDeudorDomicilio();
		this.deudorCodigoPostal = formMandatos.getDeudorCodigoPostal();
		this.deudorIdPais = formMandatos.getDeudorIdPais();
		this.deudorPais = formMandatos.getDeudorPais();
		this.deudorIdProvincia = formMandatos.getDeudorIdProvincia();
		this.deudorProvincia = formMandatos.getDeudorProvincia();
		this.deudorIdPoblacion = formMandatos.getDeudorIdPoblacion();
		this.deudorPoblacion = formMandatos.getDeudorPoblacion();
		this.firmaFecha = formMandatos.getFirmaFecha();
		this.firmaLugar = formMandatos.getFirmaLugar();
		this.idFicheroFirma = formMandatos.getIdFichero();
		this.fechaUso = formMandatos.getFechaUso();
		this.iban = formMandatos.getIban();
		this.bic = formMandatos.getBic();
		this.banco = formMandatos.getBanco();		
	}
	
	public CenMandatosCuentasBancariasBean() {
		super();
		this.idInstitucion = "";
		this.idPersona = "";
		this.idCuenta = "";
		this.idMandato = "";
		this.tipoMandato = "";
		this.fechaCreacion = "";
		this.usuCreacion = "";
		this.refMandatoSepa = "";		
		this.tipoPago = "";
		this.esquema = "";
		this.autorizacionB2B = "";
		this.acreedorTipoId = "";
		this.acreedorId = "";
		this.acreedorNombre = "";
		this.acreedorDomicilio = "";
		this.acreedorCodigoPostal = "";
		this.acreedorIdPais = "";
		this.acreedorPais = "";		
		this.acreedorIdProvincia = "";
		this.acreedorProvincia = "";		
		this.acreedorIdPoblacion = "";
		this.acreedorPoblacion = "";
		this.deudorTipoId = "";
		this.deudorId = "";
		this.deudorNombre = "";
		this.deudorDomicilio = "";
		this.deudorCodigoPostal = "";
		this.deudorIdPais = "";
		this.deudorPais = "";
		this.deudorIdProvincia = "";
		this.deudorProvincia = "";
		this.deudorIdPoblacion = "";
		this.deudorPoblacion = "";
		this.firmaFecha = "";
		this.firmaLugar = "";
		this.idFicheroFirma = "";
		this.fechaUso = "";
		this.iban = "";
		this.bic = "";
		this.banco = "";
	}
	
	public String getIdFicheroFirma() {
		return idFicheroFirma;
	}

	public void setIdFicheroFirma(String idFicheroFirma) {
		this.idFicheroFirma = idFicheroFirma;
	}	
	
	public String getUsuCreacion() {
		return usuCreacion;
	}

	public void setUsuCreacion(String usuCreacion) {
		this.usuCreacion = usuCreacion;
	}

	public String getAcreedorTipoId() {
		return acreedorTipoId;
	}

	public void setAcreedorTipoId(String acreedorTipoId) {
		this.acreedorTipoId = acreedorTipoId;
	}

	public String getDeudorTipoId() {
		return deudorTipoId;
	}

	public void setDeudorTipoId(String deudorTipoId) {
		this.deudorTipoId = deudorTipoId;
	}

	public String getAcreedorIdPais() {
		return acreedorIdPais;
	}

	public void setAcreedorIdPais(String acreedorIdPais) {
		this.acreedorIdPais = acreedorIdPais;
	}

	public String getAcreedorIdProvincia() {
		return acreedorIdProvincia;
	}

	public void setAcreedorIdProvincia(String acreedorIdProvincia) {
		this.acreedorIdProvincia = acreedorIdProvincia;
	}

	public String getAcreedorIdPoblacion() {
		return acreedorIdPoblacion;
	}

	public void setAcreedorIdPoblacion(String acreedorIdPoblacion) {
		this.acreedorIdPoblacion = acreedorIdPoblacion;
	}

	public String getDeudorIdPais() {
		return deudorIdPais;
	}

	public void setDeudorIdPais(String deudorIdPais) {
		this.deudorIdPais = deudorIdPais;
	}

	public String getDeudorIdProvincia() {
		return deudorIdProvincia;
	}

	public void setDeudorIdProvincia(String deudorIdProvincia) {
		this.deudorIdProvincia = deudorIdProvincia;
	}

	public String getDeudorIdPoblacion() {
		return deudorIdPoblacion;
	}

	public void setDeudorIdPoblacion(String deudorIdPoblacion) {
		this.deudorIdPoblacion = deudorIdPoblacion;
	}

	public String getFechaUso() {
		return fechaUso;
	}

	public void setFechaUso(String fechaUso) {
		this.fechaUso = fechaUso;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getIdInstitucion() {
		return idInstitucion;
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
	
	public String getRefMandatoSepa() {
		return refMandatoSepa;
	}

	public void setRefMandatoSepa(String refMandatoSepa) {
		this.refMandatoSepa = refMandatoSepa;
	}	

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getEsquema() {
		return esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	public String getAcreedorId() {
		return acreedorId;
	}

	public void setAcreedorId(String acreedorId) {
		this.acreedorId = acreedorId;
	}

	public String getAcreedorNombre() {
		return acreedorNombre;
	}

	public void setAcreedorNombre(String acreedorNombre) {
		this.acreedorNombre = acreedorNombre;
	}

	public String getAcreedorDomicilio() {
		return acreedorDomicilio;
	}

	public void setAcreedorDomicilio(String acreedorDomicilio) {
		this.acreedorDomicilio = acreedorDomicilio;
	}

	public String getAcreedorCodigoPostal() {
		return acreedorCodigoPostal;
	}

	public void setAcreedorCodigoPostal(String acreedorCodigoPostal) {
		this.acreedorCodigoPostal = acreedorCodigoPostal;
	}

	public String getAcreedorPais() {
		return acreedorPais;
	}

	public void setAcreedorPais(String acreedorPais) {
		this.acreedorPais = acreedorPais;
	}

	public String getAcreedorProvincia() {
		return acreedorProvincia;
	}

	public void setAcreedorProvincia(String acreedorProvincia) {
		this.acreedorProvincia = acreedorProvincia;
	}

	public String getAcreedorPoblacion() {
		return acreedorPoblacion;
	}

	public void setAcreedorPoblacion(String acreedorPoblacion) {
		this.acreedorPoblacion = acreedorPoblacion;
	}

	public String getDeudorId() {
		return deudorId;
	}

	public void setDeudorId(String deudorId) {
		this.deudorId = deudorId;
	}

	public String getDeudorNombre() {
		return deudorNombre;
	}

	public void setDeudorNombre(String deudorNombre) {
		this.deudorNombre = deudorNombre;
	}

	public String getDeudorDomicilio() {
		return deudorDomicilio;
	}

	public void setDeudorDomicilio(String deudorDomicilio) {
		this.deudorDomicilio = deudorDomicilio;
	}

	public String getDeudorCodigoPostal() {
		return deudorCodigoPostal;
	}

	public void setDeudorCodigoPostal(String deudorCodigoPostal) {
		this.deudorCodigoPostal = deudorCodigoPostal;
	}

	public String getDeudorPais() {
		return deudorPais;
	}

	public void setDeudorPais(String deudorPais) {
		this.deudorPais = deudorPais;
	}

	public String getDeudorProvincia() {
		return deudorProvincia;
	}

	public void setDeudorProvincia(String deudorProvincia) {
		this.deudorProvincia = deudorProvincia;
	}

	public String getDeudorPoblacion() {
		return deudorPoblacion;
	}

	public void setDeudorPoblacion(String deudorPoblacion) {
		this.deudorPoblacion = deudorPoblacion;
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

	public String getAutorizacionB2B() {
		return autorizacionB2B;
	}

	public void setAutorizacionB2B(String autorizacionB2B) {
		this.autorizacionB2B = autorizacionB2B;
	}
	
	public String getTipoMandato() {
		return tipoMandato;
	}

	public void setTipoMandato(String tipoMandato) {
		this.tipoMandato = tipoMandato;
	}
	
	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}	
}