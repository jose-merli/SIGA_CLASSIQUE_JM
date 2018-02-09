package com.siga.beans;

public class CerIncompatibilidadesBean extends MasterBean
{
	private static final long serialVersionUID = -3302668868045148516L;
	
	
	// Variables
	private Integer idInstitucion;
	private Integer idTipoProducto;
	private Integer idProducto;
	private Integer idProductoInstitucion;
	private Integer idTipoProd_Incompatible;
	private Integer idProd_Incompatible;
	private Integer idProdInst_Incompatible;
	private String motivo;

	
	// Campos de la tabla
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOPRODUCTO = "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO = "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION = "IDPRODUCTOINSTITUCION";
	static public final String C_IDTIPOPROD_INCOMPATIBLE = "IDTIPOPROD_INCOMPATIBLE";
	static public final String C_IDPROD_INCOMPATIBLE = "IDPROD_INCOMPATIBLE";
	static public final String C_IDPRODINST_INCOMPATIBLE = "IDPRODINST_INCOMPATIBLE";
	static public final String C_MOTIVO = "MOTIVO";

	static public final String T_NOMBRETABLA = "CER_INCOMPATIBILIDADES";

	
	// Metodos
	public Integer getIdInstitucion()
	{
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

	public Integer getIdTipoProducto()
	{
		return idTipoProducto;
	}

	public void setIdTipoProducto(Integer idTipoProducto)
	{
		this.idTipoProducto = idTipoProducto;
	}

	public Integer getIdProducto()
	{
		return idProducto;
	}

	public void setIdProducto(Integer idProducto)
	{
		this.idProducto = idProducto;
	}

	public Integer getIdProductoInstitucion()
	{
		return idProductoInstitucion;
	}

	public void setIdProductoInstitucion(Integer idProductoInstitucion)
	{
		this.idProductoInstitucion = idProductoInstitucion;
	}

	public Integer getIdTipoProd_Incompatible()
	{
		return idTipoProd_Incompatible;
	}

	public void setIdTipoProd_Incompatible(Integer idTipoProd_Incompatible)
	{
		this.idTipoProd_Incompatible = idTipoProd_Incompatible;
	}

	public Integer getIdProd_Incompatible()
	{
		return idProd_Incompatible;
	}

	public void setIdProd_Incompatible(Integer idProd_Incompatible)
	{
		this.idProd_Incompatible = idProd_Incompatible;
	}

	public Integer getIdProdInst_Incompatible()
	{
		return idProdInst_Incompatible;
	}

	public void setIdProdInst_Incompatible(Integer idProdInst_Incompatible)
	{
		this.idProdInst_Incompatible = idProdInst_Incompatible;
	}

	public String getMotivo()
	{
		return motivo;
	}

	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}

}