package com.siga.beans;

public class FacFormaPagoSerieBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion;
	private Integer idSerieFacturacion;
	private Integer idFormaPago;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_FORMAPAGOSERIE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDSERIEFACTURACION	= "IDSERIEFACTURACION";
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";

	// Metodos SET
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }
	public void setIdSerieFacturacion (Integer id) 	{ this.idSerieFacturacion= id; }
	public void setIdFormaPago (Integer id) 	{ this.idFormaPago= id; }

	// Metodos GET
	public Integer getIdInstitucion () 	{ return this.idInstitucion; }
	public Integer getIdSerieFacturacion () 	{ return this.idSerieFacturacion; }
	public Integer getIdFormaPago () 	{ return this.idFormaPago;}
}

