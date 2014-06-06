//VERSIONES:
//julio.vicente 31-03-2005 creacion
//
package com.siga.beans;


public class FcsCobrosRetencionJudicialBean extends MasterBean
{
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_COBROS_RETENCIONJUDICIAL";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";	
	static public final String C_IDPERSONA				= "IDPERSONA";	
	static public final String C_IDRETENCION			= "IDRETENCION";
	static public final String C_IDCOBRO				= "IDCOBRO";
	static public final String C_IDPAGOSJG				= "IDPAGOSJG";
	static public final String C_FECHARETENCION			= "FECHARETENCION";
	static public final String C_IMPORTERETENIDO		= "IMPORTERETENIDO";	
	 
	/* Variables */
	private Integer  idInstitucion, idPersona, idRetencion, idCobro, idPagosJG;
	private String   fechaRetencion;
	private Double   importeRetenido;
	FcsRetencionesJudicialesBean retencionJudicial = null;
	private CenPersonaBean persona = null;
	String abonoRelacionado = null;
	String pagoRelacionado = null;
	String mes = null;
	String anio = null;
	String fechaDesdePago = null;
	String fechaHastaPago = null;
	String tipoRetencion = null;
	

	/* Metodos */
	
	public String getFechaRetencion()
	{
		return fechaRetencion;
	}

	public void setFechaRetencion(String fechaRetencion)
	{
		this.fechaRetencion = fechaRetencion;
	}

	public Integer getIdCobro()
	{
		return idCobro;
	}

	public void setIdCobro(Integer idCobro)
	{
		this.idCobro = idCobro;
	}

	public Integer getIdInstitucion()
	{
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

	public Integer getIdPagosJG()
	{
		return idPagosJG;
	}

	public void setIdPagosJG(Integer idPagosJG)
	{
		this.idPagosJG = idPagosJG;
	}

	public Integer getIdPersona()
	{
		return idPersona;
	}

	public void setIdPersona(Integer idPersona)
	{
		this.idPersona = idPersona;
	}

	public Integer getIdRetencion()
	{
		return idRetencion;
	}

	public void setIdRetencion(Integer idRetencion)
	{
		this.idRetencion = idRetencion;
	}

	public Double getImporteRetenido()
	{
		return importeRetenido;
	}

	public void setImporteRetenido(Double importeRetenido)
	{
		this.importeRetenido = importeRetenido;
	}

	public String getTipoRetencion()
	{
		return tipoRetencion;
	}

	public void setTipoRetencion(String tipoRetencion)
	{
		this.tipoRetencion = tipoRetencion;
	}

	public String getFechaDesdePago()
	{
		return fechaDesdePago;
	}

	public void setFechaDesdePago(String fechaDesdePago)
	{
		this.fechaDesdePago = fechaDesdePago;
	}

	public String getFechaHastaPago()
	{
		return fechaHastaPago;
	}

	public void setFechaHastaPago(String fechaHastaPago)
	{
		this.fechaHastaPago = fechaHastaPago;
	}

	public String getMes()
	{
		return mes;
	}

	public void setMes(String mes)
	{
		this.mes = mes;
	}

	public String getAnio()
	{
		return anio;
	}

	public void setAnio(String anio)
	{
		this.anio = anio;
	}

	public CenPersonaBean getPersona()
	{
		return persona;
	}

	public void setPersona(CenPersonaBean persona)
	{
		this.persona = persona;
	}

	public String getAbonoRelacionado()
	{
		return abonoRelacionado;
	}

	public void setAbonoRelacionado(String abonoRelacionado)
	{
		this.abonoRelacionado = abonoRelacionado;
	}

	public String getPagoRelacionado()
	{
		return pagoRelacionado;
	}

	public void setPagoRelacionado(String pagoRelacionado)
	{
		this.pagoRelacionado = pagoRelacionado;
	}

	public FcsRetencionesJudicialesBean getRetencionJudicial()
	{
		return retencionJudicial;
	}

	public void setRetencionJudicial(FcsRetencionesJudicialesBean retencionJudicial)
	{
		this.retencionJudicial = retencionJudicial;
	}	

}
