package com.siga.beans;

public class FacSerieFacturacionBean extends MasterBean {

	private static final long serialVersionUID = 3143948645003008848L;
	private Integer idInstitucion, idPlantilla, idTipoPlantillaMail, idTipoEnvios,idNombreDescargaPDF;
	private Long idSerieFacturacion,idSerieFacturacionPrevia;
	private String descripcion, nombreAbreviado, envioFactura,generarPDF, idContador, idContadorAbonos, configDeudor, configIngresos, cuentaIngresos, cuentaClientes,tipoSerie, observaciones, fechaBaja, traspasoFacturas, traspasoPlantilla, traspasoCodAuditoriaDef;
	private String[] formaPagoAutomática;				

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_SERIEFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION = "IDSERIEFACTURACION";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_NOMBREABREVIADO = "NOMBREABREVIADO";
	static public final String C_ENVIOFACTURA = "ENVIOFACTURAS";
	static public final String C_GENERARPDF = "GENERARPDF";
	static public final String C_IDCONTADOR = "IDCONTADOR";
	static public final String C_IDCONTADOR_ABONOS = "IDCONTADOR_ABONOS";
	static public final String C_CONFDEUDOR = "CONFDEUDOR";		
	static public final String C_CONFINGRESOS = "CONFINGRESOS";
	static public final String C_CTAINGRESOS = "CTAINGRESOS";
	static public final String C_CTACLIENTES = "CTACLIENTES";
	static public final String C_OBSERVACIONES = "OBSERVACIONES";
	static public final String C_TIPOSERIE = "TIPOSERIE";
	static public final String C_IDTIPOPLANTILLAMAIL = "IDTIPOPLANTILLAMAIL";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDFORMAPAGO = "IDFORMAPAGO";
	static public final String C_IDSERIEFACTURACIONPREVIA = "IDSERIEFACTURACIONPREVIA";
	static public final String C_FECHABAJA = "FECHABAJA";
	static public final String C_IDNOMBREDESCARGAPDF = "ID_NOMBRE_DESCARGA_FAC";
	static public final String C_TRASPASOFACTURAS = "TRASPASOFACTURAS";
	static public final String C_TRASPASOPLANTILLA = "TRASPASO_PLANTILLA";
	static public final String C_TRASPASOCODAUDITORIADEF = "TRASPASO_CODAUDITORIA_DEF";
	
	
	
	// Metodos SET
	public void setIdInstitucion (Integer dato) {this.idInstitucion = dato;}
	public void setIdSerieFacturacion (Long dato) {this.idSerieFacturacion = dato;}
	public void setIdPlantilla (Integer dato) {this.idPlantilla = dato;}
	public void setIdNombreDescargaPDF (Integer dato) {this.idNombreDescargaPDF = dato;}
	public void setTraspasoFacturas (String dato) {this.traspasoFacturas = dato;}
	public void setTraspasoPlantilla (String dato) {this.traspasoPlantilla = dato;}
	public void setTraspasoCodAuditoriaDef (String dato) {this.traspasoCodAuditoriaDef = dato;}
	public void setDescripcion (String dato) {this.descripcion = dato;}
	public void setNombreAbreviado (String dato) {this.nombreAbreviado = dato;}
	public void setEnvioFactura (String dato) {this.envioFactura = dato;}
	public void setGenerarPDF (String dato) {this.generarPDF = dato;}
	public void setIdContador (String dato) {this.idContador = dato;}
	public void setIdContadorAbonos (String dato) {this.idContadorAbonos = dato;}
	public void setConfigDeudor(String dato) {this.configDeudor = dato;}
	public void setConfigIngresos(String dato) {this.configIngresos = dato;}
	public void setCuentaClientes(String dato) {this.cuentaClientes = dato;}
	public void setCuentaIngresos(String dato) {this.cuentaIngresos = dato;}
	public void setObservaciones (String dato) {this.observaciones = dato;}
	public void setTipoSerie(String dato) {this.tipoSerie = dato;}
	public void setIdTipoPlantillaMail(Integer dato) {this.idTipoPlantillaMail = dato;}
	public void setIdTipoEnvios(Integer dato) {this.idTipoEnvios = dato;}
	public void setIdSerieFacturacionPrevia(Long dato) {this.idSerieFacturacionPrevia = dato;}	
	public void setFechaBaja(String dato) {this.fechaBaja = dato;}
	
	public void setFormaPagoAutomática(String[] dato) {this.formaPagoAutomática = dato;}
	
	// Metodos GET
	public Integer getIdInstitucion() {return this.idInstitucion;}
	public Long getIdSerieFacturacion()	{return this.idSerieFacturacion;}
	public Integer getIdPlantilla() {return this.idPlantilla;}
	public Integer getIdNombreDescargaPDF() {return this.idNombreDescargaPDF;}
	public String getTraspasoFacturas() {return this.traspasoFacturas;}
	public String getTraspasoPlantilla() {return this.traspasoPlantilla;}
	public String getTraspasoCodAuditoriaDef() {return this.traspasoCodAuditoriaDef;}
	public String getDescripcion() {return this.descripcion;}
	public String getNombreAbreviado() {return this.nombreAbreviado;}
	public String getEnvioFactura() {return this.envioFactura;}
	public String getGenerarPDF() {return this.generarPDF;}
	public String getIdContador() {return this.idContador;}
	public String getIdContadorAbonos() {return this.idContadorAbonos;}
	public String getConfigDeudor() {return configDeudor;}
	public String getConfigIngresos() {return configIngresos;}
	public String getCuentaClientes() {return cuentaClientes;}
	public String getCuentaIngresos() {return cuentaIngresos;}
	public String getObservaciones() {return this.observaciones;}
	public String getTipoSerie() {return tipoSerie;}	
	public Integer getIdTipoPlantillaMail() {return idTipoPlantillaMail;}
	public Integer getIdTipoEnvios() {return idTipoEnvios;}
	public Long getIdSerieFacturacionPrevia() {return idSerieFacturacionPrevia;}
	public String getFechaBaja() {return fechaBaja;}
	
	public String[] getFormaPagoAutomática() {
		// Al cambiar el tipo de campo en la jsp es necesario partir la cadena
		if (formaPagoAutomática.length == 1) {
			return formaPagoAutomática[0].split(",");
		}		
		return formaPagoAutomática;
	}
}