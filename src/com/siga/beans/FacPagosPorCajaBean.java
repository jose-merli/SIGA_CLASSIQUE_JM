/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean FAC_PAGOSPORCAJA <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class FacPagosPorCajaBean extends MasterBean{

	/* Variables */
	private Integer	idInstitucion;
	private String	idFactura;
	private Integer	idPagoPorCaja;
	private String	fecha;
	private String	contabilizado;
	private Double 	importe;
	private String	tarjeta;
	private Integer idAbono;
	private Integer idPagoAbono;
	private String  observaciones;
	private String  tipoApunte;
	private Integer numeroAutorizacion;
	private Long  	referencia;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_PAGOSPORCAJA";
	
	static public String tipoApunteCompensado = "C";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION	   	= "IDINSTITUCION";
	static public final String C_IDFACTURA         	= "IDFACTURA";
	static public final String C_IDPAGOPORCAJA     	= "IDPAGOPORCAJA";
	static public final String C_FECHA             	= "FECHA";
	static public final String C_CONTABILIZADO     	= "CONTABILIZADO";
	static public final String C_FECHAMODIFICACION 	= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION   	= "USUMODIFICACION";
	static public final String C_IMPORTE           	= "IMPORTE";
	static public final String C_TARJETA           	= "TARJETA";
	static public final String C_IDABONO           	= "IDABONO";
	static public final String C_IDPAGOABONO       	= "IDPAGOABONO";
	static public final String C_OBSERVACIONES     	= "OBSERVACIONES";
	static public final String C_NUMEROAUTORIZACION = "NUMEROAUTORIZACION";
	static public final String C_REFERENCIA     	= "REFERENCIA";
	static public final String C_TIPOAPUNTE     	= "TIPOAPUNTE";
		
	// Metodos SET&GET
	public void setIdInstitucion     (Integer 	id) 	{ this.idInstitucion     = id; } 
	public void setIdFactura         (String  	id) 	{ this.idFactura         = id; } 
	public void setIdPagoPorCaja     (Integer 	id) 	{ this.idPagoPorCaja     = id; } 
	public void setFecha             (String	id) 	{ this.fecha             = id; } 
	public void setContabilizado     (String	id) 	{ this.contabilizado     = id; } 
	public void setImporte           (Double  	id) 	{ this.importe           = id; } 
	public void setTarjeta           (String	id) 	{ this.tarjeta           = id; } 
	public void setTipoApunte        (String	id) 	{ this.tipoApunte        = id; } 
	public void setIdAbono           (Integer 	id) 	{ this.idAbono           = id; } 
	public void setIdPagoAbono       (Integer 	id) 	{ this.idPagoAbono       = id; } 
	public void setObservaciones     (String	id) 	{ this.observaciones     = id; } 
	public void setNumeroAutorizacion(Integer	id) 	{ this.numeroAutorizacion= id; } 
	public void setReferencia	     (Long	id) 		{ this.referencia	     = id; } 

	public Integer  getIdInstitucion     () 	{ return this.idInstitucion; } 
	public String   getIdFactura         () 	{ return this.idFactura; } 
	public Integer  getIdPagoPorCaja     () 	{ return this.idPagoPorCaja; } 
	public String	getFecha             () 	{ return this.fecha; } 
	public String	getContabilizado     () 	{ return this.contabilizado; } 
	public Double 	getImporte           () 	{ return this.importe; } 
	public String	getTarjeta           () 	{ return this.tarjeta; } 
	public String	getTipoApunte        () 	{ return this.tipoApunte; } 
	public Integer  getIdAbono           () 	{ return this.idAbono; } 
	public Integer  getIdPagoAbono       () 	{ return this.idPagoAbono; } 
	public String 	getObservaciones     () 	{ return this.observaciones; } 
	public Integer 	getNumeroAutorizacion() 	{ return this.numeroAutorizacion; } 
	public Long 	getReferencia	     () 	{ return this.referencia; } 
	
}
