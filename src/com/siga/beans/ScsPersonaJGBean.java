/*
 * Fecha creaci�n: 20/01/2005
 * Autor: julio.vicente
 *
 */

/**
 * Implementa las operaciones sobre el Bean de la tabla SCS_PERSONAJG
 */

package com.siga.beans;

import java.util.Vector;

public class ScsPersonaJGBean extends MasterBean{
	
	/* Variables */ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8386731589938829302L;
	CenPoblacionesBean poblacion;
	CenProvinciaBean provincia;
	Vector<ScsTelefonosPersonaJGBean> telefonos;
	
	private Integer	idPersona;
	private String	nif;
	private String	tipo;
	private String	tipoIdentificacion;
	private String	observaciones;
	private String	nombre;
	private String	apellido1;
	private String	apellido2;
	private String	direccion;
	private String	existeDomicilio;
	private String	codigoPostal;
	private String  fechaNacimiento;
	private Integer	idProfesion;
	private Integer	idMinusvalia;
	private String	regimenConyugal;
	private Integer	idInstitucion;
	private String	idPais;
	private String	idProvincia;
	private String  idPoblacion;
	private Integer	idEstadoCivil;
	private Integer	idProcurador;
	private Integer	idRepresentanteJG;
	private String	enCalidadDe;
	private String	representante;
	private String sexo;
	private String idioma;
	private String hijos;
	private String edad;	
	private String fax;
	private String correoElectronico;
	private String idTipoDir;
	private String numeroDir;
	private String escaleraDir;
	private String pisoDir;
	private String puertaDir;
	private String idTipoVia;
	private String asistidoSolicitaJG;
	private String asistidoAutorizaEEJG;
	private String autorizaAvisoTelematico;
	private String notificacionTelematica;
	
	

	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PERSONAJG";
	
	
	/*Nombre de campos de la tabla*/
	



	static public final	String	C_IDPERSONA			=				"IDPERSONA";
	static public final String 	C_NIF				=				"NIF";
	static public final String 	C_NOMBRE			=				"NOMBRE";
	static public final String 	C_APELLIDO1			=				"APELLIDO1";
	static public final String 	C_APELLIDO2			=				"APELLIDO2";
	static public final String 	C_EXISTEDOMICILIO	=				"EXISTEDOMICILIO";
	static public final String 	C_DIRECCION			=				"DIRECCION";
	static public final String 	C_CODIGOPOSTAL		=				"CODIGOPOSTAL";
	static public final String 	C_FECHANACIMIENTO	=				"FECHANACIMIENTO";
	static public final String 	C_IDPROFESION		=				"IDPROFESION";
	static public final String 	C_IDMINUSVALIA		=				"IDMINUSVALIA";	
	static public final String 	C_REGIMENCONYUGAL	=				"REGIMEN_CONYUGAL";
	static public final String 	C_IDPAIS			=				"IDPAIS";
	static public final String 	C_IDPROVINCIA		=				"IDPROVINCIA";
	static public final String 	C_IDPOBLACION		=				"IDPOBLACION";
	static public final String 	C_ESTADOCIVIL		=				"IDESTADOCIVIL";	
	static public final String  C_IDINSTITUCION 	= 				"IDINSTITUCION";
	static public final String  C_TIPOPERSONAJG 	= 				"TIPOPERSONAJG";
	static public final String  C_IDTIPOIDENTIFICACION 	= 			"IDTIPOIDENTIFICACION";
	static public final String  C_OBSERVACIONES 		=			"OBSERVACIONES";
	static public final String  C_IDREPRESENTANTEJG 	= 			"IDREPRESENTANTEJG";
	static public final String  C_ENCALIDADDE 			=			"IDTIPOENCALIDAD";
	static public final String  C_IDIOMA			  =			    "IDLENGUAJE";
	static public final String  C_SEXO			  =			        "SEXO";
	static public final String  C_TIPOCONOCE      =			        "TIPOCONOCE";
	static public final String  C_TIPOGRUPOLAB    =			        "IDTIPOGRUPOLAB";
	static public final String  C_NUMVECES		  =			        "NUMVECES";
	static public final String  C_HIJOS		      =			        "NUMEROHIJOS";
	static public final String  C_EDAD		      =			        "EDAD";	
	static public final String  C_FAX		      =			        "FAX";
	static public final String  C_CORREOELECTRONICO     =	        "CORREOELECTRONICO";
	static public final String  C_IDTIPODIR		        =			"IDTIPODIR";
	static public final String  C_NUMERODIR			    =			"NUMERODIR";
	static public final String  C_ESCALERADIR		    =			"ESCALERADIR";
	static public final String  C_PISODIR		      	=			"PISODIR";
	static public final String  C_PUERTADIR			    =			"PUERTADIR";
	static public final String  C_IDTIPOVIA			    =			"IDTIPOVIA";
	static public final String  C_ASISTIDOSOLICITAJG	=			"ASISTIDOSOLICITAJG";
	static public final String  C_ASISTIDOAUTORIZAEEJG	=			"ASISTIDOAUTORIZAEEJG";
	static public final String  C_AUTORIZAAVISOTELEMATICO	=			"AUTORIZAAVISOTELEMATICO";
	static public final String  C_NOTIFICACIONTELEMATICA	=			"NOTIFICACIONTELEMATICA";
	static public final String  C_IDPAISDIRECCION	=			"IDPAISDIR1";
	static public final String  C_DIRECCIONEXTRANJERA	=			"DIRECCIONEXTRANJERA";
	String idPaisDireccion;
	String direccionExtranjera;
	
	
	/*Metodos SET*/
	/**
	 * @param Correoelectronico The Correoelectronico to set.
	 */
	
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	/**
	 * @param Fax The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	/**
	 * @param idioma The idioma to set.
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	
	/**
	 * @param idioma The idioma to set.
	 */
	public void setHijos(String hijos) {
		this.hijos = hijos;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	/**
	 * Almacena en el Bean la institucion
	 * 
	 * @param valor Institucion
	 * @return void 
	 */
	public void setIdInstitucion 				(Integer valor) { this.idInstitucion = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la persona
	 * 
	 * @param valor Identificador persona
	 * @return void 
	 */
	public void setIdPersona 					(Integer valor) { this.idPersona = valor;}
	
	/**
	 * Almacena en el Bean el tipo de persona
	 * 
	 * @param valor Identificador persona
	 * @return void 
	 */
	public void setTipo 					(String valor) { this.tipo = valor;}
	
	/**
	 * Almacena en el Bean el tipo de identificacion
	 * 
	 * @param valor Identificador persona
	 * @return void 
	 */
	public void setTipoIdentificacion 					(String valor) { this.tipoIdentificacion = valor;}
	
	/**
	 * Almacena en el Bean las obeservaciones
	 * 
	 * @param valor Identificador persona
	 * @return void 
	 */
	public void setObservaciones 					(String valor) { this.observaciones = valor;}
	
	/**
	 * Almacena en el Bean el NIF
	 * 
	 * @param valor NIF
	 * @return void 
	 */
	public void setNif 							(String valor) { this.nif = valor;}
	
	/**
	 * Almacena en el Bean el nombre de la persona
	 * 
	 * @param valor Nombre persona
	 * @return void 
	 */
	public void setNombre						(String valor) { this.nombre = valor;}
	
	/**
	 * Almacena en el Bean el primer apellido de la persona
	 * 
	 * @param valor Primer apellido persona
	 * @return void 
	 */
	public void setApellido1					(String valor) { this.apellido1 = valor;}
	
	/**
	 * Almacena en el Bean el segundo apellido de la persona
	 * 
	 * @param valor Primer apellido persona
	 * @return void 
	 */
	public void setApellido2					(String valor) { this.apellido2 = valor;}
	
	/**
	 * Almacena en el Bean la direcci�n de la persona
	 * 
	 * @param valor Direcci�n persona
	 * @return void 
	 */
	public void setDireccion					(String valor) { this.direccion = valor;}

	/**
	 * Almacena en el Bean la direcci�n de la persona
	 * 
	 * @param valor Direcci�n persona
	 * @return void 
	 */
	public void setExisteDomicilio  			(String valor) { this.existeDomicilio = valor;}
	
	
	/**
	 * Almacena en el Bean c�digo postal
	 * 
	 * @param valor C�digo postal
	 * @return void 
	 */
	public void setCodigoPostal					(String valor) { this.codigoPostal = valor;}
	
	/**
	 * Almacena en el Bean la fecha de nacimiento
	 * 
	 * @param valor Fecha nacimiento
	 * @return void 
	 */
	public void setFechaNacimiento				(String valor) { this.fechaNacimiento = valor;}	
	
	/**
	 * Almacena en el Bean el identificador de la profesi�n
	 * 
	 * @param valor Identificador profesion
	 * @return void 
	 */
	public void setIdProfesion					(Integer valor) { this.idProfesion = valor;}
	
	
	public void setIdMinusvalia					(Integer valor) { this.idMinusvalia = valor;}
	/**

	
	/**
	 * Almacena en el Bean el r�gimen conyugal
	 * 
	 * @param valor R�gimen conyugal
	 * @return void 
	 */
	public void setRegimenConyugal				(String valor) { this.regimenConyugal = valor;}
	
	/**
	 * Almacena en el Bean el identificador del pa�s.
	 * 
	 * @param valor Identificador pa�s
	 * @return void 
	 */
	public void setIdPais						(String valor) { this.idPais = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la provincia.
	 * 
	 * @param valor Identificador provincia.
	 * @return void 
	 */
	public void setIdProvincia						(String valor) { this.idProvincia = valor;}
	
	/**
	 * Almacena en el Bean el identificador de la poblaci�n.
	 * 
	 * @param valor Identificador poblaci�n.
	 * @return void 
	 */
	public void setIdPoblacion						(String valor) { this.idPoblacion = valor;}
	
	/**
	 * Almacena en el Bean el identificador del estado civil
	 * 
	 * @param valor Identificador estado civil.
	 * @return void 
	 */
	public void setIdEstadoCivil					(Integer valor) { this.idEstadoCivil = valor;}
	
	/**
	 * Almacena en el Bean el identificador del procurador
	 * 
	 * @param valor Identificador procurador.
	 * @return void 
	 */
	public void setIdProcurador					(Integer valor) { this.idProcurador = valor;}
	
	/**
	 * Almacena en el Bean el identificador Representante JG
	 * 
	 * @param valor Identificador representante JG.
	 * @return void 
	 */
	public void setIdRepresentanteJG					(Integer valor) { this.idRepresentanteJG = valor;}
	
	/**
	 * Almacena en el Bean el identificador encalidadde
	 * 
	 * @param valor Identificador representante Legal.
	 * @return void 
	 */
	public void setEnCalidadDe					(String valor) { this.enCalidadDe = valor;}
	
	/**
	 * Almacena en el Bean el identificador Representante NOMBRE
	 * 
	 * @param valor Identificador representante NOMBRE.
	 * @return void 
	 */
	public void setRepresentante					(String valor) { this.representante = valor;}
	
	
	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	/*Metodos GET*/
	
	/**
	 * @return Returns the getCorreoelectronico
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	/**
	 * @return Returns the fax
	 */
		public String getFax() {
		return fax;
	}
	
	/**
	 * @return Returns the idioma.
	 */
	public String getHijos() {
		return hijos;
	}
	
	public String getEdad() {
		return edad;
	}

	/**
	 * @return Returns the idioma.
	 */
	public String getIdioma() {
		return idioma;
	}
		
	/**
	 * Recupera del Bean la institucion a la que pertenece un SOJ 
	 * @return Integer
	 */
	public Integer getIdInstitucion 		() { return this.idInstitucion;					}
	
	/**
	 * Recupera del Bean el identificador de una persona 
	 * @return Integer
	 */
	public Integer getIdPersona				() { return this.idPersona;						}
	
	/**
	 * Recupera del Bean el tipo de persona 
	 * @return String
	 */
	public String getTipo				() { return this.tipo;						}
	
	/**
	 * Recupera del Bean el tipo de identificacion 
	 * @return String
	 */
	public String getTipoIdentificacion				() { return this.tipoIdentificacion;						}
	
	/**
	 * Recupera del Bean el campo observaciones
	 * @return String
	 */
	public String getObservaciones				() { return this.observaciones;						}
	
	/**
	 * Almacena en el Bean el NIF
	 * @return String
	 */
	public String getNif					() { return this.nif;							}
	
	/**
	 * Recupera del Bean el nombre de la persona
	 * @return String
	 */
	public String getNombre					() { return this.nombre;						}
	
	/**
	 * Recupera del Bean el primer apellido de la persona
	 * @return String
	 */
	public String getApellido1				() { return this.apellido1;						}
	
	/**
	 * Recupera del Bean el segundo apellido de la persona
	 * @return String 
	 */
	public String getApellido2				() { return this.apellido2;}
	
	/**
	 * Recupera del Bean la direcci�n de la persona
	 * @return String 
	 */
	public String getDireccion				() { return this.direccion;						}
	
	/**
	 * Recupera del Bean c�digo postal
	 * @return String 
	 */
	public String getExisteDomicilio		() { return this.existeDomicilio;	        	}
	
	
	
	public String getCodigoPostal			() { return this.codigoPostal;					}
	
	/**
	 * Recupera del Bean la fecha de nacimiento
	 * @return String 
	 */
	public String getFechaNacimiento		() { return this.fechaNacimiento;				}	
	
	/**
	 * Recupera del Bean el identificador de la profesi�n
	 * @return Integer 
	 */
	public Integer getIdProfesion			() { return this.idProfesion;					}
	
	
	public Integer getIdMinusvalia			() { return this.idMinusvalia;					}
	
	/**
	 * Recupera del Bean el r�gimen conyugal
	 * @return String 
	 */
	public String getRegimenConyugal		() { return this.regimenConyugal;				}
	
	/**
	 * Recupera del Bean el identificador del pa�s.
	 * @return Integer 
	 */
	public String getIdPais				() { return this.idPais;}
	
	/**
	 * Recupera del Bean el identificador de la provincia.
	 * @return Integer 
	 */
	public String getIdProvincia			() { return this.idProvincia;}
	
	/**
	 * Recupera del Bean el identificador de la poblaci�n.
	 * @return Integer
	 */
	public String getIdPoblacion				() { return this.idPoblacion;}
	
	/**
	 * Recupera del Bean el identificador del estado civil
	 * @return Integer
	 */
	public Integer getIdEstadoCivil				() { return this.idEstadoCivil;}
	
	/**
	 * Recupera del Bean el identificador del procurador
	 * @return Integer
	 */
	public Integer getIdProcurador				() { return this.idProcurador;}
	
	/**
	 * Recupera del Bean el identificador del representante JG
	 * @return Integer
	 */
	public Integer getIdRepresentanteJG				() { return this.idRepresentanteJG;}
	
	/**
	 * Recupera del Bean el identificador del representante NOMBRE
	 * @return Integer
	 */
	public String getRepresentante			() { return this.representante;}
	
	/**
	 * Recupera del Bean el identificador de encalidade
	 * @return String
	 */
	public String getEnCalidadDe				() { return this.enCalidadDe;}
	/**
	 * @return Returns the sexo.
	 */
	public String getSexo() {
		return sexo;
	}


	public CenPoblacionesBean getPoblacion() {
		return poblacion;
	}


	public void setPoblacion(CenPoblacionesBean poblacion) {
		this.poblacion = poblacion;
	}


	public CenProvinciaBean getProvincia() {
		return provincia;
	}


	public void setProvincia(CenProvinciaBean provincia) {
		this.provincia = provincia;
	}


	public Vector<ScsTelefonosPersonaJGBean> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Vector<ScsTelefonosPersonaJGBean> telefonos) {
		this.telefonos = telefonos;
	}
	public String getIdTipoDir() {
		return idTipoDir;
	}
	public void setIdTipoDir(String idTipoDir) {
		this.idTipoDir = idTipoDir;
	}
	public String getNumeroDir() {
		return numeroDir;
	}
	public void setNumeroDir(String numeroDir) {
		this.numeroDir = numeroDir;
	}
	public String getEscaleraDir() {
		return escaleraDir;
	}
	public void setEscaleraDir(String escaleraDir) {
		this.escaleraDir = escaleraDir;
	}
	public String getPisoDir() {
		return pisoDir;
	}
	public void setPisoDir(String pisoDir) {
		this.pisoDir = pisoDir;
	}
	public String getPuertaDir() {
		return puertaDir;
	}
	public void setPuertaDir(String puertaDir) {
		this.puertaDir = puertaDir;
	}
	public String getIdTipoVia() {
		return idTipoVia;
	}
	public void setIdTipoVia(String idTipoVia) {
		this.idTipoVia = idTipoVia;
	}
	public String getAsistidoSolicitaJG() {
		return asistidoSolicitaJG;
	}
	public void setAsistidoSolicitaJG(String asistidoSolicitaJG) {
		this.asistidoSolicitaJG = asistidoSolicitaJG;
	}
	public String getAsistidoAutorizaEEJG() {
		return asistidoAutorizaEEJG;
	}
	public void setAsistidoAutorizaEEJG(String asistidoAutorizaEEJG) {
		this.asistidoAutorizaEEJG = asistidoAutorizaEEJG;
	}
	public String getAutorizaAvisoTelematico() {
		return autorizaAvisoTelematico;
	}
	public void setAutorizaAvisoTelematico(String autorizaAvisoTelematico) {
		this.autorizaAvisoTelematico = autorizaAvisoTelematico;
	}
	public String getNotificacionTelematica() {
		return notificacionTelematica;
	}
	public void setNotificacionTelematica(String notificacionTelematica) {
		this.notificacionTelematica = notificacionTelematica;
	}
	public String getIdPaisDireccion() {
		return idPaisDireccion;
	}
	public void setIdPaisDireccion(String idPaisDireccion) {
		this.idPaisDireccion = idPaisDireccion;
	}
	public String getDireccionExtranjera() {
		return direccionExtranjera;
	}
	public void setDireccionExtranjera(String direccionExtranjera) {
		this.direccionExtranjera = direccionExtranjera;
	}
	

}
