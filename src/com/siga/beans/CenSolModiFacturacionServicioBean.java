/* VERSIONES:
 * raul.ggonzalez - 07-02-2005 - Creacion
 */

/**
 * Clase que recoge y establece los valores del bean CEN_SOLMODIFACTURACIONSERVICIOS<br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

public class CenSolModiFacturacionServicioBean extends MasterBean{
	
	
	/* Variables */
	private Integer idTipoServicios, idServiciosInstitucion, idCuenta, idEstadoSolic; 
	private Long	idSolicitud, idInstitucion, idPersona, idDireccion, idServicio, idPeticion;
	private String 	motivo, fechaAlta;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLMODIFACTURACIONSERVICIO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD		= "IDSOLICITUD";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDCUENTA			= "IDCUENTA";
	static public final String C_IDTIPOSERVICIOS	= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO			= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION		= "IDSERVICIOSINSTITUCION";
	static public final String C_IDPETICION		= "IDPETICION";
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";
	static public final String C_MOTIVO				= "MOTIVO";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_FECHAMODIFICACION	= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION	= "USUMODIFICACION";

		//	 Metodos GET
	
	public Long getIdSolicitud()		 	{	return idSolicitud;}	
	public Long getIdInstitucion()	{	return idInstitucion;}
	public Long getIdPersona() 			{	return idPersona;}
	public Integer getIdCuenta()					{	return idCuenta;}	
	public Integer getIdTipoServicios() 				{	return idTipoServicios;}	
	public Long getIdServicio() 			{	return idServicio;}	
	public Integer getIdServiciosInstitucion() 		{	return idServiciosInstitucion;}	
	public Long getIdPeticion()				{	return idPeticion;}	
	public Integer getIdEstadoSolic()				{	return idEstadoSolic;}	
	public String getMotivo() 			{	return motivo;}
	public String getFechaAlta() 			{	return fechaAlta;}
	
	//	 Metodos SET
	public void setIdSolicitud(Long dato) 			{this.idSolicitud = dato;}
	public void setIdInstitucion(Long dato)	{this.idInstitucion = dato;}
	public void setIdPersona(Long dato) 					{this.idPersona = dato;}
	public void setIdCuenta(Integer dato)							{this.idCuenta = dato;}
	public void setIdTipoServicios(Integer dato) 							{this.idTipoServicios = dato;	}	
	public void setIdServicio(Long dato) 				{this.idServicio = dato;}
	public void setIdServiciosInstitucion(Integer dato) 		{this.idServiciosInstitucion = dato;}
	public void setIdPeticion(Long dato) 						{this.idPeticion = dato;}
	public void setIdEstadoSolic(Integer dato) 		 			{this.idEstadoSolic = dato;}
	public void setMotivo(String dato) 				{this.motivo = dato;}
	public void setFechaAlta(String dato) 				{this.fechaAlta = dato;}
	
		
}
