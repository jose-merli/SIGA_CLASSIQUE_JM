/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_FACTURA <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class FacFacturaBean extends MasterBean{

	/* Variables */
	private String 	idFactura;
	private Integer idInstitucion;
	private String 	fechaEmision;
	private String 	contabilizada;
	private String 	numeroFactura;
	private String 	observaciones;	
	private String 	observinforme;	
	private Long	idSerieFacturacion;
	private Long	idProgramacion;
	private Integer idFormaPago;
	private Long 	idPersona;
	private Integer idCuenta;
	private String 	ctaCliente;	
	private Long 	idPersonaDeudor;
	private Integer idCuentaDeudor;
	
	private Double impTotalNeto;
	private Double impTotalIva;
	private Double impTotal;
	private Double impTotalAnticipado;
	private Double impTotalPagadoPorCaja;
	private Double impTotalPagadoPorBanco;
	private Double impTotalPagado;
	private Double impTotalPorPagar;
	private Double impTotalCompensado;
	private Double impTotalPagadoSoloCaja;
	private Double impTotalPagadoSoloTarjeta;
	private Integer estado;
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_FACTURA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDFACTURA		= "IDFACTURA";	
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_FECHAEMISION	= "FECHAEMISION";
	static public final String C_NUMEROFACTURA	= "NUMEROFACTURA";
	static public final String C_CONTABILIZADA	= "CONTABILIZADA";
	static public final String C_OBSERVACIONES	= "OBSERVACIONES";
	static public final String C_IDSERIEFACTURACION	= "IDSERIEFACTURACION";
	static public final String C_IDPROGRAMACION	= "IDPROGRAMACION";
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";
	static public final String C_IDPERSONA		= "IDPERSONA";
	static public final String C_IDCUENTA		= "IDCUENTA";
	static public final String C_CTACLIENTE		= "CTACLIENTE";
	static public final String C_IDPERSONADEUDOR= "IDPERSONADEUDOR";
	static public final String C_IDCUENTADEUDOR = "IDCUENTADEUDOR";
	static public final String C_DEUDA = "DEUDA";
	static public final String C_OBSERVINFORME = "OBSERVINFORME";

	static public final String C_IMPTOTALNETO = "IMPTOTALNETO";
	static public final String C_IMPTOTALIVA = "IMPTOTALIVA";
	static public final String C_IMPTOTAL = "IMPTOTAL";
	static public final String C_IMPTOTALANTICIPADO = "IMPTOTALANTICIPADO";
	static public final String C_IMPTOTALPAGADOPORCAJA = "IMPTOTALPAGADOPORCAJA";
	static public final String C_IMPTOTALPAGADOPORBANCO = "IMPTOTALPAGADOPORBANCO";
	static public final String C_IMPTOTALPAGADO = "IMPTOTALPAGADO";
	static public final String C_IMPTOTALPORPAGAR = "IMPTOTALPORPAGAR";
	static public final String C_IMPTOTALCOMPENSADO = "IMPTOTALCOMPENSADO";
	static public final String C_IMPTOTALPAGADOSOLOCAJA = "IMPTOTALPAGADOSOLOCAJA";
	static public final String C_IMPTOTALPAGADOSOLOTARJETA = "IMPTOTALPAGADOSOLOTARJETA";
	static public final String C_ESTADO = "ESTADO";

	// Metodos SET

	public void setIdFactura (String id) 	{ this.idFactura = id; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setFechaEmision (String f) 	{ this.fechaEmision = f; }
	public void setNumeroFactura (String num)	{ this.numeroFactura = num; }
	public void setContabilizada (String s) 	{ this.contabilizada = s; }
	public void setObservaciones (String s)	{ this.observaciones = s; }
	public void setObservinforme (String s)	{ this.observinforme = s; }
	public void setIdSerieFacturacion (Long id)	{ this.idSerieFacturacion = id; }
	public void setIdProgramacion (Long id)	{ this.idProgramacion = id; }
	public void setIdFormaPago (Integer id)	{ this.idFormaPago = id; }
	public void setIdPersona (Long id) 	{ this.idPersona = id; }
	public void setIdCuenta (Integer id) 	{ this.idCuenta = id; }
	public void setCtaCliente (String s)	{ this.ctaCliente = s; }
	public void setIdPersonaDeudor (Long id) 	{ this.idPersonaDeudor = id; }
	public void setIdCuentaDeudor (Integer id) 	{ this.idCuentaDeudor = id; }

	public void setImpTotalNeto (Double id) 	{ this.impTotalNeto = id; }
	public void setImpTotalIva (Double id) 	{ this.impTotalIva = id; }
	public void setImpTotal (Double id) 	{ this.impTotal = id; }
	public void setImpTotalAnticipado (Double id) 	{ this.impTotalAnticipado = id; }
	public void setImpTotalPagadoPorCaja (Double id) 	{ this.impTotalPagadoPorCaja = id; }
	public void setImpTotalPagadoPorBanco (Double id) 	{ this.impTotalPagadoPorBanco = id; }
	public void setImpTotalPagado (Double id) 	{ this.impTotalPagado = id; }
	public void setImpTotalPorPagar (Double id) 	{ this.impTotalPorPagar = id; }
	public void setImpTotalCompensado (Double id) 	{ this.impTotalCompensado = id; }
	public void setImpTotalPagadoSoloCaja (Double id) 	{ this.impTotalPagadoSoloCaja = id; }
	public void setImpTotalPagadoSoloTarjeta (Double id) 	{ this.impTotalPagadoSoloTarjeta = id; }
	public void setEstado (Integer id) 	{ this.estado = id; }

	// Metodos GET
	public String getIdFactura () 	{ return this.idFactura; }
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public String getFechaEmision () 	{ return this.fechaEmision; }
	public String getNumeroFactura ()	{ return this.numeroFactura; }
	public String getContabilizada () 	{ return this.contabilizada; }
	public String getObservaciones ()	{ return this.observaciones; }
	public String getObservinforme ()	{ return this.observinforme; }
	public Long getIdSerieFacturacion ()	{ return this.idSerieFacturacion; }
	public Long getIdProgramacion ()	{ return this.idProgramacion; }
	public Integer getIdFormaPago ()	{ return this.idFormaPago; }
	public Long getIdPersona () 	{ return this.idPersona; }
	public Integer getIdCuenta () 	{ return this.idCuenta; }	
	public String getCtaCliente ()	{ return this.ctaCliente; }
	public Long getIdPersonaDeudor () 	{ return this.idPersonaDeudor; }
	public Integer getIdCuentaDeudor () 	{ return this.idCuentaDeudor; }	
	
	public Double getImpTotalNeto () 	{ return this.impTotalNeto; }
	public Double getImpTotalIva () 	{ return this.impTotalIva;  }
	public Double getImpTotal () 		{ return this.impTotal; }
	public Double getImpTotalAnticipado () 	{ return this.impTotalAnticipado; }
	public Double getImpTotalPagadoPorCaja () 	{ return this.impTotalPagadoPorCaja;  }
	public Double getImpTotalPagadoPorBanco () 	{ return this.impTotalPagadoPorBanco;  }
	public Double getImpTotalPagado () 	{ return this.impTotalPagado; }
	public Double getImpTotalPorPagar () 	{ return this.impTotalPorPagar; }
	public Double getImpTotalCompensado () 	{ return this.impTotalCompensado; }
	public Double getImpTotalPagadoSoloCaja () 	{ return this.impTotalPagadoSoloCaja; }
	public Double getImpTotalPagadoSoloTarjeta () 	{ return this.impTotalPagadoSoloTarjeta; }
	public Integer getEstado () 	{ return this.estado;  }
	
}
