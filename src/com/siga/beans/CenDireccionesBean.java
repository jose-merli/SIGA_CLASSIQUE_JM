
package com.siga.beans;

/**
 * Clase que recoge y establece los valores del bean CEN_DIRECCIONES
 * 
 * @author nuria.rgonzalez
 * @since 24-12-2004
 * @version adrian.ayala - 10-06-2008 - limpieza y adición de idDireccionAlta
 */
public class CenDireccionesBean extends MasterBean
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	/** Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DIRECCIONES";
	
	//Nombre campos de la tabla
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDPERSONA				= "IDPERSONA";
	static public final String C_IDDIRECCION			= "IDDIRECCION";
	static public final String C_DOMICILIO				= "DOMICILIO";
	static public final String C_CODIGOPOSTAL			= "CODIGOPOSTAL";
	static public final String C_TELEFONO1				= "TELEFONO1";
	static public final String C_TELEFONO2				= "TELEFONO2";
	static public final String C_MOVIL					= "MOVIL";
	static public final String C_FAX1					= "FAX1";
	static public final String C_FAX2					= "FAX2";
	static public final String C_CORREOELECTRONICO		= "CORREOELECTRONICO";
	static public final String C_PAGINAWEB				= "PAGINAWEB";
	static public final String C_FECHABAJA				= "FECHABAJA";
	static public final String C_PREFERENTE				= "PREFERENTE";
	static public final String C_IDPAIS					= "IDPAIS";
	static public final String C_IDPROVINCIA			= "IDPROVINCIA";
	static public final String C_IDPOBLACION			= "IDPOBLACION";
	static public final String C_POBLACIONEXTRANJERA	= "POBLACIONEXTRANJERA";
	static public final String C_IDINSTITUCIONALTA		= "IDINSTITUCIONALTA";
	static public final String C_IDDIRECCIONALTA		= "IDDIRECCIONALTA";
	
	
	//////////////////// ATRIBUTOS ////////////////////
	private Integer idInstitucion, idInstitucionAlta;
	private Long	idPersona, idDireccion, idDireccionAlta;
	private String 	idPais, idProvincia, idPoblacion, domicilio, codigoPostal, 
					telefono1, telefono2, poblacionExtranjera; 
	private String 	movil, fax1, fax2, correoElectronico, paginaweb, 
					fechaBaja, preferente;	
	
	
	//////////////////// GETTERS ////////////////////
	public String getCodigoPostal()		 	{return codigoPostal;}
	public String getCorreoElectronico()	{return correoElectronico;}
	public String getDomicilio() 			{return domicilio;}
	public String getFax1()					{return fax1;}
	public String getFax2() 				{return fax2;}
	public String getFechaBaja() 			{return fechaBaja;}
	public Long getIdDireccion() 			{return idDireccion;}
	public Integer getIdInstitucion() 		{return idInstitucion;}
	public String getIdPais()				{return idPais;}
	public Long getIdPersona()				{return idPersona;}
	public String getIdPoblacion() 			{return idPoblacion;}
	public String getPoblacionExtranjera() 	{return poblacionExtranjera;}
	public String getIdProvincia() 			{return idProvincia;}
	public String getMovil()				{return movil;}
	public String getPaginaweb()			{return paginaweb;}
	public String getPreferente() 			{return preferente;}
	public String getTelefono1() 			{return telefono1;}
	public String getTelefono2() 			{return telefono2;}
    public Integer getIdInstitucionAlta() 	{return idInstitucionAlta;}
    public Long getIdDireccionAlta() 		{return idDireccionAlta;}
	
	
	//////////////////// SETTERS ////////////////////
	public void setCodigoPostal(String codigoPostal) 				{this.codigoPostal = codigoPostal;}
	public void setCorreoElectronico(String correoElectronico)		{this.correoElectronico = correoElectronico;}
	public void setDomicilio(String domicilio) 						{this.domicilio = domicilio;}
	public void setFax1(String fax1)								{this.fax1 = fax1;}
	public void setFax2(String fax2) 								{this.fax2 = fax2;	}
	public void setFechaBaja(String fechaBaja) 						{this.fechaBaja = fechaBaja;}
	public void setIdDireccion(Long idDireccion) 					{this.idDireccion = idDireccion;}
	public void setIdInstitucion(Integer idInstitucion) 			{this.idInstitucion = idInstitucion;}
	public void setIdPais(String idPais) 							{this.idPais = idPais;}
	public void setIdPersona(Long idPersona) 		 				{this.idPersona = idPersona;}
	public void setIdPoblacion(String idPoblacion) 					{this.idPoblacion = idPoblacion;}
	public void setPoblacionExtranjera(String poblacionExtranjera)	{this.poblacionExtranjera = poblacionExtranjera;}
	public void setIdProvincia(String idProvincia) 					{this.idProvincia = idProvincia;}
	public void setMovil(String movil) 								{this.movil = movil;}	
	public void setPaginaweb(String paginaweb) 						{this.paginaweb = paginaweb;}
	public void setPreferente(String preferente) 					{this.preferente = preferente;}
	public void setTelefono1(String telefono1) 						{this.telefono1 = telefono1;}
	public void setTelefono2(String telefono2) 						{this.telefono2 = telefono2;}
    public void setIdInstitucionAlta(Integer idInstitucionAlta) 	{this.idInstitucionAlta = idInstitucionAlta;}
    public void setIdDireccionAlta(Long idDireccionAlta)			{this.idDireccionAlta = idDireccionAlta;}
}
