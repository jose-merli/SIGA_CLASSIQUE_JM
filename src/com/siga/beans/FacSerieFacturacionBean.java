/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creaci�n
 */

package com.siga.beans;


public class FacSerieFacturacionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idPlantilla, idTipoPlantillaMail, idTipoEnvios;
	
	private Long idSerieFacturacion;
	
	private String 	descripcion, nombreAbreviado, envioFactura,generarPDF, idContador, 

					configDeudor, configIngresos, cuentaIngresos, cuentaClientes,tipoSerie, observaciones;

	private String[] formaPagoAutom�tica;				


	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_SERIEFACTURACION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		 = "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION  = "IDSERIEFACTURACION";
	static public final String C_IDPLANTILLA 		 = "IDPLANTILLA";
	static public final String C_DESCRIPCION 		 = "DESCRIPCION";
	static public final String C_NOMBREABREVIADO 	 = "NOMBREABREVIADO";
	static public final String C_ENVIOFACTURA 		 = "ENVIOFACTURAS";
	static public final String C_GENERARPDF 		 = "GENERARPDF";
	static public final String C_IDCONTADOR 		 = "IDCONTADOR";
	static public final String C_CONFDEUDOR			 = "CONFDEUDOR";		
	static public final String C_CONFINGRESOS		 = "CONFINGRESOS";
	static public final String C_CTAINGRESOS		 = "CTAINGRESOS";
	static public final String C_CTACLIENTES		 = "CTACLIENTES";
	static public final String C_OBSERVACIONES 		 = "OBSERVACIONES";
	static public final String C_TIPOSERIE			 = "TIPOSERIE";
	static public final String C_IDTIPOPLANTILLAMAIL = "IDTIPOPLANTILLAMAIL";
	static public final String C_IDTIPOENVIOS		 = "IDTIPOENVIOS";
	static public final String C_IDFORMAPAGO	     = "IDFORMAPAGO";
	
	// Metodos SET
	public void setIdInstitucion (Integer id)		{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Long id)		{ this.idSerieFacturacion = id; }
	public void setIdPlantilla (Integer id)			{ this.idPlantilla = id; }
	public void setDescripcion (String d)			{ this.descripcion = d; }
	public void setNombreAbreviado (String n)		{ this.nombreAbreviado = n; }
	public void setEnvioFactura (String n)			{ this.envioFactura = n; }
	public void setGenerarPDF (String n)			{ this.generarPDF = n; }
	public void setIdContador (String n)			{ this.idContador = n; }
	public void setConfigDeudor(String configDeudor) 	 {	this.configDeudor = configDeudor;		}
	public void setConfigIngresos(String configIngresos) {	this.configIngresos = configIngresos;	}
	public void setCuentaClientes(String cuentaClientes) {	this.cuentaClientes = cuentaClientes;	}
	public void setCuentaIngresos(String cuentaIngresos) {	this.cuentaIngresos = cuentaIngresos;	}
	public void setObservaciones (String d)			     { this.observaciones = d; }
	public void setTipoSerie(String valor) {	this.tipoSerie = valor;	}
	public void setFormaPagoAutom�tica(String[] formaPagoAutom�tica) {this.formaPagoAutom�tica = formaPagoAutom�tica;}

	
	// Metodos GET
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Long getIdSerieFacturacion		()	{ return this.idSerieFacturacion; }
	public Integer getIdPlantilla			()	{ return this.idPlantilla; }
	public String  getDescripcion 			()	{ return this.descripcion; }
	public String  getNombreAbreviado		()	{ return this.nombreAbreviado; }
	public String  getEnvioFactura			()	{ return this.envioFactura; }
	public String  getGenerarPDF			()	{ return this.generarPDF; }
	public String  getIdContador			()	{ return this.idContador; }
	public String getConfigDeudor			()  { return configDeudor;	}
	public String getConfigIngresos			()  { return configIngresos;	}
	public String getCuentaClientes			()  { return cuentaClientes;	}
	public String getCuentaIngresos			()  { return cuentaIngresos;	}
	public String  getObservaciones 	    ()	{ return this.observaciones; }
	public String getTipoSerie			()  { return tipoSerie;	}
	
	public Integer getIdTipoPlantillaMail() {
		return idTipoPlantillaMail;
	}
	public void setIdTipoPlantillaMail(Integer idTipoPlantillaMail) {
		this.idTipoPlantillaMail = idTipoPlantillaMail;
	}
	
	public Integer getIdTipoEnvios() {
		return idTipoEnvios;
	}
	public void setIdTipoEnvios(Integer idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}	
	
	public String[] getFormaPagoAutom�tica() {
		
		// Al cmabiar el tipo de campo en la jsp es necesario partir la cadena
		if (formaPagoAutom�tica.length == 1) {
			return formaPagoAutom�tica[0].split(",");
		}		
		return formaPagoAutom�tica;
	}	
	
}
