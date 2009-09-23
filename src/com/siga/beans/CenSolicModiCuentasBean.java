/*
 * nuria.rgonzalez - 14-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_SOLICMODICUENTAS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenSolicModiCuentasBean extends MasterBean {

	/* Variables */
	private Integer	idInstitucion, idCuenta, idEstadoSolic; 
	private Long	idSolicitud, idPersona;
	
	private String 	motivo, abonoCargo, abonoSJCS, cbo_Codigo, codigoSucursal, digitoControl, numeroCuenta,	titular, fechaAlta;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICMODICUENTAS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD		= "IDSOLICITUD";
	static public final String C_MOTIVO				= "MOTIVO";
	static public final String C_ABONOCARGO			= "ABONOCARGO";
	static public final String C_ABONOSJCS			= "ABONOSJCS";
	static public final String C_CBO_CODIGO			= "CBO_CODIGO";
	static public final String C_CODIGOSUCURSAL		= "CODIGOSUCURSAL";
	static public final String C_DIGITOCONTROL		= "DIGITOCONTROL";
	static public final String C_NUMEROCUENTA		= "NUMEROCUENTA";
	static public final String C_TITULAR			= "TITULAR";
	static public final String C_IDPERSONA			= "IDPERSONA";	
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";	
	static public final String C_IDCUENTA			= "IDCUENTA";
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";
	static public final String C_FECHAALTA			= "FECHAALTA";
	
	//	 Metodos GET
	
	public String getAbonoCargo()		{	return abonoCargo;		}	
	public String getAbonoSJCS()		{	return abonoSJCS;		}	
	public String getCbo_Codigo()		{	return cbo_Codigo;		}	
	public String getCodigoSucursal()	{	return codigoSucursal;	}	
	public String getDigitoControl()	{	return digitoControl;	}	
	public Integer getIdCuenta()		{	return idCuenta;		}	
	public Integer getIdInstitucion()	{	return idInstitucion;	}	
	public Long getIdPersona()			{	return idPersona;		}	
	public Long getIdSolicitud()		{	return idSolicitud;		}	
	public String getMotivo()			{	return motivo;			}
	public String getNumeroCuenta()		{	return numeroCuenta;	}	
	public String getTitular()			{	return titular;			}
	public Integer getIdEstadoSolic()	{	return idEstadoSolic;	}	
	public String getFechaAlta()		{	return fechaAlta;		}	
	
//	 Metodos SET

	public void setAbonoCargo(String abonoCargo) 			{	this.abonoCargo = abonoCargo;}	
	public void setAbonoSJCS(String abonoSJCS) 				{	this.abonoSJCS = abonoSJCS;}	
	public void setCbo_Codigo(String cbo_Codigo) 			{	this.cbo_Codigo = cbo_Codigo;}	
	public void setCodigoSucursal(String codigoSucursal) 	{	this.codigoSucursal = codigoSucursal;}	
	public void setDigitoControl(String digitoControl) 		{	this.digitoControl = digitoControl;}	
	public void setIdCuenta(Integer idCuenta) 				{	this.idCuenta = idCuenta;}	
	public void setIdInstitucion(Integer idInstitucion) 	{	this.idInstitucion = idInstitucion;}
	public void setIdPersona(Long idPersona) 				{	this.idPersona = idPersona;}
	public void setIdSolicitud(Long idSolicitud) 			{	this.idSolicitud = idSolicitud;}
	public void setMotivo(String motivo) 					{	this.motivo = motivo;}
	public void setNumeroCuenta(String numeroCuenta) 		{	this.numeroCuenta = numeroCuenta;}
	public void setTitular(String titular) 					{	this.titular = titular;}
	public void setIdEstadoSolic(Integer idEstadoSolic) 	{	this.idEstadoSolic = idEstadoSolic;}
	public void setFechaAlta(String fechaAlta) 				{	this.fechaAlta = fechaAlta;}
}
