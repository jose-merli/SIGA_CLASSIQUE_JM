/*
 * nuria.rgonzalez - 04-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean CEN_SOLIMODIDIRECCIONES <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez 
 */
public class CenSoliModiDireccionesBean extends MasterBean{
	
	
	/* Variables */
	private Integer idInstitucion, idEstadoSolic; 
	private Long	idSolicitud, idPersona, idDireccion;
	private String 	motivo, domicilio, codigoPostal, telefono1, telefono2, movil;
	private String 	fax1, fax2, correoElectronico, paginaweb, preferente;
	private String 	idPais, idProvincia, idPoblacion, fechaAlta, poblacionExtranjera;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLIMODIDIRECCIONES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD		= "IDSOLICITUD";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDDIRECCION		= "IDDIRECCION";
	static public final String C_MOTIVO				= "MOTIVO";
	static public final String C_DOMICILIO			= "DOMICILIO";
	static public final String C_CODIGOPOSTAL		= "CODIGOPOSTAL";
	static public final String C_TELEFONO1			= "TELEFONO1";
	static public final String C_TELEFONO2			= "TELEFONO2";
	static public final String C_MOVIL				= "MOVIL";
	static public final String C_FAX1				= "FAX1";
	static public final String C_FAX2				= "FAX2";
	static public final String C_CORREOELECTRONICO	= "CORREOELECTRONICO";
	static public final String C_PAGINAWEB			= "PAGINAWEB";
	static public final String C_PREFERENTE			= "PREFERENTE";
	static public final String C_IDPAIS				= "IDPAIS";
	static public final String C_IDPROVINCIA		= "IDPROVINCIA";
	static public final String C_IDPOBLACION		= "IDPOBLACION";	
	static public final String C_POBLACIONEXTRANJERA		= "POBLACIONEXTRANJERA";	
	static public final String C_IDESTADOSOLIC		= "IDESTADOSOLIC";
	static public final String C_FECHAALTA			= "FECHAALTA";

		//	 Metodos GET
	
	public String getCodigoPostal()		 	{	return codigoPostal;}	
	public String getCorreoElectronico()	{	return correoElectronico;}
	public String getDomicilio() 			{	return domicilio;}
	public String getFax1()					{	return fax1;}	
	public String getFax2() 				{	return fax2;}	
	public Long getIdDireccion() 			{	return idDireccion;}	
	public Integer getIdInstitucion() 		{	return idInstitucion;}	
	public String getIdPais()				{	return idPais;}	
	public Long getIdPersona()				{	return idPersona;}	
	public String getIdPoblacion() 			{	return idPoblacion;}
	public String getPoblacionExtranjera() 			{	return poblacionExtranjera;}
	public String getIdProvincia() 			{	return idProvincia;}
	public Long getIdSolicitud()			{	return idSolicitud;}
	public String getMotivo()			 	{	return motivo;}	
	public String getMovil()				{	return movil;}	
	public String getPaginaweb()			{	return paginaweb;}	
	public String getPreferente() 			{	return preferente;}	
	public String getTelefono1() 			{	return telefono1;}	
	public String getTelefono2() 			{	return telefono2;}
	public Integer getIdEstadoSolic()		{	return idEstadoSolic;	}	
	public String getFechaAlta()			{	return fechaAlta;		}	
	
	//	 Metodos SET
	public void setCodigoPostal(String codigoPostal) 			{this.codigoPostal = codigoPostal;}
	public void setCorreoElectronico(String correoElectronico)	{this.correoElectronico = correoElectronico;}
	public void setDomicilio(String domicilio) 					{this.domicilio = domicilio;}
	public void setFax1(String fax1)							{this.fax1 = fax1;}
	public void setFax2(String fax2) 							{this.fax2 = fax2;	}	
	public void setIdDireccion(Long idDireccion) 				{this.idDireccion = idDireccion;}
	public void setIdInstitucion(Integer idInstitucion) 		{this.idInstitucion = idInstitucion;}
	public void setIdPais(String idPais) 						{this.idPais = idPais;}
	public void setIdPersona(Long idPersona) 		 			{this.idPersona = idPersona;}
	public void setIdPoblacion(String idPoblacion) 				{this.idPoblacion = idPoblacion;}
	public void setPoblacionExtranjera(String idPoblacion) 				{this.poblacionExtranjera = idPoblacion;}
	public void setIdProvincia(String idProvincia) 				{this.idProvincia = idProvincia;}
	public void setIdSolicitud(Long idSolicitud) 				{this.idSolicitud = idSolicitud;}
	public void setMotivo(String motivo) 						{this.motivo = motivo;}
	public void setMovil(String movil) 							{this.movil = movil;}	
	public void setPaginaweb(String paginaweb) 					{this.paginaweb = paginaweb;}
	public void setPreferente(String preferente) 				{this.preferente = preferente;}
	public void setTelefono1(String telefono1) 					{this.telefono1 = telefono1;}
	public void setTelefono2(String telefono2) 					{this.telefono2 = telefono2;}
	public void setIdEstadoSolic(Integer idEstadoSolic) 		{this.idEstadoSolic = idEstadoSolic;}
	public void setFechaAlta(String fechaAlta) 					{this.fechaAlta = fechaAlta;}
	
		
}
