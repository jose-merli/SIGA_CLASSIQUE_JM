/*
 * Created on 01-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

public class ScsAcreditacionProcedimientoBean extends MasterBean
{
	private static final long serialVersionUID = -1626927180430121686L;
	
	/* Variables */
	private Integer		idInstitucion;
	private String		idProcedimiento;
	private Integer		idAcreditacion;
	private Double		porcentaje;
	private Integer		nigNumeroProcedimiento;
	private String		codigoExt;
	private String		codSubtarifa;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_ACREDITACIONPROCEDIMIENTO";

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPROCEDIMIENTO = "IDPROCEDIMIENTO";
	static public final String C_IDACREDITACION = "IDACREDITACION";
	static public final String C_PORCENTAJE = "PORCENTAJE";
	static public final String C_NIG_NUMPROCEDIMIENTO = "NIG_NUMPROCEDIMIENTO";
	static public final String C_CODIGOEXT = "CODIGOEXT";
	static public final String C_CODSUBTARIFA = "CODSUBTARIFA";

	public Integer getIdInstitucion()
	{
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}
	public String getIdProcedimiento()
	{
		return idProcedimiento;
	}
	public void setIdProcedimiento(String idProcedimiento)
	{
		this.idProcedimiento = idProcedimiento;
	}
	public Integer getIdAcreditacion()
	{
		return idAcreditacion;
	}
	public void setIdAcreditacion(Integer idAcreditacion)
	{
		this.idAcreditacion = idAcreditacion;
	}
	public Double getPorcentaje()
	{
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje)
	{
		this.porcentaje = porcentaje;
	}
	public Integer getNigNumeroProcedimiento()
	{
		return nigNumeroProcedimiento;
	}
	public void setNigNumeroProcedimiento(Integer nigNumeroProcedimiento)
	{
		this.nigNumeroProcedimiento = nigNumeroProcedimiento;
	}
	public String getCodigoExt()
	{
		return codigoExt;
	}
	public void setCodigoExt(String codigoExt)
	{
		this.codigoExt = codigoExt;
	}
	public String getCodSubtarifa()
	{
		return codSubtarifa;
	}
	public void setCodSubtarifa(String codSubtarifa)
	{
		this.codSubtarifa = codSubtarifa;
	}

}
