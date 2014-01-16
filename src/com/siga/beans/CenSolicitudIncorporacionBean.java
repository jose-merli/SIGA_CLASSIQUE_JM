/*
 * Created on 14-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;


/**
 * @author daniel.campos
 * @version: modificado por david.sanchezp el 27/12/2005 para incluir el campo sexo
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenSolicitudIncorporacionBean extends MasterBean {

	/* Variables */
	private Long 	idSolicitud, idPersonaTemp, idDireccionTemp;
	
	private Integer idTratamiento, 
					idTipoIdentificacion, 	idInstitucion, 
					idEstadoCivil, 			idEstado, 
					idTipoSolicitud, 		idTipoColegiacion,
					idModalidadDocumentacion;
	
	private String 	nColegiado, 		nombre, 		apellido1, 			apellido2, 
					numIdentificador, 	domicilio, 		codigoPostal, 		telef1, 
					telef2, 			movil, 			fax1, 				fax2, 
					correoElectronico, 	observaciones, 	naturalDe,			idPais,
					idProvincia,		idPoblacion,	fechaSolicitud, 	fechaEstadoColegial,
					fechaNacimiento, 	fechaEstado,	sexo, poblacionExtranjera;
	
	private String 	abonoCargo, abonoSJCS, cbo_Codigo, codigoSucursal, digitoControl, numeroCuenta,
					titular, cuentaContable, residente, iban;	

	Integer idPersona;
	String fechaAlta;
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_SOLICITUDINCORPORACION";

	/* Nombre campos de la tabla */
	static public final String C_IDSOLICITUD 			= "IDSOLICITUD";
	static public final String C_FECHASOLICITUD 		= "FECHASOLICITUD";
	static public final String C_NCOLEGIADO 			= "NCOLEGIADO";
	static public final String C_IDTRATAMIENTO 			= "IDTRATAMIENTO";
	static public final String C_NOMBRE 				= "NOMBRE";
	static public final String C_APELLIDO1 				= "APELLIDO1";
	static public final String C_APELLIDO2 				= "APELLIDO2";
	static public final String C_NUMEROIDENTIFICADOR 	= "NUMEROIDENTIFICADOR";
	static public final String C_DOMICILIO 				= "DOMICILIO";
	static public final String C_CODIGOPOSTAL 			= "CODIGOPOSTAL";
	static public final String C_TELEFONO1 				= "TELEFONO1";
	static public final String C_TELEFONO2 				= "TELEFONO2";
	static public final String C_MOVIL 					= "MOVIL";
	static public final String C_FAX1 					= "FAX1";
	static public final String C_FAX2 					= "FAX2";
	static public final String C_CORREOELECTRONICO 		= "CORREOELECTRONICO";
	static public final String C_OBSERVACIONES 			= "OBSERVACIONES";
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDESTADOCIVIL 			= "IDESTADOCIVIL";
	static public final String C_IDESTADO 				= "IDESTADO";
	static public final String C_IDTIPOSOLICITUD 		= "IDTIPOSOLICITUD";
	static public final String C_IDTIPOCOLEGIACION 		= "IDTIPOCOLEGIACION";
	static public final String C_IDPAIS 				= "IDPAIS";
	static public final String C_FECHANACIMIENTO 		= "FECHANACIMIENTO";
	static public final String C_NATURALDE 				= "NATURALDE";
	static public final String C_IDTIPOIDENTIFICACION 	= "IDTIPOIDENTIFICACION";
	static public final String C_IDPROVINCIA 			= "IDPROVINCIA";
	static public final String C_IDPOBLACION 			= "IDPOBLACION";
	static public final String C_POBLACIONEXTRANJERA 	= "POBLACIONEXTRANJERA";
	static public final String C_FECHAESTADO 			= "FECHAESTADO";
	static public final String C_SEXO					= "SEXO";
	static public final String C_IDMODALIDADDOCUMENTACION = "IDMODALIDADDOCUMENTACION";
	static public final String C_FECHAESTADOCOLEGIAL	= "FECHAESTADOCOLEGIAL";
	
	static public final String C_IDPERSONATEMP 			= "IDPERSONATEMP";
	static public final String C_IDDIRECCIONTEMP		= "IDDIRECCIONTEMP";
	
	static public final String C_RESIDENTE			= "RESIDENTE";

	static public final String C_ABONOCARGO			= "ABONOCARGO";
	static public final String C_ABONOSJCS			= "ABONOSJCS";
	static public final String C_CBO_CODIGO			= "CBO_CODIGO";
	static public final String C_CODIGOSUCURSAL		= "CODIGOSUCURSAL";
	static public final String C_DIGITOCONTROL		= "DIGITOCONTROL";
	static public final String C_NUMEROCUENTA		= "NUMEROCUENTA";
	static public final String C_TITULAR			= "TITULAR";	
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_IBAN				= "IBAN";
	
	// Metodos SET
	public void setIdSolicitud (Long id) 				{ this.idSolicitud = id; }
	public void setIdTratamiento (Integer id) 			{ this.idTratamiento = id; }
	public void setIdInstitucion (Integer id) 			{ this.idInstitucion = id; }
	public void setIdEstadoCivil (Integer id) 			{ this.idEstadoCivil = id; }
	public void setIdEstado  (Integer id) 				{ this.idEstado = id; }
	public void setIdTipoSolicitud  (Integer id) 		{ this.idTipoSolicitud = id; }
	public void setIdTipoColegiacion  (Integer id) 		{ this.idTipoColegiacion = id; }
	public void setIdTipoIdentificacion  (Integer id) 	{ this.idTipoIdentificacion = id; }
	public void setNColegiado (String n) 				{ this.nColegiado = n; }
	public void setNombre (String n) 					{ this.nombre = n; }
	public void setApellido1 (String a) 				{ this.apellido1 = a; }
	public void setApellido2 (String a) 				{ this.apellido2 = a; }
	public void setNumeroIdentificador (String n) 		{ this.numIdentificador = n; }
	public void setDomicilio (String d) 				{ this.domicilio = d; }
	public void setCodigoPostal (String cp) 			{ this.codigoPostal = cp; }
	public void setTelefono1 (String t) 				{ this.telef1 = t;}
	public void setTelefono2 (String t) 				{ this.telef2 = t;}
	public void setMovil (String t) 					{ this.movil = t;}
	public void setFax1 (String f) 						{ this.fax1 = f;}
	public void setFax2 (String f) 						{ this.fax2 = f; }
	public void setCorreoElectronico (String e) 		{ this.correoElectronico = e; }
	public void setObservaciones (String o) 			{ this.observaciones = o;}
	public void setIdPais  (String id) 					{ this.idPais = id; }
	public void setNaturalDe (String s) 				{ this.naturalDe = s; }
	public void setIdProvincia  (String id) 			{ this.idProvincia = id; }
	public void setIdPoblacion  (String id) 			{ this.idPoblacion = id; }
	public void setPoblacionExtranjera  (String id) 	{ this.poblacionExtranjera = id; }
	public void setFechaSolicitud (String f) 			{ this.fechaSolicitud = f ; }
	public void setFechaNacimiento (String f) 			{ this.fechaNacimiento = f; }
	public void setFechaEstado (String f) 				{ this.fechaEstado = f; }
	public void setFechaEstadoColegial (String f) 		{ this.fechaEstadoColegial = f; }
	public void setAbonoCargo(String abonoCargo) 		{ this.abonoCargo = abonoCargo;}
	public void setCbo_Codigo(String cbo_Codigo) 		{ this.cbo_Codigo = cbo_Codigo;}
	public void setCodigoSucursal(String codigoSucursal){ this.codigoSucursal = codigoSucursal;}
	public void setDigitoControl(String digitoControl) 	{ this.digitoControl = digitoControl;}
	public void setNumeroCuenta(String numeroCuenta) 	{ this.numeroCuenta = numeroCuenta;}
	public void setAbonoSJCS(String abonoSJCS) 			{ this.abonoSJCS = abonoSJCS;}
	public void setTitular(String titular) 				{ this.titular = titular;}
	public void setSexo(String sexo) 					{ this.sexo = sexo;}
	public void setIdModalidadDocumentacion(Integer idModalidadDocumentacion) {this.idModalidadDocumentacion = idModalidadDocumentacion;}
	public void setResidente(boolean residente) 		{ this.residente = residente?"1":"0";}
	
	
	// Metodos GET
	public Long getIdSolicitud 				() 	{ return this.idSolicitud; }
	public Integer getIdTratamiento 		()	{ return this.idTratamiento; }
	public Integer getIdInstitucion 		()	{ return this.idInstitucion; }
	public Integer getIdEstadoCivil 		()	{ return this.idEstadoCivil; }
	public Integer getIdEstado  			()	{ return this.idEstado; }
	public Integer getIdTipoSolicitud 		()	{ return this.idTipoSolicitud; }
	public Integer getIdTipoColegiacion 	() 	{ return this.idTipoColegiacion; }
	public Integer getIdTipoIdentificacion	() 	{ return this.idTipoIdentificacion; }
	public String getNColegiado 			()	{ return this.nColegiado; }
	public String getNombre 				()	{ return this.nombre; }
	public String getApellido1 				()	{ return this.apellido1; }
	public String getApellido2 				() 	{ return this.apellido2; }
	public String getNumeroIdentificador	() 	{ return this.numIdentificador; }
	public String getDomicilio 				() 	{ return this.domicilio; }
	public String getCodigoPostal 			() 	{ return this.codigoPostal; }
	public String getTelefono1 				() 	{ return this.telef1; }
	public String getTelefono2 				() 	{ return this.telef2; }
	public String getMovil 					() 	{ return this.movil; }
	public String getFax1 					() 	{ return this.fax1; }
	public String getFax2 					() 	{ return this.fax2; }
	public String getCorreoElectronico	 	() 	{ return this.correoElectronico; }
	public String getObservaciones 			() 	{ return this.observaciones; }
	public String getIdPais  				() 	{ return this.idPais; }
	public String getNaturalDe 				() 	{ return this.naturalDe; }
	public String getIdProvincia  			() 	{ return this.idProvincia; }
	public String getIdPoblacion  			() 	{ return this.idPoblacion; }
	public String getPoblacionExtranjera  	() 	{ return this.poblacionExtranjera; }
	public String getFechaSolicitud 		() 	{ return this.fechaSolicitud; }
	public String getFechaNacimiento 		() 	{ return this.fechaNacimiento; }
	public String getFechaEstado	 		() 	{ return this.fechaEstado; }
	public String getFechaEstadoColegial	() 	{ return this.fechaEstadoColegial; }
	public String getAbonoCargo				() 	{ return this.abonoCargo;}
	public String getCbo_Codigo				() 	{ return this.cbo_Codigo;}
	public String getCodigoSucursal			() 	{ return this.codigoSucursal;}
	public String getDigitoControl			() 	{ return this.digitoControl;}
	public String getNumeroCuenta			() 	{ return this.numeroCuenta;}
	public String getAbonoSJCS				() 	{ return this.abonoSJCS;}
	public String getTitular				() 	{ return this.titular;}
	public String getSexo					()  { return sexo; }
	public Integer getIdModalidadDocumentacion(){return this.idModalidadDocumentacion;	}
	public boolean getResidente				()  { return residente.equalsIgnoreCase("1")?true:false; }
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Long getIdPersonaTemp() {
		return idPersonaTemp;
	}
	public void setIdPersonaTemp(Long idPersonaTemp) {
		this.idPersonaTemp = idPersonaTemp;
	}
	public Long getIdDireccionTemp() {
		return idDireccionTemp;
	}
	public void setIdDireccionTemp(Long idDireccionTemp) {
		this.idDireccionTemp = idDireccionTemp;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	
}