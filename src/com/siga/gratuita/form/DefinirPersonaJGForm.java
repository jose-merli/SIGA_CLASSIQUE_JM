/*
 * Fecha creación: 20/01/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
* Maneja el formulario que mantiene la tabla SCS_PERSONAJG
*/
 public class DefinirPersonaJGForm extends MasterForm{	
 	
	private String idPaisAux="idPaisAux";
	private String idProvinciaAux="idProvinciaAux";
	private String idPoblacionAux="idPoblacionAux";
	private String idEstadoCivilAux="idEstadoCivilAux";
	private String regimenConyugalAux="regimenConyugalAux";
	public void    setIdPaisAux				 (String	valor)	{ this.datos.put(this.idPaisAux, valor);}
	public void    setIdProvinciaAux		 (String	valor)	{ this.datos.put(this.idProvinciaAux, valor);}
	public void    setIdPoblacionAux		 (String	valor)	{ this.datos.put(this.idPoblacionAux, valor);}
	public void    setIdEstadoCivilAux		 (String	valor)	{ this.datos.put(this.idEstadoCivilAux, valor);}
	public void    setRegimenConyugalAux	 (String	valor)	{ this.datos.put(this.regimenConyugalAux, valor);}
	public String getIdPaisAux	 			()	{return (String)this.datos.get(this.idPaisAux);}
	public String getIdProvinciaAux		 	()	{return (String)this.datos.get(this.idProvinciaAux);}
	public String getIdPoblacionAux		 	()	{return (String)this.datos.get(this.idPoblacionAux);}
	public String getIdEstadoCivilAux	 	()	{return (String)this.datos.get(this.idEstadoCivilAux);}
	public String getRegimenConyugalAux		()	{return (String)this.datos.get(this.regimenConyugalAux);}
 	
	
	// Metodos Set (Formulario (*.jsp))
 	/**
	 * Almacena en la Hashtable si la persona existía en la base de datos 
	 * 
	 * @param Si existía o no 
	 * @return void 
	 */
	public void setExistia					(String existia)			{ this.datos.put("EXISTIA", existia);							}
 	
 	/**
	 * Almacena en la Hashtable el identificador abogado de un SOJ 
	 * 
	 * @param Identificador solicitante SOJ 
	 * @return void 
	 */
	public void setIdPersona			(String idPersona)				{ this.datos.put(ScsPersonaJGBean.C_IDPERSONA, idPersona);							}

	/**
	 * Almacena en la Hashtable la institucion de un SOJ 
	 * 
	 * @param Institucion SOJ 
	 * @return void 
	 */
	public void setIdInstitucion	 	(String idInstitucion)			{ this.datos.put(ScsPersonaJGBean.C_IDINSTITUCION, idInstitucion);					}
	
	/**
	 * Almacena en la Hashtable el NIF 
	 * 
	 * @param NIF persona
	 * @return void 
	 */
	public void setNif				 	(String nif)					{ this.datos.put(ScsPersonaJGBean.C_NIF, nif);										}
	
	/**
	 * Almacena en la Hashtable el nombre de un solicitante 
	 * 
	 * @param Nombre solicitante
	 * @return void 
	 */
	public void setNombre				(String nombre)				{ this.datos.put(ScsPersonaJGBean.C_NOMBRE, nombre);									}
	
	/**
	 * Almacena en la Hashtable el primer apellido de un solicitante 
	 * 
	 * @param Primer apellido solicitante
	 * @return void 
	 */
	public void setApellido1			(String apellido1)				{ this.datos.put(ScsPersonaJGBean.C_APELLIDO1, apellido1);							}
	
	/**
	 * Almacena en la Hashtable el segundo apellido de un solicitante 
	 * 
	 * @param Segundo apellido solicitante
	 * @return void 
	 */
	public void setApellido2			(String apellido2)				{ this.datos.put(ScsPersonaJGBean.C_APELLIDO2, apellido2);							}
	
	/**
	 * Almacena en la Hashtable el direccón de un solicitante 
	 * 
	 * @param Direccion solicitante
	 * @return void 
	 */
	public void setDireccion			(String direccion)				{ this.datos.put(ScsPersonaJGBean.C_DIRECCION, direccion);							}
	
	/**
	 * Almacena en la Hashtable el código postal de un solicitante 
	 * 
	 * @param Código postal
	 * @return void 
	 */
	public void setCodigoPostal			(String codigoPostal)			{ this.datos.put(ScsPersonaJGBean.C_CODIGOPOSTAL, codigoPostal);					}
	
	/**
	 * Almacena en la Hashtable el primer apellido de un solicitante 
	 * 
	 * @param Primer apellido solicitante
	 * @return void 
	 */
	public void setFechaNacimiento		(String fechaNacimiento)		{ this.datos.put(ScsPersonaJGBean.C_FECHANACIMIENTO, fechaNacimiento);				}
	
	/**
	 * Almacena en la Hashtable el identificador de la profesión de un solicitante 
	 * 
	 * @param Identificador profesión
	 * @return void 
	 */
	public void setIdProfesion			(String idProfesion)			{ this.datos.put(ScsPersonaJGBean.C_IDPROFESION, idProfesion);						}
	
	/**
	 * Almacena en la Hashtable el régimen conyugal de un solicitante 
	 * 
	 * @param Régimen conyugal
	 * @return void 
	 */
	public void setRegimenConyugal		(String regimenConyugal)		{ this.datos.put(ScsPersonaJGBean.C_REGIMENCONYUGAL, regimenConyugal);				}
	
	/**
	 * Almacena en la Hashtable el identificador del país 
	 * 
	 * @param Identificador país
	 * @return void 
	 */
	public void setIdPais				(String idPais)					{ this.datos.put(ScsPersonaJGBean.C_IDPAIS, idPais);								}
	
	/**
	 * Almacena en la Hashtable el identificador de la provincia 
	 * 
	 * @param Identificador provincia
	 * @return void 
	 */
	public void setIdProvincia			(String idProvincia)			{ this.datos.put(ScsPersonaJGBean.C_IDPROVINCIA, idProvincia);						}
	
	/**
	 * Almacena en la Hashtable el identificador de la poblacion 
	 * 
	 * @param Identificador poblacion
	 * @return void 
	 */
	public void setIdPoblacion			(String idPoblacion)			{ this.datos.put(ScsPersonaJGBean.C_IDPOBLACION, idPoblacion);						}
	
	/**
	 * Almacena en la Hashtable el identificador de la poblacion 
	 * 
	 * @param Identificador poblacion
	 * @return void 
	 */
	public void setIdEstadoCivil		(String idEstadoCivil)			{ this.datos.put(ScsPersonaJGBean.C_ESTADOCIVIL, idEstadoCivil);					}
	
	/**
	 * Almacena en la Hashtable el identificador del teléfono 
	 * 
	 * @param Identificador teléfono
	 * @return void 
	 */
	public void setIdTelefono		 	(String idTelefono)				{ this.datos.put(ScsTelefonosPersonaBean.C_IDTELEFONO, idTelefono);					}
	
	/**
	 * Almacena en la Hashtable el nombre del teléfono 
	 * 
	 * @param Nombre teléfono
	 * @return void 
	 */
	public void setNombreTelefono		(String nombreTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NOMBRETELEFONO, nombreTelefono);			}
	
	/**
	 * Almacena en la Hashtable el número de teléfono 
	 * 
	 * @param Número teléfono
	 * @return void 
	 */
	public void setNumeroTelefono		(String numeroTelefono)			{ this.datos.put(ScsTelefonosPersonaBean.C_NUMEROTELEFONO, numeroTelefono);			}
	
	// Metodos Get 1 por campo Formulario (*.jsp)
 	/**
	 * Recupera de la Hashtable el identificador abogado de un SOJ 
	 * @return int 
	 */
	public String getIdPersona			()								{ return this.datos.get(ScsPersonaJGBean.C_IDPERSONA).toString();							}

	/**
	 * Recupera de la Hashtable la institucion de un SOJ 
	 * @return int 
	 */
	public String getIdInstitucion	 	()								{ return this.datos.get(ScsPersonaJGBean.C_IDINSTITUCION).toString();						}
	
	/**
	 * Recupera de la Hashtable el NIF
	 * @return String
	 */
	public String getNif				 ()								{ return this.datos.get(ScsPersonaJGBean.C_NIF).toString();									}
	
	/**
	 * Recupera de la Hashtable el nombre de un solicitante
	 * @return String
	 */
	public String getNombre				()								{ return (String)this.datos.get(ScsPersonaJGBean.C_NOMBRE);									}
	
	/**
	 * Recupera de la Hashtable el primer apellido de un solicitante 
	 * @return String 
	 */
	public String getApellido1			()								{ return (String)this.datos.get(ScsPersonaJGBean.C_APELLIDO1);								}
	
	/**
	 * Recupera de la Hashtable el segundo apellido de un solicitante

	 * @return String 
	 */
	public String getApellido2			()								{ return (String)this.datos.get(ScsPersonaJGBean.C_APELLIDO2);								}
	
	/**
	 * Recupera de la Hashtable el direccón de un solicitante
	 * @return String 
	 */
	public String getDireccion			()								{ return (String)this.datos.get(ScsPersonaJGBean.C_DIRECCION);								}
	
	/**
	 * Recupera de la Hashtable el código postal de un solicitante
	 * @return String 
	 */
	public String getCodigoPostal		()								{ return (String)this.datos.get(ScsPersonaJGBean.C_CODIGOPOSTAL);							}
	
	/**
	 * Recupera de la Hashtable el primer apellido de un solicitante
	 * @return String 
	 */
	public String getFechaNacimiento	()								{ return this.datos.get(ScsPersonaJGBean.C_FECHANACIMIENTO).toString();						}
	
	/**
	 * Recupera de la Hashtable el identificador de la profesión de un solicitante
	 * @return int 
	 */
	public String getIdProfesion		()								{ return this.datos.get(ScsPersonaJGBean.C_IDPROFESION).toString();							}
	
	/**
	 * Recupera de la Hashtable el régimen conyugal de un solicitante
	 * @return String 
	 */
	public String getRegimenConyugal	()								{ return (String)this.datos.get(ScsPersonaJGBean.C_REGIMENCONYUGAL);						}
	
	/**
	 * Recupera de la Hashtable el identificador del país
	 * @return int 
	 */
	public String getIdPais				()								{ return this.datos.get(ScsPersonaJGBean.C_IDPAIS).toString();								}
	
	/**
	 * Recupera de la Hashtable el identificador del estado civil
	 * @return String 
	 */
	public String getIdEstadoCivil		()								{ return this.datos.get(ScsPersonaJGBean.C_ESTADOCIVIL).toString();							}	
	
	/**
	 * Recupera de la Hashtable el identificador de la provincia
	 * @return int 
	 */
	public String getIdProvincia		()								{ return this.datos.get(ScsPersonaJGBean.C_IDPROVINCIA).toString();							}
	
	/**
	 * Recupera de la Hashtable el identificador de la poblacion
	 * @return int 
	 */
	public String getIdPoblacion		()								{ return this.datos.get(ScsPersonaJGBean.C_IDPOBLACION).toString();							}
	
	/**
	 * Recupera de la Hashtable el identificador del teléfono
	 * @return Integer 
	 */
	public String getIdTelefono			()								{ return this.datos.get(ScsTelefonosPersonaBean.C_IDTELEFONO).toString();		}
	
	/**
	 * Recupera de la Hashtable el nombre del teléfono
	 * @return String 
	 */
	public String getNombreTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NOMBRETELEFONO).toString();	}
	
	/**
	 * Recupera de la Hashtable el número de teléfono
	 * @return String 
	 */
	public String getNumeroTelefono		()								{ return this.datos.get(ScsTelefonosPersonaBean.C_NUMEROTELEFONO).toString();	}
	/**
	 * Almacena en la Hashtable si la persona existía en la base de datos 
	 * @return void 
	 */
	public String getExistia					()						{ return this.datos.get("EXISTIA").toString();								}	
}
  