package com.siga.certificados.form;

import com.siga.general.MasterForm;

public class SIGAMantenimientoCertificadosIncompatibilidadesForm extends MasterForm
{
	private static final long serialVersionUID = -1686633803141350551L;

	private String idInstitucion = "";
	private String idTipoProducto = "";
	private String idProducto = "";
	private String idProductoInstitucion = "";
	private String idTipoProd_Incompatible = "";
	private String idProd_Incompatible = "";
	private String idProdInst_Incompatible = "";
	private String motivo = "";
	
	
	private String[] modos = { "modosRelacionPlantillas" };

	private String certificado = "";
	private String editable = "";
	private String descripcionCertificado = "";
	private String recarga;
	private String nuevo;
	private String relacion; // indica si es vamos por el camino de relación o simplemente es editar.
	private String idRelacion;

	
	///////////////////// GETTERS Y SETTERS /////////////////////
	public String getIdInstitucion()
	{
		return idInstitucion;
	}

	public void setIdInstitucion(String idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

	public String getIdTipoProducto()
	{
		return idTipoProducto;
	}

	public void setIdTipoProducto(String idTipoProducto)
	{
		this.idTipoProducto = idTipoProducto;
	}

	public String getIdProducto()
	{
		return idProducto;
	}

	public void setIdProducto(String idProducto)
	{
		this.idProducto = idProducto;
	}

	public String getIdProductoInstitucion()
	{
		return idProductoInstitucion;
	}

	public void setIdProductoInstitucion(String idProductoInstitucion)
	{
		this.idProductoInstitucion = idProductoInstitucion;
	}

	public String getIdTipoProd_Incompatible()
	{
		return idTipoProd_Incompatible;
	}

	public void setIdTipoProd_Incompatible(String idTipoProd_Incompatible)
	{
		this.idTipoProd_Incompatible = idTipoProd_Incompatible;
	}

	public String getIdProd_Incompatible()
	{
		return idProd_Incompatible;
	}

	public void setIdProd_Incompatible(String idProd_Incompatible)
	{
		this.idProd_Incompatible = idProd_Incompatible;
	}

	public String getIdProdInst_Incompatible()
	{
		return idProdInst_Incompatible;
	}

	public void setIdProdInst_Incompatible(String idProdInst_Incompatible)
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

	
	public String[] getModos()
	{
		return this.modos;
	}

	// public String getModo()
	// {
	// return modo;
	// }
	//
	// public void setModo(String modo)
	// {
	// this.modo = modo;
	// }

	public String getCertificado()
	{
		return certificado;
	}

	public void setCertificado(String certificado)
	{
		this.certificado = certificado;
	}

	public String getEditable()
	{
		return editable;
	}

	public void setEditable(String editable)
	{
		this.editable = editable;
	}

	public String getDescripcionCertificado()
	{
		return descripcionCertificado;
	}

	public void setDescripcionCertificado(String descripcionCertificado)
	{
		this.descripcionCertificado = descripcionCertificado;
	}

	public String getRecarga()
	{
		return recarga;
	}

	public void setRecarga(String recarga)
	{
		this.recarga = recarga;
	}

	public String getNuevo()
	{
		return nuevo;
	}

	public void setNuevo(String nuevo)
	{
		this.nuevo = nuevo;
	}

	public String getRelacion()
	{
		return relacion;
	}

	public void setRelacion(String relacion)
	{
		this.relacion = relacion;
	}

	public String getIdRelacion()
	{
		return idRelacion;
	}

	public void setIdRelacion(String idRelacion)
	{
		this.idRelacion = idRelacion;
	}

}