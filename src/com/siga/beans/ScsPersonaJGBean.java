/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 *
 */

/**
 * Implementa las operaciones sobre el Bean de la tabla SCS_PERSONAJG
 */

package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

public class ScsPersonaJGBean extends MasterBean{
	
	/* Variables */ 
	
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
	
	
	
	
	
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PERSONAJG";
	
	
	/*Nombre de campos de la tabla*/
	



	static public final	String	C_IDPERSONA			=				"IDPERSONA";
	static public final String 	C_NIF				=				"NIF";
	static public final String 	C_NOMBRE			=				"NOMBRE";
	static public final String 	C_APELLIDO1			=				"APELLIDO1";
	static public final String 	C_APELLIDO2			=				"APELLIDO2";
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
	 * Almacena en el Bean la dirección de la persona
	 * 
	 * @param valor Dirección persona
	 * @return void 
	 */
	public void setDireccion					(String valor) { this.direccion = valor;}
	
	/**
	 * Almacena en el Bean código postal
	 * 
	 * @param valor Código postal
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
	 * Almacena en el Bean el identificador de la profesión
	 * 
	 * @param valor Identificador profesion
	 * @return void 
	 */
	public void setIdProfesion					(Integer valor) { this.idProfesion = valor;}
	
	
	public void setIdMinusvalia					(Integer valor) { this.idMinusvalia = valor;}
	/**

	
	/**
	 * Almacena en el Bean el régimen conyugal
	 * 
	 * @param valor Régimen conyugal
	 * @return void 
	 */
	public void setRegimenConyugal				(String valor) { this.regimenConyugal = valor;}
	
	/**
	 * Almacena en el Bean el identificador del país.
	 * 
	 * @param valor Identificador país
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
	 * Almacena en el Bean el identificador de la población.
	 * 
	 * @param valor Identificador población.
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
	 * Recupera del Bean la dirección de la persona
	 * @return String 
	 */
	public String getDireccion				() { return this.direccion;						}
	
	/**
	 * Recupera del Bean código postal
	 * @return String 
	 */
	public String getCodigoPostal			() { return this.codigoPostal;					}
	
	/**
	 * Recupera del Bean la fecha de nacimiento
	 * @return String 
	 */
	public String getFechaNacimiento		() { return this.fechaNacimiento;				}	
	
	/**
	 * Recupera del Bean el identificador de la profesión
	 * @return Integer 
	 */
	public Integer getIdProfesion			() { return this.idProfesion;					}
	
	
	public Integer getIdMinusvalia			() { return this.idMinusvalia;					}
	
	/**
	 * Recupera del Bean el régimen conyugal
	 * @return String 
	 */
	public String getRegimenConyugal		() { return this.regimenConyugal;				}
	
	/**
	 * Recupera del Bean el identificador del país.
	 * @return Integer 
	 */
	public String getIdPais				() { return this.idPais;}
	
	/**
	 * Recupera del Bean el identificador de la provincia.
	 * @return Integer 
	 */
	public String getIdProvincia			() { return this.idProvincia;}
	
	/**
	 * Recupera del Bean el identificador de la población.
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
	
	}
