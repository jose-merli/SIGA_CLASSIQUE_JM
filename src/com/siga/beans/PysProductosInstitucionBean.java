/*
 * VERSIONES:
 * 
 * daniel.casla	- 4-11-2004 - Inicio
 * miguel.villegas - 25-11-2004 - Continuacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSPRODUCTOINSTITUCION <br/> 
 */
package com.siga.beans;

public class PysProductosInstitucionBean extends MasterBean {
	/* Constantes */
	public static final String PI_COMUNICACION_CODIGO="M";
	public static final String PI_DILIGENCIA_CODIGO="D";
	public static final String PI_CERTIFICADO_CODIGO="C";
	public static final String PI_COMUNICACION_DESCRIPCION="productos.literal.Comunicacion";
	public static final String PI_DILIGENCIA_DESCRIPCION="productos.literal.Diligencia";
	public static final String PI_CERTIFICADO_DESCRIPCION="productos.literal.Certificado";
	
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoProducto;
	private Long idProducto;
	private Long idProductoInstitucion;
	private String 	descripcion;
	private String 	cuentacontable;
	private Double valor;
	private Float porcentajeIva;
	private String 	momentoCargo;
	private String 	solicitarBaja;
	private String 	solicitarAlta;
	private Long idImpresora;
	private Long idPlantilla;
	private String tipoCertificado;
	private String 	fechaBaja;
	private String 	sufijo;
	private String 	nofacturable;
	
	private String 	idContador;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PRODUCTOSINSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOPRODUCTO		= "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO		= "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION	= "IDPRODUCTOINSTITUCION";
	static public final String C_DESCRIPCION	= "DESCRIPCION";
	static public final String C_CUENTACONTABLE	= "CUENTACONTABLE";
	static public final String C_VALOR	= "VALOR";
	static public final String C_PORCENTAJEIVA	= "PORCENTAJEIVA";
	static public final String C_MOMENTOCARGO	= "MOMENTOCARGO";
	static public final String C_SOLICITARBAJA	= "SOLICITARBAJA";
	static public final String C_SOLICITARALTA	= "SOLICITARALTA";
	static public final String C_IDIMPRESORA	= "IDIMPRESORA";
	static public final String C_IDPLANTILLA	= "IDPLANTILLA";
	static public final String C_TIPOCERTIFICADO = "TIPOCERTIFICADO";
	static public final String C_FECHABAJA = "FECHABAJA";
	static public final String C_SUFIJO = "SUFIJO";
	static public final String C_IDCONTADOR = "IDCONTADOR";
	static public final String C_NOFACTURABLE = "NOFACTURABLE";

	// Metodos SET
	public void setIdInstitucion (Integer id)	{ this.idInstitucion=id;}
	public void setIdTipoProducto (Integer id)	{ this.idTipoProducto=id;}
	public void setIdProducto (Long id)	{ this.idProducto=id;}
	public void setIdProductoInstitucion (Long id)	{ this.idProductoInstitucion=id;}
	public void setDescripcion (String s)	{  this.descripcion=s;}
	public void setCuentacontable (String s)	{ this.cuentacontable=s;}
	public void setValor (Double v)	{ this.valor=v;}
	public void setPorcentajeIva (Float v)	{ this.porcentajeIva=v;}
	public void setMomentoCargo (String s)	{ this.momentoCargo=s;}
	public void setSolicitarBaja (String s)	{ this.solicitarBaja=s;}
	public void setSolicitarAlta (String s)	{ this.solicitarAlta=s;}
	public void setIdImpresora (Long id)	{ this.idImpresora=id;}
	public void setIdPlantilla (Long id)	{ this.idPlantilla=id;}
	public void setTipoCertificado (String tipoCertificado)	{ this.tipoCertificado=tipoCertificado;}
	public void setFechaBaja (String aux)	{ this.fechaBaja=aux;}
	public void setIdContador (String aux)	{ this.idContador=aux;}
	public void setnoFacturable (String aux)	{ this.nofacturable=aux;}
		

	// Metodos GET
	public Integer getIdInstitucion ()	{ return this.idInstitucion;}
	public Integer getIdTipoProducto ()	{ return this.idTipoProducto;}
	public Long getIdProducto ()	{ return this.idProducto;}
	public Long getIdProductoInstitucion ()	{ return this.idProductoInstitucion;}
	public String getDescripcion ()	{ return this.descripcion;}
	public String getCuentacontable ()	{ return this.cuentacontable;}
	public Double getValor ()	{ return this.valor;}
	public Float getPorcentajeIva ()	{ return this.porcentajeIva;}
	public String getMomentoCargo ()	{ return this.momentoCargo;}
	public String getSolicitarBaja ()	{ return this.solicitarBaja;}
	public String getSolicitarAlta ()	{ return this.solicitarAlta;}
	public Long getIdImpresora ()	{ return this.idImpresora;}
	public Long getIdPlantilla ()	{ return this.idPlantilla;}
	public String getTipoCertificado ()	{ return this.tipoCertificado;}		
	public String getFechaBaja ()	{ return this.fechaBaja;}		
	public String getIdContador()	{ return this.idContador;}		
	public String getnoFacturable()	{ return this.nofacturable;}

	public String getSufijo() {
		return this.sufijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
}
